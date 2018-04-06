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


// Java imports
import java.io.File;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.HashSet;
import java.util.Date;
import java.util.concurrent.locks.Lock;


/* 
 * Abstract class for log classes.
 * 
 * All log classes should use the singleton design pattern, and therefore
 * include the static method {@code getInstance}.
 */
public abstract class Logger {
    /* Constants */
    // The default log file.
    final protected static File DEFAULT_LOG_FILE
        = new File(System.getProperty("user.dir") + "\\tools\\log\\log.log");
    
    // The default date format
    final protected static DateFormat DEFAULT_DATE_FORMAT
        = new SimpleDateFormat("HH:mm:ss.SSS");
    
    /* Static variables */
    // The terminal message and type.
    private static String[] terminateMsg = null;
    private static Type terminateType = Type.INFO;
    
    // The default logger object.
    private static Logger defLog;
    
    // The header on each log file.
    protected static String header;
    
    // Whether to use a timeStamp as default or not.
    protected static boolean useTimeStamp = true;
    
    // Whetehr to use the full exception notation as default or not.
    protected static boolean useFull = true;
    
    // The date format used for logging
    protected static DateFormat dateFormat = DEFAULT_DATE_FORMAT;
    
    /* Non-static variables */
    // The lock of the writer.
    protected Lock lock;
    
    // The log file to write to.
    protected File logFile = DEFAULT_LOG_FILE;
    
    // Enum denoting the severity type of a log action.
    public enum Type {
        NONE, INFO, WARNING, ERROR, DEBUG;
    }
    
    
    // Default constructor.
    protected Logger() { }
    
    
    /* 
     * Checks whether the default logger is not null.
     * If it is null, set the default logger to defaultLogger.
     */
    private static void checkDef() {
        if (defLog == null) defLog = NullLog.getInstance();
    }
    
    /* 
     * Formats the date using the date format
     * 
     * @param timeStamp the time stamp to be converted.
     * @return a {@code String} representing the date.
     */
    protected static String formatDate(Date timeStamp) {
        return dateFormat.format(timeStamp);
    }
    
    /* 
     * Checks the given type on null values.
     * 
     * @param type the type to be checked. May be null.
     * @return if {@code type != null} then the provided type.
     *     {@code Type.NONE} otherwise.
     */
    protected static Type checkType(Type type) {
        return (type != null
                    ? type
                    : Type.NONE);
    }
    
    
    /* -------------------------------------------------------------------------
     * Log function to be implement
     * -------------------------------------------------------------------------
     */
    /* 
     * Writes the exception to a log file.
     * It is given that {@code type != null} and {@code timeStamp != null}.
     * 
     * @param e the exception to be written.
     * @param type the severity type.
     * @param timeStamp the time stamp in milliseconds precision.
     */
    protected void writeE(Exception e, Date timeStamp) {
        writeE(e, Type.ERROR, new Date());
    }
    
    abstract protected void writeE(Exception e, Type type, Date timeStamp);
    
    /* 
     * Writes the object to a log file.
     * It is given that {@code type != null} and {@code timeStamp != null}.
     * 
     * @param obj the object to be written.
     * @param type the severity type.
     * @param timeStamp the time stamp in milliseconds precision.
     */
    protected void writeO(Object obj, Date timeStamp) {
        writeO(obj, Type.DEBUG, timeStamp);
    }
    
    abstract protected void writeO(Object obj, Type type, Date timeStamp);
    
    /* 
     * Writes an object array to a log file.
     * Ensures that all data is logged consecutively.
     * It is given that {@code type != null} and {@code timeStamp != null}.
     * 
     * @param objArr the object array to be written.
     * @param type the severity type.
     * @param timeStamp the time stamp in milliseconds precision.
     */
    protected void writeOA(Object[] objArr, Date timeStamp) {
        writeOA(objArr, Type.DEBUG, timeStamp);
    }
    
