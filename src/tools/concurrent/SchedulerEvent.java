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


/**
 * Enum for scheduler observer events.
 * 
 * The general structure of the argument for the observers is:<br>
 * "{@code new Object[] {Event, args}}"<br>
 * or<br>
 * "{@code Event}"
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public enum SchedulerEvent {
    
    /**
     * This event should be used when the scheduler was started.
     * This event will always be the first event of the scheduler.
     */
    STARTED,
    
    /**
     * This event should be used when the scheduler was terminated.
     * This event will always be the last event which is produced from
     * a scheduler.
     */
    TERMINATED,
    
    /**
     * This event should be used when a new task starts its execution.
     * Any task which is accepted into the queue must produce this
     * event when it is started.
     */
    TASK_STARTED,
    
    /**
     * This event should be used when a task is finished.
     * Every task which was started must produce either this event or
     * {@link #ALL_TASKS_FINISHED} when it finishes. The only exception
     * is when the scheduler is terminated. In this case, it is allowed
     * to produce either events if the {@link #TERMINATED} event has
     * not yet been produced.
     * 
     * @see #ALL_TASKS_FINISHED
     */
    TASK_FINISHED,
    
    /**
     * This event should be used when a task is finished and there are
     * no more remaining tasks left in the queue.
     * This event is an extension of {@link #TASK_FINISHED}. It
     * additionally requires the task queue to be empty to be produced.
     * This event replaces the {@link #TASK_FINISHED} event.
     * 
     * @see #TASK_FINISHED
     */
    ALL_TASKS_FINISHED,
    
    /**
     * This event should be used in other, user defined expansions.
     * The default implementation for this for an observable which is
     * also observing a scheduler is to notify it's oveservers by
     * wrapping this event.
     */
    OTHER;
    
    
}