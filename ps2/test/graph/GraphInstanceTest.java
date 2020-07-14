/* Copyright (c) 2015-2017 MIT 6.005/6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for instance methods of Graph.
 * 
 * <p>PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain Graph instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */

public abstract class GraphInstanceTest {
    
    /*
     * Testing Strategy for empty():
     *      NOTE: no inputs, only output is empty graph
     *            validate with vertices()
     * ------------------------------------------------------------------------
     * Testing Strategy for add(L vertex):
     *   vertex:
     *      - add vertex to empty graph
     *      - add vertex that is not already in graph
     *      - add vertex that already is in graph   
     * ------------------------------------------------------------------------
     * Testing Strategy for remove(L vertex):
     *   vertex:
     *      - remove from empty graph
     *      - (try to) remove vertex that is not in graph 
     *      - remove vertex that is in graph and has 0 edges to other vertices
     *      - remove vertex that is in graph and has 1 edge to other vertices
     *      - remove vertex that is in graph and has > 1 edges to other vertices
     *      - remove vertex that is in graph and has 0 edges from other vertices
     *      - remove vertex that is in graph and has 1 edge from other vertices
     *      - remove vertex that is in graph and has > 1 edges from other vertices 
     *   output:
     *      - nonempty graph
     *      - empty graph
     * ------------------------------------------------------------------------
     * Testing Strategy for set(L source, L target, int weight):
     *   weight:
     *      - 0 (remove edge)
     *      - greater than 0 (add edge)
     *   source:
     *      - already exists in graph
     *      - does not exist in graph
     *   target:
     *      - already exists in graph
     *      - does not exist in graph
     *   relationship between source and target:
     *      - edge already exists in graph
     *      - edge does not exist in graph
     *      - source and target are same vertex
     * ------------------------------------------------------------------------
     * Testing Strategy for sources(L target):
     *   target:
     *      - target does not exist in graph
     *      - target does exist in graph and has no sources
     *      - target does exist in graph and has one source
     *      - target does exist in graph and has multiple sources
     * ------------------------------------------------------------------------
     * Testing Strategy for targets(L source):
     *   source:
     *      - source does not exist in graph
     *      - source does exist in graph and has no targets
     *      - source does exist in graph and has one target
     *      - source does exist in graph and has multiple targets
     * ------------------------------------------------------------------------
     * Testing Strategy for vertices():
     *      NOTE: no inputs
     *      - get vertices of empty graph
     *      - get vertices of graph with one vertex
     *      - get vertices of graph with >1 vertices
     * ------------------------------------------------------------------------
    */
    
    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract Graph<String> emptyInstance();
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    //covers test for empty()
    @Test
    public void testInitialVerticesEmpty() {
        assertEquals("expected new graph to have no vertices",
                Collections.emptySet(), emptyInstance().vertices());
    }

    //---------------------TESTS FOR add(L vertex) BEGIN----------------------//
    
    //covers case adding vertex to empty graph
    //            adding vertex that is not already in graph
    @Test
    public void testAddVertexToEmptyGraph() {
        Graph<String> testGraph = emptyInstance();
        boolean addedFirst = testGraph.add("first");
        assertEquals("expected adding one vertex to empty graph to return true", true, addedFirst);
        assertEquals("vertices should have one vertex after adding one vertex", 1, testGraph.vertices().size());
    }
    
    //covers case adding vertex that is already in graph
    @Test
    public void testAddVertexAlreadyInGraph() {
        Graph<String> testGraph = emptyInstance();
        boolean addedFirst = testGraph.add("first");
        boolean addedSecond = testGraph.add("first");
        assertEquals("expected adding one vertex to empty graph to return true", true, addedFirst);
        assertEquals("expected adding second same vertex to graph to return false", false, addedSecond);
        assertEquals("vertices should have one vertex after adding one vertex and failing"
                + "to add one duplicate", 1, testGraph.vertices().size());
    }
    
    //---------------------TESTS FOR add(L vertex) END----------------------//
    //---------------------TESTS FOR remove(L vertex) BEGIN----------------------//

