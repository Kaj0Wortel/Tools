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

package tools.data;


// Java imports
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileTime;


/**
 * Class for representing a virtual file.
 * This class should be used to temporarily store (meta) data about a file. <br>
 * Emulates the data of the with the {@link InputStream} provided on creation.
 * The input stream in this class can be used only once, and is therefore
 * a one-use-cast-away-shell.
 * 
 * @todo
 * - Improve the single-use input stream to a mutli-use input stream.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class VirtualFile {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The file corresponding with the virtual file. */
    private final File file;
    /** The input stream containing the data of the virtual file. */
    private final InputStream is;
    /** The creation time of the original virtual file. */
    private final FileTime creationTime;
    /** The last access time of the original virtual file. */
    private final FileTime lastAccessTime;
    /** The last access time of the original virtual file. */
    private final FileTime lastModifiedTime;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new virtual file.
     * 
     * @param file The file corresponding with the virtual file.
     * @param is The input stream containing the data of the virtual file.
     * @param creationTime The creation time of the original virtual file.
     * @param lastAccessTime The last access time of the original virtual file.
     * @param lastModifiedTime The last access time of the original virtual file.
     */
    public VirtualFile(File file, InputStream is, FileTime creationTime,
            FileTime lastAccessTime, FileTime lastModifiedTime) {
        this.file = file;
        this.is = is;
        this.creationTime = creationTime;
        this.lastAccessTime = lastAccessTime;
        this.lastModifiedTime = lastModifiedTime;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * @return The file corresponding with the virtual file
     */
    public File getFile() {
        return file;
    }
    
    /**
     * @return The input stream containing the data of the virtual file.
     */
    public InputStream getStream() {
        return is;
    }
    
    /**
     * @return The creation time of the original file.
     */
    public FileTime getCreationTime() {
        return creationTime;
    }
    
    /**
     * @return The last access time of the original file.
     */
    public FileTime getLastAccessTime() {
        return lastAccessTime;
    }
    
    /**
     * @return The last modified time of the original file.
     */
    public FileTime getLastModifiedTime() {
        return lastModifiedTime;
    }
    
    /**
     * Sets the time attributes to the file.
     * 
     * @throws IOException If there was an IO error.
     */
    public void setAttributes()
            throws IOException {
        BasicFileAttributeView attrib = Files.getFileAttributeView(file.toPath(),
                BasicFileAttributeView.class);
        attrib.setTimes(lastModifiedTime, lastAccessTime, creationTime);
    }
    
    
}
