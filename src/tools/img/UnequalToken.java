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
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;


/**DONE
 * Token class for unequally divided images on a sheet. <br>
 * Uses {@link LoadImages2#loadImage(String, String, Rectangle[][])} for reading the images.
 * 
 * @author Kaj Wortel
 */
class UnequalToken
        extends Token {
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The locations and sizes of the images to crop. */
    final private Rectangle[][] recs;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Constructor.
     * 
     * @param shortFileName The local image file.
     * @param idName The id used for referencing.
     * @param recs The bounds of the sub-images.
     */
    UnequalToken(String shortFileName, String idName, Rectangle[][] recs) {
        super(shortFileName, idName);
        this.recs = recs;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public BufferedImage[][] getSheet() {
        try {
            return LoadImages2.ensureLoadedAndGetImage(
                    getFileName(), getIDName(), recs
            );
            
        } catch (IOException | IllegalArgumentException e) {
            Logger.write(e);
            return null;
        }
    }
    
    
}
