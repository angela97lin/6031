/* Copyright (c) 2015-2017 MIT 6.005/6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package calculus;

import java.math.BigDecimal;
import java.util.Map;
import lib6005.parser.*;

/**
 * An immutable data type representing a mathematical expression, as defined
 * in the PS3 handout.
 * 
 * You may, however, add additional methods, or strengthen the specs of existing methods.
 */
public interface Expression {
    
    // Datatype definition for Expression:
    //  Expression = Number(number:String)
    //             + Variable(c:char)
    //             + Plus(left:Expression, right:Expression)
    //             + Times(left:Expression, right:Expression)
    //             + Exponent(base:Expression, power:Integer)
    
    /**
     * Creates a new Expression representing a nonnegative number.
     * @param number the string version of the number to represent;
     * must include only digits, which may be followed by a decimal point
     * and more digits; leading decimal point numbers such as
     * .6 or .12324 are NOT allowed; in order to represent numerically
     * equivalent numbers, use 0.6 and 0.12324
     * @return Expression representing a nonnegative number whose
     * value is given by number
     */
    public static Expression make(String number){
        return new Number(number);
    }
    
    /**
     * Creates a new Expression representing a variable.
     * @param c a character to represent our variable by;
     * must either be an upper case or lower case alphabetical character
     * @return Expression representing a variable as
     * derived by c
     */
    public static Expression make(char c){
        return new Variable(c);
    }
    
    /**
     * Creates a new Expression representing the summation of two Expressions.
     * @param left left-side argument Expression to perform addition on
     * @param right left-side argument Expression to perform addition on
     * @return an Expression representing the summation of left and right
     */
    public static Expression add(Expression left, Expression right){
        return new Plus(left, right);
    }
    
    /**
     * Creates a new Expression representing the product of two Expressions.
     * @param left left-side argument Expression to perform multiplication on
     * @param right left-side argument Expression to perform multiplication on
     * @return an Expression representing the product of left and right
     */
    public static Expression times(Expression left, Expression right){
        return new Times(left, right);
    }
    
    /**
     * Creates a new Expression representing the exponentiation of
     * base to the power-th power
     * @param base Expression that represents the base of our exponent
     * @param power Expression to represent the power to raise our
     * exponent to; must represent a numerical value.
     * @return Expression representing a base expression raised to an nonnegative integer power
     */
    public static Expression power(Expression base, Integer power){
        return new Exponent(base, power);
    }
    
    /**
     * Differentiates an expression.
     * @param variable to take the derivative with respect to
     * @return an expression with the derivative of the input with respect to the variable
     */
    public Expression differentiate(char variable);
    
    /**
     * Evaluates the expression.
     * @return BigDecimal representing the numerical value of the expression.
     * @throws IllegalArgumentException if there are any variables in the expression.
     */
    public BigDecimal evaluate() throws IllegalArgumentException;
    
    /**
     * Returns a new Expression in which all variables in our environment has been substituted with its numerical value.
     * @param environment maps variables to values.  Variables are required to be 1-letter [A-Za-z] case-sensitive strings.
     *         The set of variables in environment is allowed to be different than the 
     *         set of variables actually found in expression.  Values must be nonnegative numbers.
     * @return an expression equal to the input, but after substituting every variable v that appears in both
     *         the expression and the environment with its numerical value, as specified in environment.get(v). 
     */
    public Expression numberify(Map<String,BigDecimal> environment);
    
    /**
     * Determines whether or not an expression can be completely evaluated (to one constant expression) with the given environment
     * @param environment maps variables to values.  Variables are required to be 1-letter [A-Za-z] case-sensitive strings.
     *         The set of variables in environment is allowed to be different than the 
     *         set of variables actually found in expression.  Values must be nonnegative numbers.
     * @return true if all variables in our expression can be replaced with numerical values, as specified in environment.get(v),
     *         false otherwise.
     */
    public boolean canEvaluate(Map<String,BigDecimal> environment);
    
    /**
     * Parse an expression.
     * @param input expression to parse, as defined in the PS3 handout.
     * @return expression AST for the input
     * @throws IllegalArgumentException if the expression is invalid
     */
    public static Expression parse(String input) {
        try {
            return ExpressionParser.parse(input);
        } catch (UnableToParseException e) {
            throw new IllegalArgumentException("the expression is invalid", e);
        }
    }
     
    /**
     * @return a parsable representation of this expression, such that
     * for all e:Expression, e.equals(Expression.parse(e.toString())).
     */
    @Override 
    public String toString();

    /**
     * @param thatObject any object
     * @return true if and only if this and thatObject are structurally-equal
     * Expressions, as defined in the PS3 handout.
     */
    @Override
    public boolean equals(Object thatObject);
    
    /**
     * @return hash code value consistent with the equals() definition of structural
     * equality, such that for all e1,e2:Expression,
     *     e1.equals(e2) implies e1.hashCode() == e2.hashCode()
     */
    @Override
    public int hashCode();
        
}
