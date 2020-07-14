
package norn;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lib6005.parser.ParseTree;
import lib6005.parser.Parser;
import lib6005.parser.UnableToParseException;

/**
 * Parser used to create ListExpressions that represents the input string.
 */

public class ListExpressionParser {
    
    /*
     * Thread Safety Argument:
     *      - static methods that either 
     *          -take no inputs
     *          -take in an immutable String and a thread-safe DefinedMailingList
     *      - all operations within the parse function are locked on the definedMailingList parameter
     *            passed in. Thus, multiple clients can call and mutate the same instance of a definedMailingLists once at a time.
     *      - makeAST and makeParser are private methods that are only used within parse(),
     *            which is a thread-safe method, and thus, are also thread-safe.
     */

    private static final File GRAMMAR_FILE = new File("src/norn/Grammar.g");

    // the nonterminals of the grammar
    private enum Grammar {
        WHITESPACE, EMAIL, NAME, PRIMITIVE, INTERSECT, DIFFERENCE, UNION, LISTDEF, SEQUENCE, ROOT, DEFINITION
    };

    private static Parser<Grammar> parser = makeParser();

    /**
     * Compiles the grammar into a parser.
     * 
     * @param grammarFilename <b>Must be in this class's Java package.</b>
     * @return parser for the grammar
     * @throws RuntimeException if grammar file can't be read or has syntax errors
     */
    private static Parser<Grammar> makeParser() {
        try {
            // read the grammar as a file, relative to the project root.
            return Parser.compile(GRAMMAR_FILE, Grammar.ROOT);

        // Parser.compile() throws two checked exceptions.
        // Translate these checked exceptions into unchecked RuntimeExceptions,
        // because these failures indicate internal bugs rather than client errors
        } catch (IOException e) {
            throw new RuntimeException("can't read the grammar file", e);
        } catch (UnableToParseException e) {
            throw new RuntimeException("the grammar has a syntax error", e);
        }
    }
    
    /**
     * Checks whether or not a string can be parsed into a valid tree
     * based on our grammar.
     * @param input string to be parsed
     * @return the parse tree of the string, if it can be parsed
     * @throws IllegalArgumentException if input string cannot be parsed correctly (illegal syntax)
     */
    public static ParseTree<Grammar> isValid(String input) throws IllegalArgumentException {
        final ParseTree<Grammar> parseTree;
        try {
            parseTree = parser.parse(input);
        } catch (UnableToParseException e) {
            throw new IllegalArgumentException("the input has a syntax error", e);
        }
        return parseTree;
    }

    /**
     * Parses a string into a ListExpression representing the string, as specified by the Norn1 and Norn2 specifications
     * (http://web.mit.edu/6.031/www/sp17/projects/norn1/, http://web.mit.edu/6.031/www/sp17/projects/norn2/)
     * @param input string to parse; can contain alphabetical letters, numbers, dashes, periods, semicolons, equal signs, 
     * parentheses, whitespaces, and underscores.
     * Ambiguous operations like a,b=c or a=b=c are parsed in the only way that they can be legally interpreted
     *      --> a,(b=c) and a=(b=c) for the above examples
     * @param definedMailingLists all mailing lists currently defined by the client
     * @return ListExpression the list expression representing the input string
     * @throws IllegalArgumentException if the input is not able to be parsed into a valid list expression
     * @throws AssertionError if a mailing loop is detected
     */
    public static ListExpression parse(final String input, final DefinedMailingLists definedMailingLists)
                                                                throws IllegalArgumentException, AssertionError {
        synchronized(definedMailingLists) {
            // parse into a parse tree
            ParseTree<Grammar> parseTree = isValid(input);
            
            //Make a dependencies map to search for mailing loops
            final Map<String, Set<String>> newDependencies = Dependencies.parse(parseTree.text());
            final Map<String, Set<String>> dependencies = definedMailingLists.getDependencies();
            
            // Replace any old dependencies with new ones
            dependencies.putAll(newDependencies);
            
            // the max number levels of "dependencies" before a mailing loop can be detected
            // is the number of nodes (keys) in the map
            // combineDependencies produces one additional layer of dependencies, repeat n times
            int n = dependencies.size();
            for (int i=0; i<n; i++) {
                Dependencies.combineDependencies(dependencies);
            }
            
            // update the dependencies of the mailing list
            definedMailingLists.addDependencies(dependencies);
            
            //make AST from the parse tree
            final ListExpression expression = makeAST(parseTree,definedMailingLists);
            
            //System.out.println("AST " + expression);
            return expression;
        }
    }
    
