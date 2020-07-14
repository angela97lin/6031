/* Copyright (c) 2015-2017 MIT 6.005/6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for static methods of Graph.
 * 
 * To facilitate testing multiple implementations of Graph, instance methods are
 * tested in GraphInstanceTest.
 */
public class GraphStaticTest {
    
    // Testing strategy for Graph with different vertex label types:
    //   empty()
    //     no inputs, only output is empty graph
    //     observe with vertices()
    //   Strings (in GraphicInstance)
   
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testEmptyVerticesEmpty() {
        assertEquals("expected empty() graph to have no vertices",
                Collections.emptySet(), Graph.empty().vertices());
    }

    //---------------------TESTS FOR GRAPH<LONG> BEGIN----------------------//
    
    //covers case adding vertex to empty graph
    //            adding vertex that is not already in graph
    @Test
    public void testAddVertexToEmptyGraphLong() {
        Graph<Long> testGraph = Graph.empty();
        boolean addedFirst = testGraph.add((long) 1);
        assertEquals("expected adding one vertex to empty graph to return true", true, addedFirst);
        assertEquals("vertices should have one vertex after adding one vertex", 1, testGraph.vertices().size());
    }
    
    //covers case adding vertex that is already in graph
    @Test
    public void testAddVertexAlreadyInGraphLong() {
        Graph<Long> testGraph = Graph.empty();
        boolean addedFirst = testGraph.add((long)1);
        boolean addedSecond = testGraph.add((long)1);
        assertEquals("expected adding one vertex to empty graph to return true", true, addedFirst);
        assertEquals("expected adding second same vertex to graph to return false", false, addedSecond);
        assertEquals("vertices should have one vertex after adding one vertex and failing"
                + "to add one duplicate", 1, testGraph.vertices().size());
    }
   

    //covers case removing from empty graph
    @Test
    public void testRemoveVertexFromEmptyGraphLong() {
        Graph<Long> testGraph = Graph.empty();
        boolean removeShouldFail = testGraph.remove((long)6);
        assertEquals("expected removing from empty graph to return false", false, removeShouldFail);
    }
    
    //covers case removing vertex not in graph
    @Test
    public void testRemoveVertexNotInGraphLong() {
        Graph<Long> testGraph = Graph.empty();
        testGraph.add((long)6);
        testGraph.add((long)8);
        boolean removeShouldFail = testGraph.remove((long)10);
        assertEquals("expected removing node not in graph to return false", false, removeShouldFail);
    }
    
    //covers case removing vertex that has 0 edges to other vertices
    //            removing vertex that has 0 edges from other vertices
    //            removing vertex results in empty graph
    @Test
    public void testRemoveVertexWithNoEdgeToOrFromOtherLong() {
        Graph<Long> testGraph = Graph.empty();
        testGraph.add((long)100);
        boolean successfulRemoval = testGraph.remove((long)100);
        assertEquals("expected removing from graph to return true", true, successfulRemoval);
        assertEquals("vertices should not have removed vertex", false, testGraph.vertices().contains(100));
        assertEquals("vertices should have no vertices after removal", 0, testGraph.vertices().size());
    }
    
    //covers case removing vertex that has 1 edge to another vertex
    //            removing vertex results in nonempty graph
    @Test
    public void testRemoveVertexWithOneEdgeToOtherLong() {
        Graph<Long> testGraph = Graph.empty();
        testGraph.set((long)10, (long)8, 6);
        boolean successfulRemoval = testGraph.remove((long)10);
        assertEquals("expected removing from graph to return true", true, successfulRemoval);
        assertEquals("vertices should have one vertex after removal", 1, testGraph.vertices().size());
        assertEquals("the vertex 8 should have no more sources", 
                Collections.emptySet(), testGraph.sources((long)8).keySet());
    }
    
    //covers case removing vertex that has > 1 edge to other vertices
    @Test
    public void testRemoveVertexWithMoreThanOneEdgeToOtherLong() {
        Graph<Long> testGraph = Graph.empty();
        testGraph.set((long)6, (long)7, 6);
        testGraph.set((long)6, (long)5, 6);
        boolean successfulRemoval = testGraph.remove((long)6);
        assertEquals("expected removing from graph to return true", true, successfulRemoval);
        assertEquals("vertices should have two vertices after removal", 2, testGraph.vertices().size());
        assertEquals("the vertex 7 should have no more sources", 
                Collections.emptySet(), testGraph.sources((long)7).keySet());
        assertEquals("the vertex 5 should have no more sources", 
                Collections.emptySet(), testGraph.sources((long)5).keySet());
    }
    
    //covers case removing vertex that has 1 edge from another vertex
    @Test
    public void testRemoveVertexWithOneEdgeFromOtherLong() {
        Graph<Long> testGraph = Graph.empty();
        testGraph.set((long)23, (long)4, 6);
        boolean successfulRemoval = testGraph.remove((long)4);
        assertEquals("expected removing from graph to return true", true, successfulRemoval);
        assertEquals("vertices should have one vertex after removal", 1, testGraph.vertices().size());
        assertEquals("the vertex '23' should have no more targets", 
                Collections.emptySet(), testGraph.targets((long)23).keySet());
    }
    
    //covers case removing vertex that has > 1 edge from other vertices
    @Test
    public void testRemoveVertexWithMoreThanOneEdgeFromOtherLong() {
        Graph<Long> testGraph = Graph.empty();
        testGraph.set((long)1, (long)9, 6);
        testGraph.set((long)12, (long)9, 6);
        boolean successfulRemoval = testGraph.remove((long)9);
        assertEquals("expected removing from graph to return true", true, successfulRemoval);
        assertEquals("vertices should have two vertices after removal", 2, testGraph.vertices().size());
        assertEquals("the vertex '1' should have no more targets", 
                Collections.emptySet(), testGraph.targets((long)1).keySet());
        assertEquals("the vertex '12' should have no more targets", 
                Collections.emptySet(), testGraph.targets((long)12).keySet());
    }
   

    //covers case where source and target are same vertex
    @Test
    public void testSetSourceAndTargetAreSameVertexLong() {
        Graph<Long> testGraph = Graph.empty();
        testGraph.add((long)45);
        int previousWeight = testGraph.set((long)45, (long)45, 6);
        assertEquals("expected setting new edge to return 0", 0, previousWeight);
        assertEquals("vertices should still have just one vertex after setting new edge", 1, testGraph.vertices().size());
        //check vertex 'going from' added successfully via targets
        assertEquals("the vertex '45' should have one target", 
                1, testGraph.targets((long)45).keySet().size());
        assertEquals("the vertex '45' should be mapped to vertex '45' with size 6", 
                6, testGraph.targets((long)45).get((long)45).intValue());
        //check vertex 'going to' added successfully via sources
        assertEquals("the vertex '45' should have one source", 
                1, testGraph.sources((long)45).keySet().size());
        assertEquals("the vertex '45' should be mapped to vertex '45' with size 6", 
                6, testGraph.sources((long)45).get((long)45).intValue());
    }
    
