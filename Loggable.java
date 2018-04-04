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


// Java imports
import java.io.File;


public abstract class Loggable {
    private static File logFile = new File(System.getProperty("user.dir") + "\\tools\\log.txt");
    
    @Deprecated
    public Loggable() {}
    
    public static synchronized void write(Exception e) {
        throw new UnsupportedOperationException("Log-operation \"write(Exception)\" was not supported");
    }
    
    public static synchronized void write(Object obj) {
        throw new UnsupportedOperationException("Log-operation \"write(Object)\" was not supported");
    }
    
    public static synchronized void write(Object[] obj) {
        throw new UnsupportedOperationException("Log-operation \"write(Object[])\" was not supported");
    }
    
    public static synchronized boolean clear() {
        throw new UnsupportedOperationException("Log-operation \"clear()\" was not supported");
    }
    
    public static synchronized boolean setLogFile(File file) {
        throw new UnsupportedOperationException("Log-operation \"setLogFile()\" was not supported");
    }
    
    public static synchronized void setUseTimeStamp(boolean newTimeStamp) {
        throw new UnsupportedOperationException("Log-operation \"setUseTimeStamp()\" was not supported");
    }
    
    public static synchronized void setUseFull(boolean newFull) {
        throw new UnsupportedOperationException("Log-operation \"setUseFull()\" was not supported");
    }
}
