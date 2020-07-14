/* Copyright (c) 2015-2017 MIT 6.005/6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;


/**
 * Tests for GraphPoet.
 */
public class GraphPoetTest {
    /*
     * Testing strategy for GraphPoet:
     * 
     * GraphPoet(File corpus):
     *      - corpus does not exist / not found
     *      - corpus is empty
     *      - corpus contains text all on one line
     *      - corpus contains multiple lines
     * 
     * GraphPoet(String corpus):
     *      - empty string (corpus.length() = 0)
     *      - number of words = 1
     *      - number of words > 1
     * 
     * String poem(String input):
     *    input:
     *      - empty string (input.length() = 0)
     *      - number of words = 1
     *      - number of words > 1
     *      - has no possible bridge words
     *      - has one possible bridge word
     *      - has multiple possible bridge words (tied)
     *      - has caps
     *      - has repeated words
     *      - has punctuation
     *      
     * toString():
     *      - graph has no vertices
     *      - graph has one vertex
     *      - graph has more than one vertex
    */
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    
    @Test(expected=IOException.class)
    public void testPoemFileNotFound() throws IOException {
        File testFile = new File("test/poet/NICETRYBRuh.txt");
        GraphPoet testPoet = new GraphPoet(testFile); 
    }
    
    //covers case where GraphPoet made from empty file
    @Test
    public void testPoemEmptyFileCorpus() throws IOException {
        File testFile = new File("test/poet/emptyTest");
        GraphPoet testPoet = new GraphPoet(testFile);        
        String input = "hello hello";
        String actual = testPoet.poem(input);
        assertEquals("test poem when corpus from empty file is incorrect", input, actual);    
    }
    
    //covers case where GraphPoet made from file all from one line
    //                  corpus is one word long
    @Test
    public void testPoemFileOneLine() throws IOException {
        File testFile = new File("test/poet/oneLineTest");
        GraphPoet testPoet = new GraphPoet(testFile);        
        String input = "heLlO HellO";
        String expected = "heLlO HellO";
        String actual = testPoet.poem(input);
        assertEquals("produced poem when corpus was read from a one-line file was not as expected", expected, actual);        
    }
    
    //covers case where GraphPoet made from file from multiple lines
    //                  corpus has more than one word
    //                  input has repetition 
    @Test
    public void testPoemFileMultipleLines() throws IOException {
        File testFile = new File("test/poet/firstTest");
        GraphPoet testPoet = new GraphPoet(testFile);        
        String input = "hELLo hellO Hello";
        String expected = "hELLo hello hellO hello Hello";
        String actual = testPoet.poem(input);
        assertEquals("produced poem when corpus was read from multi-line poem was not as expected", expected, actual);        
    }
 
    //covers case where GraphPoet made from string of one word
    //                  input is empty
    @Test
    public void testPoemStringInputEmpty() {
        String corpus = "yay";
        GraphPoet testPoet = new GraphPoet(corpus);
        String input = "";
        String expected = "";
        String actual = testPoet.poem(input);
        assertEquals("produced poem when corpus was made from one-word string and input was empty was not as expected", 
                     expected, actual);      
    }
    
    //covers case where GraphPoet made from an empty string
    //                  corpus is empty
    @Test
    public void testPoemStringCorpusEmpty() {
        String corpus = "";
        GraphPoet testPoet = new GraphPoet(corpus);
        String input = "Seek to explore new and exciting synergies!";
        String expected = "Seek to explore new and exciting synergies!";
        String actual = testPoet.poem(input);
        assertEquals("produced poem when corpus was made from empty and poem was not empty was not as expected", 
                expected, actual);      
    }

    //covers case where GraphPoet made from a string with more than one word
    //                  corpus number of words > 1
    //                  multiple bridge words possible
    @Test
    public void testPoemStringCorpusMultipleBridges() {
        GraphPoet testPoet = new GraphPoet("To explore great new explore strange new worlds\nTo seek out new life and new civilizations");
        String input = "Seek to explore new and exciting synergies!";
        String expectedFirst = "Seek to explore strange new life and exciting synergies!";
        String expectedSecond = "Seek to explore great new life and exciting synergies!";
        String actual = testPoet.poem(input);
        assertTrue("produced poem was not as expected", actual.equals(expectedFirst) ||
                                                        actual.equals(expectedSecond));      
    }
    
