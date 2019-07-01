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
package tools.dataStructures.node;


// Tools imports
import tools.dataStructures.Node;


// Java imports
import java.util.List;
import java.util.ArrayList;


/* 
 * Under construction.
 * Do not use!
 */
public class GraphNode<E> extends Node<E> {
    private final boolean directed;
    
    public GraphNode(E entry, boolean isDirected) {
        super(entry);
        System.err.println("Unfinished class!");
        directed = isDirected;
        edges = new ArrayList<Edge>();
        
    }
    
    public GraphNode(E entry, List<Edge> edges, boolean isDirected) {
        super(null);
        System.err.println("Unfinished class!");
        directed = isDirected;
        
        this.edges = new ArrayList<Edge>(edges.size());
        this.edges.addAll(edges);
    }
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Set/add/remove methods
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Overwrites all current edges by the new ones in the given list.
     */
    public void setEdges(List<Edge> edges) {
        this.edges = new ArrayList<Edge>(edges.size());
        this.edges.addAll(edges);
    }
    
    /* 
     * Adds a single edge iff it does not yet exists in the list.
     */
    public boolean addEdge(Edge newEdge) {
        if (!edges.contains(newEdge)) {
            edges.add(newEdge);
            return true;
            
        } else {
            return false;
        }
    }
    
    /* 
     * Adds all edges in the given list.
     * If an edge already exists, ignore it.
     * Returns an boolean array which gives information on which elements were
     * added and which ones ignored.
     */
    public boolean[] addAllEdges(List<Edge> newEdges) {
        boolean[] added = new boolean[newEdges.size()];
        int numAdded = 0;
        
        
        for (int i = 0; i < newEdges.size() - numAdded; i++) {
            if (this.addEdge(newEdges.get(i))) {
                added[i] = true;
                numAdded++;
                
            } else {
                added[i] = false;
            }
        }
        
        return added;
    }
    
    /* 
     * Removes all edges from this node to the given node.
     * Returns the number of removed instances.
     * Note:
     * There are always 0 or 1 edges removed due to the implementation.
     */
    public int removeEdge(Node removeEdge) {
        return (edges.remove(removeEdge) ? 1 : 0);
    }
    
    public int removeAllEdges(ArrayList<Node> removeEdges) {
        int removed = 0;
        
        for (int i = 0; i < removeEdges.size(); i++) {
           // if (edges
            
            for (int j = 0; j < removeEdges.size(); j++) {
                if (edges.get(i).equals(removeEdges.get(j))) {
                    edges.remove(i--);
                    removed++;
                    break;
                }
            }
        }
        
        return removed;
    }
    
    public int removeAllEdges() {
        int elems = edges.size();
        edges.clear();
        return elems;
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Get methods
     * ----------------------------------------------------------------------------------------------------------------
     */
    public List<Edge> getEdges() {
        return edges;
    }
    
    public boolean isConnected(Node node) {
        return edges.contains(node);
    }
    
    public boolean isDirected() {
        return directed;
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
            if (edges.get(i).equals(node)) {
                return true;
            }
        }
        
        return false;
    }
}