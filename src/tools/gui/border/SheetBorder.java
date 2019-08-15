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
import java.awt.Insets;
import javax.swing.border.Border;


// Tools imports
import tools.data.img.GUIImageSheet;
import tools.data.img.ImageSheet;
import tools.gui.GUIState;


/**
 * 
 * 
 * @author Kaj Wortel
 */
public class SheetBorder
        implements Border {
    
    /* -------------------------------------------------------------------------
     * Constants.
     * -------------------------------------------------------------------------
     */
     
     
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    private GUIState state = GUIState.DEFAULT;
    private GUIImageSheet sheets;
    private Insets insets;
    
    
    /* -------------------------------------------------------------------------
     * Inner classes.
     * -------------------------------------------------------------------------
     */
    
    
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
    public void setState(GUIState state) {
        if (state == null) throw new NullPointerException();
        this.state = state;
    }
    
    public GUIState getState() {
        return state;
    }
    
    public void setImageSheet(GUIImageSheet sheets) {
        if (sheets == null) throw new NullPointerException();
        this.sheets = sheets;
    }
    
    public void setInsets(Insets insets) {
        if (insets == null) throw new NullPointerException();
        this.insets = new Insets(insets.top, insets.left, insets.bottom, insets.right);
    }
    
    @Override
    public void paintBorder(Component c, Graphics g, int x0, int y0, int width, int height) {
        ImageSheet sheet = sheets.get(state);
        Insets in = insets;
        
        int[] w = new int[3];
        w[0] = Math.min(in.left, width);
        w[2] = Math.min(in.right, width);
        w[1] = Math.max(0, width - w[0] - w[2]);
        
        int[] h = new int[3];
        h[0] = Math.min(in.top, height);
        h[2] = Math.min(in.bottom, height);
        h[1] = Math.max(0, width - w[0] - w[2]);
        
        int[] x = new int[3];
        x[0] = x0;
        x[1] = x[0] + w[0];
        x[2] = x[0] + width - w[2];
        
        int[] y = new int[3];
        y[0] = y0;
        y[1] = y[0] + h[0];
        y[2] = y[0] + height - h[2];
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == 1 && j == 1) continue;
                g.drawImage(sheet.get(i, j), x[i], y[j], w[i], h[j], null);
                //System.out.println("(" + w[i] + ", " + h[j] + ") @ (" + x[i] + ", " + y[j] + ")");
            }
        }
    }
    
    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(insets.top, insets.left, insets.bottom, insets.right);
    }
    
    @Override
    public boolean isBorderOpaque() {
        return sheets.get(state).isOpaque();
    }
    
    
}
