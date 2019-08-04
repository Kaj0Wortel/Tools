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


// Tools imports


// Java imports
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import tools.data.array.ArrayTools;


/**
 * Iterator class for returning all elements in a collection or an
 * iterable at random.
 * 
 * @author Kaj Wortel (0991586)
 * @param <V> the type of the returned classes.
 */
public  class RandomIterator<V>
        implements Iterator<V> {
    
    private Iterator<V> it;
    
    
    /**
     * Constructor.
     * 
     * @param iterable the iterable to create this iterator of.
     */
    public RandomIterator(Iterable<V> iterable) {
        List<V> list = new ArrayList<V>();
        for (V value : iterable) {
            list.add(value);
        }
        Collections.shuffle(list);
        it = list.iterator();
    }
    
    /**
     * @param col the collection to create this iterator of.
     */
    public RandomIterator(Collection<V> col) {
        V[] arr = (V[]) new Object[col.size()];
        int i = 0;
        for (V value : col) {
            arr[i++] = value;
        }
        ArrayTools.shuffle(arr);
        it = new ArrayIterator(arr);
    }
    
    
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
