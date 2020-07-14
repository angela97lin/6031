package norn;

import java.util.HashMap;
import java.util.Map;

/**
 * DefinedMailingLists is a mutable type representing the collection of
 * all of the mailing lists that have been defined by the user.
 */
public class DefinedMailingLists {

    /*
     * Abstraction function:   
     *      AF(definedMailingList) = a collection of the mailing lists defined
     *                               in definedMailingList
     * 
     * Rep invariant:
     *      - true
     *      
     * Safety from rep exposure:
     *      - all fields are final
     *      - constructor and getMailingLists() uses defensive copying
     *        of immutable objects to make map elements safe from rep exposure.
     */
    
    private final Map<String, ListExpression> definedMailingLists;
    
    /**
     * Creates a new DefinedMailingLists with no previously defined mailing lists.
     */
    public DefinedMailingLists(){
        this.definedMailingLists = new HashMap<>();
    }
    
    /**
     * Creates a new DefinedMailingLists with previously defined mailing lists.
     * @param definedMailingList the map in which the keys are previously defined
     *  mailing list names, and each corresponding value is the set of recipients
     *  that corresponds to that mailing list.
     */
    public DefinedMailingLists(Map<String, ListExpression> definedMailingList){
        this.definedMailingLists = new HashMap<>();
        for (String name : definedMailingList.keySet()){
            ListExpression listRecipient = definedMailingList.get(name);
            definedMailingLists.put(name, listRecipient);
        }
    }
    
    /**
     * Retrieves all previously defined mailing lists
     * @return a map of all previously defined mailing lists, in which
     * each key represents the name of a mailing list, and each corresponding
     * value is a list expression representing the set of recipients of that mailing list
     */
    public Map<String, ListExpression> getMailingLists(){
        //uses defensive copying to prevent rep exposure
        Map<String, ListExpression> mailingLists = new HashMap<>();
        for (String name : this.definedMailingLists.keySet()){
            ListExpression listRecipient = definedMailingLists.get(name);            
            mailingLists.put(name, listRecipient);
        }  
        return mailingLists;
    }

    /**
     * Adds a newly defined mailing list to our collection.
     * @param name name of new mailing list
     * @param expression a list expression representing the recipients of this new mailing list
     */
    public void addMailingList(final String name, final ListExpression expression){
        this.definedMailingLists.put(name.toLowerCase(), expression);
    }
    
   
}
