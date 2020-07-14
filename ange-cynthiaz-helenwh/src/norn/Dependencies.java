package norn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * A class of static methods used to
 *  -finding direct dependencies
 *  -find higher-layered dependencies
 */
public class Dependencies {
    
    // Thread Safety:
    //     - OPERATORS and WHITE_SPACE are private constants and thus, safe by immutability. 
    //     -parse()/checkDependencies() takes in immutable String(s) and creates a new map/set
    //          (safe by immutability)
    //     -replaceListNameExpression takes in Strings, outputs a string
    //          (safe by immutability) 
    //     -combineDependencies mutates a map, but is called only in ListExpressionParser.parse()
    //          where it is called on a new map produced in every call to parse;
    //          and where parse is locked on the DefinedMailingList instance (only one thread
    //          can use the DefinedMailingList while parse is operating)
    
    private static final String OPERATORS= "[\\!\\,\\*\\(\\)]+";
    private static final String WHITE_SPACE = "[\\s\\t\\r\\n]+";
    
    /**
     * "Parse" the dependencies from a string
     * @param parseTree ; a string of the parseTree to be parsed by makeAST 
     * @return a map of list names to a set of dependencies (all lowercase)
     */
    static Map<String, Set<String>> parse(String parseTree) {
        Map<String, Set<String>> map = new HashMap<String, Set<String>>();
        parseTree = parseTree.toLowerCase().replaceAll(WHITE_SPACE,"");
        String[] split = parseTree.split("\\;");
        for (String a : split) {
            if (a.contains("=")) {
                int splitAt = a.indexOf("=");
                String name = a.substring(0, splitAt);
                String[] nameSplit = name.split(OPERATORS);
                name = nameSplit[nameSplit.length-1];
                String expression = a.substring(splitAt+1);
                Set<String> dependencies = checkDependencies(name, expression);
                map.put(name, dependencies);
            }
        }
        return map;
    }

    /**
     * Modifies the input map to "combine" the dependencies of a map within one level, i.e. 
     * {a=[b], b=[c]} --> {a=[b, c], b=[c]}
     * @param map a map of valid String names to sets of valid String names
     * @throws AssertionError when a mailing loop is detected
     */
    static void combineDependencies(Map<String, Set<String>> map) throws AssertionError {
        for (String key: map.keySet()) {
            for (String a: map.get(key)) {
                if (map.get(a)!=null) {
                    if (map.get(a).contains(key)) {
                        throw new AssertionError("mailing loop detected");
                    }
                    Set<String> more = new HashSet<String>(map.get(key));
                    more.addAll(map.get(a));
                    map.put(key, more);
                }
            }
        }
    }
    
    /**
     * Find all the dependencies for a list expression, excluding itself
     *  i.e. a = a, b, c will produce the set <b,c>
     *  Used in the parser to check for mail loops before adding new definitions
     * @param name a string representing a list expression
     * @param listExpression a valid list expression
     * @return a set of all dependencies, excluding name
     */
    static synchronized Set<String> checkDependencies(String name, String listExpression) {
        String[] split = listExpression.replaceAll(WHITE_SPACE, "").split(OPERATORS);
        Set<String> names = new HashSet<String>();
        for (String a : split) {
            if (a.contains("=")) {
                int splitAt = a.lastIndexOf("=");
                a = a.substring(splitAt+1);
            }
            if (!a.contains("@")) {
                names.add(a);
            }
        }
        names.remove(name);
        names.remove("");

        return names;
    }
    
    /**
     * Replaces instances of listName in a String with its corresponding listNameExpression
     * @param toBeReplaced String to be replaced
     * @param listName String to replace, cannot be empty String
     * @param listNameExpression String to replace with
     * @return toBeReplaced after all occurrences of listName is replaced with "(" + listNameExpression + ")"
     *          if the occurrence is not part of another listName or an email. Spaces might be inconsistent
     */
     static String replaceListNameExpression(String toBeReplaced, String listName, String listNameExpression) {
        toBeReplaced = " " + toBeReplaced.trim() + " ";
        final String invalidCharPattern = "[a-zA-Z0-9@_.-]"; // do not replace if the letter before/after the name is one of these characters
        String[] arraySections = toBeReplaced.split(listName);
        List<String> sections = new ArrayList<>();
        for (String section : arraySections) { sections.add(section); }
        StringBuilder replacedExpression = new StringBuilder();
        replacedExpression.append(sections.get(0));
        for (int i = 0; i < sections.size() - 1; i++) {
            String sectionBefore = sections.get(i);
            String sectionAfter = sections.get(i+1);
            if ( (sectionBefore.equals("") ||   
                   ! String.valueOf(sectionBefore.charAt(sectionBefore.length()-1)).matches(invalidCharPattern)) &&
                 (sectionAfter.equals("") || 
                   ! String.valueOf(sectionAfter.charAt(0)).matches(invalidCharPattern))) {
                // replace
                replacedExpression.append("(" + listNameExpression + ")" + sections.get(i+1));
            } else { 
                // do not replace;
                replacedExpression.append(listName + sections.get(i+1));
            }
        }
        return replacedExpression.toString();
    }

}
