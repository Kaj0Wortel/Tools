/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2018 by Kaj Wortel - all rights reserved                    *
 * Contact: kaj.wortel@gmail.com                                             *
 *                                                                           *
 * This file is part of the tools project, which can be found on github:     *
 * https://github.com/Kaj0Wortel/tools                                       *
 *                                                                           *
 * It is allowed to use, (partially) copy and modify this file               *
 * in any way for private use only by using this header.                     *
 * It is not allowed to redistribute any (modifed) versions of this file     *
 * without my permission.                                                    *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package tools;


// Java imports
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;


// Tools imports
import tools.log.Logger;


/**
 * Provides easy alpha handeling for a {@link BufferedImage}. <br>
 * <br>
 * This class is currently under development. <br>
 * Do not use this class.
 * 
 * @todo
 * Current status: reviving old artefact class.
 * 
 * @author Kaj Wortel
 * 
 * @deprecated needs to be reviewed before being used anywhere.
 */
@Deprecated
public class BufferedImageAlpha {
    private final BufferedImage baseImage;
    private Color backgroundColor = new Color(255, 255, 255);
    private BufferedImage alphaImage;
    private int alpha = 0xFF;
    
    public BufferedImageAlpha(Image newImage) {
        // Create a buffered image with transparency
        baseImage = new BufferedImage(newImage.getWidth(null), newImage.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
        alphaImage = new BufferedImage(newImage.getWidth(null), newImage.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
        
        // Draw the image on to the buffered image
        // baseImage
        Graphics2D g2dBase = baseImage.createGraphics();
        g2dBase.drawImage(newImage, 0, 0, null);
        g2dBase.dispose();
        // alphaImage
        Graphics2D g2dA = alphaImage.createGraphics();
        g2dA.drawImage(newImage, 0, 0, null);
        g2dA.dispose();
        
        //baseImage = alphaImage;
    }
    
    /* 
     ****************************************
     * THIS CONSTRUCTOR SHOULD NOT BE USED
     ****************************************
     */
    public BufferedImageAlpha(File file) throws IOException, FileNotFoundException {
        if (1 == 1) { // used to block the use of this constructor
            throw new NullPointerException("WRONG CONSTRUCTOR USED!");
        }
        // tmp
        baseImage = null;
        // end tmp
        System.out.println("file start");
        
        if (file.exists()) {
            System.out.println("file exists");
        } else {
            System.out.println("no file exists");
            throw new FileNotFoundException();
        }
        
        BufferedImage tempImage = ImageIO.read(file);
        System.out.println("file end");
        
        // creates a BufferedImage and copies each pixel
        alphaImage = new BufferedImage(tempImage.getWidth(), tempImage.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        for (int width = tempImage.getWidth() - 1; width >= 0; width--) {
            for (int height = tempImage.getHeight() - 1; height >= 0; height--) {
                int tempArgb = tempImage.getRGB(width, height) & 0x00FFFFFF | 0xFF000000;
                alphaImage.setRGB(width, height, tempArgb);
            }
        }
    }
    
    /* -------------------------------------------------------------------------
     * Function
     * -------------------------------------------------------------------------
     */
    /* 
     * Sets the alpha value of the image
     * 
     * @param alpha the new alpha value.
     */
    public void setAlpha(int alpha) {
        if (alpha > 255) {
            this.alpha = 255;
        } else if (alpha < 0) {
            this.alpha = 0;
        } else {
            this.alpha = alpha;
        }
    }
    
    /* 
     * Sets the background color.
     * @param color the new background color.
     */
    public void setBackgroundColor(Color color) { // not tested yet
        backgroundColor = color;
    }
    
    /* 
     * Redraws the image.
     */
    public void redraw() {
        if (baseImage != null) {
            
            for (int width = 0; width < baseImage.getWidth(); width++) {
                for (int height = 0; height < baseImage.getHeight() ; height++) {
                    int argb = baseImage.getRGB(width, height);
                    
                    // remove the first 6 (hex-)zeros
                    int baseAlpha = (argb >> 24) & 0x0FF;
                    // calculate the new alpha
                    int newAlpha = baseAlpha * alpha / 0x0FF;
                    // calculate the new argb by adding the old rgb and the shifted new alpha
                    int newArgb = (argb & 0x00FFFFFF) | (newAlpha << 24);
                    alphaImage.setRGB(width, height, newArgb);
                    
                }
            }
            
        }
    }
    
    /* 
     * Changes the alpha value of an image.
     */
    public static BufferedImage changeAlpha(Image image, int alpha)
        throws IllegalArgumentException {
        if (alpha < 0 || alpha > 255) {
            throw new IllegalArgumentException("Illegal alpha value: " + alpha);
        }
        
        if (image != null) {
            if (alpha == 255) {
                if (image instanceof BufferedImage) {
                    return (BufferedImage) image;
                    
                } else {
                    BufferedImage output = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
                    Graphics2D g2d = output.createGraphics();
                    g2d.drawImage(image, 0, 0, null);
                    g2d.dispose();
                    return output;
                }
            }
            
            // Create an input and output BufferedImage
            BufferedImage output;
            BufferedImage input;
            if (image instanceof BufferedImage) {
                input = (BufferedImage) image;
                output = new BufferedImage(image.getWidth(null), image.getHeight(null), ((BufferedImage) image).getType());
                
            } else {
                input = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
                Graphics2D g2d = input.createGraphics();
                g2d.drawImage(image, 0, 0, null);
                g2d.dispose();
                
                output = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
            }
            
            // Copy the input image on the output image with the changed alpha
            for (int width = 0; width < output.getWidth(); width++) {
                for (int height = 0; height < output.getHeight(); height++) {
                    
                    // Get the ARGB value of the given image
                    int argb = input.getRGB(width, height);
                    
                    // Calculate the alpha of the image
                    int inputAlpha = (argb >> 24) & 0x0FF;
                    
                    // Calculate the new alpha of the output image
                    int outputAlpha = alpha * inputAlpha / 0x0FF;
                    
                    // Set the ARGB value in the output image
                    output.setRGB(width, height, (outputAlpha << 24) | argb & 0x0FFFFFF);
                }
            }
            return output;
            
        } else {
            return null;
        }
    }
    
    /* -------------------------------------------------------------------------
     * Get function
     * -------------------------------------------------------------------------
     */
    public int getAlpha() {
        return alpha;
    }
    
    /* 
     * @return the width of the image.
     */
    public int getWidth() {
        return alphaImage.getWidth();
    }
    
    /* 
     * @return the height of the image.
     */
    public int getHeight() {
        return alphaImage.getHeight();
    }
    
    /* 
     * @return the current image.
     */
    public BufferedImage getBufferedImage() {
        //redraw();
        if (alphaImage == null) {
            Logger.write("Null");
        }
        return alphaImage;
    }
    
}