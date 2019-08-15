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
 * for converting the {@link File} and {@link Path} arguments to {@link String}
 * arguments.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public abstract class AbstractFileTreeStringImpl
        extends FileTree {
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public Iterator<Path> walk(Path path, FileVisitOption... options)
            throws IOException {
        return walk(path.toString(), options);
    }
    
    @Override
    public Iterator<Path> walk(File file, FileVisitOption... options)
            throws IOException {
        return walk(file.toString(), options);
    }
    
    @Override
    public long size(Path path) {
        return size(path.toString());
    }
    
    @Override
    public long size(File file) {
        return size(file.toString());
    }
    
    @Override
    public boolean exists(Path path) {
        return exists(path.toString());
    }
    
    @Override
    public boolean exists(File file) {
        return exists(file.toString());
    }
    
    @Override
    public InputStream getStream(Path path)
            throws IOException, SecurityException {
        return getStream(path.toString());
    }
    
    @Override
    public InputStream getStream(File file)
            throws IOException, SecurityException {
        return getStream(file.toString());
    }
    
    @Override
    public byte[] readAllBytes(Path path)
            throws IOException, OutOfMemoryError, SecurityException {
        return readAllBytes(path.toString());
    }
    
    @Override
    public byte[] readAllBytes(File file)
            throws IOException, OutOfMemoryError, SecurityException {
        return readAllBytes(file.toString());
    }
    
    
}
