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
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


// Tools imports
import tools.MultiTool;
import tools.PublicCloneable;
import tools.data.Wrapper;
import tools.iterators.ArrayIterator;


/**
 * Base class for array extension classes. <br>
 * The aim of this class is to simplify creating array-like data classes which
 * allow certain read and/or write permission and differend kind of
 * read and/or write operations. <br>
 * <br>
 * An important key-feature of these classes is that they only act as a wrapper
 * around the actual array. This implies that modifications in the given source
 * array will also occur in this array and vice verca. <br>
 * Because these classes only acts as a wrapper, they can be created in
 * constant time, which makes them very handy for exporting an array with
 * restricted or additional operations.
 * 
 * @version 1.1
 * @author Kaj Wortel
 */
public abstract class AttributedArray<V>
        implements Iterable<V>, PublicCloneable {
     
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The backening array. */
    protected final Wrapper<V[]> array;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates an attributed array from the given array.
     * 
     * @param arr The backening array of any type.
     */
    public AttributedArray(Object arr) {
        if (arr == null) throw new NullPointerException();
        if (!arr.getClass().isArray()) {
            throw new IllegalArgumentException("The given object is not an array.");
        }
        array = new Wrapper(arr);
    }
    
    /**
     * Creates a attributed array from the given array.
     * 
     * @param arr The backening array.
     */
    public AttributedArray(V... arr) {
        if (arr == null) throw new NullPointerException();
        array = new Wrapper<V[]>(arr);
    }
    
    /**
     * Creates a attributed array from the given array. <br>
     * Note 
     * 
     * @param arr The backening array in a wrapper.
     * 
     * @throws NullPointerException If {@code array == null}.
     * @throws IllegalArgumentException If {@code array} is not an array.
     */
    public AttributedArray(Wrapper<V[]> arr) {
        if (arr == null) throw new NullPointerException();
        if (!arr.isArray()) {
            throw new IllegalArgumentException("Expected an array, but found: "
                    + arr.get().getClass().getName());
        }
        this.array = arr;
    }
    
    
    /* -------------------------------------------------------------------------
     * Access functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Gets the value at the given index.
     * 
     * @param index The index of the element to return.
     * 
     * @return The element at the given index.
     */
    protected V get(int index) {
        return (V) array.get(index);
    }
    
    /**
     * Sets the given value at the given index.
     * 
     * @param value The new value of array.
     * @param index The index of the value to set.
     * 
     * @return The previous value at the given location.
     */
    protected V set(V value, int index) {
        V old = (V) array.get(index);
        array.set(value, index);
        return old;
    }
    
    /**
     * @return The length of the array.
     */
    public int length() {
        return array.length();
    }
    
    @Override
    public Iterator<V> iterator() {
        return new ArrayIterator<V>(array);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AttributedArray) {
            return Objects.deepEquals(array, ((AttributedArray) obj).array);
            
        } else {
            return Objects.deepEquals(array, obj);
        }
    }
    
    @Override
    public int hashCode() {
        return MultiTool.calcHashCode(array);
    }
    
    @Override
    public String toString() {
        return array.toString();
    }
    
    @Override
    public abstract AttributedArray clone();
    
    
    /* -------------------------------------------------------------------------
     * Array copy functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Copies the array to the given array of the same type. <br>
     * If not enough space is available in the copy, then the last elements
     * are not copied. If too much space is available in the copy, then the
     * last elements in the copy remain unchanged.
     * 
     * @param copy The array to store the copy in.
     * 
     * @throws IllegalArgumentException If:
     *     <ul>
     *         <li> {@code offOrig < 0} </li>
     *         <li> {@code offCopy < 0} </li>
     *         <li> {@code offOrig + len >= length()} </li>
     *         <li> {@code offCopy + len >= copy.length} </li>
     *     </ul>
     * 
     * @see #copyOf(Object[], int, int, int)
     */
    public V[] copyOf(V[] copy)
            throws IllegalArgumentException {
        return copyOf(copy, 0, 0, Math.min(array.length(), copy.length));
    }
    
    /**
     * Copies the array to the given array of the same type. <br>
     * If not enough space is available in the copy, then the last elements
     * are not copied. If too much space is available in the copy, then the
     * last elements in the copy remain unchanged.
     * 
     * @param copy The array to store the copy in.
     * @param offOrig The offset from where to start copying elements.
     * @param offCopy The offset from where to copy elements to the given array (inclusive).
     * @param len The number of elements to copy.
     * 
     * @throws IllegalArgumentException If:
     *     <ul>
     *         <li> {@code offOrig < 0} </li>
     *         <li> {@code offCopy < 0} </li>
     *         <li> {@code offOrig + len >= length()} </li>
     *         <li> {@code offCopy + len >= copy.length} </li>
     *     </ul>
     */
    public V[] copyOf(V[] copy, int offOrig, int offCopy, int len)
            throws IllegalArgumentException {
        if (offOrig < 0)
            throw new IllegalArgumentException("offOrig(" + offOrig + ") < 0");
        if (offCopy < 0)
            throw new IllegalArgumentException("offCopy(" + offCopy + ") < 0");
        if (offOrig + len > array.length()) {
            throw new IllegalArgumentException("offOrig(" + offOrig+ ") + len(" + len
                    + ") > thislength(" + array.length() + ")");
        }
        if (offOrig + len > copy.length) {
            throw new IllegalArgumentException("offCopy(" + offCopy + ") + len(" + len
                    + ") > copy.length(" + copy.length + ")");
        }
        
        for (int i = 0; i < len; i++) {
            copy[i + offCopy] = (V) array.get(i + offOrig);
        }
        
        return copy;
    }
    
    /**
     * Copies the array to the given Object array. <br>
     * If not enough space is available in the copy, then the last elements
     * are not copied. If too much space is available in the copy, then the
     * last elements in the copy remain unchanged.
     * 
     * @param copy The array to store the copy in.
     * 
     * @throws IllegalArgumentException If:
     *     <ul>
     *         <li> {@code offOrig < 0} </li>
     *         <li> {@code offCopy < 0} </li>
     *         <li> {@code offOrig + len >= length()} </li>
     *         <li> {@code offCopy + len >= copy.length} </li>
     *     </ul>
     * 
     * @see #copyOf(Object, int, int, int)
     */
    public <A> A copyOf(A copy)
            throws IllegalArgumentException {
        return copyOf(copy, 0, 0, Math.min(array.length(), ArrayTools.length(copy)));
    }
    
    /**
     * Copies the array to the given Object array. <br>
     * If not enough space is available in the copy, then the last elements
     * are not copied. If too much space is available in the copy, then the
     * last elements in the copy remain unchanged.
     * 
     * @param copy The array to store the copy in.
     * @param offOrig The offset from where to start copying elements.
     * @param offCopy The offset from where to copy elements to the given array (inclusive).
     * @param len The number of elements to copy.
     * 
     * @throws IllegalArgumentException If:
     *     <ul>
     *         <li> {@code offOrig < 0} </li>
     *         <li> {@code offCopy < 0} </li>
     *         <li> {@code offOrig + len >= length()} </li>
     *         <li> {@code offCopy + len >= copy.length} </li>
     *     </ul>
     */
    public <A> A copyOf(A copy, int offOrig, int offCopy, int len)
            throws IllegalArgumentException {
        if (offOrig < 0)
            throw new IllegalArgumentException("offOrig < 0: " + offOrig);
        if (offCopy < 0)
            throw new IllegalArgumentException("offCopy < 0: " + offCopy);
        if (offOrig + len >= array.length())
            throw new IllegalArgumentException("offOrig + len >= length()");
        if (offOrig + len >= ArrayTools.length(copy))
            throw new IllegalArgumentException("offCopy + len >= copy.length");
        
        for (int i = 0; i < len; i++) {
            ArrayTools.set(copy, i + offCopy, array.get(i + offOrig));
        }
        
        return copy;
    }
    
    
    /* -------------------------------------------------------------------------
     * List copy functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Adds all elements from this array to the given list.
     * 
     * @param list The list to add the elements to.
     * 
     * @return The given list.
     */
    public List<V> asList(List<V> list) {
        if (list == null) throw new NullPointerException();
        list.addAll(asList());
        return list;
    }
    
    /**
     * @return A list from the given array.
     */
    public List<V> asList() {
        return ArrayTools.asList(array.get());
    }
    
    
}
