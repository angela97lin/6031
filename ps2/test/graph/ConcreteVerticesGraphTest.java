/* Copyright (c) 2015-2017 MIT 6.005/6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for ConcreteVerticesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteVerticesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteVerticesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteVerticesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteVerticesGraph<String>();
    }

    //--------------------------------TESTING CONCRETEVERTICESGRAPH CLASS BEGINS----------------------------------//
   
    /*
     * Testing strategy for toString(): --> MUST USE CONTAINS TO TEST THIS SINCE DO NOT KNOW ORDER
     *    - vertices.size() = 0
     *    - vertices.size() = 1
     *    - vertices.size() > 1
     *          - order of vertices same
     *          - order of vertices not the same
     */
    //      two graphs with multiple vertices not in same order
    
    //covers case where graph is empty (no vertices, vertices.size() = 0)
    @Test
    public void testToStringEmptyVertices(){
        Graph<String> testGraph = emptyInstance();
        String expected = "Graph with vertices:[]";
        String actual = testGraph.toString();
        assertEquals("toString was not as expected", expected, actual);
    }
    
    //covers case where vertices.size() = 1
    @Test
    public void testToStringOneVertex(){
        Graph<String> testGraph = emptyInstance();
        String vertexName = "namey name";
        testGraph.add(vertexName);
        String expected = "Graph with vertices:["+vertexName+"]";
        String actual = testGraph.toString();
        assertEquals("toString was not as expected", expected, actual);
    }
    
  //covers case where vertices.size() > 1
    @Test
    public void testToStringMoreThanOneVertex(){
        Graph<String> testGraph = emptyInstance();
        String vertexName = "namey name";
        String vertexNameTwo = "second name";
        String vertexNameThree = "im going crazy here?!?@?!@?!?@!?;;;;;;;";
        String vertexNameFour = "four four four four";
        List<String> vertices = Arrays.asList(vertexName, vertexNameTwo, vertexNameThree, vertexNameFour);
        for (String vertex : vertices){
            testGraph.add(vertex);
        }
        String actual = testGraph.toString();
        for (String vertex : vertices){
            assertTrue("toString did not behave as expected", actual.contains(vertex));
        }
    }
    
    //--------------------------------TESTING CONCRETEVERTICESGRAPH CLASS ENDS----------------------------------//
    //--------------------------------TESTING VERTEX CLASS BEGINS----------------------------------//

    /*
     * Testing strategy for Vertex<String>:
     * 
     * Vertex(String name): 
     *      - name.length() = 0
     *      - name.length() > 0
     * -------------------------------------------------------
     * getName():
     *      - name.length() = 0
     *      - name.length() > 0
     * -------------------------------------------------------
     * getsTargets():     
     *      - targets.size() = 0
     *      - targets.size() = 1
     *      - targets.size() > 1
     * -------------------------------------------------------
     * removeTarget(String removeVertex):
     *      - removeVertex in targets
     *      - removeVertex not in targets
     * -------------------------------------------------------
     * addTarget(String target, int weight):
     *      - target already exists in targets
     *      - target does not exist in targets
     * -------------------------------------------------------
     * toString():
     *      - name.length() = 0
     *      - name.length() > 0
     * -------------------------------------------------------
     * equals(Object thatObject):
     *      - not a Vertex object
     *      - has same name (equal)
     *      - has different name (not equal)
     * -------------------------------------------------------
     * hashCode():
     *    - two equivalent Vertex objects have same hashCode
     *    - two Vertex with empty names should have same hashCode
    */

    //covers constructor when name.length() = 0
    //covers name.length() = 0
    @Test 
    public void testGetNameAndConstructorEmpty(){
        String name = "";
        Vertex<String> testVertex = new Vertex<String>(name);
        assertEquals("name of vertex is incorrect", name, testVertex.getName()); 
    }
    
    //covers constructor when name.length() > 0
    //covers name.length() > 0
    @Test 
    public void testGetNameAndConstructorNonEmpty(){
        String name = "v to the tex";
        Vertex<String> testVertex = new Vertex<String>(name);
        assertEquals("name of vertex is incorrect", name, testVertex.getName()); 
    }

    //covers case where targets.size() = 0
    @Test
    public void testGetTargetsZero(){
        String name = "v to the tex";
        Vertex<String> testVertex = new Vertex<String>(name);
        Map<String, Integer> targets = testVertex.getTargets();
        assertEquals("size of targets is incorrect", 0, targets.keySet().size()); 
    }
    
    //covers case where targets.size() > 0
    @Test
    public void testGetTargetsGreaterThanZero(){
        String name = "v to the tex";
        String targetName = "tarGET";
        Vertex<String> testVertex = new Vertex<String>(name);
        int weight = 6;
        testVertex.addTarget(targetName, weight);
        Map<String, Integer> targets = testVertex.getTargets();
        assertEquals("size of targets is incorrect", 1, targets.keySet().size()); 
    }
    

    //covers case where target does not exist in targets
    @Test
    public void testAddTargetDoesNotExist(){
        String name = "v to the tex";
        String targetName = "tarGET";
        Vertex<String> testVertex = new Vertex<String>(name);
        int weight = 6;
        testVertex.addTarget(targetName, weight);
        Map<String, Integer> targets = testVertex.getTargets();
        assertEquals("size of targets is incorrect", 1, targets.keySet().size()); 
        assertEquals("target and weight not correctly mapped", weight, targets.get(targetName).intValue()); 
    }
    
    //covers case where target already exists in targets
    @Test
    public void testAddTargetAlreadyExists(){
        String name = "v to the tex";
        String targetName = "tarGET";
        String failedTargetName = "tarGET";
        Vertex<String> testVertex = new Vertex<String>(name);
        int weight = 6;
        testVertex.addTarget(targetName, weight);
        int newWeight = 10;
        int oldValue = testVertex.addTarget(failedTargetName, newWeight);
        Map<String, Integer> targets = testVertex.getTargets();
        assertEquals("size of targets is incorrect", 1, targets.keySet().size()); 
        assertEquals("old value of target mapping is incorrect", 6, oldValue); 
        assertEquals("target and weight not correctly mapped", 10, targets.get(targetName).intValue()); 
    }

    //covers case where removing target that does not exist in targets
    @Test
    public void testRemoveTargetDoesNotExist(){
        String name = "v to the tex";
        String targetName = "tarjay";
        Vertex<String> testVertex = new Vertex<String>(name);
        int removed = testVertex.removeTarget(targetName);
        assertEquals("removal of target did not produce expected behavior", 0, removed); 
    }
  
    //covers case where target already exists in targets
    @Test
    public void testRemoveTargetsExists(){
        String name = "v to the tex";
        String targetName = "tarjay";
        Vertex<String> testVertex = new Vertex<String>(name);
        int weight = 10;
        testVertex.addTarget(targetName, weight);
        int removed = testVertex.removeTarget(targetName);
        Map<String, Integer> targets = testVertex.getTargets();
        assertEquals("size of targets is incorrect", 0, targets.keySet().size()); 
        assertEquals("removal of target did not produce expected behavior", weight, removed); 
    }
    
    //covers case where name.length() = 0
    @Test 
    public void testToStringEmpty(){
        String name = "";
        Vertex<String> testVertex = new Vertex<String>(name);
        assertEquals("toString of vertex is incorrect", name, testVertex.toString()); 
    }
    
    //covers case where name.length() > 0
    @Test 
    public void testToStringNotEmpty(){
        String name = "not empty";
        Vertex<String> testVertex = new Vertex<String>(name);
        assertEquals("toString of vertex is incorrect", name, testVertex.toString()); 
    }
    
    //covers case where compared with a non-Vertex object
    @Test
    public void testEqualsNotVertex(){
        String vertexName = "its-a-me, vertex";
        Vertex<String> testVertex = new Vertex<String>(vertexName);
        assertEquals("vertex and string should not be equal", false, testVertex.equals(vertexName)); 
    }
    
    //covers case where compared with a Vertex object with same name
    @Test
    public void testEqualsSameVertex(){
        String vertexName = "its-a-me, vertex";
        String alsoVertexName = "its-a-me, vertex";
        Vertex<String> testVertex = new Vertex<String>(vertexName);
        Vertex<String> alsoTestVertex = new Vertex<String>(alsoVertexName);
        assertEquals("two vertices should be equal", true, testVertex.equals(alsoTestVertex)); 
    }
    
    //covers case where compared with a Vertex object with different name
    @Test
    public void testEqualsNotSameVertex(){
        String vertexName = "its-a-me, vertex";
        String notSameVertexName = "its-not-a-me, vertex";
        Vertex<String> testVertex = new Vertex<String>(vertexName);
        Vertex<String> notSameTestVertex = new Vertex<String>(notSameVertexName);
        assertEquals("vertex and string should not be equal", false, testVertex.equals(notSameTestVertex)); 
    }
    
    //covers case where two equal Vertex objects with empty names should have same hashCode
    @Test
    public void testHashCodeSameVertexEmpty(){
        String vertexName = "";
        String alsoVertexName = "";
        Vertex<String> testVertex = new Vertex<String>(vertexName);
        Vertex<String> alsoTestVertex = new Vertex<String>(alsoVertexName);
        assertEquals("two vertices are equal and thus should have same hashCode", testVertex.hashCode(), alsoTestVertex.hashCode()); 
    }
    
    //covers case where two equal Vertex objects should have same hashCode
    @Test
    public void testHashCodeSameVertex(){
        String vertexName = "its-a-me, vertex";
        String alsoVertexName = "its-a-me, vertex";
        Vertex<String> testVertex = new Vertex<String>(vertexName);
        Vertex<String> alsoTestVertex = new Vertex<String>(alsoVertexName);
        assertEquals("two vertices are equal and thus should have same hashCode", testVertex.hashCode(), alsoTestVertex.hashCode()); 
    }
    
    //--------------------------------TESTING VERTEX CLASS ENDS----------------------------------//

}
