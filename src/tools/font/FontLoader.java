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

package tools.font;


// Java packages
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.UIManager;
import tools.io.FileTools;


// Tools packages
import tools.log.Logger;
import tools.Var;
import tools.io.PartFile;


/**DONE (line 125?)
 * Auto-loads all the nessecary FONTS for the application at start-up.
 * 
 * @author Kaj Wortel
 */
public class FontLoader {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** Map for storing the fonts. */
    public static final Map<String, Font> FONTS = new HashMap<>();
    
    /** The default font. */
    private static Font defaultFont = (Font) UIManager.get("Label.font");
    
    
    /* -------------------------------------------------------------------------
     * Constructor.
     * -------------------------------------------------------------------------
     */
    /**
     * This is a singleton class. No instances should be made.
     * 
     * @deprecated No instances should be made.
     */
    @Deprecated
    private FontLoader() { }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * (Re-)loads a font from a file.
     * 
     * @param localPath the path of the font file relative to the font directoy.
     * @param style the style of the font. Should be one of:
     *     <ul>
     *       <li>{@link Font.PLAIN} </li>
     *       <li>{@link Font.ITALIC} </li>
     *       <li>{@link Font.BOLD} </li>
     *       <li>{@link Font.BOLD} | {@link Font.ITALIC} </li>
     *     </ul>
     * 
     * @see Var#FONT_DIR
     */
    public static Font loadLocalFont(String localPath, int style) {
        return loadFont(Var.FONT_DIR + localPath, style);
    }
    
    /**
     * (Re-)loads a font from a file.
     * 
     * @param path the path to the font file.
     * @param style the style of the font. Should be one of:
     *     <ul>
     *       <li>{@link Font.PLAIN}</li>
     *       <li>{@link Font.ITALIC}</li>
     *       <li>{@link Font.BOLD}</li>
     *       <li>{@link Font.BOLD} | {@link Font.ITALIC}</li>
     *     </ul>
     */
    public static Font loadFont(String path, int style) {
        return loadFont(path, style, Font.TRUETYPE_FONT);
    }
    
    /**
     * (Re-)loads a font from a file.
     * 
     * @param path the path to the font file.
     * @param style the style of the font. Should be one of:
     *     <ul>
     *       <li>{@link Font.PLAIN}</li>
     *       <li>{@link Font.ITALIC}</li>
     *       <li>{@link Font.BOLD}</li>
     *       <li>{@link Font.BOLD} | {@link Font.ITALIC}</li>
     *     </ul>
     * @param type The font type. Should always be {@link Font.TRUETYPE_FONT}.
     */
    protected static Font loadFont(String path, int style, int type) {
        Font font = null;
        
        try (InputStream stream = FontLoader.class.getResourceAsStream(path)) {
            //font = Font.createFont(type, fis).deriveFont(style, 12F);
            //font = Font.createFont(type, fis).deriveFont(12F);
            font = Font.createFont(type, stream);// Check this.
            FONTS.put(path, font);
            
        } catch (FontFormatException | IOException e) {
            Logger.write(e);
        }
        
        return font;
    }
    
    /**
     * @param localPath The local path of the font file.
     * @return The local font from the given path if it was loaded. {@code null} otherwise.
     * 
     * @see Var#FONT_DIR
     */
    public static Font getLocalFont(String localPath) {
        return getFont(Var.FONT_DIR + localPath);
    }
    
    /**
     * @param path The path of the font file.
     * @return The font from the given path if it was loaded. {@code null} otherwise.
     */
    public static Font getFont(String path) {
        return FONTS.get(path);
    }
    
    /**
     * Registers the given font. <br>
     * When successfull, the font can be used in the application for e.g. html code.
     * 
     * @param font The font to be registered
     * @return {@code true} if the font was added successfully. {@code false} otherwise.
     */
    public static boolean registerFont(Font font) {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
    }
    
