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


// JUnit imports
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;


// Test imports.
import tools.AbstractTestClass;


/**
 * Test class for the {@link Array2D} class
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class Array2DTest
        extends AbstractTestClass {
    
    /* -------------------------------------------------------------------------
     * Helper functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Checks whether the size of the array is conform to the provided width and height.
     * 
     * @param arr The array to check.
     * @param width The expected width.
     * @param height The expected height.
     */
    private static void assertSize(Array2D arr, int width, int height) {
        assertEquals(arr.getWidth(), width);
        assertEquals(arr.getHeight(), height);
    }
    
    /**
     * Checks whether array only contains the given value.
     * 
     * @param <V> The type of the value.
     * 
     * @param arr The array to check.
     * @param val The value to check for.
     */
    private static <V> void assertValue(Array2D<V> arr, V val) {
        for (int i = 0; i < arr.getWidth(); i++) {
            for (int j = 0; j < arr.getHeight(); j++) {
                assertEquals(genIndex(i, j), val, arr.get(i, j));
            }
        }
    }
    
    /**
     * Checks whether the given area contains the right value, and the remaining area another value.
     * 
     * @param <V> The type of the values.
     * 
     * @param arr The array to check.
     * @param x The x-coord of the special area.
     * @param y The y-coord of the special area.
     * @param w The width of the special area.
     * @param h The height of the special area.
     * @param val The value for the special area.
     * @param def The value for the remaining area.
     */
    private static <V> void assertValue(Array2D<V> arr, int x, int y, int w, int h, V val, V def) {
        for (int i = 0; i < arr.getWidth(); i++) {
            for (int j = 0; j < arr.getHeight(); j++) {
                if (x <= i && i < x + w && y <= j && j < y + h) {
                    assertEquals(genIndex(i, j), val, (Object) arr.get(i, j));
                } else {
                    assertEquals(genIndex(i, j), def, (Object) arr.get(i, j));
                }
            }
        }
    }
    
    /**
     * Fills the given {@link Array2D} within the bounds with the expected value, and then
     * checks whether the values were actually filled in.
     * 
     * @param <V> The type of the array.
     * 
     * @param arr The array to check.
     * @param x The x-coord to start filling.
     * @param y The y-coord to start filling.
     * @param w The width of the aread to fill.
     * @param h The height of the area to fill.
     * @param expected The value to fill.
     */
    private <V> void checkFill(Array2D arr, int x, int y, int w, int h, V expected) {
        arr.fill(x, y, w, h, expected);
        for (int i = x; i < w; i++) {
            for (int j = y; j < h; j++) {
                Object found = arr.get(i, j);
                assertEquals(genIndex(i, j), expected, found);
            }
        }
    }
    
    /**
     * Generates and checks the generated values of the sub-array from a source array.
     * 
     * @param arr The source array.
     * @param x The x-coordinate used to generate the sub-array.
     * @param x The y-coordinate used to generate the sub-array.
     * @param width The width of the sub-array.
     * @param height The height of the sub-array.
     */
    private static <V> void checkSub(Array2D<V> arr, int x, int y, int width, int height) {
        Array2D<V> sub = arr.getSubArray(x, y, width, height);
        assertEquals(width, sub.getWidth());
        assertEquals(height, sub.getHeight());
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                assertEquals(genIndex(x, y, width, height, i, j),arr.get(x + i, y + j), sub.get(i, j));
            }
        }
    }
    
    /**
     * Fills the given array with values from {@code init} to
     * {@code init + arr.getWidth() * arr.getHeight()}. The rows are filled incrementally.
     * 
     * @param arr The array to fill.
     * @param init The initial number in the array.
     */
    private static void incrFill(Array2D<Integer> arr, int init) {
        int val = init;
        for (int j = 0; j < arr.getHeight(); j++) {
            for (int i = 0; i < arr.getWidth(); i++) {
                arr.set(i, j, val++);
            }
        }
    }
    
    
    /* -------------------------------------------------------------------------
     * Test functions.
     * -------------------------------------------------------------------------
     */
    // <editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Test for {@link Array2D#Array2D(int, int)}. <br>
     * Test with normal values.
     */
    @Test
    public void constructor0() {
        Array2D<Integer> arr = new Array2D<Integer>(10, 20, Integer.class);
        assertTrue("Expected width 10, but found: " + arr.getWidth(), arr.getWidth() == 10);
        assertTrue("Expected width 20, but found: " + arr.getHeight(), arr.getHeight() == 20);
    }
    
    /**
     * Test for {@link Array2D#Array2D(int, int)}. <br>
     * Test with zero width.
     */
    @Test
    public void constructor1() {
        Array2D<Integer> arr = new Array2D<Integer>(0, 20, Integer.class);
        assertTrue("Expected width 0, but found: " + arr.getWidth(), arr.getWidth() == 0);
        assertTrue("Expected width 20, but found: " + arr.getHeight(), arr.getHeight() == 20);
    }
    
    /**
     * Test for {@link Array2D#Array2D(int, int)}. <br>
     * Test with zero height.
     */
    @Test
    public void constructor2() {
        Array2D<Integer> arr = new Array2D<Integer>(10, 0, Integer.class);
        org.junit.Assert.assertTrue("Expected width 10, but found: " + arr.getWidth(), arr.getWidth() == 10);
        assertTrue("Expected width 0, but found: " + arr.getHeight(), arr.getHeight() == 0);
    }
    
    /**
     * Test for {@link Array2D#Array2D(int, int)}. <br>
     * Test with zero width and height.
     */
    @Test
    public void constructor3() {
        Array2D<Integer> arr = new Array2D<Integer>(0, 0, Integer.class);
        assertTrue("Expected width 0, but found: " + arr.getWidth(), arr.getWidth() == 0);
        assertTrue("Expected width 0, but found: " + arr.getHeight(), arr.getHeight() == 0);
    }
    
    /**
     * Test for {@link Array2D#Array2D(int, int)}. <br>
     * Test with negative width.
     */
    @Test
    public void constructor4() {
        expEx(IllegalArgumentException.class, () -> {
            new Array2D<Integer>(-10, 20, Integer.class);
        });
    }
    
    /**
     * Test for {@link Array2D#Array2D(int, int)}. <br>
     * Test with negative height.
     */
    @Test
    public void constructor5() {
        expEx(IllegalArgumentException.class, () -> {
            new Array2D<Integer>(10, -20, Integer.class);
        });
    }
    
    /**
     * Test for {@link Array2D#Array2D(int, int)}. <br>
     * Test with negative width and height.
     */
    @Test
    public void constructor6() {
        expEx(IllegalArgumentException.class, () -> {
            new Array2D<Integer>(-10, -20, Integer.class);
        });
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="fill">
    /**
     * Test for {@link Array2D#fill(int, int, int, int, Object)},
     * {@link Array2D#set(int, int, Object)} and {@link Array2D#get(int, int)}.
     */
    @Test
    public void fill0() {
        int w = 1000;
        int h = 1001;
        Array2D<Integer> arr = new Array2D(w, h, Integer.class);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                arr.set(i, j, i * w + j);
            }
        }
        checkFill(arr, 0, 0, w, h, 10);
    }
    
    /**
     * Test for {@link Array2D#fill(int, int, int, int, Object)}. <br>
     * Test with partial filling of Strings.
     */
    @Test
    public void fill1() {
        Array2D<String> arr = new Array2D<>(10, 11, String.class);
        checkFill(arr, 5, 5, 5, 6, "Some string");
    }
    
    /**
     * Test for {@link Array2D#fill(Object)}. <br>
     * Filling an empty array.
     */
    @Test
    public void fill2() {
        Array2D arr = new Array2D<Integer>(0, 0, Integer.class);
        arr.fill(10);
    }
    
    /**
     * Test for {@link Array2D#fill(int, int, int, int, Object)}. <br>
     * Tests with partial filling of ints.
     */
    @Test
    public void fill3() {
        Array2D arr = new Array2D<Integer>(10, 11, Integer.class);
            checkFill(arr, 5, 5, 5, 6, 2);
    }
    
    /**
     * Test for {@link Array2D#fill(int, int, int, int, Object)}. <br>
     * Tests with out of bounds areas.
     */
    @Test
    public void fill4() {
        Array2D arr = new Array2D<Integer>(10, 11, Integer.class);
        expEx(IndexOutOfBoundsException.class, () -> arr.fill(0, 0, 10, 12, 1));
        expEx(IndexOutOfBoundsException.class, () -> arr.fill(0, 0, 11, 11, 1));
        expEx(IndexOutOfBoundsException.class, () -> arr.fill(0, 0, 11, 12, 1));
        
        expEx(IllegalArgumentException.class, () -> arr.fill(0, 0, -1, 11, 1));
        expEx(IllegalArgumentException.class, () -> arr.fill(0, 0, 10, -1, 1));
        expEx(IllegalArgumentException.class, () -> arr.fill(0, 0, -1, -1, 1));
        
        expEx(IndexOutOfBoundsException.class, () -> arr.fill(-1, 0, 10, 11, 1));
        expEx(IndexOutOfBoundsException.class, () -> arr.fill(0, -1, 10, 11, 1));
        expEx(IndexOutOfBoundsException.class, () -> arr.fill(-1, -1, 10, 11, 1));
        
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 11; j++) {
                final int x = i;
                final int y = j;
                expEx(IndexOutOfBoundsException.class, () -> arr.fill(x, y, 11-x, 12-y, 1));
            }
        }
        
        expEx(IndexOutOfBoundsException.class, () -> arr.fill(10, 0, 1, 11, 1));
        expEx(IndexOutOfBoundsException.class, () -> arr.fill(0, 11, 10, 1, 1));
        expEx(IndexOutOfBoundsException.class, () -> arr.fill(10, 11, 1, 1, 1));
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="set/get">
    /**
     * Test for {@link Array2D#set(int, int, Object)} and {@link Array2D#get(int, int)}. <br>
     * Tests default replaceArray and get behaviour.
     */
    @Test
    public void setGet0() {
        int w = 200;
        int h = 200;
        Array2D<Integer> arr = new Array2D<>(w, h, Integer.class);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                assertEquals(arr.get(i, j), null);
                Integer val = i * w + j;
                arr.set(i, j, val);
                assertEquals(arr.get(i, j), val);
            }
        }
        
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                assertEquals(arr.get(i, j), (Integer) (i * w + j));
            }
        }
    }
    
    /**
     * Test for {@link Array2D#set(int, int, Object)} and {@link Array2D#get(int, int)}. <br>
     * Tests setting and getting values at indices out of bounds.
     */
    @Test
    public void setGet1() {
        Array2D<Integer> arr = new Array2D<>(10, 11, Integer.class);
        expEx(IndexOutOfBoundsException.class, () -> arr.set(-1, 1, 1));
        expEx(IndexOutOfBoundsException.class, () -> arr.set(1, -1, 1));
        expEx(IndexOutOfBoundsException.class, () -> arr.set(-1, -1, 1));
        expEx(IndexOutOfBoundsException.class, () -> arr.set(20, 1, 1));
        expEx(IndexOutOfBoundsException.class, () -> arr.set(1, 20, 1));
        expEx(IndexOutOfBoundsException.class, () -> arr.set(20, 20, 1));
        
        expEx(IndexOutOfBoundsException.class, () -> arr.get(-1, 1));
        expEx(IndexOutOfBoundsException.class, () -> arr.get(1, -1));
        expEx(IndexOutOfBoundsException.class, () -> arr.get(-1, -1));
        expEx(IndexOutOfBoundsException.class, () -> arr.get(20, 1));
        expEx(IndexOutOfBoundsException.class, () -> arr.get(1, 20));
        expEx(IndexOutOfBoundsException.class, () -> arr.get(20, 20));
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="setSize">
    /**
     * Test for {@link Array2D#setSize(int, int)}, {@link Array2D#getWidth()} and
     * {@link Array2D#getHeight()}. <br>
     * Tests normal behaviour. This includes:
     * <ul>
     *   <li> Resizing to larger array. </li>
     *   <li> Resizing to smaller array. </li>
     *   <li> Checking if the values are not copied. </li>
     * </ul>
     */
    @Test
    public void setSize0() {
        // Size [10 x 11]
        Array2D<Integer> arr = new Array2D<>(10, 11, Integer.class);
        assertEquals(arr.getWidth(), 10);
        assertEquals(arr.getHeight(), 11);
        
        // Size [20 x 21]
        arr.fill(10);
        arr.setSize(20, 21);
        assertEquals(arr.getWidth(), 20);
        assertEquals(arr.getHeight(), 21);
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 21; j++) {
                assertEquals(genIndex(i, j), arr.get(i, j), null);
            }
        }
        expEx(IndexOutOfBoundsException.class, () -> arr.get(-1, 1));
        expEx(IndexOutOfBoundsException.class, () -> arr.get(1, -1));
        expEx(IndexOutOfBoundsException.class, () -> arr.get(21, 1));
        expEx(IndexOutOfBoundsException.class, () -> arr.get(1, 22));
        
        // Size [10 x 11]
        arr.fill(11);
        arr.setSize(10, 11);
        assertEquals(arr.getWidth(), 10);
        assertEquals(arr.getHeight(), 11);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 11; j++) {
                assertEquals(genIndex(i, j), arr.get(i, j), null);
            }
        }
        expEx(IndexOutOfBoundsException.class, () -> arr.get(-1, 1));
        expEx(IndexOutOfBoundsException.class, () -> arr.get(1, -1));
        expEx(IndexOutOfBoundsException.class, () -> arr.get(11, 1));
        expEx(IndexOutOfBoundsException.class, () -> arr.get(1, 12));
    }
    
    /**
     * Test for {@link Array2D#setSize(int, int)}, {@link Array2D#getWidth()} and
     * {@link Array2D#getHeight()}. <br>
     * Tests attempts to resize to a negative array size.
     */
    @Test
    public void setSize1() {
        Array2D<Integer> arr = new Array2D<>(10, 11, Integer.class);
        expEx(NegativeArraySizeException.class, () -> arr.setSize(1, -1));
        assertSize(arr, 10, 11);
        expEx(NegativeArraySizeException.class, () -> arr.setSize(0, -1));
        assertSize(arr, 10, 11);
        expEx(NegativeArraySizeException.class, () -> arr.setSize(-1, 1));
        assertSize(arr, 10, 11);
        expEx(NegativeArraySizeException.class, () -> arr.setSize(-1, 0));
        assertSize(arr, 10, 11);
        expEx(NegativeArraySizeException.class, () -> arr.setSize(-1, -1));
        assertSize(arr, 10, 11);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="setSizeAndCopy">
    /**
     * Test for {@link Array2D#setSizeAndCopy(int, int)}, {@link Array2D#getWidth()} and
     * {@link Array2D#getHeight()}. <br>
     * Tests normal behaviour. This includes:
     * <ul>
     *   <li> Resizing to larger array. </li>
     *   <li> Resizing to smaller array. </li>
     *   <li> Checking if the values are copied successfully. </li>
     * </ul>
     */
    @Test
    public void setSizeAndCopy0() {
        // Size [10 x 11]
        Array2D<Integer> arr = new Array2D<>(10, 11, Integer.class);
        assertSize(arr, 10, 11);
        
        // Size [20 x 21]
        arr.fill(10);
        arr.setSizeAndCopy(20, 21);
        assertSize(arr, 20, 21);
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 21; j++) {
                if (i < 10 && j < 11) {
                    assertEquals(genIndex(i, j), (Object) arr.get(i, j), 10);
                } else {
                    assertEquals(genIndex(i, j), arr.get(i, j), null);
                }
            }
        }
        expEx(IndexOutOfBoundsException.class, () -> arr.get(-1, 1));
        expEx(IndexOutOfBoundsException.class, () -> arr.get(1, -1));
        expEx(IndexOutOfBoundsException.class, () -> arr.get(21, 1));
        expEx(IndexOutOfBoundsException.class, () -> arr.get(1, 22));
        
        // Size [10 x 11]
        arr.fill(11);
        arr.setSizeAndCopy(10, 11);
        assertSize(arr, 10, 11);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 11; j++) {
                assertEquals(genIndex(i, j), (Object) arr.get(i, j), 11);
            }
        }
        expEx(IndexOutOfBoundsException.class, () -> arr.get(-1, 1));
        expEx(IndexOutOfBoundsException.class, () -> arr.get(1, -1));
        expEx(IndexOutOfBoundsException.class, () -> arr.get(11, 1));
        expEx(IndexOutOfBoundsException.class, () -> arr.get(1, 12));
    }
    
    /**
     * Test for {@link Array2D#setSizeAndCopy(int, int)}, {@link Array2D#getWidth()} and
     * {@link Array2D#getHeight()}. <br>
     * Tests attempts to resize to a negative array size.
     */
    @Test
    public void setSizeAndCopy1() {
        Array2D<Integer> arr = new Array2D<>(10, 11, Integer.class);
        expEx(NegativeArraySizeException.class, () -> arr.setSizeAndCopy(1, -1));
        assertSize(arr, 10, 11);
        expEx(NegativeArraySizeException.class, () -> arr.setSizeAndCopy(0, -1));
        assertSize(arr, 10, 11);
        expEx(NegativeArraySizeException.class, () -> arr.setSizeAndCopy(-1, 1));
        assertSize(arr, 10, 11);
        expEx(NegativeArraySizeException.class, () -> arr.setSizeAndCopy(-1, 0));
        assertSize(arr, 10, 11);
        expEx(NegativeArraySizeException.class, () -> arr.setSizeAndCopy(-1, -1));
        assertSize(arr, 10, 11);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="setRow">
    /**
     * Test for {@link Array2D#setRow(Object[], int, int, int, int)} and
     * {@link Array2D#setRow(Object[], int, int, int, int)}. <br>
     * Tests normal behaviour.
     */
    @Test
    public void setRow0() {
        Array2D<String> arr;
        int w = 100;
        int h = 101;
        String[] in = genStrArr(w);
        for (int k = 0; k < h; k++) {
            arr = new Array2D<>(w, h, String.class);
            arr.setRow(in, k);
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 11; j++) {
                    if (j == k) {
                        assertEquals(genIndex(i, j, k), in[i], arr.get(i, j));
                    } else {
                        assertEquals(genIndex(i, j, k), null, arr.get(i, j));
                    }
                }
            }
        }
    }
    
    /**
     * Test for {@link Array2D#setRow(Object, int, int, int, int)} and
     * {@link Array2D#setRow(Object, int, int, int, int)}. <br>
     * Tests normal behaviour.
     */
    @Test
    public void setRow1() {
        Array2D<String> arr;
        int w = 100;
        int h = 101;
        String[] in = genStrArr(w);
        for (int k = 0; k < h; k++) {
            arr = new Array2D<>(w, h, String.class);
            arr.setRow((Object) in, k);
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 11; j++) {
                    if (j == k) {
                        assertEquals(genIndex(i, j, k), in[i], arr.get(i, j));
                    } else {
                        assertEquals(genIndex(i, j, k), null, arr.get(i, j));
                    }
                }
            }
        }
    }
    
    /**
     * Test for {@link Array2D#setRow(Object, int, int, int, int)} and
     * {@link Array2D#setRow(Object, int, int, int, int)}. <br>
     * Setting faulty typed values.
     */
    @Test
    public void setRow2() {
        int w = 100;
        int h = 101;
        final int[] in = genIntArr(h);
        for (int i = 0; i < w; i++) {
            final Array2D<String> arr = new Array2D<>(w, h, String.class);
            final int col = i;
            expEx(IllegalArgumentException.class, () -> arr.setRow((Object) in, col));
        }
    }
    
    /**
     * Test for {@link Array2D#setRow(Object, int, int, int, int)} and
     * {@link Array2D#setRow(Object, int, int, int, int)}. <br>
     * Setting with primitive type array.
     */
    @Test
    public void setRow3() {
        Array2D<Integer> arr;
        int w = 100;
        int h = 101;
        int[] in = genIntArr(w);
        for (int k = 0; k < h; k++) {
            arr = new Array2D<>(w, h, Integer.class);
            arr.setRow((Object) in, k);
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 11; j++) {
                    if (j == k) {
                        assertEquals(genIndex(i, j, k), in[i], (Object) arr.get(i, j));
                    } else {
                        assertEquals(genIndex(i, j, k), null, arr.get(i, j));
                    }
                }
            }
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="setColumn">
    /**
     * Test for {@link Array2D#setColumn(Object[], int)} and
     * {@link Array2D#setColumn(Object[], int, int, int, int)}. <br>
     * Tests normal behaviour.
     */
    @Test
    public void setColumn0() {
        Array2D<String> arr;
        int w = 100;
        int h = 101;
        String[] in = genStrArr(h);
        for (int k = 0; k < w; k++) {
            arr = new Array2D<>(w, h, String.class);
            arr.setColumn(in, k);
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 11; j++) {
                    if (i == k) {
                        assertEquals(genIndex(i, j, k), in[j], arr.get(i, j));
                    } else {
                        assertEquals(genIndex(i, j, k), null, arr.get(i, j));
                    }
                }
            }
        }
    }
    
    /**
     * Test for {@link Array2D#setColumn(Object, int)} and
     * {@link Array2D#setColumn(Object, int, int, int, int)}. <br>
     * Tests normal behaviour.
     */
    @Test
    public void setColumn1() {
        Array2D<String> arr;
        int w = 100;
        int h = 101;
        String[] in = genStrArr(h);
        for (int k = 0; k < w; k++) {
            arr = new Array2D<>(w, h, String.class);
            arr.setColumn((Object) in, k);
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 11; j++) {
                    if (i == k) {
                        assertEquals(genIndex(i, j, k), in[j], arr.get(i, j));
                    } else {
                        assertEquals(genIndex(i, j, k), null, arr.get(i, j));
                    }
                }
            }
        }
    }
    
    /**
     * Test for {@link Array2D#setColumn(Object, int)} and
     * {@link Array2D#setColumn(Object, int, int, int, int)}. <br>
     * Setting faulty typed values.
     */
    @Test
    public void setColumn2() {
        int w = 100;
        int h = 101;
        final int[] in = genIntArr(h);
        for (int k = 0; k < w; k++) {
            final Array2D<String> arr = new Array2D<>(w, h, String.class);
            final int index = k;
            expEx(IllegalArgumentException.class, () -> arr.setColumn((Object) in, index));
        }
    }
    
    /**
     * Test for {@link Array2D#setColumn(Object, int)} and
     * {@link Array2D#setColumn(Object, int, int, int, int)}. <br>
     * Setting with primitive type array.
     */
    @Test
    public void setColumn3() {
        Array2D<Integer> arr;
        int w = 100;
        int h = 101;
        int[] in = genIntArr(h);
        for (int k = 0; k < w; k++) {
            arr = new Array2D<>(w, h, Integer.class);
            arr.setColumn((Object) in, k);
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 11; j++) {
                    if (i == k) {
                        assertEquals(genIndex(i, j, k), in[j], (Object) arr.get(i, j));
                    } else {
                        assertEquals(genIndex(i, j, k), null, arr.get(i, j));
                    }
                }
            }
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="setArray">
    /**
     * Test for {@link Array2D#setArray(Array2D)}. <br>
     * Tests default behaviour.
     */
    @Test
    public void setArray0() {
        Array2D<Integer> dst = new Array2D<>(5, 6, Integer.class, 0);
        Array2D<Integer> src = new Array2D<>(10, 11, Integer.class, 1);
        dst.setArray(src);
        assertValue(dst, 1);
        
        dst = new Array2D<>(5, 6, Integer.class, 0);
        src = new Array2D<>(2, 3, Integer.class, 1);
        dst.setArray(src);
        assertSize(dst, 5, 6);
        assertValue(dst, 0, 0, 2, 3, 1, 0);
    }
    
    /**
     * Test for {@link Array2D#setArray(Object[])}. <br>
     * Tests default behaviour.
     */
    @Test
    public void setArray1() {
        Array2D<Integer> dst = new Array2D<>(5, 6, Integer.class, 0);
        Integer[][] src = fill(new Integer[10][11], 1);
        dst.setArray(src);
        assertValue(dst, 1);
        
        dst = new Array2D<>(5, 6, Integer.class, 0);
        src = fill(new Integer[2][3], 1);
        dst.setArray(src);
        assertSize(dst, 5, 6);
        assertValue(dst, 0, 0, 2, 3, 1, 0);
    }
    
    /**
     * Test for {@link Array2D#setArray(Object[])}. <br>
     * Tests default behaviour.
     */
    @Test
    public void setArray2() {
        Array2D<Integer> dst = new Array2D<>(5, 6, Integer.class, 0);
        int[][] src = fill(new int[10][11], 1);
        dst.setArray(src);
        assertValue(dst, 1);
        
        dst = new Array2D<>(5, 6, Integer.class, 0);
        src = fill(new int[2][3], 1);
        dst.setArray(src);
        assertSize(dst, 5, 6);
        assertValue(dst, 0, 0, 2, 3, 1, 0);
    }
    
    /**
     * Test for {@link Array2D#setArray(Array2D, int, int, int, int, int, int)}. <br>
     * Tests default behaviour.
     */
    @Test
    public void setArray3() {
        Array2D<Integer> dst = new Array2D<>(5, 6, Integer.class, 0);
        Array2D<Integer> src = new Array2D<>(10, 11, Integer.class, 1);
        dst.setArray(src, 0, 0, 0, 0, 5, 6);
        assertSize(dst, 5, 6);
        assertValue(dst, 1);
        
        dst = new Array2D<>(5, 6, Integer.class, 0);
        src = new Array2D<>(2, 3, Integer.class, 1);
        dst.setArray(src, 0, 0, 0, 0, 2, 3);
        assertSize(dst, 5, 6);
        assertValue(dst, 0, 0, 2, 3, 1, 0);
        
        dst = new Array2D<>(10, 11, Integer.class, 0);
        src = new Array2D<>(12, 13, Integer.class, 1);
        incrFill(src, 0);
        dst.setArray(src, 4, 5, 1, 2, 4, 5);
        assertSize(dst, 10, 11);
        for (int i = 0; i < dst.getWidth(); i++) {
            for (int j = 0; j < dst.getHeight(); j++) {
                if (1 <= i && i < 5 && 2 <= j && j < 7) {
                    Integer val = (i + 3) + (j + 3)*src.getWidth();
                    assertEquals(genIndex(i, j), val, dst.get(i, j));
                } else {
                    assertEquals(genIndex(i, j), 0, (Object) dst.get(i, j));
                }
            }
        }
    }
    
    /**
     * Test for {@link Array2D#setArray(Object[][], int, int, int, int, int, int)}. <br>
     * Tests default behaviour.
     */
    @Test
    public void setArray4() {
        Array2D<Integer> dst = new Array2D<>(5, 6, Integer.class, 0);
        Integer[][] src = new Integer[10][11];
        fill(src, 1);
        dst.setArray(src, 0, 0, 0, 0, 5, 6);
        assertSize(dst, 5, 6);
        assertValue(dst, 1);
        
        dst = new Array2D<>(5, 6, Integer.class, 0);
        src = new Integer[2][3];
        fill(src, 1);
        dst.setArray(src, 0, 0, 0, 0, 2, 3);
        assertSize(dst, 5, 6);
        assertValue(dst, 0, 0, 2, 3, 1, 0);
        
        dst = new Array2D<>(10, 11, Integer.class, 0);
        src = new Integer[12][13];
        incrFill(src, 0);
        assertSize(dst, 10, 11);
        dst.setArray(src, 4, 5, 1, 2, 4, 5);
        for (int i = 0; i < dst.getWidth(); i++) {
            for (int j = 0; j < dst.getHeight(); j++) {
                if (1 <= i && i < 5 && 2 <= j && j < 7) {
                    Integer val = (i + 3) + (j + 3)*src.length;
                    assertEquals(genIndex(i, j), val, dst.get(i, j));
                } else {
                    assertEquals(genIndex(i, j), 0, (Object) dst.get(i, j));
                }
            }
        }
    }
    
    /**
     * Test for {@link Array2D#setArray(Object[], int, int, int, int, int, int)}. <br>
     * Tests default behaviour.
     */
    @Test
    public void setArray5() {
        Array2D<Integer> dst = new Array2D<>(5, 6, Integer.class, 0);
        int[][] src = new int[10][11];
        fill(src, 1);
        dst.setArray(src, 0, 0, 0, 0, 5, 6);
        assertSize(dst, 5, 6);
        assertValue(dst, 1);
        
        dst = new Array2D<>(5, 6, Integer.class, 0);
        src = new int[2][3];
        fill(src, 1);
        dst.setArray(src, 0, 0, 0, 0, 2, 3);
        assertSize(dst, 5, 6);
        assertValue(dst, 0, 0, 2, 3, 1, 0);
        
        dst = new Array2D<>(10, 11, Integer.class, 0);
        src = new int[12][13];
        incrFill(src, 0);
        dst.setArray(src, 4, 5, 1, 2, 4, 5);
        assertSize(dst, 10, 11);
        for (int i = 0; i < dst.getWidth(); i++) {
            for (int j = 0; j < dst.getHeight(); j++) {
                if (1 <= i && i < 5 && 2 <= j && j < 7) {
                    Integer val = (i + 3) + (j + 3)*src.length;
                    assertEquals(genIndex(i, j), val, dst.get(i, j));
                } else {
                    assertEquals(genIndex(i, j), 0, (Object) dst.get(i, j));
                }
            }
        }
    }
    
    /**
     * Test for {@link Array2D#setArray(Array2D, int, int, int, int, int, int)},
     * {@link Array2D#setArray(Object[][], int, int, int, int, int, int)} and
     * {@link Array2D#setArray(Object[], int, int, int, int, int, int)}. <br>
     * Tests setting invallid bounds.
     */
    @Test
    public void setArray6() {
        final Array2D<Integer> dst = new Array2D<>(5, 6, Integer.class, 0);
        final Array2D<Integer> src1 = new Array2D<>(10, 11, Integer.class, 1);
        final Integer[][] src2 = fill(new Integer[10][11], 1);
        final int[][] src3 = fill(new int[10][11], 1);
        
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src1, -1, 0, 1, 1, 1, 1));
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src2, -1, 0, 1, 1, 1, 1));
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src3, -1, 0, 1, 1, 1, 1));
        assertSize(dst, 5, 6);
        assertValue(dst, 0);
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src1, 0, -1, 1, 1, 1, 1));
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src2, 0, -1, 1, 1, 1, 1));
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src3, 0, -1, 1, 1, 1, 1));
        assertSize(dst, 5, 6);
        assertValue(dst, 0);
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src1, -1, -1, 1, 1, 1, 1));
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src2, -1, -1, 1, 1, 1, 1));
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src3, -1, -1, 1, 1, 1, 1));
        assertSize(dst, 5, 6);
        assertValue(dst, 0);
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src1, 1, 1, -1, 0, 1, 1));
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src2, 1, 1, -1, 0, 1, 1));
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src3, 1, 1, -1, 0, 1, 1));
        assertSize(dst, 5, 6);
        assertValue(dst, 0);
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src1, 1, 1, 0, -1, 1, 1));
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src2, 1, 1, 0, -1, 1, 1));
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src3, 1, 1, 0, -1, 1, 1));
        assertSize(dst, 5, 6);
        assertValue(dst, 0);
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src1, 1, 1, -1, -1, 1, 1));
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src2, 1, 1, -1, -1, 1, 1));
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src3, 1, 1, -1, -1, 1, 1));
        assertSize(dst, 5, 6);
        assertValue(dst, 0);
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src1, -1, -1, -1, -1, 1, 1));
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src2, -1, -1, -1, -1, 1, 1));
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src3, -1, -1, -1, -1, 1, 1));
        assertSize(dst, 5, 6);
        assertValue(dst, 0);
        
        expEx(IllegalArgumentException.class, () -> dst.getArray(src1, 0, 0, 0, 0, -1, 1));
        expEx(IllegalArgumentException.class, () -> dst.getArray(src2, 0, 0, 0, 0, -1, 1));
        expEx(IllegalArgumentException.class, () -> dst.getArray(src3, 0, 0, 0, 0, -1, 1));
        assertSize(dst, 5, 6);
        assertValue(dst, 0);
        expEx(IllegalArgumentException.class, () -> dst.getArray(src1, 0, 0, 0, 0, 1, -1));
        expEx(IllegalArgumentException.class, () -> dst.getArray(src2, 0, 0, 0, 0, 1, -1));
        expEx(IllegalArgumentException.class, () -> dst.getArray(src3, 0, 0, 0, 0, 1, -1));
        assertSize(dst, 5, 6);
        assertValue(dst, 0);
        expEx(IllegalArgumentException.class, () -> dst.getArray(src1, 0, 0, 0, 0, -1, -1));
        expEx(IllegalArgumentException.class, () -> dst.getArray(src2, 0, 0, 0, 0, -1, -1));
        expEx(IllegalArgumentException.class, () -> dst.getArray(src3, 0, 0, 0, 0, -1, -1));
        assertSize(dst, 5, 6);
        assertValue(dst, 0);
        
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src1, 10, 11, 0, 0, 1, 0));
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src2, 10, 11, 0, 0, 1, 0));
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src3, 10, 11, 0, 0, 1, 0));
        assertSize(dst, 5, 6);
        assertValue(dst, 0);
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src1, 10, 11, 0, 0, 0, 1));
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src2, 10, 11, 0, 0, 0, 1));
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src3, 10, 11, 0, 0, 0, 1));
        assertSize(dst, 5, 6);
        assertValue(dst, 0);
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src1, 10, 11, 0, 0, 1, 1));
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src2, 10, 11, 0, 0, 1, 1));
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src3, 10, 11, 0, 0, 1, 1));
        assertSize(dst, 5, 6);
        assertValue(dst, 0);
        
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src1, 0, 0, 5, 6, 1, 0));
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src2, 0, 0, 5, 6, 1, 0));
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src3, 0, 0, 5, 6, 1, 0));
        assertSize(dst, 5, 6);
        assertValue(dst, 0);
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src1, 0, 0, 5, 6, 0, 1));
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src2, 0, 0, 5, 6, 0, 1));
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src3, 0, 0, 5, 6, 0, 1));
        assertSize(dst, 5, 6);
        assertValue(dst, 0);
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src1, 0, 0, 5, 6, 1, 1));
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src2, 0, 0, 5, 6, 1, 1));
        expEx(IndexOutOfBoundsException.class, () -> dst.getArray(src3, 0, 0, 5, 6, 1, 1));
        assertSize(dst, 5, 6);
        assertValue(dst, 0);
    }
    
    /**
     * Test for {@link Array2D#setArray(Object[], int, int, int, int, int, int)} and
     * {@link Array2D#setArray(Object[]}. <br>
     * Tests setting non-primitive objects.
     */
    @Test
    public void setArray7() {
        Array2D<Integer> dst = new Array2D<>(10, 11, Integer.class, 0);
        expEx(IllegalArgumentException.class, () -> dst.setArray(new double[1][1]));
        assertValue(dst, 0);
        expEx(IllegalArgumentException.class, () -> dst.setArray(new double[1][1], 0, 0, 0, 0, 1, 1));
        assertValue(dst, 0);
        Object[] src = fill(new Integer[10][11], 1);
        dst.setArray(src);
        assertValue(dst, 1);
        dst.fill(0);
        dst.setArray(src, 0, 0, 0, 0, 10, 11);
        assertValue(dst, 1);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="replaceArray">
    /**
     * Test for {@link Array2D#setArray(Array2D)}. <br>
     * Tests default behaviour.
     */
    @Test
    public void replaceArray0() {
        Array2D<Integer> dst = new Array2D<>(5, 6, Integer.class, 0);
        Array2D<Integer> src = new Array2D<>(10, 11, Integer.class, 1);
        dst.replaceArray(src);
        assertSize(dst, 10, 11);
        assertValue(dst, 1);
        assertTrue(dst.equals(src));
        assertTrue(src.equals(dst));
        
        dst = new Array2D<>(5, 6, Integer.class, 0);
        src = new Array2D<>(2, 3, Integer.class, 1);
        dst.replaceArray(src);
        assertSize(dst, 2, 3);
        assertValue(dst, 1);
        assertTrue(dst.equals(src));
        assertTrue(src.equals(dst));
    }
    
    /**
     * Test for {@link Array2D#setArray(Object[][])}. <br>
     * Tests default behaviour.
     */
    @Test
    @SuppressWarnings("ArrayEquals")
    public void replaceArray1() {
        Array2D<Integer> dst = new Array2D<>(5, 6, Integer.class, 0);
        Integer[][] src = fill(new Integer[10][11], 1);
        dst.replaceArray(src);
        assertSize(dst, 10, 11);
        assertValue(dst, 1);
        assertTrue(dst.equals(src));
        
        dst = new Array2D<>(5, 6, Integer.class, 0);
        src = fill(new Integer[2][3], 1);
        dst.replaceArray(src);
        assertSize(dst, 2, 3);
        assertValue(dst, 1);
        assertTrue(dst.equals(src));
    }
    
    /**
     * Test for {@link Array2D#setArray(Object[][])}. <br>
     * Tests default behaviour.
     */
    @Test
    @SuppressWarnings("ArrayEquals")
    public void replaceArray2() {
        Array2D<Integer> dst = new Array2D<>(5, 6, Integer.class, 0);
        int[][] src = fill(new int[10][11], 1);
        dst.replaceArray(src);
        assertSize(dst, 10, 11);
        assertValue(dst, 1);
        assertTrue(dst.equals(src));
        
        dst = new Array2D<>(5, 6, Integer.class, 0);
        src = fill(new int[2][3], 1);
        dst.replaceArray(src);
        assertSize(dst, 2, 3);
        assertValue(dst, 1);
        assertTrue(dst.equals(src));
    }
    
    /**
     * Test for {@link Array2D#setArray(Array2D, int, int, int, int)}. <br>
     * Tests default behaviour.
     */
    @Test
    public void replaceArray3() {
        Array2D<Integer> dst = new Array2D<>(5, 6, Integer.class, 0);
        Array2D<Integer> src = new Array2D<>(10, 11, Integer.class, 1);
        dst.replaceArray(src, 0, 0, 5, 6);
        assertSize(dst, 5, 6);
        assertValue(dst, 1);
        
        dst = new Array2D<>(5, 6, Integer.class, 0);
        src = new Array2D<>(2, 3, Integer.class, 1);
        dst.replaceArray(src, 0, 0, 2, 3);
        assertSize(dst, 2, 3);
        assertValue(dst, 1);
        
        dst = new Array2D<>(10, 11, Integer.class, 0);
        src = new Array2D<>(12, 13, Integer.class, 1);
        incrFill(src, 0);
        dst.replaceArray(src, 3, 4, 5, 6);
        assertSize(dst, 5, 6);
        for (int i = 0; i < dst.getWidth(); i++) {
            for (int j = 0; j < dst.getHeight(); j++) {
                if (i < 5 && j < 6) {
                    Integer val = (i + 3) + (j + 4)*src.getWidth();
                    assertEquals(genIndex(i, j), val, dst.get(i, j));
                } else {
                    assertEquals(genIndex(i, j), 0, (Object) dst.get(i, j));
                }
            }
        }
    }
    
    /**
     * Test for {@link Array2D#setArray(Object[][], int, int, int, int)}. <br>
     * Tests default behaviour.
     */
    @Test
    public void replaceArray4() {
        Array2D<Integer> dst = new Array2D<>(5, 6, Integer.class, 0);
        Integer[][] src = fill(new Integer[10][11], 1);
        dst.replaceArray(src, 0, 0, 5, 6);
        assertSize(dst, 5, 6);
        assertValue(dst, 1);
        
        dst = new Array2D<>(5, 6, Integer.class, 0);
        src = fill(new Integer[2][3], 1);
        dst.replaceArray(src, 0, 0, 2, 3);
        assertSize(dst, 2, 3);
        assertValue(dst, 1);
        
        dst = new Array2D<>(10, 11, Integer.class, 0);
        src = fill(new Integer[12][13], 1);
        incrFill(src, 0);
        dst.replaceArray(src, 3, 4, 5, 6);
        assertSize(dst, 5, 6);
        for (int i = 0; i < dst.getWidth(); i++) {
            for (int j = 0; j < dst.getHeight(); j++) {
                if (i < 5 && j < 6) {
                    Integer val = (i + 3) + (j + 4)*src.length;
                    assertEquals(genIndex(i, j), val, dst.get(i, j));
                } else {
                    assertEquals(genIndex(i, j), 0, (Object) dst.get(i, j));
                }
            }
        }
    }
    
    /**
     * Test for {@link Array2D#setArray(Object[], int, int, int, int)}. <br>
     * Tests default behaviour.
     */
    @Test
    public void replaceArray5() {
        Array2D<Integer> dst = new Array2D<>(5, 6, Integer.class, 0);
        int[][] src = fill(new int[10][11], 1);
        dst.replaceArray(src, 0, 0, 5, 6);
        assertSize(dst, 5, 6);
        assertValue(dst, 1);
        
        dst = new Array2D<>(5, 6, Integer.class, 0);
        src = fill(new int[2][3], 1);
        dst.replaceArray(src, 0, 0, 2, 3);
        assertSize(dst, 2, 3);
        assertValue(dst, 1);
        
        dst = new Array2D<>(10, 11, Integer.class, 0);
        src = fill(new int[12][13], 1);
        incrFill(src, 0);
        dst.replaceArray(src, 3, 4, 5, 6);
        assertSize(dst, 5, 6);
        for (int i = 0; i < dst.getWidth(); i++) {
            for (int j = 0; j < dst.getHeight(); j++) {
                if (i < 5 && j < 6) {
                    Integer val = (i + 3) + (j + 4)*src.length;
                    assertEquals(genIndex(i, j), val, dst.get(i, j));
                } else {
                    assertEquals(genIndex(i, j), 0, (Object) dst.get(i, j));
                }
            }
        }
    }
    
    /**
     * Test for {@link Array2D#replaceArray(Array2D, int, int, int, int)}. <br>
     * Tests setting invallid bounds.
     */
    @Test
    public void replaceArray6() {
        final Array2D<Integer> dst1 = new Array2D<>(5, 6, Integer.class, 0);
        final Array2D<Integer> src = new Array2D<>(10, 11, Integer.class, 1);
        expEx(IndexOutOfBoundsException.class, () -> dst1.replaceArray(src, -1, 0, 1, 1));
        assertSize(dst1, 5, 6);
        assertValue(dst1, 0);
        expEx(IndexOutOfBoundsException.class, () -> dst1.replaceArray(src, 0, -1, 1, 1));
        assertSize(dst1, 5, 6);
        assertValue(dst1, 0);
        expEx(IndexOutOfBoundsException.class, () -> dst1.replaceArray(src, -1, -1, 1, 1));
        assertSize(dst1, 5, 6);
        assertValue(dst1, 0);
        
        expEx(IllegalArgumentException.class, () -> dst1.replaceArray(src, 0, 0, -1, 1));
        assertSize(dst1, 5, 6);
        assertValue(dst1, 0);
        expEx(IllegalArgumentException.class, () -> dst1.replaceArray(src, 0, 0, 1, -1));
        assertSize(dst1, 5, 6);
        assertValue(dst1, 0);
        expEx(IllegalArgumentException.class, () -> dst1.replaceArray(src, 0, 0, -1, -1));
        assertSize(dst1, 5, 6);
        assertValue(dst1, 0);
        
        expEx(IndexOutOfBoundsException.class, () -> dst1.replaceArray(src, 10, 11, 1, 0));
        assertSize(dst1, 5, 6);
        assertValue(dst1, 0);
        expEx(IndexOutOfBoundsException.class, () -> dst1.replaceArray(src, 10, 11, 0, 1));
        assertSize(dst1, 5, 6);
        assertValue(dst1, 0);
        expEx(IndexOutOfBoundsException.class, () -> dst1.replaceArray(src, 10, 11, 1, 1));
        assertSize(dst1, 5, 6);
        assertValue(dst1, 0);
    }
    
    /**
     * Test for {@link Array2D#setArray(Object[], int, int, int, int, int, int)} and
     * {@link Array2D#setArray(Object[]}. <br>
     * Tests setting non-primitive and faulty objects.
     */
    @Test
    public void replaceArray7() {
        Array2D<Integer> dst = new Array2D<>(10, 11, Integer.class, 0);
        expEx(IllegalArgumentException.class, () -> dst.replaceArray(new double[1][1]));
        assertValue(dst, 0);
        expEx(IllegalArgumentException.class, () -> dst.replaceArray(new double[1][1], 0, 0, 1, 1));
        assertValue(dst, 0);
        Object[] src = fill(new Integer[5][6], 1);
        dst.replaceArray(src);
        assertValue(dst, 1);
        assertSize(dst, 5, 6);
        dst.fill(0);
        dst.replaceArray(src, 0, 0, 4, 5);
        assertValue(dst, 1);
        assertSize(dst, 4, 5);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Object overrides">
    /**
     * Test for {@link Array2D#clone()}, {@link Array2D#equals(Object)},
     * {@link Array2D#hashCode()} and {@link Array2D#toString()}. <br>
     * Tests the following points:
     * <ul>
     *   <li> Whether the clone has the same values as the original object </li>
     *   <li> Whether unsetted values are copied correctly. </li>
     *   <li> Whether changing values in the clone does not affect the original. </li>
     *   <li> Whether the hash code of the original and the clone is equal. </li>
     *   <li> Whether the toString function generates the same result for the cloned array. </li>
     * </ul>
     */
    @Test
    public void cloneEqualsHashCodeToString0() {
        int w = 100;
        int h = 101;
        Array2D<Integer> arr = new Array2D<>(w, h, Integer.class);
        Array2D<Integer> clone = arr.clone();
        assertTrue(arr.equals(clone));
        assertTrue(clone.equals(arr));
        assertEquals(arr.hashCode(), clone.hashCode());
        assertEquals(arr.toString(), clone.toString());
        
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                assertEquals(genIndex(i, j), null, clone.get(i, j));
                arr.set(i, j, i*h + j);
            }
        }
        
        // Create new clone.
        clone = arr.clone();
        assertTrue(arr.equals(clone));
        assertTrue(clone.equals(arr));
        assertEquals(arr.hashCode(), clone.hashCode());
        assertEquals(arr.toString(), clone.toString());
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                assertEquals(genIndex(i, j), i*h + j, (Object) clone.get(i, j));
                clone.set(i, j, w*h + i*h + j);
                assertEquals(genIndex(i, j), i*h + j, (Object) arr.get(i, j));
            }
        }
    }
    
    /**
     * Test for {@link Array2D#equals(Object)}. <br>
     * Tests edge cases for arrays with no elements.
     */
    @Test
    public void equals0() {
        Array2D<Integer> arr1 = new Array2D<Integer>(0, 0, Integer.class);
        Array2D<Integer> arr2 = new Array2D<Integer>(1, 0, Integer.class);
        Array2D<Integer> arr3 = new Array2D<Integer>(0, 1, Integer.class);
        Array2D<Integer> arr4 = new Array2D<Integer>(1, 1, Integer.class);
        
        assertTrue(arr1.equals(arr1));
        assertFalse(arr1.equals(arr2));
        assertFalse(arr1.equals(arr3));
        assertFalse(arr1.equals(arr4));
        
        assertFalse(arr2.equals(arr1));
        assertTrue(arr2.equals(arr2));
        assertFalse(arr2.equals(arr3));
        assertFalse(arr2.equals(arr4));
        
        assertFalse(arr3.equals(arr1));
        assertFalse(arr3.equals(arr2));
        assertTrue(arr3.equals(arr3));
        assertFalse(arr3.equals(arr4));
        
        assertFalse(arr4.equals(arr1));
        assertFalse(arr4.equals(arr2));
        assertFalse(arr4.equals(arr2));
        assertTrue(arr4.equals(arr4));
    }
    
    /**
     * Test for {@link Array2D#equals(Object)}. <br>
     * Tests equally sized arrays.
     */
    @Test
    @SuppressWarnings("ArrayEquals")
    public void equals1() {
        int w = 10;
        int h = 11;
        // Initialize the variables.
        Array2D<Integer> arr1 = new Array2D<Integer>(w, h, Integer.class);
        Array2D<Integer> arr2 = new Array2D<Integer>(w, h, Integer.class);
        Array2D<Integer> arr3 = new Array2D<Integer>(w, h, Integer.class);
        Array2D<Integer> arr4 = new Array2D<Integer>(w, h, Integer.class, 0);
        int[][] data = new int[w][h];
        assertEquals(arr1, arr2);
        assertTrue(arr1.equals(new Integer[w][h]));
        assertTrue(arr4.equals(data));
        
        // Fill arr1/2 and data.
        for (int i = 0; i < w; i++) {
            arr1.setColumn(genIntArr(h), i);
            arr2.setColumn(genIntArr(h), i);
            data[i] = genIntArr(h);
        }
        assertTrue(arr1.equals(arr2));
        assertTrue(arr2.equals(arr1));
        assertTrue(arr1.equals(data));
        
        assertFalse(arr1.equals(arr3));
        assertFalse(arr3.equals(arr1));
        
        // Create sub array.
        Array2D<Integer> arr5 = arr1.getSubArray(0, 0, w, h);
        assertTrue(arr5.equals(arr1));
        assertTrue(arr5.equals(arr2));
        assertTrue(arr1.equals(arr5));
        assertTrue(arr2.equals(arr5));
        
        assertFalse(arr5.equals(arr3));
        assertFalse(arr3.equals(arr5));
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="getSubArray">
    /**
     * Test for {@link Array2D#getSubArray(int, int, int, int)}. <br>
     * Tests the default behaviour.
     */
    @Test
    public void getSubArray0() {
        int width = 15;
        int height = 16;
        Array2D<Integer> arr = new Array2D<>(width, height, Integer.class);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                arr.set(i, j, i*height + j);
            }
        }
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                for (int w = 0; w + i < width; w++) {
                    for (int h = 0; h + j < height; h++) {
                        checkSub(arr, i, j, w, h);
                    }
                }
            }
        }
    }
    
    /**
     * Test for {@link Array2D#getSubArray(int, int, int, int)}. <br>
     * Tests getting sub-arrays which are (partially) out of bounds or have
     * negative sizes.
     */
    @Test
    public void getSubArray1() {
        Array2D<Integer> arr = new Array2D<>(10, 20, Integer.class);
        expEx(IndexOutOfBoundsException.class, () -> arr.getSubArray(-1, 1, 2, 2));
        expEx(IndexOutOfBoundsException.class, () -> arr.getSubArray(1, -1, 2, 2));
        expEx(IllegalArgumentException.class, () -> arr.getSubArray(1, 1, -2, 2));
        expEx(IllegalArgumentException.class, () -> arr.getSubArray(1, 1, 2, -2));
        
        expEx(IndexOutOfBoundsException.class, () -> arr.getSubArray(-1, -1, 2, 2));
        expEx(IllegalArgumentException.class, () -> arr.getSubArray(1, 1, -2, -2));
        expEx(IndexOutOfBoundsException.class, () -> arr.getSubArray(-3, -3, 2, 2));
        expEx(IllegalArgumentException.class, () -> arr.getSubArray(-1, -1, -2, -2));
        
        expEx(IndexOutOfBoundsException.class, () -> arr.getSubArray(9, 1, 2, 2));
        expEx(IndexOutOfBoundsException.class, () -> arr.getSubArray(10, 1, 2, 2));
        expEx(IndexOutOfBoundsException.class, () -> arr.getSubArray(11, 1, 2, 2));
        expEx(IllegalArgumentException.class, () -> arr.getSubArray(9, 1, -2, 2));
        expEx(IllegalArgumentException.class, () -> arr.getSubArray(10, 1, -2, 2));
        expEx(IllegalArgumentException.class, () -> arr.getSubArray(11, 1, -2, 2));
        
        expEx(IndexOutOfBoundsException.class, () -> arr.getSubArray(1, 19, 2, 2));
        expEx(IndexOutOfBoundsException.class, () -> arr.getSubArray(1, 20, 2, 2));
        expEx(IndexOutOfBoundsException.class, () -> arr.getSubArray(1, 21, 2, 2));
        expEx(IllegalArgumentException.class, () -> arr.getSubArray(1, 19, 2, -2));
        expEx(IllegalArgumentException.class, () -> arr.getSubArray(1, 20, 2, -2));
        expEx(IllegalArgumentException.class, () -> arr.getSubArray(1, 21, 2, -2));
        
        expEx(IndexOutOfBoundsException.class, () -> arr.getSubArray(9, 19, 2, 2));
        expEx(IndexOutOfBoundsException.class, () -> arr.getSubArray(10, 20, 2, 2));
        expEx(IndexOutOfBoundsException.class, () -> arr.getSubArray(11, 21, 2, 2));
        expEx(IllegalArgumentException.class, () -> arr.getSubArray(9, 19, -2, -2));
        expEx(IllegalArgumentException.class, () -> arr.getSubArray(10, 20, -2, -2));
        expEx(IllegalArgumentException.class, () -> arr.getSubArray(11, 21, -2, -2));
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="getRow">
    /**
     * Test for {@link Array2D#getPrimRow(int)}, {@link Array2D#getRow(int)},
     * {@link Array2D#getRow(Object, int)}, {@link Array2D#getRow(Object[] int)},
     * {@link Array2D#getRow(Object, int, int, int)} and
     * {@link Array2D#getRow(Object[], int, int, int)}. <br>
     * Tests default behaviour.
     */
    @Test
    public void getRow0() {
        int w = 11;
        Array2D<Integer> arr = new Array2D<Integer>(w, 10, Integer.class);
        incrFill(arr, 0);
        for (int j = 0; j < arr.getHeight(); j++) {
            Integer[][] objRows = new Integer[][] {
                arr.getRow(j),
                arr.getRow(new Integer[w], j),
                arr.getRow(new Integer[w], 0, j, w),
                
                arr.getRow(new Integer[w - 2], j),
                arr.getRow(new Integer[w - 2], 2, j, w - 4)
            };
            int[][] primRows = new int[][] {
                arr.getPrimRow(j),
                arr.getRow(new int[w], j),
                arr.getRow(new int[w], 0, j, w),
                
                arr.getRow(new int[w - 2], j),
                arr.getRow(new int[w - 2], 2, j, w - 4)
            };
            for (int i = 0; i < w; i++) {
                Integer exp = i + j*arr.getWidth();
                for (int k = 0; k < 3; k++) {
                    assertEquals(genIndex(i, j), exp, objRows[k][i]);
                    assertEquals(genIndex(i, j), exp, (Integer) primRows[k][i]);
                }
                if (i >= w - 2) continue;
                assertEquals(genIndex(i, j), exp, objRows[3][i]);
                assertEquals(genIndex(i, j), exp, (Integer) primRows[3][i]);
                if (i < 2 || w - 4 <= i) continue;
                assertEquals(genIndex(i, j), exp, objRows[4][i - 2]);
                assertEquals(genIndex(i, j), exp, (Integer) primRows[4][i - 2]);
            }
        }
    }
    
    /**
     * Test for {@link Array2D#getRow(Object, int)} and
     * {@link Array2D#getRow(Object, int, int, int)}. <br>
     * Tests passing super typed and wrongly typed arrays.
     */
    @Test
    public void getRows1() {
        int w = 11;
        final Array2D<Integer> arr = new Array2D<Integer>(w, 10, Integer.class);
        incrFill(arr, 0);
        for (int index = 0; index < arr.getHeight(); index++) {
            int j = index;
            expEx(IllegalArgumentException.class, () -> {
                arr.getRow(new Double[w], j);
            });
            expEx(IllegalArgumentException.class, () -> {
                arr.getRow(new Double[w], 0, j, w);
            });
            expEx(IllegalArgumentException.class, () -> {
                arr.getRow(new double[w], j);
            });
            expEx(IllegalArgumentException.class, () -> {
                arr.getRow(new double[w], 0, j, w);
            });
            
            Number[][] rows = new Number[][] {
                arr.getRow(new Number[w], j),
                arr.getRow(new Number[w], 0, j, w)
            };
            
            for (int i = 0; i < w; i++) {
                Integer exp = i + j*w;
                for (int k = 0; k < 2; k++) {
                    assertEquals(genIndex(i, j), exp, rows[k][i]);
                }
            }
        }
        
        expEx(IllegalStateException.class, () -> new Array2D<String>(1, 1, String.class).getPrimRow(0));
    }
    
    /**
     * Test for {@link Array2D#getPrimRow(int)}, {@link Array2D#getRow(Object, int)}
     * and {@link Array2D#getRow(Object, int, int, int)}.  <br>
     * Tests getting {@code null} values.
     */
    @Test
    public void getRows2() {
        int w = 11;
        final Array2D<Integer> arr = new Array2D<Integer>(w, 10, Integer.class);
        for (int j = 0; j < arr.getHeight(); j++) {
            int[][] rows = new int[][] {
                arr.getPrimRow(j),
                arr.getRow(new int[w], j),
                arr.getRow(new int[w], 0, j, w)
            };
            for (int i = 0; i < arr.getWidth(); i++) {
                for (int k = 0; k < 3; k++) {
                    assertEquals(genIndex(i, j), 0, rows[k][i]);
                }
            }
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="getColumn">
    /**
     * Test for {@link Array2D#getPrimColumn(int)}, {@link Array2D#getColumn(int)},
     * {@link Array2D#getColumn(Object, int)}, {@link Array2D#getColumn(Object[] int)},
     * {@link Array2D#getColumn(Object, int, int, int)} and
     * {@link Array2D#getColumn(Object[], int, int, int)}. <br>
     * Tests default behaviour.
     */
    @Test
    public void getColumn0() {
        int h = 11;
        Array2D<Integer> arr = new Array2D<Integer>(10, h, Integer.class);
        incrFill(arr, 0);
        for (int i = 0; i < arr.getWidth(); i++) {
            Integer[][] objCols = new Integer[][] {
                arr.getColumn(i),
                arr.getColumn(new Integer[h], i),
                arr.getColumn(new Integer[h], i, 0, h),
                
                arr.getColumn(new Integer[h - 2], i),
                arr.getColumn(new Integer[h - 2], i, 2, h - 4)
            };
            int[][] primCols = new int[][] {
                arr.getPrimColumn(i),
                arr.getColumn(new int[h], i),
                arr.getColumn(new int[h], i, 0, h),
                
                arr.getColumn(new int[h - 2], i),
                arr.getColumn(new int[h - 2], i, 2, h - 4)
            };
            for (int j = 0; j < h; j++) {
                Integer exp = i + j*arr.getWidth();
                for (int k = 0; k < 3; k++) {
                    assertEquals(genIndex(i, j), exp, objCols[k][j]);
                    assertEquals(genIndex(i, j), exp, (Integer) primCols[k][j]);
                }
                if (j >= h - 2) continue;
                assertEquals(genIndex(i, j), exp, objCols[3][j]);
                assertEquals(genIndex(i, j), exp, (Integer) primCols[3][j]);
                if (j < 2 || h - 4 <= j) continue;
                assertEquals(genIndex(i, j), exp, objCols[4][j - 2]);
                assertEquals(genIndex(i, j), exp, (Integer) primCols[4][j - 2]);
            }
        }
    }
    
    /**
     * Test for {@link Array2D#getColumn(Object, int)} and
     * {@link Array2D#getColumn(Object, int, int, int)}. <br>
     * Tests passing super typed and wrongly typed arrays.
     */
    @Test
    public void getColumn1() {
        int h = 11;
        final Array2D<Integer> arr = new Array2D<Integer>(10, h, Integer.class);
        incrFill(arr, 0);
        for (int index = 0; index < arr.getWidth(); index++) {
            int i = index;
            expEx(IllegalArgumentException.class, () -> {
                arr.getColumn(new Double[h], i);
            });
            expEx(IllegalArgumentException.class, () -> {
                arr.getColumn(new Double[h], i, 0, h);
            });
            expEx(IllegalArgumentException.class, () -> {
                arr.getColumn(new double[h], i);
            });
            expEx(IllegalArgumentException.class, () -> {
                arr.getColumn(new double[h], i, 0, h);
            });
            
            Number[][] cols = new Number[][] {
                arr.getColumn(new Number[h], i),
                arr.getColumn(new Number[h], i, 0, h)
            };
            
            for (int j = 0; j < h; j++) {
                Integer exp = i + j*arr.getWidth();
                for (int k = 0; k < 2; k++) {
                    assertEquals(genIndex(i, j), exp, cols[k][j]);
                }
            }
        }
        
        expEx(IllegalStateException.class, () -> new Array2D<String>(1, 1, String.class).getPrimColumn(0));
    }
    
    /**
     * Test for {@link Array2D#getPrimColumn(int)}, {@link Array2D#getColumn(Object, int)}
     * and {@link Array2D#getColumn(Object, int, int, int)}.  <br>
     * Tests getting {@code null} values.
     */
    @Test
    public void getColumn2() {
        int h = 11;
        final Array2D<Integer> arr = new Array2D<Integer>(10, h, Integer.class);
        for (int i = 0; i < arr.getWidth(); i++) {
            int[][] cols = new int[][] {
                arr.getPrimColumn(i),
                arr.getColumn(new int[h], i),
                arr.getColumn(new int[h], i, 0, h)
            };
            for (int j = 0; j < arr.getHeight(); j++) {
                for (int k = 0; k < 3; k++) {
                    assertEquals(genIndex(j, i), 0, cols[k][j]);
                }
            }
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="getArray">
    /**
     * 
     */
    @Test
    public void getArray0() {
        
    }
    // </editor-fold>
    
    
}
