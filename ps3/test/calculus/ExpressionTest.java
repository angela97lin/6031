/* Copyright (c) 2015-2017 MIT 6.005/6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package calculus;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import lib6005.parser.Parser;

/**
 * Tests for the Expression abstract data type.
 */
public class ExpressionTest {
    /*
     * 
     * Testing Strategy for ExpressionTest:
     *  ============================================================== 
     * Expression.parse():
     * 
     * input:
     *    --> is a valid input:
     *         - has numbers:
     *              - numbers that have digits, with a decimal point, followed by more digits (6.031)
     *              - numbers with a trailing decimal point (6.)
     *              - numbers with a leading decimal point (.6)
     *              - numbers without a decimal point (42)        
     *         - has variables:
     *              - upper case letter
     *              - lower case letter
     *         - has addition operation (+):
     *              - addition of same types:
     *                  - number + number
     *                  - variable + variable
     *              - addition of mixed types:
     *                  - variable + number
     *              - arguments:
     *                  - addition of two
     *                  - addition of > 2
     *         - has multiplication operation (*):
     *              - multiplication of same types:
     *                  - number + number
     *                  - variable + variable
     *              - multiplication of mixed types:
     *                  - variable + number
     *              - arguments:
     *                  - multiplication of two
     *                  - multiplication of > 2
     *         - has exponentiation operation (^):
     *              - base is number (power is nonnegative int)
     *              - base is variable (power is nonnegative int)
     *              - base is parenthesized expression
     *              - nested powers (2^2^2)
     *         - parentheses:
     *              - has no parentheses
     *              - has one parentheses
     *                  - parentheses around whole expression should be equal
     *              - has nested parentheses (((x)))
     *         - spacing:
     *              - has > 1 spacing between operations (plus, times, exponent)
     *              - has 1 space between operations (plus, times, exponent)
     *              - does not have spacing between operations
     *              - trailing, leading whitespace
     *              - \r, \t, \n
     *         - PEMDAS:
     *              - parentheses before exponents, multiplication, addition
     *              - exponents before multiplication, addition
     *              - multiplication before addition
     *         - Reading exercises 
     * 
     *    --> is an invalid input:
     *         - is an empty string
     *         - has numbers:
     *              - numbers that have multiple decimal points (.12.34)
     *              - spaces within numbers (2 . 3)
     *              - has letters between (121gdfgdfgd23q2)
     *         - has variables:
     *              - more than one letter (xyz)
     *         - has addition operation (+):
     *              - no/invalid left argument
     *              - no/invalid right argument
     *              - no left and right argument 
     *         - has multiplication operation (*):
     *              - no/invalid left argument
     *              - no/invalid right argument
     *              - no left and right argument 
     *         - has exponential terms:
     *              - no power term
     *              - no base term
     *              - power is not a number, but not an integer
     *              - power is not a number
     *              - power is a negative number
     *         - has parentheses:
     *              - unbalanced parentheses (left only, right only, invalid)
     *              - empty parentheses ((()))
     *         - spacing
     *              - no argument between two numbers
     *              - no argument between number and variable
     *              - no argument between two variables
     *              - no argument between parentheses
     *         - other symbols
     *              - has undefined symbols (!#@$- etc)
     *         - Reading exercises
     * ==============================================================     
     * Expression make(String number)/Number variant:
     * 
     *      canEvaluate(Map<String,BigDecimal> environment):
     *          - always true
     *          
     *      numberify(Map<String,BigDecimal> environment):
     *          - number = 0, number = 1, number > 1
     *      
     *      evaluate():
     *          - number = 0, number = 1, number > 1
     *          - number is a whole number, not a whole number
     *      
     *      differentiate(char variable):
     *          - number = 0, number = 1, number > 1
     *      
     *      toString():
     *          - number = 0, number = 1, number > 0
     *          - number is a whole number (5), number is not a whole number (.5, .213913), number is a very precise value (1.00000000)
     * 
     *      equals(Object thatObject):
     *          - thatObject is not an Number object 
     *          - thatObject is a Number object with an equivalent value.
     *              - values are both 0, one is 0 and one is >0, both are >0
     *              - values are entirely equivalent (1.00 and 1.00)
     *              - values are not represented equivalently, but are the same (1.500000 and 1.5)
     *          - thatObject is a Number object with a different value.
     *          
     *      hashCode():
     *          - two equivalent Number objects have same hashCode
     *              - not represented equivalently (1.50000 and 1.5)
     *              - represented equivalently
     *                  
     *==============================================================
     * Expression make(char c)/Variable variant:
     * 
     *      canEvaluate(Map<String,BigDecimal> environment):
     *          - c in environment
     *          - c not in environment

     *      numberify(Map<String,BigDecimal> environment):
     *          - c in environment
     *          - c not in environment
     *          
     *      evaluate():
     *          - always throws IllegalArgumentException

     *      differentiate(char variable):
     *          - c == variable
     *          - c != variable
     *              - upper case vs lower case
     *          
     *      toString():
     *          - c is lowercase, uppercase
     * 
     *      equals(Object thatObject):
     *          - thatObject is not an Variable object 
     *          - thatObject is a Variable object with an equivalent value.
     *          - thatObject is a Variable object with a different value.
     *              - lowercase vs uppercase
     *              - two different alphabetical characters
     *              
     *      hashCode():
     *          - two equivalent Variable objects have same hashCode
     *==============================================================
     * Expression add(Expression left, Expression right)/Plus variant:
     * 
     *      canEvaluate(Map<String,BigDecimal> environment):
     *          - left
     *              - no variables in left in environment
     *              - some variables in left in environment
     *              - all variables in left in environment
     *          - right
     *              - no variables in right in environment
     *              - some variables in right in environment
     *              - all variables in right in environment     
     *              
     *      numberify(Map<String,BigDecimal> environment):
     *          - left
     *              - no variables in left in environment
     *              - some variables in left in environment
     *              - all variables in left in environment
     *          - right
     *              - no variables in right in environment
     *              - some variables in right in environment
     *              - all variables in right in environment    
     *          
     *      evaluate():
     *          - left
     *              - no variables in left in environment
     *              - some variables in left in environment 
     *          - right
     *              - no variables in right in environment
     *              - some variables in right in environment 
     *          
     *      differentiate(char variable):
     *          - left:
     *              - variable is not a variable in left
     *              - variable is the only variable in left
     *              - variable is one of many (>1) variables in left
     *          - right:
     *              - variable is not a variable in right
     *              - variable is the only variable in right
     *              - variable is one of many (>1) variables in right
     *              
     *      toString():
     *          - left:
     *              - Number
     *              - Variable
     *              - Plus
     *              - Times
     *              - Exponent
     *          - right:
     *              - Number
     *              - Variable
     *              - Plus
     *              - Times
     *              - Exponent
     * 
     *      equals(Object thatObject):
     *          - thatObject is not an Plus object
     *          - thatObject is an equivalent Plus object 
     *              - has recursive Expressions
     *          - thatObject is not an equivalent Plus object
     *              - has same left and right expressions, but are flipped
     *                (thatObject.left = this.right, thatObject.right = this.left)
     *              - simply just different expressions
     *              
     *      hashCode():
     *          - two equivalent Plus objects have same hashCode
     *==============================================================
     * Expression times(Expression left, Expression right)/Times variant:
     *      
     *      canEvaluate(Map<String,BigDecimal> environment):
     *          - left
     *              - no variables in left in environment
     *              - some variables in left in environment
     *              - all variables in left in environment
     *          - right
     *              - no variables in right in environment
     *              - some variables in right in environment
     *              - all variables in right in environment     
     *              
     *      numberify(Map<String,BigDecimal> environment):
     *          - left
     *              - no variables in left in environment
     *              - some variables in left in environment
     *              - all variables in left in environment
     *          - right
     *              - no variables in right in environment
     *              - some variables in right in environment
     *              - all variables in right in environment    
     *          
     *      evaluate():
     *          - left
     *              - no variables in left in environment
     *              - some variables in left in environment 
     *          - right
     *              - no variables in right in environment
     *              - some variables in right in environment 
     *              
     *      differentiate(char variable):
     *          - left:
     *              - variable is not a variable in left
     *              - variable is the only variable in left
     *              - variable is one of many (>1) variables in left
     *          - right:
     *              - variable is not a variable in right
     *              - variable is the only variable in right
     *              - variable is one of many (>1) variables in right
     *              
     *      toString():
     *          - left:
     *              - Number
     *              - Variable
     *              - Plus
     *              - Times
     *              - Exponent
     *          - right:
     *              - Number
     *              - Variable
     *              - Plus
     *              - Times
     *              - Exponent
     * 
     *      equals(Object thatObject):    
     *          - thatObject is not an Times object
     *          - thatObject is an equivalent Times object 
     *              - has recursive Expressions
     *          - thatObject is not an equivalent Times object
     *              - has same left and right expressions, but are flipped
     *                (thatObject.left = this.right, thatObject.right = this.left)
     *              - simply just different expressions
     *      hashCode():
     *          - two equivalent Times objects have same hashCode
     *============================================================== 
     * Expression power(Expression base, Expression power)/Exponent variant:
     * 
     *     canEvaluate(Map<String,BigDecimal> environment):
     *          - base
     *              - no variables in base in environment
     *              - some variables in base in environment
     *              - all variables in base in environment
     *              
     *      numberify(Map<String,BigDecimal> environment):
     *          - base
     *              - no variables in base in environment
     *              - some variables in base in environment
     *              - all variables in base in environment
     *              
     *      evaluate():
     *          - base
     *              - no variables in base in environment
     *              - some variables in base in environment 
     *              
     *      differentiate(char variable):
     *          - base:
     *              - variable is not a variable in base
     *              - variable is the only variable in base
     *              - variable is one of many (>1) variables in base        
     *          - power:
     *              - is 0
     *              - is > 0
     *              
     *      toString():
     *          - base:
     *              - Number
     *              - Variable
     *              - Plus
     *              - Times
     *              - Exponent
     *          - power:
     *              - 0, 1, >1 
     * 
     *      equals(Object thatObject):
     *          - thatObject is not an Exponent object
     *          - thatObject is an equivalent Exponent object 
     *              - has recursive Expressions as base
     *          - thatObject is not an equivalent Exponent object
     *              - has same base, but different powers
     *              - has same powers, but different bases
     *              - simply just different expressions    
     *      hashCode():
     *          - two equivalent Exponent objects have same hashCode
     *          
     * ============================================================== 
     * NOTE: the breakdown here is simply to have the different breakdowns for
     * differentiation all together. All actual testing of differentiation occurs
     * when testing the methods of each variant (via Expression.differentiate)
     * Expression.differentiate(char c):
     *      - Expression to parse:
     *          - number (0, 1, >1)
     *          - variable (uppercase, lower case)
     *              - c == variable
     *              - c != variable
     *          - sum
     *              - left:
     *                  - c in left
     *                  - c not in left
     *              - right:
     *                  - c in right
     *                  - c not in right          
     *          - product
     *              - left:
     *                  - c in left
     *                  - c not in left
     *              - right:
     *                  - c in right
     *                  - c not in right       
     *          - exponent:
     *               - power:
     *                  - 0
     *                  - >0
     *               - base: 
     *                   - c in base
     *                   - c not in base
     *      - c 
     *          - is NOT in the expression
     *          - is in the expression 1 time
     *          - is in the expression > 1 times
     *          
     */


    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testParserLibVersion() {
        assertEquals("2.0.1", Parser.VERSION); // check the version of ParserLib
    }     


    //--------------------------------TESTING FOR Expression.parse() BEGINS----------------------------------//


    //covers case for numbers that have digits, with a decimal point, followed by more digits (6.031)
    //            has no parentheses
    @Test
    public void testParseHasNumbersDigitsDecimalDigits(){
        Expression expression = Expression.parse("1.232324");
        String expressionString = expression.toString();
        Expression stringParsedExpression = Expression.parse(expressionString);
        assertEquals("expression should be equal", expression, stringParsedExpression);

        Expression expressionTwo = Expression.parse("0.101231");
        String expressionStringTwo = expressionTwo.toString();
        Expression stringParsedExpressionTwo = Expression.parse(expressionStringTwo);
        assertEquals("expression should be equal", expressionTwo, stringParsedExpressionTwo);
    }

    //covers case for numbers with a trailing decimal point (6.)
    @Test
    public void testParseHasNumbersTrailingDecimal(){
        Expression expression = Expression.parse("0.");
        String expressionString = expression.toString();
        Expression stringParsedExpression = Expression.parse(expressionString);
        assertEquals("expression should be equal", expression, stringParsedExpression);

        Expression expressionTwo = Expression.parse("1.");
        String expressionStringTwo = expressionTwo.toString();
        Expression stringParsedExpressionTwo = Expression.parse(expressionStringTwo);
        assertEquals("expression should be equal", expressionTwo, stringParsedExpressionTwo);
    }

    //covers case for numbers with a leading decimal point (.6)
    @Test
    public void testParseHasNumbersLeadingDecimal(){
        Expression expression = Expression.parse(".0");
        String expressionString = expression.toString();
        Expression stringParsedExpression = Expression.parse(expressionString);
        assertEquals("expression should be equal", expression, stringParsedExpression);

        Expression expressionTwo = Expression.parse(".1");
        String expressionStringTwo = expressionTwo.toString();
        Expression stringParsedExpressionTwo = Expression.parse(expressionStringTwo);
        assertEquals("expression should be equal", expressionTwo, stringParsedExpressionTwo);
    }

    //covers case for numbers without a decimal point (42)
    @Test
    public void testParseHasNumbersNoDecimal(){
        Expression expression = Expression.parse("0");
        String expressionString = expression.toString();
        Expression stringParsedExpression = Expression.parse(expressionString);
        assertEquals("expression should be equal", expression, stringParsedExpression);

        Expression expressionTwo = Expression.parse("1132");
        String expressionStringTwo = expressionTwo.toString();
        Expression stringParsedExpressionTwo = Expression.parse(expressionStringTwo);
        assertEquals("expression should be equal", expressionTwo, stringParsedExpressionTwo);
    }

    //covers case for variables that are upper case
    @Test
    public void testParseHasVariableUpperCase(){
        Expression expression = Expression.parse("A");
        String expressionString = expression.toString();
        Expression stringParsedExpression = Expression.parse(expressionString);
        assertEquals("expression should be equal", expression, stringParsedExpression);

        Expression expressionTwo = Expression.parse("Z");
        String expressionStringTwo = expressionTwo.toString();
        Expression stringParsedExpressionTwo = Expression.parse(expressionStringTwo);
        assertEquals("expression should be equal", expressionTwo, stringParsedExpressionTwo);
    }

    //covers case for variables that are lower case
    @Test
    public void testParseHasVariableLowerCase(){
        Expression expression = Expression.parse("b");
        String expressionString = expression.toString();
        Expression stringParsedExpression = Expression.parse(expressionString);
        assertEquals("expression should be equal", expression, stringParsedExpression);

        Expression expressionTwo = Expression.parse("y");
        String expressionStringTwo = expressionTwo.toString();
        Expression stringParsedExpressionTwo = Expression.parse(expressionStringTwo);
        assertEquals("expression should be equal", expressionTwo, stringParsedExpressionTwo);
    }

    //covers case for addition of number and number
    //                has 0 spaces between addition operation
    //                has 1 space between addition operation
    //                has > 1 spaces between addition operation
    @Test
    public void testParseAdditionNumberAndNumber(){
        Expression expression = Expression.parse("1 + 2");
        String expressionString = expression.toString();
        Expression stringParsedExpression = Expression.parse(expressionString);
        assertEquals("expression should be equal", expression, stringParsedExpression);

        Expression expressionTwo = Expression.parse("3        +                4 ");
        String expressionStringTwo = expressionTwo.toString();
        Expression stringParsedExpressionTwo = Expression.parse(expressionStringTwo);
        assertEquals("expression should be equal", expressionTwo, stringParsedExpressionTwo);

        Expression expressionThree = Expression.parse("3+4");
        String expressionStringThree = expressionThree.toString();
        Expression stringParsedExpressionThree = Expression.parse(expressionStringThree);
        assertEquals("expression should be equal", expressionThree, stringParsedExpressionThree);
    }

    //covers case for addition of variable and variable
    @Test
    public void testParseAdditionVariableAndVariable(){
        Expression expression = Expression.parse("a + b");
        String expressionString = expression.toString();
        Expression stringParsedExpression = Expression.parse(expressionString);
        assertEquals("expression should be equal", expression, stringParsedExpression);

        Expression expressionTwo = Expression.parse("C + s");
        String expressionStringTwo = expressionTwo.toString();
        Expression stringParsedExpressionTwo = Expression.parse(expressionStringTwo);
        assertEquals("expression should be equal", expressionTwo, stringParsedExpressionTwo);

        Expression expressionThree = Expression.parse("3+4");
        String expressionStringThree = expressionThree.toString();
        Expression stringParsedExpressionThree = Expression.parse(expressionStringThree);
        assertEquals("expression should be equal", expressionThree, stringParsedExpressionThree);
    }

    //covers case for addition of mixed types
    //                addition with two arguments
    @Test
    public void testParseAdditionMixed(){
        Expression expression = Expression.parse("a + 3");
        String expressionString = expression.toString();
        Expression stringParsedExpression = Expression.parse(expressionString);
        assertEquals("expression should be equal", expression, stringParsedExpression);

        Expression expressionTwo = Expression.parse("C + 5");
        String expressionStringTwo = expressionTwo.toString();
        Expression stringParsedExpressionTwo = Expression.parse(expressionStringTwo);
        assertEquals("expression should be equal", expressionTwo, stringParsedExpressionTwo);

        Expression expressionThree = Expression.parse("3+t");
        String expressionStringThree = expressionThree.toString();
        Expression stringParsedExpressionThree = Expression.parse(expressionStringThree);
        assertEquals("expression should be equal", expressionThree, stringParsedExpressionThree);
    }

