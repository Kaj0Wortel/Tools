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

package tools;


// Java imports
import java.io.File;
import java.util.Random;


/**
 * TODO: add comments.
 * 
 * Interface for storing the constants used in the tools package.
 * 
 * @author Kaj Wortel (0991586)
 */
public interface Var {
    final public static String LS = System.getProperty("line.separator");
    final public static String FS = System.getProperty("file.separator");
    final public static String LS_REGEX = "\n\r|\r\n|\n|\r";
    
    final public static String WORKING_DIR = System.getProperty("user.dir")
            + FS + "src" + FS;
    final public static String TOOL_DIR = WORKING_DIR + "tools" + FS;
    final public static String FONT_DIR = TOOL_DIR + "font" + FS;
    final public static String IMG_DIR = WORKING_DIR + "img" + FS;
    final public static String DATA_DIR = WORKING_DIR + "data" + FS;
    final public static String TEST_DIR = WORKING_DIR + "test" + FS;
    final public static File LOG_FILE = new File(WORKING_DIR + "log.log");
    
    final public static String PROJECT_DIR = DATA_DIR + "projects" + FS;
    final public static String CACHE_DIR = DATA_DIR + ".cache" + FS;
    final public static String BACKUP_DIR = DATA_DIR + "backup" + FS;
    
    final public static Random RAN = new Random();
    
    
}
