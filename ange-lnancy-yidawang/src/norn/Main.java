package norn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Console interface to the list expression system.
 */
public class Main {

    /**
     * Read expression and command inputs from the console and output results.
     * An empty input terminates the program.
     * @param args unused
     * @throws IOException if there is an error reading the input
     */
    public static void main(String[] args) throws IOException {
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.print("> ");
            final String input = in.readLine();
            if (input.isEmpty()) {
                return; // exits the program
            }
            try {
                Set<String> expression = parseEmailRecipients(input, definedMailingLists);
                final String output = expression.toString().replace("[", "").replace("]", "");
                System.out.println(output);
            } catch (RuntimeException re) {
                System.out.println(re.getClass().getName() + ": " + re.getMessage());
            }
        }
    }
    
    /**
     * Given an input string, returns the set of case-insensitive email recipients represented by the string.
     * For example, given an input string, "bitdiddle@mit.edu,Bitdiddle@mit.edu,alyssap@mit.edu", 
     * will return the set, [bitdiddle@mit.edu, alyssap@mit.edu]
     * 
     * The email addresses will be sorted in this order: -@.(0-9)_(a-z)
     * For example, given an input string, 
     *          "...@mit.edu, ___@gmail.com, -@mit.edu, ---@mit.edu, _____@gmail.com, .asd..@mit.edu, 45345-345@gmail.com, asb_123@gmail.com",
     * the function should return 
     *          [---@mit.edu, -@mit.edu, ...@mit.edu, .asd..@mit.edu, 45345-345@gmail.com, ___@gmail.com, _____@gmail.com, asb_123@gmail.com]
     * 
     * @param input string of alphabetical letters, numbers, dashes, periods, semicolons, equal signs, 
     * parentheses, and underscores representing valid list expressions. Whitespace characters around operators, 
     * email addresses, and list names are irrelevant and ignored, 
     * so a,b,c means the same as a , b , c. 
     * Whitespace characters are spaces, tabs (\t), carriage returns (\r), and linefeeds (\n).
     * @return produce a comma-separated set of recipient email addresses represented by the list expressions, 
     * suitable for copy and paste into an mail client
     * @throws IllegalArgumentException if the input string is unable to be parsed (contains illegal punctuation)
     */
    public static Set<String> parseEmailRecipients(final String input, DefinedMailingLists defined){
        ListExpression l = ListExpressionParser.parse(input,defined);
        Set<ListExpression> out = l.getEmailRecipients();
        SortedSet<String> environment = new TreeSet<>();
        for (ListExpression def: out) {
            environment.add(def.toString());
        }
        return environment;
    }
}
