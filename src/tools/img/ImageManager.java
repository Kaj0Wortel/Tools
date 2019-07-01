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
import tools.Var;
import tools.io.LoadImages2;
import tools.log.Logger;


// Java imports
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;


/**DONE
 * Function class for easier handeling of images. <br>
 * Applies dynamic loading of requested image sheets and unloads images sheets
 * that were not used within the time limit.
 * 
 * Uses the static singleton design pattern.
 * 
 * @author Kaj Wortel
 */
public class ImageManager {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The map containing all registered tokens. */
    final private static Map<String, Token> tokenMap = new HashMap<>();
    /** The queue containing all entries which should be removed at some point. */
    final private static DelayQueue<Delay> queue = new DelayQueue<>();
    
    /** Default delay for removing an entry from the cache. */
    private static long removeDelay = 60;
    /** The time unit used for removing an entry from the cache. */
    private static TimeUnit timeUnit = TimeUnit.SECONDS;
    
    
    /* -------------------------------------------------------------------------
     * Constructor.
     * -------------------------------------------------------------------------
     */
    /**
     * Private constructor due to the static single design pattern.
     */
    private ImageManager() { }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Sets the delay after which an inactive image sheet will be deleted.
     * Note that requesting the image sheet will reset the timer.
     * 
     * @param delay the new delay.
     * 
     * Note that the change WILL NOT take effect for already queued tokens.
     * Request them again via {@link getImage(String).}
     */
    public static void setDelay(long delay, TimeUnit timeUnit) {
        if (delay <= 0)
            throw new IllegalArgumentException(
                    "Expected a delay bigger then 0, but found " + delay);
        removeDelay = delay;
        ImageManager.timeUnit = timeUnit;
    }
    
    /**
     * Registers a image sheet for later use.
     * 
     * Note: when there already exists an id with the same name,
     * but a different token, the previous token <b>WILL BE OVERWRITTEN</b>!
     * Choose your ID's with caution!
     * 
     * @param shortFileName The short file name of the image sheet
     *     (e.g. starting at the {@code img} directory).
     * @param idName The name that can be used for later refference.
     * @param width  The width of each subimage.
     * @param height The height of each subimage.
     * 
     * @see Var#IMG_DIR
     */
    public static void registerSheet(String shortFileName, String idName,
            int width, int height) {
        registerSheet(shortFileName, idName, 0, 0, -1, -1, width, height);
    }
    
    /**
     * Registers a image sheet for later use.
     * 
     * Note: when there already exists an id with the same name,
     * but a different token, the previous token <b>WILL BE OVERWRITTEN</b>!
     * Choose your ID's with caution!
     * 
     * @param shortFileName The local image file name.
     * @param idName The name that can be used for later refference.
     * @param startX The pixel x-coordinate of the start location in the image. (incl.)
     * @param startY The pixel y-coordinate of the start location in the image. (incl.)
     * @param endX The pixel x-coordinate of the end location of the image. (excl.)
     * @param endY The pixel y-coordinate of the end location of the image. (excl.)
     * @param width The width of each sub-image.
     * @param height The height of each sub-image.
     * 
     * @see Var#IMG_DIR
     */
    public static void registerSheet(String shortFileName, String idName,
            int startX, int startY, int endX, int endY, int width, int height) {
        tokenMap.put(idName,
                new EqualToken(shortFileName, idName,
                        startX, startY,
                        endX, endY,
                        width, height));
    }
    
    /**
     * Registers a image sheet for later use.
     * 
     * Note: when there already exists an id with the same name,
     * but a different token, the previous token <b>WILL BE OVERWRITTEN</b>!
     * Choose your ID's with caution!
     * 
     * @param shortFileName The local image file name.
     * @param idName The name that can be used for later refference.
     * @param recs The locations and sizes of the images to load.
     * 
     * @see Var#IMG_DIR
     */
    public static void registerSheet(String shortFileName, String idName, Rectangle[][] recs) {
        tokenMap.put(idName, new UnequalToken(shortFileName, idName, recs));
    }
    
    /**
     * @param idName The id name the sheet was registered with.
     * @return The sheet that was registered with the given id name.
     * 
     * @throws NoSuchElementException Iff the sheet was not yet registered.
     */
    public static BufferedImage[][] getSheet(String idName)
            throws NoSuchElementException {
        if (idName == null) return null;
        
        Token token = tokenMap.get(idName);
        if (token == null)
            throw new NoSuchElementException(
                    "The token has not yet been registered. Req. id: "
                            + idName);
        Delay delay = new Delay(removeDelay, timeUnit, token);
        // Remove earlier queued delays for this image.
        queue.remove(delay);
        queue.add(delay);
        
        return token.getSheet();
    }
    
