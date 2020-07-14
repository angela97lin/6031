/* Copyright (c) 2015-2017 MIT 6.005/6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package norn;

import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

/**
 * Tests for the static methods of Dependencies.
 */
public class DependenciesTest { 
    
    // Testing strategy for Dependencies.java

    // Testing Strategy for parse(String parseTree) --> Map<String, Set<String>> map
    // Partitions:
    //      parseTree contains union, intersection, difference, sequences
    //      parseTree contains 0, 1, >1 definitions
    //      list contains 0, 1, >1 dependencies
    
    // Testing Strategy for checkDependencies(String name, String listExpression) 
    //                      --> Set<String> dependencies
    // Partitions:
    //      listExpression contains 0, 1, >1 different dependencies
    //      listExpression contains name as a dependency
    //      listExpression contains dependencies where name appears as a substring
    
    // Testing Strategy for combineDependencies(Map<String, Set<String>> map)
    // Partitions:
    //      map.size() = 0, 1, >1
    //      map contains values with 0, 1, >1 dependencies
    //      map contains 0, 1, >1 instances where one list expression depends on another
    //      map contains mail loops
    
    // Testing Strategy for replaceListNameExpression(String toBeReplaced, 
    //          String listName, String listNameExpression) --> String replacedExpression
    // Partitions:
    //      toBeReplaced.length()           =0, =1, >1
    //      listName.length()               =1, >1
    //      listNameExpression.length()     =0, =1, >1
    //      listName is part of an email address
    //      listName is part of another different listName
    //      position of listName in toBeReplaced:
    //          listName is not in toBeReplaced
    //          listName is at beginning of toBeReplaced
    //          listName is in middle of toBeReplaced
    //          listName is at end of toBeReplaced
    //          listName == toBeReplaced
    //          listName occurs more than once in toBeReplaced
   
    //---------------------------------------------------------------------------------
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    //---------------------------------------------------------------------------------

    // Tests for parse
    
    // covers   parseTree contains union
    //          parseTree contains 0 definitions
    @Test
    public void testParseNoDefinitions() {
        Map<String, Set<String>> map = Dependencies.parse("ice@cream, chocolate@brownies");
        assertEquals("expected empty map", new HashMap<>(), map);
    }

    // covers   parseTree contains union, difference
    //          parseTree contains 1 definition
    //          list contains 0 dependencies
    @Test
    public void testParseOneDefinition() {
        String listName = "lactoseIntolerant";
        Map<String, Set<String>> map = Dependencies.parse(listName + "= "
                + "(ice@cream, chocolate@brownies)!ice@cream");
        assertEquals("expected size 1", 1, map.size());
        assertTrue("expected lactoseintolerant in map", map.containsKey(listName.toLowerCase()));
        assertEquals("expected 0 dependencies", 0, map.get(listName.toLowerCase()).size());
    }

    // covers   parseTree contains union, intersection, sequences
    //          parseTree contains >1 definition
    //          list contains 1, >1 dependencies
    @Test
    public void testParseManyDefinitions() {
        Map<String, Set<String>> map = Dependencies.parse("food = "
                + "veggies,yummy;"
                + "veggies=spinach@green, vegetables;"
                + "yummy=ice@cream,spinach@green;"
                + "healthy=yummy*veggies");
        // dependencies: {yummy=[], healthy=[yummy, veggies], veggies=[vegetables], food=[yummy, veggies]}
        assertEquals("expected size 4", 4, map.size());
        assertTrue("expected food in map", map.containsKey("food"));
        assertEquals("expected food to have 2 dependencies", 2, map.get("food").size());
        assertTrue("expected food dependencies to contain veggies", map.get("food")
                .contains("veggies"));
        assertTrue("expected food dependencies to contain yummy", map.get("food")
                .contains("yummy"));
        assertTrue("expected veggies in map", map.containsKey("veggies"));
        assertEquals("expected veggies to have 1 dependency", 1, map.get("veggies").size());
        assertTrue("expected veggies dependencies to contain vegetables", map.get("veggies")
                .contains("vegetables"));
        assertTrue("expected yummy in map", map.containsKey("yummy"));
        assertEquals("expected yummy to have 0 dependencies", 0, map.get("yummy").size());
        assertTrue("expected healthy in map", map.containsKey("healthy"));
        assertEquals("expected healthy to have 2 dependencies", 2, map.get("healthy").size());
        assertTrue("expected healthy dependencies to contain yummy", map.get("healthy")
                .contains("yummy"));
        assertTrue("expected healthy dependencies to contain veggies", map.get("healthy")
                .contains("veggies"));
    }
    
    //---------------------------------------------------------------------------------

    // Tests for checkDependencies

