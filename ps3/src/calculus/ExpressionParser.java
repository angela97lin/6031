package calculus;

import java.io.File;
import java.io.IOException;
import java.util.List;
import lib6005.parser.ParseTree;
import lib6005.parser.Parser;
import lib6005.parser.UnableToParseException;
//import lib6005.parser.Visualizer; (commented out since unnused, but left in case I want to debug.:))

public class ExpressionParser {

    /**
     * Main method. Parses and evaluates an expression.
     * 
     * @param args command line arguments, not used
     * @throws UnableToParseException if example expression can't be parsed
     */
    public static void main(final String[] args) throws UnableToParseException {
    }
    
    private static final File GRAMMAR_FILE = new File("src/calculus/Expression.g");

    // the nonterminals of the our grammar
    private enum ExpressionGrammar {
        ROOT, SUM, PRIMARY, NUMBER, WHITESPACE,
        PRODUCT, VARIABLE, EXPONENT
    };

    private static Parser<ExpressionGrammar> PARSER = makeParser(GRAMMAR_FILE);
    
    /**
     * Compile the grammar into a parser.
     * 
     * @param grammar file containing the grammar
     * @return parser for the grammar
     * @throws RuntimeException if grammar file can't be read or has syntax errors
     */
    private static Parser<ExpressionGrammar> makeParser(final File grammar) {
        try {
            return Parser.compile(grammar, ExpressionGrammar.ROOT);
            // translate these checked exceptions into unchecked RuntimeExceptions,
            // because these failures indicate internal bugs rather than client errors
        } catch (IOException e) {
            throw new RuntimeException("can't read the grammar file", e);
        } catch (UnableToParseException e) {
            throw new RuntimeException("the grammar has a syntax error", e);
        }
    }

    /**
     * Parse a string into an expression.
     * @param string string to parse
     * @return Expression parsed from the string
     * @throws UnableToParseException if the string doesn't match the Expression grammar
     */
    public static Expression parse(final String string) throws UnableToParseException {
        // parse the example into a parse tree
        final ParseTree<ExpressionGrammar> parseTree = PARSER.parse(string);

        // display the parse tree in a web browser, for debugging only
        //Visualizer.showInBrowser(parseTree);

        // make an AST from the parse tree
        final Expression expression = makeAbstractSyntaxTree(parseTree);        
        return expression;
    }
    
    /**
     * Convert a parse tree into an abstract syntax tree.
     * 
     * @param parseTree constructed according to the grammar in Expression.g
     * @return abstract syntax tree corresponding to parseTree
     */
    private static Expression makeAbstractSyntaxTree(final ParseTree<ExpressionGrammar> parseTree) {
        switch (parseTree.name()) {
    
        case ROOT: // root ::= sum;
            {
                final ParseTree<ExpressionGrammar> child = parseTree.children().get(0);
                return makeAbstractSyntaxTree(child);
            }
        
        case SUM: //sum ::= product ('+' product)*;
            {
                final List<ParseTree<ExpressionGrammar>> children = parseTree.children();
                Expression expression = makeAbstractSyntaxTree(children.get(0));
                for (int i = 1; i < children.size(); ++i) {
                    expression = Expression.add(expression, makeAbstractSyntaxTree(children.get(i)));
                }
                return expression;
            }
        
        case PRIMARY: //primary ::= number | variable | '(' sum ')';
            {
                final ParseTree<ExpressionGrammar> child = parseTree.children().get(0);
                // check which alternative (number or sum) was actually matched
                switch (child.name()) {
                case NUMBER:
                    return makeAbstractSyntaxTree(child);
                case VARIABLE:
                    return makeAbstractSyntaxTree(child);
                case SUM:
                    return makeAbstractSyntaxTree(child); 
                default:
                    throw new AssertionError("should never get here");
                }
            }

        case NUMBER: //number ::= [0-9]+ ('.')? [0-9]* | ('.') [0-9]+;
            {
                final String number = parseTree.text();
                return Expression.make(number);
            }
            
        case PRODUCT: //product ::= exponent ('*' exponent)*
        {
            final List<ParseTree<ExpressionGrammar>> children = parseTree.children();
            Expression expression = makeAbstractSyntaxTree(children.get(0));
            for (int i = 1; i < children.size(); ++i) {
                expression = Expression.times(expression, makeAbstractSyntaxTree(children.get(i)));
            }
            return expression;
        }
        case VARIABLE: //variable ::= [a-zA-Z];
        {
            final String var = parseTree.text();
            char varChar = var.charAt(0);
            return Expression.make(varChar);
        }
        case EXPONENT: //exponent ::= primary ('^' number)*;
        {
            final List<ParseTree<ExpressionGrammar>> children = parseTree.children();
            Expression expression = makeAbstractSyntaxTree(children.get(0));
            for (int i = 1; i < children.size(); ++i) {
                String child = children.get(i).text();
                Integer power = Integer.parseInt(child);
                expression = Expression.power(expression, power);
            }
            return expression;
        }
        default:
            throw new AssertionError("should never get here!");
        }

    }

}
