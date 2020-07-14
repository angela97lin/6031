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
     *      - listSet is an empty set.
     *      
     * Safety from rep exposure:
     *      - all fields are private, immutable and final
     *      - listSet is returned from getEmailRecipients(), but is immutable (Collections.emptySet() is immutable) 
     *        and final, so cannot be reassigned or mutated by the client.
     * 
     * Thread Safety Argument:
     *      - this variant is immutable 
     *        because the only field is an immutable empty set, and
     *        we only use observer and constructor methods.
     *        Therefore, it is thread-safe via immutability.
     */

    private final Set<String> listSet;

    /**
     * Creates a new ListExpression representing an empty recipient.
     */
    public Empty(){
        listSet = Collections.emptySet();
        checkRep();
    }
    
    /**Checks that the rep invariant holds.*/
    private void checkRep(){
        assert (listSet.size() == 0);
    }
    
    @Override
    public Set<String> getEmailRecipients() {
        return listSet;
    }

    @Override 
    public String toString(){
        return "";
    }
    
    @Override
    public boolean equals(Object thatObject){
        if (!(thatObject instanceof ListExpression)) return false;
        ListExpression that = (ListExpression)thatObject;
        
        return SameSet.same(this.getEmailRecipients(), that.getEmailRecipients());
    }

    @Override
    public int hashCode() {
        return SameSet.hashCode(this.getEmailRecipients());
    }
  

}
