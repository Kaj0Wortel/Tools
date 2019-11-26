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


// Tools imports
import tools.Var;


/**
 * This class stores the information about a node in the {@link LinkedRBTree} data structure.
 * 
 * @version 1.0
 * @author Kaj Wortel
 * 
 * @see RBNode
 * @see LinkedRBTree
 * @see LinkedRBKey
 */
public class LinkedRBNode<D extends LinkedRBKey<D>>
        extends RBNode<D> {
    
    /* -------------------------------------------------------------------------
     * Variables.
     * -------------------------------------------------------------------------
     */
    /** The sorted next node of this node. */
    private LinkedRBNode<D> next;
    /** The sorted previous node of this node. */
    private LinkedRBNode<D> prev;
    
    
    /* -------------------------------------------------------------------------
     * Constructors.
     * -------------------------------------------------------------------------
     */
    /**
     * Creates a new node with the given data.
     * 
     * @param data The data of this node.
     */
    private LinkedRBNode(D data) {
        super(data);
    }
    
    /**
     * Creates a new {@link LinkedRBNode}. Automatically links the key to the node. <br>
     * This factory design pattern is used to prevent leaking of this instance
     * during initialisation.
     * 
     * @param <D1> The type of the node to be initialised.
     * 
     * @param data The data of the new node.
     * 
     * @return A freshly initialised node.
     * 
     * @throws IllegalArgumentException if the given data already belongs to another
     *     linked node.
     */
    public static <D1 extends LinkedRBKey<D1>> LinkedRBNode<D1> createNode(D1 data) {
        if (data.getNode() != null) {
            throw new IllegalArgumentException(
                    "A linked key cannot be added to more than one search tree!");
        }
        LinkedRBNode<D1> node = new LinkedRBNode<D1>(data);
        data.setNode(node);
        return node;
    }
    
    
    /* -------------------------------------------------------------------------
     * Functions.
     * -------------------------------------------------------------------------
     */
    /**
     * @param next The new next node of this node.
     */
    protected void setNext(LinkedRBNode<D> next) {
        this.next = next;
    }
    
    /**
     * @param prev The new previous node of this node.
     */
    protected void setPrev(LinkedRBNode<D> prev) {
        this.prev = prev;
    }
    
    /**
     * @return The next node of this node.
     */
    public LinkedRBNode<D> getNext() {
        return next;
    }
    
    /**
     * @return The previous node of this node.
     */
    public LinkedRBNode<D> getPrev() {
        return prev;
    }
    
    /**
     * @return {@code true} if this node has a next node. {@code false} otherwise.
     */
    public boolean hasNext() {
        return next != null;
    }
    
    /**
     * @return {@code true} if this node has a previous node. {@code false} otherwise.
     */
    public boolean hasPrev() {
        return prev != null;
    }
    
    /**
     * @return The data of the next node, or {@code null} if there is no next node.
     */
    public D getNextData() {
        return (next == null ? null : next.getData());
    }
    
    /**
     * @return The data of the previous node, or {@code null} if there is no previous node.
     */
    public D getPrevData() {
        return (prev == null ? null : prev.getData());
    }
    
    @Override
    public String toString() {
        return "Node[" + Var.LS +
                "  this  : " + (getData() == null ? "null" : getData().toString()) + Var.LS + 
                "  color : " + getColor() + Var.LS + 
                "  parent: " + (!hasParent() || getParent().getData() != null
                ? "null" : getParent().getData().toString()) + Var.LS +
                "  left  : " + (!hasLeft() || getLeft().getData() == null
                ? "null" : getLeft().getData().toString()) + Var.LS +
                "  right : " + (!hasRight() || getRight().getData() == null
                ? "null" : getRight().getData().toString()) + Var.LS +
                "  next  : " + (!hasNext() || getNext().getData() == null
                ? "null" : getNext().getData().toString()) + Var.LS +
                "  prev  : " + (!hasPrev() || getPrev().getData() == null
                ? "null" : getPrev().getData().toString()) + Var.LS +
                "]";
    }
    
    
}
