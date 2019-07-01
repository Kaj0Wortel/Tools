/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) May 2019 by Kaj Wortel - all rights reserved                *
 * Contact: kaj.wortel@gmail.com                                             *
 *                                                                           *
 * This file is part of the tools project, which can be found on github:     *
 * https://github.com/Kaj0Wortel/tools                                       *
 *                                                                           *
 * It is allowed to use, (partially) copy and modify this file               *
 * in any way for private use only by using this header.                     *
 * It is not allowed to redistribute any (modified) versions of this file    *
 * without my permission.                                                    *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
// Note to self: Not completely tested.
// Works, but not the pause/resume function (fucks up the initial delay somehow).


package tools.concurrent;


// Tool imports
import tools.MultiTool;
import tools.log.Logger;


// Java imports
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * TODO: comments
 * 
 * Class which replaces the {@link java.util.Timer} class. The added functionalities are:
 * <ul>
 *   <li> Changing the update interval. Keeps the same task running. Can be set
 *        via interval or updates per second (fps).</li>
 *   <li> Pausing a timer. A paused timer will, after being resumed, continue
 *        as if it wasn't paused. So if the next update is over 500ms before
 *        being paused, then after being resumed the next update will occur over
 *        500ms. If the update interval was decreased with {@code dt} while the
 *        timer was paused, then the next update will occur over
 *        {@code max(0, 500-dt)}.</li>
 *   <li> Canceling a timer. After a timer has been canceled, it can be started
 *        again with the same settings.</li>
 *   <li> Manual and automatic frame rate handeling. In manual mode, a target
 *        interval is set and the updates occur after every interval time.
 *        If the previous update was still running, then the current update is
 *        ignored.
 *        In automatic mode, a target interval is set and AIMD is used to prevent
 *        clashing update cycles.
 *   <li> Starting a timer which is already started does nothing.</li>
 *   <li> Resuming a timer which is already started or canceled does nothing.</li>
 *   <li> Pausing a timer which is already paused or canceled does nothing.</li>
 *   <li> Canceling a timer which is already canceled does nothing.</li>
 * </ul>
 * 
 * This class is suitable for concurrent access.
 * 
 * @author Kaj Wortel
 */
public class ThreadTimer {
    /** The current state of the timer. */
    public enum TimerState {
        RUNNING, PAUSED, CANCELED
    }
    
    // Lock and condition for concurrent operations.
    final private Lock interruptLock = new ReentrantLock();
    final private Lock lock = new ReentrantLock(true);
    final private Condition threadIDCondition = lock.newCondition();
    
    private static FPSState fpsState = FPSState.MANUAL;
    
    // Counters to keep track of the timer id's.
    private static AtomicInteger timerIDCounter = new AtomicInteger(0);
    final private int timerID;
    
    private Thread updateThread;
    private volatile int threadID = 0;
    private volatile int prevThreadID = 0;
    
    // The tasks to be executed.
    final private Runnable[] tasks;
    // The delay set by the user when starting the timer.
    private long initialDelay;
    // The initial delay.
    private volatile long delay;
    // The timer interval.
    private volatile long interval;
    // The target interval.
    private volatile long targetInterval;
    
    // The start timestamp of the timer for the current iteration.
    private volatile long startTime;
    
    // The pause timestamp of the timer. If there was no pause in
    // this iteration then it is equal to the start timestamp.
    private volatile long pauseTime;
    
    private TimerState timerState = TimerState.CANCELED;
    
    // Keeps track of how many cycles must pass before the
    // additative increase is replaced multiplicative increase.
    public int waitMul = 0;
    
    // Denotes the thread priority for this timer.
    private int priority = Thread.NORM_PRIORITY;
    
    // The scheduler used for executing the tasks.
    final private Scheduler sched;
    
    // Metric variables.
    final private static int METRIC_SIZE = 20;
    private boolean restartMetric = true;
    private volatile long metricTimeStamp = System.currentTimeMillis();
    private volatile float metricAvgSpeed = 0.0f;
    private volatile int metricCompleted = 0;
    private volatile int metricFPS = 0;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Constructor.
     * 
     * @see #ThreadTimer(long, long, Scheduler, Runnable...)
     */
    public ThreadTimer(long interval, Runnable... rs) {
        this(interval, interval, null, rs);
    }
    
    /**
     * Constructor.
     * 
     * @see #ThreadTimer(long, long, Scheduler, Runnable...)
     */
    public ThreadTimer(long delay, long interval, Runnable... rs) {
        this(delay, interval, null, rs);
    }
    
    /**
     * Constructor.
     * 
     * @see #ThreadTimer(long, long, Scheduler, Runnable...)
     */
    public ThreadTimer(long interval, Scheduler sched, Runnable... rs) {
        this(interval, interval, sched, rs);
    }
    
