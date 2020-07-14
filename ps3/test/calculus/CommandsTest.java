/* Copyright (c) 2015-2017 MIT 6.005/6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package calculus;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * Tests for the static methods of Commands.
 */
public class CommandsTest {

    /* Testing strategy for CommandsTest:
     * ============================================================== 
     * Commands.evaluate(String expression, Map<String,BigDecimal> environment)
     *      - expression
     *          - numbers only
     *          - variables only
     *          - has sum term
     *          - has product term
     *          - has exponent term  
     *          - has parentheses
     *          - is invalid
     *      - environment
     *          - size = 0
     *          - size = 1
     *          - size > 1
     *      - relationship between expression and environment
     *          - environment contains all variables in expression
     *          - environment contains some variables in expression
     *          - environment contains no variables in expression
     *  ============================================================== 
     * Commands.differentiate(String expression, String variable):
     *      - expression:
     *          - has number (0, 1, >1)
     *          - has variable (uppercase, lower case)
     *              - c == variable
     *              - c != variable
     *                  - upper case vs. lower case
     *          - has sum term
     *              - left-hand side:
     *                  - variable is not a variable in left
     *                  - variable is the only variable in left
     *                  - variable is one of many (>1) variables in left
     *              - right-hand side:
     *                  - variable is not a variable in right
     *                  - variable is the only variable in right
     *                  - variable is one of many (>1) variables in right        
     *          - has product term
     *              - left-hand side:
     *                  - variable is not a variable in left
     *                  - variable is the only variable in left
     *                  - variable is one of many (>1) variables in left
     *              - right-hand side:
     *              - variable is not a variable in right
     *              - variable is the only variable in right
     *              - variable is one of many (>1) variables in right  
     *          - has exponent:
     *               - power:
     *                  - 0
     *                  - >0
     *               - base: 
     *                   - c in base
     *                   - c not in base
     *          - invalid
     *      - variable
     *          - is NOT in the expression
     *          - is in the expression 1 time
     *          - is in the expression > 1 times
     *          - is invalid
     *              - length = 0
     *              - length > 1
     *              - not a letter
     *    
     */


    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    //covers case where value of number = 0
    @Test 
    public void testDifferentiateNumberIsZero(){     
        String testNumberInputFirst = "0";
        String testNumberInputSecond = "0.00000";
        String expected = "0";
        String actualFirst = Commands.differentiate(testNumberInputFirst, "A");
        String actualSecond = Commands.differentiate(testNumberInputSecond, "A");
        assertTrue("Differentiation of constant should return 0", expected.equals(actualFirst));
        assertTrue("Differentiation of constant should return 0", expected.equals(actualSecond));
    }

    //covers case where value of number = 1
    @Test 
    public void testDifferentiateNumberIsOne(){     
        String testNumberInputFirst = "1";
        String testNumberInputSecond = "1.0000000";
        String expected = "0";
        String actualFirst = Commands.differentiate(testNumberInputFirst, "A");
        String actualSecond = Commands.differentiate(testNumberInputSecond, "A");
        assertTrue("Differentiation of constant should return 0", expected.equals(actualFirst));
        assertTrue("Differentiation of constant should return 0", expected.equals(actualSecond));
    }

    //covers case where value of number > 1
    @Test 
    public void testDifferentiateNumberIsGreaterThanOne(){     
        String testNumberInputFirst = "12.12345";
        String testNumberInputSecond = "12";
        String expected = "0";
        String actualFirst = Commands.differentiate(testNumberInputFirst, "A");
        String actualSecond = Commands.differentiate(testNumberInputSecond, "A");
        assertTrue("Differentiation of constant should return 0", expected.equals(actualFirst));
        assertTrue("Differentiation of constant should return 0", expected.equals(actualSecond));
    }

    //covers case where value of c == variable
    @Test 
    public void testDifferentiateVariableEquals(){     
        String testFirst = "v";
        String testSecond = "A";
        String actualFirst = Commands.differentiate(testFirst, "v");
        String actualSecond = Commands.differentiate(testSecond, "A");
        String expected = "1";
        assertTrue("Differentiation of constant should return 1", expected.equals(actualFirst));
        assertTrue("Differentiation of constant should return 1", expected.equals(actualSecond));
    }

