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
import tools.gui.GUIState;


/**
 * Interface for an image sheet getter for the {@link tools.gui} package.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public interface GUIImageSheet {
    
    /**
     * Returns the image sheet corresponding with the given state.
     * 
     * @param state The state to get the image sheet for.
     * 
     * @return The image sheet corresponding with the state.
     */
    public default ImageSheet get(GUIState state) {
        switch(state) {
            case DEFAULT:
                return getDefault();
            case ROLL_OVER:
                return getRollOver();
            case PRESSED:
                return getPressed();
            case DISABLED:
                return getDisabled();
            default:
                throw new IllegalStateException();
        }
    }
    
    /**
     * Returns the image sheet which is used for the default state.
     * 
     * @return The default image sheet.
     */
    public ImageSheet getDefault();
    
    /**
     * Returns the image sheet which is used for the roll over state.
     * 
     * @return The roll over image sheet.
     */
    public ImageSheet getRollOver();
    
    /**
     * Returns the image sheet which is used for the pressed state.
     * 
     * @return The pressed image sheet.
     */
    public ImageSheet getPressed();
    
    /**
     * Returns the image sheet which is used for the disabled state.
     * 
     * @return The disable image sheet.
     */
    public ImageSheet getDisabled();
    
    
}
