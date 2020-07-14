package norn;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import lib6005.parser.UnableToParseException;

public class ParserTest {

    /*
     * Testing strategy for ListExpressionParser.java: 
     * 
     * Test one public method: parse 
     *      -covers all ADT expressions (,!*;) and equals()
     *      Partitions: 
     *          Contains no operations: 
     *              Number of lists: 0, 1
     *              
     *          Just union (,), difference (!), or intersection (*):
     *              Number of lists: 1 (union with empty list), 2, >2
     *              Single addresses, lists
     *              One set is a superset, subset, equals, incomparable to other set
     *              
     *          Just sequences (;): 
     *              Number of lists: 1 (sequence with empty list), 2, >2
     *              Single addresses, lists
     *              
     *          Defining mailing lists (=): 
     *              Number of lists: 0 (empty), 1, >1
     *              Single addresses, lists
     *              (Special cases: using undefined mailing lists, defining as itself, editing definitions, nested mailing lists a = (b = c)) == a = c
     *              
     *          Combination of operations: 
     *              Number of different operations: 2, 3, 4, 5
     *              Operations used: union, difference, intersection, sequence, defining mailing lists (test every combination)
     *              Single addresses, lists
     *              
     *          Contains valid constructs: 
     *            - Matched parentheses, nested parentheses, whitespace,
     *              valid usernames (uppercase letters, lowercase letters, digits, underscores, dashes, periods, plus signs), 
     *              valid domain name (letters (upper/lower), digits, underscores, dashes, periods), valid defined list names 
     *              (letters (upper/lower), digits, underscores, dashes, periods)
     *            - case insensitivity
     *            
     *          Contains invalid constructs: 
     *            - Unmatched parentheses, adjacent lists w/o operations, invalid username, invalid domain name, 
     *              invalid list name
     *            - Looping mailing list definitions (a = b; b = c; c = a)
     */

    private static final ListExpression EMPTY = ListExpression.makeEmpty();
    
    private static final String STUDENT_1_EMAIL = "student1@mit.edu";
    private static final ListExpression STUDENT_1 = ListExpression.makeEmail("student1@mit.edu");
    
    private static final String STUDENT_2_EMAIL = "student2@mit.edu";
    private static final ListExpression STUDENT_2 = ListExpression.makeEmail("student2@mit.edu");
    
    private static final String STUDENT_3_EMAIL = "student3@mit.edu";
    private static final ListExpression STUDENT_3 = ListExpression.makeEmail("student3@mit.edu");
    
    private static final String INSTRUCTOR_1_EMAIL = "goldman@mit.edu";
    private static final ListExpression INSTRUCTOR_1 = ListExpression.makeEmail("goldman@mit.edu");
    
    private static final String INSTRUCTOR_2_EMAIL = "miller@mit.edu";
    private static final ListExpression INSTRUCTOR_2 = ListExpression.makeEmail("miller@mit.edu");
    
    private static final String STUDENTS_STRING = STUDENT_1_EMAIL + "," + STUDENT_2_EMAIL + "," + STUDENT_3_EMAIL;
    private static final String INSTRUCTORS_STRING = INSTRUCTOR_1_EMAIL + "," + INSTRUCTOR_2_EMAIL;
    private static final String SOME_PEOPLE_STRING = STUDENT_1_EMAIL + "," + INSTRUCTOR_1_EMAIL;
    private static final String EVERYONE_STRING = STUDENT_1_EMAIL + "," + STUDENT_2_EMAIL + "," + STUDENT_3_EMAIL + "," + INSTRUCTOR_1_EMAIL + "," + INSTRUCTOR_2_EMAIL;
    
    private static final ListExpression STUDENTS = ListExpression.makeUnion(ListExpression.makeUnion(STUDENT_1, STUDENT_2), STUDENT_3);
    private static final ListExpression SOME_PEOPLE = ListExpression.makeUnion(STUDENT_1, INSTRUCTOR_1);
    private static final ListExpression INSTRUCTORS = ListExpression.makeUnion(INSTRUCTOR_1,  INSTRUCTOR_2);
    private static final ListExpression EVERYONE = ListExpression.makeUnion(STUDENTS,  INSTRUCTORS);
    
    private static final DefinedMailingLists ENVIRONMENT_6031 = new DefinedMailingLists();
    {
        ENVIRONMENT_6031.addMailingList("students", STUDENTS_STRING);
        ENVIRONMENT_6031.addMailingList("instructors", INSTRUCTORS_STRING);
        ENVIRONMENT_6031.addMailingList("some6031people", SOME_PEOPLE_STRING);
        ENVIRONMENT_6031.addMailingList("everyone",  EVERYONE_STRING);
    }
    
    private static final ListExpression ALL_6031_PEOPLE = ListExpression.makeUnion(STUDENT_1,
                                                          ListExpression.makeUnion(STUDENT_2, 
                                                          ListExpression.makeUnion(STUDENT_3, 
                                                          ListExpression.makeUnion(INSTRUCTOR_1, INSTRUCTOR_2))));
   
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    //Covers: no operations, no lists
    @Test 
    public void testEmptyMailingList() throws UnableToParseException {
        assertEquals("Expected empty input to return empty mailing list", EMPTY, ListExpressionParser.parse("", new DefinedMailingLists()));
    }
    
    //Covers: no operations, one list (valid user (letters), valid domain(letters)) 
    @Test
    public void testSingleMailingListValidLetters() throws UnableToParseException {
        String input = "RFCYOLO@mit";
        ListExpression expected = ListExpression.makeEmail(input);
        assertEquals("Expected single valid address to parse correctly", expected, ListExpressionParser.parse(input,  new DefinedMailingLists()));
    }
    
    //Covers: no operations , one list (valid user (letters and digits), valid host (letters and digits))
    @Test
    public void testSingleMailingListValidDigits() throws UnableToParseException {
        String input = "software4life@6031";
        ListExpression expected = ListExpression.makeEmail(input);
        assertEquals("Expected single valid address to parse correctly", expected, ListExpressionParser.parse(input,  new DefinedMailingLists()));
    }
    
    //Covers: no operations , one list (valid user (letters and underscores), valid host (letters and underscores))
    @Test
    public void testSingleMailingListValidUnderscores() throws UnableToParseException {
        String input = "max_goldman@course_staff";
        ListExpression expected = ListExpression.makeEmail(input);
        assertEquals("Expected single valid address to parse correctly", expected, ListExpressionParser.parse(input,  new DefinedMailingLists()));
    }

    //Covers: no operations , one list (valid user (letters and dashes), valid host (letters and dashes))
    @Test
    public void testSingleMailingListValidDashes() throws UnableToParseException {
        String input = "RFC-ETU-SFB@6031-students";
        ListExpression expected = ListExpression.makeEmail(input);
        assertEquals("Expected single valid address to parse correctly", expected, ListExpressionParser.parse(input,  new DefinedMailingLists()));
    }

