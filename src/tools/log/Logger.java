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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;


/**
 * Abstract base class for log classes.
 * 
 * @todo
 * - Cleanup
 * - Add class based-logging.
 * - Add logger for a file tree.
 * - Add group logging (invent what and how first (maybe with a lock?)).
 * - Allow the logger class to be 'paused':
 *   - All log operations are recorded.
 *   - After 'unpausing' the logger, all log operations are executed at once as
 *     if they were just invoked.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public abstract class Logger {
    
    /* -------------------------------------------------------------------------
     * Constants.
     * -------------------------------------------------------------------------
     */
    /** The default date format */
    protected static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("HH:mm:ss.SSS");
    
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The message which should be written upon termination of the program. */
    private static Object terminateMsg = null;
    /** The type of the terminal message. */
    private static Type terminateType = Type.INFO;
    
    /** The default logger. */
    private static Logger defLog;
    
    /** The header to print before writing data. */
    protected static String header;
    
    /** Whether to use the full exception notation as default or not. */
    protected static boolean useFull = true;
    
    
    /** Whether to use a time stamp by default or not. */
    protected static boolean useTimeStamp = true;
    /** The date format used for logging a time stamp. */
    protected static DateFormat dateFormat = DEFAULT_DATE_FORMAT;
    
    /** The lock of each logger. */
    protected Lock lock;
    
    
    /* -------------------------------------------------------------------------
     * Inner classes..
     * -------------------------------------------------------------------------
     */
    /**
     * Enum class denoting the severity type of a log action.
     */
    public enum Type {
        
        /**
         * This type is used when the type field should be blank.
         */
        NONE,
        
        /**
         * This type is used to notify the reader of the log that the text
         * contains plain information.
         */
        INFO,
        
        /**
         * This type is used to notify the reader of the log that something
         * might have gone wrong, but it was not fatal.
         */
        WARNING,
        
        /**
         * This type is used to notify the reader of the log that something
         * went wrong and, most likely, the application could not recover
         * from the error.
         */
        ERROR,
        
        /**
         * This type is used to notify the reader of the log that this text
         * is only temporary and should be removed when the component is finished.
         */
        DEBUG;
        
        
    }
    
    
    /* -------------------------------------------------------------------------
     * Tool functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Checks whether the default logger is not null.
     * If it is {@code null}, set the default logger to {@link NullLogger}.
     */
    private static void checkDef() {
        if (defLog == null) defLog = NullLogger.getInstance();
    }
    
    /**
     * Formats the date using the date format
     * 
     * @param timeStamp the time stamp to be converted.
     * 
     * @return A string representing the date.
     */
    protected static String formatDate(Date timeStamp) {
        return dateFormat.format(timeStamp);
    }
    
    /**
     * Checks the given type on null values.
     * 
     * @param type The type to be checked. May be null.
     * 
     * @return The provided type if {@code type != null}. {@link Type#NONE} otherwise.
     */
    protected static Type checkType(Type type) {
        return (type != null ? type : Type.NONE);
    }
    
    
    /* -------------------------------------------------------------------------
     * Log function to be implemented
     * -------------------------------------------------------------------------
     */
    /**
     * Writes the exception to a log file. <br>
     * It is given that {@code type != null} and {@code timeStamp != null}.
     * 
     * @param e The exception to be written.
     * @param timeStamp The time stamp in milliseconds precision.
     */
    protected void writeE(Exception e, Date timeStamp) {
        writeE(e, Type.ERROR, new Date());
    }
    
    /**
     * Writes the exception to a log file. <br>
     * It is given that {@code type != null} and {@code timeStamp != null}.
     * 
     * @param e The exception to be written.
     * @param type The severity type. The default is {@link Type#ERROR}.
     * @param timeStamp The time stamp in milliseconds precision.
     */
    protected abstract void writeE(Exception e, Type type, Date timeStamp);
    
    /**
     * Writes the object to a log file. <br>
     * It is given that {@code type != null} and {@code timeStamp != null}.
     * 
     * @param obj The object to be written.
     * @param timeStamp The time stamp in milliseconds precision.
     */
    protected void writeO(Object obj, Date timeStamp) {
        writeO(obj, Type.DEBUG, timeStamp);
    }
    
    /**
     * Writes the object to a log file. <br>
     * It is given that {@code type != null} and {@code timeStamp != null}.
     * 
     * @param obj The object to be written.
     * @param type The severity type.
     * @param timeStamp The time stamp in milliseconds precision.
     */
    protected abstract void writeO(Object obj, Type type, Date timeStamp);
    
    /**
     * Writes an object array to a log file.
     * Ensures that all data is logged consecutively. <br>
     * It is given that {@code type != null} and {@code timeStamp != null}.
     * 
     * @param objArr The object array to be written.
     * @param timeStamp The time stamp in milliseconds precision.
     */
    protected void writeOA(Object[] objArr, Date timeStamp) {
        writeOA(objArr, Type.DEBUG, timeStamp);
    }
    
    /**
     * Writes an object array to a log file.
     * Ensures that all data is logged consecutively. <br>
     * It is given that {@code type != null} and {@code timeStamp != null}.
     * 
     * @param objArr The object array to be written.
     * @param type The severity type.
     * @param timeStamp The time stamp in milliseconds precision.
     */
    protected void writeOA(Object[] objArr, Type type, Date timeStamp) {
        if (defLog.lock != null) defLog.lock.lock();
        try {
            if (objArr == null) {
                writeO("null", timeStamp);
                
            } else {
                for (Object obj : objArr) {
                    if (obj == null) {
                        writeO("null", type, timeStamp);
                        
                    } else if (obj instanceof Exception) {
                        defLog.writeE((Exception) obj, type, timeStamp);
                        
                    } else if (obj.getClass().isArray()) {
                        writeOA((Object[]) obj, type, timeStamp);
                        
                    } else {
                        defLog.writeO(obj, type, timeStamp);
                    }
                }
            }
            
        } finally {
            if (defLog.lock != null) defLog.lock.unlock();
        }
    }
    
    /**
     * Closes the log file and releases system resources.
     */
    protected abstract void close();
    
    /**
     * Flushes the writer.
     */
    protected abstract void flush();
    
    
    /**-------------------------------------------------------------------------
     * Static delegate functions
     * -------------------------------------------------------------------------
     */
    /**
     * Delegates the static write action to the default logger instance.
     * 
     * @see #writeE(Exception, Date)
     */
    public static void write(Exception e) {
        checkDef();
        defLog.writeE(e, new Date());
    }
    
    /**
     * Delegates the static write action to the default logger instance.
     * 
     * @see #writeE(Exception, Type, Date)
     */
    public static void write(Exception e, Type type) {
        checkDef();
        defLog.writeE(e, checkType(type), new Date());
    }
    
    /**
     * Delegates the static write action to the default logger instance.
     * 
     * @see #writeO(Object, Date)
     */
    public static void write(Object obj) {
        checkDef();
        defLog.writeO(obj, new Date());
    }
    
    /**
     * Delegates the static write action to the default logger instance.
     * 
     * @see #writeO(Object, Type, Date)
     */
    public static void write(Object obj, Type type) {
        checkDef();
        defLog.writeO(obj, checkType(type), new Date());
    }
    
    /**
     * Delegates the static write action to the default logger instance. <br>
     * Ensures that all data is logged consecutively and with the same time stamp.
     * 
     * @param objArr the object array to be logged.
     * 
     * @see #writeOA(Object[], Date)
     */
    public static void write(Object[] objArr) {
        checkDef();
        defLog.writeOA(objArr, new Date());
    }
    
    /**
     * Delegates the static write action to the default logger instance. <br>
     * Ensures that all data is logged consecutively.
     * 
     * @param objArr the object array to be logged.
     * 
     * @see #writeOA(Object[], Type, Date)
     */
    public static void write(Object[] objArr, Type type) {
        checkDef();
        defLog.writeOA(objArr, checkType(type), new Date());
    }
    
    /**
     * Closes the logger and releases system resources.
     * 
     * @apiNote
     * No default logger is created here if none existed yet.
     * 
     * @see #close()
     */
    public static void closeLog() {
        if (defLog != null) defLog.close();
    }
    
    /**
     * Flushes the writer.
     * 
     * @apiNote
     * No default logger is created here if none existed yet.
     */
    public static void flushLog() {
        if (defLog != null) defLog.flush();
    }
    
    /**
     * Changes whether to use the time stamp.
     * 
     * @param useTimeStamp Whether to use the time stamp or not.
     */
    public static void setUsagetimeStamp(boolean useTimeStamp) {
        Logger.useTimeStamp = useTimeStamp;
    }
    
    /**
     * Changes whether to use the full log notation.
     * 
     * @param useFull whether to use the full notation or not.
     */
    public static void setUsageFull(boolean useFull) {
        Logger.useFull = useFull;
    }
    
    /**
     * @return the default logger.
     */
    public static Logger getLog() {
        checkDef();
        return defLog;
    }
    
    /**
     * @param log the default logger.
     */
    public static void setDefaultLogger(Logger log) {
        defLog = log;
    }
    
    /**
     * Sets the message that will be logged when the application shuts down.
     * Use {@code msg == null} to prevent any message to be written.
     * Additionally, no message will be printed if no default log is selected.
     * 
     * @param msg The message to be logged.
     */
    public static void setShutDownMessage(Object msg) {
        setShutDownMessage(msg, Type.INFO);
    }
    
    /**
     * Sets the message that will be logged when the application shuts down.
     * Use {@code msg = null} to prevent any message to be written.
     * Additionally, no message will be printed if no default log is selected.
     * 
     * @param msg The message to be logged.
     * @param type The severity type of the message. The default type is {@link Type#INFO}.
     */
    public static void setShutDownMessage(Object msg, Type type) {
        terminateMsg = msg;
        terminateType = checkType(type);
    }
    
    /**
     * @return The message that will be logged when the application shuts down.
     */
    public static Object getShutDownMessage() {
        return terminateMsg;
    }
    
    /**
     * @return The type of the message that will be logged when the application shuts down.
     */
    public static Type getShutDownType() {
        return terminateType;
    }
    
    /**
     * Sets the date format for logging.
     * 
     * @param format The format used for a {@code SimpleDateFormat}.
     */
    public static void setDateFormat(String format) {
        setDateFormat(new SimpleDateFormat(format));
    }
    
    /**
     * Sets the date format for logging.
     * Use {@code null} to reset to default.
     * 
     * @param df The date format used for logging.
     */
    public static void setDateFormat(DateFormat df) {
        dateFormat = (df == null ? DEFAULT_DATE_FORMAT : df);
    }
    
    /**
     * @return The date format used to log the date.
     */
    public static DateFormat getDateFormat() {
        return dateFormat;
    }
    
    /**
     * Sets the log header. <br>
     * Use {@code null} to have no header. <br>
     * Use {@code &date&} to use the time stamp of when the log was created.
     * 
     * @param header The header to print before writing data
     */
    public static void setLogHeader(String header) {
        Logger.header = header;
    }
    
    /**
     * Adds a shutdown hook for writing the terminate message and
     * closing the stream resources of the log file.
     */
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (Logger.getLog() != null) {
                // Write the terminal message (if any) to the default log.
                Object msg = Logger.getShutDownMessage();
                if (msg != null) {
                    write(msg, Logger.getShutDownType());
                }
                
                // Close the log
                Logger.closeLog();
            }
        }, "Shutdown-Log-Thread"));
    }
    
    
}
