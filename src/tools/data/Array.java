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

package tools.data;


// Java imports.
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import tools.MultiTool;


/**
 * TODO.
 * - equals(Object, Object) (all types)
 * - deepEquals(Object, Object) (all types)
 * - partOf(byte[] b, int off, int len) (all types)
 * - binarySearch(byte[] a, byte key) (all types)
 * - binarySearch(byte[] a, int fromIndex, int toIndex, byte key) (all types)
 * - migrate calcHashCode from MultiTool here: deepHashCode(Object[] a)
 * - hashCode(boolean[] a)
 * - migrate deepEquals from MultiTool here: deepToString(Object[] a)
 * - equals(boolean[] a, boolean[] a2) (all types)
 * - fill(boolean[] a, boolean val) (all types) (all types)
 * - fill(boolean[] a, int fromIndex, int toIndex, boolean val) (all types)
 * - sort(byte[] a) (all types)
 * - sort(byte[] a, int fromIndex, int toIndex) (all types)
 * 
 * The {@code Array} class provides static methods to dynamically resolve
 * primitive typed arrays mixed with object arrays.
 * <br>
 * It re-implements the get, set and length functions for primitive types
 * of the {@link java.lang.reflect.Array} class, and the toString function
 * of the {@link java.util.Arrays} class in pure java code.
 * Because of this, JIT can optimize the code better compared to the
 * native implementations of the reflect array class.
 * 
 * The functions permits widening conversions to occur during a get or set
 * operation, but throws an {@code ClassCastException} if a narrowing
 * conversion would occur.
 * 
 * @author Kaj Wortel
 * 
 * @see java.lang.reflect.Array
 * @see java.lang.reflect.Arrays
 */
public final class Array {

    /* -------------------------------------------------------------------------
     * Constructor.
     * -------------------------------------------------------------------------
     */
    /**
     * Constructor.
     * Class Array is not instantiable.
     */
    private Array() { }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /* -------------------------------------------------------------------------
     * To string functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Entrypoint of the {@code Arrays.toString} function.
     * 
     * @param array The array to print.
     * 
     * @return The String representation as specified by {@link Arrays#toString(Object[])}.
     * 
     * @throws IllegalArgumentException If the given object is not an array.
     * 
     * @see Arrays#toString(Object[])
     * @see Arrays#toString(boolean[])
     * @see Arrays#toString(byte[])
     * @see Arrays#toString(char[])
     * @see Arrays#toString(short[])
     * @see Arrays#toString(int[])
     * @see Arrays#toString(long[])
     * @see Arrays#toString(float[])
     * @see Arrays#toString(double[])
     */
    public static String toString(Object array)
            throws IllegalArgumentException {
        if (array instanceof Object[]) return Arrays.toString((Object[]) array);
        else if (array instanceof boolean[]) return Arrays.toString((boolean[]) array);
        else if (array instanceof byte[]) return Arrays.toString((byte[]) array);
        else if (array instanceof char[]) return Arrays.toString((char[]) array);
        else if (array instanceof short[]) return Arrays.toString((short[]) array);
        else if (array instanceof int[]) return Arrays.toString((int[]) array);
        else if (array instanceof long[]) return Arrays.toString((long[]) array);
        else if (array instanceof float[]) return Arrays.toString((float[]) array);
        else if (array instanceof double[]) return Arrays.toString((double[]) array);
        else if (array == null) throw new NullPointerException("Array was null!");
        else if (!array.getClass().isArray()) {
            throw new IllegalArgumentException("Argument was not an array!");
        }
        throw new IllegalStateException();
    }
    