    //Covers: no operations , one list (valid user (letters and periods), valid host (letters and periods))
    @Test
    public void testSingleMailingListValidPeriods() throws UnableToParseException {
        String input = "many.test.cases@6.031";
        ListExpression expected = ListExpression.makeEmail(input);
        assertEquals("Expected single valid address to parse correctly", expected, ListExpressionParser.parse(input,  new DefinedMailingLists()));
    }
    
    
    //Covers: no operations , one list (valid user (letters and plus), valid host (letters))
    @Test
    public void testSingleMailingListValidPlus() throws UnableToParseException {
        String input = "me+you@6.031fun";
        ListExpression expected = ListExpression.makeEmail(input);
        assertEquals("Expected single valid address to parse correctly", expected, ListExpressionParser.parse(input,  new DefinedMailingLists()));
    }
    
    //Covers: no operations, one list (invalid user: special symbol) 
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidSingleMailingListInvalidUserApostrophe() throws UnableToParseException {
        String input = "can't_have_apostrophes@6.031";
        ListExpression parsed = ListExpressionParser.parse(input, new DefinedMailingLists());
    }

    //Covers: no operations, one list (invalid user: at sign) 
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidSingleMailingListInvalidUserAt() throws UnableToParseException {
        String input = "me@my_dorm@6.031";
        ListExpression parsed = ListExpressionParser.parse(input, new DefinedMailingLists());
    }
      
    //Covers: no operations, one list (invalid user: white spaces) 
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidSingleMailingListInvalidUserSpace() throws UnableToParseException {
        String input = "here I am@6.031";
        ListExpression parsed = ListExpressionParser.parse(input, new DefinedMailingLists());
    }
    
    //Covers: no operations, one list (invalid domain: special symbol) 
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidSingleMailingListInvalidDomainApostrophe() throws UnableToParseException {
        String input = "user@what's_up";
        ListExpression parsed = ListExpressionParser.parse(input, new DefinedMailingLists());
    }
    
    //Covers: no operations, one list (invalid domain: at sign) 
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidSingleMailingListInvalidDomainAt() throws UnableToParseException {
        String input = "me@mit@cambridge";
        ListExpression parsed = ListExpressionParser.parse(input, new DefinedMailingLists());
    }
     
    //Covers: no operations, one list (invalid domain: white space) 
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidSingleMailingListInvalidDomainSpace() throws UnableToParseException {
        String input = "me@MIT in Cambridge";
        ListExpression parsed = ListExpressionParser.parse(input, new DefinedMailingLists());
    }
    
    //Covers: no operations , one list (invalid domain (plus))
    @Test(expected=IllegalArgumentException.class)
    public void testSingleMailingListInvalidPlusInDomain() throws UnableToParseException {
        String input = "meandyou@6.031+fun";
        ListExpression parsed = ListExpressionParser.parse(input, new DefinedMailingLists());
    }
    
    //Covers: case insensitivity for username
    @Test
    public void testSingleMailingListCaseInsensitive() throws UnableToParseException {
        String input = "AStudent@mIt.EdU";
        ListExpression parsed = ListExpressionParser.parse(input, new DefinedMailingLists());
        ListExpression expected = ListExpression.makeEmail("astudent@mit.edu");
        assertEquals("Expected case not to matter in username and hostname", expected, parsed);
    }
    
    //Covers: define mailing list name with periods
    @Test
    public void testDefineMailingListWithPeriods(){
        String input = "what.a.Name = students";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        definedMailingLists.addMailingList("students", STUDENTS_STRING);
        ListExpression parsed = ListExpressionParser.parse(input, definedMailingLists);
        ListExpression expectedParsed = STUDENTS; 
        String expected = "students";
        String actual = definedMailingLists.get("what.a.name");
        assertEquals("Parsed list expression not as expected", expectedParsed, parsed);
        assertEquals("Expected definition of mailing list was incorrect", expected, actual);
    }
    
    //Covers: define mailing list name with dashes
    @Test
    public void testDefineMailingListWithDashes(){
        String input = "what-a-Name = students      ";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        definedMailingLists.addMailingList("students", STUDENTS_STRING);
        ListExpression parsed = ListExpressionParser.parse(input, definedMailingLists);
        ListExpression expectedParsed = STUDENTS;
        String expected = "students";
        String actual = definedMailingLists.get("what-a-name");
        assertEquals("Parsed list expression not as expected", expectedParsed, parsed);
        assertEquals("Expected definition of mailing list was incorrect", expected, actual);
    }
    
    //Covers: define mailing list name with underscores
    @Test
    public void testDefineMailingListWithUnderscores(){
        String input = "what_a_Name = students";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        definedMailingLists.addMailingList("students", STUDENTS_STRING);
        ListExpression parsed = ListExpressionParser.parse(input, definedMailingLists);
        ListExpression expectedParsed = STUDENTS;
        String expected = "students";
        String actual = definedMailingLists.get("what_a_name");
        assertEquals("Parsed list expression not as expected", expectedParsed, parsed);
        assertEquals("Expected definition of mailing list was incorrect", expected, actual);
    }
    
    //Covers: define mailing list as empty
    @Test
    public void testDefineMailingListEmpty(){
        String input = "whataName =";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        ListExpression parsed = ListExpressionParser.parse(input, definedMailingLists);
        ListExpression parsedExpected = ListExpression.makeEmpty();

        String expected = "";
        String actual = definedMailingLists.get("whataname");
        assertEquals("Parsed expression was not as expected", parsedExpected, parsed);
        assertEquals("Expected definition of mailing list was incorrect", expected, actual);
    }
    
    //Covers: define mailing list as one list (single address)
    @Test
    public void testDefineMailingListAsOneList(){
        String input = "animals = cow+++@cow";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        ListExpression parsed = ListExpressionParser.parse(input, definedMailingLists);
        ListExpression expectedParsed = ListExpression.makeEmail("cow+++@cow");
        String expected = "cow+++@cow";
        String actual = definedMailingLists.get("animals");
        assertEquals("Expected definition of mailing list was incorrect", expectedParsed, parsed);
        assertEquals("Expected definition of mailing list was incorrect", expected, actual);

    }
    
    //Covers: define mailing list as one list (single mailing list)
    @Test
    public void testDefineMailingListAsOneMailingList(){
        String input = "animals = students";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        definedMailingLists.addMailingList("students", STUDENTS_STRING);
        ListExpression parsed = ListExpressionParser.parse(input, definedMailingLists);
        ListExpression expectedParsed = STUDENTS;
        String expected = "students";
        String actual = definedMailingLists.get("animals");
        assertEquals("Parsed list expression was not as expected", expectedParsed, parsed);
        assertEquals("Expected definition of mailing list was incorrect", expected, actual);
    }
    
    //Covers: define mailing list as >1 list (single address)
    @Test
    public void testDefineMailingListAsMoreThanOneList(){
        String input = "animals = cow@XXX, what.a.great...@email.for.testing";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        ListExpression parsed = ListExpressionParser.parse(input, definedMailingLists);
        ListExpression cowEmail = ListExpression.makeEmail("cow@XXX");
        ListExpression greatEmail = ListExpression.makeEmail("what.a.great...@email.for.testing");
        ListExpression expectedParsed = ListExpression.makeUnion(cowEmail, greatEmail);
        String expected = "cow@xxx,what.a.great...@email.for.testing";
        String actual = definedMailingLists.get("animals");
        assertEquals("Parsed list expression was not as expected", expectedParsed, parsed);
        assertEquals("Expected definition of mailing list was incorrect", expected, actual);
    }
    
