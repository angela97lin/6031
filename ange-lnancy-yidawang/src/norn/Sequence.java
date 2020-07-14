package norn;
import java.util.Set;
/**
 * A sequence of two list expressions or list definitions, separated by a semicolon.
 */
public class Sequence implements ListExpression{

    /*
     * Abstraction function:   
     *      AF(first, second) = list expression that represents the recipients produced by second, 
     *      after substituting the expressions of all named list definitions found in first
     * 
     * Rep invariant:
     *      - true
     *      
     * Safety from rep exposure:
     *      - all fields are immutable and final
     *      - getEmailRecipients() uses defensive copying
     *        to return a new Email object using the immutable
     *        and final emailAddress string, so is safe from
     *        rep exposure.
     */
    
    private final ListExpression first;
    private final ListExpression second;
    
    /**
     * Creates a ListExpression that represents the recipients of one list expression produced after
     * substituting the expressions of all named list definitions found in the first list expression
     * @param first a ListExpression representing a set of recipients
     * @param second a ListExpression representing a set of recipients
     */
    public Sequence(ListExpression first, ListExpression second){
        this.first = first;
        this.second = second;
    }
    
    @Override
    public Set<ListExpression> getEmailRecipients() {
        return second.getEmailRecipients();
    }
    
    @Override
    public String toString(){
        String emailRecipientSetString = this.getEmailRecipients().toString();
        String retStr = emailRecipientSetString.replace("[", "").replace("]", "");//removing set brackets
        return retStr;    
    }
    
    @Override
    public boolean equals(Object thatObject){
        if (!(thatObject instanceof Sequence)) return false;
        Sequence thatSequence = (Sequence) thatObject;
        //based on structural equality
        boolean firstEquals = (this.first.equals(thatSequence.first));
        boolean secondEquals = (this.second.equals(thatSequence.second));
        return firstEquals && secondEquals;
    }
    
    @Override
    public int hashCode(){
        return first.hashCode() + second.hashCode();
    }

}

