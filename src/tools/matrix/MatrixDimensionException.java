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
//todo
package tools.matrix;


/**
 * This exception should be thrown when the matrix is an incorrect dimension.
 * 
 * @version 0.0
 * @author Kaj Wortel
 * 
 * @deprecated Old class, and needs to be refactored.
 */
@Deprecated
public class MatrixDimensionException
        extends RuntimeException {
    
    public MatrixDimensionException(String id) {
        super(id);
    }
    
    
}