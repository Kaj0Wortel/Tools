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

package tools.data.file;


// Java imports
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitOption;
import java.nio.file.Path;
import java.util.Iterator;


/**
 * Abstract instance of {@link FileTree} to reduce the boilerplate code
 * for converting the {@link String} and {@link File} arguments to {@link Path}
 * arguments.
 * 
 * @version 1.1
 * @author Kaj Wortel
 */
public abstract class AbstractFileTreePathImpl
        extends FileTree {
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public Iterator<Path> walk(String path, FileVisitOption... options)
            throws IOException {
        return walk(new File(path).toPath(), options);
    }
    
    @Override
    public Iterator<Path> walk(File file, FileVisitOption... options)
            throws IOException {
        return walk(file.toPath(), options);
    }
    
    @Override
    public long size(String path) {
        return size(new File(path).toPath());
    }
    
    @Override
    public long size(File file) {
        return size(file.toPath());
    }
    
    @Override
    public boolean exists(String path) {
        return exists(new File(path).toPath());
    }
    
    @Override
    public boolean exists(File file) {
        return exists(file.toPath());
    }
    
    @Override
    public InputStream getStream(String path)
            throws IOException, SecurityException {
        return getStream(new File(path).toPath());
    }
    
    @Override
    public InputStream getStream(File file)
            throws IOException, SecurityException {
        return getStream(file.toPath());
    }
    
    @Override
    public byte[] readAllBytes(String path)
            throws IOException, OutOfMemoryError, SecurityException {
        return readAllBytes(new File(path).toPath());
    }
    
    @Override
    public byte[] readAllBytes(File file)
            throws IOException, OutOfMemoryError, SecurityException {
        return readAllBytes(file.toPath());
    }
    
    @Override
    public boolean isDirectory(String path)
            throws IOException {
        return isDirectory(new File(path).toPath());
    }
    
    @Override
    public boolean isDirectory(File file)
            throws IOException {
        return isDirectory(file.toPath());
    }
    
    
}
