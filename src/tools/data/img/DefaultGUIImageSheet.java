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


// Tools imports
import tools.Var;
import tools.data.file.FileTree;


/**
 * This class is a default implementation of a GUI image sheet which uses custom images.
 * 
 * @version 1.1
 * @author Kaj Wortel
 */
public class DefaultGUIImageSheet
        extends ManagedGUIImageSheet {
    
    /* -------------------------------------------------------------------------
     * Constants.
     * -------------------------------------------------------------------------
     */
    /** The base directory for the images. */
    private static final String DIR = Var.L_IMG_DIR + "border_type_1" + Var.FS;
    /** The local image location of the overlay image. */
    private static final String OVERLAY = DIR + "content_button_TYPE_001_overlay.png";
    /** The local image location of the default underlay image. */
    private static final String BUTTON_DEF = DIR + "content_button_TYPE_001_u_def.png";
    /** The local image location of the mosue over underlay image. */
    private static final String BUTTON_OVER = DIR + "content_button_TYPE_001_u_over.png";
    /** The local image location of the pressed underlay image. */
    private static final String BUTTON_PRESS = DIR + "content_button_TYPE_001_u_press.png";
    /** The local image location of the disabled underlay image. */
    private static final String BUTTON_DIS = DIR + "content_button_TYPE_001_u_dis.png";
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates an image sheet using the default custom images.
     */
    public DefaultGUIImageSheet() {
        super(FileTree.getLocalFileTree(), OVERLAY, BUTTON_DEF, BUTTON_OVER,
                BUTTON_PRESS, BUTTON_DIS, 16, 16);
    }
    
    
}
