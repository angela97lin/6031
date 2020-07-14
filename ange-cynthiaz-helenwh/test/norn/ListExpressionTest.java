package norn;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import lib6005.parser.UnableToParseException;

public class ListExpressionTest {

    /*
     * Testing Strategy for ListExpression.java:
     * 
     * Note: most methods already covered by parser tests 
     * (makeUnion, makeDifference, makeIntersection, makeEmail, makeEmail)
     * 
     * getEmailRecipients():
     *  - Empty: always empty set
     *  - Email: always set with just email
     *  - Intersection: 0, 1, >1 emails
     *  - Difference: 0, 1, >1 emails
     *  - Sequence: 0, 1, >1 emails
     *  - Union: 0, 1, >1 emails
     *  
     * equals(): 
     *  - empty mailing lists (always equal)
     *  - email addresses
     *      - same email (is equal)
     *      - different emails (is not equal)
     *  - mailing lists
     *      - consists of same expression operation (is equal)
     *      - consists of different operations, but evaluate to same set of emails (is equal)
     *      - is not equal (has different set of emails)
     *       
     * toString():
     *  - empty mailing lists (0 recipients)
     *  - emails (mailing lists of size 1)
     *  - mailing lists of size > 1
     * 
     * hashCode(): all mailing lists that are equals should have the same hashCode
     *  - empty mailing lists are equals
     *  - the same emails are equals
     *  - mailingLists that are equals
     *      - consist of the same expression operation
     *      - consist of different expression operations, but evaluate to the same set of emails
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
    
    
    // testing getEmailRecipients():
   
    /**
     * Helper method to check the correctness of getEmailRecipients().
     * 
     * Checks that the output of a command has the correct set of ListExpressions expected
     * 
     * @param contains a list of the mailing addresses that should appear in the string. 
     * @param actual the output to test correctness of
     */
    private void assertCorrectEmailRecipients(Set<String> expected, Set<String> actual) {
        //check that every expression in expected appears in actual
        for (String expression : expected) {
            assertTrue(actual.contains(expression));
        }
        //check that every expression in actual appears in expected
        for (String expression : actual) {
            assertTrue(expected.contains(expression));
        }
    }
    
    // covers case where getEmailRecipients on empty
    @Test 
    public void testGetEmailRecipientsEmpty() {
        Set<String> expected = Collections.emptySet();
        Set<String> actual = EMPTY.getEmailRecipients();
        assertCorrectEmailRecipients(expected, actual);
    }

    // covers case where getEmailRecipients on email address
    @Test 
    public void testGetEmailRecipientsEmail() {
        Set<String> expected = new HashSet<>();
        expected.add(STUDENT_1_EMAIL);
        Set<String> actual = STUDENT_1.getEmailRecipients();
        assertCorrectEmailRecipients(expected, actual);
    }
   
    // covers case where getEmailRecipients on union (0 emails)
    @Test 
    public void testGetEmailRecipientsUnionZeroEmails() {
        Set<String> expected = Collections.emptySet();
        ListExpression union = ListExpression.makeUnion(EMPTY, EMPTY);
        Set<String> actual = union.getEmailRecipients();
        assertCorrectEmailRecipients(expected, actual);
    }
    
    // covers case where getEmailRecipients on union (1 email)
    @Test 
    public void testGetEmailRecipientsUnionOneEmail() {
        Set<String> expected = new HashSet<>();
        expected.add(STUDENT_1_EMAIL);
        expected.add(INSTRUCTOR_1_EMAIL);
        ListExpression union = ListExpression.makeUnion(STUDENT_1, SOME_PEOPLE);
        Set<String> actual = union.getEmailRecipients();
        assertCorrectEmailRecipients(expected, actual);
    }
    
