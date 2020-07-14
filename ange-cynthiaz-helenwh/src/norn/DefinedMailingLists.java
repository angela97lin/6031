package norn;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * DefinedMailingLists is a mutable, threadsafe type representing the collection of
 * all of the mailing lists that have been defined by the user.
 */
public class DefinedMailingLists {
        
    /*
     * Abstraction Function:   
     *      AF(definedMailingList, dependencies) = a collection of the mailing lists where
     *      each key in definedMailingList is the lower-case name of a mailing list represented
     *      by definedMailingList.get(key), and all the dependencies of mailing list keys in definedMailingLists
     *      are mapped to dependencies.get(key)
     *      
     * Rep invariant:
     *      - all keys in definedMailingLists are valid mailing list names
     *      - all values in the definedMailingLists are parsable list expressions
     *      - all keys in dependencies can not be in their respective values (a mailing loop)
     *      - all keys in dependences are valid mailing list names
     *      - all values in dependencies are sets of valid mailing list names
     *      
     * Safety from Rep Exposure:
     *      - all fields are private and final
     *      - constructor, getDependencies(), and getMailingLists() use defensive copying
     *        of immutable objects to make map elements safe from rep exposure.     
     *
     * Thread Safety Argument:
     *      - each instance of definedMailingList is threadsafe by the monitor pattern
     */      
  
    private static final String NAME = "[a-z0-9_\\.\\-]+";
    private final Map<String, String> definedMailingLists;
    private final Map<String, Set<String>> dependencies;
    
    /**
     * Creates a new DefinedMailingLists with no previously defined mailing lists.
     */
    public DefinedMailingLists(){
        this.definedMailingLists = new HashMap<>();
        this.dependencies = new HashMap<>();
        checkRep();
    }
    
    /**
     * Creates a new DefinedMailingLists with previously defined mailing lists.
     * @param definedMailingList the map in which the keys are previously defined
     *  mailing list names, and each corresponding value is the parsable list expression
     *  that corresponds to that mailing list.
     */
    public DefinedMailingLists(Map<String, String> definedMailingList){
        this.definedMailingLists = new HashMap<>();
        this.dependencies = new HashMap<>();
        for (String name : definedMailingList.keySet()){
            String caseInsensitiveName = name.toLowerCase();
            String listRecipient = definedMailingList.get(name).toLowerCase();
            this.addMailingList(caseInsensitiveName, listRecipient);
        }
        checkRep();
    }
    
    /**
     * Check that the rep invariant holds.
     */
    private synchronized void checkRep(){ 
        for (String key : definedMailingLists.keySet()) {
            assert key.matches(NAME);
            ListExpressionParser.isValid(definedMailingLists.get(key));
        }
        for (String key : dependencies.keySet()) {
            assert key.matches(NAME);
            for (String value : dependencies.get(key)) {
                assert !value.equals(key);
                assert value.matches(NAME);
            }
        }
    }
    
    /**
     * "Add" a copy of the map to the dependencies
     * @param map to be added, keys and values must have valid list names
     */
    synchronized void addDependencies(Map<String, Set<String>> map) {
        Map<String, Set<String>> newMap = new HashMap<String, Set<String>>();
        for (String name : map.keySet()){
            newMap.put(name, map.get(name));
        }
        dependencies.putAll(map);
        checkRep();
    }
    
    /**
     * @return a copy of the dependencies stored in this ADT
     */
    synchronized Map<String, Set<String>> getDependencies() {
        //uses defensive copying to prevent rep exposure
        Map<String, Set<String>> map = new HashMap<>();
        for (String name : this.dependencies.keySet()){
            Set<String> values = new HashSet<String>();      
            values.addAll(dependencies.get(name));
            map.put(name, values);
        }  
        checkRep();
        return map;
    }


    /**
     * Retrieves a previously defined mailing list
     * @param name of a mailing list
     * @return an empty string (undefined mailing list) if the name doesn't exist, or 
     * a parseable list expression representing that mailing list
     */
    synchronized String get(final String name){ 
        if (definedMailingLists.containsKey(name.toLowerCase())){
            String mailingList = definedMailingLists.get(name.toLowerCase());
            checkRep();
            return mailingList;
        }
        checkRep();
        return "";
    }
    
    /**
     * If the name doesn't exist in our collection, adds a newly defined 
     * mailing list with its dependencies to our collection.
     * If the name does exist, replaces the already-existing defined 
     * mailing list with the given mailing list.
     * @param name name of a new mailing list or an already existing mailing list
     * @param expression a parseable list expression representing this mailing list
     */
    synchronized void addMailingList(final String name, final String expression){
        //replace the any instances of name with its previously defined value
        String body = Dependencies.replaceListNameExpression(expression, name, definedMailingLists.get(name));
        body = body.replaceAll("\\s", "");   
        this.definedMailingLists.put(name.toLowerCase(), body);
        checkRep();
    }
    
    /**
     * Retrieves a string representation of the system.
     * @return a string containing all name/list expression key/value pairs in the format
     * name + "= " + definedMailingLists.get(name) + "; ";
     * such that the return string could be parsed to a create the "same" environment / definedMailingLists.
     */
    @Override
    public synchronized String toString() {
        String result = "";
        for (String name : definedMailingLists.keySet()) {
            result += name + "= " + definedMailingLists.get(name) + ";";
        }
        checkRep();
        return result;
    }
    
}