    //covers case removing from empty graph
    @Test
    public void testRemoveVertexFromEmptyGraph() {
        boolean removeShouldFail = emptyInstance().remove("nice try bruh");
        assertEquals("expected removing from empty graph to return false", false, removeShouldFail);
    }
    
    //covers case removing vertex not in graph
    @Test
    public void testRemoveVertexNotInGraph() {
        Graph<String> testGraph = emptyInstance();
        testGraph.add("fun fun fun");
        testGraph.add("o hello dere");
        boolean removeShouldFail = testGraph.remove("nice try bruh");
        assertEquals("expected removing node not in graph to return false", false, removeShouldFail);
    }
    
    //covers case removing vertex that has 0 edges to other vertices
    //            removing vertex that has 0 edges from other vertices
    //            removing vertex results in empty graph
    @Test
    public void testRemoveVertexWithNoEdgeToOrFromOther() {
        Graph<String> testGraph = emptyInstance();
        testGraph.add("going nowhere");
        boolean successfulRemoval = testGraph.remove("going nowhere");
        assertEquals("expected removing from graph to return true", true, successfulRemoval);
        assertEquals("vertices should not have removed vertex", false, testGraph.vertices().contains("going nowhere"));
        assertEquals("vertices should have no vertices after removal", 0, testGraph.vertices().size());
    }
    
    //covers case removing vertex that has 1 edge to another vertex
    //            removing vertex results in nonempty graph
    @Test
    public void testRemoveVertexWithOneEdgeToOther() {
        Graph<String> testGraph = emptyInstance();
        testGraph.set("going from", "going to", 6);
        boolean successfulRemoval = testGraph.remove("going from");
        assertEquals("expected removing from graph to return true", true, successfulRemoval);
        assertEquals("vertices should have one vertex after removal", 1, testGraph.vertices().size());
        assertEquals("the vertex 'going to' should have no more sources", 
                Collections.emptySet(), testGraph.sources("going to").keySet());
    }
    
    //covers case removing vertex that has > 1 edge to other vertices
    @Test
    public void testRemoveVertexWithMoreThanOneEdgeToOther() {
        Graph<String> testGraph = emptyInstance();
        testGraph.set("going from", "going to", 6);
        testGraph.set("going from", "also going to", 6);
        boolean successfulRemoval = testGraph.remove("going from");
        assertEquals("expected removing from graph to return true", true, successfulRemoval);
        assertEquals("vertices should have two vertices after removal", 2, testGraph.vertices().size());
        assertEquals("the vertex 'going to' should have no more sources", 
                Collections.emptySet(), testGraph.sources("going to").keySet());
        assertEquals("the vertex 'also going to' should have no more sources", 
                Collections.emptySet(), testGraph.sources("also going to").keySet());
    }
    
    //covers case removing vertex that has 1 edge from another vertex
    @Test
    public void testRemoveVertexWithOneEdgeFromOther() {
        Graph<String> testGraph = emptyInstance();
        testGraph.set("going from", "going to", 6);
        boolean successfulRemoval = testGraph.remove("going to");
        assertEquals("expected removing from graph to return true", true, successfulRemoval);
        assertEquals("vertices should have one vertex after removal", 1, testGraph.vertices().size());
        assertEquals("the vertex 'going from' should have no more targets", 
                Collections.emptySet(), testGraph.targets("going from").keySet());
    }
    
    //covers case removing vertex that has > 1 edge from other vertices
    @Test
    public void testRemoveVertexWithMoreThanOneEdgeFromOther() {
        Graph<String> testGraph = emptyInstance();
        testGraph.set("going from", "going to", 6);
        testGraph.set("also going from", "going to", 6);
        boolean successfulRemoval = testGraph.remove("going to");
        assertEquals("expected removing from graph to return true", true, successfulRemoval);
        assertEquals("vertices should have two vertices after removal", 2, testGraph.vertices().size());
        assertEquals("the vertex 'going from' should have no more targets", 
                Collections.emptySet(), testGraph.targets("going from").keySet());
        assertEquals("the vertex 'also going from' should have no more targets", 
                Collections.emptySet(), testGraph.targets("also going from").keySet());
    }
    
    //---------------------TESTS FOR remove(L vertex) END----------------------//    
    //---------------------TESTS FOR set(L source, L target, int weight) BEGIN----------------------//

