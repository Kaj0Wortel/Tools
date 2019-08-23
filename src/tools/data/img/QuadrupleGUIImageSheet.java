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


/**
 * The default image sheet collection for GUI applications.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class QuadrupleGUIImageSheet
        extends GUIImageSheet {
     
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The image sheet used for the default state. */
    private final BoundedImageSheet def;
    /** The image sheet used for the roll over state. */
    private final BoundedImageSheet rollOver;
    /** The image sheet used for the pressed state. */
    private final BoundedImageSheet pressed;
    /** The image sheet used for the disabled state. */
    private final BoundedImageSheet disabled;
    
    /** The minimum width of all sheets. */
    private final int minWidth;
    /** The minimum height of all sheets. */
    private final int minHeight;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new image sheet collection for GUI applications.
     * 
     * @param def The default image sheet used for no interaction with the button.
     * @param rollOver The image sheet used for when the mouse is over the button,
     *     but not pressed.
     * @param pressed The image sheet used for when the button is pressed.
     * @param disabled The image sheet used for when the button is disabled.
     */
    public QuadrupleGUIImageSheet(BoundedImageSheet def, BoundedImageSheet rollOver,
            BoundedImageSheet pressed, BoundedImageSheet disabled) {
        this.def = def;
        this.rollOver = rollOver;
        this.pressed = pressed;
        this.disabled = disabled;
        
        this.minWidth = super.getMinWidth();
        this.minHeight = super.getMinHeight();
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public BoundedImageSheet getDefault() {
        return def;
    }
    
    @Override
    public BoundedImageSheet getRollOver() {
        return rollOver;
    }
    
    @Override
    public BoundedImageSheet getPressed() {
        return pressed;
    }
    
    @Override
    public BoundedImageSheet getDisabled() {
        return disabled;
    }
    
    @Override
    public int getMinWidth() {
        return minWidth;
    }
    
    @Override
    public int getMinHeight() {
        return minHeight;
    }
    
    
}
