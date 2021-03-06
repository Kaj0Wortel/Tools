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
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.FileVisitOption;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


// Tools impors
import tools.Var;


/**
 * A file tree describes a part of a file system.
 * Every file tree has a single root file/directory.
 * 
 * @todo
 * - Implement equals and hash code functions.
 * - Add implementation for zip files. (hint: copy-past parts of the jar-reader).
 * - Add implementation for virtual files.
 * - Add methods to get a file tree which return {@code null} upon failure instead of an exception.
 * 
 * @version 1.1
 * @author Kaj Wortel
 */
public abstract class FileTree {
    
    /* -------------------------------------------------------------------------
     * Constants.
     * -------------------------------------------------------------------------
     */
    /** The map containing all file trees. */
    protected static final Map<FileTreeToken<? extends FileTree, ?>, FileTree> FILE_TREE_MAP
            = new HashMap<>();
    /** Set containing all current file locks. */
    private static final Map<String, ReentrantLock> LOCKED_MAP = new HashMap<>();
    /** The lock used for concurrent access for (un-)locking files. */
    private static final ReentrantLock FILE_LOCK = new ReentrantLock(true);
    /** The condition used to wait and signal that a file is in use/became free. */
    private static final Condition FILE_IN_USE = FILE_LOCK.newCondition();
    
    /** The lock used for concurrent access for creating file trees. */
    protected static final ReentrantLock TREE_LOCK = new ReentrantLock();
    
    
    /* -------------------------------------------------------------------------
     * Static functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Gets or creates the local file tree of this class. <br>
     * If this class is executed from a jar file, then a {@link JarFileTree}
     * instance is returned. If this file is executed from a local {@code .class}
     * file, then a {@link DirFileTree} instance is returned. <br>
     * <br>
     * Only creates a new file tree if needed. <br>
     * <br>
     * The root directory will be the root class path of the class (e.g. the execution directory).
     * 
     * @return The local file tree of this class.
     * 
     * @throws IllegalStateException If the execution folder does not exists.
     */
    public static FileTree getLocalFileTree()
            throws IllegalArgumentException {
        return FileTree.getLocalFileTree(FileTree.class);
    }
    
