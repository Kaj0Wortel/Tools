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

package tools.iterable;


// Java imports
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;


/**
 * 
 * 
 * @author Kaj Wortel
 */
public class IteratorToInputStream
        extends InputStream {
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The iterator to obtain the data from. */
    final private Iterator<Byte> it;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new input stream from the given iterator.
     * 
     * @param it the iterator to obtain the data from.
     */
    public IteratorToInputStream(Iterator<Byte> it) {
        this.it = it;
    }
    
    /**
     * Creates a new input stream from the iterator of the given iterable.
     * 
     * @param it the iterable to obtain the data from.
     */
    public IteratorToInputStream(Iterable<Byte> it) {
        this.it = it.iterator();
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public int read()
            throws IOException {
        if (it.hasNext()) return it.next() & 0xFF;
        else return -1;
    }
    
    
}
