package calculus;

import java.math.BigDecimal;
import java.util.Map;

class Variable implements Expression {

    /*
     * Abstraction function:
     *     AF(c) = c is a case-sensitive, single-letter variable Expression
     *             
     * Rep invariant:
     *     - c is an alphabetical (A-Z, a-z) letter only.
     *     - c is a single letter; this is taken care of by the nature of a char,
     *       so does not need to be checked
     *     
     * Safety from rep exposure:
     *     - all fields are immutable and final
     *     - c is returned from value(), but is a final char and thus, cannot be
     *       reassigned or mutated by clients.
     */
    
    private final char c;
   
    /** Make a Variable represented by char c. 
     * c must either be an upper case or lower case alphabetical character
     */
    public Variable(char c) {
        this.c = c;
        checkRep();
    }
    
    /**
     * Checks that the rep invariant holds.
     */
    private void checkRep(){
        assert Character.isLetter(c);
    }

    /**
     * Returns the value of c.
     * @return c, the value of this variable
     */
    public char value() {
        return c;
    }
    
    @Override
    public boolean canEvaluate(Map<String,BigDecimal> environment){
        String cString = Character.toString(c);
        return environment.containsKey(cString);
    }
    
    @Override
    public Expression numberify(Map<String,BigDecimal> environment) {
        String cString = Character.toString(c);
        //if exists in environment, can be numberified/simplified;
        //otherwise, it cannot and we must return original variable
        if (canEvaluate(environment)){
            String bigDecimalValue = environment.get(cString).toString();
            return new Number(bigDecimalValue);
        }
        return this;
    }

    @Override
    public BigDecimal evaluate() throws IllegalArgumentException {
        throw new IllegalArgumentException("cannot be evaluated");
    }

    /**
     * String version of Variable expression. 
     * Formatted as "c" where c is the value of our variable
     * @return string representation of our variable
     */
    @Override 
    public String toString() {
        return String.valueOf(c);
    }
    
    /**
     * Differentiates our variable expression using the
     * differentiation operation rule: dx/dx = 1, dy/dx = 0
     * @param variable the variable to differentiate by, a single case-sensitive letter [A-Za-z].
     * @return derivative of our expression with respect to variable. 
     */
    @Override 
    public Expression differentiate(char variable) {
        if (this.c == variable){
            return new Number("1");
        }
        return new Number("0");
    }

    /**
     * Two Variable objects are only equivalent if their values are equal.
     * 
     * @return true if this and thatObjects' values are ==
     * false otherwise
     */    
    @Override
    public boolean equals(Object thatObject){
        if (!(thatObject instanceof Variable)) return false;
        Variable thatVariable = (Variable) thatObject;
        return this.value() == thatVariable.value();
    }
    
    @Override
    public int hashCode(){
        return new Character(c).hashCode();
    }

}
