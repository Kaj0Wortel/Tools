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

package tools.event;


// Tools imports
import tools.Cloneable;
import tools.MultiTool;


// Java imports
import java.awt.event.InputEvent;

import java.awt.event.KeyEvent;
import java.text.ParseException;
import javax.swing.KeyStroke;


/**DONE
 * Class containing shorter naming for keys all under one roof instead
 * of spread out over multiple classes.
 * 
 * @author Kaj Wortel
 */
public class Key implements Cloneable {
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    // Alphabet
    /** The key for the letter `A`. */
    final public static Key A = new Key(true, KeyEvent.VK_A); // = 65 = 'A'
    /** The key for the letter `B`. */
    final public static Key B = new Key(true, KeyEvent.VK_B); // = 66 = 'B'
    /** The key for the letter `C`. */
    final public static Key C = new Key(true, KeyEvent.VK_C); // = 67 = 'C'
    /** The key for the letter `D`. */
    final public static Key D = new Key(true, KeyEvent.VK_D); // = 68 = 'D'
    /** The key for the letter `E`. */
    final public static Key E = new Key(true, KeyEvent.VK_E); // = 69 = 'E'
    /** The key for the letter `F`. */
    final public static Key F = new Key(true, KeyEvent.VK_F); // = 70 = 'F'
    /** The key for the letter `G`. */
    final public static Key G = new Key(true, KeyEvent.VK_G); // = 71 = 'G'
    /** The key for the letter `H`. */
    final public static Key H = new Key(true, KeyEvent.VK_H); // = 72 = 'H'
    /** The key for the letter `I`. */
    final public static Key I = new Key(true, KeyEvent.VK_I); // = 73 = 'I'
    /** The key for the letter `J`. */
    final public static Key J = new Key(true, KeyEvent.VK_J); // = 74 = 'J'
    /** The key for the letter `K`. */
    final public static Key K = new Key(true, KeyEvent.VK_K); // = 75 = 'K'
    /** The key for the letter `L`. */
    final public static Key L = new Key(true, KeyEvent.VK_L); // = 76 = 'L'
    /** The key for the letter `M`. */
    final public static Key M = new Key(true, KeyEvent.VK_M); // = 77 = 'M'
    /** The key for the letter `N`. */
    final public static Key N = new Key(true, KeyEvent.VK_N); // = 78 = 'N'
    /** The key for the letter `O`. */
    final public static Key O = new Key(true, KeyEvent.VK_O); // = 79 = 'O'
    /** The key for the letter `P`. */
    final public static Key P = new Key(true, KeyEvent.VK_P); // = 80 = 'P'
    /** The key for the letter `Q`. */
    final public static Key Q = new Key(true, KeyEvent.VK_Q); // = 81 = 'Q'
    /** The key for the letter `R`. */
    final public static Key R = new Key(true, KeyEvent.VK_R); // = 82 = 'R'
    /** The key for the letter `S`. */
    final public static Key S = new Key(true, KeyEvent.VK_S); // = 83 = 'S'
    /** The key for the letter `T`. */
    final public static Key T = new Key(true, KeyEvent.VK_T); // = 84 = 'T'
    /** The key for the letter `U`. */
    final public static Key U = new Key(true, KeyEvent.VK_U); // = 85 = 'U'
    /** The key for the letter `V`. */
    final public static Key V = new Key(true, KeyEvent.VK_V); // = 86 = 'V'
    /** The key for the letter `W`. */
    final public static Key W = new Key(true, KeyEvent.VK_W); // = 87 = 'W'
    /** The key for the letter `X`. */
    final public static Key X = new Key(true, KeyEvent.VK_X); // = 88 = 'X'
    /** The key for the letter `Y`. */
    final public static Key Y = new Key(true, KeyEvent.VK_Y); // = 89 = 'Y'
    /** The key for the letter `Z`. */
    final public static Key Z = new Key(true, KeyEvent.VK_Z); // = 90 = 'Z'
    
