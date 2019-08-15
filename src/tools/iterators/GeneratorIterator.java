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
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public boolean hasNext() {
        if (nextElem != null) return true;
        return (nextElem = generateNext()) != null;
    }
    
    @Override
    public E next() {
        if (!hasNext()) throw new NoSuchElementException();
        E elem = nextElem;
        nextElem = null;
        return elem;
    }
    
    /**
     * Generates the next element.
     * 
     * @return The next element to be returned, or {@code null} of no such element exists.
     */
    protected abstract E generateNext();
    
    
}
