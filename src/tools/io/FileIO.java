/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) May 2019 by Kaj Wortel - all rights reserved                *
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


// Tools imports
import tools.Var;


// Java imports
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


/**
 * TODO: private constructor + comments.
 * 
 * @author Kaj Wortel (0991586)
 */
public class FileIO {
    
    /**
     * Reads a file and parses it into an array.
     * 
     * @param file the file to read from.
     * @return an array representing the file.
     * @throws IOException if there occured an exception during the
     *     IO operations.
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
     * @param file the file to read from.
     * @param action the consumer to give the lines to.
     * 
     * @throws IOException if there occured an exception during the
     *     IO operations.
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
     * @param file the file to write to.
     * @param data the data to parse.
     * 
     * @throws IOException if there occured an exception during the
     *     IO operations.
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
    
    
}
