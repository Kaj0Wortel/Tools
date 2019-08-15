/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) August 2019 by Kaj Wortel - all rights reserved             *
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

package tools.io;


// Java imports
import java.util.EventListener;


/**
 * Listener class for capturing stream closures.
 * 
 * @author Kaj Wortel
 */
@FunctionalInterface
public interface StreamClosedListener
        extends EventListener {
    
    /**
     * This funcion is executed after the stream has been closed.
     * 
     * @param gracefull Denotes whether the stream was closed gracefully,
     *     i.e. by the {@code close()} function of the steam.
     */
    public void streamClosed(boolean gracefull);
    
    
}