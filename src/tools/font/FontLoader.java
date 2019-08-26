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

package tools.font;


// Java packages
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.UIManager;


// Tools packages
import tools.MultiTool;
import tools.log.Logger;
import tools.Var;
import tools.data.file.FileTree;


/**
 * Auto-loads all the nessecary FONTS for the application at start-up.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public final class FontLoader {
    
    /* -------------------------------------------------------------------------
     * Constants.
     * -------------------------------------------------------------------------
     */
    /** Map for storing the fonts. */
    public static final Map<Object, Font> FONTS = new HashMap<>();
    
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The default font. */
    private static Font defaultFont = (Font) UIManager.get("Label.font");
    
    
    /* -------------------------------------------------------------------------
     * Constructor.
     * -------------------------------------------------------------------------
     */
    /**
     * This is a singleton class. No instances should be made.
     * 
     * @implNote
     * Fonts must still be registered via {@link #registerFont(Font)} before they can be
     * used in swing environments.
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
     * Use this function to wait until all default additional fonts have been loaded.
     */
    public static void syncLoad() { }
    
    /**
     * Loads the font from the file in the file tree with the given path.
     * The font will have the given style. The font must be a truetype font.
     * 
     * @param fileTree The file tree to load the file from (local or global).
     * @param path The path of the font file.
     * @param style The style of the font.
     * 
     * @return The loaded font, or {@code null} if the font couldn't be loaded.
     */
    public static Font loadFont(FileTree fileTree, String path, int style) {
        return loadFont(fileTree, path, style, Font.TRUETYPE_FONT);
    }
    
    /**
     * Loads the font from the file in the file tree with the given path.
     * The font will have the given style and is of the given type.
     * 
     * @param fileTree The file tree to load the file from (local or global).
     * @param path The path of the font file.
     * @param style The style of the font.
     * @param type The type of font to load. By default {@link Font#TRUETYPE_FONT}.
     * 
     * @return The loaded font, or {@code null} if the font couldn't be loaded.
     */
    public static Font loadFont(FileTree fileTree, String path, int style, int type) {
        try (InputStream stream = fileTree.getStream(path)) {
            return loadFont(stream, fileTree.toAbsolutePath(path), style, type);
            
        } catch (FontFormatException | IOException e) {
            Logger.write(e);
            return null;
        }
    }
    
    /**
     * Loads a font from an input stream. Assumes that the input stream comes from the given absolute path.
     * 
     * @implNote
     * Fonts must still be registered via {@link #registerFont(Font)} before they can be
     * used in swing environments.
     * 
     * @apiNote
     * The name of the font file is used as key, so enter different names to avoid clashes.
     * 
     * @param stream The stream to get the data from.
     * @param path The absolute path of the 
     * @param style The style of the font.
     * @param type The type of the font.
     * 
     * @return The loaded font, or {@code null} if the font couldn't be loaded.
     * 
     * @throws FontFormatException If the font is not in the right format.
     * @throws IOException If there was some IO error.
     * 
     * @see #getStyle(java.lang.String)
     */
    public static Font loadFont(InputStream stream, String path, int style, int type)
        throws FontFormatException, IOException {
        Font font = Font.createFont(type, stream);
        FONTS.put(new File(path).getName(), font);
        return font;
    }
    
    /**
     * Returns the font with the given (font file) name.
     * 
     * @param name The name of the font file of the font entry.
     * 
     * @return The font with the given (font file) name if it was loaded. {@code null} otherwise.
     */
    public static Font getFont(String name) {
        return FONTS.get(name);
    }
    
    /**
     * Returns the font with the given (font file) name, rescaled in the given size.
     * 
     * @param name The name of the font file of the font entry.
     * @param size The size of the font.
     * 
     * @return The font with the given (font file) name if it was loaded. {@code null} otherwise.
     */
    public static Font getFont(String name, float size) {
        Font font = FONTS.get(name);
        if (font == null) return null;
        else return font.deriveFont(size);
    }
    
    /**
     * Registers the given font. <br>
     * When successfull, the font can be used in the application for e.g. swing applications.
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
     * Determines the font style from the file name.
     * 
     * @apiNote
     * The style is determined with an educated guess about how font-files are constructred.
     * 
     * @param fileName The name of the file to determine the font style of.
     * 
     * @return The style of the font.
     */
    public static int getStyle(String fileName) {
        String[] split = fileName.toLowerCase().split("\\.");
        String name = split[Math.max(0, split.length - 2)];
        
        boolean isBold = name.contains("bold") ||
                name.endsWith("b") ||
                name.endsWith("bi") ||
                name.endsWith("ib");
        boolean isItalic = name.contains("italic") ||
                name.contains("it") ||
                name.endsWith("i") ||
                name.endsWith("bi") ||
                name.endsWith("ib");
        return (isBold ? Font.BOLD : 0) | (isItalic ? Font.ITALIC : 0);
    }
    
    /**
     * @return The default font of the application.
     */
    public static Font getDefaultFont() {
        return defaultFont;
    }
    
    /**
     * Loads an entire file tree of fonts.
     * 
     * @param fileTree The file tree to load from.
     * @param fontPath The path of the root directory to load from.
     */
    public static void loadFileTree(FileTree fileTree, String fontPath) {
        Logger.write(new String[] {
            "",
            "------ START LOADING FONTS ------",
            "Path: " + fileTree.toAbsolutePath(fontPath),
            ""
        }, Logger.Type.INFO);
        
        try {
            Set<Font> allFonts = new HashSet<>(Arrays.asList(GraphicsEnvironment
                    .getLocalGraphicsEnvironment().getAllFonts()));
            
            for (Path path : MultiTool.toIterable(fileTree.walk(fontPath))) {
                if (fileTree.isDirectory(path)) continue;
                String fontLoc = fileTree.toAbsolutePath(path.toString());
                
                if (fontLoc.toLowerCase().endsWith(".ttf")) {
                    Font font = loadFont(fileTree, fontLoc, getStyle(fontLoc), Font.TRUETYPE_FONT);
                    if (font == null) {
                        Logger.write("A null font has been created: " + fontLoc, Logger.Type.ERROR);
                        
                    } else if (!registerFont(font)) {
                        if (allFonts.contains(font)) {
                            Logger.write("Font was already registered: " + fontLoc, Logger.Type.WARNING);
                            
                        } else {
                            Logger.write("Could not register font: " + fontLoc, Logger.Type.ERROR);
                        }
                        
                    } else {
                        allFonts.add(font);
                        Logger.write("Successfully loaded font: " + fontLoc, Logger.Type.INFO);
                    }
                    
                } else {
                    Logger.write("Ignored file: " + fontLoc, Logger.Type.INFO);
                }
            }
            
        } catch (IOException e) {
            Logger.write(e);
            
        } finally {
            Logger.write(new String[] {
                "------ FINISHED LOADING FONTS ------",
                ""
            }, Logger.Type.INFO);
        }
    }
    
    /**
     * Registers all fonts in the fonts folder.
     * See registerFont(Font) for more info about registering a font.
     */
    static {
        Logger.write(new String [] {
            "",
            "========== START LOADING DEFAULT FONTS =========="
        }, Logger.Type.INFO);
        try {
            Logger.write("load file tree");
            loadFileTree(FileTree.getLocalFileTree(), Var.L_FONT_DIR);
            
        } catch (RuntimeException e) {
            Logger.write(e);
            
        } finally {
            Logger.write(new String[] {
                "========== FINISHED LOADING DEFAULT FONTS ==========", 
                ""
            }, Logger.Type.INFO);
        }
    }
    
    
}