    //covers case setting where source did not already exist in graph
    //                          target already existed in graph
    //                          weight > 0
    @Test
    public void testSetSourceDidNotExistLong() {
        Graph<Long> testGraph = Graph.empty();
        testGraph.add((long)10);
        int previousWeight = testGraph.set((long)2, (long)10, 6);
        assertEquals("expected setting new edge where source did not exist to return 0", 0, previousWeight);
        assertEquals("vertices should have two vertices after setting new edge", 2, testGraph.vertices().size());
        //check vertex 'going from' added successfully
        assertEquals("the vertex '2' should have one target", 
                1, testGraph.targets((long)2).keySet().size());
        assertEquals("the vertex '2' should be mapped to vertex '10' with size 6", 
                6, testGraph.targets((long)2).get((long)10).intValue());
        //check vertex 'going to' added successfully
        assertEquals("the vertex '10' should have one source", 
                1, testGraph.sources((long)10).keySet().size());
        assertEquals("the vertex '10' should be mapped to vertex '2' with size 6", 
                6, testGraph.sources((long)10).get((long)2).intValue());
    }
    
    //covers case setting where target did not already exist in graph
    //                          source already existed in graph
    //                          weight > 0
    @Test
    public void testSetTargetDidNotExistLong() {
        Graph<Long> testGraph = Graph.empty();
        testGraph.add((long)9);
        int previousWeight = testGraph.set((long)9, (long)7, 6);
        assertEquals("expected setting new edge where source did not exist to return 0", 0, previousWeight);
        assertEquals("vertices should have two vertices after setting new edge", 2, testGraph.vertices().size());
        //check vertex '9' added successfully
        assertEquals("the vertex '9' should have one target", 
                1, testGraph.targets((long)9).keySet().size());
        assertEquals("the vertex '9' should be mapped to vertex '7' with size 6", 
                6, testGraph.targets((long)9).get((long)7).intValue());
        //check vertex '7' added successfully
        assertEquals("the vertex '7' should have one source", 
                1, testGraph.sources((long)7).keySet().size());
        assertEquals("the vertex '7' should be mapped to vertex '9' with size 6", 
                6, testGraph.sources((long)7).get((long)9).intValue());
    }
    
    //covers case setting where edge did not exist
    //                          weight > 0
    @Test
    public void testSetNonZeroEdgeDidNotExistLong() {
        Graph<Long> testGraph = Graph.empty();
        testGraph.add((long)34);
        testGraph.add((long)13);
        int previousWeight = testGraph.set((long)34, (long)13, 6);
        assertEquals("expected setting new edge where source did not exist to return 0", 0, previousWeight);
        assertEquals("vertices should have two vertices after setting new edge", 2, testGraph.vertices().size());
        //check vertex '34' added successfully
        assertEquals("the vertex '34' should have one target", 
                1, testGraph.targets((long)34).keySet().size());
        assertEquals("the vertex '34' should be mapped to vertex '13' with size 6", 
                6, testGraph.targets((long)34).get((long)13).intValue());
        //check vertex '13' added successfully
        assertEquals("the vertex '13' should have one source", 
                1, testGraph.sources((long)13).keySet().size());
        assertEquals("the vertex '13' should be mapped to vertex '34' with size 6", 
                6, testGraph.sources((long)13).get((long)34).intValue());
    }
    
    //covers case setting where edge already existed in graph
    //                          weight > 0
    @Test
    public void testSetNonZeroEdgeExistedLong() {
        Graph<Long> testGraph = Graph.empty();
        int previousWeight = testGraph.set((long)4, (long)8, 6);
        assertEquals("expected setting edge that did not exist to return old weight of 0", 0, previousWeight);
        previousWeight = testGraph.set((long)4, (long)8, 8);
        assertEquals("expected setting edge that already existed to return old weight of 6", 6, previousWeight);
        assertEquals("vertices should still have two vertices after setting edge", 2, testGraph.vertices().size());
        assertEquals("the vertex '4' should have one target", 
                1, testGraph.targets((long)4).keySet().size());
        assertEquals("the vertex '4' should be mapped to vertex '8' with size 8", 
                8, testGraph.targets((long)4).get((long)8).intValue());
        assertEquals("the vertex '8' should have one source", 
                1, testGraph.sources((long)8).keySet().size());
        assertEquals("the vertex '8' should be mapped to vertex '4' with size 8", 
                8, testGraph.sources((long)8).get((long)4).intValue());
    }
    
    //covers case setting where weight = 0
    //                          source did not exist
    //                          edge did not exist
    @Test
    public void testSetZeroWeightSourceDidNotExistLong() {
        Graph<Long> testGraph = Graph.empty();
        testGraph.add((long)1);
        int previousWeight = testGraph.set((long)2, (long)1, 0);
        assertEquals("expected setting edge where source did not exist to return 0", 0, previousWeight);
        assertEquals("vertices should have 1 vertex after setting new edge with 0 weight", 1, testGraph.vertices().size());

        assertEquals("the vertex '1' should have no targets", 
                0, testGraph.targets((long)1).keySet().size());
        assertEquals("the vertex '1' should have no sources", 
                0, testGraph.sources((long)1).keySet().size());
    }
    
    //covers case setting where weight = 0
    //                          target did not exist
    //                          edge did not exist
    @Test
    public void testSetZeroWeightTargetDidNotExistLong() {
        Graph<Long> testGraph = Graph.empty();
        testGraph.add((long)6);
        int previousWeight = testGraph.set((long)6, (long)1, 0);
        assertEquals("expected setting edge where target did not exist to return 0", 0, previousWeight);
        assertEquals("vertices should have 1 vertex after setting new edge with 0 weight", 1, testGraph.vertices().size());
        assertEquals("the vertex '6' should have no targets", 
                0, testGraph.targets((long)6).keySet().size());
        assertEquals("the vertex '6' should have no sources", 
                0, testGraph.sources((long)6).keySet().size());
    }
    
