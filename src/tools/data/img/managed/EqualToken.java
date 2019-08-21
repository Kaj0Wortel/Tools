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
import tools.data.file.FileTree;


// Tools imports
import tools.io.ImageSheetLoader;


/**
 * Token class for equally divided images on a sheet.
 * Uses {@link ImageSheetLoader} for loading and storing the images.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class EqualToken
        extends Token {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The start x-coordinate (inclusive). */
    private final int startX;
    /** The start y-coordinate (inclusive). */
    private final int startY;
    /** The end x-coordinate (exclusive). */
    private final int endX;
    /** The end y-coordinate (exclusive). */
    private final int endY;
    /** The width of each sub-image. */
    private final int width;
    /** The height of each sub-image. */
    private final int height;

    /** The number of images which fit in the width of the image sheet. */
    private int numImgWidth;
    /** The number of images which fit in the height of the image sheet. */
    private int numImgHeight;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new token which can load an image sheet with equal sizes and
     * offsets amoung the images.
     * 
     * @param fileTree The file tree to use.
     * @param path The path of the file inside the file tree.
     * @param idName The id used for referencing.
     * @param startX The start x-coordinate (inclusive).
     * @param startY The start y-coordinate (inclusive).
     * @param endX The end x-coordinate (exclusive), or {@code -1} for the maximum end point.
     * @param endY The end y-coordinate (exclusive), or {@code -1} for the maximum end point.
     * @param width The width of each sub-image.
     * @param height The height of each sub-image.
     */
    EqualToken(FileTree fileTree, String path, String idName, int startX, int startY,
            int endX, int endY, int width, int height) {
        super(fileTree, path, idName);
        
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.width = width;
        this.height = height;
        
        if (endX == -1) numImgWidth = -1;
        else numImgWidth = (endX - startX) / width;
        if (endY == -1) numImgHeight = -1;
        else numImgHeight = (endY - startY) / height;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public BufferedImage[][] getSheet()
            throws IOException {
        return ImageSheetLoader.ensureLoadedAndGetImage(getFileTree(), getPath(), getIdName(),
                startX, startY, endX, endY, width, height);
    }
    
    @Override
    public int getNumImgWidth() {
        if (numImgWidth == -1) calcDimensions();
        return numImgWidth;
    }
    
    @Override
    public int getNumImgHeight() {
        if (numImgHeight == -1) calcDimensions();
        return numImgHeight;
    }
    
    public void calcDimensions() {
        BufferedImage[][] sheet = ImageManager.getSheet(getIdName());
        if (sheet != null) {
            numImgWidth = sheet.length;
            numImgHeight = (numImgWidth > 0 ? sheet[0].length : 0);
        }
    }
    
    
}
