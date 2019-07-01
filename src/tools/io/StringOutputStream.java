/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) May 2019 by Kaj Wortel - all rights reserved                *
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

package tools.io;


// Own imports
import tools.Var;


// Java imports
import java.io.IOException;
import java.io.OutputStream;


/**
 * Wrapper class to allow strings to be written to an {@link OutputStream}.
 * 
 * @author Kaj Wortel (0991586)
 */
public class StringOutputStream
        extends OutputStream {
    
    final private OutputStream os;
    
    /**-------------------------------------------------------------------------
     * Constructor.
     * -------------------------------------------------------------------------
     */
    /**
     * Wrapper construtor.
     * 
     * @param os output stream to delegate the writing to.
     */
    public StringOutputStream(OutputStream os) {
        if (os == null) throw new NullPointerException(
                "Given stream was null!");
        this.os = os;
    }
    
    @Override
    public void write(int b)
            throws IOException {
        os.write(b);
    }
    
    /**
     * Writes a string to the output stream.
     * 
     * @param text the text to write.
     * @throws IOException 
     */
    public void write(String text)
            throws IOException {
        os.write(text.getBytes());
    }
    
    /**
     * Writes a line of text, including line separator.
     * 
     * @param text the text to write.
     * @throws IOException 
     */
    public void writeLine(String text)
            throws IOException {
        write(text);
        write(Var.LS.getBytes());
    }
    
    @Override
    public void close()
            throws IOException {
        os.close();
    }
    
    @Override
    public void flush()
            throws IOException {
        os.flush();
    }
    
    
}
