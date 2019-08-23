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


// Java imports
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Interface for adding functionality on top of a class extending {@link InputStream}. <br>
 * This interface adds easy read calls which throw an exception instead of returning {@code -1}.
 * This results in cleaner code when one would throw an exception anyways if certain data is expected. <br>
 * It also adds an easy conversion function to switch between the this implementation and the
* java {@link InputStream} variant.
 * 
 * @implNote
 * It is highly advised to let the implementing class (indirectly) extend {@link InputStream},
 * as this interface re-defines the most functions from this class.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public interface ExceptionInputStream {
    
    /* -------------------------------------------------------------------------
     * InputStream functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Default read function from the {@link InputStream} class to
     * read a single byte.
     * 
     * @return The read byte, or {@code -1} if no such byte is available.
     * 
     * @throws IOException If there was an I/O error.
     * 
     * @see InputStream#read()
     */
    public int read()
            throws IOException;
    
    /**
     * Default read function from the {@link InputStream} class to
     * read into a buffer.
     * 
     * @param b The array to read data to.
     * 
     * @return The read byte, or {@code -1} if no such byte is available.
     * 
     * @throws IOException If there was an I/O error.
     * 
     * @see InputStream#read(byte[])
     */
    public int read(byte[] b)
            throws IOException;
    
    /**
     * Default read function from the {@link InputStream} class to
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
     * @see InputStream#read(byte[], int, int)
     */
    public int read(byte[] b, int off, int len)
            throws IOException;
    
    /**
     * Returns an estimate of the number of bytes that can be read
     * (or skipped over) from this input stream without blocking,
     * which may be 0, or 0 when end of stream is detected.
     * @return An estimate of the number of bytes that can be read
     *     (or skipped over) from this input stream without blocking
     *     or 0 when it reaches the end of the input stream.
     * 
     * @throws IOException If an I/O error occurs.
     * 
     * @see InputStream#available()
     */
    public int available()
            throws IOException;
    
    /**
     * Closes this input stream and releases any system resources
     * associated with the stream.
     * 
     * @throws IOException If an I/O error occurs.
     */
    public void close()
            throws IOException;
    
    /**
     * Marks the current position in this input stream. A subsequent call
     * to the reset method repositions this stream at the last marked
     * position so that subsequent reads re-read the same bytes. 
     * 
     * @param readLimit The maximum limit of bytes that can be read
     *     before the mark position becomes invalid.
     * 
     * @see InputStream#mark(int)
     */
    public void mark(int readLimit);
    
    /**
     * Tests if this input stream supports the mark and reset methods.
     * Whether or not mark and reset are supported is an invariant property
     * of a particular input stream instance. The {@code markSupported}
     * method of {@link InputStream} returns {@code false}.
     * 
     * @return {@code true} if this stream instance supports the mark
     *     and reset methods. {@code false} otherwise.
     * 
     * @see InputStream#markSupported()
     */
    public boolean markSupported();
    
    /**
     * Reads all remaining bytes from the input stream.
     * This method blocks until all remaining bytes have been read and
     * end of stream is detected, or an exception is thrown.
     * This method does not close the input stream. 
     * 
     * @return A byte array containing the bytes read from this input stream.
     * 
     * @throws IOException If an I/O error occurs.
     * 
     * @see InputStream#readAllBytes()
     */
    public byte[] readAllBytes()
            throws IOException;
    
    /**
     * Reads the requested number of bytes from the input stream into
     * the given byte array. This method blocks until len bytes of input data
     * have been read, end of stream is detected, or an exception is thrown.
     * The number of bytes actually read, possibly zero, is returned.
     * This method does not close the input stream. 
     * 
     * @param b The byte array into which the data is read
     * @param off The start offset in b at which the data is written
     * @param len The maximum number of bytes to read
     * 
     * @return The actual number of bytes read into the buffer
     * 
     * @throws IOException If an I/O error occurs.
     * 
     * @see InputStream#readNBytes(byte[], int, int)
     */
    public int readNBytes(byte[] b, int off, int len)
            throws IOException;
    
    /**
     * Reads up to a specified number of bytes from the input stream.
     * This method blocks until the requested number of bytes have been read,
     * end of stream is detected, or an exception is thrown.
     * This method does not close the input stream. 
     * 
     * @param len The maximum number of bytes to read
     * 
     * @return A byte array containing the bytes read from this input stream
     * 
     * @throws IOException If an I/O error occurs.
     * 
     * @see InputStream#readNBytes(int)
     */
    public byte[] readNBytes(int len)
            throws IOException;
    
    /**
     * Repositions this stream to the position at the time the
     * mark method was last called on this input stream. 
     * 
     * @throws IOException If an I/O error occurs.
     * 
     * @see InputStream#reset()
     */
    public void reset()
            throws IOException;
    
    /**
     * Skips over and discards n bytes of data from this input stream.
     * 
     * @param n The number of bytes to be skipped.
     * 
     * @return The actual number of bytes skipped.
     * 
     * @throws IOException If an I/O error occurs.
     * 
     * @see InputStream#skip(long)
     */
    public long skip(long n)
            throws IOException;
    
    /**
     * Reads all bytes from this input stream and writes the bytes
     * to the given output stream in the order that they are read.
     * On return, this input stream will be at end of stream.
     * This method does not close either stream. 
     * 
     * @param out The output stream, non-null.
     * 
     * @return The number of bytes transferred
     * 
     * @throws IOException If an I/O error occurs.
     * 
     * @see InputStream#(OutputStream)
     */
    public long transferTo(OutputStream out)
            throws IOException;
    
    
    /* -------------------------------------------------------------------------
     * Exception functions.
     * -------------------------------------------------------------------------
     */
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
    
    /**
     * Converts {@code this} to an {@link InputStream}. <br>
     * If {@code this} is already an input stream, simply return {@code this}.
     * Otherwise wrap it into an {@link ExceptionInputStreamWrapper}.
     * 
     * @return An {@link InputStream} reading the bytes from {@code this} stream.
     */
    public default InputStream toInputStream() {
        if (this instanceof InputStream) {
            return (InputStream) this;
            
        } else {
            return new ExceptionInputStreamWrapper(this);
        }
    }
    
    
}
