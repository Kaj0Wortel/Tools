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


// Tools imports
import tools.data.Wrapper;


/**
 * Array extension class implementation which takes the get and set indices modulo
 * the length of the array.
 * <h2>Examples</h2>
 * Assume that we have the following array.
 * <blockquote><pre>{@code
 * CycleArray cycle = new CycleArray(new int[] {0, 1, 2});
 * }</pre></blockquote>
 * Then it holds that <br>
 * <blockquote><pre>{@code
 * cycle.get(0) == cycle.get(3) == cycle.get(-3) == cycle.get(0 + 3n) == 0
 * cycle.get(1) == cycle.get(4) == cycle.get(-2) == cycle.get(1 + 3n) == 1
 * cycle.get(1) == cycle.get(5) == cycle.get(-1) == cycle.get(2 + 3n) == 2
 * }</pre></blockquote>
 * 
 * @version 1.1
 * @author Kaj Wortel
 */
public class CycleArray<V>
        extends AttributedArray<V>
        implements ReadWriteArray<V> {
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates an attributed array from the given array.
     * 
     * @param arr The backening array of any type.
     */
    public CycleArray(Object arr) {
        super(arr);
    }
    
    /**
     * Creates a attributed array from the given array.
     * 
     * @param arr The backening array.
     */
    public CycleArray(V... arr) {
        super(arr);
    }
    
    /**
     * Creates a attributed array from the given array.
     * 
     * @param arr The backening array in a wrapper.
     * 
     * @throws NullPointerException If {@code array == null}.
     * @throws IllegalArgumentException If {@code array} is not an array.
     */
    public CycleArray(Wrapper<V[]> arr) {
        super(arr);
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public V get(int index) {
        return super.get(mod(index));
    }
    
    @Override
    public V set(V value, int index) {
        return super.set(value, mod(index));
    }
    
    /**
     * Calcualtes the index modulo the length of the array, or {@code index mod length()}.
     * 
     * @param index The index to process.
     * 
     * @return The index modulo the length of the array.
     */
    private int mod(int index) {
        int i = index % length();
        if (i < 0) i += length();
        return i;
    }
    
    @Override
    public CycleArray clone() {
        return new CycleArray(array);
    }
    
    
}
