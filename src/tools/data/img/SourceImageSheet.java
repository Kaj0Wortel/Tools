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

package tools.data.img;


// Java imports
import java.awt.Image;


/**
 * Image sheet class for using a raw image sheet as source.
 * If the width and height are not specified, then the width and height
 * of the underlying array are used. <br>
 * If the height of the given array is not equal for all sub-arrays, then the
 * maximum height is used as height, and the un-indexed areas will return
 * {@code null} when probed.
 * 
 * @version 1.2
 * @author Kaj Wortel
 */
public class SourceImageSheet
        extends BoundedImageSheet {
     
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The images used as source for this sheet. */
    private final Image[][] images;
    
    /** The width of the sheet. */
    private final int sheetWidth;
    /** The height of the sheet. */
    private final int sheetHeight;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new image sheet from a 2D array of images.
     * 
     * @apiNote
     * The images inside the sheet are <b>NOT</b> copied to a new array to save
     * memory and object creation speed. This does also mean that the backening
     * image array should not be modified. If it is modified after the constructor
     * has been called, then the behaviour of any function is undefined.
     * 
     * @param images The source images for this sheet.
     */
    public SourceImageSheet(Image[][] images) {
        if (images == null) throw new NullPointerException();
        this.images = images;
        sheetWidth = images.length;
        if (sheetWidth == 0) {
            sheetHeight = 0;
            
        } else {
            int maxHeight = 0;
            for (Image[] imgs : images) {
                maxHeight = Math.max(maxHeight, imgs.length);
            }
            sheetHeight = maxHeight;
        }
    }
    
    /**
     * Creates a new image sheet with the given width and height from a 2D array of images.
     * 
     * @apiNote
     * The images inside the sheet are <b>NOT</b> copied to a new array to save
     * memory and object creation speed. This does also mean that the backening
     * image array should not be modified. If it is modified after the constructor
     * has been called, then the behaviour of any function is undefined.
     * 
     * @param images The source images for this sheet.
     * @param sheetWidth The width of the image sheet.
     * @param sheetHeight The height of the image sheet.
     */
    public SourceImageSheet(Image[][] images, int sheetWidth, int sheetHeight) {
        if (sheetWidth < 0 || sheetHeight < 0) {
            throw new IllegalArgumentException(
                    "Expected a width(" + sheetWidth + ") and height(" + sheetHeight
                            + ") bigger or equal to 0!");
        }
        this.images = images;
        this.sheetWidth = sheetWidth;
        this.sheetHeight = sheetHeight;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public Image[][] getSheet() {
        return images;
    }
    
    @Override
    public Image get(int x, int y) {
        checkBounds(x, y);
        Image[] row = getSheet()[x];
        if (y < row.length) return row[y];
        else return null;
    }
    
    @Override
    public int getWidth() {
        return sheetWidth;
    }
    
    @Override
    public int getHeight() {
        return sheetHeight;
    }
    
    
}
