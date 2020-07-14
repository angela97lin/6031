package norn;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import lib6005.parser.UnableToParseException;


public class UserInputTest {

    // Testing Strategy for UserInput.java
    //   Partitions for readInput()
    //
    //      Invalid input:
    //        -check correct error messages printed in console for invalid inputs
    //          - invalid emails (parse error)
    //          - invalid list names (illegal mailing list name error)
    //          - mail loops (like a=b;b=a)
    //          - invalid nested list definitions (a=b)=c
    
    //      Set Operators 
    //          - union, intersect, difference
    //          - operations tested in the Parser and ListExpression, so not as thorough
    //              - empty, empty
    //              - empty, email; email, empty
    //              - email, email
    //                  -same emails, different emails
    //              - email, MailingList > 1;
    //                  -no overlap
    //                  -email in MailingList
    //              - MailingList, MailingList; 
    //                  -no overlap
    //                  -some overlap
    //                  -MailingList == MailingList
    //
    //      DefinedMailingLists assignments  
    //         - mailing list not in environment
    //         - assignment to empty mailing list
    //         - assignment to one email
    //         - assignment to list > 1
    //         - assigning a mailing list to another mailing list (by name)
    //         - assigning a mailing list to itself
    //         - assigning a mailing list to emails / other mailing lists that contain the name
    //         - nested list definition (==)
    //         - editing a mailing list
    //         - editing a mailing list of which another list is dependent on
    //
    //      Sequences
    //         - one expression in sequence ( expression )
    //         - multiple expressions in sequence ( expression ; expression ; etc.) 
    //         - sequence of semicolons ( ;;; )
    //         - followed/preceded by semicolons 
    //
    //      Grouping
    //         - test order of precedence: () * ! , = ;
    //           (i.e. grouping, intersection, difference, union, definition, sequence)

    //      Multiple calls to readInput() with the same UserInput instance
    //         - editing a mailing list
    //         - assigning a mailing list to another mailing list (by name)
    //         - editing a mailing list of which another list is dependent on
    
    
    /**
     * Checks that the output of a command has a correct string representation, without 
     * specifying the order that mailing addresses appear in the string. 
     * 
     * @param contains A list of the mailing addresses that should appear in the string. 
     * @param actual The output to test
     */
    private void assertCorrectStringRep(List<String> contains, String actual) {
        //check that every string in contains appears in actual
        for (String address : contains) {
            assert actual.contains(address);
        }
        //assert that actual does not contain any extra addresses
        assert actual.split(",").length == contains.size();
    }
    
    // Testing readInput() 
    
