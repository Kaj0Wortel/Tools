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
import tools.observer.ObsInterface;
import tools.observer.Observer;


// Java imports
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;


/**
 * TODO: comments
 * 
 * @author Kaj Wortel
 */
public abstract class Scheduler
        implements ObsInterface {
    /**
     * Enum for observer events.
     * 
     * The general structure of the argument for the observers is:<br>
     * "{@code new Object[] {Event, args}}"<br>
     * or<br>
     * "{@code Event}"
     */
    public static enum Event {
        /**
         * This event should be used when the scheduler was started using
         * the {@link #start()} function.
         */
        STARTED,
        /**
         * This event should be used when the scheduler was terminated using
         * the {@link #terminate()} function.
         */
        TERMINATED,
        /**
         * This event should be used when a new task starts its execution.
         */
        TASK_STARTED,
        /**
         * This event should be used when a task is finished.
         */
        TASK_FINISHED,
        /**
         * This event should be used when a task is finished and there are
         * no more remaining tasks left.
         * Note that {@link #TASK_FINISHED} should be fired before this event.
         */
        ALL_TASKS_FINISHED,
        /**
         * This event should be used in other, user defined expansions.
         */
        OTHER;
    }
    
    final protected ReentrantLock lock = new ReentrantLock();
    
    // List containing all observers. The CopyOnWriteArrayList is used
    // because it automatically achieves synchronization by copying the
    // array when modified.
    final private List<Observer> obs = new CopyOnWriteArrayList<>();
    
    // Whether there was a modification.
    protected boolean modified = false;
    
    
    /**
     * Schedules a task for execution.
     * 
     * @param task the task to execute.
     * @throws IllegalStateException if the scheduler was not yet started
     *     or if it is terminated.
     * 
     * @see #start()
     * @see #isStarted()
     * @see #terminate()
     * @see #isTerminated()
     */
    public abstract void scheduleTask(Runnable task)
            throws IllegalStateException;
    
    /**
     * @return the queueSize of the current request queue.
     * 
     * Note that the task which is currently being executed is NOT
     * counted here.
     */
    public abstract int queueSize();
    
    /**
     * Starts the scheduler.
     * 
     * @see #terminate()
     * @see #isStarted()
     */
    public abstract void start()
            throws IllegalStateException;
    
    /**
     * Terminates the scheduler.
     * After this call, no new tasks can be scheduled with this scheduler.
     * A new instance has to be created to schedule new tasks.
     * The scheduler will only finish tasks it already started, but not
     * tasks which were added to the queue but not yet started.
     * <br>
     * Blocks until the scheduler has terminated completely.
     * 
     * @throws IllegalStateException if the scheduler was already terminated.
     * 
     * @see #terminate(long, TimeUnit)
     * @see #forceTerminate()
     * @see #isTerminated()
     */
    public void terminate()
            throws InterruptedException {
        terminate(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Terminates the scheduler.
     * After this call, no new tasks can be scheduled with this scheduler.
     * A new instance has to be created to schedule new tasks.
     * The scheduler will only finish tasks it already started, but not
     * tasks which were added to the queue but not yet started.
     * <br>
     * Blocks until either the scheduler has terminated completely or
     * the timeout has passed.
     * 
     * @param timeout the time to wait at most before returning, or 
     * @param tu the time unit of {@code timeout}.
     * @return {@code true} if the scheduler was finished.
     *     {@code false} otherwise.
     * @throws InterruptedException if the current thread was interrupted.
     * 
     * @see #terminate()
     * @see #forceTerminate()
     * @see #isTerminated()
     */
    public abstract boolean terminate(long timeout, TimeUnit tu)
            throws InterruptedException;
    
    /**
     * Forcefully terminates the scheduler. If a task is being executed, then
     * it will be interrupted.
     * <br>
     * Blocks until the scheduler has terminated completely.
     * 
     * @throws InterruptedException if the current thread was interrupted.
     * 
     * @see #terminate()
     * @see #terminate(long, TimeUnit)
     * @see #isTerminated()
     */
    public void forceTerminate()
            throws InterruptedException {
        terminate();
    }
    
    /**
     * return {@code true} if the scheduler is started.
     *     {@code false} otherwise.
     * 
     * The result of this function should not depend on the result of
     * {@link #isTerminated()}, so a scheduler which is started and then
     * terminated should still return {@code true}.
     * 
     * @see #isTerminated()
     * @see #isActive()
     */
    public abstract boolean isStarted();
    
    /**
     * @return {@code true} if the scheduler is terminated.
     *     {@code false} otherwise.
     * 
     * @see #isStarted()
     * @see #isActive()
     */
    public abstract boolean isTerminated();
    
    /**
     * @return {@code true} if the scheduler accepts tasks, i.e. it is
     *     started and not terminated.
     * 
     * @see #isStarted()
     * @see #isTerminated()
     */
    public boolean isActive() {
        return isStarted () && !isTerminated();
    }
    
    /**
     * @return {@code true} if there are no tasks in the queue and no task
     *     is being executed. Undefined when
     *     {@link #isTerminated()} {@code == true}. {@code false} otherwise.
     */
    public abstract boolean isDone();
    
    /**
     * Waits until the entire queue is empty.
     * 
     * @throws InterruptedException if the current thread was interrupted.
     */
    public abstract void waitUntilDone()
            throws InterruptedException;
    
    
    /**-------------------------------------------------------------------------
     * Observer functions.
     * -------------------------------------------------------------------------
     */
    
    @Override
    public void addObserver(Observer o) {
        obs.add(o);
    }
    
    @Override
    public void deleteObserver(Observer o) {
        obs.remove(o);
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see #notifyObservers(Object)
     */
    @Override
    public void notifyObservers() {
        notifyObservers(null);
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see #forceNotifyObservers(Object)
     */
    @Override
    public void notifyObservers(Object arg) {
        boolean mod;
        lock.lock();
        try {
            mod = modified;
            clearChanged();
            
        } finally {
            lock.unlock();
        }
        
        if (mod) {
            forceNotifyObservers(arg);
        }
    }
    
    /**
     * Forces the notification of the observers.
     */
    public void forceNotifyObservers() {
        forceNotifyObservers(null);
    }
    
    /**
     * Forces the notification of the observers.
     * 
     * @param arg the argument for the observers.
     */
    public void forceNotifyObservers(Object arg) {
        obs.forEach(observer -> observer.update(this, arg));
    }
    
    @Override
    public void deleteObservers() {
        obs.clear();
    }
    
    @Override
    public void setChanged() {
        lock.lock();
        try {
            modified = true;
            
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public void clearChanged() {
        lock.lock();
        try {
            modified = false;
            
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public boolean hasChanged() {
        return modified;
    }
    
    @Override
    public int countObservers() {
        return obs.size();
    }
    
    
}
