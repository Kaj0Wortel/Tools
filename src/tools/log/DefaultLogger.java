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


// Tools imports
import tools.MultiTool;
import tools.Var;


// Java imports
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Abstract logger implementation which can be used as basis for a logger.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public abstract class DefaultLogger
        extends Logger {
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Default constructor which creates a new fair lock for concurrent operations.
     */
    public DefaultLogger() {
        super.lock = new ReentrantLock(true);
    }
    
    /**
     * Constructor which initializes the lock with the given lock.
     * 
     * @param lock The lock to use for this class.
     */
    public DefaultLogger(Lock lock) {
        super.lock = lock;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Writes the given text to the log.
     * 
     * @param text The text to be write to the log.
     * 
     * @implSpec
     * The default exception handeling is to print exceptions to the {@code System.err stream}.
     * 
     * @throws IOException If some IO error occured.
     */
    protected abstract void writeText(String text)
            throws IOException;
    
    
    @Override
    protected void writeE(Exception e, Type type, Date timeStamp) {
        if (useFull) {
            String[] text = Arrays.toString(e.getStackTrace()).split(", ");
            String message = e.getClass().getName() + ": " + e.getMessage();
            
            if (lock != null) lock.lock();
            try {
                processText(message, type, timeStamp, useTimeStamp);
                
                for (int i = 0; i < text.length; i++) {
                    processText(text[i], Type.NONE, timeStamp, false);
                }
                
                flush();
                
            } finally {
                if (lock != null) lock.unlock();
            }
            
        } else {
            String message = e.getClass().getName() + ": " + e.getMessage();
            
            if (lock != null) lock.lock();
            try {
                processText(message, Type.ERROR, timeStamp, useTimeStamp);
                flush();
                
            } finally {
                if (lock != null) lock.unlock();
            }
        }
    }
    
    @Override
    protected void writeO(Object obj, Type type, Date timeStamp) {
        if (lock != null) lock.lock();
        try {
            if (obj == null) obj = "null";
            String[] strArr = obj.toString().split(Var.LS);
            for (int i = 0; i < strArr.length; i++) {
                processText(strArr[i], type, timeStamp, useTimeStamp);
            }
            flush();
            
        } finally {
            if (lock != null) lock.unlock();
        }
    }
    
    /**
     * Processes the text with the given attributes to a single String.
     * 
     * @param text The text to be processed.
     * @param type The type of logging.
     * @param timeStamp The time stamp of the logging.
     * @param useDate Whether the time should be used in this logging.
     */
    protected void processText(String text, Type type, Date timeStamp, boolean useDate) {
        try {
            String dateLine;
            String infoLine;
            
            // Determine the date line
            dateLine = dateFormat.format(timeStamp) + " ";
            if (!useDate) {
                dateLine = MultiTool.fillSpaceRight("", dateLine.length());
            }
            
            // Determine the info line
            if (type == Type.NONE) {
                infoLine = MultiTool.fillSpaceRight("", 10);
                
            } else {
                infoLine = MultiTool
                        .fillSpaceRight("[" + type.toString() + "]", 10);
            }
            
            writeText(dateLine + infoLine + text + Var.LS);
            
        } catch (IOException e){
            System.err.println(e);
        }
            
    }
    
    /**
     * Writes the header of the log file if it was not yet written.
     */
    protected void writeHeader() {
        try {
            if (header == null) return;
            writeText(header.replaceAll("&date&", formatDate(new Date())) + Var.LS);
            
        } catch (IOException e){
            System.err.println(e);
        }
    }
    
}