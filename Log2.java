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


// Tools imports
import tools.TerminalErrorMessage;


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


public class Log2 extends Loggable {
    final public static int NONE = -1;
    final public static int INFO = 0;
    final public static int WARNING = 1;
    final public static int ERROR = 2;
    final public static int DEBUG = 3;
    
    private static File logFile = new File(System.getProperty("user.dir") + "\\learningGame\\log\\log.txt");
    private static boolean useTimeStamp = true;
    private static boolean useFull = true;
    private static volatile Object writeTextLock = new Object();
    
    @Deprecated
    private Log2() {}
    
    /* 
     * Logs an exception.
     * Is thread-safe.
     * Always prints all lines in one block.
     * 
     * @param e the exception to be logged
     * @param full <optional> denotes whether the exception is fully or short logged.
     * @param showDate <optional> denotes whether the date is shown for the log action.
     */
    public static void write(Exception e) {
        write(e, useFull, useTimeStamp);
    }
    
    public static void write(Exception e, boolean full) {
        write(e, full, useTimeStamp);
    }
    
    public static void write(Exception e, boolean full, boolean showDate) {
        if (full) {
            String[] text = Arrays.toString(e.getStackTrace()).split(", ");
            String message = e.getClass().getName() + ": " + e.getMessage();
            
            synchronized(writeTextLock) {
                writeText(message, showDate, 2);
                
                for (int i = 0; i < text.length; i++) {
                    writeText(text[i], false, NONE);
                }
            }
            
        } else {
            String message = e.getClass().getName() + ": " + e.getMessage();
            
            synchronized(writeTextLock) {
                writeText(message, showDate, ERROR);
            }
        }
    }
    
    /* 
     * Logs an object array.
     * Is thread-safe.
     * Always prints all elements in the array as one block.
     * 
     * If objArr.length > 1 && showDate, then only the first object is
     * written with date, and the other without date.
     * 
     * If objArr == null, then the text "null" is logged.
     * 
     * See write(Object, boolean, boolean) for more detailed info.
     * 
     * @param objArr the Objects to be logged.
     * @param showDate <optional> denotes whether the date is shown for the log action.
     *     Default set by the static variable type useTimeStamp.
     * @param type <optional> denotes the type of log.
     *     Must be one of NONE, INFO, WARNING, ERROR or DEBUG. Default is DEBUG.
     */
    public static void write(Object[] objArr) {
        write(objArr, useTimeStamp, DEBUG);
    }
    
    public static void write(Object[] objArr, boolean showDate) {
        write(objArr, showDate, DEBUG);
    }
    
    public static void write(Object[] objArr, int type) {
        write(objArr, useTimeStamp, type);
    }
    
    public static void write(Object[] objArr, boolean showDate, int type) {
        if (objArr != null) {
            if (objArr.length > 0) {
                synchronized(writeTextLock) {
                    write(objArr[0], showDate, type);
                    
                    for (int i = 1; i < objArr.length; i++) {
                        write(objArr[i], showDate, NONE);
                    }
                }
                
            } else {
                synchronized(writeTextLock) {
                    write("", showDate, type);
                }
            }
            
        } else {
            synchronized(writeTextLock) {
                writeText("null", showDate, type);
            }
        }
    }
    
    /* 
     * Logs an Object.
     * Is thread-safe.
     * 
     * @param obj the object to be logged
     * @param showDate <optional> denotes whether the date is shown for the log action.
     *     Default set by the static variable type useTimeStamp.
     * @param type <optional> denotes the type of log.
     *     Must be one of NONE, INFO, WARNING, ERROR or DEBUG. Default is DEBUG.
     */
    public static void write(Object obj) {
        synchronized(writeTextLock) {
            writeText(obj.toString(), useTimeStamp, DEBUG);
        }
    }
    
    public static void write(Object obj, boolean showDate) {
        synchronized(writeTextLock) {
            writeText(obj.toString(), showDate, DEBUG);
        }
    }
    
    public static void write(Object obj, int type) {
        synchronized(writeTextLock) {
            writeText(obj.toString(), useTimeStamp, type);
        }
    }
    
    public static void write(Object obj, boolean showDate, int type) {
        synchronized(writeTextLock) {
            writeText(obj.toString(), showDate, type);
        }
    }
    
    /* 
     * Logs a string to the logfile
     * 
     * If text == null, then the text "null" is logged.
     * 
     * If text != null, then obj.toString() is logged.
     * 
     * @param text the text to be logged
     * @param showDate denotes whether the date is shown for the log action.
     *     Default set by the static variable type useTimeStamp.
     * @param type denotes the type of log.
     *     Must be one of NONE, INFO, WARNING, ERROR or DEBUG. Default is DEBUG.
     */
    private static void writeText(String text, boolean showDate, int type) {
        String dateLine;
        String infoLine;
        
        // Create file writer (append to/create file)
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)))) {
            if (showDate) {
                // Determine and print date
                DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
                Date date = new Date();
                dateLine = dateFormat.format(date) + " ";
                
            } else {
                dateLine = "             ";
            }
            
            if (type == INFO) {
                infoLine = "[INFO]    ";
                
            } else if (type == WARNING) {
                infoLine = "[WARNING] ";
                
            } else if (type == ERROR) {
                infoLine = "[ERROR]   ";
                
            } else if (type == DEBUG) {
                infoLine = "[DEBUG]   ";
                
            } else {
                infoLine = "          ";
            }
            
            // Print text
            writer.println(dateLine + infoLine + text);
            
        } catch (IOException e) {
            e.printStackTrace();
            new TerminalErrorMessage("Could not create/append the log file: access denied.");
        }
    }
    
    /* 
     * Clears the logfile and puts the current date and time at the top.
     * If the file(-path) does not yet exist, create it.
     */
    public static boolean clear() {
        // Create file writer (overwrite/create file)
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(logFile, false)))) {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy - HH:mm:ss:SSS");
            Date date = new Date();
            writer.println(dateFormat.format(date));
            writer.println();
            writer.close();
            
        } catch (IOException e) {
            e.printStackTrace();
            new TerminalErrorMessage("Could not clear the log file: access denied.");
        }
        
        return true;
    }
    
    /* 
     * Sets the log file.
     * If the file(-path) does not yet exist, create it.
     * In any case clear the file.
     * 
     * @param file denotes the file that is used as logfile.
     */
    public static boolean setLogFile(File file) {
        logFile = file;
        logFile.getParentFile().mkdirs();
        Log2.clear();
        
        return true;
    }
    
    /* 
     * Sets whether the time stamp should be set by default. Default is true.
     * 
     * @param newTimeStamp denotes whether the timestamp should be set by default or not.
     */
    public static void setUseTimeStamp(boolean newTimeStamp) {
        useTimeStamp = newTimeStamp;
    }
    
    /* 
     * Sets whether exceptions should be logged full or not.
     * 
     * @param newFul denotes whether by default full exceptions should be logged.
     */
    public static void setUseFull(boolean newFull) {
        useFull = newFull;
    }
    
    /* 
     * @return the writelock of this log class.
     * 
     * NOTE: Use with care! Uncarefull use of this can result in blocking threads!
     */
    @Deprecated
    public static Object getWriteLock() {
        return writeTextLock;
    }
}