    //covers case for addition with arguments greater than 2
    //                parentheses around whole expression should be equal
    @Test
    public void testParseAdditionMoreThanTwo(){
        Expression expression = Expression.parse("a + 3 + x");
        String expressionString = expression.toString();
        Expression stringParsedExpression = Expression.parse(expressionString);
        assertEquals("expression should be equal", expression, stringParsedExpression);

        Expression expressionTwo = Expression.parse("C + 5 + 3");
        String expressionStringTwo = expressionTwo.toString();
        Expression stringParsedExpressionTwo = Expression.parse(expressionStringTwo);
        assertEquals("expression should be equal", expressionTwo, stringParsedExpressionTwo);

        Expression expressionThree = Expression.parse(" 3 + t + q ");
        String expressionStringThree = expressionThree.toString();
        Expression stringParsedExpressionThree = Expression.parse(expressionStringThree);
        assertEquals("expression should be equal", expressionThree, stringParsedExpressionThree);

        Expression expressionThreeParentheses = Expression.parse("(3 + t + q)");
        String expressionStringThreeParentheses = expressionThreeParentheses.toString();
        Expression stringParsedExpressionThreeParentheses = Expression.parse(expressionStringThreeParentheses);
        assertEquals("expression should be equal", expressionThree, stringParsedExpressionThreeParentheses);
    }

    //covers case for multiplication of number and number
    //                has 0 spaces between multiplication operation
    //                has 1 space between multiplication operation
    //                has > 1 spaces between multiplication operation
    @Test
    public void testParseMultiplicationNumberAndNumber(){
        Expression expression = Expression.parse("1 * 2");
        String expressionString = expression.toString();
        Expression stringParsedExpression = Expression.parse(expressionString);
        assertEquals("expression should be equal", expression, stringParsedExpression);

        Expression expressionTwo = Expression.parse("3        *                4 ");
        String expressionStringTwo = expressionTwo.toString();
        Expression stringParsedExpressionTwo = Expression.parse(expressionStringTwo);
        assertEquals("expression should be equal", expressionTwo, stringParsedExpressionTwo);

        Expression expressionThree = Expression.parse("3*4");
        String expressionStringThree = expressionThree.toString();
        Expression stringParsedExpressionThree = Expression.parse(expressionStringThree);
        assertEquals("expression should be equal", expressionThree, stringParsedExpressionThree);
    }

    //covers case for multiplication of variable and variable
    @Test
    public void testParseMultiplicationVariableAndVariable(){
        Expression expression = Expression.parse("a * b");
        String expressionString = expression.toString();
        Expression stringParsedExpression = Expression.parse(expressionString);
        assertEquals("expression should be equal", expression, stringParsedExpression);

        Expression expressionTwo = Expression.parse("C * s");
        String expressionStringTwo = expressionTwo.toString();
        Expression stringParsedExpressionTwo = Expression.parse(expressionStringTwo);
        assertEquals("expression should be equal", expressionTwo, stringParsedExpressionTwo);

        Expression expressionThree = Expression.parse("3*4");
        String expressionStringThree = expressionThree.toString();
        Expression stringParsedExpressionThree = Expression.parse(expressionStringThree);
        assertEquals("expression should be equal", expressionThree, stringParsedExpressionThree);
    }

    //covers case for multiplication of mixed types
    //                multiplication with two arguments
    @Test
    public void testParseMultiplicationMixed(){
        Expression expression = Expression.parse("a * 3");
        String expressionString = expression.toString();
        Expression stringParsedExpression = Expression.parse(expressionString);
        assertEquals("expression should be equal", expression, stringParsedExpression);

        Expression expressionTwo = Expression.parse("C * 5");
        String expressionStringTwo = expressionTwo.toString();
        Expression stringParsedExpressionTwo = Expression.parse(expressionStringTwo);
        assertEquals("expression should be equal", expressionTwo, stringParsedExpressionTwo);

        Expression expressionThree = Expression.parse("3*t");
        String expressionStringThree = expressionThree.toString();
        Expression stringParsedExpressionThree = Expression.parse(expressionStringThree);
        assertEquals("expression should be equal", expressionThree, stringParsedExpressionThree);
    }

    //covers case for multiplication with arguments greater than 2
    //                parentheses around whole expression should be equal
    @Test
    public void testParseMultiplicationMoreThanTwo(){
        Expression expression = Expression.parse("a * 3 * x");
        String expressionString = expression.toString();
        Expression stringParsedExpression = Expression.parse(expressionString);
        assertEquals("expression should be equal", expression, stringParsedExpression);

        Expression expressionTwo = Expression.parse("C * 5 * 3");
        String expressionStringTwo = expressionTwo.toString();
        Expression stringParsedExpressionTwo = Expression.parse(expressionStringTwo);
        assertEquals("expression should be equal", expressionTwo, stringParsedExpressionTwo);

        Expression expressionThree = Expression.parse(" 3 * t * q ");
        String expressionStringThree = expressionThree.toString();
        Expression stringParsedExpressionThree = Expression.parse(expressionStringThree);
        assertEquals("expression should be equal", expressionThree, stringParsedExpressionThree);

        Expression expressionThreeParentheses = Expression.parse("(3 * t * q)");
        String expressionStringThreeParentheses = expressionThreeParentheses.toString();
        Expression stringParsedExpressionThreeParentheses = Expression.parse(expressionStringThreeParentheses);
        assertEquals("expression should be equal", expressionThree, stringParsedExpressionThreeParentheses);
    }

    //covers case for exponentiation where base is number (power is nonnegative int)
    //                has 0 spaces between exponentiation operation
    //                has 1 space between exponentiation operation
    //                has > 1 spaces between exponentiation operation
    @Test
    public void testParseExponentiationBaseNumber(){
        Expression expression = Expression.parse("2 ^ 1");
        String expressionString = expression.toString();
        Expression stringParsedExpression = Expression.parse(expressionString);
        assertEquals("expression should be equal", expression, stringParsedExpression);

        Expression expressionTwo = Expression.parse("3        ^                3 ");
        String expressionStringTwo = expressionTwo.toString();
        Expression stringParsedExpressionTwo = Expression.parse(expressionStringTwo);
        assertEquals("expression should be equal", expressionTwo, stringParsedExpressionTwo);

        Expression expressionThree = Expression.parse("3^4");
        String expressionStringThree = expressionThree.toString();
        Expression stringParsedExpressionThree = Expression.parse(expressionStringThree);
        assertEquals("expression should be equal", expressionThree, stringParsedExpressionThree);
    }

    //covers case for exponentiation where base is variable (power is nonnegative int)
    @Test
    public void testParseExponentiationBaseVariable(){
        Expression expression = Expression.parse("a ^ 0");
        String expressionString = expression.toString();
        Expression stringParsedExpression = Expression.parse(expressionString);
        assertEquals("expression should be equal", expression, stringParsedExpression);

        Expression expressionTwo = Expression.parse("B ^ 2");
        String expressionStringTwo = expressionTwo.toString();
        Expression stringParsedExpressionTwo = Expression.parse(expressionStringTwo);
        assertEquals("expression should be equal", expressionTwo, stringParsedExpressionTwo);

        Expression expressionThree = Expression.parse("x^1234");
        String expressionStringThree = expressionThree.toString();
        Expression stringParsedExpressionThree = Expression.parse(expressionStringThree);
        assertEquals("expression should be equal", expressionThree, stringParsedExpressionThree);
    }

    //covers case for exponentiation where there are nested powers (2^2^2)
    @Test
    public void testParseExponentiationNestedPowers(){
        Expression expression = Expression.parse("a ^ 3 ^ 1");
        String expressionString = expression.toString();
        Expression stringParsedExpression = Expression.parse(expressionString);
        assertEquals("expression should be equal", expression, stringParsedExpression);

        Expression expressionTwo = Expression.parse("C ^ 7 ^ 0");
        String expressionStringTwo = expressionTwo.toString();
        Expression stringParsedExpressionTwo = Expression.parse(expressionStringTwo);
        assertEquals("expression should be equal", expressionTwo, stringParsedExpressionTwo);

        Expression expressionThree = Expression.parse("3^7^6");
        String expressionStringThree = expressionThree.toString();
        Expression stringParsedExpressionThree = Expression.parse(expressionStringThree);
        assertEquals("expression should be equal", expressionThree, stringParsedExpressionThree);
    }

    //covers case for exponentiation where there are parenthesized bases
    //                parentheses around whole expression should be equal
    @Test
    public void testParseExponentiationParenthesizedBase(){
        Expression expression = Expression.parse("(a * 3 * x)^ 12");
        String expressionString = expression.toString();
        Expression stringParsedExpression = Expression.parse(expressionString);
        assertEquals("expression should be equal", expression, stringParsedExpression);

        Expression expressionTwo = Expression.parse("(C * 5 * 3)^12^6");
        String expressionStringTwo = expressionTwo.toString();
        Expression stringParsedExpressionTwo = Expression.parse(expressionStringTwo);
        assertEquals("expression should be equal", expressionTwo, stringParsedExpressionTwo);

        Expression expressionThree = Expression.parse("q^2");
        String expressionStringThree = expressionThree.toString();
        Expression stringParsedExpressionThree = Expression.parse(expressionStringThree);
        assertEquals("expression should be equal", expressionThree, stringParsedExpressionThree);

        Expression expressionThreeParentheses = Expression.parse("(q^2)");
        String expressionStringThreeParentheses = expressionThreeParentheses.toString();
        Expression stringParsedExpressionThreeParentheses = Expression.parse(expressionStringThreeParentheses);
        assertEquals("expression should be equal", expressionThree, stringParsedExpressionThreeParentheses);
    }

    //covers case for nested parentheses
    //                trailing and leading whitespaces
    @Test
    public void testParseParentheses(){
        Expression expression = Expression.parse("(((a * 3 * x)))");
        String expressionString = expression.toString();
        Expression stringParsedExpression = Expression.parse(expressionString);
        assertEquals("expression should be equal", expression, stringParsedExpression);

        Expression expressionTwo = Expression.parse("((C * 5 * 3) ^12   ^   6)");
        String expressionStringTwo = expressionTwo.toString();
        Expression stringParsedExpressionTwo = Expression.parse(expressionStringTwo);
        assertEquals("expression should be equal", expressionTwo, stringParsedExpressionTwo);

        Expression expressionThree = Expression.parse("q    ^2");
        String expressionStringThree = expressionThree.toString();
        Expression stringParsedExpressionThree = Expression.parse(expressionStringThree);
        assertEquals("expression should be equal", expressionThree, stringParsedExpressionThree);

        Expression expressionFour = Expression.parse("((q+ 2 )*(  ( (g))  )  )   ");
        String expressionStringFour = expressionFour.toString();
        Expression stringParsedExpressionFour = Expression.parse(expressionStringFour);
        assertEquals("expression should be equal", expressionFour, stringParsedExpressionFour);
    }

    //covers case for nested parentheses
    //                trailing and leading whitespaces
    //                 \t, \r, \n
    @Test
    public void testParseWhitespaces(){
        Expression expression = Expression.parse("(((a * 3\t * x)))");
        String expressionString = expression.toString();
        Expression stringParsedExpression = Expression.parse(expressionString);
        assertEquals("expression should be equal", expression, stringParsedExpression);

        Expression expressionTwo = Expression.parse("((C * 5 * 3\n) ^12   ^   6)");
        String expressionStringTwo = expressionTwo.toString();
        Expression stringParsedExpressionTwo = Expression.parse(expressionStringTwo);
        assertEquals("expression should be equal", expressionTwo, stringParsedExpressionTwo);

        Expression expressionThree = Expression.parse("    q    ^\r2   ");
        String expressionStringThree = expressionThree.toString();
        Expression stringParsedExpressionThree = Expression.parse(expressionStringThree);
        assertEquals("expression should be equal", expressionThree, stringParsedExpressionThree);

        Expression expressionFour = Expression.parse("((q+ 2 )*(  ( (g))  )  )   ");
        String expressionStringFour = expressionFour.toString();
        Expression stringParsedExpressionFour = Expression.parse(expressionStringFour);
        assertEquals("expression should be equal", expressionFour, stringParsedExpressionFour);
    }

    //covers case for parentheses before exponents (PEMDAS)
    @Test
    public void testParsePEMDASParenthesesBeforeExponents(){
        Expression expression = Expression.parse("(a+2)^2");
        String expressionString = expression.toString();
        Expression stringParsedExpression = Expression.parse(expressionString);
        assertEquals("expression should be equal", expression, stringParsedExpression);

        Expression expressionTwo = Expression.parse("(a+8)*2^2");
        String expressionStringTwo = expressionTwo.toString();
        Expression stringParsedExpressionTwo = Expression.parse(expressionStringTwo);
        assertEquals("expression should be equal", expressionTwo, stringParsedExpressionTwo);

        Expression expressionThree = Expression.parse("    (q * 1 + 2)    ^2   ");
        String expressionStringThree = expressionThree.toString();
        Expression stringParsedExpressionThree = Expression.parse(expressionStringThree);
        assertEquals("expression should be equal", expressionThree, stringParsedExpressionThree);

        Expression expressionFour = Expression.parse("((q+ 2 )^1 ) ");
        String expressionStringFour = expressionFour.toString();
        Expression stringParsedExpressionFour = Expression.parse(expressionStringFour);
        assertEquals("expression should be equal", expressionFour, stringParsedExpressionFour);
    }

    //covers case for parentheses before multiplication (PEMDAS)
    @Test
    public void testParsePEMDASParenthesesBeforeMultiplication(){
        Expression expression = Expression.parse("(a+2)*6");
        String expressionString = expression.toString();
        Expression stringParsedExpression = Expression.parse(expressionString);
        assertEquals("expression should be equal", expression, stringParsedExpression);

        Expression expressionTwo = Expression.parse("(a+8)*(b+1)");
        String expressionStringTwo = expressionTwo.toString();
        Expression stringParsedExpressionTwo = Expression.parse(expressionStringTwo);
        assertEquals("expression should be equal", expressionTwo, stringParsedExpressionTwo);

        Expression expressionThree = Expression.parse(" 4 *   (q * 1 + 2)       ");
        String expressionStringThree = expressionThree.toString();
        Expression stringParsedExpressionThree = Expression.parse(expressionStringThree);
        assertEquals("expression should be equal", expressionThree, stringParsedExpressionThree);

        Expression expressionFour = Expression.parse("((q+ 2 ) * 6 ) ");
        String expressionStringFour = expressionFour.toString();
        Expression stringParsedExpressionFour = Expression.parse(expressionStringFour);
        assertEquals("expression should be equal", expressionFour, stringParsedExpressionFour);
    }

    //covers case for parentheses before addition (PEMDAS)
    @Test
    public void testParsePEMDASParenthesesBeforeAddition(){
        Expression expression = Expression.parse("(a+2) + 7^ 12");
        String expressionString = expression.toString();
        Expression stringParsedExpression = Expression.parse(expressionString);
        assertEquals("expression should be equal", expression, stringParsedExpression);

        Expression expressionTwo = Expression.parse("(a*8^7)+b");
        String expressionStringTwo = expressionTwo.toString();
        Expression stringParsedExpressionTwo = Expression.parse(expressionStringTwo);
        assertEquals("expression should be equal", expressionTwo, stringParsedExpressionTwo);

        Expression expressionThree = Expression.parse(" 4 +   (q * 1 + 2)       ");
        String expressionStringThree = expressionThree.toString();
        Expression stringParsedExpressionThree = Expression.parse(expressionStringThree);
        assertEquals("expression should be equal", expressionThree, stringParsedExpressionThree);

        Expression expressionFour = Expression.parse("((q+ 2 ) + 6 ) ");
        String expressionStringFour = expressionFour.toString();
        Expression stringParsedExpressionFour = Expression.parse(expressionStringFour);
        assertEquals("expression should be equal", expressionFour, stringParsedExpressionFour);
    }

    //covers case for exponents before multiplication (PEMDAS)
    @Test
    public void testParsePEMDASExponentsBeforeMultiplcation(){
        Expression expression = Expression.parse("a*7^ 12");
        String expressionString = expression.toString();
        Expression stringParsedExpression = Expression.parse(expressionString);
        assertEquals("expression should be equal", expression, stringParsedExpression);

        Expression expressionTwo = Expression.parse("(12*8^7)");
        String expressionStringTwo = expressionTwo.toString();
        Expression stringParsedExpressionTwo = Expression.parse(expressionStringTwo);
        assertEquals("expression should be equal", expressionTwo, stringParsedExpressionTwo);

        Expression expressionThree = Expression.parse(" (q * 1^ 121)       ");
        String expressionStringThree = expressionThree.toString();
        Expression stringParsedExpressionThree = Expression.parse(expressionStringThree);
        assertEquals("expression should be equal", expressionThree, stringParsedExpressionThree);

        Expression expressionFour = Expression.parse("a * 1212 ^ 12 ");
        String expressionStringFour = expressionFour.toString();
        Expression stringParsedExpressionFour = Expression.parse(expressionStringFour);
        assertEquals("expression should be equal", expressionFour, stringParsedExpressionFour);
    }

    //covers case for exponents before addition (PEMDAS)
    @Test
    public void testParsePEMDASExponentsBeforeAddition(){
        Expression expression = Expression.parse("a+7^ 12");
        String expressionString = expression.toString();
        Expression stringParsedExpression = Expression.parse(expressionString);
        assertEquals("expression should be equal", expression, stringParsedExpression);

        Expression expressionTwo = Expression.parse("(12+8^7)");
        String expressionStringTwo = expressionTwo.toString();
        Expression stringParsedExpressionTwo = Expression.parse(expressionStringTwo);
        assertEquals("expression should be equal", expressionTwo, stringParsedExpressionTwo);

        Expression expressionThree = Expression.parse(" (q + 1^ 121)       ");
        String expressionStringThree = expressionThree.toString();
        Expression stringParsedExpressionThree = Expression.parse(expressionStringThree);
        assertEquals("expression should be equal", expressionThree, stringParsedExpressionThree);

        Expression expressionFour = Expression.parse("a + 1212 ^ 12 ");
        String expressionStringFour = expressionFour.toString();
        Expression stringParsedExpressionFour = Expression.parse(expressionStringFour);
        assertEquals("expression should be equal", expressionFour, stringParsedExpressionFour);
    }

