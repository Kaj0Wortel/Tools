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
import java.awt.Insets;


// Tools imports
import tools.Var;
import tools.data.file.FileTree;
import tools.data.img.BoundedImageSheet;
import tools.data.img.GUIImageSheet;
import tools.data.img.ManagedImageSheet;
import tools.data.img.OverlayImageSheet;
import tools.data.img.QuadrupleGUIImageSheet;


/**
 * This border is a default implementation of the {@link SheetBorder} class
 * which uses custom images.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class DefaultSheetBorder
        extends SheetBorder {
    
    /* -------------------------------------------------------------------------
     * Constants.
     * -------------------------------------------------------------------------
     */
    /** The local image location of the overlay image. */
    private static final String OVERLAY = Var.L_IMG_DIR + "border_type_1" + Var.FS
            + "content_button_TYPE_001_overlay.png";
    /** The local image location of the default underlay image. */
    private static final String BUTTON_DEF = Var.L_IMG_DIR + "border_type_1" + Var.FS
            + "content_button_TYPE_001_u_def.png";
    /** The local image location of the mosue over underlay image. */
    private static final String BUTTON_OVER = Var.L_IMG_DIR + "border_type_1" + Var.FS
            + "content_button_TYPE_001_u_over.png";
    /** The local image location of the pressed underlay image. */
    private static final String BUTTON_PRESS = Var.L_IMG_DIR + "border_type_1" + Var.FS
            + "content_button_TYPE_001_u_press.png";
    /** The local image location of the disabled underlay image. */
    private static final String BUTTON_DIS = Var.L_IMG_DIR + "border_type_1" + Var.FS
            + "content_button_TYPE_001_u_dis.png";
    /** The image sheets to use for every instance of this border. */
    private static final GUIImageSheet DEFAULT_GUI_SHEET = generateGUISheet();
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new instance of the default border with the given insets.
     * 
     * @param top The top insets.
     * @param left The left insets.
     * @param bottom The bottom insets.
     * @param right The right insets.
     */
    public DefaultSheetBorder(int top, int left, int bottom, int right) {
        this(new Insets(top, left, bottom, right));
    }
    
    /**
     * Creates a new instance of the default border with the given insets.
     * 
     * @param in The insets of the border.
     */
    public DefaultSheetBorder(Insets in) {
        super(in, DEFAULT_GUI_SHEET);
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * This function is used to generate a value for {@link #DEFAULT_GUI_SHEET}.
     * 
     * @return The default image sheets of this border.
     */
    private static GUIImageSheet generateGUISheet() {
        FileTree toolTree = FileTree.getLocalFileTree();
        BoundedImageSheet overlay = new ManagedImageSheet(toolTree, OVERLAY, 16, 16);
        
        BoundedImageSheet def = new OverlayImageSheet(
                3, 3, new ManagedImageSheet(toolTree, BUTTON_DEF, 16, 16), overlay);
        BoundedImageSheet over = new OverlayImageSheet(
                3, 3, new ManagedImageSheet(toolTree, BUTTON_OVER, 16, 16), overlay);
        BoundedImageSheet press = new OverlayImageSheet(
                3, 3, new ManagedImageSheet(toolTree, BUTTON_PRESS, 16, 16), overlay);
        BoundedImageSheet dis = new OverlayImageSheet(
                3, 3, new ManagedImageSheet(toolTree, BUTTON_DIS, 16, 16), overlay);
        
        return new QuadrupleGUIImageSheet(def, over, press, dis);
    }
    
    
}
