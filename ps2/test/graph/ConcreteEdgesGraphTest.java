/* Copyright (c) 2015-2017 MIT 6.005/6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * Tests for ConcreteEdgesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteEdgesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteEdgesGraphTest extends GraphInstanceTest {
    
    //--------------------------------TESTING CONCRETEEDGEGRAPH CLASS BEGINS----------------------------------//

    /*
     * Provide a ConcreteEdgesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteEdgesGraph<String>();
    }
    
    /*
     * Testing Strategy for ConcreteEdgesGraph:
     * 
     * toString():
     *      vertices:
     *          - empty (length 0)
     *          - has length 1
     *          - has length > 1
     *      Note: I do not choose to test edges because I do not expose edges.
     */

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
   
  
    //--------------------------------TESTING CONCRETEEDGEGRAPH CLASS ENDS----------------------------------//
    
    //--------------------------------TESTING EDGE CLASS BEGINS----------------------------------//
    /*
     * Testing strategy for Edge:
     * 
     * getBeginVertex():
     *      - beginVertex.length() = 0
     *      - beginVertex.length() > 0
     * -------------------------------------------------------
     * getEndVertex():
     *      - endVertex.length() = 0
     *      - endVertex.length() > 0
     * -------------------------------------------------------     
     * getWeight():
     *      - weight = 1
     *      - weight > 1
     * -------------------------------------------------------
     * toString():
     *      - beginVertex and endVertex are .equals()
     *      - beginVertex and endVertex are not .equals()
     *      - weight = 1
     *      - weight > 1
     * -------------------------------------------------------
     * equals(Object thatObject):
     *      - not a Edge object
     *   Edges with....
     *   beginVertex:
     *      - same
     *      - different
     *   endVertex:
     *      - same
     *      - different
     *   weight:
     *      - same
     *      - different
     * -------------------------------------------------------     
     * hashCode():
     *      - two equivalent Edges have same hashCode
     *      - two Edges with empty beginVertex/endVertex have same hashCode
     */
    
    //covers case where beginVertex.length() = 0
    @Test
    public void testGetBeginVertexLengthZero(){
        String begin = "";
        String end = "end";
        int weight = 6;
        Edge<String> testEdge = new Edge<>(begin, end, weight);
        assertTrue("expected beginVertex to be empty string", testEdge.getBeginVertex().equals(""));
    }
    
    //covers case where beginVertex.length() > 0
    @Test
    public void testGetBeginVertexLengthNonzero(){
        String begin = "beginning";
        String end = "end";
        int weight = 6;
        Edge<String> testEdge = new Edge<>(begin, end, weight);
        assertTrue("expected beginVertex to be string with value 'beginning'", testEdge.getBeginVertex().equals("beginning"));       
    }
    
    //covers case where endVertex.length() = 0
    @Test
    public void testGetEndVertexLengthZero(){
        String begin = "beginning";
        String end = "";
        int weight = 6;
        Edge<String> testEdge = new Edge<>(begin, end, weight);
        assertTrue("expected endVertex to be empty string", testEdge.getEndVertex().equals(""));
    }
    
    //covers case where endVertex.length() > 0
    @Test
    public void testGetEndVertexLengthNonzero(){
        String begin = "begin";
        String end = "endendend";
        int weight = 6;
        Edge<String> testEdge = new Edge<>(begin, end, weight);
        assertTrue("expected endVertex to be string with value 'endendend'", testEdge.getEndVertex().equals("endendend"));
    }
    
    //covers case where weight = 1
    @Test
    public void testGetWeightOne(){
        String begin = "begin";
        String end = "endendend";
        int weight = 1;
        Edge<String> testEdge = new Edge<>(begin, end, weight);
        assertEquals("expected weight of 1", 1, testEdge.getWeight());
    }
    
    //covers case where weight > 1
    @Test
    public void testGetWeightGreaterThanOne(){
        String begin = "begin";
        String end = "endendend";
        int weight = 6;
        Edge<String> testEdge = new Edge<>(begin, end, weight);
        assertEquals("expected weight of 6", 6, testEdge.getWeight());
    }
    
    //covers case where beginVertex and endVertex are .equals()
    @Test
    public void testToStringBeginEqualsEnd(){
        String begin = "hello";
        String end = "hello";
        int weight = 1;
        Edge<String> testEdge = new Edge<>(begin, end, weight);
        String expected = "hello --> hello with weight 1";
        assertTrue("expected toString to return "+expected, expected.equals(testEdge.toString()));
    }
    
    //covers case where beginVertex and endVertex are not .equals()
    @Test
    public void testToStringBeginNotEqualsEnd(){
        String begin = "hello";
        String end = "gudbye this pset";
        int weight = 1;
        Edge<String> testEdge = new Edge<>(begin, end, weight);
        String expected = "hello --> gudbye this pset with weight 1";
        assertTrue("expected toString to return "+expected, expected.equals(testEdge.toString()));
    }
    
    //covers case where weight = 1
    @Test
    public void testToStringWeightOne(){
        String begin = "hello";
        String end = "gudbye this pset";
        int weight = 1;
        Edge<String> testEdge = new Edge<>(begin, end, weight);
        String expected = "hello --> gudbye this pset with weight 1";
        assertTrue("expected toString to return "+expected, expected.equals(testEdge.toString()));
        
    }
    
    //covers case where weight > 1
    @Test
    public void testToStringWeightGreaterThanOne(){
        String begin = "hello";
        String end = "gudbye this pset";
        int weight = 10;
        Edge<String> testEdge = new Edge<>(begin, end, weight);
        String expected = "hello --> gudbye this pset with weight 10";
        assertTrue("expected toString to return "+expected, expected.equals(testEdge.toString()));
    }

    //covers case where not an Edge object compared
    @Test 
    public void testEqualsNotEdgeObject(){
        String begin = "hello";
        String end = "gudbye this pset";
        int weight = 1;
        Edge<String> testEdge = new Edge<>(begin, end, weight);
        String notEqual = "not equal string";
        assertFalse("expected Edge not equal String object", testEdge.equals(notEqual));
    }
    
    //covers case with Edge object that has same beginVertex and same endVertex 
    //                                      same weight
    @Test
    public void testEqualsSameBeginVertexSameEndVertex(){
        String begin = "hello";
        String alsoBegin = "hello";
        String end = "gudbye this pset";
        String alsoEnd = "gudbye this pset";
        int weight = 1;
        Edge<String> testEdge = new Edge<>(begin, end, weight);
        Edge<String> equalTestEdge = new Edge<>(alsoBegin, alsoEnd, weight);
        assertTrue("expected two Edges to be equal", testEdge.equals(equalTestEdge));
    }
    
    //covers case with Edge object that has same beginVertex and same endVertex
    //                                      different weight
    @Test
    public void testEqualsSameBeginVertexSameEndVertexDifferentWeight(){
        String begin = "hello";
        String end = "gudbye this pset";
        int weight = 1;
        int secondWeight = 100;
        Edge<String> testEdge = new Edge<>(begin, end, weight);
        Edge<String> equalTestEdge = new Edge<>(begin, end, secondWeight);
        assertTrue("expected two Edges to be equal", testEdge.equals(equalTestEdge));
    }
    
    //covers case with Edge object that has same beginVertex but different endVertex
    @Test
    public void testEqualsSameBeginVertexDifferentEndVertex(){
        String begin = "hello";
        String end = "gudbye this pset";
        String secondEnd = "just leave";
        int weight = 6;
        Edge<String> testEdge = new Edge<>(begin, end, weight);
        Edge<String> equalTestEdge = new Edge<>(begin, secondEnd, weight);
        assertFalse("expected two Edges to NOT be equal", testEdge.equals(equalTestEdge));  
    }
    
    //covers case with Edge object that has different beginVertex but same endVertex
    @Test
    public void testEqualsDifferentBeginVertexSameEndVertex(){
        String begin = "hello";
        String secondBegin = "hello hello";
        String end = "gudbye this pset";
        int weight = 6;
        Edge<String> testEdge = new Edge<>(begin, end, weight);
        Edge<String> equalTestEdge = new Edge<>(secondBegin, end, weight);
        assertFalse("expected two Edges to NOT be equal", testEdge.equals(equalTestEdge));  
    }
    
    //covers case with Edge object that has different beginVertex and different endVertex
    @Test
    public void testEqualsDifferentBeginVertexDifferentEndVertex(){
        String begin = "hello";
        String secondBegin = "hello from the other side";
        String end = "gudbye this pset";
        String secondEnd = "guuuudbye";
        int weight = 6;
        Edge<String> testEdge = new Edge<>(begin, end, weight);
        Edge<String> equalTestEdge = new Edge<>(secondBegin, secondEnd, weight);
        assertFalse("expected two Edges to NOT be equal", testEdge.equals(equalTestEdge));  
    }
    
    //covers case where two equivalent Edges have same hashCode
    @Test
    public void testHashCodeEmpty(){
        String begin = "";
        String end = "";
        int weight = 6;
        Edge<String> testEdge = new Edge<>(begin, end, weight);
        Edge<String> equalTestEdge = new Edge<String>(begin, end, weight);
        assertTrue("expected two Edges to have same hashCode", testEdge.hashCode() == equalTestEdge.hashCode());  
    }
    
    //covers case where two Edges with empty beginVertex/endVertex have same hashCode
    @Test
    public void testHashCodeEqualEmpty(){
        String begin = "hello";
        String secondBegin = "hello";
        String end = "gudbye this pset";
        String secondEnd = "gudbye this pset";
        int weight = 6;
        Edge<String> testEdge = new Edge<String>(begin, end, weight);
        Edge<String> equalTestEdge = new Edge<String>(secondBegin, secondEnd, weight);
        assertTrue("expected two Edges to have same hashCode", testEdge.hashCode() == equalTestEdge.hashCode());  
    }
    

    //--------------------------------TESTING EDGE CLASS ENDS----------------------------------//
}
