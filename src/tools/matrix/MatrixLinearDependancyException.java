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
 * This exception should be thrown when a matrix has an incorrect amount
 * of linear dependant dimensions.
 */
public class MatrixLinearDependancyException extends RuntimeException {
    
    public MatrixLinearDependancyException() {
        super("Cannot create an nxn inverse matrix of a matrix that has "
                  + "less then n linearly independant dimensions.");
    }
    
    public MatrixLinearDependancyException(String id) {
        super(id);
    }
}