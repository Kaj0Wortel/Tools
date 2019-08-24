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

package tools.log;


// Java imports
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;


/**
 * Logs the data to an {@link OutputStream}.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class StreamLogger
        extends DefaultLogger {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The stream used to output the log data. */
    private OutputStream stream;
    /** The used character set. */
    private Charset charset = Charset.forName("UTF-8");
    
    
    /* -------------------------------------------------------------------------
     * Constructor.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new stream logger which logs to the default {@code System.out}.
     */
    public StreamLogger() {
        this(System.out);
    }
    
    /**
     * Creates a new stream logger which logs to the given output stream.
     * 
     * @param outputStream The output stream to log to. The default is {@code System.out}.
     */
    public StreamLogger(OutputStream outputStream) {
        super();
        stream = outputStream;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * @return The output stream the logger uses to output data.
     */
    public OutputStream getStream() {
        return stream;
    }
    
    @Override
    public void writeText(String text) throws IOException {
        if (stream != null) {
            stream.write(text.getBytes(charset));
        }
    }
    
    @Override
    protected void close() {
        try {
            if (stream != null) stream.close();
            
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    
    @Override
    protected void flush() {
        try {
            stream.flush();
            
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    
    
}