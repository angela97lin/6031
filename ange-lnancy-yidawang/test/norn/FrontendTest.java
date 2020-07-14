package norn;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;

public class FrontendTest {

    /*
     * Testing Strategy for Frontend:
     * Mainly testing for correct output and order of output
     * Partitions
     * Inputs:
     *      Valid Expressions:
     *          length of ',' operation = 0, 1, >1
     *          length of '!' operation = 0, 1, >1
     *          length of '*' operation = 0, 1, >1
     *          length of ';' operation = 0, 1, >1
     *          combination of ',', ';', '!', '*' operations
     *              test precedence order
     *          defining a mailing list '='
     *              recipients 0, 1, >1
     *              lists 0, 1, >1
     *              combination of lists and individual recipients
     *              name is a valid name:
     *                  letters only
     *                  numbers only
     *                  dashes, underscores, periods
     *                  combination of all of the above
     *          e-mails contains:
     *              letters only
     *              numbers only
     *              dashes, underscores, periods
     *              combination of all of the above
     *          case insensitivity
     *      Invalid Expressions:
     *          no username
     *          no domain
     *          operation with undefined mailing list
     *           
     */
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    //length of list = 0
    @Test
    public void testFrontendLengthZero() {
        String input = "";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs","[]",output.toString());
    }
    //length of list = 1, ',' 0 times
    @Test
    public void testFrontendLengthOne() {
        String input = "hello@mit.edu";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs","[hello@mit.edu]",output.toString());
    }
    //',' 1 time
    @Test
    public void testFrontendOneUnion() {
        String input = "hello@mit, goodbye@mit";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs","[goodbye@mit, hello@mit]",output.toString());
    }
    //length of list > 1, ',' > 1 times
    @Test
    public void testFrontendLengthGreaterThanOne() {
        String input = "hello@mit.edu, goodbye@gmail.com, ewtests@yahoo.com";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs","[ewtests@yahoo.com, goodbye@gmail.com, hello@mit.edu]",output.toString());
    }
    
    //emails contain only letters
    @Test
    public void testFrontendLettersOnly() {
        String input = "a@mit.edu,moo@gmail.com, asdfg@mit.edu";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs","[a@mit.edu, asdfg@mit.edu, moo@gmail.com]",output.toString());
    }
    //emails contain only numbers
    @Test
    public void testFrontendNumbersOnly() {
        String input = "123@mit.edu, 987654321@gmail.com,121212@mit.edu";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs","[121212@mit.edu, 123@mit.edu, 987654321@gmail.com]",output.toString());
        
    }
    //emails contain only dashes
    @Test
    public void testFrontendDashesOnly() {
        String input = "---@mit.edu, -----@gmail.com, -@gmail.com";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs","[-----@gmail.com, ---@mit.edu, -@gmail.com]",output.toString());
    }
    //emails contain only underscores
    @Test
    public void testFrontendUnderscoresOnly() {
        String input = "___@mit.edu,_____@gmail.com,_@gmail.com";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs","[_@gmail.com, ___@mit.edu, _____@gmail.com]",output.toString());
    }
    //emails contain periods
    @Test
    public void testFrontendPeriodsOnly() {
        String input = "...@mit,.@gmail.com,.....@gmail.com.io";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs","[.....@gmail.com.io, ...@mit, .@gmail.com]",output.toString());
    }
    //emails contain combination of letters, digits, underscores, dashes, and periods
    @Test
    public void testFrontendLettersDigitsUnderscoresDashesPeriods() {
        String input = ".asd..@mit.edu,45345-345@gmail.com, asb_123@gmail.com";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs","[.asd..@mit.edu, 45345-345@gmail.com, asb_123@gmail.com]",output.toString());
    }
    
    //case insensitivity
    @Test
    public void testFrontendCaseInsensitivity() {
        String input = "a@mit.edu, B@mit.edu, asdf@mit.edu, b@mit.edu, AsdF@mit.edu";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs","[a@mit.edu, asdf@mit.edu, b@mit.edu]",output.toString());
    }
    
