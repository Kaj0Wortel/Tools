
/* * * * * * * * * * * * *
 * Created by Kaj Wortel *
 *     Last modified:    *
 *       24-01-2018      *
 *      (dd-mm-yyyy)     *
 * * * * * * * * * * * * */

package tools.matrix;

/* 
 * Represents a 4D vector.
 * Mainly used to ensure bounds.
 */
public class Vec4 extends Vec {
    /* ----------------------------------------------------------------------------------------------------------------
     * Public constants
     * ----------------------------------------------------------------------------------------------------------------
     */
    // Origin and axis vectors.
    public final static Vec4 O = new Vec4(0, 0, 0, 0);
    public final static Vec4 X = new Vec4(1, 0, 0, 0);
    public final static Vec4 Y = new Vec4(0, 1, 0, 0);
    public final static Vec4 Z = new Vec4(0, 0, 1, 0);
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructor
     * ----------------------------------------------------------------------------------------------------------------
     */
    public Vec4(double... values) {
        super(values);
    }
    
    public Vec4(boolean transpose, double... values) {
        super(transpose, values);
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
        if ((row != 1 || col != 4) && (row != 4 || col != 1))
            throw new MatrixDimensionException("Illegal change of matrix size. Found: " + row + "x" + col
                                                   + ", expected: 1x4 or 4x1.");
        return true;
    }
}
