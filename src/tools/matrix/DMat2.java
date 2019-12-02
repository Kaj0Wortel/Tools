/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) December 2019 by Kaj Wortel - all rights reserved          *
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
 * Matrix class for {@code 2x2} {@code double} matrices.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class DMat2 {
    
    /* -------------------------------------------------------------------------
     * Constants.
     * -------------------------------------------------------------------------
     */
     
     
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The values of the matrix. */
    protected double
            m00, m01,
            m10, m11;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new 3x3 matrix. <br>
     * The following matrix is created:
     * <pre>
     * [ m00 m01 ]
     * [ m10 m11 ]
     * </pre>
     * 
     * @param m00 A value of the matrix.
     * @param m01 A value of the matrix.
     * @param m10 A value of the matrix.
     * @param m11 A value of the matrix. 
     */
    public DMat2(double m00, double m01, double m10, double m11) {
        this.m00 = m00;
        this.m01 = m01;
        this.m10 = m10;
        this.m11 = m11;
    }
    
    
    /* -------------------------------------------------------------------------
     * Get/set Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * @return the value at {@code (0, 0)}.
     */
    public double m00() {
        return m00;
    }
    
    /**
     * @return the value at {@code (0, 1)}.
     */
    public double m01() {
        return m01;
    }
    
    /**
     * @return the value at {@code (1, 0)}.
     */
    public double m10() {
        return m10;
    }
    
    /**
     * @return the value at {@code (1, 1)}.
     */
    public double m11() {
        return m11;
    }
    
    /**
     * @param d The new value for {@code (0, 0)}.
     */
    public void m00(double d) {
        m00 = d;
    }
    
    /**
     * @param d The new value for {@code (0, 1)}.
     */
    public void m01(double d) {
        m01 = d;
    }
    
    /**
     * @param d The new value for {@code (1, 0)}.
     */
    public void m10(double d) {
        m10 = d;
    }
    
    /**
     * @param d The new value for {@code (1, 1)}.
     */
    public void m11(double d) {
        m11 = d;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * @return The identity matrix.
     */
    public DMat2 i() {
        return new DMat2(
                1, 0,
                0, 1
        );
    }
    
    /**
     * Addition.
     * 
     * @param mat The matrix to add to {@code this}.
     * 
     * @return {@code this} matrix.
     */
    public DMat2 add(DMat2 mat) {
        m00 += mat.m00;
        m01 += mat.m01;
        m10 += mat.m10;
        m11 += mat.m11;
        return this;
    }
    
    /**
     * Copy addition.
     * 
     * @param mat The matrix to add to {@code this}.
     * 
     * @return A new matrix containing the result of {@code this + mat}
     */
    public DMat2 cAdd(DMat2 mat) {
        return new DMat2(
                m00 + mat.m00, m01 + mat.m01,
                m10 + mat.m10, m11 + mat.m11
        );
    }
    
    /**
     * Subtraction.
     * 
     * @param mat The matrix to subtract from {@code this}.
     * 
     * @return {@code this} matrix.
     */
    public DMat2 sub(DMat2 mat) {
        m00 -= mat.m00;
        m01 -= mat.m01;
        m10 -= mat.m10;
        m11 -= mat.m11;
        return this;
    }
    
    /**
     * Copy subtraction.
     * 
     * @param mat The matrix to subtract from {@code this}.
     * 
     * @return A new matrix containing the result of {@code this - mat}
     */
    public DMat2 cSub(DMat2 mat) {
        return new DMat2(
                m00 - mat.m00, m01 - mat.m01,
                m10 - mat.m10, m11 - mat.m11
        );
    }
    
    /**
     * Inverse subtraction.
     * 
     * @param mat The matrix to subtract {@code this} from.
     * 
     * @return {@code mat}.
     */
    public DMat2 iSub(DMat2 mat) {
        mat.m00 -= m00;
        mat.m01 -= m01;
        mat.m10 -= m10;
        mat.m11 -= m11;
        return mat;
    }
    
    
    /**
     * Inverse copy subtraction.
     * 
     * @param mat The matrix to subtract {@code this} from.
     * 
     * @return A new matrix containing the result of {@code mat - this}
     */
    public DMat2 icSub(DMat2 mat) {
        return new DMat2(
                mat.m00 - m00, mat.m01 - m01,
                mat.m10 - m10, mat.m11 - m11
        );
    }
    
    /**
     * Matrix multiplication.
     * 
     * @param mat The matrix to post-multiply with.
     * 
     * @return {@code this} matrix.
     */
    public DMat2 mul(DMat2 mat) {
        double a00 = m00;
        double a01 = m01;
        double a10 = m10;
        double a11 = m11;
        m00 = a00 * mat.m00 + a01 * mat.m10;
        m01 = a00 * mat.m01 + a01 * mat.m11;
        m10 = a10 * mat.m00 + a11 * mat.m10;
        m11 = a10 * mat.m01 + a11 * mat.m11;
        return this;
    }
    
    /**
     * Copy matrix multiplication.
     * 
     * @param mat The matrix to post-multiply with.
     * 
     * @return A new matrix containing the result of {@code mat * this}
     */
    public DMat2 cMul(DMat2 mat) {
        return new DMat2(
                m00 * mat.m00 + m01 * mat.m10, m00 * mat.m01 + m01 * mat.m11,
                m10 * mat.m00 + m11 * mat.m10, m10 * mat.m01 + m11 * mat.m11
        );
    }
    
    /**
     * Inverse matrix multiplication.
     * 
     * @param mat The matrix to pre-multiply with.
     * 
     * @return {@code this} matrix.
     */
    public DMat2 iMul(DMat2 mat) {
        double a00 = m00;
        double a01 = m01;
        double a10 = m10;
        double a11 = m11;
        m00 = mat.m00 * a00 + mat.m01 * a10;
        m01 = mat.m00 * a01 + mat.m01 * a11;
        m10 = mat.m10 * a00 + mat.m11 * a10;
        m11 = mat.m10 * a01 + mat.m11 * a11;
        return this;
    }
    
    /**
     * Inverse copy matrix multiplication.
     * 
     * @param mat The matrix to pre-multiply with.
     * 
     * @return {@code this}.
     */
    public DMat2 icMul(DMat2 mat) {
        return new DMat2(
                mat.m00 * m00 + mat.m01 * m10, mat.m00 * m01 + mat.m01 * m11,
                mat.m10 * m00 + mat.m11 * m10, mat.m10 * m01 + mat.m11 * m11
        );
    }
    
    /**
     * Scalar multiplication.
     * 
     * @param val The value to multiply with.
     * 
     * @return {@code this} matrix.
     */
    public DMat2 mul(double val) {
        m00 *= val;
        m01 *= val;
        m10 *= val;
        m11 *= val;
        return this;
    }
    
    /**
     * Copy scalar multiplication.
     * 
     * @param val The value to multiply with.
     * 
     * @return A new matrix containing the result of {@code val * this}
     */
    public DMat2 cMul(double val) {
        return new DMat2(
                m00 * val, m01 * val,
                m10 * val, m11 * val
        );
    }
    
    /**
     * @return The determinant of this matrix.
     */
    public double det() {
        return m00 * m11 - m01 * m10;
    }
    
    /**
     * @return {@code true} if the inverse of {@code this} matrix exists.
     */
    public boolean inverseExists() {
        return det() != 0;
    }
    
    /**
     * @return The inverse of {@code this}, if it exists.
     * 
     * @throws IllegalArgumentException If no inverse exists.
     */
    public DMat2 inverse() {
        double det = det();
        if (det == 0) {
            throw new IllegalArgumentException("No inverse exists!");
        }
        return new DMat2(
                 m11/det, -m01/det,
                -m10/det,  m00/det
        );
    }
    
    
}
