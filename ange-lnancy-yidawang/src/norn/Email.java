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
     *        Usernames and domain names are nonempty case-insensitive strings 
     *        of letters, digits, underscores, dashes, and periods. 
     *      
     * Safety from rep exposure:
     *      - all fields are immutable and final
     *      - getEmailRecipients() uses defensive copying
     *        to return a new Email object using the immutable
     *        and final emailAddress string, so is safe from
     *        rep exposure.
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
        boolean isValid = emailAddress.matches("^[\\w.\\-]+@[\\w.\\-]+$");
        assert (isValid);
    }
    
    @Override
    public Set<ListExpression> getEmailRecipients() {
        HashSet<ListExpression> listSet = new HashSet<>();
        listSet.add(new Email(emailAddress));
        return listSet;
    }

    @Override 
    public String toString(){
        return emailAddress;
    }
    
    @Override
    public boolean equals(Object thatObject){
        if (!(thatObject instanceof Email)) return false;
        Email thatEmail = (Email) thatObject;
        boolean emailEquals = (this.emailAddress.equals(thatEmail.emailAddress));
        return emailEquals;
    }

    @Override
    public int hashCode(){
        return emailAddress.hashCode();
    }
  
}
