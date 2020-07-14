package norn;

import java.util.Set;

/**
 * A class of helper functions that aids with comparing sets.
 */
class SameSet {
    
    /*
    * Thread Safety Argument:
    *   - all methods are static observers
    *   - all inputs are immutable sets passed in from ListExpression
    *     so the sets can not change while each method is running
    */

    /**
     * Determines if two sets contain exactly the same elements. 
     * 
     * @param a set to be compared against b 
     * @param b set to be compared against a
     * @return true if a and b contain exactly the same elements, else false
     */
    static boolean same(Set<String> a, Set<String> b) {
        return a.size() == b.size() && a.containsAll(b);
    }
    
    /**
     * Generates a hashCode value for a given set, that satisfies
     * the following criterion: 
     * If the two sets contain exactly the same elements, they will produce
     * the same result; they need not be the same object in memory. 
     * *Note: As per the object contract, this method of generating a 
     * hash function should not be used if the sets in question may change
     * or are part of a mutable object. 
     * 
     * @param set The set for which we are generating a hash value
     * @return a hashCode obeying the requirement above. 
     */
    static int hashCode(Set<String> set) {
        int hash = 0;
        for (String elt : set) {
            hash += elt.hashCode();
        }
        return hash;
    }
    
    /**
     * Makes a string representation of a set composed of all
     * the elements in the set, comma-delimited, with one space 
     * between elements, with no trailing or leading whitespace and no
     * trailing commas. If the set is empty, returns the 
     * empty string. 
     * 
     * @param set The object to represent as a string
     * @return A String representation of set, as described above
     */
    static String makeString(Set<String> set) {
        if (set.size() == 0) {return "";}
        
        String str = ""; 
        String delimiter = ", ";
        for (String elt: set) {
            str += elt + delimiter;
        }
        //remove the last delimiter
        str = str.substring(0,  str.length() - delimiter.length());
        
        return str;
    }
}