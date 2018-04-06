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

package tools.swing;


// Tools imports
import tools.ImageTools;

import tools.io.LoadImages2;


// Java imports
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.IOException;

import javax.swing.AbstractButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;


/* 
 * Old artefact.
 * Use with care!
 */
public class Button
    extends AbstractButton {
    final protected static String imgLoc = System.getProperty("user.dir")
        + "\\tools\\";
    
    // Borders
    final private Image upperBorder;
    final private Image rightBorder;
    final private Image lowerBorder;
    final private Image leftBorder;
    
    // Corners
    final private Image upLeftCorner;
    final private Image upRightCorner;
    final private Image downRightCorner;
    final private Image downLeftCorner;
    
    // Background
    final private Image backgroundNoAction;
    final private Image backgroundHover;
    final private Image backgroundPressed;
    final private Image backgroundDisabled;
    
    private Image paintBackgroundImage;
    
    // Size and barsize
    private int sizeX;
    private int sizeY;
    private int barSize;
    
    // Contents
    private JLabel label;
    private Image image;
    
    // Used for storing the current button state
    private boolean enabled = true;
    private boolean mouseIsOverButton = false;
    private boolean mouseIsPressed = false;
    
    /* -------------------------------------------------------------------------
     * Constructors
     * -------------------------------------------------------------------------
     */
    public Button(int sizeX, int sizeY, int barSize, Image image)
        throws IOException {
        this(sizeX, sizeY, barSize);
        this.image = image;
    }
    
    public Button(int sizeX, int sizeY, int barSize, String text)
        throws IOException {
        this(sizeX, sizeY, barSize);
        this.setText(text);
    }
    
    public Button(int sizeX, int sizeY, int barSize) throws IOException {
        this(sizeX, sizeY, barSize, 
             new Image[][] {
            {
                ImageTools.imageDeepCopy(LoadImages2.ensureLoadedAndGetImage(imgLoc + "button_img_TYPE_001.png", 16, 16)[0][0]),
                ImageTools.imageDeepCopy(LoadImages2.getImage(imgLoc + "button_img_TYPE_001.png")[1][0]),
                ImageTools.imageDeepCopy(LoadImages2.getImage(imgLoc + "button_img_TYPE_001.png")[2][0]), 
                ImageTools.imageDeepCopy(LoadImages2.getImage(imgLoc + "button_img_TYPE_001.png")[3][0])
            }, {
                ImageTools.imageDeepCopy(LoadImages2.getImage(imgLoc + "button_img_TYPE_001.png")[0][1]),
                ImageTools.imageDeepCopy(LoadImages2.getImage(imgLoc + "button_img_TYPE_001.png")[1][1]),
                ImageTools.imageDeepCopy(LoadImages2.getImage(imgLoc + "button_img_TYPE_001.png")[2][1]),
                ImageTools.imageDeepCopy(LoadImages2.getImage(imgLoc + "button_img_TYPE_001.png")[3][1])
            }, {
                ImageTools.imageDeepCopy(LoadImages2.getImage(imgLoc + "button_img_TYPE_001.png")[0][2]),
                ImageTools.imageDeepCopy(LoadImages2.getImage(imgLoc + "button_img_TYPE_001.png")[1][2]),
                ImageTools.imageDeepCopy(LoadImages2.getImage(imgLoc + "button_img_TYPE_001.png")[2][2]),
                ImageTools.imageDeepCopy(LoadImages2.getImage(imgLoc + "button_img_TYPE_001.png")[3][2]) 
            }
        });
    }
    
    public Button(int sizeX, int sizeY, int barSize, Image image, Image[][] imgs) {
        this(sizeX, sizeY, barSize, imgs);
        this.image = image;
    }
    
    public Button(int sizeX, int sizeY, int barSize, String text, Image[][] imgs) {
        this(sizeX, sizeY, barSize, imgs);
        this.setText(text);
    }
    
    /* 
     * @param sizeX the width of the button.
     * @param sizeY the height of the button.
     * @param barSize the size of the bar.
     * @param imgs
     * - imgs[0][0] : bar up
     * - imgs[0][1] : bar right
     * - imgs[0][2] : bar down
     * - imgs[0][3] : bar left
     * 
     * - imgs[1][0] : corner up-left
     * - imgs[1][1] : corner up-right
     * - imgs[1][2] : corner down-right
     * - imgs[1][3] : corner down-left
     * 
     * - imgs[2][0] : background no action
     * - imgs[2][1] : background hover
     * - imgs[2][2] : background selected
     * - imgs[2][3] : background disabled
     * 
     * NOTE that the images are resized to the correct size.
     * Make a deep copy of them if you want to use them for other
     * purposes / buttons with a different size. You can use
     * tools.MultiTool.imageDeepCopy(Image) for this.
     */
    public Button(int sizeX, int sizeY, int barSize, Image[][] imgs) {
        if (imgs.length != 3 || 
            imgs[0].length != 4 || 
            imgs[1].length != 4 || 
            imgs[2].length != 4) {
            
            throw new IllegalArgumentException("Expected 3x4 array for Image[][].");
        }
        
        this.setLayout(null);
        // Set the size variables
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.barSize = barSize;
        
        // Set the size of the button
        this.setSize(sizeX, sizeY);
        
        // Add the mouse listener
        this.addMouseListener(listener);
        
        // Initialize the images
        // Borders
        upperBorder = imgs[0][0].getScaledInstance(sizeX - 2*barSize, barSize, Image.SCALE_SMOOTH);
        rightBorder = imgs[0][1].getScaledInstance(barSize, sizeY - 2*barSize, Image.SCALE_SMOOTH);
        lowerBorder = imgs[0][2].getScaledInstance(sizeX - 2*barSize, barSize, Image.SCALE_SMOOTH);
        leftBorder = imgs[0][3].getScaledInstance(barSize, sizeY - 2*barSize, Image.SCALE_SMOOTH);
        
        // Corners
        upLeftCorner = imgs[1][0].getScaledInstance(barSize, barSize , Image.SCALE_SMOOTH);
        upRightCorner = imgs[1][1].getScaledInstance(barSize, barSize, Image.SCALE_SMOOTH);
        downRightCorner = imgs[1][2].getScaledInstance(barSize, barSize, Image.SCALE_SMOOTH);
        downLeftCorner = imgs[1][3].getScaledInstance(barSize, barSize, Image.SCALE_SMOOTH);
        
        //Background
        backgroundNoAction = imgs[2][0].getScaledInstance(sizeX, sizeY, Image.SCALE_SMOOTH);
        backgroundHover = imgs[2][1].getScaledInstance(sizeX, sizeY, Image.SCALE_SMOOTH);
        backgroundPressed = imgs[2][2].getScaledInstance(sizeX, sizeY, Image.SCALE_SMOOTH);
        backgroundDisabled = imgs[2][3].getScaledInstance(sizeX, sizeY, Image.SCALE_SMOOTH);
        
        paintBackgroundImage = backgroundNoAction;
    }
    
    /* -------------------------------------------------------------------------
     * Mouse listeners
     * -------------------------------------------------------------------------
     */
    MouseAdapter listener = new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            if (enabled) {
                mouseIsPressed = false;
                mouseIsOverButton = true;
                paintBackgroundImage = backgroundHover;
                repaint();
            }
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            if (enabled) {
                mouseIsPressed = false;
                mouseIsOverButton = false;
                paintBackgroundImage = backgroundNoAction;
                repaint();
            }
        }
        
        @Override
        public void mousePressed(MouseEvent e) {
            if (enabled) {
                mouseIsPressed = true;
                paintBackgroundImage = backgroundPressed;
                
                fireActionEvents(Integer.toString(e.getButton()),
                                 e.getWhen(),
                                 e.getModifiers());
                repaint();
            }
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
            if (enabled) {
                mouseIsPressed = false;
                paintBackgroundImage = backgroundHover;
                repaint();
            }
        }
    };
    
    /* -------------------------------------------------------------------------
     * Private functions
     * -------------------------------------------------------------------------
     */
    /* 
     * Fires an ActionEvent for all ActionListeners currently listening.
     * Uses as time the current time.
     * 
     * See fireActioneEvents(String, long, int) for more info.
     */
    private void fireActionEvents(String command, int modifiers) {
        fireActionEvents(command, System.currentTimeMillis(), modifiers);
    }
    
    /* 
     * Fires an ActionEvent for all ActionListeners currently listening.
     * Uses the swing thread for execution of the actionEvents.
     */
    private void fireActionEvents(String command, long when, int modifiers) {
        SwingUtilities.invokeLater
            (new Runnable() {
            public void run() {
                ActionListener[] als = getListeners(ActionListener.class);
                ActionEvent e = new ActionEvent(Button.this,
                                                ActionEvent.ACTION_PERFORMED,
                                                command, when, modifiers);
                
                for (int i = 0; i < als.length; i++) {
                    als[i].actionPerformed(e);
                }
            }
        });
    }
    
    /* -------------------------------------------------------------------------
     * Public functions
     * -------------------------------------------------------------------------
     */
    /* 
     * If there was no label set, create a new label with the text and
     * add it to the button.
     * Otherwise change the text.
     * 
     * @param text text to set.
     */
    public void setText(String text) {
        if (label == null) {
            label = new JLabel(text);
            this.add(label);
            
            if ((int) (sizeX - barSize * 2) > 0 && (int) (sizeY - barSize * 2) > 0) {
                label.setSize((int) (sizeX - barSize * 2), (int) (sizeY - barSize * 2));
                label.setLocation((int) (barSize * 1), (int) (barSize * 1));
                
            } else {
                label.setSize(sizeX, sizeY);
                label.setLocation(0, 0);
            }
        } else {
            label.setText(text);
        }
    }
    
    /* 
     * Returns the text from the label. If no text set (hence label == null), return null.
     * 
     * @return returns the text on the label.
     */
    public String getText() {
        return (label == null ? null : label.getText());
    }
    
    /* 
     * en-/disables button.
     * When enabled:
     * A press of the mouse button when the mouse is over the button notifies
     *     all ActionListeners.
     * Also the background is changed accordingly.
     * 
     * When disabled:
     * The button ignores the mouse. The background is always "backgroundDisabled".
     * 
     * When it is already enabled and is enabled again, no action is taken.
     * Idem for disabled.
     * 
     * @param enable whether the button should be enabled or disabled.
     */
    @Override
    public void setEnabled(boolean enable) {
        if (enable != enabled) {
            enabled = enable;
            
            if (enable) { // Was disabled. Now enabled.
                if (mouseIsPressed) {
                    paintBackgroundImage = backgroundPressed;
                    
                } else if (mouseIsOverButton) {
                    paintBackgroundImage = backgroundHover;
                    
                } else {
                    paintBackgroundImage = backgroundNoAction;
                }
                
            } else { // Was enabled. Now disabled.
                paintBackgroundImage = backgroundDisabled;
            }
        }
        
        super.setEnabled(enable);
    }
    
    /* 
     * Simulates a button press event.
     * 
     * @param source the component that fired the event.
     * @param button which button of the mouse was used.
     */
    public void pressButton() {
        pressButton(this);
    }
    public void pressButton(int button) {
        pressButton(this, button);
    }
    public void pressButton(Component source) {
        pressButton(source, MouseEvent.NOBUTTON);
    }
    
    public void pressButton(Component source, int button) {
        listener.mousePressed
            (new MouseEvent
                 (source,
                  MouseEvent.MOUSE_PRESSED,
                  System.currentTimeMillis(),
                  0,    // no modifiers
                  0, 0, // click occured at (0,0) relative to source
                  1,    // 1 click
                  false, button
                 )
            );
    }
    
    /* 
     * Simulates a button release event.
     * 
     * @param source the component that fired the event.
     * @param button which button of the mouse was used.
     */
    public void releaseButton() {
        releaseButton(this);
    }
    public void releaseButton(int button) {
        releaseButton(this, button);
    }
    public void releaseButton(Component source) {
        releaseButton(source, MouseEvent.NOBUTTON);
    }
    
    public void releaseButton(Component source, int button) {
        listener.mouseReleased
            (new MouseEvent
                 (source,
                  MouseEvent.MOUSE_RELEASED,
                  System.currentTimeMillis(),
                  0,    // no modifiers
                  0, 0, // click occured at (0,0) relative to source
                  1,    // 1 click
                  false, button
                 )
            );
    }
    
    /* 
     * Simulates a mouse entered event.
     * 
     * @param source the component that fired the event.
     * @param button which button of the mouse was used.
     */
    public void mouseEntered() {
        mouseEntered(this);
    }
    public void mouseEntered(int button) {
        mouseEntered(this, button);
    }
    public void mouseEntered(Component source) {
        mouseEntered(source, MouseEvent.NOBUTTON);
    }
    
    public void mouseEntered(Component source, int button) {
        listener.mouseEntered
            (new MouseEvent
                 (source,
                  MouseEvent.MOUSE_ENTERED,
                  System.currentTimeMillis(),
                  0,    // no modifiers
                  0, 0, // click occured at (0,0) relative to source
                  0,    // 0 clicks
                  false, button
                 )
            );
    }
    
    /* 
     * Simulates a mouse exited event.
     * 
     * @param source the component that fired the event.
     * @param button which button of the mouse was used.
     */
    public void mouseExited() {
        mouseExited(this);
    }
    public void mouseExited(int button) {
        mouseExited(this, button);
    }
    public void mouseExited(Component source) {
        mouseExited(source, MouseEvent.NOBUTTON);
    }
    
    public void mouseExited(Component source, int button) {
        listener.mouseExited
            (new MouseEvent
                 (source,
                  MouseEvent.MOUSE_EXITED,
                  System.currentTimeMillis(),
                  0, // no modifiers
                  0, 0, // click occured at (0,0) relative to source
                  0, // 0 click
                  false, button
                 )
            );
    }
    
    /* 
     * Sets the given image as icon image
     * 
     * @param image image that will be used for the button.
     */
    public void setImage(Image image) {
        this.image = image;
    }
    
    /* 
     * Paint the button.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // background
        g.drawImage(paintBackgroundImage, 0, 0, null);
        
        // borders
        g.drawImage(upperBorder, barSize, 0, null);
        g.drawImage(lowerBorder, barSize, sizeY - barSize, null);
        g.drawImage(rightBorder, sizeX - barSize, barSize, null);
        g.drawImage(leftBorder, 0, barSize, null);
        
        // corners
        g.drawImage(upLeftCorner, 0, 0, null);
        g.drawImage(upRightCorner, sizeX - barSize, 0, null);
        g.drawImage(downLeftCorner, 0, sizeY - barSize, null);
        g.drawImage(downRightCorner, sizeX - barSize, sizeY - barSize, null);
        
        // contents
        if (image != null) {
            g.drawImage(image,
                        (sizeX - image.getWidth(null)) / 2,
                        (sizeY - image.getHeight(null)) / 2,
                        null);
        }
    }
}