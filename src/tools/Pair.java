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


import java.util.Objects;
import tools.data.array.ArrayTools;



/**
 * Class for coupling two objects together.
 * 
 * @version 1.0
 * @author Kaj Wortel
 */
public final class Pair<V1, V2>
        implements PublicCloneable {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** First value of the pair. */
    private V1 v1;
    /** Second value of the pair. */
    private V2 v2;
    /** Whether the pair is modifiable. */
    private final boolean mod;
    
    
    /* -------------------------------------------------------------------------
     * Constructor.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new pair of values.
     * 
     * @param v1 Value 1 of the pair.
     * @param v2 Value 2 of the pair.
     */
    public Pair(V1 v1, V2 v2) {
        this(v1, v2, true);
    }
    
    /**
     * Creates a new pair of values.
     * 
     * @param v1 Value 1 of the pair.
     * @param v2 Value 2 of the pair.
     * @param mod Whether this pair is modifiable.
     */
    public Pair(V1 v1, V2 v2, boolean mod) {
        this.v1 = v1;
        this.v2 = v2;
        this.mod = mod;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * @return The first value of the pair.
     */
    public V1 getFirst() {
        return v1;
    }
    
    /**
     * @return The second value of the pair.
     */
    public V2 getSecond() {
        return v2;
    }
    
    /**
     * @return {@code true} if the pair is modifiable.
     *     {@code false} otherwise.
     */
    public boolean isModifiable() {
        return mod;
    }
    
    /**
     * @param v1 The new value of the first value.
     * 
     * @throws IllegalStateException If the pair is not modifiable.
     */
    public void setFirst(V1 v1)
            throws IllegalStateException {
        checkMod();
        this.v1 = v1;
    }
    
    /**
     * @param v2 The new value of the second value.
     * 
     * @throws IllegalStateException If the pair is not modifiable.
     */
    public void setSecond(V2 v2)
            throws IllegalStateException {
        checkMod();
        this.v2 = v2;
    }
    
    /**
     * @throws IllegalStateException If the pair is not modifiable.
     */
    private void checkMod()
            throws IllegalStateException {
        if (!mod) {
            throw new IllegalStateException(
                    "Tried to modify an unmodifiable pair!");
        }
    }
    
    @Override
    public int hashCode() {
        return MultiTool.calcHashCode(new Object[] {v1, v2});
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pair)) return false;
        Pair<V1, V2> other = (Pair<V1, V2>) obj;
        return ArrayTools.deepEquals(v1, other.v1) &&
                ArrayTools.deepEquals(v2, other.v2);
    }
    
    @Override
    public Pair<V1, V2> clone() {
        return new Pair<V1, V2>(v1, v2);
    }
    
    
}
