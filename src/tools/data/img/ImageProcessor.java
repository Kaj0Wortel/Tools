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
 * Functional interface for processing an image in the {@link ProcessedImageSheet} class.
 * 
 * @author Kaj Wortel
 */
@FunctionalInterface
public interface ImageProcessor {
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Processes the source image to create the final image.
     * 
     * @param x The x-coord of the image to process.
     * @param y The y-coord of the image to process.
     * @param width The width of the resulting image. Must be non-negative and non-zero.
     * @param height The height of the resulting image. Must be non-negative and non-zero.
     * @param scaleHints The hints used for scaling.
     * @param img The images to process.
     * 
     * @return The processed image.
     */
    public Image process(int x, int y, int width, int height,
            int scaleHints, Image[] imgs);
    
    
}
