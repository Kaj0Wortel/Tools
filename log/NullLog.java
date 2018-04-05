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
import tools.log.Loggable;


// Java imports
import java.io.File;


/* 
 * This is a log class that ignores all log action.
 */
public class NullLog extends Loggable {
    
    /* 
     * This is a static singleton class. No instances should be made.
     */
    @Deprecated
    private NullLog() { }
    
    public static synchronized void write(Exception e) { }
    
    public static synchronized void write(Object obj) { }
    
    public static synchronized void write(Object[] obj) { }
    
    public static synchronized boolean clear() {
        return true;
    }
    
    public static synchronized boolean setLogFile(File file) {
        return true;
    }
    
    public static synchronized void setUseTimeStamp(boolean newTimeStamp) { }
    
    public static synchronized void setUseFull(boolean newFull) { }
}