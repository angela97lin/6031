package norn;

import java.util.HashSet;
import java.util.Set;

/**
 * The difference of two recipients sets. 
 * A difference is the set of all recipients in one collection, but not the other. 
 */
public class Difference implements ListExpression{

    /*
     * Abstraction function:   
     *      AF(first, second) = A list expression representing the difference of first and second (first \ second), 
     *                          which is the set of recipients in first, but NOT in second.
     *                          
     * Rep invariant:
     *      - true
     *      
     * Safety from rep exposure:
     *      - all fields are immutable and final
     *      - getEmailRecipients() utilizes defensive copying (of immutable objects)
     *        to return a new copy of emailRecipients,
     *        so emailRecipients is safe from rep exposure.
     */
    
    private final ListExpression first;
    private final ListExpression second;
    private final Set<ListExpression> emailRecipients;
    
    /**
     * Make a new Difference object, which is a ListExpression representing the email
     * recipients contained in first but not in second.
     * @param first a ListExpression representing a set of recipients
     * @param second a ListExpression representing a set of recipients
     */
    public Difference(ListExpression first, ListExpression second){
        this.first = first;
        this.second = second;
        
        //find all recipients recursively in first, check if recipient NOT in second;
        //if so, add them to the difference set
        this.emailRecipients = new HashSet<>();
        Set<ListExpression> firstSet = first.getEmailRecipients();
        Set<ListExpression> secondSet = second.getEmailRecipients();
        for (ListExpression expressionFirst : firstSet){
            if (!secondSet.contains(expressionFirst)){
                this.emailRecipients.add(expressionFirst);
            }
        }
    }

    @Override
    public Set<ListExpression> getEmailRecipients() {
        HashSet<ListExpression> recipientSet = new HashSet<>();
        for (ListExpression expression : this.emailRecipients){
            recipientSet.add(expression);
        }
        return recipientSet;
    }
    
    @Override
    public String toString(){
        String emailRecipientSetString = this.emailRecipients.toString();
        String retStr = emailRecipientSetString.replace("[", "").replace("]", "");//removing set brackets
        return retStr;
    }
    
    @Override
    public boolean equals(Object thatObject){
        if (!(thatObject instanceof Difference)) return false;
        Difference thatDifference = (Difference) thatObject;
        boolean firstEquals = (this.first.equals(thatDifference.first));
        boolean secondEquals = (this.second.equals(thatDifference.second));
        return firstEquals && secondEquals;
    }
    
    @Override
    public int hashCode(){
        return first.hashCode() + second.hashCode();
    }


}
