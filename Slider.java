/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2018 by Kaj Wortel - all rights reserved                *
 * Contact: <kaj.wortel@gmail.com>                                       *
 *                                                                       *
 * This file is part of the tools project.                               *
 *                                                                       *
 * It is allowed to use, (partially) copy and modify this file           *
 * in any way for private use only.                                      *
 * It is not allowed to redistribute any (modifed) versions of this file *
 * without my permission.                                                *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package tools;


// Own packages
import tools.LoadImages2;
import tools.ImageTools;


// Java packages
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import java.util.ArrayList;

import java.io.IOException;

import javax.imageio.IIOException;
import javax.swing.JPanel;


// tmp
//import javax.swing.JFrame;


public class Slider extends JPanel{
    public static String workingPath = System.getProperty("user.dir") + "\\tools\\";
    private static double SPACING_FACTOR = 0.25;
    private int spacing = 0;
    
    final private BufferedImage[] source;
    private Image[] show = null;
    
    private int value = 0;
    private int minValue = 0;
    private int maxValue = 100;
    
    final public static int RAISED_SLIDER = 0;
    final public static int LOWERED_SLIDER = 1;
    
    final public static int RAISED_BUTTON = 0;
    final public static int LOWERED_BUTTON = 1;
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructor
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Creates a Slider using LOWERED_SLIDER and RAISED_BUTTON as types for
     *     resp. the slider and the button.
     */
    public Slider() throws IOException, IIOException {
        this(LOWERED_SLIDER, RAISED_BUTTON);
    }
    
    /* 
     * Creates a Slider using the predefined image sheet.
     * 
     * @param st Denotes the slider type. Must be either RAISED_SLIDER
     *     or LOWERED_SLIDER.
     * @param bt Denotes the button type. Must be either RAISED_BUTTON
     *     or LOWERED_BUTTON.
     */
    public Slider(final int st, final int bt) throws IOException, IIOException {
        this(// leftEnd
             LoadImages2.ensureLoadedAndGetImage
                 (workingPath + "slider_img_TYPE_00" + st + ".png", // File loc
                  workingPath + "slider_img_TYPE_00" + st + ".png_left_right_end", // Name
                  0, 0,   // Start X/Y
                  12, 48, // End X/Y
                  12, 24  // Size X/Y per image
                 )[0][0],
             // middle
             LoadImages2.ensureLoadedAndGetImage
                 (workingPath + "slider_img_TYPE_00" + st + ".png",  // File loc
                  workingPath + "slider_img_TYPE_00" + st + ".png_middle_bar", // Name
                  12, 0,  // Start X/Y
                  36, 24, // End X/Y
                  24, 24  // Size X/Y per image
                 )[0][0],
             // rightEnd
             LoadImages2.ensureLoadedAndGetImage
                 (workingPath + "slider_img_TYPE_00" + st + ".png_left_right_end")[0][1], // Name
             // button
             LoadImages2.ensureLoadedAndGetImage
                 (workingPath + "slider_img_TYPE_00" + bt + ".png", // File loc
                  workingPath + "slider_img_TYPE_00" + bt + ".png_middle_button", // name
                  12, 24,  // Start X/Y
                  36, 48, // End X/Y
                  24, 24  // Size X/Y per image
                 )[0][0]
            );
    }
    