    // Numbers
    /** The key for the number `0`. */
    final public static Key N0 = new Key(true, KeyEvent.VK_0); // = 48 = '0'
    /** The key for the number `1`. */
    final public static Key N1 = new Key(true, KeyEvent.VK_1); // = 49 = '1'
    /** The key for the number `2`. */
    final public static Key N2 = new Key(true, KeyEvent.VK_2); // = 50 = '2'
    /** The key for the number `3`. */
    final public static Key N3 = new Key(true, KeyEvent.VK_3); // = 51 = '3'
    /** The key for the number `4`. */
    final public static Key N4 = new Key(true, KeyEvent.VK_4); // = 52 = '4'
    /** The key for the number `5`. */
    final public static Key N5 = new Key(true, KeyEvent.VK_5); // = 53 = '5'
    /** The key for the number `6`. */
    final public static Key N6 = new Key(true, KeyEvent.VK_6); // = 54 = '6'
    /** The key for the number `7`. */
    final public static Key N7 = new Key(true, KeyEvent.VK_7); // = 55 = '7'
    /** The key for the number `8`. */
    final public static Key N8 = new Key(true, KeyEvent.VK_8); // = 56 = '8'
    /** The key for the number `9`. */
    final public static Key N9 = new Key(true, KeyEvent.VK_9); // = 57 = '9'
    
    // Arrow keys
    /** The key for left arrow key. */
    final public static Key LEFT  = new Key(true, KeyEvent.VK_LEFT);  // = 37 = '%'
    /** The key for upper arrow key. */
    final public static Key UP    = new Key(true, KeyEvent.VK_UP);    // = 38 = '&'
    /** The key for right arrow key. */
    final public static Key RIGHT = new Key(true, KeyEvent.VK_RIGHT); // = 39 = '''
    /** The key for lower arrow key. */
    final public static Key DOWN  = new Key(true, KeyEvent.VK_DOWN);  // = 40 = '('
    
    // Function keys
    /** The key for the `F1` function key. */
    final public static Key F1  = new Key(true, KeyEvent.VK_F1);  // = 112
    /** The key for the `F2` function key. */
    final public static Key F2  = new Key(true, KeyEvent.VK_F2);  // = 113
    /** The key for the `F3` function key. */
    final public static Key F3  = new Key(true, KeyEvent.VK_F3);  // = 114
    /** The key for the `F4` function key. */
    final public static Key F4  = new Key(true, KeyEvent.VK_F4);  // = 115
    /** The key for the `F5` function key. */
    final public static Key F5  = new Key(true, KeyEvent.VK_F5);  // = 116
    /** The key for the `F6` function key. */
    final public static Key F6  = new Key(true, KeyEvent.VK_F6);  // = 117
    /** The key for the `F7` function key. */
    final public static Key F7  = new Key(true, KeyEvent.VK_F7);  // = 118
    /** The key for the `F8` function key. */
    final public static Key F8  = new Key(true, KeyEvent.VK_F8);  // = 119
    /** The key for the `F9` function key. */
    final public static Key F9  = new Key(true, KeyEvent.VK_F9);  // = 120
    /** The key for the `F10` function key. */
    final public static Key F10 = new Key(true, KeyEvent.VK_F10); // = 121
    /** The key for the `F11` function key. */
    final public static Key F11 = new Key(true, KeyEvent.VK_F11); // = 122
    /** The key for the `F12` function key. */
    final public static Key F12 = new Key(true, KeyEvent.VK_F12); // = 123
    /** The key for the `F13` function key. */
    final public static Key F13 = new Key(true, KeyEvent.VK_F13); // = 61440
    /** The key for the `F14` function key. */
    final public static Key F14 = new Key(true, KeyEvent.VK_F14); // = 61441
    /** The key for the `F15` function key. */
    final public static Key F15 = new Key(true, KeyEvent.VK_F15); // = 61442
    /** The key for the `F16` function key. */
    final public static Key F16 = new Key(true, KeyEvent.VK_F16); // = 61443
    /** The key for the `F17` function key. */
    final public static Key F17 = new Key(true, KeyEvent.VK_F17); // = 61444
    /** The key for the `F18` function key. */
    final public static Key F18 = new Key(true, KeyEvent.VK_F18); // = 61445
    /** The key for the `F19` function key. */
    final public static Key F19 = new Key(true, KeyEvent.VK_F19); // = 61446
    /** The key for the `F20` function key. */
    final public static Key F20 = new Key(true, KeyEvent.VK_F20); // = 61447
    /** The key for the `F21` function key. */
    final public static Key F21 = new Key(true, KeyEvent.VK_F21); // = 61448
    /** The key for the `F22` function key. */
    final public static Key F22 = new Key(true, KeyEvent.VK_F22); // = 61449
    /** The key for the `F23` function key. */
    final public static Key F23 = new Key(true, KeyEvent.VK_F23); // = 61450
    /** The key for the `F24` function key. */
    final public static Key F24 = new Key(true, KeyEvent.VK_F24); // = 61451
    
