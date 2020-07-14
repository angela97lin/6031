package norn;

import java.util.HashSet;
import java.util.Set;

/**
 * A mailing list containing a set of recipients.
 */
public class MailingList implements ListExpression {

    /*
     * Abstraction function:   
     *      AF(name, recipientSet) = the list expression representing a mailing list with
     *                               the name name and corresponding to the set of email recipients
     *                               in recipientSet
     *                                                        
     * Rep invariant:
     *      - name is a nonempty case-insensitive strings consisting
     *        of letters, digits, underscores, dashes, and periods.
     *      - recipient cannot include a mailing list with the same name (same mailing list) 
     *      
     * Safety from rep exposure:
     *      - all fields are immutable and final
     *      - constructor adds from recipientSet variable,
     *        but utilizes defensive copying of set, and
     *        adds immutable ListExpression objects, so safe from
     *        rep exposure.
     */

    private final String name;
    private final ListExpression recipients;  
    private final Set<ListExpression> recipientSet;
    
    /**
     * Make a new MailingList, with the name name, containing the set of
     * recipients in recipientSet
     * @param name the name of the mailing list
     * @param recipientSet the set of ListExpressions representing the recipients
     */
    public MailingList(String name, ListExpression recipients){
        this.name = name.toLowerCase();
        this.recipients = recipients;
        //uses defensive copying of immutable objects
        this.recipientSet = new HashSet<>();
        for (ListExpression expression : recipients.getEmailRecipients()){
            this.recipientSet.add(expression);
        }
        checkRep();
    }
    
    /**
     * Check that the rep invariant holds.
     */
    private void checkRep(){
        boolean isValid = name.matches("^[\\w.\\-]+$");
        assert (isValid);
    }
    
    @Override
    public Set<ListExpression> getEmailRecipients() {
        //makes sure if this contains other lists,
        //returns emails in those lists, not the list names
        HashSet<ListExpression> listSet = new HashSet<>();
        for (ListExpression expression : recipientSet){
            Set<ListExpression> expressionSet = expression.getEmailRecipients();
            for (ListExpression expressionDefined : expressionSet){
                listSet.add(expressionDefined);
            }
        }
        return listSet;
    }
    
    @Override 
    public String toString(){
        String emailRecipientSetString = this.recipientSet.toString();
        String retStr = emailRecipientSetString.replace("[", "").replace("]", "");//removing set brackets
        return retStr;    
    }
    
    @Override
    public boolean equals(Object thatObject){
        //note: since we are defining ListExpressions to be
        //structurally equal, a = b,c != a = c,b
        if (!(thatObject instanceof MailingList)) return false;
        MailingList thatMailingList = (MailingList) thatObject; 
        boolean recipientEqual = this.recipients.equals(thatMailingList.recipients);
        boolean nameEqual = this.name.equals(thatMailingList.name);
        return recipientEqual && nameEqual;
    }

    @Override
    public int hashCode(){
        return recipientSet.hashCode();
    }

}