    /* 
     * Creates a Slider with the given images.
     * First stores the images in the array source to prevent quality loss after 
     * multiple scalings.
     * Uses the overridable protected method createImages(Image, Image, Image, Image)
     * to create the images to be displayed
     * 
     * @param leftEnd denotes the image of left end of the slider.
     * @param middle denotes the image of the middle part of the slider.
     * @param rightEnd denotes the image of the right end of the slider.
     * @param button denotes the image of the button of the slider.
     * 
     * All parameters are not allowed to be null.
     */
    public Slider(Image leftEnd, Image middle, Image rightEnd, Image button) {
        super(null);
        
        source = new BufferedImage[] {
            ImageTools.toBufferedImage(leftEnd),
                ImageTools.toBufferedImage(middle),
                ImageTools.toBufferedImage(rightEnd),
                ImageTools.toBufferedImage(button)
        };
        
        this.addMouseMotionListener(ml);
        
        
        createImages(source[0], source[1], source[2], source[3]);
        this.setBackground(new Color(0, 0, 0, 0));
        this.setOpaque(false);
        
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Set functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Sets the value of the slider.
     * 
     * @param v New value
     */
    public void setValue(int v) {
        if (v > maxValue) {
            firePropertyChange("value", value, maxValue);
            value = maxValue;
            
        } else if (v < minValue) {
            firePropertyChange("value", value, minValue);
            value = minValue;
            
        } else {
            firePropertyChange("value", value, v);
            value = v;
        }
    }
    
    /* 
     * Sets the max value of the slider.
     * 
     * @param v New max value.
     */
    public void setMaxValue(int maxV) {
        firePropertyChange("maxValue", maxValue, maxV);
        maxValue = maxV;
        
        if (value > maxValue) {
            firePropertyChange("value", value, maxValue);
            value = maxValue;
        }
    }
    
    /* 
     * Sets the min value of the slider.
     * 
     * @param minV New min value.
     */
    public void setMinValue(int minV) {
        
        firePropertyChange("minValue", minValue, minV);
        minValue = minV;
        
        if (value < minValue) {
            value = minValue;
            firePropertyChange("value", value, minValue);
        }
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Get functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * @return the value of the slider.
     */
    public int getValue() {
        return value;
    }
    
    /* 
     * @return the max value of the slider.
     */
    public int getMaxValue() {
        return maxValue;
    }
    
    /* 
     * @return the min value of the slider.
     */
    public int getMinValue() {
        return minValue;
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Creates the show images from the four given images.
     * 
     * @param leftEnd the image for the left end of the slider.
     * @param middle the image for the middle part of the slider.
     * @param rightEnd the image for the right end of the slider.
     * @param button the image for the button.
     */
    protected void createImages(Image leftEnd, Image middle,
                                Image rightEnd, Image button) {
        if (getWidth() == 0 || getHeight() == 0) {
            show = null;
            return;
        }
        
        int scaleType = Image.SCALE_SMOOTH; // tmp
        
        int buttonSize = (getWidth() <= getHeight()
                              ? getWidth()
                              : getHeight());
        
        spacing = (int) (buttonSize * SPACING_FACTOR);
        
        int endWidth = (getWidth() >= getHeight() - 2*spacing
                            ? (getHeight() - 2*spacing) / 2
                            : getWidth() / 2);
        
        show = new Image[] {
            leftEnd.getScaledInstance(endWidth, 2*endWidth, scaleType),
                middle.getScaledInstance(getWidth() - 2*endWidth - 2*spacing, getHeight() - 2*spacing, scaleType),
                rightEnd.getScaledInstance(endWidth, 2*endWidth, scaleType),
                button.getScaledInstance(buttonSize, buttonSize, scaleType)
        };
    }
    
    @Override
    public void setBounds(int x, int y, int width, int height) {
        if (width != this.getWidth() || height != this.getHeight()) {
            super.setBounds(x, y, width, height);
            createImages(source[0], source[1], source[2], source[3]);
            
        } else {
            super.setBounds(x, y, width, height);
        }
    }
    
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Listeners
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * MouseMotionListener for the current slider object.
     * Udates the postion of the slider.
     */
    MouseMotionListener ml = new MouseAdapter() {
        @Override
        public void mouseDragged(MouseEvent e) {
            if (show != null && show[3] != null) {
                int x = e.getX();
                int y = e.getY();
                int buttonSize = show[3].getWidth(null);
                
                // The previous value.
                int oldValue = value;
                
                // New value derived from the mouse location.
                int newValue = (int) ((x - 0.5*buttonSize) / (getWidth() - buttonSize) * (maxValue - minValue) + minValue + 0.5);
                
                if (newValue > maxValue) {
                    value = maxValue;
                    
                } else if (newValue < minValue) {
                    value = minValue;
                    
                } else { // minValue <= newValue <= maxValue
                    value = newValue;
                }
                
                firePropertyChange("value", oldValue, newValue);
                
                // Only repaint the slider when the value has changed.
                if (oldValue != newValue) {
                    repaint();
                }
            }
            
            getParent().repaint();
        }
    };
    
    
    /* 
     * Paints the images of the slider on the panel
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Bound checking
        if (show == null) return;
        if (minValue >= maxValue) return;
        
        // Bound restoring
        if (value > maxValue) value = maxValue;
        if (value < minValue) value = minValue;
        
        if (show[0] != null) {
            g.drawImage(show[0],
                        spacing,
                        spacing,
                        null);
        }
        
        if (show[1] != null) {
            g.drawImage(show[1],
                        spacing + show[0].getWidth(null),
                        spacing,
                        null);
        }
        
        if (show[2] != null) {
            g.drawImage(show[2],
                        spacing + show[0].getWidth(null) + show[1].getWidth(null),
                        spacing,
                        null);
        }
        
        if (show[3] != null) {
            g.drawImage(show[3],
                        (int) ((getWidth() - show[3].getWidth(null)) * (((double) value - minValue) / maxValue)),
                        0,
                        null);
        }
    }
    
    /*
    // tmp
    public static void main(String[] args) {
        try {
            JFrame frame = new JFrame("test");
            frame.setLayout(null);
            frame.setSize(500, 500);
            
            Slider s = new Slider();
            s.setBounds(50, 50, 200, 25);
            frame.add(s);
            frame.getContentPane().setBackground(Color.RED);
            
            //s.setMinValue(0);
            //s.setMaxValue(100);
            //s.setValue(25);
            
            //System.out.println(s.getMinValue());
            //System.out.println(s.getValue());
            //System.out.println(s.getMaxValue());
            s.setMinValue(0);
            s.setMaxValue(100);
            s.setValue(50);
            s.repaint();
            s.setValue(0);
            s.repaint();
            
            
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            
            long timeStart = System.currentTimeMillis();
            for (int i = 0; i < 30; i++) {
                //s.setBounds(50, 50, 200 + 5*i, 50);
            }
            long timeEnd = System.currentTimeMillis();
            
            System.out.println(timeEnd - timeStart);
            
            int i = 0;
            while(true) {
                i++;
                try {
                    Thread.sleep(100);
                    s.setValue(i);
                    s.repaint();
                    
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}



