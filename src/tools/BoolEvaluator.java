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

package tools;


/**
 * Comparable-like interface that returns a {@code boolean}
 * instead of an {@code int}.
 *
 * @param <V> The value class to evaluate.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
@FunctionalInterface
public interface BoolEvaluator<V> {
    
    /**
     * Evaluates the expression.
     * 
     * @param v The value to evaluate.
     * 
     * @return {@code true} if the expression evaluates to {@code true}.
     *     {@code false} otherwise.
     */
    public boolean evaluate(V v);
    
    
}