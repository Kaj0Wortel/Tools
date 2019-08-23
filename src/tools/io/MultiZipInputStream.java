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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


// Tools imports
import tools.MultiTool;


/**
 * Input stream for multiple zip files.
 * It uses the {@link ZipInputStream} class to obtain data from a single zip file.
 * Additionally, multiple zip files can be read as if they are one big file.<br>
 * <br>
 * Accepts files in the same format as produced by {@link MultiZipOutputStream}.
 * 
 * @todo
 * - Check all cases!
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class MultiZipInputStream
        extends InputStream
        implements ExceptionInputStream {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The prefix of a series of zip files. */
    private final String filePrefix;
    /** Whether the data is spread over more than one file. */
    private final boolean parted;
    
    // Counters and state variables.
    /** Counter keeping track of which zip file currently is being read. */
    private int fileCounter = 0;
    /** The underlying zip input stream for the current zip file. */
    private ZipInputStream zis;
    /** The zip entry of the current zip file. */
    private ZipEntry curEntry = null;
    /** Whether the current entry is finished. */
    private boolean entryFinished = false;
    /** Denotes whether the file has been read if the data is parted. */
    private boolean readSingleFile = false;
    /** Denotes whether the reader is closed. */
    private boolean closed = false;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new zip reader with the path. <br>
     * If {@code parted == true}, then the reader will read multiple files with the same
     * prefix, but a different part number. <br>
     * <br>
     * Example 1: <br>
     * Input files:
     * <ul>
     *   <li> /some/path/data.zip </li>
     * </ul>
     * Then read all the contents of the zip file via:<br>
     * {@code new ZipReader("/some/path/data.zip", false)} <br>
     * <br>
     * Example 2: <br>
     * Input files:
     * <ul>
     *   <li> /some/path/data.zip.part000 </li>
     *   <li> /some/path/data.zip.part001 </li>
     *   <li> /some/path/data.zip.part002 </li>
     *   <li> /some/path/data.zip.part003 </li>
     * </ul>
     * Then read all the contents of the zip files via:<br>
     * {@code new ZipReader("/some/path/data.zip", true)} <br>
     * 
     * @param path The 
     * @param parted
     * 
     * @throws IOException If the first file does not exist, or if some IO error occured.
     */
    public MultiZipInputStream(String path, boolean parted)
            throws IOException {
        this.filePrefix = path;
        this.parted = parted;
        generateNextStream();
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Generates the next entry of the zip stream.
     * If allowed and needed, opens the next zip file if it exists.
     * If the name of the previous entry is equal to the name of the new entry,
     * then the data of the new entry is considered a continuation of the previous entry.
     * 
     * @return {@code true} if the new entry is the same entry as previous one.
     * 
     * @throws IOException If some IO error occured.
     */
    private boolean generateNextEntry()
            throws IOException {
        ZipEntry entry;
        while (zis == null || (entry = zis.getNextEntry()) == null) {
            generateNextStream();
            if (zis == null) {
                curEntry = null;
                entryFinished = true;
                return false;
            }
        }
        
        boolean sameEntry = (curEntry != null
                && Objects.equals(curEntry.getName(), entry.getName()));
        curEntry = entry;
        entryFinished = !sameEntry;
        return sameEntry;
    }
    
    /**
     * Closes the previous stream if any, and opens the stream for the
     * next zip file if multiple zip files should be read and if the 
     * next file exists.
     * 
     * @throws IOException If some IO error occured.
     */
    private void generateNextStream()
            throws IOException {
        if (zis != null) {
            zis.close();
            zis = null;
        }
        if (readSingleFile) return;
        
        String fileName = (!parted
                ? filePrefix
                : filePrefix + ".part" + MultiTool.fillZero(fileCounter++, 3));
        File file = new File(fileName);
        
        zis = (file.exists()
                ? new ZipInputStream(new FileInputStream(file))
                : null);
        
        readSingleFile = !parted;
    }
    
    /**
     * Gets the next entry. <br>
     * The returned entry is the first entry of a possible block
     * of entries with the same name and spread over separate files.
     * 
     * @return The next entry.
     * 
     * @throws IOException If some IO error occured.
     */
    public ZipEntry nextEntry()
            throws IOException {
        if (closed) throw new IOException("The stream was already closed.");
        if (curEntry == null || !entryFinished) {
            while (generateNextEntry()) { }
        }
        return curEntry;
    }
    
    @Override
    public int read()
            throws IOException {
        byte[] data = new byte[1];
        if (read(data, 0, 1) != 1) return -1;
        else return data[0] & 0xFF;
    }
    
    @Override
    public int read(byte[] b)
            throws IOException {
        return read(b, 0, b.length);
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len)
            throws IOException {
        if (closed) throw new IOException("The stream was already closed.");
        if (entryFinished) return -1;
        if (zis == null) {
            generateNextEntry();
            if (zis == null) return -1;
        }
        
        int read;
        int lenRemaining = len;
        int newOff = off;
        do {
            read = zis.read(b, newOff, lenRemaining);
            if (read == -1) break;
            lenRemaining -= Math.max(0, read);
            newOff += Math.max(0, read);
            
        } while (lenRemaining > 0 && generateNextEntry() && zis != null);
        
        return len - lenRemaining;
    }
    
    @Override
    public void close()
            throws IOException {
        if (!closed) {
            if (zis != null) zis.close();
        }
    }
    
    
}
