
package norn;

import java.io.File;
import java.io.IOException;
import java.util.List;

import lib6005.parser.ParseTree;
import lib6005.parser.Parser;
import lib6005.parser.UnableToParseException;

/**
 * Parser used to create ListExpressions that represents the input string.
 */

public class ListExpressionParser {


    private static final File GRAMMAR_FILE = new File("src/norn/Grammar.g");

    // the nonterminals of the grammar
    private enum Grammar {
        EMAIL, DOMAIN, NAME, ROOT, UNION, WHITESPACE, MAILINGLIST, DIFFERENCE, INTERSECTION, SEQUENCE, PAREN, USERNAME
    };

    private static Parser<Grammar> parser = makeParser(GRAMMAR_FILE);

    /**
     * Compiles the grammar into a parser.
     * 
     * @param grammar file containing the grammar
     * @return parser for the grammar
     * @throws RuntimeException if grammar file can't be read or has syntax errors
     */
    private static Parser<Grammar> makeParser(final File grammar) {
        try {

            return Parser.compile(grammar, Grammar.ROOT);

            // translate these checked exceptions into unchecked
            // RuntimeExceptions,
            // because these failures indicate internal bugs rather than client
            // errors
        } catch (IOException e) {
            throw new RuntimeException("can't read the grammar file", e);
        } catch (UnableToParseException e) {
            throw new IllegalArgumentException("the grammar has a syntax error", e);
        }
    }


    /**
     * Parses a string into a ListExpression representing the string.
     * Legal Strings: an empty string,
     *          an email (consisting of an username, '@', and a domain, usernames and domains can be any length
     *                  they can contain letters (upper and lower), numbers, dashes, periods, and underscores
     *                  note: JANEDOE@MIT.EDU is the same as janedoe@mit.edu)
     *          a mailing list that consists of none, or some emails (in the string, they should be represented by 
     *                  name of mailing list '=' email1, email2, ... etc, or just a list of emails)
     *          a string that combines mailing lists/emails with "*", "!" (ie. email ! mailing list or mailinglist * mailinglist)
     *          "*" represents an intersection, or overlapping emails, of the two expressions on either side
     *          "!" represents a difference, or what emails are in the first expression and not the second
     * @param input string to parse; can contain alphabetical letters, numbers, dashes, periods, semicolons, equal signs, 
     * parentheses, and underscores. Whitespace characters around operators, email addresses, and list names are 
     * irrelevant and ignored, so a,b,c means the same as a , b , c. 
     * Whitespace characters are spaces, tabs (\t), carriage returns (\r), and linefeeds (\n).
     * @param definedMailingLists all mailing lists currently defined by the client
     * @return ListExpression the list expression representing the input string
     * @throws AssertionError if the input is not able to be parsed into a valid list expression, or if the client attempts to
     * call a list definition not yet defined in definedMailingLists
     */
    public static ListExpression parse(final String input, final DefinedMailingLists definedMailingLists) throws IllegalArgumentException {
        // parse into a parse tree
        final ParseTree<Grammar> parseTree;
        try {
            parseTree = parser.parse(input);
        } catch (UnableToParseException e) {
            throw new IllegalArgumentException("the grammar has syntax error", e);
        }

        //Keeping these comments in case we ever need the syntax for showing the tree.
        //System.out.println("parse tree " + parseTree);
        // display the parse tree in a web browser, for debugging only
        //Visualizer.showInBrowser(parseTree);

        //make AST from the parse tree
        final ListExpression expression = makeAbstractSyntaxTree(parseTree,definedMailingLists);

        //System.out.println("AST " + expression);
        return expression;
    }

