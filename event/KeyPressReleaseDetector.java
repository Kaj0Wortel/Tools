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

import java.util.ArrayList;


/* 
 * A KeyListener class that detects key presses and released in an interval.
 * This class uses an update function instead of the default event handeling.
 * When the update function is invoked, the key presses and releases in the
 * interval between now and the previous update call can be retrieved
 * via {@code getKeys()}.
 * This is done to prevent key changes while executing the update loop.
 */
public class KeyPressReleaseDetector extends KeyAdapter {
    // List containing all key presses and releases.
    private ArrayList<Key> curKeys = new ArrayList<Key>();
    
    // List containing all key presses and releases of the previous interval.
    private ArrayList<Key> curUpdate = new ArrayList<Key>();
    
    /* 
     * Method is called when a key was pressed.
     * Adds the new key both {@code keysCurPressed} and {@code keysPressedSinceLastUpdate}
     * if it is not in there yet.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        curKeys.add(new Key(e, false));
    }
    
    /* 
     * Method is called when a key was released.
     * Removes the key from {@code keysCurPressed}.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        curKeys.add(new Key(e, true));
    }
    
    /* 
     * Updates the list of keys.
     * 
     * @return the keys that were pressed and released between the current
     *     and the previous update in order of occurance.
     */
    public ArrayList<Key> update() {
        curUpdate = curKeys;
        curKeys = new ArrayList<Key>();
        return curUpdate;
    }
    
    /* 
     * @return the keys that were pressed and released between the
     *     previous two updates in order of occurance.
     */
    public ArrayList<Key> getCurUpdate() {
        return curUpdate;
    }
    
}


