package norn;

import java.util.Set;
import lib6005.parser.UnableToParseException;
/**
 * A thread-safe input system which manages defined mailing lists for later retrieval.
 */
public class UserInput {

    /*
     * Abstraction function:   
     *      AF(definedMailingLists) = a input system that saves any defined 
     *                                mailing lists into definedMailingLists for later retrieval.
     * 
     * Rep invariant:
     *      - definedMailingLists != null
     *      
     * Safety from rep exposure:
     *      - all fields are private and final
     *      - definedMailingLists is mutable but is never returned to the client.
     *      
     * Thread Safety:
     *      - definedMailingLists is thread-safe
     *      - the only two methods, readInput and getParsableDefinedMailingLists,
     *        are synchronized to prevent interleaving (monitor pattern)
     */

    private final DefinedMailingLists definedMailingLists = new DefinedMailingLists();
    
    /**
     * Check that the rep invariant holds.
     */
    private synchronized void checkRep(){
        assert (definedMailingLists != null);
    }
    
    /**
     * Retrieves a string representation of the current user environment.
     * @return the string representation of the current user environment, as specified by
     * DefinedMailingLists' toString().
     */
    synchronized String getParsableDefinedMailingLists(){
        return definedMailingLists.toString();
    }

    /**
     * Given an input string, returns the set of case-insensitive email recipients represented by the string.
     * For example, given an input string, "bitdiddle@mit.edu,Bitdiddle@mit.edu,alyssap@mit.edu", 
     * will return the set, [bitdiddle@mit.edu, alyssap@mit.edu]
     * @param input string of alphabetical letters, numbers, dashes, periods, plus signs, semicolons, equal signs, 
     * parentheses, and underscores representing valid list expressions. Whitespace characters (spaces, tabs, carriage
     * returns, and newlines) around operators, email addresses, and list names 
     * are irrelevant and ignored, so a,b,c means the same as a , b , c. 
     * Precedence order from highest to lowest is as follows: () * ! , = ;
     * @return produce a comma-separated set of recipient email addresses represented by the list expressions, 
     * suitable for copy and paste into an mail client
     * @throws UnableToParseException if the input string is unable to be parsed (contains illegal punctuation)
     * @throws AssertionError if the input string contains mail loops
     */
    synchronized String readInput(final String input) throws UnableToParseException, AssertionError {
        ListExpression l;
        try {
            l = ListExpressionParser.parse(input, definedMailingLists);
        } catch (IllegalArgumentException e) {
            throw new UnableToParseException("Invalid list expression: unable to parse.");
        } catch (AssertionError e) {
            throw new AssertionError("Invalid input: mail loops.");
        }
        Set<String> out = l.getEmailRecipients();
        checkRep();
        return out.toString().replace("[", "").replace("]", "");
    }

}
