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

package tools.data.array;


// Java imports
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.RandomAccess;


// Tools imports
import tools.MultiTool;
import tools.PublicCloneable;
import tools.Var;


/**
 * This class represents a robust and fail-fast 2D array. This array allows
 * the basic set and get operations for single elements, a (partial) row or column,
 * and a (partial) 2D array. <br>
 * <br>
 * Additionally, if the component type has a primitive counter part (e.g. Integer - int),
 * then all functions also accept primitive arrays of that counter part. <br>
 * Note that these functions are fail fast, so if an other typed object or primitive
 * typed array is used, then an {@link IllegalArgumentException} is thrown. <br>
 * <br>
 * This class cannot be used for concurrent operations since the operations are
 * <b>NOT</b> atomic.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class Array2D<V>
        implements PublicCloneable, RandomAccess {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The type of the array. */
    private final Class<V> type;
    /** The primitive type of the array, or {@code null} if it doesn't have a primitive type. */
    private final Class<?> primType;
    /** The array containing the data. */
    protected V[][] array;
    /** The height of the array. */
    private int arrayHeight;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    // <editor-fold defaultstate="collapsed" desc="constructors">
    /**
     * Initializes a new 2D array with the given width and height. All values are by
     * default initialized to {@code null}.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code width * height + width}) (for initializing
     * all values to {@code null}).
     * 
     * @param width The width of the array.
     * @param height The height of the array.
     * 
     * @throws IllegalArgumentException If the given {@code width} and/or {@code height}
     *     are negative and non-zero.
     */
    public Array2D(int width, int height, Class<V> type) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Expected positive or zero as size, but found: ["
                    + width + " x " + height + "].");
        }
        array = (V[][]) new Object[width][arrayHeight = height]; // TODO
        this.type = type;
        primType = MultiTool.getPrimitiveTypeOf(type);
    }
    
    /**
     * Initializes a new 2D array with the given width and height. All values are by
     * default initialized to {@code value}.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code width * height + width}).
     * 
     * @param width The width of the array.
     * @param height The height of the array.
     * @param value The default value to initialize the 2D array with.
     * 
     * @throws IllegalArgumentException If the given {@code width} and/or {@code height}
     *     are negative (excluding zero).
     */
    public Array2D(int width, int height, Class<V> type, V value) {
        this(width, height, type);
        fill(value);
    }
    
    /**
     * Clone constructor. The new array will have the same size and data as the source array.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code source.getWidth() * source.getHeight() + source.getWidth()}).
     * 
     * @param source The 2D array to clone.
     */
    public Array2D(Array2D<V> source) {
        array = (V[][]) new Object[source.getWidth()][arrayHeight = source.getHeight()];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                array[i][j] = source.array[i][j];
            }
        }
        type = source.type;
        primType = source.primType;
    }
    
    /**
     * Copies a chunk of data from the given source array. <br>
     * The values which are copied are inside the area denoted by the given bounds.
     * 
     * <h2>Examples</h2>
     * Let's consider the following source:
     * <pre>{@code
     * source = [[1, 2, 3],
     *           [4, 5, 6],
     *           [7, 8, 9]];
     * }</pre>
     * Then arrays are initialized as following:
     * <pre>{@code
     * new Array2D(source, 0, 0, 2, 2) = [[1, 2], [4, 5]];
     * new Array2D(source, 1, 1, 2, 2) = [[5, 6], [8, 9]];
     * new Array2D(source, 0, 0, 4, 4) = [[1   , 2   , 3   , null]
     *                                    [4   , 5   , 6   , null]
     *                                    [7   , 8   , 9   , null]
     *                                    [null, null, null, null]];
     * }</pre>
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code width * height + width}).
     * 
     * @param source The 2D array to copy the values from.
     * @param x The x-coordinate to start copying from.
     * @param y The y-coordinate to start copying from.
     * @param width The width of the area to copy.
     * @param height The height of the area to copy.
     */
    public Array2D(Array2D<V> source, int x, int y, int width, int height) {
        source.checkBounds(x, y, width, height);
        array = (V[][]) new Object[width][arrayHeight = height];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                array[i][j] = source.array[x + i][y + j];
            }
        }
        this.type = source.type;
        this.primType = source.primType;
    }
    // </editor-fold>
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    // <editor-fold defaultstate="collapsed" desc="private functions">
    /**
     * Checks whether the specified bounds are within the bounds of this array.
     * 
     * @param x The x-coordinate of the bounds.
     * @param y The y-coordinate of the bounds.
     * @param width The width of the bounds.
     * @param height The height of the bounds.
     * 
     * @throws IllegalArgumentException If the provided width and/or height
     *     are negative.
     * @throws IndexOutOfBoundsException If the provided bounds are not within
     *     the bounds of this array.
     */
    private void checkBounds(int x, int y, int width, int height)
            throws IllegalArgumentException, IndexOutOfBoundsException {
        checkArea(x, y, width, height, getWidth(), getHeight());
    }
    
    /**
     * Checks whether the designated area is out of bounds. If so, throws an
     * {@link IndexOutOfBoundsException}. Additionally, if the width and/or
     * height are negative, then throw an {@link IndexOutOfBoundsException}.
     * 
     * @param x The x-coordinate of the target area.
     * @param y The y-coordinate of the target area.
     * @param width The width of the target area.
     * @param height The height of the target area.
     * @param widthBound The bounding width.
     * @param heightBound The bounding height.
     * 
     * @throws IndexOutOfBoundsException If the target area is out of bounds.
     */
    private void checkArea(int x, int y, int width, int height,
            int widthBound, int heightBound)
            throws IndexOutOfBoundsException {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException(
                    "Expected the bounds to have a positive width and height, but found the area ["
                            + width + " x " + height + "].");
        }
        if (x < 0 || widthBound < x + width || y < 0 || heightBound < y + height) {
            throw new IndexOutOfBoundsException(
                    "The provided bounds [" + width + " x " + height + "] @ (" + x + ", " + y
                            + ") do not lie within the bounds of the array: ["
                            + widthBound + " x " + heightBound + "].");
        }
    }
    
    /**
     * Checks the type and depth of the given array. <br>
     * The type of the array should be such that this array can read data from the given array.
     * 
     * @param arr The array to check.
     * @param depth The expected depth of the array.
     * 
     * @throws IllegalArgumentException If the depth of the array is incorrect,
     *     or the type of the array is invalid.
     */
    private void checkArrayType(Object arr, int depth)
            throws IllegalArgumentException {
        Class<?> c = arr.getClass();
        for (int i = 0; i < depth; i++) {
            if (!c.isArray()) {
                throw new IllegalArgumentException(
                        "Expected an array with a depth of " + depth
                                + ", but found: '" + arr.getClass().getCanonicalName() + "'.");
            }
            c = c.getComponentType();
        }
        
        if (c != type && c != primType) {
            throw new IllegalArgumentException("Expected an array of type '"
                    + type.getCanonicalName() + (primType == null
                            ? ""
                            : "' or '" + primType.getCanonicalName())
                    + "', but found: '" + c.getCanonicalName() + "'.");
        }
    }
    
    /**
     * Checks the type and depth of the given array. <br>
     * The type of the array should be such that the array can store data from this array.
     * 
     * @param arr The array to check.
     * @param depth The expected depth of the array.
     * 
     * @throws IllegalArgumentException If the depth of the array is incorrect,
     *     or the type of the array is invalid.
     */
    private void checkArrayStoreType(Object arr, int depth)
            throws IllegalArgumentException {
        Class<?> c = arr.getClass();
        for (int i = 0; i < depth; i++) {
            if (!c.isArray()) {
                throw new IllegalArgumentException(
                        "Expected an array with a depth of " + depth
                                + ", but found: '" + arr.getClass().getCanonicalName() + "'.");
            }
            c = c.getComponentType();
        }
        
        if (!c.isAssignableFrom(type) && c != primType) {
            throw new IllegalArgumentException("Expected an array of type '"
                    + type.getCanonicalName() + (primType == null
                            ? ""
                            : "', '" + primType.getCanonicalName())
                    + "' or a super class, but found: '" + c.getCanonicalName() + "'.");
        }
    }
    // </editor-fold>
    
    /**
     * @return The type of the array.
     */
    public Class<V> getType() {
        return type;
    }
    
    /**
     * @return The type of the array.
     */
    public Class<?> getPrimitiveType() {
        return primType;
    }
    
    /**
     * @return The width of the array.
     */
    public int getWidth() {
        return array.length;
    }
    
    /**
     * @return The height of the array.
     */
    public int getHeight() {
        return arrayHeight;
    }
    
    /**
     * Resizes the array and purges the old values.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code width * height + width}) (for initializing
     * all values to {@code null}).
     * 
     * @param width The new width of the array.
     * @param height The new height of the array.
     * 
     * @throws NegativeArraySizeException If the given width and/or height are negative.
     */
    public void setSize(int width, int height) {
        array = (V[][]) new Object[width][height];
        arrayHeight = height;
    }
    
    /**
     * Resizes the array and copies the old values. All values which are inside
     * the new bounds will be kept, but the values outside the new bounds will
     * be removed. Newly generated coordinates will be set to {@code null}.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code width * height + min(width, oldWidth) *
     * min(height, oldHeight)}).
     * 
     * @param width The new width of the array.
     * @param height The new height of the array.
     * 
     * @throws NegativeArraySizeException If the given width and/or height are negative.
     */
    public void setSizeAndCopy(int width, int height) {
        if (width == getWidth() && height == getHeight()) return;
        V[][] newArr = (V[][]) new Object[width][height];
        for (int i = 0; i < array.length && i < newArr.length; i++) {
            for (int j = 0; j < array[i].length && j < newArr[i].length; j++) {
                newArr[i][j] = array[i][j];
            }
        }
        array = newArr;
        arrayHeight = height;
    }
    
    /**
     * Retrieves the element at {@code (x, y)}.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code 1}).
     * 
     * @param x The x-coordinate of the element.
     * @param y The y-coordinate of the element.
     * 
     * @return The element at {@code (x, y)}.
     * 
     * @throws IndexOutOfBoundsException If the coordinates were out of bounds.
     */
    public V get(int x, int y)
            throws IndexOutOfBoundsException {
        return array[x][y];
    }
    
    /**
     * Sets the value at {@code (x, y)}.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code 1}).
     * 
     * @param x The x-coordinate of the element.
     * @param y The y-coordinate of the element.
     * @param value The new value.
     * 
     * @return The old value at {@code (x, y)}.
     * 
     * @throws IndexOutOfBoundsException If the coordinates were out of bounds.
     */
    public V set(int x, int y, V value)
            throws IndexOutOfBoundsException {
        V old = array[x][y];
        array[x][y] = value;
        return old;
    }
    
    // <editor-fold defaultstate="collapsed" desc="setRow">
    /**
     * Copies a part of the source array to a a row of this array.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code min(getWidth(), arr.length)}).
     * 
     * @param arr The array to copy.
     * @param y The row to copy to.
     * 
     * @throws IndexOutOfBoundsException If the provided y-coordinate was out of bounds.
     * 
     * @see #setRow(Object[], int, int, int, int) 
     */
    public void setRow(V[] arr, int y)
            throws IndexOutOfBoundsException {
        setRow(arr, 0, y, 0, Math.min(arr.length, getWidth()));
    }
    
    /**
     * Copies a part of a source array to a row of this array starting at {@code (x, y)}
     * and has a length of {@code len}.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code len}).
     * 
     * @param arr The array to copy.
     * @param targetX The target x-coordinate.
     * @param targetY The target x-coordinate.
     * @param sourceOff The offset from where to start copying from the source array.
     * @param len The total number of value to copy.
     * 
     * @throws IllegalArgumentException If the length {@code len} is negative.
     * @throws IndexOutOfBoundsException If the provided ranges were out of bounds.
     */
    public void setRow(V[] arr, int targetX, int targetY, int sourceOff, int len)
            throws IllegalArgumentException, IndexOutOfBoundsException {
        checkBounds(targetX, targetY, len, 1);
        if (sourceOff < 0 || arr.length <= sourceOff) {
            throw new IndexOutOfBoundsException("The provided range [" + sourceOff + ", "
                    + (sourceOff + len) + "] lies outside the source array (length="
                    + ArrayTools.length(arr) + ")");
        }
        
        for (int i = 0; i < len; i++) {
            array[targetX + i][targetY] = arr[sourceOff + i];
        }
    }
    
    /**
     * Copies a part of the source array to a a row of this array.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code min(getWidth(), arr.length)}).
     * 
     * @implSpec
     * This function should only be used for primitive typed arrays.
     * 
     * @param arr The array to copy.
     * @param y The row to copy to.
     * 
     * @throws IndexOutOfBoundsException If the provided y-coordinate was out of bounds.
     * 
     * @see #setRow(Object, int, int, int, int) 
     */
    public void setRow(Object arr, int y)
            throws IndexOutOfBoundsException {
        setRow(arr, 0, y, 0, Math.min(ArrayTools.length(arr), getWidth()));
    }
    
    /**
     * Copies a part of a source array to a row of this array starting at {@code (x, y)}
     * and has a length of {@code len}.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code len}).
     * 
     * @implSpec
     * This function should only be used for primitive typed arrays.
     * 
     * @param arr The array to copy.
     * @param targetX The target x-coordinate.
     * @param targetY The target x-coordinate.
     * @param sourceOff The offset from where to start copying from the source array.
     * @param len The total number of values to copy.
     * 
     * @throws IllegalArgumentException If {@code arr} is not an array, or if {@code len} is negative.
     * @throws IndexOutOfBoundsException If the provided ranges were out of bounds.
     */
    public void setRow(Object arr, int targetX, int targetY, int sourceOff, int len)
            throws IllegalArgumentException, IndexOutOfBoundsException {
        checkArrayType(arr, 1);
        if (arr instanceof Object[]) {
            setRow((V[]) arr, targetX, targetY, sourceOff, len);
            
        } else {
            checkBounds(targetX, targetY, len, 1);
            if (sourceOff < 0 || ArrayTools.length(arr) <= sourceOff) {
                throw new IndexOutOfBoundsException("The provided range [" + sourceOff + ", "
                        + (sourceOff + len) + "] lies outside the source array (length="
                        + ArrayTools.length(arr) + ")");
            }
            
            int i = targetX;
            for (Iterator<V> it = ArrayTools.<V>getRangeIterator(arr, sourceOff, len); it.hasNext(); ) {
                array[i++][targetY] = it.next();
            }
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="setColumn">
    /**
     * Copies a part of the source array to a a column of this array.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code min(getHeight(), arr.length)}).
     * 
     * @param arr The array to copy.
     * @param x The column to copy to.
     * 
     * @throws IndexOutOfBoundsException If the provided x-coordinate was out of bounds.
     * 
     * @see #setColumn(Object[], int, int, int, int)
     */
    public void setColumn(V[] arr, int x) {
        setColumn(arr, x, 0, 0, Math.min(arr.length, getHeight()));
    }
    
    /**
     * Copies a part of a source array to a column of this array starting at {@code (x, y)}
     * and has a length of {@code len}.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code len}).
     * 
     * @param arr The array to copy.
     * @param targetX The target x-coordinate.
     * @param targetY The target x-coordinate.
     * @param sourceOff The offset from where to start copying from the source array.
     * @param len The total number of value to copy.
     * 
     * @throws IndexOutOfBoundsException If the provided ranges were out of bounds.
     * 
     * @see #setColumn(Object, int, int, int, int)
     */
    public void setColumn(V[] arr, int targetX, int targetY, int sourceOff, int len)
            throws IndexOutOfBoundsException {
        checkBounds(targetX, targetY, 1, len);
        if (sourceOff < 0 || ArrayTools.length(arr) <= sourceOff) {
            throw new IndexOutOfBoundsException("The provided range [" + sourceOff + ", "
                    + (sourceOff + len) + "] lies outside the source array (length="
                    + ArrayTools.length(arr) + ")");
        }
        
        for (int i = 0; i < len; i++) {
            set(targetX, targetY + i, arr[sourceOff + i]);
        }
    }
    
    /**
     * Copies a part of the source array to a a column of this array.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code min(getHeight(), arr.length)}).
     * 
     * @implSpec
     * This function should only be used for primitive typed arrays.
     * 
     * @param arr The array to copy.
     * @param x The column to copy to.
     * 
     * @see #setColumn(Object, int, int, int, int)
     */
    public void setColumn(Object arr, int x) {
        setColumn(arr, x, 0, 0, Math.min(ArrayTools.length(arr), getHeight()));
    }
    
    /**
     * Copies a part of a source array to a column of this array starting at {@code (x, y)}
     * and has a length of {@code len}.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code len}).
     * 
     * @implSpec
     * This function should only be used for primitive typed arrays.
     * 
     * @param arr The array to copy.
     * @param targetX The target x-coordinate.
     * @param targetY The target x-coordinate.
     * @param sourceOff The offset from where to start copying from the source array.
     * @param len The total number of values to copy.
     * 
     * @throws IllegalArgumentException If {@code arr} is not an array.
     * @throws IndexOutOfBoundsException If the provided ranges were out of bounds.
     */
    public void setColumn(Object arr, int targetX, int targetY, int sourceOff, int len)
            throws IllegalArgumentException,  IndexOutOfBoundsException {
        checkArrayType(arr, 1);
        if (arr instanceof Object[]) {
            setColumn((V[]) arr, targetX, targetY, sourceOff, len);
            
        } else {
            checkBounds(targetX, targetY, 1, len);
            if (sourceOff < 0 || ArrayTools.length(arr) <= sourceOff) {
                throw new IndexOutOfBoundsException("The provided range [" + sourceOff + ", "
                        + (sourceOff + len) + "] lies outside the source array (length="
                        + ArrayTools.length(arr) + ")");
            }
            
            int i = targetY;
            for (Iterator<V> it = ArrayTools.<V>getRangeIterator(arr, sourceOff, len); it.hasNext(); ) {
                array[targetX][i++] = it.next();
            }
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="setArray">
    /**
     * Copies the data from the given array to this array.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code min(source.getWidth(), width) *
     * min(source.getHeight(), height)}).
     * 
     * @param source The 2D array to copy the values from.
     */
    public void setArray(Array2D<V> source) {
        int w = Math.min(source.getWidth(), getWidth());
        int h = Math.min(source.getHeight(), getHeight());
        
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                array[i][j] = source.array[i][j];
            }
        }
    }
    
    /**
     * Copies the data within the specified bounds from the given array to
     * the specified bounds of this array.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code width * height}).
     * 
     * @param source The 2D array to copy the values from.
     * @param sourceX The x-coordinate to start copying from.
     * @param sourceY The y-coordinate to start copying from.
     * @param destX The x-coordinate to start copying data to.
     * @param destY The y-coordinate to start copying data to.
     * @param width The width of the area to copy.
     * @param height The height of the area to copy.
     */
    public void setArray(Array2D<V> source, int sourceX, int sourceY, int destX, int destY,
            int width, int height) {
        source.checkBounds(sourceX, sourceY, width, height);
        checkBounds(destX, destY, width, height);
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                array[destX + i][destY + j] = source.array[sourceX + i][sourceY + j];
            }
        }
    }
    
    /**
     * Copies the data from the given array to this array.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code min(source.getWidth(), width) *
     * min(source.getHeight(), height)}).
     * 
     * @param source The 2D array to copy the values from.
     */
    public void setArray(V[][] source) {
        int w = Math.min(source.length, getWidth());
        int h = Math.min(w == 0 ? 0 : ArrayTools.length(source[0]), getHeight());
        
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                array[i][j] = source[i][j];
            }
        }
    }
    
    /**
     * Copies the data within the specified bounds from the given array to
     * the specified bounds of this array.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code width * height}).
     * 
     * @param source The 2D array to copy the values from.
     * @param sourceX The x-coordinate to start copying from.
     * @param sourceY The y-coordinate to start copying from.
     * @param destX The x-coordinate to start copying data to.
     * @param destY The y-coordinate to start copying data to.
     * @param width The width of the area to copy.
     * @param height The height of the area to copy.
     */
    public void setArray(V[][] source, int sourceX, int sourceY, int destX, int destY,
            int width, int height) {
        int w = source.length;
        int h = (w == 0 ? 0 : source[0].length);
        checkArea(sourceX, sourceY, width, height, w, h);
        checkBounds(destX, destY, width, height);
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                array[destX + i][destY + j] = source[sourceX + i][sourceY + j];
            }
        }
    }
    
    /**
     * Copies the data from the given array to this array.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code min(source.getWidth(), width) *
     * min(source.getHeight(), height)}).
     * 
     * @implSpec
     * This function should only be used for primitive typed arrays.
     * 
     * @param source The 2D array to copy the values from.
     */
    public void setArray(Object[] source) {
        checkArrayType(source, 2);
        if (source instanceof Object[][]) {
            setArray((V[][]) source);
            return;
        }
        
        @SuppressWarnings("null") // NullPointerException should be thrown anyways if null.
        int w = Math.min(source.length, getWidth());
        int h = Math.min(w == 0 ? 0 : ArrayTools.length(source[0]), getHeight());
        for (int i = 0; i < w; i++) {
            int j = 0;
            for (Iterator<V> it = ArrayTools.<V>getRangeIterator(
                    source[i], 0, h); it.hasNext(); ) {
                array[i][j++] = it.next();
            }
        }
    }
    
    /**
     * Copies the data within the specified bounds from the given array to
     * the specified bounds of this array.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code width * height}).
     * 
     * @implSpec
     * This function should only be used for primitive typed arrays.
     * 
     * @param source The 2D array to copy the values from.
     * @param sourceX The x-coordinate to start copying from.
     * @param sourceY The y-coordinate to start copying from.
     * @param destX The x-coordinate to start copying data to.
     * @param destY The y-coordinate to start copying data to.
     * @param width The width of the area to copy.
     * @param height The height of the area to copy.
     */
    public void setArray(Object[] source, int sourceX, int sourceY, int destX, int destY,
            int width, int height) {
        checkArrayType(source, 2);
        if (source instanceof Object[][]) {
            setArray((V[][]) source, sourceX, sourceY, destX, destY, width, height);
            return;
        }
        
        @SuppressWarnings("null") // NullPointerException should be thrown anyways if null.
        int w = source.length;
        int h = (w == 0 ? 0 : ArrayTools.length(source[0]));
        checkArea(sourceX, sourceY, width, height, w, h);
        checkBounds(destX, destY, width, height);
        for (int i = 0; i < width; i++) {
            int j = 0;
            for (Iterator<V> it = ArrayTools.<V>getRangeIterator(
                    source[sourceX + i], sourceY, height); it.hasNext(); ) {
                array[destX + i][destY + j++] = it.next();
            }
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="replaceArray">
    /**
     * Purges the current array and creates a new array with the same width, height
     * and values as the given source.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code source.getWidth() * source.getHeight()
     * + source.getWidth()}).
     * 
     * @param source The 2D array to copy the values from.
     */
    public void replaceArray(Array2D<V> source) {
        int w = source.getWidth();
        int h = source.getHeight();
        setSize(w, h);
        
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                array[i][j] = source.array[i][j];
            }
        }
    }
    
    /**
     * Purges the current array and creates a new array with the same width, height
     * and values as the given source.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code source.getWidth() * source.getHeight()
     * + source.getWidth()}).
     * 
     * @param source The 2D array to copy the values from.
     * @param sourceX The x-coordinate to start copying from.
     * @param sourceY The y-coordinate to start copying from.
     * @param width The width of the area to copy.
     * @param height The height of the area to copy.
     * 
     * @throws IllegalArgumentException If the given width and/or height are negative.
     * @throws IndexOutOfBoundsException If the given bounds do not lie within
     *     the bounds of this array or the source array.
     */
    public void replaceArray(Array2D<V> source, int sourceX, int sourceY, int width, int height)
            throws IllegalArgumentException, IndexOutOfBoundsException {
        source.checkBounds(sourceX, sourceY, width, height);
        setSize(width, height);
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                array[i][j] = source.array[sourceX + i][sourceY + j];
            }
        }
    }
    
    /**
     * Purges the current array and creates a new array with the same width, height
     * and values as the given source.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code source.width * source.height + source.width}).
     * 
     * @param source The 2D array to copy the values from.
     */
    public void replaceArray(V[][] source)
            throws IllegalArgumentException {
        int w = source.length;
        int h = (w == 0 ? 0 : source[0].length);
        setSize(w, h);
        
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                array[i][j] = source[i][j];
            }
        }
    }
    
    /**
     * Purges the current array and creates a new array with the same width, height
     * and values as the given source.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code width * height + width}).
     * 
     * @param source The 2D array to copy the values from.
     * @param sourceX The x-coordinate to start copying from.
     * @param sourceY The y-coordinate to start copying from.
     * @param width The width of the area to copy.
     * @param height The height of the area to copy.
     * 
     * @throws IllegalArgumentException If the given width and/or height are negative.
     * @throws IndexOutOfBoundsException If the given bounds do not lie within
     *     the bounds of this array or the source array.
     */
    public void replaceArray(V[][] source, int sourceX, int sourceY, int width, int height)
            throws IllegalArgumentException, IndexOutOfBoundsException {
        int w = source.length;
        int h = (w == 0 ? 0 : source[0].length);
        checkArea(sourceX, sourceY, width, height, w, h);
        setSize(width, height);
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                array[i][j] = source[sourceX + i][sourceY + j];
            }
        }
    }
    
    /**
     * Purges the current array and creates a new array with the same width, height
     * and values as the given source.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code width * height + width}).
     * 
     * @implSpec
     * This function should only be used for primitive typed arrays.
     * 
     * @param source The 2D array to copy the values from.
     * 
     * @throws IllegalArgumentException If the given source is not a 2D array of the same type.
     */
    public void replaceArray(Object[] source)
            throws IllegalArgumentException {
        checkArrayType(source, 2);
        if (source instanceof Object[][]) {
            replaceArray((V[][]) source);
            return;
        }
        
        @SuppressWarnings("null") // NullPointerException should be thrown anyways if null.
        int w = source.length;
        int h = (w == 0 ? 0 : ArrayTools.length(source[0]));
        setSize(w, h);
        
        for (int i = 0; i < w; i++) {
            int j = 0;
            for (Iterator<V> it = ArrayTools.<V>getRangeIterator(source[i], 0, h); it.hasNext(); ) {
                array[i][j++] = it.next();
            }
        }
    }
    
    /**
     * Purges the current array and creates a new array with the same width, height
     * and values as the given source.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code width * height + width}).
     * 
     * @implSpec
     * This function should only be used for primitive typed arrays.
     * 
     * @param source The 2D array to copy the values from.
     * @param sourceX The x-coordinate to start copying from.
     * @param sourceY The y-coordinate to start copying from.
     * @param width The width of the area to copy.
     * @param height The height of the area to copy.
     * 
     * @throws IllegalArgumentException If the given source is not a 2D array of the same type,
     *     or if the given width and/or height are negative.
     * @throws IndexOutOfBoundsException If the given source coordinates are outside the
     *     bounds of the source.
     */
    public void replaceArray(Object[] source, int sourceX, int sourceY, int width, int height)
            throws IndexOutOfBoundsException {
        checkArrayType(source, 2);
        if (source instanceof Object[][]) {
            replaceArray((V[][]) source, sourceX, sourceY, width, height);
            return;
        }
        
        @SuppressWarnings("null") // NullPointerException should be thrown anyways if null.
        int w = source.length;
        int h = (w == 0 ? 0 : ArrayTools.length(source[0]));
        checkArea(sourceX, sourceY, width, height, w, h);
        setSize(width, height);
        for (int i = 0; i < width; i++) {
            int j = 0;
            for (Iterator<V> it = ArrayTools.<V>getRangeIterator(
                    source[sourceX + i], sourceY, height); it.hasNext(); ) {
                array[i][j++] = it.next();
            }
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="fill">
    /**
     * Fills the entire array with the given value.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code getWidth() * getHeight()}).
     * 
     * @param value The value to fill the array with.
     * 
     * @throws IllegalArgumentException If the provided width and/or height are negative.
     * @throws IndexOutOfBoundsException If the provided bounds are not within the
     *     bounds of this array.
     */
    public void fill(V value)
            throws IllegalArgumentException, IndexOutOfBoundsException {
        fill(0, 0, getWidth(), getHeight(), value);
    }
    
    /**
     * Fills the specified part of the array with the specified value.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code width * height}).
     * 
     * @param x The x-coordinate to start filling the array from.
     * @param y The y-coordinate to start filling the array from.
     * @param width The width of the area to fill.
     * @param height The height of the area to fill.
     * @param value The value to fill the array with.
     * 
     * @throws IllegalArgumentException If the provided width and/or height are negative.
     * @throws IndexOutOfBoundsException If the provided bounds are not within the bounds of this array.
     */
    public void fill(int x, int y, int width, int height, V value)
            throws IllegalArgumentException, IndexOutOfBoundsException {
        checkBounds(x, y, width, height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                array[x + i][y + j] = value;
            }
        }
    }
    // </editor-fold>
    
    /**
     * Copies the data inside the given bounds to a new 2D array. <br>
     * For more information, take a look at {@link #Array2D(Array2D, int, int, int, int)}.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code width * height + width}).
     * 
     * @param x The x-coordinate to start copying from.
     * @param y The y-coordinate to start copying from.
     * @param width The width of the area to copy.
     * @param height The height of the area to copy.
     * 
     * @return A new 2D array containing the data inside the given bounds.
     * 
     * @see #Array2D(Array2D, int, int, int, int)
     * 
     * @throws IllegalArgumentException If the provided width and/or height
     are negative.
     * @throws IndexOutOfBoundsException If the provided bounds are not within
     *     the bounds of this array or the source array.
     */
    public Array2D<V> getSubArray(int x, int y, int width, int height)
            throws IllegalArgumentException, IndexOutOfBoundsException {
        return new Array2D<V>(this, x, y, width, height);
    }
    
    // <editor-fold defaultstate="collapsed" desc="getRow">
    /**
     * Returns a fresh primitive typed array containing values in the specified row.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code width}).
     * This function is slower compared to the other {@code getRow} functions
     * because it generically creates an array.
     * 
     * @param y The row to get.
     * 
     * @return A primative array having the same width as this array,
     *     and contains the values in the {@code y'th} row of this array.
     * 
     * @throws IllegalStateException If the type of this array doesn't have a primitive type.
     * @throws IndexOutOfBoundsException If the given {@code y} value
     *     lies outside the bounds of this array.
     */
    public <T> T getPrimRow(int y)
            throws IllegalStateException, IndexOutOfBoundsException {
        if (primType == null) {
            throw new IllegalStateException("The type " + type.getName()
                    + " doesn't have a primitive type!");
        }
        int width = getWidth();
        Object arr = Array.newInstance(primType, width);
        getRow(arr, y);
        return (T) arr;
    }
    
    /**
     * Returns a fresh array containing values in the specified row.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code width}).
     * This function is slower compared to the other {@code getRow} functions
     * because it generically creates an array.
     * 
     * @param y The row to get.
     * 
     * @return A array having the same width as this array, and contains the
     *     values in the {@code y'th} column of this array.
     * 
     * @throws IndexOutOfBoundsException If the given {@code y} value
     *     lies outside the bounds of this array.
     */
    public V[] getRow(int y) {
        int width = getWidth();
        V[] arr = (V[]) Array.newInstance(type, width);
        getRow(arr, 0, y, 0, width);
        return arr;
    }
    
    /**
     * Copies the values of the row to the provided array.
     * The indices in the destination array which lie outside
     * the bounds of the array will remain unchanged. <br>
     * <br>
     * The values are copied from the {@code y}'th row.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code min(dest.length, width)}).
     * 
     * @param dest The destination array.
     * @param y The y-coordinate to start copying values from.
     * 
     * @return The destination array.
     * 
     * @throws IndexOutOfBoundsException If the given {@code y} value
     *     lies outside the bounds of this array.
     */
    public V[] getRow(V[] dest, int y)
            throws IndexOutOfBoundsException {
        return getRow(dest, 0, y, 0, Math.min(dest.length, getWidth()));
    }
    
    /**
     * Copies the values of the row to the provided array.
     * The indices in the destination array which lie outside
     * the bounds of the array will remain unchanged. <br>
     * <br>
     * The values are copied from the {@code y}'th row, and are copied from
     * the {@code x}'th index onwards.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code len}).
     * 
     * @param dest The destination array.
     * @param x The x-coordinate to start copying values from. Default is {@code 0}.
     * @param y The y-coordinate to start copying values from.
     * @param off The offset to start placing values in the destination array.
     * @param len The maximum number of elements to return.
     * 
     * @return The destination array.
     * 
     * @throws IndexOutOfBoundsException If the given {@code x} and/or {@code y}
     *     values lie outside the bounds of this array or the destination array.
     */
    public V[] getRow(V[] dest, int x, int y, int off, int len)
            throws IndexOutOfBoundsException {
        checkArea(off, 0, len, 1, ArrayTools.length(dest), 1);
        checkBounds(x, y, len, 1);
        for (int i = 0; i < len; i++) {
            dest[off + i] = array[x + i][y];
        }
        return dest;
    }
    
    /**
     * Copies the values of the row to the provided array.
     * The indices in the destination array which lie outside
     * the bounds of the array will remain unchanged. <br>
     * <br>
     * The values are copied from the {@code y}'th row.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code min(dest.length, width}).
     * 
     * @implSpec
     * This function should only be used for primitive typed arrays, or super classes.
     * 
     * @param dest The destination array.
     * @param y The y-coordinate to start copying values from.
     * 
     * @return The destination array.
     * 
     * @throws IllegalArgumentException If the depth of the array is incorrect,
     *     or the type of the array is invalid.
     * @throws IndexOutOfBoundsException If the given {@code y} value lies outside
     *     the bounds of this array.
     */
    public <T> T getRow(T dest, int y)
            throws IllegalArgumentException, IndexOutOfBoundsException {
        return getRow(dest, 0, y, 0, Math.min(ArrayTools.length(dest), getWidth()));
    }
    
    /**
     * Copies the values of the row to the provided array.
     * The indices in the destination array which lie outside
     * the bounds of the array will remain unchanged. <br>
     * <br>
     * The values are copied from the {@code y}'th row, and are copied from
     * the {@code x}'th index onwards.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code len}).
     * 
     * @implSpec
     * This function should only be used for primitive typed arrays, or super classes.
     * 
     * @param dest The destination array.
     * @param x The x-coordinate to start copying values from. Default is {@code 0}.
     * @param y The y-coordinate to start copying values from.
     * @param off The offset to start placing values in the destination array.
     * @param len The maximum number of elements to return.
     * 
     * @return The destination array.
     * 
     * @throws IllegalArgumentException If the depth of the array is incorrect,
     *     or the type of the array is invalid.
     * @throws IndexOutOfBoundsException If the given {@code x}  and /or {@code y}
     *     values lie outside the bounds of this array.
     */
    public <T> T getRow(T dest, int x, int y, int off, int len)
            throws IllegalArgumentException, IndexOutOfBoundsException {
        checkArrayStoreType(dest, 1);
        if (dest instanceof Object[]) {
            return (T) getRow((V[]) dest, x, y, off, len);
            
        } else {
            checkArea(off, 0, len, 1, ArrayTools.length(dest), 1);
            checkBounds(x, y, len, 1);
            Object defValue = MultiTool.getDefaultPrim(primType);
            ArrayTools.setRange(dest, off, len, (int i) -> {
                V val = array[x + i - off][y];
                return (val == null ? defValue : val);
            });
            return dest;
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="getColumn">
    /**
     * Returns a fresh primitive typed array containing values in the specified column.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code height}).
     * This function is slower compared to the other {@code getColumn} functions
     * because it generically creates an array. <br>
     * <br>
     * Additionally, all {@code null} values are converted to the default value
     * the primitive type of this array.
     * 
     * @param x The column to get.
     * 
     * @return A primative array having the same width as this array, and
     *     contains the values in the {@code y'th} column of this array.
     * 
     * @throws IllegalStateException If the type of this array doesn't have a primitive type.
     * @throws IndexOutOfBoundsException If the given {@code y} value
     *     lies outside the bounds of this array.
     */
    public <T> T getPrimColumn(int x)
            throws IllegalStateException, IndexOutOfBoundsException {
        if (primType == null) {
            throw new IllegalStateException("The type " + type.getName()
                    + " doesn't have a primitive type!");
        }
        int height = getHeight();
        Object arr = Array.newInstance(primType, height);
        getColumn(arr, x);
        return (T) arr;
    }
    
    /**
     * Returns a fresh array containing values in the specified column.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code height}).
     * This function is slower compared to the other {@code getColumn} functions
     * because it generically creates an array.
     * 
     * @param x The column to get.
     * 
     * @return A array having the same width as this array, and contains the
     *     values in the {@code y'th} column of this array.
     * 
     * @throws IndexOutOfBoundsException If the given {@code y} value
     *     lies outside the bounds of this array.
     */
    public V[] getColumn(int x) {
        int height = getHeight();
        V[] arr = (V[]) Array.newInstance(type, height);
        getColumn(arr, x, 0, 0, height);
        return arr;
    }
    
    /**
     * Copies the values of the column to the provided array.
     * The indices in the destination array which lie outside
     * the bounds of the array will remain unchanged. <br>
     * <br>
     * The values are copied from the {@code x}'th column.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code min(dest.length, height)}).
     * 
     * @param dest The destination array.
     * @param x The x-coordinate to start copying values from.
     * 
     * @return The destination array.
     * 
     * @throws IndexOutOfBoundsException If the given {@code x} value
     *     lies outside the bounds of this array.
     */
    public V[] getColumn(V[] dest, int x)
            throws IndexOutOfBoundsException {
        return getColumn(dest, x, 0, 0, Math.min(dest.length, getHeight()));
    }
    
    /**
     * Copies the values of the column to the provided array.
     * The indices in the destination array which lie outside
     * the bounds of the array will remain unchanged. <br>
     * <br>
     * The values are copied from the {@code x}'th column, and are copied from
     * the {@code y}'th index onwards.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code len}).
     * 
     * @param dest The destination array.
     * @param x The x-coordinate to start copying values from.
     * @param y The y-coordinate to start copying values from. Default is {@code 0}.
     * @param off The offset to start placing values in the destination array.
     * @param len The number of values to set in the array.
     * 
     * @return The destination array.
     * 
     * @throws IndexOutOfBoundsException If the given {@code x} and/or {@code y}
     *     values lie outside the bounds of this array or the destination array.
     */
    public V[] getColumn(V[] dest, int x, int y, int off, int len)
            throws IndexOutOfBoundsException {
        checkArea(off, 0, len, 1, dest.length, 1);
        checkBounds(x, y, 1, len);
        for (int i = 0; i < len; i++) {
            dest[off + i] = array[x][y + i];
        }
        return dest;
    }
    
    /**
     * Copies the values of the column to the provided array.
     * The indices in the destination array which lie outside the bounds of the array
     * will remain unchanged. <br>
     * <br>
     * The values are copied from the {@code x}'th column, and are copied from
     * the {@code y}'th index onwards. <br>
     * <br>
     * If the given array is a primitive typed array, then all {@code null} values are converted
     * to the default value the primitive type of this array.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code min(dest.length, height)}).
     * 
     * @implSpec
     * This function should only be used for primitive typed arrays, or super classes.
     * 
     * @param dest The destination array.
     * @param x The x-coordinate to start copying values from.
     * 
     * @return The destination array.
     * 
     * @throws IllegalArgumentException If the depth of the array is incorrect,
     *     or the type of the array is invalid.
     * @throws IndexOutOfBoundsException If the given {@code x} value lies outside
     *     the bounds of this array.
     */
    public <T> T getColumn(T dest, int x)
            throws IllegalArgumentException, IndexOutOfBoundsException {
        return getColumn(dest, x, 0, 0, Math.min(ArrayTools.length(dest), getHeight()));
    }
    
    /**
     * Copies the values of the column to the provided array.
     * The indices in the destination array which lie outside
     * the bounds of the array will remain unchanged. <br>
     * <br>
     * The values are copied from the {@code x}'th column, and are copied from
     * the {@code y}'th index onwards. <br>
     * <br>
     * If the given array is a primitive typed array, then all {@code null} values are converted
     * to the default value the primitive type of this array.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code len}).
     * 
     * @implSpec
     * This function should only be used for primitive typed arrays or super classes.
     * 
     * @param dest The destination array.
     * @param x The x-coordinate to start copying values from.
     * @param y The y-coordinate to start copying values from. Default is {@code 0}.
     * @param off The offset to start placing values in the destination array.
     * @param len The number of values to set in the array.
     * 
     * @return The destination array.
     * 
     * @throws IllegalArgumentException If the depth of the array is incorrect,
     *     or the type of the array is invalid.
     * @throws IndexOutOfBoundsException If the given {@code x} or {@code y} values
     *     lie outside the bounds of this array.
     */
    public <T> T getColumn(T dest, int x, int y, int off, int len)
            throws IllegalArgumentException, IndexOutOfBoundsException {
        checkArrayStoreType(dest, 1);
        if (dest instanceof Object[]) {
            return (T) getColumn((V[]) dest, x, y, off, len);
            
        } else {
            checkArea(off, 0, len, 1, ArrayTools.length(dest), 1);
            checkBounds(x, y, 1, len);
            Object defValue = MultiTool.getDefaultPrim(primType);
            ArrayTools.setRange(dest, off, len, (int i) -> {
                V val = array[x][y + i - off];
                return (val == null ? defValue : val);
            });
            return dest;
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="getArray">
    /**
     * Returns a primitive array of size [{@code getWidth() x getHeight()}] which
     * contains the elements of this array. <br>
     * <br>
     * Additionally, all {@code null} values are converted to the default value
     * the primitive type of this array.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code getWidth() * getHeight() + getWidth()}). <br>
     * This function is slower compared to the other {@code getArray} functions
     * because it generically creates an array.
     * 
     * @return A primative array having the size of this array and containing the values of this array.
     * 
     * @throws IllegalStateException If the type of this array doesn't have a primitive type.
     */
    public <T> T getPrimArray()
            throws IllegalStateException {
        if (primType == null) {
            throw new IllegalStateException("The type " + type.getName()
                    + " doesn't have a primitive type!");
        }
        int width = getWidth();
        int height = getHeight();
        Object[][] arr = (Object[][]) Array.newInstance(primType, width, height);
        Object defValue = MultiTool.getDefaultPrim(primType);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                V val = array[i][j];
                return (T) (val == null ? defValue : val);
            }
        }
        
        return (T) arr;
    }
    
    /**
     * Returns an array of size [{@code getWidth() x getHeight()}] which contains the
     * elements of this array.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code width * height + width}) <br>
     * This function is slower compared to the other {@code getArray} functions
     * because it generically creates an array.
     * 
     * @return An array having the size of this array and containing the values of this array.
     */
    public V[][] getArray() {
        int width = getWidth();
        int height = getHeight();
        V[][] arr = (V[][]) Array.newInstance(type, width, height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                arr[i][j] = array[i][j];
            }
        }
        
        return arr;
    }
    
    /**
     * Copies the data inside the given bounds from the source array to {@code this} array. <br>
     * If the provided width or height are negative, then the values are mirrored in
     * respectively the width or height. The values which are copied are inside the area
     * denoted by the given bounds. <br>
     * <br>
     * If the given array is a primitive typed array, then all {@code null} values are converted
     * to the default value the primitive type of this array.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code width * height}).
     * 
     * @param dest The destination array.
     * @param x The x-coordinate to start copying from.
     * @param y The y-coordinate to start copying from.
     * @param offX The x-coordinate to start pasting to.
     * @param offY The y-coordinate to start pasting to.
     * @param width The width of the area to copy.
     * @param height The height of the area to copy.
     * 
     * @throws IllegalArgumentException If the provided width and/or height are negative.
     * @throws IndexOutOfBoundsException If the provided bounds are not within
     *     the bounds of this array or the destination array.
     */
    public Array2D<V> getArray(Array2D<V> dest, int x, int y, int offX, int offY,
            int width, int height)
            throws IllegalArgumentException, IndexOutOfBoundsException {
        checkBounds(x, y, width, height);
        dest.checkBounds(offX, offY, width, height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                array[offX + i][offY + j] = dest.array[x + i][y + j];
            }
        }
        return dest;
    }
    
    /**
     * Copies the values from this array to the given destination array. <br>
     * The indices in the destination array which lie outside the bounds of
     * this array will remain unchanged in the destination array.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code min(dest.length, width) * min(dest[0].length, height)}).
     * 
     * @param dest The destination array.
     * 
     * @return The destination array.
     */
    public V[][] getArray(V[][] dest) {
        int w = Math.min(dest.length, getWidth());
        int h = Math.min(dest[0].length, getHeight());
        
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                dest[i][j] = array[i][j];
            }
        }
        return dest;
    }
    
    /**
     * Copies the values of this array to the provided 2D array.
     * The indices in the destination array which lie outside
     * the bounds of the array will remain unchanged.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code width * height}).
     * 
     * @param dest The destination array.
     * @param x The x-coordinate to start copying from.
     * @param y The y-coordinate to start copying from.
     * @param offX The x-coordinate to start writing the values to.
     * @param offY The y-coordinate to start writing the values to.
     * @param width The width of the area to get.
     * @param height The height of the area to get.
     * 
     * @return The destination array.
     * 
     * @throws IndexOutOfBoundsException If the provided bounds are not within
     *     the bounds of this array or the destination array.
     */
    public V[][] getArray(V[][] dest, int x, int y, int offX, int offY, int width, int height)
            throws IndexOutOfBoundsException {
        checkBounds(x, y, width, height);
        int w = dest.length;
        int h = (w == 0 ? 0 : dest[0].length);
        checkArea(offX, offY, width, height, w, h);
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                dest[offX + i][offY + j] = array[x + i][y + j];
            }
        }
        
        return dest;
    }
    
    /**
     * Copies the values of this array to the provided 2D array.
     * The indices in the destination array which lie outside
     * the bounds of this array will remain unchanged in the destination array. <br>
     * <br>
     * If the given array is a primitive typed array, then all {@code null} values are converted
     * to the default value the primitive type of this array.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code min(dest.length, width) * min(dest[0].length, height)}).
     * 
     * @implSpec
     * This function should only be used for primitive typed arrays, or super classes.
     * 
     * @param dest The destination array.
     * 
     * @return The destination array.
     * 
     * @throws IllegalArgumentException If the depth of the array is incorrect,
     *     or the type of the array is invalid.
     */
    public <T> T[] getArray(T[] dest)
            throws IllegalArgumentException {
        checkArrayStoreType(dest, 2);
        if (dest instanceof Object[][]) {
            return (T[]) getArray((V[][]) dest);
        }
        @SuppressWarnings("null") // NullPointerException should be thrown anyways if null.
        int w = Math.min(dest.length, getWidth());
        int h = Math.min(ArrayTools.length(dest[0]), getHeight());
        
        Object defValue = MultiTool.getDefaultPrim(primType);
        for (int index = 0; index < w; index++) {
            final int i = index;
            ArrayTools.setRange(dest, 0, h, (j) -> {
                V val = array[i][j];
                if (val == null) return defValue;
                return val;
            });
        }
        return dest;
    }
    
    /**
     * Copies the values of this array to the provided 2D array.
     * The indices in the destination array which lie outside
     * the bounds of the array will remain unchanged. <br>
     * <br>
     * If the given array is a primitive typed array, then all {@code null} values are converted
     * to the default value the primitive type of this array.
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code width * height}).
     * 
     * @implSpec
     * This function should only be used for primitive typed arrays, or super classes.
     * 
     * @param dest The destination array.
     * @param x The x-coordinate to start copying from.
     * @param y The y-coordinate to start copying from.
     * @param offX The x-coordinate to start writing the values to.
     * @param offY The y-coordinate to start writing the values to.
     * @param width The width of the area to get.
     * @param height The height of the area to get.
     * 
     * @return The destination array.
     * 
     * @throws IllegalArgumentException If the depth of the array is incorrect,
     *     or the type of the array is invalid.
     * @throws IndexOutOfBoundsException If the provided bounds are not within
     *     the bounds of this array or the source array.
     */
    public <T> T[] getArray(T[] dest, int x, int y, int offX, int offY, int width, int height)
            throws IllegalArgumentException, IndexOutOfBoundsException {
        checkArrayStoreType(dest, 2);
        if (dest instanceof Object[][]) {
            return (T[]) getArray((V[][]) dest, x, y, offX, offY, width, height);
        }
        
        checkBounds(x, y, width, height);
        @SuppressWarnings("null") // NullPointerException should be thrown anyways if null.
        int w = dest.length;
        int h = (w == 0 ? 0 : ArrayTools.length(dest[0]));
        checkArea(offX, offY, width, height, w, h);
        
        Object defValue = MultiTool.getDefaultPrim(primType);
        for (int index = 0; index < width; index++) {
            final int i = index;
            ArrayTools.setRange(dest[offX + i], offY, height, (j) -> {
                V val = array[x + i][y + j - offY];
                if (val == null) return defValue;
                return val;
            });
        }
        
        return dest;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Object overrides">
    /**
     * {@inheritDoc}
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code getWidth() * getHeight()}).
     * 
     * @see #Array2D(Array2D)
     */
    @Override
    public Array2D<V> clone() {
        return new Array2D<V>(this);
    }
    
    /**
     * {@inheritDoc}
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code getWidth() * getHeight()}).
     */
    @Override
    public String toString() {
        int w = getWidth();
        int h = getHeight();
        int[] max = new int[w];
        String[][] data = new String[w][h];
        
        // Generate data + keep track of max.
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                data[i][j] = (array[i][j] == null ? "null" : array[i][j].toString());
                max[i] = Math.max(max[i], data[i][j].length());
            }
        }
        
        StringBuilder sb = new StringBuilder("╔");
        
        // Generate divider based on max.
        StringBuilder midDivBuilder = new StringBuilder("╟");
        StringBuilder postDivBuilder = new StringBuilder("╚");
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < max[i] + 2; j++) {
                sb.append("═");
                midDivBuilder.append("─");
                postDivBuilder.append("═");
            }
            if (i != w-1) {
                sb.append("╤");
                midDivBuilder.append("┼");
                postDivBuilder.append("╧");
            }
        }
        sb.append("╗");
        midDivBuilder.append("╢");
        postDivBuilder.append("╝");
        String div = midDivBuilder.toString();
        
        // Write data + dividers.
        sb.append(Var.LS);
        for (int j = 0; j < h; j++) {
            sb.append("║ ");
            for (int i = 0; i < w; i++) {
                if (i != 0) sb.append(" │ ");
                sb.append(MultiTool.fillLeft(data[i][j], max[i], ' '));
            }
            sb.append(" ║");
            sb.append(Var.LS);
            if (j != h-1) {
                sb.append(div);
                sb.append(Var.LS);
            }
        }
        sb.append(postDivBuilder);
        return sb.toString();
    }
    
    /**
     * {@inheritDoc}
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code getWidth() * getHeight()}).
     */
    @Override
    public int hashCode() {
        return MultiTool.calcHashCode(array);
    }
    
    /**
     * {@inheritDoc}
     * 
     * @apiNote
     * This function runs in <i>O</i>({@code getWidth() * getHeight()}).
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        
        if (obj instanceof Array2D) {
            Array2D arr = (Array2D) obj;
            if (getHeight() != arr.getHeight()) return false;
            return ArrayTools.deepEquals(arr.array, array);
            
        } else {
            return ArrayTools.deepEquals(array, obj);
        }
    }
    // </editor-fold>
    
    
}