    // covers case where getEmailRecipients on union (> 1 email)
    @Test 
    public void testGetEmailRecipientsUnionMoreThanOneEmail() {
        Set<String> expected = new HashSet<>();
        expected.add(STUDENT_1_EMAIL);
        expected.add(STUDENT_2_EMAIL);
        expected.add(STUDENT_3_EMAIL);
        expected.add(INSTRUCTOR_1_EMAIL);
        expected.add(INSTRUCTOR_2_EMAIL);
        ListExpression union = ListExpression.makeUnion(SOME_PEOPLE, EVERYONE);
        Set<String> actual = union.getEmailRecipients();
        assertCorrectEmailRecipients(expected, actual);
    }
    
    // covers case where getEmailRecipients on intersection (0 emails)
    @Test 
    public void testGetEmailRecipientsIntersectionZeroEmails() {
        Set<String> expected = Collections.emptySet();
        ListExpression intersection = ListExpression.makeIntersection(STUDENT_1, EMPTY);
        Set<String> actual = intersection.getEmailRecipients();
        assertCorrectEmailRecipients(expected, actual);
    }
    
    // covers case where getEmailRecipients on intersection (1 email)
    @Test 
    public void testGetEmailRecipientsIntersectionOneEmail() {
        Set<String> expected = new HashSet<>();
        expected.add(STUDENT_1_EMAIL);
        ListExpression intersection = ListExpression.makeIntersection(STUDENT_1, SOME_PEOPLE);
        Set<String> actual = intersection.getEmailRecipients();
        assertCorrectEmailRecipients(expected, actual);
    }
    
    // covers case where getEmailRecipients on intersection (> 1 email)
    @Test 
    public void testGetEmailRecipientsIntersectionMoreThanOneEmail() {
        Set<String> expected = new HashSet<>();
        expected.add(STUDENT_1_EMAIL);
        expected.add(INSTRUCTOR_1_EMAIL);
        ListExpression intersection = ListExpression.makeIntersection(SOME_PEOPLE, EVERYONE);
        Set<String> actual = intersection.getEmailRecipients();
        assertCorrectEmailRecipients(expected, actual);
    }
    
    // covers case where getEmailRecipients on difference (0 emails)
    @Test 
    public void testGetEmailRecipientsDifferenceZeroEmails() {
        Set<String> expected = Collections.emptySet();
        ListExpression difference = ListExpression.makeDifference(STUDENT_1, STUDENT_1);
        Set<String> actual = difference.getEmailRecipients();
        assertCorrectEmailRecipients(expected, actual);
    }
    
    // covers case where getEmailRecipients on difference (1 email)
    @Test 
    public void testGetEmailRecipientsDifferenceOneEmail() {
        Set<String> expected = Collections.emptySet();
        ListExpression difference = ListExpression.makeDifference(STUDENT_1, SOME_PEOPLE);
        Set<String> actual = difference.getEmailRecipients();
        assertCorrectEmailRecipients(expected, actual);
    }
    
    // covers case where getEmailRecipients on difference (> 1 email)
    @Test 
    public void testGetEmailRecipientsDifferenceMoreThanOneEmail() {
        Set<String> expected = new HashSet<>();
        expected.add(INSTRUCTOR_1_EMAIL);
        expected.add(INSTRUCTOR_2_EMAIL);
        ListExpression difference = ListExpression.makeDifference(EVERYONE, STUDENTS);
        Set<String> actual = difference.getEmailRecipients();
        assertCorrectEmailRecipients(expected, actual);
    }
    
    // covers case where getEmailRecipients on sequence (0 emails)
    @Test 
    public void testGetEmailRecipientsSequenceZeroEmails() {
        Set<String> expected = Collections.emptySet();
        ListExpression sequence = ListExpression.makeSequence(INSTRUCTOR_1, EMPTY);
        Set<String> actual = sequence.getEmailRecipients();
        assertCorrectEmailRecipients(expected, actual);
    }
    
