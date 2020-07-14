package norn;

import java.util.HashSet;
import java.util.Set;

/**
 * A ListExpression representing the union of two recipients sets. 
 * A union is the set of all recipients in either of the two collections.
 */
class Union implements ListExpression {
    
    /*
     * Abstraction function:   
     *      AF(ListExpression first, ListExpression second) = the list expression 
     *                                                        representing first U second,
     *                                                        which is the union of the recipients
     *                                                        in first and the recipients in second
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
     * Make a Union which is a list expression
     * representing the set of all recipients in first or second.
     * @param first a ListExpression representing a set of recipients
     * @param second a ListExpression representing a set of recipients
     */
    public Union(ListExpression first, ListExpression second){
        this.first = first;
        this.second = second;
        
        //find all recipients recursively in first and second,
        //and add them to the set to return to client.
        this.emailRecipients = new HashSet<>();
        Set<ListExpression> firstSet = first.getEmailRecipients();
        Set<ListExpression> secondSet = second.getEmailRecipients();
        for (ListExpression expressionFirst : firstSet){
            this.emailRecipients.add(expressionFirst);
        }
        for (ListExpression expressionSecond : secondSet){
            this.emailRecipients.add(expressionSecond);
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
        if (!(thatObject instanceof Union)) return false;
        Union thatUnion = (Union) thatObject;
        boolean unionFirstEquals = (this.first.equals(thatUnion.first));
        boolean unionSecondEquals = (this.second.equals(thatUnion.second));
        return unionFirstEquals && unionSecondEquals;
    }

    @Override
    public int hashCode(){
        return first.hashCode() + second.hashCode();
    }
    
}
