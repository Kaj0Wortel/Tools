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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Logger implementation which logs the data to a file.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class FileLogger
        extends DefaultLogger {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The default log file to write to. */
    protected File logFile;
    /** The writer used to write the log data to the file. */
    private PrintWriter writer;
    /** Whether to append to the given file. */
    private boolean append;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new file logger which logs to the given file.
     * By default, removes all data from the file at initialization, but
     * appends after closing an re-opening.
     * 
     * @param fileName The name of the file to log to.
     * 
     * @throws IOException If some IO error occured.
     */
    public FileLogger(String fileName)
            throws IOException {
        this(new File(fileName));
    }
    
    /**
     * Creates a new file logger which logs to the given file.
     * By default, removes all data from the file at initialization, but
     * appends after closing an re-opening.
     * 
     * @param file The file to log to.
     * 
     * @throws IOException If some IO error occured.
     */
    public FileLogger(File file)
            throws IOException {
        this(file, true, false);
    }
    
    /**
     * Creates a new file logger which logs to the given file.
     * It only appends the data for the first time if {@code appendBeginOnly == true},
     * and after being closed an re-opened it appends data only if {@code append == true}.
     * 
     * @param fileName The Name of the file to log to.
     * @param append Whether to append to the log file after the stream has been closed
     *     and then re-opened.
     * @param appendBeginOnly Whether to append to the logfile at initialization.
     * 
     * @throws IOException If some IO error occured.
     */
    public FileLogger(String fileName, boolean append, boolean appendBeginOnly)
            throws IOException {
        this(new File(fileName), append, appendBeginOnly);
    }
    
    /**
     * Creates a new file logger which logs to the given file.
     * It only appends the data for the first time if {@code appendBeginOnly == true},
     * and after being closed an re-opened it appends data only if {@code append == true}.
     * 
     * @param file The file to log to.
     * @param append Whether to append to the log file after the stream has been closed
     *     and then re-opened.
     * @param appendBeginOnly Whether to append to the logfile at initialization.
     * 
     * @throws IOException If some IO error occured.
     */
    public FileLogger(File file, boolean append, boolean appendBeginOnly)
            throws IOException {
        this.logFile = file;
        file.getParentFile().mkdirs();
        this.append = append;
        createWriter(appendBeginOnly);
        if (!appendBeginOnly) writeHeader();
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Checks if the writer non-null and opened.
     * <ul>
     *   <li> If the writer is {@code null}, a new writer is created and overwrites
     *        the old file of the file and write the header. </li>
     *   <li> If the writer is not {@code null} but is closed, a new writer
     *        is created and appends to the file. </li>
     * </ul>
     * After calling this function it is guaranteed that either an {@link IOException} has
     * been thrown, or {@code writer != null}.
     * 
     * @throws IOException If some IO error occured.
     * 
     * @see #writeHeader()
     */
    protected void checkWriter()
            throws IOException {
        if (writer != null || !isClosed()) return;
        createWriter(append);
    }
    
    /**
     * Creates a new writer.
     * 
     * @param append whether to append to the current log file or to
     *     overwrite the file.
     */
    protected void createWriter(boolean append)
            throws IOException {
        // Close any previously open writers.
        close();
        
        // Create the new writer.
        writer = new PrintWriter(new BufferedWriter(new FileWriter(logFile, append)));
        
        // Add a header for clean files.
        if (!append) writeHeader();
    }
    
    @Override
    protected void writeText(String text)
            throws IOException {
        checkWriter();
        writer.print(text);
    }
    
    /**
     * Sets the log file.
     * 
     * @param fileName The name of the new log file.
     * @param append Whether to append to the new log file.
     */
    protected void setFile(String fileName, boolean append) {
        setFile(new File(fileName), append);
    }
    
    /**
     * Sets the log file.
     * 
     * @param file The name of the log file.
     * @param append Whether to append to the new log file.
     */
    protected void setFile(File file, boolean append) {
        try {
            // Close the previous log file.
            close();
            
            // Set the new log file.
            logFile = file;
            
            // Create a new writer.
            createWriter(append);
            
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    
    /**
     * Clears the log file.
     */
    protected void clear() {
        try {
            // Close the previous log file.
            close();
            
            // Create a new writer.
            createWriter(false);
            
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    
    @Override
    protected void close() {
        if (writer != null) writer.close();
        writer = null;
    }
    
    /**
     * @return whether the log file is closed.
     */
    protected boolean isClosed() {
        return writer == null;
    }
    
    @Override
    protected void flush() {
        if (writer != null) writer.flush();
    }
    
    /**
     * Sets whether to append to the log file after the stream
     * has been closed and then re-opened.
     * 
     * @param append Whether to append to the log file.
     */
    public void setAppend(boolean append) {
        this.append = append;
    }
    
    /**
     * @return Whether to append to the log file after the stream
     *     has been closed and then re-opened.
     */
    public boolean usesAppend() {
        return append;
    }
    
    
}