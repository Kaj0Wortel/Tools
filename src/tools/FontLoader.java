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


// Game2 packages
import tools.log.Logger;
import static tools.log.Logger.Type;


// Java packages
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Hashtable;


/**
 * Auto-loads all the nessecary fonts for the application at start-up.
 */
public class FontLoader {
    final private static String STATIC_PATH = System.getProperty("user.dir")
        + System.getProperty("file.separator") + "font"
        + System.getProperty("file.separator");
    final public static Hashtable<String, Font> fonts = new Hashtable<>();
    
    
    /* -------------------------------------------------------------------------
     * Constructor
     * -------------------------------------------------------------------------
     */
    /* 
     * This is a singleton class. No instances should be made.
     */
    @Deprecated
    private FontLoader() { }
    
    
    /* -------------------------------------------------------------------------
     * Functions
     * -------------------------------------------------------------------------
     */
    /**
     * Loads a font from a file.
     * 
     * @param localPath the path of the font file relative to the font directoy.
     * @param path the path to the font file.
     * @param style the style of teh font. Should be one of: {@code Font.PLAIN},
     *     {@code Font.ITALIC}, {@code Font.BOLD} or
     *     {@code Font.BOLD + Font.ITALIC}
     */
    public static Font loadLocalFont(String localPath, int style) {
        return loadFont(STATIC_PATH + localPath, style);
    }
    
    public static Font loadFont(String path, int style) {
        return loadFont(path, style, Font.TRUETYPE_FONT);
    }
    
    protected static Font loadFont(String path, int style, int type) {
        Font font = null;
        
        try (FileInputStream fis = new FileInputStream(path)) {
            //font = Font.createFont(type, fis).deriveFont(style, 12F);
            font = Font.createFont(type, fis).deriveFont(12F);
            fonts.put(path, font);
            
        } catch (FontFormatException | IOException e) {
            Logger.write(e);
        }
        
        return font;
    }
    
    /**
     * @param fontName the name of the font.
     * @return the font.
     */
    public static Font getLocalFont(String localName) {
        return getFont(STATIC_PATH + localName);
    }
    
    public static Font getFont(String fontName) {
        return fonts.get(fontName);
    }
    
    /**
     * Registers the given font.
     * When successfull, the font can be used in the application
     * for e.g. html code.
     * 
     * @param font the font to be registered
     */
    public static boolean registerFont(Font font) {
        return GraphicsEnvironment.getLocalGraphicsEnvironment()
                .registerFont(font);
    }
    
    
    /**-------------------------------------------------------------------------
     * Static.
     * -------------------------------------------------------------------------
     */
    /**
     * Registers all fonts in the fonts folder.
     * See registerFont(Font) for more info about registering a font.
     */
    static {
        Font[] allFonts = null;
        
        Logger.write(" === Start loading fonts === ", Type.INFO);
        ArrayList<File[]> files = MultiTool.listFilesAndPathsFromRootDir(new File(STATIC_PATH), false);
        
        for (File[] file : files) {
            String fontLoc = file[0].toString();
            
            if (fontLoc.endsWith(".ttf")) {
                String fontString = fontLoc.toLowerCase();
                
                int style = (fontString.contains("bold") ||
                             fontString.endsWith("b.ttf") ||
                             fontString.endsWith("bi.ttf") ||
                             fontString.endsWith("ib.ttf") ? Font.BOLD : 0) |
                    (fontString.contains("italic") ||
                     fontString.contains("it") ||
                     fontString.endsWith("i.ttf") ||
                     fontString.endsWith("bi.ttf") ||
                     fontString.endsWith("ib.ttf") ? Font.ITALIC : 0);
                if (style == 0) style = Font.PLAIN;
                /*
                System.out.println(style == Font.PLAIN ? "PLAIN"
                                       : style == Font.BOLD ? "BOLD"
                                       : style == Font.ITALIC ? "ITALIC"
                                       : style == (Font.BOLD | Font.ITALIC) ? "BOLD + ITALIC"
                                       : "ERROR!");
                */
                
                Font font = loadFont(file[0].toString(), style, Font.TRUETYPE_FONT);
                if (font == null) {
                    Logger.write("A null font has been created: " + file[0].toString(), Type.ERROR);
                    
                } else if (!registerFont(font)) {
                    if (allFonts == null) {
                        allFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
                    }
                    
                    boolean isRegistered = false;
                    for (Font checkFont : allFonts) {
                        if (checkFont.getName().equals(font.getName())) {
                            isRegistered = true;
                            break;
                        }
                    }
                    
                    if (isRegistered) {
                        Logger.write("Font was already registered: " + file[0].toString(), Type.WARNING);
                        
                    } else {
                        Logger.write("Could not register font: " + file[0].toString(), Type.ERROR);
                    }
                    
                } else {
                    Logger.write("Successfully loaded font: " + file[0].toString(), Type.INFO);
                }
            } else {
                Logger.write("Ignored file: " + file[0].toString(), Type.INFO);
            }
            
        }
        
        Logger.write(new String[] {" === Finished loading fonts === ", ""}, Type.INFO);
    }
    
    
}
