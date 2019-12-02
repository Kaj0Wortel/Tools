/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) December 2019 by Kaj Wortel - all rights reserved           *
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

package tools.matrix;


/**
 * Vector class for {@code double} vectors of length {@code 2}. <br>
 * A vector has no strict orientation, so {@code new DVec2(1, 2)} represents both
 * <pre>
 * ( 1 )
 * ( 2 ) </pre>
 * and
 * <pre>( 1  2 )</pre>
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class DVec2 {
    
    /* -------------------------------------------------------------------------
     * Constants.
     * -------------------------------------------------------------------------
     */
     
     
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The x component of the vector. */
    protected double x;
    /** The y component of the vector. */
    protected double y;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new vector of length 2. <br>
     * The following matrix is created: <pre>
     * ( x )
     * ( y ) </pre>
     * 
     * @param x The x component of the vector.
     * @param y The y component of the vector.
     */
    public DVec2(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    
    /* -------------------------------------------------------------------------
     * Get/set functions.
     * -------------------------------------------------------------------------
     */
    /**
     * @return The x component of {@code this} vector.
     */
    public double x() {
        return x;
    }
    
    /**
     * @return The y component of {@code this} vector.
     */
    public double y() {
        return y;
    }
    
    /**
     * @param x The new x component of {@code this} vector.
     */
    public void x(double x) {
        this.x = x;
    }
    
    /**
     * @param y The new y component of {@code this} vector.
     */
    public void y(double y) {
        this.y = y;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Addition.
     * 
     * @param v The vector to add.
     * 
     * @return {@code this}.
     */
    public DVec2 add(DVec2 v) {
        x += v.x;
        y += v.y;
        return this;
    }
    
    /**
     * Copy addition.
     * 
     * @param v The vector to add.
     * 
     * @return A new vector representing {@code this + v}.
     */
    public DVec2 cAdd(DVec2 v) {
        return new DVec2(x + v.x, y + v.y);
    }
    
    /**
     * Subtraction.
     * 
     * @param v The vector to subtract.
     * 
     * @return {@code this}.
     */
    public DVec2 sub(DVec2 v) {
        x -= v.x;
        y -= v.y;
        return this;
    }
    
    /**
     * Copy subtraction.
     * 
     * @param v The vector to subtract.
     * 
     * @return A new vector representing {@code this - v}.
     */
    public DVec2 cSub(DVec2 v) {
        return new DVec2(x - v.x, y - v.y);
    }
    
    /**
     * Inverse subtraction.
     * 
     * @param v The vector to subtract from.
     * 
     * @return {@code v}.
     */
    public DVec2 iSub(DVec2 v) {
        v.x -= x;
        v.y -= y;
        return v;
    }
    
    /**
     * Inverse copy subtraction.
     * 
     * @param v The vector to subtract from.
     * 
     * @return A new vector representing {@code v - this}.
     */
    public DVec2 icSub(DVec2 v) {
        return new DVec2(v.x - x, v.y - y);
    }
    
    /**
     * @param v The vector used in the dot product.
     * 
     * @return The dot product of {@code this} and {@code v}.
     */
    public double dot(DVec2 v) {
        return x*v.x + y*v.y;
    }
    
    /**
     * Matrix vector multiplication.
     * 
     * @param mat The matrix to pre-multiply with.
     * 
     * @return {@code this}
     */
    public DVec2 mul(DMat2 mat) {
        double a = x;
        double b = y;
        x = a*mat.m00 + b*mat.m01;
        y = a*mat.m10 + b*mat.m11;
        return this;
    }
    
    /**
     * Copy matrix vector multiplication.
     * 
     * @param mat The matrix to pre-multiply with.
     * 
     * @return A new vector representing {@code mat * this}.
     */
    public DVec2 cMul(DMat2 mat) {
        return new DVec2(
                x*mat.m00 + y*mat.m01,
                x*mat.m10 + y*mat.m11
        );
    }
    
    /**
     * Vector matrix multiplication.
     * 
     * @param mat The matrix to post-multiply with.
     * 
     * @return {@code this}
     */
    public DVec2 iMul(DMat2 mat) {
        double a = x;
        double b = y;
        x = a*mat.m00 + b*mat.m10;
        y = a*mat.m01 + b*mat.m11;
        return this;
    }
    
    /**
     * Copy vector matrix multiplication.
     * 
     * @param mat The matrix to post-multiply with.
     * 
     * @return A new vector representing {@code this * mat}.
     */
    public DVec2 icMul(DMat2 mat) {
        return new DVec2(
                x*mat.m00 + y*mat.m10,
                x*mat.m01 + y*mat.m11
        );
    }
    
    
}
