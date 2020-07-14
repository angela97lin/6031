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
     *      AF(first, second) =  A list expression representing first intersect second,
     *                           which the intersection of the recipients in first and 
     *                           the recipients in second (the set of recipients in first AND in second)
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
     * Make a new Intersection, which is a ListExpression representing the email
     * recipients contained in both first and second.
     * @param first a ListExpression representing a set of recipients
     * @param second a ListExpression representing a set of recipients
     */
    public Intersection(ListExpression first, ListExpression second){
        this.first = first;
        this.second = second;

        //find all recipients recursively in first, check if recipient also in second;
        //if so, add them to the set and return this set to client
        this.emailRecipients = new HashSet<>();
        Set<ListExpression> firstSet = first.getEmailRecipients();
        Set<ListExpression> secondSet = second.getEmailRecipients();
        for (ListExpression expressionFirst : firstSet){
            if (secondSet.contains(expressionFirst)){
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
        if (!(thatObject instanceof Intersection)) return false;
        Intersection thatIntersection = (Intersection) thatObject;
        boolean firstEquals = (this.first.equals(thatIntersection.first));
        boolean secondEquals = (this.second.equals(thatIntersection.second));
        return firstEquals && secondEquals;
    }

    @Override
    public int hashCode(){
        return first.hashCode() + second.hashCode();
    }

}
