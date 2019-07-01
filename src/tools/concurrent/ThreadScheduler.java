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
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * TODO: comments
 * 
 * Updating thread.
 * Use {@link #scheduleTask(Runnable)} to schedule a task.
 */
public class ThreadScheduler
        extends Scheduler {
    final protected Deque<Runnable> requestQueue = new LinkedList<>();
    
    final protected Condition addedToQueue = lock.newCondition();
    final protected Condition waitForEmpty = lock.newCondition();
    
    final protected Lock interruptLock = new ReentrantLock();
    
    private static AtomicInteger id = new AtomicInteger(0);
    protected volatile boolean started = false;
    protected volatile boolean terminated = false;
    protected volatile boolean isDone = true;
    
    final protected Thread thread;
    
    
    /**
     * Creates a new update thread.
     */
    public ThreadScheduler() {
        this(Thread.NORM_PRIORITY);
    }
    
    /**
     * Creates a new scheduler with an update thread. <br>
     * Use {@link #start()} to start the thread and
     * {@link #scheduleTask(Runnable)} to schedule a task on this thread.<br>
     * Use {@link #terminate()} to terminate the update thread and clean-up.
     * 
     * @param priority 
     */
    public ThreadScheduler(int priority) {
        int threadID = id.getAndIncrement();
        
        thread = new Thread("Update-thread-" + threadID) {
            @Override
            public void run() {
                while (true) {
                    /** Obtain the task. */
                    Runnable r = null;
                    lock.lock();
                    try {
                        // If the queue is empty, wait for a task.
                        if (requestQueue.isEmpty()) {
                            isDone = true;
                            lock.unlock();
                            try {
                                forceNotifyObservers(Event.ALL_TASKS_FINISHED);
                                
                            } finally {
                                lock.lock();
                            }
                        }
                        
                        if (requestQueue.isEmpty()) {
                            waitForEmpty.signalAll();
                            addedToQueue.await();
                            
                        } else {
                            isDone = false;
                        }
                        
                        if (terminated || Thread.currentThread()
                                .isInterrupted()) {
                            cleanup();
                            return;
                        }
                        
                        // Obtain and remove the task from the queue.
                        r = requestQueue.pollFirst();
                        
                    } catch (InterruptedException e) {
                        if (terminated || Thread.currentThread()
                                .isInterrupted()) {
                            cleanup();
                            return;
                            
                        } else {
                            Logger.write(new Object[] {
                                "Interrupted exception was caught in "
                                        + "update thread:",
                                e
                            }, Logger.Type.ERROR);
                        }
                        
                    } finally {
                        lock.unlock();
                    }
                    
                    /** Execute the task. */
                    forceNotifyObservers(Event.TASK_STARTED);
                    try {
                        if (r != null) {
                            r.run();
                        }
                        
                        if (terminated || Thread.currentThread()
                                .isInterrupted()) {
                            cleanup();
                            return;
                        }
                        
                    } catch (Exception e) {
                        // Note that no interrupts from {@link #interrupt()} from
                        // this class can be thrown when the task is executing.
                        if (!terminated) {
                            Logger.write(new Object[] {
                                "Uncaught exception in " + getName() + ":",
                                e
                            }, Logger.Type.ERROR);
                        }
                        
                    }
                    
                    forceNotifyObservers(Event.TASK_FINISHED);
                    
                    if (terminated || Thread.currentThread().isInterrupted()) {
                        cleanup();
                        return;
                    }
                }
            }
        };
        thread.setPriority(priority);
    }
    
    
    /**-------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Internal cleanup when terminated.
     */
    private void cleanup() {
        lock.lock();
        try {
            requestQueue.clear();
            
            // To prevent waiting threads for stopped scheduleTask thread.
            waitForEmpty.signalAll();
            
        } finally {
            lock.unlock();
            forceNotifyObservers(Event.TERMINATED);
        }
    }
    
    @Override
    public void scheduleTask(Runnable task)
            throws IllegalStateException {
        if (!started) throw new IllegalStateException("Not yet started!");
        if (terminated) throw new IllegalStateException("Already terminated!");
        if (task == null) return;
        
        lock.lock();
        try {
            if (terminated) return;
            isDone = false;
            requestQueue.addLast(task);
            addedToQueue.signalAll();
            
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public int queueSize() {
        return requestQueue.size();
    }
    
    @Override
    public void start()
            throws IllegalStateException {
        if (started) return;
        lock.lock();
        try {
            if (terminated)
                throw new IllegalStateException("Already terminated!");
            started = true;
            thread.start();
            
        } finally {
            lock.unlock();
            forceNotifyObservers(Event.STARTED);
        }
    }
    
    @Override
    public void terminate()
            throws InterruptedException {
        if (terminated) return;
        lock.lock();
        try {
            terminated = true;
            addedToQueue.signalAll();
            waitForEmpty.signalAll();
            
        } finally {
            lock.unlock();
        }
        
        thread.join();
    }
    
    @Override
    public boolean terminate(long timeout, TimeUnit tu)
            throws InterruptedException {
        if (terminated) return true;
        lock.lock();
        try {
            terminated = true;
            addedToQueue.signalAll();
            waitForEmpty.signalAll();
            
        } finally {
            lock.unlock();
        }
        
        thread.join(Math.max(1L, tu.toMillis(timeout)));
        return thread.isAlive();
    }
    
    /**
     * {@inheritDoc}
     * 
     * Does not terminate if the current executing task is infinitely
     * blocking and does not does not check the interrupted flag of the thread.
     */
    @Override
    public void forceTerminate()
            throws InterruptedException {
        if (terminated) return;
        lock.lock();
        try {
            terminated = true;
            thread.interrupt();
            addedToQueue.signalAll();
            waitForEmpty.signalAll();
            
        } finally {
            lock.unlock();
        }
        
        thread.join();
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
            if (!started) {
                forceNotifyObservers(Event.TERMINATED);
            }
        }
    }
    
    @Override
    public boolean isDone() {
        lock.lock();
        try {
            return isDone;
            
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public void waitUntilDone()
            throws InterruptedException {
        lock.lock();
        try {
            if (terminated) return;
            if (isDone) return;
            waitForEmpty.await();
            
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * @param priority the new priority of the update thread.
     * 
     * @see Thread#setPriority(int)
     */
    public void setPriority(int priority) {
        thread.setPriority(priority);
    }
    
    /**
     * @return the priority of the update thread.
     * 
     * @see Thread#getPriority()
     */
    public int getPriority() {
        return thread.getPriority();
    }
    
    
}