    //defining empty mailing list
    @Test
    public void testDefineMailingListEmpty() {
        String input = "x = ";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs","[]",output.toString());
    }
    //defining mailing list with 1 recipient
    @Test
    public void testDefineMailingListOne() {
        String input = "tests = gross@mit.edu";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs","[gross@mit.edu]",output.toString());
    }
    //defining mailing list with > 1 recipients
    @Test
    public void testDefineMailingListMoreRecipients() {
        String input = "what = happy@mit, sad@edu, IWantSleep@now";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs","[happy@mit, iwantsleep@now, sad@edu]",output.toString());
    }
    //defining mailing list with 1 list
    @Test
    public void testDefineMailingListOneListRecipient() {
        String input = "a = a@sd.sdf, b@jklol ; b = a";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs","[a@sd.sdf, b@jklol]",output.toString());
    }
    //defining mailing list with > 1 lists
    @Test
    public void testDefineMailingListMultipleListRecipients() {
        String input = "a = a@sdf, c@sdd_sd.s ; b = b@12.D ; c = a,b";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs","[a@sdf, b@12.d, c@sdd_sd.s]",output.toString());
    }
    //defining mailing list to be combination of single recipients and lists
    @Test
    public void testDefineMailingListCombination() {
        String input = "a234 = a@sdf, c@sdd_sd.s ; moo = a234, 123@derp";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs","[123@derp, a@sdf, c@sdd_sd.s]",output.toString());
    }

    //length of '!' operation = 1, length of '*' = 0
    @Test
    public void testDifferenceLengthOne() {
        String input = "12asf@mit, moo@mit ! (woof@mit, meow@mit, moo@mit)";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs","[12asf@mit]",output.toString());
    }
    //length of '!' operation > 1, length of ';' = 0
    @Test
    public void testDifferenceLengthGreaterThanOne() {
        String input = "corgi@mit, golden@mit.edu ! golden@mit, cats@mit ! cats@mit! Im@bored";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs","[corgi@mit, golden@mit.edu]",output.toString());
    }
   
    //length of '*' operation = 1
    @Test
    public void testIntersectionLengthOne() {
        String input = "a@mit, b@mit * MoMa@nyc, b@mit";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs","[a@mit, b@mit]",output.toString());
    }
    //length of '*' operation > 1
    @Test
    public void testIntersectionLengthGreaterThanOne() {
        String input = "(seoul@korea.com, incheon@seoul) "
                + "* (seoul@korea.com, HK@boba) "
                + "* (hk@boba, seoul@korea.com)";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs","[seoul@korea.com]",output.toString());
    }
    
    //length of ';' operation = 1 
    @Test
    public void testSequenceLengthOne() {
        String input = "a = b@sf.sd, 123@dsf.s ; b = --_--@123, a";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs", "[--_--@123, 123@dsf.s, b@sf.sd]", output.toString());
    }
    //length of ';' operation > 1
    @Test
    public void testSequenceLengthGreaterThanOne() {
        String input = "12sd = -sd@sd.sdf, a@jk ; b = 12@lol, -_sd.@ds, a@jk ; c = 12sd * b ";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs", "[a@jk]", output.toString());
    }
    
    //combination of ',', ';', '!', '*' operations
    @Test
    public void testOperationsCombination() {
        String input = "-._ = 0101_@abc ; (a@a, b@b) * (b@b ! c@c) ; lol = hi@bye";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs", "[hi@bye]", output.toString());
    }
    //various combinations of operations with parentheses
    //Difference and Difference
    @Test
    public void testOperationsWithParenDiffAndDiff() {
        String input = "((ast@123,boo@o.o.s)!(boo@o.o.s, apple@banana)),(a@ba!meow@moo,dog@cat)";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs", "[a@ba, ast@123, dog@cat]", output.toString());
    }
    
    //Intersection and Intersection
    @Test
    public void testOperationsWithParenIntersectAndIntersect() {
        String input = "((ab2@cd, asdf@g)*(asdf@g,soo@oo)),((MoO@cow, grass@GreEn)*moo@Cow)";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs", "[asdf@g, moo@cow]", output.toString());
    }
    