    // covers case where getEmailRecipients on sequence (1 email)
    @Test 
    public void testGetEmailRecipientsSequenceOneEmail() {
        Set<String> expected = new HashSet<>();
        expected.add(STUDENT_1_EMAIL);
        expected.add(INSTRUCTOR_1_EMAIL);
        ListExpression sequence = ListExpression.makeSequence(STUDENT_2, SOME_PEOPLE);
        Set<String> actual = sequence.getEmailRecipients();
        assertCorrectEmailRecipients(expected, actual);
    }
    
    // covers case where getEmailRecipients on sequence (> 1 email)
    @Test 
    public void testGetEmailRecipientsSequenceMoreThanOneEmail() {
        Set<String> expected = new HashSet<>();
        expected.add(STUDENT_1_EMAIL);
        expected.add(STUDENT_2_EMAIL);
        expected.add(STUDENT_3_EMAIL);
        expected.add(INSTRUCTOR_1_EMAIL);
        expected.add(INSTRUCTOR_2_EMAIL);
        ListExpression sequence = ListExpression.makeSequence(SOME_PEOPLE, EVERYONE);
        Set<String> actual = sequence.getEmailRecipients();
        assertCorrectEmailRecipients(expected, actual);
    }
    
    /**
     * Helper method to check the correctness of toString().
     * 
     * Checks that the output of a command has a correct string representation, without 
     * specifying the order that mailing addresses appear in the string. 
     * 
     * @param contains a list of the mailing addresses that should appear in the string. 
     * @param actual the output to test correctness of
     */
    private void assertCorrectStringRep(List<String> contains, String actual) {
        //check that every string in contains appears in actual
        for (String makeEmail : contains) {
            assertTrue(actual.contains(makeEmail));
        }
        //assert that actual does not contain any extra addresses 
        assertTrue(actual.split(",").length == contains.size());
    }
    
    // covers case where toString on empty
    @Test 
    public void testEmptyToString() {
        String input = ListExpression.makeEmpty().toString();
        String expected = "";
        assertTrue("empty list should be an empty string", expected.equals(input));
    }
    
    // covers case where toString on valid email
    @Test
    public void testValidEmailToStringOne() {      
        assertEquals("toString for list expression not printed correctly", STUDENT_1_EMAIL, STUDENT_1.toString());
    }
    
    // covers case where toString on valid email
    @Test
    public void testValidEmailToStringTwo() {      
        assertEquals("toString for list expression not printed correctly", STUDENT_2_EMAIL, STUDENT_2.toString());
    }
    
    // covers case where toString on valid email
    @Test
    public void testValidEmailToStringThree() {      
        assertEquals("toString for list expression not printed correctly", STUDENT_3_EMAIL, STUDENT_3.toString());
    }

    // covers case where toString on valid email
    @Test
    public void testValidEmailToStringFour() {      
        ListExpression input = ListExpression.makeEmail("YAM@bu.edu");
        String actual = input.toString();
        String expected = "yam@bu.edu";
        assertEquals("toString for list expression not printed correctly", expected, actual);
    }
    
    // covers case where toString on mailing lists of size 1 (union)
    @Test
    public void testUnionEmptyToString() {
        String input1 = ListExpression.makeUnion(ListExpression.makeEmpty(), ListExpression.makeEmpty()).toString();
        String expected1 = "";
        assertTrue("empty string is rendered correctly", expected1.equals(input1));
    }
    
    // covers case where toString on mailing lists of size 1 (intersection)
    @Test
    public void testIntersectEmptyToString() {
        String input1 = ListExpression.makeIntersection(ListExpression.makeEmpty(), ListExpression.makeEmpty()).toString();
        String expected1 = "";
        assertTrue("empty string is rendered correctly", expected1.equals(input1));
    }
    
    // covers case where toString on mailing lists of size 1 (difference) 
    @Test
    public void testDifferenceEmptyToString() {
        String input1 = ListExpression.makeDifference(ListExpression.makeEmpty(), ListExpression.makeEmpty()).toString();
        String expected1 = "";
        assertTrue("empty string is rendered correctly", expected1.equals(input1));
    }
    
