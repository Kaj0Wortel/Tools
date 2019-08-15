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
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;


/**
 * Image sheet which adds the images on top of each other. <br>
 * The first image in the array is drawn first, hence it will appear
 * on the bottom layer of the image.
 * 
 * @todo
 * In {@link #PROCESSOR}, figure out a way to add rendering hints.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class OverlayImageSheet
        extends ProcessedImageSheet {
    
    /* -------------------------------------------------------------------------
     * Constants.
     * -------------------------------------------------------------------------
     */
    /**
     * The processor used to create overlays.
     */
    private static final ImageProcessor PROCESSOR = (x, y, width, height, scaleHints, imgs) -> {
        BufferedImage target = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2d = target.createGraphics();
        for (Image img : imgs) {
            //System.out.println("width: " + width + ", height: " + height + (img == null ? ", null img!" : ""));
            if (img == null) continue;
            if (width == img.getWidth(null) && height == img.getHeight(null)) {
                g2d.drawImage(img, 0, 0, null);
            } else {
                // TODO: add rendering hints.
                //Map<?, ?> hints = new HashMap<>();
                //g2d.addRenderingHints(hints);
                g2d.drawImage(img, 0, 0, width, height, null);
            }
        }
        g2d.dispose();
        return target;
    };
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new image sheet with the given images. <br>
     * Overlays the image sheets from the lower index towards the higher index.
     * 
     * @param width The width of the sheet.
     * @param height The height of the  sheet.
     * @param imgs The images to be processed.
     * 
     * @see ProcessedImageSheet#ProcessedImageSheet(int, int, ImageProcessor, Image[][]...)
     */
    public OverlayImageSheet(int width, int height, Image[][]... img) {
        super(width, height, PROCESSOR, img);
    }
    
    /**
     * Creates a new image sheet with the given image sheets. <br>
     * Overlays the image sheets from the lower index towards the higher index.
     * 
     * @param width The width of the sheet.
     * @param height The height of the  sheet.
     * @param imgs The images to be processed.
     * 
     * @see ProcessedImageSheet#ProcessedImageSheet(int, int, ImageProcessor, ImageSheet...)
     */
    public OverlayImageSheet(int width, int height, ImageSheet... sheets) {
        super(width, height, PROCESSOR, sheets);
    }
    
    
}
