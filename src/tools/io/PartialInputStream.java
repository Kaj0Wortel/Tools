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


/**
 * Input stream for reading a part of another input stream.
 * 
 * @author Kaj Wortel
 */
public class PartialInputStream
        extends InputStream {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The data source input stream. */
    private InputStream is;
    /** The remaining number of bytes allowed to be read from the stream. */
    private long bytesRem;
    /** Whether the underlying stream is still valid. */
    private boolean invalid = false;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a partial input stream from the given stream. <br>
     * Note that the given stream is not allowed to be read before
     * this stream is finished.
     * 
     * @param is The data source input stream.
     * @param bytes The number of bytes allowed to be read from the stream.
     */
    public PartialInputStream(InputStream is, long bytes) {
        this.is = is;
        bytesRem = bytes;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    private void checkStream()
            throws IOException {
        if (invalid) throw new IOException("The stream is no longer valid!");
    }

    @Override
    public int read()
            throws IOException {
        checkStream();
        if (bytesRem <= 0) return -1;
        bytesRem--;
        return is.read();
    }
    
    @Override
    public int read(byte[] b)
            throws IOException {
        return read(b, 0, b.length);
    }
    
    @Override
    public int read(byte[] b, int off, int len)
            throws IOException {
        checkStream();
        if (b == null) throw new NullPointerException();
        if (off < 0) throw new IllegalArgumentException("off < 0");
        if (len < 0) throw new IllegalArgumentException("len < 0");
        if (off + len > b.length) throw new IllegalArgumentException("off + len > b.length");
        
        if (bytesRem <= 0) return -1;
        if (bytesRem < len) {
            int read = (int) bytesRem;
            bytesRem = 0L;
            return is.read(b, off, read);
        }
        // bytesRem >= len.
        bytesRem -= len;
        return is.read(b, off, len);
    }
    
    @Override
    public int available()
            throws IOException {
        checkStream();
        return (int) Math.min(bytesRem, is.available());
    }
    
    @Override
    public long skip(long n)
            throws IOException {
        checkStream();
        long skipped = is.skip(Math.min(bytesRem, n));
        bytesRem -= skipped;
        return skipped;
    }
    
    /**
     * Skips the remainder of the stream.
     * 
     * @return The actual number of bytes skipped.
     * 
     * @throws IOException If the stream does not support seek,
     *     or if some IO error occurs.
     */
    public long skipRemaining()
            throws IOException {
        return skip(bytesRem);
    }
    
    /**
     * {@inheritDoc}
     * 
     * Note that this function does not close the actual underlying stream.
     */
    @Override
    public void close()
            throws IOException {
        invalidateStream();
    }
    
    /**
     * Invalidates the stream. This will cause {@link IOException}s
     * to be thrown for every function which requires the underlaying stream,
     * excluding {@link #close()}
     * 
     * @throws IOException If the stream could not be invalidated.
     */
    public void invalidateStream()
            throws IOException {
        if (invalid || bytesRem <= 0) return;
        try {
            skipRemaining();
            
        } finally {
            invalid = true;
        }
    }
    
    /**
     * @return {@code true} if the stream is valid. {@code false} otherwise.
     */
    public boolean isValid() {
        return !invalid;
    }
    
    
}
