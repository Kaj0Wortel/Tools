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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Extends the {@link BlockByteBuffer} class with concurrent access. <br>
 * This class is thread safe, but also slower compared to {@link BlockByteBuffer}.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class ConcurrentBlockByteBuffer
        extends BlockByteBuffer {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The lock used for the concurrent operations. */
    protected final Lock lock = new ReentrantLock();
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public void add(byte value) {
        lock.lock();
        try {
            super.add(value);
            
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public void add(byte[] values, int off, int len) {
        lock.lock();
        try {
            super.add(values, off, len);
            
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public int size() {
        lock.lock();
        try {
            return super.size();
            
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public boolean isEmpty() {
        lock.lock();
        try {
            return super.isEmpty();
            
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public byte getByte() {
        lock.lock();
        try {
            return super.getByte();
            
        } finally {
            lock.unlock();
        }
    }  
    
    @Override
    public byte[] get(byte[] dest, int off, int len) {
        lock.lock();
        try {
            return super.get(dest, off, len);
            
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public byte[] get(int len) {
        lock.lock();
        try {
            return super.get(len);
            
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public byte[] getRemaining() {
        lock.lock();
        try {
            return super.getRemaining();
            
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public int getRemaining(byte[] dest, int off) {
        lock.lock();
        try {
            return super.getRemaining(dest, off);
            
        } finally {
            lock.unlock();
        }
    }
    
    
}
