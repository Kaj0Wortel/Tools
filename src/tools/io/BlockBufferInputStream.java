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


// Tools imports
import tools.data.BlockByteBuffer;


// Java imports
import java.io.IOException;
import java.io.InputStream;


/**
 * Abstract class for making an {@link InputStream} which accepts only
 * (dynamic) chunks of data instead of a stream.
 * 
 * @author Kaj Wortel
 */
public abstract class BlockBufferInputStream
        extends InputStream {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The buffer used for storing the data. */
    private final BlockByteBuffer buffer = new BlockByteBuffer();
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Checks if the buffer is large enough to read the next block. <br>
     * If not, request more data.
     * 
     * @param amt The amount of requested bytes.
     * 
     * @return The amount of bytes which can be removed from the buffer
     *     for the request (i.e. a value {@code 0 <= n <= amt}).
     * 
     * @throws IOException If an IO error occurs.
     */
    private int checkBuffer(int amt)
            throws IOException {
        while (buffer.size() < amt) {
            byte[] data = new byte[getNextBlockSize()];
            int len = readBlock(data);
            if (len == -1) return buffer.size();
            buffer.add(data, 0, len);
        }
        
        return amt;
    }
    
    @Override
    public int read()
            throws IOException {
        return (checkBuffer(1) == 1
                ? buffer.get(1)[0] & 0xFF
                : -1);
    }
    
    @Override
    public int read(byte[] b)
            throws IOException {
        int avail = checkBuffer(b.length);
        if (avail <= 0) return -1;
        buffer.get(b, 0, avail);
        return avail;
    }
    
    @Override
    public int read(byte[] b, int off, int len)
            throws IOException {
        int avail = checkBuffer(len);
        if (avail <= 0) return -1;
        buffer.get(b, off, avail);
        return avail;
    }
    
    /**
     * Returns the block size for the {@link #readBlock(byte[])} function. <br>
     * This function will be called exactly once before each call to the function
     * {@link #readBlock(byte[])}. <br>
     * Furthermore is guarateed that this function will only be called
     * after the {@link #readBlock(byte[])} function has been invoked for the
     * previous block (if such a block exists).
     * 
     * @return The next block size.
     * 
     * @throws IOException If an IO error occurs.
     */
    protected abstract int getNextBlockSize()
            throws IOException;
    
    /**
     * Reads a block from the underlying input stream. <br>
     * The block must be written into the provided byte array {@code b},
     * and is filled from low index to high index. <br>
     * If there are is less data in the stream than the array size, the first
     * part of the array is filled and the second part remains unchanged.
     * 
     * @param b The array to write the data to. It is filled from low to high index.
     * 
     * @return The number of bytes written to {@code b}.
     * 
     * @throws IOException If an IO error occurs.
     */
    public abstract int readBlock(byte[] b)
            throws IOException;
    
    
}
