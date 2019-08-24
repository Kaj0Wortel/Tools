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
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;


// Tools imports
import tools.observer.ToolObserver;


/**
 * This class provides a fair time-wise distribution over multiple schedulers. <br>
 * To do so, it gives tasks to any scheduler which doesn't have a task yet.
 * If none available, then it queue's the task and schedules the task for
 * the first scheduler which comes available.
 * <br>
 * Has support for easy scheduler creation using reflection.
 * 
 * @todo testing
 * 
 * @apiNote
 * This class assumes that the added schedulers implement the observer functions
 * correctly and use the right format, as described in {@link SchedulerEvent}.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class SchedulerSet<S extends Scheduler>
        extends Scheduler {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** Queue containing the requests to be executed. */
    protected final Deque<Runnable> requestQueue = new LinkedList<>();
    /** The schedulers to be used for scheduling tasks. */
    private S[] schedulers;
    
    /** The condition used to signal that all tasks might be finished. */
    private Condition tasksFinished = lock.newCondition();
    /** The condition used to signal that all schedulers are terminated. */
    private Condition allTerminated = lock.newCondition();
    
    /** Whether the scheduler set was started. */
    private boolean started = false;
    /** Whether there are no more tasks available. */
    private boolean isFinished = false;
    /** Whether the scheduler set was terminated. */
    private boolean terminated = true;
    
    /**
     * The Observer used for observing when a scehduler has finished it's tasks.
     * This Observer should be added to all schedulers which can be used
     * for scheduling tasks by this class.
     */
    private final ToolObserver<Scheduler, SchedulerEventObject> schedulerObserver = (s, e) -> {
        if (terminated) return;
        SchedulerEvent event = e.getEvent();
        
        // Ignore events.
        if (event == null || event == SchedulerEvent.STARTED)  {
            return;
        }
        
        if (event == SchedulerEvent.OTHER) {
            forceNotifyObservers(new SchedulerEventObject(this, e));
            
        } else if (event == SchedulerEvent.TERMINATED) {
            lock.lock();
            try {
                for (int i = 0; i < schedulers.length; i++) {
                    if (!schedulers[i].isTerminated()) return;
                }
                
                terminated = true;
                allTerminated.signalAll();
                
            } finally {
                lock.unlock();
            }
            forceNotifyObservers(new SchedulerEventObject(this, SchedulerEvent.TERMINATED));
            
        } else if (event == SchedulerEvent.TASK_FINISHED || event == SchedulerEvent.ALL_TASKS_FINISHED) {
            boolean isDone = false;
            lock.lock();
            try {
                isDone = isDone();
                
                // If there are no more tasks running, notify waiting threads.
                if (isDone) {
                    tasksFinished.signalAll();
                }
                
                if (!requestQueue.isEmpty() && s != null && s.isDone()) {
                    s.scheduleTask(requestQueue.pollFirst());
                }
                
            } finally {
                lock.unlock();
            }
            forceNotifyObservers(new SchedulerEventObject(this, (isDone
                    ? SchedulerEvent.ALL_TASKS_FINISHED
                    : SchedulerEvent.TASK_FINISHED), e));
            
        } else {
            throw new IllegalStateException();
        }
    };
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a scheduler set with {@code amt} schedulers
     * of type {@code clazz}.
     * 
     * @param clazz The class of the schedulers to create.
     * @param amt The amount of schedulers to create.
     * 
     * @throws IllegalArgumentException If some exception occured during
     *     the creation of the schedulers, or if the amount is less or equal
     *     to {@code 0}.
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
     * @param clazz The class of the schedulers to create.
     * @param amt The amount of schedulers to create. Must be at least {@code 1}.
     * @param types The type values of the constructor to use.
     * @param args The arguments to use in the constructor.
     * 
     * @throws IllegalArgumentException If some exception occured during
     *     the creation of the schedulers, or if the amount is less or equal
     *     to {@code 0}.
     */
    public SchedulerSet(Class<S> clazz, int amt, Class<?>[] types, Object[] args)
            throws IllegalArgumentException {
        if (amt <= 0) {
            throw new IllegalArgumentException(
                    "Expected an amount > 1, but found: " + amt);
        }
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
     * @apiNote
     * The array is cloned to prevent accidental outside modifications.
     * 
     * @param schedulers The schedulers for this scheduler set.
     *     All elements must be non-null. Must have a length of at least 1.
     * 
     * @throws IllegalStateException If the length of the array is {@code 0}.
     * @throws NullPointerException If the provided array or one of its
     *     elements is {@code null}.
     */
    public SchedulerSet(S... schedulers)
            throws IllegalStateException, NullPointerException {
        // Error checking.
        if (schedulers == null) {
            throw new NullPointerException("Schedulers array was null!");
        }
        if (schedulers.length == 0) {
            throw new IllegalArgumentException(
                    "Expected a length > 0, but found: " + schedulers.length);
        }
        
        // Copy schedulers.
        schedulers = (S[]) new Scheduler[schedulers.length];
        for (int i = 0; i < schedulers.length; i++) {
            // Error checking.
            if (schedulers[i] == null) {
                throw new NullPointerException(
                        "Scheduler " + (i + 0) + " was null!");
            }
            
            // Compy scheduler.
            this.schedulers[i] = schedulers[i];
            // Add observer.
            schedulers[i].addObserver(schedulerObserver);
        }
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public void scheduleTask(Runnable task) {
        if (task == null) throw new NullPointerException();
        lock.lock();
        try {
            isFinished = false;
            // Check if there is a free scheduler.
            for (int i = 0; i < schedulers.length; i++) {
                Scheduler s = schedulers[i];
                if (s.isDone()) {
                    s.scheduleTask(task);
                    return;
                }
            }
            
            // No available scheduler, so add to request queue
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
    public void start()
            throws IllegalStateException {
        lock.lock();
        try {
            int i = 0;
            for (S s : schedulers) {
                s.start();
            }
            
        } finally {
            started = true;
            lock.unlock();
            forceNotifyObservers(new SchedulerEventObject(this, SchedulerEvent.STARTED));
        }
    }
    
    @Override
    public void terminate()
            throws InterruptedException {
        lock.lock();
        try {
            for (S s : schedulers) {
                s.tryTerminate();
            }
            allTerminated.await();
            
        } finally {
            terminated = true;
            lock.unlock();
        }
    }
    
    @Override
    public boolean terminate(long timeout, TimeUnit tu)
            throws InterruptedException {
        boolean rtn = true;
        
        lock.lock();
        try {
            // Notify all schedulers to terminate.
            for (S s : schedulers) {
                s.tryTerminate();
            }
            
            allTerminated.await(timeout, tu);
            
        } finally {
            terminated = true;
            lock.unlock();
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
                s.forceTerminate();
            }
            
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public boolean tryTerminate() {
        lock.lock();
        try {
            boolean directTerm = false;
            for (Scheduler s : schedulers) {
                if (!s.tryTerminate()) directTerm = false;
            }
            return directTerm;
            
        } finally {
            lock.unlock();
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
        lock.lock();
        try {
            if (isFinished) return true;
            if (!requestQueue.isEmpty()) return false;
            for (int i = 0; i < schedulers.length; i++) {
                if (!schedulers[i].isDone()) return false;
            }
            return true;
            
        } finally {
            lock.unlock();
        }
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
