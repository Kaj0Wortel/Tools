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


import tools.Var;



/**
 * This class stores the information about the node in a {@link RBTree}.
 * The value of the node is initially passed in the constructor. To change
 * this value, use {@link RBTree(RBNode, int)}.
 * 
 * @author Kaj Wortel
 * @see {@link LinkedRBTree}
 */
public class RBNode<D extends RBKey> {
     
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The color of the node. */
    private RBColor color = RBColor.BLACK;
    /** The value of the node. */
    private int value;
    /** The parent of the node. */
    private RBNode<D> parent = null;
    /** The left node of this node in the tree. */
    private RBNode<D> left = null;
    /** The right node of this node in the tree. */
    private RBNode<D> right = null;
    /** The data element of this node. */
    private D data;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new node with the given value to be used as key in the tree. <br>
     * Also accepts an additional data element.
     * 
     * @param tree The tree of this node.
     * @param value The value of this node.
     * @param data The data of this node.
     */
    public RBNode(D data) {
        this.value = data.value();
        this.data = data;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    void setColor(RBColor color) {
        this.color = color;
    }
    
    public final RBColor getColor() {
        return color;
    }
    
    /**
     * Returns the key of the data element.
     * 
     * @return The integer value of the key of the data element.
     */
    public final int getValue() {
        return value;
    }
    
    /**
     * Sets the value of this node.
     * @param value 
     */
    void setValue(int value) {
        this.value = value;
    }
    
    /**
     * Updates the value of this node.
     */
    public final void updateValue() {
        setValue(data.value());
    }
    
    /**
     * @param parent The new parent of this node.
     */
    void setParent(RBNode<D> parent) {
        this.parent = parent;
    }
    
    /**
     * @return The parent of this node, or {@code null} if this is the root node.
     */
    public final RBNode<D> getParent() {
        return parent;
    }
    
    /**
     * @param left The new left child of this node.
     */
    void setLeft(RBNode<D> left) {
        this.left = left;
    }
    
    /**
     * @return The left child of this node.
     */
    public final RBNode<D> getLeft() {
        return left;
    }
    
    /**
     * @param right The new right child of this node.
     */
    void setRight(RBNode<D> right) {
        this.right = right;
    }
    
    /**
     * @return The right child of this node.
     */
    public final RBNode<D> getRight() {
        return right;
    }
    
    /**
     * @param data The new data of this node.
     */
    public void setData(D data) {
        this.data = data;
    }
    
    /**
     * @return The data of this node.
     */
    public D getData() {
        return data;
    }
    
    /**
     * @return The uncle of this node, i.e. the other sibling of the parent.
     *     Returns {@code null} if no such node exists.
     */
    public final RBNode<D> getUncle() {
        if (parent == null) return null;
        return parent.getSibling();
    }
    
    /**
     * @return The sibling of this node, or {@code null} if no such node exist
     */
    public final RBNode<D> getSibling() {
        if (parent == null) return null;
        return (parent.getLeft() == this
                ? parent.getRight()
                : parent.getLeft());
    }
    
    /**
     * @return The grand parent of this node, or {@code null} if no such node exist.
     */
    public final RBNode<D> getGrandParent() {
        return (parent == null
                ? null
                : parent.getParent());
    }
    
    /**
     * @return {@code true} if this node has a left child.
     */
    public final boolean hasLeft() {
        return left != null;
    }
    
    /**
     * @return {@code true} if this node has a right child.
     */
    public final boolean hasRight() {
        return right != null;
    }
    
    /**
     * @return If this node has a child.
     */
    public final boolean hasChild() {
        return hasLeft() || hasRight();
    }
    
    /**
     * @return {@code true} if this node is the right child. {@code false} otherwise.
     */
    public final boolean isRight() {
        return (parent == null
                ? false
                : parent.getRight() == this);
    }
    
    /**
     * @return {@code true} if this node is the left child. {@code false} otherwise.
     */
    public final boolean isLeft() {
        return (parent == null
                ? false
                : parent.getLeft() == this);
    }
    
    /**
     * @return {@code true} if this node is a root node. {@code false} otherwise.
     */
    public final boolean isRoot() {
        return parent == null;
    }
    
    /**
     * @return {@code true} if the color of this node is red.
     */
    public final boolean isRed() {
        return color == RBColor.RED;
    }
    
    /**
     * @return {@code true} if the color of this node is black.
     */
    public final boolean isBlack() {
        return color == RBColor.BLACK;
    }
    
    /**
     * @return {@code true} if the left node is a black node. This includes the NIL leaves.
     */
    public final boolean leftIsBlack() {
        return left == null || left.color == RBColor.BLACK;
    }
    
    /**
     * @return {@code true} if the right node is a black node. This includes the NIL leaves.
     */
    public final boolean rightIsBlack() {
        return right == null || right.color == RBColor.BLACK;
    }
    
    @Override
    public String toString() {
        return "Node[" + Var.LS +
                "  this  : " + (data == null ? "null" : data.toString()) + Var.LS + 
                "  color : " + color + Var.LS + 
                "  parent: " + (parent == null || parent.data == null ? "null" : parent.data.toString()) + Var.LS +
                "  left  : " + (left == null || left.data == null ? "null" : left.data.toString()) + Var.LS +
                "  right : " + (right == null || right.data == null ? "null" : right.data.toString()) + Var.LS +
                "]";
    }
    
    @Override
    public int hashCode() {
        return data.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof RBNode) {
            RBNode<D> node = (RBNode<D>) obj;
            return (node.value == value && node.hashCode() == hashCode() && node.data.equals(data));
            
        } else if (obj instanceof RBKey) {
            RBKey key = (RBKey) obj;
            return (key.value() == value && data.hashCode() == key.hashCode() && key.equals(data));
        }
        return false;
    }
    
    
}
