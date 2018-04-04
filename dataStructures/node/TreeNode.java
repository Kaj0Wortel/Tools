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

package tools.dataStructures.node;

// Tools imports
import tools.dataStructures.Node;

// Java imports
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;


/* 
 * Under construction.
 * Do not use!
 * 
 */
/* 
 * Todo:
 * change the .getChildren() method to a return type
 * for only the nodes of the children.
 */
public class TreeNode<E> extends Node<E> implements Cloneable {
    protected Number key;
    protected Node parent;
    protected ArrayList<Edge> children;
    
    public TreeNode(E entry) {
        super(entry);
        System.err.println("Unfinished class!");
        key = null;
        parent = null;
        children = new ArrayList<Edge>();
    }
    
    public TreeNode(Number key) {
        super(null);
        System.err.println("Unfinished class!");
        this.key = key;
        entry = null;
        parent = null;
        children = new ArrayList<Edge>();
    }
    
    public TreeNode(Number key, E entry) {
        super(entry);
        System.err.println("Unfinished class!");
        this.key = key;
        parent = null;
        children = new ArrayList<Edge>();
    }
    
    @SuppressWarnings("unchecked")
    public TreeNode(Number key, E entry, Node parent, ArrayList<Node> children) {
        super(entry);
        System.err.println("Unfinished class!");
        this.key = key;
        this.parent = parent;
        this.children = (ArrayList<Edge>) children.clone();
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Methods for Cloneable
     * ----------------------------------------------------------------------------------------------------------------
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object clone() {
        ListIterator<Edge> childIter = children.listIterator();
        ArrayList<Edge> childClone = new ArrayList<Edge>();
        
        while (childIter.hasNext()) {
            childClone.add((Edge) childIter.next());
        }
        
        return new TreeNode(key, entry, parent, childClone);
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Set/add/remove methods
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Sets the key.
     */
    public Number setKey(Number key) {
        return this.key = key;
    }
    
    /* 
     * Overwrites all current children by the new ones in the given list.
     */
    public void setChildren(ArrayList<Edge> newChildren) {
        if (newChildren == null) {
            children.clear();
            
        } else {
            children = new ArrayList<Edge>(newChildren.size());
            children.addAll(newChildren);
        }
    }
    
    /* 
     * Adds a single child.
     * Returns true iff the child was added.
     * Returns false iff the child was not added due to
     * the fact that it already occured.
     */
    public boolean addChild(Edge newChild) {
        if (!children.contains(newChild)) {
            children.add(newChild);
            return true;
            
        } else {
            return false;
        }
    }
    
    /* 
     * Adds all children in the given list.
     * See "boolean addChild(Node)" for more info
     */
    public boolean[] addChildren(ArrayList<Edge> newChildren) {
        boolean[] added = new boolean[newChildren.size()];
        int numAdded = 0;
        
        for (int i = 0; i < children.size() - numAdded; i++) {
            if (this.addChild(newChildren.get(i))) {
                added[i] = true;
                numAdded++;
                
            } else {
                added[i] = false;
            }
        }
        
        return added;
    }
    
    /* 
     * Removes all edges to the children.
     * Returns the number of children removed
     */
    public int removeAllChildren() {
        int size = children.size();
        children.clear();
        return  size;
    }
    
    /* 
     * Removes and returns the current parent of the node.
     */
    public Node removeParent() {
        Node oldParent = parent;
        parent = null;
        return oldParent;
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Get methods
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Returns all children of this node.
     */
    public List<Edge> getChildren() {
        return children;
    }
    
    public List<Edge> getEdges() {
        return getChildren();
    }
    
    /* 
     * Returns the childIterator of this list.
     */
    public ListIterator<Edge> childIterator() {
        return children.listIterator();
    }
    
    /* 
     * Returns the parent of this Node
     */
    public Node getParent() {
        return parent;
    }
    
    /* 
     * Returns its current key.
     */
    public Number getKey() {
        return key;
    }
    
    
    // tmp
    public static void main(String[] args) {
        //TreeNode<Integer,String> a = new Node<Integer,String>(10,"a",);
    }
    
    /*  ----------------------------------------------------------------------------------------------------------------
     * Check methods
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Checks if the given node is directly connected. Returns true iff so.
     */
    public boolean isDirectConnected(Node node) {
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).equals(node)) {
                return true;
            }
        }
        
        return false;
    }
}
