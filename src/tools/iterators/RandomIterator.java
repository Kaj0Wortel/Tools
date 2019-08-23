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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * Iterator implementation which returns all elements in a collection or
 * iterable in a random order.
 * 
 * @todo
 * - If possible, add performance for collection (random access)
 * 
 * @version 1.0
 * @author Kaj Wortel
 * 
 * @param <V> The type of the returned values.
 */
public  class RandomIterator<V>
        implements Iterator<V> {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The iterator returning the suffled values.  */
    private Iterator<V> it;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Randomizes the order of the values returned by the iterator of the given iterable.
     * 
     * @param iterable The iterable to randomize.
     */
    public RandomIterator(Iterable<V> iterable) {
        this(iterable.iterator());
    }
    
    /**
     * Randomizes the order of the values returned by the iterator of the given iterable.
     * 
     * @param iterable The iterable to randomize.
     */
    public RandomIterator(Iterator<V> iterator) {
        List<V> list = new ArrayList<V>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        Collections.shuffle(list);
        it = list.iterator();
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
    public V next() {
        return it.next();
    }
    
    @Override
    public void remove() {
        it.remove();
    }
    
    
}
