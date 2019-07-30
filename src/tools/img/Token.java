/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) July 2019 by Kaj Wortel - all rights reserved               *
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

package tools.img;


// Tools imports
import tools.MultiTool;
import tools.Var;


// Java imports
import java.awt.image.BufferedImage;


/**DONE
 * Token class for the storing a image sheets entry.
 * 
 * @author Kaj Wortel
 */
abstract class Token {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The local path of the image file. */
    private final String fileName;
    /** The id of the image sheet. */
    private final String idName;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Constructor.
     * 
     * @param shortFileName The local image file.
     * @param idName The id used for referencing.
     * 
     * @see LoadImages2#loadImage(String, String, int, int, int, int, int, int)
     */
    Token(String shortFileName, String idName) {
        this.fileName = Var.IMG_DIR + shortFileName;
        this.idName = idName;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * @return The local path of the image file.
     */
    public String getFileName() {
        return fileName;
    }
    
    /**
     * @return the id name of this token.
     */
    public String getIDName() {
        return idName;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Token)) return false;
        Token token = (Token) obj;
        return fileName.equals(token.fileName) &&
                idName.equals(token.idName);
    }
    
    @Override
    public int hashCode() {
        return MultiTool.calcHashCode(new Object[] {fileName, idName});
    }
    
    /**
     * @return the image sheet represented by this token.
     */
    public abstract BufferedImage[][] getSheet();
    
    
}
