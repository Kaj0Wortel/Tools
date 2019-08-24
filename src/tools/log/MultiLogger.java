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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Logger implementation which distributes a single log operation to
 * all initially provided loggers. <br>
 * <br>
 * Logger classes which are an instance of {@link DefaultLogger} are
 * handled differently. The pre-processing steps are calculated in this class,
 * and only the {@link DefaultLogger#writeText(String)} function is invoked
 * for each default logger, as the same data would be generated multiple times
 * otherwise. Other logger classes will have to generate everything separately.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class MultiLogger
        extends DefaultLogger {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The non-default loggers to distribute the data over. */
    private final List<Logger> loggers;
    /** The default loggers to distribute the data over. */
    private final List<DefaultLogger> defLoggers;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a logger which distributes all log operations to all given loggers.
     * 
     * @param loggers The loggers to distribute the data over.
     */
    public MultiLogger(Logger... loggers) {
        if (loggers == null) {
            this.loggers = new ArrayList<Logger>(0);
            this.defLoggers = new ArrayList<DefaultLogger>(0);
            
        } else {
            this.loggers = new ArrayList<Logger>();
            this.defLoggers = new ArrayList<DefaultLogger>();
            for (int i = 0; i < loggers.length; i++) {
                if (loggers[i] instanceof DefaultLogger) {
                    this.defLoggers.add((DefaultLogger) loggers[i]);
                    
                } else {
                    this.loggers.add(loggers[i]);
                }
            }
        }
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    protected void writeText(String msg)
            throws IOException {
        for (DefaultLogger defLog : defLoggers) {
            defLog.writeText(msg);
        }
    }
    
    @Override
    protected void writeE(Exception e, Type type, Date timeStamp) {
        for (Logger log : loggers) {
            log.writeE(e, type, timeStamp);
        }
        
        super.writeE(e, type, timeStamp);
    }
    
    @Override
    protected void writeO(Object obj, Type type, Date timeStamp) {
        for (Logger log : loggers) {
            log.writeO(obj, type, timeStamp);
        }
        
        super.writeO(obj, type, timeStamp);
    }
    
    @Override
    protected void flush() {
        for (DefaultLogger defLog : defLoggers) {
            defLog.flush();
        }
        for (Logger log : loggers) {
            log.flush();
        }
    }
    
    @Override
    protected void close() {
        for (DefaultLogger defLog : defLoggers) {
            defLog.close();
        }
        for (Logger log : loggers) {
            log.close();
        }
    }
    
    
    
}
