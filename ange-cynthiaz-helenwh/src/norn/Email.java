package norn;

import java.util.HashSet;
import java.util.Set;

/**
 * An email address, which represents a single recipient.
 */
class Email implements ListExpression {
    
    /*
     * Abstraction function:   
     *      AF(emailAddress) = a recipient with the email address emailAddress
     * 
     * Rep invariant:
     *      - emailAddress is a nonempty string consisting of
     *        username and domain name separated by an '@' symbol, like bitdiddle@mit.edu. 
     *        Usernames are nonempty case-insensitive strings 
     *        of letters, digits, underscores, dashes, periods, and plus signs. 
     *        Domain names are nonempty case-insensitive strings 
     *        of letters, digits, underscores, dashes, and periods. 
     *      
     * Safety from rep exposure:
     *      - all fields are private, immutable and final
     *      - getEmailRecipients() uses defensive copying
     *        to return a new Email object using the immutable
     *        and final field emailAddress, so is safe from
     *        rep exposure.
     *        
     * Thread Safety Argument:
     *      - this variant is immutable 
     *        because emailAddress (only field in rep) is an immutable String,
     *        and we only use observer and constructor methods 
     *        (getEmailRecipients() uses defensive copying to prevent rep exposure)
     *        Therefore, it is thread-safe via immutability.
     */
    private final String emailAddress;
    
    /**
     * Make a new Email object, which is a list expression representing
     * a single recipient, with the case-insensitive lower case version
     * of the provided email address.
     * @param emailAddress the email address of the recipient.
     * emailAddress must consist of a username and domain name separated by an '@' symbol, 
     * like bitdiddle@mit.edu, and usernames and domain names must be nonempty case-insensitive strings 
     * of letters, digits, underscores, dashes, and periods.
     */
    public Email(String emailAddress){
        this.emailAddress = emailAddress.toLowerCase();
        checkRep();
    }
    
    /**
     * Check that the rep invariant holds.
     */
    private void checkRep(){
        boolean isValid = emailAddress.matches("^[\\w.\\-\\+]+@[\\w.\\-]+$");
        assert (isValid);
    }
    
    @Override
    public Set<String> getEmailRecipients() {
        HashSet<String> listSet = new HashSet<>();
        listSet.add(emailAddress);
        return listSet;
    }

    @Override 
    public String toString(){
        return emailAddress;
    }
    
    @Override
    public boolean equals(Object thatObject){
        if ( ! (thatObject instanceof ListExpression)) { return false; }
        ListExpression that = (ListExpression)thatObject;
        return SameSet.same(this.getEmailRecipients(), that.getEmailRecipients());
    }

    @Override
    public int hashCode() {
        return SameSet.hashCode(this.getEmailRecipients());
    }
  
}
