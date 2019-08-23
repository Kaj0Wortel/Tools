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


// Java imports
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.attribute.FileTime;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


// Tools imports
import tools.MultiTool;


/**
 * Output stream which writes a stream of data to one or more zip files.
 * This class uses the {@link ZipOutputStream} class for writing to a single
 * zip file. <br>
 * A limit can be set on how big a zip file can be. If the amount of data exceeds
 * the limit, then a new zip file will be created and a new entry with the same
 * name will be created. The rest of the data will be created.
 * <br>
 * If the stream should not be parted, then {@code maxSize} is ignored. <br>
 * <br>
 * <h2>Naming scheme</h2>
 * If the stream should not be parted, then the zip file is exactly named as the given path. <br>
 * <b>Example</b>: <br>
 * Given path: "/some/path/data.zip" <br>
 * Generated file: "/some/path/data.zip" <br>
 * <br>
 * If the stream should be parted, then ".partxxx" -- where 'x' is a single digit
 * number -- is appended. The number starts at {@code 0} and is
 * increased linearly after each file. If the number doesn't fit in the three reserved
 * characters, then more characters are added. <br>
 * <b>Example</b>: <br>
 * Given path: "/some/path/data.zip" <br>
 * Number of files generated: 1001 <br>
 * Generated files:
 * <ul>
 *   <li> "/some/path/data.zip.part000" </li>
 *   <li> "/some/path/data.zip.part001" </li>
 *   <li> "/some/path/data.zip.part002" </li>
 *   <li> ... </li>
 *   <li> "/some/path/data.zip.part999" </li>
 *   <li> "/some/path/data.zip.part1000" </li>
 * </ul>
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class MultiZipOutputStream
        extends OutputStream {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The path of the zip file to write to. */
    private final String targetPath;
    /** Denotes whether the data should be split over multiple zip files. */
    private final boolean parted;
    /** Denotes the maximum file size if the stream should be parted. */
    private final long maxSize;
    
    /** The current zip output stream. */
    private ZipOutputStream zos;
    /** The current entry of the zip output stream. */
    private ZipEntry entry;
    
    // Counters
    /** Counter for keeping track of the next zip file number. */
    private int fileCounter = 0;
    /** Denotes the file size of the current zip file. */
    private long fileSize;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new output stream for multiple zip files.
     * 
     * @param targetPath The path of the zip file.
     * @param parted Whether the stream should be parted over multiple zip files.
     * @param maxSize The maximum size of a zip file. Is only used when
     *     {@code parted == true}.
     */
    public MultiZipOutputStream(String targetPath, boolean parted, long maxSize) {
        this.targetPath = targetPath;
        this.parted = parted;
        this.maxSize = maxSize;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Sets the zip entry of the Stream.
     * Automatically closes the previous entry. <br>
     * This function must be called before writing any data.
     * 
     * @param entry The new entry.
     * 
     * @throws IOException If some IO error occured.
     */
    public void setEntry(ZipEntry entry)
            throws IOException {
        this.entry = entry;
        if (zos == null) {
            nextStream();
            
        } else {
            zos.putNextEntry(entry);
        }
    }
    
    /**
     * @return The current zip entry.
     */
    public ZipEntry getEntry() {
        return entry;
    }
    
    /**
     * Opens the stream for the next source file.
     * 
     * @throws IOException If some IO error occured.
     */
    private void nextStream()
            throws IOException {
        if (zos != null) zos.close();
        String fileName = (!parted
                ? targetPath
                : targetPath + ".part" + MultiTool.fillZero(fileCounter++, 4));
        File file = new File(fileName);
        file.getParentFile().mkdirs();
        file.createNewFile();
        zos = new ZipOutputStream(new FileOutputStream(file));
        zos.putNextEntry(cloneEntry(entry));
    }
    
    /**
     * Creates a clone of the given zip entry.
     * 
     * @param source The zip entry to be cloned.
     * 
     * @return A clone of the given zip entry.
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
    public void write(final int i)
            throws IOException {
        write(new byte[] {(byte) i});
    }
    
    @Override
    public void write(final byte[] data)
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
