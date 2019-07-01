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

package tools.io;


// Tools imports
import tools.Var;


// Java imports
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;
import java.util.Map;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;


/**TODO
 * Provides an easy, time efficient and storage efficient way for retrieving
 * image sheets that are used at multiple threads at the same time.
 * 
 * Is thread safe.
 * 
 * @author Kaj Wortel
 */
public class LoadImages2 {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The map the images will be stored in. */
    final public static ConcurrentHashMap<String, BufferedImage[][]> images
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
    @Deprecated
    private LoadImages2() { }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    // Wrapper functions
    
    // (0) Using {@code new File(fileName)} as file
    //     (1) Using {@code fileName} as name
    public static BufferedImage[][] loadImage(String fileName)
            throws IllegalArgumentException, IOException {
        return loadImage(new File(fileName), fileName,
                0, 0,
                -1, -1,
                -1, -1);
    }
    
    public static BufferedImage[][] loadImage(String fileName,
            int width, int height)
            throws IllegalArgumentException, IOException {
        return loadImage(new File(fileName), fileName,
                0, 0,
                -1, -1,
                width, height);
    }
    
    public static BufferedImage[][] loadImage(String fileName,
            int startX, int startY,
            int endX, int endY,
            int width, int height)
            throws IllegalArgumentException, IOException {
        return loadImage(new File(fileName), fileName,
                startX, startY,
                endX, endY,
                width, height);
    }
    
    //     (1) Using {@code name} as name
    public static BufferedImage[][] loadImage(String fileName, String name)
            throws IllegalArgumentException, IOException {
        return loadImage(new File(fileName), name,
                0, 0,
                -1, -1,-1, -1);
    }
    
    public static BufferedImage[][] loadImage(String fileName, String name,
            int width, int height)
            throws IllegalArgumentException, IOException {
        return loadImage(new File(fileName), name,
                0, 0,
                -1, -1,
                width, height);
    }
    
    public static BufferedImage[][] loadImage(String fileName, String name,
            int startX, int startY,
            int endX, int endY,
            int width, int height)
            throws IllegalArgumentException, IOException {
        return loadImage(new File(fileName), name,
                startX, startY,
                endX, endY,
                width, height);
    }
    
    
    // (0) Using {@code file} as file
    //     (1) Using {@code file.getPaht()} as name
    public static BufferedImage[][] loadImage(File file)
            throws IllegalArgumentException, IOException {
        return loadImage(file, file.getPath(),
                0, 0,
                -1, -1,
                -1, -1);
    }
    
    public static BufferedImage[][] loadImage(File file,
            int width, int height)
            throws IllegalArgumentException, IOException {
        return loadImage(file, file.getPath(),
                0, 0,
                -1, -1,
                width, height);
    }
    
    public static BufferedImage[][] loadImage(File file,
            int startX, int startY,
            int endX, int endY,
            int width, int height)
            throws IllegalArgumentException, IOException {
        return loadImage(file, file.getPath(),
                startX, startY,
                endX, endY,
                width, height);
    }
    
    //     (1) Using {@code name} as name
    public static BufferedImage[][] loadImage(File file, String name)
            throws IllegalArgumentException, IOException {
        return loadImage(file, name,
                0, 0,
                -1, -1,
                -1, -1);
    }
    
    public static BufferedImage[][] loadImage(File file, String name,
            int width, int height)
            throws IllegalArgumentException, IOException {
        return loadImage(file, name,
                0, 0,
                -1, -1,
                width, height);
    }
    
