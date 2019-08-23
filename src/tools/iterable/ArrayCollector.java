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

package tools.iterable;


// Tools imports
import tools.data.Wrapper;
import tools.iterators.ArrayIterator;
import tools.iterators.MultiIterator;


// Java imports
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Collector class for creating an iterator over multiple arrays
 * and elements.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class ArrayCollector<V>
        implements Iterable<V> {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** List containing all data so far. */
    private final List<Wrapper<V[]>> data = new ArrayList<>();
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Appends the element to the end of the list.
     * 
     * @param part The item to append.
     */
    public void appendItem(V part) {
        data.add(new Wrapper(new Object[] {part}));
    }
    
    /**
     * Appends a wrapped element to the end of the list.
     * 
     * @param part The item to append.
     */
    public void appendItem(Wrapper<V> part) {
        if (part == null) throw new NullPointerException();
        data.add(new Wrapper(new Object[] {part.get()}));
    }
    
    /**
     * Adds the given parts to the list.
     * 
     * @param part The part to be added.
     */
    public void append(V[] part) {
        if (part == null) throw new NullPointerException();
        data.add(new Wrapper<V[]>(part));
    }
    
    /**
     * Appends all elements in the array in the wrapper to the list.
     * 
     * @param part The elements to append.
     */
    public void append(Wrapper<V[]> part) {
        if (part == null) throw new NullPointerException();
        data.add(part);
    }
    
    @Override
    public Iterator<V> iterator() {
        ArrayIterator<V>[] its = new ArrayIterator[data.size()];
        for (int i = 0; i < its.length; i++) {
            its[i] = new ArrayIterator(data.get(i));
        }
        
        return new MultiIterator(its);
    }
    
    
}
