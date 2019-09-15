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
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Provides an easy way of processing a data file. Has the following
 * functionality:
 * <ul>
 *   <li> Keeps track of a line counter.</li>
 *   <li> Supports custom comment (single line and multiple line).</li>
 *   <li> Supports CSV file handling:</li><ul>
 *     <li> Format: data;data;data; (etc.)</li>
 *     <li> Read single cell.</li>
 *     <li> Read line of cells.</li>
 *   </ul>
 *   <li> Supports config file handling:</li><ul>
 *     <li> Format: name=data,data,data (etc.)</li>
 *     <li> Read name field.</li>
 *     <li> Read data field.</li>
 *     <li> Changeable delimiters for name and data fields.</li>
 *   </ul>
 *   <li> Supports stream marking.</li>
 *   <li> Supports easy pre and post line processing for extended classes.</li>
 *   <li> Can be used as a wrapper for readers.</li>
 * </ul>
 * 
 * @todo
 * - Check constructors.
 * - Add implementation of the {@link #read()} function.
 * - Add implementation for a full read of a config file to a map.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class BufferedReaderPlus
        extends BufferedReader
        implements Closeable {
    
    /* -------------------------------------------------------------------------
     * Constants.
     * -------------------------------------------------------------------------
     */
    // Types of supported comments.
    /** No comments. */
    public static final int NO_COMMENT = -1;
    /** Comment lines with {@code #}. */
    public static final int HASHTAG_COMMENT = 0;
    /** Comment lines with {@code //}. */
    public static final int JAVA_SINGLE_LINE_COMMENT = 1;
    /** Comment lines with {@code //} and multiline with {@code /*  * /}. */
    public static final int JAVA_COMMENT = 2;
    /** Comment lines with a {@code /:\}. */
    public static final int DOCUMENT_WITH_LINKS_COMMENT = 3;
    /** Comment multiline with a {@code <!--  -->}. */
    public static final int HTML_COMMENT = 4;
    
    // Types of supported readers.
    /** Support all available file types. */
    public static final int TYPE_ALL = -1;
    /** Support CSV file types. */
    public static final int TYPE_NONE = 0;
    /** Support CSV file types. */
    public static final int TYPE_CSV = 1;
    /** Support CONFIG file types. */
    public static final int TYPE_CONFIG = 2;
    
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** Current lineCounter in the file. */
    private int lineCounter = 0;
    
    /** {@code true} iff in a multipleLineComment block. */
    private boolean multipleLineCommentActive = false;
    
    // Temporary storage for the mark option.
    /** Variable for marking the stream. */
    private int markedLineCounter = 0;
    /** Variable for determining whether the multiline comment was active before the mark. */
    private boolean markedMultipleLineCommentActive = false;
    /** Variable for storing the buffer at the marked position. */
    private String markedBufferedLine = "";

    /** Stores the current line when reading cells (CSV fileTypes only). */
    private String bufferedLine = "";

    // Stores the current comment String.
    /** The string used for making a single comment line. */
    private String singleCommentString;
    /** The string used for starting a multi comment line. */
    private String multipleCommentStartString;
    /** The string used for ending a multi comment line. */
    private String multipleCommentEndString;

    // Field and data storage of the conf file type.
    /** The name of entry. */
    private String confFieldName = null;
    /** The values of the entry. */
    private String[] confData = null;
    /** The separator used to separate the entry name from the entry data. */
    private String confNameSeparator = "=";
    /** The separator used to separate the data entries. */
    private String confDataSeparator = ",";

    /** The type of reader. */
    private int type;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    // Constructors for a file.
    /**
     * Creates a new Reader for the file.
     * 
     * @param file The file to read.
     * 
     * @throws FileNotFoundException If the file could not be accessed.
     */
    public BufferedReaderPlus(String file)
            throws FileNotFoundException {
        this(new FileReader(file));
    }
    
    // - Constructors with a comment type.
    /**
     * Creates a new Reader for the file with the given comment type.
     * 
     * @param file The file to read.
     * @param commentType The supported type of comment used. Must be one of:
     *     <ul>
     *       <li> {@link #NO_COMMENT}</li>
     *       <li> {@link #HASHTAG_COMMENT}</li>
     *       <li> {@link #JAVA_SINGLE_LINE_COMMENT}</li>
     *       <li> {@link #JAVA_COMMENT}</li>
     *       <li> {@link #DOCUMENT_WITH_LINKS_COMMENT}</li>
     *       <li> {@link #HTML_COMMENT}</li>
     *     </ul>
     * 
     * @throws FileNotFoundException Iff the file could not be accessed.
     */
    public BufferedReaderPlus(String file, int commentType)
            throws FileNotFoundException {
        this(new FileReader(file), commentType);
    }
    
    /**
     * Creates a new Reader for the file of the given file type
     * with the given comment type.
     * 
     * @param file The file to read.
     * @param commentType The supported type of comment used. Must be one of:
     *     <ul>
     *       <li> {@link #NO_COMMENT}</li>
     *       <li> {@link #HASHTAG_COMMENT}</li>
     *       <li> {@link #JAVA_SINGLE_LINE_COMMENT}</li>
     *       <li> {@link #JAVA_COMMENT}</li>
     *       <li> {@link #DOCUMENT_WITH_LINKS_COMMENT}</li>
     *       <li> {@link #HTML_COMMENT}</li>
     *     </ul>
     * @param type The type of file that will be read. Must be one of:
     *     <ul>
     *       <li> {@link #TYPE_ALL}</li>
     *       <li> {@link #TYPE_NONE}</li>
     *       <li> {@link #TYPE_CSV}</li>
     *       <li> {@link #TYPE_CONFIG}</li>
     *     </ul>
     * 
     * @throws FileNotFoundException Iff the file could not be accessed.
     */
    public BufferedReaderPlus(String file, int commentType, int type)
            throws FileNotFoundException {
        this(new FileReader(file), commentType, type);
    }
    
    // - Constructors with single line comments.
    /**
     * Creates a new Reader for the file which has single line comments.
     * 
     * @param file The file to read.
     * @param singleComment The String used to indicate a single line comment.
     *     Default is {@code null}.
     */
    public BufferedReaderPlus(String file, String singleComment)
            throws FileNotFoundException {
        this(new FileReader(file), singleComment);
    }
    
    /**
     * Creates a new Reader for the file of the given file type
     * which has single line comments.
     * 
     * @param file The file to read.
     * @param singleComment The String used to indicate a single line comment.
     *     Default is {@code null}.
     * @param type The type of file that will be read. Must be one of:
     *     <ul>
     *       <li> {@link #TYPE_ALL}</li>
     *       <li> {@link #TYPE_NONE}</li>
     *       <li> {@link #TYPE_CSV}</li>
     *       <li> {@link #TYPE_CONFIG}</li>
     *     </ul>
     */
    public BufferedReaderPlus(String file, String singleComment, int type)
            throws FileNotFoundException {
        this(new FileReader(file), singleComment, type);
    }
    
    // - Constructors with multiple line comments.
    /**
     * Creates a new Reader for the file which has multiple line comments.
     * 
     * @param file The file to read.
     * @param multipleCommentStart The String used to indicate the start of
     *     a multiple line comment. Default is {@code null}.
     * @param multipleCommentEnd The String used to indicate the end of
     *     a multiple line comment. Default is {@code null}.
     */
    public BufferedReaderPlus(String file, String multipleCommentStart,
                              String multipleCommentEnd)
            throws FileNotFoundException {
        this(new FileReader(file), multipleCommentStart, multipleCommentEnd);
    }
    
    /**
     * Creates a new Reader for the file of the given file type
     * which has multiple line comments.
     * 
     * @param file The file to read.
     * @param multipleCommentStart The String used to indicate the start of
     *     a multiple line comment. Default is {@code null}.
     * @param multipleCommentEnd The String used to indicate the end of
     *     a multiple line comment. Default is {@code null}.
     * @param type The type of file that will be read. Must be one of:
     *     <ul>
     *       <li> {@link #TYPE_ALL}</li>
     *       <li> {@link #TYPE_NONE}</li>
     *       <li> {@link #TYPE_CSV}</li>
     *       <li> {@link #TYPE_CONFIG}</li>
     *     </ul>
     */
    public BufferedReaderPlus(String file, String multipleCommentStart,
                              String multipleCommentEnd, int type)
            throws FileNotFoundException {
        this(new FileReader(file), multipleCommentStart, multipleCommentEnd,
                type);
    }
    
    // - Constructors with both single and multiple line comments.
    /**
     * Creates a new Reader for the file of the given file type
     * which has single and multiple line comments.
     * 
     * @param file The file to read.
     * @param singleComment The String used to indicate a single line comment.
     *     Default is {@code null}.
     * @param multipleCommentStart The String used to indicate the start of
     *     a multiple line comment. Default is {@code null}.
     * @param multipleCommentEnd The String used to indicate the end of
     *     a multiple line comment. Default is {@code null}.
     */
    public BufferedReaderPlus(String file, String singleComment,
                              String multipleCommentStart,
                              String multipleCommentEnd)
            throws FileNotFoundException {
        this(new FileReader(file), singleComment, multipleCommentStart,
                multipleCommentEnd);
    }
    
    /**
     * Creates a new Reader for the file which has single and multiple line comments.
     * 
     * @param file The used reader object.
     * @param singleComment The String used to indicate a single line comment.
     *     Default is {@code null}.
     * @param multipleCommentStart The String used to indicate the start of
     *     a multiple line comment. Default is {@code null}.
     * @param multipleCommentEnd The String used to indicate the end of
     *     a multiple line comment. Default is {@code null}.
     * @param type The type of file that will be read. Must be one of:
     *     <ul>
     *       <li> {@link #TYPE_ALL}</li>
     *       <li> {@link #TYPE_NONE}</li>
     *       <li> {@link #TYPE_CSV}</li>
     *       <li> {@link #TYPE_CONFIG}</li>
     *     </ul>
     */
    public BufferedReaderPlus(String file, String singleComment,
                              String multipleCommentStart,
                              String multipleCommentEnd, int type)
            throws FileNotFoundException {
        this(new FileReader(file), singleComment, multipleCommentStart,
                multipleCommentEnd, type);
    }
    
    // Constructors with a reader.
    /**
     * Constructor for a reader of a file.
     * 
     * @param reader The used reader object.
     */
    public BufferedReaderPlus(Reader reader) {
        this(reader, TYPE_NONE);
    }
    
    // - Constructors with a comment type.
    /**
     * Constructor for a reader of a file with the given comment type.
     * 
     * @param reader The used reader object.
     * @param commentType The supported type of comment used. Must be one of:
     *     <ul>
     *       <li> {@link #NO_COMMENT}</li>
     *       <li> {@link #HASHTAG_COMMENT}</li>
     *       <li> {@link #JAVA_SINGLE_LINE_COMMENT}</li>
     *       <li> {@link #JAVA_COMMENT}</li>
     *       <li> {@link #DOCUMENT_WITH_LINKS_COMMENT}</li>
     *       <li> {@link #HTML_COMMENT}</li>
     *     </ul>
     */
    public BufferedReaderPlus(Reader reader, int commentType) {
        this(reader, commentType, TYPE_NONE);
    }
    
    /**
     * Constructor for a of the given file type with the given comment type.
     * 
     * @param reader The used reader object.
     * @param commentType The supported type of comment used. Must be one of:
     *     <ul>
     *       <li> {@link #NO_COMMENT}</li>
     *       <li> {@link #HASHTAG_COMMENT}</li>
     *       <li> {@link #JAVA_SINGLE_LINE_COMMENT}</li>
     *       <li> {@link #JAVA_COMMENT}</li>
     *       <li> {@link #DOCUMENT_WITH_LINKS_COMMENT}</li>
     *       <li> {@link #HTML_COMMENT}</li>
     *     </ul>
     * @param type The type of file that will be read. Must be one of:
     *     <ul>
     *       <li> {@link #TYPE_ALL}</li>
     *       <li> {@link #TYPE_NONE}</li>
     *       <li> {@link #TYPE_CSV}</li>
     *       <li> {@link #TYPE_CONFIG}</li>
     *     </ul>
     */
    public BufferedReaderPlus(Reader reader, int commentType, int type) {
        super(reader);
        this.type = type;
        
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
    
    // - Constructors with single line comments.
    /**
     * Constructor for a reader of a file which has single line comments.
     * 
     * @param reader The used reader object.
     * @param singleComment The String used to indicate a single line comments.
     *     Default is {@code null}.
     */
    public BufferedReaderPlus(Reader reader, String singleComment) {
        this(reader, singleComment, null, null, TYPE_NONE);
    }
    
    /**
     * Constructor for a reader of a file of the given file type
     * which has single line comments.
     * 
     * @param reader The used reader object.
     * @param singleComment The String used to indicate a single line comment.
     *     Default is {@code null}.
     * @param type The type of file that will be read. Must be one of:
     *     <ul>
     *       <li> {@link #TYPE_ALL}</li>
     *       <li> {@link #TYPE_NONE}</li>
     *       <li> {@link #TYPE_CSV}</li>
     *       <li> {@link #TYPE_CONFIG}</li>
     *     </ul>
     */
    public BufferedReaderPlus(Reader reader, String singleComment, int type) {
        this(reader, singleComment, null, null, type);
    }
    
    // - Constructors with multiple line comments.
    /**
     * Constructor for a reader of a file which has multiple line comments.
     * 
     * @param reader The used reader object.
     * @param multipleCommentStart The String used to indicate the start of
     *     a multiple line comment. Default is {@code null}.
     * @param multipleCommentEnd The String used to indicate the end of
     *     a multiple line comment. Default is {@code null}.
     */
    public BufferedReaderPlus(Reader reader, String multipleCommentStart,
            String multipleCommentEnd) {
        this(reader, null, multipleCommentStart, multipleCommentEnd, TYPE_NONE);
    }
    
    /**
     * Constructor for a reader of a file of the given file type
     * which has custom multiple line comments.
     * 
     * @param reader The used reader object.
     * @param multipleCommentStart The String used to indicate the start of
     *     a multiple line comment. Default is {@code null}.
     * @param multipleCommentEnd The String used to indicate the end of
     *     a multiple line comment. Default is {@code null}.
     * @param type The type of file that will be read. Must be one of:
     *     <ul>
     *       <li> {@link #TYPE_ALL}</li>
     *       <li> {@link #TYPE_NONE}</li>
     *       <li> {@link #TYPE_CSV}</li>
     *       <li> {@link #TYPE_CONFIG}</li>
     *     </ul>
     */
    public BufferedReaderPlus(Reader reader, String multipleCommentStart,
            String multipleCommentEnd, int type) {
        this(reader, null, multipleCommentStart, multipleCommentEnd, type);
    }
    
    // - Constructors with both single and multiple line comments.
    /**
     * Constructor for a reader of a file which has custom single
     * and multiple line comments.
     * 
     * @param reader the used reader object.
     * @param singleComment The String used to indicate a single line comment.
     *     Default is {@code null}.
     * @param multipleCommentStart The String used to indicate the start of
     *     a multiple line comment. Default is {@code null}.
     * @param multipleCommentEnd The String used to indicate the end of
     *     a multiple line comment. Default is {@code null}.
     */
    public BufferedReaderPlus(Reader reader, String singleComment,
            String multipleCommentStart, String multipleCommentEnd) {
        this(reader, singleComment, multipleCommentStart, multipleCommentEnd, TYPE_NONE);
    }
    
    // Full constructor
    /**
     * Constructor for a reader of a file of the given file type
     * which has custom single and multiple line comments.
     * 
     * @param reader the used reader object.
     * @param singleComment the String used to indicate a single line comment.
     *     Default is {@code null}.
     * @param multipleCommentStart The String used to indicate the start of
     *     a multiple line comment. Default is {@code null}.
     * @param multipleCommentEnd The String used to indicate the end of
     *     a multiple line comment. Default is {@code null}.
     * @param type The type of file that will be read. Must be one of:
     *     <ul>
     *       <li> {@link #TYPE_ALL}</li>
     *       <li> {@link #TYPE_NONE}</li>
     *       <li> {@link #TYPE_CSV}</li>
     *       <li> {@link #TYPE_CONFIG}</li>
     *     </ul>
     */
    public BufferedReaderPlus(Reader reader, String singleComment,
            String multipleCommentStart, String multipleCommentEnd, int type) {
        super(reader);
        this.type = type;
        
        singleCommentString = singleComment;
        multipleCommentStartString = multipleCommentStart;
        multipleCommentEndString = multipleCommentEnd;
    }
    
    
    /* -------------------------------------------------------------------------
     * Read line fuctions.
     * -------------------------------------------------------------------------
     */
    /**
     * Reads a single line.
     * 
     * @returns The next line of the file.
     * @throws IOException when no read actions are allowed from the file.
     */
    @Override
    public String readLine()
            throws IOException {
        lineCounter++;
        return super.readLine();
    }
    
    /**
     * Reads a line while skipping empty lines comments.
     * 
     * @returns The next processed line of the file.
     * @throws IOException when no read actions are allowed from the file.
     */
    public String readProcessedLine()
            throws IOException {
        String line;
        
        do {
            // Read the line
            line = readLine();
            
            // Process the line
            line = postProcessLine(processLine(preProcessLine(line)));
            
            // Check for null
            if (line != null) {
                // Remove starting and trailing spaces
                line = line.trim();
                // Remove trailing semi-colons and remove staring and
                // trailing spaces if it is a csv file.
                if (supportsCSV()) {
                    line = removeCSVTrailings(line).trim();
                }
            }
            
        } while (line != null && line.equals(""));
        return line;
    }
    
    /**
     * Pre-processes the given line.
     * Is supposed to be overridden to do operations before processing the line.
     * 
     * @param line The text to be processed.
     * @return processed line.
     */
    protected String preProcessLine(String line) {
        return line;
    }
    
    /**
     * Post-processes the given line.
     * Is supposed to be overriden to do operations after processing the line.
     * 
     * @param line The text to be processed.
     * @return The Processed line.
     */
    protected String postProcessLine(String line) {
        return line;
    }
    
    /**
     * @return {@code true} if the reader supports single line comments.
     */
    public final boolean hasSingleLineComment() {
        return !(singleCommentString == null
            || singleCommentString.equals(""));
    }
    
    /**
     * @return {@code true} if the reader supports multi line comments.
     */
    public final boolean hasMultiLineComment() {
        return !(multipleCommentStartString == null
            || multipleCommentStartString.equals("")
            || multipleCommentEndString == null
            || multipleCommentEndString.equals(""));
    }
    
    /**
     * Processes the given line. Used for ignoring single and multiple line
     * comments.
     *
     * @param line text to be processed
     * @return The processed line.
     *
     * @see #processLineMulti(String)
     * @see #processLineSingleMulti(String)
     */
    private String processLine(final String line) {
        // If the input is null, nothing can be processed.
        if (line == null) {
            return null;
        }
        
        boolean hasSingleLineComment = hasSingleLineComment();
        boolean hasMultiLineComment = hasMultiLineComment();

        // If there are no processing actions, simply return the line.
        if (!hasSingleLineComment && !hasMultiLineComment) {
            return line;
        }
        
        if (hasSingleLineComment && hasMultiLineComment) {
            return processLineSingleMulti(line);
        }
        
        if (hasMultiLineComment) { // {@code hasSingleLine == false}.
            return processLineMulti(line);
        }
        
        // {@code hasSingleLine == true && hasMultiLine == false}.
        int index = line.indexOf(singleCommentString);
        return (index == -1 ? line : line.substring(0, index));
    }
    
    /**
     * Processes the given line for single line and multiple line comments.
     * Should only be invoked via {@link #processLine(String)}
     *
     * @param line The line to process.
     * @return The processed line.
     *
     * @see #processLine(String)
     */
    private String processLineSingleMulti(final String line) {
        StringBuilder sb = new StringBuilder();
        int pointer = 0;
        while (true) {
            int index;
            if (multipleLineCommentActive) {
                index = line.indexOf(multipleCommentEndString, pointer);
                if (index == -1) {
                    return sb.toString();
                }
                
            } else {
                index = line.indexOf(multipleCommentStartString, pointer);
                int singleCommentIndex = line.indexOf(singleCommentString,
                    pointer);
                if (singleCommentIndex != -1
                    && (index == -1 || singleCommentIndex < index)) {
                    sb.append(line, pointer, singleCommentIndex);
                    return sb.toString();
                }
                if (index == -1) {
                    sb.append(line.substring(pointer));
                    return sb.toString();
                }
                sb.append(line, pointer, index);
            }
            
            multipleLineCommentActive = !multipleLineCommentActive;
            pointer = index + multipleCommentEndString.length();
        }
    }
    
    /**
     * Processes the gien line for multiple line comments.
     * Should only be invoked via {@link #processLine(String)}
     *
     * @param line The line to process.
     * @return The processed line.
     *
     * @see #processLine(String)
     */
    private String processLineMulti(final String line) {
        StringBuilder sb = new StringBuilder();
        int pointer = 0;
        while (true) {
            int index;
            if (multipleLineCommentActive) {
                index = line.indexOf(multipleCommentEndString, pointer);
                if (index == -1) {
                    return sb.toString();
                }
                
            } else {
                index = line.indexOf(multipleCommentStartString, pointer);
                if (index == -1) {
                    sb.append(line.substring(pointer));
                    return sb.toString();
                }
                sb.append(line, pointer, index);
            }
            
            multipleLineCommentActive = !multipleLineCommentActive;
            pointer = index + multipleCommentEndString.length();
        }
    }
    
    
    /* -------------------------------------------------------------------------
     * CSV file functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Returns {@code true} if reader supports CSV file type.
     * {@code false} otherwise.
     *
     * @return Whether the reader supports the csv file type series.
     */
    public boolean supportsCSV() {
        return type == TYPE_ALL || type == TYPE_CSV;
    }
    
    /**
     * Removes trailing semi-colons from csv-files.
     *
     * @param line The line to remove the trainling semi-colons from.
     * @return The line without trailing semi-colons.
     */
    public static String removeCSVTrailings(final String line) {
        if (line == null) return null;
        if (line.length() == 0) return "";
        
        // Iterate backwards
        int i = line.length() - 1;
        while (i >= 0 && line.charAt(i) == ';') {
            i--;
        }
        return (i == -1 ? "" : line.substring(0, i + 1));
    }
    
    /**
     * Reads a processed cell from a csv file.
     * <br>
     * Note:<br>
     * If no line was buffered, pick the next one iff {@code lineBlock == false}.
     * Otherwise return {@code null}.
     *
     * @return The data of a single cell from the file. Returns {@code null} iff
     *     EOF reached or if {@code lineBlock == true} and EOL reached.
     *
     * @throws IOException Iff the data could not be retrieved from the file.
     * 
     * @see #readCSVCell(boolean)
     * @see #readCSVCell(boolean, boolean)
     */
    public String readCSVCell()
        throws IOException {
        return readCSVCell(true);
    }
    
    /**
     * Reads a processed cell from a csv file.
     * <br>
     * Note:<br>
     * If no line was buffered, pick the next one iff {@code lineBlock == false}.
     * Otherwise return {@code null}.
     *
     * @param processed Whether to use processed lines as input or not.
     *     Default is {@code true}.
     * @return The data of a single cell from the file. Returns {@code null} iff
     *     EOF reached or if {@code lineBlock == true} and EOL reached.
     *
     * @throws IOException Iff the data could not be retrieved from the file.
     *                               
     * @see BufferedReaderPlus#readCSVCell(boolean, boolean)
     */
    public String readCSVCell(final boolean processed)
        throws IOException {
        return readCSVCell(processed, false);
    }
    
    /**
     * Reads a processed cell from a csv file.
     * <br>
     * Note:<br>
     * If no line was buffered, pick the next one iff {@code lineBlock == false}.
     * Otherwise return {@code null}.
     *
     * @param processed Whether to use processed lines as input or not. Default is {@code true}.
     * @param lineBlock Whether to return null if the end of a line has been
     *     reached ({@code true}), or to take the next line ({@code false}).
     * @return The data of a single cell from the file. Returns {@code null} iff
     *     EOF reached or if {@code lineBlock == true} and EOL reached.
     *
     * @throws IOException Iff the data could not be retrieved from the file.
     */
    public String readCSVCell(final boolean processed, final boolean lineBlock)
        throws IOException {
        if (!supportsCSV()) {
            throw new IllegalStateException(
                "Reader is not configured to read \".csv\" file types.");
        }
        
        // Update the buffer if necessary
        if (bufferedLine == null || bufferedLine.length() == 0) {
            // If the {@code bufferLength == 0}, read new line, if allowed.
            // Otherwise return null.
            if (lineBlock) {
                return null;
            }
            
            // Reads the next line
            bufferedLine = (processed ? readProcessedLine() : readLine());
            
            // Check for EOF
            if (bufferedLine == null) {
                return null;
            }
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
    
    /**
     * Reads either all remaining processed cells on the buffered line or all
     * cells on a new line from a csv file.
     * <br>
     * Note:<br>
     * If no line was buffered, pick the next one iff {@code lineBlock == false}.
     * Otherwise return {@code null}.
     *
     * @param processed Whether to use processed lines as input or not.
     * @return The data of each cell in an array in increasing order. Returns {@code null}
     *     iff EOF reached or if {@code lineBlock == true} and EOL reached.
     *
     * @throws IOException iff the data could not be retrieved from the file.
     *                               <p>
     * @see BufferedReaderPlus#readCSVLine(boolean, boolean)
     */
    public List<String> readCSVLine(final boolean processed)
        throws  IOException {
        return readCSVLine(processed, false);
    }
    
    /**
     * Reads either all remaining processed cells on the buffered line or all
     * cells on a new line from a csv file.
     * <br>
     * Note:<br>
     * If no line was buffered, pick the next one iff {@code lineBlock == false}.
     * Otherwise return {@code null}.
     *
     * @param processed Whether to use processed lines as input or not.
     * @param lineBlock Whether to return null if the end of the line has been
     *     reached (true), or to take the next line (false).
     * @return The data of each cell in an array in increasing order. Returns {@code null}
     *     iff EOF reached or if {@code lineBlock == true} and EOL reached.
     *
     * @throws IOException Iff the data could not be retrieved from the file.
     */
    public List<String> readCSVLine(final boolean processed,
                                    final boolean lineBlock)
        throws IOException {
        if (!supportsCSV()) {
            throw new IllegalStateException(
                "Reader is not configured to read \".csv\" file types.");
        }
        
        // Read and clear the buffer
        String line = bufferedLine;
        bufferedLine = "";
        
        // Checks the buffer length.
        if (line.length() == 0) {
            // If the {@code bufferLength == 0}, read new line iff allowed.
            // Otherwise return null.
            if (lineBlock) {
                return null;
            }
            
            // Reads the next line
            line = (processed ? readProcessedLine() : readLine());

            // Check for the null-value
            if (line == null) {
                return null;
            }
        }
        
        List<String> cells = new ArrayList<>();
        int prevColon = 0;
        
        // Puts all Strings separated by a semi-colon in the ArrayList.
        // If no semi-colon is present, return the full line as
        // one element in the ArrayList
        for (int i = 0; i <= line.length(); i++) {
            if (i != line.length() && line.charAt(i) != ';') {
                continue;
            }
            if (processed) {
                cells.add(line.substring(prevColon, i).trim());

            } else {
                cells.add(line.substring(prevColon, i));
            }
            
            prevColon = i + 1;
        }
        
        return cells;
    }
    
    /* -------------------------------------------------------------------------
     * CONFIG file functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Returns {@code true} if the reader supports configuration files, {@code
     * false} otherwise.
     *
     * @return Whether the reader supports the conf file type series.
     */
    public boolean supportsConf() {
        return type == TYPE_ALL || type == TYPE_CONFIG;
    }
    
    /**
     * Setter for name separator.
     *
     * @param sep The new conf file type name separator.
     */
    public void setConfNameSeparator(final String sep) {
        confNameSeparator = sep;
    }
    
    /**
     * Setter for data separator.
     *
     * @param sep The new conf file type data separator.
     */
    public void setConfDataSeparator(final String sep) {
        confDataSeparator = sep;
    }
    
    /**
     * Reads the next field.
     *
     * @return {@code true} if EOF was not yet reached.
     *     {@code false} if EOF was reached.
     *
     * @throws IOException iff the data could not be retrieved from the file.
     * @see BufferedReaderPlus#readNextConfLine(boolean)
     */
    public boolean readNextConfLine()
        throws IOException {
        return readNextConfLine(true);
    }
    
    /**
     * Reads the next field. The field name can be retrieved via {@link
     * #getField()} and the data can be retrieved via {@link #getData()}.
     *
     * @param processed Whether to use processed lines as input or not.
     *     Default is {@code true}.
     * @return {@code true} If EOF was not yet reached.
     *
     * @throws IOException Iff the data could not be retrieved from
     *                               the file.
     */
    public boolean readNextConfLine(final boolean processed)
        throws IOException {
        if (!supportsConf()) {
            throw new IllegalStateException(
                "Reader is not configured to read \".conf\" file types.");
        }
        
        String line = (processed ? readProcessedLine() : readLine());
        if (line == null) {
            confFieldName = null;
            confData = null;
            return false;
        }
        
        String[] split = line.split(confNameSeparator);
        
        if (split.length == 0) {
            // Empty line.
            confFieldName = null;
            confData = null;
            
        } else if (split.length == 1) {
            // Field name only.
            confFieldName = split[0];
            confData = new String[0];
            
        } else if (split.length == 2) {
            // Field with data.
            confFieldName = split[0];
            confData = split[1].split(confDataSeparator);
            
        } else { // split.length > 2
            // Field with data containing the field name separator.
            confFieldName = split[0];
            confData = line.substring(confFieldName.length() + 1)
                .split(confDataSeparator);
        }
        
        return true;
    }
    
    /**
     * Getter for a data field.
     *
     * @return The name of the field, or {@code null} if no previous field
     *     was available (not yet started or EOF).
     */
    public String getField() {
        return confFieldName;
    }
    
    /**
     * Helper function for testing if fields are equal. <br>
     * More specifically, {@code true} is returned if, and only if,
     * {@code name == null ? field == null : name.equals(field)}.
     *
     * @param name The name of the field to check for.
     * @return {@code true} iff the given name equals the current field.
     *     {@code false} otherwise.
     */
    public boolean fieldEquals(final String name) {
        return (Objects.equals(name, confFieldName));
    }
    
    /**
     * Getter for confData.
     *
     * @return Array containing the values of the field. {@code null} if no
     *     values available, and an empty array if no previous data available
     *     (not yet started or EOF).
     */
    public String[] getData() {
        return confData;
    }
    
    /**
     * Getter for ith item of the data.
     *
     * @param i The index of the element to be returned.
     * @return The element of the values of the field at the provided index.
     *     {@code null} if no values available or if the index is not within
     *     the range of the values array.
     */
    public String getData(final int i) {
        if (confData == null) {
            return null;
        } else if (i < 0 || i > confData.length) {
            return null;
        } else {
            return confData[i];
        }
    }
    
    /* -------------------------------------------------------------------------
     * Other functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Marks the present location in the steam. After reading
     * <it>readAheadLimit</it> characters, attempting to reset the stream may
     * fail.
     *
     * @param readAheadLimit number of characters that will be at most read
     *     before {@link #reset()} is called.
     * 
     * @throws IOException if the current reader is not initialized for stream marking.
     * 
     * @see #reset()
     */
    @Override
    public void mark(final int readAheadLimit)
        throws IOException {
        markedLineCounter = lineCounter;
        markedMultipleLineCommentActive = multipleLineCommentActive;
        markedBufferedLine = bufferedLine;

        super.mark(readAheadLimit);
    }
    
    /**
     * Resets to the last marked point in the stream.
     *
     * @throws IOException if the current reader is not initialized for stream marking.
     * 
     * @see #mark(int)
     */
    @Override
    public void reset()
        throws IOException {
        lineCounter = markedLineCounter;
        multipleLineCommentActive = markedMultipleLineCommentActive;
        bufferedLine = markedBufferedLine;

        super.reset();
    }
    
    /**
     * Getter for linecounter.
     *
     * @return The current linecounter.
     */
    public int getLineCounter() {
        return lineCounter;
    }
    
    
    /* -------------------------------------------------------------------------
     * Not supported functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Not supported function.
     *
     * @throws UnsupportedOperationException Always when called.
     * 
     * @deprecated Function cannot and shouldn't be implemented.
     */
    @Override
    @Deprecated(forRemoval = false)
    public int read() {
        throw new UnsupportedOperationException("Operation was not supported");
    }
    
    /**
     * Not supported function.
     *
     * @throws UnsupportedOperationException Always when called
     * 
     * @deprecated Function cannot and shouldn't be implemented.
     */
    @Override
    @Deprecated(forRemoval = false)
    public int read(final char[] cbuf, final int off, final int len) {
        throw new UnsupportedOperationException("Operation was not supported");
    }
    
}
