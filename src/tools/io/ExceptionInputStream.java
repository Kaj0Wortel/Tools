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

package tools.io;


import java.io.IOException;



// Own imports


// Tools imports


// Java imports


/**
 * 
 * 
 * @author Kaj Wortel
 */
public interface ExceptionInputStream {
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Default read function from the {@link java.io.InputStream} class to
     * read a single byte.
     * 
     * @return The read byte, or {@code -1} if no such byte is available.
     * 
     * @throws IOException If there was an I/O error.
     * 
     * @see java.io.InputStream#read()
     */
    public int read()
            throws IOException;
    
    /**
     * Default read function from the {@link java.io.InputStream} class to
     * read into a buffer.
     * 
     * @param b The array to read data to.
     * 
     * @return The read byte, or {@code -1} if no such byte is available.
     * 
     * @throws IOException If there was an I/O error.
     * 
     * @see java.io.InputStream#read(byte[])
     */
    public int read(byte[] b)
            throws IOException;
    
    /**
     * Default read function from the {@link java.io.InputStream} class to
     * read into a part of a buffer.
     * 
     * @param b The array to read data to.
     * @param off The index to start reading from (inclusive).
     * @param len The amount of bytes to read.
     * 
     * @return The read byte, or {@code -1} if no such byte is available.
     * 
     * @throws IOException If there was an I/O error.
     * 
     * @see java.io.InputStream#read(byte[], int, int)
     */
    public int read(byte[] b, int off, int len)
            throws IOException;
    
    /**
     * Reads a single byte from the stream. <br>
     * Throws an {@link InsufficientDataException} if no data is available.
     * 
     * @return The read byte.
     * 
     * @throws InsufficientDataException If there was no more data available.
     * @throws IOException If there was an I/O error.
     * 
     * @see #read()
     */
    public default byte readException()
            throws InsufficientDataException, IOException {
        int val = read();
        if (val == -1) {
            throw new InsufficientDataException(
                    "Expected 1 byte, but could only read " + val + " bytes!");
        }
        return (byte) (val & 0xFF);
    }
    
    /**
     * Reads a single byte from the stream. <br>
     * Throws an {@link InsufficientDataException} if no data is available.
     * 
     * @param b The array to read data to.
     * 
     * @throws InsufficientDataException If there was no more data available.
     * @throws IOException If there was an I/O error.
     * 
     * @see #read(byte[])
     */
    public default void readException(byte[] b)
            throws InsufficientDataException, IOException {
        int val = read(b);
        if (val != b.length) {
            throw new InsufficientDataException(
                    "Expected " + b.length + " byte(s), but could only read " + val + " byte(s)!");
        }
    }
    
    /**
     * Reads a single byte from the stream. <br>
     * Throws an {@link InsufficientDataException} if not enough data is available.
     * 
     * @param b The array to read data to.
     * @param off The index to start reading from (inclusive).
     * @param len The amount of bytes to read.
     * 
     * @throws InsufficientDataException If there was no more data available.
     * @throws IOException If there was an I/O error.
     * 
     * @see #read(byte[], int, int)
     */
    public default void readException(byte[] b, int off, int len)
            throws InsufficientDataException, IOException {
        int val = read(b, off, len);
        if (val != len) {
            throw new InsufficientDataException(
                    "Expected " + len + " byte(s), but could only read " + val + " byte(s)!");
        }
    }
    
    /**
     * Reads {@code amt} bytes from the stream. <br>
     * Throws an {@link InsufficientDataException} if not enough data is available.
     * 
     * @param amt The amount of bytes to read.
     * 
     * @return An array of length {@code amt} containing the bytes from the stream.
     * 
     * @throws InsufficientDataException If there was no more data available.
     * @throws IOException If there was an I/O error.
     * 
     * @see #readException(byte[])
     */
    public default byte[] readException(int amt)
            throws InsufficientDataException, IOException {
        byte[] b = new byte[amt];
        readException(b);
        return b;
    }
    
    /**
     * Reads {@code len} bytes from the stream into an array of length {@code amt}.
     * starting at index {@code off}. <br>
     * Throws an {@link InsufficientDataException} if not enough data is available.
     * 
     * @param amt The amount of bytes to read.
     * @param off The index to start reading from (inclusive).
     * @param len The amount of bytes to read.
     * 
     * @return An array of length {@code amt} containing {@code len} bytes
     *     from the stream, beginning at index {@code off}
     * 
     * @throws InsufficientDataException If there was no more data available.
     * @throws IOException If there was an I/O error.
     * 
     * @see #readException(byte[])
     */
    public default byte[] readException(int amt, int off, int len)
            throws InsufficientDataException, IOException {
        byte[] b = new byte[amt];
        readException(b, off, len);
        return b;
    }
    
    
}
