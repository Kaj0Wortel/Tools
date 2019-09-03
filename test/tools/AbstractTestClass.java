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

package tools;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import tools.data.array.ArrayTools;



/**
 * This is an abstract testing class which should be extended by all testing classes.
 * 
 * @todo
 * - All
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public abstract class AbstractTestClass {
    
    /* -------------------------------------------------------------------------
     * Inner-classes.
     * -------------------------------------------------------------------------
     */
    /**
     * Same interface as {@link Runnable}, but now the run function is allowed
     * to throw an exception.
     */
    @FunctionalInterface
    protected static interface ExceptionRunner {
        
        /**
         * The function to be invoked by the runner.
         * 
         * @throws Exception The exception to be thrown.
         */
        public void run()
                throws Exception;
        
        
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Runs the given code and tests whether the provided exception is thrown.
     * 
     * @param c The class of the expected exception.
     * @param er The exception runner to execute.
     */
    protected static void expEx(Class<? extends Exception> c, ExceptionRunner er) {
        try {
            er.run();
            fail("Expected a " + c.getName() + " to be thrown, but it terminated normally.");
            
        } catch (Exception e) {
            assertTrue("Expected a " + c.getName() + " to be thrown, but a "
                    + e.getClass().getName() + "was thrown instead.", c.isInstance(e));
        }
    }
    
    /**
     * Generates an array of strings containing the number {@code 0} up to {@code len}.
     * 
     * @param len The length of the array to return.
     * 
     * @return A deterministic array of strings.
     */
    protected static String[] genStrArr(int len) {
        String[] strs = new String[len];
        for (int i = 0; i < strs.length; i++) {
            strs[i] = Integer.toString(i);
        }
        return strs;
    }
    
    /**
     * Generates an array of integers containing the number {@code 0} up to {@code len}.
     * 
     * @param len The length of the array to return.
     * 
     * @return A deterministic array of integers.
     */
    protected static int[] genIntArr(int len) {
        int[] ints = new int[len];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = i;
        }
        return ints;
    }
    
    /**
     * Generates an index to be used as message from the given indices.
     * 
     * @param indices The indices used to generate the message from.
     * 
     * @return A message from the proviced indices.
     */
    protected static String genIndex(Object... indices) {
        if (indices == null) return "";
        StringBuilder sb = new StringBuilder("@(");
        boolean first = true;
        for (int i = 0; i < indices.length; i++) {
            if (first) first = false;
            else sb.append(",");
            sb.append((indices[i] == null ? "null" : indices[i].toString()));
        }
        sb.append(")");
        return sb.toString();
    }
    
    /**
     * Fills the given 2D array with the given value.
     * 
     * @param arr The array to fill.
     * @param init The initial number in the array.
     */
    protected static <T> T[] fill(T[] arr, Object val) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < ArrayTools.length(arr[i]); j++) {
                ArrayTools.set(arr[i], j, val);
            }
        }
        return arr;
    }
    
    /**
     * Fills the given 2D array with values from {@code init} to
     * {@code init + arr.getWidth() * arr.getHeight()}. The rows are filled incrementally.
     * 
     * @param arr The array to fill.
     * @param init The initial number in the array.
     */
    public static <T> T[] incrFill(T[] arr, int init) {
        if (arr.length == 0) return arr;
        int val = init;
        for (int j = 0; j < ArrayTools.length(arr[0]); j++) {
            for (int i = 0; i < arr.length; i++) {
                ArrayTools.set(arr[i], j, val++);
            }
        }
        return arr;
    }
    
    
}
