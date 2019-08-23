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


/**
 * This exception should be thrown when not enough data is available.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class InsufficientDataException
        extends IOException {
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a blank exception.
     */
    public InsufficientDataException() {
        super();
    }
    
    /**
     * Creates a new exception with the given message.
     * 
     * @param message The error message.
     */
    public InsufficientDataException(String message) {
        super(message);
    }
    
    /**
     * Creates a new exception with the given message and cause.
     * 
     * @param message The error message.
     * @param cause The cause for this exception.
     */
    public InsufficientDataException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Creates a new exception with the given cause.
     * 
     * @param cause The cause for this exception.
     */
    public InsufficientDataException(Throwable cause) {
        super(cause);
    }
    
    
}