    //covers case for multiplication before addition (PEMDAS)
    @Test
    public void testParsePEMDASMultiplicationBeforeAddition(){
        Expression expression = Expression.parse("a+7* 12");
        String expressionString = expression.toString();
        Expression stringParsedExpression = Expression.parse(expressionString);
        assertEquals("expression should be equal", expression, stringParsedExpression);

        Expression expressionTwo = Expression.parse("(12+8*7)");
        String expressionStringTwo = expressionTwo.toString();
        Expression stringParsedExpressionTwo = Expression.parse(expressionStringTwo);
        assertEquals("expression should be equal", expressionTwo, stringParsedExpressionTwo);

        Expression expressionThree = Expression.parse(" (q + 1* 121)       ");
        String expressionStringThree = expressionThree.toString();
        Expression stringParsedExpressionThree = Expression.parse(expressionStringThree);
        assertEquals("expression should be equal", expressionThree, stringParsedExpressionThree);

        Expression expressionFour = Expression.parse("a + 1212 * 12 ");
        String expressionStringFour = expressionFour.toString();
        Expression stringParsedExpressionFour = Expression.parse(expressionStringFour);
        assertEquals("expression should be equal", expressionFour, stringParsedExpressionFour);
    }

    //covers examples given in pset3
    @Test
    public void testParseReadingExamples(){
        Expression expression = Expression.parse("3 + 2.4 ");
        String expressionString = expression.toString();
        Expression stringParsedExpression = Expression.parse(expressionString);
        assertEquals("expression should be equal", expression, stringParsedExpression);

        Expression expressionTwo = Expression.parse("3 * x + 2.4 ");
        String expressionStringTwo = expressionTwo.toString();
        Expression stringParsedExpressionTwo = Expression.parse(expressionStringTwo);
        assertEquals("expression should be equal", expressionTwo, stringParsedExpressionTwo);

        Expression expressionThree = Expression.parse(" 3 * (x + 2.4)       ");
        String expressionStringThree = expressionThree.toString();
        Expression stringParsedExpressionThree = Expression.parse(expressionStringThree);
        assertEquals("expression should be equal", expressionThree, stringParsedExpressionThree);

        Expression expressionFour = Expression.parse("(3 * x) ^ 2  ");
        String expressionStringFour = expressionFour.toString();
        Expression stringParsedExpressionFour = Expression.parse(expressionStringFour);
        assertEquals("expression should be equal", expressionFour, stringParsedExpressionFour);
    }

    //covers examples given in pset3, continued
    @Test
    public void testParseReadingExamples2(){
        Expression expression = Expression.parse("((3 + 4) * x * x)  ");
        String expressionString = expression.toString();
        Expression stringParsedExpression = Expression.parse(expressionString);
        assertEquals("expression should be equal", expression, stringParsedExpression);

        Expression expressionTwo = Expression.parse("a*x^2 + b*x + c ");
        String expressionStringTwo = expressionTwo.toString();
        Expression stringParsedExpressionTwo = Expression.parse(expressionStringTwo);
        assertEquals("expression should be equal", expressionTwo, stringParsedExpressionTwo);

        Expression expressionThree = Expression.parse(" (2*x )+ ( y*x )       ");
        String expressionStringThree = expressionThree.toString();
        Expression stringParsedExpressionThree = Expression.parse(expressionStringThree);
        assertEquals("expression should be equal", expressionThree, stringParsedExpressionThree);

        Expression expressionFour = Expression.parse("4 + 3 * x + 2 * x * x + 1 * x * x * (((x)))   ");
        String expressionStringFour = expressionFour.toString();
        Expression stringParsedExpressionFour = Expression.parse(expressionStringFour);
        assertEquals("expression should be equal", expressionFour, stringParsedExpressionFour);
    }
   

    //covers case for invalid input input is empty string
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidEmptyString(){
        Expression expression = Expression.parse("");
    }

    //covers case for invalid input where numbers multiple decimal points
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidNumberLeadingDecimalPoint(){
        Expression expression = Expression.parse(".6.");
    }

    //covers case for invalid input where numbers have spaces
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidNumberSpacesBetweenLeftOfDecimal(){
        Expression expression = Expression.parse("0      .6");
    }

    //covers case for invalid input where numbers have spaces
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidNumberSpacesBetweenRightOfDecimal(){
        Expression expression = Expression.parse("1.      1");
    }

    //covers case for invalid input where numbers have spaces
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidNumberSpacesBetweenLeftAndRightOfDecimal(){
        Expression expression = Expression.parse("4     .     1       ");
    }

    //covers case for invalid input where numbers have spaces
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidNumberSpacesBetweenNoDecimal(){
        Expression expression = Expression.parse(" 32423     42     ");
    }

    //covers case for invalid input where variables consist of more than one letter
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidVariableLengthGreaterThanOne(){
        Expression expression = Expression.parse("omilord");
    }

    //covers case for invalid input where addition has no right argument
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidAdditionNoRightArgument(){
        Expression expression = Expression.parse(" 12 + ");
    }

    //covers case for invalid input where addition has no left argument
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidAdditionNoLeftArgument(){
        Expression expression = Expression.parse(" + f");
    }

    //covers case for invalid input where addition has no left argument nor right
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidAdditionNoLeftRightArgument(){
        Expression expression = Expression.parse(" + ");
    }

    //covers case for invalid input where addition has invalid left argument
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidAdditionInvalidLeftArgument(){
        Expression expression = Expression.parse(" ++ 1");
    }

    //covers case for invalid input where addition has invalid left argument
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidAdditionInvalidRightArgument(){
        Expression expression = Expression.parse(" c + #");
    }

    //covers case for invalid input where addition has invalid left and right arguments
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidAdditionInvalidLeftAndRightArgument(){
        Expression expression = Expression.parse(" ? + #");
    }

    //covers case for invalid input where multiplication has no right argument
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidMultiplicationNoRightArgument(){
        Expression expression = Expression.parse(" 12* ");
    }

    //covers case for invalid input where multiplication has no left argument
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidMultiplicationNoLeftArgument(){
        Expression expression = Expression.parse(" * (f)");
    }

    //covers case for invalid input where multiplication has no left argument nor right
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidMultiplicationNoLeftRightArgument(){
        Expression expression = Expression.parse(" * ");
    }

    //covers case for invalid input where multiplication has invalid left argument
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidMultiplicationInvalidLeftArgument(){
        Expression expression = Expression.parse(" ** 1");
    }

    //covers case for invalid input where multiplication has invalid left argument
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidMultiplicationInvalidRightArgument(){
        Expression expression = Expression.parse(" c * #");
    }

    //covers case for invalid input where multiplication has invalid left and right arguments
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidMultiplicationInvalidLeftAndRightArgument(){
        Expression expression = Expression.parse(" ? * #");
    }

    //covers case for invalid input where exponentiation has no base argument
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidExponentiationNoBaseArgument(){
        Expression expression = Expression.parse(" ^12 ");
    }

    //covers case for invalid input where exponentiation has no power argument
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidExponentiationNoPowerArgument(){
        Expression expression = Expression.parse(" 121^ ");
    }

    //covers case for invalid input where exponentiation has no base argument nor power
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidExponentiationNoPowerBaseArgument(){
        Expression expression = Expression.parse(" ^ ");
    }

    //covers case for invalid input where exponentiation has invalid base argument
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidExponentiationInvalidBaseArgument(){
        Expression expression = Expression.parse(" ^^ 1");
    }

    //covers case for invalid input where exponentiation has invalid power argument
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidExponentiationInvalidPowerArgument(){
        Expression expression = Expression.parse(" c ^ #");
    }

    //covers case for invalid input where exponentiation has invalid power and base arguments
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidExponentiationInvalidPowerBaseArgument(){
        Expression expression = Expression.parse(" ? ^ #");
    }

    //covers case for invalid input where exponentiation has power is not a whole number
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidExponentiationInvalidPowerNotWholeNumber(){
        Expression expression = Expression.parse(" c ^ 123.212");
    }

    //covers case for invalid input where exponentiation has power is not a nonnegative number
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidExponentiationInvalidPowerNotNonnegativeNumber(){
        Expression expression = Expression.parse(" c ^-1");
    }

    //covers case for invalid input where exponentiation has power is not a number
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidExponentiationInvalidPowerNotNumber(){
        Expression expression = Expression.parse(" c ^ c");
    }

    //covers case for invalid input where parentheses are unbalanced, has left but not right
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidParenthesesUnbalancedLeft(){
        Expression expression = Expression.parse(" (");
    }

    //covers case for invalid input where parentheses are unbalanced, has right but not left
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidParenthesesUnbalancedRight(){
        Expression expression = Expression.parse(")");
    }

    //covers case for invalid input where parentheses are unbalanced/invalid
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidParenthesesUnbalancedInvalid(){
        Expression expression = Expression.parse("[)");
    }

    //covers case for invalid input where parentheses have no content
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidParenthesesEmptyInvalid(){
        Expression expression = Expression.parse("((((((((((()))))))))))");
    }

    //covers case for invalid input where no arguments between two numbers 
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidSpacingNumbers(){
        Expression expression = Expression.parse("1           6");
    }

    //covers case for invalid input where no arguments between two variables 
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidSpacingVariables(){
        Expression expression = Expression.parse("w           e");
    }

    //covers case for invalid input where no arguments between number and variable
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidSpacingNumberAndVariable(){
        Expression expression = Expression.parse("1           e");
    }

    //covers case for invalid input where no arguments between parentheses
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidSpacingParentheses(){
        Expression expression = Expression.parse("(1)           (e)");
    }

    //covers case for invalid input where there are undefined symbols
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidUndefinedSymbols(){
        Expression expression = Expression.parse("#");
    }

    //covers test case given in pset3 examples
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidReadingExample1(){
        Expression expression = Expression.parse("3 *");
    }

    //covers test case given in pset3 examples
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidReadingExample2(){
        Expression expression = Expression.parse("( 3");
    }

    //covers test case given in pset3 examples
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidReadingExample3(){
        Expression expression = Expression.parse("3 x");
    }

    //covers test case given in pset3 examples
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidReadingExample4(){
        Expression expression = Expression.parse("3^x");
    }
    //--------------------------------TESTING FOR Expression.parse() ENDS----------------------------------//
    //--------------------------------TESTING FOR NUMBER BEGINS----------------------------------//
    //covers case where n == 0
    @Test 
    public void testToStringNumberZero(){
        String testInput = "0";
        String expected = "0";
        Expression testNumber = Expression.make(testInput);
        String testString = testNumber.toString();
        assertTrue("Number string was not as expected", expected.equals(testString));

        String testInputDouble = "0.0000000";
        String expectedDouble = "0";
        Expression testNumberDouble = Expression.make(testInputDouble);
        String testStringDouble = testNumberDouble.toString();
        assertTrue("Number string was not as expected", expectedDouble.equals(testStringDouble));
    }

    //covers case where n == 1
    @Test 
    public void testToStringNumberOne(){
        String testInput = "1";
        String expected = "1";
        Expression testNumber = Expression.make(testInput);
        String testString = testNumber.toString();
        assertTrue("Number string was not as expected", expected.equals(testString));


        String testInputDouble = "1.0";
        String expectedDouble = "1";
        Expression testNumberDouble = Expression.make(testInputDouble);
        String testStringDouble = testNumberDouble.toString();
        assertTrue("Number string was not as expected", expectedDouble.equals(testStringDouble));
    }

    //covers case where n > 0
    //                  n is not a whole number
    @Test 
    public void testToStringNumberGreaterThanZeroDecimal(){
        String testInput = "1.2";
        String expected = "1.2";
        Expression testNumber = Expression.make(testInput);
        String testString = testNumber.toString();
        assertTrue("Number string was not as expected", expected.equals(testString));
    }

    //covers case where n > 0
    //                  n is a whole number
    @Test 
    public void testToStringNumberGreaterThanZeroWholeNumber(){
        String testInput = "6";
        String expected = "6";
        Expression testNumber = Expression.make(testInput);
        String testString = testNumber.toString();
        assertTrue("Number string was not as expected", expected.equals(testString));
    }

    //covers case where n > 0
    //                  n is a really precise/long decimal
    @Test 
    public void testToStringNumberGreaterThanZeroReallyLong(){
        String testInput = "6.1234567890987654321";
        String expected = "6.1234567890987654321";
        Expression testNumber = Expression.make(testInput);
        String testString = testNumber.toString();
        assertTrue("Number string was not as expected", expected.equals(testString));
    }

    
    //covers case where thatObject is not an Number object
    @Test 
    public void testEqualsNotANumberObject(){
        char testVariableInput = 't';
        String testNumbersInput = "6";
        Expression testNumber = Expression.make(testNumbersInput);
        Expression testVariable = Expression.make(testVariableInput);
        assertFalse("Number should not equal Variable object", testNumber.equals(testVariable));
    }

    //covers case where thatObject is a Number object with an equivalent value.
    //                  value are both 0
    @Test 
    public void testEqualsNumbersAreEqualAndZero(){

        String testNumberInputFirst = "0";
        String testNumberInputSecond = "0.0";
        String testNumberInputThird = "0.00000000";
        Expression testNumberFirst = Expression.make(testNumberInputFirst);
        Expression testNumberSecond = Expression.make(testNumberInputSecond);
        Expression testNumberThird = Expression.make(testNumberInputThird);
        assertTrue("Two Number objects should be equal", testNumberFirst.equals(testNumberSecond));
        assertTrue("Two Number objects should be equal", testNumberFirst.equals(testNumberThird));

        assertTrue("Two Number objects should be equal", testNumberSecond.equals(testNumberFirst));
        assertTrue("Two Number objects should be equal", testNumberSecond.equals(testNumberThird));

        assertTrue("Two Number objects should be equal", testNumberThird.equals(testNumberSecond));
        assertTrue("Two Number objects should be equal", testNumberThird.equals(testNumberFirst));
    }

    //covers case where thatObject is a Number object with a different value.
    //                  value is 0 and > 0
    @Test 
    public void testEqualsNumbersZeroAndNotZeroNumberNotEqual(){     
        String testNumberInputFirst = "0.12345";
        String testNumberInputSecond = "0.0";
        Expression testNumberFirst = Expression.make(testNumberInputFirst);
        Expression testNumberSecond = Expression.make(testNumberInputSecond);

        assertFalse("Two Number objects should not be equal", testNumberFirst.equals(testNumberSecond));
        assertFalse("Two Number objects should not be equal", testNumberSecond.equals(testNumberFirst));
    }

    //covers case where thatObject is a Number object with an equivalent value.
    @Test 
    public void testEqualsNumbersAreEquivalentInEveryWay(){     
        String testNumberInputFirst = "12.12345";
        String testNumberInputSecond = "12.12345";
        Expression testNumberFirst = Expression.make(testNumberInputFirst);
        Expression testNumberSecond = Expression.make(testNumberInputSecond);

        assertTrue("Two Number objects should be equal", testNumberFirst.equals(testNumberSecond));
        assertTrue("Two Number objects should be equal", testNumberSecond.equals(testNumberFirst));
    }

    //covers case where thatObject is a Number object with an equivalent value.
    //                  values are not represented equivalently, but are the same (eg 1.500000 and 1.5)
    @Test 
    public void testEqualsNumbersAreNotRepresentedEquivalentlyButHaveSameValue(){     
        String testNumberInputFirst = "12.12345";
        String testNumberInputSecond = "12.12345000000";
        Expression testNumberFirst = Expression.make(testNumberInputFirst);
        Expression testNumberSecond = Expression.make(testNumberInputSecond);

        assertTrue("Two Number objects should be equal", testNumberFirst.equals(testNumberSecond));
        assertTrue("Two Number objects should be equal", testNumberSecond.equals(testNumberFirst));
    }

    //covers case where thatObject is a Number object with an equivalent value.
    //                  represented equivalently 

    @Test 
    public void testHashCodeNumbersAreEquivalentInEveryWay(){     
        String testNumberInputFirst = "12.12345";
        String testNumberInputSecond = "12.12345";
        Expression testNumberFirst = Expression.make(testNumberInputFirst);
        Expression testNumberSecond = Expression.make(testNumberInputSecond);
        int hashCodeFirst = testNumberFirst.hashCode();
        int hashCodeSecond = testNumberSecond.hashCode();
        assertEquals("Equal Number objects should have same hashCodes", hashCodeFirst, hashCodeSecond);
    }

    //covers case where thatObject is a Number object with an equivalent value.
    //                  not represented equivalently
    @Test 
    public void testHashCodeNumbersAreNotRepresentedEquivalently(){     
        String testNumberInputFirst = "12.1234500000";
        String testNumberInputSecond = "12.12345";
        Expression testNumberFirst = Expression.make(testNumberInputFirst);
        Expression testNumberSecond = Expression.make(testNumberInputSecond);
        int hashCodeFirst = testNumberFirst.hashCode();
        int hashCodeSecond = testNumberSecond.hashCode();
        assertEquals("Equal Number objects should have same hashCodes", hashCodeFirst, hashCodeSecond);
    }

    //covers case where value of number = 0
    @Test 
    public void testDifferentiateNumberIsZero(){     
        String testNumberInputFirst = "0";
        String testNumberInputSecond = "0.00000";
        Expression testNumberFirst = Expression.make(testNumberInputFirst);
        Expression testNumberSecond = Expression.make(testNumberInputSecond);
        Expression actualFirst = testNumberFirst.differentiate('A');
        Expression actualSecond = testNumberSecond.differentiate('a');
        Expression expected = Expression.make("0");
        assertTrue("Differentiation of constant should return 0", expected.equals(actualFirst));
        assertTrue("Differentiation of constant should return 0", expected.equals(actualSecond));
    }

