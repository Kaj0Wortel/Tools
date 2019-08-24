/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2018 by Kaj Wortel - all rights reserved                    *
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


// Tools imports
import java.util.Objects;
import tools.MultiTool;
import tools.numbers.PrimitiveNumber;
import tools.numbers.ToolNumber;
import tools.numbers.ToolNumberAdapter;
import tools.observer.HashObservableInterface;
import tools.PublicCloneable;


/**
 * Class for matrix operations.
 * 
 * @version 0.0
 * @author Kaj Wortel
 * 
 * @deprecated Old class, and needs to be refactored.
 */
@Deprecated
public class Mat
        extends ToolNumberAdapter<Mat>
        implements PublicCloneable, HashObservableInterface {
    
    protected ToolNumber[][] values;
    
    /**-------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a 1x1 matrix only containing {@code 0.0}.
     */
    public Mat() {
        this(0.0);
    }
    
    /**
     * Creates a 1x1 matrix only containing the given number.
     * 
     * @param num the sole value in the matrix.
     * @throws IllegalArgumentException iff
     *     {@code num} is not a primative number data type class.
     */
    public Mat(Number num) {
        this((PrimitiveNumber<?>) PrimitiveNumber.toPrimNumber(num), false);
    }
    
    /**
     * Creates a 1x1 matrix only containing the given ToolNumber.
     * 
     * @param value the sole value in the matrix.
     * @param cloneMat whether to clone the value.
     *     Default is true.
     */
    public Mat(ToolNumber value) {
        this(value, true);
    }
    
    public Mat(ToolNumber value, boolean cloneMat) {
        setMatrix(new ToolNumber[][] {
            new ToolNumber[] {
                value
            }
        }, cloneMat);
    }
    
    /**
     * Creates a matrix from the given numbers.
     */
    public Mat(Number[]... values) {
        this(true, (PrimitiveNumber[][]) PrimitiveNumber.toPrimNumber(values));
    }
        
    /**
     * Creates a matrix object of the given matrix.
     * 
     * @param values the given matrix.
     * @param cloneMat whether to clone the given matrix.
     *     Default is true.
     */
    public Mat(ToolNumber[]... values)
            throws MatrixDimensionException {
        this(true, values);
    }
    
    public Mat(boolean cloneMat, ToolNumber[]... values)
            throws MatrixDimensionException {
        setMatrix(values, cloneMat);
    }
    
    /**
     * Creates a matrix with a given number of rows and columns.
     * Fills the matrix from left to right, top to down with the given values.
     * 
     * @param row the number of rows.
     * @param col the number of columns.
     * @param cloneMat whether to clone the matrix.
     * @param values the contents of the matrix.
     */
    public Mat(int row, int col, Number... values)
            throws MatrixDimensionException {
        this(row, col, true,
             (PrimitiveNumber[]) PrimitiveNumber.toPrimNumber(values));
    }
    
    public Mat(int row, int col, boolean cloneMat, Number... values)
            throws MatrixDimensionException {
        this(row, col, cloneMat,
             (PrimitiveNumber[]) PrimitiveNumber.toPrimNumber(values));
    }
    
    public Mat(int row, int col, ToolNumber... values)
            throws MatrixDimensionException {
        this(row, col, true,
             (PrimitiveNumber[]) PrimitiveNumber.toPrimNumber(values));
    }
    
    public Mat(int row, int col, boolean cloneMat, ToolNumber... values)
            throws MatrixDimensionException {
        if (values.length != row * col)
            throw new MatrixDimensionException
                ("Given 1D matrix has an invallid length. Expected: "
                     + (row*col) + ", found: " + values.length + ".");
        ToolNumber[][] newValues = new ToolNumber[row][col];
        
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                newValues[i][j] = (cloneMat
                                       ? values[i*col + j].clone()
                                       : values[i*col + j]);
            }
        }
        
        setMatrix(newValues, false);
    }
    
    /**
     * Creates a matrix from 2 other matrices, where the two matrices
     * are placed such that the lower right corner of {@code mat_1} hits the
     * upper left corner of {@code mat_2}. The values on the right of
     * {@code mat_1} and the left of {@code mat_2} are filled with resp.
     * {@code fillValue_1} and {@code fillValue_2}.
     * The matrix will look like this:
     * [[A, B]
     *  [C, D]]
     * with {@code A = mat_1, B = fillValue_1, C = fillValue_2, D = mat_2}.
     * 
     * @param mat_1 the upper left part matrix.
     * @param fillValue_1 the value to be filled for the upper right part
     *     of the matrix.
     * @param fillValue_2 the value to be filled for the lower left part
     *     of the matrix.
     * @param mat_2 the lower right part matrix.
     */
    public Mat(Mat mat_1, Number fillValue_1, Number fillValue_2, Mat mat_2) {
        this(mat_1, (PrimitiveNumber) PrimitiveNumber
                 .toPrimNumber(fillValue_1),
             (PrimitiveNumber) PrimitiveNumber
                 .toPrimNumber(fillValue_2), mat_2);
    }
    
    public Mat(Mat mat_1, ToolNumber fillValue_1, ToolNumber fillValue_2, 
               Mat mat_2) {
        int rowsMat_1 = mat_1.getNumRows();
        int rowsMat_2 = mat_2.getNumRows();
        int colsMat_1 = mat_1.getNumCols();
        int colsMat_2 = mat_2.getNumCols();
        
        int row = rowsMat_1 + rowsMat_2;
        int col = colsMat_1 + colsMat_2;
        ToolNumber[][] newValues = new ToolNumber[row][col];
        
        ToolNumber[][] valuesMat_1 = mat_1.getValues();
        ToolNumber[][] valuesMat_2 = mat_2.getValues();
        
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (i < rowsMat_1) {
                    // Upper left part: copy mat_1
                    if (j < colsMat_1) {
                        newValues[i][j] = valuesMat_1[i][j].clone();
                        
                        // Upper right part: use fillValue_1
                    } else {
                        newValues[i][j] = fillValue_1.clone();
                    }
                    
                } else {
                    // Lower left part: copy mat_1
                    if (j < colsMat_1) {
                        newValues[i][j] = fillValue_2.clone();
                        
                        // Lower right part: use fillValue_1
                    } else {
                        newValues[i][j]
                            = valuesMat_2[i - rowsMat_1][j - colsMat_1].clone();
                    }
                }
            }
        }
        
        setMatrix(newValues, false);
    }
    
    /**
     * Creates a matrix object from another matrix.
     * 
     * @param mat the matrix to clone.
     */
    public Mat(Mat mat) {
        this(true, mat.values);
    }
    
    
    /**-------------------------------------------------------------------------
     * Static functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates an identity matrix with the dimensions (rows x cols).
     * 
     * @param rows the number of rows of the new I matrix.
     * @param cols the number of columns of the new I matrix.
     * @param result the matrix in which the result will be stored.
     *     Default is {@code null}.
     * @return {@code result}.
     */
    public static <M extends Mat> M createIMat(int rows, int cols) {
        return createIMat(rows, cols, null);
    }
    
    public static <M extends Mat> M createIMat(int rows, int cols, M result) {
        ToolNumber[][] matIValues = new ToolNumber[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == j) {
                    matIValues[i][j] = new PrimitiveNumber<Double>(1.0);
                    
                } else {
                    matIValues[i][j] = new PrimitiveNumber<Double>(0.0);
                }
            }
        }
        
        // Set the matrix to the result and return.
        if (result == null) {
            result = M.createDefaultInstance();
        }
        
        return result.setMatrix(matIValues, false);
    }
    
    /**
     * Multiplies all matrices, assuming the order left to right.
     * (so mat[begin] * mat[begin+1] * ... * mat[end-1]).
     * 
     * @param begin the first index of {@code mats} that will be used in
     *     the multiplication (inclusive). Default is 0.
     * @param end the last index of {@code mats} that will be used in the
     *     multiplication (exclusive). default is mats.length.
     * @param result matrix that will contain the result of the operation.
     *     Default is {@code null}.
     * @param mats the matrices to be multiplied.
     * 
     * @return {@code result}.
     * 
     * @throws NullPointerException iff {@code mats == null}.
     * @throws IllegalArgumentException iff
     *     {@code 0 <= begin < end <= mats.length} does not hold.
     * @throws MatrixDimensionException iff any of the matrix multiplications
     *     is not allowed due to incorrect matrix dimensions.
     */
    public static <M extends Mat> M multiplyAll(Mat[] mats)
            throws IllegalArgumentException, MatrixDimensionException {
        return multiplyAll(null, mats);
    }
    
    public static <M extends Mat> M multiplyAll(int begin, int end, Mat[] mats)
            throws IllegalArgumentException, MatrixDimensionException {
        return multiplyAll(begin, end, null, mats);
    }
    
    public static <M extends Mat> M multiplyAll(M result, Mat[] mats)
            throws IllegalArgumentException, MatrixDimensionException {
        return multiplyAll(0, mats.length, result, mats);
    }
    
    public static <M extends Mat> M multiplyAll(int begin, int end,
                                                M result, Mat[] mats)
            throws IllegalArgumentException, MatrixDimensionException {
        if (mats == null) throw new NullPointerException
                ("mats is not allowed to be null!");
        if (mats.length == 0) throw new IllegalArgumentException
                ("There must be at least 1 matrix to be multiplied!");
        
        if (begin < 0) throw new IllegalArgumentException
                ("begin(" + begin + ") < 0");
        if (end > mats.length) throw new IllegalArgumentException
                ("end(" + begin + ") > mats.length");
        if (begin >= end) throw new IllegalArgumentException
                ("begin(" + begin + ") >= end(" + end + ")");
        
        if (result == null) {
            result = mats[begin].mul(1);
            
        } else {
            result.setMatrix(mats[begin].getValues(true), false);
        }
        
        // Multiply all matrices in order with the result.
        for (int i = begin + 1; i < end; i++) {
            result.muli(mats[i]);
        }
        
        // Return the result.
        return result;
    }
    
    /* todo
     * @param matLeft the left matrix of the inproduct.
     * @param matRight the right matrix of the inproduct.
     * @param result the result of the operatrion.
     *     Default is null.
     * @return the inproduct of {@code matLeft} with {@code matRight}.
     * @throws MatrixDimensionException iff
     *     the given matrices have invallid dimensions.
     *//*
    public static <M extends Mat> M inproduct(Mat matLeft, Mat matRight)
            throws MatrixDimensionException {
        return inproduct(matLeft, matRight, null);
    }
    
    public static <M extends Mat> M inproduct(Mat matLeft, Mat matRight,
                                              M result)
            throws MatrixDimensionException {
        return multiply(matLeft.transpose(), matRight, result);
    }
    
    
    /**-------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Sets the values of the matrix.
     * 
     * @param num the new values of the matrix.
     * @return {@code this}.
     * @throws NullPointerException iff {@code mat == null} or
     *     any {@code mat[i] == null}.
     * @throws IllegalArgumentException iff
     *     {@code mat == null || mat[i] == null}.
     * @throws MatrixDimensionException iff {@code mat.length == 0}.
     * 
     * Converts each value of {@code} to a {@code PrimitiveNumber}.
     * Then uses {@link setMatrix(ToolNumber[][])} for setting the matrix.
     */
    public <M extends Mat> M setMatrix(Number[][] num) {
        return setMatrix((ToolNumber[][]) PrimitiveNumber.toPrimNumber(num),
                         false);
    }
    
    /**
     * Sets the values of the matrix.
     * 
     * @param mat the new matrix.
     * @param cloneMat whether to clone the given matrix. Default is true.
     * @return {@code this}.
     * @throws NullPointerException iff {@code mat == null} or
     *     any {@code mat[i] == null}.
     * @throws IllegalArgumentException iff
     *     {@code mat == null || mat[i] == null}.
     * @throws MatrixDimensionException iff {@code mat.length == 0}.
     */
    public <M extends Mat> M setMatrix(ToolNumber[][] num) {
        return setMatrix(num, true);
    }
    
    public <M extends Mat> M setMatrix(ToolNumber[][] num, boolean cloneMat) 
            throws NullPointerException, MatrixDimensionException {
        // Check for null
        if (num == null)
            throw new IllegalArgumentException
                ("Given matrix is not allowed to be null.");
        // Check for emty size
        if (num.length == 0)
            throw new MatrixDimensionException
                ("Invallid matrix dimension: length = 0");
        
        // Determine the new row and col
        int rows = num.length;
        int cols = num[0].length;
        
        // Check for null and equal column length
        for (int i = 0; i < num.length; i++) {
            if (num[i] == null)
                throw new IllegalArgumentException
                    ("Given matrix is not allowed to be null.");
            if (num[i].length != cols)
                throw new MatrixDimensionException
                    ("Invallid column size(" + num[i].length
                         + ") at row: " + i);
        }
        
        // Might throw exceptions for invallid dimensions.
        matrixDimCheck(rows, cols);
        
        if (!cloneMat) {
            // If the matrix should not be cloned, simply replace
            // the values variable.
            this.values = num;
            setChanged();
            
        } else {
            // If the matrix should be cloned, copy all values.
            values = new ToolNumber[rows][cols];
            
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    values[i][j] = num[i][j].clone();
                }
            }
        }
        
        setChanged();
        
        // Notify the observers that {@code values} might have been changed.
        // Note that {@code values} is not modified when they have the same
        // dimensions and contain the same values.
        notifyObservers(values);
        
        return (M) this;
    }
    
    /**
     * Transposes this matrix.
     * 
     * @param result matrix that will contain the result of the operation.
     *     Default is {@code this}.
     * @param clone whether to clone the values of the new matrix.
     *     Default if no result matrix is given is false.
     *     Default if a result matrix is given is true.
     * @throws NullPointerException iff {@code mat == null}.
     * @return {@code result}.
     */
    public <M extends Mat> M transpose() {
        return transpose((M) this, false);
    }
    
    public <M extends Mat> M transpose(boolean clone) {
        return transpose((M) this, clone);
    }
    
    public <M extends Mat> M transpose(M result) {
        return transpose(result, true);
    }
    
    public <M extends Mat> M transpose(M result, boolean clone) {
        ToolNumber[][] newValues
            = new ToolNumber[values[0].length][values.length];
        
        for (int row = 0; row < values.length; row++) {
            for (int col = 0; col < values[0].length; col++) {
                newValues[col][row] = (clone
                                           ? values[row][col].clone()
                                           : values[row][col]);
            }
        }
        
        // Set the matrix to the result and return.
        if (result == null) {
            result = createDefaultInstance();
        }
        
        return result.setMatrix(newValues, false);
    }
    
    /**
     * Calculates the cofactor matrix of the current matrix.
     * 
     * @param result the matrix in which the result will be stored.
     *     Default is null. If null, the default matrix is used.
     * @return the cofactor matrix.
     * @throws MatrixDimensionException iff the matrix is not square.
     * 
     * Calculation:
     * Cof_[i, j](mat) = det(M_[mat, i, j]) * (-1)^(i+j)
     * where:
     * - Cof_[i, j] = cofactor at (i, j);
     * - mat = the matrix to be calculated.
     * - det(x) = calculate the determinant of x.
     * - M_[m, i, j] = the minor of matrix m at (i, j).
     */
    public <M extends Mat> M calcCoMatrix() {
        return calcCoMatrix(null);
    }
    
    public <M extends Mat> M calcCoMatrix(M result) {
        if (!isSquare()) {
            throw new MatrixDimensionException
                ("Cofactor matrix is undefined for non-square matrices. "
                     + "Expected: nxn with n > 1, "
                     + "but found: " + getNumRows() + "x" + getNumCols() + ".");
        }
        
        ToolNumber[][] newValues
            = new ToolNumber[getNumRows()][getNumCols()];
        
        for (int row = 0; row < getNumRows(); row++) {
            for (int col = 0; col < getNumRows(); col++) {
                newValues[row][col] = calcMinor(row, col).det();
                if ((row + col) % 2 == 1) {
                    newValues[row][col].muli(-1);
                }
            }
        }
        
        // Set the matrix to the result and return.
        if (result == null) {
            result = createDefaultInstance();
        }
        
        return result.setMatrix(newValues, false);
    }
    
    /**
     * Calculates the transpose cofactor matrix of the current matrix.
     * 
     * @param result the matrix in which the result will be stored.
     *     Default is null. If null, the default matrix is used.
     * @return the cofactor matrix.
     * @throws MatrixDimensionException iff the matrix is not square.
     * 
     * For calculation, see {@link calcCoMatrix()}.
     */
    public Mat calcTransCoMatrix() {
        return calcTransCoMatrix(null);
    }
    
    public Mat calcTransCoMatrix(Mat result) {
        if (!isSquare()) {
            throw new MatrixDimensionException
                ("Cofactor matrix is undefined for non-square matrices. "
                     + "Expected: nxn with n > 1, "
                     + "but found: " + getNumRows() + "x" + getNumCols() + ".");
        }
        
        ToolNumber[][] newValues
            = new ToolNumber[getNumRows()][getNumCols()];
        
        for (int row = 0; row < getNumRows(); row++) {
            for (int col = 0; col < getNumRows(); col++) {
                newValues[col][row] = calcMinor(row, col).det();
                if ((row + col) % 2 == 1) {
                    newValues[col][row].muli(-1);
                }
            }
        }
        
        // Set the matrix to the result and return.
        if (result == null) {
            result = createDefaultInstance();
        }
        
        return result.setMatrix(newValues, false);
    }
    
    /**
     * Calculates the minor matrix at (row, col) of the current matrix.
     * 
     * @param row the row to ignore for the minor.
     * @param col the column to ignore for the minor.
     * @param result the matrix used for storing the result.
     * @param clone whether to clone the values.
     * @return the minor of the current matrix at (row, col).
     */
    public <M extends Mat> M calcMinor(int row, int col) {
        return calcMinor(row, col, null, true);
    }
    
    public <M extends Mat> M calcMinor(int row, int col, M result) {
        return calcMinor(row, col, result, true);
    }
    
    public <M extends Mat> M calcMinor(int row, int col, boolean clone) {
        return calcMinor(row, col, null, clone);
    }
    
    public <M extends Mat> M calcMinor(int row, int col, M result,
                                       boolean clone) {
        ToolNumber[][] newValues
            = new ToolNumber[getNumRows() - 1][getNumCols() - 1];
        
        for (int i = 0; i < getNumRows(); i++) {
            for (int j = 0; j < getNumCols(); j++) {
                if (i == row || j == col) continue;
                
                int newRow = i - (i < row ? 0 : 1);
                int newCol = j - (j < col ? 0 : 1);
                newValues[newRow][newCol] = (clone
                                                 ? values[i][j].clone()
                                                 : values[i][j]);
            }
        }
        
        // Set the matrix to the result and return.
        if (result == null) {
            result = createDefaultInstance();
        }
        
        return result.setMatrix(newValues, false);
    }
    
    /**
     * @result the determinant of the current matrix.
     * @throws MatrixDimensionException iff the matrix is not square.
     * 
     * Calculation:
     * det(mat)[i][j] = mat[i][j] * det(M_[mat, i, j])
     * where:
     *  - det(m) = the determinant of matrix m.
     *  - mat = the current matrix.
     *  - M_[m, i, j] = the minor matrix at (i, j).
     */
    public ToolNumber det() {
        int rows = getNumRows();
        int cols = getNumCols();
        
        if (!isSquare()) {
            throw new MatrixDimensionException
                ("Determinant is undefined for non-square matrices. "
                     + "Expected: nxn with n >= 1, "
                     + "but found: " + rows + "x" + cols + ".");
        }
        
        if (rows == 1) {
            return values[0][0];
            
        } else if (rows == 2) {
            return values[0][0].mul(values[1][1])
                .sub(values[0][1].mul(values[1][0]));
            
        } else if (rows == 3) {
            return values[0][0].mul(values[1][1]).mul(values[2][2])
                .add(values[0][1].mul(values[1][2]).mul(values[2][0]))
                .add(values[0][2].mul(values[1][0]).mul(values[2][1]))
                .sub(values[0][2].mul(values[1][1]).mul(values[2][0]))
                .sub(values[0][1].mul(values[1][0]).mul(values[2][2]))
                .sub(values[0][0].mul(values[1][2]).mul(values[2][1]));
            
        } else { // rows > 3
            ToolNumber result = new PrimitiveNumber<Double>(0.0);
            
            for (int i = 0; i < cols; i++) {
                // Create the minor matrix at the point row=0, col=i.
                Mat minor = this.calcMinor(0, i, false);
                
                // Recursivly determine the determinant.
                ToolNumber detResult = values[0][i].mul(minor.det());
                if (i/2 == i/2.0) {
                    result.addi(detResult);
                    
                } else {
                    result.subi(detResult);
                }
            }
            
            return result;
        }
    }
    
    /**
     * Converts the current matrix to a matrix compatible with the gl shader.
     * 
     * @return linear matrix compatible with the gl shader.
     */
    public double[] toGLMatrix() {
        double[] result = new double[values.length * values[0].length];
        
        for (int i = 0; i < getNumRows(); i++) {
            for (int j = 0; j < getNumCols(); j++) {
                // Note the transpose here
                result[i*getNumCols() + j] = values[j][i].doubleValue();
            }
        }
        
        return result;
    }
    
    /**
     * @param result the result of the operation.
     *     Default is null.
     * @return the identity matrix that has the same dimension as this matrix.
     */
    public <M extends Mat> M createIMat() {
        return createIMat(values.length, values[0].length);
    }
    
    public <M extends Mat> M createIMat(M result) {
        return createIMat(values.length, values[0].length, result);
    }
    
    /**
     * Resets the matrix to to all 0 values.
     * Obtains the result by subtracting all values from eachother.
     */
    public void clear() {
        for (int row = 0; row < getNumRows(); row++) {
            for (int col = 0; col < getNumCols(); col++) {
                values[row][col].subi(values[row][col]);
            }
        }
    }
    
    /**
     * Gets the values of the matrix.
     * 
     * @param cloneMat whether the values should be cloned
     *     before being returned. Default is true.
     * @return the values of the matrix.
     */
    public ToolNumber[][] getValues() {
        return getValues(true);
    }
    
    public ToolNumber[][] getValues(boolean cloneMat) {
        if (cloneMat) {
            return MultiTool.deepArrayClone(values);
            
        } else {
            return values;
        }
    }
    
    /**
     * @return the number of rows the matrix has.
     */
    public int getNumRows() {
        return values.length;
    }
    
    /**
     * @return the number of columns the matrix has.
     */
    public int getNumCols() {
        return values[0].length;
    }
    
    /**
     * @return whether the current matrix is square.
     */
    public boolean isSquare() {
        return values.length == values[0].length;
    }
    
    /**
     * Adds the values of {@code rowFrom} to {@code rowTo}.
     * 
     * @param rowFrom the row used for adding.
     * @param rowTo the destination row.
     * @param scalar the factor which the from-row is pre-multiplied with
     *     before being added to the to-row.
     * @param clone denotes whether to do the operation in-place or return
     *     a clone.
     * @return a matrix where the from-row was added to the to-row.
     * @throws IllegalArgumentException iff
     *     {@code rowFrom < 0 || rowFrom >= #rows ||
     *         rowTo < 0 || rowTo >= #rows}.
     * 
     * Note that {@code rowFrom} and {@code rowTo} denote the indeces
     * of the matrix.
     */
    protected <M extends Mat> M addRowToRow(int rowFrom, int rowTo,
                                            Number scalar, boolean clone) {
        if (rowFrom < 0 || rowFrom >= getNumRows())
            throw new IllegalArgumentException
            ("From-row exceeds matrix boundries!");
        if (rowTo < 0 || rowTo >= getNumRows())
            throw new IllegalArgumentException
            ("From-row exceeds matrix boundries!");
        
        // Get the result
        ToolNumber[][] result = getValues(clone);
        
        for (int i = 0; i < getNumCols(); i++) {
            result[rowTo][i].addi(values[rowFrom][i].mul(scalar));
        }
        
        return clone
            ? createDefaultInstance().setMatrix(result, false)
            : (M) this;
    }
    
    public <M extends Mat> M addRowToRow(int rowFrom, int rowTo,
                                         Number scalar) {
        return addRowToRow(rowFrom, rowTo, scalar, true);
    }
    
    public <M extends Mat> M addRowToRowi(int rowFrom, int rowTo,
                                          Number scalar) {
        return addRowToRow(rowFrom, rowTo, scalar, false);
    }
    
    /**
     * Adds the values of {@code colFrom} to {@code colTo}.
     * 
     * @param colFrom the Col used for adding.
     * @param colTo the destination Col.
     * @param scalar the factor which the from-col is pre-multiplied with
     *     before being added to the to-col.
     * @param clone denotes whether to do the operation in-place or return
     *     a clone.
     * @return a matrix where the from-col was added to the to-col.
     * @throws IllegalArgumentException iff
     *     {@code colFrom < 0 || colFrom >= #cols ||
     *         colTo < 0 || colTo >= #cols}.
     * 
     * Note that {@code colFrom} and {@code colTo} denote the indeces
     * of the matrix.
     */
    protected <M extends Mat> M addColToCol(int colFrom, int colTo,
                                            Number scalar, boolean clone) {
        if (colFrom < 0 || colFrom >= getNumCols())
            throw new IllegalArgumentException
            ("From-col exceeds matrix boundries!");
        if (colTo < 0 || colTo >= getNumCols())
            throw new IllegalArgumentException
            ("From-col exceeds matrix boundries!");
        
        ToolNumber[][] result = getValues(clone);
        
        for (int i = 0; i < getNumCols(); i++) {
            result[i][colTo].addi(values[i][colFrom].mul(scalar));
        }
        
        return clone
            ? createDefaultInstance().setMatrix(result, false)
            : (M) this;
    }
    
    public <M extends Mat> M addColToCol(int colFrom, int colTo,
                                         Number scalar) {
        return addColToCol(colFrom, colTo, scalar, true);
    }
    
    public <M extends Mat> M addColToColi(int colFrom, int colTo,
                                          Number scalar) {
        return addColToCol(colFrom, colTo, scalar, false);
    }
    
    /**
     * Multiplies the given row with a scalar.
     * 
     * @param row row to multiply.
     * @param scalar the used scalar.
     * @param clone denotes whether to do the operation in-place or return
     *     a clone.
     * @return a matrix where all values on the given row are multiplied with
     *     the given scalar.
     * @throws IllegalArgumentException iff {@code row < 0 || row >= #rows}.
     */
    public <M extends Mat> M mulRow(int row, Number scalar) {
        return mulRow(row, scalar, true);
    }
    
    public <M extends Mat> M mulRowi(int row, Number scalar) {
        return mulRow(row, scalar, false);
    }
    
    protected <M extends Mat> M mulRow(int row, Number scalar, boolean clone) {
        if (row < 0 || row >= getNumRows())
            throw new IllegalArgumentException
            ("Row exceeds matrix boundries!");
        ToolNumber[][] result = getValues(clone);
        
        for (int i = 0; i < getNumRows(); i++) {
            result[row][i].muli(scalar);
        }
        
        return clone
            ? createDefaultInstance().setMatrix(result, false)
            : (M) this;
    }
    
    /**
     * Multiplies the given col with a scalar.
     * 
     * @param col col to multiply.
     * @param scalar the used scalar.
     * @param clone denotes whether to do the operation in-place or return
     *     a clone.
     * @return a matrix where all values on the given col are multiplied with
     *     the given scalar.
     * @throws IllegalArgumentException iff {@code col < 0 || col >= #cols}.
     */
    public <M extends Mat> M mulCol(int col,  Number scalar) {
        return mulCol(col, scalar, true);
    }
    
    public <M extends Mat> M mulColi(int col, Number scalar) {
        return mulCol(col, scalar, false);
    }
    
    protected <M extends Mat> M mulCol(int col, Number scalar, boolean clone) {
        if (col < 0 || col >= getNumCols())
            throw new IllegalArgumentException
            ("Col exceeds matrix boundries!");
        
        ToolNumber[][] result = getValues(clone);
        
        for (int i = 0; i < getNumCols(); i++) {
            result[i][col].muli(scalar);
        }
        
        return clone
            ? createDefaultInstance().setMatrix(result, false)
            : (M) this;
    }
    
    
    /**-------------------------------------------------------------------------
     * Overrides from ToolNumber.
     * -------------------------------------------------------------------------
     */
    @Override
    public Number getValue() {
        if (getNumRows() == 1 && getNumCols() == 0) {
            return values[0][0].getValue();
            
        } else {
            throw new IllegalStateException
                ("Expected an 1x1 matrix, but found an " + getNumRows()
                     + "x" + getNumCols() + " matrix.");
        }
    }
    
    /**
     * @return the inverse of this matrix.
     * 
     * Calculation notes:
     * Calculates the inverse by adding rows of the matrix to other rows to
     * create the I matrix. Then do the exact same operations (in the same
     * order) for an I matrix. This matrix will then be the result.
     * 
     * Note that the types of the ToolNumbers used in {@code values} will
     * be converted to {@code Double}. To prevent this from happening,
     * use {@link inverseKeepType()}. Also note that this method is
     * ~2.4 times faster!
     */
    @Override
    public  <T extends ToolNumber> T inverse() {
        if (!isSquare()) {
            throw new MatrixDimensionException
                ("Cofactor matrix is undefined for non-square matrices. "
                     + "Expected: nxn with n >= 1, "
                     + "but found: " + getNumRows() + "x" + getNumCols() + ".");
        }
        
        Mat cloneMat = this.clone();
        ToolNumber[][] cloneValues = cloneMat.getValues(false);
        Mat resultMat = this.createIMat();
        
        for (int i = 0; i < getNumRows(); i++) {
            if (cloneValues[i][i].doubleValue() == 0.0) {
                /* If the value on the center line equals 0, find
                   another row where there is a value on that column and
                   add that row to the current row.
                   Note that j starts with {@code i+1} since the values above
                   already have a number != 0 on the columns on the left.*/
                boolean found = false;
                for (int j = i+1; j < getNumRows(); j++) {
                    if (cloneValues[j][i].doubleValue() != 0.0) {
                        ToolNumber divVal = cloneValues[j][i].divInv(1.0);
                        resultMat.addRowToRowi(j, i, divVal);
                        cloneMat.addRowToRowi(j, i, divVal);
                        
                        found = true;
                        break;
                    }
                }
                
                if (!found) throw new MatrixLinearDependancyException();
                
            } else {
                // If the value on the center line does not equal zero,
                // multiply the row with the inverse of that value.
                ToolNumber mulValue = cloneValues[i][i].inverse();
                resultMat.mulRowi(i, mulValue);
                cloneMat.mulRowi(i, mulValue);
            }
            
            // For every row, subtract the current row n times such that in
            // the end all values in the current column except that one of
            // the current row equal 0.
            for (int j = 0; j < getNumRows(); j++) {
                if (i == j) continue;
                ToolNumber mulValue = cloneValues[j][i].mul(-1.0);
                resultMat.addRowToRowi(i, j, mulValue);
                cloneMat.addRowToRowi(i, j, mulValue);
            }
        }
        
        return (T) resultMat;
    }
    
    /**
     * @return the inverse of this matrix.
     * 
     * Calculation notes:
     * inverse(mat) = CM_[mat]^T / det(mat)
     * where:
     *  - CM_[m] = the cofactor matrix of m.
     *  - m^T = transpose matrix of m.
     *  - det(m) = determinant of m.
     * 
     * Note that the type of the ToolNumbers used in {@code values} will remain
     * the same. To convert the default type to Double,
     * use {@link inverse()}. Also note that this method is ~2.4 times slower!
     */
    public <M extends Mat> M inverseKeepType() {
        if (!isSquare()) {
            throw new MatrixDimensionException
                ("Cofactor matrix is undefined for non-square matrices. "
                     + "Expected: nxn with n >= 1, "
                     + "but found: " + getNumRows() + "x" + getNumCols() + ".");
        }
        
        ToolNumber det = det();
        if (det.doubleValue() == 0.0)
            throw new MatrixLinearDependancyException();
            
        return calcTransCoMatrix().divi(det);
    }
    
    @Override
    public Mat sqrt() throws UnsupportedOperationException {
        // todo
        // https://en.wikipedia.org/wiki/Square_root_of_a_matrix
        throw new UnsupportedOperationException("Not yet supprted!");
    }
    
    
    /**-------------------------------------------------------------------------
     * Overrides from ToolNumberAdapter.
     * -------------------------------------------------------------------------
     */
    /**
     * Adds the first two numbers an outputs the result using the result matrix.
     * 
     * @throws MatrixDimensionException iff
     *     o either of {@code tn1} or {@code tn2} is a matrix, and the other
     *       a value and the matrix has not the dimension 1x1
     *     o both {@code tn1} or {@code tn2} are matrices and their number
     *       of rows and columns differ from eachother.
     * @throws NumberFormatException iff
     *     neither {@code tn1} as {@code tn2} are a matrix.
     * 
     * Also see {@link ToolNumberAdapter#addTool(ToolNumber, ToolNumber, M)}.
     */
    @Override
    protected <M extends Mat> M addTool(ToolNumber tn1, ToolNumber tn2,
                                        M result)
            throws MatrixDimensionException, NumberFormatException {
        if (tn1 == null || tn2 == null) throw new NullPointerException();
        boolean isMat1 = tn1 instanceof Mat;
        boolean isMat2 = tn2 instanceof Mat;
        ToolNumber[][] newValues;
        
        if (isMat1 && isMat2) {
            Mat matLeft  = (Mat) tn1;
            Mat matRight = (Mat) tn2;
            
            // Check matrix dimensions.
            if (matRight.getNumRows() != matLeft.getNumRows())
                throw new MatrixDimensionException
                    ("The number rows of the matrices are not equal.");
            if (matRight.getNumCols() != matLeft.getNumCols())
                throw new MatrixDimensionException
                    ("The number columns of the matrices are not equal.");
            
            ToolNumber[][] left  = matLeft.getValues();
            ToolNumber[][] right = matRight.getValues();
            newValues
                = new ToolNumber[matLeft.getNumRows()][matRight.getNumCols()];
            
            // Add the matrices.
            for (int i = 0; i < matLeft.getNumRows(); i++) {
                for (int j = 0; j < matRight.getNumCols(); j++) {
                        newValues[i][j] = left[i][j].add(right[i][j]);
                }
            }
            
        } else if (isMat1 ^ isMat2) {
            // Either of them is a matrix.
            // If the matrix is not an 1x1 matrix,
            // throw a NumberFormatException.
            Mat mat = (Mat) (isMat1 ? tn1 : tn2);
            
            // Check matrix dimensions.
            if (mat.getNumRows() != 1 && mat.getNumCols() != 1) {
                throw new MatrixDimensionException
                    ("Expected an 1x1 matrix, but found: " + mat.getNumRows()
                         + "x" + mat.getNumCols());
            }
            
            // Update the values.
            newValues = mat.getValues(true);
            if (isMat1) {
                newValues[0][0].addi(tn2);
                
            } else {
                newValues[0][0].addInvi(tn1);
            }
            
        } else {
            // Neither of them is a matix.
            throw new NumberFormatException
                ("Neither of the given numbers is a matrix.");
        }
        
        // Set the matrix to the result and return.
        if (result == null) {
            result = createDefaultInstance();
        }
        
        return result.setMatrix(newValues, false);
    }
    
    /**
     * Subtracts the first number from the second number and outputs
     * the result via the result matrix.
     * 
     * @throws MatrixDimensionException iff
     *     o either of {@code tn1} or {@code tn2} is a matrix, and the other
     *       a value and the matrix has not the dimension 1x1
     *     o both {@code tn1} or {@code tn2} are matrices and their number
     *       of rows and columns differ from eachother.
     * @throws NumberFormatException iff
     *     neither {@code tn1} as {@code tn2} are a matrix.
     * 
     * Also see {@link ToolNumberAdapter#subTool(ToolNumber, ToolNumber, M)}.
     */
    @Override
    protected <M extends Mat> M subTool(ToolNumber tn1,
                                        ToolNumber tn2, M result)
            throws MatrixDimensionException, NumberFormatException {
        if (tn1 == null || tn2 == null) throw new NullPointerException(
                "Expected two non-null numbers, but found at least one null.");
        boolean isMat1 = tn1 instanceof Mat;
        boolean isMat2 = tn2 instanceof Mat;
        ToolNumber[][] newValues;
        
        if (isMat1 && isMat2) {
            Mat matLeft  = (Mat) tn1;
            Mat matRight = (Mat) tn2;
            
            // Check matrix dimensions.
            if (matRight.getNumRows() != matLeft.getNumRows())
                throw new MatrixDimensionException
                    ("The number rows of the matrices are not equal.");
            if (matRight.getNumCols() != matLeft.getNumCols())
                throw new MatrixDimensionException
                    ("The number columns of the matrices are not equal.");
            
            ToolNumber[][] left  = matLeft.getValues(false);
            ToolNumber[][] right = matRight.getValues(false);
            newValues
                = new ToolNumber[matLeft.getNumRows()][matRight.getNumCols()];
            
            // Add the matrices.
            for (int i = 0; i < matLeft.getNumRows(); i++) {
                for (int j = 0; j < matRight.getNumCols(); j++) {
                        newValues[i][j] = left[i][j].sub(right[i][j]);
                }
            }
            
        } else if (isMat1 ^ isMat2) {
            // Either of them is a matrix.
            // If the matrix is not an 1x1 matrix,
            // throw a NumberFormatException.
            Mat mat = (Mat) (isMat1 ? tn1 : tn2);
            
            // Check matrix dimensions.
            if (mat.getNumRows() != 1 && mat.getNumCols() != 1){
                throw new MatrixDimensionException
                    ("Expected an 1x1 matrix, but found: " + mat.getNumRows()
                         + "x" + mat.getNumCols());
                
            }
            
            // Update the values.
            newValues = mat.getValues(true);
            if (isMat1) {
                newValues[0][0].subi(tn2);
                
            } else {
                newValues[0][0].subInvi(tn1);
            }
            
        } else {
            // Neither of them is a matix.
            throw new NumberFormatException
                ("Neither of the given numbers is a matrix.");
        }
        
        // Set the matrix to the result and return.
        if (result == null) {
            result = createDefaultInstance();
        }
        
        return result.setMatrix(newValues, false);
    }
    
    /**
     * Multiplies the first number with the second number and outputs
     * the result via the result matrix.
     * 
     * @throws MatrixDimensionException iff
     *     both {@code tn1} or {@code tn2} are matrices and the number of
     *     columns of {@code tn1} is not equal to the number of
     *     rows of {@code tn2}.
     * @throws NumberFormatException iff
     *     neither {@code tn1} as {@code tn2} are a matrix.
     * 
     * Also see {@link ToolNumberAdapter#mulTool(ToolNumber, ToolNumber, M)}.
     */
    @Override
    protected <M extends Mat> M mulTool(ToolNumber tn1,
                                        ToolNumber tn2, M result)
            throws MatrixDimensionException, NumberFormatException {
        if (tn1 == null || tn2 == null) throw new NullPointerException(
                "Expected two non-null numbers, but found at least one null.");
        boolean isMat1 = tn1 instanceof Mat;
        boolean isMat2 = tn2 instanceof Mat;
        ToolNumber[][] newValues;
        
        if (isMat1 && isMat2) {
            Mat matLeft  = (Mat) tn1;
            Mat matRight = (Mat) tn2;
            
            // Check matrix dimensions.
            if (matLeft.getNumCols() != matRight.getNumRows())
                throw new MatrixDimensionException
                    ("Number of colums of the left matrix ("
                         + matLeft.getNumCols()
                         + ") is not equal to the number of rows of the right "
                         + "matrix (" + matRight.getNumRows() + ")");
            
            ToolNumber[][] left  = matLeft.getValues(false);
            ToolNumber[][] right = matRight.getValues(false);
            newValues
                = new ToolNumber[matLeft.getNumRows()][matRight.getNumCols()];
            
            for (int i = 0; i < matLeft.getNumRows(); i++) {
                for (int j = 0; j < matRight.getNumCols(); j++) {
                    for (int k = 0; k < matLeft.getNumCols(); k++) {
                        ToolNumber mul = left[i][k].mul(right[k][j]);
                        if (newValues[i][j] == null) {
                            newValues[i][j] = mul;
                            
                        } else {
                            newValues[i][j].addi(mul);
                        }
                    }
                }
            }
            
        } else if (isMat1 ^ isMat2) {
            // Either of them is a matrix.
            Mat mat =  (Mat) (isMat1 ? tn1 : tn2);
            ToolNumber[][] matValues = mat.getValues(false);
            newValues = new ToolNumber[mat.getNumRows()][mat.getNumCols()];
            
            for (int i = 0; i < mat.getNumRows(); i++) {
                for (int j = 0; j < mat.getNumCols(); j++) {
                    if (isMat1) {
                        newValues[i][j] = matValues[i][j].mul(tn2);
                        
                    } else {
                        newValues[i][j] = matValues[i][j].mulInv(tn1);
                    }
                }
            }
            
        } else {
            // Neither of them is a matix.
            throw new NumberFormatException
                ("Neither of the given numbers is a matrix.");
        }
        
        // Set the matrix to the result and return.
        if (result == null) {
            result = createDefaultInstance();
        }
        
        return result.setMatrix(newValues, false);
    }
    
    /**
     * Divides the first number by the second number and outputs
     * the result via the result matrix.
     * Note that if both are matrices, matrix multiplication is used, 
     * but now every first number is divided by the second number, so:
     * {@code tn1 * (tn2)^-1} is what actually happens (every number in
     * {@code tn2} is inversed)).
     * 
     * @throws MatrixDimensionException iff
     *     both {@code tn1} or {@code tn2} are matrices and the number of
     *     columns of {@code tn1} is not equal to the number of
     *     rows of {@code tn2}.
     * @throws NumberFormatException iff
     *     neither {@code tn1} as {@code tn2} are a matrix.
     * 
     * Also see {@link ToolNumberAdapter#divTool(ToolNumber, ToolNumber, M)}.
     */
    @Override
    protected <M extends Mat> M divTool(ToolNumber tn1,
                                        ToolNumber tn2, M result)
            throws MatrixDimensionException, NumberFormatException {
        if (tn1 == null || tn2 == null) throw new NullPointerException(
                "Expected two non-null numbers, but found at least one null.");
        boolean isMat1 = tn1 instanceof Mat;
        boolean isMat2 = tn2 instanceof Mat;
        ToolNumber[][] newValues;
        
        if (isMat1 && isMat2) {
            Mat matLeft  = (Mat) tn1;
            Mat matRight = (Mat) tn2;
            
            // Check matrix dimensions
            if (matLeft.getNumCols() != matRight.getNumRows())
                throw new MatrixDimensionException
                    ("Number of colums of the left matrix ("
                         + matLeft.getNumCols()
                         + ") is not equal to the number of rows of the right "
                         + "matrix (" + matRight.getNumRows() + ")");
            
            ToolNumber[][] left  = matLeft.getValues(false);
            ToolNumber[][] right = matRight.getValues(false);
            newValues
                = new ToolNumber[matLeft.getNumRows()][matRight.getNumCols()];
            
            for (int i = 0; i < matLeft.getNumRows(); i++) {
                for (int j = 0; j < matRight.getNumCols(); j++) {
                    for (int k = 0; k < matLeft.getNumCols(); k++) {
                        ToolNumber mul = left[i][k].mul(right[k][j]);
                        if (newValues[i][j] == null) {
                            newValues[i][j] = mul;
                            
                        } else {
                            newValues[i][j].addi(mul);
                        }
                    }
                }
            }
            
        } else if (isMat1 ^ isMat2) {
            // Either of them is a matrix.
            Mat mat =  (Mat) (isMat1 ? tn1 : tn2);
            ToolNumber[][] matValues = mat.getValues(false);
            newValues = new ToolNumber[mat.getNumRows()][mat.getNumCols()];
            
            for (int i = 0; i < mat.getNumRows(); i++) {
                for (int j = 0; j < mat.getNumCols(); j++) {
                    if (isMat1) {
                        newValues[i][j] = matValues[i][j].div(tn2);
                        
                    } else {
                        newValues[i][j] = matValues[i][j].divInv(tn1);
                    }
                }
            }
            
        } else {
            // Neither of them is a matix.
            throw new NumberFormatException
                ("Neither of the given numbers is a matrix.");
        }
        
        // Set the matrix to the result and return.
        if (result == null) {
            result = createDefaultInstance();
        }
        
        return result.setMatrix(newValues, false);
    }
    
    /**
     * Calculates {@code tn1 % tn2}.
     * 
     * @throws MatrixDimensionException iff
     *     o either of {@code tn1} or {@code tn2} is a matrix, and the other
     *       a value and the matrix has not the dimension 1x1
     *     o both {@code tn1} or {@code tn2} are matrices and their number
     *       of rows and columns differ from eachother.
     * @throws NumberFormatException iff
     *     neither {@code tn1} as {@code tn2} are a matrix.
     * 
     * Also see {@link ToolNumberAdapter#modTool(ToolNumber, ToolNumber, M)}.
     */
    @Override
    protected <M extends Mat> M modTool(ToolNumber tn1,
                                        ToolNumber tn2, M result)
            throws MatrixDimensionException, NumberFormatException {
        if (tn1 == null || tn2 == null) throw new NullPointerException(
                "Expected two non-null numbers, but found at least one null.");
        boolean isMat1 = tn1 instanceof Mat;
        boolean isMat2 = tn2 instanceof Mat;
        ToolNumber[][] newValues;
        
        if (isMat1 && isMat2) {
            Mat matLeft  = (Mat) tn1;
            Mat matRight = (Mat) tn2;
            
            // Check matrix dimensions.
            if (matRight.getNumRows() != matLeft.getNumRows())
                throw new MatrixDimensionException
                    ("The number rows of the matrices are not equal.");
            if (matRight.getNumCols() != matLeft.getNumCols())
                throw new MatrixDimensionException
                    ("The number columns of the matrices are not equal.");
            
            ToolNumber[][] left  = matLeft.getValues();
            ToolNumber[][] right = matRight.getValues();
            newValues
                = new ToolNumber[matLeft.getNumRows()][matRight.getNumCols()];
            
            // Add the matrices.
            for (int i = 0; i < matLeft.getNumRows(); i++) {
                for (int j = 0; j < matRight.getNumCols(); j++) {
                    newValues[i][j] = left[i][j].mod(right[i][j]);
                }
            }
            
        } else if (isMat1 ^ isMat2) {
            // Either of them is a matrix.
            // If the matrix is not an 1x1 matrix,
            // throw a NumberFormatException.
            Mat mat = (Mat) (isMat1 ? tn1 : tn2);
            
            // Check matrix dimensions.
            if (mat.getNumRows() != 1 && mat.getNumCols() != 1){
                throw new MatrixDimensionException
                    ("Expected an 1x1 matrix, but found: " + mat.getNumRows()
                         + "x" + mat.getNumCols());
                
            }
            
            // Update the values.
            newValues = mat.getValues(true);
            if (isMat1) {
                newValues[0][0].modi(tn2);
                
            } else {
                newValues[0][0].modInvi(tn1);
            }
            
        } else {
            // Neither of them is a matix.
            throw new NumberFormatException
                ("Neither of the given numbers is a matrix.");
        }
        
        // Set the matrix to the result and return.
        if (result == null) {
            result = createDefaultInstance();
        }
        
        return result.setMatrix(newValues, false);
    }
    
    @Override
    public void update(Mat mat) {
        this.setMatrix(mat.getValues(false));
    }
    
    
    /**-------------------------------------------------------------------------
     * Overrides from Number.
     * -------------------------------------------------------------------------
     */
    @Override
    public byte byteValue() {
        if (getNumRows() == 1 && getNumCols() == 0) {
            return values[0][0].byteValue();
            
        } else {
            throw new NumberFormatException
                ("Expected an 1x1 matrix, but found an " + getNumRows()
                     + "x" + getNumCols() + " matrix.");
        }
    }
    
    @Override
    public short shortValue() {
        if (getNumRows() == 1 && getNumCols() == 0) {
            return values[0][0].shortValue();
            
        } else {
            throw new NumberFormatException
                ("Expected an 1x1 matrix, but found an " + getNumRows()
                     + "x" + getNumCols() + " matrix.");
        }
    }
    
    @Override
    public int intValue() {
        if (getNumRows() == 1 && getNumCols() == 0) {
            return values[0][0].intValue();
            
        } else {
            throw new NumberFormatException
                ("Expected an 1x1 matrix, but found an " + getNumRows()
                     + "x" + getNumCols() + " matrix.");
        }
    }
    
    @Override
    public long longValue() {
        if (getNumRows() == 1 && getNumCols() == 0) {
            return values[0][0].longValue();
            
        } else {
            throw new NumberFormatException
                ("Expected an 1x1 matrix, but found an " + getNumRows()
                     + "x" + getNumCols() + " matrix.");
        }
    }
    
    @Override
    public float floatValue() {
        if (getNumRows() == 1 && getNumCols() == 0) {
            return values[0][0].floatValue();
            
        } else {
            throw new NumberFormatException
                ("Expected an 1x1 matrix, but found an " + getNumRows()
                     + "x" + getNumCols() + " matrix.");
        }
    }
    
    @Override
    public double doubleValue() {
        if (getNumRows() == 1 && getNumCols() == 0) {
            return values[0][0].doubleValue();
            
        } else {
            throw new NumberFormatException
                ("Expected an 1x1 matrix, but found an " + getNumRows()
                     + "x" + getNumCols() + " matrix.");
        }
    }
    
    
    /**-------------------------------------------------------------------------
     * Other overrides.
     * -------------------------------------------------------------------------
     */
    /**
     * @return a String representation of the matrix.
     */
    @Override
    public String toString() {
        String[] outer = new String[values.length];
        
        for (int i = 0; i < values.length; i++) {
            String[] inner = new String[values[i].length];
            for (int j = 0; j < values[i].length; j++) {
                inner[j] = "" + values[i][j];
            }
            
            outer[i] = String.join(", ", inner);
        }
        
        String ls = System.getProperty("line.separator");
        return "[["+ String.join("]"+ ls + " [", outer) + "]]";
    }
    
    /**
     * @return whether {@code this} and {@code obj} are equivalent.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (!(obj instanceof Mat)) return false;
        
        return Objects.deepEquals(values, ((Mat) obj).values);
    }
    
    /**
     * @return a hash code for the current matrix.
     */
    @Override
    public int hashCode() {
        return MultiTool.calcHashCode((Object) values);
    }
    
    /**
     * Clones the current matrix.
     * 
     * @return a clone of the matrix.
     * 
     * Note that the returned value is a {@code Mat}, and not
     * a {@code M extends Mat} since this is not allowed due to
     * the existance of the{@code Object.clone()} method.
     * However, it is allowed to cast the clone to the desired class.
     */
    @Override
    public Mat clone() {
        return createDefaultInstance().setMatrix(values, true);
    }
    
    
    /**-------------------------------------------------------------------------
     * Functions that should be overridden/hidden by subclasses.
     * -------------------------------------------------------------------------
     */
    /**
     * This method is called when the matrix is set via
     * {@link setMatrix(double[][], boolean)} and adds constrains for the
     * dimension. A MatrixDimensionException should be thrown here if
     * an unacceptable matrix dimension was given.
     */
    public void matrixDimCheck(int rows, int cols) 
            throws MatrixDimensionException {
    }
    
    /**
     * This method provides a default instance for the results used
     * in the functions. Implement this function to change the returned
     * type of each of the functions above.
     * 
     * @return the default instance.
     * 
     * Notes for implementing classes:
     *  - It is advised to return the basic instance of your class
     *    (e.g. (M) new MyMat()).
     *  - This function is used in almost every operation, so keep
     *    the runtime low, preferable O(1).
     *  - To prevent infinite recursion, do NOT use any method that might
     *    depend on this function.
     *  - For every call, a new instance should be created.
     *    So do NOT return the same (static) matrix object.
     */
    @Deprecated
    public static <M extends Mat> M createDefaultInstance() {
        return (M) new Mat();
    }
    
    
    // tmp
    public static void main(String[] args) {
        /*
        Mat mat2 = new Mat(2, 2,
                           1, 2,
                           3, 4);
        
        Mat mat3 = new Mat(3, 3,
                           1, 2, 3,
                           3, 2, 6,
                           7, 8, 9);
        
        Mat mat4 = new Mat(4, 4,
                           5 , 2 , 9 , 4 ,
                           5 , 6 , 7 , 8 ,
                           9 , 33, 5, 12,
                           13, 14, 18, 16);
        
        Mat mat5 = new Mat(3, 3,
                           6, 7, 8,
                           33, 5, 12,
                           14, 18, 16);
        /*
        System.out.println(mat2.det());
        System.out.println(mat3.det());
        System.out.println(mat4.det());
        System.out.println(mat4);
        System.out.println(mat5.det());
        *//*
        Mat mat1 = new Mat(4, 2,
                           1, 2,
                           6, 7,
                           10, 5,
                           14, 18);
        System.out.println("Mat 1:");
        System.out.println(mat1);
        
        Mat mat2 = new Mat(2, 2,
                           2, 2,
                           2, 2);
        
        System.out.println("Mat 2:");
        System.out.println(mat2);
        /*
        Mat mat3 = new Mat(2, 3,
                           3, 3, 3,
                           3, 3, 3);
        System.out.println("Mat 3:");
        System.out.println(mat3);
        
        Mat mat23 = new Mat(mat2, 0,
                            1, mat3);
        
        System.out.println("Mat 23:");
        System.out.println(mat23);*/
        /*
        System.out.println(((PrimitiveNumber) mat1.getValues(false)[0][0]).type);
        System.out.println(mat1.muli(mat2));
        System.out.println(((PrimitiveNumber) mat1.getValues(false)[0][0]).type);
        /**/
        
        
        
        /*
        Mat test = new Mat(2, 2,
                           15.0, 0.8,
                           -35.0, 4.2);
                           */
        Mat test = new Mat(4, 4,
                           1.0,   2.0,  7.3,  1.5,
                           2.0,   1.0,  2.4, -0.3,
                           1.8,   0.3,  1.0, -20.0,
                           32.0, -0.1,  0.0,  1.0);
        
        Mat invTest = test.inverse();
        Mat invInvTest = invTest.inverse();
        System.out.println(test);
        System.err.println(invTest);
        System.out.println(invInvTest);
        System.out.println(test.mul(invTest));
        System.out.println("-------------");
        System.out.println(test.det());
        System.out.println(test.calcTransCoMatrix());
        System.err.println();
        
        /**/
        /*
        Mat test = new Mat(3, 3,
                           11, 12, 14,
                           21, 22, 23,
                           31, 32, 31);
        System.out.println(test.calcTransCoMatrix());
        System.out.println(test.calcCoMatrix());
        /**/
    }
    
    
}