    // Other keys
    /** The key for the enter key. */
    final public static Key ENTER   = new Key(true, KeyEvent.VK_ENTER);   // = 10
    /** The key for the shift key. */
    final public static Key SHIFT   = new Key(true, KeyEvent.VK_SHIFT);   // = 16
    /** The key for the control key. */
    final public static Key CTRL    = new Key(true, KeyEvent.VK_CONTROL); // = 17
    /** The key for the alt key. */
    final public static Key ALT     = new Key(true, KeyEvent.VK_ALT);     // = 18
    /** The key for the escape key. */
    final public static Key ESC     = new Key(true, KeyEvent.VK_ESCAPE);  // = 27
    /** The key for the space bar key. */
    final public static Key SPACE   = new Key(true, KeyEvent.VK_SPACE);   // = 32 = ' '
    /** The key for the end key. */
    final public static Key END     = new Key(true, KeyEvent.VK_END);     // = 35
    /** The key for the home key. */
    final public static Key HOME    = new Key(true, KeyEvent.VK_HOME);    // = 36
    /** The key for the comma key. */
    final public static Key COMMA   = new Key(true, KeyEvent.VK_COMMA);   // = 44
    /** The key for the minus key. */
    final public static Key MINUS   = new Key(true, KeyEvent.VK_MINUS);   // = 45 = '-'
    /** The key for the period key. */
    final public static Key PERIOD  = new Key(true, KeyEvent.VK_PERIOD);  // = 46
    /** The key for the slash key. */
    final public static Key SLASH   = new Key(true, KeyEvent.VK_SLASH);   // = 47
    /** The key for the equals key. */
    final public static Key EQUAL   = new Key(true, KeyEvent.VK_EQUALS);  // = 61 = '='
    /** The key for the delete key. */
    final public static Key DEL     = new Key(true, KeyEvent.VK_DELETE);  // = 127
    /** The key for the left angular bracket key, or less than key. */
    final public static Key LESS    = new Key(true, KeyEvent.VK_LESS);    // = 153
    /** The key for the insert key. */
    final public static Key INSERT  = new Key(true, KeyEvent.VK_INSERT);  // = 155
    /** The key for the right angular bracket key, or greater than key. */
    final public static Key GREATER = new Key(true, KeyEvent.VK_GREATER); // = 160
    /** The key for the single quote key. */
    final public static Key QUOTE   = new Key(true, KeyEvent.VK_QUOTE);   // = 222
    /** The key for the at key. */
    final public static Key AT      = new Key(true, KeyEvent.VK_AT);      // = 512
    /** The key for the colon key. */
    final public static Key COLON   = new Key(true, KeyEvent.VK_COLON);   // = 513
    /** The key for the dollar key. */
    final public static Key DOLLAR  = new Key(true, KeyEvent.VK_DOLLAR);  // = 515
    /** The key for the plus key. */
    final public static Key PLUS    = new Key(true, KeyEvent.VK_PLUS);    // = 521
    /** The key for the windows key. */
    final public static Key WINDOWS = new Key(true, KeyEvent.VK_WINDOWS); // = 524
    /** The key for the backspace key. */
    final public static Key BACKSPACE   = new Key(true, KeyEvent.VK_BACK_SPACE);  // = 8
    /** The key for the caps lock key. */
    final public static Key CAPS_LOCK   = new Key(true, KeyEvent.VK_CAPS_LOCK);   // = 20
    /** The key for the backslash key. */
    final public static Key BSLASH      = new Key(true, KeyEvent.VK_BACK_SLASH);  // = 92
    /** The key for the print screen  key. */
    final public static Key PRINT_SCREEN = new Key(true, KeyEvent.VK_PRINTSCREEN); // = 154
    /** The key for the left brace key. */
    final public static Key LEFT_BRACE  = new Key(true, KeyEvent.VK_BRACELEFT);   // = 161
    /** The key for the right brace key. */
    final public static Key RIGHT_BRACE = new Key(true, KeyEvent.VK_BRACERIGHT);  // = 161
    /** The key for the backwards single quote (back tick) key. */
    final public static Key BACK_QUOTE  = new Key(true, KeyEvent.VK_BACK_QUOTE);  // = 192
    /** The key for the circumflex key. */
    final public static Key CIRCUMFLEX  = new Key(true, KeyEvent.VK_CIRCUMFLEX);  // = 514
    /** The key for the euro key. */
    final public static Key EURO        = new Key(true, KeyEvent.VK_EURO_SIGN);   // = 516
    /** The key for the hashtag key, or number sign key. */
    final public static Key NUMBER_SIGN = new Key(true, KeyEvent.VK_NUMBER_SIGN); // = 520
    /** The key for the underscore key. */
    final public static Key UNDERSCORE  = new Key(true, KeyEvent.VK_UNDERSCORE);  // = 523
    /** The key for the exclamation mark key. */
    final public static Key EXCLAMATION_MARK  = new Key(true, KeyEvent.VK_EXCLAMATION_MARK);  // = 517
    /** The key for the left parenthesis key. */
    final public static Key LEFT_PARENTHESIS  = new Key(true, KeyEvent.VK_LEFT_PARENTHESIS);  // = 519
    /** The key for the right parenthesis key. */
    final public static Key RIGHT_PARENTHESIS = new Key(true, KeyEvent.VK_RIGHT_PARENTHESIS); // = 522
    /** The key for the inverted exclamation mark key. */
    final public static Key INVERTED_EXCLAMATION_MARK = new Key(true, KeyEvent.VK_INVERTED_EXCLAMATION_MARK); // = 518
    
