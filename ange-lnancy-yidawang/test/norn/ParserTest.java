package norn;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ParserTest {

    /*ORDER OF PRECEDENCE ; = , ! *
     * Testing Strategy for Parser:        
     * ====================================================================================
     * String input:
     *   ** valid expressions:
     *        - is empty string (length = 0)
     *        - uses ',' 0, 1, >1 times (UNION)
     *              // valid to have unions of 
     *              //empty , email, mailing list, union, difference, intersection
     *              - empty , empty
     *              - email , email
     *              - mailing list , mailing list
     *              - union , union
     *              - difference , difference
     *              - intersection , intersection 
     *              - empty , email / email, empty
     *              - empty , union / union, empty
     *              - empty , mailing list / mailing list , empty
     *              - empty , difference / difference, empty
     *              - empty , intersection, intersection , empty
     *              - email , mailing list / mailing list , email     
     *              - email , union / union, email
     *              - email , difference / difference , email
     *              - email , intersection / intersection , email
     *              - mailing list , union / union , mailing list
     *              - mailing list , difference / difference , mailing list
     *              - mailing list , intersection / intersection / mailing list
     *              - union , difference / difference , union
     *              - union , intersection / intersection , union
     *              - difference , intersection / intersection , difference
     *              
     *        - uses '!' operation 0, 1, >1 times (DIFFERENCE) 
     *              // valid to have differences of 
     *              //empty , email, mailing list, difference, intersection
     *              - empty ! empty
     *              - email ! email
     *              - mailing list ! mailing list
     *              - difference ! difference
     *              - intersection ! intersection 
     *              - empty ! email / email, empty
     *              - empty ! mailing list / mailing list ! empty
     *              - empty ! difference / difference ! empty
     *              - empty ! intersection, intersection ! empty
     *              - email ! mailing list / mailing list ! email     
     *              - email ! difference / difference ! email
     *              - email ! intersection / intersection ! email
     *              - mailing list ! difference / difference ! mailing list
     *              - mailing list ! intersection / intersection ! mailing list
     *              - difference ! intersection / intersection ! difference
     *              
     *        - uses '*' operation 0, 1, >1 times (INTERSECTION)  
     *              // valid to have intersections of 
     *              //empty, email, mailing list, intersection
     *              - empty * empty
     *              - email * email
     *              - mailing list * mailing list
     *              - intersection * intersection 
     *              - empty * email / email, empty
     *              - empty * mailing list / mailing list * empty
     *              - empty * intersection / intersection * empty
     *              - email * mailing list / mailing list * email   
     *              - email * intersection / intersection * email
     *              - mailing list * intersection / intersection * mailing list      
     *                   
     *        - uses ';' operation 0, 1, >1 times (SEQUENCE)
     *              // valid to have sequences of 
     *              //empty , email, mailing list, mailing list definition, 
     *              union, difference, intersection 
     *              - empty ; empty
     *              - email ; email
     *              - mailing list ; mailing list
     *              - mailing list definition ; mailing list definition
     *              - union ; union
     *              - difference ; difference
     *              - intersection ; intersection 
     *              - empty ; email / email ; empty
     *              - empty ; union / union ; empty
     *              - empty ; mailing list / mailing list ; empty
     *              - empty ; mailing list definition / mailing list definition ; empty
     *              - empty ; difference / difference ; empty
     *              - empty ; intersection, intersection ; empty
     *              - email ; mailing list / mailing list ; email    
     *              - email ; mailing list definition / mailing list definition ; email 
     *              - email ; union / union ; email
     *              - email ; difference / difference ; email
     *              - email ; intersection / intersection ; email
     *              - mailing list ; union / union ; mailing list
     *              - mailing list ; mailing list definition / mailing list definition ; mailing list 
     *              - mailing list ; difference / difference ; mailing list
     *              - mailing list ; intersection / intersection ; mailing list
     *              - union ; difference / difference ; union
     *              - union ; intersection / intersection ; union
     *              - union ; mailing list definition / mailing list definition ; union
     *              - difference ; intersection / intersection ; difference
     *              - difference ; mailing list definition / mailing list definition ; union
     *              - intersection ; mailing list definition / mailing list definition ; intersection
     *        - uses '!', '*', ';' 0, 1, >1 times (tests order of operations!) in various ways
     *        - uses parentheses (test new order of operations)
     *              - ex: union ! union 
     *        
     *        - list definition specifications:
     *              - list name
     *                  - uses 0, 1, >1 letters
     *                  - uses 0, 1, >1 digits
     *                  - uses 0, 1, >1 underscores
     *                  - 0, 1, >1 dashes
     *                  - 0, 1, >1 periods
     *                  - consists only of letters
     *                  - consists only of digits
     *                  - consists only of underscores
     *                  - consists only of dashes
     *                  - various combinations of these
     *              - list recipient field:
     *                  - empty
     *                  - email address
     *                  - mailing list
     *                  - union
     *                  - intersection
     *                  - difference
     *              
     *        - email address specifications:
     *              - username of recipient
     *                  - 0, 1, >1 letters
     *                  - 0, 1, >1 digits
     *                  - 0, 1, >1 underscores
     *                  - 0, 1, >1 dashes
     *                  - 0, 1, >1 periods
     *                  - consists only of letters
     *                  - consists only of digits
     *                  - consists only of underscores
     *                  - consists only of dashes
     *                  - various combinations of these
     *              - domain of recipient
     *                  - 0, 1, >1 letters
     *                  - 0, 1, >1 digits
     *                  - 0, 1, >1 underscores
     *                  - 0, 1, >1 dashes
     *                  - 0, 1, >1 periods
     *                  - consists only of letters
     *                  - consists only of digits
     *                  - consists only of underscores
     *                  - consists only of dashes
     *                  - various combinations of these
     *                  - 1, >1 domain
     *    ** invalid expressions:
     *          - has empty username in recipient field (@gmail.com, @mit.edu)
     *          - has @ in email, but empty domain (.com, .edu, etc.)
     *          - has multiple '@' in one email address
     *          - missing both username and domain (@cow)
     *          - has extraneous symbols not defined ($, %, ^, etc.)
     *          - has whitespaces (\s, \t, \r, \n) within email address or list definition name
     *          - uses parentheses to group nested list definitions and subexpressions
     *          
     */
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    //covers case where input is empty string (length = 0)
    @Test
    public void testParseValidOnlyEmpty(){
        final String input = "";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeEmpty();
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where input is whitespace-only string (length = 0)
    @Test
    public void testParseValidOnlyWhiteSpaces(){
        final String input = "           ";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeEmpty();
        assertEquals("list expression not as expected", expected, actual);
    }

    //covers case where input uses commas 0 times
    @Test
    public void testParseValidWithOneRecipient(){
        final String input = "hellothere@mit";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeEmail(input);
        assertEquals("list expression not as expected", expected, actual);
    }

    //covers case where input uses commas 1 times
    //                  union of email, email
    @Test
    public void testParseValidEmailAddressesWithTwoRecipients(){
        final String input = "hellothere@mit, o@o.o.o.o.o.o";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression expected = ListExpression.makeUnion(emailOne, emailTwo);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where input uses commas > 1 times
    //                  union of email, empty
    //                  union of empty, empty
    @Test
    public void testParseValidEmailAddressesUnionOfEmptyAndEmpty(){
        final String input = "hellothere@mit, , ";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmpty();
        ListExpression emailThree = ListExpression.makeEmpty();
        ListExpression expectedOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression expected = ListExpression.makeUnion(expectedOne, emailThree);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where input uses commas > 1 times
    //                  union of union, empty
    //                  union of union, email
    @Test
    public void testParseValidEmailAddressesUnionOfUnionAndEmptyAndUnionAndEmail(){
        final String input = ", hellothere@mit, , cowgirl@hey";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression emailZero = ListExpression.makeEmpty();
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmpty();
        ListExpression emailThree = ListExpression.makeEmail("cowgirl@hey");

        ListExpression expectedOne = ListExpression.makeUnion(emailZero, emailOne);
        ListExpression expectedTwo = ListExpression.makeUnion(expectedOne, emailTwo);
        ListExpression expected = ListExpression.makeUnion(expectedTwo, emailThree);
        assertEquals("list expression not as expected", expected, actual);
    }
    //covers case where input uses commas > 1 times
    //                  union of empty, email
    //                  union of empty, empty
    @Test
    public void testParseValidEmailAddressesUnionOfEmailAndEmpty(){
        final String input = ", hellothere@mit, , ";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression emailZero = ListExpression.makeEmpty();
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmpty();
        ListExpression emailThree = ListExpression.makeEmpty();
        ListExpression expectedOne = ListExpression.makeUnion(emailZero, emailOne);
        ListExpression expectedTwo = ListExpression.makeUnion(expectedOne, emailTwo);
        ListExpression expected = ListExpression.makeUnion(expectedTwo, emailThree);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where input uses commas > 1 times
    @Test
    public void testParseValidEmailAddressesWithManyRecipients(){
        final String input = "hellothere@mit, h-e-L-L-o-___.w@cow.cow.cow, o@o.o.o.o.o.o";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("h-e-L-L-o-___.w@cow.cow.cow");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression expectedOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression expected = ListExpression.makeUnion(expectedOne, emailThree);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where recipients with usernames consisting of letters only
    //                  recipients have usernames with 1 letter
    //                  recipients have usernames with > 1 letters
    //                  recipients have usernames with 0 underscores
    //                  recipient has 1 domain
    @Test
    public void testParseValidEmailAddressesUsernameConsistsOfLettersOnly(){
        final String input = "w@mit";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeEmail(input);
        assertEquals("list expression not as expected", expected, actual);
        
        final String inputTwo = "lettersonly@mit";
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeEmail(inputTwo);
        assertEquals("second list expression not as expected", expectedTwo, actualTwo);
    }
    
    
    //covers case where has recipients with usernames consisting of digits only
    //                  recipients have usernames with 1 digit
    //                  recipients have usernames with > 1 digits
    //                  recipients have usernames with 0 letters 
    //                  > 1 domain
    @Test
    public void testParseValidEmailAddressesUsernameConsistsOfDigitsOnly(){
        final String input = "6@mit.edu.cow";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeEmail(input);
        assertEquals("list expression not as expected", expected, actual);
        
        final String inputTwo = "6031@mit";
        
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeEmail(inputTwo);
        assertEquals("second list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where has recipients with usernames consisting of underscores only
    //                      recipients have usernames with 1 underscores
    //                      recipients have usernames with > 1 underscores
    //                      recipients have usernames with 0 dashes
    @Test
    public void testParseValidEmailAddressesUsernameConsistsOfUnderscoresOnly(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String input = "_@mit.edu.cow";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeEmail(input);
        assertEquals("list expression not as expected", expected, actual);
        
        final String inputTwo = "_____@mit.edu.wow.qool";
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeEmail(inputTwo);
        assertEquals("second list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where has recipients with usernames consisting of periods only
    //                      recipients have usernames with 1 period
    //                      recipients have usernames with > 1 periods
    //                      recipients have usernames with 0 digits
    @Test
    public void testParseValidEmailAddressesUsernameConsistsOfPeriodsOnly(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String input = ".@mit.edu.cow";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeEmail(input);
        assertEquals("list expression not as expected", expected, actual);
        
        final String inputTwo = "......@mit.edu.wow.qool";
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeEmail(inputTwo);
        assertEquals("second list expression not as expected", expectedTwo, actualTwo);
    }

    //covers case where has recipients with usernames consisting of dashes only
    //                      recipients have usernames with 1 dash
    //                      recipients have usernames with > 1 dashes
    //                      recipients have usernames with 0 periods
    @Test
    public void testParseValidEmailAddressesUsernameConsistsOfDashesOnly(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String input = "-@mit.edu.cow";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeEmail(input);
        assertEquals("list expression not as expected", expected, actual);
        
        final String inputTwo = "---------------------@mit.edu.wow.qool";
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeEmail(inputTwo);
        assertEquals("second list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where has recipient with usernames with > 1 letters
    //                      recipient with usernames with > 1 digits
    //                      recipient with usernames with > 1 periods
    //                      recipient with usernames with > 1 underscores
    //                      recipient with usernames with > 1 dashes
    @Test
    public void testParseValidEmailAddressMixedUsername1(){
        final String input = "_a1.-qsfijwmkl43u...a-@mit.edu.cow";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeEmail(input);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where has recipient with usernames with > 1 letters
    //                      recipient with usernames with > 1 digits
    //                      recipient with usernames with > 1 periods
    //                      recipient with usernames with > 1 underscores
    //                      recipient with usernames with > 1 dashes
    @Test
    public void testParseValidEmailAddressMixedUsername2(){
        final String inputTwo = "A-1r-34f----...1.-bB@mit.edu.wow.qool";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeEmail(inputTwo);
        assertEquals("second list expression not as expected", expectedTwo, actualTwo);
        
    }
    //covers case where recipients with domain names consisting of letters only
    //                  recipients have domain names with 1 letter
    //                  recipients have domain names with > 1 letters
    //                  recipients have domain names with 0 underscores
    @Test
    public void testParseValidEmailAddressesDomainNameConsistsOfLettersOnly(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String input = "wut@m";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeEmail(input);
        assertEquals("list expression not as expected", expected, actual);
        
        final String inputTwo = "wutwutwut.wutwutwut@cowcowcow.cowcowcow";
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeEmail(inputTwo);
        assertEquals("second list expression not as expected", expectedTwo, actualTwo);
    }
    
    
    //covers case where has recipients with domain consisting of digits only
    //                  recipients have domain names with 1 digit
    //                  recipients have domain names with > 1 digits
    //                  recipients have domain names with 0 letters 
    @Test
    public void testParseValidEmailAddressesDomainNameConsistsOfDigitsOnly(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String input = "-@1234567890";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeEmail(input);
        assertEquals("list expression not as expected", expected, actual);
        
        final String inputTwo = "---------------------@6";
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeEmail(inputTwo);
        assertEquals("second list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where has recipients with domain names consisting of underscores only
    //                      recipients have domain names  with 1 underscores
    //                      recipients have domain names with > 1 underscores
    //                      recipients have domain names with 0 dashes
    @Test
    public void testParseValidEmailAddressesDomainNameConsistsOfUnderscoresOnly(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String input = "-@_________";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeEmail(input);
        assertEquals("list expression not as expected", expected, actual);
        
        final String inputTwo = "---------------------@_";
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeEmail(inputTwo);
        assertEquals("second list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where has recipients with domain names consisting of periods only
    //                      recipients have domain names with 1 period
    //                      recipients have domain names with > 1 periods
    //                      recipients have domain names with 0 digits
    @Test
    public void testParseValidEmailAddressesDomainNameConsistsOfPeriodsOnly(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String input = "-@.";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeEmail(input);
        assertEquals("list expression not as expected", expected, actual);
        
        final String inputTwo = "---------------------@......";
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeEmail(inputTwo);
        assertEquals("second list expression not as expected", expectedTwo, actualTwo);
    }

    //covers case where has recipients with domain names consisting of dashes only
    //                      recipients have domain names with 1 dash
    //                      recipients have domain names with > 1 dashes
    //                      recipients have domain names with 0 periods
    @Test
    public void testParseValidEmailAddressesDomainNameConsistsOfDashesOnly(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String input = "-@mit.edu------";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeEmail(input);
        assertEquals("list expression not as expected", expected, actual);
        
        final String inputTwo = "---------------------@-";
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeEmail(inputTwo);
        assertEquals("second list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where has recipient with domain names with > 1 letters
    //                      recipient with domain names with > 1 digits
    //                      recipient with domain names with > 1 periods
    //                      recipient with domain names with > 1 underscores
    //                      recipient with domain names with > 1 dashes
    @Test
    public void testParseValidEmailAddressMixedDomainName1(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String input = "-@mit.edu.cow._-213rrvxvWQDhgh...";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeEmail(input);
        assertEquals("list expression not as expected", expected, actual);

    }
    
    //covers case where has recipient with domain names with > 1 letters
    //                      recipient with domain names with > 1 digits
    //                      recipient with domain names with > 1 periods
    //                      recipient with domain names with > 1 underscores
    //                      recipient with domain names with > 1 dashes
    @Test
    public void testParseValidEmailAddressMixedDomainName2(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String inputTwo = "---------------------@mit._-_-_-234.cow";
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeEmail(inputTwo);
        assertEquals("second list expression not as expected", expectedTwo, actualTwo);
    }

    //covers case where has empty parameters "hello@mit.edu,,bye@mit.edu"
    //                  has trailing comma
    @Test
    public void testParseValidEmailAddressEmptyParameters(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String input = "hellothere@mit,    , o@o.o.o.o.o.o,";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmpty();
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmpty();
        ListExpression expectedOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression expectedTwo = ListExpression.makeUnion(expectedOne, emailThree);
        ListExpression expected = ListExpression.makeUnion(expectedTwo, emailFour);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where input a union of two unions (union , union)
    //            uses parentheses to change order of operations
    @Test
    public void testParseValidUnionOfUnionAndUnion(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        final String input = "(hellothere@mit, 123456@mit.edu), (o@o.o.o.o.o.o, 4323..fdgf@miiiiiiit.e.e.e.e.e)";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression expectedOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression expectedTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression expected = ListExpression.makeUnion(expectedOne, expectedTwo);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where input a union of two differences (difference , difference)
    @Test
    public void testParseValidUnionOfDifferenceAndDifference(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "cow ! grass , grass ! cow";
        ListExpression leftDifference = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression rightDifference = ListExpression.makeDifference(mailingListTwo, mailingListOne);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeUnion(leftDifference, rightDifference);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where input a union of two intersections (intersection , intersections)
    @Test
    public void testParseValidUnionOfIntersectionAndIntersection(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "cow * grass , grass * cow";
        ListExpression leftIntersection = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression rightIntersection = ListExpression.makeIntersection(mailingListTwo, mailingListOne);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeUnion(leftIntersection, rightIntersection);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where input a union of two mailing lists (mailing lists , mailing lists)
    @Test
    public void testParseValidUnionOfMailingListAndMailingList(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);

        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        final String input = "cow, grass";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeUnion(mailingListOne, mailingListTwo);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where input a union of empty , mailing list / mailing list , empty
    @Test
    public void testParseValidUnionOfEmptyAndMailingList(){ 
        ListExpression emailZero = ListExpression.makeEmpty();
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        final String input = " , cow";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeUnion(emailZero, mailingListOne);
        assertEquals("list expression not as expected", expected, actual);
        
        final String inputTwo = " cow, ";
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeUnion(mailingListOne,emailZero);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where input a union of email , mailing list / mailing list , email  
    @Test
    public void testParseValidUnionOfEmailAndMailingList(){ 
        ListExpression emailZero = ListExpression.makeEmail("hellothere@mit");
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        final String input = "hellothere@mit , cow";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeUnion(emailZero, mailingListOne);
        assertEquals("list expression not as expected", expected, actual);
        
        final String inputTwo = " cow,hellothere@mit ";
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeUnion(mailingListOne,emailZero);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where input a union of empty , difference / difference, empty
    @Test
    public void testParseValidUnionOfDifferenceAndEmpty(){ 
        ListExpression emailZero = ListExpression.makeEmpty();

        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "cow ! grass , ";
        ListExpression leftDifference = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeUnion(leftDifference, emailZero);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = ", cow ! grass ";
        ListExpression rightDifference = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeUnion(emailZero, rightDifference);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where input a union of empty , intersection, intersection , empty
    @Test
    public void testParseValidUnionOfIntersectionAndEmpty(){ 
        ListExpression emailZero = ListExpression.makeEmpty();

        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "cow * grass , ";
        ListExpression leftDifference = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeUnion(leftDifference, emailZero);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = ", cow * grass ";
        ListExpression rightDifference = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeUnion(emailZero, rightDifference);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where input a union of email , difference / difference , email
    //            parentheses are used  
    @Test
    public void testParseValidUnionOfDifferenceAndEmail(){ 
        ListExpression emailZero = ListExpression.makeEmail("hellothere@mit");

        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "( cow ! grass ) , (hellothere@mit)";
        ListExpression leftDifference = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeUnion(leftDifference, emailZero);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "(hellothere@mit), ( cow ! grass ) ";
        ListExpression rightDifference = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeUnion(emailZero, rightDifference);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where input a union of email , intersection / intersection , email
    //            parentheses are used  
    @Test
    public void testParseValidUnionOfIntersectionAndEmail(){ 
        ListExpression emailZero = ListExpression.makeEmail("hellothere@mit");

        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "( cow * grass)  , (hellothere@mit)";
        ListExpression left = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeUnion(left, emailZero);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "(hellothere@mit),  (cow * grass)  ";
        ListExpression right = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeUnion(emailZero, right);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where input a union of mailing list , union / union , mailing list
    //            parentheses are used  
    @Test
    public void testParseValidUnionOfMailingListAndUnion(){ 
        ListExpression emailZero = ListExpression.makeEmail("hellothere@mit");

        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        ListExpression unionFive = ListExpression.makeUnion(emailZero, emailThree);
        String input = "( cow , (hellothere@mit,o@o.o.o.o.o.o))";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeUnion(mailingListOne,unionFive);
        assertEquals("list expression not as expected 1", expected, actual);
        
        String inputTwo = "(hellothere@mit,o@o.o.o.o.o.o),  (cow)  ";
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeUnion(unionFive,mailingListOne);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where input a union of mailing list , intersection / intersection , mailing list
    //            parentheses are used  
    @Test
    public void testParseValidUnionOfMailingListAndIntersection(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "( cow * grass)  , (cow)";
        ListExpression left = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeUnion(left, mailingListOne);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "(grass),  (cow * grass)  ";
        ListExpression right = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeUnion(mailingListTwo, right);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where input a union of mailing list , difference / difference , mailing list
    //            parentheses are used  
    @Test
    public void testParseValidUnionOfMailingListAndDifference(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "( cow ! grass)  , (cow)";
        ListExpression left = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeUnion(left, mailingListOne);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "(grass),  (cow ! grass)  ";
        ListExpression right = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeUnion(mailingListTwo, right);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
    
  //covers case where input a union of union , difference / difference , union
    //            parentheses are used  
    @Test
    public void testParseValidUnionOfUnionAndDifference(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        
        ListExpression emailFive = ListExpression.makeEmail("hello----there@mit");
        ListExpression emailSix = ListExpression.makeEmail("123456-----@mit.edu");
        ListExpression unionEmail = ListExpression.makeUnion(emailFive, emailSix);

        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "( cow ! grass)  , (hello----there@mit, 123456-----@mit.edu)";
        ListExpression left = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression expected = ListExpression.makeUnion(left, unionEmail);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "(hello----there@mit, 123456-----@mit.edu),  (cow ! grass)  ";
        ListExpression right = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeUnion(unionEmail, right);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where input a union , intersection / intersection , union
    //            parentheses are used  
    @Test
    public void testParseValidUnionOfUnionAndIntersection(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        
        ListExpression emailFive = ListExpression.makeEmail("hello----there@mit");
        ListExpression emailSix = ListExpression.makeEmail("123456-----@mit.edu");
        ListExpression unionEmail = ListExpression.makeUnion(emailFive, emailSix);

        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "( cow * grass)  , (hello----there@mit, 123456-----@mit.edu)";
        ListExpression left = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression expected = ListExpression.makeUnion(left, unionEmail);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "(hello----there@mit, 123456-----@mit.edu),  (cow * grass)  ";
        ListExpression right = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeUnion(unionEmail, right);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where input a difference of difference ! intersection / intersection ! difference
    @Test
    public void testParseValidUnionOfDifferenceAndIntersection(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        
        ListExpression emailFive = ListExpression.makeEmail("hello----there@mit");
        ListExpression emailSix = ListExpression.makeEmail("hello----there@mit");
        ListExpression unionEmail = ListExpression.makeIntersection(emailFive, emailSix);

        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "cow ! grass  , hello----there@mit * hello----there@mit";
        ListExpression left = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression expected = ListExpression.makeUnion(left, unionEmail);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "hello----there@mit * hello----there@mit,  cow ! grass ";
        ListExpression right = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeUnion(unionEmail, right);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where input a difference of two empty recipients (empty ! empty)
    @Test
    public void testParseValidDifferenceOfEmptyAndEmpty(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        final String input = "() ! ()";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression emailOne = ListExpression.makeEmpty();
        ListExpression emailTwo = ListExpression.makeEmpty();
        ListExpression expected = ListExpression.makeDifference(emailOne, emailTwo);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where input a difference of two email recipients (email ! email)
    @Test
    public void testParseValidDifferenceOfEmailAndEmail(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        final String input = "hellothere@mit ! (123456@mit.edu)";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression expected = ListExpression.makeDifference(emailOne, emailTwo);
        assertEquals("list expression not as expected", expected, actual);
    }
    //covers case where input a difference of two unions (union ! union)
    //            uses parentheses to change order of operations
    @Test
    public void testParseValidDifferenceOfUnionAndUnion(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        final String input = "(hellothere@mit, 123456@mit.edu) ! (o@o.o.o.o.o.o, 4323..fdgf@miiiiiiit.e.e.e.e.e)";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression expectedOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression expectedTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression expected = ListExpression.makeDifference(expectedOne, expectedTwo);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where input a difference of two differences (difference ! difference)
    @Test
    public void testParseValidDifferenceOfDifferenceAndDifference(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "cow ! grass ! grass ! cow";
        ListExpression leftDifference = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression rightDifference = ListExpression.makeDifference(leftDifference, mailingListTwo);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeDifference(rightDifference, mailingListOne);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where input a difference of two differences (difference ! difference)
    //uses parentheses
    @Test
    public void testParseValidDifferenceOfDifferenceAndDifferenceParentheses(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "(cow ! grass) ! (grass ! cow)";
        ListExpression leftDifference = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression rightDifference = ListExpression.makeDifference(mailingListTwo, mailingListOne);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeDifference(leftDifference, rightDifference);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where input a difference of two intersections (intersection ! intersections)
    @Test
    public void testParseValidDifferenceOfIntersectionAndIntersection(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "(cow * grass) ! (grass * cow)";
        ListExpression leftIntersection = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression rightIntersection = ListExpression.makeIntersection(mailingListTwo, mailingListOne);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeDifference(leftIntersection, rightIntersection);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where input a difference of two mailing lists (mailing lists ! mailing lists)
    @Test
    public void testParseValidDifferenceOfMailingListAndMailingList(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);

        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        final String input = "cow ! grass";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where input a difference of empty ! email / email ! empty
    @Test
    public void testParseValidDifferenceOfEmailAndEmpty(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        final String input = "hellothere@mit ! ()";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmpty();
        ListExpression expected = ListExpression.makeDifference(emailOne, emailTwo);
        assertEquals("list expression not as expected", expected, actual);
        
        final String inputTwo = "() ! hellothere@mit  ";
        ListExpression expectedTwo = ListExpression.makeDifference(emailTwo, emailOne);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where input a difference of empty ! mailing list / mailing list ! empty
    @Test
    public void testParseValidDifferenceOfEmptyAndMailingList(){ 
        ListExpression emailZero = ListExpression.makeEmpty();
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        final String input = " ! cow";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeDifference(emailZero, mailingListOne);
        assertEquals("list expression not as expected", expected, actual);
        
        final String inputTwo = " cow! ";
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeDifference(mailingListOne,emailZero);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where input a difference of email ! mailing list / mailing list ! email  
    @Test
    public void testParseValidDifferenceOfEmailAndMailingList(){ 
        ListExpression emailZero = ListExpression.makeEmail("hellothere@mit");
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        final String input = "hellothere@mit ! cow";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeDifference(emailZero, mailingListOne);
        assertEquals("list expression not as expected", expected, actual);
        
        final String inputTwo = " cow!hellothere@mit ";
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeDifference(mailingListOne,emailZero);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }

    //covers case where input a difference of empty ! difference / difference ! empty
    @Test
    public void testParseValidDifferenceOfDifferenceAndEmpty(){ 
        ListExpression emailZero = ListExpression.makeEmpty();

        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "cow ! grass ! ";
        ListExpression leftDifference = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeDifference(leftDifference, emailZero);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "! cow ! grass ";
        ListExpression rightDifference = ListExpression.makeDifference(emailZero, mailingListOne);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeDifference(rightDifference, mailingListTwo);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where input a difference of empty ! intersection, intersection ! empty
    @Test
    public void testParseValidDifferenceOfIntersectionAndEmpty(){ 
        ListExpression emailZero = ListExpression.makeEmpty();

        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "cow * grass ! ";
        ListExpression leftDifference = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeDifference(leftDifference, emailZero);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "! cow * grass ";
        ListExpression rightDifference = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeDifference(emailZero, rightDifference);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }

    //covers case where input a difference of email ! difference / difference ! email
    //            parentheses are used  
    @Test
    public void testParseValidDifferenceOfDifferenceAndEmail(){ 
        ListExpression emailZero = ListExpression.makeEmail("hellothere@mit");

        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "( cow ! grass ) ! (hellothere@mit)";
        ListExpression leftDifference = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeDifference(leftDifference, emailZero);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "(hellothere@mit) ! ( cow ! grass ) ";
        ListExpression rightDifference = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeDifference(emailZero, rightDifference);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
    
    
    //covers case where input a difference of email ! intersection / intersection ! email
    //            parentheses are used  
    @Test
    public void testParseValidDifferenceOfIntersectionAndEmail(){ 
        ListExpression emailZero = ListExpression.makeEmail("hellothere@mit");

        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "( cow * grass)  ! (hellothere@mit)";
        ListExpression left = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeDifference(left, emailZero);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "(hellothere@mit)!  (cow * grass)  ";
        ListExpression right = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeDifference(emailZero, right);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
   
    //covers case where input a difference of mailing list ! union / union ! mailing list
    //            parentheses are used  
    @Test
    public void testParseValidDifferenceOfMailingListAndUnion(){ 
        ListExpression emailZero = ListExpression.makeEmail("hellothere@mit");

        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        ListExpression unionFive = ListExpression.makeUnion(emailZero, emailThree);
        String input = "( cow ! (hellothere@mit,o@o.o.o.o.o.o))";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeDifference(mailingListOne,unionFive);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "(hellothere@mit,o@o.o.o.o.o.o)!  (cow)  ";
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeDifference(unionFive,mailingListOne);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
   
    
    //covers case where input a difference of mailing list ! intersection / intersection ! mailing list
    //            parentheses are used  
    @Test
    public void testParseValidDifferenceOfMailingListAndIntersection(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "( cow * grass)  ! (cow)";
        ListExpression left = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeDifference(left, mailingListOne);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "(grass)!  (cow * grass)  ";
        ListExpression right = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeDifference(mailingListTwo, right);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where input a difference of mailing list ! difference / difference ! mailing list
    //            parentheses are used  
    @Test
    public void testParseValidDifferenceOfMailingListAndDifference(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "( cow ! grass)  ! (cow)";
        ListExpression left = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeDifference(left, mailingListOne);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "(grass)!  (cow ! grass)  ";
        ListExpression right = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeDifference(mailingListTwo, right);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }

  //covers case where input a difference of union ! difference / difference ! union
    //            parentheses are used  
    @Test
    public void testParseValidDifferenceOfUnionAndDifference(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        
        ListExpression emailFive = ListExpression.makeEmail("hello----there@mit");
        ListExpression emailSix = ListExpression.makeEmail("123456-----@mit.edu");
        ListExpression unionEmail = ListExpression.makeUnion(emailFive, emailSix);

        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "( cow ! grass)  ! (hello----there@mit, 123456-----@mit.edu)";
        ListExpression left = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression expected = ListExpression.makeDifference(left, unionEmail);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "(hello----there@mit, 123456-----@mit.edu)!  (cow ! grass)  ";
        ListExpression right = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeDifference(unionEmail, right);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where input a difference of union ! intersection / intersection ! union
    //            parentheses are used  
    @Test
    public void testParseValidDifferenceOfUnionAndIntersection(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        
        ListExpression emailFive = ListExpression.makeEmail("hello----there@mit");
        ListExpression emailSix = ListExpression.makeEmail("123456-----@mit.edu");
        ListExpression unionEmail = ListExpression.makeUnion(emailFive, emailSix);

        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "( cow * grass)  ! (hello----there@mit, 123456-----@mit.edu)";
        ListExpression left = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression expected = ListExpression.makeDifference(left, unionEmail);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "(hello----there@mit, 123456-----@mit.edu)!  (cow * grass)  ";
        ListExpression right = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeDifference(unionEmail, right);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }

    //covers case where input a difference of union ! intersection / intersection ! union
    @Test 
    public void testParseValidDifferenceOfDifferenceAndIntersection(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        
        ListExpression emailFive = ListExpression.makeEmail("hello----there@mit");
        ListExpression emailSix = ListExpression.makeEmail("hello----there@mit");
        ListExpression unionEmail = ListExpression.makeIntersection(emailFive, emailSix);

        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "cow ! grass  ! hello----there@mit * hello----there@mit";
        ListExpression left = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression expected = ListExpression.makeDifference(left, unionEmail);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        assertEquals("list expression not as expected 1", expected, actual);
        
        String inputTwo = "hello----there@mit * hello----there@mit !  cow ! grass ";
        ListExpression right = ListExpression.makeDifference(unionEmail, mailingListOne);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeDifference(right, mailingListTwo);
        assertEquals("list expression not as expected 2", expectedTwo, actualTwo);
    }
    
    //covers case where input an intersection of two empty recipients (empty * empty)
    @Test
    public void testParseValidIntersectionOfEmptyAndEmpty(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        final String input = "() * ()";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression emailOne = ListExpression.makeEmpty();
        ListExpression emailTwo = ListExpression.makeEmpty();
        ListExpression expected = ListExpression.makeIntersection(emailOne, emailTwo);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where input an intersection of two email recipients (email * email)
    @Test
    public void testParseValidIntersectionOfEmailAndEmail(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        final String input = "hellothere@mit * (123456@mit.edu)";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression expected = ListExpression.makeIntersection(emailOne, emailTwo);
        assertEquals("list expression not as expected", expected, actual);
    }
    //covers case where input an intersection of two unions (union * union)
    //            uses parentheses to change order of operations
    @Test
    public void testParseValidIntersectionOfUnionAndUnion(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        final String input = "(hellothere@mit, 123456@mit.edu) * (o@o.o.o.o.o.o, 4323..fdgf@miiiiiiit.e.e.e.e.e)";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression expectedOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression expectedTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression expected = ListExpression.makeIntersection(expectedOne, expectedTwo);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where input an intersection of two differences (difference * difference)
    @Test
    public void testParseValidIntersectionOfDifferenceAndDifference(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "(cow ! grass) * (grass ! cow)";
        ListExpression leftDifference = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression rightDifference = ListExpression.makeDifference(mailingListTwo,mailingListOne);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeIntersection(leftDifference, rightDifference);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    
    //covers case where input an intersection of two intersections (intersection * intersections)
    @Test
    public void testParseValidIntersectionOfIntersectionAndIntersection(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "(cow * grass) * (grass * cow)";
        ListExpression leftIntersection = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression rightIntersection = ListExpression.makeIntersection(mailingListTwo, mailingListOne);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeIntersection(leftIntersection, rightIntersection);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where input an intersection of two mailing lists (mailing lists * mailing lists)
    @Test
    public void testParseValidIntersectionOfMailingListAndMailingList(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);

        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        final String input = "cow * grass";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where input an intersection of empty * mailing list / mailing list * empty
    @Test
    public void testParseValidIntersectionOfEmptyAndMailingList(){ 
        ListExpression emailZero = ListExpression.makeEmpty();
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        final String input = " * cow";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeIntersection(emailZero, mailingListOne);
        assertEquals("list expression not as expected", expected, actual);
        
        final String inputTwo = " cow* ";
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeIntersection(mailingListOne,emailZero);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where input a difference of empty * email / email * empty
    @Test
    public void testParseValidIntersectionOfEmailAndEmpty(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        final String input = "hellothere@mit * ()";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmpty();
        ListExpression expected = ListExpression.makeIntersection(emailOne, emailTwo);
        assertEquals("list expression not as expected", expected, actual);
        
        final String inputTwo = "() * hellothere@mit  ";
        ListExpression expectedTwo = ListExpression.makeIntersection(emailTwo, emailOne);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where input an intersection of email * mailing list / mailing list * email  
    @Test
    public void testParseValidIntersectionOfEmailAndMailingList(){ 
        ListExpression emailZero = ListExpression.makeEmail("hellothere@mit");
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        final String input = "hellothere@mit * cow";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeIntersection(emailZero, mailingListOne);
        assertEquals("list expression not as expected", expected, actual);
        
        final String inputTwo = " cow*hellothere@mit ";
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeIntersection(mailingListOne,emailZero);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }

    //covers case where input an intersection of empty * difference / difference * empty
    @Test
    public void testParseValidIntersectionOfDifferenceAndEmpty(){ 
        ListExpression emailZero = ListExpression.makeEmpty();

        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "(cow ! grass) * ";
        ListExpression leftDifference = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeIntersection(leftDifference, emailZero);
        assertEquals("list expression not as expected 1 ", expected, actual);
        
        String inputTwo = "* (cow ! grass ) ";
        ListExpression right = ListExpression.makeDifference(mailingListOne,mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeIntersection(emailZero, right);
        assertEquals("list expression not as expected 2", expectedTwo, actualTwo);
    }
    
    //covers case where input an intersection of empty * intersection, intersection * empty
    @Test
    public void testParseValidIntersectionOfIntersectionAndEmpty(){ 
        ListExpression emailZero = ListExpression.makeEmpty();

        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "(cow * grass )* ";
        ListExpression leftDifference = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeIntersection(leftDifference, emailZero);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "* ((cow) * grass) ";
        ListExpression rightDifference = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeIntersection(emailZero, rightDifference);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }

    //covers case where input an intersection of email * difference / difference * email
    //            parentheses are used  
    @Test
    public void testParseValidIntersectionOfDifferenceAndEmail(){ 
        ListExpression emailZero = ListExpression.makeEmail("hellothere@mit");

        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "( cow ! grass ) * (hellothere@mit)";
        ListExpression leftDifference = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeIntersection(leftDifference, emailZero);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "(hellothere@mit) * ( cow ! grass ) ";
        ListExpression rightDifference = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeIntersection(emailZero, rightDifference);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
    
    
    //covers case where input an intersection of email * intersection / intersection * email
    //            parentheses are used  
    @Test
    public void testParseValidIntersectionOfIntersectionAndEmail(){ 
        ListExpression emailZero = ListExpression.makeEmail("hellothere@mit");

        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "( cow * grass)  * (hellothere@mit)";
        ListExpression left = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeIntersection(left, emailZero);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "(hellothere@mit)*  (cow * grass)  ";
        ListExpression right = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeIntersection(emailZero, right);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
   
    //covers case where input an intersection of mailing list * union / union * mailing list
    //            parentheses are used  
    @Test
    public void testParseValidIntersectionOfMailingListAndUnion(){ 
        ListExpression emailZero = ListExpression.makeEmail("hellothere@mit");

        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        ListExpression unionFive = ListExpression.makeUnion(emailZero, emailThree);
        String input = "( cow * (hellothere@mit,o@o.o.o.o.o.o))";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeIntersection(mailingListOne,unionFive);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "(hellothere@mit,o@o.o.o.o.o.o)*  (cow)  ";
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeIntersection(unionFive,mailingListOne);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
   
    
    //covers case where input an intersection of mailing list * intersection / intersection * mailing list
    //            parentheses are used  
    @Test
    public void testParseValidIntersectionOfMailingListAndIntersection(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "( cow * grass)  * (cow)";
        ListExpression left = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeIntersection(left, mailingListOne);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "(grass)*  (cow * grass)  ";
        ListExpression right = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeIntersection(mailingListTwo, right);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where input an intersection of mailing list * difference / difference * mailing list
    //            parentheses are used  
    @Test
    public void testParseValidIntersectionOfMailingListAndDifference(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "( cow ! grass)  * (cow)";
        ListExpression left = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeIntersection(left, mailingListOne);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "(grass)*  (cow ! grass)  ";
        ListExpression right = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeIntersection(mailingListTwo, right);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }

  //covers case where input an intersection of union * difference / difference * union
    //            parentheses are used  
    @Test
    public void testParseValidIntersectionOfUnionAndDifference(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        
        ListExpression emailFive = ListExpression.makeEmail("hello----there@mit");
        ListExpression emailSix = ListExpression.makeEmail("123456-----@mit.edu");
        ListExpression unionEmail = ListExpression.makeUnion(emailFive, emailSix);

        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "( cow ! grass)  * (hello----there@mit, 123456-----@mit.edu)";
        ListExpression left = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression expected = ListExpression.makeIntersection(left, unionEmail);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "(hello----there@mit, 123456-----@mit.edu)*  (cow ! grass)  ";
        ListExpression right = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeIntersection(unionEmail, right);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where input an intersection of union * intersection / intersection * union
    //            parentheses are used  
    @Test
    public void testParseValidIntersectionOfUnionAndIntersection(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        
        ListExpression emailFive = ListExpression.makeEmail("hello----there@mit");
        ListExpression emailSix = ListExpression.makeEmail("123456-----@mit.edu");
        ListExpression unionEmail = ListExpression.makeUnion(emailFive, emailSix);

        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "( cow * grass)  * (hello----there@mit, 123456-----@mit.edu)";
        ListExpression left = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression expected = ListExpression.makeIntersection(left, unionEmail);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "(hello----there@mit, 123456-----@mit.edu)*  (cow * grass)  ";
        ListExpression right = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeIntersection(unionEmail, right);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }

    //covers case where input an intersection of union * intersection / intersection * union
    @Test 
    public void testParseValidIntersectionOfDifferenceAndIntersection(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        
        ListExpression emailFive = ListExpression.makeEmail("hello----there@mit");
        ListExpression emailSix = ListExpression.makeEmail("hello----there@mit");
        ListExpression unionEmail = ListExpression.makeIntersection(emailFive, emailSix);

        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "(cow ! grass)  * (hello----there@mit * hello----there@mit)";
        ListExpression left = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression expected = ListExpression.makeIntersection(left, unionEmail);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        assertEquals("list expression not as expected 1", expected, actual);
        
        String inputTwo = "(hello----there@mit * hello----there@mit) *  (cow ! grass) ";
        ListExpression right = ListExpression.makeDifference(mailingListOne,mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeIntersection(unionEmail, right);
        assertEquals("list expression not as expected 2", expectedTwo, actualTwo);
    }
   
    
    //covers case where input a sequence of two empty recipients (empty ; empty)
    @Test
    public void testParseValidSequenceOfEmptyAndEmpty(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        final String input = "() ; ()";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression emailOne = ListExpression.makeEmpty();
        ListExpression emailTwo = ListExpression.makeEmpty();
        ListExpression expected = ListExpression.makeSequence(emailOne, emailTwo);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where input a sequence of two email recipients (email ; email)
    @Test
    public void testParseValidSequenceOfEmailAndEmail(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        final String input = "hellothere@mit ; (123456@mit.edu)";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression expected = ListExpression.makeSequence(emailOne, emailTwo);
        assertEquals("list expression not as expected", expected, actual);
    }
    //covers case where input a sequence of two unions (union ; union)
    //            uses parentheses to change order of operations
    @Test
    public void testParseValidSequenceOfUnionAndUnion(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        final String input = "(hellothere@mit, 123456@mit.edu) ; (o@o.o.o.o.o.o, 4323..fdgf@miiiiiiit.e.e.e.e.e)";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression expectedOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression expectedTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression expected = ListExpression.makeSequence(expectedOne, expectedTwo);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where input a sequence of two differences (difference ; difference)
    @Test
    public void testParseValidSequenceOfDifferenceAndDifference(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "(cow ! grass) ; (grass ! cow)";
        ListExpression leftDifference = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression rightDifference = ListExpression.makeDifference(mailingListTwo,mailingListOne);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeSequence(leftDifference, rightDifference);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    
    //covers case where input a sequence of two intersections (intersection ; intersections)
    @Test
    public void testParseValidSequenceOfIntersectionAndIntersection(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "(cow * grass) ; (grass * cow)";
        ListExpression leftIntersection = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression rightIntersection = ListExpression.makeIntersection(mailingListTwo, mailingListOne);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeSequence(leftIntersection, rightIntersection);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where input a sequence of two mailing lists (mailing lists ; mailing lists)
    @Test
    public void testParseValidSequenceOfMailingListAndMailingList(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);

        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        final String input = "cow ; grass";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeSequence(mailingListOne, mailingListTwo);
        assertEquals("list expression not as expected", expected, actual);
    }
   
    //covers case where input a sequence of two mailing list definitions (mailing lists ; mailing lists)
    @Test
    public void testParseValidSequenceOfMailingListDefinitionAndMailingListDefinition(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(unionOne, emailThree);
        ListExpression union = ListExpression.makeUnion(unionTwo, emailFour);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);

        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        final String input = "cow = hellothere@mit, 123456@mit.edu, o@o.o.o.o.o.o, 4323..fdgf@miiiiiiit.e.e.e.e.e ;  "
                + "grass = o@o.o.o.o.o.o , 4323..fdgf@miiiiiiit.e.e.e.e.e";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeSequence(mailingListOne, mailingListTwo);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    
    //covers case where input a sequence of empty ; union / union ; empty
    @Test
    public void testParseValidSequenceOfEmailAndEmpty(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        final String input = "hellothere@mit ; ()";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmpty();
        ListExpression expected = ListExpression.makeSequence(emailOne, emailTwo);
        assertEquals("list expression not as expected", expected, actual);
        
        final String inputTwo = "() ; hellothere@mit  ";
        ListExpression expectedTwo = ListExpression.makeSequence(emailTwo, emailOne);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
        
    }
    //covers case where input a sequence of two unions (union ; union)
    //            uses parentheses to change order of operations
    @Test
    public void testParseValidSequenceOfEmptyAndUnion(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        final String input = " ; (o@o.o.o.o.o.o, 4323..fdgf@miiiiiiit.e.e.e.e.e)";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression expectedLeft = ListExpression.makeEmpty();
        ListExpression expectedRight = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression expected = ListExpression.makeSequence(expectedLeft, expectedRight);
        assertEquals("list expression not as expected", expected, actual);
        
        final String inputTwo = "  (o@o.o.o.o.o.o, 4323..fdgf@miiiiiiit.e.e.e.e.e)  ;";
        ListExpression expectedTwo = ListExpression.makeSequence(expectedRight, expectedLeft);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where input a sequence of empty ; mailing list / mailing list ; empty
    @Test
    public void testParseValidSequenceOfEmptyAndMailingList(){ 
        ListExpression emailZero = ListExpression.makeEmpty();
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        final String input = " ; cow";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeSequence(emailZero, mailingListOne);
        assertEquals("list expression not as expected", expected, actual);
        
        final String inputTwo = " cow; ";
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeSequence(mailingListOne,emailZero);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where input a sequence of email ; mailing list / mailing list ; email  
    @Test
    public void testParseValidSequenceOfEmailAndMailingList(){ 
        ListExpression emailZero = ListExpression.makeEmail("hellothere@mit");
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        final String input = "hellothere@mit ; cow";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeSequence(emailZero, mailingListOne);
        assertEquals("list expression not as expected", expected, actual);
        
        final String inputTwo = " cow;hellothere@mit ";
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeSequence(mailingListOne,emailZero);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }


    //covers case where input a sequence of empty ; difference / difference ; empty
    @Test
    public void testParseValidSequenceOfDifferenceAndEmpty(){ 
        ListExpression emailZero = ListExpression.makeEmpty();

        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "(cow ! grass) ; ";
        ListExpression leftDifference = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeSequence(leftDifference, emailZero);
        assertEquals("list expression not as expected 1 ", expected, actual);
        
        String inputTwo = "; (cow ! grass ) ";
        ListExpression right = ListExpression.makeDifference(mailingListOne,mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeSequence(emailZero, right);
        assertEquals("list expression not as expected 2", expectedTwo, actualTwo);
    }
    
    //covers case where input a sequence of empty ; intersection, intersection ; empty
    @Test
    public void testParseValidSequenceOfIntersectionAndEmpty(){ 
        ListExpression emailZero = ListExpression.makeEmpty();

        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "(cow * grass ); ";
        ListExpression leftDifference = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeSequence(leftDifference, emailZero);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "; ((cow) * grass) ";
        ListExpression rightDifference = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeSequence(emailZero, rightDifference);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }

    //covers case where input a sequence of email ; difference / difference ; email
    //            parentheses are used  
    @Test
    public void testParseValidSequenceOfDifferenceAndEmail(){ 
        ListExpression emailZero = ListExpression.makeEmail("hellothere@mit");

        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "( cow ! grass ) ; (hellothere@mit)";
        ListExpression leftDifference = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeSequence(leftDifference, emailZero);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "(hellothere@mit) ; ( cow ! grass ) ";
        ListExpression rightDifference = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeSequence(emailZero, rightDifference);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
    
    
    //covers case where input a sequence of email ; intersection / intersection ; email
    //            parentheses are used  
    @Test
    public void testParseValidSequenceOfIntersectionAndEmail(){ 
        ListExpression emailZero = ListExpression.makeEmail("hellothere@mit");

        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "( cow * grass)  ; (hellothere@mit)";
        ListExpression left = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeSequence(left, emailZero);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "(hellothere@mit);  (cow * grass)  ";
        ListExpression right = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeSequence(emailZero, right);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
   
    //covers case where input a sequence of mailing list ; union / union ; mailing list
    //            parentheses are used  
    @Test
    public void testParseValidSequenceOfMailingListAndUnion(){ 
        ListExpression emailZero = ListExpression.makeEmail("hellothere@mit");

        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        ListExpression unionFive = ListExpression.makeUnion(emailZero, emailThree);
        String input = "( cow ; (hellothere@mit,o@o.o.o.o.o.o))";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeSequence(mailingListOne,unionFive);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "(hellothere@mit,o@o.o.o.o.o.o);  (cow)  ";
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeSequence(unionFive,mailingListOne);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
   
    //covers case where input a sequence of mailing list ; intersection / intersection ; mailing list
    //            parentheses are used  
    @Test
    public void testParseValidSequenceOfMailingListAndIntersection(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "( cow * grass)  ; (cow)";
        ListExpression left = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeSequence(left, mailingListOne);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "(grass);  (cow * grass)  ";
        ListExpression right = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeSequence(mailingListTwo, right);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where input a sequence of mailing list ; difference / difference ; mailing list
    //            parentheses are used  
    @Test
    public void testParseValidSequenceOfMailingListAndDifference(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "( cow ! grass)  ; (cow)";
        ListExpression left = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeSequence(left, mailingListOne);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "(grass);  (cow ! grass)  ";
        ListExpression right = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeSequence(mailingListTwo, right);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }

  //covers case where input a sequence of union ; difference / difference ; union
    //            parentheses are used  
    @Test
    public void testParseValidSequenceOfUnionAndDifference(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        
        ListExpression emailFive = ListExpression.makeEmail("hello----there@mit");
        ListExpression emailSix = ListExpression.makeEmail("123456-----@mit.edu");
        ListExpression unionEmail = ListExpression.makeUnion(emailFive, emailSix);

        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "( cow ! grass)  ; (hello----there@mit, 123456-----@mit.edu)";
        ListExpression left = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression expected = ListExpression.makeSequence(left, unionEmail);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "(hello----there@mit, 123456-----@mit.edu);  (cow ! grass)  ";
        ListExpression right = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeSequence(unionEmail, right);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }

    //covers case where input a sequence of union ; intersection / intersection ; union
    //            parentheses are used  
    @Test
    public void testParseValidSequenceOfUnionAndIntersection(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        
        ListExpression emailFive = ListExpression.makeEmail("hello----there@mit");
        ListExpression emailSix = ListExpression.makeEmail("123456-----@mit.edu");
        ListExpression unionEmail = ListExpression.makeUnion(emailFive, emailSix);

        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "( cow * grass)  ; (hello----there@mit, 123456-----@mit.edu)";
        ListExpression left = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression expected = ListExpression.makeSequence(left, unionEmail);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "(hello----there@mit, 123456-----@mit.edu);  (cow * grass)  ";
        ListExpression right = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeSequence(unionEmail, right);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }

    //covers case where input a sequence of difference ; intersection / intersection ; difference
    @Test 
    public void testParseValidSequenceOfDifferenceAndIntersection(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        
        ListExpression emailFive = ListExpression.makeEmail("hello----there@mit");
        ListExpression emailSix = ListExpression.makeEmail("hello----there@mit");
        ListExpression unionEmail = ListExpression.makeIntersection(emailFive, emailSix);

        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        String input = "(cow ! grass)  ; (hello----there@mit * hello----there@mit)";
        ListExpression left = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression expected = ListExpression.makeSequence(left, unionEmail);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        assertEquals("list expression not as expected 1", expected, actual);
        
        String inputTwo = "(hello----there@mit * hello----there@mit) ;  (cow ! grass) ";
        ListExpression right = ListExpression.makeDifference(mailingListOne,mailingListTwo);
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeSequence(unionEmail, right);
        assertEquals("list expression not as expected 2", expectedTwo, actualTwo);
    }
    
    //covers case where input a sequence of empty ; mailing list definition / mailing list definition ; empty
    @Test
    public void testParseValidSequenceOfMailingListDefinitionAndEmpty(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(unionOne, emailThree);
        ListExpression union = ListExpression.makeUnion(unionTwo, emailFour);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        ListExpression emailEmpty = ListExpression.makeEmpty();
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        final String input = "cow = hellothere@mit, 123456@mit.edu, o@o.o.o.o.o.o, 4323..fdgf@miiiiiiit.e.e.e.e.e ;  ";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeSequence(mailingListOne, emailEmpty);
        assertEquals("list expression not as expected", expected, actual);
        
        final String inputTwo = "; cow = hellothere@mit, 123456@mit.edu, o@o.o.o.o.o.o, 4323..fdgf@miiiiiiit.e.e.e.e.e   ";
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeSequence(emailEmpty, mailingListOne);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where input a sequence of email ; mailing list definition / mailing list definition ; email
    @Test
    public void testParseValidSequenceOfMailingListDefinitionAndEmail(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");;
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(unionOne, emailThree);
        ListExpression union = ListExpression.makeUnion(unionTwo, emailFour);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        ListExpression emailSeparate = ListExpression.makeEmail("yayayayay@mit.edu");
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        final String input = "cow = hellothere@mit, 123456@mit.edu, o@o.o.o.o.o.o, 4323..fdgf@miiiiiiit.e.e.e.e.e ;  yayayayay@mit.edu";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeSequence(mailingListOne, emailSeparate);
        assertEquals("list expression not as expected", expected, actual);
        
        final String inputTwo = "yayayayay@mit.edu; cow = hellothere@mit, 123456@mit.edu, o@o.o.o.o.o.o, 4323..fdgf@miiiiiiit.e.e.e.e.e   ";
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeSequence(emailSeparate, mailingListOne);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
   
    
    //covers case where input a sequence of email ; union / union ; email
    @Test
    public void testParseValidSequenceOfEmailAndUnion(){ 
        ListExpression emailZero = ListExpression.makeEmail("hellothere@mit");

        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
    
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        String input = "( hellothere@mit ; (hellothere@mit,o@o.o.o.o.o.o))";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeSequence(emailZero,unionOne);
        assertEquals("list expression not as expected", expected, actual);
        
        String inputTwo = "(hellothere@mit,o@o.o.o.o.o.o);  (hellothere@mit)  ";
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeSequence(unionOne,emailZero);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }

    //covers case where input a sequence of mailing list ; mailing list definition / mailing list definition ; mailing list
    @Test
    public void testParseValidSequenceOfMailingListDefinitionAndMailingList(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");;
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(unionOne, emailThree);
        ListExpression union = ListExpression.makeUnion(unionTwo, emailFour);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("grass", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        final String input = "cow = hellothere@mit, 123456@mit.edu, o@o.o.o.o.o.o, 4323..fdgf@miiiiiiit.e.e.e.e.e ;  grass";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeSequence(mailingListOne, mailingListTwo);
        assertEquals("list expression not as expected", expected, actual);
        
        final String inputTwo = "grass; cow = hellothere@mit, 123456@mit.edu, o@o.o.o.o.o.o, 4323..fdgf@miiiiiiit.e.e.e.e.e   ";
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeSequence(mailingListTwo, mailingListOne);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where input a sequence of union ; mailing list definition / mailing list definition ; union
    @Test
    public void testParseValidSequenceOfMailingListDefinitionAndUnion(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");;
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(unionOne, emailThree);
        ListExpression union = ListExpression.makeUnion(unionTwo, emailFour);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        Map<String, ListExpression> defined = new HashMap<>();
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        final String input = "cow = hellothere@mit, 123456@mit.edu, o@o.o.o.o.o.o, 4323..fdgf@miiiiiiit.e.e.e.e.e ; hellothere@mit , 123456@mit.edu ";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeSequence(mailingListOne, unionOne);
        assertEquals("list expression not as expected", expected, actual);
        
        final String inputTwo = "hellothere@mit , 123456@mit.edu ; cow = hellothere@mit, 123456@mit.edu, o@o.o.o.o.o.o, 4323..fdgf@miiiiiiit.e.e.e.e.e   ";
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeSequence(unionOne, mailingListOne);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }

   
    //covers case where input a sequence of difference ; mailing list definition / mailing list definition ; difference
    @Test
    public void testParseValidSequenceOfMailingListDefinitionAndDifference(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");;
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression difference = ListExpression.makeDifference(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(unionOne, emailThree);
        ListExpression union = ListExpression.makeUnion(unionTwo, emailFour);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        Map<String, ListExpression> defined = new HashMap<>();
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        final String input = "cow = hellothere@mit, 123456@mit.edu, o@o.o.o.o.o.o, 4323..fdgf@miiiiiiit.e.e.e.e.e ; hellothere@mit ! 123456@mit.edu ";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeSequence(mailingListOne, difference);
        assertEquals("list expression not as expected", expected, actual);
        
        final String inputTwo = "hellothere@mit ! 123456@mit.edu ; cow = hellothere@mit, 123456@mit.edu, o@o.o.o.o.o.o, 4323..fdgf@miiiiiiit.e.e.e.e.e   ";
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeSequence(difference, mailingListOne);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
    
    //covers case where input a sequence of intersection ; mailing list definition / mailing list definition ; intersection
    @Test
    public void testParseValidSequenceOfMailingListDefinitionAndIntersection(){ 
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");;
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression intersection = ListExpression.makeIntersection(emailOne, emailOne);
        ListExpression unionTwo = ListExpression.makeUnion(unionOne, emailThree);
        ListExpression union = ListExpression.makeUnion(unionTwo, emailFour);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        Map<String, ListExpression> defined = new HashMap<>();
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        final String input = "cow = hellothere@mit, 123456@mit.edu, o@o.o.o.o.o.o, 4323..fdgf@miiiiiiit.e.e.e.e.e ; hellothere@mit * hellothere@mit ";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeSequence(mailingListOne, intersection);
        assertEquals("list expression not as expected", expected, actual);
        
        final String inputTwo = "hellothere@mit * hellothere@mit ; cow = hellothere@mit, 123456@mit.edu, o@o.o.o.o.o.o, 4323..fdgf@miiiiiiit.e.e.e.e.e   ";
        ListExpression actualTwo = ListExpressionParser.parse(inputTwo,definedMailingLists);
        ListExpression expectedTwo = ListExpression.makeSequence(intersection, mailingListOne);
        assertEquals("list expression not as expected", expectedTwo, actualTwo);
    }
    //covers case where list name consists of only letters
    //                  list of one email recipient
    @Test
    public void testParseValidEmailListNameOnlyLetters(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String name = "newlist";
        final String input = "newlist = yayay1234567890@m.i.t.e.d.u";
        ListExpression emailOne = ListExpression.makeEmail("yayay1234567890@m.i.t.e.d.u");
        ListExpression expected = ListExpression.makeMailingList(name, emailOne);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where list name consists of only digits
    //                  list of no email recipients
    @Test
    public void testParseValidEmailListNameOnlyDigits(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String name = "6031";
        final String input = "6031 = ";
        ListExpression emailOne = ListExpression.makeEmpty();
        ListExpression expected = ListExpression.makeMailingList(name, emailOne);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        assertEquals("list expression not as expected", expected, actual);
    }    
    
    //covers case where list name consists of only periods
    //                  0 dashes
    //                  1 period
    //                  list of no email recipients (empty)
    @Test
    public void testParseValidEmailListNameOnlyPeriods(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String name = ".";
        final String input = ". = ";
        ListExpression emailOne = ListExpression.makeEmpty();
        ListExpression expected = ListExpression.makeMailingList(name, emailOne);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where list name consists of only dashes
    //                  0 letters
    //                  0 digits
    //                  0 periods
    //                  > 1 dashes
    //                  list of one one email recipient
    @Test
    public void testParseValidEmailListNameOnlyDashes(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String name = "----";
        final String input = "---- = yayay1234567890@m.i.t.e.d.u";
        ListExpression emailOne = ListExpression.makeEmail("yayay1234567890@m.i.t.e.d.u");
        ListExpression expected = ListExpression.makeMailingList(name, emailOne);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where list name consists of only underscores
    //                  list of one one email recipient
    @Test
    public void testParseValidEmailListNameOnlyUnderscores(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String name = "____";
        final String input = "____ = yayay1234567890@m.i.t.e.d.u";
        ListExpression emailOne = ListExpression.makeEmail("yayay1234567890@m.i.t.e.d.u");
        ListExpression expected = ListExpression.makeMailingList(name, emailOne);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where list name consists of mixed
    //                  list of one one email recipient
    @Test
    public void testParseValidEmailListNameMixed(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String name = "--3424rfdc.2323sc";
        final String input = "--3424rfdc.2323sc = yayay1234567890@m.i.t.e.d.u";
        ListExpression emailOne = ListExpression.makeEmail("yayay1234567890@m.i.t.e.d.u");
        ListExpression expected = ListExpression.makeMailingList(name, emailOne);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where list name consists of mixed
    //                  list of one one email recipient
    //                  1 letter
    //                  1 digit
    //                  1 period
    //                  1 dash
    //                  1 underscore
    @Test
    public void testParseValidEmailListNameMixed2(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        final String name = "a1.-_";
        final String input = "a1.-_ = yayay1234567890@m.i.t.e.d.u";
        ListExpression emailOne = ListExpression.makeEmail("yayay1234567890@m.i.t.e.d.u");
        ListExpression expected = ListExpression.makeMailingList(name, emailOne);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where list name consists of mixed
    //                  list of one one email recipient
    //                  > 1 letter
    //                  > 1 digit
    //                  > 1 period
    //                  > 1 dash
    //                  > 1 underscore
    @Test
    public void testParseValidEmailListNameMixed3(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        final String name = "asfdsfsd12323...----____";
        final String input = "aSFDSFSD12323...----____ = yayay1234567890@m.i.t.e.d.u";
        ListExpression emailOne = ListExpression.makeEmail("yayay1234567890@m.i.t.e.d.u");
        ListExpression expected = ListExpression.makeMailingList(name, emailOne);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where list recipient field is a union
    @Test
    public void testParseValidEmailListUnion(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String name = "--3424rfdc.2323sc";
        final String input = "--3424rfdc.2323sc = yayay1234567890@m.i.t.e.d.u, cowcowcow@mit, cowcowcow@mit";
        ListExpression emailOne = ListExpression.makeEmail("yayay1234567890@m.i.t.e.d.u");
        ListExpression emailTwo = ListExpression.makeEmail("cowcowcow@mit");
        ListExpression emailThree = ListExpression.makeEmail("cowcowcow@mit");

        ListExpression union = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(union, emailThree);

        ListExpression expected = ListExpression.makeMailingList(name, unionTwo);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where list recipient field is a union of mailing lists
    @Test
    public void testParseValidEmailListUnionOfMailingLists(){
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);

        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        final String input = "heythere = cow , grass";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expectedOne = ListExpression.makeUnion(mailingListOne, mailingListTwo);
        ListExpression expected = ListExpression.makeMailingList("heythere", expectedOne);
        assertEquals("list expression not as expected", expected, actual);        
    }
    
    
    //covers case where list recipient field is a intersection
    @Test
    public void testParseValidEmailListIntersection(){
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);

        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        final String input = "heythere = cow * grass";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expectedOne = ListExpression.makeIntersection(mailingListOne, mailingListTwo);
        ListExpression expected = ListExpression.makeMailingList("heythere", expectedOne);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where list recipient field is a difference
    @Test
    public void testParseValidEmailListDifference(){
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);

        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        final String input = "heythere = cow ! grass";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expectedOne = ListExpression.makeDifference(mailingListOne, mailingListTwo);
        ListExpression expected = ListExpression.makeMailingList("heythere", expectedOne);
        assertEquals("list expression not as expected", expected, actual);
    }
    
    //covers case where list recipient field is a mailing list
    @Test
    public void testParseValidEmailListMailingList(){
        //setting up "environment"
        final String name = "cz";
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression union = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList(name, union);
        Map<String, ListExpression> defined = new HashMap<>();
        defined.put(name, mailingListOne);
        final String input = "--3424rfdc.2323sc = cz ";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
        ListExpression expected = ListExpression.makeMailingList("--3424rfdc.2323sc", mailingListOne);
        assertEquals("list expression not as expected", expected, actual);
    }

    
    //
    //tests for invalid input below:
    //
    
            
    //covers case where has empty username in recipient field (@gmail.com, @mit.edu)
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidEmailAddressEmptyUsernameField(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String input = "@mit";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
    }
    
    //covers case where has @ in email, but empty domain (.com, .edu, etc.)
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidEmailAddressEmptyDomain(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String input = "hellothere@";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
    }
    
    //covers case where missing both username and domain (@)
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidEmailAddressEmptyDomainAndUsername(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String input = "hellothere@mit, @";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
    }
  
    //covers case where using extraneous symbols
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidEmailAddressExtraneousSymbols(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String input = "hellothere!!!!!@mit";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
    }
    
    //covers case where has multiple '@' in one email address
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidEmailMultipleAtSymbolsInOneEmail(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String input = "hello@there@mit";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
    }

    //covers case where has spaces in one email address
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidEmailCheckWhiteSpacesEmailAddress(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String input = "hello         there@mit";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
    }
    
    //covers case where has tabs in one email address
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidEmailCheckWhiteSpacesTabsEmailAddress(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String input = "hello\tthere@mit";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
    }
    
    //covers case where has carriage return in one email address
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidEmailCheckWhiteSpacesCarriageReturnEmailAddress(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String input = "hello\rthere@mit";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
    }
    
    //covers case where has new line in one email address
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidEmailCheckWhiteSpacesNewLineEmailAddress(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String input = "hello\nthere@mit";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
    }

    //covers case where has spaces in one list definition
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidEmailCheckWhiteSpacesListDefinition(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String input = "hello         there = cow@mit.edu";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
    }
    
    //covers case where has tabs in one list definition
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidEmailCheckWhiteSpacesTabListDefinition(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String input = "hello\tthere = cow@mit.edu";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
    }
    
    //covers case where has carriage returns in one list definition
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidEmailCheckWhiteSpacesCarriageReturnListDefinition(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String input = "hello\rthere = cow@mit.edu";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
    }
    
    //covers case where has new line returns in one list definition
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidEmailCheckWhiteSpacesNewLineListDefinition(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String input = "hello\nthere = cow@mit.edu";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
    }
    
    //covers case where has new line returns in one list definition
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidHigher(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();

        final String input = "hello\nthere = cow@mit.edu";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
    }
    
    //covers case where list name is empty
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidEmailListEmptyName(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        final String input = "  = yayay1234567890@m.i.t.e.d.u";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
    }
   
    
    //covers case where nested list definition
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidListDefinition(){
        //setting up "environment"
        ListExpression emailOne = ListExpression.makeEmail("hellothere@mit");
        ListExpression emailTwo = ListExpression.makeEmail("123456@mit.edu");
        ListExpression emailThree = ListExpression.makeEmail("o@o.o.o.o.o.o");
        ListExpression emailFour = ListExpression.makeEmail("4323..fdgf@miiiiiiit.e.e.e.e.e");
        ListExpression unionOne = ListExpression.makeUnion(emailOne, emailTwo);
        ListExpression unionTwo = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression union = ListExpression.makeUnion(unionOne, unionTwo);
        ListExpression mailingListOne = ListExpression.makeMailingList("COW", union);
        ListExpression unionThree = ListExpression.makeUnion(emailThree, emailFour);
        ListExpression mailingListTwo = ListExpression.makeMailingList("GRASS", unionThree);

        Map<String, ListExpression> defined = new HashMap<>();
        defined.put("cow", mailingListOne);
        defined.put("grass", mailingListTwo);
        defined.put("dirt", mailingListTwo);
        DefinedMailingLists definedMailingLists = new DefinedMailingLists(defined);

        final String input = "(cow = grass = dirt)";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
    } 
    
    //covers case where nested list definitions
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidListDefinitions(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        final String input = "a = b , c = d";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
    }
    
    
    //covers case where has nested sequence
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidNestedSequence(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        final String input = "hello = (x = a@mit, b@mit ; x * b@mit.edu)";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
    }
    
    //covers case where nested sequences
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidListDefinitions2(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        final String input = "a=b=c";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
    }
    
    //covers case where nested sequences with parentheses
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidListDefinitions3(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        final String input = "(a=b),c";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
    }
    
    //covers case where nested sequences
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidSequences(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        final String input = "(a;b),(c;d)";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
    }
    
    //covers case where nested sequences
    @Test(expected=IllegalArgumentException.class)
    public void testParseInvalidSequences2(){
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        final String input = "(a;b),(c = a@a;d)";
        ListExpression actual = ListExpressionParser.parse(input,definedMailingLists);
    }

}