    // covers case where toString on mailing lists of size 1 (sequence) 
    @Test
    public void testSequenceEmptyToString() {
        String input1 = ListExpression.makeSequence(ListExpression.makeEmpty(), ListExpression.makeEmpty()).toString();
        String expected1 = "";
        assertTrue("empty string is rendered correctly", expected1.equals(input1));
    }
    
    // covers case where toString on mailing lists of size 1 (union)
    @Test
    public void testUnionEmptyEmailToString() {
        ListExpression input1 = ListExpression.makeEmail("YAM@bu.edu");
        String output1 = ListExpression.makeUnion(input1, ListExpression.makeEmpty()).toString();
        String expected1 = "yam@bu.edu";
        assertTrue("string is rendered correctly", expected1.equals(output1));
    }
    
    // covers case where toString on mailing lists of size > 1
    @Test
    public void testUnionToString() throws UnableToParseException {
        String everyoneString = EVERYONE.toString();
        assertCorrectStringRep(Arrays.asList(INSTRUCTOR_1_EMAIL,INSTRUCTOR_2_EMAIL,STUDENT_1_EMAIL,STUDENT_2_EMAIL,STUDENT_3_EMAIL), everyoneString);
    }
    
    // covers case where toString on mailing lists of size > 1
    @Test
    public void testSequenceToString() throws UnableToParseException {
        ListExpression input = ListExpression.makeSequence(EVERYONE, INSTRUCTORS);
        String instructorString = input.toString();
        assertCorrectStringRep(Arrays.asList(INSTRUCTOR_1_EMAIL,INSTRUCTOR_2_EMAIL), instructorString);
    }
    
    //tests toString on mailing lists of size > 1 (intersection, difference)
    @Test
    public void testExpressionsToStringOne() throws UnableToParseException {
        //(bon_jour@m.i.t,yam@bu.edu)*(bon_jour@m.i.t, hello@mit)!yef29m@bu___x.edu, bon_jour@m.i.t,;
        ListExpression email1 = ListExpression.makeEmail("bon_jour@m.i.t");
        ListExpression email2 = ListExpression.makeEmail("yam@bu.edu");
        ListExpression email3 = ListExpression.makeEmail("hello+++@mit");
        ListExpression email4 = ListExpression.makeEmail("yef29m@bu___x.edu");
        ListExpression union1 = ListExpression.makeUnion(email1, email2);
        ListExpression union2 = ListExpression.makeUnion(email1, email3);
        ListExpression intersection = ListExpression.makeIntersection(union1, union2);
        ListExpression difference = ListExpression.makeDifference(intersection, email4);
        ListExpression union3 = ListExpression.makeUnion(difference, email1);
        ListExpression actual = ListExpression.makeUnion(union3, EMPTY);
        assertCorrectStringRep(Arrays.asList("bon_jour@m.i.t"), actual.toString());
    }
    
    //tests toString on mailing lists of size > 1
    @Test
    public void testExpressionsToStringTwo() throws UnableToParseException {
        //(bon_jour@m.i.t,yam@bu.edu)*(bon_jour@m.i.t, hello@mit)!bon_jour@m.i.t, yef29m@bu___x.edu, 
        ListExpression email1 = ListExpression.makeEmail("bon_jour@m.i.t");
        ListExpression email2 = ListExpression.makeEmail("yam@bu.edu");
        ListExpression email3 = ListExpression.makeEmail("hello+++@mit");
        ListExpression email4 = ListExpression.makeEmail("yef29m@bu___x.edu");
        ListExpression union1 = ListExpression.makeUnion(email1, email2);
        ListExpression union2 = ListExpression.makeUnion(email1, email3);
        ListExpression intersection = ListExpression.makeIntersection(union1, union2);
        ListExpression difference = ListExpression.makeDifference(intersection, email1);
        ListExpression union3 = ListExpression.makeUnion(difference, email4);
        ListExpression actual = ListExpression.makeUnion(union3, EMPTY);
        assertCorrectStringRep(Arrays.asList("yef29m@bu___x.edu"), actual.toString());
    }
    