    // Masks
    /** The mask for the shift key. */
    final public static int SHIFT_MASK = InputEvent.SHIFT_DOWN_MASK;     // =      0b0000_0100_0000
    /** The mask for the control key. */
    final public static int CTRL_MASK  = InputEvent.CTRL_DOWN_MASK;      // =      0b0000_1000_0000
    /** The mask for the meta key. */
    final public static int META_MASK  = InputEvent.META_DOWN_MASK;      // =      0b0001_0000_0000
    /** The mask for the alt key. */
    final public static int ALT_MASK   = InputEvent.ALT_DOWN_MASK;       // =      0b0010_0000_0000
    /** The mask for the alt graph key. */
    final public static int ALT_GRAPH  = InputEvent.ALT_GRAPH_DOWN_MASK; // = 0b0010_0000_0000_0000
    
    // Default values
    /** The default mask. */
    final public static int DEFAULT_MASK = 0;
    /** The default value for key release. */
    final public static boolean DEFAULT_KEY_RELEASE = false;
    
    /** Whether the key is immutable or not. */
    final private boolean immutable;
    
    /** Stores the key value. */
    private int key;
    
    /** The modifier for this key. */
    private int mask;
    
    /** Whether the key was released or pressed. */
    private boolean onKeyRelease;
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Converts a key event to a key.
     * 
     * @param e the key event.
     * 
     * @see #Key(KeyEvent, boolean)
     */
    public Key(KeyEvent e) {
        this(e, DEFAULT_KEY_RELEASE);
    }
    
    /**
     * Converts a key event to a key.
     * 
     * @param e the key event.
     * @param onKeyRelease whether the key was released or pressed.
     */
    public Key(KeyEvent e, boolean onKeyRelease) {
        this(true, e.getExtendedKeyCode(), e.getModifiersEx(), onKeyRelease);
    }
    
    /**
     * Clones the given key.
     */
    public Key(Key key) {
        this(key.immutable, key.key, key.mask, key.onKeyRelease);
    }
    