    //covers case where source and target are same vertex
    @Test
    public void testSetSourceAndTargetAreSameVertex() {
        Graph<String> testGraph = emptyInstance();
        testGraph.add("going to");
        int previousWeight = testGraph.set("going to", "going to", 6);
        assertEquals("expected setting new edge to return 0", 0, previousWeight);
        assertEquals("vertices should still have just one vertex after setting new edge", 1, testGraph.vertices().size());
        //check vertex 'going from' added successfully via targets
        assertEquals("the vertex 'going to' should have one target", 
                1, testGraph.targets("going to").keySet().size());
        assertEquals("the vertex 'going to' should be mapped to vertex 'going to' with size 6", 
                6, testGraph.targets("going to").get("going to").intValue());
        //check vertex 'going to' added successfully via sources
        assertEquals("the vertex 'going to' should have one source", 
                1, testGraph.sources("going to").keySet().size());
        assertEquals("the vertex 'going to' should be mapped to vertex 'going to' with size 6", 
                6, testGraph.sources("going to").get("going to").intValue());
    }
    
    //covers case setting where source did not already exist in graph
    //                          target already existed in graph
    //                          weight > 0
    @Test
    public void testSetSourceDidNotExist() {
        Graph<String> testGraph = emptyInstance();
        testGraph.add("going to");
        int previousWeight = testGraph.set("going from", "going to", 6);
        assertEquals("expected setting new edge where source did not exist to return 0", 0, previousWeight);
        assertEquals("vertices should have two vertices after setting new edge", 2, testGraph.vertices().size());
        //check vertex 'going from' added successfully
        assertEquals("the vertex 'going from' should have one target", 
                1, testGraph.targets("going from").keySet().size());
        assertEquals("the vertex 'going from' should be mapped to vertex 'going to' with size 6", 
                6, testGraph.targets("going from").get("going to").intValue());
        //check vertex 'going to' added successfully
        assertEquals("the vertex 'going to' should have one source", 
                1, testGraph.sources("going to").keySet().size());
        assertEquals("the vertex 'going to' should be mapped to vertex 'going from' with size 6", 
                6, testGraph.sources("going to").get("going from").intValue());
    }
    
    //covers case setting where target did not already exist in graph
    //                          source already existed in graph
    //                          weight > 0
    @Test
    public void testSetTargetDidNotExist() {
        Graph<String> testGraph = emptyInstance();
        testGraph.add("going from");
        int previousWeight = testGraph.set("going from", "going to", 6);
        assertEquals("expected setting new edge where source did not exist to return 0", 0, previousWeight);
        assertEquals("vertices should have two vertices after setting new edge", 2, testGraph.vertices().size());
        //check vertex 'going from' added successfully
        assertEquals("the vertex 'going from' should have one target", 
                1, testGraph.targets("going from").keySet().size());
        assertEquals("the vertex 'going from' should be mapped to vertex 'going to' with size 6", 
                6, testGraph.targets("going from").get("going to").intValue());
        //check vertex 'going to' added successfully
        assertEquals("the vertex 'going to' should have one source", 
                1, testGraph.sources("going to").keySet().size());
        assertEquals("the vertex 'going to' should be mapped to vertex 'going from' with size 6", 
                6, testGraph.sources("going to").get("going from").intValue());
    }
    
    //covers case setting where edge did not exist
    //                          weight > 0
    @Test
    public void testSetNonZeroEdgeDidNotExist() {
        Graph<String> testGraph = emptyInstance();
        testGraph.add("going from");
        testGraph.add("going to");
        int previousWeight = testGraph.set("going from", "going to", 6);
        assertEquals("expected setting new edge where source did not exist to return 0", 0, previousWeight);
        assertEquals("vertices should have two vertices after setting new edge", 2, testGraph.vertices().size());
        //check vertex 'going from' added successfully
        assertEquals("the vertex 'going from' should have one target", 
                1, testGraph.targets("going from").keySet().size());
        assertEquals("the vertex 'going from' should be mapped to vertex 'going to' with size 6", 
                6, testGraph.targets("going from").get("going to").intValue());
        //check vertex 'going to' added successfully
        assertEquals("the vertex 'going to' should have one source", 
                1, testGraph.sources("going to").keySet().size());
        assertEquals("the vertex 'going to' should be mapped to vertex 'going from' with size 6", 
                6, testGraph.sources("going to").get("going from").intValue());
    }
    
