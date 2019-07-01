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
import tools.log.Logger;


// Java imports
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * TODO: comments
 * 
 * Most basic implementation of {@link Scheduler}. Simply executes the task
 * directly on the same thread.
 * 
 * @author Kaj Wortel
 */
public class SelfScheduler
        extends Scheduler {
    
    final private Condition taskFinished = lock.newCondition();
    private boolean terminated = false;
    private boolean started = false;
    private int queueSize = 0;
    
    
    @Override
    public void scheduleTask(Runnable task)
            throws IllegalStateException {
        if (!started) throw new IllegalStateException("Not yet started!");
        if (terminated) throw new IllegalStateException("Already terminated!");
        
        lock.lock();
        try {
            queueSize++;
            task.run();
            
        } catch (Exception e) {
            Logger.write(new Object[] {
                "Uncaught exception in self scheduler:",
                e
            }, Logger.Type.ERROR);
            
        } finally {
            taskFinished.signalAll();
            queueSize--;
            lock.unlock();
        }
    }
    
    @Override
    public int queueSize() {
        return queueSize;
    }
    
    @Override
    public void start()
            throws IllegalStateException {
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
        lock.lock();
        try {
            return started;
            
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public boolean isTerminated() {
        lock.lock();
        try {
            return terminated;
            
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public boolean isDone() {
        return queueSize == 0;
    }
    
    @Override
    public void waitUntilDone() throws InterruptedException {
        lock.lock();
        try {
            if (queueSize != 0) {
                taskFinished.await();
            }
            
        } finally {
            lock.unlock();
        }
    }
    
    
}