    //covers case where value of number = 1
    @Test 
    public void testDifferentiateNumberIsOne(){     
        String testNumberInputFirst = "1";
        String testNumberInputSecond = "1.0000000";
        Expression testNumberFirst = Expression.make(testNumberInputFirst);
        Expression testNumberSecond = Expression.make(testNumberInputSecond);
        Expression actualFirst = testNumberFirst.differentiate('A');
        Expression actualSecond = testNumberSecond.differentiate('a');
        Expression expected = Expression.make("0");
        assertTrue("Differentiation of constant should return 0", expected.equals(actualFirst));
        assertTrue("Differentiation of constant should return 0", expected.equals(actualSecond));
    }

    //covers case where value of number > 1
    @Test 
    public void testDifferentiateNumberIsGreaterThanOne(){     
        String testNumberInputFirst = "12.12345";
        String testNumberInputSecond = "12";
        Expression testNumberFirst = Expression.make(testNumberInputFirst);
        Expression testNumberSecond = Expression.make(testNumberInputSecond);
        Expression actualFirst = testNumberFirst.differentiate('A');
        Expression actualSecond = testNumberSecond.differentiate('a');
        Expression expected = Expression.make("0");
        assertTrue("Differentiation of constant should return 0", expected.equals(actualFirst));
        assertTrue("Differentiation of constant should return 0", expected.equals(actualSecond));
    }

    //covers test case for canEvaluate number --> always true
    @Test
    public void testCanEvaluateNumber(){
        String testNumberInputFirst = "12.12345";
        String testNumberInputSecond = "12";
        Expression testNumberFirst = Expression.make(testNumberInputFirst);
        Expression testNumberSecond = Expression.make(testNumberInputSecond);
        Map<String, BigDecimal> environment = new HashMap<>();
        boolean canEvalateFirst = testNumberFirst.canEvaluate(environment);
        boolean canEvalateSecond = testNumberSecond.canEvaluate(environment);
        assertTrue("should always be able to evaluate number", canEvalateFirst);
        assertTrue("should always be able to evaluate number", canEvalateSecond);
    }

    //covers case where number = 0
    @Test
    public void testNumberifyNumberZero(){
        String testNumberInputFirst = "0";
        Expression testNumberFirst = Expression.make(testNumberInputFirst);
        Map<String, BigDecimal> environment = new HashMap<>();
        Expression numberifiedFirst = testNumberFirst.numberify(environment);
        assertTrue("numberify number should return equivalent number", numberifiedFirst.equals(testNumberFirst));
    }

    //covers case where number = 1
    @Test
    public void testNumberifyNumberOne(){
        String testNumberInputFirst = "1.00000";
        Expression testNumberFirst = Expression.make(testNumberInputFirst);
        Map<String, BigDecimal> environment = new HashMap<>();
        Expression numberifiedFirst = testNumberFirst.numberify(environment);
        assertTrue("numberify number should return equivalent number", numberifiedFirst.equals(testNumberFirst));
    }

    //covers case where number > 1
    @Test
    public void testNumberifyNumberGreaterThanOne(){
        String testNumberInputFirst = "1.45340";
        Expression testNumberFirst = Expression.make(testNumberInputFirst);
        Map<String, BigDecimal> environment = new HashMap<>();
        Expression numberifiedFirst = testNumberFirst.numberify(environment);
        assertTrue("numberify number should return equivalent number", numberifiedFirst.equals(testNumberFirst));
    }

    //covers case where number = 0
    @Test
    public void testEvaluateNumberZero(){
        String testNumberInputFirst = "0";
        Expression testNumberFirst = Expression.make(testNumberInputFirst);
        BigDecimal evaluated = testNumberFirst.evaluate();
        BigDecimal expected = new BigDecimal("0");
        assertTrue("evaluted incorrectly", evaluated.compareTo(expected) == 0);
    }

    //covers case where number = 1
    @Test
    public void testEvaluateNumberOne(){
        String testNumberInputFirst = "1.00000";
        Expression testNumberFirst = Expression.make(testNumberInputFirst);
        BigDecimal evaluated = testNumberFirst.evaluate();
        BigDecimal expected = new BigDecimal(testNumberInputFirst);
        assertTrue("evaluted incorrectly", evaluated.compareTo(expected) == 0);
    }

    //covers case where number > 1
    @Test
    public void testEvaluateNumberGreaterThanOne(){
        String testNumberInputFirst = "1.45340";
        Expression testNumberFirst = Expression.make(testNumberInputFirst);
        BigDecimal evaluated = testNumberFirst.evaluate();
        BigDecimal expected = new BigDecimal(testNumberInputFirst);
        assertTrue("evaluted incorrectly", evaluated.compareTo(expected) == 0);
    }

    //--------------------------------TESTING FOR NUMBER ENDS----------------------------------//
    //--------------------------------TESTING FOR VARIABLE BEGINS----------------------------------//

    //covers case where c is upper case
    @Test 
    public void testToStringVariableUpperCase(){
        char testCharacter = 'A';
        Expression testVariable = Expression.make(testCharacter);
        String expected = "A";
        String testString = testVariable.toString();
        assertTrue("Variable string was not as expected", expected.equals(testString));
    }

    //covers case where c is lower case
    @Test 
    public void testToStringVariableLowerCase(){
        char testCharacter = 'v';
        Expression testVariable = Expression.make(testCharacter);
        String expected = "v";
        String testString = testVariable.toString();
        assertTrue("Variable string was not as expected", expected.equals(testString));
    }

    //covers case where thatObject is not an Variable object 
    @Test 
    public void testEqualsVariableNotAVariableObject(){
        char testCharacter = 'v';
        Expression testVariable = Expression.make(testCharacter);
        String testNumberInput = "12456.0";
        Expression testNumber = Expression.make(testNumberInput);
        assertFalse("Variable object should not equal Number object", testVariable.equals(testNumber));
    }

    //covers case where thatObject is a Variable object with an equivalent value
    @Test 
    public void testEqualsVariableAreEqual(){
        char testCharacterFirst = 'v';
        Expression testVariableFirst = Expression.make(testCharacterFirst);
        char testCharacterSecond = 'v';
        Expression testVariableSecond = Expression.make(testCharacterSecond);
        assertTrue("Variable objects should be equal", testVariableFirst.equals(testVariableSecond));
    }

    //covers case where thatObject is a Variable object with a different value
    //                  two Variables differ by case (Upper case vs lower case)
    @Test 
    public void testEqualsVariableAreNotEqualBecauseOfCaseSensitivity(){
        char testCharacterFirst = 'v';
        Expression testVariableFirst = Expression.make(testCharacterFirst);
        char testCharacterSecond = 'V';
        Expression testVariableSecond = Expression.make(testCharacterSecond);
        assertFalse("Variable objects should not be equal", testVariableFirst.equals(testVariableSecond));
        assertFalse("Variable objects should not be equal", testVariableSecond.equals(testVariableFirst));
    }

    //covers case where thatObject is a Variable object with a different value
    //                  two Variables are different letters
    @Test 
    public void testEqualsVariableAreNotEqualBecauseDifferentLetters(){
        char testCharacterFirst = 'v';
        Expression testVariableFirst = Expression.make(testCharacterFirst);
        char testCharacterSecond = 'A';
        Expression testVariableSecond = Expression.make(testCharacterSecond);
        assertFalse("Variable objects should not be equal", testVariableFirst.equals(testVariableSecond));
        assertFalse("Variable objects should not be equal", testVariableSecond.equals(testVariableFirst));
    }

    //covers case where two equivalent Variable objects have same hashCode
    @Test 
    public void testHashCodeVariableAreEqual(){
        char testCharacterFirst = 'v';
        Expression testVariableFirst = Expression.make(testCharacterFirst);
        char testCharacterSecond = 'v';
        Expression testVariableSecond = Expression.make(testCharacterSecond);

        int hashCodeFirst = testVariableFirst.hashCode();
        int hashCodeSecond = testVariableSecond.hashCode();
        assertEquals("Equal Variable objects should have same hashCode", hashCodeFirst, hashCodeSecond);
    }

    //covers case where value of c == variable
    @Test 
    public void testDifferentiateVariableEquals(){     
        char testCharacterFirst = 'v';
        char testCharacterSecond = 'A';
        Expression testNumberFirst = Expression.make(testCharacterFirst);
        Expression testNumberSecond = Expression.make(testCharacterSecond);
        Expression actualFirst = testNumberFirst.differentiate('v');
        Expression actualSecond = testNumberSecond.differentiate('A');
        Expression expected = Expression.make("1");
        assertTrue("Differentiation of constant should return 1", expected.equals(actualFirst));
        assertTrue("Differentiation of constant should return 1", expected.equals(actualSecond));
    }

    //covers case where value of c != variable
    @Test 
    public void testDifferentiateVariableNotEqual(){     
        char testCharacterFirst = 'v';
        char testCharacterSecond = 'A';
        Expression testNumberFirst = Expression.make(testCharacterFirst);
        Expression testNumberSecond = Expression.make(testCharacterSecond);
        Expression actualFirst = testNumberFirst.differentiate('z');
        Expression actualSecond = testNumberSecond.differentiate('Q');
        Expression expected = Expression.make("0");
        assertTrue("Differentiation of constant should return 0", expected.equals(actualFirst));
        assertTrue("Differentiation of constant should return 0", expected.equals(actualSecond));
    }

    //covers case where value of c != variable, upper case vs lower case
    @Test 
    public void testDifferentiateVariableNotEqualCaseSensitive(){     
        char testCharacterFirst = 'v';
        char testCharacterSecond = 'A';
        Expression testNumberFirst = Expression.make(testCharacterFirst);
        Expression testNumberSecond = Expression.make(testCharacterSecond);
        Expression actualFirst = testNumberFirst.differentiate('V');
        Expression actualSecond = testNumberSecond.differentiate('a');
        Expression expected = Expression.make("0");
        assertTrue("Differentiation of constant should return 0", expected.equals(actualFirst));
        assertTrue("Differentiation of constant should return 0", expected.equals(actualSecond));
    }

