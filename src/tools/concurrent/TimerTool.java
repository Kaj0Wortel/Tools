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

package tools.concurrent;


// Tools imports
import tools.MultiTool;


// Java imports
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * TODO: comments
 * 
 * Class which adds extra functionallity to the default
 * {@link java.util.Timer} class. The added functionalities are:
 *  o Changing the update interval. Keeps the same task running. Can be set
 *    via interval or updates per second (fps).
 *  o Pausing a timer. A paused timer will, after being resumed, continue
 *    as if it wasn't paused. So if the next update is over 500ms before
 *    being paused, then after being resumed the next update will occur over
 *    500ms. If the update interval was decreased with {@code dt} while the
 *    timer was paused, then the next update will occur over
 *    {@code max(0, 500-dt)}.
 *  o Canceling a timer. After a timer has been canceled, it can be started
 *    again with the same settings.
 *  o Manual and automatic frame rate handeling. In manual mode, a target
 *    interval is set and the updates occur after every interval time.
 *    If the previous update was still running, then the current update is
 *    ignored.
 *    In automatic mode, a target interval is set and AIMD is used to prevent
 *    clashing update cycles.
 *  o Starting a timer which is already started does nothing.
 *  o Resuming a timer which is already started or canceled does nothing.
 *  o Pausing a timer which is already paused or canceled does nothing.
 *  o Canceling a timer which is already canceled does nothing.
 * 
 * This class is suitable for concurrent use.
 * 
 * @author Kaj Wortel (0991586)
 */
public class TimerTool {
    /**
     * Enum for the current state of the timer.
     */
    public enum TimerState {
        RUNNING, PAUSED, CANCELED
    }
    
    private TimerState timerState = TimerState.CANCELED;
    private static FPSState fpsState = FPSState.MANUAL;
    
    // Lock for concurrent operations.
    final private Lock lock = new ReentrantLock();
    
    // The current timer object.
    private Timer timer;
    // The tasks to be executed.
    final private Runnable[] tasks;
    // The initial delay.
    private long delay;
    // The timer interval.
    private long interval;
    // The target interval.
    private long targetInterval;
    
    // The start timestamp of the timer for the current iteration.
    private Long startTime;
    
    // The pause timestamp of the timer. If there was no pause in
    // this iteration then it is equal to the start timestamp.
    private Long pauseTime;
    
    // Whether the execution is still being performed.
    public boolean running = false;
    
    // Keeps track of how many cycles must pass before the
    // additative increase is replaced multiplicative increase.
    public int waitMul = 0;
    
    
    /**-------------------------------------------------------------------------
     * Constructor.
     * -------------------------------------------------------------------------
     */
    /**
     * @param r the action that will be executed when the timer ends.
     * @param delay the time in ms before the first exectution of
     *     {@code r.run()}.
     * @param interval the time in ms which is between two executions of
     *     {@code r.run()}.
     */
    public TimerTool(Runnable... rs) {
        this(0L, 1000L, rs);
    }
    
    public TimerTool(long interval, Runnable... rs) {
        this(0L, interval, rs);
    }
    
    public TimerTool(long delay, long interval, Runnable... rs) {
        // Update the values to the values in this class
        this.tasks = rs;
        this.delay = delay;
        this.interval = interval;
        
        // Create new timer.
        timer = new Timer(true);
        
        this.delay = delay;
        
        // For the first iteration is the start time modified because then
        // there are no problems with the pause/resume functions if the
        // timer is still in the initial delay.
        startTime = System.currentTimeMillis() + delay - interval;
        
        // By default, set the target interval to the given interval.
        targetInterval = interval;
    }
    
    
    /**-------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Create a new timer task from the given runnable.
     * Also updates the start time and the pause time.
     * 
     * @param rs the tasks to be executed. Is allowed to be null,
     * but this is not effective.
     * 
     * Handles the fps rate using M/AIMD (multiplicative/additative increase,
     * multiplicative decrease). Decreases when the executing task cannot
     * keep up with the speed of the timer. Increases when the
     * executing task can keep up with the speed of the timer and the
     * targetInterval has not yet been reached.
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
                    //System.err.println("QUIT");
                    
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
     * Starts the timer.
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
            
            timer = new Timer(true);
            
            timer.scheduleAtFixedRate(createTimerTask(tasks), delay, interval);
            
            // Update the timeState
            timerState = TimerState.RUNNING;
            
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Pauses the timer.
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
     * Resumes a paused timer.
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
            
            timer = new Timer(true);
            
            timer.scheduleAtFixedRate(createTimerTask(tasks), startDelay,
                    interval);
            
            // Update the timeState
            timerState = TimerState.RUNNING;
            
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Cancels a timer.
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
     * @param interval the new interval to be set.
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
                timer = new Timer(true);
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
     * 
     * @param interval the new target interval.
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
        return 1000.0 / interval;
    }
    
    /**
     * Sets the state for handeling the frame rate.
     * 
     * @param state the new state.
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
     * @return the current state for handeling the frame rate.
     */
    public FPSState getFPSState() {
        return fpsState;
    }
    
    /**
     * @return the current state of the timer.
     */
    public TimerState getState() {
        return timerState;
    }
    
    
    /**------------
     * TMP
     */
    private static long dt;
    public static void main(String[] args) {
        TimerTool tt = new TimerTool(1000L, 33L, () -> {
            System.out.println("STARTED");
            long curTime = System.currentTimeMillis();
            MultiTool.sleepThread(30);
            System.out.println("test: " + (curTime - dt));
            dt = curTime;
            System.out.println("COMPLETED");
        });
        dt = System.currentTimeMillis();
        tt.setTargetFPS(1);
        
        
        tt.setFPSState(FPSState.AUTO);
        tt.setFPS(1);
        tt.start();
        for (int i = 0; i < 10; i++) {
            MultiTool.sleepThread(250);
            tt.pause();
            MultiTool.sleepThread(250);
            tt.resume();
            MultiTool.sleepThread(750);
        }
        
        // To keep the program alive.
        while(true) {
            MultiTool.sleepThread(100);
        }
    }
    
    
}