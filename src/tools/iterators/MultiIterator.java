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


/**
 * Class that iterates over multiple iterators consecutively.
 * 
 * @author Kaj Wortel
 * 
 * @param <V> The type of the iterators.
 */
public class MultiIterator<V>
        implements Iterator<V> {
    
    /** The iterators. */
    final private Iterator<? extends V>[] its;
    /** Iterator counter. */
    private int counter = 0;
    
    public MultiIterator(Iterator<? extends V>... its) {
        if (its == null) throw new NullPointerException("Null iterator!");
        this.its = its;
    }
    
    @Override
    public boolean hasNext() {
        boolean hasNext = false;
        while (counter < its.length && !(hasNext = its[counter].hasNext())) {
            counter++;
        }
        
        return hasNext;
    }
    
    @Override
    public V next() {
        if (!hasNext()) throw new NoSuchElementException(
                "No more elements!");
        return its[counter].next();
    }
    
    
}