    /**
     * Full constructor.
     * 
     * @param delay the time in ms before the first exectution of the tasks.
     * @param interval the time in ms which is between two executions of
     *     the tasks.
     * @param sched the scheduler used for scheduling tasks. If {@code null},
     *     then {@link SelfScheduler} is used as default.
     * @param rs the tasks that will be executed on every interval.
     * 
     * @throws IllegalArgumentException if {@code interval <= 0} or
     *     {@code delay <= 0}.
     * @throws NullPointerException if {@code rs == null}.
     */
    public ThreadTimer(long delay, long interval, Scheduler sched,
            Runnable... rs) {
        if (interval <= 0)
            throw new IllegalArgumentException(
                    "Expected interval < 0, but found: " + interval);
        if (delay <= 0)
            throw new IllegalArgumentException(
                    "Expected delay < 0, but found: " + delay);
        if (rs == null)
            throw new NullPointerException("Runnable was null!");
        
        timerID = timerIDCounter.getAndIncrement();
        
        // Set the initial values.
        this.initialDelay = this.delay = delay;
        this.interval = interval;
        if (sched == null) {
            this.sched = new SelfScheduler();
            this.sched.start();
            
        } else this.sched = sched;
        this.tasks = rs;
        
        // By default, set the target interval to the given interval.
        targetInterval = interval;
    }
    
    
    /* -------------------------------------------------------------------------
     * Metric functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Updates the metrics and adjusts the interval is needed.
     * @param timeStamp 
     * @param exeTime 
     */
    private void updateMetric(long timeStamp, long exeTime) {
        if (restartMetric) {
            metricCompleted = 0;
            metricFPS = (int) getFPS();
            metricAvgSpeed = exeTime;
            metricTimeStamp = System.currentTimeMillis() + getInterval() / 2;
            restartMetric = false;
            return;
        }
        
        metricCompleted++;
        metricAvgSpeed = (metricAvgSpeed * (METRIC_SIZE - 1) + exeTime)
                / METRIC_SIZE;
        if (timeStamp > metricTimeStamp + 1000) {
            metricFPS = (int) (metricCompleted *
                    1000 / (metricTimeStamp - timeStamp));
            metricCompleted = 0;
            metricTimeStamp += 1000;
        }
        
        if (fpsState == FPSState.AUTO) {
            int diff = (int) (interval - exeTime);
            if (diff < 0) {
                waitMul += 10;
                setInterval((long) Math.ceil((interval * 1.05)));
                
            } else {
                if (interval > targetInterval) {
                    if (--waitMul <= 0) {
                        waitMul = 0;
                        setInterval(Math.max((long) (interval * 0.95),
                                targetInterval));
                    } else {
                        setInterval(interval - 1);
                    }
                    
                } else if (interval < targetInterval) {
                    setInterval(targetInterval);
                }
            }
        }
    }
    
    /**
     * @return the average time it takes to complete one update cycle.
     */
    public float getAverageExecutionTime() {
        return metricAvgSpeed;
    }
    
    /**
     * @return the frames per second measured by the metrics.
     */
    public int getMetricFPS() {
        return metricFPS;
    }
    
    /**
     * Re-initializes the metric values.
     */
    public void restartMetric() {
        restartMetric = true;
    }
    