    //covers case setting where edge already existed in graph
    //                          weight > 0
    @Test
    public void testSetNonZeroEdgeExisted() {
        Graph<String> testGraph = emptyInstance();
        int previousWeight = testGraph.set("going from", "going to", 6);
        assertEquals("expected setting edge that did not exist to return old weight of 0", 0, previousWeight);
        previousWeight = testGraph.set("going from", "going to", 8);
        assertEquals("expected setting edge that already existed to return old weight of 6", 6, previousWeight);
        assertEquals("vertices should still have two vertices after setting edge", 2, testGraph.vertices().size());
        assertEquals("the vertex 'going from' should have one target", 
                1, testGraph.targets("going from").keySet().size());
        assertEquals("the vertex 'going from' should be mapped to vertex 'going to' with size 8", 
                8, testGraph.targets("going from").get("going to").intValue());
        assertEquals("the vertex 'going to' should have one source", 
                1, testGraph.sources("going to").keySet().size());
        assertEquals("the vertex 'going to' should be mapped to vertex 'going from' with size 8", 
                8, testGraph.sources("going to").get("going from").intValue());
    }
    
    //covers case setting where weight = 0
    //                          source did not exist
    //                          edge did not exist
    @Test
    public void testSetZeroWeightSourceDidNotExist() {
        Graph<String> testGraph = emptyInstance();
        testGraph.add("going to");
        int previousWeight = testGraph.set("going from", "going to", 0);
        assertEquals("expected setting edge where source did not exist to return 0", 0, previousWeight);
        assertEquals("vertices should have 1 vertex after setting new edge with 0 weight", 1, testGraph.vertices().size());

        assertEquals("the vertex 'going to' should have no targets", 
                0, testGraph.targets("going to").keySet().size());
        assertEquals("the vertex 'going to' should have no sources", 
                0, testGraph.sources("going to").keySet().size());
    }
    
    //covers case setting where weight = 0
    //                          target did not exist
    //                          edge did not exist
    @Test
    public void testSetZeroWeightTargetDidNotExist() {
        Graph<String> testGraph = emptyInstance();
        testGraph.add("going from");
        int previousWeight = testGraph.set("going from", "going to", 0);
        assertEquals("expected setting edge where target did not exist to return 0", 0, previousWeight);
        assertEquals("vertices should have 1 vertex after setting new edge with 0 weight", 1, testGraph.vertices().size());
        assertEquals("the vertex 'going from' should have no targets", 
                0, testGraph.targets("going from").keySet().size());
        assertEquals("the vertex 'going from' should have no sources", 
                0, testGraph.sources("going from").keySet().size());
    }
    
    //covers case setting where weight = 0
    //                          edge already existed
    //                          --> removal of edge
    @Test
    public void testSetZeroWeightEdgeExisted() {
        Graph<String> testGraph = emptyInstance();
        int previousWeight = testGraph.set("going from", "going to", 6);
        previousWeight = testGraph.set("going from", "going to", 0);
        assertEquals("expected setting edge to 0 weight to return previous weight of 6", 6, previousWeight);
        assertEquals("vertices should still have 2 vertices after setting edge with 0 weight/removing edge", 
                2, testGraph.vertices().size());
        assertEquals("the vertex 'going from' should have no targets", 
                0, testGraph.targets("going from").keySet().size());
        assertEquals("the vertex 'going to' should have no sources", 
                0, testGraph.sources("going to").keySet().size());
    }
    //---------------------TESTS FOR set(L source, L target, int weight) END----------------------//
    //---------------------TESTS FOR sources(L target) BEGIN----------------------//

    //covers case where target does not exist in graph
    @Test
    public void testSourcesTargetDoesNotExist() {
        Graph<String> testGraph = emptyInstance();
        Map<String,Integer> sources = testGraph.sources("target does not exist bruh");
        assertEquals("the vertex 'target does not exist bruh' should have no sources", 
                0, sources.size());
    }
    