    protected void writeOA(Object[] objArr, Type type,
                                    Date timeStamp) {
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
                        
                    } else {
                        defLog.writeO(obj, type, timeStamp);
                    }
                }
            }
            
        } finally {
            if (defLog.lock != null) defLog.lock.unlock();
        }
    }
    
    /* 
     * Sets the log file.
     * 
     * @param file the new log file to be used.
     * @param append whether to append to the new file.
     *     If false, then the file is cleared and the header is printed.
     */
    abstract protected void setFile(File file, boolean append);
    
    /* 
     * Clears the current log file.
     */
    abstract protected void clear();
    
    /* 
     * Closees the log file and releases system resources.
     */
    abstract protected void close();
    
    /* 
     * @return whether the log file is closed.
     */
    abstract protected boolean isClosed();
    
    /* 
     * Flushes the writer.
     */
    abstract protected void flush();
    
    
    /* -------------------------------------------------------------------------
     * Static delegate functions
     * -------------------------------------------------------------------------
     */
    /* 
     * Delegates the static write action to the default logger singleton.
     * 
     * See {@link writeE(Exception, Date)}.
     */
    public static void write(Exception e) {
        checkDef();
        defLog.writeE(e, new Date());
    }
    
    /* 
     * Delegates the static write action to the default logger singleton.
     * 
     * See {@link writeE(Exception, Type, Date)}.
     */
    public static void write(Exception e, Type type) {
        checkDef();
        defLog.writeE(e, checkType(type), new Date());
    }
    
    /* 
     * Delegates the static write action to the default logger singleton.
     * 
     * See {@link writeO(Object, Date)}.
     */
    public static void write(Object obj) {
        checkDef();
        defLog.writeO(obj, new Date());
    }
    
    /* 
     * Delegates the static write action to the default logger singleton.
     * 
     * See {@link writeO(Object, Type, Date)}.
     */
    public static void write(Object obj, Type type) {
        checkDef();
        defLog.writeO(obj, checkType(type), new Date());
    }
    
    /* 
     * Delegates the static write action to the default logger singleton.
     * Ensures that all data is logged consecutively and with the same
     * time stamp.
     * 
     * @param objArr the object array to be logged.
     * 
     * See {@link writeOA(Object[], Date)}
     */
    public static void write(Object[] objArr) {
        checkDef();
        defLog.writeOA(objArr, new Date());
    }
    
    /* 
     * Delegates the static write action to the default logger singleton.
     * Ensures that all data is logged consecutively.
     * 
     * @param objArr the object array to be logged.
     * 
     * See {@link writeOA(Object[], Type, Date)}.
     */
    public static void write(Object[] objArr, Type type) {
        checkDef();
        defLog.writeOA(objArr, checkType(type), new Date());
    }
    
    /* 
     * Sets the log file.
     * 
     * @param fileName the name of the new log file.
     * @param file the name of the log file.
     * @param append whether to append to the new log file.
     * 
     * See {@link setFile()}.
     */
    public static void setLogFile(String fileName, boolean append) {
        setLogFile(new File(fileName), append);
    }
    
    public static void setLogFile(File file, boolean append) {
        checkDef();
        defLog.setFile(file, append);
    }
    
    /* 
     * Clears the log file.
     * 
     * See {@link clear()}.
     */
    public static void clearLog() {
        checkDef();
        defLog.clear();
    }
    
    /* 
     * Closes the log file and releases system resources.
     * Note that no default log is created here if none existed yet.
     * 
     * See {@link close()}
     */
    public static void closeLog() {
        if (defLog != null) defLog.close();
    }
    
    /* 
     * @return checks if the log is closed.
     * Note that no default log is created here if none existed yet.
     */
    public static boolean isLogClosed() {
        return defLog == null || defLog.isClosed();
    }
    
    /* 
     * Flushes the writer.
     * Note that no default log is created here if none existed yet.
     */
    public static void flushLog() {
        if (defLog != null) defLog.flush();
    }
    
    /* 
     * Changes whether to use the time stamp.
     * 
     * @param useTS whether to use the time stamp or not.
     */
    public static void setUsagetimeStamp(boolean useTS) {
        flushLog();
        useTimeStamp = useTS;
    }
    
    /* 
     * Changes whether to use the full log notation.
     * 
     * @param useFull whether to use the full notation or not.
     */
    public static void setUsageFull(boolean useFull) {
        flushLog();
        Logger.useFull = useFull;
    }
    
    /* 
     * @return the default logger.
     */
    public static Logger getLog() {
        checkDef();
        return defLog;
    }
    
    /* 
     * @param log the default logger.
     */
    public static void setDefaultLogger(Logger log) {
        closeLog();
        defLog = log;
    }
    
    /* 
     * Sets the message that will be logged when the application shuts down.
     * Use {@code null} to prevent any message to be printed.
     * Note that no message will be printed if no default log is selected.
     * 
     * @param msg the message to be logged.
     */
    public static void setShutDownMessage(String[] msg, Type type) {
        terminateMsg = msg;
        terminateType = checkType(type);
    }
    
    /* 
     * Sets the date format for logging.
     * 
     * @param format the format used for a {@code SimpleDateFormat}.
     */
    public static void setDateFormat(String format) {
        setDateFormat(new SimpleDateFormat(format));
    }
    
    /* 
     * Sets the date format for logging.
     * Use {@code null} to reset to default.
     * 
     * @param df the date format used for logging.
     */
    public static void setDateFormat(DateFormat df) {
        if (df == null) {
            dateFormat = DEFAULT_DATE_FORMAT;
            
        } else {
            dateFormat = df;
        }
    }
    
    /* 
     * Sets the log header.
     * Use {@code null} to have no header.
     * Use "&date&" to use the time stamp of when the log was created.
     */
    public static void setLogHeader(String logHeader) {
        header = logHeader;
    }
    
    /* 
     * Writes the terminal message to the log.
     */
    protected void writeTerminalMessage(Date timeStamp) {
        if (terminateMsg != null) {
            writeO(terminateMsg, terminateType, timeStamp);
        }
    }
    
    
    /* 
     * Adds a shutdown hook for writing the terminate message and
     * closing the stream resources of the log file.
     */
    static {
        Runtime.getRuntime().addShutdownHook
            (new Thread("Shutdown-Log-Thread") {
            @Override
            public void run() {
                if (defLog != null) {
                    // Write the terminal message (if any) to the default log.
                    if (terminateMsg != null && !defLog.isClosed()) {
                        write(terminateMsg, terminateType);
                    }
                    
                    // Close the log
                    defLog.close();
                }
            }
        });
    }
    
}
