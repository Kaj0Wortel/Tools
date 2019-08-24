/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) July 2019 by Kaj Wortel - all rights reserved               *
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

package tools.io;


// Java imports
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.CopyOption;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Consumer;


// Tools imports
import tools.BoolEvaluator;
import tools.Var;
import tools.data.BlockByteBuffer;


/**
 * Tool classes for all kinds of file operation.
 * 
 * @todo
 * - Take a look at the forEachFile functions
 * 
 * @version 0.0
 * @author Kaj Wortel
 */
public class FileTools {
    
    /* -------------------------------------------------------------------------
     * Constants.
     * -------------------------------------------------------------------------
     */
    /** Value used for the moving all files in a tree. */
    private final static int MOVE_FILES = 0;
    /** Value used for the copying all files in a tree. */
    private final static int COPY_FILES = 1;
    /** Value used for the deleting all files in a tree. */
    private final static int DELETE_FILES = 2;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * This is a static singleton class. No instances should be made.
     *
     * @deprecated No instances should be made.
     */
    @Deprecated
    private FileTools() { }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Reads a file and parses it into an array.
     * The data in the file is split on new line character in the array.
     * 
     * @param file The file to read from.
     * @return An array representing the file.
     * 
     * @throws IOException if there occured an exception during the IO operations.
     */
    public static String[] readFile(File file)
            throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            List<String> data = new ArrayList();
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line);
            }
            return data.toArray(new String[data.size()]);
        }
    }
    
    /**
     * Passes each line of a file to a consumer.
     * 
     * @param file The file to read from.
     * @param action The consumer to give the lines to.
     * 
     * @throws IOException Iif there occured an exception during the IO operations.
     */
    public static void forEach(File file, Consumer<String> action)
            throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                action.accept(line);
            }
        }
    }
    
    /**
     * Parses the data and writes it into a file.
     * Each element in {@code data} is written on a separate line.
     * 
     * @param file The file to write to.
     * @param data The data to parse.
     * 
     * @throws IOException Iif there occured an exception during the IO operations.
     */
    public static void writeFile(File file, String[] data, boolean append)
            throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            boolean first = true;
            for (String line : data) {
                if (first) first = false;
                else bw.write(Var.LS);
                bw.write(line);
            }
        }
    }
    
    /**
     * Lists all files in the given root directory. <br>
     * Furthermore, it holds for any entry that:
     * <ul>
     *   <li> if it represents a file, then it is listed below it's parent
     *        directory. </li>
     *   <li> if it represents a directory, then it is listed below the
     *        files in it's parent directory. </li>
     *   <li> it is <b>not</b> listed below any directories which
     *        are on the same level as their parent directory and which are
     *        also listed below the parent directory entry. </li>
     * </ul>
     * No other assumptions regarding file-order can be made on the output. <br>
     * Note that the directory entries are only listed if {@code listDirs}.
     * 
     * 
     * @param root The root directory to start listing from.
     * @param listDirs Whether to list the directories.
     * 
     * @return A list containing all files in the given root directory.
     */
    public static List<PartFile> getFileList(File root, boolean listDirs) {
        if (!root.exists()) return new ArrayList(0);
        
        List<PartFile> output = new ArrayList<>();
        if (root.isFile()) {
           output.add(new PartFile(root, ""));
           return output;
        }
        
        Stack<PartFile> dirStack = new Stack<>();
        dirStack.push(new PartFile(root, ""));
        
        while (!dirStack.isEmpty()) {
            PartFile pf = dirStack.pop();
            if (pf.isFile()) {
                output.add(pf);
                
            } else {
                if (listDirs) output.add(pf);
                for (File f : pf.listFiles()) {
                    if (f.isFile()) {
                        output.add(new PartFile(root, pf.getRelativeFileName() + f.getName()));
                        
                    } else {
                        dirStack.push(new PartFile(root, pf.getRelativeFileName() + f.getName() + Var.FS));
                    }
                }
            }
        }
        
        return output;
    }
    
    /**
     * Returns an {@link InputStream} which generates a tree structure from the given root directory.
     * 
     * @param root The root directory.
     * 
     * @return An {@link InputStream} which generates a tree structure from the given root directory.
     */
    public static InputStream listFileTree(File root) {
        return new FileLister(root);
    }
    
    /**
     * Class for listing all files in a direcctory.
     */
    private static final class FileLister
            extends InputStream {
        
        /* --------------------
         * Constants
         * --------------------
         */
        /** Constant used for appending vertical lines to the buffer. */
        private static final byte[] VERT_LINE = "│".getBytes();
        /** Constant used for appending vertical lines with a right line to the buffer. */
        private static final byte[] VERT_RIGHT_LINE = "├".getBytes();
        /** Constant used for appending an angled line to the buffer. */
        private static final byte[] VERT_RIGHT_END = "└".getBytes();
        /** Constant used for appending a horizontal line to the buffer. */
        private static final byte[] HORI_LINE = "─".getBytes();
        /** Constant used for appending a space to the buffer. */
        private static final byte[] SPACE = " ".getBytes();
        /** Constant used for appending a line separator to the buffer. */
        private static final byte[] LINE_SEP = Var.LS.getBytes();
        
        /** The root dir of the tree. */
        private final File root;
        /** The buffer used to store the currently processed bytes in. */
        private final BlockByteBuffer buffer = new BlockByteBuffer();
        
        /** Stack used for storing the indents. */
        private final Stack<byte[]> preStack = new Stack<>();
        /** Stack used for storing the current location in the file tree. */
        private final Stack<Deque<PartFile>> stack = new Stack<>();
        /** Deque used to iterate over the files. */
        private final Deque<PartFile> files = new LinkedList<>();
        
        /** Whether this is the first item in the tree. */
        private boolean init = true;
        /** String builder used to add concatenated dirs. */
        private StringBuilder dirBuilder = new StringBuilder();
        /** Whether the current entry is part of a concatenated dir. */
        private boolean concatDirs = false;
        /** Whether the first concatenated dir was the last item for that level. */
        private boolean concatIsLast = false;
        /** Stack for keeping track of which directory was concatenated. */
        private Stack<Boolean> isConcat = new Stack<>();
        
        
        /**
         * Creates a new file lister.
         * 
         * @param root The root file/directory to print the tree for.
         */
        private FileLister(File root) {
            this.root = root;
            
            if (root.isFile()) {
                buffer.add(root.toString().getBytes());
                buffer.add(LINE_SEP);
                
            } else {
                Deque<PartFile> initList = new LinkedList<>();
                initList.addLast(new PartFile(root, ""));
                stack.push(initList);
            }
        }
        
        @Override
        public int read()
                throws IOException {
            ensureBytes(1);
            if (buffer.isEmpty()) return -1;
            return buffer.getByte() & 0xFF;
        }
        
        private void ensureBytes(int amt) {
            while (buffer.size() < amt) {
                if (files.isEmpty()) {
                    if (stack.isEmpty()) return;
                    
                    Deque<PartFile> dirStack;
                    do {
                        dirStack = stack.peek();
                        if (dirStack.isEmpty()) {
                            stack.pop();
                            
                            // Update pre stack if needed.
                            if (preStack.size() >= 3 && !isConcat.pop()) {
                                preStack.pop();
                                preStack.pop();
                                preStack.pop();
                            }
                            dirStack = null;
                        }
                    } while (dirStack == null && !stack.isEmpty());
                    
                    if (stack.isEmpty()) return;
                    
                    @SuppressWarnings("null")
                    PartFile dir = dirStack.removeFirst();
                    Deque<PartFile> newDirs = new LinkedList<>();
                    
                    for (File f : dir.listFiles()) {
                        if (f.isFile()) {
                            files.addLast(new PartFile(
                                    root, dir.getRelativeFileName() + f.getName()));
                            
                        } else {
                            newDirs.addLast(new PartFile(
                                    root, dir.getRelativeFileName() + f.getName() + Var.FS));
                        }
                    }
                    
                    stack.push(newDirs);
                    
                    // Concatenate directories if there is only one directory and no files
                    // in the current directory.
                    if (newDirs.size() == 1 && files.isEmpty()) {
                        isConcat.push(true);
                        dirBuilder.append(dir.getName());
                        dirBuilder.append(Var.FS);
                        if (!concatDirs) {
                            concatDirs = true;
                            concatIsLast = dirStack.isEmpty();
                        }
                        continue;
                    }
                    
                    isConcat.push(false);
                    
                    // Add dir to buffer.
                    boolean last = (concatDirs ? concatIsLast : dirStack.isEmpty());
                    dirBuilder.append(dir.getName());
                    dirBuilder.append(Var.FS);
                    addToBuffer(last, dirBuilder.toString());
                    
                    dirBuilder = new StringBuilder();
                    concatDirs = false;
                    
                    // Update pre stack.
                    if (init) {
                        init = false;
                        
                    } else {
                        if (last) preStack.add(SPACE);
                        else preStack.add(VERT_LINE);
                        preStack.add(SPACE);
                        preStack.add(SPACE);
                    }
                    
                } else {
                    PartFile file = files.pop();
                    boolean last = files.isEmpty() && stack.peek().isEmpty();
                    addToBuffer(last, file.getName());
                }
            }
        }
        
        private void addToBuffer(boolean last, String name) {
            if (!init) {
                for (byte[] pre : preStack) {
                    buffer.add(pre);
                }
            
                if (last) buffer.add(VERT_RIGHT_END);
                else buffer.add(VERT_RIGHT_LINE);
                buffer.add(HORI_LINE);
                buffer.add(SPACE);
            }
            buffer.add(name.getBytes());
            buffer.add(LINE_SEP);
        }
        
        
    }
    
    /**
     * Moves an entire file tree from {@code soruce} to {@code target}.
     *
     * @param source The source file/directory of the move.
     * @param target The target file/directory of the move.
     * @param options The copy options for moving the files.
     * 
     * @throws IOException If some IO error occured.
     *
     * @see Files#move(Path, Path, CopyOption...)
     * @see #fileTreeAction(File, File, BoolEvaluator, int, CopyOption...)
     */
    public static void moveFileTree(File source, File target,
            CopyOption... options)
            throws IOException {
        moveFileTree(source, target, null, options);
    }
    
    /**
     * Moves an entire file tree from {@code soruce} to {@code target}.
     *
     * @param source The source file/directory of the move.
     * @param target The target file/directory of the move.
     * @param cmp The comparator for ignoring files.
     * @param options The copy options for moving the files.
     * 
     * @throws IOException If some IO error occured.
     *
     * @see Files#move(Path, Path, CopyOption...)
     * @see #fileTreeAction(File, File, BoolEvaluator, int, CopyOption...)
     */
    public static void moveFileTree(File source, File target,
            BoolEvaluator<File> cmp, CopyOption... options)
            throws IOException {
        fileTreeAction(source, target, cmp, MOVE_FILES, options);
    }
    
    /**
     * Copies an entire file tree from {@code soruce} to {@code target}.
     *
     * @param source the source file/directory of the copy.
     * @param target the target file/directory of the copy.
     * @param options the copy options for moving the files.
     * 
     * @throws IOException If some IO error occured.
     *
     * @see Files#copy(Path, Path, CopyOption...)
     * @see #fileTreeAction(File, File, BoolEvaluator, int, CopyOption...)
     */
    public static void copyFileTree(File source, File target,
            CopyOption... options)
            throws IOException {
        copyFileTree(source, target, null, options);
    }
    
    /**
     * Copies an entire file tree from {@code soruce} to {@code target}.
     *
     * @param source the source file/directory of the copy.
     * @param target the target file/directory of the copy.
     * @param cmp the comparator for ignoring files.
     * @param options the copy options for moving the files.
     * 
     * @throws IOException If some IO error occured.
     *
     * @see Files#copy(Path, Path, CopyOption...)
     * @see #fileTreeAction(File, File, BoolEvaluator, int, CopyOption...)
     */
    public static void copyFileTree(File source, File target,
            BoolEvaluator<File> cmp, CopyOption... options)
            throws IOException {
        fileTreeAction(source, target, cmp, COPY_FILES, options);
    }
    
    /**
     * Deletes the entire file tree {@code soruce}.
     *
     * @param source the source file/directory of the delete.
     * 
     * @throws IOException If some IO error occured.
     * 
     * @see File#delete()
     * @see #fileTreeAction(File, File, BoolEvaluator, int, CopyOption...)
     */
    public static void deleteFileTree(File source)
            throws IOException {
        deleteFileTree(source, null);
    }
    
    /**
     * Deletes the entire file tree {@code soruce}.
     *
     * @param source the source file/directory of the delete.
     * @param cmp the comparator for ignoring files.
     * 
     * @throws IOException If some IO error occured.
     * 
     * @see File#delete()
     * @see #fileTreeAction(File, File, BoolEvaluator, int, CopyOption...)
     */
    public static void deleteFileTree(File source, BoolEvaluator<File> cmp)
            throws IOException {
        fileTreeAction(source, null, cmp, DELETE_FILES);
    }
    
    /**
     * Performs an action on an entire file tree.
     * <br><br>
     * The actions must be one of:<ul>
     *   <li> {@link #MOVE_FILES}</li>
     *   <li> {@link #COPY_FILES}</li>
     *   <li> {@link #DELETE_FILES}</li>
     * </ul>
     * Each file is processed if, and only if<br>
     * {@code (cmp == null || !.evaluate(file))}<br>
     * holds for that file.<br>
     * <br>
     * The comparable is also applied on directories. In this case if<br>
     * {@code (cmp != null && cmp.evaluate(dir))} holds,<br>
     * then all files and sub-directories in that directory are ignored.<br>
     * <br>
     * Note that ignored files will not be processed.
     *
     * @param source the root of the source file tree.
     * @param target the root of the target file tree.
     * @param cmp the comparator for ingoring files/directories.
     * @param action the action to exectute for each file.
     * @param options the copy options used for copying files.
     * @throws IOException if the files could not be read or modified.
     *
     * @see Files#move(Path, Path, CopyOption...)
     * @see Files#copy(Path, Path, CopyOption...)
     * @see File#delete()
     */
    private static void fileTreeAction(File source, File target,
            BoolEvaluator<File> cmp, int action, CopyOption... options)
            throws IOException {
        if (!source.exists()) return;
        
        if (source.isFile()) {
            if (cmp == null || !cmp.evaluate(source)) {
                if (action == MOVE_FILES) {
                    Files.move(source.toPath(), target.toPath(), options);
                    
                } else if (action == COPY_FILES) {
                    Files.copy(source.toPath(), target.toPath(), options);
                    
                } else if (action == DELETE_FILES) {
                    source.delete();
                }
            }
            
        } else {
            if (target != null) target.mkdirs();
            
            for (File file : source.listFiles()) {
                if (cmp == null || !cmp.evaluate(file)) {
                    File newRoot = new File(
                            source.getPath() + Var.FS + file.getName());
                    File newTarget = (target == null ? null : new File(
                            target.getPath() + Var.FS + file.getName()));
                    
                    fileTreeAction(newRoot, newTarget, cmp, action, options);
                    
                    if (action == MOVE_FILES || action == DELETE_FILES) {
                        if (!file.isFile()) file.delete();
                    }
                }
            }
        }
    }
    
    /**
     * Deletes all files in a directory, or deletes a single file
     * if {@code file} is a file.
     *
     * @param dir The directory to delete all files of.
     * @param deleteSelf Whether to delete the directory itself.
     */
    public static void deleteAll(File dir, boolean deleteSelf) {
        if (dir.isFile()) {
            if (deleteSelf) dir.delete();
            return;
        }
        
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                deleteAll(file, true);
            }
        }
        
        if (deleteSelf) dir.delete();
    }
    
    /**
     * Executes an action for each file starting at the given
     * root directory.
     * <br>
     * If the root is a file, then it is accepted by default.
     * If the root is a directory, then iterate over all files in that
     * directory.
     * <br><br>
     * If directories should be used in actions, they are always listed before
     * any other files within that directory.
     * 
     * @param root The root to start performing the actions.
     * @param action The action to perform.
     * 
     * @deprecated The functionality already exists in {@link tools.data.file.FileTree}.
     */
    @Deprecated(forRemoval = true)
    public static void forEachFile(File root, Consumer<File> action) {
        forEachFile(root, true, action);
    }
    
    /**
     * Executes an action for each file starting at the given
     * root directory.
     * <br>
     * If the root is a file, then it is accepted by default.
     * If the root is a directory, then iterate over all files in that
     * directory.
     * <br><br>
     * If directories should be used in actions, they are always listed before
     * any other files within that directory.
     * 
     * @param root The root to start performing the actions.
     * @param listDirs Whether to perform the action on directories.
     * @param action The action to perform.
     * 
     * @deprecated The functionality already exists in {@link tools.data.file.FileTree}.
     */
    @Deprecated(forRemoval = true)
    public static void forEachFile(File root, boolean listDirs,
            Consumer<File> action) {
        if (root.isFile()) {
            action.accept(root);
            
        } else {
            if (listDirs) action.accept(root);
            
            File[] files = root.listFiles();
            if (files == null) return;
            for (File file : files) {
                if (!file.isFile()) {
                    forEachFile(file, listDirs, action);
                    
                } else {
                    action.accept(file);
                }
            }
        }
    }
    
    
}
