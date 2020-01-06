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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
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
        extends FileTree {
    
    /* -------------------------------------------------------------------------
     * Constants.
     * -------------------------------------------------------------------------
     */
    /** The regex used in {@link #list(TreeFile)}. */
    private final static String REGEX = ".+" + Var.FS + ".+";
    
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The token used to identify this file tree. */
    private final FileTreeToken<JarFileTree, TreeFile> token;
    /** The jar file instance used to access data. */
    private final JarFile jarFile;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new file tree of a jar file.
     * 
     * @param file The jar file.
     * 
     * @throws IOException If some I/O error occured.
     */
    protected JarFileTree(TreeFile file)
            throws IOException {
        jarFile = new JarFile(file.getPathName());
        token = new FileTreeToken<>(JarFileTree.class, file);
    }
    
    
    /* -------------------------------------------------------------------------
     * Constructor functions.
     * -------------------------------------------------------------------------
     */
    
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
    public static JarFileTree getTree(TreeFile file)
            throws IOException {
        FileTreeToken<JarFileTree, TreeFile> token = new FileTreeToken<>(JarFileTree.class, file);
        JarFileTree tree = (JarFileTree) FILE_TREE_MAP.get(token);
        if (tree == null) {
            FILE_TREE_MAP.put(token, tree = new JarFileTree(file));
        }
        
        return tree;
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
        return getTree(new TreeFile(getProjectSourceFile(c)));
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
        String localPath = toLocalPath(path).replaceAll(Var.FILE_REGEX, "/");
        if ("".equals(localPath)) return null;
        JarEntry entry = jarFile.getJarEntry(localPath);
        if (entry == null) {
            throw new FileNotFoundException("'" + path + "', '" + localPath + "'.");
        }
        return entry;
    }
    
    @Override
    public FileTreeToken<? extends FileTree, ?> getToken() {
        return token;
    }
    
    @Override
    public Iterator<TreeFile> walk(TreeFile file, FileVisitOption... options)
            throws IOException {
        JarEntry entry = getEntry(file.getPathName());
        final String entryPath = (entry == null ? "" : entry.getName());
        
        final Enumeration<JarEntry> entries = jarFile.entries();
        return new GeneratorIterator<TreeFile>() {
            @Override
            protected TreeFile generateNext() {
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    if (name.startsWith(entryPath)) {
                        if (entry.isDirectory()) {
                            return new TreeFile(toAbsolutePath(name));
                        } else {
                            return new TreeFile(toAbsolutePath(name));
                        }
                    }
                }
                done();
                return null;
            }
        };
    }
    
    @Override
    public long size(TreeFile file) {
        JarEntry entry = jarFile.getJarEntry(toLocalPath(file.getPathName()));
        if (entry == null) return 0;
        return entry.getSize();
    }
    
    @Override
    public boolean exists(TreeFile file) {
        String localPath = toLocalPath(file.getPathName());
        if ("".equals(localPath)) return true;
        return jarFile.getJarEntry(localPath) != null;
    }
    
    @Override
    public InputStream getInputStream(TreeFile file)
            throws IOException, SecurityException {
        JarEntry entry = getEntry(toLocalPath(file.getPathName()));
        return jarFile.getInputStream(entry);
    }
    
    @Override
    public byte[] readAllBytes(TreeFile file)
            throws IOException, OutOfMemoryError, SecurityException {
        return getInputStream(file).readAllBytes();
    }
    
    @Override
    public boolean isDirectory(TreeFile file)
            throws IOException {
        return getEntry(toLocalPath(file.getPathName())).isDirectory();
    }
    
    @Override
    public TreeFile[] list(TreeFile file)
            throws IOException {
        List<TreeFile> children = new ArrayList<>();
        JarEntry entry = getEntry(file.getPathName());
        String entryPath = (entry == null ? "" : entry.getName());
        
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            String name = entries.nextElement().getName();
            if (name.startsWith(entryPath) && name.length() > entryPath.length() &&
                    !name.substring(entryPath.length() + 1).matches(REGEX)) {
                children.add(new TreeFile(name));
            }
        }
        
        return children.toArray(new TreeFile[children.size()]);
    }
    
    
}
