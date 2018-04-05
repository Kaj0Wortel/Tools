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

package tools.event;


// Java imports
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.HashSet;


/* 
 * A KeyListener class that detects which keys were pressed since the
 * last update. To prevent missing key presses, this also includes keys
 * that were pressed and released between the two updates.
 */
public class KeyPressedDetector extends KeyAdapter {
    // List containing all keys that are currently pressed.
    private HashSet<Key> keysCurPressed = new HashSet<Key>();
    
    // List containing all keys that were at least once pressed between the
    // last update and now.
    private HashSet<Key> keysPressedSinceLastUpdate = new HashSet<Key>();
    
    // List containing all keys that were pressed between the last two updates.
    // This list is only updated by the update method, and not via events.
    private HashSet<Key> keysPressedHistory = new HashSet<Key>();
    
    /* 
     * This method is called when a key was pressed.
     * Adds the pressed key to both {@code keysCurPressed} and
     * {@code keysPressedSinceLastUpdate}.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        Key key = new Key(e);
        keysCurPressed.add(key);
        keysPressedSinceLastUpdate.add(key);
    }
    
    /* 
     * This method is called when a key was released.
     * Removes the pressed key from {@code keysCurPressed}.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        keysCurPressed.remove(new Key(e));
    }
    
    
    /* 
     * Returns true iff the given key was pressed between the two last updates.
     * 
     * @param key key-value to check for.
     */
    public boolean isPressed(Key key) {
        return keysPressedHistory.contains(key);
    }
    
    /* 
     * @return all keys that were pressed at least once between the two
     *     last updates. Returned set should not be modified externally.
     * 
     * Note: unsafe return on purpose for speedup.
     */
    public HashSet<Key> getKeysPressed() {
        return keysPressedHistory;
    }
    
    /* 
     * Clears all lists.
     */
    public void reset() {
        keysCurPressed.clear();
        keysPressedSinceLastUpdate.clear();
        keysPressedHistory.clear();
    }
    
    /* 
     * Updates the lists to their new status.
     * 
     * @return {@link getKeysPressed()}
     */
    public HashSet<Key> update() {
        keysPressedHistory = keysPressedSinceLastUpdate;
        keysPressedSinceLastUpdate = new HashSet<Key>(keysCurPressed);
        return getKeysPressed();
    }
    
}