    /**
     * Gets or creates the local file tree of the given class. <br>
     * If the given class is executed from a jar file, then a {@link JarFileTree}
     * instance is returned. If this file is executed from a local {@code .class}
     * file, then a {@link DirFileTree} instance is returned. <br>
     * <br>
     * Only creates a new file tree if needed. <br>
     * <br>
     * The root directory will be the root class path of the class  (e.g. the execution directory).
     * 
     * @return The local file tree for the given class.
     * 
     * @throws IllegalStateException If the execution folder does not exists.
     */
    public static FileTree getLocalFileTree(Class<?> c)
            throws IllegalArgumentException {
        TREE_LOCK.lock();
        try {
            String path = getProjectSourceFile(c);
            if (path.endsWith(".jar")) {
                return JarFileTree.getTree(new TreeFile(path));
                
            } else {
                return DirFileTree.getTree(new TreeFile(path));
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
            
        } finally {
            TREE_LOCK.unlock();
        }
    }
    
    /**
     * Returns the file tree which has the provided token. If no such file tree
     * exists, then {@code null} is returned. <br>
     * <br>
     * The token implementation is different and undefined for each file tree class.
     * A token should be obtained via the {@link FileTree#getToken()} function.
     * 
     * @param <C> The type of the file tree to return.
     * 
     * @param token The token of the file tree.
     * 
     * @return The file tree with the given token, or {@code null} if no such
     *     file tree exists.
     */
    public static <C extends FileTree> C getFileTree(FileTreeToken<C, ?> token) {
        return (C) FILE_TREE_MAP.get(token);
    }
    
    /**
     * @param c The class to get the project root from.
     * 
     * @apiNote
     * The function {@link java.net.URL#toURI} might be broken.
     * If problems arise, replace the current function with the commented line.
     * 
     * @return The project root file of the given class (e.g. the execution directory).
     * 
     * @throws IllegalStateException If the execution folder does not exists.
     */
    public static String getProjectSourceFile(Class<?> c)
            throws IllegalStateException {
        TREE_LOCK.lock();
        try {
            // DO NOT REMOVE THIS LINE:
            //return new File(c.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            return c.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
            
        } finally {
            TREE_LOCK.unlock();
        }
    }
    
    /**
     * Gets the parent directory of a file.
     * 
     * @param file The file to get the parent for.
     * 
     * @return The parent directory of the given file, or {@code null}
     *     if no such directory exists.
     */
    public TreeFile getParent(TreeFile file) {
        String str = file.getFile().getParent();
        if (str == null) return null;
        return new TreeFile(str);
    }
    
    /**
     * Gets the given child directory.
     * 
     * @param file The parent directory.
     * @param child The name of the child directory.
     * 
     * @return The child file.
     */
    public TreeFile getChild(TreeFile file, String child) {
        return new TreeFile(file.getPathName() + Var.FS + child);
    }
    
    /**
     * Traverses from the given file using the given child directories.
     * 
     * @param file The file to start traversing from.
     * @param children The directories to traverse through.
     * 
     * @return A file in the format {@code file/children[0]/children[1]/.../children[n]}.
     */
    public TreeFile traverse(TreeFile file, String... children) {
        if (children == null) throw new NullPointerException();
        if (children.length == 0) return file;
        StringBuilder sb = new StringBuilder(file.getPathName());
        for (String child : children) {
            sb.append(Var.FS);
            sb.append(child);
        }
        return new TreeFile(sb.toString());
    }
    
    /**
     * Generates the path used in the {@link #LOCKED_MAP}.
     * 
     * @param path The path to parse.
     * 
     * @return The path as it would be stored in {@link #LOCKED_MAP}.
     */
    private static String getLockPath(String path) {
        if (path == null) return null;
        if (path.length() == 0) {
            return path;
            
        } else if (path.length() >= 2) {
            return path.substring((path.startsWith(Var.FS) ? 1 : 0),
                    path.length() - (path.endsWith(Var.FS) ? 1 : 0));
                    
        } else if (path.length() == Var.FS.length()) {
            if (path.equals(Var.FS)) return "";
            else return path;
            
        } else {
            return path;
        }
    }
    
    /**
     * Locks the given file.
     * 
     * @param file The file to lock.
     */
    public void lock(TreeFile file) {
        lock(this, file);
    }
    
    /**
     * Locks the file relative to the given tree.
     * 
     * @param tree The file tree used as basis path for local files.
     * @param file The file to lock.
     */
    public static void lock(FileTree tree, TreeFile file) {
        final TreeFile abs = tree.toAbsoluteFile(file);
        TreeFile f = abs;
        FILE_LOCK.lock();
        try {
            String path = getLockPath(f.getPathName());
            while (path != null && path.length() != 0 && !Var.FS.equals(path)) {
                boolean reset = false;
                ReentrantLock lock;
                while ((lock = LOCKED_MAP.get(path)) != null) {
                    if (lock.isLocked() && !lock.isHeldByCurrentThread()) {
                        reset = true;
                        FILE_IN_USE.await();
                    }
                }
                
                if (reset) f = abs;
                else f = tree.getParent(f);
                path = getLockPath(f.getPathName());
            }
            
            path = getLockPath(abs.getPathName());
            ReentrantLock lock = LOCKED_MAP.get(path);
            if (lock == null) {
                LOCKED_MAP.put(path, lock = new ReentrantLock());
            }
            lock.lock();
            
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
            
        } finally {
            FILE_LOCK.unlock();
        }
    }
    
    /**
     * Checks whether the given file or any parent directory of it is locked.
     * 
     * @param file The file to check.
     * 
     * @return {@code true} if the file is locked. {@code false} otherwise.
     */
    public boolean isLocked(TreeFile file) {
        return isLocked(this, file);
    }
    
    /**
     * Checks whether the given file or any parent directory of it is locked.
     * This includes the case where the file was locked by the current thread.
     * 
     * @param tree The file tree used as basis path for local files.
     * @param file The file to check.
     * 
     * @return {@code true} if the file is locked. {@code false} otherwise.
     */
    public static boolean isLocked(FileTree tree, TreeFile file) {
        TreeFile f = tree.toAbsoluteFile(file);
        FILE_LOCK.lock();
        try {
            String path = getLockPath(f.getPathName());
            while (path.length() != 0 && !Var.FS.equals(path)) {
                path = getLockPath(f.getPathName());
                ReentrantLock lock = LOCKED_MAP.get(path);
                if (lock != null) {
                    if (lock.isLocked()) return true;
                }
                f = tree.getParent(f);
            }
            return false;
            
        } finally {
            FILE_LOCK.unlock();
        }
    }
    
    /**
     * Unlocks the given file.
     * 
     * @param file The file to unlock.
     */
    public void unlock(TreeFile file) {
        unlock(this, file);
    }
    
    /**
     * Unlocks the file relative to the given tree.
     * 
     * @param tree The file tree used as basis path for local files.
     * @param file The file to unlock.
     * 
     * @throws IllegalMonitorStateException If the file was not locked.
     */
    public static void unlock(FileTree tree, TreeFile file)
            throws IllegalMonitorStateException {
        String path = getLockPath(tree.toAbsolutePath(file.getPathName()));
        FILE_LOCK.lock();
        try {
            ReentrantLock lock = LOCKED_MAP.get(path);
            if (lock == null) {
                throw new IllegalMonitorStateException();
            }
            lock.unlock();
            if (!lock.isLocked()) {
                FILE_IN_USE.signal();
                if (!LOCKED_MAP.remove(path, lock)) {
                    throw new IllegalStateException();
                }
            }
            
        } finally {
            FILE_LOCK.unlock();
        }
    }
    
    /**
     * Obtains the lock for the given file, but only if it is not locked.
     * 
     * @param tree The file tree used as basis path for local files.
     * @param file The file to unlock.
     * 
     * @return {@code true} if the lock was obtained. {@code false} otherwise.
     */
    public static boolean tryLock(FileTree tree, TreeFile file) {
        FILE_LOCK.lock();
        try {
            if (isLocked(tree, file)) return false;
            else {
                lock(tree, file);
                return true;
            }
            
        } finally {
            FILE_LOCK.unlock();
        }
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Converts a local or absolute path to an absolute path.
     * 
     * @param path The path to convert.
     * 
     * @return The absolute path of the path.
     */
    public String toAbsolutePath(String path) {
        if (path.startsWith(getBasePath())) return path;
        if (path.startsWith(Var.FS)) {
            return getBasePath() + path.substring(1);
            
        } else {
            return getBasePath() + path;
        }
    }
    
    /**
     * Converts a local or absolute file to an absolute file.
     * 
     * @param tf The file to convert.
     * 
     * @return The absolute file of the given file.
     */
    public TreeFile toAbsoluteFile(TreeFile tf) {
        return new TreeFile(toAbsolutePath(tf.getPathName()));
    }
    
    /**
     * Converts a local or absolute path to a local path.
     * 
     * @param path The path to convert.
     * 
     * @return The local path of the given path.
     */
    public String toLocalPath(String path) {
        String localPath = path;
        if (path.startsWith(getBasePath())) {
            localPath = path.substring(getBasePath().length());
        }
        if (localPath.startsWith(Var.FS)) {
            return localPath.substring(1);
        }
        return localPath;
    }
    
    /**
     * Converts a local or absolute file to a local file.
     * 
     * @param tf The file to convert.
     * 
     * @return The local file of the given file.
     */
    public TreeFile tolocalFile(TreeFile tf) {
        return new TreeFile(toLocalPath(tf.getPathName()));
    }
    
    /**
     * @return The base path of the file tree.
     */
    public abstract String getBasePath();
    
    /**
     * @return The token of this file tree.
     */
    public abstract FileTreeToken<? extends FileTree, ?> getToken();
    
    /**
     * Returns an iterator which iterates over all paths from the given directory.
     * 
     * @param path The path of the starting directory.
     * @param options The walk options.
     * 
     * @return An iterator over all paths in the tree.
     * 
     * @throws IOException If some I/O error occured.
     */
    public Iterator<TreeFile> walk(String path, FileVisitOption... options)
            throws IOException {
        return walk(new TreeFile(path), options);
    }
    
    /**
     * Returns an iterator which iterates over all paths from the given directory.
     * 
     * @param file The starting directory.
     * @param options The walk options.
     * 
     * @return An iterator over all paths in the tree.
     * 
     * @throws IOException If some I/O error occured.
     */
    public abstract Iterator<TreeFile> walk(TreeFile file, FileVisitOption... options)
            throws IOException;
    
    /**
     * Determines the size of the given file in this file tree.
     * If the file doesn't exist or cannot be accessed, then {@code -1} is returned.
     * 
     * @param path The path of the file to get the size of.
     * 
     * @return The size of a file in bytes, or {@code 0} if the file could not be accessed.
     */
    public long size(String path) {
        return size(new TreeFile(path));
    }
    
    /**
     * Determines the size of the given file in this file tree.
     * If the file doesn't exist or cannot be accessed, then {@code -1} is returned.
     * 
     * @param file The file to get the size of.
     * 
     * @return The size of a file in bytes, or {@code 0} if the file could not be accessed.
     */
    public abstract long size(TreeFile file);
    
    /**
     * Checks whether the given file exists.
     * 
     * @param path The path of the file to check for existance.
     * 
     * @return {@code true} if the path exists. {@code false} otherwise.
     * 
     * @throws IOException If some I/O error occured.
     */
    public boolean exists(String path) {
        return exists(new TreeFile(path));
    }
    
    /**
     * Checks whether the given file exists.
     * 
     * @param file The file to check for existance.
     * 
     * @return {@code true} if the path exists. {@code false} otherwise.
     * 
     * @throws IOException If some I/O error occured.
     */
    public abstract boolean exists(TreeFile file);
    
    /**
     * Creates an input stream from the provided file.
     * 
     * @param path The path of the file to get the stream from.
     * 
     * @return The stream of the file with the given file.
     * 
     * @throws IOException If some I/O error occured.
     * @throws SecurityException If the security manager denies reading access of the file.
     */
    public InputStream getStream(String path)
            throws IOException, SecurityException {
        return getInputStream(new TreeFile(path));
    }
    
    /**
     * Creates an input stream from the provided file.
     * 
     * @param file The file to get the stream from.
     * 
     * @return The stream of the file with the given file.
     * 
     * @throws IOException If some I/O error occured.
     * @throws SecurityException If the security manager denies reading access of the file.
     */
    public abstract InputStream getInputStream(TreeFile file)
            throws IOException, SecurityException;
    
    /**
     * Reads all bytes from the given file and puts them in an array.
     * 
     * @param path The path of the file to read all bytes of.
     * 
     * @return A byte array containing all bytes of the file.
     * 
     * @throws IOException If an I/O error occurs while reading from the stream.
     * @throws OutOfMemoryError If the array of the required size cannot be allocated.
     * @throws SecurityException If the security manager denies reading access of the file.
     */
    public byte[] readAllBytes(String path)
            throws IOException, OutOfMemoryError, SecurityException {
        return readAllBytes(new TreeFile(path));
    }
    
    /**
     * Reads all bytes from the given file and puts them in an array.
     * 
     * @param file The file to read all bytes of.
     * 
     * @return A byte array containing all bytes of the file.
     * 
     * @throws IOException If an I/O error occurs while reading from the stream.
     * @throws OutOfMemoryError If the array of the required size cannot be allocated.
     * @throws SecurityException If the security manager denies reading access of the file.
     */
    public abstract byte[] readAllBytes(TreeFile file)
            throws IOException, OutOfMemoryError, SecurityException;
    /**
     * Checks whether the given path denotes a directory.
     * 
     * @implNote
     * If the path does not exist, then this function will return {@code false}.
     * 
     * @param path The path of the file to check.
     * 
     * @return {@code true} if the given file is a directory.
     *     {@code false} otherwise.
     * 
     * @throws IOException If some I/O error occured.
     */
    public boolean isDirectory(String path)
            throws IOException {
        return isDirectory(new TreeFile(path));
    }
    
    /**
     * Checks whether the given file is a directory.
     * 
     * @implNote
     * If the file does not exist, then this function will return {@code false}.
     * 
     * @param file The file to check.
     * 
     * @return {@code true} if the given file is a directory.
     *     {@code false} otherwise.
     * 
     * @throws IOException If some I/O error occured.
     */
    public abstract boolean isDirectory(TreeFile file)
            throws IOException;
    
    /**
     * Returns an array describing all children of the given file.
     * 
     * @param path The path of the parent file.
     * 
     * @return An array containing the children of the parent file.
     * 
     * @throws IOException If some I/O error occured.
     */
    public TreeFile[] list(String path)
            throws IOException {
        return list(new TreeFile(path));
    }
    
    /**
     * Returns an array describing all children of the given file.
     * 
     * @param file The parent file.
     * 
     * @return An array containing the children of the parent file.
     * 
     * @throws IOException If some I/O error occured.
     */
    public abstract TreeFile[] list(TreeFile file)
            throws IOException;
    
    
}
