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


/* 
 * Under construction!
 * Do not use!
 */
public class ColoredGraphNode<E> extends Node<E> {
    public ColoredGraphNode(boolean isDirected, boolean singleEdgeOnly) {
        super(null);
        edges = new ArrayList<Edge>();
        directed = isDirected;
        this.singleEdgeOnly = singleEdgeOnly;
    }
    
    public ColoredGraphNode(E entry, boolean isDirected, boolean singleEdgeOnly) {
        super(entry);
        edges = new ArrayList<Edge>();
        directed = isDirected;
        this.singleEdgeOnly = singleEdgeOnly;
    }
    
    public ColoredGraphNode(E entry, ArrayList<Edge> edges, boolean isDirected, boolean singleEdgeOnly) {
        super(entry);
        edges = new ArrayList<Edge>();
        this.edges.addAll(edges);
        directed = isDirected;
        this.singleEdgeOnly = singleEdgeOnly;
    }
    
    public ColoredGraphNode(ArrayList<Edge> edges, boolean isDirected, boolean singleEdgeOnly) {
        super(null);
        edges = new ArrayList<Edge>();
        this.edges.addAll(edges);
        directed = isDirected;
        this.singleEdgeOnly = singleEdgeOnly;
    }
    
    /*
     * Makes a deep copy of a node. Copies all edges, whether it is directed
     *    and whether it accepts single edges only. Also copeis the id of a
     *    node. This is the only way to have two similar nodes which have
     *    another content, but are equal using the Node1.equal(Node2) method.
     * The lists uses to store the edges are different, but still contain the
     *    same elements.
     * 
     * 
     *               !!! USE THIS CONSTRUCTOR WITH EXTREME CARE !!!!
     *            Only use it when you know FOR SURE what you're doing.
     * 
     */
    @Deprecated
    public ColoredGraphNode(ColoredGraphNode<E> cgn) {
        super(cgn.getEntry());
        edges.addAll(cgn.getEdges());
        directed = cgn.isDirected();
        singleEdgeOnly = cgn.usesSingleEdgesOnly();
        id = cgn.getId();
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Set/add/remove methods
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Overwrites all current Edges with colors by the new Edges in the given list.
     * XXXX
     * Overwrites all current edges with colors by the new ones in the given list.
     * The first element of Object[] is always a Node and the second an int
     * representing the color.
     * If the length is 2 or longer, only the first two fields will be used.
     * Note:
     * If the length is 0 or 1, or the the first element if not a Node or the 
     * second element is not an int, then the enty is ignored.
     * Throws a NullPointerException if the list equal to null.
     */
    public void setEdges(ArrayList<Edge> newEdges) {
        edges.clear();
        edges.addAll(newEdges);
    }
    
    /* 
     * Overwrites all current edges with colors by the new ones in the given lists.
     * If the lists are unequal in length, add only the complete edges to the list.
     * Throws a NullPointerException if either of the given lists are equal to null.
     *//*
    public int setEdges(ArrayList<Edge> nodes) {
        int size = (nodes.size() >= colors.size() ? nodes.size() : colors.size());
        boolean[] added = new boolean[edges.size()];
        this.edges = new ArrayList<Edge>(edges.size());
        
        for (int i = 0; i < size; i++) {
            this.edges.add(new Edge(this, edges.get(i), colors.get(i), directed));
        }
        
        return size;
    }*/
    /* 
     * Adds a single colored edge.
     * If singleEdgeOnly, then ignores the edge and returns false
     *     iff the edge with that color already exists.
     * Adds the edge and returns true iff not singleEdgeOnly
     *     or the edge is not yet in the list.
     */
    @SuppressWarnings("unchecked") // For creating an edge
    public boolean addEdge(Node newEdge, Integer color) {
        if (!singleEdgeOnly) {
            edges.add(new Edge<ColoredGraphNode, Node>(this, newEdge, color, directed));
            return true;
            
        } else {
            for (int i = 0; i < edges.size(); i++) {
                if (edges.get(i).getDestNode().equals(newEdge) ||
                    edges.get(i).getColor().equals(color)) {
                    return false;
                }
            }
            
            edges.add(new Edge<ColoredGraphNode, Node>(this, newEdge, color, directed));
            return true;
        }
    }
    
    /* 
     * Adds all edges with a color from the list.
     * See "addEdge(Node, int)" for more information.
     * Returns a boolean value for each element determining whether it was added or not.
     * Throws a NullPointerException iff
     *  - newEdged == null
     *  - there exists an element in newEdges which has less then two elements.
     * Ignores the elements in newEdges in which the first element is not an instance of
     *     Node and the second element is not an instance of Integer.
     */
    public boolean[] addAllEdges(ArrayList<Object[]> newEdges) {
        boolean[] returnValue = new boolean[newEdges.size()];
        
        for (int i = 0; i < newEdges.size(); i++) {
            if (newEdges.get(i)[0] instanceof Node &&
                newEdges.get(i)[1] instanceof Integer) {
                
                returnValue[i] = addEdge((Node) newEdges.get(i)[0], (Integer) newEdges.get(i)[1]);
            } else {
                returnValue[i] = false;
            }
        }
        
        return returnValue;
    }
    
    /* 
     * Removes all edges from the current node to the given node.
     * Returns the number of removed edges.
     */
    public int removeEdge(Node removeEdge) {
        int removed = 0;
        
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).getDestNode().equals(removeEdge)) {
                edges.remove(i--);
                removed++;
            }
        }
        
        return removed;
    }
    
    /* 
     * Removes all edges of the given color.
     * Returns the number of removed edges
     */
    public int removeEdgeColored(Node removeEdge, Integer removeColor) {
        int removed = 0;
        
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).getDestNode().equals(removeEdge) && 
                edges.get(i).getColor().equals(removeColor)) {
                edges.remove(i--);
                removed++;
            }
        }
        
        return removed;
    }
    
    /* 
     * Removes all edges of the given color.
     */
    public int removeColor(Integer removeColor) {
        int removed = 0;
        
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).getColor().equals(removeColor)) {
                edges.remove(i--);
                removed++;
            }
        }
        
        return removed;
    }
    
    /* 
     * Removes all edges from the current node to the given nodes.
     * Returns the number of nodes removed.
     */
    public int removeAllEdges(ArrayList<Node> removeEdges) {
        int removed = 0;
        
        for (int i = 0; i < edges.size(); i++) {
            for (int j = 0; j < removeEdges.size(); j++) {
                if (edges.get(i).getDestNode().equals(removeEdges.get(j))) {
                    edges.remove(i--);
                    removed++;
                    break;
                }
            }
        }
        
        return removed;
    }
    
    /* 
     * Removes all edges with a given color from the current node to the given nodes.
     * Returns the number of nodes removed.
     */
    public int removeAllEdgesColored(ArrayList<Edge> removeEdges) {
        int removed = 0;
        
        for (int i = 0; i < edges.size(); i++) {
            for (int j = 0; j < removeEdges.size(); j++) {
                if ((edges.get(i).getColor() == null || edges.get(i).getColor().equals(removeEdges.get(j).getColor())) && 
                    (edges.get(i).getDestNode().equals(removeEdges.get(j).getDestNode()) || 
                     (edges.get(i).isDirected() && removeEdges.get(j).isDirected() && 
                      edges.get(i).getDestNode().equals(removeEdges.get(j).getSourceNode()) && 
                      edges.get(i).getSourceNode().equals(removeEdges.get(j).getDestNode())) ) ) {
                    edges.remove(i--);
                    removed++;
                    break;
                }
            }
        }
        
        return removed;
    }
    
    /* 
     * Removes all edges.
     * Returns the number of nodes removed.
     */
    public int removeAllEdges() {
        int size = edges.size();
        edges.clear();
        return size;
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Get methods
     * ----------------------------------------------------------------------------------------------------------------
     */
    
    /* 
     * Returns the edges from this node to other nodes.
     * The compiler might complain about an "unchecked warning".
     * Just use @SuppressWarnings("unchecked") to ignore them
     */
    public List<Edge> getEdges() {
        return edges;
    }
    
    
    /*  ----------------------------------------------------------------------------------------------------------------
     * Check methods
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Checks if the given node is directly connected. Returns true iff so.
     */
    public boolean isDirectConnected(Node node) {
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).getDestNode().equals(node)) {
                return true;
            }
        }
        
        return false;
    }
    
    /* 
     * Check if the given node is directly connected with an edge of the given color.
     * Returns true iff so.
     */
    public boolean isDirectConnected(Node node, Integer color) {
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).getSourceNode().equals(node) && edges.get(i).getColor() == color) {
                return true;
            }
        }
        
        return false;
    }
    
    /* 
     * Overrides the equal(Object) method of java.lang.Object.
     * Compares two objects for equality. Returns true iff:
     *  - The pointer to the object is equal
     *  - obj is an instance of ColoredGraphNode and either:
     *     o The id's are equal (note 1).
     *     o The number of edges is equal and
     *       the equals(Object) method of the lists containing them yields true.
     * 
     * Note 1:
     * It is possible for two nodes to be not entirely equal, but
     * equals(Object) will still yield true since the id's are equal.
     */
    @Override
    public boolean equalsExtraCondition(Node node) {
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i) != node.getEdges().get(i)) { // .equals is not used here to prevent infinite loops.
                return false;
            }
        }
        
        return false;
    }
    
    @Override
    public String toString() {
        String line = this.getClass().getName()
            + "[directed=" + directed
            + ", singleEdgeOnly=" + singleEdgeOnly
            + ", Edges={";
        
        for (int i = 0; i < edges.size(); i++) {
            line += "{edgeType=" + edges.get(i).getDestNode().getClass().getSimpleName() + ", color=" + edges.get(i).getColor() + "}";
            
            if (i + 1 != edges.size()) {
                line += ", ";
            }
        }
        
        line += "}]";
        return line;
    }
    
    
    public static void main(String[] args) {
        ColoredGraphNode cgn = new ColoredGraphNode(true, true);
        cgn.addEdge(new ColoredGraphNode(true, true), 0);
        System.out.println(cgn.toString());
    }
}
