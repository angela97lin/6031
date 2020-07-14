/* Copyright (c) 2015-2017 MIT 6.005/6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package calculus;

import java.math.BigDecimal;
import java.util.Map;


/**
 * String-based commands provided by the expression system.
 * 
 * <p>PS3 instructions: this is a required class.
 * You MUST NOT change its name or package or the names or type signatures of existing methods.
 * You MUST NOT add fields, constructors, or instance methods.
 * You may, however, add additional static methods, or strengthen the specs of existing methods.
 */
public class Commands {
    
    /**
     * Differentiate an expression with respect to a variable.
     * @param expression the expression to differentiate
     * @param variable the variable to differentiate by, a single case-sensitive letter [A-Za-z].
     * @return expression's derivative with respect to variable. 
     *         returned expression string is formatted so that all operation pairs (ex: (a+b) ), numbers, and variables
     *         are parenthesized. Does not simplify any operations.
     *         For example: 232423*G differentiated with respect to 'a' returns ((G)*(0))+((232423)*(0))
     *                      (G*6)*(G*G) differentiated with respect to 'G' returns (((G)*(G))*(((6)*(1))+((G)*(0))))+(((G)*(6))*(((G)*(1))+((G)*(1))))
     *                      Z^31 differentiated with respect to 'a' returns ((31)*((Z)^30))*(0)
     * @throws IllegalArgumentException if the expression or variable is invalid (not a letter, or length != 1)
     */
    public static String differentiate(String expression, String variable) throws IllegalArgumentException {

        if (variable.length() == 0){
            throw new IllegalArgumentException("variable cannot be empty string");
        }   
        char variableChar = variable.charAt(0); //used to make sure is letter
        boolean isLetter = Character.isLetter(variableChar);
        if (variable.length() > 1 || !isLetter) {
            throw new IllegalArgumentException("variable invalid");
        }
        Expression parsed = Expression.parse(expression);
        Expression derivative = parsed.differentiate(variableChar);
        return derivative.toString();    
    }
    
    /**
     * Evaluate an expression.
     * @param expression the expression to evaluate
     * @param environment maps variables to values.  Variables are required to be 1-letter [A-Za-z] case-sensitive strings.
     *         The set of variables in environment is allowed to be different than the 
     *         set of variables actually found in expression.  Values must be nonnegative numbers.
     * @return an expression equal to the input, but after substituting every variable v that appears in both
     *         the expression and the environment with its value, environment.get(v).  If there are no
     *         variables left in this expression after substitution, it is evaluated to a single number.
     *         returned expression string is formatted so that all operation pairs (ex: (a+b) ), numbers, and variables
     *         are parenthesized.
     *         For example: if expression is ((((a * (2 + ((((q))))) + 17)))) * g and we have an environment so that
     *         a maps to 2, g maps to 1, q maps to 7, then we will return 35.
     *         if expression is ((((a * (2 + ((((q))))) + 17)))) * g and we have an environment so that just
     *         a maps to 2, g maps to 1, then we will return ((((2 * (2 + ((((q))))) + 17)))) * 1
     * @throws IllegalArgumentException if the expression is invalid
     */
    public static String evaluate(String expression, Map<String,BigDecimal> environment) {
        Expression parsedExpression = Expression.parse(expression); //throws IllegalArgumentException if the expression is invalid
        if (parsedExpression.canEvaluate(environment)){
            Expression numberifiedExpression = parsedExpression.numberify(environment);
            BigDecimal expressionValue = numberifiedExpression.evaluate();
            String expressionValueString = expressionValue.toPlainString();
            return expressionValueString;
        }
        Expression partiallyNumberifiedExpression = parsedExpression.numberify(environment);
        return partiallyNumberifiedExpression.toString();
        
        
    }
    
}
