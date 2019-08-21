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

package tools.data.img.managed;


// Java imports
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;


// Tools imports
import tools.MultiTool;


/**
 * Class for keeping track of the delay for a {@link Token} object.
 *
 * @version 1.0
 * @author Kaj Wortel
 */
class Delay
        implements Delayed {
    
    /* -------------------------------------------------------------------------
     * Constants.
     * -------------------------------------------------------------------------
     */
    /** The time unit of the delay. */
    private static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;
    
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The delay in the time unit {@link #TIME_UNIT}. */
    private final long delay;
    /** The token to time for. */
    private final Token token;
    /** The start time. */
    private final long startTimeMillis = System.currentTimeMillis();
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new delay object from the given data.
     * 
     * @param delay The delay.
     * @param timeUnit The time unit of the delay.
     * @param token The token to time for.
     */
    Delay(long delay, TimeUnit timeUnit, Token token) {
        this.delay = TIME_UNIT.convert(delay, timeUnit);
        this.token = token;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public long getDelay(TimeUnit timeUnit) {
        return timeUnit.convert(delay - (System.currentTimeMillis() - startTimeMillis), TIME_UNIT);
    }

    @Override
    public int compareTo(Delayed delayed) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - delayed.getDelay(TimeUnit.MILLISECONDS));
    }

    /**
     * @return the token represented by this instance.
     */
    public Token getToken() {
        return token;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Delay)) return false;
        return token.equals(((Delay) obj).token);
    }
    
    @Override
    public int hashCode() {
        return MultiTool.calcHashCode(token);
    }
    
    
}
