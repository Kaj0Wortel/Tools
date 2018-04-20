/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2018 by Kaj Wortel - all rights reserved                    *
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


/* 
 * This exception is thrown when a value is attempted to be stored
 * at a location that is not suitable for that value.
 */
public class StoreException
        extends RuntimeException {
    
    public StoreException() {
        super();
    }
    
    public StoreException(String msg) {
        super(msg);
    }
    
    public StoreException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    public StoreException(Throwable cause) {
        super(cause);
    }
    
}