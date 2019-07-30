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
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * Creates a buffer for bytes.
 * This buffer can be used to create equally sized blocks from a stream.
 * 
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
     * Adds {@code len} values from the index {@code off} onwards
     * to the buffer.
     * 
     * @param values The values to be added to the buffer.
     * @param off The index to start adding from.
     * @param len The amount of values to add.
     */
    public void add(byte[] values, int off, int len) {
        if (values == null) throw new NullPointerException();
        if (values.length < off + len) {
            throw new IllegalArgumentException("values.length < off + len");
        }
        for (int i = 0; i < len; i++) {
            buffer.addLast(values[off + i]);
        }
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
    
    public byte getByte() {
        return buffer.removeFirst();
    }
    
    /**
     * Retrieves {@code len} elements from the buffer and returns
     * them in the given array from the index {@code off} onwards.
     * The elements are returned according to the FIFO policy.
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
     * them in the given array. The elements are returned according to
     * the FIFO policy.
     * 
     * @param dest The destination array of the operation.
     * 
     * @return {@code dest} filled with the elements from the buffer.
     */
    public byte[] get(byte[] dest) {
        return get(dest, 0, dest.length);
    }
    
    /**
     * Returns an array of length {@code len} containing the elements
     * from the buffer. The elements are returned according to
     * the FIFO policy.
     * 
     * @param len The length of the returning array.
     * 
     * @return An array of length {@code length} containing
     *     the elements from the buffer.
     * 
     * @see #get(byte[])
     */
    public byte[] get(int len) {
        return get(new byte[len]);
    }
    
    /**
     * Retrieves the remaining elements from the buffer and returns
     * them in an array. The elements are returned according to
     * the FIFO policy.
     * 
     * @return An array containing the remaining elements of the buffer.
     * 
     * @see #get(int)
     */
    public byte[] getRemaining() {
        return get(buffer.size());
    }
    
    /**TODO: change that {@code true} is returned when the buffer is false.
     * Retrieves the remaining elements from the buffer and returns
     * them in the given array, starting at the index {@code off}.
     * The elements are returned according to the FIFO policy.
     * 
     * @param dest The destination array of the operation.
     * @param off The index to start adding from.
     * 
     * @return The given array containing the remaining elements of the buffer.
     */
    public byte[] getRemaining(byte[] dest, int off) {
        return get(dest, off, buffer.size());
    }
    
    @Override
    public Iterator<Byte> iterator() {
        return buffer.iterator();
    }
    
    
}