    //Covers: define mailing list as >1 list (mailing lists), union
    @Test
    public void testDefineMailingListAsMoreThanOneMailingList(){
        String input = "animals = students, instructors";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        definedMailingLists.addMailingList("students", STUDENTS_STRING);
        definedMailingLists.addMailingList("instructors", INSTRUCTORS_STRING);
        ListExpression parsed = ListExpressionParser.parse(input, definedMailingLists);
        ListExpression expectedParsed = EVERYONE;
        String expected = "students,instructors";
        String actual = definedMailingLists.get("animals");
        assertEquals("Expected parsed list expression was incorrect", expectedParsed, parsed);
        assertEquals("Expected definition of mailing list was incorrect", expected, actual);
    }
    
    //Covers: define mailing list as >1 list (mailing lists), difference
    @Test
    public void testDefineMailingListAsDifference(){
        String input = "animals = everyone ! instructors";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        definedMailingLists.addMailingList("everyone", EVERYONE_STRING);
        definedMailingLists.addMailingList("instructors", INSTRUCTORS_STRING);
        ListExpression parsed = ListExpressionParser.parse(input, definedMailingLists);
        ListExpression parsedExpected = ListExpression.makeDifference(EVERYONE, INSTRUCTORS);
        String expected = "everyone!instructors";
        String actual = definedMailingLists.get("animals");
        assertEquals("Parsed list expression was not as expected", parsedExpected, parsed);
        assertEquals("Expected definition of mailing list was incorrect", expected, actual);
    }
    
    //Covers: define mailing list as >1 list (mailing lists), intersection
    @Test
    public void testDefineMailingListAsIntersection(){
        String input = "animals = everyone ! instructors";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        definedMailingLists.addMailingList("everyone", EVERYONE_STRING);
        definedMailingLists.addMailingList("instructors", INSTRUCTORS_STRING);
        ListExpression parsed = ListExpressionParser.parse(input, definedMailingLists);
        ListExpression expectedParsed = STUDENTS;
        String expected = "everyone!instructors";
        String actual = definedMailingLists.get("animals");
        assertEquals("Parsed list expression was not as expected", expectedParsed, parsed);
        assertEquals("Expected definition of mailing list was incorrect", expected, actual);
    }
    
    //Covers: define mailing list as >1 list (mailing lists), sequence
    @Test
    public void testDefineMailingListAsSequence(){
        //animals = student2, student3
        String input = "animals = ((field = student1@mit.edu) ; students ! field)";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        definedMailingLists.addMailingList("students", STUDENTS_STRING);
        ListExpression parsed = ListExpressionParser.parse(input, definedMailingLists);
        ListExpression expectedParsed = ListExpression.makeUnion(STUDENT_2, STUDENT_3);
        String expected = "((field=student1@mit.edu);students!field)";
        String actual = definedMailingLists.get("animals");
        assertEquals("Parsed list expression was not as expected", expectedParsed, parsed);
        assertEquals("Expected definition of mailing list was incorrect", expected, actual);
    }
    
    //Covers: nested mailing list definitions 
    @Test
    public void testNestedMailingLists(){
        String input = "everyone = students = instructors";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        definedMailingLists.addMailingList("instructors", INSTRUCTORS_STRING);
        ListExpression parsed = ListExpressionParser.parse(input, definedMailingLists);
        ListExpression expectedParsed = INSTRUCTORS;
        String expectedEveryone = "students=instructors";
        String actualEveryone = definedMailingLists.get("everyone");
        String expectedStudents = "instructors";
        String actualStudents = definedMailingLists.get("students");
        assertEquals("Parsed expression was not as expected", expectedParsed, parsed);
        assertEquals("Expected definition of mailing list was incorrect", expectedEveryone, actualEveryone);
        assertEquals("Expected definition of mailing list was incorrect", expectedStudents, actualStudents);
    }
    
    //Covers: define mailing list as undefined mailing list
    public void testDefineMailingListAsUndefined(){
        String input = "animals = students";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        ListExpression parsed = ListExpressionParser.parse(input, definedMailingLists);
        ListExpression expectedParsed = EMPTY;
        String expected = "students";
        String actual = definedMailingLists.get("animals");
        assertEquals("Parsed expression was not as expected", expectedParsed, parsed);
        assertEquals("Expected definition of mailing list was incorrect", expected, actual);
    }
    
    //Covers: define mailing list as >1 list (with undefined mailing list)
    public void testDefineMailingListAsUndefinedAndDefinedMailingLists(){
        String input = "animals = students, instructors";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        definedMailingLists.addMailingList("instructors", INSTRUCTORS_STRING);
        ListExpression parsed = ListExpressionParser.parse(input, definedMailingLists);
        ListExpression expectedParsed = INSTRUCTORS;
        String expected = "students, instructors";
        String actual = definedMailingLists.get("animals");
        assertEquals("Parsed list expression was not as expected", expectedParsed, parsed);
        assertEquals("Expected definition of mailing list was incorrect", expected, actual);
    }
    
    //Covers: define mailing list as itself
    public void testDefineMailingListSame(){
        String input = "students = students";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        definedMailingLists.addMailingList("students", STUDENTS_STRING);
        ListExpression parsed = ListExpressionParser.parse(input, definedMailingLists);
        ListExpression parsedExpected = STUDENTS;
        String expected = STUDENTS_STRING;
        String actual = ENVIRONMENT_6031.get("students");
        assertEquals("Parsed list expression was not as expected", parsedExpected, parsed);
        assertEquals("Expected definition of mailing list was incorrect", expected, actual);
    }
    
    //Covers: mailing list name contains + symbol (invalid)
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidDefineMailingSymbols(){
        String input = "a+a = b@b";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        ListExpression parsed = ListExpressionParser.parse(input, definedMailingLists);
    }
    
    //Covers: mailing list name contains @ symbol (invalid)
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidDefineMailingAt(){
        String input = "a@a = b@b";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        ListExpression parsed = ListExpressionParser.parse(input, definedMailingLists);
    }
    
    //Covers: mail loop (invalid)
    @Test(expected=AssertionError.class)
    public void testInvalidDefineMailingListLoop(){
        String input = "a=b ; b=a";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        ListExpression parsed = ListExpressionParser.parse(input, definedMailingLists);
    }
    
    //Covers: mail loop (invalid)
    @Test(expected=AssertionError.class)
    public void testInvalidDefineMailingListLoop2(){
        String input = "a = b; b = a, a@a";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        ListExpression parsed = ListExpressionParser.parse(input, definedMailingLists);
    }
    
    //Covers: mail loop (invalid)
    @Test(expected=AssertionError.class)
    public void testInvalidDefineMailingListLoop3(){
        String input = "a = b; b = c; c = a";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        ListExpression parsed = ListExpressionParser.parse(input, definedMailingLists);
    }
    
    //Covers: mail loop (invalid)
    @Test(expected=AssertionError.class)
    public void testInvalidDefineMailingListLoop4(){
        String input = "a = b; b = c; c = d; d = e; e = a, email@email";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        ListExpression parsed = ListExpressionParser.parse(input, definedMailingLists);
    }
    
