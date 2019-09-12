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

package tools.iterators;


// Java imports
import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * This abstract class can be used to easily create a new iterator.
 * One only has to provide a single function which will generate the next element,
 * or return {@code null} if there are no more elements.
 * 
 * @author Kaj Wortel
 */
public abstract class GeneratorIterator<E>
        implements Iterator<E> {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The element to be generated next. */
    private E nextElem;
    
    private boolean generated = false;
    /** Denotes whether there still are elements remaining. */
    private boolean done = false;
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public boolean hasNext() {
        if (done) return false;
        if (!generated) {
            nextElem = generateNext();
            generated = true;
        }
        return !done;
    }
    
    @Override
    public E next() {
        if (!hasNext()) throw new NoSuchElementException();
        generated = false;
        return nextElem;
    }
    
    /**
     * This function should be called when there are no more elements available.
     * The {@link #generateNext()} function won't be called after calling this function.
     */
    protected final void done() {
        done = true;
    }
    
    /**
     * Generates the next element. If no more elements exist, then the function
     * {@link #done()} should be called.
     * 
     * @return The next element to be returned.
     */
    protected abstract E generateNext();
    
    
}
