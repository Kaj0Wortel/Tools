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
 * @version 1.1
 * @author Kaj Wortel
 */
public abstract class GUIImageSheet {
    
    /**
     * Returns the image sheet corresponding with the given state.
     * 
     * @param state The state to get the image sheet for.
     * 
     * @return The image sheet corresponding with the state.
     */
    public BoundedImageSheet get(GUIState state) {
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
    public abstract BoundedImageSheet getDefault();
    
    /**
     * Returns the image sheet which is used for the roll over state.
     * 
     * @return The roll over image sheet.
     */
    public abstract BoundedImageSheet getRollOver();
    
    /**
     * Returns the image sheet which is used for the pressed state.
     * 
     * @return The pressed image sheet.
     */
    public abstract BoundedImageSheet getPressed();
    
    /**
     * Returns the image sheet which is used for the disabled state.
     * 
     * @return The disable image sheet.
     */
    public abstract BoundedImageSheet getDisabled();
    
    /**
     * @return The miniumum width of all sheets, or {@link Integer#MAX_VALUE}
     *     If all sheets are {@code null}.
     */
    public int getMinWidth() {
        int min = Integer.MAX_VALUE;
        for (GUIState s : GUIState.values()) {
            BoundedImageSheet sheet = get(s);
            if (sheet != null) {
                min = Math.min(min, sheet.getWidth());
            }
        }
        return min;
    }
    
    /**
     * @return The minimum height of all sheets, or {@link Integer#MAX_VALUE}
     *     If all sheets are {@code null}.
     */
    public int getMinHeight() {
        int min = Integer.MAX_VALUE;
        for (GUIState s : GUIState.values()) {
            BoundedImageSheet sheet = get(s);
            if (sheet != null) {
                min = Math.min(min, sheet.getHeight());
            }
        }
        return min;
    }
    
    
}
