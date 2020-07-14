package calculus;

import java.math.BigDecimal;
import java.util.Map;

/**
 * An expression representing a nonnegative number.
 */
class Number implements Expression {
    
    /*
     * Abstraction function:
     *      AF(n) = n is an nonnegative number in decimal representation,
     *              which consists of digits and an optional decimal point (ex: 7, 4.2)
     *              
     * Rep invariant:
     *     - n is nonnegative
     * 
     * Safety from rep exposure:
     *     - all fields are immutable and final
     *     - n is returned from value(), but is a final BigDecimal, so cannot be reassigned
     *       or mutated by clients (BigDecimal is an immutable datatype)
    */

    private final BigDecimal number;

    /** Make a Number with using the String number, stripped of all trailing zeroes.
     * number must be nonempty string consisting of only digits and an optional decimal point,
     * and must be nonnegative; all trailing zeroes are stripped, and a whole number
     * is represented without a decimal point (ex: 1.0 represented as 1)
     * equivalent numbers, use 0.6 and 0.12324
     * @param number numerical value of this number
     */
    public Number(String number) {
        this.number = new BigDecimal(number).stripTrailingZeros();
        checkRep();
    }
    
    /**
     * Check that the rep invariant holds.
     */
    private void checkRep(){
        assert (number.compareTo(BigDecimal.ZERO) >= 0);
    }
    
    /**
     * Returns the value of n.
     * @return n, the value of this number
     */
    public BigDecimal value() {
        return number;
    }
    
    @Override
    public Expression numberify(Map<String,BigDecimal> environment) {
        return this;
    }

    @Override
    public boolean canEvaluate(Map<String,BigDecimal> environment){
        return true;
    }
    
    @Override
    public BigDecimal evaluate(){
        return number;
    }

    /**
     * Differentiates our number expression using the
     * differentiation operation rule: dc/dx = 0
     * (Hence, should always return a new expression representing 0)
     * @param variable the variable to differentiate by, a single case-sensitive letter [A-Za-z].
     * @return derivative of our expression with respect to variable. 
     */
    @Override 
    public Expression differentiate(char variable) {
        return new Number("0");
    }

    /**
     * String version of our expression. 
     * Formatted using the toPlainString() method of bigDecimal
     * @return string representation of our number
     */
    @Override 
    public String toString() {
        return number.toPlainString();
    }

    /**
     * Two Number objects are only equivalent if their number are equal.
     * @param thatObject an object to compare to
     * @return true if this and thatObjects' number are .equals()
     * false otherwise
     */    
    @Override
    public boolean equals(Object thatObject){
        if (!(thatObject instanceof Number)) return false;
        Number thatNumber = (Number) thatObject;
        return (this.value().compareTo(thatNumber.value()) == 0);
    }
    

    @Override
    public int hashCode(){
        //converting to Double so that numerically equivalent vals
        //(such as 1.5 and 1.50000) will still map to same value.
        Double doubleVersion = new Double(number.doubleValue());
        return doubleVersion.hashCode();
    }


}
