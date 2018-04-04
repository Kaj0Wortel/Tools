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
//todo
package tools.matrix;


/* 
 * Under development.
 * Use with care!
 */
/* 
 * Represents a 3x3 matrix.
 */
public class Mat3 extends Mat{
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructors
     * ----------------------------------------------------------------------------------------------------------------
     */
    public Mat3(double[]... mat) {
        super(mat);
    }
    
    /* 
     * Returns a matrix for a counter-clockwise rotation
     * around the x-axis.
     */
    public static Mat3 rotateMat3X(double rad) {
        return new Mat3(new double[][] {
            {1, 0            ,  0            },
            {0, Math.cos(rad), -Math.sin(rad)},
            {0, Math.sin(rad),  Math.cos(rad)}}
        );
    }
    
    /* 
     * Returns a matrix for a counter-clockwise rotation
     * around the y-axis.
     */
    public static Mat3 rotateMat3Y(double rad) {
        return new Mat3(new double[][] {
            { Math.cos(rad), 0, Math.sin(rad)},
            { 0            , 1, 0            },
            {-Math.sin(rad), 0, Math.cos(rad)}}
        );
    }
    
    /* 
     * Returns a matrix for a counter-clockwise rotation
     * around the z-axis.
     */
    public static Mat3 rotateMat3Z(double rad) {
        return new Mat3(new double[][] {
            {Math.cos(rad), -Math.sin(rad), 0},
            {Math.sin(rad),  Math.cos(rad), 0},
            {0            , 0             , 1}}
        );
    }
    /* ----------------------------------------------------------------------------------------------------------------
     * Overridden functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Overrides the matrix dimension check.
     * See the Matrix class for more info.
     */
    @Override
    protected boolean matrixDimCheck(int row, int col) {
        if (row != 3 || col != 3)
            throw new MatrixDimensionException("Illegal change of matrix size. Found: " + row + "x" + col
                                                   + "Expected: 3x3.");
        return true;
    }
}