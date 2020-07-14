package norn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

/**
 * Tests for DefinedMailingLists ADT.
 */
public class DefinedMailingListsTest {

    /*
     * Testing Strategy for DefinedMailingLists.java:
     * 
     * get(String name):
     *      - name not a valid key
     *      - name is a valid key (case-sensitive, case-insensitive)
     * 
     * addMailingList(final String name, final String expression)
     *      - name already exists in map
     *      - name does not exist in map
     * 
     * getDependencies
     *      - map is empty
     *      - 1 entry
     *      - more than one entry
     *      
     * addDependencies
     *      - no keys overlap
     *      - some keys overlap 
     *      - all keys overlap
     *      - original input map is not modified
     *      
     * toString():
     *      - map with 0, 1, >1 mappings
     */
    
    /**
     * Helper method to check the correctness of toString().
     * 
     * Checks that the output of a command has a correct string representation, without 
     * specifying the order that mailing addresses appear in the string. 
     * 
     * @param contains a list of the mailing addresses that should appear in the string. 
     * @param actual the output to test correctness of
     */
    private void assertCorrectStringRep(List<String> contains, String actual) {
        //check that every string in contains appears in actual
        for (String makeEmail : contains) {
            assertTrue(actual.contains(makeEmail));
        }
    }
    
    //testing for getMailingLists():
    
    //covers case where definedMailingLists is empty (0 mappings)
    @Test
    public void testGetMailingListsEmpty(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        assertEquals(definedMailingLists.toString(), "");
    }
 
    //covers case where definedMailingLists has one mapping (1 mapping)
    @Test
    public void testGetMailingListsOneMapping(){
        Map<String, String> input = new HashMap<>();
        input.put("name1", "a@a, b@b");
        String output = "a@a,b@b";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(input);
        assertEquals(definedMailingLists.get("name1"), output);
    }
    
    //covers case where definedMailingLists has more than one mapping (> 1 mapping)
    @Test
    public void testGetMailingListsMoreThanOneMapping(){
          Map<String, String> input = new HashMap<>();
          input.put("name1", "a@a, b@b");
          input.put("name2", "name1");
          String output1 = "a@a,b@b";
          String output2 = "name1";
          DefinedMailingLists definedMailingLists = new DefinedMailingLists(input);
          assertEquals(definedMailingLists.get("name1"), output1);
          assertEquals(definedMailingLists.get("name2"), output2);
      }
    
    //covers case where name is not a valid key
    @Test
    public void testGetNameNotValid(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        String expected = "";
        String actual = definedMailingLists.get("name");
        assertEquals("getting from defined mailing lists not as expected", expected, actual);
    }
    
    //covers case where name is a valid key
    @Test
    public void testGetNameIsValid(){
        Map<String, String> input = new HashMap<>();
        input.put("name1", "a@a, b@b");
        input.put("name2", "name1");
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(input);
        String expected = "a@a,b@b";
        String actual = definedMailingLists.get("name1");
        assertEquals("getting from defined mailing lists not as expected", expected, actual);
    }
    
    //covers case where name is a valid key (case-insensitive)
    @Test
    public void testGetNameIsValidCase(){
        Map<String, String> input = new HashMap<>();
        input.put("name1", "a@a, b@b");
        input.put("name2", "name1");
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(input);
        String expected = "a@a,b@b";
        String actual = definedMailingLists.get("NaMe1");
        assertEquals("getting from defined mailing lists not as expected", expected, actual);
    }
    
  //covers case where name does not previously exists in map
    @Test
    public void testAddMailingListNotYetDefined(){
        Map<String, String> input = new HashMap<>();
        input.put("name1", "a@a, b@b");
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(input);
        definedMailingLists.addMailingList("name2", "this+is+trolol@mit.edu");
        String expected = "this+is+trolol@mit.edu";
        String actual = definedMailingLists.get("name2");
        assertEquals("getting from defined mailing lists not as expected", expected, actual);
    }
    
    //covers case where name already exists in map (already defined)
    @Test
    public void testAddMailingListAlreadyDefined(){
        Map<String, String> input = new HashMap<>();
        input.put("name1", "a@a, b@b");
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(input);
        definedMailingLists.addMailingList("name1", "this+is+trolol@mit.edu");
        String expected = "this+is+trolol@mit.edu";
        String actual = definedMailingLists.get("name1");
        assertEquals("getting from defined mailing lists not as expected", expected, actual);
    }
    
    //covers case getDependencies on an empty map
    //covers addDependencies (of an empty map)
    @Test
    public void testGetDependenciesEmpty() {
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Map<String, Set<String>> output = definedMailingLists.getDependencies();
        assertEquals(0, output.size());
        
        Map<String, Set<String>> add = new HashMap<String, Set<String>>();
        definedMailingLists.addDependencies(add);
        output = definedMailingLists.getDependencies();
        assertEquals(0, output.size());
    }
    