    //covers case where target does exist in graph and has no sources
    @Test
    public void testSourcesTargetExistsNoSources() {
        Graph<String> testGraph = emptyInstance();
        String testTarget = "testy spicy target";
        Map<String,Integer> sources = testGraph.sources(testTarget);
        assertEquals("the vertex " + testTarget + " should have no sources", 
                0, sources.size());
    }
    
    //covers case where target does exist in graph and has one source
    @Test
    public void testSourcesTargetExistsOneSource() {
        Graph<String> testGraph = emptyInstance();
        String testSource = "testy saucy source";
        String testTarget = "testy saucy target";
        testGraph.set(testSource, testTarget, 10);
        Map<String,Integer> sources = testGraph.sources(testTarget);
        assertEquals("the vertex " + testTarget + " should have one source", 
                1, sources.size());
        assertEquals("the vertex " + testTarget + " be mapped to with length " + 10, 
                10, sources.get(testSource).intValue());
    }

    //covers case where target does exist in graph and has more than one source
    @Test
    public void testSourcesTargetExistsMoreThanOneSource() {
        Graph<String> testGraph = emptyInstance();
        String testFirstSource = "testy saucy first source";
        String testSecondSource = "testy saucy second source";
        String testTarget = "testy saucy target";
        testGraph.set(testFirstSource, testTarget, 10);
        testGraph.set(testSecondSource, testTarget, 6);
        Map<String,Integer> sources = testGraph.sources(testTarget);
        assertEquals("the vertex " + testTarget + " should have two sources", 
                2, sources.size());
        assertEquals("the vertex " + testTarget + " be mapped to length " + 10, 
                10, sources.get(testFirstSource).intValue());
        assertEquals("the vertex " + testTarget + " be mapped to length " + 6, 
                6, sources.get(testSecondSource).intValue());
    }
    
    //---------------------TESTS FOR sources(L target) END----------------------//
    //---------------------TESTS FOR targets(L sources) BEGIN----------------------//

    //covers case where source does not exist in graph
    @Test
    public void testTargetsSourceDoesNotExist() {
        Graph<String> testGraph = emptyInstance();
        Map<String,Integer> targets = testGraph.targets("source does not exist bruh");
        assertEquals("the vertex 'source does not exist bruh' should have no targets", 
                0, targets.size());
    }
    
    //covers case where source does exist in graph and has no targets
    @Test
    public void testTargetsSourceExistsNoTargets() {
        Graph<String> testGraph = emptyInstance();
        String testSource = "testy spicy source";
        Map<String,Integer> targets = testGraph.targets(testSource);
        assertEquals("the vertex " + testSource + " should have no targets", 
                0, targets.size());
    }
    
    //covers case where source does exist in graph and has one target
    @Test
    public void testTargetsSourceExistsOneTarget() {
        Graph<String> testGraph = emptyInstance();
        String testSource = "testy saucy source";
        String testTarget = "testy saucy target";
        testGraph.set(testSource, testTarget, 10);
        Map<String,Integer> targets = testGraph.targets(testSource);
        assertEquals("the vertex " + testSource + " should have one target", 
                1, targets.size());
        assertEquals("the vertex " + testTarget + " be mapped to length " + 10, 
                10, targets.get(testTarget).intValue());
    }

    //covers case where source does exist in graph and has more than one target
    @Test
    public void testTargetsSourceExistsMoreThanOneTarget() {
        Graph<String> testGraph = emptyInstance();
        String testSource = "testy saucy source";
        String testFirstTarget = "testy saucy first target";
        String testSecondTarget = "testy saucy second target";
        testGraph.set(testSource, testFirstTarget, 10);
        testGraph.set(testSource, testSecondTarget, 6);
        Map<String,Integer> targets = testGraph.targets(testSource);
        assertEquals("the vertex " + testSource + " should have two targets", 
                2, targets.size());
        assertEquals("the vertex " + testFirstTarget + " be mapped to length " + 10, 
                10, targets.get(testFirstTarget).intValue());
        assertEquals("the vertex " + testSecondTarget + " be mapped to length " + 6, 
                6, targets.get(testSecondTarget).intValue());
    }
   
    //---------------------TESTS FOR targets(L sources) END----------------------//

}