    //covers case where value of c != variable
    @Test 
    public void testDifferentiateVariableNotEqual(){     
        String testFirst = "v";
        String testSecond = "A";
        String actualFirst = Commands.differentiate(testFirst, "q");
        String actualSecond = Commands.differentiate(testSecond, "w");
        String expected = "0";
        assertTrue("Differentiation of constant should return 0", expected.equals(actualFirst));
        assertTrue("Differentiation of constant should return 0", expected.equals(actualSecond));
    }

    //covers case where value of c != variable, upper case vs lower case
    @Test 
    public void testDifferentiateVariableNotEqualCaseSensitive(){     
        String testFirst = "v";
        String testSecond = "A";
        String actualFirst = Commands.differentiate(testFirst, "V");
        String actualSecond = Commands.differentiate(testSecond, "a");
        String expected = "0";
        assertTrue("Differentiation of constant should return 0", expected.equals(actualFirst));
        assertTrue("Differentiation of constant should return 0", expected.equals(actualSecond));
    }

    //covers case variable is not a variable in left
    //            variable is not a variable in right
    @Test
    public void testDifferentiatePlusVariableNotLeftNotRight(){
        String input = "G+232423";
        String actual = Commands.differentiate(input, "V");
        String expected = "(0)+(0)";                
        assertTrue("Differentiation was not as expected", actual.equals(expected));
    }

    //covers case variable is a variable in left
    //            variable is not a variable in right
    @Test
    public void testDifferentiatePlusVariableLeftNotRight(){
        String input = "G+232423";
        String actual = Commands.differentiate(input, "G");
        String expected = "(1)+(0)";                
        assertTrue("Differentiation was not as expected", actual.equals(expected));
    }

    //covers case variable is not a variable in left
    //            variable is a variable in right
    @Test
    public void testDifferentiatePlusVariableNotLeftRight(){
        String input = "232423+G";
        String actual = Commands.differentiate(input, "G");
        String expected = "(0)+(1)";                
        assertTrue("Differentiation was not as expected", actual.equals(expected));
    }

    //covers case variable is the only variable in left
    //            variable is one of many (>1) variables in right
    @Test
    public void testDifferentiatePlusVariableLeftOnlyRightMany(){
        String input = "(G+6)+(G+G)";
        String actual = Commands.differentiate(input, "G");
        String expected = "((1)+(0))+((1)+(1))";             
        assertTrue("Differentiation was not as expected", actual.equals(expected));
    }

    //covers case variable is the only variable in right
    //            variable is one of many (>1) variables in left
    @Test
    public void testDifferentiatePlusVariableRightOnlyLeftMany(){
        String input = "(G+G)+(G+6)";
        String actual = Commands.differentiate(input, "G");
        String expected = "((1)+(1))+((1)+(0))";                           
        assertTrue("Differentiation was not as expected", actual.equals(expected));
    }

    //covers case variable is not a variable in left
    //            variable is not a variable in right
    @Test
    public void testDifferentiateTimesVariableNotLeftNotRight(){
        String input = "232423*G"; 
        String actual = Commands.differentiate(input, "a");
        String expected = "((G)*(0))+((232423)*(0))"; 
        assertTrue("Differentiation was not as expected", actual.equals(expected));
    }

    //covers case variable is a variable in left
    //            variable is not a variable in right
    @Test
    public void testDifferentiateTimesVariableLeftNotRight(){
        String input = "G*232423.1111"; 
        String actual = Commands.differentiate(input, "G");
        String expected = "((232423.1111)*(1))+((G)*(0))"; 
        assertTrue("Differentiation was not as expected", actual.equals(expected));  
    }

    //covers case variable is not a variable in left
    //            variable is a variable in right
    @Test
    public void testDifferentiateTimesVariableNotLeftRight(){
        String input = "232423.1111*G"; 
        String actual = Commands.differentiate(input, "G");
        String expected = "((G)*(0))+((232423.1111)*(1))"; 
        assertTrue("Differentiation was not as expected", actual.equals(expected));
    }

