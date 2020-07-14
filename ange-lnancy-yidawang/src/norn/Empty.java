package norn;
import java.util.Collections;
import java.util.Set;

/**
 * An empty email recipient.
 */
class Empty implements ListExpression {
    
    /*
     * Abstraction function:   
     *      AF() = a list expression representing an empty recipient.
     * 
     * Rep invariant:
     *      - true
     *      
     * Safety from rep exposure:
     *      - all fields are immutable and final
     *      - listSet is returned from getEmailRecipients(), but is immutable (Collections.emptySet() is immutable) 
     *        and final, so cannot be reassigned or mutated by the client.
     */

    private final Set<ListExpression> listSet;

    /**
     * Creates a new ListExpression representing an empty recipient.
     */
    public Empty(){
        listSet = Collections.emptySet();
    }
    
    @Override
    public Set<ListExpression> getEmailRecipients() {
        return listSet;
    }

    @Override 
    public String toString(){
        return "";
    }
    
    @Override
    public boolean equals(Object thatObject){
        if (!(thatObject instanceof Empty)) return false;
        return true;
    }

    @Override
    public int hashCode(){
        return 0;
    }

}
