package calculus;

import java.math.BigDecimal;
import java.util.Map;

/**
 * An expression representing an exponentiation.
 */

class Exponent implements Expression {

    /*
     * Abstraction function:   
     *      AF(base, power) = the expression representing base raised to the right-th power; 
     *                        base^power
     * Rep invariant:
     *      - power must be an Integer representing an nonnegative integer.
     *      
     * Safety from rep exposure:
     *      - all fields are immutable and final
     *      - constructor sets base and power Expression and Integer object as fields, but
     *        does not expose rep because Expression and Integer are final and immutable.
     */
    private final Expression base;
    private final Integer power;

    /** Make a Exponent which is left raised to the right-th power. 
     *  power must be an Integer representing an nonnegative integer 
     *  @param base the base expression of our exponent
     *  @param power the power of this exponent; must be nonnegative
     */
    public Exponent(Expression base, Integer power) {
        this.base = base;
        this.power = power;
        checkRep();
    }

    /** 
     * Check that the rep invariant holds.
     */
    private void checkRep(){
        assert (power.intValue() >= 0);
    }

    /**
     * Differentiates our exponent expression using the
     * differentiation operation rule: d(u^n)/dx = n*u^(n-1)*du/dx
     * @param variable the variable to differentiate by, a single case-sensitive letter [A-Za-z].
     * @return derivative of our exponent expression with respect to variable. 
     */
    @Override 
    public Expression differentiate(char variable) {
        if (power.intValue() == 0){
            //actually a constant with value 1
            return new Number("0");
        }
        
        Expression originalPower = Expression.make(power.toString()); //creates an expression representing the power of this exponent originally
        Integer lowerPower = new Integer(power.intValue()-1);
        Expression loweredExponent = Expression.power(base, lowerPower); //creates an expression representing the power of this exponent originally
        Expression baseDerivative = base.differentiate(variable);
        Expression lowerProduct = Expression.times(originalPower, loweredExponent);
        Expression derivative = Expression.times(lowerProduct, baseDerivative);
        return derivative;
    }

    @Override
    public BigDecimal evaluate() throws IllegalArgumentException{
        BigDecimal baseEvaluated = base.evaluate();
        BigDecimal exponentEvaluated = baseEvaluated.pow(power);
        return exponentEvaluated;
    }

    @Override    
    public Expression numberify(Map<String,BigDecimal> environment) {
        return new Exponent(base.numberify(environment), this.power);
    }

    @Override
    public boolean canEvaluate(Map<String,BigDecimal> environment){
        return base.canEvaluate(environment);
    }

    /**
     * String version of Exponent expression. 
     * Formatted as (base)^power
     * @return string representation of our exponent, as (base)^power
     */
    @Override 
    public String toString() {
        return "(" + base + ")^" + power;
    }

    /**
     * Two Exponent objects are only equivalent if their base Expression
     * are .equals(), and their power Integer are .equals()
     * @param thatObject an object to compare to
     * @return true if this and thatObjects' base Expression
     * are .equals(), and their power Integer are .equals(),
     * false otherwise
     */
    @Override
    public boolean equals(Object thatObject){
        if (!(thatObject instanceof Exponent)) return false;
        Exponent thatExponent = (Exponent) thatObject;
        boolean baseEquals = base.equals(thatExponent.base);
        boolean powerEquals = (this.power.equals(thatExponent.power));
        return baseEquals && powerEquals;
    }

    @Override
    public int hashCode(){
        return base.hashCode() + power.hashCode();
    }

}
