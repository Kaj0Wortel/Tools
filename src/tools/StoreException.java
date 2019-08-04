/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) July 2019 by Kaj Wortel - all rights reserved               *
 * Contact: kaj.wortel@gmail.com                                             *
 *                                                                           *
 * This file is part of the tools project, which can be found on github:     *
 * https://github.com/Kaj0Wortel/tools                                       *
 *                                                                           *
 * It is allowed to use, (partially) copy and modify this file               *
 * in any way for private use only by using this header.                     *
 * It is not allowed to redistribute any (modifed) versions of this file     *
 * without my permission.                                                    *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package tools;


/**
 * This exception is thrown when a value is attempted to be stored
 * at a location that is not suitable for that value.
 *
 * @version 1.0
 * @author Kaj Wortel
 */
public class StoreException
        extends RuntimeException {
    
    /**
     * Creates a fresh store exception.
     */
    public StoreException() {
        super();
    }
    
    /**
     * Creates a fresh store exception with a message.
     *
     * @param msg The message of the exception.
     */
    public StoreException(String msg) {
        super(msg);
    }
    
    /**
     * Creates a fresh store exception with a cause.
     *
     * @param cause The cause of the exception.
     */
    public StoreException(Throwable cause) {
        super(cause);
    }
    
    /**
     * Creates a fresh store exception with a message and a cause.
     *
     * @param msg The message of the exception.
     * @param cause The cause of the exception.
     */
    public StoreException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    
}
