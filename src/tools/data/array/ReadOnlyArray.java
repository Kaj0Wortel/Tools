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

package tools.data.array;


// Java imports
import tools.data.Wrapper;


/**
 * Data class for making any array read-only.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class ReadOnlyArray<V>
        extends AttributedArray<V>
        implements ReadArray {
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a read-only array from the given array.
     * 
     * @param arr The backening array of any type.
     */
    public ReadOnlyArray(Object arr) {
        super();
    }
    
    /**
     * Creates a read-only array from the given array.
     * 
     * @param arr The backening array.
     */
    public ReadOnlyArray(V... arr) {
        super(arr);
    }
    
    /**
     * Creates a read-only array from the given array.
     * 
     * @param arr The backening array in a wrapper.
     */
    public ReadOnlyArray(Wrapper<V[]> arr) {
        super(arr);
    }
    
    
    /* -------------------------------------------------------------------------
     * Access functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public V get(int index) {
        return super.get(index);
    }
    
    @Override
    public ReadOnlyArray<V> clone() {
        return new ReadOnlyArray<V>(arr);
    }
    
    
}
