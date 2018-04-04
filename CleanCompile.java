/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2018 by Kaj Wortel - all rights reserved                    *
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


// Tools import
import tools.MultiTool;


// Java import
import java.io.IOException;
import java.io.File;

import java.util.ArrayList;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

/* 
 * Not that this class only works when it uses JDK and  *NOT*  JRE!
 * Use with caution!
 */
public class CleanCompile {
    public static void cleanCompile(File dir) {
        ArrayList<File[]> filesInDir = MultiTool.listFilesAndPathsFromRootDir(dir, false);
        
        // Delete all class files.
        for (File[] file : filesInDir) {
            String name = file[0].getName();
            if (name.endsWith(".class") && !name.equals("CleanCompile.class") && !name.equals("MultiTool.class")) {
                file[0].delete();
            }
        }
        
        // Compile all files
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        ArrayList<String> filesToCompileList = new ArrayList<String>();
        
        for (File[] file : filesInDir) {
            String name = file[0].getName();
            if (name.endsWith(".java") && !name.equals("CleanCompile.java")) {
                filesToCompileList.add(file[0].getPath());
            }
        }
        
        String[] filesToCompile = MultiTool.listToArray(filesToCompileList, String.class);
        compiler.run(null, null, null, filesToCompile);
    }
    
    public static void main(String[] args) {
        CleanCompile.cleanCompile(new File(System.getProperty("user.dir")));
        System.out.println("done");
    }
}