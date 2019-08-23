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

package tools.io;


// Java imports
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.imageio.ImageIO;


// Tools imports
import tools.Var;
import tools.data.file.FileTree;


/**
 * Provides a convinient way of storing image sheets for a multi-threaded environment. <br>
 * <br>
 * An image sheet consists of a 2D BufferedImage array, where each element {@code arr[x][y]}
 * denotes the image at the coorcinates {@code (x, y)}.
 * 
 * @todo
 * Improve class description comments.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public final class ImageSheetLoader {
    
    /* -------------------------------------------------------------------------
     * Constants.
     * -------------------------------------------------------------------------
     */
    /** The map the images will be stored in. */
    private static final Map<String, BufferedImage[][]> SHEETS
            = new ConcurrentHashMap<>();
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * This is a static singleton class. No instances should be made.
     * 
     * @deprecated No instances should be made.
     */
    @Deprecated(forRemoval = false)
    private ImageSheetLoader() { }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Loads an image from the given source and convert it into an image sheet.
     * 
     * @param fileTree The file tree to use.
     * @param path The path of the file inside the file tree.
     * 
     * @implNote
     * The default value for {@code idName} is {@code fileTree.toAbsolutePath(path)}.
     * 
     * @return A 2D BufferedImage array, with the images loaded such that
     *     {@code bi[x][y]} represents the image part at {@code (x, y)}
     *     from the selected part (starting at {@code (0, 0)}).
     * 
     * @throws IOException If some I/O error occured.
     * @throws IllegalArgumentException If the given bounding values do not match with
     *     the bounds of the image sheet.
     * 
     * @see #loadImage(FileTree, String, String)
     */
    public static BufferedImage[][] loadImage(FileTree fileTree, String path)
            throws IllegalArgumentException, IOException {
        return loadImage(fileTree, path, fileTree.toAbsolutePath(path));
    }
    
    /**
     * Loads an image from the given source and convert it into an image sheet. <br>
     * Each image in this sheet has size {@code (width, height)}.
     * 
     * @implNote
     * The default value for {@code idName} is {@code fileTree.toAbsolutePath(path)}.
     * 
     * @param fileTree The file tree to use.
     * @param path The path of the file inside the file tree.
     * @param width The width of each sub-image.
     * @param height The height of each sub-image.
     * 
     * @return A 2D BufferedImage array, with the images loaded such that
     *     {@code bi[x][y]} represents the image part at {@code (x, y)}
     *     from the selected part (starting at {@code (0, 0)}).
     * 
     * @throws IOException If some I/O error occured.
     * @throws IllegalArgumentException If the given bounding values do not match with
     *     the bounds of the image sheet.
     * 
     * @see #loadImage(FileTree, String, String, int, int)
     */
    public static BufferedImage[][] loadImage(FileTree fileTree, String path,
            int width, int height)
            throws IllegalArgumentException, IOException {
        return loadImage(fileTree, path, fileTree.toAbsolutePath(path), width, height);
    }
    
    /**
     * Loads an image from the given source and convert it into an image sheet.
     * The images are created from {@code (startX, startY)} (inclusive) till
     * {@code (endX, endY)} (exclusive). <br>
     * Each image in this sheet has size {@code (width, height)}.
     * 
     * @implNote
     * The default value for {@code idName} is {@code fileTree.toAbsolutePath(path)}.
     * 
     * @param fileTree The file tree to use.
     * @param path The path of the file inside the file tree.
     * @param startX The pixel x-coordinate of the start location in the image (incl.).
     * @param startY The pixel y-coordinate of the start location in the image (incl.).
     * @param endX The pixel x-coordinate of the end location of the image (excl.).
     * @param endY The pixel y-coordinate of the end location of the image (excl.).
     * @param width The width of each sub-image.
     * @param height The height of each sub-image.
     * 
     * @return A 2D BufferedImage array, with the images loaded such that
     *     {@code bi[x][y]} represents the image part at {@code (x, y)}
     *     from the selected part (starting at {@code (0, 0)}).
     * 
     * @throws IOException If some I/O error occured.
     * @throws IllegalArgumentException If the given bounding values do not match with
     *     the bounds of the image sheet.
     * 
     * @see #loadImage(FileTree, String, String, int, int, int, int, int, int)
     */
    public static BufferedImage[][] loadImage(FileTree fileTree, String path,
            int startX, int startY, int endX, int endY, int width, int height)
            throws IllegalArgumentException, IOException {
        return loadImage(fileTree, path, fileTree.toAbsolutePath(path), startX, startY,
                endX, endY, width, height);
    }
    
    /**
     * Loads an image from the given source and convert it into an image sheet.
     * 
     * @param fileTree The file tree to use.
     * @param path The path of the file inside the file tree.
     * @param idName The ID of the image sheet to be created.
     * 
     * @return A 2D BufferedImage array, with the images loaded such that
     *     {@code bi[x][y]} represents the image part at {@code (x, y)}
     *     from the selected part (starting at {@code (0, 0)}).
     * 
     * @throws IOException If some I/O error occured.
     * @throws IllegalArgumentException If the given bounding values do not match with
     *     the bounds of the image sheet.
     * 
     * @see #loadImage(FileTree, String, String, int, int)
     */
    public static BufferedImage[][] loadImage(FileTree fileTree, String path, String idName)
            throws IllegalArgumentException, IOException {
        return loadImage(fileTree, path, idName, -1, -1);
    }
    
    /**
     * Loads an image from the given source and convert it into an image sheet. <br>
     * Each image in this sheet has size {@code (width, height)}.
     * 
     * @param fileTree The file tree to use.
     * @param path The path of the file inside the file tree.
     * @param idName The ID of the image sheet to be created.
     * @param width The width of each sub-image.
     * @param height The height of each sub-image.
     * 
     * @return A 2D BufferedImage array, with the images loaded such that
     *     {@code bi[x][y]} represents the image part at {@code (x, y)}
     *     from the selected part (starting at {@code (0, 0)}).
     * 
     * @throws IOException If some I/O error occured.
     * @throws IllegalArgumentException If the given bounding values do not match with
     *     the bounds of the image sheet.
     * 
     * @see #loadImage(FileTree, String, String, int, int, int, int, int, int)
     */
    public static BufferedImage[][] loadImage(FileTree fileTree, String path, String idName,
            int width, int height)
            throws IllegalArgumentException, IOException {
        return loadImage(fileTree, path, idName, 0, 0, -1, -1, width, height);
    }
    
    /**
     * Loads an image from the given source and convert it into an image sheet.
     * The images are created from {@code (startX, startY)} (inclusive) till
     * {@code (endX, endY)} (exclusive). <br>
     * Each image in this sheet has size {@code (width, height)}.
     * 
     * @implNote
     * <ul>
     *   <li> The default value for {@code idName} is {@code fileTree.toAbsolutePath(path)}. </li>
     *   <li> The default value for {@code startX} and {@code startY} is {@code 0}. </li>
     *   <li> The default value for {@code endX} and {@code endY} is {@code -1}. </li>
     *   <li> The default value for {@code width} and {@code height} is {@code -1}. </li>
     *   <li> If {@code endX} or {@code endY} equals {@code -1}, then these will be changed
     *        to respectivily the width or height of the image. </li>
     *   <li> If {@code width} or {@code height} equals {@code -1}, then these will be changed
     *        to {@code endX - startX} and {@code endY - startY} respectively. </li>
     * </ul>
     * 
     * @param fileTree The file tree to use.
     * @param path The path of the file inside the file tree.
     * @param idName The ID of the image sheet to be created.
     * @param startX The pixel x-coordinate of the start location in the image (incl.).
     * @param startY The pixel y-coordinate of the start location in the image (incl.).
     * @param endX The pixel x-coordinate of the end location of the image (excl.).
     * @param endY The pixel y-coordinate of the end location of the image (excl.).
     * @param width The width of each sub-image.
     * @param height The height of each sub-image.
     * 
     * @return A 2D BufferedImage array, with the images loaded such that
     *     {@code bi[x][y]} represents the image part at {@code (x, y)}
     *     from the selected part (starting at {@code (0, 0)}).
     * 
     * @throws IOException If some I/O error occured.
     * @throws IllegalArgumentException If the given bounding values do not match with
     *     the bounds of the image sheet.
     */
    public static BufferedImage[][] loadImage(FileTree fileTree, String path, String idName,
            int startX, int startY, int endX, int endY, int width, int height)
            throws IllegalArgumentException, IOException {
        // Check if the ID of the image is not yet in use.
        if (SHEETS.containsKey(idName)) {
            throw new IllegalArgumentException("Name was already used: '" + idName + "'.");
        }
        
        // Read the image
        BufferedImage bigImg = ImageIO.read(fileTree.getStream(path));
        
        // Check if the end coords are correct.
        if (endX == -1) {
            endX = bigImg.getWidth();
            
        } else if (endX > bigImg.getWidth()) {
            throw new IllegalArgumentException(
                    "Given end x was larger then the image. end x = "
                            + endX + ", img width: " + bigImg.getWidth() + ".");
        }
        
        if (endY == -1 ) {
            endY = bigImg.getHeight();
            
        } else if (endY > bigImg.getHeight()) {
            throw new IllegalArgumentException(
                    "Given end y was larger then the image. end y = "
                            + endY + ", img height: " + bigImg.getHeight() + ".");
        }
        
        // Check if the end-coords are bigger then the starting coords.
        if (startX >= endX)
            throw new IllegalArgumentException(
                    "Starting x coord >= end x coord: " 
                            + startX + " >= " + endX);
        if (startY >= endY)
            throw new IllegalArgumentException(
                    "Starting y coord >= end y coord: "
                            + startY + " >= " + endY + ".");
        
        int dX = endX - startX;
        int dY = endY - startY;
        
        // Check if the image sizes are valid.
        if (width == -1) {
            width = endX - startX;
            
        } else if (dX % width != 0) {
            throw new IllegalArgumentException(
                    "Given width (" + width + "), startX (" + startX
                            + ") and/or endX (" + endX + ") is invalid.");
        }
        
        if (height == -1) {
            height = endY - startY;
            
        } else if (dY % height != 0) {
            throw new IllegalArgumentException(
                    "Given width (" + height + "), startY (" + startY
                            + ") and/or endY (" + endY + ") is invalid.");
        }
        
        // Split the image into parts.
        BufferedImage[][] sheet = new BufferedImage[dX / width][dY / height];
        for (int i = startX ; i < endX; i += width) {
            for (int j = startY ; j < endY; j += height) {
                sheet[(i - startX) / width][(j - startY) / height]
                    = bigImg.getSubimage(i, j, width, height);
            }
        }
        
        SHEETS.put(idName, sheet);
        return sheet;
    }
    
    /**
     * Loads an image from the given source and convert it into an image sheet.
     * 
     * @param fileTree The file tree to use.
     * @param path The path of the file inside the file tree.
     * @param locs The locations and sizes of the images to load.
     *     The image stored at the indices of the returned image 2D array
     *     match the corresponding indices of this array.
     * 
     * @return a 2D BufferedImage array, with the images loaded such that
     *     they match the indices of {@code locs}.
     * 
     * @throws IOException If some I/O error occured.
     * @throws IllegalArgumentException If the given bounding values do not match with
     *     the bounds of the image sheet.
     * 
     * @see #loadImage(FileTree, String, String, Rectangle[][])
     */
    public static BufferedImage[][] loadImage(FileTree fileTree, String path,
            Rectangle[][] locs)
            throws IllegalArgumentException, IOException {
        return loadImage(fileTree, path, fileTree.toAbsolutePath(path), locs);
    }
    
    
    /**
     * Loads an image from the given source and convert it into an image sheet.
     * 
     * @param fileTree The file tree to use.
     * @param path The path of the file inside the file tree.
     * @param idName The ID of the image sheet to be created.
     * @param locs The locations and sizes of the images to load.
     *     The image stored at the indices of the returned image 2D array
     *     match the corresponding indices of this array.
     * 
     * @return a 2D BufferedImage array, with the images loaded such that
     *     they match the indices of {@code locs}.
     * 
     * @throws IOException If some I/O error occured.
     * @throws IllegalArgumentException If the given bounding values do not match with
     *     the bounds of the image sheet.
     */
    public static BufferedImage[][] loadImage(FileTree fileTree, String path, String idName,
            Rectangle[][] locs)
            throws IllegalArgumentException, IOException {
        // Check if the ID of the image is not yet in use.
        if (SHEETS.containsKey(idName))
            throw new IllegalArgumentException(
                    "Name was already used: '" + idName + "'");
        
        // Read the image.
        BufferedImage bigImg = ImageIO.read(fileTree.getStream(path));
        
        BufferedImage[][] sheet = new BufferedImage[locs.length][];
        Rectangle bounds = new Rectangle(bigImg.getWidth(), bigImg.getHeight());
        for (int i = 0; i < locs.length; i++) {
            if (locs[i] == null) continue;
            sheet[i] = new BufferedImage[locs[i].length];
            
            for (int j = 0; j < locs[i].length; j++) {
                Rectangle rec = locs[i][j];
                if (rec == null) continue;
                
                if (!bounds.contains(rec)) {
                    throw new IllegalArgumentException(
                            "Rectangle didn't fit in the image size." + Var.LS
                                    + "Size=" + bounds.width + "x" + bounds.height + "." + Var.LS
                                    + "Given rectangle: " + rec.width + "x" + rec.height
                                    + " @ (" + rec.x+ "," + rec.y + ")");
                }
                
               sheet[i][j] = bigImg.getSubimage(rec.x, rec.y, rec.width, rec.height);
            }
        }
        
        SHEETS.put(idName, sheet);
        return sheet;
    }
    
    /**
     * Check if the image sheet with the given ID already exists.
     * If it doesn't, then create it with the given settings.
     * 
     * @param fileTree The file tree to use.
     * @param path The path of the file inside the file tree.
     * 
     * @return If the sheet was already loaded, then the loaded sheet. Otherwise attempt
     *     to load the sheet and return it.
     * 
     * @throws IOException If some I/O error occured.
     * @throws IllegalArgumentException If the given bounding values do not match with
     *     the bounds of the image sheet.
     * 
     * @see #ensureLoadedAndGetImage(FileTree, String, String)
     */
    public static BufferedImage[][] ensureLoadedAndGetImage(FileTree fileTree, String path)
            throws IllegalArgumentException, IOException {
        return ensureLoadedAndGetImage(fileTree, path, fileTree.toAbsolutePath(path),
                -1, -1);
    }
    
    /**
     * Check if the image sheet with the given ID already exists.
     * If it doesn't, then create it with the given settings.
     * 
     * @param fileTree The file tree to use.
     * @param path The path of the file inside the file tree.
     * @param width The width of each sub-image.
     * @param height The height of each sub-image.
     * 
     * @return If the sheet was already loaded, then the loaded sheet. Otherwise attempt
     *     to load the sheet and return it.
     * 
     * @throws IOException If some I/O error occured.
     * @throws IllegalArgumentException If the given bounding values do not match with
     *     the bounds of the image sheet.
     * 
     * @see #ensureLoadedAndGetImage(FileTree, String, String, int, int, int, int, int, int)
     */
    public static BufferedImage[][] ensureLoadedAndGetImage(FileTree fileTree, String path,
            int width, int height)
            throws IllegalArgumentException, IOException {
        return ensureLoadedAndGetImage(fileTree, path, fileTree.toAbsolutePath(path), width, height);
    }
    
    /**
     * Check if the image sheet with the given ID already exists.
     * If it doesn't, then create it with the given settings.
     * 
     * @param fileTree The file tree to use.
     * @param path The path of the file inside the file tree.
     * @param startX The pixel x-coordinate of the start location in the image (incl.).
     * @param startY The pixel y-coordinate of the start location in the image (incl.).
     * @param endX The pixel x-coordinate of the end location of the image (excl.).
     * @param endY The pixel y-coordinate of the end location of the image (excl.).
     * @param width The width of each sub-image.
     * @param height The height of each sub-image.
     * 
     * @return If the sheet was already loaded, then the loaded sheet. Otherwise attempt
     *     to load the sheet and return it.
     * 
     * @throws IOException If some I/O error occured.
     * @throws IllegalArgumentException If the given bounding values do not match with
     *     the bounds of the image sheet.
     * 
     * @see #ensureLoadedAndGetImage(FileTree, String, String, int, int, int, int, int, int)
     */
    public static BufferedImage[][] ensureLoadedAndGetImage(FileTree fileTree, String path,
            int startX, int startY, int endX, int endY, int width, int height)
            throws IllegalArgumentException, IOException {
        return ensureLoadedAndGetImage(fileTree, path, fileTree.toAbsolutePath(path),
                startX, startY, endX, endY, width, height);
    }
    
    /**
     * Check if the image sheet with the given ID already exists.
     * If it doesn't, then create it with the given settings.
     * 
     * @param fileTree The file tree to use.
     * @param path The path of the file inside the file tree.
     * @param idName The ID of the image sheet to be retrieved and, if needed, created.
     * 
     * @return If the sheet was already loaded, then the loaded sheet. Otherwise attempt
     *     to load the sheet and return it.
     * 
     * @throws IOException If some I/O error occured.
     * @throws IllegalArgumentException If the given bounding values do not match with
     *     the bounds of the image sheet.
     * 
     * @see #ensureLoadedAndGetImage(FileTree, String, String, int, int)
     */
    public static BufferedImage[][] ensureLoadedAndGetImage(FileTree fileTree, String path,
            String idName)
            throws IllegalArgumentException, IOException {
        return ensureLoadedAndGetImage(fileTree, path, idName, -1, -1);
    }
    
    /**
     * Check if the image sheet with the given ID already exists.
     * If it doesn't, then create it with the given settings.
     * 
     * @param fileTree The file tree to use.
     * @param path The path of the file inside the file tree.
     * @param idName The ID of the image sheet to be retrieved and, if needed, created.
     * @param width The width of each sub-image.
     * @param height The height of each sub-image.
     * 
     * @return If the sheet was already loaded, then the loaded sheet. Otherwise attempt
     *     to load the sheet and return it.
     * 
     * @throws IOException If some I/O error occured.
     * @throws IllegalArgumentException If the given bounding values do not match with
     *     the bounds of the image sheet.
     * 
     * @see #ensureLoadedAndGetImage(FileTree, String, String, int, int, int, int, int, int)
     */
    public static BufferedImage[][] ensureLoadedAndGetImage(FileTree fileTree, String path,
            String idName, int width, int height)
            throws IllegalArgumentException, IOException {
        return ensureLoadedAndGetImage(fileTree, path, idName, 0, 0, -1, -1, width, height);
    }
    
    /**
     * Check if the image sheet with the given ID already exists.
     * If it doesn't, then create it with the given settings.
     * 
     * @param fileTree The file tree to use.
     * @param path The path of the file inside the file tree.
     * @param idName The ID of the image sheet to be retrieved and, if needed, created.
     * @param startX The pixel x-coordinate of the start location in the image (incl.).
     * @param startY The pixel y-coordinate of the start location in the image (incl.).
     * @param endX The pixel x-coordinate of the end location of the image (excl.).
     * @param endY The pixel y-coordinate of the end location of the image (excl.).
     * @param width The width of each sub-image.
     * @param height The height of each sub-image.
     * 
     * @return If the sheet was already loaded, then the loaded sheet. Otherwise attempt
     *     to load the sheet and return it.
     * 
     * @throws IOException If some I/O error occured.
     * @throws IllegalArgumentException If the given bounding values do not match with
     *     the bounds of the image sheet.
     * 
     * @see #loadImage(FileTree, String, String, int, int, int, int, int, int)
     */
    public static BufferedImage[][] ensureLoadedAndGetImage(FileTree fileTree, String path,
            String idName, int startX, int startY, int endX, int endY, int width, int height)
            throws IllegalArgumentException, IOException {
        BufferedImage[][] sheet = SHEETS.get(idName);
        if (sheet != null) return sheet;
        return loadImage(fileTree, path, idName, startX, startY, endX, endY, width, height);
    }
    
    /**
     * Check if the image sheet with the given ID already exists.
     * If it doesn't, then create it with the given settings.
     * 
     * @param fileTree The file tree to use.
     * @param path The path of the file inside the file tree.
     * @param locs The locations and sizes of the images to load.
     *     The image stored at the indices of the returned image 2D array
     *     match the corresponding indices of this array.
     * 
     * @return If the sheet was already loaded, then the loaded sheet. Otherwise attempt
     *     to load the sheet and return it.
     * 
     * @throws IOException If some I/O error occured.
     * @throws IllegalArgumentException If the given bounding values do not match with
     *     the bounds of the image sheet.
     * 
     * @see #ensureLoadedAndGetImage(FileTree, String, String, Rectangle[][])
     */
    public static BufferedImage[][] ensureLoadedAndGetImage(FileTree fileTree, String path,
            Rectangle[][] locs)
            throws IllegalArgumentException, IOException {
        return ensureLoadedAndGetImage(fileTree, path, fileTree.toAbsolutePath(path), locs);
    }
    
    /**
     * Check if the image sheet with the given ID already exists.
     * If it doesn't, then create it with the given settings.
     * 
     * @param fileTree The file tree to use.
     * @param path The path of the file inside the file tree.
     * @param idName The ID of the image sheet to be retrieved and, if needed, created.
     * @param locs The locations and sizes of the images to load.
     *     The image stored at the indices of the returned image 2D array
     *     match the corresponding indices of this array.
     * 
     * @return If the sheet was already loaded, then the loaded sheet. Otherwise attempt
     *     to load the sheet and return it.
     * 
     * @throws IOException If some I/O error occured.
     * @throws IllegalArgumentException If the given bounding values do not match with
     *     the bounds of the image sheet.
     * 
     * @see #loadImage(FileTree, String, Rectangle[][])
     */
    public static BufferedImage[][] ensureLoadedAndGetImage(FileTree fileTree, String path,
            String idName, Rectangle[][] locs)
            throws IllegalArgumentException, IOException {
        BufferedImage[][] sheet = SHEETS.get(idName);
        if (sheet != null) return sheet;
        return loadImage(fileTree, path, idName, locs);
    }
    
    /**
     * Returns an earlier stored image array.
     * 
     * @param idName The ID of the image sheet to be retrieved.
     * 
     * @return The image sheet with the given ID, or {@code null} if no such sheet exists.
     */
    public static BufferedImage[][] getImage(String idName) {
        return SHEETS.get(idName);
    }
    
    /**
     * Returns an earlier stored image.
     * 
     * @param idName The ID of the image sheet to be retrieved.
     * 
     * @return The image sheet with the given ID.
     * 
     * @throws NoSuchFieldException If there doesn't exists an image sheet with the given ID.
     */
    public static BufferedImage[][] getImageException(String idName)
            throws NoSuchFieldException {
        BufferedImage[][] sheet = SHEETS.get(idName);
        if (sheet == null) {
            throw new NoSuchElementException("Image '" + idName + "' does not exist.");
        }
        return sheet;
    }
    
    /**
     * Removes a stored image sheet.
     * 
     * @param idName The ID of the image sheet to be removed.
     * 
     * @return The removed image sheet, or {@code null} if there was no image sheet
     *     with the given ID.
     */
    public static BufferedImage[][] removeImage(String idName) {
        return SHEETS.remove(idName);
    }
    
    /**
     * Removes all stored image sheets.
     */
    public static void clear() {
        SHEETS.clear();
    }
    
    /**
     * @return An iterator over all currently stored image sheets.
     */
    public static Iterator<Map.Entry<String, BufferedImage[][]>> iterator() {
        return SHEETS.entrySet().iterator();
    }
    
    
}


