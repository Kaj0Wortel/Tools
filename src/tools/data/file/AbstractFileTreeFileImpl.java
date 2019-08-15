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
 * for converting the {@link String} and {@link Path} arguments to {@link File}
 * arguments.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public abstract class AbstractFileTreeFileImpl
        extends FileTree {
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public Iterator<Path> walk(String path, FileVisitOption... options)
            throws IOException {
        return walk(new File(path), options);
    }
    
    @Override
    public Iterator<Path> walk(Path path, FileVisitOption... options)
            throws IOException {
        return walk(path.toFile(), options);
    }
    
    @Override
    public long size(String path) {
        return size(new File(path));
    }
    
    @Override
    public long size(Path path) {
        return size(path.toFile());
    }
    
    @Override
    public boolean exists(String path) {
        return exists(new File(path));
    }
    
    @Override
    public boolean exists(Path path) {
        return exists(path.toFile());
    }
    
    @Override
    public InputStream getStream(String path)
            throws IOException, SecurityException {
        return getStream(new File(path));
    }
    
    @Override
    public InputStream getStream(Path path)
            throws IOException, SecurityException {
        return getStream(path.toFile());
    }
    
    @Override
    public byte[] readAllBytes(String path)
            throws IOException, OutOfMemoryError, SecurityException {
        return readAllBytes(new File(path));
    }
    
    @Override
    public byte[] readAllBytes(Path path)
            throws IOException, OutOfMemoryError, SecurityException {
        return readAllBytes(path.toFile());
    }
    
    
}
