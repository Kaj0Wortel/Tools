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
import tools.numbers.PrimitiveNumber;
import tools.numbers.ToolNumber;


/**
 * This class extends the {@link tools.matrix.Mat} class to a vector format.
 */
public class Vec
        extends Mat {
    /**-------------------------------------------------------------------------
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
    
    
    /**-------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a 1 dimensional vector.
     */
    public Vec() {
        super();
    }
    
    /**
     * Creates an 1xn vector with the given (tool)numbers.
     * 
     * @param cloneMat whether to clone the given array. Default is true.
     * @param values the numbers in the new vector.
     */
    public Vec(Number... values) {
        super(values.length, 1, values);
    }
    
    public Vec(boolean cloneMat, Number... values) {
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
    
    /**
     * Clone constructor.
     * Creates a vector object from another vector.
     * 
     * @param vec the vector to clone.
     */
    public Vec(Vec vec) {
        super(true, vec.values);
    }
    
    /**-------------------------------------------------------------------------
     * Static functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Converts a {@code Mat} to a {@code Vec} by creating a new instance
     * and setting the matrix.
     * 
     * @param mat the matrix to be converted.
     * @param clone whether to clone the values of mat.
     * @return a {@code Vec} object, containing equal values as {@code mat}.
     * @throws MatrixDimensionException iff
     *     {@code mat} has not the correct format for a vector.
     */
    public static Vec matToVec(Mat mat)
            throws MatrixDimensionException {
        return matToVec(mat, true);
    }
    
    public static Vec matToVec(Mat mat, boolean clone)
            throws MatrixDimensionException {
        return createDefaultInstance().setMatrix(mat.getValues(clone));
    }
    
    /**-------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Returns a new normalized vector from the current vector.
     * 
     * @param result will contain the result of the operation.
     * @return a new normalized vector of {@code this}.
     * @throws UnsupportedOperationException iff
     *     {@link ToolNumber#sqrt()} is not supported.
     */
    public <V extends Vec> V normalize()
            throws UnsupportedOperationException {
        return normalize(null);
    }
    
    public <V extends Vec> V normalize(V result)
            throws UnsupportedOperationException {
        // Check if the vector is correct.
        matrixDimCheck(getNumRows(), getNumCols());
        
        // If no result given, create a default one.
        if (result == null) {
            result = createDefaultInstance();
        }
        
        // size == 0 is only possible for the 0 vector,
        // which will not change when normalized.
        if (size() == 0) {
            return result.setMatrix(new ToolNumber[0][0], false);
        }
        
        // Clone the current matrix to the result.
        if (result != this) result.setMatrix(getValues(true));
        // To prevent recalculating the length.
        ToolNumber length = length();
        
        // Divide each result by the total length.
        for (int i = 0; i < size(); i++) {
            result.divi(length);
        }
        
        return result;
    }
    /**
     * Creats a new vector containing the result of the dot-product of
     * {@code this} and {@code vec}.
     * Computes the dot product of the two given vectors.
     * 
     * @param vec the right vector of the dot product.
     * @return {@code this dot vec}.
     * @throws MatrixDimensionException iff either of the vectors is
     *     transposed, or when the number of rows are unequal.
     */
    public ToolNumber dot(Vec vec)
            throws MatrixDimensionException {
        // Bound checking
        if (this.getNumCols() != 1 || vec.getNumCols() != 1)
            throw new MatrixDimensionException
        ("cannot take the dot product of a transposed vector. "
                + "Expected: 1xn dot 1xn, but found: "
                + this.getNumRows() + "x" + this.getNumCols() + " dot "
                + this.getNumRows() + "x" + this.getNumCols() + ".");
        if (this.getNumRows() != vec.getNumRows())
            throw new MatrixDimensionException
        ("Cannot take the dot product of two vectors of a different"
                + "dimension. Expected: 1xn dot 1xn, but found: "
                + this.getNumRows() + "x" + this.getNumCols() + " dot "
                + this.getNumRows() + "x" + this.getNumCols() + ".");
        
        // Calculate the dot product of the two vectors.
        ToolNumber result = null;
        for (int i = 0; i < this.getNumRows(); i++) {
            if (result == null) result = this.get(i).mul(vec.get(i));
            else result.addi(this.get(i).mul(vec.get(i)));
        }
        
        return result;
    }
    
    public Vec cross(Vec vec) {
        // Bound checking
        if (this.getNumCols() != 1 || vec.getNumCols() != 1)
            throw new MatrixDimensionException
        ("Cannot take the dot product of two vectors of a different dimension. "
                + "Expected: 1x3 cross 1x3 or 1x7 cross 1x7, but found: "
                + this.getNumRows() + "x" + this.getNumCols() + " cross "
                + this.getNumRows() + "x" + this.getNumCols() + ".");
        if (this.getNumRows() != vec.getNumRows() ||
                (this.getNumRows() != 3 && this.getNumRows() != 7))
            throw new MatrixDimensionException
        ("Cannot take the dot product of two vectors of a different dimension. "
                + "Expected: 1x3 cross 1x3 or 1x7 cross 1x7, but found: "
                + this.getNumRows() + "x" + this.getNumCols() + " cross "
                + this.getNumRows() + "x" + this.getNumCols() + ".");
        if (this.getNumRows() == 3) {
            return new Vec(
                    get(1).mul(vec.get(2)).subi(get(2).mul(vec.get(1))),
                    get(2).mul(vec.get(0)).subi(get(0).mul(vec.get(2))),
                    get(0).mul(vec.get(1)).subi(get(1).mul(vec.get(0)))
            );
            
        } else {
            ToolNumber[] num = new ToolNumber[7];
            for (int i = 0; i < 7; i++) {
                num[i] = get((i+1)%7).mul(vec.get((i+3)%7))
                        .subi(get((i+3)%7).mul(vec.get((i+1)%7)))
                        .addi(get((i+2)%7).mul(vec.get((i+6)%7)))
                        .subi(get((i+6)%7).mul(vec.get((i+2)%7)))
                        .addi(get((i+4)%7).mul(vec.get((i+5)%7)))
                        .subi(get((i+5)%7).mul(vec.get((i+4)%7)));
            }
            
            return new Vec(false, num);
        }
    }
    
    
    /**
     * Calculates the angle between two vectors.
     * 
     * @param vec the vector between which the angle will be calculated.
     * @return the cosine of the angle, the sine of the angle and
     *     the angle in radians.
     * @throws UnsupportedOperationException iff
     *     {@link ToolNumber#sqrt()} is not supported.
     */
    public ToolNumber cosAngle(Vec vec)
            throws UnsupportedOperationException {
        return this.dot(vec).muli(this.length().muli(vec.length()));
    }
    
    public ToolNumber sinAngle(Vec vec)
            throws UnsupportedOperationException {
        return ((Vec) this.cross(vec).divi(this.length().muli(vec.length())))
                .length();
    }
    
    public double angle(Vec vec)
            throws UnsupportedOperationException {
        return Math.acos(cosAngle(vec).doubleValue());
    }
    
    
    /**-------------------------------------------------------------------------
     * Get functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Returns the coordinates of the vector in:
     *     - vector notation  : xyzw
     *     - color notation   : rgba
     *     - texture notation : stpq
     */
    /**
     * @return the x/r/s coordinate of the vector
     */
    public ToolNumber x() {
        return get(0);
    }
    
    public ToolNumber r() {
        return x();
    }
    
    public ToolNumber s() {
        return x();
    }
    
    /**
     * @return the y/g/t coordinate of the vector.
     */
    public ToolNumber y() {
        return get(1);
    }
    
    public ToolNumber g() {
        return y();
    }
    
    public ToolNumber t() {
        return y();
    }
    
    /**
     * @return the z/b/q coordinate of the vector.
     */
    public ToolNumber z() {
        return get(2);
    }
    
    public ToolNumber b() {
        return z();
    }
    
    public ToolNumber p() {
        return z();
    }
    
    /**
     * @return the w/a/q coordinate of the vector.
     */
    public ToolNumber w() {
        return get(3);
    }
    
    public ToolNumber a() {
        return w();
    }
    
    public ToolNumber q() {
        return w();
    }
    
    /**
     * @param i the index of the value to be returned.
     * @return the value at {@code i}.
     */
    public ToolNumber get(int i)
            throws IllegalArgumentException {
        if (i < 0)
            throw new IllegalArgumentException
            ("Expected an index < 0, but found: " + i);
        
        
        if (getNumRows() == 1) {
            if (i >= getNumCols())
                throw new IllegalArgumentException
                ("Index is larger then maximum vector size.");
            
            return values[0][i];
            
        } else { // getNumCols() == 1
            if (i >= getNumRows())
                throw new IllegalArgumentException
                ("Index is larger then maximum vector size.");
            
            return values[i][0];
            
        }
    }
    
    /**
     * @return the size of the vector (number of elements).
     */
    public int size() {
        matrixDimCheck(getNumRows(), getNumCols());
        
        if (getNumRows() == 1) {
            return getNumCols();
            
        } else { // getNumCols() == 1
            return getNumRows();
        }
    }
    
    /**
     * @return the addition of the square of each coord in the vector.
     */
    public ToolNumber lengthSquare() {
        if (size() == 0) return new PrimitiveNumber<Double>(0.0);
        
        ToolNumber result = get(0).mul(get(0));
        for (int i = 1; i < size(); i++) {
            result.muli(get(i)).muli(get(i));
        }
        
        return result;
    }
    
    /**
     * @return the length of the vector in n-dimensional space.
     * @throws UnsupportedOperationException iff
     *     {@code ToolNumber#sqrt()} is not supported.
     */
    public ToolNumber length()
            throws UnsupportedOperationException {
        return lengthSquare().sqrt();
    }
    
    
    /**-------------------------------------------------------------------------
     * Overrides from Mat.
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
    
    @Deprecated
    public static <M extends Mat> M createDefaultInstance() {
        return (M) new Vec();
    }
    
    
}