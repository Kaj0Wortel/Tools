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
 * This key class is used for the {@link LinkedRBTree} data structure.
 * The user of this data structure should extends this class. <br>
 * <br>
 * When searching through the tree, one can use all function of this
 * class in {@code O(1)} time.
 * 
 * @version 1.0
 * @author Kaj Wortel
 * 
 * @see LinkedRBNode
 * @see LinkedRBTree
 */
public abstract class LinkedRBKey<D extends LinkedRBKey<D>>
        implements Comparable<D> {
    
     
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The node representing this key. */
    private LinkedRBNode<D> node;
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * Sets the node represented by this key.
     * 
     * @param node The node to link.
     */
    void setNode(LinkedRBNode<D> node) {
        if (this.node != null) throw new IllegalStateException("Key cannot be added to multiple trees!");
        this.node = node;
    }
    
    /**
     * @return The node represented by this key.
     */
    LinkedRBNode<D> getNode() {
        return node;
    }
    
    /**
     * @return The next element of the chain, or {@code null}
     *     if this node has no next element.
     */
    public D next() {
        return (node == null ? null : node.getNextData());
    }
    
    /**
     * @return The previous element of the chain, or {@code null}
     *     if this node has no previous element.
     */
    public D prev() {
        return (node == null ? null : node.getPrevData());
    }
    
    /**
     * @return The data of its left child in the tree, or {@code null}
     *     if this node has no left child.
     */
    public D left() {
        if (node == null) return null;
        RBNode<D> left = node.getLeft();
        return (left == null ? null : left.getData());
    }
    
    /**
     * @return The data of its right child in the tree, or {@code null}
     *     if this node has no right child.
     */
    public D right() {
        if (node == null) return null;
        RBNode<D> right = node.getRight();
        return (right == null ? null : right.getData());
    }
    
    /**
     * @return The data of the parent in the tree, or {@code null}
     *     if this node has no parent.
     */
    public D parent() {
        if (node == null) return null;
        RBNode<D> parent = node.getParent();
        return (parent == null ? null : parent.getData());
    }
    
    /**
     * @return {@code true} if the node of this key has a left child in the tree.
     *     {@code false} otherwise.
     */
    public boolean hasLeft() {
        return (node != null && node.hasLeft());
    }
    
    /**
     * @return {@code true} if the node of this key has a right child in the tree.
     *     {@code false} otherwise.
     */
    public boolean hasRight() {
        return (node != null && node.hasRight());
    }
    
    /**
     * @return {@code true} if the node of this key has a parent in the tree.
     *     {@code false} otherwise.
     */
    public boolean hasParent() {
        return (node != null && node.getParent() != null);
    }
    
    /**
     * @return {@code true} if the node of this key represents the root in the tree.
     *     {@code false} otherwise.
     */
    public boolean isRoot() {
        return (node != null && node.getParent() == null);
    }
    
    /**
     * @return {@code true} if the node of this key represents a leaf in the tree.
     *     {@code false} otherwise.
     */
    public boolean isLeaf() {
        return (node != null && !node.hasChild());
    }
    
    @Override
    public abstract boolean equals(Object obj);
    
    @Override
    public abstract int hashCode();
    
    
}
