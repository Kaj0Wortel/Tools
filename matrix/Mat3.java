
/* * * * * * * * * * * * *
 * Created by Kaj Wortel *
 *     Last modified:    *
 *       24-01-2018      *
 *      (dd-mm-yyyy)     *
 * * * * * * * * * * * * */

package tools.matrix;


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