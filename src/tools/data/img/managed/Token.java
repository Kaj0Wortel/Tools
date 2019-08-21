/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) August 2019 by Kaj Wortel - all rights reserved             *
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

package tools.data.img.managed;


// Java imports
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;


// Tools imports
import tools.MultiTool;
import tools.data.file.FileTree;


/**
 * Token class for the storing a image sheets entry.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public abstract class Token {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The file tree to use for accessing the file. */
    private final FileTree fileTree;
    /** The path of the image file. */
    private final String path;
    /** The ID of the image sheet. */
    private final String idName;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new token to be used for retrieving images.
     * 
     * @param fileTree The file tree to use.
     * @param path The path of the file inside the file tree.
     * @param idName The ID of the imagesheet used for referencing.
     */
    Token(FileTree fileTree, String path, String idName) {
        this.fileTree = fileTree;
        this.path = path;
        this.idName = idName;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * @return The file tree to use for accessing the file.
     */
    public FileTree getFileTree() {
        return fileTree;
    }
    
    /**
     * @return The local path of the image file.
     */
    public String getPath() {
        return path;
    }
    
    /**
     * @return The ID name of this token.
     */
    public String getIdName() {
        return idName;
    }
    
    /**
     * @return The image sheet represented by this token.
     * 
     * @throws IOException If some I/O error occured.
     */
    public abstract BufferedImage[][] getSheet()
            throws IOException;
    
    /**
     * Determines the width, counted as the number of images.
     * 
     * @return the width of the sheet, counted as the number of images.
     */
    public abstract int getNumImgWidth();
    
    /**
     * Determines the height, counted as the number of images.
     * 
     * @return the height of the sheet, counted as the number of images.
     */
    public abstract int getNumImgHeight();
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Token)) return false;
        Token token = (Token) obj;
        return Objects.equals(path, token.path) &&
                Objects.equals(idName, token.idName);
    }
    
    @Override
    public int hashCode() {
        return MultiTool.calcHashCode(new Object[] {path, idName});
    }
    
    
}
