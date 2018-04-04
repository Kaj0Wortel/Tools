
/* * * * * * * * * * * * *
 * Created by Kaj Wortel *
 *     Last modified:    *
 *       24-01-2018      *
 *      (dd-mm-yyyy)     *
 * * * * * * * * * * * * */

package tools.matrix;

/* 
 * Represents a 3D vector.
 * Mainly used to ensure bounds.
 */
public class Vec3 extends Vec {
    /* ----------------------------------------------------------------------------------------------------------------
     * Public constants
     * ----------------------------------------------------------------------------------------------------------------
     */
    // Origin and axis vectors.
    public final static Vec3 O = new Vec3(0, 0, 0);
    public final static Vec3 X = new Vec3(1, 0, 0);
    public final static Vec3 Y = new Vec3(0, 1, 0);
    public final static Vec3 Z = new Vec3(0, 0, 1);
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructor
     * ----------------------------------------------------------------------------------------------------------------
     */
    public Vec3(double... values) {
        super(values);
    }
    
    public Vec3(boolean transpose, double... values) {
        super(transpose, values);
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Get functions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Returns the x coordinate of the vector.
     */
    public double x() {
        return values[0][0];
    }
    
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
        if ((row != 1 || col != 3) && (row != 3 || col != 1))
            throw new MatrixDimensionException("Illegal change of matrix size. Found: " + row + "x" + col
                                                   + ", expected: 1x3 or 3x1.");
        return true;
    }
}
