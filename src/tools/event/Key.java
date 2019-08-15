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
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import javax.swing.KeyStroke;


// Tools imports
import tools.MultiTool;
import tools.PublicCloneable;


/**
 * Class containing shorter naming for keys all under one roof instead
 * of spread out over multiple classes.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class Key implements PublicCloneable {
    
    /* -------------------------------------------------------------------------
     * Constants.
     * -------------------------------------------------------------------------
     */
    // Alphabet
    /** The key for the letter `A`. */
    public static final Key A = new Key(KeyEvent.VK_A, true); // = 65 = 'A'
    /** The key for the letter `B`. */
    public static final Key B = new Key(KeyEvent.VK_B, true); // = 66 = 'B'
    /** The key for the letter `C`. */
    public static final Key C = new Key(KeyEvent.VK_C, true); // = 67 = 'C'
    /** The key for the letter `D`. */
    public static final Key D = new Key(KeyEvent.VK_D, true); // = 68 = 'D'
    /** The key for the letter `E`. */
    public static final Key E = new Key(KeyEvent.VK_E, true); // = 69 = 'E'
    /** The key for the letter `F`. */
    public static final Key F = new Key(KeyEvent.VK_F, true); // = 70 = 'F'
    /** The key for the letter `G`. */
    public static final Key G = new Key(KeyEvent.VK_G, true); // = 71 = 'G'
    /** The key for the letter `H`. */
    public static final Key H = new Key(KeyEvent.VK_H, true); // = 72 = 'H'
    /** The key for the letter `I`. */
    public static final Key I = new Key(KeyEvent.VK_I, true); // = 73 = 'I'
    /** The key for the letter `J`. */
    public static final Key J = new Key(KeyEvent.VK_J, true); // = 74 = 'J'
    /** The key for the letter `K`. */
    public static final Key K = new Key(KeyEvent.VK_K, true); // = 75 = 'K'
    /** The key for the letter `L`. */
    public static final Key L = new Key(KeyEvent.VK_L, true); // = 76 = 'L'
    /** The key for the letter `M`. */
    public static final Key M = new Key(KeyEvent.VK_M, true); // = 77 = 'M'
    /** The key for the letter `N`. */
    public static final Key N = new Key(KeyEvent.VK_N, true); // = 78 = 'N'
    /** The key for the letter `O`. */
    public static final Key O = new Key(KeyEvent.VK_O, true); // = 79 = 'O'
    /** The key for the letter `P`. */
    public static final Key P = new Key(KeyEvent.VK_P, true); // = 80 = 'P'
    /** The key for the letter `Q`. */
    public static final Key Q = new Key(KeyEvent.VK_Q, true); // = 81 = 'Q'
    /** The key for the letter `R`. */
    public static final Key R = new Key(KeyEvent.VK_R, true); // = 82 = 'R'
    /** The key for the letter `S`. */
    public static final Key S = new Key(KeyEvent.VK_S, true); // = 83 = 'S'
    /** The key for the letter `T`. */
    public static final Key T = new Key(KeyEvent.VK_T, true); // = 84 = 'T'
    /** The key for the letter `U`. */
    public static final Key U = new Key(KeyEvent.VK_U, true); // = 85 = 'U'
    /** The key for the letter `V`. */
    public static final Key V = new Key(KeyEvent.VK_V, true); // = 86 = 'V'
    /** The key for the letter `W`. */
    public static final Key W = new Key(KeyEvent.VK_W, true); // = 87 = 'W'
    /** The key for the letter `X`. */
    public static final Key X = new Key(KeyEvent.VK_X, true); // = 88 = 'X'
    /** The key for the letter `Y`. */
    public static final Key Y = new Key(KeyEvent.VK_Y, true); // = 89 = 'Y'
    /** The key for the letter `Z`. */
    public static final Key Z = new Key(KeyEvent.VK_Z, true); // = 90 = 'Z'
    
    // Numbers
    /** The key for the number `0`. */
    public static final Key N0 = new Key(KeyEvent.VK_0, true); // = 48 = '0'
    /** The key for the number `1`. */
    public static final Key N1 = new Key(KeyEvent.VK_1, true); // = 49 = '1'
    /** The key for the number `2`. */
    public static final Key N2 = new Key(KeyEvent.VK_2, true); // = 50 = '2'
    /** The key for the number `3`. */
    public static final Key N3 = new Key(KeyEvent.VK_3, true); // = 51 = '3'
    /** The key for the number `4`. */
    public static final Key N4 = new Key(KeyEvent.VK_4, true); // = 52 = '4'
    /** The key for the number `5`. */
    public static final Key N5 = new Key(KeyEvent.VK_5, true); // = 53 = '5'
    /** The key for the number `6`. */
    public static final Key N6 = new Key(KeyEvent.VK_6, true); // = 54 = '6'
    /** The key for the number `7`. */
    public static final Key N7 = new Key(KeyEvent.VK_7, true); // = 55 = '7'
    /** The key for the number `8`. */
    public static final Key N8 = new Key(KeyEvent.VK_8, true); // = 56 = '8'
    /** The key for the number `9`. */
    public static final Key N9 = new Key(KeyEvent.VK_9, true); // = 57 = '9'
    
    // Arrow keys
    /** The key for left arrow key. */
    public static final Key LEFT  = new Key(KeyEvent.VK_LEFT, true);  // = 37 = '%'
    /** The key for upper arrow key. */
    public static final Key UP    = new Key(KeyEvent.VK_UP, true);    // = 38 = '&'
    /** The key for right arrow key. */
    public static final Key RIGHT = new Key(KeyEvent.VK_RIGHT, true); // = 39 = '''
    /** The key for lower arrow key. */
    public static final Key DOWN  = new Key(KeyEvent.VK_DOWN, true);  // = 40 = '('
    
    // Function keys
    /** The key for the F1 function key. */
    public static final Key F1  = new Key(KeyEvent.VK_F1, true);  // = 112
    /** The key for the F2 function key. */
    public static final Key F2  = new Key(KeyEvent.VK_F2, true);  // = 113
    /** The key for the F3 function key. */
    public static final Key F3  = new Key(KeyEvent.VK_F3, true);  // = 114
    /** The key for the F4 function key. */
    public static final Key F4  = new Key(KeyEvent.VK_F4, true);  // = 115
    /** The key for the F5 function key. */
    public static final Key F5  = new Key(KeyEvent.VK_F5, true);  // = 116
    /** The key for the F6 function key. */
    public static final Key F6  = new Key(KeyEvent.VK_F6, true);  // = 117
    /** The key for the F7 function key. */
    public static final Key F7  = new Key(KeyEvent.VK_F7, true);  // = 118
    /** The key for the F8 function key. */
    public static final Key F8  = new Key(KeyEvent.VK_F8, true);  // = 119
    /** The key for the F9 function key. */
    public static final Key F9  = new Key(KeyEvent.VK_F9, true);  // = 120
    /** The key for the F10 function key. */
    public static final Key F10 = new Key(KeyEvent.VK_F10, true); // = 121
    /** The key for the F11 function key. */
    public static final Key F11 = new Key(KeyEvent.VK_F11, true); // = 122
    /** The key for the F12 function key. */
    public static final Key F12 = new Key(KeyEvent.VK_F12, true); // = 123
    /** The key for the F13 function key. */
    public static final Key F13 = new Key(KeyEvent.VK_F13, true); // = 61440
    /** The key for the F14 function key. */
    public static final Key F14 = new Key(KeyEvent.VK_F14, true); // = 61441
    /** The key for the F15 function key. */
    public static final Key F15 = new Key(KeyEvent.VK_F15, true); // = 61442
    /** The key for the F16 function key. */
    public static final Key F16 = new Key(KeyEvent.VK_F16, true); // = 61443
    /** The key for the F17 function key. */
    public static final Key F17 = new Key(KeyEvent.VK_F17, true); // = 61444
    /** The key for the F18 function key. */
    public static final Key F18 = new Key(KeyEvent.VK_F18, true); // = 61445
    /** The key for the F19 function key. */
    public static final Key F19 = new Key(KeyEvent.VK_F19, true); // = 61446
    /** The key for the F20 function key. */
    public static final Key F20 = new Key(KeyEvent.VK_F20, true); // = 61447
    /** The key for the F21 function key. */
    public static final Key F21 = new Key(KeyEvent.VK_F21, true); // = 61448
    /** The key for the F22 function key. */
    public static final Key F22 = new Key(KeyEvent.VK_F22, true); // = 61449
    /** The key for the F23 function key. */
    public static final Key F23 = new Key(KeyEvent.VK_F23, true); // = 61450
    /** The key for the F24 function key. */
    public static final Key F24 = new Key(KeyEvent.VK_F24, true); // = 61451
    
    // Other keys
    /** The key for the enter key. */
    public static final Key ENTER   = new Key(KeyEvent.VK_ENTER, true);   // = 10
    /** The key for the shift key. */
    public static final Key SHIFT   = new Key(KeyEvent.VK_SHIFT, true);   // = 16
    /** The key for the control key. */
    public static final Key CTRL    = new Key(KeyEvent.VK_CONTROL, true); // = 17
    /** The key for the alt key. */
    public static final Key ALT     = new Key(KeyEvent.VK_ALT, true);     // = 18
    /** The key for the escape key. */
    public static final Key ESC     = new Key(KeyEvent.VK_ESCAPE, true);  // = 27
    /** The key for the space bar key. */
    public static final Key SPACE   = new Key(KeyEvent.VK_SPACE, true);   // = 32 = ' '
    /** The key for the end key. */
    public static final Key END     = new Key(KeyEvent.VK_END, true);     // = 35
    /** The key for the home key. */
    public static final Key HOME    = new Key(KeyEvent.VK_HOME, true);    // = 36
    /** The key for the comma key. */
    public static final Key COMMA   = new Key(KeyEvent.VK_COMMA, true);   // = 44
    /** The key for the minus key. */
    public static final Key MINUS   = new Key(KeyEvent.VK_MINUS, true);   // = 45 = '-'
    /** The key for the period key. */
    public static final Key PERIOD  = new Key(KeyEvent.VK_PERIOD, true);  // = 46
    /** The key for the slash key. */
    public static final Key SLASH   = new Key(KeyEvent.VK_SLASH, true);   // = 47
    /** The key for the equals key. */
    public static final Key EQUAL   = new Key(KeyEvent.VK_EQUALS, true);  // = 61 = '='
    /** The key for the delete key. */
    public static final Key DEL     = new Key(KeyEvent.VK_DELETE, true);  // = 127
    /** The key for the left angular bracket key, or less than key. */
    public static final Key LESS    = new Key(KeyEvent.VK_LESS, true);    // = 153
    /** The key for the insert key. */
    public static final Key INSERT  = new Key(KeyEvent.VK_INSERT, true);  // = 155
    /** The key for the right angular bracket key, or greater than key. */
    public static final Key GREATER = new Key(KeyEvent.VK_GREATER, true); // = 160
    /** The key for the single quote key. */
    public static final Key QUOTE   = new Key(KeyEvent.VK_QUOTE, true);   // = 222
    /** The key for the at key. */
    public static final Key AT      = new Key(KeyEvent.VK_AT, true);      // = 512
    /** The key for the colon key. */
    public static final Key COLON   = new Key(KeyEvent.VK_COLON, true);   // = 513
    /** The key for the dollar key. */
    public static final Key DOLLAR  = new Key(KeyEvent.VK_DOLLAR, true);  // = 515
    /** The key for the plus key. */
    public static final Key PLUS    = new Key(KeyEvent.VK_PLUS, true);    // = 521
    /** The key for the windows key. */
    public static final Key WINDOWS = new Key(KeyEvent.VK_WINDOWS, true); // = 524
    /** The key for the backspace key. */
    public static final Key BACKSPACE   = new Key(KeyEvent.VK_BACK_SPACE, true);  // = 8
    /** The key for the caps lock key. */
    public static final Key CAPS_LOCK   = new Key(KeyEvent.VK_CAPS_LOCK, true);   // = 20
    /** The key for the backslash key. */
    public static final Key BSLASH      = new Key(KeyEvent.VK_BACK_SLASH, true);  // = 92
    /** The key for the print screen  key. */
    public static final Key PRINT_SCREEN = new Key(KeyEvent.VK_PRINTSCREEN, true); // = 154
    /** The key for the left brace key. */
    public static final Key LEFT_BRACE  = new Key(KeyEvent.VK_BRACELEFT, true);   // = 161
    /** The key for the right brace key. */
    public static final Key RIGHT_BRACE = new Key(KeyEvent.VK_BRACERIGHT, true);  // = 161
    /** The key for the backwards single quote (back tick) key. */
    public static final Key BACK_QUOTE  = new Key(KeyEvent.VK_BACK_QUOTE, true);  // = 192
    /** The key for the circumflex key. */
    public static final Key CIRCUMFLEX  = new Key(KeyEvent.VK_CIRCUMFLEX, true);  // = 514
    /** The key for the euro key. */
    public static final Key EURO        = new Key(KeyEvent.VK_EURO_SIGN, true);   // = 516
    /** The key for the hashtag key, or number sign key. */
    public static final Key NUMBER_SIGN = new Key(KeyEvent.VK_NUMBER_SIGN, true); // = 520
    /** The key for the underscore key. */
    public static final Key UNDERSCORE  = new Key(KeyEvent.VK_UNDERSCORE, true);  // = 523
    /** The key for the exclamation mark key. */
    public static final Key EXCLAMATION_MARK  = new Key(KeyEvent.VK_EXCLAMATION_MARK, true);  // = 517
    /** The key for the left parenthesis key. */
    public static final Key LEFT_PARENTHESIS  = new Key(KeyEvent.VK_LEFT_PARENTHESIS, true);  // = 519
    /** The key for the right parenthesis key. */
    public static final Key RIGHT_PARENTHESIS = new Key(KeyEvent.VK_RIGHT_PARENTHESIS, true); // = 522
    /** The key for the inverted exclamation mark key. */
    public static final Key INVERTED_EXCLAMATION_MARK = new Key(KeyEvent.VK_INVERTED_EXCLAMATION_MARK, true); // = 518
    
    // Masks
    /** The mask for the shift key. */
    public static final int SHIFT_MASK = InputEvent.SHIFT_DOWN_MASK;     // =      0b0000_0100_0000
    /** The mask for the control key. */
    public static final int CTRL_MASK  = InputEvent.CTRL_DOWN_MASK;      // =      0b0000_1000_0000
    /** The mask for the meta key. */
    public static final int META_MASK  = InputEvent.META_DOWN_MASK;      // =      0b0001_0000_0000
    /** The mask for the alt key. */
    public static final int ALT_MASK   = InputEvent.ALT_DOWN_MASK;       // =      0b0010_0000_0000
    /** The mask for the alt graph key. */
    public static final int ALT_GRAPH  = InputEvent.ALT_GRAPH_DOWN_MASK; // = 0b0010_0000_0000_0000
    
    // Default values
    /** The default mask. */
    public static final int DEFAULT_MASK = 0;
    /** The default value for key release. */
    public static final boolean DEFAULT_KEY_RELEASE = false;
    /** The default mutable value. */
    public static final boolean DEFAULT_MUTABLE = false;
    
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** Whether the key is immutable or not. */
    private final boolean immutable;
    
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
     * Converts a key event to an immutable key.
     * 
     * @param e the key event.
     * @param onKeyRelease whether the key was released or pressed.
     */
    public Key(KeyEvent e, boolean onKeyRelease) {
        this(e.getExtendedKeyCode(), e.getModifiersEx(), onKeyRelease, true);
    }
    
    /**
     * Clones the given key.
     */
    public Key(Key key) {
        this(key.key, key.mask, key.onKeyRelease, key.immutable);
    }
    
    /**
     * Creates a new mutable key.
     * 
     * @see #Key(boolean, int, int, boolean)
     */
    public Key(int key) {
        this(key, false);
    }
    
    /**
     * Creates a new mutable key.
     * 
     * @see #Key(boolean, int, int, boolean)
     */
    public Key(int key, int mask) {
        this(key, mask, DEFAULT_KEY_RELEASE, DEFAULT_MUTABLE);
    }
    
    /**
     * Creates a new mutable key.
     * 
     * @see #Key(boolean, int, int, boolean)
     */
    public Key(int key, boolean onKeyRelease) {
        this(key, DEFAULT_MASK, onKeyRelease, DEFAULT_MUTABLE);
    }
    
    /**
     * Creates a new mutable key.
     * 
     * @see #Key(boolean, int, int, boolean)
     */
    public Key(int key, int mask, boolean onKeyRelease) {
        this(key, mask, onKeyRelease, DEFAULT_MUTABLE);
    }
    
    /**
     * Creates a new key.
     * 
     * @param key Value for this key.
     * @param modifier The modifier of the key.
     * @param onKeyRelease Whether the key was released or pressed.
     * @param immutable Whether this key should be immutable.
     */
    public Key(int key, int mask, boolean onKeyRelease, boolean immutable) {
        this.key = key;
        this.mask = mask;
        this.onKeyRelease = onKeyRelease;
        this.immutable = immutable;
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
     * Sets the mask of this key or having the same mask value. <br>
     * If the current key is not immutable, then the value of {@code this}
     * is changed. Otherwise, a new object is created with the same
     * values as {@code this}, but with the new mask set.
     * 
     * @return A key having the same key value as {@code key} and the given mask.
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
     * Adds the mask to the key. The mask is added with the OR-operation. <br>
     * If the current key is not immutable, then the value of {@code this}
     * is changed. Otherwise, a new object is created with the same
     * values as {@code this}, but with the new mask applied.
     * 
     * @return A key having the same key value as {@code key},
     *     but with the new generated mask.
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
     * Sets the key value of {@code this} key. <br>
     * If the current key is not immutable, then the value of {@code this}
     * is changed. Otherwise, a new object is created with the same
     * values as {@code this}, but with the new key value set.
     * 
     * @return A key having the same properties as {@code this}, but
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
     * Sets the on key release value. <br>
     * If the current key is not immutable, then the value of {@code this}
     * is changed. Otherwise, a new object is created with the same
     * values as {@code this}, but with the new on key release value set.
     * 
     * @return A new key having the same properties as {@code this}, but
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
     * {@inheritDoc}
     * 
     * @see #Key(Key)
     */
    @Override
    public Key clone() {
        return new Key(this);
    }
    
    /**
     * {@inheritDoc}
     * 
     * Checks if two keys are identical, i.e. the {@link #key}, {@link #mask},
     * {@link #onKeyRelease} and {@link #immutable} fields are compared. <br>
     * <br>
     * Also returns {@code true} if:
     * <ul>
     *   <li> {@code obj} is a {@link Number}, and the integer value of
     *        this number is equal to the key value. </li>
     *   <li> {@code obj} is a {@link KeyStroke}, and the key and mask
     *        value are equal. </li>
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
        }
        
        return false;
    }
    
    @Override
    public int hashCode() {
        return MultiTool.calcHashCode(new Object[] {key, mask, onKeyRelease});
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
     * with:
     * <ul>
     *   <li> {@code class}: the value of {@code Key.class.getCanonicalName()} </li>
     *   <li> {@code field_x}: the name of field 'x'. </li>
     *   <li> {@code value_x}: the value of field 'x'. </li>
     * </ul>
     * <br>
     * Moreover, the for any non-null key {@code key} holds that: <br>
     * {@code Key.parse(key.toString()).equals(key)}
     * 
     * @param source The string to parse.
     * 
     * @return A {@link Key} object from the parsed string.
     * 
     * @throws ParseException if the given string was invallid.
     * 
     * @see #toString();
     */
    public static Key parse(String source)
            throws ParseException {
        String[] parts = source.split("\\[|\\]");
        if (parts.length != 2) {
            throw new ParseException("Format in invallid. "
                    + "Expected 'class[f1=v1,...,fn=vn]', but found: '"
                    + source + "'", -1);
        }
        if (!Key.class.getCanonicalName().equals(parts[0])) {
            throw new ParseException("Class in string is invallid. "
                    + "Expected: '" + Key.class.getCanonicalName()
                    + "', but found: '" + parts[0] + "'", 0);
        }
        int pos = parts[0].length() + 1;
        parts = parts[1].split(",");
        if (parts.length != 4) {
            throw new ParseException("Format of fields is invallid. "
                    + "Expected 4 fields separated by ',', but found "
                    + parts.length + ". Source: '" + source + "'", pos);
        }
        
        Integer key = null;
        Integer mask = null;
        Boolean onKeyRelease = null;
        Boolean immutable = null;
        for (int i = 0; i < parts.length; pos += parts[i++].length() + 1) {
            String[] split = parts[i].split("=");
            if (split.length != 2) {
                throw new ParseException("Invallid format. Expected: "
                        + "'field=value', but found: '" + parts[i]
                        + "'. Source: '" + source + "'", pos);
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
                throw new ParseException("Could not convert value: '" + value
                        + "'. Source: '" + source + "'",
                        pos + field.length() + 1);
            }
        }
        
        if (key == null) {
            throw new ParseException("The key value field is missing. Source: '"
                    + source + "'", source.length());
        }
        if (mask == null) {
            throw new ParseException("The mask field is missing. Source: '"
                    + source + "'", source.length());
        }
        if (onKeyRelease == null) {
            throw new ParseException("The on key release field is missing. Source: '"
                    + source + "'", source.length());
        }
        if (immutable == null) {
            throw new ParseException("The immutable field is missing. Source: '"
                    + source + "'", source.length());
        }
        
        return new Key(key, mask, onKeyRelease, immutable);
    }
    
    /**
     * Returns a visual repersentation of this key.
     * 
     * @return A visual representation of this key.
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