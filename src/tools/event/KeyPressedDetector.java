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

package tools.event;


// Java imports
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.SwingUtilities;


/**
 * A KeyListener class which can be used for cycle based applications.
 * This class keeps track of which keys were pressed since the last update cycle.
 * To prevent missing key presses, this also includes keys that were pressed
 * <b>and</b> released between the two updates. <br>
 * <br>
 * The {@link #update()} function should be invoked at the beginning of each cycle.
 * Afterwards, all key-processing functions should use this class to check for
 * certain key events. It is guaranteed that the key events of this class remain
 * constant in the same cycle, assuming {@link #update()} is noly invoked at the
 * beginning of the cycle.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class KeyPressedDetector
        extends KeyAdapter {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** List containing all keys that are currently pressed. */
    private Set<Key> keysCurPressed = new HashSet<Key>();
    
    /** List containing all keys that were at least once pressed between the last update and now. */
    private Set<Key> keysPressedSinceLastUpdate = new HashSet<Key>();
    
    /** List containing all keys that were pressed between the last two updates.
     *  This list is only updated by the update method, and not via events. */
    private Set<Key> keysPressedHistory = new HashSet<Key>();
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new key detector.
     */
    public KeyPressedDetector() { }
    
    /**
     * Creates a new key detector and starts listening on the given component.
     * 
     * @param comp The component to listen to.
     */
    public KeyPressedDetector(Component comp) {
        SwingUtilities.invokeLater(() -> {
            comp.addKeyListener(this);
        });
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public void keyPressed(KeyEvent e) {
        Key key = new Key(e);
        keysCurPressed.add(key);
        keysPressedSinceLastUpdate.add(key);
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        keysCurPressed.remove(new Key(e));
    }
    
    /**
     * Checks if the key was pressed.
     * 
     * @param key The key to check for.
     * 
     * @return {@code true} if the given key was pressed between the two last updates.
     */
    public boolean wasPressed(Key key) {
        return keysPressedHistory.contains(key);
    }
    
    /**
     * Returns the set of keys which were pressed between the last two updates.
     * 
     * @implNote
     * Unsafe return on purpose for speedup. <br>
     * Do <b>NOT</b> modify the returned set!
     * 
     * @return All keys that were pressed at least once between the two
     *     last updates. Returned set should not be modified externally.
     */
    public Set<Key> getKeysPressed() {
        return keysPressedHistory;
    }
    
    /**
     * Clears the key pressed and released history of the previous and current cycle.
     * Note that this does <b>NOT</b> include resetting the keys which were already pressed.
     */
    public void reset() {
        keysPressedSinceLastUpdate.clear();
        keysPressedHistory.clear();
    }
    
    /**
     * Updates the lists to their new status. <br>
     * This function should be invoked at the start of each cycle.
     */
    public void update() {
        keysPressedHistory = keysPressedSinceLastUpdate;
        keysPressedSinceLastUpdate = new HashSet<Key>(keysCurPressed);
    }
    
    
}