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

package tools.iterators;


// Java imports
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;


// Tools imports
import tools.Var;
import tools.io.PartFile;


/**
 * Iterator implementation which iterates over all files in a directory. <br>
 * <br>
 * It is guaranteed that:
 * <ul>
 *   <li> All files which have the given directory as (indirect) root will be listed. </li>
 *   <li> All non-directory files are listed directly below their parent directory.
 *        No directory will be listed between these non-directory files with the same
 *        parent directory. </li>
 *   <li> All directory files are listed directly below the non-directory
 *        files of their root directory. </li>
 * </ul>
 * No other assumptions about the order of the files can be made.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class FileIterator
        extends GeneratorIterator<PartFile> {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The root directory to iterate over. */
    private final PartFile root;
    /** Whether to list the directories or not. */
    private final boolean listDirs;
    
    /** The stack for keeping track of which files to add. */
    private final Stack<PartFile> stack = new Stack<>();
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new file iterator which will iterate over all children of
     * the given {@code root} file. This includes all files which have the
     * root file as indirect parent directory. <br>
     * If {@code root} is a file, then only {@code root} will be returned.
     * 
     * @param root The root file.
     * @param listDirs Whether to list the directories.
     * 
     * @throws IOException If some IO error occured.
     */
    public FileIterator(File root, boolean listDirs)
            throws IOException {
        if (!root.exists()) throw new FileNotFoundException(root.toString());
        this.root = new PartFile(root.toString(), "");
        this.listDirs = listDirs;
        stack.push(this.root);
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    protected PartFile generateNext() {
        while (!stack.isEmpty()) {
            PartFile file = stack.pop();
            if (file.isFile()) {
                return file;
            }
            
            String prefix = file.getRelativeFileName();
            List<PartFile> files = new LinkedList<>();
            
            // Add directories, remember files.
            for (File child : file.listFiles()) {
                PartFile childPartFile = new PartFile(file.getDirName(), prefix + Var.FS + child.getName());
                if (child.isFile()) files.add(childPartFile);
                else stack.push(childPartFile);
            }
            // Add files after the directories.
            for (PartFile childDir : files) {
                stack.push(childDir);
            }
            if (listDirs) return file;
        }
        done();
        return null;
    }
    
    
}
