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


/**
 * Image sheets class which allows processing of multiple images.
 * 
 * @version 1.0
 * @version 0.0
 * @author Kaj Wortel
 */
public class ProcessedImageSheet
        extends AbstractImageSheet {
     
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The numWidth of the sheets. */
    public final int numWidth;
    /** The numHeight of the sheets. */
    public final int numHeight;
    /** The processor used to process the images. */
    public final ImageProcessor proc;
    /** The image sheets to be processed. */
    public final ImageSheet[] sheets;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new processed image sheets of the given dimensions.
     * 
     * @implNote
     * The images should be given in the following format: <br>
     * {@code imgs[i][x][y]} <br>
     * where {@code i} denotes the different sheets, and {@code x} and {@code y}
     * denote the coordinates in the sheets.
     * 
     * @param numWidth The number of images which are in the width of the sheet.
     * @param numHeight The number of images which are in the height of the sheet.
     * @param proc The image processor used to process the images.
     * @param imgs The images to be processed.
     */
    public ProcessedImageSheet(int numWidth, int numHeight, ImageProcessor proc, Image[][]... imgs) {
        if (proc == null || imgs == null) throw new NullPointerException();
        
        this.numWidth = numWidth;
        this.numHeight = numHeight;
        this.proc = proc;
        sheets = new ImageSheet[imgs.length];
        for (int i = 0; i < imgs.length; i++) {
            sheets[i] = new SourceImageSheet(imgs[i]);
        }
    }
    
    /**
     * Creates a new processed image sheets of the given dimensions.
     * 
     * @param width The numWidth of the sheets.
     * @param height The numHeight of the  sheets.
     * @param proc The image processor used to process the images.
     * @param sheets The sheets to be processed.
     */
    public ProcessedImageSheet(int width, int height, ImageProcessor proc, ImageSheet... sheets) {
        if (proc == null || sheets == null || sheets.length == 0) throw new NullPointerException();
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException(
                    "Expected a width and height of at least 1, but found ("
                            + width + ", " + height + ")");
        }
        this.numWidth = width;
        this.numHeight = height;
        this.proc = proc;
        this.sheets = sheets;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public Image[][] getSheet() {
        if (numWidth < 0 || numHeight < 0) return null;
        Image[][] imgSheet = new Image[numWidth][numHeight];
        for (int x = 0; x < imgSheet.length; x++) {
            for (int y = 0; y < imgSheet[0].length; y++) {
                imgSheet[x][y] = get(x, y, -1, -1, 0);
            }
        }
        return imgSheet;
    }
    
    @Override
    public Image get(int x, int y, int width, int height, int scaleHints) {
        if (0 < x && x >= numWidth) {
            throw new IllegalArgumentException(
                    "Expected 0 <= x(" + x + ") < width(" + width + ").");
        }
        if (0 < y && y >= numHeight) {
            throw new IllegalArgumentException(
                    "Expected 0 <= y(" + y + ") < height(" + height + ").");
        }
        
        Image[] imgs = new Image[sheets.length];
        int maxWidth = 1;
        int maxHeight = 1;
        for (int i = 0; i < sheets.length; i++) {
            ImageSheet sheet = sheets[i];
            if ((sheet.getNumWidth() <= 0 || x < sheet.getNumWidth()) && 
                    (sheet.getNumHeight() <= 0 || y < sheet.getNumHeight())) {
                imgs[i] = sheet.get(x, y, width, height, scaleHints);
                if (imgs[i] != null) {
                    maxWidth = Math.max(maxWidth, imgs[i].getWidth(null));
                    maxHeight = Math.max(maxHeight, imgs[i].getHeight(null));
                }
            }
        }
        if (width <= 0) width = maxWidth;
        if (height <= 0) height = maxHeight;
        return proc.process(x, y, width, height, scaleHints, imgs);
    }
    
    @Override
    public int getNumWidth() {
        return numWidth;
    }
    
    @Override
    public int getNumHeight() {
        return numHeight;
    }
    
    
}