    /**
     * Convert a parse tree into an abstract syntax tree.
     * 
     * @param parseTree constructed according to the grammar in Grammar.g
     * @return abstract syntax tree corresponding to parseTree
     * @throws IllegalArgumentException if the input does not match the ListExpression grammar
     */
    private static ListExpression makeAbstractSyntaxTree(final ParseTree<Grammar> parseTree, final DefinedMailingLists definedMailingLists) {
        switch(parseTree.name()){
        case ROOT:
        {
            final ParseTree<Grammar> child = parseTree.children().get(0);
            return makeAbstractSyntaxTree(child, definedMailingLists);
        }

        case UNION:
        {
            final List<ParseTree<Grammar>> children = parseTree.children();
            ListExpression expression = makeAbstractSyntaxTree(children.get(0),definedMailingLists);
            for (int i = 1; i < children.size(); ++i) {
                expression = ListExpression.makeUnion(expression, makeAbstractSyntaxTree(children.get(i),definedMailingLists));
            }
            return expression;
        }

        case INTERSECTION:
        {
            final List<ParseTree<Grammar>> children = parseTree.children();
            ListExpression expression = makeAbstractSyntaxTree(children.get(0),definedMailingLists);
            for (int i = 1; i < children.size(); ++i) {
                expression = ListExpression.makeIntersection(expression, makeAbstractSyntaxTree(children.get(i),definedMailingLists));
            }
            return expression;
        }

        case DIFFERENCE:
        {
            final List<ParseTree<Grammar>> children = parseTree.children();
            ListExpression expression = makeAbstractSyntaxTree(children.get(0),definedMailingLists);
            for (int i = 1; i < children.size(); ++i) {
                expression = ListExpression.makeDifference(expression, makeAbstractSyntaxTree(children.get(i),definedMailingLists));
            }
            return expression;
        }

        case SEQUENCE:
        {
            final List<ParseTree<Grammar>> children = parseTree.children();
            ListExpression expression = makeAbstractSyntaxTree(children.get(0),definedMailingLists);
            for (int i = 1; i < children.size(); ++i) {
                expression = ListExpression.makeSequence(expression, makeAbstractSyntaxTree(children.get(i),definedMailingLists));
            }
            return expression;
        }

        case MAILINGLIST: //defining a mailing list
        {
            final List<ParseTree<Grammar>> children = parseTree.children();
            final String  name = children.get(0).text(); //name of mailing list
            ListExpression expression = makeAbstractSyntaxTree(children.get(1),definedMailingLists);
            ListExpression newMailingList = ListExpression.makeMailingList(name, expression);
            definedMailingLists.addMailingList(name, newMailingList); //now a defined list
            return newMailingList;
        }
        case EMAIL:
        {
            final String email = parseTree.text();
            if (email.equals("")){
                return ListExpression.makeEmpty();
            }
            return ListExpression.makeEmail(email);
        }
        case NAME:
        {
            final String name = parseTree.text();
            if (definedMailingLists.getMailingLists().containsKey(name)){
                return definedMailingLists.getMailingLists().get(name);
            }
            throw new AssertionError("mailing list does not exist:"+name);

        }
        case PAREN:
        { 
            final ParseTree<Grammar> child = parseTree.children().get(0);
            switch(child.name()){
            case UNION: {
                final List<ParseTree<Grammar>> children = parseTree.children();
                ListExpression expression = makeAbstractSyntaxTree(children.get(0),definedMailingLists);
                for (int i = 1; i < children.size(); ++i) {
                    expression = ListExpression.makeUnion(expression, makeAbstractSyntaxTree(children.get(i),definedMailingLists));
                }
                return expression;
            }
            case NAME: {
                final String name = parseTree.text();
                if (definedMailingLists.getMailingLists().containsKey(name)){
                    return definedMailingLists.getMailingLists().get(name);
                }
                throw new AssertionError("mailing list does not exist");
            }
            case EMAIL: {
                final String email = parseTree.text();
                if (email.equals("")){
                    return ListExpression.makeEmpty();
                }
                return ListExpression.makeEmail(email);
            }
            default:
                throw new AssertionError("should never get here!"); 
            }
        }
        default:
            throw new AssertionError("should never get here!"); 
        }
    }



}