    //Intersection of Differences
    @Test
    public void testOperationsWithParenThree() {
        String input = "(--.--@ugh!why@asfd,..@-_-, -_-_-@..sd)*((-_-_-@..sd,bow@wow)!(bow@wow, sdf@u.s), ..@-_-)";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs", "[-_-_-@..sd, ..@-_-]", output.toString());
    }
    
    //Differences of intersection of mailing lists
    @Test
    public void testLongSequence() {
        String input = "m_1 = 1-2-3@abc, 3-2-1@cba; m-2 = 1_2_3@bcd, 3-2-1@cba; m3 = m_1 *m-2; m_4 = a@b;"
                + "m_5 = b@b, A@B; m3 ! m_5";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs", "[3-2-1@cba]", output.toString());
    }
    
    //Super-nested operations
    @Test
    public void testOperationsWithParenFive() {
        String input = "(((a@b,12@_0_,12.3@-0-)*(12.3@-0-, a@B)) ! (monke@y,meow@woof)) "
                + "* (12.3@-0-, a@b, monke@y), happy@maybe";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs", "[12.3@-0-, a@b, happy@maybe]", output.toString());
    }
    
    //output contains emails that begin with all characters (-_.[0-9][a-z])
    //odering: -@.(0-9)_(a-z)
    @Test
    public void testAllCharactersOrder() {
        String input = "a-B@s.2, 99_bCd@o, -__-asw@-_-., ..s_-aSoo@___.23, _23_ab@jk, _abs@-_-,"
                + "..-_@zGs, Zab@dab, Z1z@ugh";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs", "[-__-asw@-_-., ..-_@zgs, ..s_-asoo@___.23, 99_bcd@o, _23_ab@jk, "
                + "_abs@-_-, a-b@s.2, z1z@ugh, zab@dab]", output.toString());
    }
    //sequence containing all operations
    @Test
    public void testSequenceWithAllOperations() {
        String input = "zoo = monkey@banana, cow@grass, cat@meow, dog@woof; real_zoo = zoo ! (cat@meow, dog@woof);"
                + "real_zoo *  monkey@banana, snake@snek";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs", "[monkey@banana, snake@snek]", output.toString());
    }
    //Long Sequence a
    @Test
    public void testLongSequenceA() {
        String input = "animals = cow@grass, monkey@banana, dog@woof, cat@meow, snake@ssss; "
                + "zoo = cow@grass, monkey@banana; pet = dog@woof, , cat@meow; "
                + "1 = animals ! pet; 2 = animals * zoo; 1, 2";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
        assertEquals("wrong outputs", "[cow@grass, monkey@banana, snake@ssss]", output.toString());
    }
    
    //Invalid cases --> are the correct errors being thrown?
    //invalid - no username
    @Test(expected=IllegalArgumentException.class)
    public void testIllegalNoUsername() {
        String input = "@mit.edu, @yahoo.com";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
    }
    //invalid - no domain
    @Test(expected=IllegalArgumentException.class)
    public void testIllegalNoDomain() {
        String input = "a@m, B@, asdf";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
    }
    //Invalid - nested sequences
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidNestedSequence() {
        String input = "(a=b@b;b=a@a),(a=b;a * b)";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
    }
    //Invalid - nested definitions
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidNestedDefinition() {
        String input = "(a=b@b), (b=asc@dfs)";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
    }
    //operations with an undefined mailing list
    @Test(expected=AssertionError.class)
    public void testInvalidMailingListNotDefined() {
        String input = "a=b";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
    }
    //valid sequence contains undefined mailing list
    @Test(expected=AssertionError.class)
    public void testInvalidSequenceWithUndefinedList() {
        String input = "a=a@b, c@d ; a=b";
        DefinedMailingLists definedMailingLists = new DefinedMailingLists();
        Set<String> output = norn.Main.parseEmailRecipients(input, definedMailingLists);
    }
}