    /* -------------------------------------------------------------------------
     * Control flow functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Starts the timer.
     * Does nothing if the timer is already running or paused.
     */
    public void start() {
        lock.lock();
        try {
            if (timerState == TimerState.RUNNING ||
                    timerState == TimerState.PAUSED) return;
            
            // Update time stamps.
            metricTimeStamp = pauseTime = startTime
                    = System.currentTimeMillis();
            delay = initialDelay;
            
            // Update the timeState.
            timerState = TimerState.RUNNING;
            
            // Set and start update thread.
            updateThread = createUpdateThread(++threadID);
            updateThread.start();
            
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Pauses the timer.
     * Does nothing if the timer is already paused or stopped.
     */
    public void pause() {
        lock.lock();
        try {
            if (timerState == TimerState.PAUSED ||
                    timerState == TimerState.CANCELED) return;
            
            // Stop the timer.
            threadID++;
            
            // Set the pause time stamp.
            pauseTime = System.currentTimeMillis();
            System.err.println("PAUSED: " + (pauseTime - startTime));
            
            // Update the timeState.
            timerState = TimerState.PAUSED;
            
            // Notify thread about pause.
            wait.signalAll();
            
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Resumes a paused timer.
     * Does nothing if the timer is running or canceled.
     */
    public void resume() {
        lock.lock();
        try {
            if (timerState == TimerState.RUNNING ||
                    timerState == TimerState.CANCELED) return;
            
            // Obtain current time stamp.
            long curTime = System.currentTimeMillis();
            long timePaused = curTime - pauseTime;
            long timeWaited = pauseTime - startTime;
            System.err.println("RESUME: " + timePaused + ", " + timeWaited);
            
            // Update time stamps.
            delay = Math.max(0, interval - timeWaited);
            startTime += timePaused;
            metricTimeStamp += timePaused;
            
            // Update the timeState.
            timerState = TimerState.RUNNING;
            
            // Create and start update thread.
            updateThread = createUpdateThread(threadID);
            updateThread.start();
            
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Cancels a timer.
     * Does nothing if the timer is already canceled.
     */
    public void cancel() {
        lock.lock();
        try {
            if (timerState == TimerState.CANCELED) return;
            
            // Kill the current timer if needed.
            if (timerState == TimerState.RUNNING) {
                threadID++;
            }
            
            // Update the timeState.
            timerState = TimerState.CANCELED;
            
            // Notify thread about cancel.
            wait.signalAll();
            
        } finally {
            lock.unlock();
        }
    }
    
    
    /* -------------------------------------------------------------------------
     * Settings functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Sets a new interval for the timer.
     * 
     * @param interval the new interval to be set.
     */
    public void setInterval(long interval) {
        lock.lock();
        try {
            this.interval = Math.max(1L, interval);
            wait.signalAll();
            
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Sets the interval using frame rates.
     * Uses {@link #setInterval(long)} for the implementation.
     * 
     * @param fps the new fps.
     */
    public void setFPS(double fps) {
        setInterval((long) (1000 / fps));
    }
    
    /**
     * Sets the target interval.
     * When using the auto-fps mode, this value will be used
     * even when a shorter interval is possible.
     * Also sets the current interval to the target interval
     * to improve convergence.
     * <br>
     * Blocks until
     * 
     * @param interval the new target interval.
     */
    public void setTargetInterval(long interval) {
        lock.lock();
        try {
            targetInterval = interval;
            this.interval = interval;
            wait.signalAll();
            
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Sets the target frame rate.
     * 
     * @param fps the new target frame rate.
     */
    public void setTargetFPS(double fps) {
        setTargetInterval((long) (1000 / fps));
    }
    
    /**
     * @return the current interval between two updates.
     */
    public long getInterval() {
        return interval;
    }
    
    /**
     * @return the current number of frames per second.
     */
    public double getFPS() {
        return 1000.0 / getInterval();
    }
    
    /**
     * Sets the state for handeling the frame rate.
     * 
     * @param state the new state.
     */
    public void setFPSState(FPSState state) {
        fpsState = state;
    }
    
    /**
     * @return the current state for handeling the frame rate.
     */
    public FPSState getFPSState() {
        return fpsState;
    }
    
    /**
     * @param priority the new update thread priority.
     * 
     * Note: should be within the range of {@link Thread#MIN_PRIORITY} and
     * {@link Thread#MAX_PRIORITY}. Default is {@link Thread#NORM_PRIORITY}.
     * 
     * @see Thread#setPriority(int)
     */
    public void setPriority(int priority) {
        this.priority = priority;
        if (updateThread != null) updateThread.setPriority(priority);
    }
    
    /**
     * @return the current priority of the update thread.
     */
    public int getPriority() {
        return priority;
    }
    
    /**
     * @return the current state of the timer.
     */
    public TimerState getState() {
        return timerState;
    }
    
    
    /* -------------------------------------------------------------------------
     * Private functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Wakes the update thread if needed such that a change will be taken
     * into account.
     *//*
    private void notifyThread() {
        /*
        if (!interruptLock.tryLock()) return;
        try {
            if (updateThread != null) updateThread.interrupt();
            
        } finally {
            interruptLock.unlock();
        }*//*
        
        lock.lock();
        try {
            wait.signalAll();
            
        } finally {
            lock.unlock();
        }
    }*/
    
    Condition wait = lock.newCondition();
    
    /**
     * Function for the update thread.
     * 
     * @param tID the of of this thread.
     */
    @SuppressWarnings("UseSpecificCatch")
    private void threadFunction(int tID) {
        // Set initial thread priority.
        Thread.currentThread().setPriority(priority);
        
        lock.lock();
        try {
            // Prevent multiple threads from running in case of fast
            // start/stop actions.
            while (prevThreadID < tID - 1) {
                // Terminate current thread if it should be stopped.
                if (tID != threadID) return;
                
                try {
                    threadIDCondition.await();
                } catch (InterruptedException e) { }
            }
            
            // Terminate current thread if it should be stopped.
            if (tID != threadID) return;
            
            while (startTime + delay > System.currentTimeMillis() &&
                    tID == threadID) {
                System.err.println("delay sleep:" +
                        (startTime + delay - System.currentTimeMillis()));
                try {
                    long time = Math.max(0, startTime + delay
                            - System.currentTimeMillis());
                    wait.await(time, TimeUnit.MILLISECONDS);
                    
                } catch (InterruptedException e) {
                    Logger.write(new Object[] {
                        "Updater thread " + tID + " was interrupted!",
                        "Interrupt was ignored!",
                        e
                    }, Logger.Type.WARNING);
                }
            }
            
            // Terminate current thread if it should be stopped.
            if (tID != threadID) return;
            
            // Increase the start time.
            startTime += delay;
            
        } finally {
            lock.unlock();
        }
        
        restartMetric = true;
        do {
            // tmp
            System.out.println("Thread id: " + threadID);
            System.out.println("my thread: " + tID);
            
            // If the thread was interrupted, clear the interrupt status
            // and log an error.
            if (Thread.interrupted()) {
                Logger.write(new Object[] {
                    "Updater thread " + tID + " was interrupted!",
                    "Interrupt was ignored!"
                }, Logger.Type.WARNING);
            }
            
            // Run the tasks on the scheduler.
            for (Runnable task : tasks) {
                try {
                    sched.scheduleTask(task);
                    
                } catch (Exception e) {
                    Logger.write(e);
                }
            }
            
            // Wait for the scheduler to finish.
            try {
                sched.waitUntilDone();
                
            } catch(InterruptedException e) {
                Logger.write(e);
            }
            
            lock.lock();
            try {
                // Update metrics.
                long timeStamp = System.currentTimeMillis();
                long exeTime = timeStamp - startTime;
                updateMetric(timeStamp, exeTime);
                
                // Wait remaining time.
                long sleepTime = interval - exeTime;
                System.err.println("sleep time: " + sleepTime);
                while (sleepTime/* - 2*/ > 0 && tID == threadID) {
                    try {
                        wait.await(sleepTime/* - 2*/, TimeUnit.MILLISECONDS);
                        sleepTime -= (System.currentTimeMillis() - timeStamp);
                        
                    } catch (InterruptedException e) {
                        Logger.write(new Object[] {
                            "Updater thread " + tID + " was interrupted!",
                            "Interrupt was ignored!",
                            e
                        }, Logger.Type.WARNING);
                        /*
                        long timeWaited = System.currentTimeMillis() - timeStamp;
                        System.err.println("INTERRUPT: " + timeWaited);
                        if (tID != threadID) return;
                        timeStamp = System.currentTimeMillis();
                        sleepTime = interval - (timeStamp - startTime);
                        */
                    }
                }
                
                /*
                // Precision: +-1 ms
                while (System.currentTimeMillis() - startTime < interval &&
                        tID == threadID) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) { }
                }
                /*
                // Precision: +-0 ms
                while (System.currentTimeMillis() - startTime
                        < interval) { }
                /**/

                // Update start time stamp.
                if (tID == threadID) {
                    startTime += interval;
                }
                
            } finally {
                lock.unlock();
            }
            
        } while (tID == threadID);
    }
    
    /**
     * @param tID the ID of the update thread to create.
     * @return A fresh update thread with the given id.
     */
    private Thread createUpdateThread(int tID) {
        return new Thread("Timer-thread-" + timerID) {
            @Override
            public void run() {
                threadFunction(tID);
                lock.lock();
                try {
                    prevThreadID++;
                    threadIDCondition.signalAll();
                    
                } finally {
                    lock.unlock();
                }
            }
        };
    }
    
    
    
    
    
    
    /**------------
     * TMP
     */
    private static void printMetric() {
        System.out.println("exe: " + tt.getAverageExecutionTime());
        System.out.println("fps: " + tt.getMetricFPS());
    }
    private static ThreadTimer tt;
    private static long dt;
    public static void main(String[] args) {
        tt = new ThreadTimer(1000L, () -> {
            System.out.println("STARTED");
            printMetric();
            long curTime = System.currentTimeMillis();
            MultiTool.sleepThread(15);
            System.out.println("dt : " + (curTime - dt));
            dt = curTime;
            System.out.println("COMPLETED");
            System.out.println();
        });
        
        dt = System.currentTimeMillis();
        //tt.setFPS(1);
        //tt.setTargetFPS(1);
        //tt.setFPSState(FPSState.AUTO);
        MultiTool.sleepThread(100);
        tt.start();
        MultiTool.sleepThread(1000);
        
        for (int i = 0; i < 10; i++) {
            MultiTool.sleepThread(500);
            tt.pause();
            MultiTool.sleepThread(250);
            tt.resume();
            MultiTool.sleepThread(500);
        }
        
        // To keep the program alive.
        while(true) {
            MultiTool.sleepThread(100);
        }
    }
    
    
}