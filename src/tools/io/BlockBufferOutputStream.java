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


// Own imports
import tools.data.BlockByteBuffer;


// Java imports
import java.io.IOException;
import java.io.OutputStream;


/**
 * Abstract class for making an {@link OutputStream} which accepts only
 * (dynamic) chunks of data instead of a stream.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public abstract class BlockBufferOutputStream
        extends OutputStream {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The buffer used for storing the data. */
    private final BlockByteBuffer buffer = new BlockByteBuffer();
    /** The block size to use. */
    private int blockSize = 0;
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Checks if the buffer is large enough to process the next block.
     * If so, process it.
     * 
     * @throws IOException If an IO error occurs.
     */
    private void checkBuffer()
            throws IOException {
        while (buffer.size() >= blockSize) {
            if (blockSize > 0)  {
                byte[] data = buffer.get(blockSize);
                processBlock(data);
            }
            blockSize = getNextBlockSize();
        }
    }
    
    @Override
    public void write(int data)
            throws IOException {
        buffer.add((byte) (data & 0xFF));
        checkBuffer();
    }
    
    /**
     * Writes the given byte to the buffer.
     * 
     * @param data The byte to write to the buffer.
     * 
     * @throws IOException If an IO error occurs.
     * 
     * @see #write(int)
     */
    public void write(byte data)
            throws IOException {
        buffer.add(data);
        checkBuffer();
    }
    
    @Override
    public void write(byte[] data)
            throws IOException {
        buffer.add(data);
        checkBuffer();
    }
    
    @Override
    public void write(byte[] data, int off, int len)
            throws IOException {
        buffer.add(data, off, len);
        checkBuffer();
    }
    
    @Override
    public void flush()
            throws IOException {
        checkBuffer();
        if (buffer.size() != 0) {
            byte[] data = buffer.getRemaining();
            processBlock(data);
        }
    }
    
    @Override
    @SuppressWarnings("ConvertToTryWithResources")
    public void close()
            throws IOException {
        flush();
        super.close();
    }
    
    /**
     * Returns the next block size for the {@link #processBlock(byte[])} function. <br>
     * This function will be called exactly once for every block.<br>
     * Furthermore is guarateed that this function will only be called
     * after the {@link #processBlock(byte[])} function has been invoked for the
     * previous block (if such a block exists).
     * 
     * @return The next block size.
     * 
     * @throws IOException If an IO error occurs.
     */
    protected abstract int getNextBlockSize()
            throws IOException;
    
    /**
     * The data block to process. <br>
     * It is guaranteed that the length of {@code data} is at most to the
     * value previously returned by {@link #getNextBlockSize()}.
     * 
     * @param data The data to be processed.
     * 
     * @throws IOException If an IO error occurs.
     */
    protected abstract void processBlock(byte[] data)
            throws IOException;
    
    
}
