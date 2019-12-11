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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Abstract file tree for adding write options to a file tree.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public abstract class WritableFileTree
        extends FileTree {
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates the given directory.
     * 
     * @param path The path of the directory to be created.
     * 
     * @return {@code true} if the directory was created. False otherwise.
     * 
     * @throws IOException if some I/O error occured.
     */
    public boolean mkdir(String path)
            throws IOException {
        return mkdir(new TreeFile(path));
    }
    
    /**
     * Creates the given directory.
     * 
     * @param file The directory to be created.
     * 
     * @return {@code true} if the directory was created. False otherwise.
     * 
     * @throws IOException if some I/O error occured.
     */
    public abstract boolean mkdir(TreeFile file)
            throws IOException;
    
    /**
     * Creates the given directory, including all directories on the given path.
     * 
     * @param path The path of the directory to be created.
     * 
     * @return {@code true} if the directory was created. False otherwise.
     * 
     * @throws IOException if some I/O error occured.
     */
    public boolean mkdirs(String path)
            throws IOException {
        return mkdirs(new TreeFile(path));
    }
    
    /**
     * Creates the given directory, including all parent directories.
     * 
     * @param file The directory to be created.
     * 
     * @return {@code true} if the directory was created. False otherwise.
     * 
     * @throws IOException if some I/O error occured.
     */
    public abstract boolean mkdirs(TreeFile file)
            throws IOException;
    
    /**
     * Writes the given data to the given file.
     * 
     * @param path The path of the file to write the data to.
     * @param append Whether the data should be appended to the file.
     * @param data The data to be written.
     * 
     * @throws IOException if some I/O error occured.
     */
    public void writeFile(String path, boolean append, InputStream data)
            throws IOException {
        writeFile(new TreeFile(path), append, data);
    }
    
    /**
     * Writes the given data to the given file.
     * 
     * @param file The file to write the data to.
     * @param append Whether the data should be appended to the file.
     * @param data The data to be written.
     * 
     * @throws IOException if some I/O error occured.
     */
    public abstract void writeFile(TreeFile file, boolean append, InputStream data)
            throws IOException;
    
    
    /**
     * Creates an unbuffered output stream to the given path.
     * 
     * @param path The path of the file to write the data to.
     * @param append Whether to append the data to the file.
     * 
     * @return An output stream for the given file.
     * 
     * @throws IOException if some I/O error occured.
     */
    public OutputStream getOutputStream(String path, boolean append)
            throws IOException {
        return getOutputStream(new TreeFile(path), append);
    }
    
    /**
     * Creates an unbuffered output stream to the given file.
     * 
     * @param file The file to write the data to.
     * @param append Whether to append the data to the file.
     * 
     * @return An output stream for the given file.
     * 
     * @throws IOException if some I/O error occured.
     */
    public abstract OutputStream getOutputStream(TreeFile file, boolean append)
            throws IOException;
    
    /**
     * Deletes the file at the given path. If the file denotes a directory,
     * then all children of that directory are deleted. <br>
     * Also returns {@code true} if the given file doesn't exist.
     * 
     * @param path The path of the file to be deleted.
     * 
     * @return {@code true} if the file was deleted successfully. {@code false} otherwise.
     * 
     * @code IOException If some I/O error occured.
     */
    public boolean delete(String path)
            throws IOException {
        return delete(new TreeFile(path));
    }
    
    /**
     * Deletes the given file. If the file denotes a directory, then all children of
     * that directory are deleted. <br>
     * Also returns {@code true} if the given file doesn't exist.
     * 
     * @param file The file to be deleted.
     * 
     * @return {@code true} if the file was deleted successfully. {@code false} otherwise.
     * 
     * @code IOException If some I/O error occured.
     */
    public boolean delete(TreeFile file)
            throws IOException {
        if (isDirectory(file)) {
            return deleteDir(file, true);
        } else {
            return deleteFile(file);
        }
    }
    
    /**
     * Deletes the file at the given file. The file cannot be a directory. <br>
     * Also returns {@code true} if the given file doesn't exist.
     * 
     * @param path The path of the file to be deleted.
     * 
     * @return {@code true} if the file was deleted successfully. {@code false} otherwise.
     * 
     * @throws IOException If some I/O error occured.
     * @throws IllegalArgumentException If the given file was a directory.
     */
    public boolean deleteFile(String path)
            throws IOException, IllegalArgumentException {
        return deleteFile(new TreeFile(path));
    }
    
    /**
     * Deletes the given file. The file cannot be a directory. <br>
     * Also returns {@code true} if the given file doesn't exist.
     * 
     * @param file The file to be deleted.
     * 
     * @return {@code true} if the file was deleted successfully. {@code false} otherwise.
     * 
     * @throws IOException If some I/O error occured.
     * @throws IllegalArgumentException If the given file was a directory.
     */
    public abstract boolean deleteFile(TreeFile file)
            throws IOException, IllegalArgumentException;
    
    /**
     * Deletes the file at the given path. The file must be a directory.
     * Only deletes all children if {@code deleteChildren == true}, if there are any. <br>
     * Also returns {@code true} if the given directory doesn't exist.
     * 
     * @param path The path of the file to be deleted.
     * @param deleteChildren Whether to delete the children of the directory, if it has any.
     * 
     * @return {@code true} if the file was deleted successfully. {@code false} otherwise.
     * 
     * @throws IOException If some I/O error occured.
     * @throws IllegalArgumentException If the given file was a directory.
     */
    public boolean deleteDir(String path, boolean deleteChildren)
            throws IOException, IllegalArgumentException {
        return deleteDir(new TreeFile(path), deleteChildren);
    }
    
    /**
     * Deletes the given file. The file must be a directory. Only deletes all
     * children if {@code deleteChildren == true}, if there are any. <br>
     * Also returns {@code true} if the given directory doesn't exist.
     * 
     * @param file The file to be deleted.
     * @param deleteChildren Whether to delete the children of the directory, if it has any.
     * 
     * @return {@code true} if the file was deleted successfully. {@code false} otherwise.
     * 
     * @throws IOException If some I/O error occured.
     * @throws IllegalArgumentException If the given file was a directory.
     */
    public abstract boolean deleteDir(TreeFile file, boolean deleteChildren)
            throws IOException, IllegalArgumentException;
    
    
}
