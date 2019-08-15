/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) August 2019 by Kaj Wortel - all rights reserved             *
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

package tools.data.array;


/**
 * Interface for classes which support the write function of the
 * {@link AttributedArray} class.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public interface WriteArray<V> {
    
    /**
     * Sets the given value at the given index.
     * 
     * @param value The new value of array.
     * @param index The index of the value to set.
     * 
     * @return The previous value at the given location.
     */
    public V set(final V value, final int index);
    
    
}
