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


/**
 * Class that iterates over multiple iterators.
 * 
 * @version 01.0
 * @author Kaj Wortel
 * 
 * @param <V> The type of the iterators.
 */
public class MultiIterator<V>
        extends GeneratorIterator<V> {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The iterators to iterate over. */
    private final Iterator<? extends V>[] its;
    /** Counter of the iterator to use. */
    private int counter = 0;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new iterator over multiple iterators.
     * The iterators are iterated consecutively from the lowest index
     * towards the higest index, and each iterator is fully iterated before
     * iterating the next one.
     * 
     * @param its The iterators to iterate over.
     */
    public MultiIterator(Iterator<? extends V>... its) {
        if (its == null) throw new NullPointerException("Null iterator!");
        this.its = its;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    protected V generateNext() {
        while (counter < its.length && !its[counter].hasNext()) {
            counter++;
        }
        if (counter < its.length) return its[counter].next();
        done();
        return null;
    }
    
    
}