    //covers case setting where weight = 0
    //                          edge already existed
    //                          --> removal of edge
    @Test
    public void testSetZeroWeightEdgeExistedLong() {
        Graph<Long> testGraph = Graph.empty();
        int previousWeight = testGraph.set((long)3, (long)8, 6);
        previousWeight = testGraph.set((long)3, (long)8, 0);
        assertEquals("expected setting edge to 0 weight to return previous weight of 6", 6, previousWeight);
        assertEquals("vertices should still have 2 vertices after setting edge with 0 weight/removing edge", 
                2, testGraph.vertices().size());
        assertEquals("the vertex '3' should have no targets", 
                0, testGraph.targets((long)3).keySet().size());
        assertEquals("the vertex '8' should have no sources", 
                0, testGraph.sources((long)8).keySet().size());
    }

    //covers case where target does not exist in graph
    @Test
    public void testSourcesTargetDoesNotExistLong() {
        Graph<Long> testGraph = Graph.empty();
        Map<Long,Integer> sources = testGraph.sources((long)342);
        assertEquals("the vertex 'target does not exist bruh' should have no sources", 
                0, sources.size());
    }
    
    //covers case where target does exist in graph and has no sources
    @Test
    public void testSourcesTargetExistsNoSourcesLong() {
        Graph<Long> testGraph = Graph.empty();
        Long testTarget = (long)9;
        Map<Long,Integer> sources = testGraph.sources(testTarget);
        assertEquals("the vertex " + testTarget + " should have no sources", 
                0, sources.size());
    }
    
    //covers case where target does exist in graph and has one source
    @Test
    public void testSourcesTargetExistsOneSourceLong() {
        Graph<Long> testGraph = Graph.empty();
        Long testSource = (long)8;
        Long testTarget = (long)7;
        testGraph.set(testSource, testTarget, 10);
        Map<Long,Integer> sources = testGraph.sources(testTarget);
        assertEquals("the vertex " + testTarget + " should have one source", 
                1, sources.size());
        assertEquals("the vertex " + testTarget + " be mapped to with length " + 10, 
                10, sources.get(testSource).intValue());
    }

    //covers case where target does exist in graph and has more than one source
    @Test
    public void testSourcesTargetExistsMoreThanOneSourceLong() {
        Graph<Long> testGraph = Graph.empty();
        Long testFirstSource = (long)64;
        Long testSecondSource = (long)7;
        Long testTarget = (long)2;
        testGraph.set(testFirstSource, testTarget, 10);
        testGraph.set(testSecondSource, testTarget, 6);
        Map<Long,Integer> sources = testGraph.sources(testTarget);
        assertEquals("the vertex " + testTarget + " should have two sources", 
                2, sources.size());
        assertEquals("the vertex " + testTarget + " be mapped to length " + 10, 
                10, sources.get(testFirstSource).intValue());
        assertEquals("the vertex " + testTarget + " be mapped to length " + 6, 
                6, sources.get(testSecondSource).intValue());
    }
    
    //covers case where source does not exist in graph
    @Test
    public void testTargetsSourceDoesNotExistLong() {
        Graph<Long> testGraph = Graph.empty();
        Map<Long,Integer> targets = testGraph.targets((long)23123);
        assertEquals("the vertex 'source does not exist bruh' should have no targets", 
                0, targets.size());
    }
    
    //covers case where source does exist in graph and has no targets
    @Test
    public void testTargetsSourceExistsNoTargetsLong() {
        Graph<Long> testGraph = Graph.empty();
        Long testSource = (long)18;
        Map<Long,Integer> targets = testGraph.targets(testSource);
        assertEquals("the vertex " + testSource + " should have no targets", 
                0, targets.size());
    }
    
    //covers case where source does exist in graph and has one target
    @Test
    public void testTargetsSourceExistsOneTargetLong() {
        Graph<Long> testGraph = Graph.empty();
        Long testSource = (long)5;
        Long testTarget = (long)6;
        testGraph.set(testSource, testTarget, 10);
        Map<Long,Integer> targets = testGraph.targets(testSource);
        assertEquals("the vertex " + testSource + " should have one target", 
                1, targets.size());
        assertEquals("the vertex " + testTarget + " be mapped to length " + 10, 
                10, targets.get(testTarget).intValue());
    }

    //covers case where source does exist in graph and has more than one target
    @Test
    public void testTargetsSourceExistsMoreThanOneTargetLong() {
        Graph<Long> testGraph = Graph.empty();
        Long testSource = (long)10;
        Long testFirstTarget = (long)3;
        Long testSecondTarget = (long)100;
        testGraph.set(testSource, testFirstTarget, 10);
        testGraph.set(testSource, testSecondTarget, 6);
        Map<Long,Integer> targets = testGraph.targets(testSource);
        assertEquals("the vertex " + testSource + " should have two targets", 
                2, targets.size());
        assertEquals("the vertex " + testFirstTarget + " be mapped to length " + 10, 
                10, targets.get(testFirstTarget).intValue());
        assertEquals("the vertex " + testSecondTarget + " be mapped to length " + 6, 
                6, targets.get(testSecondTarget).intValue());
    }
   
    //---------------------TESTS FOR GRAPH<LONG> END----------------------//
    

    //---------------------TESTS FOR GRAPH<INTEGER> BEGIN----------------------//
    
    //covers case adding vertex to empty graph
    //            adding vertex that is not already in graph
    @Test
    public void testAddVertexToEmptyGraph() {
        Graph<Integer> testGraph = Graph.empty();
        boolean addedFirst = testGraph.add(1);
        assertEquals("expected adding one vertex to empty graph to return true", true, addedFirst);
        assertEquals("vertices should have one vertex after adding one vertex", 1, testGraph.vertices().size());
    }
    
    //covers case adding vertex that is already in graph
    @Test
    public void testAddVertexAlreadyInGraph() {
        Graph<Integer> testGraph = Graph.empty();
        boolean addedFirst = testGraph.add(1);
        boolean addedSecond = testGraph.add(1);
        assertEquals("expected adding one vertex to empty graph to return true", true, addedFirst);
        assertEquals("expected adding second same vertex to graph to return false", false, addedSecond);
        assertEquals("vertices should have one vertex after adding one vertex and failing"
                + "to add one duplicate", 1, testGraph.vertices().size());
    }
   

    //covers case removing from empty graph
    @Test
    public void testRemoveVertexFromEmptyGraph() {
        Graph<Integer> testGraph = Graph.empty();
        boolean removeShouldFail = testGraph.remove(6);
        assertEquals("expected removing from empty graph to return false", false, removeShouldFail);
    }
    
