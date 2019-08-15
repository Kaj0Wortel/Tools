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

package tools.data.file;


// Java imports
import java.util.Objects;


// Tools imports
import tools.MultiTool;


/**
 * Class used to uniquely identify a file tree.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public class FileTreeToken<C extends FileTree, T> {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The type of the file tree. */
    protected final Class<C> c;
    /** The token to uniquely identify a file tree of this type. */
    protected final T token;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new token for a file tree.
     * 
     * @param c The type of file tree.
     * @param token The token to uniquely identify a file tree of this type.
     */
    public FileTreeToken(Class<C> c, T token) {
        this.c = c;
        this.token = token;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * @return The class of the file tree this token belongs to.
     */
    protected Class<C> getTreeClass() {
        return c;
    }
    
    /**
     * @return The token of the file tree this token belongs to.
     */
    protected T getTreeToken() {
        return token;
    }
    
    @Override
    public int hashCode() {
        return MultiTool.calcHashCode(new Object[] {c, token});
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FileTreeToken)) return false;
        FileTreeToken ftt = (FileTreeToken) obj;
        return Objects.equals(ftt.c, this.c) &&
                Objects.equals(ftt.token, this.token);
    }
    
    @Override
    public String toString() {
        return FileTreeToken.class.getCanonicalName() + "["
                + "class: " + (c == null ? "null" : c.getCanonicalName())
                + ", token: " + (token == null ? "null" : token.toString());
    }
    
    
}
