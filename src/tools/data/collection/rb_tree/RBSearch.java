/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) November 2019 by Kaj Wortel - all rights reserved           *
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

package tools.data.collection.rb_tree;


/**
 * Search function used to find items in the {@link RBTree} data structure. <br>
 * 
 * @version 1.1
 * @author Kaj Wortel
 * 
 * @see RBTree
 */
public interface RBSearch<D extends Comparable<D>> {
    
    /**
     * Enum denoting the options allowed for searching through the tree.
     */
    public enum Choice {
        /** Returns the current element. */
        CURRENT,
        /** Returns the left element. */
        LEFT,
        /** Returns the right element. */
        RIGHT,
        /** Traverses the tree to the left. */
        GO_LEFT,
        /** Traverses the tree to the right. */
        GO_RIGHT;
    }
    
    /**
     * For every level, the user gets the current data, and it's two children. <br>
     * If a child is {@code null}, then it doesn't exist. <br>
     * The key value of the left node is always samller than the key value of the
     * current node, and the key value of the right node is always smaller than
     * the key value current node. <br>
     * <br>
     * As return value, one of the values of {@link Choice} should be returned.
     * If {@code null} is returned, then the search is aborted and {@code null} is
     * returned by tne search function.
     * 
     * @param cur The current data. Is never {@code null}.
     * @param left The data on the left. Can be {@code null}.
     * @param right The data on the right. Can be {@code null}.
     * 
     * @return The option to be made, or {@code null} to stop the search {@code null}.
     * 
     * @see Choice
     */
    public Choice evaluate(D cur, D left, D right);
    
    
}
