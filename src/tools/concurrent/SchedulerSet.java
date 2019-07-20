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
import tools.observer.Observer;


// Java imports
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;


/**
 * TODO: comments
 * 
 * This class provides a fair distribution over multiple schedulers.
 * To do so, it gives tasks to any scheduler which doesn't have a task yet.
 * If none available, then it queue's the task and schedules the task for
 * the first scheduler which comes available.
 * <br>
 * Assumes that the added schedulers implement the observer functions correctly
 * and use the right format, as described in {@link Scheduler.Event}.
 * <br>
 * Has support for easy scheduler creation.
 * 
 * @author Kaj Wortel
 */
public class SchedulerSet<S extends Scheduler>
        extends Scheduler {
    
    final protected Deque<Runnable> requestQueue = new LinkedList<>();
    private S[] schedulers;
    private Condition tasksFinished = lock.newCondition();
    private boolean started = false;
    private boolean terminated = true;
    
    
    final private Observer schedulerObserver = (oi, arg) -> {
        Event e = getEvent(arg);
        
        // Ignore start and terminate events.
        if (e == Event.STARTED || e == Event.TERMINATED) {
            return;
        }
        
        lock.lock();
        try {
            boolean tasksAvailable = !requestQueue.isEmpty();
            boolean isDone = isDone();
            
            // If there are no more tasks running, notify waiting threads.
            if (isDone) {
                tasksFinished.signalAll();
            }
            
            // Propagate event if needed.
            if (e != Event.ALL_TASKS_FINISHED) {
                forceNotifyObservers(arg);
                
            } else if (isDone && !tasksAvailable) {
                forceNotifyObservers(arg);
            }
            
            // Skip event if it is not of the right format or other event type.
            if (e == null || e == Event.OTHER) return;
            
            // Skip if observable is not of the right class.
            if (!(oi instanceof Scheduler)) return;
            Scheduler s = (Scheduler) oi;
            
            // If the task was finished, no new task was available, and there
            // are tasks left, then add a new task to the queue.
            if (e == Event.ALL_TASKS_FINISHED && tasksAvailable) {
                s.scheduleTask(requestQueue.pollFirst());
            }
            
        } finally {
            lock.unlock();
        }
    };
    
    
    /**-------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a scheduler set with {@code amt} schedulers
     * of type {@code clazz}.
     * 
     * @param clazz the class of the schedulers to create.
     * @param amt the amount of schedulers to create.
     * @throws IllegalArgumentException is some exception occured during
     *     the creation of the schedulers.
     * 
     * @see #SchedulerSet(Class, int, Class[], Object[])
     */
    public SchedulerSet(Class<S> clazz, int amt)
            throws IllegalArgumentException {
        this(clazz, amt, null, null);
    }
    
    /**
     * Creates a scheduler set with {@code amt} schedulers
     * of type {@code clazz}. Uses the constructor specified by {@code types}
     * and uses the arguments {@code args} to create the schedulers.
     * 
     * @param clazz the class of the schedulers to create.
     * @param amt the amount of schedulers to create.
     * @param types the type values of the constructor to use.
     * @param args the arguments to use in the constructor.
     * @throws IllegalArgumentException is some exception occured during
     *     the creation of the schedulers.
     */
    public SchedulerSet(Class<S> clazz, int amt,
            Class<?>[] types, Object[] args)
            throws IllegalArgumentException {
        schedulers = (S[]) Array.newInstance(clazz, amt);
        try {
            Constructor<S> c = clazz.getDeclaredConstructor(types);
            for (int i = 0; i < amt; i++) {
                schedulers[i] = c.newInstance(args);
                schedulers[i].addObserver(schedulerObserver);
            }
            
        } catch (IllegalAccessException | InstantiationException |
            InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }
    
    /**
     * Creates a scheduler set of the given schedulers.
     * 
     * @param schedulers the schedulers for this scheduler set.
     *     All elements must be non-null.
     */
    public SchedulerSet(S... schedulers) {
        schedulers = (S[]) new Scheduler[schedulers.length];
        for (int i = 0; i < schedulers.length; i++) {
            if (schedulers[i] == null) {
                Logger.write("Attempted to add null scheduler to "
                        + "scheduler set!", Logger.Type.ERROR);
                throw new NullPointerException();
            }
            this.schedulers[i] = schedulers[i];
            schedulers[i].addObserver(schedulerObserver);
        }
    }
    
    
    /**-------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * @param arg argument to convert to an {@link Event}.
     * @return the {@link Event} represented by {@code arg}, or {@code null}
     *     if it cannot be converted.
     */
    private static Event getEvent(Object arg) {
        if (arg instanceof Event) return (Event) arg;
        
        if (arg == null || !arg.getClass().isArray()) return null;
        Object[] arr = (Object[]) arg;
        if (arr.length < 1 || !(arr[0] instanceof Event)) return null;
        return (Event) arr[0];
    }
    
    @Override
    public void scheduleTask(Runnable task) {
        lock.lock();
        try {
            // Check if there is a free scheduler.
            for (int i = 0; i < schedulers.length; i++) {
                Scheduler s = schedulers[i];
                if (s.isDone()) {
                    s.scheduleTask(task);
                    return;
                }
            }
            
            // No directly available scheduler, so add to request queue
            // and give to first free scheduler.
            requestQueue.addLast(task);
            
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public int queueSize() {
        lock.lock();
        try {
            int sum = 0;
            for (S s : schedulers) {
                sum += s.queueSize();
            }
            return sum + requestQueue.size();
            
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public void start() {
        lock.lock();
        try {
            int i = 0;
            for (S s : schedulers) {
                s.start();
            }
            
        } finally {
            started = true;
            lock.unlock();
            forceNotifyObservers(Event.STARTED);
        }
    }
    
    @Override
    public void terminate()
            throws InterruptedException {
        lock.lock();
        try {
            for (S s : schedulers) {
                s.terminate();
            }
            
        } finally {
            terminated = true;
            lock.unlock();
            forceNotifyObservers(Event.TERMINATED);
        }
    }
    
    @Override
    public boolean terminate(long timeout, TimeUnit tu)
            throws InterruptedException {
        long timeStampPre = System.currentTimeMillis();
        long timeStampPost;
        long remTimeout = timeout;
        boolean rtn = true;
        
        lock.lock();
        try {
            // Notify all schedulers to terminate.
            for (S s : schedulers) {
                s.terminate(0L, TimeUnit.MILLISECONDS);
            }
            
            // Wait for all schedulers to terminate.
            for (S s : schedulers) {
                if (!s.terminate(remTimeout, tu)) rtn = false;
                
                timeStampPost = System.currentTimeMillis();
                remTimeout = Math.max(0, remTimeout
                        - (timeStampPost - timeStampPre));
                timeStampPre = timeStampPost;
            }
            
        } finally {
            terminated = true;
            lock.unlock();
            forceNotifyObservers(Event.TERMINATED);
        }
        
        return rtn;
    }
    
    @Override
    public void forceTerminate()
            throws InterruptedException {
        lock.lock();
        try {
            // Notify all schedulers to terminate.
            for (S s : schedulers) {
                s.terminate(0L, TimeUnit.MILLISECONDS);
            }
            
            for (S s : schedulers) {
                s.forceTerminate();
            }
            
        } finally {
            terminated = true;
            lock.unlock();
            forceNotifyObservers(Event.TERMINATED);
        }
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
        int i = 0;
        for (S s : schedulers) {
            if (!s.isDone()) return false;
        }
        return requestQueue.isEmpty();
    }
    
    @Override
    public void waitUntilDone()
            throws InterruptedException {
        lock.lock();
        try {
            while (!isDone()) {
                tasksFinished.await();
            }
            
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * @return an array containing the schedulers of this scheduler set.
     */
    public S[] getSchedulers() {
        return schedulers;
    }
    
    
}
