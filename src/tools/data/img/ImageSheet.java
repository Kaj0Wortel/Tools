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
import java.awt.Graphics2D;
import java.awt.Image;


// Tools imports
import tools.ImageTools;


/**
 * Interface for image sheets. <br>
 * An image sheet consists of a 2D sheet of images, which can be addessed via coordinates.
 * The coordinates can be nagative, and the images returned from the get functions
 * are allowed to be {@code null}.
 * 
 * @version 2.0
 * @author Kaj Wortel
 */
public abstract class ImageSheet {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** Denotes whether this image sheet is opaque. */
    protected boolean opaque;
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Scales the given image to the given width and heigh using the scaling hints.
     * The width and height remain unchanged if respectivily the width and height
     * equal {@code -1}.
     * 
     * @todo
     * - Migrate to {@link ImageTools}.
     * - Check negative width/height condition.
     * 
     * @param img The image to scale.
     * @param width The width of the resulting image, or {@code -1} if it remains unchanged.
     * @param height The height of the resulting image, or {@code -1} if it remains unchanged.
     * @param scaleHints The scaling hints.
     * 
     * @return An image with the given width and height, and scaled using the scaling hints.
     */
    protected final static Image scale(Image img, int width, int height, int scaleHints) {
        if (img == null || (width <= 0 && height <= 0)) return img;
        int imgWidth = img.getWidth(null);
        int imgHeight = img.getHeight(null);
        if (width < 0) width = imgWidth;
        if (height == -1) height = imgHeight;
        if (width == imgWidth && imgHeight == height) return img;
        return img.getScaledInstance(width, height, scaleHints);
    }
    
    /**
     * Returns the image sheet at {@code (x, y)}.
     * 
     * @apiNote
     * The default result of this function is equal to {@code #getSheet()[x][y]},
     * but this function might evaluate it faster, or can return different (error) values.
     * 
     * @implSpec
     * The implementation of this function is optional, and is mainly used to increase performance.
     * 
     * @param x The x-coordinate of the image to get.
     * @param y The y-coordinate of the image to get.
     * 
     * @return The image at {@code (x, y)}.
     * 
     * @throws IndexOutOfBoundsException If the given indices {@code x} and {@code y}
     *     are out of bounds.
     * 
     * @see #getSheet()
     */
    public Image get(int x, int y)
            throws IndexOutOfBoundsException {
        return getSheet()[x][y];
    }
    
    /**
     * Returns the scaled image at {@code (x, y)}.
     * 
     * @apiNote
     * The default result of this function is equal to
     * {@code get(x, y).getScaledInstance(width, height, scaleHints)},
     * but it might be evaluated faster, or can return different (error) values.
     * 
     * @implSpec
     * The implementation of this function is optional, and is mainly used to increase performance.
     * 
     * @param x The x-coordinate of the image to get.
     * @param y The y-coordinate of the image to get.
     * @param width The width of the image to return. {@code -1} if the default width should be used.
     * @param height The height of the image to return. {@code -1} if the default height should be used.
     * @param scaleHints The scale hints used for scaling the image.
     * 
     * @return The image sheet at {@code (x, y)} with the given width and height,
     *     and scaled using the scale hints.
     * 
     * @throws IndexOutOfBoundsException If the given indices {@code x} and {@code y}
     *     are out of bounds.
     * @throws IllegalArgumentException If the given width and height are zero or negative.
     * 
     * @see #get(int, int)
     * @see Image#getScaledInstance(int, int, int)
     */
    public Image get(int x, int y, int width, int height, int scaleHints)
            throws IllegalArgumentException, IndexOutOfBoundsException {
        return get(x, y).getScaledInstance(width, height, scaleHints);
    }
    
    /**
     * @return {@code true} if the images are opaque. {@code false} otherwise.
     */
    public boolean isOpaque() {
        return opaque;
    }
    
    /**
     * Sets the opacity of the image sheet.
     * 
     * @param opaque The new opacity of the image sheet.
     */
    public void setOpaque(boolean opaque) {
        this.opaque = opaque;
    }
    
    /**
     * Returns the underlaying source image sheet. <br>
     * This function should in general not be used as special value and exception handling
     * cannot be done here.]
     * 
     * @implNote
     * The returned array should not be modified, as it might be the underlying array
     * which stores the images. Note that it is <b>NOT</b> guaranteed that the returned
     * value is the underlaying array.
     * 
     * @return The image sheet described by this object, or {@code null} if not supported.
     */
    public abstract Image[][] getSheet();
    
    /**
     * @return {@code false} if requesting the image at (x, y) will always result
     *     in an {@link IndexOutOfBoundsException} being thrown. {@code true} otherwise.
     */
    public abstract boolean canAccess(int x, int y);
    
    /**
     * Converts {@code this} to a {@link BoundedImageSheet}
     * 
     * @return A fresh bounded image sheet representing {@code this}, bounded .
     */
    public BoundedImageSheet toBoundedSheet(final int width, final int height) {
        return new SubImageSheet(this, width, height);
    }
    
    /**
     * Draws the image at ({@code x}, {@code y}) at the given coordinates
     * ({@code posX}, {@code posY}).
     * 
     * @implSpec
     * The implementation of this function is optional, and is mainly used to increase performance.
     * 
     * @param g2d The graphics object used to draw on.
     * @param x The x-coordinate of the image on the sheet to use.
     * @param y The y-coordinate of the image on the sheet to use.
     * @param posX The amount to translate the image in the x-axis.
     * @param posY The amount to translate the image in the y-axis.
     * 
     * @return {@code true} if anything was drawn on the graphics object. {@code false} otherwise.
     * 
     * @throws IndexOutOfBoundsException If the given indices {@code x} and {@code y}
     *     are out of bounds.
     * 
     * @see #get(int, int)
     */
    public boolean draw(Graphics2D g2d, int x, int y, int posX, int posY)
            throws IndexOutOfBoundsException {
        Image img = get(x, y);
        if (img == null) return false;
        g2d.drawImage(img, posX, posY, null);
        return true;
    }
    
    /**
     * Resizes the image at ({@code x}, {@code y}) to the size ({@code width} x {@code height}) and
     * draws it at the given coordinates ({@code posX}, {@code posY}).
     * 
     * @implSpec
     * The implementation of this function is optional, and is mainly used to increase performance.
     * 
     * @param g2d The graphics object used to draw on.
     * @param x The x-coordinate of the image on the sheet to use.
     * @param y The y-coordinate of the image on the sheet to use.
     * @param posX The amount to translate the image in the x-axis.
     * @param posY The amount to translate the image in the y-axis.
     * @param width The width of the image to draw.
     * @param height The height of the image to draw.
     * @param scaleHints The scale hints used for scaling the image.
     * 
     * @return {@code true} if anything was drawn on the graphics object. {@code false} otherwise.
     * 
     * @throws IndexOutOfBoundsException If the given indices {@code x} and {@code y}
     *     are out of bounds.
     * 
     * @see #get(int, int, int, int, int)
     */
    public boolean draw(Graphics2D g2d, int x, int y, int posX, int posY,
            int width, int height, int scaleHints)
            throws IndexOutOfBoundsException {
        Image img = get(x, y, width, height, scaleHints);
        if (img == null) return false;
        g2d.drawImage(img, posX, posY, null);
        return true;
    }
    
    
}
