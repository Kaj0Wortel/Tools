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
public class DefaultGUIImageSheet
        implements GUIImageSheet {
     
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The image sheet used for the default state. */
    private final ImageSheet def;
    /** The image sheet used for the roll over state. */
    private final ImageSheet rollOver;
    /** The image sheet used for the pressed state. */
    private final ImageSheet pressed;
    /** The image sheet used for the disabled state. */
    private final ImageSheet disabled;
    
    
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
    public DefaultGUIImageSheet(ImageSheet def, ImageSheet rollOver,
            ImageSheet pressed, ImageSheet disabled) {
        this.def = def;
        this.rollOver = rollOver;
        this.pressed = pressed;
        this.disabled = disabled;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public ImageSheet getDefault() {
        return def;
    }
    
    @Override
    public ImageSheet getRollOver() {
        return rollOver;
    }
    
    @Override
    public ImageSheet getPressed() {
        return pressed;
    }
    
    @Override
    public ImageSheet getDisabled() {
        return disabled;
    }
    
    
}
