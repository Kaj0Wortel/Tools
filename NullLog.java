/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2018 by Kaj Wortel - all rights reserved                *
 * Contact: kaj.wortel@gmail.com                                         *
 *                                                                       *
 * This file is part of the tools project, which can be found on github: *
 * https://github.com/Kaj0Wortel/tools                                   *
 *                                                                       *
 * It is allowed to use, (partially) copy and modify this file           *
 * in any way for private use only by using this header.                 *
 * It is not allowed to redistribute any (modifed) versions of this file *
 * without my permission.                                                *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package tools;

// Java imports
a packages
import java.io.File;


public class NullLog extends Loggable {
    
    @Deprecated
    public NullLog() { }
    
    public static synchronized void write(Exception e) { }
    
    public static synchronized void write(Object obj) { }
    
    public static synchronized void write(Object[] obj) {
        
    }
    
    public static synchronized boolean clear() {
        return true;
    }
    
    public static synchronized boolean setLogFile(File file) {
        return true;
    }
    
    public static synchronized void setUseTimeStamp(boolean newTimeStamp) { }
    
    public static synchronized void setUseFull(boolean newFull) { }
}