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


// Java imports
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.ArrayList;


public class KeyDetector extends KeyAdapter {
    private static ArrayList<Integer> keysDown = new ArrayList<Integer>();
    
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getExtendedKeyCode();
        if (!keysDown.contains(key)) {
            keysDown.add(key);
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getExtendedKeyCode();
        
        if (keysDown.contains(key)) {
            keysDown.remove((Integer) key);
        }
    }
    
    public static int[] getIsDownArray() {
        int length = keysDown.size();
        int[] clone;
        
        if (length > 0) {
            clone = new int[length];
            for (int i = 0; i < length; i++) {
                clone[i] = keysDown.get(i);
            }
        } else {
            clone = null;
        }
        
        return clone;
    }
    
    public static ArrayList<Integer> getIsDownArrayList() {
        return keysDown;
    }
    
    public static boolean isDown(int key) {
        return keysDown.contains(key);
    }
}