    /**
     * @param idName The id name of the sheet.
     * @param x The x-coord of the image to be returned.
     * @param y The y-coord of the image to be returned.
     * @return The image at the sheet denoted by {@code idName} at the coords {@code (x, y)}.
     */
    public static BufferedImage getImage(String idName, int x, int y) {
        if (idName == null) return null;
        
        BufferedImage[][] sheet = getSheet(idName);
        imgCoordBoundCheck(idName, sheet, x, y);
        
        return sheet[x][y];
    }
    
    /**
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
     * @param idName The id name of the sheet.
     * @param i The index for vertical retrieval of the image.
     * @return The image at the sheet denoted by {@code idName} at index
     *     {@code i}, where the 2D array is checked left to up to down, then left to right.
     * 
     * @see #getVertImage(String, int) &nbsp for getting an image the other way around.
     * @see #getImage(String, int, int) &nbsp for getting an image by direct x- and y-coords.
     */
    public static BufferedImage getVertImage(String idName, int i) {
        if (idName == null) return null;
        
        BufferedImage[][] sheet = getSheet(idName);
        imgCoordBoundCheck(idName, sheet, i);
        
        int height = sheet[0].length;
        return sheet[i / height][i % height];
    }
    
    /**
     * Checks whether the given index {@code i} is a vallid for
     * the given sheet {@code sheet}.
     * 
     * @param idName The id name of the image. Only for debugging purposes.
     * @param sheet The sheet to check the index of.
     * @param i The value representing the index.
     */
    private static void imgCoordBoundCheck(String idName,
            BufferedImage[][] sheet, int i) {
        int width = sheet.length;
        if (width == 0)
            throw new IllegalStateException(
                    "Empty image array found (width == 0), idName=" + idName);
        
        int height = sheet[0].length;
        if (height == 0)
            throw new IllegalStateException(
                    "Empty image array found (height == 0), idName=" + idName);
        
        if (i < 0)
            throw new IllegalArgumentException(
                    "Expected an index larger equal then 0, but found: " + i
                            + ". idName=" + idName);
        
        if (i > width * height)
            throw new IllegalArgumentException(
                    "Expected an index less or equal then width(" + width
                            + ") * height(" + height + ") = "
                            + (width * height) + ", but found index " + i
                            + ". idName=" + idName);
    }
    
    /**
     * Checks whether the given coords {@code x} and {@code y} are vallid
     * indices for the given sheet {@code sheet}.
     * 
     * @param idName The id name of the image sheet.
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
        
        int height = sheet[0].length;
        if (height == 0) {
            throw new IllegalStateException(
                    "Empty image array found (height == 0), idName=" + idName);
        }
        
        if (x < 0) {
            throw new IllegalArgumentException(
                    "Expected an x-coord larger equal then 0, but found: " + x
                            + ". idName=" + idName);
        }
        if (y < 0) {
            throw new IllegalArgumentException(
                    "Expected an y-coord larger equal then 0, but found: " + y
                            + ". idName=" + idName);
        }
        
        if (x > width) {
            throw new IllegalArgumentException(
                    "Expected an x-coord less or equal to the width(" + width
                            + "), but found x-coord " + x + ". idName=" + idName);
        }
        if (y > height) {
            throw new IllegalArgumentException(
                    "Expected an y-coord less or equal to the height(" + height
                            + "), but found y-coord " + y + ". idName=" + idName);
        }
    }
    
    /**
     * Clears both all loaded images and all registered images.
     */
    public static void clearAll() {
        queue.clear();
        tokenMap.clear();
        LoadImages2.clear();
    }
    
    /**
     * @param idName The id name of the sheet.
     * @return The width of the sheet, counted as the number of images.
     * 
     * @throws UnsupportedOperationException Iff corresponding image is unequal.
     */
    public static int getNumImgWidth(String idName) {
        Token token = tokenMap.get(idName);
        if (token instanceof EqualToken) {
            return ((EqualToken) token).getNumImgWidth();
            
        } else {
            throw new UnsupportedOperationException(
                    "Operation is not supported for unequal distributed images.");
        }
    }
    
    /**
     * @param idName The id name of the sheet.
     * @return The height of the sheet, counted as the number of images.
     * 
     * @throws UnsupportedOperationException Iff corresponding image is unequal.
     */
    public static int getNumImgHeight(String idName) {
        Token token = tokenMap.get(idName);
        if (token instanceof EqualToken) {
            return ((EqualToken) token).getNumImgHeight();
        } else {
            throw new UnsupportedOperationException(
                    "Operation is not supported for unequal distributed images.");
        }
    }
    
    /**
     * @param idName The id name of the sheet.
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
                        Delay delay = queue.poll(1, TimeUnit.DAYS);
                        if (delay == null) continue;
                        LoadImages2.removeImage(delay.getToken().getIDName());
                        
                    } catch (InterruptedException e) {
                        Logger.write(e);
                    }
                }
            }
        }.start();
    }
    
    
}
