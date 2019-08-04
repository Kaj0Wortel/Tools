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
 * Interface for classes which support the read function of the
 * {@link AttributedArray} class.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public interface ReadArray<V> {
    
    /**
     * Gets the value at the given index.
     * 
     * @param index The index of the element to return.
     * 
     * @return The element at the given index.
     */
    public V get(final int index);
    
    
}
