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

package tools.data;


/**
 * Array data class for preventing an {@link IndexOutOfBoundsException}
 * when requesting non-existing elements from an array.
 * 
 * @author Kaj Wortel
 */
public class DefaultValueArray<V> {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The backening array. */
    private final Wrapper<V[]> arr;
    /** The default value. */
    private final Wrapper<V> def;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a object which can return values from the given array or a default value.
     * 
     * @param data The backening array.
     * @param def The default value.
     */
    public DefaultValueArray(V[] arr, V def) {
        this(new Wrapper<V[]>(arr), def);
    }
    
    /**
     * Creates a object which can return values from the given array or a default value.
     * 
     * @param data The backening array.
     * @param def The default value.
     */
    public DefaultValueArray(Object arr, V def) {
        this(new Wrapper(arr), def);
    }
    
    /**
     * Creates a object which can return values from the given array or a default value.
     * 
     * @param data The backening array in a wrapper.
     * @param def The default value.
     */
    public DefaultValueArray(Wrapper<V[]> arr, V def) {
        this(arr, new Wrapper<V>(def));
    }
    
    /**
     * Creates a object which can return values from the given array or a default value.
     * 
     * @param data The backening array in a wrapper.
     * @param def The default value in a wrapper.
     */
    public DefaultValueArray(Wrapper<V[]> arr, Wrapper<V> def) {
        this.arr = arr;
        this.def = def;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * @param i The index of the element to return.
     * 
     * @return {@code data[i]} if the index is in range. The default value otherwise.
     */
    public V get(int i) {
        if (i < arr.length() && i >= 0) {
            return (V) arr.get(i);
        } else {
            return def.get();
        }
    }
    
    
}