    //Covers: just union, one argument (union with empty list)
    @Test
    public void testUnionWithEmptyAtEnd() throws UnableToParseException {
        String input = STUDENT_1_EMAIL + ",";
        ListExpression expected = STUDENT_1;
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, new DefinedMailingLists()));
    }
    
    
    //Covers: just union, one argument (union with empty list)
    @Test
    public void testUnionWithEmptyAtBeginning() throws UnableToParseException {
        String input = "," + STUDENT_1_EMAIL;
        ListExpression expected = STUDENT_1;
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, new DefinedMailingLists()));
    }
    
    
    //Covers: Just union, two arguments, incomparable
    @Test
    public void testUnionTwoAddresses() throws UnableToParseException {
        String input = STUDENT_1_EMAIL + "," + STUDENT_2_EMAIL;
        ListExpression expected = ListExpression.makeUnion(STUDENT_1, STUDENT_2);
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, new DefinedMailingLists()));
    }
    
    
    //Covers: Just union, two arguments, incomparable
    @Test
    public void testUnionTwoListsIncomparable() throws UnableToParseException {
        String input = "students, instructors";
        ListExpression expected = ALL_6031_PEOPLE;
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031));
    }
    
    //Covers: Just union, two arguments, subset
    @Test
    public void testUnionTwoListsSubset() throws UnableToParseException {
        String input = "students, everyone";
        ListExpression expected = ALL_6031_PEOPLE;
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031));
    }
    
    //Covers: Just union, two arguments, subset (undefined mailing list)
    @Test
    public void testUnionTwoListsSubsetUndefinedMailingList() throws UnableToParseException {
        String input = "students, whatevenisthis";
        ListExpression expected = STUDENTS;
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031));
    }
    
    //Covers: Just union, two arguments, superset
    @Test
    public void testUnionTwoListsSuperset() throws UnableToParseException {
        String input = "everyone, instructors";
        ListExpression expected = ALL_6031_PEOPLE;
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031));
    }
    
    //Covers: Just union, two arguments, equals
    @Test
    public void testUnionTwoListsEquals() throws UnableToParseException {
        String input = "some6031people, sOME6031people";
        ListExpression expected = SOME_PEOPLE;
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031));
    }
   
    //Covers: Just union, >2 arguments 
    @Test
    public void testUnionThreeAddresses() throws UnableToParseException {
        String input = STUDENT_1_EMAIL + "," + STUDENT_2_EMAIL + "," + STUDENT_3_EMAIL;
        ListExpression expected = ListExpression.makeUnion(STUDENT_3, ListExpression.makeUnion(STUDENT_1, STUDENT_2));
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, new DefinedMailingLists()));
    }
    
    
    //Covers: Just union, >2 arguments
    @Test
    public void testUnionEmptyListInMiddle() throws UnableToParseException {
        String input = STUDENT_1_EMAIL + ",," + STUDENT_2_EMAIL; 
        ListExpression expected = ListExpression.makeUnion(STUDENT_1,  STUDENT_2); 
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, new DefinedMailingLists()));
    }
    
    
    //Covers: Just union, contains lists 
    @Test
    public void testUnionWithList() throws UnableToParseException {
        String input = "students," + INSTRUCTOR_1_EMAIL;
        ListExpression expected = ListExpression.makeUnion(INSTRUCTOR_1, STUDENTS);
        assertEquals("expected successful union with defined mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031));
    }
    
    
    //Covers: just difference, one argument (diff with empty list)
    @Test
    public void testDifferenceMinusEmptySet() throws UnableToParseException {
        String input = STUDENT_1_EMAIL + "!";
        ListExpression expected = STUDENT_1;
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, new DefinedMailingLists()));
    }
    
    
    //Covers: just difference, one argument (diff with empty list)
    @Test
    public void testDifferenceEmptySet() throws UnableToParseException {
        String input = "!" + STUDENT_1_EMAIL;
        ListExpression expected = ListExpression.makeEmpty();
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, new DefinedMailingLists()));
    }
    
    
    //Covers: just difference, two arguments, superset, contains lists
    @Test
    public void testDifferenceSuperset() throws UnableToParseException {
        String input = "students ! " + STUDENT_1_EMAIL; 
        ListExpression expected = ListExpression.makeUnion(STUDENT_2, STUDENT_3);
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031));
    }
    
    
    //Covers: just difference, two arguments, superset, undefined mailing list
    @Test
    public void testDifferenceSupersetUndefinedMailingList() throws UnableToParseException {
        String input = STUDENT_1_EMAIL + "! what-is-a-mail";
        ListExpression expected = STUDENT_1;
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031));
    }
    
    //Covers: just difference, two arguments, subset, contains lists 
    @Test
    public void testDifferenceSubset() throws UnableToParseException {
        String input = STUDENT_1_EMAIL + " ! students";
        ListExpression expected = ListExpression.makeEmpty();
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031));
    }
    
    //Covers: just difference, two arguments, subset, undefined mailing list
    @Test
    public void testDifferenceSubsetUndefinedMailingList() throws UnableToParseException {
        String input = "what-is-a-mail ! students";
        ListExpression expected = ListExpression.makeEmpty();
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031));
    }
    
    //Covers: just difference, two arguments, subset, contains lists 
    @Test
    public void testDifferenceEquals() throws UnableToParseException {
        String input = "students ! students";
        ListExpression expected = ListExpression.makeEmpty();
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031));
    }
    
    
    //Covers: just difference, two arguments, incomparable, contains lists 
    @Test
    public void testDifferenceIncomparable() throws UnableToParseException {
        String input = "some6031People ! students";
        ListExpression expected = INSTRUCTOR_1;
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031));
    }
    
    
    //Covers: just difference, >2 arguments
    @Test
    public void testDifferenceInSeries() throws UnableToParseException {
        //note: Should group like ((students ! <student1>) ! some6031People), which returns student2, student 3
        //NOT: (students ! (<student1> ! some6031People)), which returns student1, student2, student3
        String input = "students !" + STUDENT_1_EMAIL + "! some6031People";
        ListExpression expected = ListExpression.makeUnion(STUDENT_2,  STUDENT_3);
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031));
    }
    
    
    //Covers: just difference, >2 arguments
    @Test
    public void testDifferenceEmptyListInMiddle() throws UnableToParseException {
        //note: Should group like ((students ! <empty>) ! some6031People), which returns student2, student 3
        String input = "students ! ! some6031People";
        ListExpression expected = ListExpression.makeUnion(STUDENT_2,  STUDENT_3);
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031));
    }
    
    
    //Covers: just intersection, one argument (intersect with empty list)
    @Test
    public void testIntersectionEmptySet() throws UnableToParseException {
        String input = STUDENT_1_EMAIL + "*";
        ListExpression expected = ListExpression.makeEmpty();
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, new DefinedMailingLists()));
    }
    
    
    //Covers: just intersection, one argument (intersect with empty list)
    @Test
    public void testIntersectionEmptySetBeginning() throws UnableToParseException {
        String input = "*" + STUDENT_1_EMAIL;
        ListExpression expected = ListExpression.makeEmpty();
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, new DefinedMailingLists()));
    }
    
    
    //Covers: just intersection, two arguments, superset/subset, contains lists
    @Test
    public void testIntersectionSuperset() throws UnableToParseException {
        String input = "students * " + STUDENT_1_EMAIL; 
        ListExpression expected = STUDENT_1;
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031));
    }
    
    //Covers: just intersection, two arguments, sets are equals, contains lists 
    @Test
    public void testIntersectionEquals() throws UnableToParseException {
        String input = "students * students";
        ListExpression parsed = ListExpressionParser.parse(input, ENVIRONMENT_6031);
        assertEquals("expected operation to return correct mailing list", STUDENTS, parsed);
    }
    
    //Covers: just intersection, two arguments, incomparable, contains lists 
    @Test
    public void testIntersectionIncomparable() throws UnableToParseException {
        String input = "some6031People * students";
        ListExpression expected = STUDENT_1;
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031));
    }
    
    //Covers: just intersection, >2 arguments
    @Test
    public void testIntersectioneInSeries() throws UnableToParseException {
        String input = "students *" + STUDENT_1_EMAIL + "* some6031People";
        ListExpression expected = STUDENT_1;
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031));
    }
    
    
    //Covers: just intersection, >2 arguments
    @Test
    public void testIntersectionEmptyListInMiddle() throws UnableToParseException {
        String input = "students * * some6031People";
        ListExpression expected = EMPTY;
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031));
    }
    
    
    //Covers: just sequence, one argument (sequence with empty list)
    @Test
    public void testSequenceAndEmptySet() throws UnableToParseException {
        String input = STUDENT_1_EMAIL + ";";
        ListExpression expected = ListExpression.makeEmpty();
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, new DefinedMailingLists()));
    }
    
    
    //Covers: just sequence, one argument (sequence with empty list)
    @Test
    public void testSequenceEmptySetAnd() throws UnableToParseException {
        String input = ";" + STUDENT_1_EMAIL;
        ListExpression expected = STUDENT_1;
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, new DefinedMailingLists()));
    }
    
    
    //Covers: just sequence, two arguments, contains lists
    @Test
    public void testSequenceMailingListAndEmail() throws UnableToParseException {
        String input = "students ; " + STUDENT_1_EMAIL; 
        ListExpression expected = STUDENT_1;
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031));
    }
    
    //Covers: just sequence, two arguments, equal, contains lists 
    @Test
    public void testSequenceEquals() throws UnableToParseException {
        String input = "students ; students";
        ListExpression expected = STUDENTS;
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031));
    }
    
    //Covers: just sequence, two arguments, contains lists 
    @Test
    public void testSequenceTwoMailingLists() throws UnableToParseException {
        String input = "some6031People ; students";
        ListExpression expected = STUDENTS;
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031));
    }
    
    //Covers: just sequence, >2 arguments
    @Test
    public void testSequenceEmptyListInMiddle() throws UnableToParseException {
        String input = "students ; ; students";
        ListExpression expected = STUDENTS;
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031));
    }
    
    
    //Covers: union and difference
    @Test
    public void testUnionAndDifference() throws UnableToParseException {
        //Note: Difference has preference over union. Test both arrangements to ensure correct operation order
        
        //Should group like students, (instructors ! some6031People) which produces students, instructor2
        //NOT like (students, instructors) ! some6031People, which produces student2, student3, instructor3
        String unionFirst = "students, instructors ! some6031People"; 
        ListExpression expectedUnionFirst = ListExpression.makeUnion(STUDENTS, INSTRUCTOR_2);
        assertEquals("expected operation to return correct mailing list", expectedUnionFirst, ListExpressionParser.parse(unionFirst, ENVIRONMENT_6031));
        
        //Should group like ( instructors ! some6031People), students which produces students, instructor2
        //NOT like instructors ! (some6031People, students) which products instructor2
        String differenceFirst = "instructors ! some6031People, students";
        ListExpression expectedDifferenceFirst = ListExpression.makeUnion(STUDENTS, INSTRUCTOR_2);
        assertEquals("expected operation to return correct mailing list", expectedDifferenceFirst, ListExpressionParser.parse(differenceFirst, ENVIRONMENT_6031));
    }
    
    
    //Covers: union and difference
    //        parentheses used to change the order of operations
    @Test
    public void testUnionAndDifferenceWithParentheses() throws UnableToParseException {        
        //(students, instructors) ! some6031People, which produces student2, student3, instructor2
        String unionFirst = "(students, instructors) ! some6031People"; 
        ListExpression expectedUnionFirst = ListExpression.makeUnion(INSTRUCTOR_2, ListExpression.makeUnion(STUDENT_2, STUDENT_3));
        assertEquals("expected operation to return correct mailing list", expectedUnionFirst, ListExpressionParser.parse(unionFirst, ENVIRONMENT_6031));
        
        //instructors ! (some6031People, students) which products instructor2
        String unionFirstAgain = "instructors ! (some6031People , students)";
        ListExpression expectedUnionFirstAgain = INSTRUCTOR_2;
        assertEquals("expected operation to return correct mailing list", expectedUnionFirstAgain, ListExpressionParser.parse(unionFirstAgain, ENVIRONMENT_6031));
    }
    
    
    //Covers: union and intersection
    @Test
    public void testUnionAndIntersection() throws UnableToParseException {
        //Note: Intersection has preference over union. Test both arrangements to ensure correct operation order
        
        //Should group like students, (instructors*some6031People) which produces students, instructor1
        //NOT like (students, instructors) * some6031People, which produces student1, instructor1
        String unionFirst = "students, instructors * some6031People"; 
        ListExpression expectedUnionFirst = ListExpression.makeUnion(STUDENTS, INSTRUCTOR_1);
        assertEquals("expected operation to return correct mailing list", expectedUnionFirst, ListExpressionParser.parse(unionFirst, ENVIRONMENT_6031));
        
        //Should group like ( instructors * some6031People), students which produces instructor1, students
        //NOT like instructors * (some6031People, students) which products instructor1
        String differenceFirst = "instructors * some6031People, students";
        ListExpression expectedDifferenceFirst = ListExpression.makeUnion(STUDENTS, INSTRUCTOR_1);
        assertEquals("expected operation to return correct mailing list", expectedDifferenceFirst, ListExpressionParser.parse(differenceFirst, ENVIRONMENT_6031));
    }
    

    //Covers: union and intersection
    //        parentheses used to change the order of operations
    @Test
    public void testUnionAndIntersectionWithParentheses() throws UnableToParseException {
        //(students, instructors) * some6031People produces student1, instructor1
        String unionFirst = "(students, instructors) * some6031People"; 
        ListExpression expectedUnionFirst = ListExpression.makeUnion(STUDENT_1, INSTRUCTOR_1);
        assertEquals("expected operation to return correct mailing list", expectedUnionFirst, ListExpressionParser.parse(unionFirst, ENVIRONMENT_6031));
        
        //instructors * (some6031People, students) which products instructor1
        String unionFirstAgain = "instructors * (some6031People, students)";
        ListExpression expectedUnionFirstAgain = INSTRUCTOR_1;
        assertEquals("expected operation to return correct mailing list", expectedUnionFirstAgain, ListExpressionParser.parse(unionFirstAgain, ENVIRONMENT_6031));
    }
    
    
    //Covers: union and sequence
    @Test
    public void testUnionAndSequence() throws UnableToParseException {
        //Note: Union has preference over sequence. Test both arrangements to ensure correct operation order.
        
        String unionFirst = "students; instructors, some6031People"; 
        ListExpression expectedUnionFirst = ListExpression.makeUnion(SOME_PEOPLE, INSTRUCTORS);
        assertEquals("expected operation to return correct mailing list", expectedUnionFirst, ListExpressionParser.parse(unionFirst, ENVIRONMENT_6031));
        
        String unionFirstAgain = "instructors, some6031People; students";
        ListExpression expectedUnionFirstAgain = STUDENTS;
        assertEquals("expected operation to return correct mailing list", expectedUnionFirstAgain, ListExpressionParser.parse(unionFirstAgain, ENVIRONMENT_6031));
    }
    
    
    //Covers: union and sequence
    //        parentheses used to change the order of operations
    @Test
    public void testUnionAndSequenceWithParentheses() throws UnableToParseException {
        //Note: Because of grouping of parentheses, sequence now has precedence over
        //union. Test both arrangements to ensure correct operation order.
        
        String unionFirst = "("+ STUDENT_1_EMAIL+ "; instructors)," + STUDENT_2_EMAIL; 
        ListExpression expectedUnionFirst = ListExpression.makeUnion(INSTRUCTORS, STUDENT_2);
        assertEquals("expected operation to return correct mailing list", expectedUnionFirst, ListExpressionParser.parse(unionFirst, ENVIRONMENT_6031));
        
        String unionFirstAgain = "instructors, (some6031People; students)";
        ListExpression expectedUnionFirstAgain = EVERYONE;
        assertEquals("expected operation to return correct mailing list", expectedUnionFirstAgain, ListExpressionParser.parse(unionFirstAgain, ENVIRONMENT_6031));
    }
    
    //Covers: union and list definition (a@a,b=c@c) 
    @Test
    public void testUnionAndListDefinition() throws UnableToParseException {
        String input = "a@a, b=c@c,b@b";
        ListExpression a = ListExpression.makeEmail("a@a");
        ListExpression b = ListExpression.makeEmail("b@b");
        ListExpression c = ListExpression.makeEmail("c@c");
        DefinedMailingLists map = new DefinedMailingLists();
        ListExpression parsed = ListExpressionParser.parse(input, map);
        ListExpression parsedExpected = ListExpression.makeUnion(a, ListExpression.makeUnion(b, c));
        assertEquals("Parsed list expression was not as expected", parsed, parsedExpected);
        String expectedDefinition = "c@c,b@b";
        String actualDefintion = map.get("B");
        assertEquals("Expected mailing list definition not as expected", expectedDefinition, actualDefintion);
    }
     
    //Covers: intersection and difference 
    @Test
    public void testIntersectionAndDifference() throws UnableToParseException {
        //Note: intersection has preference over difference. Test both arrangements to ensure correct operation order
        
        //Should group like students ! (some6031People * instructors), which produces students, 
        //NOT like (students ! some6031People) * instructors, which produces no one. 
        String differenceFirst = "students ! some6031People * instructors"; 
        ListExpression expectedDifferenceFirst = STUDENTS;
        assertEquals("expected operation to return correct mailing list", expectedDifferenceFirst, ListExpressionParser.parse(differenceFirst, ENVIRONMENT_6031));
        
        //Should group like (profs * students) ! students, which produces no one,
        String intersectionFirst = "instructors * students ! students";
        ListExpression expectedIntersectionFirst = EMPTY; 
        assertEquals("expected operation to return correct mailing list", expectedIntersectionFirst, ListExpressionParser.parse(intersectionFirst, ENVIRONMENT_6031));
    }
    
    
    //Covers: intersection and difference 
    //        parentheses used to change the order of operations
    @Test
    public void testIntersectionAndDifferenceWithParentheses() throws UnableToParseException {        
        //(students ! some6031People) * instructors, which produces no one. 
        String differenceFirst = "(students ! some6031People) * instructors"; 
        ListExpression expectedDifferenceFirst = EMPTY;
        assertEquals("expected operation to return correct mailing list", expectedDifferenceFirst, ListExpressionParser.parse(differenceFirst, ENVIRONMENT_6031));
        
        String differenceFirstAgain = "instructors * (students ! students)";
        ListExpression expectedDifferenceAgain = EMPTY; 
        assertEquals("expected operation to return correct mailing list", expectedDifferenceAgain, ListExpressionParser.parse(differenceFirstAgain, ENVIRONMENT_6031));
    }
        

    //Covers: intersection and sequence
    @Test
    public void testIntersectionAndSequence() throws UnableToParseException {
        //Note: Intersection has preference over sequence. Test both arrangements to ensure correct operation order.
        
        String intersectionFirst = "students ; instructors * everyone"; 
        ListExpression expectedIntersectionFirst = ListExpression.makeIntersection(EVERYONE, INSTRUCTORS);
        assertEquals("expected operation to return correct mailing list", expectedIntersectionFirst, ListExpressionParser.parse(intersectionFirst, ENVIRONMENT_6031));
        
        String intersectionFirstAgain = "instructors ; some6031People * students";
        ListExpression expectedIntersectionFirstAgain = STUDENT_1;
        assertEquals("expected operation to return correct mailing list", expectedIntersectionFirstAgain, ListExpressionParser.parse(intersectionFirstAgain, ENVIRONMENT_6031));
    }
    
    //Covers: intersection and sequence
    //        parentheses used to change the order of operations
    @Test
    public void testIntersectionAndSequenceWithParentheses() throws UnableToParseException {        
        String intersectionFirst = "(students ; instructors) * everyone"; 
        ListExpression expectedIntersectionFirst = ListExpression.makeIntersection(EVERYONE, INSTRUCTORS);
        assertEquals("expected operation to return correct mailing list", expectedIntersectionFirst, ListExpressionParser.parse(intersectionFirst, ENVIRONMENT_6031));
        
        String intersectionFirstAgain = "(instructors ;" + STUDENT_2_EMAIL+ ") * students";
        ListExpression expectedIntersectionFirstAgain = STUDENT_2;
        assertEquals("expected operation to return correct mailing list", expectedIntersectionFirstAgain, ListExpressionParser.parse(intersectionFirstAgain, ENVIRONMENT_6031));
    }
    
    //Covers: intersection and mailing list definition
    @Test
    public void testIntersectionAndMailingListDefinition() throws UnableToParseException {
        String input = "(a@a  ,   c@c)  *   b=      c@c,     b@b,      z    ";
        ListExpression c = ListExpression.makeEmail("c@c");
        DefinedMailingLists map = new DefinedMailingLists();
        ListExpression parsed = ListExpressionParser.parse(input, map);
        ListExpression parsedExpected = c;
        assertEquals("Parsed list expression was not as expected", parsed, parsedExpected);
        String expectedDefinition = "c@c,b@b,z";
        String actualDefintion = map.get("B");
        assertEquals("Expected mailing list definition not as expected", expectedDefinition, actualDefintion);
    }

    //Covers: difference and sequence    
    @Test
    public void testDifferenceAndSequence() throws UnableToParseException {
        //Note: Difference has preference over sequence. Test both arrangements to ensure correct operation order.

        String differenceFirst = "students ; instructors ! everyone"; 
        ListExpression expectedDifferenceFirst = EMPTY;
        assertEquals("expected operation to return correct mailing list", expectedDifferenceFirst, ListExpressionParser.parse(differenceFirst, ENVIRONMENT_6031));
        
        String differenceFirstAgain = "instructors ; some6031People ! students";
        ListExpression expectedDifferenceFirstAgain = INSTRUCTOR_1;
        assertEquals("expected operation to return correct mailing list", expectedDifferenceFirstAgain, ListExpressionParser.parse(differenceFirstAgain, ENVIRONMENT_6031));
    }
    
    //Covers: difference and sequence
    //        parentheses used 
    @Test
    public void testDifferenceAndSequenceWithParentheses() throws UnableToParseException {
        String differenceFirst = "(students ; instructors) ! everyone"; 
        ListExpression expectedDifferenceFirst = EMPTY;
        assertEquals("expected operation to return correct mailing list", expectedDifferenceFirst, ListExpressionParser.parse(differenceFirst, ENVIRONMENT_6031));
        
        String differenceFirstAgain = "(instructors ; some6031People) ! students";
        ListExpression expectedDifferenceFirstAgain = INSTRUCTOR_1;
        assertEquals("expected operation to return correct mailing list", expectedDifferenceFirstAgain, ListExpressionParser.parse(differenceFirstAgain, ENVIRONMENT_6031));
    }
    
    //Covers: difference and mailing list definition    
    @Test
    public void testDifferenceAndMailingListDefinition() throws UnableToParseException {
        String input = "(a@a  ,   c@c)  !   b=      c@c,     b@b,      z    ";
        
        ListExpression a = ListExpression.makeEmail("a@a");
        DefinedMailingLists map = new DefinedMailingLists();
        ListExpression parsed = ListExpressionParser.parse(input, map);
        ListExpression parsedExpected = a;
        assertEquals("Parsed list expression was not as expected", parsed, parsedExpected);
        String expectedDefinition = "c@c,b@b,z";
        String actualDefintion = map.get("B");
        assertEquals("Expected mailing list definition not as expected", expectedDefinition, actualDefintion);
}
    
    //Covers: sequence and defining mailing lists
    @Test
    public void testSequencesAndMailingListDefinition() throws UnableToParseException {
        //animals: tiger@rawr, lion@rawr, cute@meow, chicken@c.h.i.c.k.e.n
        //field: tiger@rawr, lion@rawr
        //house: cute@meow
        //wild: chicken@c.h.i.c.k.e.n
        String input = "animals = field, house, wild ; field = tiger@rawr, lion@rawr ; house = cats ! dogs ; "
                        + "cats = cute@meow ! field; wild = chicken@c.h.i.c.k.e.n ";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        ListExpression chickenEmail = ListExpression.makeEmail("chicken@c.h.i.c.k.e.n");
        ListExpression expectedParsed = chickenEmail;
        ListExpression parsed = ListExpressionParser.parse(input, definedMailingLists);    
        String animalsExpected = "field,house,wild";
        String fieldExpected = "tiger@rawr,lion@rawr";
        String houseExpected = "cats!dogs";
        String wildExpected = "chicken@c.h.i.c.k.e.n";
        String catsExpected = "cute@meow!field";
        String animalsActual = definedMailingLists.get("animals");
        String fieldActual = definedMailingLists.get("field");
        String houseActual = definedMailingLists.get("house");
        String wildActual = definedMailingLists.get("wild");
        String catsActual = definedMailingLists.get("cats");
        assertEquals("expected operation to return correct mailing list", expectedParsed, parsed);
        assertEquals("defined mailing list definition not as expected", animalsExpected, animalsActual);
        assertEquals("defined mailing list definition not as expected", fieldExpected, fieldActual);
        assertEquals("defined mailing list definition not as expected", houseExpected, houseActual);
        assertEquals("defined mailing list definition not as expected", wildExpected, wildActual);
        assertEquals("defined mailing list definition not as expected", catsExpected, catsActual);
    }
    
    //Covers: three operations (union, difference, intersection)
    @Test
    public void testThreeOperations() throws UnableToParseException {
        //Should group like: (student1 , (students ! (student 1 * some6031People))) which produces students.
        String input = STUDENT_1_EMAIL + ", students !" + STUDENT_1_EMAIL + " * some6031People";
        ListExpression expected = STUDENTS;
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031));
    }
    
    //Covers: four operations (union, difference, intersection, sequence)
    @Test
    public void testFourOperations() throws UnableToParseException {
        //Should group like: (student1 , (students ! ((student 1 * some6031People) ; students)) which produces students.
        String input = STUDENT_1_EMAIL + ", students !" + "(" + STUDENT_1_EMAIL + " * some6031People)" + "; students";
        ListExpression expected = STUDENTS;
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031));
    }
    
    //Covers: all five operations (union, difference, intersection, sequence)
    @Test
    public void testAllFiveOperations() throws UnableToParseException {
        //Should group like: ((instructor2 * instructor2) , (animals ! some6031people)) ; ( animals = student1, student2) which produces students.
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        String input = INSTRUCTOR_2_EMAIL + "*" + INSTRUCTOR_2_EMAIL + " , animals ! some6031People ; animals = " + STUDENT_1_EMAIL + " , " + STUDENT_2_EMAIL;
        ListExpression expected = ListExpression.makeUnion(STUDENT_1, STUDENT_2);
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, definedMailingLists));
    }
    
    //Covers: redefining mailing lists / editing list definitions
    @Test
    public void testRedefiningMailingLists() throws UnableToParseException {
        String input = "animals = students; animalS = instructors";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        definedMailingLists.addMailingList("students", STUDENTS_STRING);
        definedMailingLists.addMailingList("instructors", INSTRUCTORS_STRING);
        ListExpression parsed = ListExpressionParser.parse(input, definedMailingLists);
        ListExpression expectedParsed = INSTRUCTORS;
        String actual = definedMailingLists.get("animals");
        String expected = "instructors";
        assertEquals("Parsed list expression was not as expected", expectedParsed, parsed);
        assertEquals("Expected definition of mailing list was incorrect", expected, actual);
    }
    
    //Covers: redefining mailing lists / editing list definitions
    @Test
    public void testRedefiningMailingLists2() throws UnableToParseException {
        String input = "animals = students; animalS = animals, instructors ; animALS, students";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        definedMailingLists.addMailingList("students", STUDENTS_STRING);
        definedMailingLists.addMailingList("instructors", INSTRUCTORS_STRING);
        ListExpression parsed = ListExpressionParser.parse(input, definedMailingLists);
        ListExpression expectedParsed = EVERYONE;
        String actual = definedMailingLists.get("animals");
        String expected = "(students),instructors";
        assertEquals("Parsed list expression was not as expected", expectedParsed, parsed);
        assertEquals("Expected definition of mailing list was incorrect", expected, actual);
    }
    
    //Covers: redefining mailing lists / editing list definitions 
    @Test
    public void testRedefiningMailingLists3() throws UnableToParseException {
        String input = "animals = students ! some; animalS = animals, instructors ; animALS, students";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        definedMailingLists.addMailingList("students", STUDENTS_STRING);
        definedMailingLists.addMailingList("instructors", INSTRUCTORS_STRING);
        ListExpression parsed = ListExpressionParser.parse(input, definedMailingLists);
        ListExpression expectedParsed = EVERYONE;
        String actual = definedMailingLists.get("animals");
        String expected = "(students!some),instructors";
        assertEquals("Parsed list expression was not as expected", expectedParsed, parsed);
        assertEquals("Expected definition of mailing list was incorrect", expected, actual);
    }
    
    @Test
    public void testRedefiningMailingLists4() {
        String input = "a = hello; hello = a@a, b@b; a = a, c@c";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        ListExpression a = ListExpression.makeEmail("a@a");
        ListExpression b = ListExpression.makeEmail("b@b");
        ListExpression c = ListExpression.makeEmail("c@c");
        ListExpression expectedParsed = ListExpression.makeUnion(ListExpression.makeUnion(a, b), c);
        ListExpression parsed = ListExpressionParser.parse(input, definedMailingLists);
        String actualA = definedMailingLists.get("a");
        String expectedA = "(hello),c@c";
        String actualHello = definedMailingLists.get("hello");
        String expectedHello = "a@a,b@b";
        assertEquals("Parsed list expression was not as expected", expectedParsed, parsed);
        assertEquals("Expected definition of mailing list was incorrect", expectedA, actualA);
        assertEquals("Expected definition of mailing list was incorrect", expectedHello, actualHello);
    }
    
    @Test
    public void testRedefiningMailingLists5() {
        String input = "a = hello; hello = a@a, b@b; a = a, c@c; a = a, b@b";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        ListExpression a = ListExpression.makeEmail("a@a");
        ListExpression b = ListExpression.makeEmail("b@b");
        ListExpression c = ListExpression.makeEmail("c@c");
        ListExpression expectedParsed = ListExpression.makeUnion(ListExpression.makeUnion(a, b), c);
        ListExpression parsed = ListExpressionParser.parse(input, definedMailingLists);
        String actualA = definedMailingLists.get("a");
        String expectedA = "((hello),c@c),b@b";
        String actualHello = definedMailingLists.get("hello");
        String expectedHello = "a@a,b@b";
        assertEquals("Parsed list expression was not as expected", expectedParsed, parsed);
        assertEquals("Expected definition of mailing list was incorrect", expectedA, actualA);
        assertEquals("Expected definition of mailing list was incorrect", expectedHello, actualHello);
    }

    //Covers: includes matched parentheses
    @Test
    public void testRegroupingParensUnion() throws UnableToParseException {
        //Should group like (students, instructors) ! some6031People, which produces student2, student3, instructor2
        //instead of students, (instructors ! some6031People) which produces students, instructor2
        String input = "(students, instructors) ! some6031People"; 
        ListExpression expected = ListExpression.makeUnion(ListExpression.makeUnion(STUDENT_2, STUDENT_3), INSTRUCTOR_2);
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031)); 
    }
    
    //Covers: includes matched parentheses
    @Test 
    public void testRegroupingParensDifference() throws UnableToParseException {
        //Should group like (students ! some6031People) * instructors, which produces no one.         
        // Instead of  students ! (some6031People * instructors), which produces students
        String input = "(students ! some6031People) * instructors"; 
        ListExpression expected = EMPTY;
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031));
    }
    
    //Covers: includes matched parentheses
    @Test
    public void testParensIntersectionNoRegrouping() throws UnableToParseException {
      //Should group like students, (instructors*some6031People) which produces students, instructor1
        //NOT like (students, instructors) * some6031People, which produces student1, instructor1
        String unionFirst = "students, (instructors * some6031People)"; 
        ListExpression expectedUnionFirst = ListExpression.makeUnion(STUDENTS, INSTRUCTOR_1);
        assertEquals("expected operation to return correct mailing list", expectedUnionFirst, ListExpressionParser.parse(unionFirst, ENVIRONMENT_6031));
    }
    
    //Covers: includes nested parentheses
    @Test
    public void testNestedParentheses() throws UnableToParseException {
        String unionFirst = "((((students, ((instructors * some6031People))))))"; 
        ListExpression expectedUnionFirst = ListExpression.makeUnion(STUDENTS, INSTRUCTOR_1);
        assertEquals("expected operation to return correct mailing list", expectedUnionFirst, ListExpressionParser.parse(unionFirst, ENVIRONMENT_6031));
    }
    
    //Covers: includes whitespace 
    @Test
    public void testWeirdWhitespace() throws UnableToParseException {
        String input = " students , \t \r \n instructors      ";
        ListExpression expected = EVERYONE; 
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031));
    }
    
    //Uses example given in Norn 2 instructions
    @Test
    public void testReading1() throws UnableToParseException {
        String input = " room1=alice@mit.edu; room1=room1,eve@mit.edu; room1 ";
        ListExpression alice = ListExpression.makeEmail("alice@mit.edu");
        ListExpression eve = ListExpression.makeEmail("eve@mit.edu");
        ListExpression expected = ListExpression.makeUnion(alice, eve); 
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, new DefinedMailingLists()));
    }
   
    //Uses example given in Norn 2 instructions
    @Test
    public void testReading2() throws UnableToParseException {
        String input = " room1=alice@mit.edu; room2=bob@mit.edu; suite=room1,room2; room1=eve@mit.edu; suite ";
        ListExpression eve = ListExpression.makeEmail("eve@mit.edu");
        ListExpression bob = ListExpression.makeEmail("bob@mit.edu");
        ListExpression expected = ListExpression.makeUnion(eve, bob); 
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, new DefinedMailingLists()));
    }
    
    //Uses example given in Norn 2 instructions
    @Test
    public void testReadingDependencies(){
        String input = "suite=room1,room2; room1=alice@mit.edu; room2=bob@mit.edu; suite ";
        ListExpression parsedOne = ListExpressionParser.parse(input, new DefinedMailingLists());
        String inputDuplicate = "suite=room1,room2; room1=alice@mit.edu; room2=bob@mit.edu; suite ";
        ListExpression parsedTwo = ListExpressionParser.parse(inputDuplicate, new DefinedMailingLists());
        assertEquals("lists that depend on each other can be created in any order", parsedOne, parsedTwo);
    }
    
    //Covers: includes unmatched left parentheses (invalid) 
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidUnmatchedLeftParen() throws UnableToParseException {
        String input = "everyone , (students ! instructors";
        ListExpression parsed = ListExpressionParser.parse(input,  ENVIRONMENT_6031);
    }
    
    //Covers: includes unmatched right parentheses (invalid) 
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidUnmatchedRightParen() throws UnableToParseException {
        String input = "everyone * students ! instructors)";
        ListExpression parsed = ListExpressionParser.parse(input,  ENVIRONMENT_6031);
    }
    
    //Covers: adjacent lists without operations (invalid) 
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidAdjacentListsNoOperation() throws UnableToParseException {
        String input = "everyone students instructors";
        ListExpression parsed = ListExpressionParser.parse(input,  ENVIRONMENT_6031);
    }
    
    //Covers: invalid name 
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidName() throws UnableToParseException {
        String input = "students , can't_have_$$$$_in_your_name_:(";
        ListExpression parsed = ListExpressionParser.parse(input,  ENVIRONMENT_6031);
    }
    

    @Test(expected=AssertionError.class)
    public void testDependenciesTwo() {
        String input = "bagginses=a";
        DefinedMailingLists map = new DefinedMailingLists();
        ListExpression output = ListExpressionParser.parse(input, map);
        ListExpressionParser.parse("a=bagginses,c", map);
    }

}