    //taken from reading
    //covers case where one bridge word possible
    @Test
    public void testPoemFromReading() {
        GraphPoet testPoet = new GraphPoet("This is a test of the Mugar Omni Theater sound system.");
        String input = "Test the system.";
        String expected = "Test of the system.";
        String actual = testPoet.poem(input);
        assertEquals("produced poem from reading was not as expected", expected, actual);        
    }
    
    //covers case where GraphPoet made from a string with more than one word
    //                  corpus number of words > 1
    //                  no bridge words possible
    @Test
    public void testPoemStringCorpusMultipleWordsNoBridge() {
        GraphPoet testPoet = new GraphPoet("To explore great new worlds\nTo seek out new life and new civilizations");
        String input = "I seek to sleep!";
        String expected = "I seek to sleep!";
        String actual = testPoet.poem(input);
        assertEquals("produced poem was not as expected", expected, actual);      
    }
    
    //covers case where GraphPoet made from a string with more than one word
    //                  corpus number of words > 1
    //                  input is one word long
    @Test
    public void testPoemStringCorpusMultipleWordsOneWordInput() {
        GraphPoet testPoet = new GraphPoet("To explore great new worlds\nTo seek out new life and new civilizations");
        String input = "sleep!";
        String expected = "sleep!";
        String actual = testPoet.poem(input);
        assertEquals("produced poem was not as expected", expected, actual);      
    }
    
    //covers case where GraphPoet made from a string with more than one word
    //                  corpus number of words > 1
    //                  capitalization in inputs and corpus
    @Test
    public void testPoemStringCorpusMultipleWordsCapitalization() {
        GraphPoet testPoet = new GraphPoet("To exPlore tHe DARk dEptHs of sleep");
        String input = "I seek to explore the depths of sleep!";
        String expected = "I seek to explore the dark depths of sleep!";
        String actual = testPoet.poem(input);
        assertEquals("produced poem was not as expected", expected, actual);      
    }
    
    //covers case where GraphPoet made from a string with more than one word
    //                  corpus number of words > 1
    //                  punctuation in inputs and corpus
    @Test
    public void testPoemStringCorpusMultipleWordsPunctuation() {
        GraphPoet testPoet = new GraphPoet("To exPlore tHe dark!!!!!!!!! dEptHs of sleep");
        String input = "I seek to explore the depths of sleep!";
        String expected = "I seek to explore the dark!!!!!!!!! depths of sleep!";
        String actual = testPoet.poem(input);
        assertEquals("produced poem was not as expected", expected, actual);      
    }
    
    //NOTE: For rep exposure safety, I chose NOT to expose 'graph'.
    
    //covers case where graph is empty (no vertices, vertices.size() = 0)
    @Test
    public void testToStringEmptyVertices(){
        GraphPoet testPoet = new GraphPoet("");
        String expected = "GraphPoet with vertices:";
        String actual = testPoet.toString();
        assertEquals("toString was not as expected", expected, actual);
    }
    
    //covers case where vertices.size() = 1
    @Test
    public void testToStringOneVertex(){
        String vertex = "AYEEEEE";
        GraphPoet testPoet = new GraphPoet(vertex);
        String expected = "GraphPoet with vertices: "+vertex.toLowerCase();
        String actual = testPoet.toString();
        assertEquals("toString was not as expected", expected, actual);
    }
    
    //covers case where vertices.size() > 1
    @Test
    public void testToStringMoreThanOneVertex(){
        String firstVertex = "heya";
        String secondVertex = "howru";
        List<String> vertices = Arrays.asList(firstVertex, secondVertex);
        GraphPoet testPoet = new GraphPoet(firstVertex + " " + secondVertex);
        String actual = testPoet.toString();
        for (String vertex : vertices){
            assertTrue("toString did not behave as expected", actual.contains(vertex));
        }
    }
    
   
}
