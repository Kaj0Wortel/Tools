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

package tools;


// Java imports
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JFrame;


/**
 * Provides an easy way for getting and setting CURSORS used in a GUI application. <br>
 * The default cursors are initialized upon static class loading.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class ModCursors {
    
    /* -------------------------------------------------------------------------
     * Constants.
     * -------------------------------------------------------------------------
     */
    /** Map where the cursors will be stored. */
    public static final ConcurrentHashMap<String, Cursor> CURSORS
            = new ConcurrentHashMap<String, Cursor>();
    
    /*
     * Provided cursors.
     */
    /** The crosshair cursor. */
    public static final Cursor CROSSHAIR_CURSOR = new Cursor(Cursor.CROSSHAIR_CURSOR);
    /** The default cursor. */
    public static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
    /** The hand cursor. */
    public static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);
    /** The move cursor. */
    public static final Cursor MOVE_CURSOR = new Cursor(Cursor.MOVE_CURSOR);
    /** The text select cursor. */
    public static final Cursor TEXT_CURSOR = new Cursor(Cursor.TEXT_CURSOR);
    /** The wait cursor. */
    public static final Cursor WAIT_CURSOR = new Cursor(Cursor.WAIT_CURSOR);
    
    /*
     * Provided resize cursors.
     */
    /** The north-resize cursor. */
    public static final Cursor N_RESIZE_CURSOR = new Cursor(Cursor.N_RESIZE_CURSOR);
    /** The north-east-resize cursor. */
    public static final Cursor NE_RESIZE_CURSOR = new Cursor(Cursor.NE_RESIZE_CURSOR);
    /** The east-resize cursor. */
    public static final Cursor E_RESIZE_CURSOR = new Cursor(Cursor.E_RESIZE_CURSOR);
    /** The south-east-resize cursor. */
    public static final Cursor SE_RESIZE_CURSOR = new Cursor(Cursor.SE_RESIZE_CURSOR);
    /** The south-resize cursor. */
    public static final Cursor S_RESIZE_CURSOR = new Cursor(Cursor.S_RESIZE_CURSOR);
    /** The south-west-resize cursor. */
    public static final Cursor SW_RESIZE_CURSOR = new Cursor(Cursor.SW_RESIZE_CURSOR);
    /** The west-resize cursor. */
    public static final Cursor W_RESIZE_CURSOR = new Cursor(Cursor.W_RESIZE_CURSOR);
    /** The north-west-resize cursor. */
    public static final Cursor NW_RESIZE_CURSOR = new Cursor(Cursor.NW_RESIZE_CURSOR);
    
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The currently active frame to change the mouse for. */
    private JFrame frame;
    
    
    /* -------------------------------------------------------------------------
     * Constructor.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new mod cursor object for the given frame.
     * 
     * @param frame the parent frame of the application.
     */
    public ModCursors(JFrame frame) {
        this.frame = frame;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Sets the given cursor for the active frame.
     * 
     * @param c the cursor to set.
     */
    public void setCursor(Cursor c) {
        if (frame != null) frame.setCursor(c);
    }
    
    /**
     * Sets the active frame to change the cursor of.
     * 
     * @param frame the new active frame.
     */
    public void setFrame(JFrame frame) {
        this.frame = frame;
    }
    
    /**
     * @return the current active frame.
     */
    public JFrame getFrame() {
        return frame;
    }
    
    
    /* -------------------------------------------------------------------------
     * Static functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a cursor from an image.
     * 
     * @param name The name of the cursor.
     * @param bi The image of the cursor.
     * 
     * @throws IllegalArgumentException If the given name already occurs.
     * 
     * @see #loadCursor(java.lang.String, java.awt.Image, int, int) 
     * @see replaceCursor(String, Image, int, int).
     */
    public static void loadCursor(String name, Image bi)
            throws IllegalArgumentException {
        loadCursor(name, bi, 0, 0);
    }
    
    /**
     * Creates a cursor from an image.
     * 
     * @param name The name of the cursor.
     * @param bi The image of the cursor.
     * @param x The x hot spot of the cursor.
     * @param y The y hot spot of the cursor.
     * 
     * @throws IllegalArgumentException If the given name already occurs.
     * 
     * @see replaceCursor(String, Image, int, int).
     */
    public static void loadCursor(String name, Image bi, int x, int y)
            throws IllegalArgumentException {
        if (CURSORS.contains(name)) {
            throw new IllegalArgumentException(
                    "There already exists a cursor with the name: " + name);
        }
        
        replaceCursor(name, bi, x, y);
    }
    
    /**
     * Replaces the cursor of the given name by the new image.
     * 
     * @param name The name of the cursor.
     * @param bi The image of the cursor.
     * 
     * @return The removed cursor.
     * 
     * @see #replaceCursor(java.lang.String, java.awt.Image, int, int) 
     */
    public static Cursor replaceCursor(String name, Image bi) {
        return replaceCursor(name, bi, 0, 0);
    }
    
    /**
     * Replaces the cursor of the given name by the new image.
     * 
     * @param name The name of the cursor.
     * @param bi The image of the cursor.
     * @param x The x hot spot of the cursor.
     * @param y The y hot spot of the cursor.
     * 
     * @return The removed cursor.
     */
    public static Cursor replaceCursor(String name, Image bi, int x, int y) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Cursor newCursor = tk.createCustomCursor(bi, new Point(x, y), name);
        return CURSORS.put(name, newCursor);
    }
    
    /**
     * @param name The name of the cursor to be returned.
     * 
     * @return The cursor stored at the given name.
     */
    public static Cursor getCursor(String name) {
        return CURSORS.get(name);
    }
    
    /**
     * Removes the given cursor.
     * 
     * @param name The name of the cursor to be removed.
     * 
     * @return The removed cursor.
     */
    public static Cursor removeCursor(String name) {
        return CURSORS.remove(name);
    }
    
    /**
     * Clears all created cursors.
     */
    public static void clear() {
        CURSORS.clear();
    }
    
    
}