    //covers case removing vertex not in graph
    @Test
    public void testRemoveVertexNotInGraph() {
        Graph<Integer> testGraph = Graph.empty();
        testGraph.add(6);
        testGraph.add(8);
        boolean removeShouldFail = testGraph.remove(10);
        assertEquals("expected removing node not in graph to return false", false, removeShouldFail);
    }
    
    //covers case removing vertex that has 0 edges to other vertices
    //            removing vertex that has 0 edges from other vertices
    //            removing vertex results in empty graph
    @Test
    public void testRemoveVertexWithNoEdgeToOrFromOther() {
        Graph<Integer> testGraph = Graph.empty();
        testGraph.add(100);
        boolean successfulRemoval = testGraph.remove(100);
        assertEquals("expected removing from graph to return true", true, successfulRemoval);
        assertEquals("vertices should not have removed vertex", false, testGraph.vertices().contains(100));
        assertEquals("vertices should have no vertices after removal", 0, testGraph.vertices().size());
    }
    
    //covers case removing vertex that has 1 edge to another vertex
    //            removing vertex results in nonempty graph
    @Test
    public void testRemoveVertexWithOneEdgeToOther() {
        Graph<Integer> testGraph = Graph.empty();
        testGraph.set(10, 8, 6);
        boolean successfulRemoval = testGraph.remove(10);
        assertEquals("expected removing from graph to return true", true, successfulRemoval);
        assertEquals("vertices should have one vertex after removal", 1, testGraph.vertices().size());
        assertEquals("the vertex 8 should have no more sources", 
                Collections.emptySet(), testGraph.sources(8).keySet());
    }
    
    //covers case removing vertex that has > 1 edge to other vertices
    @Test
    public void testRemoveVertexWithMoreThanOneEdgeToOther() {
        Graph<Integer> testGraph = Graph.empty();
        testGraph.set(6, 7, 6);
        testGraph.set(6, 5, 6);
        boolean successfulRemoval = testGraph.remove(6);
        assertEquals("expected removing from graph to return true", true, successfulRemoval);
        assertEquals("vertices should have two vertices after removal", 2, testGraph.vertices().size());
        assertEquals("the vertex 7 should have no more sources", 
                Collections.emptySet(), testGraph.sources(7).keySet());
        assertEquals("the vertex 5 should have no more sources", 
                Collections.emptySet(), testGraph.sources(5).keySet());
    }
    
    //covers case removing vertex that has 1 edge from another vertex
    @Test
    public void testRemoveVertexWithOneEdgeFromOther() {
        Graph<Integer> testGraph = Graph.empty();
        testGraph.set(23, 4, 6);
        boolean successfulRemoval = testGraph.remove(4);
        assertEquals("expected removing from graph to return true", true, successfulRemoval);
        assertEquals("vertices should have one vertex after removal", 1, testGraph.vertices().size());
        assertEquals("the vertex '23' should have no more targets", 
                Collections.emptySet(), testGraph.targets(23).keySet());
    }
    
    //covers case removing vertex that has > 1 edge from other vertices
    @Test
    public void testRemoveVertexWithMoreThanOneEdgeFromOther() {
        Graph<Integer> testGraph = Graph.empty();
        testGraph.set(1, 9, 6);
        testGraph.set(12, 9, 6);
        boolean successfulRemoval = testGraph.remove(9);
        assertEquals("expected removing from graph to return true", true, successfulRemoval);
        assertEquals("vertices should have two vertices after removal", 2, testGraph.vertices().size());
        assertEquals("the vertex '1' should have no more targets", 
                Collections.emptySet(), testGraph.targets(1).keySet());
        assertEquals("the vertex '12' should have no more targets", 
                Collections.emptySet(), testGraph.targets(12).keySet());
    }
   

    //covers case where source and target are same vertex
    @Test
    public void testSetSourceAndTargetAreSameVertex() {
        Graph<Integer> testGraph = Graph.empty();
        testGraph.add(45);
        int previousWeight = testGraph.set(45, 45, 6);
        assertEquals("expected setting new edge to return 0", 0, previousWeight);
        assertEquals("vertices should still have just one vertex after setting new edge", 1, testGraph.vertices().size());
        //check vertex 'going from' added successfully via targets
        assertEquals("the vertex '45' should have one target", 
                1, testGraph.targets(45).keySet().size());
        assertEquals("the vertex '45' should be mapped to vertex '45' with size 6", 
                6, testGraph.targets(45).get(45).intValue());
        //check vertex 'going to' added successfully via sources
        assertEquals("the vertex '45' should have one source", 
                1, testGraph.sources(45).keySet().size());
        assertEquals("the vertex '45' should be mapped to vertex '45' with size 6", 
                6, testGraph.sources(45).get(45).intValue());
    }
    
    //covers case setting where source did not already exist in graph
    //                          target already existed in graph
    //                          weight > 0
    @Test
    public void testSetSourceDidNotExist() {
        Graph<Integer> testGraph = Graph.empty();
        testGraph.add(10);
        int previousWeight = testGraph.set(2, 10, 6);
        assertEquals("expected setting new edge where source did not exist to return 0", 0, previousWeight);
        assertEquals("vertices should have two vertices after setting new edge", 2, testGraph.vertices().size());
        //check vertex 'going from' added successfully
        assertEquals("the vertex '2' should have one target", 
                1, testGraph.targets(2).keySet().size());
        assertEquals("the vertex '2' should be mapped to vertex '10' with size 6", 
                6, testGraph.targets(2).get(10).intValue());
        //check vertex 'going to' added successfully
        assertEquals("the vertex '10' should have one source", 
                1, testGraph.sources(10).keySet().size());
        assertEquals("the vertex '10' should be mapped to vertex '2' with size 6", 
                6, testGraph.sources(10).get(2).intValue());
    }
    
    //covers case setting where target did not already exist in graph
    //                          source already existed in graph
    //                          weight > 0
    @Test
    public void testSetTargetDidNotExist() {
        Graph<Integer> testGraph = Graph.empty();
        testGraph.add(9);
        int previousWeight = testGraph.set(9, 7, 6);
        assertEquals("expected setting new edge where source did not exist to return 0", 0, previousWeight);
        assertEquals("vertices should have two vertices after setting new edge", 2, testGraph.vertices().size());
        //check vertex '9' added successfully
        assertEquals("the vertex '9' should have one target", 
                1, testGraph.targets(9).keySet().size());
        assertEquals("the vertex '9' should be mapped to vertex '7' with size 6", 
                6, testGraph.targets(9).get(7).intValue());
        //check vertex '7' added successfully
        assertEquals("the vertex '7' should have one source", 
                1, testGraph.sources(7).keySet().size());
        assertEquals("the vertex '7' should be mapped to vertex '9' with size 6", 
                6, testGraph.sources(7).get(9).intValue());
    }
    