    //covers case variable is the only variable in left
    //            variable is one of many (>1) variables in right
    @Test
    public void testDifferentiateTimesVariableLeftOnlyRightMany(){
        String input = "(G*6)*(G*G)"; 
        String actual = Commands.differentiate(input, "G");
        String expected = "(((G)*(G))*(((6)*(1))+((G)*(0))))+(((G)*(6))*(((G)*(1))+((G)*(1))))"; 
        assertTrue("Differentiation was not as expected", actual.equals(expected));
    }

    //covers case variable is the only variable in right
    //            variable is one of many (>1) variables in left
    @Test
    public void testDifferentiateTimesVariableRightOnlyLeftMany(){
        String input = "(G*G)*(G*6)"; 
        String actual = Commands.differentiate(input, "G");
        String expected = "(((G)*(6))*(((G)*(1))+((G)*(1))))+(((G)*(G))*(((6)*(1))+((G)*(0))))";    
        assertTrue("Differentiation was not as expected", actual.equals(expected));
    }

    //covers case where variable is not a variable in base
    @Test
    public void testDifferentiateExponentVariableNotBase(){
        String input = "Z^31"; 
        String actual = Commands.differentiate(input, "a");
        String expected = "((31)*((Z)^30))*(0)";    
        assertTrue("Differentiation was not as expected", actual.equals(expected));

        String inputTwo = "(Z*p)^31"; 
        String actualTwo = Commands.differentiate(inputTwo, "a");
        String expectedTwo = "((31)*(((Z)*(p))^30))*(((p)*(0))+((Z)*(0)))";    
        assertTrue("Differentiation was not as expected"+actualTwo, actualTwo.equals(expectedTwo));
    }

    //covers case where variable is the only variable in base
    //            power > 0
    @Test
    public void testDifferentiateExponentVariableOnlyBase(){
        String input = "w^31"; 
        String actual = Commands.differentiate(input, "w");
        String expected = "((31)*((w)^30))*(1)";    
        assertTrue("Differentiation was not as expected", actual.equals(expected));

        String inputTwo = "(Z*Z)^31"; 
        String actualTwo = Commands.differentiate(inputTwo, "Z");
        String expectedTwo = "((31)*(((Z)*(Z))^30))*(((Z)*(1))+((Z)*(1)))";    
        assertTrue("Differentiation was not as expected", actualTwo.equals(expectedTwo));
    }

    //covers case where variable is one of many variables in base
    @Test
    public void testDifferentiateExponentVariablesManyBase(){
        String input = "(Z*A)^31^2"; 
        String actual = Commands.differentiate(input, "w");
        String expected = "((2)*((((Z)*(A))^31)^1))*(((31)*(((Z)*(A))^30))*(((A)*(0))+((Z)*(0))))";    
        assertTrue("Differentiation was not as expected", actual.equals(expected));

        String inputTwo = "(Z*A)^31"; 
        String actualTwo = Commands.differentiate(inputTwo, "Z");
        String expectedTwo = "((31)*(((Z)*(A))^30))*(((A)*(1))+((Z)*(0)))";    
        assertTrue("Differentiation was not as expected", actualTwo.equals(expectedTwo));
    }

    //covers case where power = 0
    @Test
    public void testDifferentiateExponentPowerZero(){
        String input = "w^0"; 
        String actual = Commands.differentiate(input, "w");
        String expected = "0";    
        assertTrue("Differentiation was not as expected", actual.equals(expected));
    }
    
    //covers case where power = 0
    @Test
    public void testDifferentiateExponentPowerZeroDifferentExponent(){
        String input = "w^0"; 
        String actual = Commands.differentiate(input, "p");
        String expected = "0";    
        assertTrue("Differentiation was not as expected", actual.equals(expected));
    }

    //covers case where expression is invalid
    @Test(expected=IllegalArgumentException.class)
    public void testDifferentiateInvalidExpression(){
        String input = "w^9.21"; 
        String actual = Commands.differentiate(input, "p");
    }

    //covers case where variable length = 0
    @Test(expected=IllegalArgumentException.class)
    public void testDifferentiateInvalidVariableLengthZero(){
        String input = "w^9"; 
        String actual = Commands.differentiate(input, "");
    }

