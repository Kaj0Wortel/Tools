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
 * Abstract implementation of the {@link ImageSheet} interface.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public abstract class AbstractImageSheet
        implements ImageSheet {
    
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
    @Override
    public boolean isOpaque() {
        return opaque;
    }
    
    @Override
    public void setOpaque(boolean opaque) {
        this.opaque = opaque;
    }
    
    /**
     * Scales the given image to the given width and heigh using the scaling hints.
     * The width and height remain unchanged if respectivily the width and height
     * equal {@code -1}.
     * 
     * @todo
     * migrate to {@link ImageTools}.
     * 
     * @param img The image to scale.
     * @param width The width of the resulting image, or {@code -1} if it remains unchanged.
     * @param height The height of the resulting image, or {@code -1} if it remains unchanged.
     * @param scaleHints The scaling hints.
     * 
     * @return An image with the given width and height, and scaled using the scaling hints.
     */
    protected final static Image scale(Image img, int width, int height, int scaleHints) {
        if (img == null || (width < 0 && height < 0)) return img;
        int imgWidth = img.getWidth(null);
        int imgHeight = img.getHeight(null);
        if (width < 0) width = imgWidth;
        if (height == -1) height = imgHeight;
        if (width == imgWidth && imgHeight == height) return img;
        return img.getScaledInstance(width, height, scaleHints);
    }
    
    
}
