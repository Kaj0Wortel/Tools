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


/* 
 * Under construction.
 * Do not use!
 */
public class Edge<N1 extends Node, N2 extends Node> {
    N1 sourceNode = null;
    N2 destNode = null;
    Integer color = null;
    boolean directed;
    
    protected Edge(N1 sourceNode, N2 destNode, boolean directed) {
        this.sourceNode = sourceNode;
        this.destNode = destNode;
        this.directed = directed;
    }
    
    protected Edge(N1 sourceNode, N2 destNode, Integer color, boolean directed) {
        this.sourceNode = sourceNode;
        this.destNode = destNode;
        this.color = color;
        this.directed = directed;
    }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Get methods
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Returns the source node
     */
    public N1 getSourceNode() {
        return (N1) sourceNode;
    }
    
    /* 
     * Returns the destinition node
     */
    public N2 getDestNode() {
        return (N2) destNode;
    }
    
    /* 
     * Returns true iff colored edges are used
     */
    public boolean usesColoredEdges() {
        return (color != null);
    }
    
    /* 
     * Returns true iff the edge is directed
     */
    public boolean isDirected() {
        return directed;
    }
    
    /* 
     * Returns the color of the node.
     * Returns null iff no color set.
     */
    public Integer getColor() {
        return color;
    }
    
    public static boolean equalColor(Edge e1, Edge e2) {
        Integer c1 = e1.getColor();
        Integer c2 = e2.getColor();
        
        if (c1 == null && c2 == null) {
            return true;
            
        } else if ((c1 == null && c2 != null) || 
                   (c1 != null && c2 == null)) {
            return false;
            
        } else {
            return (c1 == c2);
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Edge) {
            Edge e = (Edge) obj;
            
            if (this.isDirected() != e.isDirected()) {
                return false;
                
            } else if (!Edge.equalColor(this, e)) {
                return false;
                
            } else {
                Node sn1 = this.getSourceNode();
                Node sn2 = e.getSourceNode();
                Node dn1 = this.getDestNode();
                Node dn2 = e.getDestNode();
                
                if (this.isDirected()) {
                    if ((sn1.equals(sn2) && dn1.equals(dn2)) || 
                        (sn1.equals(dn2) && dn1.equals(sn2))) {
                        return true;
                    }
                    
                } else {
                    if (sn1.equals(sn2) && dn1.equals(dn2)) {
                        return true;
                    }
                }
                
            }
        }
        
        return false;
    }
    
    @Override
    public String toString() {
        return this.getClass().getName()
            + "[source=" + sourceNode.toString()
            + ", destination=" + destNode.toString()
            + ", directed=" + directed
            + ", color=" + color + "]";
    }
}