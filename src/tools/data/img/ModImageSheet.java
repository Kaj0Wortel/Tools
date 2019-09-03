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
import tools.data.array.Array2D;


// Tools imports


/**
 * 
 * @todo
 * - Add overrides for draw and get.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class ModImageSheet
        extends BoundedImageSheet {
     
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** Array containing all images and the relative coordinates to their images. */
    private final Array2D<Item> imgs;
    
    
    /* -------------------------------------------------------------------------
     * Inner-classes.
     * -------------------------------------------------------------------------
     */
    /**
     * Item class for keeping track of the different images on a sheet.
     */
    private static class Item {
        
        /* --------------------
         * Variables.
         * --------------------
         */
        /** The sheet containing the image. */
        private ImageSheet sheet;
        /** The x-coord to get from the sheet. */
        private int x;
        /** The y-coord to get from the sheet. */
        private int y;
        
        
        /* --------------------
         * Constructors.
         * --------------------
         */
        /**
         * Creates an item which contains a sheet and a reference to an image.
         * 
         * @param sheet The sheet containing the image. 
         * @param x The x-coord to get from the sheet.
         * @param y The y-coord to get from the sheet.
         */
        public Item(ImageSheet sheet, int x, int y) {
            this.sheet = sheet;
            this.x = x;
            this.y = y;
        }
        
        
    }
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new modifiable image sheet with the given width and height.
     * 
     * @param width The width of the sheet.
     * @param height The height of the sheet.
     */
    public ModImageSheet(int width, int height) {
        imgs = new Array2D<>(width, height, Item.class);
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public int getWidth() {
        return imgs.getWidth();
    }

    @Override
    public int getHeight() {
        return imgs.getHeight();
    }
    
    /**
     * Sets the size of the sheet and removes all currently added images.
     * 
     * @param width The new width.
     * @param height The new height.
     */
    public void setSize(int width, int height) {
        imgs.setSize(width, height);
    }
    
    /**
     * Sets the size of the sheet, but keeps all currently added images
     * which are inside the bounds of the given bounds.
     * 
     * @param width The new width.
     * @param height The new height.
     */
    public void resize(int width, int height) {
        imgs.setSizeAndCopy(width, height);
    }
    
    /**
     * Sets the given image for the given coordinates.
     * 
     * @param x The x-coordinate for the image.
     * @param y The y-coordinate for the image.
     * @param img The image to be added.
     * 
     * @throws IndexOutOfBoundsException If the given coordinates are out of bound.
     * 
     * @see #setImage(int, int, int, int, ImageSheet)
     */
    public void setImage(int x, int y, Image img)
            throws IndexOutOfBoundsException {
        setImage(x, y, 0, 0, new SourceImageSheet(new Image[][] {new Image[] {img}}));
    }
    
    /**
     * Sets the given image sheet for the given coordinates.
     * 
     * @param x The x-coordinate for the image sheet.
     * @param y The y-coordinate for the image sheet.
     * @param sourceX The x-coordinate for the image inside the provided image sheet.
     * @param sourceY The y-coordinate for the image inside the provided image sheet.
     * @param sheet The image sheet to be added.
     * 
     * @throws IndexOutOfBoundsException If the given coordinates are out of bound.
     * 
     * @see #setImage(int, int, int, int, ImageSheet)
     */
    public void setImage(int x, int y, int sourceX, int sourceY, ImageSheet sheet)
            throws IndexOutOfBoundsException {
        checkBounds(x, y);
        imgs.set(x, y, new Item(sheet, sourceX, sourceY));
    }
    
    /**
     * Sets the given image sheet for all value inside the given bounds. <br>
     * The width and height are allowed to be zero and negative.
     * If zero, then simply no arguments are set.
     * If negative, then all images are placed in backwards order.
     * 
     * <h2>Examples</h2>
     * Assume we have an image sheet with the following images (represented as numbers):
     * <pre>{@code
     * ImageSheet source = [1, 2, 3],
     *                     [4, 5, 6],
     *                     [7, 8, 9]}</pre>
     * and an instance {@code modSheet} of this class with a width and height of 3.
     * Then:
     * <table border='1'>
     *   <tr><th>Code</th>
     *       <th>Result</th></tr>
     *   <tr><td>{@code modSheet.setRange(0, 0, 0, 0, 3, 3, source)}</td>
     *       <td><pre>[1, 2, 3]<br>[4, 5, 6]<br>[7, 8, 9]</pre></td></tr>
     *   <tr><td>{@code modSheet.setRange(1, 1, 0, 0, 2, 2, source)}</td>
     *       <td><pre>[null, null, null]<br>[null,    1,    2]<br>[null,    4,    5]</pre></td></tr>
     *   <tr><td>{@code modSheet.setRange(2, 2, 0, 0, -3, -3, source)}</td>
     *       <td><pre>[9, 8, 7]<br>[6, 5, 4]<br>[3, 2, 1]</pre></td></tr>
     *   <tr><td>{@code modSheet.setRange(1, 1, 1, 1, -2, 2, source)}</td>
     *       <td><pre>[null, null, null]<br>[   6,    5, null]<br>[   8,    9, null]</pre></td></tr>
     * </table>
     * 
     * @param x The x-coordinate for the image sheet.
     * @param y The y-coordinate for the image sheet.
     * @param sourceX The x-coordinate for the image inside the provided image sheet.
     * @param sourceY The y-coordinate for the image inside the provided image sheet.
     * @param width The width of the area to add.
     * @param height The height of the area to add. Can be zero or negative.
     * @param sheet The image sheet to be added. Can be zero or negative.
     * 
     * @throws IndexOutOfBoundsException If the given coordinates are out of bound.
     */
    public void setRange(int x, int y, int sourceX, int sourceY, int width, int height,
            ImageSheet sheet)
            throws IndexOutOfBoundsException {
        checkBounds(x, y);
        checkBounds(x + width, y + height);
        Item[][] items = new Item[width][height];
        for (int i = 0; i < Math.abs(width); i++) {
            for (int j = 0; j < Math.abs(height); j++) {
            int si = (width < 0 ? -i : i);
            int sj = (height < 0 ? -j : j);
                items[x + si][y + sj] = new Item(sheet, sourceX + si, sourceY + sj);
            }
        }
        imgs.setArray(items, 0, 0, x, y, width, height);
    }
    
    @Override
    public Image get(int x, int y) {
        Item item = imgs.get(x, y);
        if (item == null) return null;
        return item.sheet.get(item.x, item.y);
    }
    
    @Override
    public Image get(int x, int y, int width, int height, int scaleHints) {
        Item item = imgs.get(x, y);
        if (item == null) return null;
        return item.sheet.get(item.x, item.y, width, height, scaleHints);
    }
    
    @Override
    public boolean draw(Graphics2D g2d, int x, int y, int posX, int posY) {
        Item item = imgs.get(x, y);
        if (item == null) return false;
        return item.sheet.draw(g2d, item.x, item.y, posX, posY);
    }
    
    @Override
    public boolean draw(Graphics2D g2d, int x, int y, int posX, int posY,
            int width, int height, int scaleHints) {
        Item item = imgs.get(x, y);
        if (item == null) return false;
        return item.sheet.draw(g2d, item.x, item.y, posX, posY, width, height, scaleHints);
    }
    
    @Override
    public Image[][] getSheet() {
        Image[][] result = new Image[imgs.getWidth()][imgs.getHeight()];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                result[i][j] = get(i, j);
            }
        }
        return result;
    }
    
    
}
