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

package tools.img;


// Tools imports
import tools.io.LoadImages2;
import tools.log.Logger;


// Java imports
import java.awt.image.BufferedImage;
import java.io.IOException;


/**DONE
 * Token class for equally divided images on a sheet.
 * Uses {@link LoadImages2#loadImage(String, String, int, int, int, int, int, int)} for reading the images.
 * 
 * @author Kaj Wortel
 */
public class EqualToken
        extends Token {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The start x-coordinate (inclusive). */
    final private int startX;
    /** The start y-coordinate (inclusive). */
    final private int startY;
    /** The end x-coordinate (exclusive). */
    final private int endX;
    /** The end y-coordinate (exclusive). */
    final private int endY;
    /** The width of each sub-image. */
    final private int width;
    /** The height of each sub-image. */
    final private int height;

    /** The number of images which fit in the width of the image sheet. */
    final private int numImgWidth;
    /** The number of images which fit in the height of the image sheet. */
    final private int numImgHeight;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new token which can load an image sheet with equal sizes and
     * offsets amoung the images.
     * 
     * @param shortFileName The local image file.
     * @param idName The id used for referencing.
     * @param startX The start x-coordinate (inclusive).
     * @param startY The start y-coordinate (inclusive).
     * @param endX The end x-coordinate (exclusive).
     * @param endY The end y-coordinate (exclusive).
     * @param width The width of each sub-image.
     * @param height The height of each sub-image.
     * 
     * @see LoadImages2#loadImage(String, String, int, int, int, int, int, int)
     */
    EqualToken(String shortFileName, String idName, int startX, int startY,
            int endX, int endY, int width, int height) {
        super(shortFileName, idName);
        
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.width = width;
        this.height = height;
        
        this.numImgWidth = (endX - startX) / width;
        this.numImgHeight = (endY - startY) / height;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public BufferedImage[][] getSheet() {
        try {
            return LoadImages2.ensureLoadedAndGetImage(getFileName(), getIDName(),
                    startX, startY, endX, endY, width, height);
            
        } catch (IOException | IllegalArgumentException e) {
            Logger.write(e);
            return null;
        }
    }
    
    /**
     * Determines the width, counted as the number of images.
     * 
     * @return the width of the sheet, counted as the number of images.
     */
    public int getNumImgWidth() {
        return numImgWidth;
    }
    
    /**
     * Determines the height, counted as the number of images.
     * 
     * @return the height of the sheet, counted as the number of images.
     */
    public int getNumImgHeight() {
        return numImgHeight;
    }
    
    
}
