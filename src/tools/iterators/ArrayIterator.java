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

package tools.iterators;


// Java imports
import java.util.Iterator;
import java.util.NoSuchElementException;


// Tools imports
import tools.data.array.ArrayTools;
import tools.data.Wrapper;


/**
 * Simple class that iterates over an array.
 * Has no modification checks, hence the returned values will differ
 * when the backening array is modified. Therefore, the behaviour of
 * this iterator will be undefined if the backening array array is modified
 * after the iterator has been created.
 * 
 * @todo
 * - Add support for iterating over partial arrays (offset + length).
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class ArrayIterator<V>
        implements Iterator<V> {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The wrapped backening array. */
    private final Wrapper<V[]> array;
    /** The index counter of the array. */
    private int index = 0;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new iterator over the given wrapped array.
     * 
     * @param array The wrapped array to iterate over.
     */
    public ArrayIterator(Wrapper<V[]> array) {
        if (array == null || array.get() == null) {
            throw new NullPointerException();
        }
        this.array = array;
    }
    
    /**
     * Creates a new iterator over the given array.
     * 
     * @param array The array to iterate over.
     */
    public ArrayIterator(V[] array) {
        if (array == null) throw new NullPointerException();
        this.array = new Wrapper<V[]>(array);
    }
    
    /**
     * Creates a new iterator over the given array.
     * This function should be used as entrypoint for primitive typed arrays. <br>
     * <b>Caution:</b><br>
     * Initializing the iterator with the wrong type will only be noticed
     * while iterating, not when initializing.
     * 
     * @param array The primitive array to iterate over.
     */
    public ArrayIterator(Object array) {
        if (array == null) throw new NullPointerException();
        if (!array.getClass().isArray()) {
            throw new IllegalArgumentException("Expected an array, but found: " + array);
        }
        this.array = new Wrapper(array);
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public boolean hasNext() {
        return index < ArrayTools.length(array.get());
    }
    
    @Override
    public V next() {
        if (!hasNext())
            throw new NoSuchElementException("No next element available! "
                    + "(length = " + array.get().length + ").");
        return (V) array.get(index++);
    }
    
    @Override
    public void remove() {
        array.set(null, index - 1);
    }
    
    
}
