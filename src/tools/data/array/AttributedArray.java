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
 * read and/or write operations.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public abstract class AttributedArray<V>
        implements Iterable<V>, PublicCloneable {
     
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The backening array. */
    protected final Wrapper<V[]> arr;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates an attributed array from the given array.
     * 
     * @param arr The backening array of any type.
     */
    public AttributedArray(final Object arr) {
        this(new Wrapper(arr));
    }
    
    /**
     * Creates a attributed array from the given array.
     * 
     * @param arr The backening array.
     */
    public AttributedArray(final V... arr) {
        this(new Wrapper<V[]>(arr));
    }
    
    /**
     * Creates a attributed array from the given array.
     * 
     * @param arr The backening array in a wrapper.
     * 
     * @throws NullPointerException If {@code arr == null}.
     * @throws IllegalArgumentException If {@code arr} is not an array.
     */
    public AttributedArray(final Wrapper<V[]> arr) {
        if (arr == null) throw new NullPointerException();
        if (!arr.isArray()) {
            throw new IllegalArgumentException("Expected an array, but found: "
                    + arr.get().getClass().getName());
        }
        this.arr = arr;
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
    protected V get(final int index) {
        return (V) arr.get(index);
    }
    
    /**
     * Sets the given value at the given index.
     * 
     * @param value The new value of array.
     * @param index The index of the value to set.
     * 
     * @return The previous value at the given location.
     */
    protected V set(final V value, final int index) {
        V old = (V) arr.get(index);
        arr.set(value, index);
        return old;
    }
    
    /**
     * @return The length of the array.
     */
    public int length() {
        return arr.length();
    }
    
    @Override
    public Iterator<V> iterator() {
        return new ArrayIterator<V>(arr);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof AttributedArray) {
            return Objects.deepEquals(arr, ((AttributedArray) obj).arr);
            
        } else {
            return Objects.deepEquals(arr, obj);
        }
    }
    
    @Override
    public int hashCode() {
        return MultiTool.calcHashCode(arr);
    }
    
    @Override
    public String toString() {
        return arr.toString();
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
    public V[] copyOf(final V[] copy)
            throws IllegalArgumentException {
        return copyOf(copy, 0, 0, Math.min(arr.length(), copy.length));
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
    public V[] copyOf(final V[] copy, final int offOrig, final int offCopy, final int len)
            throws IllegalArgumentException {
        if (offOrig < 0)
            throw new IllegalArgumentException("offOrig(" + offOrig + ") < 0");
        if (offCopy < 0)
            throw new IllegalArgumentException("offCopy(" + offCopy + ") < 0");
        if (offOrig + len > arr.length()) {
            throw new IllegalArgumentException("offOrig(" + offOrig+ ") + len(" + len
                    + ") > thislength(" + arr.length() + ")");
        }
        if (offOrig + len > copy.length) {
            throw new IllegalArgumentException("offCopy(" + offCopy + ") + len(" + len
                    + ") > copy.length(" + copy.length + ")");
        }
        
        for (int i = 0; i < len; i++) {
            copy[i + offCopy] = (V) arr.get(i + offOrig);
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
    public <A> A copyOf(final A copy)
            throws IllegalArgumentException {
        return copyOf(copy, 0, 0, Math.min(arr.length(), ArrayTools.length(copy)));
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
    public <A> A copyOf(final A copy, final int offOrig, final int offCopy, final int len)
            throws IllegalArgumentException {
        if (offOrig < 0)
            throw new IllegalArgumentException("offOrig < 0: " + offOrig);
        if (offCopy < 0)
            throw new IllegalArgumentException("offCopy < 0: " + offCopy);
        if (offOrig + len >= arr.length())
            throw new IllegalArgumentException("offOrig + len >= length()");
        if (offOrig + len >= ArrayTools.length(copy))
            throw new IllegalArgumentException("offCopy + len >= copy.length");
        
        for (int i = 0; i < len; i++) {
            ArrayTools.set(copy, i + offCopy, arr.get(i + offOrig));
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
    public List<V> asList(final List<V> list) {
        if (list == null) throw new NullPointerException();
        list.addAll(asList());
        return list;
    }
    
    /**
     * @return A list from the given array.
     */
    public List<V> asList() {
        return ArrayTools.asList(arr.get());
    }
    
    
}
