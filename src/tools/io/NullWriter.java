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
import java.io.Writer;


/**
 * This is a writer implementation which ignores all data written to it.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class NullWriter
        extends Writer {
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new writer which ignores all data written to it.
     */
    public NullWriter() { }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public void close() { }
    
    @Override
    public void flush() { }
    
    @Override
    public void write(char[] c, int i, int i2) { }
    
    
}