    // testing equals():
    
    // covers case where empty mailing lists are equals
    @Test
    public void testEmptyEquals() throws UnableToParseException {
        ListExpression alsoEmpty = ListExpression.makeEmpty();
        assertEquals("empty is equal to empty", EMPTY, alsoEmpty);
    }
    
    // covers case where two same emails are equal
    @Test
    public void testEmailEqualsAreEqual() throws UnableToParseException {
        assertEquals("two equivalent list expressions should be .equals", INSTRUCTOR_1, INSTRUCTOR_1);
    }
    
    // covers case where two different emails are not equal
    @Test
    public void testEmailEqualsNotEqual() throws UnableToParseException {      
        assertFalse("two list expressions should not be equivalent", INSTRUCTOR_1.equals(INSTRUCTOR_2));
    }
    
    // covers case where two equivalent mailing lists are equal, same operations
    @Test
    public void testMailingListEqualsSameOperationsOne() throws UnableToParseException {
        ListExpression somePeopleEqual = ListExpression.makeUnion(STUDENT_1, INSTRUCTOR_1);
        assertEquals("list expressions should be equivalent", SOME_PEOPLE, somePeopleEqual);
    }
    
    // covers case mailing lists are equal, different operations
    @Test
    public void testMailingListEqualsDifferentOperationsOne() throws UnableToParseException {
        ListExpression somePeopleMore = ListExpression.makeUnion(STUDENT_1, 
                                        ListExpression.makeUnion(INSTRUCTOR_2 , INSTRUCTOR_1));
        ListExpression somePeopleEqual = ListExpression.makeIntersection(somePeopleMore, SOME_PEOPLE);
        assertEquals("list expressions should be equivalent", SOME_PEOPLE, somePeopleEqual);
    }
    
    // covers case where mailing lists are equal, different operations (intersection)
    @Test
    public void testMailingListEqualsTwoIntersection() throws UnableToParseException {
        ListExpression instructorOneEqual = ListExpression.makeIntersection(INSTRUCTORS, SOME_PEOPLE);
        assertEquals("list expressions should be equivalent", INSTRUCTOR_1, instructorOneEqual);
    }
    
    // covers case where mailing lists are equal, different operations (sequence)
    @Test
    public void testMailingListEqualsTwoSequence() throws UnableToParseException {
        ListExpression instructorsEqual = ListExpression.makeSequence(EVERYONE, INSTRUCTORS);
        assertEquals("list expressions should be equivalent", INSTRUCTORS, instructorsEqual);
    }
    
    // covers case where mailing lists are equal, different operations (difference)
    @Test
    public void testMailingListEqualsTwoDifference() throws UnableToParseException {
        ListExpression studentsEqual = ListExpression.makeDifference(EVERYONE, INSTRUCTORS);
        assertEquals("list expressions should be equivalent", STUDENTS, studentsEqual);
    }
    
    // covers case where mailing lists are not equal
    @Test
    public void testMailingListNotEquals() throws UnableToParseException {
        assertFalse("list expressions should not be equivalent", INSTRUCTORS.equals(EMPTY));
        assertFalse("list expressions should not be equivalent", INSTRUCTORS.equals(SOME_PEOPLE));
        assertFalse("list expressions should not be equivalent", INSTRUCTORS.equals(EVERYONE));
    }
    
    // testing hashCode():

    // covers case where hashCode of empty mailing lists are equals
    @Test
    public void testEmptyEqualsHashCode() throws UnableToParseException {
        ListExpression alsoEmpty = ListExpression.makeEmpty();
        assertEquals("empty is equal to empty", EMPTY, alsoEmpty);
        assertEquals("hashcode for two lists should be equal", EMPTY.hashCode(), alsoEmpty.hashCode());
    }
    
