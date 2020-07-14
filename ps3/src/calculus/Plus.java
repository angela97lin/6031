package calculus;

import java.math.BigDecimal;
import java.util.Map;

/**
 * An expression representing the sum of two expressions.
 */
public class Plus implements Expression {

    /*
     * Abstraction function:   
     *      AF(left, right) = the expression representing the sum of left and right; 
     *                        left + right
     *                        
     * Rep invariant:
     *      - true
     *      
     * Safety from rep exposure:
     *      - all fields are immutable and final
     *      - constructor sets left and right Expression objects as fields, but
     *        does not expose rep because Expression objects are immutable.
     */
    
    private final Expression left, right;

    /** Make a Plus which is the sum of left and right. 
     * @param left left-side expression of sum
     * @param right right-side expression of sum
     * */
    public Plus(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }
    
    /**
     * Differentiates our sum expression using the
     * differentiation operation rule: d(u+v)/dx = du/dx + dv/dx
     * @param variable the variable to differentiate by, a single case-sensitive letter [A-Za-z].
     * @return derivative of our sum expression with respect to variable. 
     */
    @Override 
    public Expression differentiate(char variable) {
        Expression leftDerivative = left.differentiate(variable);
        Expression rightDerivative = right.differentiate(variable);
        return Expression.add(leftDerivative, rightDerivative);
    }
    
    @Override
    public BigDecimal evaluate() throws IllegalArgumentException{
        BigDecimal leftValue = left.evaluate();
        BigDecimal rightValue = right.evaluate();
        BigDecimal sum = leftValue.add(rightValue);
        return sum;
    }
    
    @Override
    public Expression numberify(Map<String,BigDecimal> environment) {
        Expression leftNumberified = left.numberify(environment);
        Expression rightNumberified = right.numberify(environment);
        return new Plus(leftNumberified, rightNumberified);
    }
    
    @Override
    public boolean canEvaluate(Map<String,BigDecimal> environment){
        return left.canEvaluate(environment) && right.canEvaluate(environment);
    }
    
    /**
     * String version of Plus expression. 
     * Formatted as (left)+(right)
     * @return string representation of our sum, represented as (left)+(right)
     */
    @Override 
    public String toString() {
        return "(" + left + ")+(" + right + ")";
    }

    /**
     * Two Plus objects are only equivalent if their left Expressions
     * are .equals(), and their right Expressions are .equals()
     * @param thatObject object to compare to
     * @return true if this and thatObjects' left Expressions
     * are .equals(), and their right Expressions are .equals(),
     * false otherwise
     * Note: this means that if two Plus objects have flipped left/right
     * arguments, they will NOT be considered equivalent
     */
    @Override
    public boolean equals(Object thatObject){
        if (!(thatObject instanceof Plus)) return false;
        Plus thatPlus = (Plus) thatObject;
        boolean leftEquals = left.equals(thatPlus.left);
        boolean rightEquals = right.equals(thatPlus.right);
        return leftEquals && rightEquals;
    }

    @Override
    public int hashCode(){
        return left.hashCode() + right.hashCode();
    }



}
