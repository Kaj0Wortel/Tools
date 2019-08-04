
package tools.data.array;


// Tools imports
import tools.data.Wrapper;


/**
 * Array extension class implementation which takes the get and set indices modulo
 * the length of the array.
 * <br>
 * Example: <br>
 * Assume that <br>
 * <blockquote><pre>{@code
 * CycleArray cycle = new CycleArray(new int[] {0, 1, 3});
 * }</pre></blockquote>
 * Then it holds that <br>
 * <blockquote><pre>{@code
 * cycle.get(0) == cycle.get(4) == cycle.get(-4) == cycle.get(0 + n) == 0
 * cycle.get(1) == cycle.get(5) == cycle.get(-3) == cycle.get(1 + n) == 1
 * etc...
 * }</pre></blockquote>
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class CycleArray<V>
        extends AttributedArray<V>
        implements ReadWriteArray<V> {
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates an attributed array from the given array.
     * 
     * @param arr The backening array of any type.
     */
    public CycleArray(final Object arr) {
        super(arr);
    }
    
    /**
     * Creates a attributed array from the given array.
     * 
     * @param arr The backening array.
     */
    public CycleArray(final V... arr) {
        super(arr);
    }
    
    /**
     * Creates a attributed array from the given array.
     * 
     * @param arr The backening array in a wrapper.
     * 
     * @throws NullPointerException If {@code arr == null}.
     * @throws IllegalArgumentException If {@code arr} is not an array.
     */
    public CycleArray(final Wrapper<V[]> arr) {
        super(arr);
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    @Override
    public V get(final int index) {
        return super.get(mod(index));
    }
    
    @Override
    public V set(final V value, final int index) {
        return super.set(value, mod(index));
    }
    
    /**
     * Calcualtes the index modulo the length of the array, or {@code index mod length()}.
     * 
     * @param index The index to process.
     * 
     * @return The index modulo the length of the array.
     */
    private int mod(final int index) {
        int i = index % length();
        if (i < 0) i += length();
        return i;
    }
    
    @Override
    public CycleArray clone() {
        return new CycleArray(arr);
    }
    
    
}
