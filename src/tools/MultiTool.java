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
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;


// Tools imports
import tools.data.array.ArrayTools;
import tools.data.Wrapper;
import tools.event.Key;
import tools.font.FontLoader;
import tools.log.FileLogger;
import tools.log.Logger;
import tools.log.MultiLogger;
import tools.log.ScreenLogger;


/**
 * This tool class contains handy methods which don't belong somewhere specific.
 * 
 * @todo
 * Take a look at the {@link #deepClone(Object)} and {@link #deepArrayClone(Object[])} functions
 * (maybe rename the latter to {@code deepCloneArray}).
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public final class MultiTool {
    
    /* -------------------------------------------------------------------------
     * Constants.
     * -------------------------------------------------------------------------
     */
    /** The space character to be used in HTML text. */
    public static final String HTML_SPACE = new String(new char[] {0x00A0});
    /** The initial value of the hash for merging hash values of arrays. */
    private static final int HASH_CODE_INIT = 41;
    /** The multiplication value of the hash for merging hash values of arrays. */
    private static final int HASH_MUL = 37;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * This is a static singleton class. No instances should be made.
     *
     * @deprecated No instances should be made.
     */
    @Deprecated
    private MultiTool() { }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Sleeps the current thread.
     * Mainly used to avoid the annoying catch statement.
     *
     * @param time Time in ms that the thread sleeps.
     *
     * @see Thread#sleep(long)
     */
    public static void sleepThread(long time) {
        try {
            Thread.sleep(time);
            
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }
    
    /**
     * Fires an ActionEvent for all ActionListeners in the array. <br>
     * Uses another thread for execution to lighten the load of the swing thread. <br>
     * Uses as time the current time stamp.
     *
     * @param source The source of the event.
     * @param command The command used for the event.
     * @param modifiers The modifiers for the event.
     * @param als Array containing the ActionListeners that
     *     need to be notified of the event.
     *
     * @see #fireActionEvents(Object, String, long, int, ActionListener[])
     */
    public static void fireActionEvents(Object source, String command,
            int modifiers, ActionListener[] als) {
        fireActionEvents(source, command, System.currentTimeMillis(), modifiers, als);
    }
    
    /**
     * Fires an ActionEvent for all ActionListeners currently listening. <br>
     * Uses another thread for execution to lighten the load of the swing thread.
     *
     * @param source The source of the event.
     * @param command The command used for the event.
     * @param when The time when the event occured.
     * @param modifiers The modifiers for the event.
     * @param als Array containing the ActionListeners that
     *     need to be notified of the event.
     */
    public static void fireActionEvents(Object source, String command,
            long when, int modifiers, ActionListener[] als) {
        if (als == null) return;
        
        new Thread(source.getClass().getName() + " ActionEvent") {
            @Override
            public void run() {
                ActionEvent e = new ActionEvent(source,
                        ActionEvent.ACTION_PERFORMED, command, when, modifiers);
                for (ActionListener al : als) {
                    if (al == null) continue;
                    al.actionPerformed(e);
                }
            }
        }.start();
    }
    
    /**
     * Converts a double to a String, having 'decimals' decimals.
     *
     * @param num The number to be converted.
     * @param decimals Non-zero non-negative number of decimals.
     * 
     * @return A String representation of a double, having {@code decimals} decimals.
     */
    public static String doubleToStringDecimals(double num, int decimals) {
        if (decimals < 0) {
            throw new IllegalArgumentException(
                    "Number of decimals was negative: " + decimals);
        }
        
        String number = Double.toString(num);
        for (int i = 0; i < number.length(); i++) {
            if (number.charAt(i) == '.') {
                if (decimals == 0) {
                    return number.substring(0, i);

                } else if (number.length() > i + decimals) {
                    return number.substring(0, i + decimals + 1);
                    
                } else {
                    StringBuilder sb = new StringBuilder(number);
                    while (sb.length() < i + decimals + 1) {
                        sb.append("0");
                    }
                    return sb.toString();
                }
            }
        }
        
        // '.' was not found
        StringBuilder sb = new StringBuilder(number);
        sb.append('.');
        for (int i = 0; i < decimals; i++) {
            sb.append('0');
        }
        
        return sb.toString();
    }
    
    /**
     * Converts an Integer to a String, with zero's filled
     * till the n'th position. <br>
     * If the length of the number is bigger than n, then the full number
     * is returned.
     *
     * @param num The number to be converted.
     * @param len The length of the number + number of leading zeroes.
     *
     * @return The given integer converted to a string in base 10, and
     *     is left filled with zeroes such that the total length of the
     *     returning string is {@code n}
     * 
     * @see #fillZero(int, int, int)
     */
    public static String fillZero(int num, int len)
            throws NumberFormatException {
        return fillZero(num, len, 10);
    }
    
    /**
     * Converts an Integer to a String, with zero's filled
     * till the n'th position. <br>
     * If the length of the number is bigger than n, then the full number
     * is returned.
     *
     * @param num The number to be converted.
     * @param len The length of the number + number of leading zeroes.
     * @param base The base to convert the number to.
     *
     * @return The given integer converted to a string in base {@code base},
     *     and is left filled with zeroes such that the total length of
     *     the returning string is {@code n}
     */
    public static String fillZero(int num, int len, int base)
            throws NumberFormatException {
        return fillLeft(Integer.toString(num, base), len, '0');
    }
    
    /**
     * Adds spaces to the left of the given text till the total length
     * of the text is equal to the given size. <br>
     * If the initial length of the text is bigger than the given size,
     * no action is taken.
     *
     * @param text The text to process.
     * @param len The target length of the text.
     * 
     * @return The input string, left filled with spaces.
     */
    public static String fillSpaceLeft(String text, int len) {
        return fillLeft(text, len, ' ');
    }
    
    /**
     * Adds spaces to the right of the given text till the total length
     * of the text is equal to the given size.
     * If the initial length of the text is bigger than the given size,
     * no action is taken.
     *
     * @param text The text to process.
     * @param len The target length of the text.
     * 
     * @return The input string, right filled with spaces.
     */
    public static String fillSpaceRight(String text, int len) {
        return fillRight(text, len, ' ');
    }
    
    /**
     * Fills the given string on the left with the given fill character {@code fill}.
     * If the initial length of the text is bigger than the given size,
     * no action is taken.
     * 
     * @param text The text to add the filling to.
     * @param len The target length of the text.
     * @param fill The char used for filling the text.
     * 
     * @return The given text, left filled with the given char {@code fill}
     *     until the target length has been reached.
     */
    public static String fillLeft(String text, int len, char fill) {
        if (text.length() >= len) return text;
        StringBuilder sb = new StringBuilder();
        for (int i = text.length(); i < len; i++) {
            sb.append(fill);
        }
        sb.append(text);
        return sb.toString();
    }
    
    /**
     * Fills the given string on the right with the given fill character {@code fill}.
     * If the initial length of the text is bigger than the given size,
     * no action is taken.
     * 
     * @param text The text to add the filling to.
     * @param len The target length of the text.
     * @param fill The char used for filling the text.
     * 
     * @return The given text, right filled with the given char {@code fill}
     *     until the target length has been reached.
     */
    public static String fillRight(String text, int len, char fill) {
        if (text.length() >= len) return text;
        StringBuilder sb = new StringBuilder(text);
        for (int i = text.length(); i < len; i++) {
            sb.append(fill);
        }
        return sb.toString();
    }
    
    /**
     * Fills the given string on the left and right with the given fill
     * character {@code fill}. The amount of added characters on the left
     * and right side will differ at most {@code 1} (so {@code |#left - #right| <= 1}),
     * where {@code #left <= #right} If the initial length of the text is bigger
     * than the given size, no action is taken.
     * 
     * @param text The text to add the filling to.
     * @param len The target length of the text.
     * @param fill The char used for filling the text.
     * 
     * @return The given text, left and right filled with the given char
     *     {@code fill} until the target length has been reached.
     */
    public static String fillCenter(String text, int len, char fill) {
        if (text.length() >= len) return text;
        StringBuilder sb = new StringBuilder();
        int amt = len - text.length();
        int left = amt / 2;
        int right = amt - left;
        
        for (int i = 0; i < left; i++) {
            sb.append(fill);
        }
        sb.append(text);
        for (int i = 0; i < right; i++) {
            sb.append(fill);
        }
        return sb.toString();
    }
    
    /**
     * Approximates the width of a string rendered with the given font.
     *
     * @param text The text to render.
     * @param font The used font.
     * 
     * @return An approximation of the width of the string.
     *     Assumes normal English language as input string.
     */
    public static double calcStringWidth(String text, Font font) {
            FontRenderContext frc = new FontRenderContext(font.getTransform(), true, true);
            return 1.1 * font.getStringBounds(text, frc).getWidth();
    }
    
    /**
     * Converts all spaces in the input String to spaces that
     * are visible in html.
     *
     * @param text The text to process.
     */
    public static String toHTMLSpace(String text) {
        return text.replaceAll(" ", HTML_SPACE);
    }
    
    /**
     * Checks whether the provided string can be converted to a number. <br>
     * <br>
     * It is allowed to pass an empty or null string as argument.
     * The result in this case will be {@code false}.
     * 
     * @param str The string to check.
     * @param base The base of the possible number in the string.
     *     It must hold that {@code 2 <= base <= 36}.
     * 
     * @return {@code true} if the string only contains numbers in the given base.
     *     {@code false} otherwise.
     */
    public static boolean isNumber(String str, int base) {
        if (str == null || str.length() == 0) return false;
        boolean dotFound = false;
        
        for (int i = 0; i < str.length(); i++) {
            if (!isNumber(str.charAt(i), base)) {
                if (!dotFound && str.charAt(i) == '.') {
                    dotFound = true;
                    
                } else {
                    return false;
                }
            }

        }
        
        return true;
    }
    
    /**
     * Checks whether the given character is a number in the given base. <br>
     * For the base must hold that {@code 2 <= base <= 36}.
     * 
     * @param n The character to check.
     * @param base The base of the character to check.
     * 
     * @return {@code true} if the given char represents a digit in the given base.
     */
    public static boolean isNumber(char n, int base) {
        if (base < 2) {
            throw new IllegalArgumentException("Expected 2 <= base <= 26, but found base: " + base);
            
        } else if (base <= 10) {
            return n >= '0' && n <= '0' + base - 1;
            
        } else if (base <= 36) {
            return (n >= '0' && n <= '9') ||
                    (n >= 'a' && n <= 'a' + base - 1) ||
                    (n >= 'A' && n <= 'A' + base - 1);
        } else {
            throw new IllegalArgumentException("Expected 2 <= base <= 36, but found base: " + base);
        }
    }
    
    /**
     * Calculates the power of a base. <br>
     * Calculates the power significantly faster compared to
     * {@link Math#pow(double, double)}, but only accepts integer powers.
     *
     * Benchmarks compared to {@link Math#pow(double, double)}:
     * <ul>
     *   <li> 2.4 times faster for very small powers an bases
     *        (e.g. between -50 and 50). </li>
     *   <li> 2.5 times faster for small powers and bases
     *        (e.g. between -10,000 and +10,000). </li>
     *   <li> 2.5 times faster for medium powers and bases
     *        (e.g. between +-10,000,000 and +-20,000,000). </li>
     *   <li> 2.6 times faster for large powers and bases
     *        (e.g. between +-1,073,741,823 and +-2,147,483,647). </li>
     *   <li> 2.6 times faster for random powers and bases
     *        (e.g. between -2,145,338,308 and +2,145,338,308). </li>
     *   <li> 5.7 times faster for very small powers and large bases
     *        (e.g. powers between -50 and 50, bases between
     *        +-1,073,741,823 and +-2,147,483,647). </li>
     *   <li> 2.6 times faster for large powers and very small bases
     *        (e.g. powers between +-1,073,741,823 and +-2,147,483,647,
     *        bases between -50 and 50). </li>
     * </ul>
     * 
     * @implNote
     * <ul>
     *   <li> The results that did't differ more then 2 of the least significant
     *        digits from the result generated by the other method. </li>
     *   <li> For each test the sample size was 214.748.360. </li>
     * </ul>
     *
     * @param base The base of the operation.
     * @param pow The power of the operation.
     * 
     * @result the result of {@code base ^ pow}.
     */
    public static double intPow(double base, int pow) {
        if (pow > 0) {
            double r = base;
            double f = 1;
            
            while (pow != 1) {
                if (pow % 2 == 0) {
                    r *= r;
                    pow >>= 1;
                    
                } else if (pow % 3 == 0) {
                    r *= r * r;
                    pow /= 3;
                    
                } else if (pow % 5 == 0) {
                    double r2 = r * r;
                    r *= r2 * r2;
                    pow /= 5;
                    
                } else {
                    f *= r;
                    pow -= 1;
                }
            }
            
            return f * r;

        } else if (pow < 0) {
            return 1.0 / intPow(base, -pow);

        } else {
            return 1.0;
        }
    }
    
    // Benchmark function.
    /*
    public static void main(String[] args) {
        int n = Integer.MAX_VALUE / 100;
        System.out.println("n = " + n);
        Random RAN = new Random();
        
        long timeQuick = 0;
        long timeJava = 0;
        
        for (int c = 0; c < 10; c++) {
            double[] bases = new double[n];
            int[] powers = new int[n];
            for (int i = 0; i < n; i++) {
                //bases[i] = RAN.nextDouble() * 100 - 50;
                //powers[i] = RAN.nextInt(100) - 50;
                
                //bases[i] = (RAN.nextBoolean() ? 1 : -1) * (RAN.nextDouble() * 10_000_000 + 10_000_000);
                //powers[i] = (RAN.nextBoolean() ? 1 : -1) * (RAN.nextInt(10_000_000) + 10_000_000);
                
                //bases[i] = (RAN.nextBoolean() ? 1 : -1) * (RAN.nextDouble() * 10_000_000 + 10_000_000);
                //powers[i] = (RAN.nextBoolean() ? 1 : -1) * (RAN.nextInt(10_000_000) + 10_000_000);
                
                //bases[i] = (RAN.nextBoolean() ? 1 : -1) * (RAN.nextDouble() * Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 2);
                //powers[i] = (RAN.nextBoolean() ? 1 : -1) * (RAN.nextInt(Integer.MAX_VALUE / 2 ) + Integer.MAX_VALUE / 2 );
                
                bases[i] = (RAN.nextDouble() * 2 - 1) * (Integer.MAX_VALUE / 1.001);
                powers[i] = (int) ((RAN.nextDouble() * 2 - 1) * (Integer.MAX_VALUE / 1.001));

            }
            
            long beginA = System.currentTimeMillis();
            for (int i = 0; i < n; i++) {
                intPow(bases[i], powers[i]);
            }
            long endA = System.currentTimeMillis();
            long dA = endA - beginA;
            timeQuick += dA;
            System.out.println("intPow: " + dA);
            System.out.println("Total time: " + timeQuick);
            
            
            long beginB = System.currentTimeMillis();
            for (int i = 0; i < n; i++) {
                Math.pow(bases[i], powers[i]);
            }
            long endB = System.currentTimeMillis();
            long dB = endB - beginB;
            timeJava += dB;
            System.out.println("Math.pow: " + dB);
            System.out.println("Total time: " + timeJava);
            
            System.out.println("ratio = " + ((double) dB) / (double) dA);
            System.out.println("-----------------");
        }
        System.out.println("ratio = " + ((double) timeJava) / (double) timeQuick);
    }*/

    /**
     * Makes a deep clone of the given value.
     * If the value is an array, recursivly make a clone of each element
     * and put them in a new array.
     *
     * @apiNote
     * Supported types:
     * <ul>
     *   <li> {@code value} contains tools.Cloneable elements, then each element is
     *        simply cloned by invoking the clone method. </li>
     *   <li> {@code value} contains java.lang.Cloneable elements, then each element
     *        is cloned by bypassing the private access modifier of clone method.
     *        Note that the clone method should be overridden by that class. </li>
     *   <li> {@code value} contains a primitive data type (boolean, char, byte,
     *        short, int, long, float, double) or is a String. </li>
     * </ul>
     *
     * @param <V> The type of the value to clone.
     * @param value The value to be cloned.
     * 
     * @return a clone of the value. This means that {@code value != \return
     *     && value.equals(\return)} will hold afterwards (assuming that the
     *     equals method is implemented correctly).
     * 
     * @throws IllegalStateException If the clone method could not terminate normally.
     * @throws UnsupportedOperationException If the provided value does not contain
     *     one of the supported types.
     * 
     * @deprecated One should clone the array type specific to reduce the runtime significantly.
     *     This is because array creation via reflect is very inefficient since it uses native
     *     code, hence it cannot be optimized via the JIT. Moreover, a lot of cases are not
     *     supported.
     */
    @Deprecated
    @SuppressWarnings({"BooleanConstructorCall", "BoxingBoxedValue",
        "RedundantStringConstructorCall", "deprecation"})
    public static <V> V deepClone(V value)
            throws IllegalStateException, UnsupportedOperationException {
        if (value == null) return null;
        
        if (value.getClass().isArray()) {
            //return (V) deepArrayClone(safeObjArrCast(value));
            throw new RuntimeException("TODO"); // TODO
            
        } else {
            if (value instanceof tools.PublicCloneable) {
                return (V) ((PublicCloneable) value).clone();
                
            } else if (value instanceof java.lang.Cloneable) {
                try {
                    Method clone = value.getClass().getMethod("clone");
                    clone.setAccessible(false);
                    return (V) clone.invoke(value);
                    
                } catch (NoSuchMethodException |
                         SecurityException |
                         IllegalAccessException e) {
                    // When the method was not reacheable.
                    Logger.write(new Object[] {
                        "Unaccessable clone method of object \""
                            + value.toString() + "\"!", e
                    });
                    
                    throw new IllegalStateException
                        ("Could not finish cloning! Last element: "
                             + value.toString());
                    
                } catch (InvocationTargetException e) {
                    // When the clone method threw an exception.
                    Logger.write(new Object[] {
                        "An error occured while cloning the object \""
                            + value.toString() + "\":", e.getCause()
                    });
                    
                    throw new IllegalStateException
                        ("An error occured while cloning the object \""
                            + value.toString() + "\".");
                }
                
            } else {
                if (value instanceof Boolean) return (V) new Boolean((Boolean) value);
                if (value instanceof Character) return (V) new Character((Character) value);
                if (value instanceof Byte) return (V) new Byte((Byte) value);
                if (value instanceof Short) return (V) new Short((Short) value);
                if (value instanceof Integer) return (V) new Integer((Integer) value);
                if (value instanceof Long) return (V) new Long((Long) value);
                if (value instanceof Float) return (V) new Float((Float) value);
                if (value instanceof Double) return (V) new Double((Double) value);
                if (value instanceof String)return (V) new String((String) value);
                
                // For anything else.
                throw new UnsupportedOperationException
                    ("Expected a cloneable object, but found: " + value.getClass().toString());
            }
        }
    }
    
    /**
     * Makes a deep clone of an array.
     * 
     * @param <T> The type of the array.
     * 
     * @param objArr The array to clone.
     * 
     * @return A deep clone of the given array.
     * 
     * @throws IllegalStateException If the clone method could not terminate normally.
     * @throws UnsupportedOperationException If the provided value does not contain
     *     one of the supported types.
     * 
     * @deprecated One should clone the array type specific to reduce the runtime significantly.
     *     This is because array creation via reflect is very inefficient since it uses native
     *     code, hence it cannot be optimized via the JIT.
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    public static <T> T[] deepArrayClone(T[] objArr)
            throws IllegalStateException, IllegalArgumentException {
        T[] newObjArr = (T[]) Array.newInstance(objArr.getClass().getComponentType(),
                objArr.length);
        for (int i = 0; i < objArr.length; i++) {
            newObjArr[i] = (T) deepClone(objArr[i]);
        }
        
        return (T[]) newObjArr;
    }
    
    /**
     * Calculates a hash code for the given object. <br>
     * This function will always return the same value in the following case:
     * <ul>
     *   <li> If the provided object is not an array, then the provided object
     *        is equal according to the {@link Object#equals(Object)} contract. </li>
     *   <li> If the provided object is an array, then these rules also also
     *        hold for each element in the array. </li>
     *   <li> If the provided object is an array, then the order of the
     *        elements is equal.
     * </ul>
     * 
     * @implNote
     * <ul>
     *   <li> The calculation of the hash code of primitive types is handled
     *        by the {@code calcHashCode(prim p)} functions instead of the default
     *        {@code Prim.hashCode(prim p)} funcion. </li>
     *   <li> If the provided object is an array, then the hash code might differ
     *        if the order of the elements is different. </li>
     * </ul>
     * 
     * @param obj The object to determine the hash code of.
     * 
     * @return A hash code for the given object.
     * 
     * @see #calcHashCode(boolean)
     * @see #calcHashCode(byte)
     * @see #calcHashCode(short)
     * @see #calcHashCode(char)
     * @see #calcHashCode(int)
     * @see #calcHashCode(long)
     * @see #calcHashCode(float)
     * @see #calcHashCode(double)
     */
    public static int calcHashCode(Object obj) {
        if (obj == null) return 0;
        
        if (obj.getClass().isArray()) {
            int result = HASH_CODE_INIT;
            
            Wrapper<Object[]> arr = new Wrapper(obj);
            for (int i = 0; i < arr.length(); i++) {
                result = HASH_MUL * result + calcHashCode(arr.get(i));
            }
            return result;
            
        } else {
            if (obj instanceof Boolean) return calcHashCode((boolean) obj);
            else if (obj instanceof Byte) return calcHashCode((byte) obj);
            else if (obj instanceof Short) return calcHashCode((short) obj);
            else if (obj instanceof Character) return calcHashCode((char) obj);
            else if (obj instanceof Integer) return calcHashCode((int) obj);
            else if (obj instanceof Long) return calcHashCode((long) obj);
            else if (obj instanceof Float) return calcHashCode((float) obj);
            else if (obj instanceof Double) return calcHashCode((double) obj);
            else return obj.hashCode();
        }
    }
    
    /**
     * Calculates the hash code of a boolean. <br>
     * Differs from {@link Boolean#hashCode(boolean)} by increasing the
     * distance between possible hash values and evades the value 0. <br>
     * The runtimes of these two functions are equal.
     * 
     * @param b The boolean to calculates the hash code of.
     * 
     * @return The hash code of the provided value.
     */
    public static int calcHashCode(boolean b) {
        return (b ? 0x70FFFFFF : 0x000000FF);
    }
    
    /**
     * Calculates the hash code of a byte. <br>
     * Differs from {@link Byte#hashCode(byte)} by increasing the
     * distance between possible hash values and evades the value 0. <br>
     * This function should only be used when the hash code of an array
     * must be determined, as it is slower but reduces collisions.
     * 
     * @implNote
     * <ul>
     *   <li> This function is (slightly) slower compared to the java
     *        {@link Byte#hashCode(byte)} function. </li>
     *   <li> The only operations used in this function are {@code &},
     *        {@code <<} and {@code |} to preserve performance. </li>
     * </ul>
     * 
     * @param b The byte to calculate the hash code of.
     * 
     * @return The hash code of the provided value.
     */
    public static int calcHashCode(byte b) {
        return 1 |
                ((b & 0b0000_0001) << 3) |
                ((b & 0b0000_0010) << 6) |
                ((b & 0b0000_0100) << 9) |
                ((b & 0b0000_1000) << 12) |
                ((b & 0b0001_0000) << 15) |
                ((b & 0b0010_0000) << 18) |
                ((b & 0b0100_0000) << 21) |
                ((b & 0b1000_0000) << 24);
    }
    
    /**
     * Calculates the hash code of a short. <br>
     * Differs from {@link Short#hashCode(short)} by increasing the
     * distance between possible hash values and evades the value 0. <br>
     * This function should only be used when the hash code of an array
     * must be determined, as it is slower but reduces collisions.
     * 
     * @implNote
     * <ul>
     *   <li> This function is (slightly) slower compared to the java
     *        {@link Short#hashCode(short)} function. </li>
     *   <li> The only operations used in this function are {@code &},
     *        {@code <<} and {@code |} to preserve performance. </li>
     * </ul>
     * 
     * @param s The short to calculate the hash code of.
     * 
     * @return The hash code of the provided value.
     */
    public static int calcHashCode(short s) {
        return 1 |
                ((s & 0b0000_0001) << 1) | ((s & 0b0000_0001_0000_0000) << 9) |
                ((s & 0b0000_0010) << 2) | ((s & 0b0000_0010_0000_0000) << 10) |
                ((s & 0b0000_0100) << 3) | ((s & 0b0000_0100_0000_0000) << 11) |
                ((s & 0b0000_1000) << 4) | ((s & 0b0000_1000_0000_0000) << 12) |
                ((s & 0b0001_0000) << 5) | ((s & 0b0001_0000_0000_0000) << 13) |
                ((s & 0b0010_0000) << 6) | ((s & 0b0010_0000_0000_0000) << 14) |
                ((s & 0b0100_0000) << 7) | ((s & 0b0100_0000_0000_0000) << 15) |
                ((s & 0b1000_0000) << 8) | ((s & 0b1000_0000_0000_0000) << 16);
    }
    
    /**
     * Calculates the hash code of a char. <br>
     * Differs from {@link Character#hashCode(char)} by increasing the
     * distance between possible hash values and evades the value 0. <br>
     * This function should only be used when the hash code of an array
     * must be determined, as it is slower but reduces collisions.
     * 
     * @implNote
     * <ul>
     *   <li> This function is (slightly) slower compared to the java
     *        {@link Character#hashCode(char)} function. </li>
     *   <li> The only operations used in this function are {@code &},
     *        {@code <<} and {@code |} to preserve performance. </li>
     * </ul>
     * 
     * @param c The char to calculate the hash code of.
     * 
     * @return The hash code of the provided value.
     */
    public static int calcHashCode(char c) {
        return Character.hashCode(c);
    }
    
    /**
     * Calculates the hash code of a int. <br>
     * Differs from {@link Integer#hashCode(int)} by increasing the
     * distance between possible hash values and evades the value 0. <br>
     * This function should only be used when the hash code of an array
     * must be determined, as it is slower but reduces collisions.
     * 
     * @implNote
     * <ul>
     *   <li> This function is (slightly) slower compared to the java
     *        {@link Integer#hashCode(int)} function. </li>
     *   <li> The only operations used in this function are {@code &},
     *        {@code <<} and {@code |} to preserve performance. </li>
     * </ul>
     * 
     * @param i The int to calculate the hash code of.
     * 
     * @return The hash code of the provided value.
     */
    public static int calcHashCode(int i) {
        return (i << 16) | (i >>> 16);
    }
    
    /**
     * Calculates the hash code of a long. <br>
     * This function is equal to {@link Long#hashCode(long)}.
     * 
     * @param l The long to calculate the hash code of.
     * 
     * @return The hash code of the provided value.
     */
    public static int calcHashCode(long l) {
        return (int) (l ^ (l >>> 32));
    }
    
    /**
     * Calculates the hash code of a float. <br>
     * Differs from {@link Float#hashCode(float)} by increasing the
     * distance between possible hash values and evades the value 0. <br>
     * This function should only be used when the hash code of an array
     * must be determined, as it is slower but reduces collisions.
     * 
     * @implNote
     * This function is (slightly) slower compared to the java
     * {@link Float#hashCode(float)} function as it additionally uses
     * {@link #calcHashCode(int)}.
     * 
     * @param f The float to calculate the hash code of.
     * 
     * @return The hash code of the provided value.
     * 
     * @see #calcHashCode(int)
     */
    public static int calcHashCode(float f) {
        return calcHashCode(Float.floatToIntBits(f));
    }
    
    /**
     * Calculates the hash code of a double. <br>
     * Differs from {@link Double#hashCode(double)} by increasing the
     * distance between possible hash values and evades the value 0. <br>
     * This function should only be used when the hash code of an array
     * must be determined, as it is slower but reduces collisions.
     * 
     * @implNote
     * <ul>
     *   <li> This function is (slightly) slower compared to the java
     *        {@link Double#hashCode(double)} function as it additionally
     *        uses {@link #calcHashCode(long)}. </li>
     * </ul>
     * 
     * @param d The double to calculate the hash code of.
     * 
     * @return The hash code of the provided value.
     * 
     * @see #calcHashCode(long)
     */
    public static int calcHashCode(double d) {
        return calcHashCode(Double.doubleToLongBits(d));
    }
    
    /**
     * Adds the given entries to the map. <br>
     * This function is intended to be used in constructor initialization
     * when passing a fresh map with initial values to the super class.
     * 
     * @apiNote
     * If a key in the provided entries already occurs in the map, then the
     * value will be overwritten by the one provided in the entries array. <br>
     * Moreover, if the same key occurs multiple times in the provided entries,
     * then the resulting map will map this key to the value specified by the
     * key with the highest index in the array of provided entries among the
     * duplicate keys.
     * 
     * @param <K> The class of the key of the map.
     * @param <V> The class of the value of the map.
     * @param <M> The class of the map to add to.
     * 
     * @param map The map to add the entries to.
     * @param entries The entries to be added to the map. The first element
     *     of the pair contains the key, and the second element the value.
     * 
     * @return The original {@code map} with the provided entries added to it.
     */
    public static <K, V, M extends Map<? super K, ? super V>> M addToMap(
            M map, Pair<K, V>... entries) {
        if (entries == null) return map;
        for (Pair<K, V> entry : entries) {
            map.put(entry.getFirst(), entry.getSecond());
        }
        return map;
    }
    
    /**
     * Adds all elements of the given array to the given collection. <br>
     * This function is intended to be used in constructor initialization
     * when passing a fresh collection with initial values to the super class.
     *
     * @param <V> The class type of the array elements.
     * @param <C> The collection class type.
     * 
     * @param col The collection to add the elements to.
     * @param arr The array containing the elements to be added.
     * 
     * @return The original collection {@code col} with the added elements in {@code arr}.
     */
    public static <V, C extends Collection<? super V>> C addToCollection(
            C col, V... arr) {
        if (col == null || arr == null) throw new NullPointerException();
        for (V val : arr) {
            col.add(val);
        }
        return col;
    }
        
    /**
     * Creates an {@link Iterable} from the given {@link Iterator}. <br>
     * The returned object should be used as a 'cast-away shell', as it
     * takes the input directly from the provided iterator. <br>
     * <br>
     * Example:<br>
     * <code>
     * Iterator<Integer> it = ... <br>
     * for (int i : MultiTool.toIterable(it)) { <br>
     *     ... <br>
     * } <br>
     * </code>
     * 
     * @param <V> The type of the iterator.
     * 
     * @param it The iterator.
     * 
     * @return An {@link Iterable} which returns the provided {@link Iterator} as iterator.
     */
    public static <V> Iterable<V> toIterable(Iterator<V> it) {
        return () -> it;
    }
    
    /**
     * Consumer-like interface which includes the source.
     *
     * @param array the array the for each statement will be applied.
     * @param action the action that is executed for each element.
     * 
     * @see Consumer
     */
    @FunctionalInterface
    public static interface SourceConsumer<S, V> {
        
        /**
         * Accepts the value from the given source.
         * 
         * @param source The source of the value.
         * @param value The value to accept.
         */
        public void accept(S source, V value);
        
        
    }
    
    /**
     * Executes an action for each element of a collection.
     * Usage is identical compared to {@link Iterable#forEach(Consumer)}.
     *
     * @param <V> The class of the value.
     * @param <C> The class of the collection.
     * @param col The collection.
     * @param action The action that will be executed for all elements.
     * 
     * @deprecated Replaced by
     * {@link tools.data.array.ArrayTools#forEach(java.lang.Object, java.util.function.Consumer)}
     */
    @Deprecated(forRemoval = true)
    public static <V, C extends Collection<V>> void forEach(C col, SourceConsumer<C, V> action) {
        for (V v : col) {
            action.accept(col, v);
        }
    }
    
    /**
     * Interface that to execute some code to generate
     * an object upon initialization of the constructor.
     *
     * @param <V> The class of the object to generate
     */
    @FunctionalInterface
    public static interface ConstructorExecutor<V> {
        
        /**
         * @return The object to generate.
         */
        public V execute();
        
        
    }
    
    /**
     * Creates a fresh object from the given executor and returns it. <br>
     * This function is intended to be used in constructor initialization
     * when passing a fresh object the super class.
     *
     * @param <V> The class of the value.
     * @param exe The executor used to create the object.
     * 
     * @return The value that was generated by the executor.
     */
    public static <V> V createObject(ConstructorExecutor<V> exe) {
        return exe.execute();
    }
    
    /**
     * Prints the current stack trace on a different thread such that it can
     * be used in swing-environments with short event-bursts without delaying
     * the swing thread. <br>
     * Uses the default error stream to print the stack trace and the current
     * thread to get the stack trace from.
     * 
     * @see #printStackTrace(PrintStream, Thread)
     */
    public static void printStackTrace() {
        printStackTrace(System.err, Thread.currentThread());
    }
    
    /**
     * Prints the current stack trace on a different thread such that it can
     * be used in swing-environments with short event-bursts without delaying
     * the swing thread. <br>
     * Uses the current thread to get the stack trace from.
     * 
     * @param stream The stream that will be used to print the stack trace on.
     * 
     * @see #printStackTrace(PrintStream, Thread)
     */
    public static void printStackTrace(PrintStream stream) {
        printStackTrace(stream, Thread.currentThread());
    }
    
    /**
     * Prints the current stack trace on a different thread such that it can
     * be used in swing-environments with short event-bursts without delaying
     * the swing thread. <br>
     * Uses the default error stream to print the stack trace.
     * 
     * @param thread The thread to print the stack trace of.
     * 
     * @see #printStackTrace(PrintStream, Thread)
     */
    public static void printStackTrace(Thread thread) {
        printStackTrace(System.err, thread);
    }
    
    /**
     * Prints the current stack trace on a different thread such that it can
     * be used in swing-environments with short event-bursts without delaying
     * the swing thread.
     * 
     * @param stream The stream that will be used to print the stack trace on.
     * @param thread The thread to print the stack trace of.
     */
    public static void printStackTrace(PrintStream stream, Thread thread) {
        StackTraceElement[] ste = thread.getStackTrace();
        new Thread() {
            @Override
            public void run() {
                synchronized(stream) {
                    stream.println();
                    stream.println(Arrays.toString(ste).replaceAll(", ", "," + Var.LS));
                }
            }
        }.start();
    }
    
    /**
     * Logs the current stack trace of the current thread.
     */
    public static void logStackTrace() {
        logStackTrace(Thread.currentThread());
    }
    
    /**
     * Logs the current stack trace.
     *
     * @param thread The thread to get the stacktrace of.
     */
    public static void logStackTrace(Thread thread) {
        StackTraceElement[] ste = thread.getStackTrace();
        Logger.write(Var.LS + ArrayTools.toDeepString(ste).replaceAll(", ", "," + Var.LS));
    }
    
    /**
     * Initializes the default loggers. <br>
     * First creates a {@link FileLogger} and set it as default.
     * Then creates a {@link ScreenLogger}, which will also load the fonts.
     *
     * @param file the file the {@link FileLogger} will log to.
     */
    public static void initLogger(File file) {
        // Netbeans is picky over some code later on.
        @SuppressWarnings("UnusedAssignment")
        FileLogger fl = null;
        try {
            Logger.setDefaultLogger(fl = new FileLogger(file));
            
        } catch (IOException e) {
            System.err.println(e);
            System.exit(-1);
        }
        
        Map<Key, Runnable> keyMap = new HashMap<>();
        keyMap.put(Key.ESC, () -> System.exit(0));
        Logger.setDefaultLogger(new MultiLogger(
                new ScreenLogger("Logger", keyMap), fl));
        
        Logger.write("Starting application...", Logger.Type.INFO);
        Logger.setShutDownMessage("Shutting down application...",
                Logger.Type.INFO);
        
        // Wait for the fonts to be fully loaded.
        FontLoader.syncLoad();
    }
    
    /**
     * Checks whether the bounds of the given container have been changed.
     * 
     * @param c The component to check.
     * @param x The new x-coord of {@code c}.
     * @param y The new y-coord of {@code c}.
     * @param width The new width of {@code c}.
     * @param height The new height of {@code c}.
     * 
     * @return {@code true} if at least one of the values has changed.
     *     {@code false} otherwise.
     */
    public static boolean boundsChanged(Container c, int x, int y, int width, int height) {
        return (x != c.getX()) || (y != c.getY()) || (width != c.getWidth()) ||
                (height != c.getHeight());
    }
    
    /**
     * Counts the number of wrapped lines in the given text component. <br>
     * All credits go to:
     * <a href="http://tech.chitgoks.com/2012/03/21/get-line-count-of-jtextarea-including-word-wrap-lines/">
     * blogmeister at tech.chitgoks.com</a>
     * 
     * @param txtComp The component to count the lines of.
     * 
     * @return The number of lines in the component, including wrapped lines.
     *     {@code -1} if the document model was corrupted.
     */
    @SuppressWarnings("deprecation")
    public static int countWrappedLines(JTextComponent txtComp) {
        try {
            int height = txtComp.modelToView(txtComp.getDocument()
                    .getEndPosition().getOffset() - 1).y;
            int fontHeight = txtComp.getFontMetrics(txtComp.getFont()).getHeight();
            return height / fontHeight + 1;
            
        } catch (BadLocationException e) {
            return -1;
        }
    }
    
    /**
     * Calculates whether two rectangles with their corresponding
     * location and size intersect. Boundaries are included.
     *
     * @param x1 The x-coord of rectangle 1.
     * @param y1 The y-coord of rectangle 1.
     * @param w1 The width of rectangle 1.
     * @param h1 The height of rectangle 1.
     * @param x2 The x-coord of rectangle 2.
     * @param y2 The y-coord of rectangle 2.
     * @param w2 The width of rectangle 2.
     * @param h2 The height of rectangle 2.
     * 
     * @return {@code true} if the two rectangle intersect.
     */
    public static boolean intersects(int x1, int y1, int w1, int h1, 
            int x2, int y2, int w2, int h2) {
        return ((x1 <= x2 && x1 + w1 >= x2) ||
                (x1 <= x2 + w2 && x1 + w1 >= x2 + w2)) &&
               ((y1 <= y2 && y1 + h1 >= y2) ||
                (y1 <= y2 + h2 && y1 + h1 >= y2 + h2));
    }
    
    /**
     * Converts a {@code long} to an byte array.
     *
     * @param data The input long.
     * 
     * @return A byte array representing the given {@code data}.
     */
    public static byte[] toBytes(long data) {
        byte[] rtn = new byte[8];
        rtn[0] = (byte) (data >>> 56);
        rtn[1] = (byte) (data >>> 48);
        rtn[2] = (byte) (data >>> 40);
        rtn[3] = (byte) (data >>> 32);
        rtn[4] = (byte) (data >>> 24);
        rtn[5] = (byte) (data >>> 16);
        rtn[6] = (byte) (data >>> 8);
        rtn[7] = (byte) (data & 0xFF);
        return rtn;
    }
    
    /**
     * Converts an array of bytes to a {@code long}.
     *
     * @param data The input data. Must have a length of 8.
     * 
     * @return An {@code long} from the bytes.
     */
    public static long longFromBytes(byte[] data) {
        if (data.length != 8) {
            throw new IllegalArgumentException(
                    "Expected an array of length 8, but found: " + data.length);
        }
        return (((long) (data[0] & 0xFF)) << 56)
                | (((long) (data[1] & 0xFF)) << 48)
                | (((long) (data[2] & 0xFF)) << 40)
                | (((long) (data[3] & 0xFF)) << 32)
                | (((long) (data[4] & 0xFF)) << 24)
                | (((long) (data[5] & 0xFF)) << 16)
                | (((long) (data[6] & 0xFF)) << 8)
                | ((long) (data[7] & 0xFF));
    }
    
    /**
     * Converts an {@code int} to an byte array.
     *
     * @param data The input int.
     * 
     * @return A byte array representing the given {@code data}.
     */
    public static byte[] toBytes(int data) {
        byte[] rtn = new byte[4];
        rtn[0] = (byte) (data >>> 24);
        rtn[1] = (byte) (data >>> 16);
        rtn[2] = (byte) (data >>> 8);
        rtn[3] = (byte) (data & 0xFF);
        return rtn;
    }
    
    /**
     * Converts an array of bytes to an {@code int}.
     *
     * @param data The input data. Must have a length of 4.
     * 
     * @return An {@code int} from the bytes.
     */
    public static int intFromBytes(byte[] data) {
        if (data.length != 4) {
            throw new IllegalArgumentException(
                    "Expected an array of length 4, but found: " + data.length);
        }
        return (((data[0] & 0xFF) << 24)
                | ((data[1] & 0xFF) << 16)
                | ((data[2] & 0xFF) << 8)
                | (data[3] & 0xFF));
    }
    
    /**
     * Converts a {@code short} to an byte array.
     *
     * @param data The input short.
     * 
     * @return A byte array representing the given {@code data}.
     */
    public static byte[] toBytes(short data) {
        byte[] rtn = new byte[2];
        rtn[0] = (byte) (data >>> 8);
        rtn[1] = (byte) (data & 0xFF);
        return rtn;
    }
    
    /**
     * Converts an array of bytes to a {@code short}.
     *
     * @param data The input data. Must have a length of 2.
     * 
     * @return A {@code short} from the bytes.
     */
    public static int shortFromBytes(byte[] data) {
        if (data.length != 2) {
            throw new IllegalArgumentException(
                    "Expected an array of length 2, but found: " + data.length);
        }
        return ((data[0] & 0xFF) << 8)
                | (data[1] & 0xFF);
    }
    
    /**
     * Returns the primitive type of the provided class, or {@code null}
     * if the given class doesn't have a corresponding primitive type.
     * 
     * @param c The provided class.
     * 
     * @return The primitive type of provided class if it exists. {@code null} otherwise.
     */
    public static Class<?> getPrimitiveTypeOf(Class<?> c) {
        if (c == Boolean.class) return Boolean.TYPE;
        if (c == Byte.class) return Byte.TYPE;
        if (c == Short.class) return Short.TYPE;
        if (c == Character.class) return Character.TYPE;
        if (c == Integer.class) return Integer.TYPE;
        if (c == Long.class) return Long.TYPE;
        if (c == Float.class) return Float.TYPE;
        if (c == Double.class) return Double.TYPE;
        if (c == Void.class) return Void.TYPE;
        return null;
    }
    
    /**
     * Returns the base class of a primitive type, or {@code null}
     * if the given class it not a primitive type.
     * 
     * @param c The provided class.
     * 
     * @return The base class of the provided class if it exists. {@code null} otherwise.
     */
    public static Class<?> getBaseClassOf(Class<?> c) {
        if (c == Boolean.TYPE) return Boolean.class;
        if (c == Byte.TYPE) return Byte.class;
        if (c == Short.TYPE) return Short.class;
        if (c == Character.TYPE) return Character.class;
        if (c == Integer.TYPE) return Integer.class;
        if (c == Long.TYPE) return Long.class;
        if (c == Float.TYPE) return Float.class;
        if (c == Double.TYPE) return Double.class;
        if (c == Void.TYPE) return Void.class;
        return null;
    }
    
    /**
     * Returns the default value for a primitive type. If the given class isn't a
     * primitive type, then {@code null} is returned.
     * 
     * @param <T> The base class of the primitive type.
     * 
     * @param c The class of the primitive type.
     * 
     * @return The default value of the primitive type, or {@code null}
     *     if the given class doesn't have a primitive value.
     */
    public static <T> T getDefaultPrim(Class<T> c) {
        if (c == Boolean.TYPE) return (T) Boolean.FALSE;
        if (c == Byte.TYPE) return (T) (Byte) (byte) 0;
        if (c == Short.TYPE) return (T) (Short) (short) 0;
        if (c == Character.TYPE) return (T) (Character) (char) 0;
        if (c == Integer.TYPE) return (T) (Integer) 0;
        if (c == Long.TYPE) return (T) (Long) 0L;
        if (c == Float.TYPE) return (T) (Float) 0.0f;
        if (c == Double.TYPE) return (T) (Double) 0.0;
        return null;
    }
    
    
}
