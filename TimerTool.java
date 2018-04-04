/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2018 by Kaj Wortel - all rights reserved                    *
 * Contact: kaj.wortel@gmail.com                                             *
 *                                                                           *
 * This file is part of the tools project, which can be found on github:     *
 * https://github.com/Kaj0Wortel/tools                                       *
 *                                                                           *
 * It is allowed to use, (partially) copy and modify this file               *
 * in any way for private use only by using this header.                     *
 * It is not allowed to redistribute any (modifed) versions of this file     *
 * without my permission.                                                    *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package tools;


// Java imports
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;


public class TimerTool {
    private Timer timer = new Timer(true);
    private Runnable task; // the task to be executed. Called via rask.run()
    private Long delay; // the initial delay
    private Long interval; // the timerinterval
    
    // the start timestamp of the timer for the current iteration
    private Long startTime = System.currentTimeMillis();
    
    // the pause timestamp of the timer. If there was no pause in
    // this iteration then it is equal to the start timestamp
    private Long pauseTime = System.currentTimeMillis();
    
    // the current state of the timer
    private TimerState timerState = TimerState.CANCELED;
    public enum TimerState {
        RUNNING, PAUSED, CANCELED
    }
    
    // --------------------------------------------------------------------------------------------------------
    // Constructor
    public TimerTool(Runnable r, Long delay, Long interval) {
        // Update the values to the values in this class
        this.task = r;
        this.delay = delay;
        this.interval = interval;
        
        // Initialize the timer. Also checks if the interval is not equal to 'null'
        // If so, set the interval to 10 ms
        // NOTE that this.interval still equals null!
        if (interval != null) {
            timer = new Timer(true);
        
        } else {
            timer = new Timer(false);
        }
        
        // Checks if the delay is not equal to 'null'.
        // If so, set the initial deylay equal to '0'
        if (delay == null) {
            delay = 0L;
            this.delay = delay;
        }
        
        // For the first iteration is the start time modified because then there are no problems
        // with the pause/resume functions if the timer is still in the initial delay.
        if (interval != null) {
            startTime = System.currentTimeMillis() + delay  - interval;
        } else {
            startTime = System.currentTimeMillis() + delay;
        }
    }
    
    // --------------------------------------------------------------------------------------------------------
    // Functions
    
    /* 
     * Create a new timer task from the given runnable.
     * Also updates the start time and the pause time.
     */
    private TimerTask createTimerTask(Runnable r) {
        return new TimerTask() {
            @Override
            public void run() {
                // Update the timestamps
                startTime = System.currentTimeMillis();
                pauseTime = System.currentTimeMillis();
                
                // Run the function
                r.run();
                
                if (interval == null) {
                    timer.cancel();
                }
            }
        };
    }
    
    /* 
     * (Re)-starts the timer
     */
    public void start() {
        if (timerState == TimerState.RUNNING) {
            timer.cancel();
            timer.purge();
        }
        
        // update the timestamps
        startTime = System.currentTimeMillis();
        pauseTime = System.currentTimeMillis();
        
        timerState = TimerState.RUNNING;
        timer = new Timer(true);
        
        if (interval != null) {
            timer.scheduleAtFixedRate(createTimerTask(task), delay, interval);
        } else {
            timer.schedule(createTimerTask(task), delay);
        }
    }
    
    /* 
     * Pauses the timer
     */
    public void pause() {
        if (timerState == TimerState.RUNNING) {
            timer.cancel();
            timer.purge();
        }
        timerState = TimerState.PAUSED;
        pauseTime = System.currentTimeMillis();
    }
    
    /* 
     * Resumes a paused timer
     */
    public void resume() {
        if (timerState == TimerState.RUNNING) {
            timer.cancel();
            timer.purge();
        }
        long newDelay = interval - (pauseTime - startTime);
        if (newDelay < 0) {
            newDelay = 0;
        }
        
        // update the timestamps
        startTime = System.currentTimeMillis() - newDelay;
        pauseTime = System.currentTimeMillis() - newDelay;
        
        timerState = TimerState.RUNNING;
        timer = new Timer(true);
        
        if (interval != null) {
            timer.scheduleAtFixedRate(createTimerTask(task), delay, interval);
        } else {
            timer.schedule(createTimerTask(task), delay);
        }
    }
    
    /* 
     * Cancels a scheduled timer
     */
    public void cancel() {
        if (timerState == TimerState.RUNNING) {
            timer.cancel();
            timer.purge();
        }
        timerState = TimerState.CANCELED;
    }
    
    /* 
     * Sets a new interval for executing a task
     */
    public void setInterval(long interval) {
        this.interval = interval;
        
        if (timerState == TimerState.RUNNING) {
            timer.cancel();
            timer.purge();
            
            long newDelay = interval - (System.currentTimeMillis() - startTime);
            if (newDelay < 0) {
                newDelay = 0;
            }
            
            // update the timestamps
            startTime = System.currentTimeMillis() - newDelay;
            pauseTime = System.currentTimeMillis() - newDelay;
            
            timer = new Timer(true);
            timer.scheduleAtFixedRate(createTimerTask(task), newDelay, interval);
            pauseTime = System.currentTimeMillis();
        }
    }
    
}