    //covers case where variable length > 1
    @Test(expected=IllegalArgumentException.class)
    public void testDifferentiateInvalidVariableLengthGreaterThanOne(){
        String input = "w^7"; 
        String actual = Commands.differentiate(input, "cscscs");
    }

    //covers case where variable not a letter
    @Test(expected=IllegalArgumentException.class)
    public void testDifferentiateInvalidVariableNotLetter(){
        String input = "w^9"; 
        String actual = Commands.differentiate(input, "1");
    }
    
    //covers case expression is a number only
    @Test
    public void testEvaluateNumberOnly(){
        String input = "1"; 
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("a", new BigDecimal("2"));
        String actual = Commands.evaluate(input, environment);
        String expected = "1";
        assertTrue("evaluated string not expected", actual.equals(expected));
    }
    
    //covers case expression is a variable only
    //            environment size = 1
    //            environment contains all variables in expression
    @Test
    public void testEvaluateVariableOnly(){
        String input = "a"; 
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("a", new BigDecimal("2"));
        String actual = Commands.evaluate(input, environment);
        String expected = "2";
        assertTrue("evaluated string not expected", actual.equals(expected));
    }
    
    //covers case expression has sum term
    //            environment size > 1
    //            environment contains some variables in expression
    @Test
    public void testEvaluateSumSomeVariables(){
        String input = "a + 2 + q + 1 + g"; 
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("a", new BigDecimal("2"));
        environment.put("q", new BigDecimal("2"));
        String actual = Commands.evaluate(input, environment);
        String expected = "((((2)+(2))+(2))+(1))+(g)";
        assertTrue("evaluated string not expected", actual.equals(expected));
    }
    
    //covers case expression has sum term
    //            environment size > 1
    //            environment contains all variables in expression
    @Test
    public void testEvaluateSumAllVariables(){
        String input = "a + 2 + q + 1 + g"; 
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("a", new BigDecimal("2"));
        environment.put("g", new BigDecimal("1"));
        environment.put("q", new BigDecimal("7"));
        String actual = Commands.evaluate(input, environment);
        String expected = "13";
        assertTrue("evaluated string not expected", actual.equals(expected));
    }
    
    //covers case expression has sum term
    //            environment size > 1
    //            environment contains no variables in expression
    @Test
    public void testEvaluateSumNoVariables(){
        String input = "a + 2 + q + 1 + g"; 
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("z", new BigDecimal("2"));
        environment.put("x", new BigDecimal("1"));
        environment.put("e", new BigDecimal("7"));
        String actual = Commands.evaluate(input, environment);
        String expected = "((((a)+(2))+(q))+(1))+(g)";
        assertTrue("evaluated string not expected", actual.equals(expected));
    }
    
    //covers case expression has product term
    //            environment size > 1
    //            environment contains some variables in expression
    @Test
    public void testEvaluateMultiplicationSomeVariables(){
        String input = "a * 2 * q * (1 + g)"; 
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("a", new BigDecimal("2"));
        environment.put("q", new BigDecimal("6"));
        String actual = Commands.evaluate(input, environment);
        String expected = "(((2)*(2))*(6))*((1)+(g))";
        assertTrue("evaluated string not expected"+actual, actual.equals(expected));
    }
    
    //covers case expression has product term
    //            environment size > 1
    //            environment contains all variables in expression
    @Test
    public void testEvaluateMultiplicationAllVariables(){
        String input = "a * (2 + q) + 17 * g"; 
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("a", new BigDecimal("2"));
        environment.put("g", new BigDecimal("1"));
        environment.put("q", new BigDecimal("7"));
        String actual = Commands.evaluate(input, environment);
        String expected = "35";
        assertTrue("evaluated string not expected", actual.equals(expected));
    }
    
    //covers case expression has product term
    //            environment size > 1
    //            environment contains no variables in expression
    @Test
    public void testEvaluateMultiplicationNoVariables(){
        String input = "a * (2 + q) * 1 * g"; 
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("z", new BigDecimal("2"));
        environment.put("x", new BigDecimal("1"));
        environment.put("e", new BigDecimal("7"));
        String actual = Commands.evaluate(input, environment);
        String expected = "(((a)*((2)+(q)))*(1))*(g)";
        assertTrue("evaluated string not expected"+actual, actual.equals(expected));
    }
    
