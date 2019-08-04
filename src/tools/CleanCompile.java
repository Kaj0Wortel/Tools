/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) July 2019 by Kaj Wortel - all rights reserved               *
 * Contact: kaj.wortel@gmail.com                                             *
 *                                                                           *
 * This file is part of the tools project, which can be found on github:     *
 * https://github.com/Kaj0Wortel/tools                                       *
 *                                                                           *
 * It is allowed to use, (partially) copy and modify this file               *
 * in any way for private use only by using this header.                     *
 * It is not allowed to redistribute any (modifed) versions of this file     *
 * without my permission.                                                    *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package tools;


// Java import
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;


// Tools imports.
import tools.io.FileTools;
import tools.io.PartFile;


/**
 * Contains a method to clean and compile an entire folder or file.
 * 
 * @todo
 * - add functionality for commandline arguments in {@link #main(String[] args)}.
 * 
 * @apiNote
 * Only works when the application uses JDK, and <b>NOT</b> JRE!
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class CleanCompile {
    
    /**
     * Compiles the provided directory tree or file.
     * 
     * @param dir The root directory to compile, or a single file.
     */
    public static void cleanCompile(File dir) {
        List<PartFile> filesInDir = FileTools.getFileList(dir, false);
        
        // Delete all class files.
        for (PartFile file : filesInDir) {
            String name = file.getName();
            if (name.endsWith(".class")) {
                file.delete();
            }
        }
        
        // Compile all files
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        ArrayList<String> filesToCompileList = new ArrayList<String>();
        
        for (PartFile file : filesInDir) {
            String name = file.getName();
            if (name.endsWith(".java")) {
                filesToCompileList.add(file.getPath());
            }
        }
        
        String[] filesToCompile = filesToCompileList.toArray(new String[filesToCompileList.size()]);
        compiler.run(null, null, null, filesToCompile);
    }
    
    /**
     * Cleans and compiles all given files/directories.
     * 
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            //System.out.println("Use -h or --help for help");
            return;
        }
        
        for (String file : args) {
            System.out.println("Begin clean compile of: " + file);
            CleanCompile.cleanCompile(new File(file));
            System.out.println("done");
        }
    }
    
    
}