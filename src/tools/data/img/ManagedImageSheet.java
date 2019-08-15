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
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


// Tools imports
import tools.img.ImageManager;


/**
 * Image sheet implementation which uses image sheet arrays from the {@link ImageManager} class.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class ManagedImageSheet
        extends AbstractImageSheet {
     
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The id name of the image sheet. */
    private final String idName;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new sheet from the given data. <br>
     * Also adds the image file to the image manager with the ID the same as the file name.
     * 
     * @param shortFileName The short file name of the image sheet.
     *     This value is also used as ID.
     * @param width  The width of each subimage.
     * @param height The height of each subimage.
     * 
     * @see ImageManager#registerSheet(String, String, int, int)
     */
    public ManagedImageSheet(String shortFileName, int width, int height) {
        this(shortFileName, shortFileName, width, width);
    }
    
    /**
     * Creates a new sheet from the given data. <br>
     * Also adds the image file to the image manager with the given ID.
     * 
     * @param shortFileName The short file name of the image sheet.
     * @param idName The name that can be used for later reference.
     * @param width  The width of each subimage.
     * @param height The height of each subimage.
     * 
     * @see ImageManager#registerSheet(String, String, int, int)
     */
    public ManagedImageSheet(String shortFileName, String idName, int width, int height) {
        ImageManager.registerSheet(shortFileName, idName, width, height);
        this.idName = idName;
    }
    
    /**
     * Creates a new sheet from the given data. <br>
     * Also adds the image file to the image manager with the ID the same as the file name.
     * 
     * @param shortFileName The local image file name.
     * @param startX The pixel x-coordinate of the start location in the image. (incl.)
     * @param startY The pixel y-coordinate of the start location in the image. (incl.)
     * @param endX The pixel x-coordinate of the end location of the image. (excl.)
     * @param endY The pixel y-coordinate of the end location of the image. (excl.)
     * @param width The width of each sub-image.
     * @param height The height of each sub-image.
     * 
     * @see #ManagedImageSheet(String, String, int, int, int, int, int, int)
     */
    public ManagedImageSheet(String shortFileName, int startX, int startY,
            int endX, int endY, int width, int height) {
        this(shortFileName, shortFileName, startX, startY, endX, endY, width, height);
    }
    
    /**
     * Creates a new sheet from the given data. <br>
     * Also adds the image file to the image manager with the given ID.
     * 
     * @param shortFileName The local image file name.
     * @param idName The name that can be used for later reference.
     * @param startX The pixel x-coordinate of the start location in the image. (incl.)
     * @param startY The pixel y-coordinate of the start location in the image. (incl.)
     * @param endX The pixel x-coordinate of the end location of the image. (excl.)
     * @param endY The pixel y-coordinate of the end location of the image. (excl.)
     * @param width The width of each sub-image.
     * @param height The height of each sub-image.
     * 
     * @see ImageManager#registerSheet(String, String, int, int, int, int, int, int)
     */
    public ManagedImageSheet(String shortFileName, String idName, int startX, int startY,
            int endX, int endY, int width, int height) {
        ImageManager.registerSheet(shortFileName, idName, startX, startY, endX, endY, width, height);
        this.idName = idName;
    }
    
    /**
     * Creates a new sheet from the given data. <br>
     * Also adds the image file to the image manager with the ID the same as the file name.
     * 
     * @param shortFileName The local image file name.
     * @param idName The name that can be used for later reference.
     * @param recs The locations and sizes of the images to load.
     * 
     * @see #ManagedImageSheet(String, String, Rectangle[][])
     */
    public ManagedImageSheet(String shortFileName, Rectangle[][] recs) {
        this(shortFileName, shortFileName, recs);
    }
    
    /**
     * Creates a new sheet from the given data. <br>
     * Also adds the image file to the image manager with the given ID.
     * 
     * @param shortFileName The local image file name.
     * @param recs The locations and sizes of the images to load.
     * 
     * @see ImageManager#registerSheet(String, String, Rectangle[][])
     */
    public ManagedImageSheet(String shortFileName, String idName, Rectangle[][] recs) {
        ImageManager.registerSheet(shortFileName, shortFileName, recs);
        this.idName = shortFileName;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * @return The ID name of the image sheet.
     */
    public String getIdName() {
        return idName;
    }
    
    @Override
    public BufferedImage[][] getSheet() {
        return ImageManager.getSheet(idName);
    }
    
    @Override
    public Image get(int x, int y, int width, int height, int scaleHints) {
        return ImageManager.getImage(idName, x, y).getScaledInstance(width, height, scaleHints);
    }
    
    @Override
    public int getNumWidth() {
        return ImageManager.getNumImgWidth(idName);
    }
    
    @Override
    public int getNumHeight() {
        return ImageManager.getNumImgHeight(idName);
    }
    
    
}
