/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) September 2019 by Kaj Wortel - all rights reserved          *
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

package tools.data.file;


// Java imports
import java.io.File;
import java.nio.file.Path;
import java.util.Objects;


// Tools imports
import tools.MultiTool;


/**
 * File type for the classes in the {@code tools.data.file} package.
 * 
 * @author Kaj Wortel
 */
public class TreeFile {
     
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The path of the tree file. */
    private final String pathName;
    /** The file pointed to by this tree file.. */
    private final File file;
    /** The path of the tree file. */
    private final Path path;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new tree file from the given path name.
     * 
     * @param pathName The path of the file. This path can be relative or absolute.
     */
    public TreeFile(String pathName) {
        this.pathName = pathName;
        file = new File(pathName);
        path = file.toPath();
    }
    
    /**
     * Creates a new tree file from the given file.
     * 
     * @param file The file to point to.
     */
    public TreeFile(File file) {
        pathName = file.getPath();
        this.file = file;
        path = file.toPath();
    }
    
    /**
     * Creates a new tree file from the given path.
     * 
     * @param path The path to the file.
     */
    public TreeFile(Path path) {
        pathName = path.toString();
        file = path.toFile();
        this.path = path;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * @return The name of path of the file being described.
     */
    public String getPathName() {
        return pathName;
    }
    
    /**
     * @return The file being described.
     */
    public File getFile() {
        return file;
    }
    
    /**
     * @return The path of the file being described.
     */
    public Path getPath() {
        return path;
    }
    
    /**
     * Returns a string representation of this file. <br>
     * The returned value is equal to {@link #getPathName()}.
     * 
     * @return A string representation of this class.
     */
    @Override
    public String toString() {
        return pathName;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TreeFile)) return false;
        TreeFile tf = (TreeFile) obj;
        return Objects.equals(pathName, tf.pathName);
    }
    
    @Override
    public int hashCode() {
        return MultiTool.calcHashCode(pathName);
    }
    
    
}
