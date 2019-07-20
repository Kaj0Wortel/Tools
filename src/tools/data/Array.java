/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) May 2019 by Kaj Wortel - all rights reserved                *
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
 * - binarySearch(byte[] a, byte key) (all types)
 * - binarySearch(byte[] a, int fromIndex, int toIndex, byte key) (all types)
 * - copyOf(boolean[] original, int newLength)
 * - copyOfRange(boolean[] original, int from, int to)
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
        if (array instanceof Object[]) {
            return Arrays.toString((Object[]) array);
            
        } else if (array instanceof boolean[]) {
            return Arrays.toString((boolean[]) array);
            
        } else if (array instanceof byte[]) {
            return Arrays.toString((byte[]) array);
            
        } else if (array instanceof char[]) {
            return Arrays.toString((char[]) array);
            
        } else if (array instanceof short[]) {
            return Arrays.toString((short[]) array);
            
        } else if (array instanceof int[]) {
            return Arrays.toString((int[]) array);
            
        } else if (array instanceof long[]) {
            return Arrays.toString((long[]) array);
            
        } else if (array instanceof float[]) {
            return Arrays.toString((float[]) array);
            
        } else if (array instanceof double[]) {
            return Arrays.toString((double[]) array);
            
        } else if (array == null) {
            throw new NullPointerException("Array was null!");
            
        } else if (!array.getClass().isArray()) {
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
        if (array instanceof Object[]) {
            return ((Object[]) array).length;
            
        } else if (array instanceof boolean[]) {
            return ((boolean[]) array).length;
            
        } else if (array instanceof byte[]) {
            return ((byte[]) array).length;
            
        } else if (array instanceof char[]) {
            return ((char[]) array).length;
            
        } else if (array instanceof short[]) {
            return ((short[]) array).length;
            
        } else if (array instanceof int[]) {
            return ((int[]) array).length;
            
        } else if (array instanceof long[]) {
            return ((long[]) array).length;
            
        } else if (array instanceof float[]) {
            return ((float[]) array).length;
            
        } else if (array instanceof double[]) {
            return ((double[]) array).length;
            
        } else if (array == null) {
            throw new NullPointerException("Array was null!");
            
        } else if (!array.getClass().isArray()) {
            throw new IllegalArgumentException("Argument was not an array!");
        }
        throw new IllegalStateException();
    }

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
        if (array instanceof Object[]) {
            return ((Object[]) array)[index];
            
        } else if (array instanceof boolean[]) {
            return ((boolean[]) array)[index];
            
        } else if (array instanceof byte[]) {
            return ((byte[]) array)[index];
            
        } else if (array instanceof char[]) {
            return ((char[]) array)[index];
            
        } else if (array instanceof short[]) {
            return ((short[]) array)[index];
            
        } else if (array instanceof int[]) {
            return ((int[]) array)[index];
            
        } else if (array instanceof long[]) {
            return ((long[]) array)[index];
            
        } else if (array instanceof float[]) {
            return ((float[]) array)[index];
            
        } else if (array instanceof double[]) {
            return ((double[]) array)[index];
            
        } else if (array == null) {
            throw new NullPointerException("Array was null!");
            
        } else if (!array.getClass().isArray()) {
            throw new IllegalArgumentException("Argument was not an array!");
            
        } else {
            throw new IllegalStateException();
        }
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
        if (array instanceof Object[]) {
            ((Object[]) array)[index] = value;
            
        } else if (array instanceof boolean[]) {
            ((boolean[]) array)[index] = (boolean) value;
            
        } else if (array instanceof byte[]) {
            ((byte[]) array)[index] = (byte) value;
            
        } else if (array instanceof char[]) {
            ((char[]) array)[index] = (char) value;
            
        } else if (array instanceof short[]) {
            ((short[]) array)[index] = (short) value;
            
        } else if (array instanceof int[]) {
            ((int[]) array)[index] = (int) value;
            
        } else if (array instanceof long[]) {
            ((long[]) array)[index] = (long) value;
            
        } else if (array instanceof float[]) {
            ((float[]) array)[index] = (float) value;
            
        } else if (array instanceof double[]) {
            ((double[]) array)[index] = (double) value;
            
        } else if (array == null) {
            throw new NullPointerException("Array was null!");
            
        } else if (!array.getClass().isArray()) {
            throw new IllegalArgumentException("Argument was not an array!");
            
        } else {
            throw new IllegalStateException();
        }
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
    
    
}