    // covers   listExpression contains 0 dependencies
    @Test
    public void testCheckDependenciesNone() {
        Set<String> dependencies = Dependencies.checkDependencies("hi", "hello@hello; bye@bye");
        assertEquals("expected no dependencies", 0, dependencies.size());
    }
    
    // covers   listExpression contains 1 dependency
    @Test
    public void testCheckDependenciesOne() {
        Set<String> dependencies = Dependencies.checkDependencies("hi", "hola");
        assertEquals("expected 1 dependency", 1, dependencies.size());
        assertTrue("expected hola as a dependency", dependencies.contains("hola"));
    }
    
    // covers   listExpression contains >1 different dependencies
    //          listExpression contains name as a dependency
    //          listExpression contains dependencies where name appears as a substring
    @Test
    public void testCheckDependenciesMany() {
        Set<String> dependencies = Dependencies.checkDependencies("food", 
                "vegies, food, fruits * berries, foods");
        assertEquals("expected 3 dependencies", 4, dependencies.size());
        assertTrue("expected vegies as a dependency", dependencies.contains("vegies"));
        assertTrue("expected berries as a dependency", dependencies.contains("berries"));
        assertTrue("expected fruits as a dependency", dependencies.contains("fruits"));
        assertTrue("expected fruits as a dependency", dependencies.contains("foods"));
    }
    
    //---------------------------------------------------------------------------------
    
    // Tests for combineDependencies

    // covers   map.size() = 0
    @Test
    public void testCombineEmpty() {
        Map<String, Set<String>> empty = new HashMap<String, Set<String>>();
        Map<String, Set<String>> expected = new HashMap<String, Set<String>>();
        Dependencies.combineDependencies(empty);
        assertEquals("expected no change", expected, empty);
    }
    
    // covers   map.size() = 1
    //          map contains values with 1 dependency
    @Test
    public void testCombineOne() {
        Map<String, Set<String>> map = new HashMap<String, Set<String>>();
        Set<String> dependencies1 = new HashSet<>();
        dependencies1.add("dependency");
        map.put("name", dependencies1);
        Dependencies.combineDependencies(map);
        assertEquals("expected no change", 1, map.size());
        assertEquals("expected no change", 1, map.get("name").size());
        assertTrue("expected no change", map.get("name").contains("dependency"));
    }
    
    // covers   map.size() > 1
    //          map contains values with 0, >1 dependencies
    //          map contains 0 instances where one list expression depends on another
    @Test
    public void testCombineNoSharedDependencies() {
        Map<String, Set<String>> map = new HashMap<String, Set<String>>();
        Set<String> dependencies1 = new HashSet<>();
        dependencies1.add("dependency1");
        dependencies1.add("dependency2");
        map.put("hasDependencies", dependencies1);
        map.put("hasNoDependencies", new HashSet<>());
        Dependencies.combineDependencies(map);
        assertEquals("expected no change", 2, map.size());
        assertEquals("expected no change", 2, map.get("hasDependencies").size());
        assertTrue("expected no change", map.get("hasDependencies").contains("dependency1"));
        assertTrue("expected no change", map.get("hasDependencies").contains("dependency2"));
        assertEquals("expected no change", 0, map.get("hasNoDependencies").size());
    }

    // covers   map.size() > 1
    //          map contains values with 1, >1 dependencies
    //          map contains 1 instance where one list expression depends on another
    @Test
    public void testCombineOneDepend() {
        Map<String, Set<String>> map = new HashMap<String, Set<String>>();
        Set<String> dependencies1 = new HashSet<>();
        dependencies1.add("2");
        dependencies1.add("3");
        map.put("1", dependencies1);
        Set<String> dependencies2 = new HashSet<>();
        dependencies2.add("1");
        map.put("4", dependencies2);
        Dependencies.combineDependencies(map);
        assertEquals("expected no change in map size", 2, map.size());
        assertEquals("expected no change in 1 dependencies", 2, map.get("1").size());
        assertTrue("expected no change in 1 dependencies", map.get("1").contains("2"));
        assertTrue("expected no change in 1 dependencies", map.get("1").contains("3"));
        assertEquals("expected 4 dependencies to change", 3, map.get("4").size());
        assertTrue(map.get("4").contains("1"));
        assertTrue(map.get("4").contains("2"));
        assertTrue(map.get("4").contains("3"));
    }

