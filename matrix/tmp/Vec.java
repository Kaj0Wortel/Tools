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
 * In development.
 * Use with care!
 */
public class Vec extends Mat {
    /* -------------------------------------------------------------------------
     * Constants
     * -------------------------------------------------------------------------
     */
    // xyzw-coordinate system variables (vectors)
    final public static int X_COORD = 0;
    final public static int Y_COORD = 1;
    final public static int Z_COORD = 2;
    final public static int W_COORD = 3;
    
    // rgba-coordinate system variables (colors)
    final public static int R_COORD = 0;
    final public static int G_COORD = 1;
    final public static int B_COORD = 2;
    final public static int A_COORD = 3;
    
    // stpq-coordinate system variables (texture)
    final public static int S_COORD = 0;
    final public static int T_COORD = 1;
    final public static int P_COORD = 2;
    final public static int Q_COORD = 3;
    
    
    /* -------------------------------------------------------------------------
     * Constructor
     * -------------------------------------------------------------------------
     */
    public Vec() {
        super();
    }
    
    public Vec(double value) {
        super(value);
    }
    
    public Vec(Vec vec) {
        super((Mat) vec);
    }
    
    public Vec(double... values) {
        super(values);
    }
    
    public Vec(boolean cloneVec, double... values) {
        super(cloneVec, values);
    }
    
    /* -------------------------------------------------------------------------
     * Public static methods
     * -------------------------------------------------------------------------
     */
    /* 
     * Computes the dot product of the two given vectors.
     * 
     * @param v1 the left vector of the dot product.
     * @param v2 the right vector of the dot product.
     * @return {@code v1 dot v2}.
     * @throws MatrixDimensionException iff either of the vectors is
     *     transposed, or when the number of rows are unequal.
     */
    public static double dot(Vec v1, Vec v2)
            throws MatrixDimensionException {
        // Bound checking
        if (v1.getNumCols() != 1 || v2.getNumCols() != 1)
            throw new MatrixDimensionException
                ("cannot take the dot product of a transposed vector. "
                     + "Expected: 1xn dot 1xn, but found: "
                     + v1.getNumRows() + "x" + v1.getNumCols() + " dot "
                     + v1.getNumRows() + "x" + v1.getNumCols() + ".");
        if (v1.getNumRows() != v2.getNumRows())
            throw new MatrixDimensionException
                ("Cannot take the dot product of two vectors of a different"
                     + "dimension. Expected: 1xn dot 1xn, but found: "
                     + v1.getNumRows() + "x" + v1.getNumCols() + " dot "
                     + v1.getNumRows() + "x" + v1.getNumCols() + ".");
        
        double[][] values_v1 = v1.getValues(false);
        double[][] values_v2 = v2.getValues(false);
        double result = 0;
        
        // Calculate the dot product of the two vectors.
        for (int i = 0; i < v1.getNumRows(); i++) {
            result += values_v1[i][0] * values_v2[i][0];
        }
        
        return result;
        // todo: remove
        //return multiply(transpose(v1, false), v2).getValues()[0][0];
    }
    
    /* 
     * Computes the cross vector of the two vectors.
     */
    public static Vec cross(Vec v1, Vec v2) {
        return new Vec(0);
    }
    
    /* 
     * Calculates the normal vector of a vector.
     * Returns the same vector when the length is 0.
     * Update denotes whether the vector should be updated.
     * Default is true.
     */
    public static Vec normalize(Vec v) {
        return normalize(v, true);
    }
    
    public static Vec normalize(Vec v, boolean update) {
        double length = v.length();
        
        // length == 0 is only possible for the vector (0, 0...0, 0),
        // which will not change when normalized.
        if (length == 0) return v;
        
        double[][] values = v.getValues();
        
        if (v.row == 1) { // || row == col == 1
            for (int i = 0; i < values.length; i++) {
                values[0][i] = values[0][i] / length;
            }
            
        } else { // col == 1
            for (int i = 0; i < values[0].length; i++) {
                values[i][0] = values[i][0] / length;
            }
        }
        
        if (update) {
            return (Vec) v.setMatrix(values);
            
        } else {
            // This is allowed since it is certain that row == 1 || col == 1.
            return (Vec) new Mat(values);
        }
    }
    
    /* 
     * Calculates the angle between two vectors
     */
    public static double cosAngle(Vec v1, Vec v2) {
        return v1.dot(v2) / (v1.length() * v2.length());
    }
    
    public static double sinAngle(Vec v1, Vec v2) {
        return ((Vec) multiply(1.0 / (v1.length() * v2.length()), cross(v1, v2))).length();
    }
    
    public static double angle(Vec v1, Vec v2) {
        return Math.acos(cosAngle(v1, v2));
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Public methods
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Normalizes this vector.
     * See the static method for more details.
     */
    public Vec normalize() {
        return normalize(this);
    }
    
    /* 
     * Calculates the square of the euclidian length.
     */
    public double lengthSqr() {
        double length = 0;
        
        if (col != 1) {
            for (int i = 0; i < values[0].length; i++) {
                length += values[0][i] * values[0][i];
            }
        } else {
            for (int i = 0; i < values.length; i++) {
                length += values[i][0] * values[i][0];
            }
        }
        
        return length;
    }
    
    /* 
     * Calculates the euclidian length of the vector.
     * Uses the function lengthSqrt.
     */
    public double length() {
        return Math.sqrt(lengthSqr());
    }
    
    public double dot(Vec v) {
        return dot(this, v);
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Get functions
     * ----------------------------------------------------------------------------------------------------------------
     * 
    // rgba-coordinate system variables (colors)
    final public static int R_COORD = 0;
    final public static int G_COORD = 1;
    final public static int B_COORD = 2;
    final public static int A_COORD = 3;
    
    // stpq-coordinate system variables (texture)
    final public static int S_COORD = 0;
    final public static int T_COORD = 1;
    final public static int P_COORD = 2;
     */
    /* 
     * Returns the coordinates of the vector in:
     * - vector notation  : xyzw
     * - color notation   : rgba
     * - texture notation : stpq
     */
    
    
    public double x() {
        return values[0][0];
    }
    public double r() {return x();}
    public double s() {return x();}
    
    /* 
     * Returns the y coordinate of the vector.
     */
    public double y() {
        if (row > col) {
            return values[1][0];
            
        } else {
            return values[0][1];
        }
    }
    
    /* 
     * Returns the z coordinate of the vector.
     */
    public double z() {
        if (row > col) {
            return values[2][0];
            
        } else {
            return values[0][2];
        }
    }
    
    /* 
     * Returns the w coordinate of the vector.
     */
    public double w() {
        if (row > col) {
            return values[3][0];
            
        } else {
            return values[0][3];
        }
    }
    /* 
     * Returns the coordinate represented by i.
     */
    public double getCoord(int i) {
        if (i < 0)
            throw new IllegalArgumentException("Index is not allowed to be < 0! Index found: " + i + ".");
        if (i >= row || i >= col)
            throw new IllegalArgumentException();
        
        if (row >= col) {
            return values[i][0];
            
        } else {
            return values[0][i];
        }
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
        if (row != 1 && col != 1)
            throw new MatrixDimensionException("Illegal change of matrix size. Found: " + row + "x" + col
                                                   + ", expected: 1xn or nx1.");
        return true;
    }
    
    public static void main(String[] args) {
        Vec v1 = new Vec(1, 1, 1);
        Vec v2 = new Vec(2, 3, 4);
        System.out.println(v1);
        System.out.println(v2);
        System.out.println(v1.dot(v2));
    }
}