    //covers case setting where edge did not exist
    //                          weight > 0
    @Test
    public void testSetNonZeroEdgeDidNotExist() {
        Graph<Integer> testGraph = Graph.empty();
        testGraph.add(34);
        testGraph.add(13);
        int previousWeight = testGraph.set(34, 13, 6);
        assertEquals("expected setting new edge where source did not exist to return 0", 0, previousWeight);
        assertEquals("vertices should have two vertices after setting new edge", 2, testGraph.vertices().size());
        //check vertex '34' added successfully
        assertEquals("the vertex '34' should have one target", 
                1, testGraph.targets(34).keySet().size());
        assertEquals("the vertex '34' should be mapped to vertex '13' with size 6", 
                6, testGraph.targets(34).get(13).intValue());
        //check vertex '13' added successfully
        assertEquals("the vertex '13' should have one source", 
                1, testGraph.sources(13).keySet().size());
        assertEquals("the vertex '13' should be mapped to vertex '34' with size 6", 
                6, testGraph.sources(13).get(34).intValue());
    }
    
    //covers case setting where edge already existed in graph
    //                          weight > 0
    @Test
    public void testSetNonZeroEdgeExisted() {
        Graph<Integer> testGraph = Graph.empty();
        int previousWeight = testGraph.set(4, 8, 6);
        assertEquals("expected setting edge that did not exist to return old weight of 0", 0, previousWeight);
        previousWeight = testGraph.set(4, 8, 8);
        assertEquals("expected setting edge that already existed to return old weight of 6", 6, previousWeight);
        assertEquals("vertices should still have two vertices after setting edge", 2, testGraph.vertices().size());
        assertEquals("the vertex '4' should have one target", 
                1, testGraph.targets(4).keySet().size());
        assertEquals("the vertex '4' should be mapped to vertex '8' with size 8", 
                8, testGraph.targets(4).get(8).intValue());
        assertEquals("the vertex '8' should have one source", 
                1, testGraph.sources(8).keySet().size());
        assertEquals("the vertex '8' should be mapped to vertex '4' with size 8", 
                8, testGraph.sources(8).get(4).intValue());
    }
    
    //covers case setting where weight = 0
    //                          source did not exist
    //                          edge did not exist
    @Test
    public void testSetZeroWeightSourceDidNotExist() {
        Graph<Integer> testGraph = Graph.empty();
        testGraph.add(1);
        int previousWeight = testGraph.set(2, 1, 0);
        assertEquals("expected setting edge where source did not exist to return 0", 0, previousWeight);
        assertEquals("vertices should have 1 vertex after setting new edge with 0 weight", 1, testGraph.vertices().size());

        assertEquals("the vertex '1' should have no targets", 
                0, testGraph.targets(1).keySet().size());
        assertEquals("the vertex '1' should have no sources", 
                0, testGraph.sources(1).keySet().size());
    }
    
    //covers case setting where weight = 0
    //                          target did not exist
    //                          edge did not exist
    @Test
    public void testSetZeroWeightTargetDidNotExist() {
        Graph<Integer> testGraph = Graph.empty();
        testGraph.add(6);
        int previousWeight = testGraph.set(6, 1, 0);
        assertEquals("expected setting edge where target did not exist to return 0", 0, previousWeight);
        assertEquals("vertices should have 1 vertex after setting new edge with 0 weight", 1, testGraph.vertices().size());
        assertEquals("the vertex '6' should have no targets", 
                0, testGraph.targets(6).keySet().size());
        assertEquals("the vertex '6' should have no sources", 
                0, testGraph.sources(6).keySet().size());
    }
    
    //covers case setting where weight = 0
    //                          edge already existed
    //                          --> removal of edge
    @Test
    public void testSetZeroWeightEdgeExisted() {
        Graph<Integer> testGraph = Graph.empty();
        int previousWeight = testGraph.set(3, 8, 6);
        previousWeight = testGraph.set(3, 8, 0);
        assertEquals("expected setting edge to 0 weight to return previous weight of 6", 6, previousWeight);
        assertEquals("vertices should still have 2 vertices after setting edge with 0 weight/removing edge", 
                2, testGraph.vertices().size());
        assertEquals("the vertex '3' should have no targets", 
                0, testGraph.targets(3).keySet().size());
        assertEquals("the vertex '8' should have no sources", 
                0, testGraph.sources(8).keySet().size());
    }

    //covers case where target does not exist in graph
    @Test
    public void testSourcesTargetDoesNotExist() {
        Graph<Integer> testGraph = Graph.empty();
        Map<Integer,Integer> sources = testGraph.sources(342);
        assertEquals("the vertex 'target does not exist bruh' should have no sources", 
                0, sources.size());
    }
    
    //covers case where target does exist in graph and has no sources
    @Test
    public void testSourcesTargetExistsNoSources() {
        Graph<Integer> testGraph = Graph.empty();
        Integer testTarget = 9;
        Map<Integer,Integer> sources = testGraph.sources(testTarget);
        assertEquals("the vertex " + testTarget + " should have no sources", 
                0, sources.size());
    }
    
    //covers case where target does exist in graph and has one source
    @Test
    public void testSourcesTargetExistsOneSource() {
        Graph<Integer> testGraph = Graph.empty();
        Integer testSource = 8;
        Integer testTarget = 7;
        testGraph.set(testSource, testTarget, 10);
        Map<Integer,Integer> sources = testGraph.sources(testTarget);
        assertEquals("the vertex " + testTarget + " should have one source", 
                1, sources.size());
        assertEquals("the vertex " + testTarget + " be mapped to with length " + 10, 
                10, sources.get(testSource).intValue());
    }

    //covers case where target does exist in graph and has more than one source
    @Test
    public void testSourcesTargetExistsMoreThanOneSource() {
        Graph<Integer> testGraph = Graph.empty();
        Integer testFirstSource = 64;
        Integer testSecondSource = 7;
        Integer testTarget = 2;
        testGraph.set(testFirstSource, testTarget, 10);
        testGraph.set(testSecondSource, testTarget, 6);
        Map<Integer,Integer> sources = testGraph.sources(testTarget);
        assertEquals("the vertex " + testTarget + " should have two sources", 
                2, sources.size());
        assertEquals("the vertex " + testTarget + " be mapped to length " + 10, 
                10, sources.get(testFirstSource).intValue());
        assertEquals("the vertex " + testTarget + " be mapped to length " + 6, 
                6, sources.get(testSecondSource).intValue());
    }
    
