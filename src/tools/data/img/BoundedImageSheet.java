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
import tools.log.Logger;


/**
 * Image sheet abstract implementation for bounded image sheets. <br>
 * Bounded image sheets have a width and height and requesting an image within
 * these bounds will never throw an {@link IndexOutOfBoundsException}.
 * This requirement does <b>NOT</b> hold for the {@link #getSheet()} function. <br>
 * Note that it is still allowed to return {@code null} for an image.
 * 
 * @version 2.0
 * @author Kaj Wortel
 */
public abstract class BoundedImageSheet
        extends ImageSheet {
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Returns the number of images which are in the width of the image sheet.
     * 
     * @return The width of the sheet, counted as the number of images,
     *     or {@code -1} if this function is not supported.
     */
    public abstract int getWidth();
    
    /**
     * Returns the number of images which are in the height of the image sheet.
     * 
     * @return The height of the sheet, counted as the number of images,
     *     or {@code -1} if this function is not supported.
     */
    public abstract int getHeight();
    
    /**
     * Returns the total number of images in this sheet.
     * 
     * @return The total number of images in this sheet.
     */
    public int size() {
        return getWidth() * getHeight();
    }
    
    @Override
    public Image get(int x, int y) {
        checkBounds(x, y);
        return super.get(x, y);
    }
    
    /**
     * Returns the {@code i}'th image of the row sheet. <br>
     * <br>
     * A row sheet is constructed by concatenating all rows of the original sheet. <br>
     * Example:<br>
     * Sheet indices:
     * <table border='1'>
     *   <tr><td> 00 </td><td> 01 </td><td> 02 </td><td> 03 </td></tr>
     *   <tr><td> 04 </td><td> 05 </td><td> 06 </td><td> 07 </td></tr>
     *   <tr><td> 08 </td><td> 09 </td><td> 10 </td><td> 11 </td></tr>
     * </table>
     * Then: <br>
     * {@code getFromRows(i) ==} image with index {@code i}.
     * 
     * @apiNote
     * This function only relays calls towards the {@link #get(int, int)} function.
     * 
     * @param i The index of the row image to return.
     * 
     * @return The {@code i}'th image from the row sheet.
     * 
     * @throws IndexOutOfBoundsException If the given index points are outside the allowed range.
     * 
     * @see #get(int, int)
     */
    public Image getFromRows(int i)
            throws IndexOutOfBoundsException {
        checkLinearBounds(i);
        int x = i % getWidth();
        int y = i / getWidth();
        return get(x, y);
    }
    
    /**
     * Returns the {@code i}'th image of the column sheet, scaled to the size of
     * {@code width} by {@code height} by using the given scale hints. <br>
     * <br>
     * A row sheet is constructed by concatenating all rows of the original sheet. <br>
     * Example:<br>
     * Sheet indices:
     * <table border='1'>
     *   <tr><td> 00 </td><td> 01 </td><td> 02 </td><td> 03 </td></tr>
     *   <tr><td> 04 </td><td> 05 </td><td> 06 </td><td> 07 </td></tr>
     *   <tr><td> 08 </td><td> 09 </td><td> 10 </td><td> 11 </td></tr>
     * </table>
     * Then:<br>
     * {@code getFromRows(i, w, h, sh) ==} image with index {@code i}, scaled to ({@code w x h})
     * using scale hints {@code sh}.
     * 
     * @apiNote
     * This function only relays calls towards the {@link #get(int, int, int, int, int)} function.
     * 
     * @param i The index of the row image to return.
     * @param width The width of the image to return. {@code -1} if the default width should be used.
     * @param height The height of the image to return. {@code -1} if the default height should be used.
     * @param scaleHints The scale hints used for scaling the image.
     * 
     * @return The {@code i}'th image from the row sheet with the given width and height,
     *     and scaled using the scale hints.
     * 
     * @throws IndexOutOfBoundsException If the given index points are outside the allowed range.
     * 
     * @see #get(int, int, int, int, int)
     */
    public Image getFromRows(int i, int width, int height, int scaleHints)
            throws IndexOutOfBoundsException {
        checkLinearBounds(i);
        int x = i % getWidth();
        int y = i / getWidth();
        return get(x, y, width, height, scaleHints);
    }
    
    /**
     * Draws the {@code i}'th image of row sheet on the given graphics object.
     * Also translates the image to the given position.
     * 
     * @apiNote
     * This function only relays calls towards the {@link #draw(Graphics2D, int, int, int, int)} function.
     * 
     * @param g2d The graphics object used to draw on.
     * @param i The linear row index of the image to draw.
     * @param posX The amount to translate the image in the x-axis.
     * @param posY The amount to translate the image in the y-axis.
     * 
     * @return {@code true} if anything was drawn on the graphics object. {@code false} otherwise.
     * 
     * @throws IndexOutOfBoundsException If the given index points are outside the allowed range.
     * 
     * @see #getFromRows(int) :&nbsp For more info about row sheets.
     */
    public boolean drawFromRow(Graphics2D g2d, int i, int posX, int posY)
            throws IndexOutOfBoundsException {
        checkLinearBounds(i);
        int x = i % getWidth();
        int y = i / getWidth();
       return draw(g2d, x, y, posX, posY);
    }
    
    /**
     * Draws the {@code i}'th image of row sheet on the given graphics object.
     * Also resizes and translates the image to the given size and position.
     * 
     * @apiNote
     * This function only relays calls towards the
     * {@link #draw(Graphics2D, int, int, int, int, int, int, int)} function.
     * 
     * @param g2d The graphics object used to draw on.
     * @param i The linear row index of the image to draw.
     * @param posX The amount to translate the image in the x-axis.
     * @param posY The amount to translate the image in the y-axis.
     * @param width The width of the image to draw.
     * @param height The height of the image to draw.
     * @param scaleHints The scale hints used for scaling the image.
     * 
     * @return {@code true} if anything was drawn on the graphics object. {@code false} otherwise.
     * 
     * @throws IndexOutOfBoundsException If the given index points are outside the allowed range.
     * 
     * @see #getFromRows(int, int, int, int) :&nbsp For more info about row sheets.
     */
    public boolean drawFromRow(Graphics2D g2d, int i, int posX, int posY,
            int width ,int height, int scaleHints)
            throws IndexOutOfBoundsException {
        checkWidthHeight(width, height);
        checkLinearBounds(i);
        int x = i % getWidth();
        int y = i / getWidth();
       return draw(g2d, x, y, posX, posY, width, height, scaleHints);
    }
    
    /**
     * Returns the {@code i}'th image of the column sheet. <br>
     * <br>
     * A column sheet is constructed by concatenating all columns of the original sheet. <br>
     * Example:<br>
     * Sheet indices:
     * <table border='1'>
     *   <tr><td> 00 </td><td> 03 </td><td> 06 </td><td> 09 </td></tr>
     *   <tr><td> 01 </td><td> 04 </td><td> 07 </td><td> 10 </td></tr>
     *   <tr><td> 02 </td><td> 05 </td><td> 08 </td><td> 11 </td></tr>
     * </table>
     * Then:<br>
     * {@code getFromCols(i) ==} image with index {@code i}.
     * 
     * @apiNote
     * This function only relays calls towards the {@link #get(int, int)} function.
     * 
     * @param i The index of the column image to return.
     * 
     * @return The {@code i}'th image from the column sheet.
     * 
     * @throws IndexOutOfBoundsException If the given index points are outside the allowed range.
     * 
     * @see #checkLinearBounds(int)
     * @see #get(int, int)
     */
    public Image getFromCols(int i)
            throws IndexOutOfBoundsException {
        checkLinearBounds(i);
        int x = i / getHeight();
        int y = i % getHeight();
        return get(x, y);
    }
    
    /**
     * Returns the {@code i}'th image of the column sheet, scaled to the size of
     * {@code width} by {@code height} by using the given scale hints. <br>
     * <br>
     * A column sheet is constructed by concatenating all columns of the original sheet. <br>
     * Example:<br>
     * Sheet indices:
     * <table border='1'>
     *   <tr><td> 00 </td><td> 03 </td><td> 06 </td><td> 09 </td></tr>
     *   <tr><td> 01 </td><td> 04 </td><td> 07 </td><td> 10 </td></tr>
     *   <tr><td> 02 </td><td> 05 </td><td> 08 </td><td> 11 </td></tr>
     * </table>
     * Then:<br>
     * {@code getFromCols(i, w, h, sh) ==} image at {@code i}, scaled to ({@code w x h})
     * using scale hints {@code sh}.
     * 
     * @apiNote
     * This function only relays calls towards the {@link #get(int, int, int, int, int)} function.
     * 
     * @param i The index of the column image to return.
     * @param width The width of the image to return. {@code -1} if the default width should be used.
     * @param height The height of the image to return. {@code -1} if the default height should be used.
     * @param scaleHints The scale hints used for scaling the image.
     * 
     * @return The {@code i}'th image from the column sheet with the given width and height,
     *     and scaled using the scale hints.
     * 
     * @throws IndexOutOfBoundsException If the given index points are outside the allowed range.
     * 
     * @see #checkLinearBounds(int)
     * @see #get(int, int, int, int, int)
     */
    public Image getFromCols(int i , int width, int height, int scaleHints)
            throws IndexOutOfBoundsException {
        checkLinearBounds(i);
        int x = i / getHeight();
        int y = i % getHeight();
        return get(x, y, width, height, scaleHints);
    }
    
    /**
     * Draws the {@code i}'th image of column sheet on the given graphics object.
     * Also translates the image to the given position.
     * 
     * @apiNote
     * This function only relays calls towards the {@link #draw(Graphics2D, int, int, int, int)} function.
     * 
     * @param g2d The graphics object used to draw on.
     * @param i The linear column index of the image to draw.
     * @param posX The amount to translate the image in the x-axis.
     * @param posY The amount to translate the image in the y-axis.
     * 
     * @return {@code true} if anything was drawn on the graphics object. {@code false} otherwise.
     * 
     * @throws IndexOutOfBoundsException If the given index points are outside the allowed range.
     * 
     * @see #getFromCols(int, int, int, int) :&nbsp For more info about column sheets.
     */
    public boolean drawFromCols(Graphics2D g2d, int i, int posX, int posY)
            throws IndexOutOfBoundsException {
        checkLinearBounds(i);
        int x = i / getHeight();
        int y = i % getHeight();
       return draw(g2d, x, y, posX, posY);
    }
    
    /**
     * Draws the {@code i}'th image of column sheet on the given graphics object.
     * Also resizes and translates the image to the given size and position.
     * 
     * @apiNote
     * This function only relays calls towards the
     * {@link #draw(Graphics2D, int, int, int, int, int, int, int)} function.
     * 
     * @param g2d The graphics object used to draw on.
     * @param i The linear column index of the image to draw.
     * @param posX The amount to translate the image in the x-axis.
     * @param posY The amount to translate the image in the y-axis.
     * @param width The width of the image to draw.
     * @param height The height of the image to draw.
     * @param scaleHints The scale hints used for scaling the image.
     * 
     * @return {@code true} if anything was drawn on the graphics object. {@code false} otherwise.
     * 
     * @throws IndexOutOfBoundsException If the given index points are outside the allowed range.
     * 
     * @see #getFromCols(int, int, int, int) :&nbsp For more info about column sheets.
     */
    public boolean drawFromCols(Graphics2D g2d, int i, int posX, int posY,
            int width ,int height, int scaleHints)
            throws IndexOutOfBoundsException {
        checkWidthHeight(width, height);
        checkLinearBounds(i);
        int x = i / getHeight();
        int y = i % getHeight();
       return draw(g2d, x, y, posX, posY, width, height, scaleHints);
    }
    
    /**
     * Checks whether the given value is a valid linear index for image sheet.
     * 
     * @param i The linear index to check.
     * 
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     */
    protected final void checkLinearBounds(int i) {
        if (0 <= i && i < size()) {
            throw new IndexOutOfBoundsException(
                    "Index was out of bounds! Expected: 0 <= index(" + i
                            + ") <  #img(" + size() + ").");
        }
    }
    
    /**
     * Checks whether the given values are valid indices for this image sheet.
     * 
     * @param x The x-coord to check.
     * @param y The y-coord to check.
     * 
     * @throws IndexOutOfBoundsException If at least one of the indices is out of bounds.
     */
    protected final void checkBounds(int x, int y) {
        if (x < 0 || getWidth() <= x) {
            throw new IndexOutOfBoundsException(
                    "Index was out of bounds! Expected: 0 <= x(" + x
                            + ") < width(" + getWidth() + ").");
        }
        if (y < 0 || getHeight() <= y) {
            throw new IndexOutOfBoundsException(
                    "Index was out of bounds! Expected: 0 <= y(" + y
                            + ") < height(" + getHeight() + ").");
        }
    }
    
    @Override
    public boolean canAccess(int x, int y) {
        return (0 <= x || x < getWidth()) ||
                (0 <= y || y < getHeight());
    }
    
    /**
     * Creates a GUI image sheet based on this image sheet. <br>
     * The created GUI sheet will only return this sheet for all states.
     * 
     * @return A GUI image sheet based on this image sheet.
     */
    public GUIImageSheet asGUIImageSheet() {
        return new SingleGUIImageSheet(this);
    }
    
    
}
