/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) May 2019 by Kaj Wortel - all rights reserved                *
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
 * 
 * 
 * @author Kaj Wortel (0991586)
 */
public abstract class BlockBufferOutputStream
        extends OutputStream {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    final private BlockByteBuffer buffer;
    
    private int blockSize = -1;
    private boolean blockSizeRequested = false;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * 
     * @param out the outputstream to write the data to.
     */
    public BlockBufferOutputStream() {
        buffer = new BlockByteBuffer();
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Checks if the buffer is large enough to process the next block.
     * If so, process it.
     * 
     * @throws IOException if the outputstream threw an {@link IOException}.
     */
    private void checkBuffer()
            throws IOException {
        if (!blockSizeRequested) {
            blockSizeRequested = true;
            blockSize = getNextBlockSize();
        }
        
        while (buffer.size() >= blockSize) {
            byte[] data = buffer.get(blockSize);
            processBlock(data);
            blockSize = getNextBlockSize();
        }
    }
    
    @Override
    final public void write(int data)
            throws IOException {
        buffer.add((byte) data);
        checkBuffer();
    }
    
    /**
     * @see write(int)
     */
    final public void write(byte data)
            throws IOException {
        buffer.add(data);
        checkBuffer();
    }
    
    @Override
    final public void write(byte[] data)
            throws IOException {
        buffer.add(data);
        checkBuffer();
    }
    
    @Override
    final public void write(byte[] data, int off, int len)
            throws IOException {
        buffer.add(data, off, len);
        checkBuffer();
    }
    
    @Override
    public void flush()
            throws IOException {
        checkBuffer();
        if (buffer.size() != 0) {
            if (fillEnd()) {
                int remaining = buffer.size();
                byte[] data = new byte[blockSize];
                buffer.getRemaining(data, 0);
                getFillBlocks(data, remaining);
                processBlock(data);
                
            } else {
                byte[] data = buffer.getRemaining();
                processBlock(data);
            }
        }
    }
    
    /**
     * Whether the block size must be equal to the value returned by
     * {@link #getNextBlockSize()} when the stream is being closed.
     * 
     * @return {@code true} if the size of the blocks must be exactly
     *     equal to the block size.
     */
    protected abstract boolean fillEnd();
    
    /**
     * Returns the next block size for the {@link #processBlock(byte[])} function. <br>
     * This function will be called exactly once for every block.<br>
     * Furthermore is guarateed that this function will only be called
     * after the {@link #processBlock(byte[])} function has been invoked for the
     * previous block (if such a block exists).
     * 
     * @return the next block size.
     */
    protected abstract int getNextBlockSize();
    
    /**
     * The data block to process. <br>
     * It is guaranteed that the length of {@code data} is equal to the
     * value previously returned by {@link #getNextBlockSize()} if,
     * and only if {@link #fillEnd()} returned {@code true}.
     * 
     * @param data the data to be encrypted.
     * 
     * @throws IOException if the block could not be processed.
     */
    protected abstract void processBlock(byte[] data)
            throws IOException;
    
    /**
     * Fills the given array {@code dest} from index {@link off} onwards
     * with (random) data. Can only be invoked when the stream is being closed
     * and will never be invoked if {@link #fillEnd()} returned {@code false}.
     * 
     * @param dest the array to modify.
     * @param off the index to start the modification of (inclusive).
     */
    protected abstract void getFillBlocks(byte[] dest, int off);
    
    
}