    //covers case where source does not exist in graph
    @Test
    public void testTargetsSourceDoesNotExist() {
        Graph<Integer> testGraph = Graph.empty();
        Map<Integer,Integer> targets = testGraph.targets(23123);
        assertEquals("the vertex 'source does not exist bruh' should have no targets", 
                0, targets.size());
    }
    
    //covers case where source does exist in graph and has no targets
    @Test
    public void testTargetsSourceExistsNoTargets() {
        Graph<Integer> testGraph = Graph.empty();
        Integer testSource = 18;
        Map<Integer,Integer> targets = testGraph.targets(testSource);
        assertEquals("the vertex " + testSource + " should have no targets", 
                0, targets.size());
    }
    
    //covers case where source does exist in graph and has one target
    @Test
    public void testTargetsSourceExistsOneTarget() {
        Graph<Integer> testGraph = Graph.empty();
        Integer testSource = 5;
        Integer testTarget = 6;
        testGraph.set(testSource, testTarget, 10);
        Map<Integer,Integer> targets = testGraph.targets(testSource);
        assertEquals("the vertex " + testSource + " should have one target", 
                1, targets.size());
        assertEquals("the vertex " + testTarget + " be mapped to length " + 10, 
                10, targets.get(testTarget).intValue());
    }

    //covers case where source does exist in graph and has more than one target
    @Test
    public void testTargetsSourceExistsMoreThanOneTarget() {
        Graph<Integer> testGraph = Graph.empty();
        Integer testSource = 10;
        Integer testFirstTarget = 3;
        Integer testSecondTarget = 100;
        testGraph.set(testSource, testFirstTarget, 10);
        testGraph.set(testSource, testSecondTarget, 6);
        Map<Integer,Integer> targets = testGraph.targets(testSource);
        assertEquals("the vertex " + testSource + " should have two targets", 
                2, targets.size());
        assertEquals("the vertex " + testFirstTarget + " be mapped to length " + 10, 
                10, targets.get(testFirstTarget).intValue());
        assertEquals("the vertex " + testSecondTarget + " be mapped to length " + 6, 
                6, targets.get(testSecondTarget).intValue());
    }
   
    //---------------------TESTS FOR GRAPH<INTEGER> END----------------------//
    
    
    
   //---------------------TESTS FOR GRAPH<DOUBLE> BEGIN----------------------//
    
    //covers case adding vertex to empty graph
    //            adding vertex that is not already in graph
    @Test
    public void testAddVertexToEmptyGraphDouble() {
        Graph<Double> testGraph = Graph.empty();
        boolean addedFirst = testGraph.add(1.);
        assertEquals("expected adding one vertex to empty graph to return true", true, addedFirst);
        assertEquals("vertices should have one vertex after adding one vertex", 1, testGraph.vertices().size());
    }
    
    //covers case adding vertex that is already in graph
    @Test
    public void testAddVertexAlreadyInGraphDouble() {
        Graph<Double> testGraph = Graph.empty();
        boolean addedFirst = testGraph.add(1.);
        boolean addedSecond = testGraph.add(1.);
        assertEquals("expected adding one vertex to empty graph to return true", true, addedFirst);
        assertEquals("expected adding second same vertex to graph to return false", false, addedSecond);
        assertEquals("vertices should have one vertex after adding one vertex and failing"
                + "to add one duplicate", 1, testGraph.vertices().size());
    }
   

    //covers case removing from empty graph
    @Test
    public void testRemoveVertexFromEmptyGraphDouble() {
        Graph<Double> testGraph = Graph.empty();
        boolean removeShouldFail = testGraph.remove(6.);
        assertEquals("expected removing from empty graph to return false", false, removeShouldFail);
    }
    
    //covers case removing vertex not in graph
    @Test
    public void testRemoveVertexNotInGraphDouble() {
        Graph<Double> testGraph = Graph.empty();
        testGraph.add(6.);
        testGraph.add(8.);
        boolean removeShouldFail = testGraph.remove(10.);
        assertEquals("expected removing node not in graph to return false", false, removeShouldFail);
    }
    
    //covers case removing vertex that has 0 edges to other vertices
    //            removing vertex that has 0 edges from other vertices
    //            removing vertex results in empty graph
    @Test
    public void testRemoveVertexWithNoEdgeToOrFromOtherDouble() {
        Graph<Double> testGraph = Graph.empty();
        testGraph.add(100.);
        boolean successfulRemoval = testGraph.remove(100.);
        assertEquals("expected removing from graph to return true", true, successfulRemoval);
        assertEquals("vertices should not have removed vertex", false, testGraph.vertices().contains(100.));
        assertEquals("vertices should have no vertices after removal", 0, testGraph.vertices().size());
    }
    
    //covers case removing vertex that has 1 edge to another vertex
    //            removing vertex results in nonempty graph
    @Test
    public void testRemoveVertexWithOneEdgeToOtherDouble() {
        Graph<Double> testGraph = Graph.empty();
        testGraph.set(10., 8., 6);
        boolean successfulRemoval = testGraph.remove(10.);
        assertEquals("expected removing from graph to return true", true, successfulRemoval);
        assertEquals("vertices should have one vertex after removal", 1, testGraph.vertices().size());
        assertEquals("the vertex 8 should have no more sources", 
                Collections.emptySet(), testGraph.sources(8.).keySet());
    }
    
    //covers case removing vertex that has > 1 edge to other vertices
    @Test
    public void testRemoveVertexWithMoreThanOneEdgeToOtherDouble() {
        Graph<Double> testGraph = Graph.empty();
        testGraph.set(6., 7., 6);
        testGraph.set(6., 5., 6);
        boolean successfulRemoval = testGraph.remove(6.);
        assertEquals("expected removing from graph to return true", true, successfulRemoval);
        assertEquals("vertices should have two vertices after removal", 2, testGraph.vertices().size());
        assertEquals("the vertex 7 should have no more sources", 
                Collections.emptySet(), testGraph.sources(7.).keySet());
        assertEquals("the vertex 5 should have no more sources", 
                Collections.emptySet(), testGraph.sources(5.).keySet());
    }
    