    //covers case where c in environment
    @Test
    public void testCanEvaluateVariableInEnvironment(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);      
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("G", new BigDecimal("2"));
        boolean testCanEvaluate = testFirst.canEvaluate(environment);
        assertTrue("should be able to evaluate", testCanEvaluate);
    }

    //covers case where c not in environment
    @Test
    public void testCanEvaluateVariableNotInEnvironment(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);      
        Map<String,BigDecimal> environment = new HashMap<>();
        boolean testCanEvaluate = testFirst.canEvaluate(environment);
        assertFalse("should not be able to evaluate", testCanEvaluate);
    }

    //covers case where c in environment
    @Test
    public void testNumberifyVariableInEnvironment(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);      
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("G", new BigDecimal("2"));
        Expression evaluated = testFirst.numberify(environment);
        Expression expected = new Number("2");
        assertTrue("evaluated incorrectly", expected.equals(evaluated));
    }

    //covers case where c not in environment
    @Test
    public void testNumberifyVariableNotInEnvironment(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);      
        Map<String,BigDecimal> environment = new HashMap<>();
        Expression evaluated = testFirst.numberify(environment);
        assertTrue("evaluated incorrectly", testFirst.equals(evaluated));
    }

    //covers case where c not in environment
    @Test(expected=IllegalArgumentException.class)
    public void testEvaluateVariableAlwaysFalse(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);     
        BigDecimal evaluated = testFirst.evaluate();
    }

    //--------------------------------TESTING FOR VARIABLE ENDS----------------------------------//
    //--------------------------------TESTING FOR PLUS BEGINS----------------------------------//   

    //covers case where left is a Number object
    //                  right is a Number object
    @Test 
    public void testToStringPlusNumberAndNumber(){
        String testInputFirst = "0";
        Expression testNumberFirst = Expression.make(testInputFirst);                
        String testInputSecond = "0.0000000";
        Expression testNumberSecond = Expression.make(testInputSecond);
        String expected = "(0)+(0)";
        Expression testPlus = Expression.add(testNumberFirst, testNumberSecond);
        String actual = testPlus.toString();
        assertTrue("Plus string was not as expected", actual.equals(expected));
    }

    //covers case where left is a Variable object
    //                  right is a Number object
    @Test 
    public void testToStringPlusVariableAndNumber(){
        char testInputFirst = 'a';
        Expression testFirst = Expression.make(testInputFirst);                
        String testInputSecond = "12.6547864";
        Expression testSecond = Expression.make(testInputSecond);
        String expected = "(a)+(12.6547864)";
        Expression testPlus = Expression.add(testFirst, testSecond);
        String actual = testPlus.toString();
        assertTrue("Plus string was not as expected"+actual, actual.equals(expected));
    }

    //covers case where left is a Number object
    //                  right is a Variable object
    @Test 
    public void testToStringPlusNumberAndVariable(){
        char testInputFirst = 'A';
        Expression testFirst = Expression.make(testInputFirst);                
        String testInputSecond = "12.6547864";
        Expression testSecond = Expression.make(testInputSecond);
        String expected = "(12.6547864)+(A)";
        Expression testPlus = Expression.add(testSecond, testFirst);
        String actual = testPlus.toString();
        assertTrue("Plus string was not as expected", actual.equals(expected));
    }

    //covers case where left is a Variable object
    //                  right is a Variable object
    @Test 
    public void testToStringPlusVariableAndVariable(){
        char testInputFirst = 'a';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'q';
        Expression testSecond = Expression.make(testInputSecond);

        String expected = "(a)+(q)";
        Expression testPlus = Expression.add(testFirst, testSecond);
        String actual = testPlus.toString();
        assertTrue("Plus string was not as expected"+actual, actual.equals(expected));
    }

    //covers case where left is a Plus object
    //                  right is a Plus object
    @Test 
    public void testToStringPlusPlusAndPlus(){
        char testInputFirst = 'a';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'q';
        Expression testSecond = Expression.make(testInputSecond);
        char testInputThird = 'A';
        Expression testThird = Expression.make(testInputThird);                
        String testInputFourth = "12.6547864";
        Expression testFourth = Expression.make(testInputFourth);
        Expression testLeftPlus = Expression.add(testFirst, testSecond);
        Expression testRightPlus = Expression.add(testThird, testFourth);
        Expression testPlus = Expression.add(testLeftPlus, testRightPlus);

        String expected = "((a)+(q))+((A)+(12.6547864))";
        String actual = testPlus.toString();
        assertTrue("Plus string was not as expected", actual.equals(expected));
    }

    //covers case where left is a Plus object
    //                  right is a Number object
    @Test 
    public void testToStringPlusPlusAndNumber(){
        char testInputFirst = 'a';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'q';
        Expression testSecond = Expression.make(testInputSecond);            
        String testInputThird = "12.6547864";
        Expression testThird = Expression.make(testInputThird);
        Expression testLeftPlus = Expression.add(testFirst, testSecond);
        Expression testPlus = Expression.add(testLeftPlus, testThird);

        String expected = "((a)+(q))+(12.6547864)";
        String actual = testPlus.toString();
        assertTrue("Plus string was not as expected"+actual, actual.equals(expected));
    }

    //covers case where left is a Variable object
    //                  right is a Plus object
    @Test 
    public void testToStringPlusVariableAndPlus(){
        char testInputFirst = 'a';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'q';
        Expression testSecond = Expression.make(testInputSecond);            
        String testInputThird = "12.6547864";
        Expression testThird = Expression.make(testInputThird);
        Expression testLeftPlus = Expression.add(testFirst, testThird);
        Expression testPlus = Expression.add(testSecond, testLeftPlus);

        String expected = "(q)+((a)+(12.6547864))";
        String actual = testPlus.toString();
        assertTrue("Plus string was not as expected"+actual, actual.equals(expected));
    }

    //covers case where left is a Times object
    //                  right is a Times object
    @Test 
    public void testToStringPlusTimesAndTimes(){
        char testInputFirst = 'a';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'q';
        Expression testSecond = Expression.make(testInputSecond);      

        String testInputThird = "12.6547864";
        Expression testThird = Expression.make(testInputThird);
        String testInputFourth = "12.6547864";
        Expression testFourth = Expression.make(testInputFourth);

        Expression testLeftTimes = Expression.times(testFirst, testSecond);
        Expression testRightTimes = Expression.times(testThird, testFourth);
        Expression testPlus = Expression.add(testLeftTimes, testRightTimes);

        String expected = "((a)*(q))+((12.6547864)*(12.6547864))";
        String actual = testPlus.toString();
        assertTrue("Plus string was not as expected"+actual, actual.equals(expected));
    }

    //covers case where left is a Times object
    //                  right is a Plus object
    @Test 
    public void testToStringPlusTimesAndPlus(){
        char testInputFirst = 'a';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'q';
        Expression testSecond = Expression.make(testInputSecond);      

        String testInputThird = "12.6547864";
        Expression testThird = Expression.make(testInputThird);
        String testInputFourth = "12.6547864";
        Expression testFourth = Expression.make(testInputFourth);

        Expression testLeftTimes = Expression.times(testFirst, testSecond);
        Expression testRightPlus = Expression.add(testThird, testFourth);
        Expression testPlus = Expression.add(testLeftTimes, testRightPlus);

        String expected = "((a)*(q))+((12.6547864)+(12.6547864))";
        String actual = testPlus.toString();
        assertTrue("Plus string was not as expected"+actual, actual.equals(expected));
    }

    //covers case where left is a Exponent object
    //                  right is a Exponent object
    @Test 
    public void testToStringPlusExponentAndExponent(){
        char testInputFirst = 'a';
        Expression testBaseFirst = Expression.make(testInputFirst);                
        Integer powerFirst = 2;
        Expression testFirst = Expression.power(testBaseFirst, powerFirst);

        String testInputSecond = "12.6547864";
        Expression testBaseSecond = Expression.make(testInputSecond);
        Integer powerSecond = 100;
        Expression testSecond = Expression.power(testBaseSecond, powerSecond);

        Expression testPlus = Expression.add(testFirst, testSecond);

        String expected = "((a)^2)+((12.6547864)^100)";
        String actual = testPlus.toString();
        assertTrue("Plus string expected to be:"+expected+
                "but was actually:"+actual, actual.equals(expected));
    }

    //covers case where left is a Exponent object
    //                  right is a Variable object
    @Test 
    public void testToStringPlusExponentAndVariable(){
        char testInputFirst = 'a';
        Expression testBaseFirst = Expression.make(testInputFirst);                
        Integer powerFirst = 2;
        Expression testFirst = Expression.power(testBaseFirst, powerFirst);
        Expression testPlus = Expression.add(testFirst, testBaseFirst);

        String expected = "((a)^2)+(a)";
        String actual = testPlus.toString();
        assertTrue("Expression string expected to be:"+expected+
                "but was actually:"+actual, actual.equals(expected));
    }

    //covers case where left is a Plus object
    //                  right is a Exponent object
    @Test 
    public void testToStringPlusPlusAndExponent(){
        char testInputFirst = 'a';
        Expression testBaseFirst = Expression.make(testInputFirst);                
        Integer powerFirst = 0;
        Expression testFirst = Expression.power(testBaseFirst, powerFirst);
        String testInputSecond = "12.6547864";
        Expression testBaseSecond = Expression.make(testInputSecond);
        Expression testSecond = Expression.add(testBaseFirst, testBaseSecond);
        Expression testPlus = Expression.add(testSecond, testFirst);

        String expected = "((a)+(12.6547864))+((a)^0)";
        String actual = testPlus.toString();
        assertTrue("Plus string expected to be:"+expected+
                "but was actually:"+actual, actual.equals(expected));   
    }

    //covers case where left is a Plus object
    //                  right is a Number object
    @Test 
    public void testToStringConsistent(){
        //e:Expression, e.equals(Expression.parse(e.toString()))
        String testInputFirst = "8";
        Expression testFirst = Expression.make(testInputFirst);             
        String testInputSecond = "0.5555555555555551";
        Expression testSecond = Expression.make(testInputSecond);
        Expression testLeft = Expression.add(testFirst, testSecond);
        String testInputThird = "9999999999999999993";
        Expression testThird = Expression.make(testInputThird);
        Expression testPlus = Expression.add(testLeft, testThird);
        Expression testParsed = Expression.parse("8 + 0.5555555555555551 + 9999999999999999993");
        String testInputFirstDuplicate = "8.00";
        Expression testFirstDuplicate = Expression.make(testInputFirstDuplicate);             
        String testInputSecondDuplicate = "0.5555555555555551000";
        Expression testSecondDuplicate = Expression.make(testInputSecondDuplicate);
        Expression testLeftDuplicate = Expression.add(testFirstDuplicate, testSecondDuplicate);
        String testInputThirdDuplicate = "9999999999999999993.0";
        Expression testThirdDuplicate = Expression.make(testInputThirdDuplicate);
        Expression testPlusDuplicate = Expression.add(testLeftDuplicate, testThirdDuplicate);
        String parsed = testParsed.toString();
        String actual = testPlus.toString();
        String duplicate = testPlusDuplicate.toString();
        assertTrue("two strings should be equivalent 1", parsed.equals(duplicate));   
        assertTrue("two strings should be equivalent 2", testPlusDuplicate.equals(Expression.parse(duplicate)));   
        assertTrue("two strings should be equivalent 3", testParsed.equals(Expression.parse(parsed)));   
    }
        
    //covers case where thatObject is not an Plus object 
    @Test 
    public void testEqualsPlusNotAPlusObject(){
        char testInputFirst = 'a';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'q';
        Expression testSecond = Expression.make(testInputSecond);            
        String testInputThird = "12.6547864";
        Expression testThird = Expression.make(testInputThird);
        Expression testPlus = Expression.add(testFirst, testSecond);

        assertFalse("Plus object should not equal Number object", testPlus.equals(testThird));
    }

    //covers case where thatObject is an equivalent Plus object
    @Test
    public void testEqualsPlusSimpleEqualPlusObjects(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'G';
        Expression testSecond = Expression.make(testInputSecond);            
        String testInputThird = "12.6547864";
        Expression testThird = Expression.make(testInputThird);
        String testInputFourth = "12.6547864";
        Expression testFourth = Expression.make(testInputFourth);
        Expression testPlusFirst = Expression.add(testFirst, testThird);
        Expression testPlusSecond = Expression.add(testSecond, testFourth);
        assertTrue("Plus object should equal other Plus object", testPlusFirst.equals(testPlusSecond));
    }

    //covers case where thatObject is an equivalent Plus object
    //"recursive" required
    @Test
    public void testEqualsPlusComplexEqualPlusObjects(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);            
        String testInputThird = "12.6547864";
        Expression testThird = Expression.make(testInputThird);
        String testInputFourth = "12.6547864";
        Expression testFourth = Expression.make(testInputFourth);
        Expression testPlusFirstLeft = Expression.add(testFirst, testThird);
        Expression testPlusFirstRight = Expression.add(testSecond, testFourth);
        Expression testPlusFirst = Expression.add(testPlusFirstLeft, testPlusFirstRight);

        //all duplicates of first batch 
        char testInputFirstDuplicate = 'G';
        Expression testFirstDuplicate = Expression.make(testInputFirstDuplicate);                
        char testInputSecondDuplicate = 'A';
        Expression testSecondDuplicate = Expression.make(testInputSecondDuplicate);            
        String testInputThirdDuplicate = "12.6547864";
        Expression testThirdDuplicate = Expression.make(testInputThirdDuplicate);
        String testInputFourthDuplicate = "12.6547864";
        Expression testFourthDuplicate = Expression.make(testInputFourthDuplicate);
        Expression testPlusSecondLeft = Expression.add(testFirstDuplicate, testThirdDuplicate);
        Expression testPlusSecondRight = Expression.add(testSecondDuplicate, testFourthDuplicate);
        Expression testPlusSecond = Expression.add(testPlusSecondLeft, testPlusSecondRight);

        assertTrue("Plus object should equal other Plus object", testPlusFirst.equals(testPlusSecond));
    }

    //covers case where thatObject is not an equivalent Plus object
    //                  has same left and right expressions, but are flipped
    @Test
    public void testEqualsPlusNotEqualFlippedLeftAndRight(){
        char testInputFirst = 'Q';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'Q';
        Expression testSecond = Expression.make(testInputSecond);            
        String testInputThird = "6.031";
        Expression testThird = Expression.make(testInputThird);
        String testInputFourth = "6.031";
        Expression testFourth = Expression.make(testInputFourth);
        Expression testPlusFirst = Expression.add(testFirst, testThird);
        Expression testPlusSecond = Expression.add(testFourth, testSecond);
        assertFalse("Plus object should not equal other Plus object", testPlusFirst.equals(testPlusSecond));
    }

    //covers case where thatObject is not an equivalent Plus object
    //                  simply just two different Plus expression
    @Test
    public void testEqualsPlusNotEqualAtAll(){
        char testInputFirst = 'Q';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'W';
        Expression testSecond = Expression.make(testInputSecond);            
        String testInputThird = "6.031";
        Expression testThird = Expression.make(testInputThird);
        String testInputFourth = "6.170";
        Expression testFourth = Expression.make(testInputFourth);
        Expression testPlusFirst = Expression.add(testFirst, testThird);
        Expression testPlusSecond = Expression.add(testFourth, testSecond);
        assertFalse("Plus object should not equal other Plus object", testPlusFirst.equals(testPlusSecond));
    }

    //covers case where two equivalent Plus objects have same hashCode
    @Test
    public void testHashCodePlusSimple(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'G';
        Expression testSecond = Expression.make(testInputSecond);            
        String testInputThird = "12.6547864";
        Expression testThird = Expression.make(testInputThird);
        String testInputFourth = "12.6547864";
        Expression testFourth = Expression.make(testInputFourth);
        Expression testPlusFirst = Expression.add(testFirst, testThird);
        Expression testPlusSecond = Expression.add(testSecond, testFourth);
        int hashCodeFirst = testPlusFirst.hashCode();
        int hashCodeSecond = testPlusSecond.hashCode();
        assertEquals("Two equal Plus objects should have the same hashCode", hashCodeFirst, hashCodeSecond);
    }

    //covers case where thatObject is an equivalent Plus object
    //"recursive" required
    @Test
    public void testHashCodePlusComplex(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);            
        String testInputThird = "12.6547864";
        Expression testThird = Expression.make(testInputThird);
        String testInputFourth = "12.6547864";
        Expression testFourth = Expression.make(testInputFourth);
        Expression testPlusFirstLeft = Expression.add(testFirst, testThird);
        Expression testPlusFirstRight = Expression.add(testSecond, testFourth);
        Expression testPlusFirst = Expression.add(testPlusFirstLeft, testPlusFirstRight);

        //all duplicates of first batch 
        char testInputFirstDuplicate = 'G';
        Expression testFirstDuplicate = Expression.make(testInputFirstDuplicate);                
        char testInputSecondDuplicate = 'A';
        Expression testSecondDuplicate = Expression.make(testInputSecondDuplicate);            
        String testInputThirdDuplicate = "12.6547864";
        Expression testThirdDuplicate = Expression.make(testInputThirdDuplicate);
        String testInputFourthDuplicate = "12.6547864";
        Expression testFourthDuplicate = Expression.make(testInputFourthDuplicate);
        Expression testPlusSecondLeft = Expression.add(testFirstDuplicate, testThirdDuplicate);
        Expression testPlusSecondRight = Expression.add(testSecondDuplicate, testFourthDuplicate);
        Expression testPlusSecond = Expression.add(testPlusSecondLeft, testPlusSecondRight);

        int hashCodeFirst = testPlusFirst.hashCode();
        int hashCodeSecond = testPlusSecond.hashCode();
        assertEquals("Two equal Plus objects should have the same hashCode", hashCodeFirst, hashCodeSecond);
    }

    //covers case where no variables in left in environment
    //            no variables in right in environment
    @Test
    public void testCanEvaluatePlusNoLeftNoRight(){
        //G+A + Q
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);     
        char testInputThird = 'Q';
        Expression testThird = Expression.make(testInputThird);   
        Expression testPlusFirst = Expression.add(testSecond, testThird);
        Expression testPlus = Expression.add(testFirst, testPlusFirst);
        Map<String,BigDecimal> environment = new HashMap<>();
        boolean actual = testPlus.canEvaluate(environment);
        assertFalse("should not be able to evaluate", actual);
    }

    //covers case where no variables in right in environment
    //            some variables in left in environment
    @Test
    public void testCanEvaluatePlusSomeLeftNoRight(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);     
        char testInputThird = 'Q';
        Expression testThird = Expression.make(testInputThird);   
        Expression testPlusFirst = Expression.add(testFirst, testSecond);
        Expression testPlus = Expression.add(testPlusFirst, testThird);
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("Q", new BigDecimal("2"));
        boolean actual = testPlus.canEvaluate(environment);
        assertFalse("should not be able to evaluate", actual);
    }

    //covers case where no variables in left in environment
    //            some variables in right in environment
    @Test
    public void testCanEvaluatePlusNoLeftSomeRight(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);     
        char testInputThird = 'Q';
        Expression testThird = Expression.make(testInputThird);  
        char testInputFourth = 'v';
        Expression testFourth = Expression.make(testInputFourth); 
        Expression testPlusFirst = Expression.add(testFirst, testSecond);
        Expression testPlusSecond = Expression.add(testThird, testFourth);
        Expression testPlus = Expression.add(testPlusFirst, testPlusSecond);
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("Q", new BigDecimal("2"));
        boolean actual = testPlus.canEvaluate(environment);
        assertFalse("should not be able to evaluate", actual);
    }

    //covers case where all variables in left in environment
    //            some variables in right in environment
    @Test
    public void testCanEvaluatePlusAllLeftSomeRight(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);     
        char testInputThird = 'Q';
        Expression testThird = Expression.make(testInputThird);  
        char testInputFourth = 'v';
        Expression testFourth = Expression.make(testInputFourth); 
        Expression testPlusFirst = Expression.add(testFirst, testSecond);
        Expression testPlusSecond = Expression.add(testThird, testFourth);
        Expression testPlus = Expression.add(testPlusFirst, testPlusSecond);
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("G", new BigDecimal("2"));
        environment.put("A", new BigDecimal("2"));
        environment.put("v", new BigDecimal("2"));
        boolean actual = testPlus.canEvaluate(environment);
        assertFalse("should not be able to evaluate", actual);
    }

    //covers case where all variables in left in environment
    //            all variables in right in environment
    @Test
    public void testCanEvaluatePlusAllLeftAllRight(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);     
        char testInputThird = 'Q';
        Expression testThird = Expression.make(testInputThird);  
        char testInputFourth = 'v';
        Expression testFourth = Expression.make(testInputFourth); 
        Expression testPlusFirst = Expression.add(testFirst, testSecond);
        Expression testPlusSecond = Expression.add(testThird, testFourth);
        Expression testPlus = Expression.add(testPlusFirst, testPlusSecond);
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("G", new BigDecimal("2"));
        environment.put("A", new BigDecimal("2"));
        environment.put("Q", new BigDecimal("0"));
        environment.put("v", new BigDecimal("2"));
        boolean actual = testPlus.canEvaluate(environment);
        assertTrue("should be able to evaluate", actual);
    }

    //covers case where no variables in left in environment
    //            no variables in right in environment

    @Test
    public void testNumberifyPlusNoLeftNoRight(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);     
        char testInputThird = 'Q';
        Expression testThird = Expression.make(testInputThird);   
        Expression testPlusFirst = Expression.add(testSecond, testThird);
        Expression testPlus = Expression.add(testFirst, testPlusFirst);
        Map<String,BigDecimal> environment = new HashMap<>();
        Expression actual = testPlus.numberify(environment);
        assertTrue("numberified incorrectly", testPlus.equals(actual));
    }

    //covers case where no variables in right in environment
    //            some variables in left in environment
    @Test
    public void testNumberifyPlusSomeLeftNoRight(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);     
        char testInputThird = 'Q';
        Expression testThird = Expression.make(testInputThird);   
        Expression testPlusFirst = Expression.add(testFirst, testSecond);
        Expression testPlus = Expression.add(testPlusFirst, testThird);
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("Q", new BigDecimal("2"));

        String expectedNumberify = "2";
        Expression expectedNumberifyExpression = Expression.make(expectedNumberify);   
        Expression expected = Expression.add(testPlusFirst, expectedNumberifyExpression);
        Expression actual = testPlus.numberify(environment);
        assertTrue("numberified incorrectly", expected.equals(actual));
    }

    //covers case where no variables in left in environment
    //            some variables in right in environment
    @Test
    public void testNumberifyPlusNoLeftSomeRight(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);     
        char testInputThird = 'Q';
        Expression testThird = Expression.make(testInputThird);  
        char testInputFourth = 'v';
        Expression testFourth = Expression.make(testInputFourth); 
        Expression testPlusFirst = Expression.add(testFirst, testSecond);
        Expression testPlusSecond = Expression.add(testThird, testFourth);
        Expression testPlus = Expression.add(testPlusFirst, testPlusSecond);
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("Q", new BigDecimal("2"));

        String expectedNumberify = "2";
        Expression expectedNumberifyExpression = Expression.make(expectedNumberify);   
        Expression expectedRight = Expression.add(expectedNumberifyExpression, testFourth);

        Expression expected = Expression.add(testPlusFirst, expectedRight);
        Expression actual = testPlus.numberify(environment);
        assertTrue("numberified incorrectly", expected.equals(actual));
    }

    //covers case where all variables in left in environment
    //            some variables in right in environment
    @Test
    public void testNumberifyPlusAllLeftSomeRight(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);     
        char testInputThird = 'Q';
        Expression testThird = Expression.make(testInputThird);  
        char testInputFourth = 'v';
        Expression testFourth = Expression.make(testInputFourth); 
        Expression testPlusFirst = Expression.add(testFirst, testSecond);
        Expression testPlusSecond = Expression.add(testThird, testFourth);
        Expression testPlus = Expression.add(testPlusFirst, testPlusSecond);
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("G", new BigDecimal("2"));
        environment.put("A", new BigDecimal("6"));
        environment.put("Q", new BigDecimal("0"));

        String expectedNumberifyFirst = "2";
        Expression expectedNumberifyFirstExpression = Expression.make(expectedNumberifyFirst); 
        String expectedNumberifySecond = "6";
        Expression expectedNumberifySecondExpression = Expression.make(expectedNumberifySecond); 
        Expression expectedLeft = Expression.add(expectedNumberifyFirstExpression, expectedNumberifySecondExpression);
        String expectedNumberifyThird = "0";
        Expression expectedNumberifyThirdExpression = Expression.make(expectedNumberifyThird); 
        Expression expectedRight = Expression.add(expectedNumberifyThirdExpression, testFourth);

        Expression expected = Expression.add(expectedLeft, expectedRight);
        Expression actual = testPlus.numberify(environment);
        assertTrue("numberified incorrectly", expected.equals(actual));
    }

    //covers case where all variables in left in environment
    //            all variables in right in environment
    @Test
    public void testNumberifyPlusAllLeftAllRight(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);     
        char testInputThird = 'Q';
        Expression testThird = Expression.make(testInputThird);  
        char testInputFourth = 'v';
        Expression testFourth = Expression.make(testInputFourth); 
        Expression testPlusFirst = Expression.add(testFirst, testSecond);
        Expression testPlusSecond = Expression.add(testThird, testFourth);
        Expression testPlus = Expression.add(testPlusFirst, testPlusSecond);
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("G", new BigDecimal("2"));
        environment.put("A", new BigDecimal("213"));
        environment.put("Q", new BigDecimal("0"));
        environment.put("v", new BigDecimal("9"));
        String expectedNumberifyFirst = "2";
        Expression expectedNumberifyFirstExpression = Expression.make(expectedNumberifyFirst); 
        String expectedNumberifySecond = "213";
        Expression expectedNumberifySecondExpression = Expression.make(expectedNumberifySecond); 
        Expression expectedLeft = Expression.add(expectedNumberifyFirstExpression, expectedNumberifySecondExpression);
        String expectedNumberifyThird = "0";
        Expression expectedNumberifyThirdExpression = Expression.make(expectedNumberifyThird); 
        String expectedNumberifyFourth = "9";
        Expression expectedNumberifyFourthExpression = Expression.make(expectedNumberifyFourth); 
        Expression expectedRight = Expression.add(expectedNumberifyThirdExpression, expectedNumberifyFourthExpression);

        Expression expected = Expression.add(expectedLeft, expectedRight);
        Expression actual = testPlus.numberify(environment);
        assertTrue("numberified incorrectly", expected.equals(actual));
    }

    //covers case where no variables in left in environment
    //            no variables in right in environment

    @Test(expected=IllegalArgumentException.class)
    public void testEvaluatePlusNoLeftNoRight(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);     
        char testInputThird = 'Q';
        Expression testThird = Expression.make(testInputThird);   
        Expression testPlusFirst = Expression.add(testSecond, testThird);
        Expression testPlus = Expression.add(testFirst, testPlusFirst);
        BigDecimal actual = testPlus.evaluate();
    }

    //covers case where no variables in right in environment
    //            some variables in left in environment
    @Test(expected=IllegalArgumentException.class)
    public void testEvaluatePlusSomeLeftNoRight(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);     
        char testInputThird = 'Q';
        Expression testThird = Expression.make(testInputThird);   
        Expression testPlusFirst = Expression.add(testFirst, testSecond);
        Expression testPlus = Expression.add(testPlusFirst, testThird);
        BigDecimal actual = testPlus.evaluate();
    }

    //covers case where no variables in left in environment
    //            some variables in right in environment
    @Test(expected=IllegalArgumentException.class)
    public void testEvaluatePlusNoLeftSomeRight(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);     
        char testInputThird = 'Q';
        Expression testThird = Expression.make(testInputThird);  
        char testInputFourth = 'v';
        Expression testFourth = Expression.make(testInputFourth); 
        Expression testPlusFirst = Expression.add(testFirst, testSecond);
        Expression testPlusSecond = Expression.add(testThird, testFourth);
        Expression testPlus = Expression.add(testPlusFirst, testPlusSecond);
        BigDecimal actual = testPlus.evaluate();
    }

    //covers case where all variables in left in environment
    //            some variables in right in environment
    @Test(expected=IllegalArgumentException.class)
    public void testEvaluatePlusAllLeftSomeRight(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);     
        char testInputThird = 'Q';
        Expression testThird = Expression.make(testInputThird);  
        char testInputFourth = 'v';
        Expression testFourth = Expression.make(testInputFourth); 
        Expression testPlusFirst = Expression.add(testFirst, testSecond);
        Expression testPlusSecond = Expression.add(testThird, testFourth);
        Expression testPlus = Expression.add(testPlusFirst, testPlusSecond);
        BigDecimal actual = testPlus.evaluate();
    }

    //covers case where all variables in left in environment
    //            all variables in right in environment
    @Test
    public void testEvaluatePlusAllLeftAllRight(){
        String testInputFirst = "1";
        Expression testFirst = Expression.make(testInputFirst);                
        String testInputSecond = "9";
        Expression testSecond = Expression.make(testInputSecond);     
        String testInputThird = "0";
        Expression testThird = Expression.make(testInputThird);  
        String testInputFourth = "1234";
        Expression testFourth = Expression.make(testInputFourth); 
        Expression testPlusFirst = Expression.add(testFirst, testSecond);
        Expression testPlusSecond = Expression.add(testThird, testFourth);
        Expression testPlus = Expression.add(testPlusFirst, testPlusSecond);
        BigDecimal actual = testPlus.evaluate();
        BigDecimal expected = new BigDecimal("1244");
        assertTrue("evaluated incorrectly", expected.compareTo(actual) == 0);
    }

    //--------------------------------TESTING FOR PLUS ENDS----------------------------------//
    //--------------------------------TESTING FOR TIMES BEGINS----------------------------------//   

    //covers case where left is a Number object
    //                  right is a Number object
    @Test 
    public void testToStringTimesNumberAndNumber(){
        String testInputFirst = "0";
        Expression testNumberFirst = Expression.make(testInputFirst);                
        String testInputSecond = "0.0000000";
        Expression testNumberSecond = Expression.make(testInputSecond);
        String expected = "(0)*(0)";
        Expression testTimes = Expression.times(testNumberFirst, testNumberSecond);
        String actual = testTimes.toString();
        assertTrue("Times string was not as expected", actual.equals(expected));
    }

    //covers case where left is a Variable object
    //                  right is a Number object
    @Test 
    public void testToStringTimesVariableAndNumber(){
        char testInputFirst = 'a';
        Expression testFirst = Expression.make(testInputFirst);                
        String testInputSecond = "12.6547864";
        Expression testSecond = Expression.make(testInputSecond);
        String expected = "(a)*(12.6547864)";
        Expression testTimes = Expression.times(testFirst, testSecond);
        String actual = testTimes.toString();
        assertTrue("Times string was not as expected", actual.equals(expected));
    }

    //covers case where left is a Number object
    //                  right is a Variable object
    @Test 
    public void testToStringTimesNumberAndVariable(){
        char testInputFirst = 'A';
        Expression testFirst = Expression.make(testInputFirst);                
        String testInputSecond = "12.6547864";
        Expression testSecond = Expression.make(testInputSecond);
        String expected = "(12.6547864)*(A)";
        Expression testTimes = Expression.times(testSecond, testFirst);
        String actual = testTimes.toString();
        assertTrue("Times string was not as expected", actual.equals(expected));
    }

    //covers case where left is a Variable object
    //                  right is a Variable object
    @Test 
    public void testToStringTimesVariableAndVariable(){
        char testInputFirst = 'a';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'q';
        Expression testSecond = Expression.make(testInputSecond);

        String expected = "(a)*(q)";
        Expression testTimes = Expression.times(testFirst, testSecond);
        String actual = testTimes.toString();
        assertTrue("Times string was not as expected", actual.equals(expected));
    }

    //covers case where left is a Plus object
    //                  right is a Plus object
    @Test 
    public void testToStringTimesPlusAndPlus(){
        char testInputFirst = 'a';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'q';
        Expression testSecond = Expression.make(testInputSecond);
        char testInputThird = 'A';
        Expression testThird = Expression.make(testInputThird);                
        String testInputFourth = "12.6547864";
        Expression testFourth = Expression.make(testInputFourth);
        Expression testLeftPlus = Expression.add(testFirst, testSecond);
        Expression testRightPlus = Expression.add(testThird, testFourth);
        Expression testTimes = Expression.times(testLeftPlus, testRightPlus);

        String expected = "((a)+(q))*((A)+(12.6547864))";
        String actual = testTimes.toString();
        assertTrue("Times string was not as expected", actual.equals(expected));
    }

    //covers case where left is a Plus object
    //                  right is a Number object
    @Test 
    public void testToStringTimesPlusAndNumber(){
        char testInputFirst = 'a';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'q';
        Expression testSecond = Expression.make(testInputSecond);            
        String testInputThird = "12.6547864";
        Expression testThird = Expression.make(testInputThird);
        Expression testLeftPlus = Expression.add(testFirst, testSecond);
        Expression testTimes = Expression.times(testLeftPlus, testThird);

        String expected = "((a)+(q))*(12.6547864)";
        String actual = testTimes.toString();
        assertTrue("Times string was not as expected", actual.equals(expected));
    }

    //covers case where left is a Variable object
    //                  right is a Plus object
    @Test 
    public void testToStringTimesVariableAndPlus(){
        char testInputFirst = 'a';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'q';
        Expression testSecond = Expression.make(testInputSecond);            
        String testInputThird = "12.6547864";
        Expression testThird = Expression.make(testInputThird);
        Expression testLeftPlus = Expression.add(testFirst, testThird);
        Expression testTimes = Expression.times(testSecond, testLeftPlus);

        String expected = "(q)*((a)+(12.6547864))";
        String actual = testTimes.toString();
        assertTrue("Times string was not as expected", actual.equals(expected));
    }

    //covers case where left is a Times object
    //                  right is a Times object
    @Test 
    public void testToStringTimesTimesAndTimes(){
        char testInputFirst = 'a';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'q';
        Expression testSecond = Expression.make(testInputSecond);      

        String testInputThird = "12.6547864";
        Expression testThird = Expression.make(testInputThird);
        String testInputFourth = "12.6547864";
        Expression testFourth = Expression.make(testInputFourth);

        Expression testLeftTimes = Expression.times(testFirst, testSecond);
        Expression testRightTimes = Expression.times(testThird, testFourth);
        Expression testTimes = Expression.times(testLeftTimes, testRightTimes);

        String expected = "((a)*(q))*((12.6547864)*(12.6547864))";
        String actual = testTimes.toString();
        assertTrue("Times string was not as expected", actual.equals(expected));
    }

    //covers case where left is a Times object
    //                  right is a Plus object
    @Test 
    public void testToStringTimesTimesAndPlus(){
        char testInputFirst = 'a';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'q';
        Expression testSecond = Expression.make(testInputSecond);      

        String testInputThird = "12.6547864";
        Expression testThird = Expression.make(testInputThird);
        String testInputFourth = "12.6547864";
        Expression testFourth = Expression.make(testInputFourth);

        Expression testLeftTimes = Expression.times(testFirst, testSecond);
        Expression testRightPlus = Expression.add(testThird, testFourth);
        Expression testTimes = Expression.times(testLeftTimes, testRightPlus);

        String expected = "((a)*(q))*((12.6547864)+(12.6547864))";
        String actual = testTimes.toString();
        assertTrue("Times string was not as expected"+actual, actual.equals(expected));
    }

    //covers case where left is a Exponent object
    //                  right is a Exponent object
    @Test 
    public void testToStringTimesExponentAndExponent(){
        char testInputFirst = 'a';
        Expression testBaseFirst = Expression.make(testInputFirst);       
        Integer powerFirst = 2;
        Expression testFirst = Expression.power(testBaseFirst, powerFirst);

        String testInputSecond = "12.6547864";
        Expression testBaseSecond = Expression.make(testInputSecond);
        Integer powerSecond = 100;
        Expression testSecond = Expression.power(testBaseSecond, powerSecond);

        Expression testTimes = Expression.times(testFirst, testSecond);

        String expected = "((a)^2)*((12.6547864)^100)";
        String actual = testTimes.toString();
        assertTrue("Times string expected to be:"+expected+
                "but was actually:"+actual, actual.equals(expected));
    }

    //covers case where left is a Exponent object
    //                  right is a Variable object
    @Test 
    public void testToStringTimesExponentAndVariable(){
        char testInputFirst = 'a';
        Expression testBaseFirst = Expression.make(testInputFirst);                
        Integer powerFirst = 2;
        Expression testFirst = Expression.power(testBaseFirst, powerFirst);
        Expression testTimes = Expression.times(testFirst, testBaseFirst);

        String expected = "((a)^2)*(a)";
        String actual = testTimes.toString();
        assertTrue("Times string expected to be:"+expected+
                "but was actually:"+actual, actual.equals(expected));
    }

    //covers case where left is a Plus object
    //                  right is a Exponent object
    @Test 
    public void testToStringTimesPLusAndExponent(){
        char testInputFirst = 'a';
        Expression testBaseFirst = Expression.make(testInputFirst);                
        Integer powerFirst = 0;
        Expression testFirst = Expression.power(testBaseFirst, powerFirst);
        String testInputSecond = "12.6547864";
        Expression testBaseSecond = Expression.make(testInputSecond);
        Expression testSecond = Expression.add(testBaseFirst, testBaseSecond);
        Expression testTimes = Expression.times(testSecond, testFirst);

        String expected = "((a)+(12.6547864))*((a)^0)";
        String actual = testTimes.toString();
        assertTrue("Times string expected to be:"+expected+
                "but was actually:"+actual, actual.equals(expected));   
    }

    //covers case where thatObject is not an Plus object 
    @Test 
    public void testEqualsTimesNotATimesObject(){
        char testInputFirst = 'a';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'q';
        Expression testSecond = Expression.make(testInputSecond);            
        String testInputThird = "12.6547864";
        Expression testThird = Expression.make(testInputThird);
        Expression testTimes = Expression.times(testFirst, testSecond);

        assertFalse("Times object should not equal Number object", testTimes.equals(testThird));
    }

    //covers case where thatObject is an equivalent Times object
    @Test
    public void testEqualsTimesimpleEqualPlusObjects(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'G';
        Expression testSecond = Expression.make(testInputSecond);            
        String testInputThird = "12.6547864";
        Expression testThird = Expression.make(testInputThird);
        String testInputFourth = "12.6547864";
        Expression testFourth = Expression.make(testInputFourth);
        Expression testTimesFirst = Expression.times(testFirst, testThird);
        Expression testTimesSecond = Expression.times(testSecond, testFourth);
        assertTrue("Times object should equal other Times object", testTimesFirst.equals(testTimesSecond));
    }

    //covers case where thatObject is an equivalent Times object
    //"recursive" required
    @Test
    public void testEqualsTimesComplexEqualPlusObjects(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);            
        String testInputThird = "12.6547864";
        Expression testThird = Expression.make(testInputThird);
        String testInputFourth = "12.6547864";
        Expression testFourth = Expression.make(testInputFourth);
        Expression testPlusFirstLeft = Expression.add(testFirst, testThird);
        Expression testPlusFirstRight = Expression.add(testSecond, testFourth);
        Expression testTimesFirst = Expression.times(testPlusFirstLeft, testPlusFirstRight);

        //all duplicates of first batch 
        char testInputFirstDuplicate = 'G';
        Expression testFirstDuplicate = Expression.make(testInputFirstDuplicate);                
        char testInputSecondDuplicate = 'A';
        Expression testSecondDuplicate = Expression.make(testInputSecondDuplicate);            
        String testInputThirdDuplicate = "12.6547864";
        Expression testThirdDuplicate = Expression.make(testInputThirdDuplicate);
        String testInputFourthDuplicate = "12.6547864";
        Expression testFourthDuplicate = Expression.make(testInputFourthDuplicate);
        Expression testPlusSecondLeft = Expression.add(testFirstDuplicate, testThirdDuplicate);
        Expression testPlusSecondRight = Expression.add(testSecondDuplicate, testFourthDuplicate);
        Expression testTimesSecond = Expression.times(testPlusSecondLeft, testPlusSecondRight);

        assertTrue("Times object should equal other Times object", testTimesFirst.equals(testTimesSecond));
    }

    //covers case where thatObject is not an equivalent Times object
    //                  has same left and right expressions, but are flipped
    @Test
    public void testEqualsTimesNotEqualFlippedLeftAndRight(){
        char testInputFirst = 'Q';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'Q';
        Expression testSecond = Expression.make(testInputSecond);            
        String testInputThird = "6.031";
        Expression testThird = Expression.make(testInputThird);
        String testInputFourth = "6.031";
        Expression testFourth = Expression.make(testInputFourth);
        Expression testTimesFirst = Expression.times(testFirst, testThird);
        Expression testTimesSecond = Expression.times(testFourth, testSecond);
        assertFalse("Times object should not equal other Times object", testTimesFirst.equals(testTimesSecond));
    }

    //covers case where thatObject is not an equivalent Times object
    //                  simply just two different Times expression
    @Test
    public void testEqualsTimesNotEqualAtAll(){
        char testInputFirst = 'Q';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'W';
        Expression testSecond = Expression.make(testInputSecond);            
        String testInputThird = "6.031";
        Expression testThird = Expression.make(testInputThird);
        String testInputFourth = "6.170";
        Expression testFourth = Expression.make(testInputFourth);
        Expression testTimesFirst = Expression.times(testFirst, testThird);
        Expression testTimesSecond = Expression.times(testFourth, testSecond);
        assertFalse("Times object should not equal other Times object", testTimesFirst.equals(testTimesSecond));
    }

    //covers case where two equivalent Times objects have same hashCode
    @Test
    public void testHashCodeTimesSimple(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'G';
        Expression testSecond = Expression.make(testInputSecond);            
        String testInputThird = "12.6547864";
        Expression testThird = Expression.make(testInputThird);
        String testInputFourth = "12.6547864";
        Expression testFourth = Expression.make(testInputFourth);
        Expression testTimesFirst = Expression.times(testFirst, testThird);
        Expression testTimesSecond = Expression.times(testSecond, testFourth);
        int hashCodeFirst = testTimesFirst.hashCode();
        int hashCodeSecond = testTimesSecond.hashCode();
        assertEquals("Two equal Times objects should have the same hashCode", hashCodeFirst, hashCodeSecond);
    }

    //covers case where thatObject is an equivalent Times object
    //"recursive" required
    @Test
    public void testHashCodeTimesComplex(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);            
        String testInputThird = "12.6547864";
        Expression testThird = Expression.make(testInputThird);
        String testInputFourth = "12.6547864";
        Expression testFourth = Expression.make(testInputFourth);
        Expression testPlusFirstLeft = Expression.add(testFirst, testThird);
        Expression testPlusFirstRight = Expression.add(testSecond, testFourth);
        Expression testTimesFirst = Expression.times(testPlusFirstLeft, testPlusFirstRight);

        //all duplicates of first batch 
        char testInputFirstDuplicate = 'G';
        Expression testFirstDuplicate = Expression.make(testInputFirstDuplicate);                
        char testInputSecondDuplicate = 'A';
        Expression testSecondDuplicate = Expression.make(testInputSecondDuplicate);            
        String testInputThirdDuplicate = "12.6547864";
        Expression testThirdDuplicate = Expression.make(testInputThirdDuplicate);
        String testInputFourthDuplicate = "12.6547864";
        Expression testFourthDuplicate = Expression.make(testInputFourthDuplicate);
        Expression testPlusSecondLeft = Expression.add(testFirstDuplicate, testThirdDuplicate);
        Expression testPlusSecondRight = Expression.add(testSecondDuplicate, testFourthDuplicate);
        Expression testTimesSecond = Expression.times(testPlusSecondLeft, testPlusSecondRight);

        int hashCodeFirst = testTimesFirst.hashCode();
        int hashCodeSecond = testTimesSecond.hashCode();
        assertEquals("Two equal Times objects should have the same hashCode", hashCodeFirst, hashCodeSecond);
    }

    //covers case variable is not a variable in left
    //            variable is not a variable in right
    @Test
    public void testDifferentiateTimesVariableNotLeftNotRight(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        String testInputSecond = "232423";
        Expression testSecond = Expression.make(testInputSecond);         
        Expression testPlusFirst = Expression.times(testFirst, testSecond);
        Expression actual = testPlusFirst.differentiate('a');
        Expression expected = Expression.parse("((232423)*(0))+((G)*(0))");   
        assertTrue("Differentiation was not as expected", actual.equals(expected));
    }

    //covers case variable is a variable in left
    //            variable is not a variable in right
    @Test
    public void testDifferentiateTimesVariableLeftNotRight(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        String testInputSecond = "232423";
        Expression testSecond = Expression.make(testInputSecond);         
        Expression testPlusFirst = Expression.times(testFirst, testSecond);
        Expression actual = testPlusFirst.differentiate('G');
        Expression expected = Expression.parse("((232423)*(1))+((G)*(0))");   
        assertTrue("Differentiation was not as expected"+actual.toString(), actual.equals(expected));
    }

    //covers case variable is not a variable in left
    //            variable is a variable in right
    @Test
    public void testDifferentiateTimesVariableNotLeftRight(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        String testInputSecond = "232423";
        Expression testSecond = Expression.make(testInputSecond);         
        Expression testPlusFirst = Expression.times(testSecond, testFirst);
        Expression actual = testPlusFirst.differentiate('G');
        Expression expected = Expression.parse("((G)*(0))+((232423)*(1))");   
        assertTrue("Differentiation was not as expected", actual.equals(expected));
    }

    //covers case variable is the only variable in left
    //            variable is one of many (>1) variables in right
    @Test
    public void testDifferentiateTimesVariableLeftOnlyRightMany(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'G';
        Expression testSecond = Expression.make(testInputSecond);            
        String testInputThird = "6";
        Expression testThird = Expression.make(testInputThird);
        char testInputFourth = 'G';
        Expression testFourth = Expression.make(testInputFourth);
        Expression testPlusFirstLeft = Expression.times(testFirst, testThird);//G*6
        Expression testPlusFirstRight = Expression.times(testSecond, testFourth);//G*G
        Expression testPlusFirst = Expression.times(testPlusFirstLeft, testPlusFirstRight);
        Expression actual = testPlusFirst.differentiate('G');//
        Expression expected = Expression.parse("((G*G)*((6*1)+(G*0)))+((G*6)*((G*1)+(G*1)))");   
        assertTrue("Differentiation was not as expected", actual.equals(expected));
    }

    //covers case variable is the only variable in right
    //            variable is one of many (>1) variables in left
    @Test
    public void testDifferentiateTimesVariableRightOnlyLeftMany(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'G';
        Expression testSecond = Expression.make(testInputSecond);            
        String testInputThird = "6";
        Expression testThird = Expression.make(testInputThird);
        char testInputFourth = 'G';
        Expression testFourth = Expression.make(testInputFourth);
        Expression testPlusFirstRight = Expression.times(testFirst, testThird);
        Expression testPlusFirstLeft = Expression.times(testSecond, testFourth);
        Expression testPlusFirst = Expression.times(testPlusFirstLeft, testPlusFirstRight);
        Expression actual = testPlusFirst.differentiate('G');
        Expression expected = Expression.parse("((G*6)*((G*1)+(G*1)))+((G*G)*((6*1)+(G*0)))");   
        assertTrue("Differentiation was not as expected", actual.equals(expected));
    }


    //covers case where no variables in left in environment
    //            no variables in right in environment
    @Test
    public void testCanEvaluateTimesNoLeftNoRight(){
        //G+A + Q
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);     
        char testInputThird = 'Q';
        Expression testThird = Expression.make(testInputThird);   
        Expression testPlusFirst = Expression.times(testSecond, testThird);
        Expression testPlus = Expression.times(testFirst, testPlusFirst);
        Map<String,BigDecimal> environment = new HashMap<>();
        boolean actual = testPlus.canEvaluate(environment);
        assertFalse("should not be able to evaluate", actual);
    }

    //covers case where no variables in right in environment
    //            some variables in left in environment
    @Test
    public void testCanEvaluateTimesSomeLeftNoRight(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);     
        char testInputThird = 'Q';
        Expression testThird = Expression.make(testInputThird);   
        Expression testPlusFirst = Expression.times(testFirst, testSecond);
        Expression testPlus = Expression.times(testPlusFirst, testThird);
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("Q", new BigDecimal("2"));
        boolean actual = testPlus.canEvaluate(environment);
        assertFalse("should not be able to evaluate", actual);
    }

    //covers case where no variables in left in environment
    //            some variables in right in environment
    @Test
    public void testCanEvaluateTimesNoLeftSomeRight(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);     
        char testInputThird = 'Q';
        Expression testThird = Expression.make(testInputThird);  
        char testInputFourth = 'v';
        Expression testFourth = Expression.make(testInputFourth); 
        Expression testPlusFirst = Expression.times(testFirst, testSecond);
        Expression testPlusSecond = Expression.times(testThird, testFourth);
        Expression testPlus = Expression.times(testPlusFirst, testPlusSecond);
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("Q", new BigDecimal("2"));
        boolean actual = testPlus.canEvaluate(environment);
        assertFalse("should not be able to evaluate", actual);
    }

    //covers case where all variables in left in environment
    //            some variables in right in environment
    @Test
    public void testCanEvaluateTimesAllLeftSomeRight(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);     
        char testInputThird = 'Q';
        Expression testThird = Expression.make(testInputThird);  
        char testInputFourth = 'v';
        Expression testFourth = Expression.make(testInputFourth); 
        Expression testPlusFirst = Expression.times(testFirst, testSecond);
        Expression testPlusSecond = Expression.times(testThird, testFourth);
        Expression testPlus = Expression.times(testPlusFirst, testPlusSecond);
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("G", new BigDecimal("2"));
        environment.put("A", new BigDecimal("2"));
        environment.put("v", new BigDecimal("2"));
        boolean actual = testPlus.canEvaluate(environment);
        assertFalse("should not be able to evaluate", actual);
    }

    //covers case where all variables in left in environment
    //            all variables in right in environment
    @Test
    public void testCanEvaluateTimesAllLeftAllRight(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);     
        char testInputThird = 'Q';
        Expression testThird = Expression.make(testInputThird);  
        char testInputFourth = 'v';
        Expression testFourth = Expression.make(testInputFourth); 
        Expression testPlusFirst = Expression.times(testFirst, testSecond);
        Expression testPlusSecond = Expression.times(testThird, testFourth);
        Expression testPlus = Expression.times(testPlusFirst, testPlusSecond);
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("G", new BigDecimal("2"));
        environment.put("A", new BigDecimal("2"));
        environment.put("Q", new BigDecimal("0"));
        environment.put("v", new BigDecimal("2"));
        boolean actual = testPlus.canEvaluate(environment);
        assertTrue("should be able to evaluate", actual);
    }

    //covers case where no variables in left in environment
    //            no variables in right in environment

    @Test
    public void testNumberifyTimesNoLeftNoRight(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);     
        char testInputThird = 'Q';
        Expression testThird = Expression.make(testInputThird);   
        Expression testPlusFirst = Expression.times(testSecond, testThird);
        Expression testPlus = Expression.times(testFirst, testPlusFirst);
        Map<String,BigDecimal> environment = new HashMap<>();
        Expression actual = testPlus.numberify(environment);
        assertTrue("numberified incorrectly", testPlus.equals(actual));
    }

    //covers case where no variables in right in environment
    //            some variables in left in environment
    @Test
    public void testNumberifyTimesSomeLeftNoRight(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);     
        char testInputThird = 'Q';
        Expression testThird = Expression.make(testInputThird);   
        Expression testPlusFirst = Expression.times(testFirst, testSecond);
        Expression testPlus = Expression.times(testPlusFirst, testThird);
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("Q", new BigDecimal("2"));

        String expectedNumberify = "2";
        Expression expectedNumberifyExpression = Expression.make(expectedNumberify);   
        Expression expected = Expression.times(testPlusFirst, expectedNumberifyExpression);
        Expression actual = testPlus.numberify(environment);
        assertTrue("numberified incorrectly", expected.equals(actual));
    }

    //covers case where no variables in left in environment
    //            some variables in right in environment
    @Test
    public void testNumberifyTimesNoLeftSomeRight(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);     
        char testInputThird = 'Q';
        Expression testThird = Expression.make(testInputThird);  
        char testInputFourth = 'v';
        Expression testFourth = Expression.make(testInputFourth); 
        Expression testPlusFirst = Expression.times(testFirst, testSecond);
        Expression testPlusSecond = Expression.times(testThird, testFourth);
        Expression testPlus = Expression.times(testPlusFirst, testPlusSecond);
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("Q", new BigDecimal("2"));

        String expectedNumberify = "2";
        Expression expectedNumberifyExpression = Expression.make(expectedNumberify);   
        Expression expectedRight = Expression.times(expectedNumberifyExpression, testFourth);

        Expression expected = Expression.times(testPlusFirst, expectedRight);
        Expression actual = testPlus.numberify(environment);
        assertTrue("numberified incorrectly", expected.equals(actual));
    }

    //covers case where all variables in left in environment
    //            some variables in right in environment
    @Test
    public void testNumberifyTimesAllLeftSomeRight(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);     
        char testInputThird = 'Q';
        Expression testThird = Expression.make(testInputThird);  
        char testInputFourth = 'v';
        Expression testFourth = Expression.make(testInputFourth); 
        Expression testPlusFirst = Expression.times(testFirst, testSecond);
        Expression testPlusSecond = Expression.times(testThird, testFourth);
        Expression testPlus = Expression.times(testPlusFirst, testPlusSecond);
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("G", new BigDecimal("2"));
        environment.put("A", new BigDecimal("6"));
        environment.put("Q", new BigDecimal("0"));

        String expectedNumberifyFirst = "2";
        Expression expectedNumberifyFirstExpression = Expression.make(expectedNumberifyFirst); 
        String expectedNumberifySecond = "6";
        Expression expectedNumberifySecondExpression = Expression.make(expectedNumberifySecond); 
        Expression expectedLeft = Expression.times(expectedNumberifyFirstExpression, expectedNumberifySecondExpression);
        String expectedNumberifyThird = "0";
        Expression expectedNumberifyThirdExpression = Expression.make(expectedNumberifyThird); 
        Expression expectedRight = Expression.times(expectedNumberifyThirdExpression, testFourth);

        Expression expected = Expression.times(expectedLeft, expectedRight);
        Expression actual = testPlus.numberify(environment);
        assertTrue("numberified incorrectly", expected.equals(actual));
    }

    //covers case where all variables in left in environment
    //            all variables in right in environment
    @Test
    public void testNumberifyTimesAllLeftAllRight(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);     
        char testInputThird = 'Q';
        Expression testThird = Expression.make(testInputThird);  
        char testInputFourth = 'v';
        Expression testFourth = Expression.make(testInputFourth); 
        Expression testPlusFirst = Expression.times(testFirst, testSecond);
        Expression testPlusSecond = Expression.times(testThird, testFourth);
        Expression testPlus = Expression.times(testPlusFirst, testPlusSecond);
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("G", new BigDecimal("2"));
        environment.put("A", new BigDecimal("213"));
        environment.put("Q", new BigDecimal("0"));
        environment.put("v", new BigDecimal("9"));
        String expectedNumberifyFirst = "2";
        Expression expectedNumberifyFirstExpression = Expression.make(expectedNumberifyFirst); 
        String expectedNumberifySecond = "213";
        Expression expectedNumberifySecondExpression = Expression.make(expectedNumberifySecond); 
        Expression expectedLeft = Expression.times(expectedNumberifyFirstExpression, expectedNumberifySecondExpression);
        String expectedNumberifyThird = "0";
        Expression expectedNumberifyThirdExpression = Expression.make(expectedNumberifyThird); 
        String expectedNumberifyFourth = "9";
        Expression expectedNumberifyFourthExpression = Expression.make(expectedNumberifyFourth); 
        Expression expectedRight = Expression.times(expectedNumberifyThirdExpression, expectedNumberifyFourthExpression);

        Expression expected = Expression.times(expectedLeft, expectedRight);
        Expression actual = testPlus.numberify(environment);
        assertTrue("numberified incorrectly", expected.equals(actual));
    }

    //covers case where no variables in left in environment
    //            no variables in right in environment

    @Test(expected=IllegalArgumentException.class)
    public void testEvaluateTimesNoLeftNoRight(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);     
        char testInputThird = 'Q';
        Expression testThird = Expression.make(testInputThird);   
        Expression testPlusFirst = Expression.times(testSecond, testThird);
        Expression testPlus = Expression.times(testFirst, testPlusFirst);
        BigDecimal actual = testPlus.evaluate();
    }

    //covers case where no variables in right in environment
    //            some variables in left in environment
    @Test(expected=IllegalArgumentException.class)
    public void testEvaluateTimesSomeLeftNoRight(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);     
        char testInputThird = 'Q';
        Expression testThird = Expression.make(testInputThird);   
        Expression testPlusFirst = Expression.times(testFirst, testSecond);
        Expression testPlus = Expression.times(testPlusFirst, testThird);
        BigDecimal actual = testPlus.evaluate();
    }

    //covers case where no variables in left in environment
    //            some variables in right in environment
    @Test(expected=IllegalArgumentException.class)
    public void testEvaluateTimesNoLeftSomeRight(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);     
        char testInputThird = 'Q';
        Expression testThird = Expression.make(testInputThird);  
        char testInputFourth = 'v';
        Expression testFourth = Expression.make(testInputFourth); 
        Expression testPlusFirst = Expression.times(testFirst, testSecond);
        Expression testPlusSecond = Expression.times(testThird, testFourth);
        Expression testPlus = Expression.times(testPlusFirst, testPlusSecond);
        BigDecimal actual = testPlus.evaluate();
    }

    //covers case where all variables in left in environment
    //            some variables in right in environment
    @Test(expected=IllegalArgumentException.class)
    public void testEvaluateTimesAllLeftSomeRight(){
        char testInputFirst = 'G';
        Expression testFirst = Expression.make(testInputFirst);                
        char testInputSecond = 'A';
        Expression testSecond = Expression.make(testInputSecond);     
        char testInputThird = 'Q';
        Expression testThird = Expression.make(testInputThird);  
        char testInputFourth = 'v';
        Expression testFourth = Expression.make(testInputFourth); 
        Expression testPlusFirst = Expression.times(testFirst, testSecond);
        Expression testPlusSecond = Expression.times(testThird, testFourth);
        Expression testPlus = Expression.times(testPlusFirst, testPlusSecond);
        BigDecimal actual = testPlus.evaluate();
    }

    //covers case where all variables in left in environment
    //            all variables in right in environment
    @Test
    public void testEvaluateTimesAllLeftAllRight(){
        String testInputFirst = "1.6";
        Expression testFirst = Expression.make(testInputFirst);                
        String testInputSecond = "9.1";
        Expression testSecond = Expression.make(testInputSecond);     
        String testInputThird = ".8";
        Expression testThird = Expression.make(testInputThird);  
        String testInputFourth = "12.34";
        Expression testFourth = Expression.make(testInputFourth); 
        Expression testPlusFirst = Expression.times(testFirst, testSecond);
        Expression testPlusSecond = Expression.times(testThird, testFourth);
        Expression testPlus = Expression.times(testPlusFirst, testPlusSecond);
        BigDecimal actual = testPlus.evaluate();
        BigDecimal expected = new BigDecimal("143.73632");
        assertTrue("evaluated incorrectly", expected.compareTo(actual) == 0);
    }
    //--------------------------------TESTING FOR TIMES ENDS----------------------------------//
    //--------------------------------TESTING FOR EXPONENT BEGINS----------------------------------//


    //covers case where base is a Expression
    //                  power = 0
    @Test
    public void testToStringExponentBaseNumberPowerZero(){
        String input = "3.34567";
        Expression base = Expression.make(input);
        Integer powerFirst = 0;
        Expression testExponent = Expression.power(base, powerFirst);
        String expected = "(3.34567)^0";
        String actual = testExponent.toString();
        assertTrue("Exponent string was not as expected", expected.equals(actual));
    }

    //covers case where base is a Variable
    //                  power = 1
    @Test
    public void testToStringExponentBaseVariablePowerOne(){
        char input = 'r';
        Expression base = Expression.make(input);
        Integer powerFirst = 1;
        Expression testExponent = Expression.power(base, powerFirst);
        String expected = "(r)^1";
        String actual = testExponent.toString();
        assertTrue("Exponent string was not as expected", expected.equals(actual));
    }


    //covers case where base is a Plus
    //                  power > 1
    @Test
    public void testToStringExponentBasePlusPowerGreaterThanOne(){
        char inputLeft = 'r';
        Expression baseLeft = Expression.make(inputLeft);
        char inputRight = 'p';
        Expression baseRight = Expression.make(inputRight);
        Expression base = Expression.add(baseLeft, baseRight);
        Integer powerFirst = 605;
        Expression testExponent = Expression.power(base, powerFirst);
        String expected = "((r)+(p))^605";
        String actual = testExponent.toString();
        assertTrue("Exponent string was not as expected", expected.equals(actual));
    }

    //covers case where base is a Times
    //                  power = 0
    @Test
    public void testToStringExponentBaseTimesPowerZero(){
        char inputLeft = 'r';
        Expression baseLeft = Expression.make(inputLeft);
        char inputRight = 'p';
        Expression baseRight = Expression.make(inputRight);
        Expression base = Expression.times(baseLeft, baseRight);
        Integer powerFirst = 605;
        Expression testExponent = Expression.power(base, powerFirst);
        String expected = "((r)*(p))^605";
        String actual = testExponent.toString();
        assertTrue("Exponent string was not as expected", expected.equals(actual));
    }

    //covers case where base is a Exponent
    //                  power = 1
    @Test
    public void testToStringExponentBaseExponentPowerOne(){
        char input = 'r';
        Expression innerBase = Expression.make(input);
        Integer innerPower = 4;
        Expression innerExponent = Expression.power(innerBase, innerPower);
        Integer power = 6031;
        Expression testExponent = Expression.power(innerExponent, power);
        String expected = "((r)^4)^6031";
        String actual = testExponent.toString();
        assertTrue("Exponent string was not as expected", expected.equals(actual));
    }


    //covers case where thatObject is not an Exponent object
    @Test 
    public void testEqualsExponentNotAnExponentObject(){
        char testVariableInput = 't';
        Expression testVariable = Expression.make(testVariableInput);
        Integer power = 6031;
        Expression testExponent = Expression.power(testVariable, power);
        assertFalse("Exponent should not equal Number object", testExponent.equals(testVariable));
    }

    //covers case where thatObject is an equivalent Exponent object 
    @Test 
    public void testEqualsExponentEquivalentSimple(){
        char testVariableInput = 't';
        Expression testVariable = Expression.make(testVariableInput);
        Integer power = 6031;
        Expression testExponent = Expression.power(testVariable, power);

        char testVariableInputDuplicate = 't';
        Expression testVariableDuplicate = Expression.make(testVariableInputDuplicate);
        Integer powerDuplicate = 6031;
        Expression testExponentDuplicate = Expression.power(testVariableDuplicate, powerDuplicate);

        assertTrue("Exponent objects should be equivalent", testExponent.equals(testExponentDuplicate));
    }

    //covers case where thatObject is an equivalent Exponent
    @Test
    public void testEqualsExponentEquivalentComplex(){
        char inputLeft = 'r';
        Expression baseLeft = Expression.make(inputLeft);
        char inputRight = 'p';
        Expression baseRight = Expression.make(inputRight);
        Expression base = Expression.times(baseLeft, baseRight);
        Integer power = 605;
        Expression testExponent = Expression.power(base, power);

        char inputLeftDuplicate = 'r';
        Expression baseLeftDuplicate = Expression.make(inputLeftDuplicate);
        char inputRightDuplicate = 'p';
        Expression baseRightDuplicate = Expression.make(inputRightDuplicate);
        Expression baseDuplicate = Expression.times(baseLeftDuplicate, baseRightDuplicate);
        Integer powerDuplicate = 605;
        Expression testExponentDuplicate = Expression.power(baseDuplicate, powerDuplicate);

        assertTrue("Exponent objects should be equivalent", testExponent.equals(testExponentDuplicate));
    }

    //covers case where thatObject is not an equivalent Exponent
    //                  has same powers, but different bases
    @Test
    public void testEqualsExponentNotEquivalentExponentsDifferentBase(){
        char inputLeft = 'A';
        Expression baseLeft = Expression.make(inputLeft);
        char inputRight = 'p';
        Expression baseRight = Expression.make(inputRight);
        Expression base = Expression.times(baseLeft, baseRight);
        Integer power = 605;
        Expression testExponent = Expression.power(base, power);

        char inputLeftDifferent = 'r';
        Expression baseLeftDifferent = Expression.make(inputLeftDifferent);
        char inputRightDifferent = 'p';
        Expression baseRightDifferent = Expression.make(inputRightDifferent);
        Expression baseDifferent = Expression.times(baseLeftDifferent, baseRightDifferent);
        Integer powerDifferent = 6031;
        Expression testExponentDifferent = Expression.power(baseDifferent, powerDifferent);

        assertFalse("Exponent objects should not be equivalent", testExponent.equals(testExponentDifferent));
    }

    //covers case where thatObject is not an equivalent Exponent
    //                  has same base, but different powers
    @Test
    public void testEqualsExponentNotEquivalentExponentsDifferentPower(){
        char inputLeft = 'A';
        Expression baseLeft = Expression.make(inputLeft);
        char inputRight = 'p';
        Expression baseRight = Expression.make(inputRight);
        Expression base = Expression.times(baseLeft, baseRight);
        Integer power = 6031;
        Expression testExponent = Expression.power(base, power);

        char inputLeftDifferent = 'r';
        Expression baseLeftDifferent = Expression.make(inputLeftDifferent);
        char inputRightDifferent = 'p';
        Expression baseRightDifferent = Expression.make(inputRightDifferent);
        Expression baseDifferent = Expression.times(baseLeftDifferent, baseRightDifferent);
        Integer powerDifferent = 6031;
        Expression testExponentDifferent = Expression.power(baseDifferent, powerDifferent);

        assertFalse("Exponent objects should not be equivalent", testExponent.equals(testExponentDifferent));
    }

    //covers case where thatObject is not an equivalent Exponent
    @Test
    public void testEqualsExponentNotEquivalentExponentsJustDifferent(){
        char inputLeft = 'Z';
        Expression baseLeft = Expression.make(inputLeft);
        char inputRight = 'p';
        Expression baseRight = Expression.make(inputRight);
        Expression base = Expression.times(baseLeft, baseRight);
        Integer power = 31;
        Expression testExponent = Expression.power(base, power);

        char inputLeftDifferent = 'r';
        Expression baseLeftDifferent = Expression.make(inputLeftDifferent);
        char inputRightDifferent = 'R';
        Expression baseRightDifferent = Expression.make(inputRightDifferent);
        Expression baseDifferent = Expression.times(baseLeftDifferent, baseRightDifferent);
        Integer powerDifferent = 605;
        Expression testExponentDifferent = Expression.power(baseDifferent, powerDifferent);

        assertFalse("Exponent objects should not be equivalent", testExponent.equals(testExponentDifferent));
    }

    //covers case where two equivalent Exponent objects have same hashCode
    @Test
    public void testHashCodeExponentSimple(){
        char testVariableInput = 't';
        Expression testVariable = Expression.make(testVariableInput);
        Integer power = 6031;
        Expression testExponent = Expression.power(testVariable, power);

        char testVariableInputDuplicate = 't';
        Expression testVariableDuplicate = Expression.make(testVariableInputDuplicate);
        Integer powerDuplicate = 6031;
        Expression testExponentDuplicate = Expression.power(testVariableDuplicate, powerDuplicate);

        int hashCodeExponent = testExponent.hashCode();
        int hashCodeExponentDuplicate = testExponentDuplicate.hashCode();
        assertEquals("Equivalent Exponent objects should have equivalent hashCodes", hashCodeExponent, hashCodeExponentDuplicate);
    }

    //covers case where two equivalent Exponent objects have same hashCode
    @Test
    public void testHashCodeExponentComplex(){
        char inputLeft = 'r';
        Expression baseLeft = Expression.make(inputLeft);
        char inputRight = 'p';
        Expression baseRight = Expression.make(inputRight);
        Expression base = Expression.times(baseLeft, baseRight);
        Integer power = 605;
        Expression testExponent = Expression.power(base, power);

        char inputLeftDuplicate = 'r';
        Expression baseLeftDuplicate = Expression.make(inputLeftDuplicate);
        char inputRightDuplicate = 'p';
        Expression baseRightDuplicate = Expression.make(inputRightDuplicate);
        Expression baseDuplicate = Expression.times(baseLeftDuplicate, baseRightDuplicate);
        Integer powerDuplicate = 605;
        Expression testExponentDuplicate = Expression.power(baseDuplicate, powerDuplicate);

        int hashCodeExponent = testExponent.hashCode();
        int hashCodeExponentDuplicate = testExponentDuplicate.hashCode();
        assertEquals("Equivalent Exponent objects should have equivalent hashCodes", hashCodeExponent, hashCodeExponentDuplicate);
    }

    //covers case where variable is not a variable in base
    @Test
    public void testDifferentiateExponentVariableNotBase(){
        char input= 'Z';
        Expression base = Expression.make(input);
        Integer power = 31;
        Expression testExponent = Expression.power(base, power);
        Expression testDerivative = testExponent.differentiate('a');
        Expression expected = Expression.parse("(31*(Z^30))*0");
        assertTrue("Derivative was not as expected", testDerivative.equals(expected));

        char inputLeft = 'Z';
        Expression baseLeft = Expression.make(inputLeft);
        char inputRight = 'p';
        Expression baseRight = Expression.make(inputRight);
        Expression baseTest = Expression.times(baseLeft, baseRight);
        Integer powerTest = 31;
        Expression testExponentSecond = Expression.power(baseTest, powerTest);
        Expression testDerivativeSecond = testExponentSecond.differentiate('a');
        Expression expectedSecond = Expression.parse("(31*((Z*p)^30))*((p*0)+(Z*0))");
        assertTrue("Derivative was not as expected", testDerivativeSecond.equals(expectedSecond));
    }

    //covers case where variable is the only variable in base
    //            power > 0
    @Test
    public void testDifferentiateExponentVariableOnlyBase(){
        char input= 'w';
        Expression base = Expression.make(input);
        Integer power = 31;
        Expression testExponent = Expression.power(base, power);
        Expression testDerivative = testExponent.differentiate('w');
        Expression expected = Expression.parse("((31)*((w)^30))*(1)");
        assertTrue("Derivative was not as expected", testDerivative.equals(expected));

        char inputLeft = 'Z';
        Expression baseLeft = Expression.make(inputLeft);
        char inputRight = 'Z';
        Expression baseRight = Expression.make(inputRight);
        Expression baseTest = Expression.times(baseLeft, baseRight);
        Integer powerTest = 31;
        Expression testExponentSecond = Expression.power(baseTest, powerTest);
        Expression testDerivativeSecond = testExponentSecond.differentiate('Z');
        Expression expectedSecond = Expression.parse("(31*((Z*Z)^30))*((Z*1)+(Z*1))");
        assertTrue("Derivative was not as expected", testDerivativeSecond.equals(expectedSecond));
    }

    //covers case where variable is one of many variables in base
    @Test
    public void testDifferentiateExponentVariablesManyBase(){
        char inputLeft = 'Z';
        Expression baseLeft = Expression.make(inputLeft);
        char inputRight = 'A';
        Expression baseRight = Expression.make(inputRight);
        Expression baseTest = Expression.times(baseLeft, baseRight);
        Integer powerTest = 31;
        Expression testExponentSecond = Expression.power(baseTest, powerTest);
        Expression testDerivativeSecond = testExponentSecond.differentiate('Z');
        Expression expectedSecond = Expression.parse("(((31)*(Z*A)^30))*((A*1)+(Z*0))");
        assertTrue("Derivative was not as expected", testDerivativeSecond.equals(expectedSecond));
    }

    //covers case where power = 0
    @Test
    public void testDifferentiateExponentPowerZero(){
        char input= 'w';
        Expression base = Expression.make(input);
        Integer power = 0;
        Expression testExponent = Expression.power(base, power);
        Expression testDerivative = testExponent.differentiate('w');
        Expression expected = Expression.make("0");
        assertTrue("Derivative was not as expected", testDerivative.equals(expected));
    }

    //covers case where power = 0
    @Test
    public void testDifferentiateExponentPowerZeroDifferentExponent(){
        char input= 'w';
        Expression base = Expression.make(input);
        Integer power = 0;
        Expression testExponent = Expression.power(base, power);
        Expression testDerivative = testExponent.differentiate('p');
        Expression expected = Expression.make("0");
        assertTrue("Derivative was not as expected", testDerivative.equals(expected));
    }


    //covers case where no variables in base in environment
    @Test
    public void testCanEvaluateExponentVariableNotBase(){
        Map<String,BigDecimal> environment = new HashMap<>();

        char input= 'Z';
        Expression base = Expression.make(input);
        Integer power = 31;
        Expression testExponent = Expression.power(base, power);
        boolean evaluate = testExponent.canEvaluate(environment);
        assertFalse("should not be able to evaluate", evaluate);

        char inputLeft = 'Z';
        Expression baseLeft = Expression.make(inputLeft);
        char inputRight = 'p';
        Expression baseRight = Expression.make(inputRight);
        Expression baseTest = Expression.times(baseLeft, baseRight);
        Integer powerTest = 31;
        Expression testExponentSecond = Expression.power(baseTest, powerTest);
        boolean evaluateTwo = testExponentSecond.canEvaluate(environment);
        assertFalse("should not be able to evaluate", evaluateTwo);

    }

    //covers case where all variables in base in environment
    @Test
    public void testCanEvaluateExponentVariableOnlyBase(){
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("w", new BigDecimal("12.2"));
        environment.put("Z", new BigDecimal("112.2"));
        char input= 'w';
        Expression base = Expression.make(input);
        Integer power = 31;
        Expression testExponent = Expression.power(base, power);
        boolean evaluate = testExponent.canEvaluate(environment);
        assertTrue("should be able to evaluate", evaluate);

        char inputLeft = 'Z';
        Expression baseLeft = Expression.make(inputLeft);
        char inputRight = 'Z';
        Expression baseRight = Expression.make(inputRight);
        Expression baseTest = Expression.times(baseLeft, baseRight);
        Integer powerTest = 31;
        Expression testExponentSecond = Expression.power(baseTest, powerTest);
        boolean evaluateTwo = testExponentSecond.canEvaluate(environment);
        assertTrue("should be able to evaluate", evaluateTwo);
    }

    //covers case where some variables in base in environment
    @Test
    public void testCanExponentVariablesManyBase(){
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("w", new BigDecimal("12.2"));
        environment.put("Z", new BigDecimal("112.2"));
        char inputLeft = 'Z';
        Expression baseLeft = Expression.make(inputLeft);
        char inputRight = 'A';
        Expression baseRight = Expression.make(inputRight);
        Expression baseTest = Expression.times(baseLeft, baseRight);
        Integer powerTest = 31;
        Expression testExponentSecond = Expression.power(baseTest, powerTest);
        boolean evaluateTwo = testExponentSecond.canEvaluate(environment);
        assertFalse("should not be able to evaluate", evaluateTwo);    
    }

    //covers case where power = 0
    @Test
    public void testCanExponentExponentPowerZero(){
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("w", new BigDecimal("12.2"));
        char input= 'w';
        Expression base = Expression.make(input);
        Integer power = 0;
        Expression testExponent = Expression.power(base, power);
        boolean evaluate = testExponent.canEvaluate(environment);
        assertTrue("should be able to evaluate", evaluate);    
    }

    //covers case where no variables in base in environment
    @Test
    public void testNumberifyExponentVariableNotBase(){
        Map<String,BigDecimal> environment = new HashMap<>();

        char input= 'Z';
        Expression base = Expression.make(input);
        Integer power = 31;
        Expression testExponent = Expression.power(base, power);
        Expression numberified = testExponent.numberify(environment);
        assertTrue("numberified not as expected", numberified.equals(testExponent));

        char inputLeft = 'Z';
        Expression baseLeft = Expression.make(inputLeft);
        char inputRight = 'p';
        Expression baseRight = Expression.make(inputRight);
        Expression baseTest = Expression.times(baseLeft, baseRight);
        Integer powerTest = 31;
        Expression testExponentSecond = Expression.power(baseTest, powerTest);
        Expression numberifiedTwo = testExponentSecond.numberify(environment);
        assertTrue("numberified not as expected", numberifiedTwo.equals(testExponentSecond));

    }

    //covers case where all variables in base in environment
    @Test
    public void testNumberifyExponentVariableOnlyBase(){
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("w", new BigDecimal("12.2"));
        environment.put("Z", new BigDecimal("112.2"));
        char input= 'w';
        Expression base = Expression.make(input);
        Integer power = 31;
        Expression testExponent = Expression.power(base, power);
        Expression numberified = testExponent.numberify(environment);
        Expression expectedBase = Expression.make("12.2");
        Expression expected = Expression.power(expectedBase, power);
        assertTrue("numberified not as expected", numberified.equals(expected));

        char inputLeft = 'Z';
        Expression baseLeft = Expression.make(inputLeft);
        char inputRight = 'Z';
        Expression baseRight = Expression.make(inputRight);
        Expression baseTest = Expression.times(baseLeft, baseRight);
        Integer powerTest = 31;
        Expression testExponentSecond = Expression.power(baseTest, powerTest);
        Expression numberifiedSecond = testExponentSecond.numberify(environment);
        
        Expression expectedBaseLeft = Expression.make("112.2");
        Expression expectedBaseRight = Expression.make("112.2");
        Expression expectedBaseSecond = Expression.times(expectedBaseLeft, expectedBaseRight);
        Expression expectedSecond = Expression.power(expectedBaseSecond, power);
        assertTrue("numberified not as expected", numberifiedSecond.equals(expectedSecond));
    }

    //covers case where some variables in base in environment
    @Test
    public void testNumberifyVariablesManyBase(){
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("w", new BigDecimal("12.2"));
        environment.put("Z", new BigDecimal("112.2"));
        char inputLeft = 'Z';
        Expression baseLeft = Expression.make(inputLeft);
        char inputRight = 'A';
        Expression baseRight = Expression.make(inputRight);
        Expression baseTest = Expression.times(baseLeft, baseRight);
        Integer powerTest = 31;
        Expression testExponentSecond = Expression.power(baseTest, powerTest);
        Expression numberifiedSecond = testExponentSecond.numberify(environment);
        
        Expression expectedBaseLeft = Expression.make("112.2");
        Expression expectedBaseRight = baseRight;
        Expression expectedBaseSecond = Expression.times(expectedBaseLeft, expectedBaseRight);
        Expression expectedSecond = Expression.power(expectedBaseSecond, powerTest);
        assertTrue("numberified not as expected", numberifiedSecond.equals(expectedSecond));

    }

    //covers case where power = 0
    @Test
    public void testNumberifyExponentPowerZero(){
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("w", new BigDecimal("12.2"));
        char input= 'w';
        Expression base = Expression.make(input);
        Integer power = 0;
        Expression testExponent = Expression.power(base, power);
        Expression expectedBase = Expression.make("12.2");
        Expression expected = Expression.power(expectedBase, power);

        Expression evaluated = testExponent.numberify(environment);
        assertTrue("numberified not as expected", evaluated.equals(expected));
    }

    //--------------------------------TESTING FOR EXPONENT ENDS----------------------------------//




}