    // covers invalid emails
    @Test(expected=UnableToParseException.class)
    public void testInvalidEmailSymbolOne() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "a@@dfs";
        userInput.readInput(input);
    }

    @Test(expected=UnableToParseException.class)
    public void testInvalidEmailSymbolTwo() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "a@d's";
        userInput.readInput(input);
    }

    @Test(expected=UnableToParseException.class)
    public void testInvalidEmailSymbolThree() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "a$@dfs";
        userInput.readInput(input);
    }
    
    // covers invalid list names
    @Test(expected=UnableToParseException.class)
    public void testInvalidListNameOne() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "a@dfs = hello@mit";
        userInput.readInput(input);
    }

    @Test(expected=UnableToParseException.class)
    public void testInvalidListNameTwo() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "a$dfs = hello@mit";
        userInput.readInput(input);
    }

    @Test(expected=UnableToParseException.class)
    public void testInvalidListNameThree() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "a'dfs = hello@mit";
        userInput.readInput(input);
    }    

    // covers mail loops
    @Test(expected=AssertionError.class)
    public void testMailLoops() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "a = b; b=a";
        userInput.readInput(input);
    }
    
    // covers mail loops
    @Test(expected=AssertionError.class)
    public void testMailLoops2() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "a = b; b=a, c@c";
        userInput.readInput(input);
    }
    
    // covers invalid nested list definition
    @Test(expected=UnableToParseException.class)
    public void testInvalidNestedDefinition() throws UnableToParseException {
        UserInput userInput = new UserInput();
        userInput.readInput("fruits = bananas@yellow, strawberries@red");
        userInput.readInput("yummyfood = ramen@sentouka");
        String inputA = "(food = yummyfood) = fruits";
        userInput.readInput(inputA);
    } 

    // Testing readInput() on valid MailingList expressions
    
    // covers union of empty, empty
    @Test
    public void testUnionEmptyEmpty() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = ",";
        String expected = "";
        assertEquals("expected operation to return correct mailing list", expected, userInput.readInput(input));
    }
    
    // covers intersection of empty, empty
    @Test
    public void testIntersectEmptyEmpty() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "**";
        String expected = "";
        assertEquals("expected operation to return correct mailing list", expected, userInput.readInput(input));
    }
    
    // covers difference of empty, empty
    @Test
    public void testDifferenceEmptyEmpty() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "!!";
        String expected = "";
        assertEquals("expected operation to return correct mailing list", expected, userInput.readInput(input));
    }

    // covers union of email, empty
    @Test
    public void testUnionWithEmptyAtEnd() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "student+1@mit.edu" + ",";
        String expected = "student+1@mit.edu";
        assertEquals("expected operation to return correct mailing list", expected, userInput.readInput(input));
    }
    
    // covers intersection of email, empty
    @Test
    public void testIntersectionWithEmptyAtEnd() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "student1@mit.edu * ";
        String expected = "";
        assertEquals("expected operation to return correct mailing list", expected, userInput.readInput(input));
    }
    
    // covers difference of email, empty
    @Test
    public void testDifferenceWithEmptyAtEnd() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "student1@mit.edu ! ";
        String expected = "student1@mit.edu";
        assertEquals("expected operation to return correct mailing list", expected, userInput.readInput(input));
    }
    
   // covers union of empty, email
    @Test
    public void testUnionWithEmptyAtBeginning() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = ", , student1@mit.edu";
        String expected = "student1@mit.edu";
        assertEquals("expected operation to return correct mailing list", expected, userInput.readInput(input));
    }

    // covers union of email, email, where emails don't overlap
    @Test
    public void testUnionTwoAddresses() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "student1@mit.edu, STUDENT+2@EMAIL";
        String expected1 = "student1@mit.edu";
        String expected2 = "student+2@email";
        String result = userInput.readInput(input);
        assertCorrectStringRep(Arrays.asList(expected1, expected2), result);
    }
    
    // covers intersection  of email, email, where emails overlap
    @Test
    public void testIntersectTwoListsOverlap() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "student_1@mit.edu* STUDENT_1@mit.EDU";
        String expected = "student_1@mit.edu";
        assertEquals("expected operation to return correct mailing list", expected, userInput.readInput(input));
    }
    
    // covers difference of mailing lists > 1 where emails overlap
    @Test
    public void testUnionDiffManyListsOverlap() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "student1@mit.edu, STUDENT1@mit.EDU!student3@mit.edu";
        String expected1 = "student1@mit.edu";
        String result = userInput.readInput(input);
        assertCorrectStringRep(Arrays.asList(expected1), result);
    }
    
    // covers union of email, MailingList > 1, with no overlap
    //        assignment to empty mailing list
    //        sequence of two
    @Test
    public void testUnionOneAssignmentEmpty() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "6031 =; 6031, student3@mit.edu";
        String expected = "student3@mit.edu";
        assertEquals("expected operation to return correct mailing list", expected, userInput.readInput(input));
    }
    
    // covers intersection of email, MailingList > 1, with some overlap;
    //        assignment to mailing list > 2
    //        sequence of two
    @Test
    public void testIntersectionTwoAssignmentThree() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "6031 =student3@mit.edu, student2@mit.edu*student1@mit.edu; 6031, student3@mit.edu";
        String expected1 = "student3@mit.edu";
        String result = userInput.readInput(input);
        assertCorrectStringRep(Arrays.asList(expected1), result);
    }
    
    // covers difference of email, MailingList = 1, with complete overlap;
    //        assignment to mailing list = 1
    //        sequence of two
    @Test
    public void testDifferenceTwoAssignmentOne() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "6031 =student3@mit.edu; 6031! student3@mit.edu";
        String expected = "";
        assertEquals("expected operation to return correct mailing list", expected, userInput.readInput(input));
    }
    
    // covers union of two mailing lists, no overlap
    //        assignment to mailing lists > 2
    //        sequence > 2
    @Test
    public void testUnionTwoListsAssignmentTwoNoOverlap() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "6031 =student3@mit.edu, me@mit, u@mit; 6.005= bye@mit.edu, yuck@mit; 6031, 6.005";
        String expected1 = "student3@mit.edu";
        String expected2 = "me@mit";
        String expected3 = "u@mit";
        String expected4 = "bye@mit.edu";
        String expected5 = "yuck@mit";
        String result = userInput.readInput(input);
        assertCorrectStringRep(Arrays.asList(expected1, expected2, expected3, expected4, expected5), result);
    }  
    
    // covers union of two mailing lists, some overlap
    //        assignment to mailing lists > 2
    //        sequence > 2
    @Test
    public void testUnionTwoListsAssignmentTwoSomeOverlap() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "6031 =student3@mit.edu, me@mit, u@mit; 6.005= bye@mit.edu, me@mit; 6031, 6.005";
        String expected1 = "student3@mit.edu";
        String expected2 = "me@mit";
        String expected3 = "u@mit";
        String expected4 = "bye@mit.edu";
        String result = userInput.readInput(input);
        assertCorrectStringRep(Arrays.asList(expected1, expected2, expected3, expected4), result);
    }
    
    // covers union of two mailing lists, complete overlap
    //        assignment to mailing lists > 2
    //        sequence > 2
    @Test
    public void testUnionTwoListsAssignmentTwoAllOverlap() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "6031 =student3@mit.edu, me@mit, u@mit; 6.005= student3@mit.edu, me@mit, u@mit ; 6031, 6.005";
        String expected1 = "student3@mit.edu";
        String expected2 = "me@mit";
        String expected3 = "u@mit";
        String result = userInput.readInput(input);
        assertCorrectStringRep(Arrays.asList(expected1, expected2, expected3), result);
    }   
    
    // covers union with a definition, where parsing such that the definition is valid takes precedence
    @Test
    public void testUnionDefinitionTakesPrecedence() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "a@a,b = c@c"; 
        // union normally takes precendence, but here it's parsed as "(a@a),(b=c@c)"
        String expected1 = "a@a";
        String expected2 = "c@c";
        String result = userInput.readInput(input);
        assertCorrectStringRep(Arrays.asList(expected1, expected2), result);
    } 
    
    // covers union with intersect (intersect has precedence)
    @Test
    public void testUnionUnion() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "6031 =student3@mit.edu, me@mit, yuck@mit; "
                + "6.005= bye@mit.edu, yuck@mit; "
                + "6031* 6.005,hi@mit.edu";
        String expected1 = "yuck@mit";
        String expected2 = "hi@mit.edu";
        String result = userInput.readInput(input);
        assertCorrectStringRep(Arrays.asList(expected1, expected2), result);
    } 
    
    // covers union with difference (difference has precedence)
    @Test
    public void testUnionDifference() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "6031 =student3@mit.edu, me@mit, yuck@mit; "
                + "6.005= bye@mit.edu, yuck@mit; "
                + "hi@mit.edu, 6031! 6.005";
        String expected1 = "student3@mit.edu";
        String expected2 = "me@mit";
        String expected3 = "hi@mit.edu";
        String result = userInput.readInput(input);
        assertCorrectStringRep(Arrays.asList(expected1, expected2, expected3), result);
    } 
    
    // covers intersect with difference (intersect has precedence)
    @Test
    public void testIntersectDifference() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "6031 =student3@mit.edu, me@mit, yuck@mit; "
                + "6.005= bye@mit.edu, yuck@mit; "
                + "6.006 = yuck@mit, ooh@mit; "
                + "6.006! 6031* 6.005";  // 6031*6.005 is yuck@mit
        String expected1 = "ooh@mit";
        String result = userInput.readInput(input);
        assertCorrectStringRep(Arrays.asList(expected1), result);
    } 
    
    // Testing DefinedMailingLists assignments
    
    // covers mailing list not defined in environment (should represent empty set)
    @Test
    public void testNotInEnvironmentOne() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "b";
        String output = userInput.readInput(input);
        String expected = "";
        assertEquals("expected operation to return empty mailing list", expected, output);
    }
    
    @Test
    public void testNotInEnvironmentTwo() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "a = a@a; b";
        String output = userInput.readInput(input);
        String expected = "";
        assertEquals("expected operation to return empty mailing list", expected, output);
    }
    
    @Test
    public void testNotInEnvironmentThree() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "a = b";
        String output = userInput.readInput(input);
        String expected = "";
        assertEquals("expected operation to return empty mailing list", expected, output);
    }
    
    // covers simple assignment returns the right output
    @Test
    public void testSimpleAssignment() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "MIT = helen@mit.edu, anelise@mit.edu";
        assertCorrectStringRep(Arrays.asList("helen@mit.edu", "anelise@mit.edu"), userInput.readInput(input));
    }
    
    // covers assignment of mailing list to another mailing list
    //        assignment to mailing lists > 2
    //        sequence > 2
    @Test
    public void testTwoListsAssignmentSame() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "6031 =student3@mit.edu, me@mit, u@mit; 6.005= 6031; 6.005";
        String expected1 = "student3@mit.edu";
        String expected2 = "me@mit";
        String expected3 = "u@mit";
        String result = userInput.readInput(input);
        assertCorrectStringRep(Arrays.asList(expected1, expected2, expected3), result);
    }

    // covers assigning a mailing list to itself
    @Test
    public void testAssignmentItself() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "numbers = one@mit.edu; numbers = numbers, two@mit.edu";
        String expected1 = "one@mit.edu";
        String expected2 = "two@mit.edu";
        String result = userInput.readInput(input);
        assertCorrectStringRep(Arrays.asList(expected1, expected2), result);
    }
    
    // covers assigning a mailing list to emails / other mailing lists that contain the name
    @Test
    public void testAssignmentContainsName() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "substring = substring@mit.edu; subs = substring";
        String expected1 = "substring@mit.edu";
        String result = userInput.readInput(input);
        assertCorrectStringRep(Arrays.asList(expected1), result);
    }
    
    // covers nested list definition
    @Test
    public void testNestedDefinitionToEmail() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "first = next = hello@mit";
        String expected1 = "hello@mit";
        String result = userInput.readInput(input);
        assertCorrectStringRep(Arrays.asList(expected1), result);
        assertCorrectStringRep(Arrays.asList(expected1), userInput.readInput("first"));
        assertCorrectStringRep(Arrays.asList(expected1), userInput.readInput("next"));
    }

    // covers nested list definition
    @Test
    public void testNestedDefinitionToList() throws UnableToParseException {
        UserInput userInput = new UserInput();
        userInput.readInput("first = first@mit");
        userInput.readInput("next = next@mit");
        String input = "second = next = first"; // equivalent to second = (next = first)
        String result = userInput.readInput(input);
        String expected1 = "first@mit";
        assertCorrectStringRep(Arrays.asList(expected1), result);
        assertCorrectStringRep(Arrays.asList(expected1), userInput.readInput("first"));
        assertCorrectStringRep(Arrays.asList(expected1), userInput.readInput("second"));
        assertCorrectStringRep(Arrays.asList(expected1), userInput.readInput("next"));
    }

    // covers editing a mailing list
    //        sequence > 2
    @Test
    public void testEditOneList() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "6.031 =student3@mit.edu; 6.031= student2@mit.edu, student1@mit.edu; 6.031";
        String expected1 = "student1@mit.edu";
        String expected2 = "student2@mit.edu";
        String result = userInput.readInput(input);
        assertCorrectStringRep(Arrays.asList(expected1, expected2), result);
    }
    
    // covers editing a mailing list of which another list is dependent on
    //        sequence > 2
    @Test
    public void testEditListDependent() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "6.031 =me@mit; 6.005= 6.031, you@mit; 6.031=us@mit, everyone@mit; 6.005";
        String expected1 = "you@mit";
        String expected2 = "us@mit";
        String expected3 = "everyone@mit";
        String result = userInput.readInput(input);
        assertCorrectStringRep(Arrays.asList(expected1, expected2, expected3), result);
    }
    
    // Tests for Sequences
    
    // covers empty sequence of one
    @Test
    public void testEmptySeqOne() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "";
        String expected = "";
        assertEquals("expected operation to return correct mailing list", expected, userInput.readInput(input));
    }
    
    // covers empty sequence of two
    @Test
    public void testEmptySeqTwo() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = ";";
        String expected = "";
        assertEquals("expected operation to return correct mailing list", expected, userInput.readInput(input));
    }
    
    // covers empty sequence of four
    @Test
    public void testEmptySeqMany() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = ";;;";
        String expected = "";
        assertEquals("expected operation to return correct mailing list", expected, userInput.readInput(input));
    }
    
    // covers sequence length 2 that ends in empty expression
    @Test
    public void testFollowEmptySequence() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "6031 =student3@mit.edu, me@mit, u@mit;";
        String expected = "";
        assertEquals("expected operation to return correct mailing list", expected, userInput.readInput(input));
    }
    
    // covers sequence > 2 that ends in empty expression
    @Test
    public void testFollowEmptySequenceMany() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "6031 =student3@mit.edu, me@mit, u@mit; 30.342f = blah2@mit, hi.@harv.; ";
        String expected = "";
        assertEquals("expected operation to return correct mailing list", expected, userInput.readInput(input));
    }
    
    // covers sequence that begins with empty expression
    @Test
    public void testBeginEmptySequence() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "; 6031 =student3@mit.edu, me@mit, u@mit";
        assertCorrectStringRep(Arrays.asList("student3@mit.edu", "me@mit", "u@mit"), userInput.readInput(input));
    }
    
    // covers sequence that begins with empty expression, more operations
    @Test
    public void testBeginEmptySequenceDiff() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "; 6031 =student3@mit.edu!me@mit, u@mit";
        assertCorrectStringRep(Arrays.asList("student3@mit.edu", "u@mit"), userInput.readInput(input));
    }
    
    // covers sequence > 2 that begins with empty expression
    @Test
    public void testBeginEmptySequenceMany() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "; ;       ;6031 =student3@mit.edu, me@mit, u@mit; 30.342f = blah2@mit, hi.@harv.";
        assertCorrectStringRep(Arrays.asList("blah2@mit", "hi.@harv."), userInput.readInput(input)); 
    }
    
    // Tests for Grouping
    // covers grouped union with difference
    @Test
    public void testGroupedUnionDifference() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "6031 =student3@mit.edu, me@mit, yuck@mit; "
                + "(hi@mit.edu, 6031)! (yuck@mit, a@mit)";
        String expected1 = "student3@mit.edu";
        String expected2 = "me@mit";
        String expected3 = "hi@mit.edu";
        // would also expect a@mit if there were no ()
        String result = userInput.readInput(input);
        assertCorrectStringRep(Arrays.asList(expected1, expected2, expected3), result);
    } 
    
    // covers grouped union with intersection
    @Test
    public void testGroupedUnionIntersection() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "6031 =student3@mit.edu, me@mit, hi@mit.edu; "
                + "(yuck@mit, me@mit, 6031)* yuck@mit";
        String expected1 = "yuck@mit";
        // would also expect me@mit if there were no ()
        String result = userInput.readInput(input);
        assertCorrectStringRep(Arrays.asList(expected1), result);
    } 
    
    // covers union with a grouped definition
    @Test
    public void testUnionGroupedDefinition() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "(list = a@a),yuck@mit; list";
        String expected1 = "a@a";
        // would also expect yuck@mit if there were no ()
        String result = userInput.readInput(input);
        assertCorrectStringRep(Arrays.asList(expected1), result);
    }    
    
    // covers union with grouped sequence
    @Test
    public void testUnionGroupedSequence() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "student3@mit.edu, me@mit,(hi@mit.edu ; yuck@mit)";
        String expected1 = "student3@mit.edu";
        String expected2 = "me@mit";
        String expected3 = "yuck@mit";
        // would only expect yuck@mit if there were no ()
        String result = userInput.readInput(input);
        assertCorrectStringRep(Arrays.asList(expected1, expected2, expected3), result);
    } 
    
    // covers grouped difference with intersection
    @Test
    public void testGroupedDifferenceIntersection() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "6031 =student3@mit.edu, me@mit, yuck@mit; "
                + "(6031 ! me@mit)* yuck@mit";  // (6031 ! me@mit) is student3@mit.edu,yuck@mit
        String expected1 = "yuck@mit";
        // would expect all emails in 6031 if there were no ()
        String result = userInput.readInput(input);
        assertCorrectStringRep(Arrays.asList(expected1), result);
    } 
    
    // covers difference with a grouped definition
    @Test
    public void testDifferenceGroupedDefinition() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "(list = yuck@mit)!yuck@mit; list";
        String expected1 = "yuck@mit";
        // would also expect nothing if there were no ()
        String result = userInput.readInput(input);
        assertCorrectStringRep(Arrays.asList(expected1), result);
    } 
    
    // covers difference with grouped sequence
    @Test
    public void testDifferenceGroupedSequence() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "hi@Mit.edu!(bye@mit.edu ; yuck@mit, hi@mit.edu)";
        // would expect yuck@mit, hi@mit.edu if there were no ()
        String result = userInput.readInput(input);
        assertEquals("expected operation to return empty string", "", result);
    } 
    
    // covers intersection with a grouped definition
    @Test
    public void testIntersectionGroupedDefinition() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "(list = yuck@mit, hi@mit)*yuck@mit; list";
        String expected1 = "yuck@mit";
        String expected2 = "yuck@mit";
        // would only expect nothing if there were no ()
        String result = userInput.readInput(input);
        assertCorrectStringRep(Arrays.asList(expected1, expected2), result);
    } 
    
    // covers intersection with grouped sequence
    @Test
    public void testIntersectionGroupedSequence() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String input = "hi@Mit.edu*(bye@mit.edu ; yuck@mit, hi@mit.edu)";
        String expected1 = "hi@mit.edu";
        // would also expect yuck@mit if there were no ()
        String result = userInput.readInput(input);
        assertCorrectStringRep(Arrays.asList(expected1), result);
    } 
    
    // covers definition with grouped definition
    @Test
    public void testDefinitionGroupedDefinition() throws UnableToParseException {
        UserInput userInput = new UserInput();
        userInput.readInput("fruits = bananas@yellow, strawberries@red");
        userInput.readInput("yummyfood = ramen@sentouka");
        String inputA = "food = (yummyfood = fruits)";
        String expectedA1 = "bananas@yellow";
        String expectedA2 = "strawberries@red";
        String resultA = userInput.readInput(inputA);
        assertCorrectStringRep(Arrays.asList(expectedA1, expectedA2), resultA);
        assertCorrectStringRep(Arrays.asList(expectedA1, expectedA2), userInput.readInput("yummyfood"));
        assertCorrectStringRep(Arrays.asList(expectedA1, expectedA2), userInput.readInput("food"));
    } 
    
    // covers definition with grouped sequence
    @Test
    public void testDefinitionGroupedSequence() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String inputA = "6.005 = (bye@mit.edu ; yuck@mit, hi@mit.edu)";
        String expectedA1 = "yuck@mit";
        String expectedA2 = "hi@mit.edu";
        String resultA = userInput.readInput(inputA);
        assertCorrectStringRep(Arrays.asList(expectedA1, expectedA2), resultA);
        String inputB = "6.005";
        String expectedB1 = "yuck@mit";
        String expectedB2 = "hi@mit.edu";
        String resultB = userInput.readInput(inputB);
        assertCorrectStringRep(Arrays.asList(expectedB1, expectedB2), resultB);
        // would expect bye@mit if there were no ()
    } 
    
    // Tests for multiple calls to readInput() with the same UserInput instance
    
    // covers editing an empty defined mailing list
    @Test
    public void testMultipleEditingEmpty() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String inputA = "MIT=";
        assertEquals("expected operation to return empty mailing list", "", userInput.readInput(inputA));
        List<String> reassignment = Arrays.asList("helen@mit.edu", "cynthia@mit.edu");
        String inputB = "MIT = " + reassignment.get(0) + "," + reassignment.get(1);
        assertCorrectStringRep(reassignment, userInput.readInput(inputB));
        String inputC = "MIT";
        assertCorrectStringRep(reassignment, userInput.readInput(inputC));
    }

    // covers editing a non-empty defined mailing list
    @Test
    public void testMultipleEditingNonEmpty() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String inputA = "MIT = helen@mit.edu, cynthia@mit.edu";
        assertCorrectStringRep(Arrays.asList("helen@mit.edu", "cynthia@mit.edu"), 
                userInput.readInput(inputA));
        String inputB = "MIT = harleN@mit.edu, angela_L@mit.edu";
        assertCorrectStringRep(Arrays.asList("harlen@mit.edu", "angela_l@mit.edu"), 
                userInput.readInput(inputB));
        String inputC = "MIT";
        assertCorrectStringRep(Arrays.asList("harlen@mit.edu", "angela_l@mit.edu"), 
                userInput.readInput(inputC));
    }
    // assigning a mailing list to another mailing list (by name)
    @Test
    public void testMultipleAssigningDependent() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String inputA = "goodramen = ramen@santouka, ramen@ippudo";
        assertCorrectStringRep(Arrays.asList("ramen@santouka", "ramen@ippudo"), 
                userInput.readInput(inputA));
        String inputB = "goodfood = goodramen, icecream@toscaninis";
        assertCorrectStringRep(Arrays.asList("ramen@santouka", "ramen@ippudo", "icecream@toscaninis"), 
                userInput.readInput(inputB));
    }
    
    // covers editing a mailing list of which another list is dependent on
    @Test
    public void testMultipleEditingDependent() throws UnableToParseException {
        UserInput userInput = new UserInput();
        String inputA = "MIT = angela@mit.edu, cynthia@mit.edu";
        assertCorrectStringRep(Arrays.asList("angela@mit.edu", "cynthia@mit.edu"), 
                userInput.readInput(inputA));
        String inputB = "ALLMIT = MIT, helen@mit.edu";
        assertCorrectStringRep(Arrays.asList("angela@mit.edu", "cynthia@mit.edu", "helen@mit.edu"), 
                userInput.readInput(inputB));
        String inputC = "MIT = noOne@mit.edu";
        assertCorrectStringRep(Arrays.asList("noone@mit.edu"), userInput.readInput(inputC));
        String inputD = "ALLMIT";
        assertCorrectStringRep(Arrays.asList("noone@mit.edu", "helen@mit.edu"), 
                userInput.readInput(inputD));
    }

}
