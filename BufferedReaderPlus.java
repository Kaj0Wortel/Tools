/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2018 by Kaj Wortel - all rights reserved                *
 * Contact: <kaj.wortel@gmail.com>                                       *
 *                                                                       *
 * This file is part of the tools project.                               *
 *                                                                       *
 * It is allowed to use, (partially) copy and modify this file           *
 * in any way for private use only.                                      *
 * It is not allowed to redistribute any (modifed) versions of this file *
 * without my permission.                                                *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package tools;


// Java packages
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

// tmp
import java.io.File;
import java.io.FileReader;


public class BufferedReaderPlus extends BufferedReader implements Closeable, AutoCloseable {
    final private static String fileName = System.getProperty("user.dir") + "\\building\\input.txt";
    
    private int lineCounter = 0;
    private boolean mutipleLineComment = false;
    private int markedLineCounter = 0;
    private boolean markedMutipleLineComment = false;
    
    /* Denotes which type of comment is used in the file
     * -1 : no comments.
     *  0 : all text after '#' is comments.
     *  1 : all text after '//' is comments and
     *      all text between '/*' and '* /' is comments.
     * Default is 0.
     */
    private int comment = 0;
    
    public BufferedReaderPlus(Reader reader) {
        super(reader);
    }
    
    public BufferedReaderPlus(Reader reader, int commentType) {
        super(reader);
        comment = commentType;
    }
    
    /* 
     * Reads a line.
     * 
     * @returns the next line of the file.
     * @throws IOException when no read actions are allowed from the file.
     */
    @Override
    public String readLine() throws IOException {
        lineCounter++;
        return preProcessLine(super.readLine());
    }
    
    /* 
     * Reads a line while skipping empty lines comments.
     * 
     * @returns the next processed line of the file.
     * @throws IOException when no read actions are allowed from the file.
     */
    public String readProcessedLine() throws IOException {
        String line;
        
        do {
            line = processLine(this.readLine());
        } while (line == null || line.equals(""));
        
        return line;
    }
    
    /* 
     * Pre-processes the given line.
     * Is supposed to be overridden to do operations before processing the line.
     * 
     * @param line text to be processed.
     * @return processed line.
     */
    protected String preProcessLine(String line) {
        return line;
    }
    
    /* 
     * Processes the given line.
     * Used for ignoring single and multiple line comments.
     * 
     * @param line text to be processed
     * @return processed line.
     */
    private String processLine(String line) {
        if (line == null) return null;
        
        // ------------------------------------------------------------------------------------------------------------
        if (comment == 0) { // # comments
            for (int pointer = 0; pointer < line.length(); pointer++) {
                if (line.charAt(pointer) == '#') {
                    // If there are comments, skip the last part of the line.
                    line = line.substring(0, pointer);
                }
            }
            
        } else if (comment == 1) {// //, /* and */ comments
            // --------------------------------------------------------------------------------------------------------
            int startComment = 0; // Start location in the case of multiple line comment.
            
            for (int pointer = 0; pointer < line.length(); pointer++) {
                // Checks the line for comments
                if (mutipleLineComment) {
                    if (line.length() - 1 > pointer) {
                        if (line.charAt(pointer) == '*' && line.charAt(pointer+1) == '/') {
                            
                            mutipleLineComment = false; // Update the multiple line comment flag.
                            pointer += 2;
                            
                            // Remove the comment from the current line
                            line = (startComment == 0       ? "" : line.substring(0, startComment))
                                + (pointer == line.length() ? "" : line.substring(pointer));
                            
                            startComment = 0; // Reset the beginning of the comments.
                            pointer = -1;
                        }
                        
                    } else if (pointer == line.length() - 1) {
                        if (startComment != 0) {
                            line = line.substring(0, startComment);
                            
                        } else {
                            line = "";
                        }
                        
                    }
                    
                } else { // If there are no multiple line comments
                    // Check for single line comments
                    if (pointer + 1 < line.length() && 
                        line.charAt(pointer) == '/' && line.charAt(pointer+1) == '/') {
                        
                        // If there are comments, skip the last part of the line.
                        line = line.substring(0, pointer);
                        
                        // Check for multiple line comments
                    } else if (pointer + 1 < line.length() && 
                               line.charAt(pointer) == '/' && line.charAt(pointer+1) == '*') {
                        
                        startComment = pointer; // Remember the start of a comment.
                        mutipleLineComment = true; // Set the multiple line comment flag.
                        
                        if (line.length() - pointer == 2) {
                            line = line.substring(0, pointer);
                        }
                        
                        pointer += 2; // Skip the two comment characters.
                    }
                }
            }
            
        }
        
        // Return the line
        return line.trim();
    }
    
    /* 
     * Marks the present location in the steam.
     * After reading 'readAheadLimit' characters, attempting to reset the stream may fail.
     * 
     * @param readAheadLimit number of characters that will be at most read before reset() is called.
     * @throws IOException if the current reader is not initialized for stream marking.
     */
    @Override
    public void mark(int readAheadLimit) throws IOException {
        markedLineCounter = lineCounter;
        super.mark(readAheadLimit);
    }
    
    /* 
     * Resets to the last marked point in the stream.
     * 
     * @throws IOException if the current reader is not initialized for stream marking.
     */
    @Override
    public void reset() throws IOException {
        lineCounter = markedLineCounter;
        mutipleLineComment = markedMutipleLineComment;
        super.reset();
    }
    
    /* 
     * Returns the current linecounter.
     * 
     * @return current linecounter.
     */
    public int getLineCounter() {
        return lineCounter;
    }
    
    
    /* ------------------------------
     *       !!! WARNING !!!
     *      SHOULD NOT BE USED
     * ------------------------------
     */
    @Override
    @Deprecated
    public int read() {
        throw new UnsupportedOperationException();
    }
    
    /* ------------------------------
     *       !!! WARNING !!!
     *      SHOULD NOT BE USED
     * ------------------------------
     */
    @Override
    @Deprecated
    public int read(char[] cbuf, int off, int len) throws IOException {
        throw new UnsupportedOperationException();
    }
}



