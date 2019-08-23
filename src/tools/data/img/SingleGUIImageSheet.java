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
 * Implementation of the {@link GUIImageSheet} class which returns a single
 * sheet for all states of the button.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class SingleGUIImageSheet
        extends GUIImageSheet {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The single image sheet which should be returned for every state. */
    private final BoundedImageSheet sheet;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new GUI image sheet which only returns a single sheet.
     * 
     * @param sheet The sheet to be returned for all states.
     */
    public SingleGUIImageSheet(BoundedImageSheet sheet) {
        this.sheet = sheet;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public BoundedImageSheet get(GUIState state) {
        return sheet;
    }
    
    @Override
    public BoundedImageSheet getDefault() {
        return sheet;
    }
    
    @Override
    public BoundedImageSheet getRollOver() {
        return sheet;
    }
    
    @Override
    public BoundedImageSheet getPressed() {
        return sheet;
    }
    
    @Override
    public BoundedImageSheet getDisabled() {
        return sheet;
    }
    
    
}
