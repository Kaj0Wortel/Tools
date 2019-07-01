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


/**DONE
 * Enum for how to handle the frame rate for timers.
 * 
 * @author Kaj Wortel
 */
public enum FPSState {
    
    /**
     * The timer will attempt to execute the function once every interval.
     * If the previous function was still running, then the current one
     * will be placed in the queue. If there is already a function in
     * the queue, then the current function will be discarded.
     */
    MANUAL,
    
    /**
     * The same as {@link #MANUAL}, but if the function was added to the
     * queue or discarded, then the interval will be increased.
     * If, after a while, no collisions occured, then the interval will be
     * slowly reduced towards the original value.
     */
    AUTO;
    
    
}
