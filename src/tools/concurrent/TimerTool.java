/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) August 2019 by Kaj Wortel - all rights reserved             *
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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Class which adds extra functionallity to the default
 * {@link java.util.Timer} class. The added functionalities are:
 * <ul>
 *   <li> Changing the update interval. Keeps the same task running. Can be set
 *        via interval or updates per second (fps). </li>
 *   <li> Pausing a timer. A paused timer will, after being resumed, continue
 *        as if it wasn't paused. So if the next update is over 500ms before
 *        being paused, then after being resumed the next update will occur over
 *        500ms. If the update interval was decreased with {@code dt} while the
 *        timer was paused, then the next update will occur over
 *        {@code max(0, 500-dt)}. <li>
 *   <li> Canceling a timer. After a timer has been canceled, it can be started
 *        again with the same settings. </li>
 *   <li> Manual and automatic frame rate handeling. In manual mode, a target
 *        interval is set and the updates occur after every interval time.
 *        If the previous update was still running, then the current update is
 *        ignored. <br>
 *        In automatic mode, a target interval is set and AIMD is used to prevent
 *        clashing update cycles. </li>
 *   <li> Starting a timer which is already started does nothing. </li>
 *   <li> Resuming a timer which is already started or canceled does nothing. </li>
 *   <li> Pausing a timer which is already paused or canceled does nothing. </li>
 *   <li> Canceling a timer which is already canceled does nothing. </li>
 * </ul>
 * 
 * This class is suitable for concurrent access.
 * 
 * @todo Comments + refactoring + testing
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class TimerTool {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** Lock for concurrent operations. */
    private final Lock lock = new ReentrantLock();
    /** The tasks to be executed. */
    private final Runnable[] tasks;
    /** Whether the timers will run on daemon threads. */
    private final boolean isDaemon;
    
    /** The state of the timer. */
    private TimerState timerState = TimerState.CANCELED;
    /** The FPS scheduling mode. */
    private static FPSState fpsState = FPSState.MANUAL;
    
    /** The current timer object. */
    private Timer timer;
    /** The initial delay. */
    private long delay;
    /** The timer interval. */
    private long interval;
    /** The target interval. */
    private long targetInterval;
    
    /** The start timestamp of the timer for the current iteration. */
    private Long startTime;
    
    /** The pause timestamp of the timer. If there was no pause in
     *  this iteration then it is equal to the start timestamp. */
    private Long pauseTime;
    
    /** Whether the execution is still being performed. */
    private boolean running = false;
    
    /** Keeps track of how many cycles must pass before the
     *  additative increase is replaced multiplicative increase. */
    private int waitMul = 0;
    
    
    /* -------------------------------------------------------------------------
     * Inner-classes.
     * -------------------------------------------------------------------------
     */
    /**
     * Enum for the current state of the timer.
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
     * Creates a timer with no initial delay and runs at 1 second intervals.
     * 
     * @param rs The actions that will be executed when the timer ends.
     */
    public TimerTool(Runnable... rs) {
        this(0L, 1000L, true, rs);
    }
    
    /**
     * Creates a timer with no initial delay and runs at the given interval.
     * 
     * @param rs The actions that will be executed when the timer ends.
     * @param interval The time in ms which is between two executions of {@code r.run()}.
     */
    public TimerTool(long interval, Runnable... rs) {
        this(0L, interval, true, rs);
    }
    
    /**
     * Creates a new timer with the given delay and interval, which will run
     * the given runnables as tasks.
     * 
     * @param rs The actions that will be executed when the timer ends.
     * @param interval The time in ms which is between two executions of {@code r.run()}.
     */
    public TimerTool(long delay, long interval, Runnable... rs) {
        this(delay, interval, true, rs);
    }
    
    /**
     * Creates a new timer with the given delay and interval, which will run
     * the given runnables as tasks. The times will run as daemon only
     * if {@link isDaemon == true}
     * 
     * @param rs The actions that will be executed when the timer ends.
     * @param delay The time in ms before the first exectution of {@code r.run()}.
     * @param interval The time in ms which is between two executions of {@code r.run()}.
     * @param isDaemon Whether the created times will be daemon or not. The default is {@code true}.
     */
    public TimerTool(long delay, long interval, boolean isDaemon, Runnable... rs) {
        this.tasks = rs;
        this.delay = delay;
        this.interval = interval;
        
        // Create new timer.
        timer = new Timer(this.isDaemon = isDaemon);
        
        this.delay = delay;
        
        // For the first iteration is the start time modified because then
        // there are no problems with the pause/resume functions if the
        // timer is still in the initial delay.
        startTime = System.currentTimeMillis() + delay - interval;
        
        // By default, set the target interval to the given interval.
        targetInterval = interval;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Create a new timer task from the given runnable.
     * Also updates the start time and the pause time. <br>
     * <br>
     * Handles the fps rate using M/AIMD (multiplicative/additative increase,
     * multiplicative decrease). Decreases when the executing task cannot
     * keep up with the speed of the timer. Increases when the
     * executing task can keep up with the speed of the timer and the
     * targetInterval has not yet been reached.
     * 
     * @param rs The tasks to be executed. Is allowed to be null,
     *     but this is not effective.
     */
    private TimerTask createTimerTask(Runnable... rs) {
        return new TimerTask() {
            @Override
            public void run() {
                boolean wasRunning;
                lock.lock();
                try {
                    wasRunning = running;
                    running = true;
                    
                    // Update the timestamps of the start and pause time.
                    startTime = System.currentTimeMillis();
                    pauseTime = startTime; // To ensure equal timestamps.
                    
                } finally {
                    lock.unlock();
                }
                
                if (wasRunning) {
                    if (fpsState == FPSState.AUTO) {
                        waitMul += 10;
                        setInterval((long) Math.ceil((interval * 1.05)));
                    }
                    
                    return;
                    
                } else {
                    if (fpsState == FPSState.AUTO) {
                        if (interval > targetInterval) {
                            if (--waitMul <= 0) {
                                waitMul = 0;
                                setInterval(Math.max((
                                        long) (interval * 0.95),
                                        targetInterval));
                            } else {
                                setInterval(interval - 1);
                            }
                            
                        } else if (interval < targetInterval) {
                            setInterval(targetInterval);
                        }
                    }
                }
                
                // Run the function(s) on a new thread.
                new Thread() {
                    @Override
                    public void run() {
                        if (rs != null) {
                            for (Runnable r : rs) {
                                r.run();
                            }
                        }
                        
                        lock.lock();
                        try {
                            running = false;
                            
                        } finally {
                            lock.unlock();
                        }
                    }
                }.start();
            }
        };
    }
    
    /**
     * Starts the timer. <br>
     * Does nothing if the timer is already running or paused.
     */
    public void start() {
        lock.lock();
        try {
            if (timerState == TimerState.RUNNING ||
                    timerState == TimerState.PAUSED) return;
            
            // Update the timestamps
            startTime = System.currentTimeMillis();
            pauseTime = System.currentTimeMillis();
            
            timer = new Timer(isDaemon);
            
            timer.scheduleAtFixedRate(createTimerTask(tasks), delay, interval);
            
            // Update the timeState
            timerState = TimerState.RUNNING;
            
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Pauses the timer. <br>
     * Does nothing if the timer is paused or stopped.
     */
    public void pause() {
        lock.lock();
        try {
            if (timerState == TimerState.PAUSED ||
                    timerState == TimerState.CANCELED) return;
            
            timer.cancel();
            timer.purge();
            
            // Set the pause time stamp
            pauseTime = System.currentTimeMillis();
            
            // Update the timeState
            timerState = TimerState.PAUSED;
            
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Resumes a paused timer. <br>
     * Does nothing if the timer is running or canceled.
     */
    public void resume() {
        lock.lock();
        try {
            if (timerState == TimerState.RUNNING ||
                    timerState == TimerState.CANCELED) return;
            
            // The current time.
            long curTime = System.currentTimeMillis();
            
            // Calculate the initial delay.
            long timeBeforeRun = interval - (pauseTime - startTime);
            long startDelay = (timeBeforeRun < 0 ? 0 : timeBeforeRun);
            
            // Update the start time stamp.
            startTime = curTime - timeBeforeRun;
            
            timer = new Timer(isDaemon);
            
            timer.scheduleAtFixedRate(createTimerTask(tasks), startDelay,
                    interval);
            
            // Update the timeState
            timerState = TimerState.RUNNING;
            
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Cancels a timer. <br>
     * Does nothing if the timer is canceled.
     */
    public void cancel() {
        lock.lock();
        try {
            if (timerState == TimerState.CANCELED) return;
            
            // Kill the current timer.
            if (timerState == TimerState.RUNNING) {
                timer.cancel();
                timer.purge();
            }
            
            // Update the timeState
            timerState = TimerState.CANCELED;
            
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Sets a new interval for the timer.
     * 
     * @param interval The new interval to be set.
     */
    public void setInterval(long interval) {
        lock.lock();
        try {
            // If the interval is equal, return immediately.
            if (interval == this.interval) return;
            
            // Update the interval
            this.interval = interval;
            
            // The current time
            long curTime = System.currentTimeMillis();
            
            // If the timer is running, kill it and start a new timer.
            if (timerState == TimerState.RUNNING) {
                timer.cancel();
                timer.purge();
                
                // Calculate the initial delay.
                long timeBeforeRun = interval - (curTime - startTime);
                long startDelay = (timeBeforeRun <= 0 ? 0 : timeBeforeRun);
                
                // Update the start timestamp if the timer has to start directly.
                if (timeBeforeRun <= 0) {
                    startTime = curTime + timeBeforeRun;
                }
                
                // Start a new timer
                timer = new Timer(isDaemon);
                timer.scheduleAtFixedRate(createTimerTask(tasks), startDelay,
                        interval);
            }
            
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Sets the interval using frame rates.
     * Uses {@link #setInterval(long)} for the implementation.
     * 
     * @param fps The new fps.
     */
    public void setFPS(double fps) {
        setInterval((long) (1000 / fps));
    }
    
    /**
     * Sets the target interval.
     * When using the auto-fps mode, this value will be used even when
     * a shorter interval is possible. Also sets the current interval
     * to the target interval to improve convergence.
     * 
     * @param interval The new target interval.
     */
    public void setTargetInterval(long interval) {
        lock.lock();
        try {
            targetInterval = interval;
            this.interval = interval;
            
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Sets the target frame rate.
     * 
     * @param fps The new target frame rate.
     */
    public void setTargetFPS(double fps) {
        setTargetInterval((long) (1000 / fps));
    }
    
    /**
     * @return The current interval between two updates.
     */
    public long getInterval() {
        return interval;
    }
    
    /**
     * @return The current number of frames per second.
     */
    public double getFPS() {
        return 1000.0 / interval;
    }
    
    /**
     * Sets the state for handeling the frame rate.
     * 
     * @param state The new state.
     */
    public void setFPSState(FPSState state) {
        lock.lock();
        try {
            fpsState = state;
            
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * @return The current state for handeling the frame rate.
     */
    public FPSState getFPSState() {
        return fpsState;
    }
    
    /**
     * @return The current state of the timer.
     */
    public TimerState getState() {
        return timerState;
    }
    
    
}