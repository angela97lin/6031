package norn;

import java.util.HashSet;
import java.util.Set;
/**
 * The intersection of two recipients sets. 
 * An intersection is the set of all recipients in both of the two collections.
 */
public class Intersection implements ListExpression{

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
     *      - this variant is immutable because
     *        emailRecipients set is created in the constructor, but not otherwise changed
     *        since we only use observer and constructor methods.
     *        (getEmailRecipients() utilizes defensive copying)
     *        Therefore, it is thread-safe via immutability.
     */

    private final Set<String> emailRecipients;

    /**
     * Make a new Intersection, which is a ListExpression representing the email
     * recipients contained in both first and second.
     * @param first a ListExpression representing a set of recipients
     * @param second a ListExpression representing a set of recipients
     */
    public Intersection(ListExpression first, ListExpression second){
        //find all recipients recursively in first, check if recipient also in second;
        //if so, add them to the set and return this set to client
        this.emailRecipients = new HashSet<>();
        Set<String> firstSet = first.getEmailRecipients();
        Set<String> secondSet = second.getEmailRecipients();
        for (String expressionFirst : firstSet){
            if (secondSet.contains(expressionFirst)){
                this.emailRecipients.add(expressionFirst);
            }
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
        return SameSet.makeString(this.emailRecipients);    
    }

    @Override
    public boolean equals(Object thatObject){
        if (!(thatObject instanceof ListExpression)) return false;
        ListExpression that = (ListExpression) thatObject;
        return SameSet.same(this.emailRecipients, that.getEmailRecipients());
    }

    @Override
    public int hashCode() {
        return SameSet.hashCode(this.getEmailRecipients());
    }
  

}
