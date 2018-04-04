/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2018 by Kaj Wortel - all rights reserved                *
 * Contact: <kaj.wortel@gmail.com>                                       *
 *                                                                       *
 * This file is part of the tools project.                               *
 *                                                                       *
 * It is allowed to use, (partially) copy and modify this file           *
 * in any way for private use only.                                      *
 * It is not allowed to redistribute any (modifed) versions of this file *
 * without my permission.                                                *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package tools;


// Java packages
import java.io.OutputStream;


public class NullOutputStream extends OutputStream {
    public NullOutputStream() {
    }
    
    @Override
    public void write(int i) {
    }
}
