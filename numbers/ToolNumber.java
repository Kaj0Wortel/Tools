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

package tools.numbers;

// tmp
import tools.matrix.Mat;

/* 
 * A handy number data representation providing
 * the basic operations for numbers.
 */
public abstract class ToolNumber
        extends Number 
        implements tools.Cloneable {
    
    
    // Array containing the classes of the primative data types.
    @SuppressWarnings("unchecked") // <? extends Number> instanceof Number.
    final public static Class<? extends Number>[] PRIMATIVE_CLASSES
        = (Class<? extends Number>[]) new Class[] {
        Byte.class,
        Short.class,
        Integer.class,
        Long.class,
        Float.class,
        Double.class,
        Boolean.class,
        Character.class,
        Void.class
    };
    
    // Array containing the classes of the primative number data types.
    @SuppressWarnings("unchecked") // <? extends Number> instanceof Number.
    final public static Class<? extends Number>[] PRIMATIVE_NUMBER_CLASSES
        = (Class<? extends Number>[]) new Class[] {
        Byte.class,
        Short.class,
        Integer.class,
        Long.class,
        Float.class,
        Double.class
    };
    
    // Class containing all primative data types.
    @SuppressWarnings("unchecked") // <? extends Number> instanceof Number.
    final public static Class<? extends Number>[] PRIMATIVES
        = (Class<? extends Number>[]) new Class[] {
        Byte.TYPE,
        Short.TYPE,
        Integer.TYPE,
        Long.TYPE,
        Float.TYPE,
        Double.TYPE,
        Boolean.TYPE,
        Character.TYPE,
        Void.TYPE
    };
    
    
    /* -------------------------------------------------------------------------
     * Functions
     * -------------------------------------------------------------------------
     */
    /* 
     * Adds this number to the given number
     * 
     * @param num the number to be added.
     * @return a number representing {@code this + num}.
     * @throws NullPointerException iff {@code num == null}.
     * @throws NumberFormatException iff {@code num} is invallid.
     * @throws UnsupportedOperationException iff the function is not supported.
     * 
     * Note that the second function is in place and that the first function
     * returns a new number.
     */
    public abstract <T extends ToolNumber> T add(Number num);
    public abstract <T extends ToolNumber> T addi(Number num);
    
    /* 
     * Adds the given number to this number
     * 
     * @param num the number to be added.
     * @return a number representing {@code num + this}.
     * @throws NullPointerException iff {@code num == null}.
     * @throws NumberFormatException iff {@code num} is invallid.
     * @throws UnsupportedOperationException iff the function is not supported.
     * 
     * Note that the second function is in place and that the first function
     * returns a new number.
     */
    public abstract <T extends ToolNumber> T addInv(Number num);
    public abstract <T extends ToolNumber> T addInvi(Number num);
    
    /* 
     * Subtracts the given number from the this number.
     * 
     * @param num the number to be subtracted.
     * @return a number representing {@code this - num}.
     * @throws NullPointerException iff {@code num == null}.
     * @throws NumberFormatException iff {@code num} is invallid.
     * @throws UnsupportedOperationException iff the function is not supported.
     * 
     * Note that the second function is in place and that the first function
     * returns a new number.
     */
    public abstract <T extends ToolNumber> T sub(Number num);
    public abstract <T extends ToolNumber> T subi(Number num);
    
    /* 
     * Subtracts this number from the given number.
     * 
     * @param num the number to be subtracted.
     * @return a number representing {@code num - this}.
     * @throws NullPointerException iff {@code num == null}.
     * @throws NumberFormatException iff {@code num} is invallid.
     * @throws UnsupportedOperationException iff the function is not supported.
     * 
     * Note that the second function is in place and that the first function
     * returns a new number.
     */
    public abstract <T extends ToolNumber> T subInv(Number num);
    public abstract <T extends ToolNumber> T subInvi(Number num);
    
    /* 
     * Multiplies this number with the given number.
     * 
     * @param num the number to be multiplied with.
     * @return a number representing {@code this * num}.
     * @throws NullPointerException iff {@code num == null}.
     * @throws NumberFormatException iff {@code num} is invallid.
     * @throws UnsupportedOperationException iff the function is not supported.
     * 
     * Note that the second function is in place and that the first function
     * returns a new number.
     */
    public abstract <T extends ToolNumber> T mul(Number num);
    public abstract <T extends ToolNumber> T muli(Number num);
    
    /* 
     * Multiplies the given number with this number.
     * 
     * @param num the number to be multiplied with.
     * @return a number representing {@code num * this}.
     * @throws NullPointerException iff {@code num == null}.
     * @throws NumberFormatException iff {@code num} is invallid.
     * @throws UnsupportedOperationException iff the function is not supported.
     * 
     * Note that the second function is in place and that the first function
     * returns a new number.
     */
    public abstract <T extends ToolNumber> T mulInv(Number num);
    public abstract <T extends ToolNumber> T mulInvi(Number num);
    
    /* 
     * Divides this number by the given number.
     * 
     * @param num the number that is divided over this.
     * @return a number representing {@code this / num}.
     * @throws NullPointerException iff {@code num == null}.
     * @throws NumberFormatException iff {@code num} is invallid.
     * @throws UnsupportedOperationException iff the function is not supported.
     * 
     * Note that the second function is in place and that the first function
     * returns a new number.
     */
    public abstract <T extends ToolNumber> T div(Number num);
    public abstract <T extends ToolNumber> T divi(Number num);
    
    /* 
     * Divides the given number by this number.
     * 
     * @param num the number to be divided by this.
     * @return a number representing {@code num / this}.
     * @throws NullPointerException iff {@code num == null}.
     * @throws NumberFormatException iff {@code num} is invallid.
     * @throws UnsupportedOperationException iff the function is not supported.
     * 
     * Note that the second function is in place and that the first function
     * returns a new number.
     */
    public abstract <T extends ToolNumber> T divInv(Number num);
    public abstract <T extends ToolNumber> T divInvi(Number num);
    
    /* 
     * Takes this number modulo the given number.
     * 
     * @param mod the number over wich the modulo is calculated.
     * @return a number representing {@code this % num}.
     * @throws NullPointerException iff {@code num == null}.
     * @throws NumberFormatException iff {@code num} is invallid.
     * @throws UnsupportedOperationException iff the function is not supported.
     * 
     * Note that the second function is in place and that the first function
     * returns a new number.
     */
    public abstract <T extends ToolNumber> T mod(Number num);
    public abstract <T extends ToolNumber> T modi(Number num);
    
    /* 
     * Takes the given number modulo this number.
     * 
     * @param mod the number for which the modulo is calculated.
     * @return a number representing {@code num % this}.
     * @throws NullPointerException iff {@code num == null}.
     * @throws NumberFormatException iff {@code num} is invallid.
     * @throws UnsupportedOperationException iff the function is not supported.
     * 
     * Note that the second function is in place and that the first function
     * returns a new number.
     */
    public abstract <T extends ToolNumber> T modInv(Number num);
    public abstract <T extends ToolNumber> T modInvi(Number num);
    
    /* 
     * @return the default value of the number.
     */
    public abstract Number getValue();
    
    /* 
     * @return a clone of the current number
     */
    @Override
    public abstract ToolNumber clone();
    
    /* 
     * @return the inverse of the current number.
     */
    public abstract <T extends ToolNumber> T inverse();
    
    /* 
     * Calculates this number to the power {@code pow}.
     * 
     * @param pow the power.
     * @return {@code this ^ pow}.
     * @throws IllegalArgumentException iff {@code pow < 0}.
     * @throws NullPointerException iff {@code tn == null}.
     * 
     * Uses {@link pow(ToolNumber, int)}.
     */
    public ToolNumber pow(int pow) {
        return pow(this, pow);
    }
    
    /* 
     * Calculates this number to the power {@code pow}.
     * 
     * @param tn the number to be multiplied.
     * @param pow the power.
     * @return {@code this ^ pow}.
     * @throws IllegalArgumentException iff {@code pow < 0}.
     * @throws NullPointerException iff {@code tn == null}.
     */
    public static ToolNumber pow(ToolNumber tn, int pow) {
        if (pow < 0) {
            throw new IllegalArgumentException
                ("Negative powers are not supported!");
            
        } else if (pow == 0) {
            return tn.mul(0).add(1);
            
        } else if (pow == 1) {
            return tn;
            
        } else { // pow >= 2
            if (pow / 2 == pow / 2.0) {
                // pow is even.
                return pow(tn.mul(tn), pow / 2);
                
            } else {
                // pow is odd.
                return tn.mul(pow(tn.mul(tn), pow / 2));
            }
        }
    }
    
    /* 
     * Calculates {@code tn} to the power {@code pow}.
     * 
     * @param pow the power.
     * @return {@code this ^ pow}.
     * @throws IllegalArgumentException iff {@code pow < 0}.
     * @throws NullPointerException iff {@code tn == null}.
     * 
     * Note that the calculations are done in place, so afterwards
     * {@code \return == this} holds.
     */
    public ToolNumber powi(int pow) {
        return powi(this, pow);
    }
    
    /* 
     * Calculates {@code tn} to the power {@code pow}.
     * 
     * @param tn the number to be multiplied.
     * @param pow the power.
     * @return {@code this ^ pow}.
     * @throws IllegalArgumentException iff {@code pow < 0}.
     * @throws NullPointerException iff {@code tn == null}.
     * 
     * Note that the calculations are done in place, so afterwards
     * {@code \return == this} holds.
     */
    public static ToolNumber powi(ToolNumber tn, int pow) {
        if (pow < 0) {
            throw new IllegalArgumentException
                ("Negative powers are not supported!");
            
        } else if (pow == 0) {
            return tn.muli(0).addi(1);
            
        } else if (pow == 1) {
            return tn;
            
        } else { // pow >= 2
            if (pow / 2 == pow / 2.0) {
                // pow is even.
                return powi(tn.muli(tn), pow / 2);
                
            } else {
                // pow is odd.
                return tn.muli(powi(tn.muli(tn), pow / 2));
            }
        }
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions from Number
     * -------------------------------------------------------------------------
     */
    /* 
     * Allows a {@code NumberFormatException} for the xxxValue() functions
     * of the {@code Number} class.
     */
    @Override
    public abstract byte byteValue();
    
    @Override
    public abstract short shortValue();
    
    @Override
    public abstract int intValue();
    
    @Override
    public abstract long longValue();
    
    @Override
    public abstract float floatValue();
    
    @Override
    public abstract double doubleValue();
    
    
    /* -------------------------------------------------------------------------
     * General static methods
     * -------------------------------------------------------------------------
     */
    /* 
     * Checks whether the object is a primitive number data type class.
     * If obj is an array, check every element whether it is a primative 
     * data type.
     * 
     * @param obj the object to be checked.
     * @return {@code obj}.
     * @throws IllegalArguementException iff
     *     {@code object} is not a primative number data type class.
     * 
     * Uses {@link isPrim(Object)}.
     */
    public static Object checkPrim(Object obj) {
        if (isPrim(obj)) {
            return obj;
            
        } else {
            throw new IllegalArgumentException
                ("Expected a primative data type class, but found: "
                     + obj.getClass());
        }
    }
    
    /* 
     * Checks whether the object is a prmitive number data type class.
     * If obj is an array, check every element whether it is a prmative
     * data type.
     * 
     * @param obj the object to be checked.
     * @return whether {@code object} is a primative number data type class.
     */
    public static boolean isPrim(Object obj) {
        if (obj.getClass().isArray()) {
            // Recursivly determine whether 
            for (Object o : (Object[]) obj) {
                checkPrim(o);
            }
            
            return true;
        }
        
        // Checks for each primative
        for (Class<?> primative : PRIMATIVE_NUMBER_CLASSES) {
            if (primative.isInstance(obj)) {
                return true;
            }
        }
        
        return false;
    }
    
    public static void main(String[] args) {
        Mat mat = new Mat(2, 2,
                          2, 3,
                          4, 5);
        PrimitiveNumber<Double> num = new PrimitiveNumber<Double>(2.0);
        //System.out.println(mat.pow(2));
        System.out.println(mat.mul(num));
        System.out.println(mat);
    }
    
}