    // Full function
    /**
     * Loads an image from a given file(-name) and converts it into
     * separate pieces. The images are created from {@code startX, startY}
     * (inclusive) till {@code endX, endY} (exclusive).<br>
     * Each image in this range has size (width, height).<br>
     * <br>
     * If any of {@code endX} or {@code endY} equals {@code -1},
     * then this coordinate will be changed to the maximum size of the image
     * ({@code width} for {@code endX}, {@code height} for {@code endY}).
     * If {@code width} or {@code height} equals {@code -1}, then these will be changed
     * to {@code endX - startX} and {@code endY - startY} respectively.
     * 
     * @param fileName The path of the image file.
     * @param file Image file.
     * @param name The name of the image for later retrieval.
     * @param startX The pixel x-coordinate of the start location in the image (incl.).
     * @param startY The pixel y-coordinate of the start location in the image (incl.).
     * @param endX The pixel x-coordinate of the end location of the image (excl.).
     * @param endY The pixel y-coordinate of the end location of the image (excl.).
     * @param width The width of each sub-image.
     * @param height The height of each sub-image.
     * 
     * @return A 2D BufferedImage array, with the images loaded such that
     *     {@code bi[x][y]} will retrieve the image part at {@code (x, y)}
     *     from the selected part (starting at {@code (0, 0)}).
     * 
     * @throws IOException Iff the file does not exist or is not accessable.
     * @throws IllegalArgumentException Iff:
     *     <ul>
     *       <li> {@code startX >= endX} or {@code startY >= endY}</li>
     *       <li> {@code (endX - startX) % width != 0} or {@code (endY - startY) % height != 0}</li>
     *       <li> {@code endX < the width of the image} or {@code endY < the height of the image}</li>
     *     </ul>
     */
    public static BufferedImage[][] loadImage(File file, String name,
            int startX, int startY,
            int endX, int endY,
            int width, int height)
            throws IllegalArgumentException, IOException {
        BufferedImage[][] newImg;
        
        // Check if the store name is correct
        if (images.contains(name))
            throw new IllegalArgumentException(
                    "Name was already used: \"" + name + "\"");
        
        // Read the image
        BufferedImage bigImg;
        try {
            bigImg = ImageIO.read(file);
            
        } catch (IIOException e) {
            throw new IOException(
                    "File \""+ file.toPath()
                            + "\" does not exist or is not accessable.");
        }
        
        // Check if the end coords are correct
        if (endX == -1) {
            endX = bigImg.getWidth();
            
        } else if (endX > bigImg.getWidth()) {
            throw new IllegalArgumentException(
                    "Given end x was larger then the image. end x = "
                            + endX + ", img width: " + bigImg.getWidth());
        }
        
        if (endY == -1 ) {
            endY = bigImg.getHeight();
            
        } else if (endY > bigImg.getHeight()) {
            throw new IllegalArgumentException(
                    "Given end y was larger then the image. end y = "
                            + endY + ", img height: " + bigImg.getHeight());
        }
        
        // Check if the end-coords are bigger then the starting coords.
        if (startX >= endX)
            throw new IllegalArgumentException(
                    "Starting x coord >= end x coord: " 
                            + startX + " >= " + endX);
        if (startY >= endY)
            throw new IllegalArgumentException(
                    "Starting y coord >= end y coord: "
                            + startY + " >= " + endY);
        
        int dX = endX - startX;
        int dY = endY - startY;
        
        // Check if the image sizes are valid
        if (width == -1) {
            width = endX - startX;
            
        } else if (dX % width != 0) {
            throw new IllegalArgumentException(
                    "Given width (" + width + "), startX (" + startX
                            + ") and/or endX (" + endX + ") is invallid");
        }
        
        if (height == -1) {
            height = endY - startY;
            
        } else if (dY % height != 0) {
            throw new IllegalArgumentException(
                    "Given width (" + height + "), startY (" + startY
                            + ") and/or endY (" + endY + ") is invallid");
        }
        
        // Split the image into parts
        newImg = new BufferedImage[dX / width][dY / height];
        for (int i = startX ; i < endX; i += width) {
            for (int j = startY ; j < endY; j += height) {
                newImg[(i - startX) / width][(j - startY) / height]
                    = bigImg.getSubimage(i, j, width, height);
            }
        }
        
        images.put(name, newImg);
        return newImg;
    }
    public static BufferedImage[][] loadImage(File file, Rectangle[][] locs)
            throws IllegalArgumentException, IOException {
        return loadImage(file, file.getName(), locs);
    }
    
    public static BufferedImage[][] loadImage(String fileName,
            Rectangle[][] locs)
            throws IllegalArgumentException, IOException {
        return loadImage(fileName, fileName, locs);
    }
    
