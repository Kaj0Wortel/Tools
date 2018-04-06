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

package tools.log;


// Tools imports
import tools.MultiTool;


// Java imports
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

/* 
 * Class for logging data.
 * All functions are thread safe.
 */
public class Log extends Logger {
    // The only instance of this class.
    private static Log instance = new Log();
    
    // The writer used to write the log data to the file.
    private static PrintWriter writer;
    
    /* 
     * Only a single private constructor because of singleton design pattern.
     */
    private Log() {
        lock = new ReentrantLock(true);
    }
    
    /* 
     * @return the singleton instance of this class.
     */
    public static synchronized Log getInstance() {
        return instance;
    }
    
    /* 
     * Checks if the writer non-null.
     * If the writer is null, create a new writer.
     * Also in this case, if {@code !append}, write the header of the log file.
     * After calling this method it is guaranteed that either
     * {@code writer != null} or an IOException has been thrown.
     * 
     * @param append whether to append to the previous log file.
     * 
     * Also see: {@link writeHeader()}.
     */
    private void checkWriter(boolean append) throws IOException {
        if (writer != null) return;
        
        writer = new PrintWriter
            (new BufferedWriter(new FileWriter(logFile, append)));
        
        if (!append) writeHeader();
    }
    
    /* 
     * Writes the header of the log file.
     * Here is given that {@code writer != null}.
     */
    private void writeHeader() {
        if (header == null) return;
        writer.println(header.replaceAll("&date&", formatDate(new Date())));
    }
    
    /* 
     * Writes a given text to the log file.
     */
    protected void writeText(String text, Type type, Date timeStamp,
                           boolean useDate) {
        try {
            checkWriter(false);
            
            String dateLine;
            String infoLine;
            
            // Check if there is a writer active.
            // Append if not yet created.
            checkWriter(true);
            
            // Determine the date line
            dateLine = dateFormat.format(timeStamp) + " ";
            if (!useDate) {
                dateLine = MultiTool.fillSpaceRight("", dateLine.length());
            }
            
            // Determine the info line
            infoLine = MultiTool
                .fillSpaceRight("[" + type.toString() + "]", 10);
            
            // Print text
            writer.println(dateLine + infoLine + text);
            
        } catch (IOException e){
            System.err.println(e);
        }
    }
    
    @Override
    protected void writeE(Exception e, Type type, Date timeStamp) {
        if (useFull) {
            String[] text = Arrays.toString(e.getStackTrace()).split(", ");
            String message = e.getClass().getName() + ": " + e.getMessage();
            
            lock.lock();
            try {
                writeText(message, type, timeStamp, useTimeStamp);
                
                for (int i = 0; i < text.length; i++) {
                    writeText(text[i], Type.NONE, timeStamp, false);
                }
                
                flush();
                
            } finally {
                lock.unlock();
            }
            
        } else {
            String message = e.getClass().getName() + ": " + e.getMessage();
            
            lock.lock();
            try {
                writeText(message, Type.ERROR, timeStamp, useTimeStamp);
                flush();
                
            } finally {
                lock.unlock();
            }
        }
    }
    
    @Override
    protected void writeO(Object obj, Type type, Date timeStamp) {
        lock.lock();
        try {
            writeText(obj.toString(), type, timeStamp, useTimeStamp);
            flush();
            
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    protected void setFile(File file, boolean append) {
        try {
            // Close the previous log file.
            close();
            
            // Set the new log file.
            logFile = file;
            
            // Create a new writer.
            checkWriter(append);
            
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    
    @Override
    protected void clear() {
        try {
            // Close the previous log file.
            close();
            
            // Create a new writer.
            checkWriter(false);
            
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    
    @Override
    protected void close() {
        if (writer != null) writer.close();
        writer = null;
    }
    
    @Override
    protected boolean isClosed() {
        return writer == null;
    }
    
    @Override
    protected void flush() {
        if (writer != null) writer.flush();
    }
    
}