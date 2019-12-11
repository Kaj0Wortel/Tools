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
import java.util.ListIterator;


/**
 * List iterator tool class to easily invert a list iterator.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class InverseListIterator<V>
        implements ListIterator<V> {
     
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The list iterator to invert. */
    private final ListIterator<V> li;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new list iterator which inverts the iteration direction of the list.
     * 
     * @param li The list iterator to invert.
     */
    public InverseListIterator(ListIterator<V> li) {
        this.li = li;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public boolean hasNext() {
        return li.hasPrevious();
    }
    
    @Override
    public V next() {
        return li.previous();
    }
    
    @Override
    public boolean hasPrevious() {
        return li.hasNext();
    }
    
    @Override
    public V previous() {
        return li.next();
    }
    
    @Override
    public int nextIndex() {
        return li.previousIndex();
    }
    
    @Override
    public int previousIndex() {
        return li.nextIndex();
    }
    
    @Override
    public void remove() {
        li.remove();
    }
    
    @Override
    public void set(V e) {
        li.set(e);
    }
    
    @Override
    public void add(V e) {
        li.add(e);
    }
    
    
}
