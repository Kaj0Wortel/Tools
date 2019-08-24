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
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import tools.data.file.FileTree;


// Tools imports
import tools.io.ImageSheetLoader;


/**
 * Token class for unequally divided images on a sheet. <br>
 * Uses {@link ImageSheetLoader#loadImage(FileTree, String, Rectangle[][])} for reading the images.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class UnequalToken
        extends Token {
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The locations and sizes of the images to crop. */
    private final Rectangle[][] recs;
    
    /** The number of images in the width of the sheet. */
    private final int numImgWidth;
    /** The number of images in the height of the sheet. */
    private final int numImgHeight;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new token wich can load unequally sized images in a sheet.
     * 
     * @param fileTree The file tree to use.
     * @param path The path of the file inside the file tree.
     * @param idName The id used for referencing.
     * @param recs The bounds of the sub-images.
     */
    UnequalToken(FileTree fileTree, String path, String idName, Rectangle[][] recs) {
        super(fileTree, path, idName);
        
        numImgWidth = recs.length;
        int maxHeight = 0;
        for (Rectangle[] row : this.recs = recs) {
            maxHeight = Math.max(maxHeight, row.length);
        }
        numImgHeight = maxHeight;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public BufferedImage[][] getSheet()
            throws IOException {
        return ImageSheetLoader.ensureLoadedAndGetImage(
                getFileTree(), getPath(), getIdName(), recs
        );
    }
    
    @Override
    public int getNumImgWidth() {
        return numImgWidth;
    }
    
    @Override
    public int getNumImgHeight() {
        return numImgHeight;
    }
    
    
}
