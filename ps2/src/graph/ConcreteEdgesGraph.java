/* Copyright (c) 2015-2017 MIT 6.005/6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of Graph. 
 * 
 */
public class ConcreteEdgesGraph<L> implements Graph<L> {
    
    private final Set<L> vertices = new HashSet<>();
    private final List<Edge<L>> edges = new ArrayList<>();

    // Abstraction function:
    //      AF(vertices, edges) = a directed weighted graph such that 
    //                            each element in vertices is a vertex of the graph,
    //                            and each edge in edges is a directed edge
    //                            of the graph.
    // Representation invariant:
    //     Edges may only exist between valid vertices in vertices.
    // Safety from rep exposure:
    //      All fields are private.
    //      vertices and edges are mutable collections, so 
    //      vertices() implements defensive copying, while 
    //      Map<L, Integer> targets(L source), Map<L, Integer> sources(L target)
    //      return immutable types.
    // 
    
    /**
     * Checks that rep invariant holds.
     */
    private void checkRep(){
        for (Edge<L> edge : edges){
            L begin = edge.getBeginVertex();
            L end = edge.getEndVertex();
            assert vertices.contains(begin);
            assert vertices.contains(end);
        }
    }

    @Override 
    public boolean add(L vertex) {
        boolean added = vertices.add(vertex);
        checkRep();
        return added;
    }
    
    @Override 
    public int set(L source, L target, int weight) {
        checkRep();
        if (weight == 0){
            
            Iterator<Edge<L>> iterator = edges.iterator();
            while (iterator.hasNext()){
                Edge<L> edge = iterator.next();
                L edgeBegin = edge.getBeginVertex();
                L edgeEnd = edge.getEndVertex();
                if (edgeBegin.equals(source) && edgeEnd.equals(target)){
                    int oldWeight = edge.getWeight();
                    iterator.remove();
                    return oldWeight;
                }
            }
            return 0;
        } else {
            vertices.add(source);
            vertices.add(target);
            
            //check if edge already exists
            Edge<L> newEdge = new Edge<L>(source, target, weight);
            Iterator<Edge<L>> iterator = edges.iterator();
            while (iterator.hasNext()){
                Edge<L> edge = iterator.next();
                if (newEdge.equals(edge)){
                    int oldWeight = edge.getWeight();
                    iterator.remove();
                    edges.add(newEdge);
                    return oldWeight;
                }
            }
            edges.add(newEdge);
            return 0;
        }
    }
    
    @Override 
    public boolean remove(L vertex) {
        boolean removed = vertices.remove(vertex);
        if (removed){
            //was in vertices; need to remove all associated edges as well
            Iterator<Edge<L>> iterator = edges.iterator();
            while (iterator.hasNext()){
                Edge<L> edge = iterator.next();
                L beginVertex = edge.getBeginVertex();
                L endVertex = edge.getEndVertex();
                if (vertex.equals(beginVertex) || vertex.equals(endVertex)){
                    iterator.remove();
                }
            }
        }
        checkRep();
        return removed;
    }
    
    @Override 
    public Set<L> vertices() {        
        Set<L> verticesCopy = new HashSet<>();
        for (L vertex : vertices){
            verticesCopy.add(vertex);
        }
        checkRep();
        return verticesCopy;
    }
    
    @Override 
    public Map<L, Integer> sources(L target) {
        Map<L, Integer> sources = new HashMap<>();
        for (Edge<L> edge : edges){
            L endVertex = edge.getEndVertex();
            if (endVertex.equals(target)){
                L beginVertex = edge.getBeginVertex();
                int weight = edge.getWeight();
                sources.put(beginVertex, weight);
            }
        }
        checkRep();
        return Collections.unmodifiableMap(sources);
    }
    
    @Override 
    public Map<L, Integer> targets(L source) {
        Map<L, Integer> targets = new HashMap<>();
        for (Edge<L> edge : edges){
            L beginVertex = edge.getBeginVertex();
            if (beginVertex.equals(source)){
                L endVertex = edge.getEndVertex();
                int weight = edge.getWeight();
                targets.put(endVertex, weight);
            }
        }
        checkRep();
        return Collections.unmodifiableMap(targets);
    }
    
    /**
     * Returns a string with information about what vertices this graph contains.
     * @return string that prints all of the vertices in vertices.
     */
    @Override 
    public String toString(){
        return "Graph with vertices:" + vertices().toString();
    }
   
    
}

/**
 * Edge represents an immutable directed vector from one vertex to another.
 * This class is internal to the rep of ConcreteEdgesGraph.
 *
 */
class Edge<L> {

    private final L beginVertex;
    private final L endVertex;
    private final int weight;
    
    // Abstraction function:
    //      AF(beginVertex, endVertex, weight) = an edge from vertex
    //                                           beginVertex to vertex endVertex 
    //                                           with a distance of weight
    // Representation invariant:
    //    * weight > 0
    //    * (no assumptions about beginVertex and endVertex are to be made, 
    //       besides that they be non-null)
    // Safety from rep exposure:
    //      All fields are private and immutable.
    
    /**
     * Creates a new Edge object with beginVertex begin, endVertex end, and weight weight.
     * @param begin L representing the beginning vertex
     * @param end L representing the end vertex
     * @param weight the weight of the edge
     */
    public Edge(L begin, L end, int weight){
        beginVertex = begin;
        endVertex = end;
        this.weight = weight;
        checkRep();
    }

    /**
     * Checks that the rep invariant is true.
     */
    private void checkRep(){
        assert beginVertex != null;
        assert endVertex != null;
        assert weight > 0;
    }
    
    /**
     * Gets the integer weight associated with this edge.
     * @return weight  
     */
    int getWeight(){
        checkRep();
        return weight;
    }
    
    /**
     * Gets the begin vertex associated with this edge
     * @return L representing beginning vertex
     */
    L getBeginVertex(){
        checkRep();
        return beginVertex;
    }
    
    /**
     * Gets the end vertex associated with this edge.
     * @return L representing ending vertex
     */
    L getEndVertex(){
        checkRep();
        return endVertex;
    }
    
    /**
     * Strengthens built-in Object's toString() method.
     * Returns a string that represents the edge vertices, the direction of the edge (begin --> edge)
     *                       and contains the weight of the edge
     * @return a string with the begin vertex, end vertex, direction of the edge, and the weight of the edge.
     */
    @Override
    public String toString(){
        return beginVertex.toString() + " --> " + endVertex.toString() + " with weight " + weight;
    }
    
    /**
     * Checks if two objects are equal by checking if their beginVertex are .equals()
     * and if their endVertex are .equals().
     * @param thatObject another object to compare this object with
     * @return true if beginVertex and endVertex are .equals() to thatObject's beginVertex
     * and endVertex, respectively; false otherwise.
     */
    @Override 
    public boolean equals(Object thatObject){
        if (!(thatObject instanceof Edge)) return false;
        Edge<?> thatEdge = (Edge<?>) thatObject;
        return ((this.getBeginVertex().equals(thatEdge.getBeginVertex())) &&
                (this.getEndVertex().equals(thatEdge.getEndVertex())));
    }
    
    /**
     * A Vertex object's hashCode is determined by the hashCode of its
     * begin and end vertices.
     * @return hashCode of this object, determined by the sum of the hashCode
     * of this object's beginVertex and endVertex.
     */
    @Override 
    public int hashCode(){
        return getBeginVertex().hashCode()
               + getEndVertex().hashCode();
    }

    
}
