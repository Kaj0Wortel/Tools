/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) May 2019 by Kaj Wortel - all rights reserved                *
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

package tools.border;


// Tools imports
import tools.swing.IOBorder;


// Java imports
import java.awt.Insets;


/**
 * TODO: add comments.
 * 
 * Default border for the {@link tools.swing.EditField}.
 * 
 * @author Kaj Wortel (0991586)
 */
public class EditFieldDefaultBorder
        extends IOBorder {
    
    public EditFieldDefaultBorder() {
        this(new Insets(3, 3, 3, 3));
    }
    
    public EditFieldDefaultBorder(Insets in) {
        super("DEFAULT_CORNERS", "DEFAULT_BARS", in);
    }
    
    
}
