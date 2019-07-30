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
import java.util.Objects;


// Tools imports
import tools.MultiTool;


/**
 * TODO: everything
 * - clone
 * 
 * @author Kaj Wortel
 */
public class Wrapper<V> {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The data to be wrapped */
    protected V data;
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
        ArrayTools.set(data, pos, value);
    }
    
    /**
     * Gets an element of an array.  Primitive elements will be wrapped in
     * the corresponding class type.
     *
    * @param array The array to access
    * @param index The array index to access
    * @throws NullPointerException If <code>array</code> is null
    * @throws ArrayIndexOutOfBoundsException If <code>index</code> is out of bounds
    * 
    * @see ArrayTools#get(Object, int)
    */
    public Object get(int index) {
        return ArrayTools.get(data, index);
    }
    
    /**
     * The length of the array if {@code data} is an array. <br>
     * Throws an {@link IllegalArgumentException} otherwise.
     * 
     * @return The length of the array if {@code data} is an array.
     */
    public int length() {
        return ArrayTools.getLength(data);
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
            return ArrayTools.toString(data);
        } else {
            return ((Object) data).toString();
        }
    }
    
    
}
