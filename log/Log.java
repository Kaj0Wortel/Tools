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
//todo
package tools.log;


// Java imports
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Arrays;
import java.util.Date;


/*
 * This file is currently under development and should not be used.
 * (reviving old artefact class).
 */
public class Log extends Loggable {
    final private static String logFile = System.getProperty("user.dir") + "\\tools\\log.txt";
    final private static String ls = System.getProperty("line.separator");
    private static PrintWriter writer;
    
    /* 
     * This is a static singleton class. No instances should be made.
     */
    @Deprecated
    private Log() {}
    
    public static void write(boolean text) {
        write(text, true);
    }
    public static void write(boolean text, boolean showDate) {
        if (text) {
            write("true", showDate);
        } else {
            write("false", showDate);
        }
    }
    
    public static void write(char text) {
        write(text, true);
    }
    public static void write(char text, boolean showDate) {
        write(text + "", showDate);
    }
    
    public static void write(int number) {
        write(number, true);
    }
    public static void write(int number, boolean showDate) {
        write(Integer.toString(number), showDate);
    }
    
    public static void write(double number) {
        write(number, true);
    }
    public static void write(double number, boolean showDate) {
        write(Double.toString(number), showDate);
    }
    
    public static void write(Exception e) {
        write(e, true, true);
    }
    public static void write(Exception e, boolean full) {
        write(e, full, true);
    }
    public static void write(Exception e, boolean full, boolean showDate) {
        if (full) {
            String[] text = Arrays.toString(e.getStackTrace()).split(", ");
            String message = "[ERROR] " + e.getClass().getName() + ": " + e.getMessage();
            write(message, showDate);
            for (int i = 0; i < text.length; i++) {
                write("        " + text[i], false);
            }
            
        } else {
            String message = "[ERROR] " + e.getClass().getName() + ": " + e.getMessage();
            write(message, showDate);
        }
    }
    
    public static void write(String text) {
        write(text, true);
    }
    public static void write(String text, boolean showDate) {
        try {
            // create file writer (append to/create file)
            writer = new PrintWriter(new BufferedWriter(new FileWriter(logFile, false)));
            
            if (showDate) {
                // determine and print date
                DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
                Date date = new Date();
                writer.write("[" + dateFormat.format(date) + "] ");
                
            } else {
                writer.write("             ");
            }
            // print text
            writer.write(text + ls);
            
            writer.close();
            
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not write to log file");
        }
    }
    
    public static boolean clear() {
        try {
            // create file writer (overwrite/create file)
            writer = new PrintWriter(new BufferedWriter(new FileWriter(logFile, false)));
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy - HH:mm:ss:SSS");
            Date date = new Date();
            writer.write(dateFormat.format(date));
            writer.write(ls);
            writer.close();
            
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[ERROR] Could not clear the log file");
            return false;
        }
        return true;
    }
}