package calculus;

import java.math.BigDecimal;
import java.util.Map;

/**
 * An expression representing the product of two expressions.
 */
class Times implements Expression {

    /*
     * Abstraction function:   
     *      AF(left, right) = the expression representing the product of left and right; 
     *                        left * right
     * Rep invariant:
     *      - true
     *      
     * Safety from rep exposure:
     *      - all fields are immutable and final
     *      - constructor sets left and right Expression objects as fields, but
     *        does not expose rep because Expression objects are immutable and final.
     */
    private final Expression left, right;

    /** Make a Times which is the product of left and right. 
     * @param left left-side expression of product
     * @param right right-side expression of product
     */
    public Times(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Differentiates our product expression using the
     * differentiation operation rule: d(u*v)/dx = u*(dv/dx) + v*(du/dx)
     * @param variable the variable to differentiate by, a single case-sensitive letter [A-Za-z].
     * @return derivative of our product expression with respect to variable. 
     */
    @Override 
    public Expression differentiate(char variable) {
        Expression leftDerivative = left.differentiate(variable);
        Expression leftSide = Expression.times(right, leftDerivative);
        Expression rightDerivative = right.differentiate(variable);
        Expression rightSide = Expression.times(left, rightDerivative);
        return Expression.add(leftSide, rightSide);
    }

    @Override
    public BigDecimal evaluate() throws IllegalArgumentException{
        BigDecimal leftValue = left.evaluate();
        BigDecimal rightValue = right.evaluate();
        BigDecimal product = leftValue.multiply(rightValue);
        return product;    
    }

    @Override
    public Expression numberify(Map<String,BigDecimal> environment) {
        Expression leftNumberified = left.numberify(environment);
        Expression rightNumberified = right.numberify(environment);
        return new Times(leftNumberified, rightNumberified);
    }

    @Override
    public boolean canEvaluate(Map<String,BigDecimal> environment){
        boolean leftCanBeEvaluated = left.canEvaluate(environment);
        boolean rightCanBeEvaluated = right.canEvaluate(environment);
        return leftCanBeEvaluated && rightCanBeEvaluated;
    }

    /**
     * String version of Times expression. 
     * Formatted as (left)*(right)
     * @return string representation of our product, as (left)*(right)
     */
    @Override 
    public String toString() {
        return "(" + left + ")*(" + right + ")";
    }

    /**
     * Two Times objects are only equivalent if their left Expressions
     * are .equals(), and their right Expressions are .equals()
     * @param thatObject object to compare to
     * @return true if this and thatObjects' left Expressions
     * are .equals(), and their right Expressions are .equals(),
     * false otherwise
     * Note: this means that if two Times objects have flipped left/right
     * arguments, they will NOT be considered equivalent
     */
    @Override
    public boolean equals(Object thatObject){
        if (!(thatObject instanceof Times)) return false;
        Times thatTimes = (Times) thatObject;
        boolean leftEquals = left.equals(thatTimes.left);
        boolean rightEquals = right.equals(thatTimes.right);
        return leftEquals && rightEquals;
    }

    @Override
    public int hashCode(){
        return left.hashCode() + right.hashCode();
    }



}