    /**
     * Converts the given integer typed number array to a string representation
     * in base {@code radix}. <br>
     * This means that the types {@code byte}, {@code short}, {@code char},
     * {@code int}, {@code long}, their class equivalents, and custom types
     * extending {@link Number} are allowed. <br>
     * Floating point numbers ({@code float} and {@code double} are INVALID,
     * as well as classes not extending {@link Number}.
     * 
     * @param array The integer typed array to print.
     * @param radix The radix of the values.
     * 
     * @return The String representation as specified by {@link Arrays#toString(Object[])}.
     * 
     * @throws IllegalArgumentException if the given object is not an array,
     *     or has the wrong type.
     * 
     * @see Integer#toString(int, int)
     * @see Long#toString(long, int)
     */
    public static String toString(Object array, int radix)
            throws IllegalArgumentException {
        if (array instanceof Object[]) {
            if (!(array instanceof Number[])) {
                throw new IllegalArgumentException("Argument was not a number array!");
                
            } else if (array instanceof Float[]) {
                throw new IllegalArgumentException(
                        "Expected an integer typed number array, "
                                + "but found a floating point array (Float[]).");
                
            } else if (array instanceof Double[]) {
                throw new IllegalArgumentException(
                        "Expected an integer typed number array, "
                                + "but found a floating point array (Double[]).");
                
            } else {
                StringBuilder sb = new StringBuilder("[");
                Number[] arr = (Number[]) array;
                boolean first = true;
                for (int i = 0; i < arr.length; i++) {
                    if (first) first = false;
                    else sb.append(", ");
                    sb.append(Long.toString((arr[i].longValue()), radix));
                }
                sb.append("]");
                return sb.toString();
            }
            
        } else if (array instanceof byte[]) {
            StringBuilder sb = new StringBuilder("[");
            byte[] arr = (byte[]) array;
            boolean first = true;
            for (int i = 0; i < arr.length; i++) {
                if (first) first = false;
                else sb.append(", ");
                if (radix == 2) {
                    sb.append(MultiTool.fillZero(arr[i] & 0xFF, 8, radix));
                    
                } else if (radix == 16) {
                    sb.append(MultiTool.fillZero(arr[i] & 0xFF, 2, radix));    
                    
                } else {
                    sb.append(Integer.toString(arr[i] & 0xFF, radix));
                }
            }
            sb.append("]");
            return sb.toString();
            
        } else if (array instanceof char[]) {
            StringBuilder sb = new StringBuilder("[");
            byte[] arr = (byte[]) array;
            boolean first = true;
            for (int i = 0; i < arr.length; i++) {
                if (first) first = false;
                else sb.append(", ");
                if (radix == 2) {
                    sb.append(MultiTool.fillZero(arr[i] & 0xFF, 16, radix));
                    
                } else if (radix == 16) {
                    sb.append(MultiTool.fillZero(arr[i] & 0xFF, 32, radix));    
                    
                } else {
                    sb.append(Integer.toString(arr[i] & 0xFF, radix));
                }
            }
            sb.append("]");
            return sb.toString();
            
        } else if (array instanceof short[]) {
            StringBuilder sb = new StringBuilder("[");
            byte[] arr = (byte[]) array;
            boolean first = true;
            for (int i = 0; i < arr.length; i++) {
                if (first) first = false;
                else sb.append(", ");
                if (radix == 2) {
                    sb.append(MultiTool.fillZero(arr[i] & 0xFF, 16, radix));
                    
                } else if (radix == 16) {
                    sb.append(MultiTool.fillZero(arr[i] & 0xFF, 32, radix));    
                    
                } else {
                    sb.append(Integer.toString(arr[i] & 0xFF, radix));
                }
            }
            sb.append("]");
            return sb.toString();
            
        } else if (array instanceof int[]) {
            StringBuilder sb = new StringBuilder("[");
            byte[] arr = (byte[]) array;
            boolean first = true;
            for (int i = 0; i < arr.length; i++) {
                if (first) first = false;
                else sb.append(", ");
                sb.append(Integer.toString(arr[i], radix));
            }
            sb.append("]");
            return sb.toString();
            
        } else if (array instanceof long[]) {
            StringBuilder sb = new StringBuilder("[");
            byte[] arr = (byte[]) array;
            boolean first = true;
            for (int i = 0; i < arr.length; i++) {
                if (first) first = false;
                else sb.append(", ");
                sb.append(Long.toString(arr[i], radix));
            }
            sb.append("]");
            return sb.toString();
            
        } else if (array instanceof float[]) {
            throw new IllegalArgumentException(
                    "Expected an integer typed number array, "
                            + "but found a floating point array (float[]).");
            
        } else if (array instanceof double[]) {
            throw new IllegalArgumentException(
                    "Expected an integer typed number array, "
                            + "but found a floating point array (double[]).");
            
        } else if (array instanceof boolean[]) {
            throw new IllegalArgumentException(
                    "Expected an integer typed number array, "
                            + "but found a boolean array (boolean[]).");
            
        } else if (array == null) {
            throw new NullPointerException("Array was null!");
            
        } else if (!array.getClass().isArray()) {
            throw new IllegalArgumentException("Argument was not an array!");
        }
        throw new IllegalStateException();
    }
    
    
    /* -------------------------------------------------------------------------
     * Get length functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Returns the length of the specified array object, as an {@code int}.
     *
     * @param array The array
     * 
     * @return The length of the array
     * 
     * @throws IllegalArgumentException if the object argument is not an array
     * 
     * @see java.lang.reflect.Array#getLength(Object) &nbsp reflect entry point.
     */
    public static int getLength(Object array)
            throws IllegalArgumentException {
        if (array instanceof Object[]) return ((Object[]) array).length;
        else if (array instanceof boolean[]) return ((boolean[]) array).length;
        else if (array instanceof byte[]) return ((byte[]) array).length;
        else if (array instanceof char[]) return ((char[]) array).length;
        else if (array instanceof short[]) return ((short[]) array).length;
        else if (array instanceof int[]) return ((int[]) array).length;
        else if (array instanceof long[]) return ((long[]) array).length;
        else if (array instanceof float[]) return ((float[]) array).length;
        else if (array instanceof double[]) return ((double[]) array).length;
        else if (array == null) throw new NullPointerException("Array was null!");
        else if (!array.getClass().isArray()) {
            throw new IllegalArgumentException("Argument was not an array!");
        }
        throw new IllegalStateException();
    }
    
    
    /* -------------------------------------------------------------------------
     * Get functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Returns the value of the indexed component in the specified
     * array object. The value is automatically wrapped in an object
     * if it has a primitive type.
     *
     * @param array The array.
     * @param index The index.
     * 
     * @return The (possibly wrapped) value of the indexed component in the specified array.
     * 
     * @throws NullPointerException If the specified object is null.
     * @throws IllegalArgumentException If the specified object is not an array
     * @throws ArrayIndexOutOfBoundsException If the specified {@code index}
     *     argument is negative, or if it is greater than or equal to the
     *     length of the specified array.
     * 
     * @see java.lang.reflect.Array#get(Object, int) &nbsp reflect entry point.
     * @see #getBoolean(Object, int)
     * @see #getByte(Object, int)
     * @see #getShort(Object, int)
     * @see #getInt(Object, int)
     * @see #getLong(Object, int)
     * @see #getFloat(Object, int)
     * @see #getDouble(Object, int)
     */
    public static Object get(Object array, int index)
            throws IllegalArgumentException {
        if (array instanceof Object[]) return ((Object[]) array)[index];
        else if (array instanceof boolean[]) return ((boolean[]) array)[index];
        else if (array instanceof byte[]) return ((byte[]) array)[index];
        else if (array instanceof char[]) return ((char[]) array)[index];
        else if (array instanceof short[]) return ((short[]) array)[index];
        else if (array instanceof int[]) return ((int[]) array)[index];
        else if (array instanceof long[]) return ((long[]) array)[index];
        else if (array instanceof float[]) return ((float[]) array)[index];
        else if (array instanceof double[]) return ((double[]) array)[index];
        else if (array == null) {
            throw new NullPointerException("Array was null!");    
        } else if (!array.getClass().isArray()) {
            throw new IllegalArgumentException("Argument was not an array!");
        }
        throw new IllegalStateException();
    }

