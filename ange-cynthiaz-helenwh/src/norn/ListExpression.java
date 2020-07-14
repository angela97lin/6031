package norn;

import java.util.Set;

/**
 * An immutable data type representing mailing list expression,
 * as defined in the Norn1 specifications (http://web.mit.edu/6.031/www/sp17/projects/norn1/spec/)
 * and Norn2 specifications (http://web.mit.edu/6.031/www/sp17/projects/norn2/spec/)
 * as a set of recipients that might be pasted into the To: field of an email message
 */

public interface ListExpression {
    
    // Datatype definition for ListExpression:
    //  ListExpression = Empty()
    //             + Email(emailAddress:String)
    //             + Difference(first:ListExpression, second:ListExpression)
    //             + Intersection(first:ListExpression, second:ListExpression)
    //             + Union(first:ListExpression, second:ListExpression)
    //             + Sequence(first: ListExpression, second: ListExpression)
    
    /**
     * Creates a new ListExpression representing an empty recipient.
     * @return ListExpression representing an empty recipient 
     */
    public static ListExpression makeEmpty(){
        return new Empty();
    }

    /**
     * Creates a new ListExpression representing an email address.
     * @param emailAddress the email of a recipient; 
     * must be a string consisting of username and domain name 
     * separated by an '@' symbol, like bitdiddle@mit.edu. 
     * Usernames are nonempty case-insensitive strings 
     * of letters, digits, underscores, dashes, periods, and plus signs.
     * Domain names are nonempty case-insensitive strings 
     * of letters, digits, underscores, dashes, and periods.
     * @return ListExpression representing a valid email recipient
     */
    public static ListExpression makeEmail(String emailAddress){
        return new Email(emailAddress);
    }
    
    /**
     * Creates a new ListExpression representing the union of the recipients 
     * contained in two ListExpressions (mailing list expressions) 
     * @param first a ListExpression representing a set of recipients
     * @param second a ListExpression representing a set of recipients
     * @return a ListExpression representing first , second, representing the union of first and second
     * (all recipients in first and all recipients in second)
     */
    public static ListExpression makeUnion(ListExpression first, ListExpression second){
        return new Union(first, second);
    }
    
    /**
     * Creates a new ListExpression representing the difference set between the recipients
     * contained in two ListExpressions (mailing list expressions)
     * @param first, a ListExpression representing a set of recipients
     * @param second, a ListExpression representing a set of recipients
     * @return a ListExpression representing first ! second, or the difference between first and second
     * (all recipients in first that are NOT in second)
     */
    public static ListExpression makeDifference(ListExpression first, ListExpression second){
        return new Difference(first, second);
    }
    
    /**
     * Creates a new ListExpression representing the intersection set of recipients
     * contained in two ListExpressions (mailing list expressions)
     * @param first a ListExpression representing a set of recipients
     * @param second a ListExpression representing a set of recipients
     * @return a ListExpression representing first * second, or the intersection of first and second
     * (all recipients in first AND second)
     */
    public static ListExpression makeIntersection(ListExpression first, ListExpression second){
        return new Intersection(first, second);
    }
    
    /**
     * Creates a new ListExpression representing the sequence of recipients
     * contained in two ListExpressions (mailing list expressions).
     * @param first a ListExpression representing a set of recipients
     * @param second a ListExpression representing a set of recipients
     * @return a ListExpression representing first ; second, or the recipients produced by second, 
     * after substituting the expressions of all named mailing list definitions found in first
     */
    public static ListExpression makeSequence(ListExpression first, ListExpression second) {
        return new Sequence(first, second);
    }
    
    /**
     * Returns the set of all email recipients represented
     * by the list expression.
     * @return a set of strings representing the emails in this mailing list.
     */
    public Set<String> getEmailRecipients();
    
    
    /**
     * @return a String representation of all of the email recipients
     * contained within this list expression. Order is not determined in any specific way.
     */
    @Override 
    public String toString();

    /**
     * @param thatObject any object
     * @return true if and only if this and thatObject represent the same set of email recipients.
     */
    @Override
    public boolean equals(Object thatObject);
    
    /**
     * @return hash code value consistent with the equals() definition of
     * equality, such that for all e1,e2:ListExpression,
     * e1.equals(e2) implies e1.hashCode() == e2.hashCode()
     */
    @Override
    public int hashCode();


}