    //covers case removing vertex that has 1 edge from another vertex
    @Test
    public void testRemoveVertexWithOneEdgeFromOtherDouble() {
        Graph<Double> testGraph = Graph.empty();
        testGraph.set(23., 4., 6);
        boolean successfulRemoval = testGraph.remove(4.);
        assertEquals("expected removing from graph to return true", true, successfulRemoval);
        assertEquals("vertices should have one vertex after removal", 1, testGraph.vertices().size());
        assertEquals("the vertex '23' should have no more targets", 
                Collections.emptySet(), testGraph.targets(23.).keySet());
    }
    
    //covers case removing vertex that has > 1 edge from other vertices
    @Test
    public void testRemoveVertexWithMoreThanOneEdgeFromOtherDouble() {
        Graph<Double> testGraph = Graph.empty();
        testGraph.set(1., 9., 6);
        testGraph.set(12., 9., 6);
        boolean successfulRemoval = testGraph.remove(9.);
        assertEquals("expected removing from graph to return true", true, successfulRemoval);
        assertEquals("vertices should have two vertices after removal", 2, testGraph.vertices().size());
        assertEquals("the vertex '1' should have no more targets", 
                Collections.emptySet(), testGraph.targets(1.).keySet());
        assertEquals("the vertex '12' should have no more targets", 
                Collections.emptySet(), testGraph.targets(12.).keySet());
    }
   

    //covers case where source and target are same vertex
    @Test
    public void testSetSourceAndTargetAreSameVertexDouble() {
        Graph<Double> testGraph = Graph.empty();
        testGraph.add(45.);
        int previousWeight = testGraph.set(45., 45., 6);
        assertEquals("expected setting new edge to return 0", 0, previousWeight);
        assertEquals("vertices should still have just one vertex after setting new edge", 1, testGraph.vertices().size());
        //check vertex 'going from' added successfully via targets
        assertEquals("the vertex '45' should have one target", 
                1, testGraph.targets(45.).keySet().size());
        assertEquals("the vertex '45' should be mapped to vertex '45' with size 6", 
                6, testGraph.targets(45.).get(45.).intValue());
        //check vertex 'going to' added successfully via sources
        assertEquals("the vertex '45' should have one source", 
                1, testGraph.sources(45.).keySet().size());
        assertEquals("the vertex '45' should be mapped to vertex '45' with size 6", 
                6, testGraph.sources(45.).get(45.).intValue());
    }
    
    //covers case setting where source did not already exist in graph
    //                          target already existed in graph
    //                          weight > 0
    @Test
    public void testSetSourceDidNotExistDouble() {
        Graph<Double> testGraph = Graph.empty();
        testGraph.add(10.);
        int previousWeight = testGraph.set(2., 10., 6);
        assertEquals("expected setting new edge where source did not exist to return 0", 0, previousWeight);
        assertEquals("vertices should have two vertices after setting new edge", 2, testGraph.vertices().size());
        //check vertex 'going from' added successfully
        assertEquals("the vertex '2' should have one target", 
                1, testGraph.targets(2.).keySet().size());
        assertEquals("the vertex '2' should be mapped to vertex '10' with size 6", 
                6, testGraph.targets(2.).get(10.).intValue());
        //check vertex 'going to' added successfully
        assertEquals("the vertex '10' should have one source", 
                1, testGraph.sources(10.).keySet().size());
        assertEquals("the vertex '10' should be mapped to vertex '2' with size 6", 
                6, testGraph.sources(10.).get(2.).intValue());
    }
    
    //covers case setting where target did not already exist in graph
    //                          source already existed in graph
    //                          weight > 0
    @Test
    public void testSetTargetDidNotExistDouble() {
        Graph<Double> testGraph = Graph.empty();
        testGraph.add(9.);
        int previousWeight = testGraph.set(9., 7., 6);
        assertEquals("expected setting new edge where source did not exist to return 0", 0, previousWeight);
        assertEquals("vertices should have two vertices after setting new edge", 2, testGraph.vertices().size());
        //check vertex '9' added successfully
        assertEquals("the vertex '9' should have one target", 
                1, testGraph.targets(9.).keySet().size());
        assertEquals("the vertex '9' should be mapped to vertex '7' with size 6", 
                6, testGraph.targets(9.).get(7.).intValue());
        //check vertex '7' added successfully
        assertEquals("the vertex '7' should have one source", 
                1, testGraph.sources(7.).keySet().size());
        assertEquals("the vertex '7' should be mapped to vertex '9' with size 6", 
                6, testGraph.sources(7.).get(9.).intValue());
    }
    
    //covers case setting where edge did not exist
    //                          weight > 0
    @Test
    public void testSetNonZeroEdgeDidNotExistDouble() {
        Graph<Double> testGraph = Graph.empty();
        testGraph.add(34.);
        testGraph.add(13.);
        int previousWeight = testGraph.set(34., 13., 6);
        assertEquals("expected setting new edge where source did not exist to return 0", 0, previousWeight);
        assertEquals("vertices should have two vertices after setting new edge", 2, testGraph.vertices().size());
        //check vertex '34' added successfully
        assertEquals("the vertex '34' should have one target", 
                1, testGraph.targets(34.).keySet().size());
        assertEquals("the vertex '34' should be mapped to vertex '13' with size 6", 
                6, testGraph.targets(34.).get(13.).intValue());
        //check vertex '13' added successfully
        assertEquals("the vertex '13' should have one source", 
                1, testGraph.sources(13.).keySet().size());
        assertEquals("the vertex '13' should be mapped to vertex '34' with size 6", 
                6, testGraph.sources(13.).get(34.).intValue());
    }
    
    //covers case setting where edge already existed in graph
    //                          weight > 0
    @Test
    public void testSetNonZeroEdgeExistedDouble() {
        Graph<Double> testGraph = Graph.empty();
        int previousWeight = testGraph.set(4., 8., 6);
        assertEquals("expected setting edge that did not exist to return old weight of 0", 0, previousWeight);
        previousWeight = testGraph.set(4., 8., 8);
        assertEquals("expected setting edge that already existed to return old weight of 6", 6, previousWeight);
        assertEquals("vertices should still have two vertices after setting edge", 2, testGraph.vertices().size());
        assertEquals("the vertex '4' should have one target", 
                1, testGraph.targets(4.).keySet().size());
        assertEquals("the vertex '4' should be mapped to vertex '8' with size 8", 
                8, testGraph.targets(4.).get(8.).intValue());
        assertEquals("the vertex '8' should have one source", 
                1, testGraph.sources(8.).keySet().size());
        assertEquals("the vertex '8' should be mapped to vertex '4' with size 8", 
                8, testGraph.sources(8.).get(4.).intValue());
    }
    
