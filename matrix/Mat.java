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

/* 
 * In development.
 * Use with care.
 */
public class Mat {
    protected double[][] values;
    protected int row;
    protected int col;
    protected boolean square;
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructors
     * ----------------------------------------------------------------------------------------------------------------
     */
    public Mat(double[]... mat) throws MatrixDimensionException {
        setMatrix(mat);
    }
    
    public Mat(int row, int col, double... mat) throws MatrixDimensionException {
        if (mat.length != row*col)
            throw new MatrixDimensionException("Given 1D matrix has an invallid length. Expected: " + (row*col)
                                                   + ", found: " + mat.length + ".");
        double[][] newValues = new double[row][col];
        
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                newValues[i][j] = mat[i*col + j];
            }
        }
        
        setMatrix(newValues);
    }
    
    public Mat(Mat mat_1, double fillValue_1, double fillValue_2, Mat mat_2) throws MatrixDimensionException{
        int rowsMat_1 = mat_1.getNumRows();
        int rowsMat_2 = mat_2.getNumRows();
        int colsMat_1 = mat_1.getNumCols();
        int colsMat_2 = mat_2.getNumCols();
        
        int row = rowsMat_1 + rowsMat_2;
        int col = colsMat_1 + colsMat_2;
        double[][] newValues = new double[row][col];
        
        double[][] valuesMat_1 = mat_1.getValues();
        double[][] valuesMat_2 = mat_2.getValues();
        
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (i < rowsMat_1) {
                    // Upper left part: copy mat_1
                    if (j < colsMat_1) {
                        newValues[i][j] = valuesMat_1[i][j];
                        
                        // Upper right part: use fillValue_1
                    } else {
                        newValues[i][j] = fillValue_1;
                    }
                    
                } else {
                    
                    // Lower left part: copy mat_1
                    if (j < colsMat_1) {
                        newValues[i][j] = fillValue_2;
                        
                        // Lower right part: use fillValue_1
                    } else {
                        newValues[i][j] = valuesMat_2[i - rowsMat_1][j - colsMat_1];
                    }
                }
            }
        }
        
        setMatrix(newValues);
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Public static methods
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Creates an identity matrix of size (width x height).
     */
    public static Mat getIMat(int width, int height) {
        double[][] matIValues = new double[width][height];
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                matIValues[i][j] = (i == j ? 1.0 : 0.0);
            }
        }
        
        return new Mat(matIValues);
    }
    
    /* 
     * Multiplies two matrices.
     * Returns the resulting matrix.
     */
    public static Mat multiply(Mat matLeft, Mat matRight) throws MatrixDimensionException {
        // Bound checking
        if (matLeft.col != matRight.row)
            throw new MatrixDimensionException("Number of colums of the left matrix ("+ matLeft.col
                                                   + ") is not equal to the number of rows of the right matrix ("
                                                   + matRight.row + ")");
        
        double[][] newMat = new double[matLeft.row][matRight.col];
        double[][] left   = matLeft.getValues();
        double[][] right  = matRight.getValues();
        
        for (int i = 0; i < matLeft.row; i++) {
            for (int j = 0; j < matRight.col; j++) {
                newMat[i][j] = 0.0;
                
                for (int k = 0; k < matLeft.col; k++) {
                    newMat[i][j] += left[i][k] * right[k][j];
                }
            }
        }
        
        return new Mat(newMat);
    }
    
    /* 
     * Multiplies the matrix with a constant.
     * Returns the resulting matrix
     */
    public static Mat multiply(double value, Mat mat) {
        double[][] values = mat.getValues();
        
        for (int i = 0; i < values.length; i++) {
            values[i][i] *= value;
        }
        
        return new Mat(values);
    }
    
    /* 
     * Multiplies all matrices mats, assuming the order left to right.
     * (so mat[0] * mat[1] * ... * mat[n]).
     * Returns the result.
     */
    public static Mat multiplyAll(Mat... mats) {
        if (mats == null) return null;
        if (mats.length == 0) throw new IllegalArgumentException("The length of the variable 'mats' is not allowed to be 0.");
        
        Mat calcMat = mats[0];
        for (int i = 1; i < mats.length; i++) {
            calcMat.rightMultiply(mats[i]);
        }
        
        return calcMat;
    }
    
    /* 
     * Transposes the given matrix.
     * Returns transposed matrix.
     * update denotes whether the given matrix should be updated.
     */
    public static Mat transpose(Mat mat) {
        return transpose(mat, true);
    }
    
    public static Mat transpose(Mat mat, boolean update) {
        double[][] oldV = mat.getValues();
        double[][] newV = new double[oldV[0].length][oldV.length];
        
        for (int i = 0; i < oldV.length; i++) {
            for (int j = 0; j < oldV[0].length; j++) {
                newV[j][i] = oldV[i][j];
            }
        }
        
        if (update) {
            mat.setMatrix(newV);
            return mat;
        } else {
            return new Mat(newV);
        }
    }
    
    public static Mat multiplyConstAddMat(double val_1, Mat mat_1, double val_2, Mat mat_2) {
        if (mat_1.getNumRows() != mat_2.getNumRows())
            throw new MatrixDimensionException("The number rows of the matrices are not equal.");
        if (mat_1.getNumCols() != mat_2.getNumCols())
            throw new MatrixDimensionException("The number columns of the matrices are not equal.");
        
        double[][] values_1 = mat_1.getValues();
        double[][] values_2 = mat_2.getValues();
        double[][] newValues = new double[values_1.length][values_1[0].length];
        
        for (int i = 0; i < values_1.length; i++) {
            for (int j = 0; j < values_1[0].length; j++) {
                newValues[i][j] = val_1 * values_1[i][j] + val_2 * values_2[i][j];
            }
        }
        
        return new Mat(newValues);
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Public methods
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * This method is called when the matrix is set, and
     * is supposed to be overridden by subclasses that have
     * a (semi) static dimension.
     * 
     * Returns false iff the check fails, true otherwise.
     * Throw exceptions in this method to improve retracing.
     */
    protected boolean matrixDimCheck(int row, int col) {
        return true;
    }
    
    /* 
     * Sets the values of the matrix.
     */
    protected Mat setMatrix(double[][] mat) {
        if (mat == null)
            throw new NullPointerException("Variable 'mat' is not allowed to be null.");
        
        if (!matrixDimCheck(mat.length, mat[0].length))
            throw new MatrixDimensionException("Invallid matrix dimension: " + mat.length + "x" + mat[0].length);
        
        row = mat.length;
        col = mat[0].length;
        square = row == col;
        values = new double[row][col];
        
        for (int i = 0; i < row; i++) {
            if (mat[i].length != col)
                throw new MatrixDimensionException("Invallid raster format. Expected: " + row + "x" + col
                                                       + ", found height of " + mat[i].length + " at col = " + i + ".");
            
            for (int j = 0; j < col; j++) {
                values[i][j] = mat[i][j];
            }
        }
        
        return this;
    }
    
    /* 
     * Does a right multiplication for this matrix.
     * Stores and returns the result.
     * Uses the static method multiply for the calculation.
     * 
     * mat denotes the matrix that this matrix will be multiplied
     * with.
     * updateMat denotes whether the result should be stored
     * in the current Matrix. Default is true.
     */
    public Mat rightMultiply(Mat mat) {
        return rightMultiply(mat, true);
    }
    
    public Mat rightMultiply(Mat mat, boolean updateMat) {
        Mat resultMatrix = multiply(this, mat);
        
        if (updateMat) {
            this.values = resultMatrix.getValues();
            return this;
        }
        
        return resultMatrix;
    }
    
    /*
     * Does a left multiplication for this matrix.
     * Stores and returns the result.
     * Uses the static method multiply for the calculation.
     * 
     * mat denotes the matrix that this matrix will be multiplied
     * with. updateMat denotes whether the result should be stored
     * in the current Matrix. Default is true.
     */
    public Mat leftMultiply(Mat mat) {
        return leftMultiply(mat, true);
    }
    
    public Mat leftMultiply(Mat mat, boolean updateMat) {
        Mat resultMatrix = Mat.multiply(mat, this);
        
        if (updateMat) {
            this.values = resultMatrix.getValues();
            return this;
        }
        
        return resultMatrix;
    }
    
    /*
     * Multiplies the matrix with a value.
     * Uses the static method multiply for the calculation.
     * 
     * value denotes the value that is multiplied with the matrix.
     * updateMat denotes whether the result should be stored
     * in the current Matrix. Default is true.
     */
    public Mat multiply(double value) {
        return multiply(value, true);
    }
    
    public Mat multiply(double value, boolean updateMat) {
        Mat newMat = multiply(value, this);
        
        if (updateMat) {
            this.values = newMat.getValues();
            return this;
        }
        
        return newMat;
    }
    
    /* 
     * Adds a matrix to the current matrix.
     * update denotes whether the result should be stored
     * in the current matrix. Default is true.
     */
    public Mat add(Mat mat) {
        return add(mat, true);
    }
    
    public Mat add(Mat mat, boolean update) {
        Mat newMat = multiplyConstAddMat(1, this, 1, mat);
        
        if (update) {
            this.setMatrix(newMat.getValues());
            return this;
            
        } else {
            return newMat;
        }
    }
    
    /* 
     * Subtracts a matrix to the current matrix.
     * update denotes whether the result should be stored
     * in the current matrix. Default is true.
     */
    public Mat sub(Mat mat) {
        return add(mat, true);
    }
    
    public Mat sub(Mat mat, boolean update) {
        Mat newMat = multiplyConstAddMat(1, this, -1, mat);
        
        if (update) {
            this.setMatrix(newMat.getValues());
            return this;
            
        } else {
            return newMat;
        }
    }
    
    /* 
     * Multiplies a value with the current matrix, 
     * and a value with another matrix, then adds the matrices.
     * 
     * update denotes whether the result should be stored
     * in the current matrix. Default is true.
     */
    public Mat multiplyConstAddMat(double val_this, double val_mat, Mat mat) {
        return multiplyConstAddMat(val_this, val_mat, mat, true);
    }
    
    public Mat multiplyConstAddMat(double val_this, double val_mat, Mat mat, boolean update) {
        Mat newMat = multiplyConstAddMat(val_this, this, val_mat, mat);
        
        if (update) {
            this.setMatrix(newMat.getValues());
            return this;
            
        } else {
            return newMat;
        }
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Get functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /*
     * Returns the values of this matrix.
     */
    public double[][] getValues() {
        return values;
    }
    
    /* 
     * Returns the number of rows of the matrix.
     */
    public int getNumRows() {
        return row;
    }
    
    /* 
     * Returns the number of columns of the matrix.
     */
    public int getNumCols() {
        return col;
    }
    
    /* 
     * Returns whether the matrix is square.
     * Equivalent to width == height.
     */
    public boolean isSquare() {
        return square;
    }
    
    /*
     * Converts the matrix to a matrix compatible with the gl shader.
     */
    public double[] toGLMatrix() {
        double[] result = new double[values.length * values[0].length];
        
        int counter = 0;
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[0].length; j++) {
                result[counter++] = values[j][i]; // Note the transpose here
            }
        }
        
        return result;
    }
    
    /*
     * Transpose the current matrix.
     * Returns the transposed matrix.
     */
    public Mat transpose() {
        return transpose(this, true);
    }
    
    public double det() {
        if (!square)
            throw new MatrixDimensionException("Determinant is undefined for non-square matrices. "
                                                   + "Expected: nxn with n >= 2, "
                                                   + "found: " + row + "x" + col + ".");
        if (row <= 1) {
            return values[0][0];
            
        } else if (row == 2) {
            return values[0][0] * values[1][1]
                -  values[0][1] * values[1][0];
            
        } else if (row == 3) {
            return values[0][0] * values[1][1] * values[2][2]
                +  values[0][1] * values[1][2] * values[2][0]
                +  values[0][2] * values[1][0] * values[2][1]
                -  values[0][2] * values[1][1] * values[2][0]
                -  values[0][1] * values[1][0] * values[2][2]
                -  values[0][0] * values[1][2] * values[2][1];
            
        } else { // row > 3
            double result = 0.0;
            
            // Multiply every number on the first row with the determinant
            // of the remaining matrix, where all numbers on the same row
            // and collumn are deleted.
            for (int i = 0; i < col; i++) {
                double[][] newValues = new double[row-1][col-1];
                
                for (int j = 1; j < row; j++) {
                    for (int k = 0; k < col; k++) {
                        if (i == k) continue;
                        if (k > i) {
                            newValues[j-1][k-1] = values[j][k];
                        } else {
                            newValues[j-1][k] = values[j][k];
                        }
                    }
                }
                
                double detResult = values[0][i] * new Mat(newValues).det();
                if (i/2 == i/2.0) {
                    result += detResult;
                } else {
                    result -= detResult;
                }
                //System.out.println(new Mat(newValues));
                //System.out.println(new Mat(newValues).det());
                //System.out.println(result);
            }
            
            return result;
        }
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Overridden functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Overrides the equals function from the Object class.
     * Used for easy comparison between Matrix objects.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof Mat)) return false;
        Mat mat = (Mat) obj;
        
        // Check if both matrices are equal in size.
        if (this.getNumRows() != mat.getNumRows() ||
            this.getNumCols() != mat.getNumCols()) return false;
        
        // Check if the values are equal.
        double[][] values = mat.getValues();
        
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                if (values[i][j] != this.values[i][j]) return false;
            }
        }
        
        return true;
    }
    
    /* 
     * Overrides the toString function from the Object class.
     * Creates a nice String representation of this matix.
     */
    @Override
    public String toString() {
        String text = "[[";
        
        for (int i = 0; i < values.length; i++) {
            if (i != 0) {
                text += " [";
            }
            
            for (int j = 0; j < values[i].length; j++) {
                text += this.values[i][j];
                
                if (j + 1 != values[i].length) {
                    text += ", ";
                }
            }
            
            text += "]";
            if (i + 1 != values.length) {
                text += System.getProperty("line.separator");
            }
        }
        text += "]";
        return text;
    }
    
    /* 
     * Overrides the clone function from the Cloneable interface.
     * Used to make a deep copy of this Matrix object.
     */
    @Override
    public Mat clone() {
        return new Mat(this.values);
    }
    
    public static void main(String[] args) {
        
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
        System.out.println(mat2.det());
        System.out.println(mat3.det());
        System.out.println(mat4.det());
        System.out.println(mat4);
        System.out.println(mat5.det());
        /*
        System.out.println("Mat 1:");
        System.out.println(mat1);
        
        Mat mat2 = new Mat(2, 2,
                           2, 2,
                           2, 2);
        
        System.out.println("Mat 2:");
        System.out.println(mat2);
        
        Mat mat3 = new Mat(2, 3,
                           3, 3, 3,
                           3, 3, 3);
        System.out.println("Mat 3:");
        System.out.println(mat3);
        
        Mat mat23 = new Mat(mat2, 0,
                            1, mat3);
        
        System.out.println("Mat 23:");
        System.out.println(mat23);/**/
    }
}