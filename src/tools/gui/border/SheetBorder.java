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

package tools.gui.border;


// Java imports
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.border.Border;


// Tools imports
import tools.data.img.GUIImageSheet;
import tools.data.img.ImageSheet;
import tools.gui.GUIState;


/**
 * Border implementation which uses a {@link GUIImageSheet} for rendering the border.
 * The images in the image sheet must be such that for the coordinates represent
 * the following parts of the border: <br>
 * <table border='1'>
 *   <tr><td> 0, 0 </td> <td> 1, 0 </td> <td> 2, 0 </td></tr>
 *   <tr><td> 0, 1 </td> <td>      </td> <td> 2, 1 </td></tr>
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
 *   <tr><td> 1 </td> <td> 1 </td> <td> Unused </td></tr>
 *   <tr><td> 1 </td> <td> 2 </td> <td> Right side </td></tr>
 *   <tr><td> 2 </td> <td> 0 </td> <td> Lower left corner </td></tr>
 *   <tr><td> 2 </td> <td> 1 </td> <td> Lower side </td></tr>
 *   <tr><td> 2 </td> <td> 2 </td> <td> Lower right corner </td></tr>
 * </table>
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class SheetBorder
        implements Border {
     
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    private GUIState state = GUIState.DEFAULT;
    private GUIImageSheet sheets;
    private Insets insets;
    private int scaleType = Image.SCALE_DEFAULT;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Constructor.
     */
    public SheetBorder(Insets insets, GUIImageSheet sheets) {
        setImageSheet(sheets);
        setInsets(insets);
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Sets the GUI state of this border.
     * 
     * @param state The new state of the border.
     */
    public void setState(GUIState state) {
        if (state == null) throw new NullPointerException();
        this.state = state;
    }
    
    /**
     * @return The current GUI state of this border.
     */
    public GUIState getState() {
        return state;
    }
    
    /**
     * Sets the GUI image sheet to use as border.
     * 
     * @param sheets The new sheet to use. Must be non-null.
     */
    public void setImageSheet(GUIImageSheet sheets) {
        if (sheets == null) throw new NullPointerException();
        this.sheets = sheets;
    }
    
    /**
     * @return The GUI image sheet used for rendering this border.
     */
    public GUIImageSheet getImageSheet() {
        return sheets;
    }
    
    /**
     * Sets the scaling type. Must be one of:
     * <ul>
     *   <li> {@link Image#SCALE_DEFAULT} </li>
     *   <li> {@link Image#SCALE_FAST} </li>
     *   <li> {@link Image#SCALE_SMOOTH} </li>>
     *   <li> {@link Image#SCALE_REPLICATE} </li>
     *   <li> {@link Image#SCALE_AREA_AVERAGING} </li>
     * </ul>
     * 
     * @param scaleType The type used for scaling.
     */
    public void setScaleType(int scaleType) {
        this.scaleType = scaleType;
    }
    
    /**
     * Returns the scaling type. Must be one of:
     * <ul>
     *   <li> {@link Image#SCALE_DEFAULT} </li>
     *   <li> {@link Image#SCALE_FAST} </li>
     *   <li> {@link Image#SCALE_SMOOTH} </li>>
     *   <li> {@link Image#SCALE_REPLICATE} </li>
     *   <li> {@link Image#SCALE_AREA_AVERAGING} </li>
     * </ul>
     * 
     * @return The scaling type of this border.
     */
    public int getScaleType() {
        return scaleType;
    }
    
    /**
     * Sets the size of this border.
     * 
     * @param insets The new insets of this border.
     */
    public void setInsets(Insets insets) {
        if (insets == null) throw new NullPointerException();
        this.insets = new Insets(insets.top, insets.left, insets.bottom, insets.right);
    }
    
    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(insets.top, insets.left, insets.bottom, insets.right);
    }
    
    @Override
    public boolean isBorderOpaque() {
        return sheets.get(state).isOpaque();
    }
    
    @Override
    public void paintBorder(Component c, Graphics g, int x0, int y0, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        ImageSheet sheet = sheets.get(state);
        Insets in = insets;
        
        // The width of the three parts.
        int[] w = new int[3];
        w[0] = Math.min(in.left, width);
        w[2] = Math.min(in.right, width);
        w[1] = Math.max(0, width - w[0] - w[2]);
        
        // The height of the three parts.
        int[] h = new int[3];
        h[0] = Math.min(in.top, height);
        h[2] = Math.min(in.bottom, height);
        h[1] = Math.max(0, height - h[0] - h[2]);
        
        // The beginning x-coords of the three parts.
        int[] x = new int[3];
        x[0] = x0;
        x[1] = x[0] + w[0];
        x[2] = x[0] + width - w[2];
        
        // The beginning y-coords of the three parts.
        int[] y = new int[3];
        y[0] = y0;
        y[1] = y[0] + h[0];
        y[2] = y[0] + height - h[2];
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == 1 && j == 1) continue;
                sheet.draw(g2d, i, j, x[i], y[j], w[i], h[j], scaleType);
            }
        }
    }
    
    
}
