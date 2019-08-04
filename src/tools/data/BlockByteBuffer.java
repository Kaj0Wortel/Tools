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

package tools.data;


// Java imports
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * Creates a buffer for bytes.
 * This class can be used to buffer bytes between I/O operations/streams. <br>
 * <br>
 * This class is not thread safe.
 * If concurrent access is needed, use {@code ConcurrentBlockByteBuffer} instead.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class BlockByteBuffer
        implements Iterable<Byte> {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The buffer used to store the bytes. */
    private Deque<Byte> buffer = new LinkedList<>();
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Adds the given value to the buffer.
     * 
     * @param value The value to be added to the buffer.
     */
    public void add(byte value) {
        buffer.addLast(value);
    }
    
    /**
     * Adds all values in the array to the buffer.
     * 
     * @param values The values to be added to the buffer.
     * 
     * @see #add(byte[], int, int)
     * @see #add(Byte[])
     */
    public void add(byte[] values) {
        add(values, 0, values.length);
    }
    
    /**
     * Adds {@code len} values from the index {@code off} onwards to the buffer.
     * 
     * @param values The values to be added to the buffer.
     * @param off The index to start adding from.
     * @param len The amount of values to add.
     */
    public void add(byte[] values, int off, int len) {
        if (values == null) throw new NullPointerException();
        if (values.length < off + len) {
            throw new IllegalArgumentException("values.length(" + values.length + ") < off("
                    + off + ") + len(" + len + ")");
        }
        for (int i = 0; i < len; i++) {
            buffer.addLast(values[off + i]);
        }
    }
    
    /**
     * Converts the given string to bytes and add them to the buffer.
     * 
     * @param str The data string to be added.
     * 
     * @see #add(byte[])
     */
    public void add(String str) {
        add(str.getBytes());
    }
    
    /**
     * @return The size of the remaining buffer.
     */
    public int size() {
        return buffer.size();
    }
    
    /**
     * @return {@code true} if the buffer is empty. {@code false} otherwise.
     */
    public boolean isEmpty() {
        return buffer.isEmpty();
    }
    
    /**
     * Retrieves the first byte from the buffer.
     * 
     * @return The first byte from the buffer.
     */
    public byte getByte() {
        return buffer.removeFirst();
    }
    
    /**
     * Retrieves {@code len} elements from the buffer and returns
     * them in the given array from the index {@code off} onwards.
     * The elements are returned from the lower index towards the higher
     * index according to the FIFO policy.
     * 
     * @param dest The destination array of the operation.
     * @param off The index to start adding from.
     * @param len The amount of values to add.
     * 
     * @return {@code dest} filled with the elements from the buffer.
     */
    public byte[] get(byte[] dest, int off, int len) {
        for (int i = 0; i < len; i++) {
            dest[i + off] = buffer.removeFirst();
        }
        return dest;
    }
    
    /**
     * Retrieves {@code dest.length} elements from the buffer and returns
     * them in the given array. The elements are returned from the lower index towards
     * the higher index according to the FIFO policy.
     * 
     * @param dest The destination array of the operation.
     * 
     * @return {@code dest} filled with the elements from the buffer.
     * 
     * @see #get(byte[], int, int)
     */
    public byte[] get(byte[] dest) {
        return get(dest, 0, dest.length);
    }
    
    /**
     * Returns an array of length {@code len} containing the elements
     * from the buffer. The elements are returned from the lower index towards
     * the higher index according to the FIFO policy.
     * 
     * @param len The length of the returning array.
     * 
     * @return An array of the given length containing the elements from the buffer.
     * 
     * @see #get(byte[])
     */
    public byte[] get(int len) {
        return get(new byte[len]);
    }
    
    /**
     * Retrieves the remaining elements from the buffer and returns
     * them in an array. The elements are returned from the lower index towards
     * the higher index according to the FIFO policy.
     * 
     * @return An array containing the remaining elements of the buffer.
     * 
     * @see #get(int)
     */
    public byte[] getRemaining() {
        return get(buffer.size());
    }
    
    /**
     * Retrieves the remaining elements from the buffer and returns
     * them in the given array, starting at the index {@code off}.
     * The elements are returned from the lower index towards the higher
     * index according to the FIFO policy.
     * 
     * @param dest The destination array of the operation.
     * @param off The index to start adding from.
     * 
     * @return The number of bytes read.
     * 
     * @see #get(byte[], int, int)
     */
    public int getRemaining(byte[] dest, int off) {
        int minLength = Math.min(buffer.size(), dest.length);
        get(dest, off, minLength);
        return minLength;
    }
    
    @Override
    public Iterator<Byte> iterator() {
        return buffer.iterator();
    }
    
    /**
     * Retrieves {@code amt} bytes from the buffer and converts them to a string.
     * 
     * @param amt The amount of bytes to read.
     * 
     * @return A string from the next {@code amt} bytes of the buffer.
     */
    public String getString(int amt) {
        return new String(get(amt));
    }
    
    
}
