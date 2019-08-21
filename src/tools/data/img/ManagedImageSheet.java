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
import tools.data.file.FileTree;
import tools.data.img.managed.ImageManager;


/**
 * Image sheet implementation which uses image sheet arrays from the {@link ImageManager} class.
 * 
 * @todo
 * - Add direct get image for getRows/getCols.
 * 
 * @version 1.1
 * @author Kaj Wortel
 */
public class ManagedImageSheet
        extends BoundedImageSheet {
     
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The ID name of the image sheet. */
    private final String idName;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    
    /**
     * Creates a new sheet from the given data. <br>
     * Also adds the image file to the image manager with the given ID.
     * 
     * @param fileTree The file tree to use.
     * @param path The path of the file inside the file tree.
     * @param width  The width of each subimage.
     * @param height The height of each subimage.
     * 
     * @see ImageManager#registerSheet(FileTree, String, String, int, int)
     */
    public ManagedImageSheet(FileTree fileTree, String path, int width, int height) {
        this.idName = ImageManager.registerSheet(fileTree, path, width, height);
    }
    
    /**
     * Creates a new sheet from the given data. <br>
     * Also adds the image file to the image manager with the given ID.
     * 
     * @param fileTree The file tree to use.
     * @param path The path of the file inside the file tree.
     * @param startX The pixel x-coordinate of the start location in the image. (incl.)
     * @param startY The pixel y-coordinate of the start location in the image. (incl.)
     * @param endX The pixel x-coordinate of the end location of the image. (excl.)
     * @param endY The pixel y-coordinate of the end location of the image. (excl.)
     * @param width The width of each sub-image.
     * @param height The height of each sub-image.
     * 
     * @see ImageManager#registerSheet(FileTree, String, String, int, int, int, int, int, int)
     */
    public ManagedImageSheet(FileTree fileTree, String path, int startX, int startY,
            int endX, int endY, int width, int height) {
        this.idName = ImageManager.registerSheet(fileTree, path, startX, startY,
                endX, endY, width, height);
    }
    
    /**
     * Creates a new sheet from the given data. <br>
     * Also adds the image file to the image manager with the given ID.
     * 
     * @param fileTree The file tree to use.
     * @param path The path of the file inside the file tree.
     * @param recs The locations and sizes of the images to load.
     * 
     * @see ImageManager#registerSheet(FileTree, String, String, Rectangle[][])
     */
    public ManagedImageSheet(FileTree fileTree, String path, Rectangle[][] recs) {
        this.idName = ImageManager.registerSheet(fileTree, path, recs);
    }
    
    /**
     * Creates a new sheet which retrieves the image sheet from the image manager
     * with the given ID.
     * 
     * @implNote
     * The sheet must be registered before requesting images via this sheet.
     * 
     * @param idName The ID of the image sheet.
     */
    public ManagedImageSheet(String idName) {
        this.idName = idName;
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
    public Image get(int x, int y) {
        return ImageManager.getImage(idName, x, y);
    }
    
    @Override
    public int getWidth() {
        return ImageManager.getNumImgWidth(idName);
    }
    
    @Override
    public int getHeight() {
        return ImageManager.getNumImgHeight(idName);
    }
    
    
}
