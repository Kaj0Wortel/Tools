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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;


// Tools imports
import tools.log.Logger;
import tools.observer.ObsInterface;
import tools.observer.Observer;


/**
 * Abstract class for task distribution handlers which can queue and execute tasks. <br>
 * A scheduled task can be executed at any point in time <b>directly</b> after invoking
 * the {@link #scheduleTask(Runnable)} function, and can be executed on any thread,
 * including the current thread. <br>
 * Note that this implies that the task can be executed not only on one or more
 * threads, but also directly after invoking, but not yet returning from the
 * {@link #scheduleTask(Runnable)} function and on the current thread. <br>
 * <br>
 * All functions of any scheduler implementation must be thread-safe. <br>
 * <br>
 * If a task throws an exception, the this exception must be logged using the
 * {@link tools.log.Logger} class.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public abstract class Scheduler
        implements ObsInterface<Scheduler, SchedulerEventObject> {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The lock used by this scheduler. */
    protected final ReentrantLock lock = new ReentrantLock();
    
    /**
     * List containing all observers. The CopyOnWriteArrayList is used
     * because it automatically achieves synchronization by copying the
     * array when modified.
     */
    private final List<Observer<Scheduler, SchedulerEventObject>> obs
            = new CopyOnWriteArrayList<>();
    
    /** Whether there was a modification. */
    protected boolean modified = false;
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Schedules a task for execution.
     * 
     * @param task The task to execute.
     * 
     * @throws IllegalStateException If the scheduler was not yet started
     *     or if it is terminated.
     * @throws NullPointerException If {@code task == null}.
     * 
     * @see #start()
     * @see #isStarted()
     * @see #terminate()
     * @see #isTerminated()
     */
    public abstract void scheduleTask(Runnable task)
            throws IllegalStateException, NullPointerException;
    
    /**
     * @return the queueSize of the current request queue.
     * 
     * @apiSpec
     * The task which is currently being executed is NOT counted here.
     */
    public abstract int queueSize();
    
    /**
     * Starts the scheduler.
     * This function only returns when the scheduler is started. This <b>does</b>
     * mean that all initial configurations are done (i.e. tasks will be queued),
     * but this does <b>not</b> mean that the first task will be processed directly.
     * 
     * @throws IllegalStateException If the scheduler was already started or
     *     terminated, or could not be started.
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
     * tasks which were added to the queue but not yet started. <br>
     * <br>
     * Blocks until the scheduler has terminated completely.
     * 
     * @throws IllegalStateException If the scheduler was already terminated.
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
     * Terminates the scheduler. <br>
     * After this call, no new tasks can be scheduled with this scheduler,
     * even if the call results in {@code false}.
     * A new instance has to be created to schedule new tasks.
     * The scheduler will only finish tasks it already started, but not
     * tasks which were added to the queue but not yet started. <br>
     * <br>
     * Blocks until either the scheduler has terminated completely or
     * the timeout has passed.
     * 
     * @param timeout The time to wait at most before returning.
     * @param tu The time unit of {@code timeout}.
     * 
     * @return {@code true} if the scheduler is terminated. {@code false} otherwise.
     * 
     * @throws InterruptedException If the current thread was interrupted.
     * 
     * @see #terminate()
     * @see #forceTerminate()
     * @see #isTerminated()
     */
    public abstract boolean terminate(long timeout, TimeUnit tu)
            throws InterruptedException;
    
    /**
     * Forcefully terminates the scheduler.
     * If a task is being executed, then it will be interrupted. <br>
     * <br>
     * Blocks until the scheduler has terminated completely.
     * 
     * @throws InterruptedException If the current thread was interrupted.
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
     * Tries to terminate the scheduler without waiting. If this is not possible,
     * then the termination process will be invoked, but not on the current thread.
     * 
     * @ImplSpec
     * The {@link SchedulerEvent#TERMINATED} will be fired when the scheduler is.
     * 
     * @return {@code true} if the scheduler could be terminated without waiting.
     *     {@code false} otherwise.
     */
    public boolean tryTerminate() {
        if (isTerminated()) return true;
        new Thread() {
            @Override
            public void run() {
                try {
                    terminate();
                    
                } catch (InterruptedException e) {
                    Logger.write(e);
                }
            }
        }.start();
        return false;
    }
    
    /**
     * Checks whether the scheduler is started.
     * 
     * @implSpec
     * This function should always return {@code true} after the {@link #start()}
     * function has been invoked <b>and</b> completed, and {@code false} in
     * all other cases.
     * 
     * @return {@code true} if the scheduler is started.
     *     {@code false} otherwise.
     * 
     * @see #isTerminated()
     * @see #isActive()
     */
    public abstract boolean isStarted();
    
    /**
     * Checks whether the scheduler is terminated.
     * 
     * @implSpec
     * This function should always return {@code true} after the {@link #terminate()},
     * {@link #terminate(long, TimeUnit)} or {@link #forceTerminate()} functions
     * have been invoked <b>and</b> completed, and {@code false} in all other cases.
     * 
     * @return {@code true} if the scheduler is terminated.
     *     {@code false} otherwise.
     * 
     * @see #isStarted()
     * @see #isActive()
     */
    public abstract boolean isTerminated();
    
    /**
     * Checks whether the scheduler is active.
     * 
     * @implSpec
     * This function should always return {@code true} when jobs can be added
     * to the queue, and {@code false} otherwise. By default, this function
     * returns {@code true} when the scheduler has been started and not yet terminated.
     * 
     * @return {@code true} if the scheduler accepts tasks. {@code false} otherwise.
     * 
     * @see #isStarted()
     * @see #isTerminated()
     */
    public boolean isActive() {
        return isStarted () && !isTerminated();
    }
    
    /**
     * Checks whether the scheduler has no more tasks left.
     * 
     * @return {@code true} if there are no tasks in the queue and no task
     *     is being executed. Undefined when
     *     {@link #isTerminated()} {@code == true}. {@code false} otherwise.
     */
    public abstract boolean isDone();
    
    /**
     * Waits until the entire queue is empty.
     * This function will also return when the scheduler is terminated.
     * 
     * @throws InterruptedException If the current thread was interrupted.
     */
    public abstract void waitUntilDone()
            throws InterruptedException;
    
    
    /* -------------------------------------------------------------------------
     * Observer functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public void addObserver(Observer<Scheduler, SchedulerEventObject> o) {
        obs.add(o);
    }
    
    @Override
    public void deleteObserver(Observer<Scheduler, SchedulerEventObject> o) {
        obs.remove(o);
    }
    
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
    public void notifyObservers(SchedulerEventObject arg) {
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
     * Forces the notification of the observers. <br>
     * The modification flag is not checked nor modified,
     * and all observers are notified regardles.
     */
    public void forceNotifyObservers() {
        forceNotifyObservers(null);
    }
    
    /**
     * Forces the notification of the observers.
     * The modification flag is not checked nor modified,
     * and all observers are notified regardles.
     * 
     * @param arg The argument for the observers.
     */
    public void forceNotifyObservers(SchedulerEventObject arg) {
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
