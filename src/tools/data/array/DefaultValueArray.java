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
import java.util.Objects;


// Tools Imports
import tools.MultiTool;
import tools.data.Wrapper;


/**
 * Array data class for preventing an {@link IndexOutOfBoundsException}
 * when requesting non-existing elements from an array.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class DefaultValueArray<V>
        extends ReadOnlyArray<V> {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The default value. */
    protected final Wrapper<V> def;
    
    
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
        super(arr);
        this.def = def;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * @param index The index of the element to return.
     * 
     * @return The element at the given index if the index is in range.
     *     The default value otherwise.
     */
    @Override
    public V get(int index) {
        if (index < arr.length() && index >= 0) {
            return (V) arr.get(index);
        } else {
            return def.get();
        }
    }
    
    @Override
    public int hashCode() {
        return MultiTool.calcHashCode(new Object[] {arr, def});
    }
    
    @Override
    public String toString() {
        return getClass().getName()
                + "[def=" + def.toString()
                + ",arr=" + arr.toString() + "]";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof DefaultValueArray)) return false;
        DefaultValueArray dva = (DefaultValueArray) obj;
        return this.def == dva.def && Objects.equals(this.arr, dva.arr);
    }
    
    
}
