/* Copyright (c) 2015-2017 MIT 6.005/6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import graph.Graph;

/**
 * A graph-based poetry generator.
 * 
 * <p>GraphPoet is initialized with a corpus of text, which it uses to derive a
 * word affinity graph.
 * Vertices in the graph are words. Words are defined as non-empty
 * case-insensitive strings of non-space non-newline characters. They are
 * delimited in the corpus by spaces, newlines, or the ends of the file.
 * Edges in the graph count adjacencies: the number of times "w1" is followed by
 * "w2" in the corpus is the weight of the edge from w1 to w2.
 * 
 * <p>For example, given this corpus:
 * <pre>    Hello, HELLO, hello, goodbye!    </pre>
 * <p>the graph would contain two edges:
 * <ul><li> ("hello,") -> ("hello,")   with weight 2
 *     <li> ("hello,") -> ("goodbye!") with weight 1 </ul>
 * <p>where the vertices represent case-insensitive {@code "hello,"} and
 * {@code "goodbye!"}.
 * 
 * <p>Given an input string, GraphPoet generates a poem by attempting to
 * insert a bridge word between every adjacent pair of words in the input.
 * Words are defined and delimited as in the corpus.
 * The bridge word between input words "w1" and "w2" will be some "b" such that
 * w1 -> b -> w2 is a two-edge-long path with maximum-weight weight among all
 * the two-edge-long paths from w1 to w2 in this poet's affinity graph.
 * If there are no such paths, no bridge word is inserted.
 * In the output poem, input words retain their original case, while bridge
 * words are lower case. The whitespace between every word in the poem is a
 * single space, with no whitespace at the beginning or end.
 * 
 * <p>For example, given this corpus:
 * <pre>    This is a test of the Mugar Omni Theater sound system.    </pre>
 * <p>on this input:
 * <pre>    Test the system.    </pre>
 * <p>the output poem would be:
 * <pre>    Test of the system.    </pre>
 * 
 */
public class GraphPoet {
    
    private final Graph<String> graph = Graph.empty();
    
    // Abstraction function:
    //   AF(graph) = a poet-based Graph generator that, given a corpus of text
    //               will generate a word affinity graph where each vertex in the graph
    //               is a word.
    // Representation invariant:
    //   Vertices in graph cannot be the empty string. ("")
    // Safety from rep exposure:
    //   poem() and toString() return immutable Strings.
    //   graph is a private variable that is not returned from any method.
    
    /**
     * Create a new poet with the graph from corpus (as described above).
     * 
     * @param corpus text file from which to derive the poet's affinity graph
     * @throws IOException if the corpus file cannot be found or read
     */
    public GraphPoet(File corpus) throws IOException {   
        String corpusString = "";
        String line;
        FileReader fileReader = new FileReader(corpus);
        BufferedReader bufferReader = new BufferedReader(fileReader);
        while ((line = bufferReader.readLine()) != null){
            corpusString += line + " ";
        }
        fileReader.close();
        bufferReader.close();

        populateGraph(corpusString);
        checkRep();
    }

    /**
     * Creates a new poet with the graph from corpus (as described above)
     * @param corpus string to create word affinity graph from
     */
    public GraphPoet(String corpus) {
        populateGraph(corpus);
        checkRep();
    }
    
    /**
    * Given a corpus, creates and modifies graph, the word affinity graph (where each vertex in the graph
                   is a word) associated with this corpus
     * @param corpus string to create word affinity graph from
     */
    private void populateGraph(String corpus){
        //updates graph based on corpus 
        List<String> corpusList = stringSplitterCaseInsensitive(corpus);
        for (int i = 0; i < corpusList.size()-1; i++){
            String currentWord = corpusList.get(i);
            String nextWord = corpusList.get(i+1);
            //check if already an edge between the two words; if so, update; otherwise set new edge to 1
            Map<String, Integer> wordTargets = graph.targets(currentWord);
            if (wordTargets.containsKey(nextWord)){
                int oldWeight = wordTargets.get(nextWord);
                graph.set(currentWord, nextWord, oldWeight + 1);
            } else {
                graph.set(currentWord, nextWord, 1);
            }
        }
        if (!corpusList.isEmpty()){
            //add last sadly otherwise neglected vertex to graph :D
            graph.add(corpusList.get(corpusList.size()-1));
        }
        checkRep();
    }
    