    // covers case where hashCode of two empty mailing lists (but not created from same operations) are equal
    @Test
    public void testDifferenceEmptyHashCode() {
        ListExpression EMPTY = ListExpression.makeEmpty();
        ListExpression emptyDifference = ListExpression.makeDifference(EMPTY, EMPTY);
        assertEquals("empty is equal to empty", EMPTY, emptyDifference);
        assertEquals("hashcode for two lists should be equal", EMPTY.hashCode(), emptyDifference.hashCode());
    }
    
    // covers case where hashCode for two equivalent emails are equal 
    @Test
    public void testEmailEqualsHashCodeOne() throws UnableToParseException {
        ListExpression email1 = ListExpression.makeEmail("heLlow@mit");
        ListExpression email2 = ListExpression.makeEmail("heLlow@mIt");        
        assertEquals("list expressions should be equivalent", email1.hashCode(), email2.hashCode());
        assertEquals("hashCode of two equivalent list expressions should be equivalent", email1, email2);
    }
    
    // covers case where hashCode for two equivalent emails are equal 
    @Test
    public void testEmailEqualsHashCodeTwo() throws UnableToParseException {
        ListExpression email1 = ListExpression.makeEmail("h0la@-m1t-");
        ListExpression email2 = ListExpression.makeEmail("h0la@-m1t-");
        assertEquals("hashCode of two equivalent list expressions should be equivalent", email1, email2);
        assertEquals("list expressions should be equivalent", email1.hashCode(), email2.hashCode());
    }
    
    // covers case where hashCode for two equivalent mailing lists that are equal, same operations
    @Test
    public void testMailingListHashCodeSameOperationsOne() throws UnableToParseException {
        ListExpression somePeopleEqual = ListExpression.makeUnion(STUDENT_1, INSTRUCTOR_1);
        assertEquals("list expressions should be equivalent", SOME_PEOPLE, somePeopleEqual);
        assertEquals("hashCode of two equivalent list expressions should be equivalent", SOME_PEOPLE.hashCode(), somePeopleEqual.hashCode());
    }
    
    // covers case where hashCode on mailing lists that are equal, different operations
    @Test
    public void testMailingListHashCodeDifferentOperationsOne() throws UnableToParseException {
        ListExpression somePeopleMore = ListExpression.makeUnion(STUDENT_1, 
                                        ListExpression.makeUnion(INSTRUCTOR_2 , INSTRUCTOR_1));
        ListExpression somePeopleEqual = ListExpression.makeIntersection(somePeopleMore, SOME_PEOPLE);
        assertEquals("hashCode of two equivalent list expressions should be equivalent", SOME_PEOPLE.hashCode(), somePeopleEqual.hashCode());
    }
    
    // covers case where hashCode on mailing lists that are equal, different operations (intersection)
    @Test
    public void testMailingListHashCodeTwo() throws UnableToParseException {
        ListExpression instructorOneEqual = ListExpression.makeIntersection(INSTRUCTORS, SOME_PEOPLE);
        assertEquals("list expressions should be equivalent", INSTRUCTOR_1, instructorOneEqual);
        assertEquals("hashCode of two equivalent list expressions should be equivalent", INSTRUCTOR_1.hashCode(), instructorOneEqual.hashCode());
    }
    
    // covers case where mailing lists are equal, different operations (sequence)
    @Test
    public void testMailingListHashCodeThree() throws UnableToParseException {
        ListExpression instructorsEqual = ListExpression.makeSequence(EVERYONE, INSTRUCTORS);
        assertEquals("list expressions should be equivalent", INSTRUCTORS, instructorsEqual);
        assertEquals("hashCode of two equivalent list expressions should be equivalent", INSTRUCTORS.hashCode(), instructorsEqual.hashCode());

    }
    
}
