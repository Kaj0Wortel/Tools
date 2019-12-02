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
 * A vector has no strict orientation, so {@code new DVec2(1, 2, 3)} represents both
 * <pre>
 * ( 1 )
 * ( 2 )
 * ( 3 ) </pre>
 * and
 * <pre>( 1  2  3 )</pre>
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class DVec3 {
    
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
    /** The z component of the vector. */
    protected double z;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new vector of length 2. <br>
     * The following matrix is created: <pre>
     * ( x )
     * ( y )
     * ( z ) </pre>
     * 
     * @param x The x component of the vector.
     * @param y The y component of the vector.
     * @param z The z component of the vector.
     */
    public DVec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
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
     * @return The z component of {@code this} vector.
     */
    public double z() {
        return z;
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
    
    /**
     * @param z The new z component of {@code this} vector.
     */
    public void z(double z) {
        this.z = z;
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
    public DVec3 add(DVec3 v) {
        x += v.x;
        y += v.y;
        z += v.z;
        return this;
    }
    
    /**
     * Copy addition.
     * 
     * @param v The vector to add.
     * 
     * @return A new vector representing {@code this + v}.
     */
    public DVec3 cAdd(DVec3 v) {
        return new DVec3(x + v.x, y + v.y, z + v.z);
    }
    
    /**
     * Subtraction.
     * 
     * @param v The vector to subtract.
     * 
     * @return {@code this}.
     */
    public DVec3 sub(DVec3 v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        return this;
    }
    
    /**
     * Copy subtraction.
     * 
     * @param v The vector to subtract.
     * 
     * @return A new vector representing {@code this - v}.
     */
    public DVec3 cSub(DVec3 v) {
        return new DVec3(x - v.x, y - v.y, z - v.z);
    }
    
    /**
     * Inverse subtraction.
     * 
     * @param v The vector to subtract from.
     * 
     * @return {@code v}.
     */
    public DVec3 iSub(DVec3 v) {
        v.x -= x;
        v.y -= y;
        v.z -= z;
        return v;
    }
    
    /**
     * Inverse copy subtraction.
     * 
     * @param v The vector to subtract from.
     * 
     * @return A new vector representing {@code v - this}.
     */
    public DVec3 icSub(DVec3 v) {
        return new DVec3(v.x - x, v.y - y, v.z - z);
    }
    
    /**
     * @param v The vector used in the dot product.
     * 
     * @return The dot product of {@code this} and {@code v}.
     */
    public double dot(DVec3 v) {
        return x*v.x + y*v.y + z*v.z;
    }
    
    /**
     * Computes the cross product of {@code this} and {@code v}.
     * 
     * @param v The right hand vector used in the cross product.
     * 
     * @return A new vector representing {@code this X v}.
     */
    public DVec3 cross(DVec3 v) {
        return new DVec3(x*v.z - z*v.x, z*v.x - x*v.z, x*v.y - y*v.x);
    }
    
    /**
     * Matrix vector multiplication.
     * 
     * @param mat The matrix to pre-multiply with.
     * 
     * @return {@code this}
     */
    public DVec3 mul(DMat3 mat) {
        double a = x;
        double b = y;
        double c = z;
        x = a*mat.m00 + b*mat.m01 + c*mat.m02;
        y = a*mat.m10 + b*mat.m11 + c*mat.m12;
        z = a*mat.m20 + b*mat.m21 + c*mat.m22;
        return this;
    }
    
    /**
     * Copy matrix vector multiplication.
     * 
     * @param mat The matrix to pre-multiply with.
     * 
     * @return A new vector representing {@code mat * this}.
     */
    public DVec3 cMul(DMat3 mat) {
        return new DVec3(
                x*mat.m00 + y*mat.m01 + z*mat.m02,
                x*mat.m10 + y*mat.m11 + z*mat.m12,
                x*mat.m20 + y*mat.m21 + z*mat.m22
        );
    }
    
    /**
     * Vector matrix multiplication.
     * 
     * @param mat The matrix to post-multiply with.
     * 
     * @return {@code this}
     */
    public DVec3 iMul(DMat3 mat) {
        double a = x;
        double b = y;
        double c = z;
        x = a*mat.m00 + b*mat.m10 + c*mat.m20;
        y = a*mat.m01 + b*mat.m11 + c*mat.m21;
        z = a*mat.m02 + b*mat.m12 + c*mat.m22; 
        return this;
    }
    
    /**
     * Copy vector matrix multiplication.
     * 
     * @param mat The matrix to post-multiply with.
     * 
     * @return A new vector representing {@code this * mat}.
     */
    public DVec3 icMul(DMat3 mat) {
        return new DVec3(
                x*mat.m00 + y*mat.m10 + z*mat.m20,
                x*mat.m01 + y*mat.m11 + z*mat.m21,
                x*mat.m02 + y*mat.m12 + z*mat.m22
        );
    }
    
    
}
