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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.attribute.FileTime;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * Wrapper write class for the {@link ZipOutputStream}.
 * Added functionality:<br>
 * - Supports file splitting.
 * 
 * @author Kaj Wortel (0991586)
 */
public class ZipWriter
        extends OutputStream {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    final private String filePrefix;
    private ZipOutputStream zos;
    final private boolean parted;
    final private long maxSize;
    private ZipEntry entry;
    
    // Counters
    private int fileCounter = 0;
    private long fileSize;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Constructor.
     */
    public ZipWriter(String filePrefix, boolean parted, long maxSize) {
        this.filePrefix = filePrefix;
        this.parted = parted;
        this.maxSize = maxSize;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    public void setEntry(ZipEntry entry)
            throws FileNotFoundException, IOException {
        this.entry = entry;
        if (zos == null) {
            nextStream();
            
        } else {
            zos.putNextEntry(entry);
        }
    }
    
    /**
     * @return the current {@link ZipEntry}.
     */
    public ZipEntry nextEntry() {
        return entry;
    }
    
    /**
     * Opens the stream for the next source file.
     * 
     * @throws FileNotFoundException if some directories are missing.
     * @throws IOException if the file could not be written to.
     */
    private void nextStream()
            throws FileNotFoundException, IOException {
        if (zos != null) zos.close();
        String fileName = (!parted
                ? filePrefix + ".zip"
                : filePrefix +
                ".part" + MultiTool.fillZero(fileCounter++, 4) +
                ".zip");
        zos = new ZipOutputStream(new FileOutputStream(new File(fileName)));
        zos.putNextEntry(cloneEntry(entry));
    }
    
    /**
     * TODO
     * 
     * @param source
     * @return 
     */
    private ZipEntry cloneEntry(ZipEntry source) {
        ZipEntry clone = new ZipEntry(source.getName());
        clone.setComment(source.getComment());
        clone.setExtra(source.getExtra());
        {
            int method = source.getMethod();
            if (method != -1) {
                clone.setMethod(method);
            }
        }
        
        clone.setTime(source.getTime());
        {
            FileTime time;
            if ((time = source.getCreationTime()) != null) {
                clone.setCreationTime(time);
            }
            if ((time = source.getLastAccessTime()) != null) {
                clone.setLastAccessTime(time);
            }
            if ((time = source.getLastModifiedTime()) != null) {
                clone.setLastModifiedTime(time);
            }
        }
        return clone;
    }

    @Override
    public void write(int i)
            throws IOException {
        write(new byte[] {(byte) i});
    }

    @Override
    public void write(byte[] data)
            throws IOException {
        write(data, 0, data.length);
    }

    @Override
    public void write(final byte[] data, final int off, final int len)
            throws IOException {
        if (zos == null) nextStream();
        
        if (!parted) {
            zos.write(data, off, len);
            
        } else {
            int lenRemaining = len;
            int newOff = off;
            while (lenRemaining != 0) {
                long remaining = maxSize - fileSize;
                if (remaining <= lenRemaining) {
                    zos.write(data, newOff, (int) remaining);
                    newOff += remaining;
                    lenRemaining -= remaining;
                    fileSize = 0;
                    nextStream();
                    
                } else {
                    zos.write(data, newOff, lenRemaining);
                    fileSize += lenRemaining;
                    lenRemaining = 0;
                }
            }
        }
    }
    
    @Override
    public void flush()
            throws IOException {
        if (zos !=  null) zos.flush();
    }

    @Override
    public void close()
            throws IOException {
        if (zos != null) zos.close();
        zos = null;
    }
    
    
}
