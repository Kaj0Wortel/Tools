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
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


// Tools imports
import tools.Var;
import tools.io.PartFile;
import tools.iterators.FileIterator;
import tools.iterators.GeneratorIterator;


/**
 * File tree implementation for a file tree starting at a root directory.
 * 
 * @version 1.1
 * @author Kaj Wortel
 */
public class DirFileTree
        extends FileTree {
     
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The token used to identify this file tree. */
    private final FileTreeToken<DirFileTree, TreeFile> token;
    /** The root directory of this tree. */
    private final TreeFile root;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a fresh directory file tree with the given root file.
     * 
     * @param root The root directory of the file tree.
     * 
     * @throws IllegalArgumentException If the given file isn't a directory.
     * @throws IOException If the given root file doesn't exist.
     */
    protected DirFileTree(TreeFile root)
            throws IllegalArgumentException, IOException {
        if (!root.getFile().exists()) {
            throw new FileNotFoundException("The file '" + root.toString() + "' does not exist!");
        }
        if (root.getFile().isFile()) {
            throw new IllegalArgumentException("The given file '" + root.toString()
                    + "' is not a directory!");
        }
        
        this.root = (root.getPathName().endsWith(Var.FS)
                ? root
                : new TreeFile(root.getPathName() + Var.FS));
        token = new FileTreeToken<>(DirFileTree.class, root);
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
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
    public static DirFileTree getTree(TreeFile file)
            throws IllegalArgumentException, IOException {
        FileTreeToken<DirFileTree, TreeFile> token = new FileTreeToken<>(
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
        return getTree(new TreeFile(getProjectSourceFile(c)));
    }
    
    @Override
    public String getBasePath() {
        return root.toString();
    }
    
    @Override
    public FileTreeToken<DirFileTree, TreeFile> getToken() {
        return token;
    }
    
    /**
     * Converts an absolute or local tree file to an absolute file.
     * 
     * @param tf The tree file to convert.
     * 
     * @return An absolute file.
     */
    private File getAbsFile(TreeFile tf) {
        return new File(toAbsolutePath(tf.getPathName()));
    }
    
    @Override
    public Iterator<TreeFile> walk(TreeFile file, FileVisitOption... options)
            throws IOException {
        File absFile = getAbsFile(file);
        String entryPath = absFile.getAbsolutePath();
        Iterator<PartFile> it = new FileIterator(absFile, true);
        return new GeneratorIterator<TreeFile>() {
            @Override
            public TreeFile generateNext() {
                while (it.hasNext()) {
                    PartFile pf = it.next();
                    String name = pf.getAbsolutePath();
                    if (name.startsWith(entryPath)) {
                        if (pf.isDirectory()) {
                            return new TreeFile(name + Var.FS);
                        } else {
                            return new TreeFile(name);
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
        File absFile = getAbsFile(file);
        if (absFile.exists()) return absFile.length();
        else return 0;
    }
    
    @Override
    public boolean exists(TreeFile file) {
        return getAbsFile(file).exists();
    }
    
    @Override
    public InputStream getStream(TreeFile file)
            throws IOException, SecurityException {
        return new FileInputStream(getAbsFile(file));
    }
    
    @Override
    public byte[] readAllBytes(TreeFile file)
            throws IOException, OutOfMemoryError, SecurityException {
        return Files.readAllBytes(getAbsFile(file).toPath());
    }
    
    @Override
    public boolean isDirectory(TreeFile file)
            throws IOException {
        return getAbsFile(file).isDirectory();
    }
    
    @Override
    public TreeFile[] list(TreeFile parent)
            throws IOException {
        List<TreeFile> children = new ArrayList<>();
        for (File f : getAbsFile(parent).listFiles()) {
            if (f.isDirectory()) {
                children.add(new TreeFile(f.getPath() + Var.FS));
            } else {
                children.add(new TreeFile(f));
            }
        }
        return children.toArray(new TreeFile[children.size()]);
    }
    
    
}