    public static BufferedImage[][] loadImage(String fileName, String name,
            Rectangle[][] locs)
            throws IllegalArgumentException, IOException {
        return loadImage(new File(fileName), name, locs);
    }
    
    
    /**
     * Loads an image from a given file(-name) and converts it into
     * separate pieces.
     * 
     * @param fileName The path of the image file.
     * @param file Image file.
     * @param name The name of the image for later retrieval.
     * @param locs The locations and sizes of the images to load.
     *     The image stored at the indices of the returned image 2D array
     *     match the corresponding indices of this array.
     * @return a 2D BufferedImage array, with the images loaded such that
     *     they match the indices of {@code locs}.
     * 
     * @throws IOException iff the file does not exist or is not accessable.
     * @throws IllegalArgumentException iff at least one of the provided
     *     rectangles is outside the main image.
     */
    public static BufferedImage[][] loadImage(File file, String name,
            Rectangle[][] locs)
            throws IllegalArgumentException, IOException {
        if (locs == null)
            throw new NullPointerException("Expected non-null rectangles!");
        
        BufferedImage[][] newImg;
        
        // Check if the store name is correct
        if (images.contains(name))
            throw new IllegalArgumentException(
                    "Name was already used: \"" + name + "\"");
        
        // Read the image
        BufferedImage bigImg;
        try {
            bigImg = ImageIO.read(file);
            
        } catch (IIOException e) {
            throw new IOException(
                    "File \""+ file.toPath()
                            + "\" does not exist or is not accessable.");
        }
        
        newImg = new BufferedImage[locs.length][];
        
        Rectangle bounds = new Rectangle(bigImg.getWidth(), bigImg.getHeight());
        for (int i = 0; i < locs.length; i++) {
            if (locs[i] == null) continue;
            newImg[i] = new BufferedImage[locs[i].length];
            
            for (int j = 0; j < locs[i].length; j++) {
                Rectangle rec = locs[i][j];
                if (rec == null) continue;
                
                if (!bounds.contains(rec)) {
                    throw new IllegalArgumentException
        ("Rectangle didn't fit in the image size." + Var.LS
                + "Size=" + bounds.width + "x" + bounds.height + "." + Var.LS
                + "Given rectangle: " + rec.width + "x" + rec.height
                + " @ (" + rec.x+ "," + rec.y + ")");
                }
                
               newImg[i][j] = bigImg
                       .getSubimage(rec.x, rec.y, rec.width, rec.height);
            }
        }
        
        images.put(name, newImg);
        return newImg;
    }
    
    // (0) Using {@code new File(fileName)} as file
    //     (1) Using {@code filename} as name
    public static BufferedImage[][] ensureLoadedAndGetImage(String fileName)
            throws IllegalArgumentException, IOException {
        return ensureLoadedAndGetImage(new File(fileName), fileName,
                0, 0,
                -1, -1,
                -1, -1);
    }
    
    public static BufferedImage[][] ensureLoadedAndGetImage(
            String fileName,
            int width, int height)
            throws IllegalArgumentException, IOException {
        return ensureLoadedAndGetImage(new File(fileName), fileName,
                0, 0,
                -1, -1,
                width, height);
    }
    
    public static BufferedImage[][] ensureLoadedAndGetImage(
            String fileName,
            int startX, int startY,
            int endX, int endY,
            int width, int height)
            throws IllegalArgumentException, IOException {
        return ensureLoadedAndGetImage(new File(fileName), fileName,
                startX, startY,
                endX, endY,
                width, height);
    }
    
    //     (1) Using {@code name} as name
    public static BufferedImage[][] ensureLoadedAndGetImage(
            String fileName, String name)
            throws IllegalArgumentException, IOException {
        return ensureLoadedAndGetImage(new File(fileName), name,
                0, 0,
                -1, -1,
                -1, -1);
    }
    
    public static BufferedImage[][] ensureLoadedAndGetImage(
            String fileName, String name,
            int width, int height)
            throws IllegalArgumentException, IOException {
        return ensureLoadedAndGetImage(new File(fileName), name,
                0, 0,
                -1, -1,
                width, height);
    }
    
    public static BufferedImage[][] ensureLoadedAndGetImage(
            String fileName, String name,
            int startX, int startY,
            int endX, int endY,
            int width, int height)
            throws IllegalArgumentException, IOException {
        return ensureLoadedAndGetImage(new File(fileName), name,
                startX, startY,
                endX, endY,
                width, height);
    }
    
    
    // (0) Using {@code file} as file
    //     (1) Using {@code file.getpath()} as name
    public static BufferedImage[][] ensureLoadedAndGetImage(File file)
            throws IllegalArgumentException, IOException {
        return ensureLoadedAndGetImage(file, file.getPath(),
                0, 0,
                -1, -1,
                -1, -1);
    }
    
