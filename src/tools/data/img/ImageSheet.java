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
 * Interface for image sheets. <br>
 * An image sheet consists of a 2D sheet of images, which can be addessed via coordinates.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public interface ImageSheet {
    
    /**
     * Returns the image sheet as a 2D array, such that
     * {@link #get}{@code(x, y) == }{@link #getSheet()}{@code [x][y]}. <br>
     * <br>
     * The returned array should not be modified, as it might be the underlying array
     * which stores the images. Note that it is <b>not</b> guaranteed that the returned
     * value is the underlaying array.
     * 
     * @return The image sheet described by this object, or {@code null} if not supported.
     */
    public Image[][] getSheet();
    
    /**
     * Returns the image sheet at {@code (x, y)}.
     * 
     * @apiNote
     * The result of this function should be equal to {@link #getSheet()}{@code [x][y]},
     * but this function might evaluate it faster.
     * 
     * @param x The x-coordinate of the image to get.
     * @param y The y-coordinate of the image to get.
     * 
     * @return The image at {@code (x, y)}.
     */
    public default Image get(int x, int y) {
        return get(x, y, -1, -1, 0);
    }
    
    /**
     * Returns the scaled image at {@code (x, y)}.
     * 
     * @apiNote
     * The result of this function should be (more or less) equal to
     * {@link #getSheet()}{@code [x][y].getScaledInstance(width, height, scaleHints)},
     * but it might be evaluated faster.
     * 
     * @param x The x-coordinate of the image to get.
     * @param y The y-coordinate of the image to get.
     * @param width The width of the image to return.
     *     {@code -1} if the default width should be used.
     * @param height The height of the image to return.
     *     {@code -1} if the default height should be used.
     * 
     * @return The image sheet at {@code (x, y)} with the given width and height,
     *     and scaled using the scale hints.
     */
    public Image get(int x, int y, int width, int height, int scaleHints);
    
    /**
     * @return The width of the sheet, counted as the number of images,
     *     or {@code -1} if this function is not supported.
     */
    public int getNumWidth();
    
    /**
     * @return The height of the sheet, counted as the number of images,
     *     or {@code -1} if this function is not supported.
     */
    public int getNumHeight();
    
    /**
     * @return {@code true} if the images are opaque. {@code false} otherwise.
     */
    public boolean isOpaque();
    
    /**
     * Sets the opacity of the image sheet.
     * 
     * @param opaque The new opacity of the image sheet.
     */
    public void setOpaque(boolean opaque);
    
    
}
