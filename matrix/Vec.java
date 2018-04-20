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

package tools.matrix;


// Tools imports
import tools.numbers.ToolNumber;


/* 
 * This class wraps the {@link tools.matrix.Mat} class in a vector format.
 */
public class Vec
        extends Mat {
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
     * Constructors
     * -------------------------------------------------------------------------
     */
    /* 
     * Creates a 1 dimensional vector.
     */
    public Vec() {
        super();
    }
    
    /* 
     * Creates a matrix with a given number of rows and columns.
     * Fills the matrix from left to right, top to down with the given values.
     * 
     * @param row the number of rows.
     * @param col the number of columns.
     * @param values the contents of the matrix.
     */
    public Vec(Number... values)
            throws MatrixDimensionException {
        super(values.length, 1, values);
    }
    
    public Vec(boolean cloneMat, Number... values)
            throws MatrixDimensionException {
        super(values.length, 1, cloneMat, values);
    }
    
    public Vec(ToolNumber... values)
            throws MatrixDimensionException {
        super(values.length, 1, values);
    }
    
    public Vec(boolean cloneMat, ToolNumber... values)
            throws MatrixDimensionException {
        super(values.length, 1, cloneMat, values);
    }
    
    /* 
     * Creates a vector object from another vector.
     * 
     * @param vec the vector to clone.
     */
    public Vec(Vec vec) {
        super(true, vec.values);
    }
    
    /* -------------------------------------------------------------------------
     * Functions
     * -------------------------------------------------------------------------
     */
    // todo
    
    
    public static Vec matToVec(Mat mat) {
        return matToVec(mat, true);
    }
    
    public static Vec matToVec(Mat mat, boolean clone) {
        return createDefaultInstance().setMatrix(mat.getValues(clone));
    }
    
    
    /* -------------------------------------------------------------------------
     * Overrides from Mat
     * -------------------------------------------------------------------------
     */
    @Override
    public void matrixDimCheck(int row, int col)
            throws MatrixDimensionException {
        if (row != 1 && col != 1) {
            throw new MatrixDimensionException
                ("Expected a vector with dimension 1xn or nx1, but found: "
                     + row + "x" + col);
        }
    }
    
    @SuppressWarnings("unchecked") // <Vec> instanceof Mat.
    public static <M extends Mat> M createDefaultInstance() {
        return (M) new Vec();
    }
    
    public void test() { }
    
    // tmp
    public static void main(String[] arg) {
        Vec vec = new Vec(1, 1);
        vec.calcMinor(0, 0, new Vec()).test();
    }
    
}