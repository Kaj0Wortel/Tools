/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) December 2019 by Kaj Wortel - all rights reserved           *
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

package tools.iterators;


// Java imports
import java.util.Iterator;


// Tools imports
import tools.data.Function;


/**
 * Iterator class which converts an iterator of the source type to an iterator
 * of the target type by using a function.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class FunctionIterator<S, T>
        implements Iterator<T> {
     
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The source iterator. */
    private Iterator<S> it;
    /** The function used to convert the source elements to the target elements. */
    private Function<S, T> function;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new function iterator.
     * 
     * @param it The iterator to get the elements from.
     * @param function The function used to convert the elements.
     */
    public FunctionIterator(Iterator<S> it, Function<S, T> function) {
        this.it = it;
        this.function = function;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public boolean hasNext() {
        return it.hasNext();
    }
    
    @Override
    public T next() {
        return function.run(it.next());
    }
    
    
}