    /**
     * Convert a parse tree into an abstract syntax tree.
     * 
     * @param parseTree constructed according to the grammar in Grammar.g
     * @return abstract syntax tree corresponding to parseTree
     * @throws AssertionError if a mailing loop is detected
     */
    private static ListExpression makeAST(final ParseTree<Grammar> parseTree, final DefinedMailingLists definedMailingLists) throws AssertionError {
        switch(parseTree.name()){
        case ROOT: //root ::= sequence;
        {
            final ParseTree<Grammar> child = parseTree.children().get(0);
            return makeAST(child, definedMailingLists);
        }
        
        case SEQUENCE: //sequence ::= listDef (';' listDef)*;
        {
            final List<ParseTree<Grammar>> children = parseTree.children();
            ListExpression expression = makeAST(children.get(0),definedMailingLists);
            for (int i = 1; i < children.size(); ++i) {
                expression = ListExpression.makeSequence(expression, makeAST(children.get(i),definedMailingLists));
            }
            return expression;
        }
        
        case LISTDEF: //listDef ::= union | definition;
        { 
            final List<ParseTree<Grammar>> children = parseTree.children();
            if (children.size()==1) {
                // no name provided
                return makeAST(children.get(0), definedMailingLists);
            }
            else {
                return makeAST(children.get(0), definedMailingLists);
            }
        }
        case UNION: //union ::= difference (',' difference)*;
        {
            final List<ParseTree<Grammar>> children = parseTree.children();
            ListExpression expression = makeAST(children.get(0),definedMailingLists);
            for (int i = 1; i < children.size(); ++i) {
                expression = ListExpression.makeUnion(expression, makeAST(children.get(i),definedMailingLists));
            }
            return expression;
        }
        
        case DIFFERENCE: //difference ::= intersect ('!' intersect)*;
        {
            final List<ParseTree<Grammar>> children = parseTree.children();
            ListExpression expression = makeAST(children.get(0),definedMailingLists);
            for (int i = 1; i < children.size(); ++i) {
                expression = ListExpression.makeDifference(expression, makeAST(children.get(i),definedMailingLists));
            }
            return expression;
        }

        case INTERSECT: //intersect ::= primitive ('*' primitive)*; 
        {
            final List<ParseTree<Grammar>> children = parseTree.children();
            ListExpression expression = makeAST(children.get(0),definedMailingLists);
            for (int i = 1; i < children.size(); ++i) {
                expression = ListExpression.makeIntersection(expression, makeAST(children.get(i),definedMailingLists));
            }
            return expression;
        }
        
        case PRIMITIVE: //primitive ::= email? | name | '(' sequence ')' | definition;
        { 
            if (parseTree.children().equals(Collections.emptyList())){
                return ListExpression.makeEmpty();
            }
            final ParseTree<Grammar> child = parseTree.children().get(0);
            switch(child.name()){
            case SEQUENCE: {
                return makeAST(child, definedMailingLists);
            }
            case NAME: {
                final String name = child.text().toLowerCase();
                return parse(definedMailingLists.get(name), definedMailingLists);
            }
            case EMAIL: {
                final String email = child.text().toLowerCase();
                return ListExpression.makeEmail(email);
            }
            case DEFINITION: {
                return makeAST(child, definedMailingLists);
            }
            
            default:
                throw new AssertionError("should never get here!"); 
            }
        }
        case DEFINITION: // name '=' union 
        {
            final List<ParseTree<Grammar>> children = parseTree.children();
            final String name = children.get(0).text().toLowerCase(); //name of mailing list
            
            ListExpression expression = makeAST(children.get(1),definedMailingLists);
            definedMailingLists.addMailingList(name,children.get(1).text().toLowerCase()); //now a defined list
            return expression;
        }
        default:
            throw new AssertionError("should never get here!"); 
        }
    }




}