    /**
     * Sets the default font of the application.
     * Should be called <b>before</b> the <b>initialization</b> of any swing objects to take effect.
     * 
     * @param font The font that should be the default from now on.
     */
    public static void setDefaultFont(Font font) {
        defaultFont = font;
        
        UIManager.put("Button.font", font);
        UIManager.put("ToggleButton.font", font);
        UIManager.put("RadioButton.font", font);
        UIManager.put("CheckBox.font", font);
        UIManager.put("ColorChooser.font", font);
        UIManager.put("ComboBox.font", font);
        UIManager.put("Label.font", font);
        UIManager.put("List.font", font);
        UIManager.put("MenuBar.font", font);
        UIManager.put("MenuItem.font", font);
        UIManager.put("RadioButtonMenuItem.font", font);
        UIManager.put("CheckBoxMenuItem.font", font);
        UIManager.put("Menu.font", font);
        UIManager.put("PopupMenu.font", font);
        UIManager.put("OptionPane.font", font);
        UIManager.put("Panel.font", font);
        UIManager.put("ProgressBar.font", font);
        UIManager.put("ScrollPane.font", font);
        UIManager.put("Viewport.font", font);
        UIManager.put("TabbedPane.font", font);
        UIManager.put("Table.font", font);
        UIManager.put("TableHeader.font", font);
        UIManager.put("TextField.font", font);
        UIManager.put("PasswordField.font", font);
        UIManager.put("TextArea.font", font);
        UIManager.put("TextPane.font", font);
        UIManager.put("EditorPane.font", font);
        UIManager.put("TitledBorder.font", font);
        UIManager.put("ToolBar.font", font);
        UIManager.put("ToolTip.font", font);
        UIManager.put("Tree.font", font);
    }
    
    /**
     * @return the default font.
     */
    public static Font getDefaultFont() {
        return defaultFont;
    }
    
    /**
     * Use this function to sync the loading of the static class.
     */
    public static void syncLoad() { }
    
    /**
     * Registers all fonts in the fonts folder.
     * See registerFont(Font) for more info about registering a font.
     */
    static {
        Font[] allFonts = null;
        
        Logger.write(new String [] {
            "",
            "========== START LOADING FONTS =========="
        }, Logger.Type.INFO);
        List<PartFile> files = FileTools.getFileList(new File(Var.FONT_DIR), false);
        
        for (PartFile file : files) {
            String fontLoc = file.toString();
            String fontString = fontLoc.toLowerCase();
            
            if (fontString.endsWith(".ttf")) {
                
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
                                        : style == (Font.BOLD | Font.ITALIC)
                                                ? "BOLD + ITALIC"
                                                : "ERROR!");
                */
                
                Font font = loadFont(fontLoc, style, Font.TRUETYPE_FONT);
                if (font == null) {
                    Logger.write("A null font has been created: "
                            + fontLoc, Logger.Type.ERROR);
                    
                } else if (!registerFont(font)) {
                    if (allFonts == null) {
                        allFonts = GraphicsEnvironment
                                .getLocalGraphicsEnvironment().getAllFonts();
                    }
                    
                    boolean isRegistered = false;
                    for (Font checkFont : allFonts) {
                        if (checkFont.getName().equals(font.getName())) {
                            isRegistered = true;
                            break;
                        }
                    }
                    
                    if (isRegistered) {
                        Logger.write("Font was already registered: "
                                + fontLoc, Logger.Type.WARNING);
                        
                    } else {
                        Logger.write("Could not register font: "
                                + fontLoc, Logger.Type.ERROR);
                    }
                    
                } else {
                    Logger.write("Successfully loaded font: "
                            + fontLoc, Logger.Type.INFO);
                }
            } else {
                Logger.write("Ignored file: " 
                        + fontLoc, Logger.Type.INFO);
            }
            
        }
        
        Logger.write(new String[] {
            "========== FINISHED LOADING FONTS ==========", 
            ""
        }, Logger.Type.INFO);
    }
    
    
}
