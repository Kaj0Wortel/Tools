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

package tools.data;


/**
 * Runnable-like class which allows arguments and return values in the
 * {@code run} function.
 * 
 * @version 1.0
 * @author Kaj Wortel
 * 
 * @param <V> The class of argument of the function.
 * @param <R> The class of return value of the function.
 */
@FunctionalInterface
public interface Function<V, R> {
   
    /**
     * The function to execute.
     * 
     * @param arg The argument of the fuction.
     * 
     * @return The result of the function.
     */
    R run(V arg);
    
    
}
