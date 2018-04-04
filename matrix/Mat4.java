
/* * * * * * * * * * * * *
 * Created by Kaj Wortel *
 *     Last modified:    *
 *       24-01-2018      *
 *      (dd-mm-yyyy)     *
 * * * * * * * * * * * * */

package tools.matrix;


/* 
 * Represents a 4x4 matrix in homogeneous coordinates.
 */
public class Mat4 extends Mat {
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructors
     * ----------------------------------------------------------------------------------------------------------------
     */
    public Mat4(double[]... mat) {
        super(mat);
    }
    
    /* 
     * Returns a matrix for a counter-clockwise rotation
     * around the x-axis.
     */
    public static Mat4 rotateMat4X(double rad) {
        return new Mat4(new double[][] {
            {1, 0            ,  0            , 0},
            {0, Math.cos(rad), -Math.sin(rad), 0},
            {0, Math.sin(rad),  Math.cos(rad), 0},
            {0, 0            , 0             , 1}}
        );
    }
    
    /* 
     * Returns a matrix for a counter-clockwise rotation
     * around the y-axis.
     */
    public static Mat4 rotateMat4Y(double rad) {
        return new Mat4(new double[][] {
            { Math.cos(rad), 0, Math.sin(rad), 0},
            { 0            , 1, 0            , 0},
            {-Math.sin(rad), 0, Math.cos(rad), 0},
            { 0            , 0, 0            , 1}}
        );
    }
    
    /* 
     * Returns a matrix for a counter-clockwise rotation
     * around the z-axis.
     */
    public static Mat4 rotateMat4Z(double rad) {
        return new Mat4(new double[][] {
            {Math.cos(rad), -Math.sin(rad), 0, 0},
            {Math.sin(rad),  Math.cos(rad), 0, 0},
            {0            , 0             , 1, 0},
            {0            , 0             , 0, 1}}
        );
    }
    
    /* 
     * Returns a translation matrix for the given translation.
     */
    public static Mat4 translateMat4(double x, double y, double z) {
        return new Mat4 (new double[][] {
            {1, 0, 0, x},
            {0, 1, 0, y},
            {0, 0, 1, z},
            {0, 0, 0, 1}}
        );
    }
    
    public static Mat4 translateMat4(Vec4 v) {
        if (v.w() != 0) {
            return translateMat4(v.x() / v.w(),
                                 v.y() / v.w(),
                                 v.z() / v.w());
        } else {
            return translateMat4(v.x(), v.y(), v.z());
        }
    }
    
    public static Mat4 translateMat4(Vec3 v) {
        return translateMat4(v.x(), v.y(), v.z());
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Overridden functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Multiplies the matrix with a constant.
     * Returns the resulting matrix
     */
    //@Override
    public static Mat multiply(double value, Mat mat) {
        double[][] values = mat.getValues();
        
        for (int i = 0; i < values.length - 1; i++) {
            values[i][i] *= value;
        }
        
        return new Mat(values);
    }
    
    /* 
     * Overrides the matrix dimension check.
     * See the Mat class for more info.
     */
    @Override
    protected boolean matrixDimCheck(int row, int col) {
        if (row != 4 || col != 4)
            throw new MatrixDimensionException("Illegal change of matrix size. Found: " + row + "x" + col
                                                   + ", expected: 4x4.");
        return true;
    }
    
    
    public static void main(String[] args) {
        Mat4 mat4 = new Mat4(new double[][] {
            {0, 1, 2, 3},
            {4, 5, 6, 7},
            {8, 9, 10, 11},
            {12, 13, 14, 15}
        });
    }
}
