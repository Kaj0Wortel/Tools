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
import java.util.Date;


/**
 * This is a logger class that ignores all log action.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class NullLogger
        extends Logger {
    
    /* -------------------------------------------------------------------------
     * Constants.
     * -------------------------------------------------------------------------
     */
    /** The only instance of this class. */
   private static final NullLogger instance = new NullLogger();
    
   
    /* -------------------------------------------------------------------------
     * Constructor..
     * -------------------------------------------------------------------------
     */
    /**
     * Only a single private constructor because of singleton design pattern.
     */
    private NullLogger() { }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * @return A null logger instance.
     */
    public static synchronized NullLogger getInstance() {
        return instance;
    }
    
    @Override
    protected void writeE(Exception e, Date timeStamp) { }
    
    @Override
    protected void writeE(Exception e, Type type, Date timeStamp) { }
    
    @Override
    protected void writeO(Object obj, Date timeStamp) { }
    
    @Override
    protected void writeO(Object obj, Type type, Date timeStamp) { }
    
    @Override
    protected void writeOA(Object[] objArr, Date timeStamp) { }
    
    @Override
    protected void writeOA(Object[] objArr, Type type, Date timeStamp) { }
    
    @Override
    protected void close() { }
    
    @Override
    protected void flush() { }
    
    
}