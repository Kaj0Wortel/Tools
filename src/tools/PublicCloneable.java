/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) July 2019 by Kaj Wortel - all rights reserved               *
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

package tools;


/**
 * A usefull interface that forces the implementer to have a public
 * clone method instead of the private clone method from {@link Object}.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public interface PublicCloneable
        extends Cloneable {
    
    /**
     * @return A clone of {@code this}.
     * 
     * @see Object#clone()
     */
    public PublicCloneable clone();
    
    
}