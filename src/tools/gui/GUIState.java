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


/**
 * Enum class used for keeping track of the state of a GUI component.
 * 
 * @verions 1.0
 * @author Kaj Wortel
 */
public enum GUIState {
    
    /**
     * The default behaviour of the component.
     */
    DEFAULT,
    
    /**
     * The mouse is over the component and the component is not being pressed.
     */
    ROLL_OVER,
    
    /**
     * The component is pressed.
     */
    PRESSED,
    
    /**
     * The component is disabled.
     */
    DISABLED
    
}

