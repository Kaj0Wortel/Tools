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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import tools.MultiTool;
import tools.Pair;


// Own imports


// Java imports


/**
 * TODO: copy and implement <a href='http://www.docjar.com/html/api/java/util/Arrays.java.html'>THIS</a>
 * TODO: Note: must be implemented since java uses native functions which cannot be optimised, hence 10 time slower.
 * 
 * Tool class for handelings with primitive data types.
 * 
 * @author Kaj Wortel (0991586)
 */
public class PrimitiveTool {
    
    /**
     * Static tool class.
     */
    private PrimitiveTool() {
        
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
    public Object get(Object array, int index) {
        if (array instanceof Object[]) return ((Object[]) array)[index];
        if (array instanceof boolean[]) return ((boolean[]) array)[index];
        if (array instanceof byte[]) return ((byte[]) array)[index];
        if (array instanceof char[]) return ((char[]) array)[index];
        if (array instanceof short[]) return ((short[]) array)[index];
        if (array instanceof int[]) return ((int[]) array)[index];
        if (array instanceof long[]) return ((long[]) array)[index];
        if (array instanceof float[]) return ((float[]) array)[index];
        if (array instanceof double[]) return ((double[]) array)[index];
        if (array == null) throw new NullPointerException();
        throw new IllegalArgumentException("Given object was not an array!");
    }
    
    
}
