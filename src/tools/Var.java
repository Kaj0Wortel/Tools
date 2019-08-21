/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) July 2019 by Kaj Wortel - all rights reserved               *
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

package tools;


// Java imports
import java.io.File;
import java.util.Random;


/**
 * Interface for storing the constants used in the tools package.
 * 
 * @todo
 * Revise constant values and naming.
 * 
 * @version 0.0
 * @author Kaj Wortel
 */
public interface Var {
    
    /** The OS dependent line separator. */
    public static final String LS = System.getProperty("line.separator");
    /** The OS dependent file separator. */
    public static final String FS = System.getProperty("file.separator");
    /** Regex for detecting a line separator of any OS. */
    public static final String LS_REGEX = "\n\r|\r\n|\n|\r";
    
    /** The global project working directory. */
    @Deprecated
    public static final String WORKING_DIR = System.getProperty("user.dir") + FS + "src" + FS;
    /** The global project test directory. */
    @Deprecated
    public static final String TEST_DIR = System.getProperty("user.dir") + FS + "test" + FS;
    /** The global project log directory. */
    @Deprecated
    public static final String LOG_DIR = System.getProperty("user.dir") + FS + "log" + FS;
    
    /** The directory of the tool package. */
    @Deprecated
    public static final String TOOL_DIR = WORKING_DIR + "tools" + FS;
    /** The font directory. */
    @Deprecated
    public static final String FONT_DIR = WORKING_DIR + "font" + FS;
    /** The image directory. */
    @Deprecated
    public static final String IMG_DIR = WORKING_DIR + "img" + FS;
    /** The data direcotry. */
    @Deprecated
    public static final String DATA_DIR = WORKING_DIR + "data" + FS;
    /** The default log file. */
    @Deprecated
    public static final File LOG_FILE = new File(LOG_DIR + "log.log");
    
    /** Random number generator. */
    public static final Random RAN = new Random();
    
    /** The number of available CPU cores. */
    public static final int CORES = Runtime.getRuntime().availableProcessors();
    
    
    
    
    // TODO
    /** The local font directory. */
    public static final String L_FONT_DIR = "font" + FS;
    /** The local image directory. */
    public static final String L_IMG_DIR = "img" + FS;
    
    
}
