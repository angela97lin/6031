package norn;
import java.util.HashSet;
import java.util.Set;
/**
 * A sequence of two list expressions or list definitions, separated by a semicolon.
 */
public class Sequence implements ListExpression{

    /*
     * Abstraction function:   
     *      AF(emailRecipients) = A list expression representing 
     *                            the set of email recipients in emailRecipients
     * 
     * Rep invariant:
     *      - emailRecipients != null
     *      - all elements in emailRecipients != null
     *      
     * Safety from rep exposure:
     *      - all fields are private, immutable and final
     *      - getEmailRecipients() utilizes defensive copying (of immutable objects)
     *        to return a new copy of emailRecipients,
     *        so emailRecipients is safe from rep exposure.
     *        
     * Thread Safety Argument:
     *      - this variant is immutable because emailRecipients 
     *        is constructed (via defensive copying) in the constructor,
     *        but is not mutated thereafter, and we 
     *        only use observer and constructor methods.
     *        (getEmailRecipients() also uses defensive copying).
     *        Therefore, it is thread-safe via immutability.
     */
    
    private final Set<String> emailRecipients;

    /**
     * Creates a ListExpression that represents the recipients of one list expression produced after
     * substituting the expressions of all named list definitions found in the first list expression
     * @param first a ListExpression representing a set of recipients
     * @param second a ListExpression representing a set of recipients
     */
    public Sequence(ListExpression first, ListExpression second){
        Set<String> secondRecipients = second.getEmailRecipients();
        emailRecipients = new HashSet<>();
        for (String expression : secondRecipients){
            emailRecipients.add(expression);
        }
        checkRep();
    }
    
    /**
     * Check that the rep invariant holds.
     */
    private void checkRep(){
        assert (emailRecipients != null);
        for (String s : emailRecipients){
            assert (s != null);
        }
    }

    @Override
    public Set<String> getEmailRecipients() {
        HashSet<String> recipientSet = new HashSet<>();
        for (String expression : this.emailRecipients){
            recipientSet.add(expression);
        }
        return recipientSet;    
    }

    @Override
    public String toString(){
        return SameSet.makeString(this.getEmailRecipients());    
    }
    
    @Override
    public boolean equals(Object thatObject){
        if (!(thatObject instanceof ListExpression)) return false;
        ListExpression that = (ListExpression) thatObject;
        return SameSet.same(this.getEmailRecipients(), that.getEmailRecipients());
    }
    
    @Override
    public int hashCode() {
        return SameSet.hashCode(this.getEmailRecipients());
    }
  
}

