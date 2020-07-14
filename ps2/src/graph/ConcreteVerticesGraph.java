/* Copyright (c) 2015-2017 MIT 6.005/6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
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
public class ConcreteVerticesGraph<L> implements Graph<L> {
    
    private final List<Vertex<L>> vertices = new ArrayList<>();
    
    // Abstraction function:
    //    AF(vertices) = a directed weighted graph with vertices vertices
    // Representation invariant:
    //    Each vertex in vertices is distinct.
    // Safety from rep exposure:
    //    All fields are private.
    //    vertices is a mutable object so vertices() makes a defensive copy
    //    to avoid sharing with clients.
 
    /**
     * Checks that the rep invariant holds.
     */
    private void checkRep(){
        //make sure that all vertices are different in vertices
        Set<Vertex<L>> verticesSet = new HashSet<>();
        for (Vertex<L> vertex : vertices){
            assert verticesSet.add(vertex);
        }     
    }
    
    @Override 
    public boolean add(L vertex) {
        Vertex<L> newVertex = new Vertex<>(vertex);
        for (Vertex<L> alreadyVertex : vertices){
            L alreadyVertexName = alreadyVertex.getName();
            if (alreadyVertexName.equals(vertex)){
                checkRep();
                return false;
            }
        }
        boolean added = vertices.add(newVertex);
        checkRep();
        return added;
    }
    
    @Override 
    public int set(L source, L target, int weight) {
        if (weight == 0){
            //remove edge, is exists
            for (Vertex<L> vertex : vertices){
                L vertexName = vertex.getName();
                if (vertexName.equals(source)){
                    return vertex.removeTarget(target);
                }
            }
            checkRep();
            return 0;
        } 
        add(source);
        add(target);
        for (Vertex<L> vertex : vertices){
            L vertexName = vertex.getName();
            if (vertexName.equals(source)){
                checkRep();
                return vertex.addTarget(target, weight);
            }
        }
        checkRep();
        return 0;
    }
    
    @Override 
    public boolean remove(L vertex) {
        Iterator<Vertex<L>> iterator = vertices.iterator();
        while (iterator.hasNext()){
            Vertex<L> currentVertex = iterator.next();
            L currentVertexName = currentVertex.getName();
            //if current vertex exists, remove from vertices
            //and also remove all associated edges
            if (vertex.equals(currentVertexName)){
                iterator.remove();
                for (Vertex<L> otherVertex : vertices){
                    otherVertex.removeTarget(vertex);
                }
                return true;
            }
        }
        return false;
    }
    
    @Override 
    public Set<L> vertices() {
        Set<L> verticesSet = new HashSet<>();
        for (Vertex<L> vertex : vertices){
            L vertexName = vertex.getName();
            verticesSet.add(vertexName);
        }
        return verticesSet;
    }
    
    @Override 
    public Map<L, Integer> sources(L target) {
        HashMap<L, Integer> sources = new HashMap<L, Integer>();
        for (Vertex<L> vertex : vertices){
            //go through each vertex's "target" field.
            //if target shows up, that vertex is a source.
            Map<L, Integer> targets = vertex.getTargets();
            if (targets.containsKey(target)){
                L vertexName = vertex.getName();
                Integer weight = targets.get(target);
                sources.put(vertexName, weight);
            }
        }
        return sources;
    }
    
    @Override 
    public Map<L, Integer> targets(L source) {
        for (Vertex<L> vertex : vertices){
            L vertexName = vertex.getName();
            if (vertexName.equals(source)){
                return vertex.getTargets();
            }
        }
        return new HashMap<L, Integer>();
    }
   
    /**
     * Returns a string with vertex labels of the graph.
     * @return the vertex labels for every vertex of the graph
     */
    @Override
    public String toString(){
       return "Graph with vertices:" + vertices.toString();
    }
}

/**
 * 
 * Mutable type representing a vertex in a graph.
 * This class is internal to the rep of ConcreteVerticesGraph.
 * 
 */
