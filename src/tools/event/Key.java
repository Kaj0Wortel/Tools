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
 * Class containing shorter and clearer naming for keys and masks
 * all under one roof instead of spread out over multiple classes.
 * 
 * @version 1.1
 * @author Kaj Wortel
 */
public class Key
        implements PublicCloneable {
    
    /* -------------------------------------------------------------------------
     * Constants.
     * -------------------------------------------------------------------------
     */
    // Alphabet
    /** The key for the letter `A`. */
    public static final Key A = key(KeyEvent.VK_A); // = 65 = 'A'
    /** The key for the letter `B`. */
    public static final Key B = key(KeyEvent.VK_B); // = 66 = 'B'
    /** The key for the letter `C`. */
    public static final Key C = key(KeyEvent.VK_C); // = 67 = 'C'
    /** The key for the letter `D`. */
    public static final Key D = key(KeyEvent.VK_D); // = 68 = 'D'
    /** The key for the letter `E`. */
    public static final Key E = key(KeyEvent.VK_E); // = 69 = 'E'
    /** The key for the letter `F`. */
    public static final Key F = key(KeyEvent.VK_F); // = 70 = 'F'
    /** The key for the letter `G`. */
    public static final Key G = key(KeyEvent.VK_G); // = 71 = 'G'
    /** The key for the letter `H`. */
    public static final Key H = key(KeyEvent.VK_H); // = 72 = 'H'
    /** The key for the letter `I`. */
    public static final Key I = key(KeyEvent.VK_I); // = 73 = 'I'
    /** The key for the letter `J`. */
    public static final Key J = key(KeyEvent.VK_J); // = 74 = 'J'
    /** The key for the letter `K`. */
    public static final Key K = key(KeyEvent.VK_K); // = 75 = 'K'
    /** The key for the letter `L`. */
    public static final Key L = key(KeyEvent.VK_L); // = 76 = 'L'
    /** The key for the letter `M`. */
    public static final Key M = key(KeyEvent.VK_M); // = 77 = 'M'
    /** The key for the letter `N`. */
    public static final Key N = key(KeyEvent.VK_N); // = 78 = 'N'
    /** The key for the letter `O`. */
    public static final Key O = key(KeyEvent.VK_O); // = 79 = 'O'
    /** The key for the letter `P`. */
    public static final Key P = key(KeyEvent.VK_P); // = 80 = 'P'
    /** The key for the letter `Q`. */
    public static final Key Q = key(KeyEvent.VK_Q); // = 81 = 'Q'
    /** The key for the letter `R`. */
    public static final Key R = key(KeyEvent.VK_R); // = 82 = 'R'
    /** The key for the letter `S`. */
    public static final Key S = key(KeyEvent.VK_S); // = 83 = 'S'
    /** The key for the letter `T`. */
    public static final Key T = key(KeyEvent.VK_T); // = 84 = 'T'
    /** The key for the letter `U`. */
    public static final Key U = key(KeyEvent.VK_U); // = 85 = 'U'
    /** The key for the letter `V`. */
    public static final Key V = key(KeyEvent.VK_V); // = 86 = 'V'
    /** The key for the letter `W`. */
    public static final Key W = key(KeyEvent.VK_W); // = 87 = 'W'
    /** The key for the letter `X`. */
    public static final Key X = key(KeyEvent.VK_X); // = 88 = 'X'
    /** The key for the letter `Y`. */
    public static final Key Y = key(KeyEvent.VK_Y); // = 89 = 'Y'
    /** The key for the letter `Z`. */
    public static final Key Z = key(KeyEvent.VK_Z); // = 90 = 'Z'
    
    // Numbers
    /** The key for the number `0`. */
    public static final Key N0 = key(KeyEvent.VK_0); // = 48 = '0'
    /** The key for the number `1`. */
    public static final Key N1 = key(KeyEvent.VK_1); // = 49 = '1'
    /** The key for the number `2`. */
    public static final Key N2 = key(KeyEvent.VK_2); // = 50 = '2'
    /** The key for the number `3`. */
    public static final Key N3 = key(KeyEvent.VK_3); // = 51 = '3'
    /** The key for the number `4`. */
    public static final Key N4 = key(KeyEvent.VK_4); // = 52 = '4'
    /** The key for the number `5`. */
    public static final Key N5 = key(KeyEvent.VK_5); // = 53 = '5'
    /** The key for the number `6`. */
    public static final Key N6 = key(KeyEvent.VK_6); // = 54 = '6'
    /** The key for the number `7`. */
    public static final Key N7 = key(KeyEvent.VK_7); // = 55 = '7'
    /** The key for the number `8`. */
    public static final Key N8 = key(KeyEvent.VK_8); // = 56 = '8'
    /** The key for the number `9`. */
    public static final Key N9 = key(KeyEvent.VK_9); // = 57 = '9'
    
    // Arrow keys
    /** The key for left arrow key. */
    public static final Key LEFT  = key(KeyEvent.VK_LEFT);  // = 37 = '%'
    /** The key for upper arrow key. */
    public static final Key UP    = key(KeyEvent.VK_UP);    // = 38 = '&'
    /** The key for right arrow key. */
    public static final Key RIGHT = key(KeyEvent.VK_RIGHT); // = 39 = '''
    /** The key for lower arrow key. */
    public static final Key DOWN  = key(KeyEvent.VK_DOWN);  // = 40 = '('
    
    // Function keys
    /** The key for the F1 function key. */
    public static final Key F1  = key(KeyEvent.VK_F1);  // = 112
    /** The key for the F2 function key. */
    public static final Key F2  = key(KeyEvent.VK_F2);  // = 113
    /** The key for the F3 function key. */
    public static final Key F3  = key(KeyEvent.VK_F3);  // = 114
    /** The key for the F4 function key. */
    public static final Key F4  = key(KeyEvent.VK_F4);  // = 115
    /** The key for the F5 function key. */
    public static final Key F5  = key(KeyEvent.VK_F5);  // = 116
    /** The key for the F6 function key. */
    public static final Key F6  = key(KeyEvent.VK_F6);  // = 117
    /** The key for the F7 function key. */
    public static final Key F7  = key(KeyEvent.VK_F7);  // = 118
    /** The key for the F8 function key. */
    public static final Key F8  = key(KeyEvent.VK_F8);  // = 119
    /** The key for the F9 function key. */
    public static final Key F9  = key(KeyEvent.VK_F9);  // = 120
    /** The key for the F10 function key. */
    public static final Key F10 = key(KeyEvent.VK_F10); // = 121
    /** The key for the F11 function key. */
    public static final Key F11 = key(KeyEvent.VK_F11); // = 122
    /** The key for the F12 function key. */
    public static final Key F12 = key(KeyEvent.VK_F12); // = 123
    /** The key for the F13 function key. */
    public static final Key F13 = key(KeyEvent.VK_F13); // = 61440
    /** The key for the F14 function key. */
    public static final Key F14 = key(KeyEvent.VK_F14); // = 61441
    /** The key for the F15 function key. */
    public static final Key F15 = key(KeyEvent.VK_F15); // = 61442
    /** The key for the F16 function key. */
    public static final Key F16 = key(KeyEvent.VK_F16); // = 61443
    /** The key for the F17 function key. */
    public static final Key F17 = key(KeyEvent.VK_F17); // = 61444
    /** The key for the F18 function key. */
    public static final Key F18 = key(KeyEvent.VK_F18); // = 61445
    /** The key for the F19 function key. */
    public static final Key F19 = key(KeyEvent.VK_F19); // = 61446
    /** The key for the F20 function key. */
    public static final Key F20 = key(KeyEvent.VK_F20); // = 61447
    /** The key for the F21 function key. */
    public static final Key F21 = key(KeyEvent.VK_F21); // = 61448
    /** The key for the F22 function key. */
    public static final Key F22 = key(KeyEvent.VK_F22); // = 61449
    /** The key for the F23 function key. */
    public static final Key F23 = key(KeyEvent.VK_F23); // = 61450
    /** The key for the F24 function key. */
    public static final Key F24 = key(KeyEvent.VK_F24); // = 61451
    
    // Other keys
    /** The key for the enter key. */
    public static final Key ENTER   = key(KeyEvent.VK_ENTER);   // = 10
    /** The key for the shift key. */
    public static final Key SHIFT   = key(KeyEvent.VK_SHIFT);   // = 16
    /** The key for the control key. */
    public static final Key CTRL    = key(KeyEvent.VK_CONTROL); // = 17
    /** The key for the alt key. */
    public static final Key ALT     = key(KeyEvent.VK_ALT);     // = 18
    /** The key for the escape key. */
    public static final Key ESC     = key(KeyEvent.VK_ESCAPE);  // = 27
    /** The key for the space bar key. */
    public static final Key SPACE   = key(KeyEvent.VK_SPACE);   // = 32 = ' '
    /** The key for the end key. */
    public static final Key END     = key(KeyEvent.VK_END);     // = 35
    /** The key for the home key. */
    public static final Key HOME    = key(KeyEvent.VK_HOME);    // = 36
    /** The key for the comma key. */
    public static final Key COMMA   = key(KeyEvent.VK_COMMA);   // = 44
    /** The key for the minus key. */
    public static final Key MINUS   = key(KeyEvent.VK_MINUS);   // = 45 = '-'
    /** The key for the period key. */
    public static final Key PERIOD  = key(KeyEvent.VK_PERIOD);  // = 46
    /** The key for the slash key. */
    public static final Key SLASH   = key(KeyEvent.VK_SLASH);   // = 47
    /** The key for the equals key. */
    public static final Key EQUAL   = key(KeyEvent.VK_EQUALS);  // = 61 = '='
    /** The key for the delete key. */
    public static final Key DEL     = key(KeyEvent.VK_DELETE);  // = 127
    /** The key for the left angular bracket key, or less than key. */
    public static final Key LESS    = key(KeyEvent.VK_LESS);    // = 153
    /** The key for the insert key. */
    public static final Key INSERT  = key(KeyEvent.VK_INSERT);  // = 155
    /** The key for the right angular bracket key, or greater than key. */
    public static final Key GREATER = key(KeyEvent.VK_GREATER); // = 160
    /** The key for the single quote key. */
    public static final Key QUOTE   = key(KeyEvent.VK_QUOTE);   // = 222
    /** The key for the at key. */
    public static final Key AT      = key(KeyEvent.VK_AT);      // = 512
    /** The key for the colon key. */
    public static final Key COLON   = key(KeyEvent.VK_COLON);   // = 513
    /** The key for the dollar key. */
    public static final Key DOLLAR  = key(KeyEvent.VK_DOLLAR);  // = 515
    /** The key for the plus key. */
    public static final Key PLUS    = key(KeyEvent.VK_PLUS);    // = 521
    /** The key for the windows key. */
    public static final Key WINDOWS = key(KeyEvent.VK_WINDOWS); // = 524
    /** The key for the backspace key. */
    public static final Key BACKSPACE   = key(KeyEvent.VK_BACK_SPACE);  // = 8
    /** The key for the caps lock key. */
    public static final Key CAPS_LOCK   = key(KeyEvent.VK_CAPS_LOCK);   // = 20
    /** The key for the backslash key. */
    public static final Key BSLASH      = key(KeyEvent.VK_BACK_SLASH);  // = 92
    /** The key for the print screen  key. */
    public static final Key PRINT_SCREEN = key(KeyEvent.VK_PRINTSCREEN); // = 154
    /** The key for the left brace key. */
    public static final Key LEFT_BRACE  = key(KeyEvent.VK_BRACELEFT);   // = 161
    /** The key for the right brace key. */
    public static final Key RIGHT_BRACE = key(KeyEvent.VK_BRACERIGHT);  // = 161
    /** The key for the backwards single quote (back tick) key. */
    public static final Key BACK_QUOTE  = key(KeyEvent.VK_BACK_QUOTE);  // = 192
    /** The key for the circumflex key. */
    public static final Key CIRCUMFLEX  = key(KeyEvent.VK_CIRCUMFLEX);  // = 514
    /** The key for the euro key. */
    public static final Key EURO        = key(KeyEvent.VK_EURO_SIGN);   // = 516
    /** The key for the hashtag key, or number sign key. */
    public static final Key NUMBER_SIGN = key(KeyEvent.VK_NUMBER_SIGN); // = 520
    /** The key for the underscore key. */
    public static final Key UNDERSCORE  = key(KeyEvent.VK_UNDERSCORE);  // = 523
    /** The key for the exclamation mark key. */
    public static final Key EXCLAMATION_MARK  = key(KeyEvent.VK_EXCLAMATION_MARK);  // = 517
    /** The key for the left parenthesis key. */
    public static final Key LEFT_PARENTHESIS  = key(KeyEvent.VK_LEFT_PARENTHESIS);  // = 519
    /** The key for the right parenthesis key. */
    public static final Key RIGHT_PARENTHESIS = key(KeyEvent.VK_RIGHT_PARENTHESIS); // = 522
    /** The key for the inverted exclamation mark key. */
    public static final Key INVERTED_EXCLAMATION_MARK = key(KeyEvent.VK_INVERTED_EXCLAMATION_MARK); // = 518
    
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
    public static final boolean DEFAULT_IMMUTABLE = false;
    
    
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
     * @see #Key(int, int, boolean, boolean)
     */
    public Key(int key) {
        this(key, false);
    }
    
    /**
     * Creates a new mutable key.
     * 
     * @see #Key(int, int, boolean, boolean)
     */
    public Key(int key, int mask) {
        this(key, mask, DEFAULT_KEY_RELEASE, DEFAULT_IMMUTABLE);
    }
    
    /**
     * Creates a new mutable key.
     * 
     * @see #Key(int, int, boolean, boolean)
     */
    public Key(int key, boolean onKeyRelease) {
        this(key, DEFAULT_MASK, onKeyRelease, DEFAULT_IMMUTABLE);
    }
    
    /**
     * Creates a new mutable key.
     * 
     * @see #Key(int, int, boolean, boolean)
     */
    public Key(int key, int mask, boolean onKeyRelease) {
        this(key, mask, onKeyRelease, DEFAULT_IMMUTABLE);
    }
    
    /**
     * Creates a new key.
     * 
     * @param key Value for this key.
     * @param mask The mask of the key.
     * @param onKeyRelease Whether the key was released or pressed.
     * @param immutable Whether this key should be immutable.
     */
    public Key(int key, int mask, boolean onKeyRelease, boolean immutable) {
        this.key = key;
        this.mask = mask;
        this.onKeyRelease = onKeyRelease;
        this.immutable = immutable;
    }
    
    /**
     * Shorthand to create a new immutable key.
     * 
     * @param key The value for the key.
     * 
     * @return An immutable key with the default settings.
     */
    private static Key key(int key) {
        return new Key(key, DEFAULT_MASK, DEFAULT_KEY_RELEASE, true);
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