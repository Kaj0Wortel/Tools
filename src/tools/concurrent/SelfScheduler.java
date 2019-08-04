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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;


// Tools imports
import tools.log.Logger;


/**
 * Most basic implementation of {@link Scheduler}. Simply executes the task
 * directly on the same thread in the {@link #scheduleTask(Runnable)} function.
 * 
 * @todo testing
 * 
 * @apiNote
 * It is possible to let multiple threads execute via this class.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class SelfScheduler
        extends Scheduler {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** Condition used to signal when a task is finished. */
    private final Condition taskFinished = lock.newCondition();
    
    /** Denotes whether the scheduler was started. */
    private volatile boolean started = false;
    /** Denotes whether the scheduler was terminated. */
    private volatile boolean terminated = false;
    /** The size of the queue. */
    private AtomicInteger queueSize = new AtomicInteger(0);
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public void scheduleTask(Runnable task)
            throws IllegalStateException {
        if (!started) throw new IllegalStateException("Not yet started!");
        if (terminated) throw new IllegalStateException("Already terminated!");
        queueSize.incrementAndGet();
        
        try {
            task.run();
            
        } catch (Exception e) {
            Logger.write(new Object[] {
                "Uncaught exception in self scheduler:",
                e
            }, Logger.Type.ERROR);
            
        } finally {
            queueSize.decrementAndGet();
        }
        
        lock.lock();
        try {
            taskFinished.signalAll();
            
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public int queueSize() {
        lock.lock();
        try {
            return queueSize.get();
            
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public void start()
            throws IllegalStateException {
        if (started) throw new IllegalStateException("Already started!");
        if (terminated) throw new IllegalStateException("Already terminated!");
        started = true;
    }
    
    @Override
    public boolean terminate(long timeout, TimeUnit tu) {
        terminated = true;
        return true;
    }
    
    @Override
    public boolean isStarted() {
        return started;
    }
    
    @Override
    public boolean isTerminated() {
        return terminated;
    }
    
    @Override
    public boolean tryTerminate() {
        terminated = true;
        return true;
    }
    
    @Override
    public boolean isDone() {
        return queueSize.get() == 0;
    }
    
    @Override
    public void waitUntilDone()
            throws InterruptedException {
        lock.lock();
        try {
            if (queueSize.get() != 0) {
                taskFinished.await();
            }
            
        } finally {
            lock.unlock();
        }
    }
    
    
}