    public static BufferedImage[][] ensureLoadedAndGetImage(
            File file,
            int width, int height)
            throws IllegalArgumentException, IOException {
        return ensureLoadedAndGetImage(file, file.getPath(),
                0, 0,
                -1, -1,
                width, height);
    }
    
    public static BufferedImage[][] ensureLoadedAndGetImage(
            File file,
            int startX, int startY,
            int endX, int endY,
            int width, int height)
            throws IllegalArgumentException, IOException {
        return ensureLoadedAndGetImage(file, file.getPath(),
                startX, startY,
                endX, endY,
                width, height);
    }
    
    //     (1) Using {@code name} as name
    public static BufferedImage[][] ensureLoadedAndGetImage(
            File file, String name)
            throws IllegalArgumentException, IOException {
        return ensureLoadedAndGetImage(file, name,
                0, 0,
                -1, -1,
                -1, -1);
    }
    
    public static BufferedImage[][] ensureLoadedAndGetImage(
            File file, String name,
            int width, int height)
            throws IllegalArgumentException, IOException {
        return ensureLoadedAndGetImage(file, name,
                0, 0,
                -1, -1,
                width, height);
    }
    
    // Full function
    /**
     * Check if the current entry already exists. If it doesn't,
     * create it with the given settings.
     * 
     * For more info, see {@link #loadImage(File, String, int, int, int,
     * int, int, int)}.
     * parameters.
     * 
     * @return the image array described by the parameters.
     */
    public static BufferedImage[][] ensureLoadedAndGetImage(
            File file, String name,
            int startX, int startY,
            int endX, int endY,
            int width, int height)
            throws IllegalArgumentException, IOException {
        if (images.containsKey(name)) {
            return images.get(name);
            
        } else {
            return loadImage(file, name,
                    startX, startY,
                    endX, endY,
                    width, height);
        }
    }
    
    public static BufferedImage[][] ensureLoadedAndGetImage(File file,
            Rectangle[][] locs)
            throws IllegalArgumentException, IOException {
        return ensureLoadedAndGetImage(file, file.getName(), locs);
    }
    
    public static BufferedImage[][] ensureLoadedAndGetImage(String fileName,
            Rectangle[][] locs)
            throws IllegalArgumentException, IOException {
        return ensureLoadedAndGetImage(fileName, fileName, locs);
    }
    
    public static BufferedImage[][] ensureLoadedAndGetImage(String fileName,
            String name, Rectangle[][] locs)
            throws IllegalArgumentException, IOException {
        return ensureLoadedAndGetImage(new File(fileName), name, locs);
    }
    
    /**
     * Check if the current entry already exists. If it doesn't,
     * create it with the given settings.
     * 
     * For more info, see {@link #loadImage(File, String, Rectangle[][])}.
     * 
     * @return the image array described by the parameters.
     */
    public static BufferedImage[][] ensureLoadedAndGetImage(
            File file, String name, Rectangle[][] locs)
            throws IllegalArgumentException, IOException {
        if (images.containsKey(name)) {
            return images.get(name);
            
        } else {
            return loadImage(file, name, locs);
        }
    }
    
    /**
     * Returns an earlier stored image array.
     * 
     * @param name the name of the image that will be retrieved.
     * @return the image stored at "name", if it exists. null otherwise.
     */
    public static BufferedImage[][] getImage(String name) {
        return images.get(name);
    }
    
    /**
     * Returns an earlier stored image.
     * 
     * @param name the name of the image that will be retrieved.
     * @return the image stored at "name", if it exists.
     * @throws NoSuchFieldException if this image does not exist.
     */
    public static BufferedImage[][] getImageException(String name)
            throws NoSuchFieldException {
        if (images.containsKey(name)) {
            return getImage(name);
            
        } else {
            throw new NoSuchFieldException(
                    "Image \"" + name + "\" does not exist.");
        }
    }
    
    /**
     * Removes a stored image.
     * 
     * @param name the name of the image that will be retrieved.
     * @return true iff the image was in the hashtable and removed.
     */
    public static BufferedImage[][] removeImage(String name) {
        return images.remove(name);
    }
    
    /**
     * Removes all stored images.
     */
    public static void clear() {
        images.clear();
    }
    
    /**
     * @return an iterator over the images table.
     */
    public static Iterator<Map.Entry<String, BufferedImage[][]>> iterator() {
        return images.entrySet().iterator();
    }
    
    
}


