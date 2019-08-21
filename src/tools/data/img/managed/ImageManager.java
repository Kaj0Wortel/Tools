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

package tools.data.img.managed;


// Java imports
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;


// Tools imports
import tools.data.file.FileTree;
import tools.data.img.BoundedImageSheet;
import tools.data.img.ImageSheet;
import tools.data.img.ManagedImageSheet;
import tools.io.ImageSheetLoader;
import tools.log.Logger;


/**
 * Function class for easier handeling of images. <br>
 * Applies dynamic loading of requested image sheets and unloads images sheets
 * which were not used within the time limit. <br>
 * <br>
 * When an image sheet -- or a part of it -- is requested, then there are two possibilities:
 * <ol>
 *   <li>
 *     <b>Case</b>: The Sheet is loaded in memory. <br>
 *     <b>Action</b>: The timer for that sheet is resetted the sheet is returned. <br>
 *     <b>Possible errors</b>:
 *     <ul>
 *       <li>
 *         The image was not registered. <br>
 *         Result: a {@link NoSuchElementException} is thrown.
 *       </li>
 *       </ul>
 *   </li>
 *   <li>
 *     <b>Case</b>: The sheet is not loaded in memory. <br>
 *     <b>Action</b>: The sheet is loaded in memory from the original image location,
 *                    the timer is set and the sheet is returned. <br>
 *     <b>Possible errors</b>:
 *     <ul>
 *       <li>
 *         The image was not registered. <br>
 *         Result: a {@link NoSuchElementException} is thrown.
 *       </li>
 *       <li>
 *         The original image location is not accessible. <br>
 *         Result: a {@link IllegalStateException} is thrown with the {@link IOException} as source.
 *       </li>
 *     </ul>
 *   </li>
 * </ol>
 * 
 * @todo
 * Add examples here.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public final class ImageManager {
    
    /* -------------------------------------------------------------------------
     * Constants.
     * -------------------------------------------------------------------------
     */
    /** The map containing all registered tokens. */
    private static final Map<String, Token> TOKEN_MAP = new HashMap<>();
    /** The queue containing all entries which should be removed at some point. */
    private static final DelayQueue<Delay> QUEUE = new DelayQueue<>();
    
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The delay for removing an entry from the cache. */
    private static long removeDelay = 60;
    /** The time unit of {@link #removeDelay}. */
    private static TimeUnit timeUnit = TimeUnit.SECONDS;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * This is a static singleton class. No instances should be made.
     * 
     * @deprecated No instances should be made.
     */
    @Deprecated
    private ImageManager() { }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Returns the ID of the image entry from the given file tree at the given path.
     * 
     * @param fileTree The file tree the image file is located.
     * @param path The path the image is located at.
     * 
     * @return The ID of the image entry.
     */
    public static String getIdOf(FileTree fileTree, String path) {
        return fileTree.toAbsolutePath(path);
    }
    
    /**
     * Returns the token with the given ID, checks for null and resets the timer for
     * that token.
     * 
     * @param idName The ID of the token to get.
     * 
     * @return The token with the given ID.
     * 
     * @throws NoSuchElementException If no ID with the given name exists.
     */
    private static Token getToken(String idName)
            throws NoSuchElementException {
        if (idName == null) return null;
        Token token = TOKEN_MAP.get(idName);
        if (token == null) {
            throw new NoSuchElementException(
                    "No such ID has been registered. Requested ID: '" + idName + "'.");
        }
        return token;
    }
    
    /**
     * (Re-)sets the timer of the given token.
     * 
     * @param token The token the timer needs to be (re-)setted.
     */
    private static void resetTimerOf(Token token) {
        Delay delay = new Delay(removeDelay, timeUnit, token);
        QUEUE.remove(delay);
        QUEUE.add(delay);
        
    }
    
    /**
     * Sets the delay after which an inactive image sheet will be removed from memory.
     * 
     * @apiNote
     * This change will <b>NOT</b> take effect for already queued tokens.
     * It will only affect those images which have been requested after this function was called.
     * 
     * @param delay The new delay.
     * @param timeUnit The time unit of the delay.
     */
    public static void setDelay(long delay, TimeUnit timeUnit) {
        if (delay <= 0) {
            throw new IllegalArgumentException("Expected a delay bigger then 0, but found " + delay);
        }
        removeDelay = delay;
        ImageManager.timeUnit = timeUnit;
    }
    
    /**
     * Registers a image sheet for later use.
     * 
     * @implNote
     * When there already exists a sheet with the same absolute path, then
     * the previous sheet <b>WILL BE OVERWRITTEN</b> with this call.
     * 
     * @param fileTree The file tree to use.
     * @param path The path of the file inside the file tree.
     * @param width  The width of each subimage.
     * @param height The height of each subimage.
     * 
     * @return The ID of the sheet for later referencing.
     * 
     * @throws IllegalArgumentException If the given path doesn't exists.
     * 
     * @see #registerSheet(FileTree, String, String, int, int, int, int, int, int)
     */
    public static String registerSheet(FileTree fileTree, String path, int width, int height)
            throws IllegalArgumentException {
        return registerSheet(fileTree, path, 0, 0, -1, -1, width, height);
    }
    
    /**
     * Registers a image sheet for later use.
     * 
     * @implNote
     * When there already exists a sheet with the same absolute path, then
     * the previous sheet <b>WILL BE OVERWRITTEN</b> with this call.
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
     * @return The ID of the sheet for later referencing.
     * 
     * @throws IllegalArgumentException If the given path doesn't exists.
     */
    public static String registerSheet(FileTree fileTree, String path, int startX, int startY,
            int endX, int endY, int width, int height)
            throws IllegalArgumentException {
        if (!fileTree.exists(path)) {
            throw new IllegalArgumentException("The path doesn't exist: '" + path + "'.");
        }
        String idName = getIdOf(fileTree, path);
        Token token = new EqualToken(fileTree, path, idName, startX, startY,
                endX, endY, width, height);
        TOKEN_MAP.put(idName, token);
        return idName;
    }
    
    /**
     * Registers a image sheet for later use.
     * 
     * @implNote
     * When there already exists a sheet with the same absolute path, then
     * the previous sheet <b>WILL BE OVERWRITTEN</b> with this call.
     * 
     * @param fileTree The file tree to use.
     * @param path The path of the file inside the file tree.
     * @param recs The locations and sizes of the images to load.
     * 
     * @return The ID of the sheet for later referencing.
     * 
     * @throws IllegalArgumentException If the given path doesn't exists.
     */
    public static String registerSheet(FileTree fileTree, String path, Rectangle[][] recs) {
        if (!fileTree.exists(path)) {
            throw new IllegalArgumentException("The path doesn't exist: '" + path + "'.");
        }
        String idName = getIdOf(fileTree, path);
        TOKEN_MAP.put(idName, new UnequalToken(fileTree, path, idName, recs));
        return idName;
    }
    
    /**
     * Returns whether a sheet with the given ID was registered.
     * 
     * @param idName The ID of the sheet to check.
     * 
     * @return {@code true} if a sheet with the given ID was registered. {@code false} otherwise.
     */
    public static boolean hasSheet(String idName) {
        return TOKEN_MAP.containsKey(idName);
    }
    
    /**
     * @param idName The ID of a sheet.
     * 
     * @return The sheet that was registered with the given ID.
     * 
     * @throws IllegalStateException If the sheet could not be loaded.
     * @throws NoSuchElementException If the sheet was not yet registered.
     */
    public static BufferedImage[][] getSheet(String idName)
            throws IllegalStateException, NoSuchElementException {
        Token token = getToken(idName);
        resetTimerOf(token);
        
        try {
            return token.getSheet();
            
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
    
    /**
     * Returns an {@link ImageSheet} which represents the sheet.
     * 
     * @param idName The ID of the image sheet.
     * 
     * @return The image sheet with the given ID.
     * 
     * @throws IllegalStateException If the sheet could not be loaded.
     * @throws NoSuchElementException If the sheet was not yet registered.
     */
    public static BoundedImageSheet getImageSheet(String idName)
            throws IllegalStateException, NoSuchElementException {
        return new ManagedImageSheet(getToken(idName).getIdName());
    }
    
    /**
     * Returns a {@Link BufferedImage} of the image which is located at the coordinates {@code (x, y)}
     * at the image sheet with the given ID.
     * 
     * @param idName The ID of the sheet.
     * @param x The x-coord of the image to be returned.
     * @param y The y-coord of the image to be returned.
     * 
     * @return The image on the image sheet with the given ID at the coordinatess {@code (x, y)}.
     * 
     * @throws IllegalArgumentException If the given {@code x} and {@code y} lie outside the range
     *     of the sheet.
     * @throws IllegalStateException If the sheet could not be loaded, or if the loaded sheet
     *     contains less than 1 image.
     * @throws NoSuchElementException If the sheet was not yet registered.
     */
    public static BufferedImage getImage(String idName, int x, int y)
            throws IllegalStateException, NoSuchElementException {
        BufferedImage[][] sheet = getSheet(idName);
        imgCoordBoundCheck(idName, sheet, x, y);
        return sheet[x][y];
    }
    
    /**
     * Returns the image of the sheet with the given ID and which is at the
     * given index when linearizing the sheet to a single column.
     * 
     * @param idName The id name of the sheet.
     * @param i The index for horizontal retrieval of the image.
     * @return The image at the sheet denoted by {@code idName} at index
     *     {@code i}, where the 2D array is checked left to right, then up to down.
     * 
     * @see #getVertImage(String, int) &nbsp for getting an image the other way around.
     * @see #getImage(String, int, int) &nbsp for getting an image by direct x- and y-coords.
     */
    public static BufferedImage getHoriImage(String idName, int i) {
        if (idName == null) return null;
        
        BufferedImage[][] sheet = getSheet(idName);
        imgCoordBoundCheck(idName, sheet, i);
        
        int width = sheet.length;
        return sheet[i % width][i / width];
    }
    
    /**
     * Returns the image of the sheet with the given ID and which is at the
     * given index when linearizing the sheet to a single row.
     * 
     * @param idName The id name of the sheet.
     * @param i The index for vertical retrieval of the image.
     * 
     * @return The image at the sheet denoted by {@code idName} at index
     *     {@code i}, where the 2D array is checked left to up to down, then left to right.
     * 
     * @see #getVertImage(String, int) &nbsp for getting an image the other way around.
     * @see #getImage(String, int, int) &nbsp for getting an image by direct x- and y-coords.
     * 
     * @throws IllegalArgumentException If the given {@code i} lies outside the range of the sheet.
     * @throws IllegalStateException If the sheet could not be loaded, or if the loaded sheet
     *     contains less than 1 image.
     * @throws NoSuchElementException If the sheet was not yet registered.
     */
    public static BufferedImage getVertImage(String idName, int i) {
        if (idName == null) return null;
        
        BufferedImage[][] sheet = getSheet(idName);
        imgCoordBoundCheck(idName, sheet, i);
        
        int height = sheet[0].length;
        return sheet[i / height][i % height];
    }
    
    /**
     * Checks whether the given index {@code i} is a valid for
     * the given sheet {@code sheet}.
     * 
     * @param idName The ID name of the image. Only used here for debugging purposes.
     * @param sheet The sheet to check the index of.
     * @param i The value representing the index.
     */
    private static void imgCoordBoundCheck(String idName, BufferedImage[][] sheet, int i) {
        int width = sheet.length;
        if (width == 0) {
            throw new IllegalStateException(
                    "Empty image array found (width == 0), idName=" + idName);
        }
        
        int height = sheet[0].length;
        if (height == 0) {
            throw new IllegalStateException(
                    "Empty image array found (height == 0), idName=" + idName);
        }
        
        if (i < 0) {
            throw new IllegalArgumentException(
                    "Expected an index larger equal then 0, but found: " + i
                            + ". idName=" + idName);
        }
        
        if (i > width * height) {
            throw new IllegalArgumentException(
                    "Expected an index less or equal then width(" + width
                            + ") * height(" + height + ") = "
                            + (width * height) + ", but found index " + i
                            + ". idName=" + idName);
        }
    }
    
    /**
     * Checks whether the given coords {@code x} and {@code y} are valid
     * indices for the given sheet {@code sheet}.
     * 
     * @param idName The ID name of the image sheet.
     * @param sheet The sheet to check the index of.
     * @param x The x-coord of the image.
     * @param y The y-coord of the image.
     */
    private static void imgCoordBoundCheck(String idName, BufferedImage[][] sheet, int x, int y) {
        if (sheet == null) {
            throw new NullPointerException(
                    "Sheet found was null. idName=" + idName);
        }
        
        int width = sheet.length;
        if (width == 0) {
            throw new IllegalStateException(
                    "Empty image array found (width == 0), idName=" + idName);
        }
        
        int height = sheet[x].length;
        if (height == 0) {
            throw new IllegalStateException(
                    "Empty image array found (height == 0), idName=" + idName);
        }
        
        if (x < 0) {
            throw new IllegalArgumentException(
                    "Expected an x-coord larger equal then 0, but found: x=" + x
                            + ". idName=" + idName);
        }
        if (y < 0) {
            throw new IllegalArgumentException(
                    "Expected an y-coord larger equal then 0, but found: " + y
                            + ". idName=" + idName);
        }
        
        if (x >= width) {
            throw new IllegalArgumentException(
                    "Expected an x-coord less or equal to the width(" + width
                            + "), but found x-coord " + x + ". idName=" + idName);
        }
        if (y >= height) {
            throw new IllegalArgumentException(
                    "Expected an y-coord less or equal to the height(" + height
                            + "), but found y-coord " + y + ". idName=" + idName);
        }
    }
    
    /**
     * Clears all loaded images and all registered images.
     */
    public static void clearAll() {
        QUEUE.clear();
        TOKEN_MAP.clear();
        ImageSheetLoader.clear();
    }
    
    /**
     * Returns the number of images which are in the width of the image sheet.
     * 
     * @param idName The ID name of the sheet.
     * 
     * @return The width of the sheet with the given ID, counted as the number of images.
     */
    public static int getNumImgWidth(String idName) {
        return getToken(idName).getNumImgWidth();
    }
    
    /**
     * Returns the number of images which are in the height of the image sheet.
     * 
     * @param idName The ID name of the sheet.
     * 
     * @return The height of the sheet with the given ID, counted as the number of images.
     */
    public static int getNumImgHeight(String idName) {
        return getToken(idName).getNumImgHeight();
    }
    
    /**
     * @param idName The ID name of the sheet.
     * 
     * @return The total number of images in the sheet.
     */
    public static int getNumImg(String idName) {
        return getNumImgWidth(idName) * getNumImgHeight(idName);
    }
    
    /**
     * Upon static class initialisation, create an inactive thread that
     * waits until an image becomes invalid and removes this image from RAM. <br>
     * The thread is scheduled with the lowest priority to prevent disrupting
     * time-sensitive threads.
     */
    static {
        new Thread(ImageManager.class.getName() + " Thread") {
            @Override
            public void run() {
                Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
                while (true) {
                    try {
                        Delay delay = QUEUE.poll(1, TimeUnit.DAYS);
                        if (delay == null) continue;
                        ImageSheetLoader.removeImage(delay.getToken().getIdName());
                        
                    } catch (InterruptedException e) {
                        Logger.write(e);
                    }
                }
            }
        }.start();
    }
    
    
}