    //covers case where getDependencies map has one entry
    //covers case of adding map with no overlaps 
    //check mutability issues of adding a map in addDependencies
    @Test
    public void testGetAddDependenciesOne() {
        Map<String, String> input = new HashMap<>();
        input.put("name1", "a@a, b");
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(input);
        Map<String, Set<String>> add = new HashMap<>();
        Set<String> dependencies = new HashSet<String>();
        dependencies.add("b");
        add.put("name1", dependencies);
        definedMailingLists.addDependencies(add);
        
        Map<String, Set<String>> output = definedMailingLists.getDependencies();
        assertEquals(1, output.size());
        assertTrue(output.get("name1").contains("b"));
        
        //check that changing add does not change the output
        add.remove("name1");
        output = definedMailingLists.getDependencies();
        assertEquals(1, output.size());
    }
    
    //covers case where getDependencies map has one entry
    //covers case of adding map with no overlaps 
    //covers case of adding empty map (no change)
    //check mutability issues of adding a map in addDependencies
    @Test
    public void testGetAddDependenciesEmpty() {
        Map<String, String> input = new HashMap<>();
        input.put("name1", "a@a, b");
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(input);
        Map<String, Set<String>> add = new HashMap<>();
        Set<String> dependencies = new HashSet<String>();
        dependencies.add("b");
        add.put("name1", dependencies);
        definedMailingLists.addDependencies(add);
        definedMailingLists.addDependencies(Collections.emptyMap());
        
        Map<String, Set<String>> output = definedMailingLists.getDependencies();
        assertEquals(output.size(), 1);
        assertTrue(output.get("name1").contains("b"));
    }
    
    //covers case where getDependencies map has one entry
    //covers case of adding map with no overlaps 
    //check mutability issues of adding a map in addDependencies
    @Test
    public void testGetAddDependenciesMany() {
        Map<String, String> input = new HashMap<>();
        input.put("name1", "a@a, b");
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(input);
        Map<String, Set<String>> add = new HashMap<>();
        Set<String> dependencies = new HashSet<String>();
        dependencies.add("b");
        add.put("name1", dependencies);
        definedMailingLists.addDependencies(add);
        definedMailingLists.addDependencies(Collections.emptyMap());
        
        Map<String, Set<String>> output = definedMailingLists.getDependencies();
        assertEquals(output.size(), 1);
        assertTrue(output.get("name1").contains("b"));
    }
    
    //covers case where getDependencies map has one entry
    //covers case of adding map with no overlaps 
    //check mutability issues of adding a map in addDependencies
    @Test
    public void testGetAddDependenciesNoOverlap() {
        Map<String, String> input = new HashMap<>();
        input.put("name1", "a@a, b");
        input.put("b", "c, b");
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(input);
        Map<String, Set<String>> add = new HashMap<>();
        Set<String> dependencies1 = new HashSet<String>();
        dependencies1.add("b");
        add.put("name1", dependencies1);
        definedMailingLists.addDependencies(add);
        
        Set<String> dependencies2 = new HashSet<String>();
        dependencies2.add("c");
        dependencies2.add("b");
        add.put("name1", dependencies2);
        definedMailingLists.addDependencies(add);
        
        Map<String, Set<String>> output = definedMailingLists.getDependencies();
        assertEquals(output.get("name1").size(), 2);
        assertTrue(output.get("name1").contains("b"));
        assertTrue(output.get("name1").contains("c"));
    }
    
    //covers case where getDependencies map has one entry
    //covers case of adding map with some overlap
    //check mutability issues of adding a map in addDependencies
    @Test
    public void testGetAddDependenciesAllOverlap() {
        Map<String, String> input = new HashMap<>();
        input.put("name1", "a@a, b");
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(input);
        Map<String, Set<String>> add = new HashMap<>();
        Set<String> dependencies = new HashSet<String>();
        dependencies.add("b");
        add.put("name1", dependencies);
        definedMailingLists.addDependencies(add);
        dependencies.add("c");
        add.put("name2", dependencies);
        add.put("name3", Collections.emptySet());
        definedMailingLists.addDependencies(add);
        Map<String, Set<String>> output = definedMailingLists.getDependencies();
        assertEquals(output.size(), 3);
        assertTrue(output.get("name1").contains("b"));
        assertTrue(output.get("name1").contains("c"));
        assertEquals(output.get("name1").size(), 2);
        assertTrue(output.get("name2").contains("b"));
        assertTrue(output.get("name2").contains("c"));
        assertEquals(output.get("name2").size(), 2);
        assertEquals(output.get("name3").size(), 0);
    }
    
    //covers case where there are 0 mappings
    @Test
    public void testToStringZeroMappings(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        String actual = definedMailingLists.toString();
        assertCorrectStringRep(Arrays.asList(""), actual);
    }

    //covers case where there is 1 mapping
    @Test
    public void testToStringOneMapping(){
        Map<String, String> input = new HashMap<>();
        input.put("name1", "a@a, b@b");
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(input);
        String actual = definedMailingLists.toString();
        assertCorrectStringRep(Arrays.asList("name1= a@a,b@b"), actual);
    }
    
    //covers case where there is > 1 mapping
    @Test
    public void testToStringMoreThanOneMappings(){
        Map<String, String> input = new HashMap<>();
        input.put("name1", "a@a, b@b");
        input.put("name2", "name1");
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(input);
        String actual = definedMailingLists.toString();
        assertCorrectStringRep(Arrays.asList("name1= a@a,b@b","name2= name1"), actual);
    }

}