    /**
     * Returns the value of the indexed component in the specified
     * array object, as a {@code boolean}.
     *
     * @param array The array.
     * @param index The index.
     * 
     * @return The value of the indexed component in the specified array.
     * 
     * @throws ClassCastException If the specified object is not
     *     an array, or if the indexed element cannot be converted to the
     *     return type by an identity or widening conversion
     * @throws ArrayIndexOutOfBoundsException If the specified {@code index}
     *     argument is negative, or if it is greater than or equal to the
     *     length of the specified array
     * 
     * @see java.lang.reflect.Array#getBoolean(Object, int) &nbsp reflect entry point.
     * @see #get(Object, int)
     */
    public static boolean getBoolean(Object array, int index)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        return ((boolean[]) array)[index];
    }
    
    /**
     * Returns the value of the indexed component in the specified
     * array object, as a {@code byte}.
     *
     * @param array The array.
     * @param index The index.
     * 
     * @return The value of the indexed component in the specified array.
     * 
     * @throws ClassCastException If the specified object is not
     *     an array, or if the indexed element cannot be converted to the
     *     return type by an identity or widening conversion
     * @throws ArrayIndexOutOfBoundsException If the specified {@code index}
     *     argument is negative, or if it is greater than or equal to the
     *     length of the specified array
     * 
     * @see java.lang.reflect.Array#getByte(Object, int) &nbsp reflect entry point.
     * @see #get(Object, int)
     */
    public static byte getByte(Object array, int index)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        return ((byte[]) array)[index];
    }

    /**
     * Returns the value of the indexed component in the specified
     * array object, as a {@code char}.
     *
     * @param array The array.
     * @param index The index.
     * 
     * @return The value of the indexed component in the specified array.
     * 
     * @throws ClassCastException If the specified object is not
     *     an array, or if the indexed element cannot be converted to the
     *     return type by an identity or widening conversion
     * @throws ArrayIndexOutOfBoundsException If the specified {@code index}
     *     argument is negative, or if it is greater than or equal to the
     *     length of the specified array
     * 
     * @see java.lang.reflect.Array#getChar(Object, int) &nbsp reflect entry point.
     * @see #get(Object, int)
     */
    public static char getChar(Object array, int index)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        return ((char[]) array)[index];
    }

    /**
     * Returns the value of the indexed component in the specified
     * array object, as a {@code boolean}.
     *
     * @param array The array.
     * @param index The index.
     * 
     * @return The value of the indexed component in the specified array.
     * 
     * @throws ClassCastException If the specified object is not
     *     an array, or if the indexed element cannot be converted to the
     *     return type by an identity or widening conversion
     * @throws ArrayIndexOutOfBoundsException If the specified {@code index}
     *     argument is negative, or if it is greater than or equal to the
     *     length of the specified array
     * 
     * @see java.lang.reflect.Array#getShort(Object, int) &nbsp reflect entry point.
     * @see #get(Object, int)
     */
    public static short getShort(Object array, int index)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        return ((short[]) array)[index];
    }

    /**
     * Returns the value of the indexed component in the specified
     * array object, as a {@code short}.
     *
     * @param array The array.
     * @param index The index.
     * 
     * @return The value of the indexed component in the specified array.
     * 
     * @throws ClassCastException If the specified object is not
     *     an array, or if the indexed element cannot be converted to the
     *     return type by an identity or widening conversion
     * @throws ArrayIndexOutOfBoundsException If the specified {@code index}
     *     argument is negative, or if it is greater than or equal to the
     *     length of the specified array
     * 
     * @see java.lang.reflect.Array#getInt(Object, int) &nbsp reflect entry point.
     * @see #get(Object, int)
     */
    public static int getInt(Object array, int index)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        return ((int[]) array)[index];
    }

    /**
     * Returns the value of the indexed component in the specified
     * array object, as a {@code long}.
     *
     * @param array The array.
     * @param index The index.
     * 
     * @return The value of the indexed component in the specified array.
     * 
     * @throws ClassCastException If the specified object is not
     *     an array, or if the indexed element cannot be converted to the
     *     return type by an identity or widening conversion
     * @throws ArrayIndexOutOfBoundsException If the specified {@code index}
     *     argument is negative, or if it is greater than or equal to the
     *     length of the specified array
     * 
     * @see java.lang.reflect.Array#getLong(Object, int) &nbsp reflect entry point.
     * @see #get(Object, int)
     */
    public static long getLong(Object array, int index)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        return ((long[]) array)[index];
    }

    /**
     * Returns the value of the indexed component in the specified
     * array object, as a {@code float}.
     *
     * @param array The array.
     * @param index The index.
     * 
     * @return The value of the indexed component in the specified array.
     * 
     * @throws ClassCastException If the specified object is not
     *     an array, or if the indexed element cannot be converted to the
     *     return type by an identity or widening conversion
     * @throws ArrayIndexOutOfBoundsException If the specified {@code index}
     *     argument is negative, or if it is greater than or equal to the
     *     length of the specified array
     * 
     * @see java.lang.reflect.Array#getFloat(Object, int) &nbsp reflect entry point.
     * @see #get(Object, int)
     */
    public static float getFloat(Object array, int index)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        return ((float[]) array)[index];
    }

    /**
     * Returns the value of the indexed component in the specified
     * array object, as a {@code double}.
     *
     * @param array The array.
     * @param index The index.
     * 
     * @return The value of the indexed component in the specified array.
     * 
     * @throws ClassCastException If the specified object is not
     *     an array, or if the indexed element cannot be converted to the
     *     return type by an identity or widening conversion
     * @throws ArrayIndexOutOfBoundsException If the specified {@code index}
     *     argument is negative, or if it is greater than or equal to the
     *     length of the specified array
     * 
     * @see java.lang.reflect.Array#getDouble(Object, int) &nbsp reflect entry point.
     * @see #get(Object, int)
     */
    public static double getDouble(Object array, int index)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        return ((double[]) array)[index];
    }
    
    
    /* -------------------------------------------------------------------------
     * Set functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Sets the value of the indexed component of the specified array
     * object to the specified new value. The new value is first
     * automatically unwrapped if the array has a primitive component type.
     * 
     * @param array The array.
     * @param index The index into the array.
     * @param value The new value of the indexed component.
     * 
     * @throws IllegalArgumentException If the specified object argument
     *     is not an array.
     * @throws ArrayIndexOutOfBoundsException If the specified {@code index}
     *     argument is negative, or if it is greater than or equal to
     *     the length of the specified array
     * @throws ClassCastException If {@code value} is not the same type
     *     as {@code array}.
     * 
     * @see java.lang.reflect.Array#set(Object, int, Object) &nbsp reflect entry point
     * @see #setBoolean(Object, int, boolean)
     * @see #setByte(Object, int, byte)
     * @see #setShort(Object, int, short)
     * @see #setInt(Object, int, int)
     * @see #setLong(Object, int, long)
     * @see #setFloat(Object, int, float)
     * @see #setDouble(Object, int, double)
     */
    public static void set(Object array, int index, Object value)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException,
            ClassCastException {
        if (array instanceof Object[]) ((Object[]) array)[index] = value;
        else if (array instanceof boolean[]) ((boolean[]) array)[index] = (boolean) value;
        else if (array instanceof byte[]) ((byte[]) array)[index] = (byte) value;
        else if (array instanceof char[]) ((char[]) array)[index] = (char) value;
        else if (array instanceof short[]) ((short[]) array)[index] = (short) value;
        else if (array instanceof int[]) ((int[]) array)[index] = (int) value;
        else if (array instanceof long[]) ((long[]) array)[index] = (long) value;
        else if (array instanceof float[]) ((float[]) array)[index] = (float) value;
        else if (array instanceof double[]) ((double[]) array)[index] = (double) value;
        else if (array == null) {
            throw new NullPointerException("Array was null!");    
        } else if (!array.getClass().isArray()) {
            throw new IllegalArgumentException("Argument was not an array!");
        }
        throw new IllegalStateException();
    }

    /**
     * Sets the value of the indexed component of the specified array
     * object to the specified {@code boolean} value.
     * 
     * @param array The array.
     * @param index The index into the array.
     * @param b The new value of the indexed component.
     * 
     * @throws ArrayIndexOutOfBoundsException If the specified {@code index}
     *     argument is negative, or if it is greater than or equal to
     *     the length of the specified array.
     * @throws ClassCastException If the specified object argument
     *     is not an array of {@code boolean}s.
     * 
     * @see java.lang.reflect.Array#setBoolean(Object, int, boolean) &nbsp reflect entry point.
     * @see #set(Object, int, Object)
     */
    public static void setBoolean(Object array, int index, boolean b)
            throws ArrayIndexOutOfBoundsException, ClassCastException {
        ((boolean[]) array)[index] = b;
    }

    /**
     * Sets the value of the indexed component of the specified array
     * object to the specified {@code byte} value.
     * 
     * @param array The array.
     * @param index The index into the array.
     * @param b The new value of the indexed component.
     * 
     * @throws ArrayIndexOutOfBoundsException If the specified {@code index}
     *     argument is negative, or if it is greater than or equal to
     *     the length of the specified array.
     * @throws ClassCastException If the specified object argument
     *     is not an array of {@code byte}s.
     * 
     * @see java.lang.reflect.Array#setByte(Object, int, byte) &nbsp reflect entry point.
     * @see #set(Object, int, Object)
     */
    public static void setByte(Object array, int index, byte b)
            throws ArrayIndexOutOfBoundsException, ClassCastException {
        ((byte[]) array)[index] = b;
    }

    /**
     * Sets the value of the indexed component of the specified array
     * object to the specified {@code char} value.
     * 
     * @param array The array.
     * @param index The index into the array.
     * @param c The new value of the indexed component.
     * 
     * @throws ArrayIndexOutOfBoundsException If the specified {@code index}
     *     argument is negative, or if it is greater than or equal to
     *     the length of the specified array.
     * @throws ClassCastException If the specified object argument
     *     is not an array of {@code char}s.
     * 
     * @see java.lang.reflect.Array#setChar(Object, int, char) &nbsp reflect entry point.
     * @see #set(Object, int, Object)
     */
    public static void setChar(Object array, int index, char c)
            throws ArrayIndexOutOfBoundsException, ClassCastException {
        ((char[]) array)[index] = c;
    }

    /**
     * Sets the value of the indexed component of the specified array
     * object to the specified {@code short} value.
     * 
     * @param array The array.
     * @param index The index into the array.
     * @param s The new value of the indexed component.
     * 
     * @throws ArrayIndexOutOfBoundsException If the specified {@code index}
     *     argument is negative, or if it is greater than or equal to
     *     the length of the specified array.
     * @throws ClassCastException If the specified object argument
     *     is not an array of {@code short}s.
     * 
     * @see java.lang.reflect.Array#setShort(Object, int, short) &nbsp reflect entry point.
     * @see #set(Object, int, Object)
     */
    public static void setShort(Object array, int index, short s)
            throws ArrayIndexOutOfBoundsException, ClassCastException {
        ((short[]) array)[index] = s;
    }

    /**
     * Sets the value of the indexed component of the specified array
     * object to the specified {@code int} value.
     * 
     * @param array The array.
     * @param index The index into the array.
     * @param i The new value of the indexed component.
     * 
     * @throws ArrayIndexOutOfBoundsException If the specified {@code index}
     *     argument is negative, or if it is greater than or equal to
     *     the length of the specified array.
     * @throws ClassCastException If the specified object argument
     *     is not an array of {@code int}s.
     * 
     * @see java.lang.reflect.Array#setIntObject, int, int) &nbsp reflect entry point.
     * @see #set(Object, int, Object)
     */
    public static void setInt(Object array, int index, int i)
            throws ArrayIndexOutOfBoundsException, ClassCastException {
        ((int[]) array)[index] = i;
    }

    /**
     * Sets the value of the indexed component of the specified array
     * object to the specified {@code long} value.
     * 
     * @param array The array.
     * @param index The index into the array.
     * @param l The new value of the indexed component.
     * 
     * @throws ArrayIndexOutOfBoundsException If the specified {@code index}
     *     argument is negative, or if it is greater than or equal to
     *     the length of the specified array.
     * @throws ClassCastException If the specified object argument
     *     is not an array of {@code long}s.
     * 
     * @see java.lang.reflect.Array#setLong(Object, int, long) &nbsp reflect entry point.
     * @see #set(Object, int, Object)
     */
    public static void setLong(Object array, int index, long l)
            throws ArrayIndexOutOfBoundsException, ClassCastException {
        ((long[]) array)[index] = l;
    }

    /**
     * Sets the value of the indexed component of the specified array
     * object to the specified {@code boolean} value.
     * 
     * @param array The array.
     * @param index The index into the array.
     * @param f The new value of the indexed component.
     * 
     * @throws ArrayIndexOutOfBoundsException If the specified {@code index}
     *     argument is negative, or if it is greater than or equal to
     *     the length of the specified array.
     * @throws ClassCastException If the specified object argument
     *     is not an array of {@code boolean}s.
     * 
     * @see java.lang.reflect.Array#setFloat(Object, int, float) &nbsp reflect entry point.
     * @see #set(Object, int, Object)
     */
    public static void setFloat(Object array, int index, float f)
            throws ArrayIndexOutOfBoundsException, ClassCastException {
        ((float[]) array)[index] = f;
    }

    /**
     * Sets the value of the indexed component of the specified array
     * object to the specified {@code double} value.
     * 
     * @param array The array.
     * @param index The index into the array.
     * @param d The new value of the indexed component.
     * 
     * @throws ArrayIndexOutOfBoundsException If the specified {@code index}
     *     argument is negative, or if it is greater than or equal to
     *     the length of the specified array.
     * @throws ClassCastException If the specified object argument
     *     is not an array of {@code double}s.
     * 
     * @see java.lang.reflect.Array#setDouble(Object, int, double) &nbsp reflect entry point.
     * @see #set(Object, int, Object)
     */
    public static void setDouble(Object array, int index, double d)
            throws ArrayIndexOutOfBoundsException, ClassCastException {
        ((double[]) array)[index] = d;
    }
    
    
    /* -------------------------------------------------------------------------
     * List functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Unwraps an array of {@link Wrapper}s to a list of the underlaying type.
     * 
     * @param <V> The underlaying type.
     * @param arr The array to unwrap.
     * 
     * @return A list containing all unwrapped elements.
     */
    public static <V> List<V> unwrapToList(Wrapper<V>[] arr) {
        if (arr == null) return null;
        List<V> list = new ArrayList<>(arr.length);
        for (Wrapper<V> wrap : arr) {
            list.add(wrap.get());
        }
        return list;
    }
    
    /**
     * Converts the given array to a list of the same type.
     * 
     * @param <V> The type of the given array.
     * @param arr The array to convert.
     * 
     * @return An array of the given list.
     * 
     * @see Arrays#asList(Object...)
     */
    public static <V> List<V> asList(V... arr) {
        return Arrays.asList(arr);
    }
    
    
    /* -------------------------------------------------------------------------
     * Copy functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Copies any typed source array {@code src} to the destination array {@code dst}. <br>
     * The number of elements copied is equal to the minimum of the length of both arrays. <br>
     * If the destination array is longer, then all elements are copied. The last elements
     * will remain unchanged. <br>
     * If the source array is longer, then the first elements which fit are copied to the
     * destination array.
     * 
     * @param src The source array.
     * @param dst The destination array.
     * 
     * @return The elements from {@code src} copied to {@code dst}.
     * 
     * @see #copyOf(Object, Object, int, int)
     * @see #copyOf(Object, Object, int, int, int)
     */
    public static <V> V copyOf(Object src, V dst) {
        return copyOf(src, dst, 0, 0, Math.min(Array.getLength(src), Array.getLength(dst)));
    }
    
    /**
     * Copies any typed source array {@code src} to the destination array {@code dst}. <br>
     * There will be {@code len} elements copied from {@code src} to {@code dst}, starting
     * at the offset {@code offSrc} from the source array.
     * 
     * @param src The source array.
     * @param dst The destination array.
     * @param offSrc The offset to start copying elements from the source array.
     * @param len The number of elements to copy.
     * 
     * @return The elements from {@code src} copied to {@code dst}.
     * 
     * @see #copyOf(Object, Object, int, int, int)
     */
    public static <V> V copyOf(Object src, V dst, int offSrc, int len) {
        return copyOf(src, dst, offSrc, 0, len);
    }
    
    /**
     * Copies any typed source array {@code src} to the destination array {@code dst}. <br>
     * There will be {@code len} elements copied from {@code src} to {@code dst}, starting
     * at the offset {@code offSrc} from the source array and the offset {@code offDst} from
     * the destination array.
     * 
     * @param src The source array.
     * @param dst The destination array.
     * @param offSrc The offset to start copying elements from the source array.
     * @param offDst The offset to start writing elements to the destination array.
     * @param len The number of elements to copy.
     * 
     * @return The elements from {@code src} copied to {@code dst}.
     */
    @SuppressWarnings("MismatchedReadAndWriteOfArray") // Indirect read/writes due to array casting.
    public static <V> V copyOf(Object src, V dst, int offSrc, int offDst, int len) {
        if (src == null || dst == null) throw new NullPointerException();
        if (!src.getClass().isArray() || !dst.getClass().isArray()) {
            throw new IllegalArgumentException("Argument was not an array!");
        }
        
        boolean isObjArrSrc = (src instanceof Object[]);
        boolean isObjArrDst = (dst instanceof Object[]);
        
        if (isObjArrSrc && isObjArrDst) {
            Object[] s = (Object[]) src;
            Object[] d = (Object[]) dst;
            for (int i = 0; i < len; i++) {
                d[i + offDst] = s[i + offSrc];
            }
            
        } else if (!isObjArrSrc && !isObjArrDst) {
            //if (dst.getClass().isAssignableFrom(src.getClass())) throw new IllegalArgumentException("");
            if (src.getClass() != dst.getClass()) {
                throw new ClassCastException("Cannot cast " + src.getClass() + " to " + dst.getClass() + "!");
            }
            if (src instanceof boolean[]) {
                copyOf((boolean[]) src, (boolean[]) dst, offSrc, offDst, len);
                
            } else if (src instanceof byte[]) {
                copyOf((byte[]) src, (byte[]) dst, offSrc, offDst, len);
                
            } else if (src instanceof char[]) {
                copyOf((char[]) src, (char[]) dst, offSrc, offDst, len);
                 
            } else if (src instanceof short[]) {
                copyOf((short[]) src, (short[]) dst, offSrc, offDst, len);
                 
            } else if (src instanceof int[]) {
                copyOf((int[]) src, (int[]) dst, offSrc, offDst, len);
                 
            } else if (src instanceof long[]) {
                copyOf((long[]) src, (long[]) dst, offSrc, offDst, len);
                
            } else if (src instanceof float[]) {
                copyOf((float[]) src, (float[]) dst, offSrc, offDst, len);
                
            } else if (src instanceof double[]) {
                copyOf((double[]) src, (double[]) dst, offSrc, offDst, len);
                
            } else {
                throw new IllegalStateException();
            }
            
        } else if (isObjArrSrc) {
            Object[] s = (Object[]) src;
            if (dst instanceof boolean[]) {
                boolean[] d = (boolean[]) dst;
                for (int i = 0; i < len; i++) {
                    d[offDst + i] = (boolean) s[offSrc + i];
                }
                
            } else if (dst instanceof byte[]) {
                byte[] d = (byte[]) dst;
                for (int i = 0; i < len; i++) {
                    d[offDst + i] = (byte) s[offSrc + i];
                }
                
            } else if (dst instanceof char[]) {
                char[] d = (char[]) dst;
                for (int i = 0; i < len; i++) {
                    d[offDst + i] = (char) s[offSrc + i];
                }
                 
            } else if (dst instanceof short[]) {
                short[] d = (short[]) dst;
                for (int i = 0; i < len; i++) {
                    d[offDst + i] = (short) s[offSrc + i];
                }
                 
            } else if (dst instanceof int[]) {
                int[] d = (int[]) dst;
                for (int i = 0; i < len; i++) {
                    d[offDst + i] = (int) s[offSrc + i];
                }
                 
            } else if (dst instanceof long[]) {
                long[] d = (long[]) dst;
                for (int i = 0; i < len; i++) {
                    d[offDst + i] = (long) s[offSrc + i];
                }
                
            } else if (dst instanceof float[]) {
                float[] d = (float[]) dst;
                for (int i = 0; i < len; i++) {
                    d[offDst + i] = (float) s[offSrc + i];
                }
                
            } else if (dst instanceof double[]) {
                double[] d = (double[]) dst;
                for (int i = 0; i < len; i++) {
                    d[offDst + i] = (double) s[offSrc + i];
                }
                
            } else {
                throw new IllegalStateException();
            }
            
        } else { // isObjArrDst == true
            Object[] d = (Object[]) dst;
            if (src instanceof boolean[]) {
                boolean[] s = (boolean[]) src;
                for (int i = 0; i < len; i++) {
                    d[offDst + i] = s[offSrc + i];
                }
                
            } else if (src instanceof byte[]) {
                byte[] s = (byte[]) src;
                for (int i = 0; i < len; i++) {
                    d[offDst + i] = s[offSrc + i];
                }
                
            } else if (src instanceof char[]) {
                char[] s = (char[]) src;
                for (int i = 0; i < len; i++) {
                    d[offDst + i] = s[offSrc + i];
                }
                 
            } else if (src instanceof short[]) {
                short[] s = (short[]) src;
                for (int i = 0; i < len; i++) {
                    d[offDst + i] = s[offSrc + i];
                }
                 
            } else if (src instanceof int[]) {
                int[] s = (int[]) src;
                for (int i = 0; i < len; i++) {
                    d[offDst + i] = s[offSrc + i];
                }
                 
            } else if (src instanceof long[]) {
                long[] s = (long[]) src;
                for (int i = 0; i < len; i++) {
                    d[offDst + i] = s[offSrc + i];
                }
                
            } else if (src instanceof float[]) {
                float[] s = (float[]) src;
                for (int i = 0; i < len; i++) {
                    d[offDst + i] = s[offSrc + i];
                }
                
            } else if (src instanceof double[]) {
                double[] s = (double[]) src;
                for (int i = 0; i < len; i++) {
                    d[offDst + i] = s[offSrc + i];
                }
            } else {
                throw new IllegalStateException();
            }
        }
        
        return dst;
    }
    
    /**
     * Copies a boolean array {@code src} to the destination boolean array {@code dst}. <br>
     * The number of elements copied is equal to the minimum of the length of both arrays. <br>
     * If the destination array is longer, then all elements are copied. The last elements
     * will remain unchanged. <br>
     * If the source array is longer, then the first elements which fit are copied to the
     * destination array.
     * 
     * @param src The source array.
     * @param dst The destination array.
     * 
     * @return The elements from {@code src} copied to {@code dst}.
     * 
     * @see #copyOf(boolean[], boolean[], int, int)
     * @see #copyOf(boolean[], boolean[], int, int, int)
     */
    public static boolean[] copyOf(boolean[] src, boolean[] dst) {
        return copyOf(src, dst, 0, 0, Math.min(Array.getLength(src), Array.getLength(dst)));
    }
    
    /**
     * Copies a boolean array {@code src} to the destination boolean array {@code dst}. <br>
     * There will be {@code len} elements copied from {@code src} to {@code dst}, starting
     * at the offset {@code offSrc} from the source array.
     * 
     * @param src The source array.
     * @param dst The destination array.
     * @param offSrc The offset to start copying elements from the source array.
     * @param len The number of elements to copy.
     * 
     * @return The elements from {@code src} copied to {@code dst}.
     * 
     * @see #copyOf(boolean[], boolean[], int, int, int)
     */
    public static boolean[] copyOf(boolean[] src, boolean[] dst, int offSrc, int len) {
        return copyOf(src, dst, offSrc, 0, len);
    }
    
    /**
     * Copies a boolean array {@code src} to the destination boolean array {@code dst}. <br>
     * There will be {@code len} elements copied from {@code src} to {@code dst}, starting
     * at the offset {@code offSrc} from the source array and the offset {@code offDst} from
     * the destination array.
     * 
     * @param src The source array.
     * @param dst The destination array.
     * @param offSrc The offset to start copying elements from the source array.
     * @param offDst The offset to start writing elements to the destination array.
     * @param len The number of elements to copy.
     * 
     * @return The elements from {@code src} copied to {@code dst}.
     */
    public static boolean[] copyOf(boolean[] src, boolean[] dst, int offSrc, int offDst, int len) {
        for (int i = 0; i < len; i++) {
            dst[offDst + i] = src[offSrc + i];
        }
        return dst;
    }
    
    /**
     * Copies a byte array {@code src} to the destination byte array {@code dst}. <br>
     * The number of elements copied is equal to the minimum of the length of both arrays. <br>
     * If the destination array is longer, then all elements are copied. The last elements
     * will remain unchanged. <br>
     * If the source array is longer, then the first elements which fit are copied to the
     * destination array.
     * 
     * @param src The source array.
     * @param dst The destination array.
     * 
     * @return The elements from {@code src} copied to {@code dst}.
     * 
     * @see #copyOf(byte[], byte[], int, int)
     * @see #copyOf(byte[], byte[], int, int, int)
     */
    public static byte[] copyOf(byte[] src, byte[] dst) {
        return copyOf(src, dst, 0, 0, Math.min(src.length, dst.length));
    }
    
    /**
     * Copies a byte array {@code src} to the destination byte array {@code dst}. <br>
     * There will be {@code len} elements copied from {@code src} to {@code dst}, starting
     * at the offset {@code offSrc} from the source array.
     * 
     * @param src The source array.
     * @param dst The destination array.
     * @param offSrc The offset to start copying elements from the source array.
     * @param len The number of elements to copy.
     * 
     * @return The elements from {@code src} copied to {@code dst}.
     * 
     * @see #copyOf(byte[], byte[], int, int, int)
     */
    public static byte[] copyOf(byte[] src, byte[] dst, int offSrc, int len) {
        return copyOf(src, dst, offSrc, 0, len);
    }
    
    /**
     * Copies a byte array {@code src} to the destination byte array {@code dst}. <br>
     * There will be {@code len} elements copied from {@code src} to {@code dst}, starting
     * at the offset {@code offSrc} from the source array and the offset {@code offDst} from
     * the destination array.
     * 
     * @param src The source array.
     * @param dst The destination array.
     * @param offSrc The offset to start copying elements from the source array.
     * @param offDst The offset to start writing elements to the destination array.
     * @param len The number of elements to copy.
     * 
     * @return The elements from {@code src} copied to {@code dst}.
     */
    public static byte[] copyOf(byte[] src, byte[] dst, int offSrc, int offDst, int len) {
        for (int i = 0; i < len; i++) {
            dst[offDst + i] = src[offSrc + i];
        }
        return dst;
    }
    
    /**
     * Copies a char array {@code src} to the destination char array {@code dst}. <br>
     * The number of elements copied is equal to the minimum of the length of both arrays. <br>
     * If the destination array is longer, then all elements are copied. The last elements
     * will remain unchanged. <br>
     * If the source array is longer, then the first elements which fit are copied to the
     * destination array.
     * 
     * @param src The source array.
     * @param dst The destination array.
     * 
     * @return The elements from {@code src} copied to {@code dst}.
     * 
     * @see #copyOf(char[], char[], int, int)
     * @see #copyOf(char[], char[], int, int, int)
     */
    public static char[] copyOf(char[] src, char[] dst) {
        return copyOf(src, dst, 0, 0, Math.min(src.length, dst.length));
    }
    
    /**
     * Copies a char array {@code src} to the destination char array {@code dst}. <br>
     * There will be {@code len} elements copied from {@code src} to {@code dst}, starting
     * at the offset {@code offSrc} from the source array.
     * 
     * @param src The source array.
     * @param dst The destination array.
     * @param offSrc The offset to start copying elements from the source array.
     * @param len The number of elements to copy.
     * 
     * @return The elements from {@code src} copied to {@code dst}.
     * 
     * @see #copyOf(char[], char[], int, int, int)
     */
    public static char[] copyOf(char[] src, char[] dst, int offSrc, int len) {
        return copyOf(src, dst, offSrc, 0, len);
    }
    
    /**
     * Copies a char array {@code src} to the destination char array {@code dst}. <br>
     * There will be {@code len} elements copied from {@code src} to {@code dst}, starting
     * at the offset {@code offSrc} from the source array and the offset {@code offDst} from
     * the destination array.
     * 
     * @param src The source array.
     * @param dst The destination array.
     * @param offSrc The offset to start copying elements from the source array.
     * @param offDst The offset to start writing elements to the destination array.
     * @param len The number of elements to copy.
     * 
     * @return The elements from {@code src} copied to {@code dst}.
     */
    public static char[] copyOf(char[] src, char[] dst, int offSrc, int offDst, int len) {
        for (int i = 0; i < len; i++) {
            dst[offDst + i] = src[offSrc + i];
        }
        return dst;
    }
    
    /**
     * Copies a short array {@code src} to the destination short array {@code dst}. <br>
     * The number of elements copied is equal to the minimum of the length of both arrays. <br>
     * If the destination array is longer, then all elements are copied. The last elements
     * will remain unchanged. <br>
     * If the source array is longer, then the first elements which fit are copied to the
     * destination array.
     * 
     * @param src The source array.
     * @param dst The destination array.
     * 
     * @return The elements from {@code src} copied to {@code dst}.
     * 
     * @see #copyOf(short[], short[], int, int)
     * @see #copyOf(short[], short[], int, int, int)
     */
    public static short[] copyOf(short[] src, short[] dst) {
        return copyOf(src, dst, 0, 0, Math.min(src.length, dst.length));
    }
    
    /**
     * Copies a short array {@code src} to the destination short array {@code dst}. <br>
     * There will be {@code len} elements copied from {@code src} to {@code dst}, starting
     * at the offset {@code offSrc} from the source array.
     * 
     * @param src The source array.
     * @param dst The destination array.
     * @param offSrc The offset to start copying elements from the source array.
     * @param len The number of elements to copy.
     * 
     * @return The elements from {@code src} copied to {@code dst}.
     * 
     * @see #copyOf(short[], short[], int, int, int)
     */
    public static short[] copyOf(short[] src, short[] dst, int offSrc, int len) {
        return copyOf(src, dst, offSrc, 0, len);
    }
    
    /**
     * Copies a short array {@code src} to the destination short array {@code dst}. <br>
     * There will be {@code len} elements copied from {@code src} to {@code dst}, starting
     * at the offset {@code offSrc} from the source array and the offset {@code offDst} from
     * the destination array.
     * 
     * @param src The source array.
     * @param dst The destination array.
     * @param offSrc The offset to start copying elements from the source array.
     * @param offDst The offset to start writing elements to the destination array.
     * @param len The number of elements to copy.
     * 
     * @return The elements from {@code src} copied to {@code dst}.
     */
    public static short[] copyOf(short[] src, short[] dst, int offSrc, int offDst, int len) {
        for (int i = 0; i < len; i++) {
            dst[offDst + i] = src[offSrc + i];
        }
        return dst;
    }
    
    /**
     * Copies a int array {@code src} to the destination int array {@code dst}. <br>
     * The number of elements copied is equal to the minimum of the length of both arrays. <br>
     * If the destination array is longer, then all elements are copied. The last elements
     * will remain unchanged. <br>
     * If the source array is longer, then the first elements which fit are copied to the
     * destination array.
     * 
     * @param src The source array.
     * @param dst The destination array.
     * 
     * @return The elements from {@code src} copied to {@code dst}.
     * 
     * @see #copyOf(int[], int[], int, int)
     * @see #copyOf(int[], int[], int, int, int)
     */
    public static int[] copyOf(int[] src, int[] dst) {
        return copyOf(src, dst, 0, 0, Math.min(src.length, dst.length));
    }
    
    /**
     * Copies a int array {@code src} to the destination int array {@code dst}. <br>
     * There will be {@code len} elements copied from {@code src} to {@code dst}, starting
     * at the offset {@code offSrc} from the source array.
     * 
     * @param src The source array.
     * @param dst The destination array.
     * @param offSrc The offset to start copying elements from the source array.
     * @param len The number of elements to copy.
     * 
     * @return The elements from {@code src} copied to {@code dst}.
     * 
     * @see #copyOf(int[], int[], int, int, int)
     */
    public static int[] copyOf(int[] src, int[] dst, int offSrc, int len) {
        return copyOf(src, dst, offSrc, 0, len);
    }
    
    /**
     * Copies a int array {@code src} to the destination int array {@code dst}. <br>
     * There will be {@code len} elements copied from {@code src} to {@code dst}, starting
     * at the offset {@code offSrc} from the source array and the offset {@code offDst} from
     * the destination array.
     * 
     * @param src The source array.
     * @param dst The destination array.
     * @param offSrc The offset to start copying elements from the source array.
     * @param offDst The offset to start writing elements to the destination array.
     * @param len The number of elements to copy.
     * 
     * @return The elements from {@code src} copied to {@code dst}.
     */
    public static int[] copyOf(int[] src, int[] dst, int offSrc, int offDst, int len) {
        for (int i = 0; i < len; i++) {
            dst[offDst + i] = src[offSrc + i];
        }
        return dst;
    }
    
    /**
     * Copies a long array {@code src} to the destination long array {@code dst}. <br>
     * The number of elements copied is equal to the minimum of the length of both arrays. <br>
     * If the destination array is longer, then all elements are copied. The last elements
     * will remain unchanged. <br>
     * If the source array is longer, then the first elements which fit are copied to the
     * destination array.
     * 
     * @param src The source array.
     * @param dst The destination array.
     * 
     * @return The elements from {@code src} copied to {@code dst}.
     * 
     * @see #copyOf(long[], long[], int, int)
     * @see #copyOf(long[], long[], int, int, int)
     */
    public static long[] copyOf(long[] src, long[] dst) {
        return copyOf(src, dst, 0, 0, Math.min(src.length, dst.length));
    }
    
    /**
     * Copies a long array {@code src} to the destination long array {@code dst}. <br>
     * There will be {@code len} elements copied from {@code src} to {@code dst}, starting
     * at the offset {@code offSrc} from the source array.
     * 
     * @param src The source array.
     * @param dst The destination array.
     * @param offSrc The offset to start copying elements from the source array.
     * @param len The number of elements to copy.
     * 
     * @return The elements from {@code src} copied to {@code dst}.
     * 
     * @see #copyOf(long[], long[], int, int, int)
     */
    public static long[] copyOf(long[] src, long[] dst, int offSrc, int len) {
        return copyOf(src, dst, offSrc, 0, len);
    }
    
    /**
     * Copies a long array {@code src} to the destination long array {@code dst}. <br>
     * There will be {@code len} elements copied from {@code src} to {@code dst}, starting
     * at the offset {@code offSrc} from the source array and the offset {@code offDst} from
     * the destination array.
     * 
     * @param src The source array.
     * @param dst The destination array.
     * @param offSrc The offset to start copying elements from the source array.
     * @param offDst The offset to start writing elements to the destination array.
     * @param len The number of elements to copy.
     * 
     * @return The elements from {@code src} copied to {@code dst}.
     */
    public static long[] copyOf(long[] src, long[] dst, int offSrc, int offDst, int len) {
        for (int i = 0; i < len; i++) {
            dst[offDst + i] = src[offSrc + i];
        }
        return dst;
    }
    
    /**
     * Copies a float array {@code src} to the destination float array {@code dst}. <br>
     * The number of elements copied is equal to the minimum of the length of both arrays. <br>
     * If the destination array is longer, then all elements are copied. The last elements
     * will remain unchanged. <br>
     * If the source array is longer, then the first elements which fit are copied to the
     * destination array.
     * 
     * @param src The source array.
     * @param dst The destination array.
     * 
     * @return The elements from {@code src} copied to {@code dst}.
     * 
     * @see #copyOf(float[], float[], int, int)
     * @see #copyOf(float[], float[], int, int, int)
     */
    public static float[] copyOf(float[] src, float[] dst) {
        return copyOf(src, dst, 0, 0, Math.min(src.length, dst.length));
    }
    
    /**
     * Copies a float array {@code src} to the destination float array {@code dst}. <br>
     * There will be {@code len} elements copied from {@code src} to {@code dst}, starting
     * at the offset {@code offSrc} from the source array.
     * 
     * @param src The source array.
     * @param dst The destination array.
     * @param offSrc The offset to start copying elements from the source array.
     * @param len The number of elements to copy.
     * 
     * @return The elements from {@code src} copied to {@code dst}.
     * 
     * @see #copyOf(float[], float[], int, int, int)
     */
    public static float[] copyOf(float[] src, float[] dst, int offSrc, int len) {
        return copyOf(src, dst, offSrc, 0, len);
    }
    
    /**
     * Copies a float array {@code src} to the destination float array {@code dst}. <br>
     * There will be {@code len} elements copied from {@code src} to {@code dst}, starting
     * at the offset {@code offSrc} from the source array and the offset {@code offDst} from
     * the destination array.
     * 
     * @param src The source array.
     * @param dst The destination array.
     * @param offSrc The offset to start copying elements from the source array.
     * @param offDst The offset to start writing elements to the destination array.
     * @param len The number of elements to copy.
     * 
     * @return The elements from {@code src} copied to {@code dst}.
     */
    public static float[] copyOf(float[] src, float[] dst, int offSrc, int offDst, int len) {
        for (int i = 0; i < len; i++) {
            dst[offDst + i] = src[offSrc + i];
        }
        return dst;
    }
    
    /**
     * Copies a double array {@code src} to the destination double array {@code dst}. <br>
     * The number of elements copied is equal to the minimum of the length of both arrays. <br>
     * If the destination array is longer, then all elements are copied. The last elements
     * will remain unchanged. <br>
     * If the source array is longer, then the first elements which fit are copied to the
     * destination array.
     * 
     * @param src The source array.
     * @param dst The destination array.
     * 
     * @return The elements from {@code src} copied to {@code dst}.
     * 
     * @see #copyOf(double[], double[], int, int)
     * @see #copyOf(double[], double[], int, int, int)
     */
    public static double[] copyOf(double[] src, double[] dst) {
        return copyOf(src, dst, 0, 0, Math.min(src.length, dst.length));
    }
    
    /**
     * Copies a double array {@code src} to the destination double array {@code dst}. <br>
     * There will be {@code len} elements copied from {@code src} to {@code dst}, starting
     * at the offset {@code offSrc} from the source array.
     * 
     * @param src The source array.
     * @param dst The destination array.
     * @param offSrc The offset to start copying elements from the source array.
     * @param len The number of elements to copy.
     * 
     * @return The elements from {@code src} copied to {@code dst}.
     * 
     * @see #copyOf(double[], double[], int, int, int)
     */
    public static double[] copyOf(double[] src, double[] dst, int offSrc, int len) {
        return copyOf(src, dst, offSrc, 0, len);
    }
    
    /**
     * Copies a double array {@code src} to the destination double long {@code dst}. <br>
     * There will be {@code len} elements copied from {@code src} to {@code dst}, starting
     * at the offset {@code offSrc} from the source array and the offset {@code offDst} from
     * the destination array.
     * 
     * @param src The source array.
     * @param dst The destination array.
     * @param offSrc The offset to start copying elements from the source array.
     * @param offDst The offset to start writing elements to the destination array.
     * @param len The number of elements to copy.
     * 
     * @return The elements from {@code src} copied to {@code dst}.
     */
    public static double[] copyOf(double[] src, double[] dst, int offSrc, int offDst, int len) {
        for (int i = 0; i < len; i++) {
            dst[offDst + i] = src[offSrc + i];
        }
        return dst;
    }
    
    
}
