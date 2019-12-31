/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) July 2019 by Kaj Wortel - all rights reserved               *
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

package tools.concurrent;


// Java imports
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


// Tool imports
import tools.MultiTool;
import tools.log.Logger;


/**
 * Class which replaces the {@link java.util.Timer} class. The added functionalities are:
 * <ul>
 *   <li> Changing the update interval. Keeps the same task running. Can be set
 *        via interval or updates per second (fps). </li>
 *   <li> Pausing a timer. A paused timer will, after being resumed, continue
 *        as if it wasn't paused. So if the next update is over 500ms before
 *        being paused, then after being resumed the next update will occur over
 *        500ms. If the update interval was decreased with {@code dt} while the
 *        timer was paused, then the next update will occur over
 *        {@code max(0, 500-dt)}. </li>
 *   <li> Canceling a timer. After a timer has been canceled, it can be started
 *        again with the same settings. </li>
 *   <li> Manual and automatic frame rate handeling. In manual mode, a target
 *        interval is set and the updates occur after every interval time.
 *        If the previous update was still running, then the current update is
 *        ignored. <br>
 *        In automatic mode, a target interval is set and AIMD is used to prevent
 *        clashing update cycles. </li>
 *   <li> Starting a timer which is already started does nothing.</li>
 *   <li> Resuming a timer which is already started or canceled does nothing.</li>
 *   <li> Pausing a timer which is already paused or canceled does nothing.</li>
 *   <li> Canceling a timer which is already canceled does nothing.</li>
 * </ul>
 * 
 * This class is thread safe.
 * 
 * @todo
 * - Fix performance bug when quickly pausing/unpausing. See case below.
 * - Improve performance.
 * - Extensive testing.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class ThreadTimer {
    
    /* -------------------------------------------------------------------------
     * Constants.
     * -------------------------------------------------------------------------
     */
    /** Counter to keep track of the timer id's of {@code ThreadTimer}s. */
    private static final AtomicInteger TIMER_ID_COUNTER = new AtomicInteger(0);
    /** The amount of metric data kept in memory. */
    private final static int METRIC_SIZE = 20;
    
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The ID of this {@code ThreadTimer}. */
    private final int timeId;
    /** The tasks to be executed. */
    private final Runnable[] tasks;
    
    /** Lock used for concurrent operations. */
    private final Lock lock = new ReentrantLock(true);
    /** The condition used to wait for the currently executing
     *  thread to prevent race conditions. */
    private final Condition threadIdCondition = lock.newCondition();
    /** The condition to wake the update thread after it is done. */
    private final Condition wait = lock.newCondition();
    
    /** The current FPS scheduling mode. */
    private static FPSState fpsState = FPSState.MANUAL;
    /** The current state of the timer. */
    private TimerState timerState = TimerState.CANCELED;
    
    /** The thread used for executing the run functions. */
    private Thread updateThread;
    /** The current ID of the thread which is allowed to execute the run function.
     *  This value is updated <b>before</b> the thread starts. */
    private volatile int threadId = 0;
    /** The previous ID of the thraed which was allowed to execute the run function.
     *  This value is updated <b>after</b> the previous thread was terminated. */
    private volatile int prevThreadId = 0;
    
    /** The delay set by the user when starting the timer. */
    private long initialDelay;
    /** The initial delay of the timer used for un-pausing the timer. */
    private volatile long delay;
    /** The current interval of the interval. */
    private volatile long interval;
    /** The interval the timer should aim for when using {@link FPSState#AUTO}. */
    private volatile long targetInterval;
    
    /** The start timestamp of the timer for the current iteration. */
    private volatile long startTime;
    /** The pause timestamp of the timer. If there was no pause in
     *  this iteration then it is equal to {@link #startTime}. */
    private volatile long pauseTime;
    
    /**  Keeps track of how many cycles must pass before the
     *  additative increase is replaced by multiplicative increase. */
    private int waitMul = 0;
    
    /** Denotes the thread priority for this timer. */
    private int priority = Thread.NORM_PRIORITY;
    
    /** The scheduler used for executing the tasks. */
    private final Scheduler sched;
    
    /*
     * Metric variables.
     */
    private final Lock metricLock = new ReentrantLock();
    /** Whether to restart the metrics on the next cycle. */
    private boolean restartMetric = true;
    /** The timestamp used for calculating time metrics. */
    private long metricTimeStamp = System.currentTimeMillis();
    /** The average interval time of the timer. */
    private float metricAvgSpeed = 0.0f;
    /** The number of time the metrics were calculated this second. */
    private int metricCompleted = 0;
    /** The FPS rate metric. */
    private int metricFPS = 0;
    
    
    /* -------------------------------------------------------------------------
     * Inner classes.
     * -------------------------------------------------------------------------
     */
    /** 
     * Enum for denoting the state of the timer.
     */
    public enum TimerState {
        /** Denotes that the timer is currently executing the run-wait cycle. */
        RUNNING,
        /** Denotes that the time is not executing the run-wait cycle, but
         *  remembers how much time is remaining from the previous cycle and
         *  the interval settings. */
        PAUSED,
        /** Denotes that the timer is not running, and can only be started
         *  in this state. */
        CANCELED
    }
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new timer which uses a separate thread to execute the given functions
     * once every interval.
     * 
     * @param interval The time in ms which is between two executions of the tasks.
     * @param rs The tasks that will be executed on every interval.
     * 
     * @throws IllegalArgumentException If {@code interval <= 0}.
     * @throws NullPointerException If {@code rs == null}.
     * 
     * @see #ThreadTimer(long, long, Scheduler, Runnable...)
     */
    public ThreadTimer(long interval, Runnable... rs) {
        this(interval, interval, null, rs);
    }
    
    /**
     * Creates a new timer which uses a separate thread to execute the given functions
     * once every interval. After starting, first waits until the initial delay has past.
     * 
     * @param delay The time in ms before the first exectution of the tasks.
     * @param interval The time in ms which is between two executions of the tasks.
     * @param rs The tasks that will be executed on every interval.
     * 
     * @throws IllegalArgumentException If {@code interval <= 0} or {@code delay <= 0}.
     * @throws NullPointerException If {@code rs == null}.
     * 
     * @see #ThreadTimer(long, long, Scheduler, Runnable...)
     */
    public ThreadTimer(long delay, long interval, Runnable... rs) {
        this(delay, interval, null, rs);
    }
    
    /**
     * Creates a new timer which uses a separate thread to execute the given functions
     * once every interval on the given scheduler.
     * 
     * @param interval The time in ms which is between two executions of the tasks.
     * @param sched The scheduler used for scheduling tasks. If {@code null}, then
     *     {@link SelfScheduler} is used as default.
     * @param rs The tasks that will be executed on every interval.
     * 
     * @throws IllegalArgumentException If {@code interval <= 0}.
     * @throws NullPointerException If {@code rs == null}.
     * 
     * @see #ThreadTimer(long, long, Scheduler, Runnable...)
     */
    public ThreadTimer(long interval, Scheduler sched, Runnable... rs) {
        this(interval, interval, sched, rs);
    }
    
    /**
     * Creates a new timer which uses a separate thread to execute the given functions
     * once every interval on the given scheduler. After starting, first waits until
     * the initial delay has past.
     * 
     * @param delay The time in ms before the first exectution of the tasks.
     * @param interval The time in ms which is between two executions of the tasks.
     * @param sched The scheduler used for scheduling tasks. If {@code null}, then
     *     {@link SelfScheduler} is used as default.
     * @param rs The tasks that will be executed on every interval.
     * 
     * @throws IllegalArgumentException If {@code interval <= 0} or {@code delay <= 0}.
     * @throws NullPointerException If {@code rs == null}.
     */
    public ThreadTimer(long delay, long interval, Scheduler sched, Runnable... rs) {
        if (rs == null) throw new NullPointerException("Runnable was null!");
        if (interval <= 0){
            throw new IllegalArgumentException(
                    "Expected interval < 0, but found: " + interval);
        }
        if (delay < 0) {
            throw new IllegalArgumentException(
                    "Expected delay < 0, but found: " + delay);
        }
        
        timeId = TIMER_ID_COUNTER.getAndIncrement();
        
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
     * 
     * @param timeStamp The timestamp before execution of the method to calculate
     *     the metrics of.
     * @param exeTime The execution time of the method to calculate the metrics of.
     */
    private void updateMetric(long timeStamp, long exeTime) {
        // Update metrics.
        metricLock.lock();
        try {
            if (restartMetric) {
                metricCompleted = 0;
                metricFPS = (int) getFPS();
                metricAvgSpeed = exeTime;
                metricTimeStamp = System.currentTimeMillis() + getInterval() / 2;
                restartMetric = false;
                return;
            }
            
            // Update metrics.
            metricCompleted++;
            metricAvgSpeed = (metricAvgSpeed * (METRIC_SIZE - 1) + exeTime) / METRIC_SIZE;
            while (timeStamp > metricTimeStamp + 1000) {
                metricFPS = (int) (metricCompleted * 1000 / (metricTimeStamp - timeStamp));
                metricCompleted = 0;
                metricTimeStamp += 1000;
            }
            
        } finally {
            metricLock.unlock();
        }
        
        // Change interval if needed.
        if (fpsState == FPSState.AUTO) {
            int diff = (int) (interval - exeTime);
            if (diff < 0) {
                // Multiplicative decrease (= increase interval fast).
                waitMul += 10;
                double add = Math.max(interval * 0.05, 1);
                setInterval((long) Math.ceil((interval + add)));
                
            } else {
                if (interval > targetInterval) {
                    if (--waitMul <= 0) {
                        // Multiplicative increase (= decrease interval fast).
                        waitMul = 0;
                        long sub = Math.max((long) Math.ceil(interval * 0.05), 1);
                        setInterval(Math.max((long) (interval - sub), targetInterval));

                    } else {
                        // Additative increase (= decrease interval slow).
                        setInterval(interval - 1);
                    }
                    
                } else if (interval < targetInterval) {
                    // Cap interval at the target interval.
                    setInterval(targetInterval);
                }
            }
        }
    }
    
    /**
     * @return the average time it takes to complete one update cycle.
     */
    public float getAverageExecutionTime() {
        metricLock.lock();
        try {
            return metricAvgSpeed;
            
        } finally {
            metricLock.unlock();
        }
    }
    
    /**
     * @return the frames per second measured by the metrics.
     */
    public int getMetricFPS() {
        metricLock.lock();
        try {
            return metricFPS;
            
        } finally {
            metricLock.unlock();
        }
    }
    
    /**
     * Re-initializes the metric values.
     */
    public void restartMetric() {
        metricLock.lock();
        try {
            restartMetric = true;
            
        } finally {
            metricLock.unlock();
        }
    }
    
    /* -------------------------------------------------------------------------
     * Control flow functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Starts the timer. <br>
     * Does nothing if the timer is already running or paused.
     */
    public void start() {
        lock.lock();
        try {
            if (timerState == TimerState.RUNNING ||
                    timerState == TimerState.PAUSED) return;
            
            // Update time stamps.
            pauseTime = startTime = System.currentTimeMillis();
            delay = initialDelay;
            
            // Update the timeState.
            timerState = TimerState.RUNNING;
            
            // Set and start update thread.
            updateThread = createUpdateThread(++threadId);
            updateThread.start();
            
        } finally {
            lock.unlock();
        }
        
        restartMetric();
    }
    
    /**
     * Pauses the timer. <br>
     * Does nothing if the timer is already paused or stopped.
     */
    public void pause() {
        lock.lock();
        try {
            if (timerState == TimerState.PAUSED ||
                    timerState == TimerState.CANCELED) return;
            
            // Stop the timer.
            threadId++;
            
            // Set the pause time stamp.
            pauseTime = System.currentTimeMillis();
            
            // Update the timeState.
            timerState = TimerState.PAUSED;
            
            // Notify thread about pause.
            wait.signalAll();
            
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Resumes the paused timer. <br>
     * Does nothing if the timer is running or canceled.
     */
    public void resume() {
        lock.lock();
        try {
            if (timerState == TimerState.RUNNING ||
                    timerState == TimerState.CANCELED) return;
            
            // Update time stamps.
            long timePaused = System.currentTimeMillis() - pauseTime;
            long timeWaited = pauseTime - startTime;
            System.out.println("RESUME: " + timePaused + ", " + timeWaited);
            
            // Update time stamps.
            delay = Math.max(0, interval - timeWaited);
            startTime += timePaused;
            
            // Update the timeState.
            timerState = TimerState.RUNNING;
            
            // Create and start update thread.
            updateThread = createUpdateThread(threadId);
            updateThread.start();
            
        } finally {
            lock.unlock();
        }
        
        metricLock.lock();
        try {
            restartMetric();

        } finally {
            metricLock.unlock();
        }
    }
    
    /**
     * Cancels a timer. <br>
     * Does nothing if the timer is already canceled.
     */
    public void cancel() {
        lock.lock();
        try {
            if (timerState == TimerState.CANCELED) return;
            
            // Kill the current timer if needed.
            if (timerState == TimerState.RUNNING) {
                threadId++;
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
     * Waits a given amount of nanoseconds.
     * 
     * @param nanos The amount of nanoseconds to waint.
     * @param tId The id of the waiting thread.
     */
    private void waitTime(long nanos, int tId) {
        if (nanos <= 100_000) return;
        long nanosRem = nanos;
        lock.lock();
        try {
            while (nanosRem > 100_000L && tId == threadId) {
                try {
                    nanosRem = wait.awaitNanos(nanosRem - 100_000);
                    
                } catch (InterruptedException e) {
                    Logger.write(new Object[] {
                        "Update thread interrupted (" + tId + "): "
                                + Thread.currentThread().getName(),
                        "Ignoring interrupt!",
                        e
                    }, Logger.Type.WARNING);
                }
            }
            
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Function for the update thread.
     * 
     * @param tId The of of this thread.
     */
    @SuppressWarnings("UseSpecificCatch")
    private void threadFunction(int tId) {
        // Set initial thread priority.
        Thread.currentThread().setPriority(priority);
        
        lock.lock();
        try {
            // Prevent multiple threads from running in case of fast
            // start/stop triggers.
            //while (prevThreadId < tId - 1) {
            while (threadId < tId - 1) {
                // Terminate current thread if it should be stopped.
                if (tId != threadId) return;
                
                try {
                    threadIdCondition.await();
                    
                } catch (InterruptedException e) {
                    Logger.write(new Object[] {
                        "Update thread interrupted (" + tId + "): "
                                + Thread.currentThread().getName(),
                        "Ignoring interrupt!",
                        e
                    }, Logger.Type.WARNING);
                }
            }
            
            // Terminate current thread if it should be stopped.
            if (tId != threadId) return;
            
            // Wait the delay time.
            waitTime((startTime - System.currentTimeMillis() + delay) * 1000_000L, tId);
            
            // Terminate current thread if it should be stopped.
            if (tId != threadId) return;
            
            // Increase the start time.
            startTime += delay;
            
        } finally {
            lock.unlock();
        }
        
        
        metricLock.lock();
        try {
            restartMetric = true;
            
        } finally {
            metricLock.unlock();
        }
            
        do {
            // tmp TODO
            //System.out.println("Thread id: " + threadId);
            //System.out.println("my thread: " + tId);
            
            // If the thread was interrupted, clear the interrupt status
            // and log an error.
            if (Thread.interrupted()) {
                Logger.write(new Object[] {
                    "Updater thread " + tId + " was interrupted!",
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
                
            } catch(Exception e) {
                Logger.write(new Object[] {
                    "Update thread interrupted (" + tId + "): "
                            + Thread.currentThread().getName(),
                    "Ignoring interrupt!",
                    e
                }, Logger.Type.WARNING);
            }
            
            // Update metrics.
            long timeStamp = System.currentTimeMillis();
            long exeTime = timeStamp - startTime;
            metricLock.lock();
            try {
                updateMetric(timeStamp, exeTime);
                
            } finally {
                metricLock.unlock();
            }
            
            lock.lock();
            try {
                // tmp TODO
                //System.out.println("interval: " + interval + ", exe: " + exeTime);
                //System.out.println(interval - exeTime);
                waitTime((interval - exeTime) * 1000_000L, tId);

                // Update start time stamp.
                if (tId == threadId) {
                    startTime += interval;
                }
                
            } finally {
                lock.unlock();
            }
            
        } while (tId == threadId);
    }
    
    /**
     * @param tId The ID of the update thread to create.
     * 
     * @return A fresh update thread with the given id.
     */
    private Thread createUpdateThread(int tId) {
        return new Thread(() -> {
            threadFunction(tId);
            
            lock.lock();
            try {
                prevThreadId++;
                threadIdCondition.signalAll();
                
            } finally {
                lock.unlock();
            }
        }, "Timer-thread-" + timeId);
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
    
    public static void main2(String[] args) {
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
        for (int i = 0; i < 100; i++) {
            long time1 = System.currentTimeMillis();
            tt.waitTime(1000*1000, 0);
            long time2 = System.currentTimeMillis();
            System.out.println("diff: " + (time2 - time1));
        }
    }
    
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
        /**/
        for (int i = 0; i < 7; i++) {
            MultiTool.sleepThread(500);
            tt.pause();
            MultiTool.sleepThread(250);
            tt.resume();
            MultiTool.sleepThread(500);
        }
        /**/
        
        // To keep the program alive.
        while(true) {
            MultiTool.sleepThread(100);
        }
    }
    
    
}
