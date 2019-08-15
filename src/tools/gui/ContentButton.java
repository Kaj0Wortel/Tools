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
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractButton;


// Tools imports
import javax.swing.border.Border;
import tools.data.img.GUIImageSheet;
import tools.gui.border.SheetBorder;


/**
 * A button which can be customized by using image sheets.
 * 
 * @author Kaj Wortel
 */
public class ContentButton
        extends AbstractButton {
     
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The state of the button. */
    private GUIState state = GUIState.DEFAULT;
    /** The sheet to get the images from. */
    private GUIImageSheet sheet;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new content button.
     * 
     * @implNote
     * The size of the image sheet must be at least {@code 3x3}.
     * The images in the sheet must be such that they are organized in the following way:
     * <pre>{@code
     * (0,0) ---- (1,0) ---- (2,0)
     *   |                     |
     * (0,1)      (1,1)      (2,1)
     *   |                     |
     * (0,2) ---- (1,2) ---- (2,2)
     * }</pre>
     * 
     * @param in The insets of the border.
     * @param sheet The image sheet to use. The image sheet must have a size of at least {@code 3x3}.
     */
    public ContentButton(Insets in, GUIImageSheet sheet) {
        setImageSheet(sheet);
        setBorder(new SheetBorder(in, sheet));
        
        addMouseListener(new MouseAdapter() {
            /** Denotes whether the mouse is currently over the button. */
            private boolean mouseOver = false;
            /** Denotes whether the button is currently pressed. */
            private boolean pressed = false;
            
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
                if (mouseOver) setState(GUIState.ROLL_OVER);
                else setState(GUIState.DEFAULT);
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
        if (getBorder() instanceof SheetBorder) {
            ((SheetBorder) border).setState(state);
        }
        repaint();
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
        if (sheet == null) throw new NullPointerException();
        boolean equal = sheet.equals(this.sheet);
        this.sheet = sheet;
        if (!equal) repaint();
    }
    
    public GUIImageSheet getImageSheet() {
        return sheet;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Insets in = getInsets();
        int w = getWidth() - in.left - in.right;
        int h = getHeight() - in.top - in.bottom;
        g.drawImage(sheet.get(state).get(1, 1, w, h, Image.SCALE_DEFAULT), in.left, in.right, null);
    }
    
    
}
