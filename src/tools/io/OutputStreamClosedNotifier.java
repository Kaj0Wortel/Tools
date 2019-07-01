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


// Tools imports
import tools.event.StreamClosedListener;


// Java imports
import java.io.IOException;
import java.io.OutputStream;


/**
 * Wrapper class for attaching a listener for detecting
 * the closing of a stream.
 * 
 * @author Kaj Wortel (0991586)
 */
public class OutputStreamClosedNotifier
        extends OutputStream {
    
    final private OutputStream os;
    private StreamClosedListener listener = null;
    private volatile boolean closed = false;
    
    
    /**-------------------------------------------------------------------------
     * Constructor.
     * -------------------------------------------------------------------------
     */
    /**
     * Wrapper constructor.
     * 
     * @param os output stream to delegate the writing to.
     */
    public OutputStreamClosedNotifier(OutputStream os) {
        if (os == null) throw new NullPointerException(
                "Given stream was null!");
        this.os = os;
    }
    
    
    /**-------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * {@inheritDoc}
     */
    @Override
    public void write(int b)
            throws IOException {
        try {
            os.write(b);
            
        } catch (IOException e) {
            if (!closed) {
                closed = true;
                notifyListener(false);
            }
            throw new IOException(e);
        }
    }
    
    @Override
    public void write(byte[] b)
            throws IOException {
        try {
            os.write(b);
            
        } catch (IOException e) {
            if (!closed) {
                closed = true;
                notifyListener(false);
            }
            throw new IOException(e);
        }
    }
    
    @Override
    public void write(byte[] b, int off, int len)
            throws IOException {
        try {
            os.write(b, off, len);
            
        } catch (IOException e) {
            if (!closed) {
                closed = true;
                notifyListener(false);
            }
            throw new IOException(e);
        }
    }
    
    @Override
    public void flush()
            throws IOException {
        try {
            os.flush();
            
        } catch (IOException e) {
            if (!closed) {
                closed = true;
                notifyListener(false);
            }
            throw new IOException(e);
        }
    }
    
    @Override
    public void close()
            throws IOException {
        if (!closed) {
            closed = true;
            try {
                os.close();
                
            } catch (IOException e) {
                notifyListener(false);
                return;
            }
            notifyListener(true);
            
        } else {
            os.close();
        }
    }
    
    /**
     * Notifies the listener about the closed stream.
     */
    private void notifyListener(boolean gracefull) {
        if (listener != null) listener.streamClosed(gracefull);
    }
    
    /**
     * @param listener the new listener which will be notified when
     *     the steam is closed.
     */
    public void setListener(StreamClosedListener listener) {
        this.listener = listener;
    }
    
    
}