class Vertex<L> {
    
    private final L name;
    private final Map<L, Integer> targets;
    
    // Abstraction function:
    //     AF(name, targets) = a vertex represented with name label, 
    //                       with edges directed to all other vertices in targets     
    // Representation invariant:
    //     - Each vertex in targets must be distinct.
    //     - Each vertex in targets must be mapped to a positive Integer (representing weight
    //    of edge between the two vertices)
    //     - L must be an immutable type
    // Safety from rep exposure:
    //     - All fields are final and private.
    //     - name is returned from getName(), but is an immutable type.
    //     - getTargets() uses defensive copying to return a copy of the targets field.
    
    /**
     * Creates a new Vertex object with name name
     * @param name name field of new Vertex created
     */
    public Vertex(L name){
       this.name = name;
       targets = new HashMap<L, Integer>();
       checkRep();
    }
    
    /**
     * Checks that the rep invariant holds.
     */
    private void checkRep(){
        Set<L> checkSet = new HashSet<L>();
        for (L target : targets.keySet()){
            assert (checkSet.add(target));
            assert (targets.get(target).intValue() > 0);
        }
    }
    
    /**
     * Returns name of vertex.
     * @return name representing name of vertex
     */
    public L getName(){
        return name;
    }
    
    /**
     * Returns the targets of this vertex; each target mapped to the weight of the edge
     * from this vertex to the target
     * @return map of targets mapped to the their edge weights
     */
    public Map<L, Integer> getTargets(){
        Map<L, Integer> targetsCopy = new HashMap<L, Integer>();
        for ( L vertex : targets.keySet()){
            int weight = targets.get(vertex);
            targetsCopy.put(vertex, weight);
        }
        return targetsCopy;
    }

    /**
     * Removes a target from vertex's list of targets, if it exists
     * @param removeVertex vertex to remove
     * @return the weight of the edge from vertex to removeVertex, or 
     * 0 if removeVertex was not a target
     */
    public int removeTarget(L removeVertex){
        checkRep();
        Iterator<L> iterator = targets.keySet().iterator();
        while (iterator.hasNext()){
            L vertex = iterator.next();
            if (vertex.equals(removeVertex)){
                int oldWeight = targets.get(vertex);
                iterator.remove();
                return oldWeight;
            }
        }
        return 0;
    }
    
    /**
     * Adds a target from vertex's list of targets, if it was not 
     * previously in targets. Otherwise, updates the weight
     * of the existing weight mapped in targets with new weight
     * @param target a label
     * @param weight weight of the edge between this vertex and target
     * @return the old weight of the edge between this vertex and target if
     *         it existed, or 0 if target did not exist
     */
    public int addTarget(L target, int weight){
        if (targets.containsKey(target)){
            int oldValue = targets.put(target, weight);
            checkRep();
            return oldValue;
        }
        targets.put(target, weight);
        checkRep();
        return 0;
    }
   
  
    /**
     * Strengthens built-in Object's toString() method.
     * Returns the stringified version of our name
     * @return a string representing name label of this vertex
     */
   @Override
   public String toString(){
       return name.toString();
   }
   
   /**
    * Two Vertex objects are considered equal if they have the same name.
    * @param thatObject another object to compare this object to
    * @return true if thatObject is a Vertex object, and if thatObject's name is
    * .equals() this Vertex's name.
    */
   @Override 
   public boolean equals(Object thatObject){
       if (!(thatObject instanceof Vertex<?>)) return false;
       Vertex<?> thatVertex = (Vertex<?>) thatObject;
       return this.getName().equals(thatVertex.getName());
   }
   
   /**
    * A Vertex object's hashCode is determined by the hashCode of its name.
    * @return hashCode of this Vertex object, which is simply the hashCode
    * of its name.
    */
   @Override 
   public int hashCode(){
       return getName().hashCode();
   }
}
