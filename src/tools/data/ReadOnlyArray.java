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


// Tools imports
import tools.MultiTool;
import tools.iterators.ArrayIterator;


// Java imports
import java.util.Iterator;
import java.util.List;
import java.util.Objects;



/**
 * Data class for making any array read-only.
 * 
 * @author Kaj Wortel
 */
public class ReadOnlyArray<V>
        implements Iterable<V>, tools.Cloneable {
    
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
     * Creates a read-only array from the given array.
     * 
     * @param arr The backening array of any type.
     */
    public ReadOnlyArray(final Object arr) {
        this(new Wrapper(arr));
    }
    
    /**
     * Creates a read-only array from the given array.
     * 
     * @param arr The backening array.
     */
    public ReadOnlyArray(final V... arr) {
        this(new Wrapper<V[]>(arr));
    }
    
    /**
     * Creates a read-only array from the given array.
     * 
     * @param arr The backening array in a wrapper.
     */
    public ReadOnlyArray(final Wrapper<V[]> arr) {
        this.arr = arr;
    }
    
    
    /* -------------------------------------------------------------------------
     * Access functions.
     * -------------------------------------------------------------------------
     */
    /**
     * @param index The index of the element to return.
     * 
     * @return The element at the given index.
     */
    public V get(final int index) {
        return (V) arr.get(index);
    }

    @Override
    public Iterator<V> iterator() {
        return new ArrayIterator<V>(arr);
    }
    
    /**
     * @return The length of the array.
     */
    public int length() {
        return arr.length();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ReadOnlyArray) {
            return Objects.deepEquals(arr, ((ReadOnlyArray) obj).arr);
        } else {
            return Objects.deepEquals(arr, obj);
        }
    }
    
    @Override
    public int hashCode() {
        return MultiTool.calcHashCode(arr);
    }
    
    @Override
    public ReadOnlyArray<V> clone() {
        return new ReadOnlyArray<V>(arr);
    }
    
    @Override
    public String toString() {
        return arr.toString();
    }
    
    
    /* -------------------------------------------------------------------------
     * Array copy functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Copies the read-only array to the given array of the same type. <br>
     * If not enough space is available in the copy, then the last elements
     * are not copied. If too much space is available in the copy, then the
     * last elements in the copy remain unchanged.
     * 
     * @param copy The array to store the copy in.
     */
    public V[] copyOf(final V[] copy) {
        return copyOf(copy, 0, 0, Math.min(arr.length(), copy.length));
    }
    
    /**
     * Copies the read-only array to the given array of the same type. <br>
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
    public V[] copyOf(final V[] copy, final int offOrig, final int offCopy, final int len) {
        if (offOrig < 0)
            throw new IllegalArgumentException("offOrig < 0: " + offOrig);
        if (offCopy < 0)
            throw new IllegalArgumentException("offCopy < 0: " + offCopy);
        if (offOrig + len > arr.length())
            throw new IllegalArgumentException("offOrig + len > length()");
        if (offOrig + len > copy.length)
            throw new IllegalArgumentException("offCopy + len > copy.length");
        
        for (int i = 0; i < len; i++) {
            copy[i + offCopy] = (V) arr.get(i + offOrig);
        }
        
        return copy;
    }
    
    /**
     * Copies the read-only array to the given Object array. <br>
     * If not enough space is available in the copy, then the last elements
     * are not copied. If too much space is available in the copy, then the
     * last elements in the copy remain unchanged.
     * 
     * @param copy The array to store the copy in.
     */
    public <A> A copyOf(final A copy) {
        return copyOf(copy, 0, 0, Math.min(arr.length(), Array.getLength(copy)));
    }
    
    /**
     * Copies the read-only array to the given Object array. <br>
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
    public <A> A copyOf(final A copy, final int offOrig, final int offCopy, final int len) {
        if (offOrig < 0)
            throw new IllegalArgumentException("offOrig < 0: " + offOrig);
        if (offCopy < 0)
            throw new IllegalArgumentException("offCopy < 0: " + offCopy);
        if (offOrig + len >= arr.length())
            throw new IllegalArgumentException("offOrig + len >= length()");
        if (offOrig + len >= Array.getLength(copy))
            throw new IllegalArgumentException("offCopy + len >= copy.length");
        
        for (int i = 0; i < len; i++) {
            Array.set(copy, i + offCopy, arr.get(i + offOrig));
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
        for (V val : this) {
            list.add(val);
        }
        return list;
    }
    
    /**
     * @return A list from the given array.
     */
    public List<V> asList() {
        return Array.asList(arr.get().clone());
    }
    
    
}
