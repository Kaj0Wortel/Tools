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
//todo
package tools.dataStructures;


// Tools imports
import tools.dataStructures.node.Edge;


// Java imports
import java.util.List;


/* 
 * Under construction.
 * Do not use!
 */
public abstract class Node<E> implements Cloneable {
    private static Long idCounter = Long.MIN_VALUE;
    protected Long id;
    
    protected boolean directed;
    protected boolean singleEdgeOnly;
    protected List<Edge> edges;
    
    protected E entry;
    
    public Node(E entry) {
        id = createId();
        this.entry = entry;
    }
    
    public Node(E entry, Long id) {
        this.id = id;
        this.entry = entry;
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Set/add/remove methods
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Creates a new and unused id.
     * Can create a maximum of 2^64 different id's.
     */
    private static synchronized long createId() {
        return idCounter++;
    }
    
    /* 
     * Sets the entry for this node.
     */
    public E setEntry(E entry) {
        return this.entry = entry;
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Get methods
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Returns whether the graph is directed or not
     */
    public boolean isDirected() {
        return directed;
    }
    
    /* 
     * Returns whether the graph uses single edges between nodes
     */
    public boolean usesSingleEdgesOnly() {
        return singleEdgeOnly;
    }
    
    /* 
     * Returns the entry of this node.
     */
    public E getEntry() {
        return entry;
    }
    
    /* 
     * Returns the id of this node.
     */
    protected long getId() {
        return id;
    }
    
    public abstract List<Edge> getEdges();
    
    /*  ----------------------------------------------------------------------------------------------------------------
     * Check methods
     * ----------------------------------------------------------------------------------------------------------------
     */
    public abstract boolean isDirectConnected(Node node);
    
    @Override
    @SuppressWarnings("unchecked") // Some compiler issues with the getEdges return method
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
            
        } else if (obj.getClass().equals(obj.getClass())) {
            Node node = (Node) obj;
            
            if (this.isDirected() == node.isDirected() && 
                this.usesSingleEdgesOnly() == node.usesSingleEdgesOnly()) {
                
                if (this.getId() == node.getId()) {
                    return true;
                }
                
                List nodeEdges = node.getEdges();
                List thisEdges = node.getEdges();
                
                if (thisEdges.size() != nodeEdges.size()) {
                    return false;
                    
                } else if (thisEdges.equals(nodeEdges)) {
                    return true;
                    
                } else {
                    return equalsExtraCondition(node);
                }
            }
        }
        return false;
    }
    
    /* 
     * Method which is supposed to be overridden when some extra
     *    conditions were to be added to the equals(Object) method.
     */
    protected boolean equalsExtraCondition(Node node) {
        return true;
    }
    
    @Override
    public String toString() {
        String line = this.getClass().getName()
            + "[directed=" + directed
            + ", singleEdgeOnly=" + singleEdgeOnly
            + ", Edges={";
        
        for (int i = 0; i < edges.size(); i++) {
            line += "{edgeType=" + edges.get(i).getClass().getSimpleName();
        }
        
        line += "}]";
        return line;
    }
}
