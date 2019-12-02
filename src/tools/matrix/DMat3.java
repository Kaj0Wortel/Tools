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
public class DMat3 {
    
    /* -------------------------------------------------------------------------
     * Constants.
     * -------------------------------------------------------------------------
     */
     
     
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The values of the matrix */
    protected double
            m00, m01, m02,
            m10, m11, m12,
            m20, m21, m22;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new 3x3 matrix. <br>
     * The following matrix is created:
     * <pre>
     * [ m00 m01 m02 ]
     * [ m10 m11 m12 ]
     * [ m20 m21 m22 ]
     * </pre>
     * 
     * @param m00 A value of the matrix.
     * @param m01 A value of the matrix.
     * @param m02 A value of the matrix.
     * @param m10 A value of the matrix.
     * @param m11 A value of the matrix.
     * @param m12 A value of the matrix.
     * @param m20 A value of the matrix.
     * @param m21 A value of the matrix.
     * @param m22 A value of the matrix.
     */
    public DMat3(double m00, double m01, double m02,
            double m10, double m11, double m12,
            double m20, double m21, double m22) {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
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
     * @return the value at {@code (0, 2)}.
     */
    public double m02() {
        return m02;
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
     * @return the value at {@code (1, 2)}.
     */
    public double m12() {
        return m12;
    }
    
    /**
     * @return the value at {@code (2, 0)}.
     */
    public double m20() {
        return m20;
    }
    
    /**
     * @return the value at {@code (2, 1)}.
     */
    public double m21() {
        return m21;
    }
    
    /**
     * @return the value at {@code (2, 2)}.
     */
    public double m22() {
        return m22;
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
     * @param d The new value for {@code (0, 2)}.
     */
    public void m02(double d) {
        m02 = d;
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
    
    /**
     * @param d The new value for {@code (1, 2)}.
     */
    public void m12(double d) {
        m12 = d;
    }
    
    /**
     * @param d The new value for {@code (2, 0)}.
     */
    public void m20(double d) {
        m20 = d;
    }
    
    /**
     * @param d The new value for {@code (2, 1)}.
     */
    public void m21(double d) {
        m21 = d;
    }
    
    /**
     * @param d The new value for {@code (2, 2)}.
     */
    public void m22(double d) {
        m22 = d;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * @return The identity matrix.
     */
    public static DMat3 i() {
        return new DMat3(
                1, 0, 0,
                0, 1, 0,
                0, 0, 1
        );
    }
    
    /**
     * Addition.
     * 
     * @param mat The matrix to add to {@code this}.
     * 
     * @return {@code this} matrix.
     */
    public DMat3 add(DMat3 mat) {
        m00 += mat.m00;
        m01 += mat.m01;
        m02 += mat.m02;
        m10 += mat.m10;
        m11 += mat.m11;
        m12 += mat.m12;
        m20 += mat.m20;
        m21 += mat.m21;
        m22 += mat.m22;
        return this;
    }
    
    /**
     * Copy addition.
     * 
     * @param mat The matrix to add to {@code this}.
     * 
     * @return A new matrix containing the result of {@code this + mat}
     */
    public DMat3 cAdd(DMat3 mat) {
        return new DMat3(
                m00 + mat.m00, m01 + mat.m01, m02 + mat.m02,
                m10 + mat.m10, m11 + mat.m11, m12 + mat.m12,
                m20 + mat.m20, m21 + mat.m21, m22 + mat.m22
        );
    }
    
    /**
     * Subtraction.
     * 
     * @param mat The matrix to subtract from {@code this}.
     * 
     * @return {@code this} matrix.
     */
    public DMat3 sub(DMat3 mat) {
        m00 -= mat.m00;
        m01 -= mat.m01;
        m01 -= mat.m01;
        m10 -= mat.m10;
        m11 -= mat.m11;
        m12 -= mat.m12;
        m20 -= mat.m20;
        m21 -= mat.m21;
        m22 -= mat.m21;
        return this;
    }
    
    /**
     * Copy subtraction.
     * 
     * @param mat The matrix to subtract from {@code this}.
     * 
     * @return A new matrix containing the result of {@code this - mat}
     */
    public DMat3 cSub(DMat3 mat) {
        return new DMat3(
                m00 - mat.m00, m01 - mat.m01, m02 - mat.m02,
                m10 - mat.m10, m11 - mat.m11, m12 - mat.m12,
                m20 - mat.m20, m21 - mat.m21, m22 - mat.m22
        );
    }
    
    /**
     * Inverse subtraction.
     * 
     * @param mat The matrix to subtract {@code this} from.
     * 
     * @return {@code mat}.
     */
    public DMat3 iSub(DMat3 mat) {
        mat.m00 -= m00;
        mat.m01 -= m01;
        mat.m02 -= m02;
        mat.m10 -= m10;
        mat.m11 -= m11;
        mat.m12 -= m12;
        mat.m20 -= m20;
        mat.m21 -= m21;
        mat.m22 -= m21;
        return mat;
    }
    
    /**
     * Inverse copy subtraction.
     * 
     * @param mat The matrix to subtract {@code this} from.
     * 
     * @return A new matrix containing the result of {@code mat - this}
     */
    public DMat3 icSub(DMat3 mat) {
        return new DMat3(
                mat.m00 - m00, mat.m01 - m01, mat.m02 - m02,
                mat.m10 - m10, mat.m11 - m11, mat.m12 - m12,
                mat.m20 - m20, mat.m21 - m21, mat.m22 - m22
        );
    }
    
    /**
     * Matrix multiplication.
     * 
     * @param mat The matrix to post-multiply with.
     * 
     * @return {@code this} matrix.
     */
    public DMat3 mul(DMat3 mat) {
        double a00 = m00;
        double a01 = m01;
        double a02 = m02;
        double a10 = m10;
        double a11 = m11;
        double a12 = m12;
        double a20 = m20;
        double a21 = m21;
        double a22 = m22;
        
        m00 = a00*mat.m00 + a01*mat.m10 + a02*mat.m20;
        m01 = a00*mat.m01 + a01*mat.m11 + a02*mat.m21;
        m02 = a00*mat.m02 + a01*mat.m12 + a02*mat.m22;
        
        m10 = a10*mat.m00 + a11*mat.m10 + a12*mat.m20;
        m11 = a10*mat.m01 + a11*mat.m11 + a12*mat.m21;
        m12 = a10*mat.m02 + a11*mat.m12 + a12*mat.m22;
        
        m00 = a20*mat.m00 + a21*mat.m10 + a22*mat.m20;
        m01 = a20*mat.m01 + a21*mat.m11 + a22*mat.m21;
        m02 = a20*mat.m02 + a21*mat.m12 + a22*mat.m22;
        return this;
    }
    
    /**
     * Copy matrix multiplication.
     * 
     * @param mat The matrix to post-multiply with.
     * 
     * @return A new matrix containing the result of {@code mat * this}
     */
    public DMat3 cMul(DMat3 mat) {
        return new DMat3(
                m00*mat.m00 + m01*mat.m10 + m02*mat.m20,
                m00*mat.m01 + m01*mat.m11 + m02*mat.m21,
                m00*mat.m02 + m01*mat.m12 + m02*mat.m22,
                
                m10*mat.m00 + m11*mat.m10 + m12*mat.m20,
                m10*mat.m01 + m11*mat.m11 + m12*mat.m21,
                m10*mat.m02 + m11*mat.m12 + m12*mat.m22,
                
                m20*mat.m00 + m21*mat.m10 + m22*mat.m20,
                m20*mat.m01 + m21*mat.m11 + m22*mat.m21,
                m20*mat.m02 + m21*mat.m12 + m22*mat.m22
        );
    }
    
    /**
     * Inverse matrix multiplication.
     * 
     * @param mat The matrix to pre-multiply with.
     * 
     * @return {@code this} matrix.
     */
    public DMat3 iMul(DMat3 mat) {
        double a00 = m00;
        double a01 = m01;
        double a02 = m02;
        double a10 = m10;
        double a11 = m11;
        double a12 = m12;
        double a20 = m20;
        double a21 = m21;
        double a22 = m22;
        
        m00 = mat.m00*a00 + mat.m01*a10 + mat.m02*a20;
        m01 = mat.m00*a01 + mat.m01*a11 + mat.m02*a21;
        m02 = mat.m00*a02 + mat.m01*a12 + mat.m02*a22;
        
        m10 = mat.m10*a00 + mat.m11*a10 + mat.m12*a20;
        m11 = mat.m10*a01 + mat.m11*a11 + mat.m12*a21;
        m12 = mat.m10*a02 + mat.m11*a12 + mat.m12*a22;
        
        m00 = mat.m20*a00 + mat.m21*a10 + mat.m22*a20;
        m01 = mat.m20*a01 + mat.m21*a11 + mat.m22*a21;
        m02 = mat.m20*a02 + mat.m21*a12 + mat.m22*a22;
        return this;
    }
    
    /**
     * Inverse copy matrix multiplication.
     * 
     * @param mat The matrix to pre-multiply with.
     * 
     * @return A new matrix containing the result of {@code mat * this}
     */
    public DMat3 icMul(DMat3 mat) {
        return new DMat3(
                mat.m00*m00 + mat.m01*m10 + mat.m02*m20,
                mat.m00*m01 + mat.m01*m11 + mat.m02*m21,
                mat.m00*m02 + mat.m01*m12 + mat.m02*m22,
                
                mat.m10*m00 + mat.m11*m10 + mat.m12*m20,
                mat.m10*m01 + mat.m11*m11 + mat.m12*m21,
                mat.m10*m02 + mat.m11*m12 + mat.m12*m22,
                
                mat.m20*m00 + mat.m21*m10 + mat.m22*m20,
                mat.m20*m01 + mat.m21*m11 + mat.m22*m21,
                mat.m20*m02 + mat.m21*m12 + mat.m22*m22
        );
    }
    
    /**
     * Scalar multiplication.
     * 
     * @param val The value to multiply with.
     * 
     * @return {@code this} matrix.
     */
    public DMat3 mul(double val) {
        m00 *= val;
        m01 *= val;
        m02 *= val;
        m10 *= val;
        m11 *= val;
        m12 *= val;
        m20 *= val;
        m21 *= val;
        m22 *= val;
        return this;
    }
    
    /**
     * Copy scalar multiplication.
     * 
     * @param val The value to multiply with.
     * 
     * @return A new matrix containing the result of {@code val * this}
     */
    public DMat3 cMul(double val) {
        return new DMat3(
                m00 * val, m01 * val, m02 * val,
                m10 * val, m11 * val, m12 * val,
                m20 * val, m21 * val, m22 * val
        );
    }
    
    /**
     * Calculates the determinant of the matrix 2x2 matrix. <br>
     * The result is: <br>
     * | a00 a01 | <br>
     * | a10 a11 |
     * 
     * @param a00 The element at (0, 0) in the 2x2 matrix.
     * @param a01 The element at (0, 1) in the 2x2 matrix.
     * @param a10 The element at (1, 0) in the 2x2 matrix.
     * @param a11 The element at (1, 1) in the 2x2 matrix.
     * 
     * @return The determinant of the 2x2 matrix.
     */
    private double detMat2(double a00, double a01, double a10, double a11) {
        return a00 * a11 - a01 * a10;
    }
    
    /**
     * Calculates the determinant of the matrix 2x2 matrix. <br>
     * The result is: <br>
     * | m00 m01 m02 | <br>
     * | m10 m11 m12 | <br>
     * | m20 m21 m22 |
     * 
     * @return The determinant of this matrix.
     */
    public double det() {
        return m00 * detMat2(m11, m12, m21, m22)
                - m01 * detMat2(m10, m12, m20, m22)
                + m02 * detMat2(m10, m11, m20, m21);
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
    public DMat3 inverse() {
        DMat3 minor = minorMatrix();
        minor.m01 = -minor.m01;
        minor.m10 = -minor.m10;
        minor.m12 = -minor.m12;
        minor.m21 = -minor.m21;
        double det = m00*minor.m00 + m01*minor.m01 + m02*minor.m02;
        if (det == 0) {
            throw new IllegalArgumentException("No inverse exists!");
        }
        
        return minor.transpose().mul(1/det);
    }
    
    /**
     * @return The minor matrices of this matrix.
     */
    public DMat3 minorMatrix() {
        return new DMat3(
                detMat2(m11, m12, m21, m22), detMat2(m10, m12, m20, m22), detMat2(m10, m11, m20, m21),
                detMat2(m01, m02, m21, m22), detMat2(m00, m02, m20, m22), detMat2(m00, m01, m20, m21),
                detMat2(m01, m02, m11, m12), detMat2(m00, m02, m10, m12), detMat2(m00, m01, m10, m11)
        );
    }
    
    public DMat3 adj() {
        DMat3 minor = minorMatrix();
        minor.m01 = -minor.m01;
        minor.m10 = -minor.m10;
        minor.m12 = -minor.m12;
        minor.m21 = -minor.m21;
        return minor;
    }
    
    /**
     * Transpose matrix.
     * 
     * @return {@code thes}
     */
    public DMat3 transpose() {
        double tmp = m01;
        m01 = m10;
        m10 = tmp;
        
        tmp = m20;
        m20 = m02;
        m02 = tmp;
        
        tmp = m12;
        m12 = m21;
        m21 = tmp;
        return this;
    }
    
    /**
     * Copy transpose matrix.
     * 
     * @return <pre>this<sup>T</sup></pre>
     */
    public DMat3 cTranspose() {
        return new DMat3(
                m00, m10, m20,
                m01, m11, m21,
                m02, m12, m22
        );
    }
    
    
}
