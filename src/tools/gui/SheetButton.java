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

package tools.gui;


// Java imports
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import javax.swing.AbstractButton;
import javax.swing.border.Border;


// Tools imports
import tools.data.img.GUIImageSheet;
import tools.data.img.ImageSheet;
import tools.gui.border.SheetBorder;


/**
 * A button which can be customized by using image sheets.
 * The image sheets must follow the following coordinate scheme:
 * <table border='1'>
 *   <tr><td> 0, 0 </td> <td> 1, 0 </td> <td> 2, 0 </td></tr>
 *   <tr><td> 0, 1 </td> <td> 1, 1 </td> <td> 2, 1 </td></tr>
 *   <tr><td> 0, 2 </td> <td> 1, 2 </td> <td> 2, 2 </td></tr>
 * </table>
 * <br>
 * Description per coordinate:<br>
 * <table border='1'>
 *   <tr><th> x </th> <th> y </th> <th> Description </th></tr>
 *   <tr><td> 0 </td> <td> 0 </td> <td> Upper left corner </td></tr>
 *   <tr><td> 1 </td> <td> 0 </td> <td> Upper side </td></tr>
 *   <tr><td> 2 </td> <td> 0 </td> <td> Upper right corner </td></tr>
 *   <tr><td> 0 </td> <td> 1 </td> <td> Left side </td></tr>
 *   <tr><td> 1 </td> <td> 1 </td> <td> Center </td></tr>
 *   <tr><td> 1 </td> <td> 2 </td> <td> Right side </td></tr>
 *   <tr><td> 2 </td> <td> 0 </td> <td> Lower left corner </td></tr>
 *   <tr><td> 2 </td> <td> 1 </td> <td> Lower side </td></tr>
 *   <tr><td> 2 </td> <td> 2 </td> <td> Lower right corner </td></tr>
 * </table>
 * 
 * @todo
 * - Add to string function for debugging.
 * - Add text to print over the button.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class SheetButton
        extends AbstractButton {
     
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The state of the button. */
    private GUIState state = GUIState.DEFAULT;
    /** The sheet to get the images from. */
    private GUIImageSheet sheet;
    /** The scaling type of the button. */
    private int scaleType = Image.SCALE_DEFAULT;
    
    /** The image to be displayed on the button. */
    private ImageSheet image;
    
    /** Denotes whether the mouse is currently over the button. */
    private boolean mouseOver = false;
    /** Denotes whether the button is currently pressed. */
    private boolean pressed = false;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new content button. <br>
     * The size of the image sheets must be at least {@code 3x3},
     * and must follow the following coordinate scheme:
     * <table border='1'>
     *   <tr><td> 0, 0 </td> <td> 1, 0 </td> <td> 2, 0 </td></tr>
     *   <tr><td> 0, 1 </td> <td> 1, 1 </td> <td> 2, 1 </td></tr>
     *   <tr><td> 0, 2 </td> <td> 1, 2 </td> <td> 2, 2 </td></tr>
     * </table>
     * 
     * @param in The insets of the border.
     * @param sheet The image sheets to use.
     */
    public SheetButton(Insets in, GUIImageSheet sheet) {
        this(in, sheet, null);
    }
    
    /**
     * Creates a new content button. <br>
     * The size of the image sheets must be at least {@code 3x3},
     * and must follow the following coordinate scheme:
     * <table border='1'>
     *   <tr><td> 0, 0 </td> <td> 1, 0 </td> <td> 2, 0 </td></tr>
     *   <tr><td> 0, 1 </td> <td> 1, 1 </td> <td> 2, 1 </td></tr>
     *   <tr><td> 0, 2 </td> <td> 1, 2 </td> <td> 2, 2 </td></tr>
     * </table>
     * <br>
     * Additionally, displays the image at {@code (0, 0)} from the {@code image}
     * image sheet at the center of the button.
     * 
     * @param in The insets of the border.
     * @param sheet The image sheets used to generate the button.
     * @param image The image to be displayed.
     */
    public SheetButton(Insets in, GUIImageSheet sheet, ImageSheet image) {
        setImageSheet(sheet);
        setBorder(new SheetBorder(in, sheet));
        this.image = image;
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (getState() == GUIState.DISABLED) return;
                pressed = true;
                setState(GUIState.PRESSED);
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (getState() == GUIState.DISABLED) return;
                pressed = false;
                if (mouseOver) {
                    setState(GUIState.ROLL_OVER);
                    fireActionPerformed(new ActionEvent(SheetButton.this, ActionEvent.ACTION_FIRST,
                            "PRESSED", System.currentTimeMillis(), e.getModifiersEx()));
                } else setState(GUIState.DEFAULT);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                if (getState() == GUIState.DISABLED) return;
                mouseOver = true;
                if (!pressed) setState(GUIState.ROLL_OVER);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (getState() == GUIState.DISABLED) return;
                mouseOver = false;
                if (!pressed) setState(GUIState.DEFAULT);
            }
        });
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * @return The current state of the button.
     */
    public GUIState getState() {
        return state;
    }
    
    /**
     * Sets the state of the button.
     * 
     * @implNote
     * Repaints the button <b>only</b> after changing the state.
     * 
     * @param state The new state of the button. Must be non-null.
     * 
     * @throws NullPointerException If {@code state == null}.
     */
    public void setState(GUIState state) {
        if (this.state == state) return;
        if (state == null) throw new NullPointerException();
        this.state = state;
        Border border = getBorder();
        if (border instanceof SheetBorder) {
            ((SheetBorder) border).setState(state);
        }
        repaint();
    }
    
    /**
     * Resets the state of the button to the initial state.
     */
    public void reset() {
        this.state = GUIState.DEFAULT;
        this.pressed = false;
        this.mouseOver = false;
    }
    
    /**
     * Returns the center scaling type.
     * The returned value will be one of the following:
     * <ul>
     *   <li> {@link Image#SCALE_DEFAULT} </li>
     *   <li> {@link Image#SCALE_FAST} </li>
     *   <li> {@link Image#SCALE_SMOOTH} </li>
     *   <li> {@link Image#SCALE_REPLICATE} </li>
     *   <li> {@link Image#SCALE_AREA_AVERAGING} </li>
     * </ul>
     * 
     * @return The scaling type used to scale the center of the button.
     */
    public int getScaleType() {
        return scaleType;
    }
    
    /**
     * Sets the scaling type of the center of the button.
     * The new scaling type must be one of the following values:
     * <ul>
     *   <li> {@link Image#SCALE_DEFAULT} </li>
     *   <li> {@link Image#SCALE_FAST} </li>
     *   <li> {@link Image#SCALE_SMOOTH} </li>
     *   <li> {@link Image#SCALE_REPLICATE} </li>
     *   <li> {@link Image#SCALE_AREA_AVERAGING} </li>
     * </ul>
     * 
     * @param scaleType The new scaling type.
     */
    public void setScaleType(int scaleType) {
        this.scaleType = scaleType;
    }
    
    /**
     * Returns the border scaling type.
     * The returned value will be one of the following:
     * <ul>
     *   <li> {@link Image#SCALE_DEFAULT} </li>
     *   <li> {@link Image#SCALE_FAST} </li>
     *   <li> {@link Image#SCALE_SMOOTH} </li>
     *   <li> {@link Image#SCALE_REPLICATE} </li>
     *   <li> {@link Image#SCALE_AREA_AVERAGING} </li>
     *   <li> {@code 0} </li>
     * </ul>
     * Where {@code 0} is only returned is the does not have a scaling type.
     * 
     * @return The scaling type used to scale the border of the button.
     * 
     * @see SheetBorder#setScaleType(int)
     */
    public int getBorderScaleType() {
        Border border = getBorder();
        if (!(border instanceof SheetBorder)) return 0;
        return ((SheetBorder) border).getScaleType();
    }
    
    /**
     * Sets the scaling type of the border of the button.
     * The new scaling type must be one of the following values:
     * <ul>
     *   <li> {@link Image#SCALE_DEFAULT} </li>
     *   <li> {@link Image#SCALE_FAST} </li>
     *   <li> {@link Image#SCALE_SMOOTH} </li>
     *   <li> {@link Image#SCALE_REPLICATE} </li>
     *   <li> {@link Image#SCALE_AREA_AVERAGING} </li>
     * </ul>
     * 
     * @param scaleType The new scaling type.
     * 
     * @see SheetBorder#getScaleType()
     */
    public void setBorderScaleType(int scaleType) {
        Border border = getBorder();
        if (border instanceof SheetBorder) {
            ((SheetBorder) border).setScaleType(scaleType);
        }
    }
    
    /**
     * Sets the image sheet to use for rendering.
     * 
     * @implNote
     * Repaints the image sheet only if the actual sheet changes.
     * 
     * @param sheet The new image sheet to use for rendering the button.
     */
    public void setImageSheet(GUIImageSheet sheet) {
        if (this.sheet == sheet) return;
        this.sheet = sheet;
        repaint();
    }
    
    /**
     * @return The image sheet used for this button.
     */
    public GUIImageSheet getImageSheet() {
        return sheet;
    }
    
    /**
     * @return The image which is painted on top of all parts in the center part of the button.
     */
    public ImageSheet getImage() {
        return image;
    }
    
    /**
     * Sets the image whic his painted on top of all parts in the center part of the button.
     * 
     * @param image The new image, or {@code null} for no image.
     */
    public void setImage(ImageSheet image) {
        if (this.image == image) return;
        this.image = image;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        Insets in = getInsets();
        int w = getWidth() - in.left - in.right;
        int h = getHeight() - in.top - in.bottom;
        
        if (w <= 0 || h <= 0) return;
        if (sheet != null) {
            sheet.get(state).draw(g2d, 1, 1, in.left, in.top, w, h, scaleType);
        }
        if (image != null) {
            image.draw(g2d, 0, 0, in.left, in.top, w, h, scaleType);
        }
    }
    
    
}
