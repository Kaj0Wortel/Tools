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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import tools.MultiTool;


/**
 * TODO: everything
 * 
 * @author Kaj Wortel
 */
public class Wrapper<V> {
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The data to be wrapped */
    private V data;
    /*
    final private static Map<Class, Function> TO_STRING = new HashMap<>();
    static {
        TO_STRING.put(Byte.TYPE     , (Function<byte[]   , String>) Arrays::toString);
        TO_STRING.put(Short.TYPE    , (Function<short[]  , String>) Arrays::toString);
        TO_STRING.put(Integer.TYPE  , (Function<int[]    , String>) Arrays::toString);
        TO_STRING.put(Long.TYPE     , (Function<long[]   , String>) Arrays::toString);
        TO_STRING.put(Float.TYPE    , (Function<float[]  , String>) Arrays::toString);
        TO_STRING.put(Double.TYPE   , (Function<double[] , String>) Arrays::toString);
        TO_STRING.put(Boolean.TYPE  , (Function<boolean[], String>) Arrays::toString);
        TO_STRING.put(Character.TYPE, (Function<char[]   , String>) Arrays::toString);
        TO_STRING.put(Object.class  , (Function<Object[] , String>) Arrays::toString);
    }*/
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Constructor.
     */
    public Wrapper(V data) {
        set(data);
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    public void set(V data) {
        this.data = data;
    }
    
    public V get() {
        return data;
    }
    
    public void set(V value, int pos) {
        Array.set(data, pos, value);
    }
    
    /**
     * Gets an element of an array.  Primitive elements will be wrapped in
     * the corresponding class type.
     *
    * @param array the array to access
    * @param index the array index to access
    * @return the element at <code>array[index]</code>
    * 
    * @throws IllegalArgumentException if <code>array</code> is not an array
    * @throws NullPointerException if <code>array</code> is null
    * @throws ArrayIndexOutOfBoundsException if <code>index</code> is out of bounds
    * 
    * @see #getBoolean(Object, int)
    * @see #getByte(Object, int)
    * @see #getChar(Object, int)
    * @see #getShort(Object, int)
    * @see #getInt(Object, int)
    * @see #getLong(Object, int)
    * @see #getFloat(Object, int)
    * @see #getDouble(Object, int)
    */
    public Object get(int index) {
        if (data instanceof Object[]) return ((Object[]) data)[index];
        if (data instanceof boolean[]) return ((boolean[]) data)[index];
        if (data instanceof byte[]) return ((byte[]) data)[index];
        if (data instanceof char[]) return ((char[]) data)[index];
        if (data instanceof short[]) return ((short[]) data)[index];
        if (data instanceof int[]) return ((int[]) data)[index];
        if (data instanceof long[]) return ((long[]) data)[index];
        if (data instanceof float[]) return ((float[]) data)[index];
        if (data instanceof double[]) return ((double[]) data)[index];
        if (data == null) throw new NullPointerException();
        throw new IllegalArgumentException("Data is not an array!");
    }
    
    /**
     * The length of the array if {@code data} is an array.
     * Throws an {@link IllegalArgumentException} otherwise.
     * 
     * @return the length of the array if {@code data} is an array.
     */
    public int length() {
        return Array.getLength(data);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        Object v1 = data;
        Object v2 = (obj instanceof Wrapper ? ((Wrapper) obj).get() : obj);
        return Objects.deepEquals(v1, v2);
    }
    
    @Override
    public int hashCode() {
        return MultiTool.calcHashCode(data);
    }
    
    @Override
    public String toString() {
        if (data == null) return "null";
        Class<?> c = ((Object) data).getClass();
        if (c.isArray()) {
            return Array.toString(data);
        } else {
            return ((Object) data).toString();
        }
    }
    
    
}
