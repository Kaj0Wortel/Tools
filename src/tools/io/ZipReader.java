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


// Own imports
import tools.MultiTool;


// Java imports
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import tools.log.Logger;


/**
 * 
 * 
 * @author Kaj Wortel (0991586)
 */
public class ZipReader
        extends InputStream {
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    final private String filePrefix;
    private ZipInputStream zis;
    final private boolean parted;
    
    // Counters
    private int fileCounter = 0;
    private ZipEntry curEntry = null;
    private boolean entryFinished = false;
    private boolean closed = false;
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Constructor.
     */
    public ZipReader(String filePrefix, boolean parted)
            throws FileNotFoundException, IOException {
        this.filePrefix = filePrefix;
        this.parted = parted;
        generateNextStream();
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * 
     * @return {@code true} if the new entry is the same entry as
     *     previous one.
     * 
     * @throws FileNotFoundException if some directories are missing.
     * @throws IOException if the file could not be written to.
     */
    private boolean generateNextEntry()
            throws FileNotFoundException, IOException {
        if (zis == null) generateNextStream();
        if (zis == null) return false;
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) == null) {
            generateNextStream();
            if (zis == null) {
                curEntry = null;
                entryFinished = true;
                return true;
            }
        }
        
        boolean sameEntry = (curEntry != null
                && Objects.equals(curEntry.getName(), entry.getName()));
        curEntry = entry;
        entryFinished = !sameEntry;
        return sameEntry;
    }
    
    /**
     * Opens the stream for the next source file.
     * 
     * @throws IOException if the file could not be written to.
     */
    private void generateNextStream()
            throws IOException {
        if (zis != null) zis.close();
        String fileName = (!parted
                ? filePrefix + ".zip"
                : filePrefix +
                ".part" + MultiTool.fillZero(fileCounter++, 4) +
                ".zip");
        File file = new File(fileName);
        
        try {
            zis = (file.exists()
                    ? new ZipInputStream(new FileInputStream(file))
                    : null);
            
        } catch (FileNotFoundException e) {
            Logger.write(new Object[] {
                this.getClass().getSimpleName() + ": File does not exist:",
                e
            }, Logger.Type.ERROR);
        }
    }
    
    /**
     * 
     * @return
     * @throws IOException 
     */
    public ZipEntry nextEntry()
            throws IOException {
        if (closed) throw new IOException("The stream was already closed.");
        if (curEntry == null || !entryFinished) generateNextEntry();
        entryFinished = (curEntry == null);
        return curEntry;
    }
    
    @Override
    public int read()
            throws IOException {
        byte[] data = new byte[1];
        if (read(data, 0, 1) == -1) return -1;
        else return data[0];
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