    // covers   map.size() > 1
    //          map contains values with 1, >1 dependencies
    //          map contains >1 instance where one list expression depends on another
    @Test
    public void testCombineMultipleDepend() {
        Map<String, Set<String>> map = new HashMap<String, Set<String>>();
        Set<String> dependencies1 = new HashSet<>();
        dependencies1.add("2");
        dependencies1.add("3");
        map.put("1", dependencies1);
        Set<String> dependencies2 = new HashSet<>();
        dependencies2.add("1");
        map.put("4", dependencies2);
        Set<String> dependencies3 = new HashSet<>();
        dependencies3.add("4");
        map.put("5", dependencies3);
        Dependencies.combineDependencies(map);
        assertEquals("expected 4 dependencies to change", 3, map.get("4").size());
        assertTrue(map.get("4").contains("1"));
        assertTrue(map.get("4").contains("2"));
        assertTrue(map.get("4").contains("3"));
        assertEquals("expected 4 dependencies to change", 4, map.get("5").size());
        assertTrue(map.get("5").contains("1"));
        assertTrue(map.get("5").contains("2"));
        assertTrue(map.get("5").contains("3"));
        assertTrue(map.get("5").contains("4"));
    }
    
    // covers   map contains mail loops
    @Test(expected=AssertionError.class)
    public void testCombineMailLoops() {
        Map<String, Set<String>> map = new HashMap<String, Set<String>>();
        Set<String> dependencies1 = new HashSet<>();
        dependencies1.add("2");
        map.put("1", dependencies1);
        Set<String> dependencies2 = new HashSet<>();
        dependencies2.add("1");
        map.put("2", dependencies2);
        Dependencies.combineDependencies(map);
    }
    
    //---------------------------------------------------------------------------------

    // Tests for replaceListNameExpression
    
    // covers   toBeReplaced.length()       =0
    //          listName.length()           >1
    //          listNameExpression.length() >1
    //          listName is not in toBeReplaced
    @Test
    public void testEmptyToBeReplaced() {
        String replaced = Dependencies.replaceListNameExpression("", "listname", "expression");
        assertEquals("expected empty", "", replaced.trim());
    }
    // covers   toBeReplaced.length()       =1
    //          listName.length()           =1
    //          listNameExpression.length() >1
    //          listName == toBeReplaced
    @Test
    public void testLengthOneToBeReplaced() {
        String replaced = Dependencies.replaceListNameExpression("s", "s", "expression");
        assertEquals("expected (expression)", "(expression)", replaced.trim());
    }
    // covers   toBeReplaced.length()       >1
    //          listName.length()           =1
    //          listNameExpression.length() >1
    //          listName is at beginning of toBeReplaced
    @Test
    public void testLengthOneToBeReplaced2() {
        String replaced = Dependencies.replaceListNameExpression("s ", "s", "expression");
        assertEquals("expected (expression)", "(expression)", replaced.trim());
    }

    // covers   toBeReplaced.length()       >1
    //          listName.length()           >1
    //          listNameExpression.length() =0
    //          listName is at end of toBeReplaced
    @Test
    public void testLengthZeroExpression() {
        String replaced = Dependencies.replaceListNameExpression("  sa", "sa", "");
        assertEquals("expected ()", "()", replaced.trim());
    }
    // covers   toBeReplaced.length()       >1
    //          listName.length()           >1
    //          listNameExpression.length() >1
    //          listName is in middle of toBeReplaced
    @Test
    public void testMiddle() {
        String replaced = Dependencies.replaceListNameExpression("replace me plz", "me", "replace");
        assertEquals("expected replace (replace) plz", "replace (replace) plz", replaced.trim());
    }
    // covers   toBeReplaced.length()       >1
    //          listName.length()           >1
    //          listNameExpression.length() >1
    //          listName is at beginning of toBeReplaced
    @Test
    public void testBeginning() {
        String replaced = Dependencies.replaceListNameExpression("me plz", "me", "hi");
        assertEquals("expected (hi) plz", "(hi) plz", replaced.trim());
    }
    // covers   toBeReplaced.length()       >1
    //          listName.length()           >1
    //          listNameExpression.length() >1
    //          listName occurs more than once in toBeReplaced
    @Test
    public void testMultiple() {
        String replaced = Dependencies.replaceListNameExpression("me plz me ", "me", "hi");
        assertEquals("expected (hi) plz (hi)", "(hi) plz (hi)", replaced.trim());
    }
    // covers   toBeReplaced.length()       >1
    //          listName.length()           >1
    //          listNameExpression.length() >1
    //          listName is part of an email address
    @Test
    public void testEmail() {
        String replaced = Dependencies.replaceListNameExpression("j@m", "m", "c@m");
        assertEquals("expected j@m", "j@m", replaced.trim());
    }
    // covers   toBeReplaced.length()       >1
    //          listName.length()           >1
    //          listNameExpression.length() >1
    //          listName is part of another different listName
    @Test
    public void testListName() {
        String replaced = Dependencies.replaceListNameExpression("maddy, cali@h", "m", "c@m");
        assertEquals("expected unchanged", "maddy, cali@h", replaced.trim());
    }
    
}