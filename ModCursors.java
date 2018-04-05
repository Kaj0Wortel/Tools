/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2018 by Kaj Wortel - all rights reserved                    *
 * Contact: kaj.wortel@gmail.com                                             *
 *                                                                           *
 * This file is part of the tools project, which can be found on github:     *
 * https://github.com/Kaj0Wortel/tools                                       *
 *                                                                           *
 * It is allowed to use, (partially) copy and modify this file               *
 * in any way for private use only by using this header.                     *
 * It is not allowed to redistribute any (modifed) versions of this file     *
 * without my permission.                                                    *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package tools;


// Tools imports
import tools.log.Log2;


// Java imports
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class ModCursors {
    final public static String WORKING_DIR
        = System.getProperty("user.dir") + "\\tools\\";
    
    final public static Cursor EMPTY_CURSOR
        = createCursor(WORKING_DIR + "img\\", "empty_square.png");
    final public static Cursor DEFAULT_CURSOR
        = new Cursor(Cursor.DEFAULT_CURSOR);
    
    private static Cursor createCursor(String path, String fileName) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        File file = new File(path + fileName);
        Cursor newCursor = new Cursor(Cursor.DEFAULT_CURSOR);
        
        try {
            BufferedImage image = ImageIO.read(file);
            newCursor = tk.createCustomCursor(image, new Point(0, 0), fileName);
            
        } catch (IOException e) {
            Log2.write("Could not access: \"" + file.getName() + "\".", true);
        }
        
        return newCursor;
    }
    
}