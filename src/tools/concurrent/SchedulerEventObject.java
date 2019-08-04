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
 * Data object for notifying events for the scheduler classes.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class SchedulerEventObject {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The scheduler the event originated from. */
    private final Scheduler scheduler;
    /** The type of event. */
    private final SchedulerEvent event;
    /** The cause of this event. */
    private final SchedulerEventObject cause;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new event object with the given source scheduler and event cause.
     * 
     * @param scheduler The scheduler the event originated from.
     * @param cause The event object which caused this event.
     */
    public SchedulerEventObject(Scheduler scheduler, SchedulerEventObject cause) {
        this.scheduler = scheduler;
        this.event = cause.getEvent();
        this.cause = cause;
    }
    
    /**
     * Creates a new event object with the given source scheduler and event.
     * 
     * @param scheduler The scheduler the event originated from.
     * @param event The event type.
     */
    public SchedulerEventObject(Scheduler scheduler, SchedulerEvent event) {
        this.scheduler = scheduler;
        this.event = event;
        this.cause = null;
    }
    
    /**
     * Creates a new event object with the given source scheduler and event.
     * 
     * @param scheduler The scheduler the event originated from.
     * @param event The event type.
     * @param cause The event object which caused this event.
     */
    public SchedulerEventObject(Scheduler scheduler, SchedulerEvent event, SchedulerEventObject cause) {
        this.scheduler = scheduler;
        this.event = event;
        this.cause = cause;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * @return The scheduler this event object originated from.
     */
    public Scheduler getScheduler() {
        return scheduler;
    }
    
    /**
     * @return The event type of this object.
     */
    public SchedulerEvent getEvent() {
        return event;
    }
    
    /**
     * @return The event object which caused this event.
     */
    public SchedulerEventObject getCause() {
        return cause;
    }
    
    
}
