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

// Tool imports
import tools.data.Array;
import tools.data.Wrapper;


/**
 * Simple class that iterates over an array.
 * Has no modification checks.
 * 
 * @author Kaj Wortel
 */
public class ArrayIterator<V>
        implements Iterator<V> {
    final private Wrapper<V[]> array;
    private int i = 0;
    
    public ArrayIterator(Wrapper<V[]> array) {
        this.array = array;
    }
    
    public ArrayIterator(V[] array) {
        this.array = new Wrapper<V[]>(array);
    }
    
    @Override
    public boolean hasNext() {
        return i < Array.getLength(array.get());
    }
    
    @Override
    public V next() {
        if (!hasNext())
            throw new NoSuchElementException("No next element available! "
                    + "(length = " + array.get().length + ").");
        return (V) array.get(i++);
    }
    
    @Override
    public void remove() {
        array.get()[i - 1] = null;
    }
    
    
}
