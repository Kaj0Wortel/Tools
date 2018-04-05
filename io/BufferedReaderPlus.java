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

package tools.io;


// Java imports
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java.util.ArrayList;


/* 
 * Provides an easy way of processing a data file.
 * Has the following functionality:
 *  - Keeps track of a line counter.
 *  - Supports custom comment (single line and multiple line).
 *  - Supports easy csv file handeling:
 *    - Read single cell.
 *    - Read line of cells.
 *  - Supports stream marking.
 *  - Supports easy pre and post line processing when extneded
 *  - Can be used as a wrapper.
 */
public class BufferedReaderPlus
    extends BufferedReader
    implements Closeable, AutoCloseable {
    
    // Types of supported comments
    final public static int NO_COMMENT = -1;
    final public static int HASHTAG_COMMENT = 0;
    final public static int JAVA_SINGLE_LINE_COMMENT = 1;
    final public static int JAVA_COMMENT = 2;
    final public static int DOCUMENT_WITH_LINKS_COMMENT = 3;
    final public static int HTML_COMMENT = 4;
    
    // Current linecounter in the file
    protected int lineCounter = 0;
    
    // true iff in a multipleLineComment block
    protected boolean multipleLineCommentActive = false;
    
    // Temporary storage for the mark option
    protected int markedLineCounter = 0;
    protected boolean markedMultipleLineCommentActive = false;
    protected String markedBufferedLine = "";
    
    // Stores the current line when reading cells (csv filetypes only)
    protected String bufferedLine = "";
    
    // Stores the current comment String
    private String singleCommentString;
    private String multipleCommentStartString;
    private String multipleCommentEndString;
    
    // Boolean that denotes whether the input file is a csv or not
    private boolean isCsv = false;
    
    
    /* -------------------------------------------------------------------------
     * Constructor
     * -------------------------------------------------------------------------
     */
    /* 
     * Constructors with String to file location.
     * These constructors only convert the given String to a FileReader
     *     and let the other constructor handle the rest.
     */
    // For the constructors with supported comments.
    public BufferedReaderPlus(String file) throws FileNotFoundException {
        this(new FileReader(file));
    }
    
    public BufferedReaderPlus(String file, int commentType)
        throws FileNotFoundException {
        this(new FileReader(file), commentType);
    }
    
    public BufferedReaderPlus(String file, boolean isCsv)
        throws FileNotFoundException {
        this(new FileReader(file), isCsv);
    }
    
    public BufferedReaderPlus(String file, int commentType, boolean isCsv)
        throws FileNotFoundException {
        this(new FileReader(file), commentType, isCsv);
    }
    
    // For constructors with custom comments.
    public BufferedReaderPlus(String file, String singleComment)
        throws FileNotFoundException {
        this(new FileReader(file));
    }
    
    public BufferedReaderPlus(String file, String singleComment, boolean isCsv)
        throws FileNotFoundException {
        this(new FileReader(file), singleComment, isCsv);
    }
    
    public BufferedReaderPlus(String file, String multipleCommentStart,
                              String multipleCommentEnd)
        throws FileNotFoundException {
        this(new FileReader(file), multipleCommentStart, multipleCommentEnd);
    }
    
    public BufferedReaderPlus(String file, String multipleCommentStart,
                              String multipleCommentEnd, boolean isCsv)
        throws FileNotFoundException {
        this(new FileReader(file), multipleCommentStart, multipleCommentEnd,
             isCsv);
    }
    
    public BufferedReaderPlus(String file, String singleComment,
                              String multipleCommentStart,
                              String multipleCommentEnd)
        throws FileNotFoundException {
        this(new FileReader(file), singleComment, multipleCommentStart,
             multipleCommentEnd);
    }
    
    public BufferedReaderPlus(String file, String singleComment,
                              String multipleCommentStart,
                              String multipleCommentEnd, boolean isCsv)
        throws FileNotFoundException {
        this(new FileReader(file), singleComment, multipleCommentStart,
             multipleCommentEnd, isCsv);
    }
    
    
    /* 
     * Constructors with supported comments.
     * 
     * @param reader the used reader object.
     * @param commentType the supported type of comment used.
     *     Must be one of NO_COMMENT, HASHTAG_COMMENT,
     *     JAVA_SINGLE_LINE_COMMENT, JAVA_COMMENT, DOCUMENT_WITH_LINKS_COMMENT
     *     or HTML_COMMENT.
     * @param isCsv whether the file should be interpreted as a csv file.
     */
    public BufferedReaderPlus(Reader reader) {
        this(reader, NO_COMMENT);
    }
    
    public BufferedReaderPlus(Reader reader, int commentType) {
        this(reader, commentType, false);
    }
    
    public BufferedReaderPlus(Reader reader, boolean isCsv) {
        this(reader, NO_COMMENT, isCsv);
    }
    
    public BufferedReaderPlus(Reader reader, int commentType, boolean isCsv) {
        super(reader);
        this.isCsv = isCsv;
        
        if (commentType == HASHTAG_COMMENT) {
            singleCommentString = "#";
            
        } else if (commentType == JAVA_SINGLE_LINE_COMMENT) {
            singleCommentString = "//";
            
        } else if (commentType == JAVA_COMMENT) {
            singleCommentString = "//";
            multipleCommentStartString = "/*";
            multipleCommentEndString = "*/";
            
        } else if (commentType == DOCUMENT_WITH_LINKS_COMMENT) {
            singleCommentString = "/:\\";
            
        } else if (commentType == HTML_COMMENT) {
            multipleCommentStartString = "<!--";
            multipleCommentEndString = "-->";
        }
    }
    
    /* 
     * Constructors with custom comments.
     * 
     * @param reader the used reader object.
     * @param singleComment the String used to indicate a single line comment.
     *     Default is "".
     * @param multipleCommentStart the String used to indicate the start of
     *     a multiple line comment. Default is "".
     * @param multipleCommentEnd the String used to indicate the end of
     *     a multiple line comment. Default is "".
     * @param isCsv whether the file should be interpreted as a csv file.
     *     Default is false.
     */
    // Single line only
    public BufferedReaderPlus(Reader reader, String singleComment) {
        this(reader, singleComment, null, null, false);
    }
    
    public BufferedReaderPlus(Reader reader, String singleComment,
                              boolean isCsv) {
        this(reader, singleComment, null, null, isCsv);
    }
    
    // Multiple line only
    public BufferedReaderPlus(Reader reader, String multipleCommentStart,
                              String multipleCommentEnd) {
        this(reader, null, multipleCommentStart, multipleCommentEnd, false);
    }
    
    public BufferedReaderPlus(Reader reader, String multipleCommentStart,
                              String multipleCommentEnd, boolean isCsv) {
        this(reader, null, multipleCommentStart, multipleCommentEnd, isCsv);
    }
    
    // Both comment types
    public BufferedReaderPlus(Reader reader, String singleComment,
                              String multipleCommentStart,
                              String multipleCommentEnd) {
        this(reader, singleComment, multipleCommentStart, multipleCommentEnd,
             false);
    }
    
    // Full constructor
    public BufferedReaderPlus(Reader reader, String singleComment,
                              String multipleCommentStart,
                              String multipleCommentEnd, boolean isCsv) {
        super(reader);
        this.isCsv = isCsv;
        
        singleCommentString = singleComment;
        multipleCommentStartString = multipleCommentStart;
        multipleCommentEndString = multipleCommentEnd;
    }
    
    /* -------------------------------------------------------------------------
     * Fuctions
     * -------------------------------------------------------------------------
     */
    /* 
     * Reads a line.
     * 
     * @returns the next line of the file.
     * @throws IOException when no read actions are allowed from the file.
     */

    @Override
    public String readLine() throws IOException {
        lineCounter++;
        return super.readLine();
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
            // Read the line
            line = readLine();
            
            // Process the line
            line = postProcessLine
                (processLine
                     (preProcessLine(line))
                );
            
            // Check for null
            if (line != null) {
                // Remove starting and trailing spaces
                line = line.trim();
                
                // Remove trailing semi-colons and remove staring and
                // trailing spaces if it is a csv file.
                if (isCsv) {
                    line = removeCSVTrailings(line).trim();
                }
            }
            
        } while (line != null && line.equals(""));
        
        return line;
    }
    
    /* 
     * Pre-processes the given line.
     * Is supposed to be overridden to do operations before
     *     processing the line.
     * 
     * @param line text to be processed.
     * @return processed line.
     */
    protected String preProcessLine(String line) {
        return line;
    }
    
    /* 
     * Post-processes the given line.
     * Is supposed to be overriden to do operations after processing the line.
     * 
     * @param lien text to be processed.
     * @return processed line.
     */
    protected String postProcessLine(String line) {
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
        // If the input is null, nothing can be processed.
        if (line == null) return null;
        
        boolean hasSingleLineComment
            = !(singleCommentString == null ||
                singleCommentString.equals(""));
        boolean hasMultipleLineComment
            = !(multipleCommentStartString == null ||
                multipleCommentStartString.equals("") ||
                multipleCommentEndString == null ||
                multipleCommentEndString.equals(""));
        
        // If there are no processing actions, simply return the line.
        if (!hasSingleLineComment && !hasMultipleLineComment) return line;
        
        // Marks the begin of a multiple line comment.
        int beginMLC = 0;
        
        for (int pointer = 0; pointer < line.length(); pointer++) {
            if (multipleLineCommentActive) {
                // Should not occur.
                if (!hasMultipleLineComment) {
                    multipleLineCommentActive = false;
                    pointer--;
                    continue;
                }
                
                // Checks if there could be a multiple line comment ending.
                if (line.length() - pointer
                        - multipleCommentEndString.length() >= 0) {
                    // Checks for multiple line comment ending.
                    if (line.substring(pointer, pointer
                                           + multipleCommentEndString.length())
                            .equals(multipleCommentEndString)) {
                        line = (beginMLC == 0
                                    ? ""
                                    : line.substring(0, beginMLC));
                        line += line.substring
                            (pointer + multipleCommentEndString.length());
                        
                        multipleLineCommentActive = false;
                        pointer += multipleCommentEndString.length() - 1;
                    }
                    
                } else {
                    // If not, ignore the rest of the line.
                    return (beginMLC == 0 ? "" : line.substring(0, beginMLC));
                }
                
            } else {
                // No multiple line comment is active.
                
                if (hasSingleLineComment) {
                    // Checks if there could be a single line comment.
                    if (line.length() - pointer
                            - singleCommentString.length() >= 0) {
                        // Checks for single line comment.
                        if (line.substring(pointer, pointer
                                               + singleCommentString.length())
                                .equals(singleCommentString)) {
                            return line.substring(0, pointer);
                        }
                    }
                }
                
                if (hasMultipleLineComment) {
                    // Checks if there could be a multiple line comment start.
                    if (line.length() - pointer
                            - multipleCommentStartString.length() >= 0) {
                        // Checks for multiple line comment start.
                        if (line.substring
                                (pointer, pointer
                                     + multipleCommentStartString.length())
                                .equals(multipleCommentStartString)) {
                            beginMLC = pointer;
                            multipleLineCommentActive = true;
                            pointer += multipleCommentStartString.length() - 1;
                        }
                    }
                }
            }
        }
        
        return line;
    }
    
    /* 
     * Removes trailing semi-colons from csv-files
     * 
     * @param line the line to remove the trainling semi-colons from.
     * @return a line without trailing semi-colons.
     */
    public static String removeCSVTrailings(String line) {
        if (line == null) return null;
        if (line.length() == 0) return "";
        
        // iterate backwards
        int i = line.length() - 1;
        for (; i >= 0 && line.charAt(i) == ';'; i--);
        
        return (i == 0 ? "" : line.substring(0, i + 1));
    }
    
    /* 
     * Reads a processed cell from a csv file.
     * 
     * @param processed whether to use processed lines as input or not.
     * @param lineBlock whether to return null if the end of the line has been
     *     reached (true), or to take the next line (false).
     * @return the data of a single cell from the file. Returns null iff
     *     EOF reached or if lineBlock == true and EOL reached.
     * @throws IOException iff the data could not be retrieved from the file.
     * @throws IllegalArgumentException iff if the file was not set as
     *     a csv file.
     * 
     * If no line was buffered, pick the next one iff lineBlock == false.
     *     Otherwise return null.
     */
    public String readCSVCell(boolean processed)
        throws IOException, IllegalArgumentException {
        return readCSVCell(processed, false);
    }
    
    public String readCSVCell(boolean processed, boolean lineBlock)
        throws IOException, IllegalArgumentException {
        if (!isCsv)
            throw new IllegalArgumentException
            ("File type is not declared as \".csv\"");
        
        // Update the buffer if nessecary
        if (bufferedLine == null || bufferedLine.length() == 0) {
            // If the bufferlength == 0, read new line if allowed.
            // Otherwise return null.
            if (lineBlock) return null;
            
            // Reads the next line
            bufferedLine = (processed ? readProcessedLine() : readLine());
            
            // Check for EOF
            if (bufferedLine == null) return null;
        }
        
        // Read the buffer
        String line = bufferedLine;
        
        // Takes the substring of the beginning of the line till
        // the first semi-colon.
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ';') {
                bufferedLine = line.substring(i + 1);
                return line.substring(0, i).trim();
            }
        }
        
        // If no semi-colon is in the line, return the full line and
        // set bufferedLine to "".
        bufferedLine = "";
        return line;
    }
    
    /* 
     * Reads either all remaining processed cells on the buffered line or all
     *     cells on a new line from a csv file.
     * 
     * @param processed whether to use processed lines as input or not.
     * @param lineBlock whether to return null if the end of the line has
     *     been reached (true),
     *     or to take the next line (false).
     * @return the data of each cell in an array in increasing order.
     *     Returns null iff EOF reached or if lineBlock == true and EOL reached.
     * @throws IOException iff the data could not be retrieved from the file.
     * @throws IllegalArgumentException iff if the file was not set as
     *     a csv file.
     * 
     * If no line was buffered, pick the next one iff lineBlock == false.
     *     Otherwise return null.
     */
    public ArrayList<String> readCSVLine(boolean processed)
        throws IOException, IllegalArgumentException {
        
        return readCSVLine(processed, false);
    }
    
    public ArrayList<String> readCSVLine(boolean processed, boolean lineBlock)
        throws IOException, IllegalArgumentException {
        
        if (!isCsv)
            throw new IllegalArgumentException
            ("File type is not declared as \".csv\"");
        
        // Read and clear the buffer
        String line = bufferedLine;
        bufferedLine = "";
        
        // Checks the buffer length.
        if (line.length() == 0) {
            // If the bufferlength == 0, read new line iff allowed.
            // Otherwise return null.
            if (lineBlock) return null;
            
            // Reads the next line
            line = (processed ? readProcessedLine() : readLine());
            
            // Check for the null-value
            if (line == null) return null;
        }
        
        ArrayList<String> cells = new ArrayList<String>();
        int prevColon = 0;
        
        // Puts all Strings separated by a semi-colon in the ArrayList.
        // If no semi-colon is present, return the full line as
        // one element in the ArrayList
        for (int i = 0; i <= line.length(); i++) {
            if (i == line.length() || 
                line.charAt(i) == ';') {
                
                if (processed) {
                    cells.add(line.substring(prevColon, i).trim());
                    
                } else {
                    cells.add(line.substring(prevColon, i));
                }
                
                prevColon = i + 1;
            }
        }
        
        return cells;
    }
    
    /* 
     * Marks the present location in the steam.
     * After reading 'readAheadLimit' characters, attempting to reset
     *     the stream may fail. See {@link BufferedReader#mark(int)}.
     * 
     * @param readAheadLimit number of characters that will be at most read
     *     before reset() is called.
     * @throws IOException if the current reader is not initialized
     *     for stream marking.
     */
    @Override
    public void mark(int readAheadLimit) throws IOException {
        markedLineCounter = lineCounter;
        markedMultipleLineCommentActive = multipleLineCommentActive;
        markedBufferedLine = bufferedLine;
        
        super.mark(readAheadLimit);
    }
    
    /* 
     * Resets to the last marked point in the stream.
     * See {@link BufferedReader#mark(int)}.
     * 
     * @throws IOException if the current reader is not initialized for stream marking.
     */
    @Override
    public void reset() throws IOException {
        lineCounter = markedLineCounter;
        multipleLineCommentActive = markedMultipleLineCommentActive;
        bufferedLine = markedBufferedLine;
        
        super.reset();
    }
    
    /* 
     * @return the current linecounter.
     */
    public int getLineCounter() {
        return lineCounter;
    }
    
    /* 
     * Not supported function.
     * 
     * @throws UnsupportedOperationException
     */
    @Override
    @Deprecated
    public int read() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Operation was not supported");
    }
    
    /* 
     * Not supported function.
     * 
     * @throws UnsupportedOperationException
     */
    @Override
    @Deprecated
    public int read(char[] cbuf, int off, int len) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Operation was not supported");
    }
    
}



