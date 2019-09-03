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


// Tools imports
import tools.MultiTool;
import tools.data.array.ReadOnlyArray;


/**
 * Image sheet which adds the images on top of each other. <br>
 * The first image in the array is drawn first, hence it will appear
 * on the bottom layer of the image.
 * 
 * @version 2.1
 * @author Kaj Wortel
 */
public class OverlayImageSheet
        extends BoundedImageSheet {
     
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The width of the sheetArr. */
    private final int sheetWidth;
    /** The height of the sheetArr. */
    private final int sheetHeight;
    /** The read-only array variant of the image sheetArr. */
    private ReadOnlyArray<BoundedImageSheet> sheetArr;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new image sheet with the given images. <br>
     * Overlays the image sheet from the lower index towards the higher index.
     * 
     * @implNote
     * The images should be given in the following format: <br>
     * {@code imgs[i][x][y]} <br>
     * where {@code i} denotes the different sheets, and {@code x} and {@code y}
     * denote the coordinates in the sheets.
     * 
     * @param sheetWidth The width of the sheet.
     * @param sheetHeight The height of the  sheet.
     * @param imgs The images to be processed.
     * 
     * @throws IndexOutOfBoundsException If the given indices {@code x} and {@code y}
     *     are out of bounds.
     */
    public OverlayImageSheet(int sheetWidth, int sheetHeight, Image[][]... imgs) {
        this(sheetWidth, sheetHeight, MultiTool.createObject(() -> {
            BoundedImageSheet[] bis = new BoundedImageSheet[imgs.length];
            for (int i = 0; i < imgs.length; i++) {
                bis[i] = new SourceImageSheet(imgs[i]);
            }
            return bis;
        }));
    }
    
    /**
     * Creates a new image sheet with the given image sheets. <br>
     * Overlays the image sheet from the lower index towards the higher index.
     * 
     * @param sheetWidth The number of images which are in the width of the sheet.
     * @param sheetHeight The number of images which are in the height of the sheet.
     * @param sheets The image sheet to be processed.
     */
    public OverlayImageSheet(int sheetWidth, int sheetHeight, BoundedImageSheet... sheets) {
        if (sheets == null) throw new NullPointerException();
        if (sheetWidth < 0 || sheetHeight < 0) {
            throw new IllegalArgumentException(
                    "Expected a non-zero and non-negative width and height, but found ("
                            + sheetWidth + ", " + sheetHeight + ")");
        }
        this.sheetWidth = sheetWidth;
        this.sheetHeight = sheetHeight;
        setSheets(sheets);
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * @return The overlaying sheets.
     */
    public ReadOnlyArray<BoundedImageSheet> getSheets() {
        return new ReadOnlyArray<>(sheetArr);
    }
    
    /**
     * Sets the given sheets as sheets to overlay. <br>
     * Overlays the image sheet from the lower index towards the higher index.
     * 
     * @param sheets The new sheets.
     */
    public void setSheets(BoundedImageSheet... sheets) {
        sheetArr = new ReadOnlyArray<>(sheets);
        for (BoundedImageSheet sheet : sheets) {
            if (sheet.getWidth() < sheetWidth || sheet.getHeight() < sheetHeight) {
                throw new IllegalArgumentException(
                        "Expected all sheets to have at least the given width(" + sheetWidth
                                + ") and height(" + sheetHeight + "), but found a sheet with size ("
                                + sheet.getWidth() + " x " + sheet.getHeight() + ").");
            }
        }
    }
    
    @Override
    public Image[][] getSheet() {
        if (sheetWidth < 0 || sheetHeight < 0) return null;
        Image[][] imgSheet = new Image[sheetWidth][sheetHeight];
        for (int x = 0; x < imgSheet.length; x++) {
            for (int y = 0; y < imgSheet[0].length; y++) {
                imgSheet[x][y] = get(x, y, -1, -1, 0);
            }
        }
        return imgSheet;
    }
    
    @Override
    public Image get(int x, int y)
            throws IndexOutOfBoundsException {
        return get(x, y, 0, 0, 0);
    }
    
    @Override
    public Image get(int x, int y, int width, int height, int scaleHints)
            throws ArrayIndexOutOfBoundsException {
        checkWidthHeight(width, height);
        checkBounds(x, y);
        if (width > 0 && height > 0) {
            BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2d = result.createGraphics();
            try {
                draw(g2d, x, y, 0, 0, width, height, scaleHints);
                
            } finally {
                g2d.dispose();
                return result;
            }
            
        } else {
            Image[] imgs = new Image[sheetArr.length()];
            int maxWidth = 1;
            int maxHeight = 1;
            for (int i = 0; i < imgs.length; i++) {
                imgs[i] = sheetArr.get(i).get(x, y);
                if (imgs[i] != null) {
                    maxWidth = Math.max(maxWidth, imgs[i].getWidth(null));
                    maxHeight = Math.max(maxHeight, imgs[i].getHeight(null));
                }
            }
            if (width <= 0) width = maxWidth;
            if (height <= 0) height = maxHeight;
            
            BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2d = result.createGraphics();
            try {
                for (int i = 0; i < imgs.length; i++) {
                    g2d.drawImage(imgs[i], 0, 0, null);
                }
                
            } finally {
                g2d.dispose();
                return result;
            }
        }
    }
    
    @Override
    public boolean draw(Graphics2D g2d, int x, int y, int posX, int posY)
            throws IndexOutOfBoundsException {
        return draw(g2d, x, y, posX, posY, 0, 0, 0);
    }
    
    @Override
    public boolean draw(Graphics2D g2d, int x, int y, int posX, int posY,
            int width, int height, int scaleHints)
            throws IndexOutOfBoundsException {
        checkWidthHeight(width, height);
        checkBounds(x, y);
        boolean drawn = false;
        for (int i = 0; i < sheetArr.length(); i++) {
            drawn = sheetArr.get(i).draw(g2d, x, y, posX, posY, width, height, scaleHints) || drawn;
        }
        return drawn;
    }
    
    @Override
    public int getWidth() {
        return sheetWidth;
    }
    
    @Override
    public int getHeight() {
        return sheetHeight;
    }
    
    
}
