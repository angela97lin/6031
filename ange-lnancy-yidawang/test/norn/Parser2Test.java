package norn;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import org.junit.Test;
import lib6005.parser.UnableToParseException;

public class Parser2Test {

    /*
     * Testing strategy for ListExpressionParser.java: 
     * 
     * Note: "lists" refers to any combination of email addresses (unions, differences,
     * intersections, sequences)
     * 
     * Test one public method: parse 
     *      TODO: add sequences, empty
     *      -covers all ADT expressions (,!*;) and equals()
     *      Partitions: 
     *          Contains no operations: 
     *              Number of lists: 0, 1
     *          Just makeUnion (,): 
     *              Number of lists: 1 (makeUnion with empty list), 2, >2
     *              Single addresses, lists
     *              Overlap between lists, no overlap between lists 
     *          Just difference(!): 
     *              Number of lists: 1 (difference with empty list), 2, >2
     *              Single addresses, lists
     *              First set is a superset, subset, equals, incomparable to other set
     *          Just intersection(*): 
     *              Number of lists: 1 (intersection with empty list), 2, >2
     *              Single addresses, lists
     *              One set is a superset/subset of the other, equals, incomparable 
     *          Combination of operations: 
     *              Number of different operations: 2, 3
     *              Operations used: makeUnion, difference, intersection (test every combination)
     *              Single addresses, lists
     *          Contains valid constructs: 
     *              Matched parentheses, whitespace, valid usernames (uppercase letters, lowercase letters, digits, underscores, dashes, periods), 
     *              valid domain name (letters (upper/lower), digits, underscores, dashes, periods), valid defined list names 
     *              (letters (upper/lower), digits, underscores, dashes, periods)
     *          Contains invalid constructs: 
     *              Unmatched parentheses, adjacent lists w/o operations, semicolons, equals, invalid username, invalid domain name, 
     *              invalid list name, undefined list name 
     *         
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
    
    private static final ListExpression STUDENTS = ListExpression.makeUnion(ListExpression.makeUnion(STUDENT_1, STUDENT_2), STUDENT_3);
    private static final ListExpression SOME_PEOPLE = ListExpression.makeUnion(STUDENT_1, INSTRUCTOR_1);
    private static final ListExpression INSTRUCTORS = ListExpression.makeUnion(INSTRUCTOR_1,  INSTRUCTOR_2);
    private static final ListExpression EVERYONE = ListExpression.makeUnion(STUDENTS,  INSTRUCTORS);
    
    private static final DefinedMailingLists ENVIRONMENT_6031 = new DefinedMailingLists();
    {
        ENVIRONMENT_6031.addMailingList("students", STUDENTS);
        ENVIRONMENT_6031.addMailingList("instructors", INSTRUCTORS);
        ENVIRONMENT_6031.addMailingList("some6031people", SOME_PEOPLE);
        ENVIRONMENT_6031.addMailingList("everyone",  EVERYONE);
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
    
    //Covers: no operations, one list (invalid user: special symbol) 
    @Test(expected=UnableToParseException.class)
    public void testSingleMailingListInvalidUserApostrophe() throws UnableToParseException {
        String input = "can't_have_apostrophes@6.031";
        ListExpression parsed = ListExpressionParser.parse(input, new DefinedMailingLists());
        assert false; //should not get to this point
    }
    
    //Covers: no operations, one list (invalid user: at sign) 
    @Test(expected=UnableToParseException.class)
    public void testSingleMailingListInvalidUserAt() throws UnableToParseException {
        String input = "me@my_dorm@6.031";
        ListExpression parsed = ListExpressionParser.parse(input, new DefinedMailingLists());
        assert false; //should not get to this point
    }
    
    
    //Covers: no operations, one list (invalid user: white spaces) 
    @Test(expected=UnableToParseException.class)
    public void testSingleMailingListInvalidUserSpace() throws UnableToParseException {
        String input = "here I am@6.031";
        ListExpression parsed = ListExpressionParser.parse(input, new DefinedMailingLists());
        assert false; //should not get to this point
    }
    
    //Covers: no operations, one list (invalid domain: special symbol) 
    @Test(expected=UnableToParseException.class)
    public void testSingleMailingListInvalidDomainApostrophe() throws UnableToParseException {
        String input = "user@what's_up";
        ListExpression parsed = ListExpressionParser.parse(input, new DefinedMailingLists());
        assert false; //should not get to this point
    }
    
    //Covers: no operations, one list (invalid domain: at sign) 
    @Test(expected=UnableToParseException.class)
    public void testSingleMailingListInvalidDomainAt() throws UnableToParseException {
        String input = "me@mit@cambridge";
        ListExpression parsed = ListExpressionParser.parse(input, new DefinedMailingLists());
        assert false; //should not get to this point
    }
    
    //Covers: no operations, one list (invalid domain: white space) 
    @Test(expected=UnableToParseException.class)
    public void testSingleMailingListInvalidDomainSpace() throws UnableToParseException {
        String input = "me@MIT in Cambridge";
        ListExpression parsed = ListExpressionParser.parse(input, new DefinedMailingLists());
        assert false; //should not get to this point
    }
    
    //Covers: case insensitivity for username
    @Test
    public void testSingleMailingListCaseInsensitive() throws UnableToParseException {
        String input = "AStudent@mIt.EdU";
        ListExpression parsed = ListExpressionParser.parse(input, new DefinedMailingLists());
        ListExpression expected = ListExpression.makeEmail("astudent@mit.edu");
        assertEquals("Expected case not to matter in username and hostname", expected, parsed);
    }
    
    //Covers: just makeUnion, one argument (makeUnion with empty list)
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
    
    //Covers: Just union, two arguments, list don't overlap
    @Test
    public void testUnionTwoAddresses() throws UnableToParseException {
        String input = STUDENT_1_EMAIL + "," + STUDENT_2_EMAIL;
        ListExpression expected = ListExpression.makeUnion(STUDENT_1, STUDENT_2);
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, new DefinedMailingLists()));
    }
    
    //Covers: Just union, two arguments, lists overlap 
    @Test
    public void testUnionTwoListsOverlap() throws UnableToParseException {
        String input = "students, instructors";
        ListExpression expected = ALL_6031_PEOPLE;
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
        Map<String, ListExpression> mailingLists = ENVIRONMENT_6031.getMailingLists();
        ListExpression expected = ListExpression.makeUnion(INSTRUCTOR_1, mailingLists.get("students"));
        assertEquals("expected succesful makeUnion with defined mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031));
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
    
    //Covers: just difference, two arguments, subset, contains lists 
    @Test
    public void testDifferenceSubset() throws UnableToParseException {
        String input = STUDENT_1_EMAIL + " ! students";
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
    
    //Covers: just difference
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
        Map<String, ListExpression> mailingLists = ENVIRONMENT_6031.getMailingLists();
        ListExpression expected = mailingLists.get("students");
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031));
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
    
    //Covers: just intersection
    @Test
    public void testIntersectionEmptyListInMiddle() throws UnableToParseException {
        String input = "students * * some6031People";
        ListExpression expected = EMPTY;
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031));
    }
    
    //Covers: makeUnion and difference
    @Test
    public void testUnionAndDifference() throws UnableToParseException {
        //Note: Difference has preference over makeUnion. Test both arrangements to ensure correct operation order
        
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
    
    //Covers: makeUnion and intersection
    @Test
    public void testUnionAndIntersection() throws UnableToParseException {
        //Note: Intersection has preference over makeUnion. Test both arrangements to ensure correct operation order
        
        //Should group like students, (instructors*some6031People) which produces students, instructor1
        //NOT like (students, instructors) * some6031People, which produces student1, instructor1
        String unionFirst = "students, instructors * some6031People"; 
        ListExpression expectedUnionFirst = ListExpression.makeUnion(STUDENTS, INSTRUCTOR_1);
        System.out.println(ENVIRONMENT_6031.getMailingLists());
        assertEquals("expected operation to return correct mailing list", expectedUnionFirst, ListExpressionParser.parse(unionFirst, ENVIRONMENT_6031));
        
        //Should group like ( instructors * some6031People), students which produces instructor1, students
        //NOT like instructors * (some6031People, students) which products instructor1
        String differenceFirst = "instructors * some6031People, students";
        ListExpression expectedDifferenceFirst = ListExpression.makeUnion(STUDENTS, INSTRUCTOR_1);
        assertEquals("expected operation to return correct mailing list", expectedDifferenceFirst, ListExpressionParser.parse(differenceFirst, ENVIRONMENT_6031));
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
    
    //Covers: all three operations 
    @Test
    public void testAllThreeOperations() throws UnableToParseException {
        //Should group like: (student1 , (students ! (student 1 * some6031People))) which produces students.
        String input = STUDENT_1_EMAIL + ", students !" + STUDENT_1_EMAIL + " * some6031People";
        ListExpression expected = STUDENTS;
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031));
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
    
    //Covers: includes weird whitespace 
    @Test
    public void testWeirdWhitespace() throws UnableToParseException {
        String input = " students , \t \r \n instructors      ";
        ListExpression expected = EVERYONE; 
        assertEquals("expected operation to return correct mailing list", expected, ListExpressionParser.parse(input, ENVIRONMENT_6031));
    }
    
    //Covers: includes left paren (invalid) 
    @Test(expected=UnableToParseException.class)
    public void testUnmatchedLeftParen() throws UnableToParseException {
        String input = "everyone , (students ! instructors";
        ListExpression parsed = ListExpressionParser.parse(input,  ENVIRONMENT_6031);
        assert false; //shouldn't get here
    }
    
    //Covers: includes right paren (invalid) 
    @Test(expected=UnableToParseException.class)
    public void testUnmatchedRightParen() throws UnableToParseException {
        String input = "everyone * students ! instructors)";
        ListExpression parsed = ListExpressionParser.parse(input,  ENVIRONMENT_6031);
        assert false; //shouldn't get here
    }
    
    //Covers: adjacent lists without operations (invalid) 
    @Test(expected=UnableToParseException.class)
    public void testAdjacentListsNoOperation() throws UnableToParseException {
        String input = "everyone students instructors";
        ListExpression parsed = ListExpressionParser.parse(input,  ENVIRONMENT_6031);
        assert false; //shouldn't get here
    }
    
    //Covers: includes semicolon (invalid) 
    @Test(expected=UnableToParseException.class)
    public void testInvalidSemicolon() throws UnableToParseException {
        String input = "everyone , students; everyone , instructors)";
        ListExpression parsed = ListExpressionParser.parse(input,  ENVIRONMENT_6031);
        assert false; //shouldn't get here
    }
    
    //Covers: Includes equals (invalid) 
    @Test(expected=UnableToParseException.class)
    public void testEqualsInvalid() throws UnableToParseException {
        String input = "everyone = students , instructors)";
        ListExpression parsed = ListExpressionParser.parse(input,  ENVIRONMENT_6031);
        assert false; //shouldn't get here
    }
    
    //Covers: invalid name 
    @Test(expected=UnableToParseException.class)
    public void testInvalidName() throws UnableToParseException {
        String input = "students , can't_have_$$$$_in_your_name_:(";
        ListExpression parsed = ListExpressionParser.parse(input,  ENVIRONMENT_6031);
        assert false; //shouldn't get here
    }
    
    //Covers: undefined list name (invalid) 
    @Test(expected=RuntimeException.class) 
    public void testUndefinedListName() throws UnableToParseException {
        String input = "students , TAs"; //TAs is not defined in ENVIRONMENT_6031
        ListExpression parsed = ListExpressionParser.parse(input,  ENVIRONMENT_6031);
        assert false; //shouldn't get here
    }
}
