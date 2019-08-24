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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.FileVisitOption;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


// Tools imports
import tools.Var;
import tools.iterators.GeneratorIterator;


/**
 * File tree implementation for a file tree from a jar file.
 * 
 * @version 1.1
 * @author Kaj Wortel
 */
public class JarFileTree
        extends AbstractFileTreeStringImpl {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The token used to identify this file tree. */
    private final FileTreeToken<JarFileTree, String> token;
    /** The jar file instance used to access data. */
    private final JarFile jarFile;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new file tree from a jar file.
     * 
     * @param path The path of the jar file.
     * 
     * @throws IOException If the file could not be read.
     */
    protected JarFileTree(Path path)
            throws IOException {
        this(path.toString());
    }
    
    /**
     * Creates a new file tree of a jar file.
     * 
     * @param path The path of the jar file.
     * 
     * @throws IOException If the jar file could not be read.
     */
    protected JarFileTree(String path)
            throws IOException {
        jarFile = new JarFile(path);
        token = new FileTreeToken<>(JarFileTree.class, path);
    }
    
    
    /* -------------------------------------------------------------------------
     * Constructor functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Returns a {@link JarFileTree} of the given jar file. <br>
     * If a file tree was already created with the given path, then the old
     * file tree is returned. <br>
     * <br>
     * This function replaces the constructor.
     * 
     * @param path The path of the jar file.
     * 
     * @return A file tree of the given jar file.
     * 
     * @throws IOException If the jar file could not be read.
     */
    public static JarFileTree getTree(String path)
            throws IOException {
        FileTreeToken<JarFileTree, String> token = new FileTreeToken<>(JarFileTree.class, path);
        JarFileTree tree = (JarFileTree) FILE_TREE_MAP.get(token);
        if (tree == null) {
            FILE_TREE_MAP.put(token, tree = new JarFileTree(path));
        }
        
        return tree;
    }
    
    /**
     * Returns a {@link JarFileTree} from the given path. <br>
     * <br>
     * This function should be used instead of the constructor. <br>
     * <br>
     * If a file tree was already created with the given path, then the
     * old file tree is returned.
     * 
     * @param path The path of the jar file.
     * 
     * @return A new {@link JarFileTree}
     * 
     * @throws IOException If the jar file could not be read.
     * @throws URISyntaxException If the provided path is not parsable to a URI.
     */
    public static JarFileTree getTree(Path path)
            throws IOException {
        return getTree(path.toString());
    }
    
    /**
     * Returns a {@link JarFileTree} of the given jar file. <br>
     * If a file tree was already created with the given file, then the old
     * file tree is returned. <br>
     * <br>
     * This function replaces the constructor.
     * 
     * @param file The jar file.
     * 
     * @return A file tree of the given jar file.
     * 
     * @throws IOException If the jar file could not be read.
     */
    public static JarFileTree getTree(File file)
            throws IOException {
        return getTree(file.toString());
    }
    
    /**
     * Returns a {@link JarFileTree} of the jar file the given class origates from. <br>
     * If a file tree was already created with the given path, then the
     * old file tree is returned. <br>
     * <br>
     * This function replaces the constructor.
     * 
     * @param c The class to get the jar file tree from.
     * 
     * @return A file tree of the jar file the given class originates from.
     * 
     * @throws IllegalStateException If the given class does not originate from a jar file.
     * @throws IOException If the jar file could not be read.
     */
    public static JarFileTree getTree(Class<?> c)
            throws IllegalStateException, IOException {
        return getTree(getProjectSourceFile(c));
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * @return The ase path of this file tree.
     */
    @Override
    public String getBasePath() {
        return jarFile.getName() + "!" + Var.FS;
    }
    
    /**
     * Obtains the entry with the given local or absolute path.
     * 
     * @param path The local or absolute path of the entry.
     * 
     * @return The entry with the given path, or {@code null} if the path matches the root directory.
     * 
     * @throws FileNotFoundException If the entry does not exists.
     */
    private JarEntry getEntry(String path)
            throws IOException {
        String localPath = toLocalPath(path);
        if ("".equals(localPath)) return null;
        JarEntry entry = jarFile.getJarEntry(localPath);
        if (entry == null) {
            throw new FileNotFoundException("'" + path + "', '" + toLocalPath(path) + "'.");
        }
        return entry;
    }
    
    @Override
    public FileTreeToken<? extends FileTree, ?> getToken() {
        return token;
    }
    
    @Override
    public Iterator<Path> walk(String path, FileVisitOption... options)
            throws IOException {
        JarEntry entry = getEntry(path);
        final String entryPath = (entry == null ? "" : entry.getName());
        
        final Enumeration<JarEntry> entries = jarFile.entries();
        return new GeneratorIterator<Path>() {
            @Override
            protected Path generateNext() {
                while (entries.hasMoreElements()) {
                    String name = entries.nextElement().getName();
                    if (name.startsWith(entryPath)) {
                        return new File(toAbsolutePath(name)).toPath();
                    }
                }
                done();
                return null;
            }
        };
    }
    
    @Override
    public long size(String path) {
        JarEntry entry = jarFile.getJarEntry(toLocalPath(path));
        if (entry == null) return 0;
        return entry.getSize();
    }
    
    @Override
    public boolean exists(String path) {
        String localPath = toLocalPath(path);
        if ("".equals(localPath)) return true;
        return jarFile.getJarEntry(localPath) != null;
    }
    
    @Override
    public InputStream getStream(String path)
            throws IOException, SecurityException {
        JarEntry entry = getEntry(path);
        return jarFile.getInputStream(entry);
    }
    
    @Override
    public byte[] readAllBytes(String path)
            throws IOException, OutOfMemoryError, SecurityException {
        return getStream(path).readAllBytes();
    }
    
    @Override
    public boolean isDirectory(String path)
            throws IOException {
        return getEntry(path).isDirectory();
    }
    
    
}