    /**
     * Checks that the rep invariant holds.
     */
    private void checkRep(){
        for (String vertex : graph.vertices()){
            assert (!vertex.equals(""));
        }
    }

    /**
     * Given a input string, returns the list of case-insensitive 'words' split along spaces.
     * Extra spaces are ignored; ex: "hello \n    hi" --> [hello, hi]
     * @param input string to split on
     * @return a list in which each element is a word from the original string
     */
    private List<String> stringSplitterCaseInsensitive(String input){
        input = input.toLowerCase();
        String[] inputSplit = input.split("\\s");
        List<String> inputList = new ArrayList<String>();
        for (int i = 0; i < inputSplit.length; i++){
            String inputWord = inputSplit[i];
            if (!inputWord.equals("")){
                inputList.add(inputWord);
            }
        }
        checkRep();
        return inputList;
    }
    
    /**
     * Given a input string, returns the list of case-insensitive 'words' split along spaces.
     * Extra spaces are ignored; ex: "hello \n    hi" --> [hello, hi]
     * @param input string to split on
     * @return a list in which each element is a word from the original string
     */
    private List<String> stringSplitterCaseSensitive(String input){
        String[] inputSplit = input.split("\\s+");
        List<String> inputList = new ArrayList<String>();
        for (int i = 0; i < inputSplit.length; i++){
            String inputWord = inputSplit[i];
            if (!inputWord.equals("")){
                inputList.add(inputWord);
            }
        }
        checkRep();
        return inputList;
    }
    
    /**
     * Return bridge word between source and target. A bridge word between source and target
     * will be some "b" such that source --> b --> target is a two-edge long path with maximum weight
     * along the two-edge long paths from source to target in this poet's affinity graph. 
     * In case of ties, arbitrarily returns the first word it finds of that maximal bridge length.
     * @param source vertex from
     * @param target vertex to
     * @return empty string if no bridge word between source or target, otherwise, return
     * maximal bridge word
     * otherwise, returns the empty string (which is used to signal a lack of a bridge word)
     */
    private String getBridgeWord(String source, String target){
        Map<String, Integer> sourceTargets = graph.targets(source);
        int maxPathWeight = 0;
        String bridge = "";
        for (String sourceTarget : sourceTargets.keySet()){ //source --> another word
            Map<String, Integer> targetTargets = graph.targets(sourceTarget);
            if (targetTargets.containsKey(target)){//if two-edge path exists; another word --> target
                int weight = sourceTargets.get(sourceTarget) + targetTargets.get(target);
                if (maxPathWeight < weight){
                    maxPathWeight = weight;
                    bridge = sourceTarget;
                }
            }
        }
        checkRep();
        return bridge.toLowerCase();
    }
    
    /**
     * Generate a poem.
     * 
     * @param input string from which to create the poem
     * @return poem (as described above)
     */
    public String poem(String input) {
        String output = "";
        String space = " ";
        if (input.equals("")){
            return input;
        }
        List<String> inputList = stringSplitterCaseSensitive(input);
        int allWordsButLast = inputList.size()-1; 
        for (int i = 0; i < allWordsButLast; i++){
            if (!inputList.get(i).equals("")){
            String currentWord = inputList.get(i);
            String nextWord = inputList.get(i+1);
            output += currentWord + space;
            String insensitiveCurrentWord = currentWord.toLowerCase();
            String insensitiveNextWord = nextWord.toLowerCase();
            String bridge = getBridgeWord(insensitiveCurrentWord, insensitiveNextWord);
            if (!bridge.equals("")){
                output += bridge + space;
            }
          }
        }
        output += inputList.get(inputList.size()-1);
        checkRep();
        return output;
    }
    
    /**
     * Returns a string with the graph that is described by this graphPoet.
     * @return a string with all of the vertices in graph
     */
    public String toString(){
        String vertices = "";
        for (String vertex : graph.vertices()){
            vertices += " " + vertex;
        }
        return "GraphPoet with vertices:"+vertices;
    }
}