    /**
     * Creates a new key.
     * 
     * @see #Key(boolean, int, int, boolean)
     */
    public Key(int key) {
        this(false, key);
    }
    
    /**
     * Creates a new key.
     * 
     * @see #Key(boolean, int, int, boolean)
     */
    public Key(int key, int mask) {
        this(false, key, mask, DEFAULT_KEY_RELEASE);
    }
    
    /**
     * Creates a new key.
     * 
     * @see #Key(boolean, int, int, boolean)
     */
    public Key(int key, boolean onKeyRelease) {
        this(false, key, DEFAULT_MASK, onKeyRelease);
    }
    
    /**
     * Creates a new key.
     * 
     * @see #Key(boolean, int, int, boolean)
     */
    public Key(int key, int mask, boolean onKeyRelease) {
        this(false, key, mask, onKeyRelease);
    }
    
    /**
     * Creates a new key.
     * 
     * @see #Key(boolean, int, int, boolean)
     */
    public Key(boolean immutable, int key) {
        this(immutable, key, DEFAULT_MASK, DEFAULT_KEY_RELEASE);
    }
    
    /**
     * Creates a new key.
     * 
     * @param immutable whether this key should be immutable.
     * @param key value for this key.
     * @param modifier the modifier of the key.
     * @param onKeyRelease whether the key was released or pressed.
     */
    public Key(boolean immutable, int key, int mask, boolean onKeyRelease) {
        this.immutable = immutable;
        this.key = key;
        this.mask = mask;
        this.onKeyRelease = onKeyRelease;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * @return whether this key is immutable
     */
    public boolean isImmutable() {
        return immutable;
    }
    
    /**
     * @return the key value.
     */
    public int getKey() {
        return key;
    }
    
    /**
     * @return the mask of this key.
     */
    public int getMask() {
        return mask;
    }
    
    /**
     * @return whether the key is being pressed or released.
     */
    public boolean isPressed() {
        return !onKeyRelease;
    }
    
    /**
     * Returns a keystroke of that key.
     */
    public KeyStroke toKeyStroke() {
        return KeyStroke.getKeyStroke(key, mask, onKeyRelease);
    }
    
    /**
     * Sets the mask of this key if not immutable
     * or having the same mask value.
     * 
     * @return a key having the same key value
     *     as {@code key} and the given mask.
     * 
     * Note that {@code this == setMask(this.getMask()) ==>
     *     this.mask == mask || !immutable}.
     */
    public Key setMask(int mask) {
        if (this.mask == mask) return this;
        
        if (immutable) {
            return new Key(key, mask, true);
            
        } else {
            this.mask = mask;
            return this;
        }
    }
    
    /**
     * Applies the mask to the key. The previous mask settings still remain.
     * 
     * @return a key having the same key value
     *     as {@code key} and the given mask.
     * 
     * Note that {@code this == setMask(this.getMask()) ==>
     *     this.mask == mask || !immutable}.
     */
    public Key applyMask(int mask) {
        if (this.mask == mask) return this;
        
        if (immutable) {
            return new Key(key, this.mask | mask, onKeyRelease);
            
        } else {
            this.mask |= mask;
            return this;
        }
    }
    
    /**
     * @return a new key having the same properties as {@code this}, but
     *     has the given key value.
     */
    public Key newKeyValue(int k) {
        if (k == key) return this;
        
        if (immutable) {
            return new Key(k, mask, onKeyRelease);
            
        } else {
            key = k;
            return this;
        }
    }
    
    /**
     * @return a new key having the same properties as {@code this}, but
     *     has the given onKeyRelease value.
     */
    public Key newOnKeyRelease(boolean okr) {
        if (onKeyRelease == okr) return this;
        
        if (immutable) {
            return new Key(key, mask, okr);
            
        } else {
            onKeyRelease = okr;
            return this;
        }
    }
    
    /**
     * Clones the current key.
     * Uses {@link Key#Key(Key)} for the cloning.
     * 
     * @return a clone of {@code this}
     * 
     * @see #Key(Key)
     */
    @Override
    public Key clone() {
        return new Key(this);
    }
    
    /**
     * Checks if two keys are identical, i.e. the {@link #key}, {@link #mask},
     * {@link #onKeyRelease}, and {@link #immutable} fields are compared. <br>
     * <br>
     * Also returns {@code true} if: <ul>
     * <li>{@code obj} is a {@link Number}, and the integer value of
     *     this number is equal to the key value.</li>
     * <li>{@code obj} is a {@link KeyStroke}, and the key and mask
     *     value are equal.</li>
     * </ul>
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Key) {
            Key cmpKey = (Key) obj;
            return this.key == cmpKey.key &&
                    this.mask == cmpKey.mask &&
                    this.onKeyRelease == cmpKey.onKeyRelease;
            
        } else if (obj instanceof Number) {
            return ((int) key) == ((Number) obj).intValue();
            
        } else if (obj instanceof KeyStroke) {
            return KeyStroke.getKeyStroke((char) key, mask)
                .equals((KeyStroke) obj);
            
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return MultiTool.calcHashCode(key, mask, onKeyRelease);
    }
    
    @Override
    public String toString() {
        return getClass().getCanonicalName()
                + "[key=" + key
                + ",mask=" + mask
                + ",onKeyRelease=" + onKeyRelease
                + ",immutable=" + immutable
                + "]";
    }
    
    /**
     * Parses the given string to a {@link Key} object. <br>
     * <br>
     * The format used must be the same as the {@link #toString} function:<br>
     * {@code 'class'['field_1'='value_1', ... ,'field_n'='value_n']}<br>
     * Here:<ul>
     *   <li>{@code class}: the value of {@code Key.class.getCanonicalName()}</li>
     *   <li>{@code field_x}: the name of field 'x'.</li>
     *   <li>{@code value_x}: the value of field 'x'.</li>
     * </ul>
     * <br>
     * Moreover, the following holds:<br>
     * {@code Key.parse(key.toString()).equals(key)}<br>
     * for any non-{@code null} key {@code key}.
     * 
     * @param source the string to parse.
     * @return a {@link Key} object from the parsed string.
     * @throws ParseException if the given string was invallid.
     * 
     * @see #toString();
     */
    public static Key parse(String source)
            throws ParseException {
        String[] parts = source.split("\\[|\\]");
        if (parts.length != 5) {
            throw new ParseException("Format of string is invallid. "
                    + "Expected \"class[f1=v1,...,fn=vn]\", but found: \""
                    + source + "\"", 0);
        }
        if (!Key.class.getCanonicalName().equals(parts[0])) {
            throw new ParseException("Class in string is invallid: \""
                    + parts[0] + "\"", 0);
        }
        
        int key = 0;
        int mask = 0;
        boolean onKeyRelease = false;
        boolean immutable = false;
        for (int i = 1, pos = 0; i < parts.length;
                pos += parts[i++].length() + 1) {
            String[] split = parts[i].split("=");
            if (split.length != 2) {
                throw new ParseException("Invallid format. Expected: "
                        + "\"field=value\", but found: \"" + parts[i]
                        + "\". Source: \"" + source + "\"", pos);
            }
            String field = split[0];
            String value = split[1];
            
            try {
                if ("key".equals(field)) {
                    key = Integer.parseInt(value);
                    
                } else if ("mask".equals(field)) {
                    mask = Integer.parseInt(value);
                    
                } else if ("onKeyRelease".equals(field)) {
                    onKeyRelease = Boolean.parseBoolean(value);
                    
                } else if ("immutable".equals(field)) {
                    immutable = Boolean.parseBoolean(value);
                    
                } else {
                    throw new ParseException("Expected a field, but found: \""
                            + field + "\". Source: \"" + source + "\"", pos);
                }
                
            } catch (NumberFormatException e) {
                throw new ParseException("Could not convert value: \"" + value
                        + "\". Source: \"" + source + "\"",
                        pos + field.length() + 1);
            }
        }
        
        return new Key(immutable, key, mask, onKeyRelease);
    }
    
    /**
     * Returns a visual repersentation of this key.
     * 
     * @return a visual representation of this key.
     * 
     * @see #getKeyString()
     * @see #getModString()
     */
    public String toShortcutString() {
        String modString = getModString();
        return getKeyString() + ("".equals(modString) ? "" : "+" + modString);
    }
    
    /**
     * @return a string representing all modifiers of this key.
     */
    public String getModString() {
        StringBuilder sb = new StringBuilder();
        boolean addSep = false;
        String sep = "+";
        
        if ((mask & SHIFT_MASK) == SHIFT_MASK) {
            addSep = true;
            sb.append("SHIFT");
        }
        
        if ((mask & CTRL_MASK) == CTRL_MASK) {
            if (addSep) sb.append(sep);
            addSep = true;
            sb.append("CTRL");
        }
        
        if ((mask & META_MASK) == META_MASK) {
            if (addSep) sb.append(sep);
            addSep = true;
            sb.append("META");
        }
        
        if ((mask & ALT_MASK) == ALT_MASK) {
            if (addSep) sb.append(sep);
            addSep = true;
            sb.append("ALT");
        }
        
        if ((mask & ALT_GRAPH) == ALT_GRAPH) {
            if (addSep) sb.append(sep);
            sb.append("ALT_GRAPH");
        }
        
        return sb.toString();
    }
    
    /**
     * @return the {@link #key} value of this key converted to a string representation.
     */
    public String getKeyString() {
        if (key >= A.key && key <= Z.key) {
            return "" + (char) key;
            
        } else if (key >= N0.key && key <= N9.key) {
            return "" + (char) key;
            
        } else if (key >= LEFT.key && key <= DOWN.key) {
            if (key == LEFT.key) return "LEFT";
            else if (key == UP.key) return "UP";
            else if (key == RIGHT.key) return "RIGHT";
            else return "DOWN";
            
        } else if (key >= F1.key && key <= F12.key) {
            return "F" + (key - KeyEvent.VK_F1 + 1);
            
        } else if (key >= F13.key && key <= F24.key) {
            return "F" + (key - KeyEvent.VK_F13 + 13);
            
        } else if (key == ENTER.key) return "ENTER";
        else if (key == SHIFT.key) return "SHIFT";
        else if (key == CTRL.key) return "CTRL";
        else if (key == ALT.key) return "ALT";
        else if (key == ESC.key) return "ESC";
        else if (key == SPACE.key) return "SPACE";
        else if (key == END.key) return "END";
        else if (key == HOME.key) return "HOME";
        else if (key == COMMA.key) return ",";
        else if (key == MINUS.key) return "-";
        else if (key == PERIOD.key) return ".";
        else if (key == SLASH.key) return "/";
        else if (key == EQUAL.key) return "=";
        else if (key == DEL.key) return "DEL";
        else if (key == LESS.key) return "<";
        else if (key == INSERT.key) return "INS";
        else if (key == GREATER.key) return ">";
        else if (key == QUOTE.key) return "'";
        else if (key == AT.key) return "@";
        else if (key == COLON.key) return ":";
        else if (key == DOLLAR.key) return "$";
        else if (key == PLUS.key) return "+";
        else if (key == WINDOWS.key) return "WINDOWS";
        else if (key == BACKSPACE.key) return "BACKSPACE";
        else if (key == CAPS_LOCK.key) return "CAPS LOCK";
        else if (key == BSLASH.key) return "\\";
        else if (key == PRINT_SCREEN.key) return "PRT SCR";
        else if (key == LEFT_BRACE.key) return "{";
        else if (key == RIGHT_BRACE.key) return "}";
        else if (key == BACK_QUOTE.key) return "`";
        else if (key == CIRCUMFLEX.key) return "^";
        else if (key == EURO.key) return "€";
        else if (key == NUMBER_SIGN.key) return "#";
        else if (key == UNDERSCORE.key) return "_";
        else if (key == EXCLAMATION_MARK.key) return "!";
        else if (key == LEFT_PARENTHESIS.key) return "(";
        else if (key == RIGHT_PARENTHESIS.key) return ")";
        else if (key == INVERTED_EXCLAMATION_MARK.key) return "¡";
        else return "UNKNOWN";
    }
    
    
}