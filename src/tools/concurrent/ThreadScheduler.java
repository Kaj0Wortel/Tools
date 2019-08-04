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


// Java imports
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;


// Tools imports
import tools.log.Logger;


/**
 * Scheduler implementation using a separate thread to execute the tasks.
 * 
 * @todo testing
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class ThreadScheduler
        extends Scheduler {
    
    /* -------------------------------------------------------------------------
     * Constants.
     * -------------------------------------------------------------------------
     */
    /** The ID counter used for keeping track of multiple thread schedulers
     *  for debugging and benchmarking. */
    private static final AtomicInteger ID = new AtomicInteger(0);
    
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The request queue used for the tasks to be executed. */
    protected final Deque<Runnable> requestQueue = new LinkedList<>();
    /** The thead used for execution of the tasks. */
    protected final Thread thread;
    /** Condition which is signaled when a task was added to the queue. */
    protected final Condition addedToQueue = lock.newCondition();
    /** Condition which is signaled when the task queue is empty and no
     *  tasks are currently being executed. */
    protected final Condition waitForEmpty = lock.newCondition();
    
    /** Denotes whether the scheduler was started. */
    protected volatile boolean started = false;
    /** Denotes whether the scheduler was terminated. */
    protected volatile boolean terminated = false;
    /** Denotes whether the scheduler is done. */
    protected volatile boolean isDone = true;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new update thread with normal priority.
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
     * @param priority The priority of the thread. Must be a value between
     *     {@code 1} and {@code 10} (both inclusive).
     * 
     * @see Thread#MIN_PRIORITY
     * @see Thread#NORM_PRIORITY
     * @see Thread#MAX_PRIORITY
     */
    public ThreadScheduler(int priority) {
        int threadID = ID.getAndIncrement();
        
        thread = new Thread("Update-thread-" + threadID) {
            @Override
            public void run() {
                try {
                    while (threadTask()) { }
                    
                } catch (Exception e) {
                    Logger.write(e);
                    
                } finally {
                    cleanup();
                }
            }
        };
        thread.setPriority(priority);
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * This function regulates running scheduled tasks, notifying observers,
     * and notifying waiting threads.
     * 
     * @return {@code true} if the cycle should be repeated. {@code false} otherwise.
     */
    private boolean threadTask() {
        /* Obtain the task. */
        Runnable r = null;
        lock.lock();
        try {
            // If the queue is empty, wait for a task.
            if (requestQueue.isEmpty()) {
                isDone = true;
                lock.unlock();
                try {
                    forceNotifyObservers(new SchedulerEventObject(
                            ThreadScheduler.this,
                            SchedulerEvent.ALL_TASKS_FINISHED));
                    
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
            
            if (terminated || Thread.currentThread().isInterrupted()) {
                return false;
            }
            
            // Obtain and remove the task from the queue.
            r = requestQueue.pollFirst();
            
        } catch (InterruptedException e) {
            if (terminated || Thread.currentThread().isInterrupted()) {
                return false;
                
            } else {
                Logger.write(new Object[] {
                    "Interrupted exception was caught in update thread:",
                    e
                }, Logger.Type.ERROR);
            }
            
        } finally {
            lock.unlock();
        }
        
        /* Execute the task. */
        forceNotifyObservers(new SchedulerEventObject(ThreadScheduler.this,
                SchedulerEvent.TASK_STARTED));
        try {
            if (r != null)r.run();
            
            if (terminated || Thread.currentThread().isInterrupted()) {
                return false;
            }
            
        } catch (Exception e) {
            // Note that no interrupts from {@link #interrupt()} from
            // this class can be thrown when the task is executing.
            if (!terminated) {
                Logger.write(new Object[] {
                    "Uncaught exception in " + Thread.currentThread().getName() + ":",
                    e
                }, Logger.Type.ERROR);
            }
        }
        
        forceNotifyObservers(new SchedulerEventObject(ThreadScheduler.this,
                (requestQueue.isEmpty()
                        ? SchedulerEvent.ALL_TASKS_FINISHED
                        : SchedulerEvent.TASK_FINISHED)));
            
        
        return !(terminated || Thread.currentThread().isInterrupted());
    }
    
    /**
     * Internal cleanup when terminated.
     */
    private void cleanup() {
        boolean generateEvent = false;
        lock.lock();
        try {
            generateEvent = !terminated;
            terminated = true;
            requestQueue.clear();
            waitForEmpty.signalAll();
            
        } finally {
            lock.unlock();
            if (generateEvent) {
                forceNotifyObservers(new SchedulerEventObject(this, SchedulerEvent.TERMINATED));
            }
        }
    }
    
    @Override
    public void scheduleTask(Runnable task)
            throws IllegalStateException {
        if (task == null) throw new NullPointerException();
        
        lock.lock();
        try {
            if (!started) throw new IllegalStateException("Not yet started!");
            if (terminated) throw new IllegalStateException("Already terminated!");
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
            if (terminated) {
                throw new IllegalStateException("Already terminated!");
            }
            started = true;
            thread.start();
            
        } finally {
            lock.unlock();
        }
        
        forceNotifyObservers(new SchedulerEventObject(this, SchedulerEvent.STARTED));
    }
    
    @Override
    public void terminate()
            throws InterruptedException {
        lock.lock();
        try {
            cleanup();
            
        } finally {
            lock.unlock();
        }
        
        thread.join();
    }
    
    @Override
    public boolean terminate(long timeout, TimeUnit tu)
            throws InterruptedException {
        lock.lock();
        try {
            cleanup();
            
        } finally {
            lock.unlock();
        }
        
        thread.join(Math.max(1L, tu.toMillis(timeout)));
        return thread.isAlive();
    }
    
    /**
     * {@inheritDoc}
     * 
     * @apiNote
     * Does not terminate if the current executing task is infinitely
     * blocking and does not does not check the interrupted flag of the thread.
     */
    @Override
    public void forceTerminate()
            throws InterruptedException {
        lock.lock();
        try {
            if (!terminated) thread.interrupt();
            cleanup();
            
        } finally {
            lock.unlock();
        }
        
        thread.join();
    }
    
    @Override
    public boolean tryTerminate() {
        lock.lock();
        try {
            cleanup();
            
        } finally {
            lock.unlock();
        }
        
        return thread.isAlive();
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
        if (terminated || isDone) return;
        lock.lock();
        try {
            if (terminated || isDone) return;
            waitForEmpty.await();
            
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * @param priority The new priority of the update thread.
     * 
     * @see Thread#setPriority(int)
     */
    public void setPriority(int priority) {
        thread.setPriority(priority);
    }
    
    /**
     * @return The priority of the update thread.
     * 
     * @see Thread#getPriority()
     */
    public int getPriority() {
        return thread.getPriority();
    }
    
    
}
