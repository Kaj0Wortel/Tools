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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;


// Tools imports
import tools.Var;
import tools.io.PartFile;
import tools.iterators.FileIterator;
import tools.iterators.GeneratorIterator;


/**
 * File tree implementation for a file tree starting at a root directory.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class DirFileTree
        extends AbstractFileTreeFileImpl {
     
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The token used to identify this file tree. */
    private final FileTreeToken<DirFileTree, File> token;
    /** The root directory of this tree. */
    private final File root;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a fresh directory file tree with the given root file.
     * 
     * @param root The root directory of the file tree.
     * 
     * @throws IOException If the given root file doesn't exist.
     * @throws IllegalArgumentException If the given file isn't a directory.
     */
    protected DirFileTree(File root)
            throws IOException, IllegalArgumentException {
        if (!root.exists()) {
            throw new FileNotFoundException("The file '" + root.toString() + "' does not exist!");
        }
        if (root.isFile()) {
            throw new IllegalArgumentException("The given file '" + root.toString()
                    + "' is not a directory!");
        }
        
        this.root = root;
        token = new FileTreeToken<>(DirFileTree.class, root);
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Returns a {@link DirFileTree} of the root directory of the file with the given name. <br>
     * If a file tree was already created with the given path, then the
     * old file tree is returned. <br>
     * <br>
     * This function replaces the constructor.
     * 
     * @param path The name of the file of the root directory of the file tree.
     * 
     * @return A file tree starting at the the root directory the given class originates from.
     * 
     * @throws IllegalStateException If the name does not denote a directory.
     * @throws IOException If the root directory could not be accessed.
     */
    public static DirFileTree getTree(String path)
            throws IOException, IllegalArgumentException {
        return getTree(new File(path));
    }
    
    /**
     * Returns a {@link DirFileTree} of the root directory of the file with the given path. <br>
     * If a file tree was already created with the given path, then the
     * old file tree is returned. <br>
     * <br>
     * This function replaces the constructor.
     * 
     * @param path The path of the file of the root directory of the file tree.
     * 
     * @return A file tree starting at the the root directory the given class originates from.
     * 
     * @throws IllegalStateException If the given path does not denote a direoctory.
     * @throws IOException If the root directory could not be accessed.
     */
    public static DirFileTree getTree(Path path)
            throws IOException, IllegalArgumentException {
        return getTree(path.toFile());
    }
    
    /**
     * Returns a {@link DirFileTree} of the root directory of the given file. <br>
     * If a file tree was already created with the given path, then the
     * old file tree is returned. <br>
     * <br>
     * This function replaces the constructor.
     * 
     * @param file The root directory of the file tree.
     * 
     * @return A file tree starting at the the root directory the given class originates from.
     * 
     * @throws IllegalStateException If the given file is not a directory.
     * @throws IOException If the root directory could not be accessed.
     */
    public static DirFileTree getTree(File file)
            throws IOException, IllegalArgumentException {
        FileTreeToken<DirFileTree, File> token = new FileTreeToken<>(
                DirFileTree.class, file);
        DirFileTree tree = (DirFileTree) FILE_TREE_MAP.get(token);
        if (tree == null) {
            tree = new DirFileTree(file);
            FILE_TREE_MAP.put(token, tree);
        }
        return tree;
    }
    
    /**
     * Returns a {@link DirFileTree} of the root directory the given class origates from. <br>
     * If a file tree was already created with the given path, then the
     * old file tree is returned. <br>
     * <br>
     * This function replaces the constructor.
     * 
     * @param c The class to get the root directory tree from.
     * 
     * @return A file tree starting at the the root directory the given class originates from.
     * 
     * @throws IllegalStateException If the given class does not originate from a directory file
     *     (e.g. it originates from a jar file).
     * @throws IOException If the root directory could not be accessed.
     */
    public static DirFileTree getTree(Class<?> c)
            throws IllegalStateException, IOException {
        try {
            return getTree(getProjectSourceFile(c));
            
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }
    
    @Override
    public String getBasePath() {
        return root.toString() + Var.FS;
    }
    
    @Override
    public FileTreeToken<DirFileTree, File> getToken() {
        return token;
    }
    
    @Override
    public Iterator<Path> walk(File file, FileVisitOption... options)
            throws IOException {
        File absFile = new File(toAbsolutePath(file.toString()));
        Iterator<PartFile> it = new FileIterator(absFile, true);
        return new GeneratorIterator<Path>() {
            @Override
            protected Path generateNext() {
                if (!it.hasNext()) return null;
                return it.next().toPath();
            }
        };
    }
    
    @Override
    public long size(File file) {
        File absFile = new File(toAbsolutePath(file.toString()));
        if (absFile.exists()) return absFile.length();
        else return 0;
    }
    
    @Override
    public boolean exists(File file) {
        return new File(toAbsolutePath(file.toString())).exists();
    }
    
    @Override
    public InputStream getStream(File file)
            throws IOException, SecurityException {
        return new FileInputStream(new File(toAbsolutePath(file.toString())));
    }
    
    @Override
    public byte[] readAllBytes(File file)
            throws IOException, OutOfMemoryError, SecurityException {
        return Files.readAllBytes((new File(toAbsolutePath(file.toString()))).toPath());
    }
    
    
}
