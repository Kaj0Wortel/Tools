/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) July 2019 by Kaj Wortel - all rights reserved               *
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


// Java imports
import java.util.RandomAccess;


// Tools imports
import tools.MultiTool;
import tools.data.array.ArrayTools;


/**
 * Class for wrapping any object or array (primitive or object).
 * This class can be used to overcome the difficulty of accepting both object
 * and primitive typed arrays. <br>
 * This class can now be used instead of fully copying an object array
 * to a primitive typed array, or vice verca. <br>
 * <br>
 * Additionally, the wrapper maintains no state of the source object. Therefore,
 * you can use it as if it was the reference of the actual source (i.e. no new
 * wrappers are needed when copying objects). <br>
 * <br>
 * <b>Example</b><br>
 * Suppose you have have as input the integer array {@code new int[] {1, 2, 3}},
 * and with it you want to instantiate the following class: <br>
 * <pre>{@code
 * public class MyClass<V> {
 *     private V[] arg;
 *     public MyClass(V[] arg) {
 *         this.arg = arg;
 *     }
 * }}</pre>
 * Now you first have to convert your {@code int[]} array to an {@code Integer[]}.
 * However, by using the wrapper you can transform the class into: <br>
 * <pre>{@code
 * public class MyClass<V> {
 *     private Wrapper<V[]> arg;
 *     public MyClass(Wrapper<V[]> arg) {
 *         this.arg = arg;
 *     }
 * }}</pre>
 * Now you can simply call {@code MyClass<Integer>(new Wrapper(int[] {1, 2, 3}))}
 * to instantiate the class, and no unneeded array copies are needed.
 * 
 * @todo
 * - Add equals function which ignores primitive typed arrays.
 * 
 * @version 1.1
 * @author Kaj Wortel
 */
public class Wrapper<V>
        implements RandomAccess {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The wrapped data. */
    protected V data;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new wrapper for the provided object.
     * 
     * @param data The data to wrap.
     */
    public Wrapper(V data) {
        set(data);
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * @return The wrapped data.
     */
    public V get() {
        return data;
    }
    
    /**
     * Gets an element of the wrapped array object.
     *
    * @param index The array index to access.
    * 
    * @throws NullPointerException If {@code array} is null.
    * @throws ArrayIndexOutOfBoundsException If {@code index} is out of bounds.
    * @throws IllegalArgumentException If the wrapped element is not an array.
    * 
    * @see ArrayTools#get(Object, int)
    */
    public Object get(int index) {
        return ArrayTools.get(data, index);
    }
    
    /**
     * Sets the wrapped object.
     * 
     * @param data The new wrapped object.
     */
    public void set(V data) {
        this.data = data;
    }
    
    /**
     * Replaces the value at the given index of the wrapped array object
     * with the given value.
     * 
     * @param value The new element to set.
     * @param index The index to place the element at.
     * 
     * @throws IllegarAgrumentException If the wrapped object is not an array.
     */
    public void set(Object value, int index) {
        ArrayTools.set(data, index, value);
    }
    
    /**
     * @return {@code true} if the wrapped object is an array.
     *     {@code false} otherwise.
     */
    public boolean isArray() {
        return data != null && data.getClass().isArray();
    }
    
    /**
     * The length of the array if {@code data} is an array. <br>
     * Throws an {@link IllegalArgumentException} otherwise.
     * 
     * @return The length of the array if {@code data} is an array.
     */
    public int length() {
        return ArrayTools.length(data);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Wrapper) {
            return ArrayTools.deepEquals(data, ((Wrapper) obj).data);
            
        } else {
            return ArrayTools.deepEquals(data, obj);
        }
    }
    
    @Override
    public int hashCode() {
        return MultiTool.calcHashCode(data);
    }
    
    /**
     * Returns a string representation of the underlying data object.
     * 
     * @return A string representation of the underlying data object.
     */
    @Override
    public String toString() {
        return ArrayTools.toDeepString(data);
    }
    
    /**
     * Returns the data value of the wrapped object, or {@code null}
     * if the provided wrapper is null.
     * 
     * @param <T> The type of the wrapped data object.
     * 
     * @param wrap The wrapper to unwrap.
     * 
     * @return {@code wrap.get()} if {@code wrap != null}. {@code null} otherwise.
     */
    public static <T> T get(Wrapper<T> wrap) {
        if (wrap == null) return null;
        else return wrap.get();
    }
    
    
}
