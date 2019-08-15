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
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class SourceImageSheet
        extends AbstractImageSheet {
     
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The images used as source for this sheet. */
    private final Image[][] images;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new image sheet from a 2D array of images.
     * 
     * @param images The source images for this sheet.
     */
    public SourceImageSheet(Image[][] images) {
        if (images == null) throw new NullPointerException();
        this.images = images;
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
    public Image get(int x, int y, int width, int height, int scaleHints) {
        return scale(images[x][y], width, height, scaleHints);
    }
    
    @Override
    public int getNumWidth() {
        return images.length;
    }
    
    @Override
    public int getNumHeight() {
        if (images.length == 0) return 0;
        return images[0].length;
    }
    
    
}