    //covers case setting where weight = 0
    //                          source did not exist
    //                          edge did not exist
    @Test
    public void testSetZeroWeightSourceDidNotExistDouble() {
        Graph<Double> testGraph = Graph.empty();
        testGraph.add(1.);
        int previousWeight = testGraph.set(2., 1., 0);
        assertEquals("expected setting edge where source did not exist to return 0", 0, previousWeight);
        assertEquals("vertices should have 1 vertex after setting new edge with 0 weight", 1, testGraph.vertices().size());

        assertEquals("the vertex '1' should have no targets", 
                0, testGraph.targets(1.).keySet().size());
        assertEquals("the vertex '1' should have no sources", 
                0, testGraph.sources(1.).keySet().size());
    }
    
    //covers case setting where weight = 0
    //                          target did not exist
    //                          edge did not exist
    @Test
    public void testSetZeroWeightTargetDidNotExistDouble() {
        Graph<Double> testGraph = Graph.empty();
        testGraph.add(6.);
        int previousWeight = testGraph.set(6., 1., 0);
        assertEquals("expected setting edge where target did not exist to return 0", 0, previousWeight);
        assertEquals("vertices should have 1 vertex after setting new edge with 0 weight", 1, testGraph.vertices().size());
        assertEquals("the vertex '6' should have no targets", 
                0, testGraph.targets(6.).keySet().size());
        assertEquals("the vertex '6' should have no sources", 
                0, testGraph.sources(6.).keySet().size());
    }
    
    //covers case setting where weight = 0
    //                          edge already existed
    //                          --> removal of edge
    @Test
    public void testSetZeroWeightEdgeExistedDouble() {
        Graph<Double> testGraph = Graph.empty();
        int previousWeight = testGraph.set(3., 8., 6);
        previousWeight = testGraph.set(3., 8., 0);
        assertEquals("expected setting edge to 0 weight to return previous weight of 6", 6, previousWeight);
        assertEquals("vertices should still have 2 vertices after setting edge with 0 weight/removing edge", 
                2, testGraph.vertices().size());
        assertEquals("the vertex '3' should have no targets", 
                0, testGraph.targets(3.).keySet().size());
        assertEquals("the vertex '8' should have no sources", 
                0, testGraph.sources(8.).keySet().size());
    }

    //covers case where target does not exist in graph
    @Test
    public void testSourcesTargetDoesNotExistDouble() {
        Graph<Double> testGraph = Graph.empty();
        Map<Double,Integer> sources = testGraph.sources(342.);
        assertEquals("the vertex 'target does not exist bruh' should have no sources", 
                0, sources.size());
    }
    
    //covers case where target does exist in graph and has no sources
    @Test
    public void testSourcesTargetExistsNoSourcesDouble() {
        Graph<Double> testGraph = Graph.empty();
        Double testTarget = 9.;
        Map<Double,Integer> sources = testGraph.sources(testTarget);
        assertEquals("the vertex " + testTarget + " should have no sources", 
                0, sources.size());
    }
    
    //covers case where target does exist in graph and has one source
    @Test
    public void testSourcesTargetExistsOneSourceDouble() {
        Graph<Double> testGraph = Graph.empty();
        Double testSource = 8.;
        Double testTarget = 7.;
        testGraph.set(testSource, testTarget, 10);
        Map<Double,Integer> sources = testGraph.sources(testTarget);
        assertEquals("the vertex " + testTarget + " should have one source", 
                1, sources.size());
        assertEquals("the vertex " + testTarget + " be mapped to with length " + 10, 
                10, sources.get(testSource).intValue());
    }

    //covers case where target does exist in graph and has more than one source
    @Test
    public void testSourcesTargetExistsMoreThanOneSourceDouble() {
        Graph<Integer> testGraph = Graph.empty();
        Integer testFirstSource = 64;
        Integer testSecondSource = 7;
        Integer testTarget = 2;
        testGraph.set(testFirstSource, testTarget, 10);
        testGraph.set(testSecondSource, testTarget, 6);
        Map<Integer,Integer> sources = testGraph.sources(testTarget);
        assertEquals("the vertex " + testTarget + " should have two sources", 
                2, sources.size());
        assertEquals("the vertex " + testTarget + " be mapped to length " + 10, 
                10, sources.get(testFirstSource).intValue());
        assertEquals("the vertex " + testTarget + " be mapped to length " + 6, 
                6, sources.get(testSecondSource).intValue());
    }
    
    //covers case where source does not exist in graph
    @Test
    public void testTargetsSourceDoesNotExistDouble() {
        Graph<Double> testGraph = Graph.empty();
        Map<Double,Integer> targets = testGraph.targets(23123.);
        assertEquals("the vertex 'source does not exist bruh' should have no targets", 
                0, targets.size());
    }
    
    //covers case where source does exist in graph and has no targets
    @Test
    public void testTargetsSourceExistsNoTargetsDouble() {
        Graph<Double> testGraph = Graph.empty();
        Double testSource = 18.;
        Map<Double,Integer> targets = testGraph.targets(testSource);
        assertEquals("the vertex " + testSource + " should have no targets", 
                0, targets.size());
    }
    
    //covers case where source does exist in graph and has one target
    @Test
    public void testTargetsSourceExistsOneTargetDouble() {
        Graph<Double> testGraph = Graph.empty();
        Double testSource = 5.;
        Double testTarget = 6.;
        testGraph.set(testSource, testTarget, 10);
        Map<Double,Integer> targets = testGraph.targets(testSource);
        assertEquals("the vertex " + testSource + " should have one target", 
                1, targets.size());
        assertEquals("the vertex " + testTarget + " be mapped to length " + 10, 
                10, targets.get(testTarget).intValue());
    }

    //covers case where source does exist in graph and has more than one target
    @Test
    public void testTargetsSourceExistsMoreThanOneTargetDouble() {
        Graph<Double> testGraph = Graph.empty();
        Double testSource = 10.;
        Double testFirstTarget = 3.;
        Double testSecondTarget = 100.;
        testGraph.set(testSource, testFirstTarget, 10);
        testGraph.set(testSource, testSecondTarget, 6);
        Map<Double,Integer> targets = testGraph.targets(testSource);
        assertEquals("the vertex " + testSource + " should have two targets", 
                2, targets.size());
        assertEquals("the vertex " + testFirstTarget + " be mapped to length " + 10, 
                10, targets.get(testFirstTarget).intValue());
        assertEquals("the vertex " + testSecondTarget + " be mapped to length " + 6, 
                6, targets.get(testSecondTarget).intValue());
    }
   
    //---------------------TESTS FOR GRAPH<DOUBLE> END----------------------//
}
