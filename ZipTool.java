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


// Java imports
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.attribute.FileTime;

import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class ZipTool {
    final static int BLOCK_SIZE = 1024;
    
    static public ArrayList<File[]> listFilesAndPathsFromRootDir(File rootDir) {
        return listFilesAndPathsFromRootDir(rootDir, "", true);
    }
    
    /* 
     * Lists all files in a dir. Outputs an ArrayList containing an array of File objects of length 2.
     * The first element contains the path of the file.
     * The second element contains the path of the location in the file relative to the given rootDir.
     * 
     * Furthermore is guarenteed that:
     * - For every directory that all its children (sub-dirs included) are listed
     *     directly below its own entry.
     * - Also, when one file occures after any directory, which is not a child of that directory,
     *     then all children (and all sub-children) of that directory are already listed.
     * - No other assumptions regarding file-order can be made on the output.
     */
    static public ArrayList<File[]> listFilesAndPathsFromRootDir(File rootDir, boolean listDirs)
        throws IllegalArgumentException, IllegalStateException {
        return listFilesAndPathsFromRootDir(rootDir, "", listDirs);
    }
    
    static public ArrayList<File[]> listFilesAndPathsFromRootDir(File rootDir, String pathSoFar, boolean listDirs)
        throws IllegalArgumentException, IllegalStateException {
        
        if (rootDir.isFile()) {
            new IllegalArgumentException("The file \"" + rootDir.getPath() + "\" is no dir.");
        }
        
        pathSoFar = (pathSoFar == null ? "" : pathSoFar);
        
        ArrayList<File[]> output = new ArrayList<File[]>();
        String root = rootDir.getPath();
        File[] listOfFiles = rootDir.listFiles();
        
        if (listDirs && !pathSoFar.equals("")) {
            output.add(new File[] {rootDir, new File(pathSoFar)});
        }
        
        for (int i = 0; i < listOfFiles.length; i++) {
            if (!root.equals(listOfFiles[i].getPath().substring(0, root.length()))) {
                throw new IllegalStateException("File \"" + listOfFiles[i] + "\" could not be found in dir \"" + root + "\"");
            }
            
            if (listOfFiles[i].isFile()) {
                output.add(new File[] {listOfFiles[i],
                    new File(pathSoFar + listOfFiles[i].getParent().substring(root.length()))});
                
            } else {
                output.addAll
                    (listFilesAndPathsFromRootDir
                         (listOfFiles[i], pathSoFar + listOfFiles[i].getPath().substring(root.length()), listDirs));
            }
        }
        
        
        return output;
    }
    
    /* 
     * Creates a zip file. The input ArrayList contains an array of File objects of length 2.
     * The first element contains the path of the file. The second element contains the path
     * of the location in the zip.
     */
    static public void createZip(ArrayList<File[]> filesAndDirs, File zipFile, File... ignoreFiles)
        throws IllegalArgumentException, IOException, ZipException {
        File[] inFiles = new File[filesAndDirs.size()];
        File[] pathInZip = new File[filesAndDirs.size()];
        
        for (int i = 0; i < filesAndDirs.size(); i++) {
            inFiles[i] = filesAndDirs.get(i)[0];
            pathInZip[i] = filesAndDirs.get(i)[1];
        }
        
        createZip(inFiles, pathInZip, zipFile);
    }
    
    /* 
     * Packs one file in a zip file.
     */
    static public void createZip(File inFile, File zipFile,File... ignoreFiles)
        throws IllegalArgumentException, IOException, ZipException {
        createZip(new File[] {inFile}, null, zipFile, ignoreFiles);
    }
    
    /* 
     * Packs an array of files in a zip file. The pathInZip array denotes where each file should
     * be stored in the zip. If pathInZip equals null then all files will be stored at the root
     * of the zip file
     */
    static public void createZip(File[] inFiles, File[] pathInZip, File zipFile, File... ignoreFiles)
        throws IllegalArgumentException, IOException, ZipException {
        
        if (pathInZip != null // 'inFiles != null' is skipped since this should thow a NullPointerException if this is missing
                && inFiles.length != pathInZip.length) {
            throw new IllegalArgumentException("Unequal length of the input files and the destination in the zip file.");
        }
        
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            
            for (int i = 0; i < inFiles.length; i++) {
                addFileToZip(zos, inFiles[i], (pathInZip == null ? null : pathInZip[i]), ignoreFiles);
            }
            
        }
    }
    
    /* 
     * Packs all files of a dir in a zip file. The pathInZip array denotes where each file should
     * be stored in the zip. If pathInZip equals null then all files will be stored at the root
     * of the zip file
     */
    static public void createZipFromRootDir(File rootDir, File pathInZip, File zipFile, File... ignoreFiles)
        throws IllegalArgumentException, IOException, ZipException {
        
        // Check if the given input file is file
        if (rootDir.isFile()) {
            throw new IllegalArgumentException("Given root dir is a file: " + rootDir.getPath());
        }
        
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            
            // Add the new zipfile to the ignoreFiles list
            File[] newIgnoreFiles;
            if (ignoreFiles == null) {
                newIgnoreFiles = new File[] {zipFile};
                
            } else {
                newIgnoreFiles = new File[ignoreFiles.length + 1];
                
                newIgnoreFiles[0] = zipFile;
                for (int i = 1; i < newIgnoreFiles.length; i++) {
                    newIgnoreFiles[i] = ignoreFiles[i];
                }
            }
            
            // Add the dir to the zip file
            addDirToZip(zos, rootDir, pathInZip, newIgnoreFiles);
        }
    }
    
    /* 
     * Writes all files in a dir in the zip file at the given path, keeping the hirachy of the dir.
     * If pathInZip equals null then the file will be stored at the root.
     */
    static public void addDirToZip(ZipOutputStream zos, File dirFile, File pathInZip, File... ignoreFiles)
        throws IllegalArgumentException, IOException, ZipException {
        
        if (dirFile.isFile()) {
            throw new IllegalArgumentException("Input dir was a file: " + dirFile.getPath());
        }
        
        ArrayList<File[]> files = listFilesAndPathsFromRootDir(dirFile, null, true);
        
        for (int i = 0; i < files.size(); i++) {
            addFileToZip(zos, files.get(i)[0], files.get(i)[1], ignoreFiles);
        }
    }
    
    /* 
     * Writes a file in the zip file at the given path. If pathInZip equals null then
     * the file will be stored at the root.
     */
    static public void addFileToZip(ZipOutputStream zos, File inFile, File pathInZip, File... ignoreFiles)
        throws IOException, ZipException {
        
        addFileToZip(zos, inFile, inFile.getName(), pathInZip, ignoreFiles);
    }
    
    /* 
     * Writes a file in the zip file at the given path with the given name. If pathInZip equals null then
     * the file will be stored at the root.
     */
    static public void addFileToZip(ZipOutputStream zos, File inFile, String fileName, File pathInZip, File... ignoreFiles)
        throws IOException, ZipException {
        
        // Ignore the file if it is in the ignoreFiles array
        if (ignoreFiles != null) {
            for (int i = 0; i < ignoreFiles.length; i++) {
                if (inFile.equals(ignoreFiles[i])) {
                    return;
                }
            }
        }
        
        if (inFile.isFile()) {
            try (FileInputStream fis = new FileInputStream(inFile)) {
                
                ZipEntry ze = (pathInZip != null
                                   ? new ZipEntry(pathInZip.getPath() + File.separator + fileName)
                                   : new ZipEntry(fileName));
                
                copyFileDate(inFile, ze);
                zos.putNextEntry(ze);
                
                byte[] data = new byte[BLOCK_SIZE];
                int len = 0;
                long lengthFile = inFile.length();
                
                while ((len = fis.read(data)) >= 0) {
                    zos.write(data, 0, len);
                    
                    if (inFile.length() != lengthFile) {
                        throw new ZipException("Tried to add a file that changed in size: " + inFile.getPath());
                    }
                }
            }
            
        } else { // inFile.isFile() == false
            ZipEntry ze = (pathInZip == null ? new ZipEntry(File.separator) : new ZipEntry(pathInZip.getPath() + File.separator));
            copyFileDate(inFile, ze);
            zos.putNextEntry(ze);
            zos.closeEntry();
        }
    }
    
    /* 
     * Unzips a zip file and writes the data to the targetDir.
     */
    static public void unZip(File zipFile, File targetDir) throws IOException {
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry ze;
            
            // Iterate over the ZipEntries and convert them to Files
            while ((ze = zis.getNextEntry()) != null) {
                // Determine the new file path and name
                File newFile = new File(targetDir.toPath() + File.separator + ze.getName());
                
                if (!ze.getName().endsWith(File.separator)) {
                    // Create the folders needed for the file
                    newFile.getParentFile().mkdirs();
                    
                    // If the file already exists, delete it
                    if (newFile.exists()) newFile.delete();
                    
                    // Write the data to the new file
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        
                        byte[] data = new byte[BLOCK_SIZE];
                        int len = 0;
                        
                        while ((len = zis.read(data)) >= 0) {
                            fos.write(data, 0, len);
                        }
                    }
                    
                    // Set the corresponding dates of the output file to the same time as the file from the zip.
                    copyFileDate(ze, newFile);
                    
                } else {
                    newFile.mkdir();
                }
                
                // Set the corresponding dates of the output file to the same time as the file from the zip.
                copyFileDate(ze, newFile);
            }
            
        }
    }
    
    /* 
     * Copies the creation, last modified and last access times from a ZipEntry to a File.
     */
    static public void copyFileDate(ZipEntry sourceZE, File destFile) throws IOException {
        FileTime creationTime = sourceZE.getCreationTime();
        FileTime lastModifiedTime = sourceZE.getLastModifiedTime();
        FileTime lastAccessTime = sourceZE.getLastAccessTime();
        
        Files.setAttribute(destFile.toPath(), "creationTime", creationTime);
        Files.setAttribute(destFile.toPath(), "lastModifiedTime", lastModifiedTime);
        Files.setAttribute(destFile.toPath(), "lastAccessTime", lastAccessTime);
    }
    
    /* 
     * Copies the creation, last modified and last access times from a File to a ZipEntry.
     */
    static public void copyFileDate(File sourceFile, ZipEntry destZE) throws IOException {
        FileTime creationTime = (FileTime) Files.getAttribute(sourceFile.toPath(), "creationTime");
        FileTime lastModifiedTime = (FileTime) Files.getAttribute(sourceFile.toPath(), "lastModifiedTime");
        FileTime lastAccessTime = (FileTime) Files.getAttribute(sourceFile.toPath(), "lastAccessTime");
        
        destZE.setCreationTime(creationTime);
        destZE.setLastModifiedTime(lastModifiedTime);
        destZE.setLastAccessTime(lastAccessTime);
    }
    
    /* 
     * Copies the creation, last modified and last access times from a File to a File.
     */
    static public void copyFileDate(File sourceFile, File destFile) throws IOException {
        FileTime creationTime = (FileTime) Files.getAttribute(sourceFile.toPath(), "creationTime");
        FileTime lastModifiedTime = (FileTime) Files.getAttribute(sourceFile.toPath(), "lastModifiedTime");
        FileTime lastAccessTime = (FileTime) Files.getAttribute(sourceFile.toPath(), "lastAccessTime");
        
        Files.setAttribute(destFile.toPath(), "creationTime", creationTime);
        Files.setAttribute(destFile.toPath(), "lastModifiedTime", lastModifiedTime);
        Files.setAttribute(destFile.toPath(), "lastAccessTime", lastAccessTime);
    }
    
    /* 
     * Returns an array containing (in this order) the creation, last modified, and last access times.
     */
    static public FileTime[] getFileData(File sourceFile) throws IOException {
        return new FileTime[] {(FileTime) Files.getAttribute(sourceFile.toPath(), "creationTime"),
            (FileTime) Files.getAttribute(sourceFile.toPath(), "lastModifiedTime"),
            (FileTime) Files.getAttribute(sourceFile.toPath(), "lastAccessTime")};
    }
    
    /* 
     * Sets the creation, last modified, and last access times (in this order) of a file);
     */
    static public void setFileData(File destFile, FileTime[] fileTimes) throws IOException {
        Files.setAttribute(destFile.toPath(), "creationTime", fileTimes[0]);
        Files.setAttribute(destFile.toPath(), "lastModifiedTime", fileTimes[1]);
        Files.setAttribute(destFile.toPath(), "lastAccessTime", fileTimes[2]);
    }
    
    /*
    public static void main(String[] args) {
        //new EncrTool();
        File inputDir = new File("C:\\Users\\s155587\\Documents\\_projects\\encryption");
        File zipFile = new File("C:\\Users\\s155587\\Documents\\_projects\\encryption\\testxx.zip");
        File outputDir = new File("C:\\Users\\s155587\\Documents\\_projects\\encryption\\testxxx");
        try {
            createZipFromRootDir(inputDir, null, zipFile);
            //unZip(zipFile, outputDir);
            //if (zipFile.exists()) zipFile.delete();
            //ArrayList<File[]> tmp = ZipTool.listFilesAndPathsFromRootDir(new File(System.getProperty("user.dir") + "\\encryption"));
            //createZip(tmp, zipFile);
            //createZip(null, null, zipFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }*/
    
    public static void main(String[] args) {
        File zipFile = new File(args[0]);
        File outputDir = new File(args[1]);
        try {
            unZip(zipFile, outputDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
}