    //covers case expression has product term
    //            environment size = 0
    //            environment contains no variables in expression
    @Test
    public void testEvaluateMultiplicationEmptyMap(){
        String input = "a * (2 + q) * 1 * g"; 
        Map<String,BigDecimal> environment = new HashMap<>();
        String actual = Commands.evaluate(input, environment);
        String expected = "(((a)*((2)+(q)))*(1))*(g)";
        assertTrue("evaluated string not expected", actual.equals(expected));
    }
    
    //covers case expression has exponent term
    //            environment size > 1
    //            environment contains some variables in expression
    @Test
    public void testEvaluateExponentSomeVariables(){
        String input = "a ^ 2 * q + (g ^ 12)"; 
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("a", new BigDecimal("2"));
        environment.put("q", new BigDecimal("6"));
        String actual = Commands.evaluate(input, environment);
        String expected = "(((2)^2)*(6))+((g)^12)";
        assertTrue("evaluated string not expected", actual.equals(expected));
    }
    
    //covers case expression has exponent term
    //            environment size > 1
    //            environment contains all variables in expression
    @Test
    public void testEvaluateExponentAllVariables(){
        String input = "a * (2.123456789 + q) + q ^ 0 * g + a"; 
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("a", new BigDecimal("2"));
        environment.put("g", new BigDecimal("1"));
        environment.put("q", new BigDecimal("7"));
        String actual = Commands.evaluate(input, environment);
        String expected = "21.246913578";
        assertTrue("evaluated string not expected"+actual, actual.equals(expected));
    }
    
    //covers case expression has exponent term
    //            environment size > 1
    //            environment contains no variables in expression
    @Test
    public void testEvaluateExponentNoVariables(){
        String input = "a * (q ^ 2) * 1 * g ^ 8"; 
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("z", new BigDecimal("2"));
        environment.put("x", new BigDecimal("1"));
        environment.put("e", new BigDecimal("7"));
        String actual = Commands.evaluate(input, environment);
        String expected = "(((a)*((q)^2))*(1))*((g)^8)";
        assertTrue("evaluated string not expected", actual.equals(expected));
    }
    
    //covers case expression has parentheses 
    //            environment size > 1
    //            environment contains some variables in expression
    @Test
    public void testEvaluateParenthesesSomeVariables(){
        String input = "(a + 2) * q * (1 + g)"; 
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("a", new BigDecimal("2"));
        environment.put("q", new BigDecimal("6"));
        String actual = Commands.evaluate(input, environment);
        String expected = "(((2)+(2))*(6))*((1)+(g))";
        assertTrue("evaluated string not expected", actual.equals(expected));
    }
    
    //covers case expression has parentheses
    //            environment size > 1
    //            environment contains all variables in expression
    @Test
    public void testEvaluateParenthesesAllVariables(){
        String input = "((((a * (2 + ((((q))))) + 17)))) * g"; 
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("a", new BigDecimal("2"));
        environment.put("g", new BigDecimal("1"));
        environment.put("q", new BigDecimal("7"));
        String actual = Commands.evaluate(input, environment);
        String expected = "35";
        assertTrue("evaluated string not expected", actual.equals(expected));
    }
    
    //covers case expression has parentheses
    //            environment size > 1
    //            environment contains no variables in expression
    @Test
    public void testEvaluateParenthesesNoVariables(){
        String input = "(((a * (2 + ((q))) * (1 + g))))"; 
        Map<String,BigDecimal> environment = new HashMap<>();
        environment.put("z", new BigDecimal("2"));
        environment.put("x", new BigDecimal("1"));
        environment.put("e", new BigDecimal("7"));
        String actual = Commands.evaluate(input, environment);
        String expected = "((a)*((2)+(q)))*((1)+(g))";
        assertTrue("evaluated string not expected", actual.equals(expected));
    }
    
    //covers case expression is invalid
    @Test(expected=IllegalArgumentException.class)
    public void testEvaluateInvalidExpression(){
        String input = "(((a * (2 + ((qqsaywhutmate))) * (1 + g))))"; 
        Map<String,BigDecimal> environment = new HashMap<>();
        String actual = Commands.evaluate(input, environment);
    }
}
