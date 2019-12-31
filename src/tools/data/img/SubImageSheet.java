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
 * Bounded image sheet class which creates a translated sub-view from another image sheet.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class SubImageSheet
        extends BoundedImageSheet {
     
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The source sheet to get all images from. */
    private final ImageSheet sheet;
    /** The amount translated in the x-axis. */
    private int transX;
    /** The amount translated in the y-axis. */
    private int transY;
    /** The width of the sub sheet. */
    private int sheetWidth;
    /** The height of the sub sheet. */
    private int sheetHeight;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new image sheet which uses a translated view of the given image sheet.
     * The sub-image has the given width and height and the underlaying images
     * are directly fetched from the given sheet.
     * 
     * @param sheet The original image sheet to get all images from.
     * @param sheetWidth The width of the sheet (in images).
     * @param sheetHeight The height of the sheet (in images).
     * 
     * @see #SubImageSheet(tools.data.img.ImageSheet, int, int, int, int)
     */
    public SubImageSheet(ImageSheet sheet, int sheetWidth, int sheetHeight) {
        this(sheet, 0, 0, sheetWidth, sheetHeight);
    }
    
    /**
     * Creates a new image sheet which uses a translated view of the given image sheet.
     * The sub-image has the given width and height and the underlaying images
     * are directly fetched from the given sheet. Moreover, the indices are translated
     * by the given translations.
     * 
     * @param sheet The original image sheet to get all images from.
     * @param transX The amount to translate the original image sheet in the x-axis (in images).
     * @param transY The amount to translate the original image sheet in the y-axis (in images).
     * @param sheetWidth The width of the sheet (in images).
     * @param sheetHeight The height of the sheet (in images).
     * 
     * @see #setSize(int, int)
     * @see #setTranslate(int, int)
     */
    public SubImageSheet(ImageSheet sheet, int transX, int transY, int sheetWidth, int sheetHeight) {
        if ((this.sheet = sheet) == null) throw new NullPointerException();
        this.sheetWidth = sheetWidth;
        this.sheetHeight = sheetHeight;
        setSize(sheetWidth, sheetHeight);
        setTranslate(transX, transY);
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * @return The amount the image sheet is translated towards the x-axis.
     */
    public int getTranslateX() {
        return transX;
    }
    
    /**
     * @return The amount the image sheet is translated towards the y-axis.
     */
    public int getTranslateY() {
        return transY;
    }
    
    @Override
    public int getWidth() {
        return sheetWidth;
    }
    
    @Override
    public int getHeight() {
        return sheetHeight;
    }
    
    @Override
    public Image get(int x, int y) {
        checkBounds(x, y);
        int newX = x + transX;
        int newY = y + transY;
        if (!sheet.canAccess(newX, newY)) return null;
        return sheet.get(newX, newY);
    }
    
    @Override
    public Image get(int x, int y, int width, int height, int scaleHints) {
        checkWidthHeight(width, height);
        checkBounds(x, y);
        int newX = x + transX;
        int newY = y + transY;
        if (!sheet.canAccess(newX, newY)) return null;
        return sheet.get(newX, newY, width, height, scaleHints);
    }
    
    @Override
    public Image[][] getSheet() {
        return sheet.getSheet();
    }
    
    @Override
    public boolean draw(Graphics2D g2d, int x, int y, int posX, int posY)
            throws IndexOutOfBoundsException {
        checkBounds(x, y);
        int newX = x + transX;
        int newY = y + transY;
        if (!sheet.canAccess(newX, newY)) return false;
        sheet.draw(g2d, newX, newY, posX, posY);
        return true;
    }
    
    @Override
    public boolean draw(Graphics2D g2d, int x, int y, int posX, int posY,
            int width, int height, int scaleHints)
            throws IndexOutOfBoundsException {
        checkWidthHeight(width, height);
        checkBounds(x, y);
        int newX = x + transX;
        int newY = y + transY;
        if (!sheet.canAccess(newX, newY)) return false;
        sheet.draw(g2d, newX, newY, posX, posY, width, height, scaleHints);
        return true;
    }
    
    /**
     * Sets the width of the sub image.
     * 
     * @param width The new width of the sheet.
     * 
     * @see #setSize(int, int)
     */
    public void setWidth(int width) {
        setSize(width, sheetHeight);
    }
    
    /**
     * Sets the height of the sub image.
     * 
     * @param height The new height of the sheet.
     * 
     * @see #setSize(int, int)
     */
    public void setHeight(int height) {
        setSize(sheetWidth, height);
    }
    
    /**
     * Sets The width and height of the sheet.
     * 
     * @param width The new width of the sheet.
     * @param height The new height of the sheet.
     */
    public void setSize(int width, int height) {
        if (width < 0) {
            throw new IllegalArgumentException("Expected a width >= 0, but found: " + width);
        }
        if (height < 0) {
            throw new IllegalArgumentException("Expected a height >= 0, but found: " + height);
        }
        sheetWidth = width;
        sheetWidth = height;
    }
    
    /**
     * Sets the amount the sub-view should be translated to the x-axis. <br>
     * <br>
     * <b>Example:</b><br>
     * The three images in the following example access the same element in the sheet:
     * <pre>
     *     SubImageSheet sheet = ...;
     *     Image img0 = sheet.get(0, 0);
     *     sheet.setXTranslate(1);
     *     Image img1 = sheet.get(1, 0);
     *     sheet.setXTranslate(2);
     *     Image img2 = sheet.get(2, 0);
     * </pre>
     * 
     * @param x The new amount the sub-view should be translated to the x-axis.
     * 
     * @see #setTranslate(int, int)
     */
    public void setXTranslate(int x) {
        setTranslate(x, transY);
    }
    
    /**
     * Sets the amount the sub-view should be translated to the y-axis. <br>
     * <br>
     * <b>Example:</b><br>
     * The three images in the following example access the same element in the sheet:
     * <pre>
     *     SubImageSheet sheet = ...;
     *     Image img0 = sheet.get(0, 0);
     *     sheet.setYTranslate(1);
     *     Image img1 = sheet.get(0, 1);
     *     sheet.setYTranslate(2);
     *     Image img2 = sheet.get(0, 2);
     * </pre>
     * 
     * @param y The new amount the sub-view should be translated to the y-axis.
     * 
     * @see #setTranslate(int, int)
     */
    public void setYTranslate(int y) {
        setTranslate(transX, y);
    }
    
    
    /**
     * Sets the amount the sub-view should be translated to the x- and y-axis. <br>
     * <br>
     * <b>Example:</b><br>
     * The three images in the following example access the same element in the sheet:
     * <pre>
     *     SubImageSheet sheet = ...;
     *     Image img0 = sheet.get(0, 0);
     *     sheet.setTranslate(1, 2);
     *     Image img1 = sheet.get(1, 2);
     *     sheet.setTranslate(4, 5);
     *     Image img2 = sheet.get(4, 5);
     * </pre>
     * 
     * @param x The new amount the sub-view should be translated to the x-axis.
     * @param y The new amount the sub-view should be translated to the y-axis.
     */
    public void setTranslate(int x, int y) {
        transX = x;
        transY = y;
    }
    
    /**
     * Moves the sub-view along the x-axis. <br>
     * <br>
     * <b>Example:</b><br>
     * The three images in the following example access the same element in the sheet:
     * <pre>
     *     SubImageSheet sheet = ...;
     *     Image img0 = sheet.get(0, 0);
     *     sheet.translateX(2);
     *     Image img1 = sheet.get(2, 0);
     *     sheet.translateX(1);
     *     Image img2 = sheet.get(3, 0);
     * </pre>
     * 
     * @param x The amount to translate the sub-view to the x-axis.
     * 
     * @see #translate(int, int)
     */
    public void translateX(int x) {
        translate(x, 0);
    }
    
    /**
     * Moves the sub-view along the y-axis. <br>
     * <br>
     * <b>Example:</b><br>
     * The three images in the following example access the same element in the sheet:
     * <pre>
     *     SubImageSheet sheet = ...;
     *     Image img0 = sheet.get(0, 0);
     *     sheet.translateY(2);
     *     Image img1 = sheet.get(0, 2);
     *     sheet.translateY(1);
     *     Image img2 = sheet.get(0, 3);
     * </pre>
     * 
     * @param y The amount to translate the sub-view to the y-axis.
     * 
     * @see #translate(int, int)
     */
    public void translateY(int y) {
        translate(0, y);
    }
    
    /**
     * Moves the sub-view along the x- and y-axis. <br>
     * <br>
     * <b>Example:</b><br>
     * The three images in the following example access the same element in the sheet:
     * <pre>
     *     SubImageSheet sheet = ...;
     *     Image img0 = sheet.get(0, 0);
     *     sheet.translate(1, 2);
     *     Image img1 = sheet.get(1, 2);
     *     sheet.translate(3, 4);
     *     Image img2 = sheet.get(4, 6);
     * </pre>
     * 
     * @param x The amount to translate the sub-view to the x-axis.
     * @param y The amount to translate the sub-view to the y-axis.
     */
    public void translate(int x, int y) {
        setTranslate(transX + x, transY + y);
    }
    
    
}
