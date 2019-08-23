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
 * Wrapper class to convert an {@link InputStream} to an
 * {@link ExceptionInputStream} and vice verca.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class ExceptionInputStreamWrapper
        extends InputStream
        implements ExceptionInputStream {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The wrapped input stream. {@code null} if {@link #eis} {@code != null}. */
    private final InputStream is;
    /** The wrapped exception input stream. {@code null} if {@link #is} {@code != null}. */
    private final ExceptionInputStream eis;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Wraps an {@link InputStream} in an {@link ExceptionInputStream}.
     * 
     * @param is The input stream to wrap.
     */
    public ExceptionInputStreamWrapper(InputStream is) {
        this.is = is;
        this.eis = null;
    }
    
    /**
     * Wraps an {@link ExceptionInputStream} in an {@link InputStream}.
     * 
     * @param eis The exception input stream to wrap.
     */
    public ExceptionInputStreamWrapper(ExceptionInputStream eis) {
        this.is = null;
        this.eis = eis;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public int read()
            throws IOException {
        return (is == null ? eis.read() : is.read());
    }
    
    @Override
    public int read(byte[] b)
            throws IOException {
        return (is == null ? eis.read(b) : is.read(b));
    }
    
    @Override
    public int read(byte[] b, int off, int len)
            throws IOException {
        return (is == null ? eis.read(b, off, len) : is.read(b, off, len));
    }
    
    @Override
    public void close()
            throws IOException {
        if (is == null) eis.close();
        else is.close();
    }
    
    @Override
    public void mark(int readLimit) {
        if (is == null) eis.mark(readLimit);
        else is.mark(readLimit);
    }
    
    @Override
    public boolean markSupported() {
        return (is == null ? eis.markSupported() : is.markSupported());
    }
    
    @Override
    public byte[] readAllBytes()
            throws IOException {
        return (is == null ? eis.readAllBytes() : is.readAllBytes());
    }
    
    @Override
    public int readNBytes(byte[] b, int off, int len)
            throws IOException {
        return (is == null ? eis.readNBytes(b, off, len) : is.readNBytes(b, off, len));
    }
    
    @Override
    public byte[] readNBytes(int len)
            throws IOException {
        return (is == null ? eis.readNBytes(len) : is.readNBytes(len));
    }
    
    @Override
    public void reset()
            throws IOException {
        if (is == null) eis.reset();
        else is.reset();
    }
    
    @Override
    public long skip(long n)
            throws IOException {
        return (is == null ? eis.skip(n) : is.skip(n));
    }
    
    @Override
    public long transferTo(OutputStream out)
            throws IOException {
        return (is == null ? eis.transferTo(out) : is.transferTo(out));
    }
    
    
}
