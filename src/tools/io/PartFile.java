/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) August 2019 by Kaj Wortel - all rights reserved             *
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

package tools.io;


// Java imports
import java.io.File;


// Tools imports.
import tools.Var;


/**
 * Extends the {@link File} class by supporting operations for files with a base path.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class PartFile
        extends File {
     
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The starting directory file. */
    private final File dir;
    /** The file path relative to the directory file. */
    private final String filePath;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new {@code PartFile} with the given starting directory
     * and the file relative to this directory.
     * 
     * @param dirName The name of the starting directory.
     * @param filePath The file path relative to the directory.
     */
    public PartFile(String dirName, String filePath) {
        super(dirName, filePath);
        this.dir = new File(dirName);
        this.filePath = filePath;
    }
    
    /**
     * Creates a new {@code PartFile} with the given starting directory
     * file relative to this directory.
     * 
     * @param dir The starting directory.
     * @param filePath The file path relative to the directory.
     */
    public PartFile(File dir, String filePath) {
        super((dir == null ? "" : dir.toString() + Var.FS) + filePath);
        this.dir = dir;
        this.filePath = filePath;
    }
    
    /**
     * Creates a new {@code PartFile} with the given starting directory
     * file relative to this directory.
     * 
     * @param dir The starting directory.
     * @param file The file path relative to the directory.
     */
    public PartFile(File dir, File file) {
        super((dir == null ? "" : dir.toString() + Var.FS) + file.toString());
        this.dir = dir;
        this.filePath = file.toString();
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * @return The directory of the file part.
     */
    public File getDir() {
        return dir;
    }
    
    /**
     * @return The string representation of the directory part of the file.
     */
    public String getDirName() {
        return dir.toString();
    }
    
    /**
     * @return The relative file path of the file part.
     */
    public File getRelativeFile() {
        return new File(filePath);
    }
    
    /**
     * @return The string representation of the relative file path of the file part.
     */
    public String getRelativeFileName() {
        return filePath;
    }
    
    
}
