package norn;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class ListExpressionTest {
    
    public static String ANGE = "ange__123@mit.edu";
    public static ListExpression ANGEEMAIL = ListExpression.makeEmail(ANGE);
    
    public static String NANCE = "lnanCYr@mit.edu";
    public static ListExpression NANCEEMAIL = ListExpression.makeEmail(NANCE);
    
    public static String YD = "yida-wang@mit.edu";
    public static ListExpression YDEMAIL = ListExpression.makeEmail(YD);
    
    public static ListExpression RANDO = ListExpression.makeEmail("rAndO484978___3.----@mifdafd----t.edu");
    
    public static ListExpression APARTMENT = ListExpression.makeUnion(ANGEEMAIL, NANCEEMAIL);
    public static ListExpression FRIENDS = ListExpression.makeUnion(YDEMAIL, APARTMENT);
    
    public static ListExpression SEQ = ListExpression.makeSequence(YDEMAIL, APARTMENT);
    
    public static ListExpression DIFF = ListExpression.makeDifference(FRIENDS, RANDO);
    public static ListExpression DIFF_EMPTY = ListExpression.makeDifference(FRIENDS, ListExpression.makeUnion(APARTMENT, YDEMAIL));
    
    public static ListExpression INT_EMPTY = ListExpression.makeIntersection(APARTMENT, RANDO);
    public static ListExpression INT = ListExpression.makeIntersection(APARTMENT, ANGEEMAIL);
    
    public static ListExpression BILBO = ListExpression.makeEmail("bilbo@shire");
    public static ListExpression FRODO = ListExpression.makeEmail("frodo@shire");
    public static ListExpression SAM = ListExpression.makeEmail("sam@shire");
    public static ListExpression MERRY = ListExpression.makeEmail("merry@shire");
    public static ListExpression PIPPIN = ListExpression.makeEmail("pippin@shire");

    public static ListExpression COUSINS = ListExpression.makeUnion(MERRY,PIPPIN);
    public static ListExpression LOVE = ListExpression.makeUnion(FRODO, SAM);
    public static ListExpression RELATED = ListExpression.makeUnion(FRODO, BILBO);
    public static ListExpression FELLOWSHIP_HOBBITS = ListExpression.makeUnion(COUSINS, LOVE);
    public static ListExpression HOBBITS_UNION = ListExpression.makeUnion(FELLOWSHIP_HOBBITS, RELATED);
    public static ListExpression HOBBITS = ListExpression.makeMailingList("hobbits", HOBBITS_UNION);
    
    public static Map<String, ListExpression> MAILING_LISTS = new HashMap<>();
    
    
    /**
     * Testing Strategy for ListExpression and variants:  
     * ====================================================================================
     * Empty:
     *      getEmailRecipients: test is empty
     *      toString: empty string
     *      hashCode: 0
     *      equals: another empty only
     *      
     * Email:
     *      getEmailRecipients: email address is correctly defined (mix in upper and lower case, numbers, dashes, underscores, periods)
     *      toString: string of email address
     *      hashCode: hashCode of string
     *      equals: equals email with same definition
     * 
     * Sequence:
     *      getEmailRecipients: empty to empty, email, sequence, union, intersection, difference, mailinglist
     *              email to email, sequence, union, intersection, difference, mailinglist
     *              sequence to sequence, union, intersection, difference, mailinglist
     *              union to union, intersection, difference, mailinglist
     *              intersection to intersection, difference, mailinglist
     *              difference to difference, mailinglist
     *              mailinglist to mailinglist
     *              - test for repeats periodically
     *              - mix in lower and upper case, numbers, dashes, underscores, periods
     *      toString: human-readable output (use mix of empty, email, union, difference, intersection) 
     *                also is equal to toString of equal union
     *      hashCode: is the same for equal sequences (use mix of empty, email, union, difference, intersection) 
     *      equals: make sure first and second are the same (use mix of empty, email, union, difference, intersection)
     *
     * Union:
     *      getEmailRecipients: empty to empty, email, sequence, union, intersection, difference, mailinglist
     *              email to email, sequence, union, intersection, difference, mailinglist
     *              sequence to sequence, union, intersection, difference, mailinglist
     *              union to union, intersection, difference, mailinglist
     *              intersection to intersection, difference, mailinglist
     *              difference to difference, mailinglist
     *              mailinglist to mailinglist
     *              - test for repeats periodically
     *              - mix in lower and upper case, numbers, dashes, underscores, periods
     *      toString: human-readable output (use mix of empty, email, union, difference, intersection) 
     *                also is equal to toString of equal union
     *      hashCode: is the same for equal unions (use mix of empty, email, union, difference, intersection) 
     *      equals: make sure first and second are the same (use mix of empty, email, union, difference, intersection) 
     * 
     * 
     * Intersection: 
     *      getEmailRecipients: empty to empty, email, sequence, union, intersection, difference, mailinglist 
     *              email to email, sequence, union, intersection, difference, mailinglist
     *              sequence to sequence, union, intersection, difference, mailinglist
     *              union to union, intersection, difference, mailinglist
     *              intersection to intersection, difference, mailinglist
     *              difference to difference, mailinglist
     *              mailinglist to mailinglist
     *              - test for repeats periodically
     *              - mix in lower and upper case, numbers, dashes, underscores, periods
     *      toString: human-readable output (use mix of empty, email, union, difference, intersection) 
     *                also is equal to toString of equal union
     *      hashCode: is the same for equal intersection (use mix of empty, email, union, difference, intersection) 
     *      equals: make sure first and second are the same (use mix of empty, email, union, difference, intersection)
     *      
     * Difference:
     *      getEmailRecipients: empty to empty, email, sequence, union, intersection, difference, mailinglist
     *              email to email, sequence, union, intersection, difference, mailinglist
     *              sequence to sequence, union, intersection, difference, mailinglist
     *              union to union, intersection, difference, mailinglist
     *              intersection to intersection, difference, mailinglist
     *              difference to difference, mailinglist
     *              mailinglist to mailinglist
     *              - test for repeats periodically
     *              - mix in lower and upper case, numbers, dashes, underscores, periods
     *      toString: human-readable output (use mix of empty, email, union, difference, intersection) 
     *                also is equal to toString of equal union
     *      hashCode: is the same for equal difference (use mix of empty, email, union, difference, intersection) 
     *      equals: make sure first and second are the same (use mix of empty, email, union, difference, intersection)
     * 
     * MailingList: 
     *      getEmailRecipients: test that takes in empty, email, sequence, union, intersection, difference
     *              - test for repeats periodically
     *              - mix in lower and upper case, numbers, dashes, underscores, periods
     *      toString: human-readable output (use mix of empty, email, union, difference, intersection) 
     *                also is equal to toString of equal union
     *      hashCode: is the same for equal difference (use mix of empty, email, union, difference, intersection) 
     *      equals: make sure first and second are the same (use mix of empty, email, union, difference, intersection)
     * 
     * DefinedMailingLists:
     *      Creators: nothing and a map<string, listexpression>
     *      getMailingLists: should return a mailing list
     *      addMailingList: add mailinglists to this mailinglist
     * 
     */
    
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    // empty ////////////////////////////////////////////////////////////////////////////////////
    
    // empty.getEmailRecipients()
    @Test
    public void testEmptyDefine() {
        ListExpression empty = ListExpression.makeEmpty();
        assertEquals("definition is empty", new HashSet<>(), empty.getEmailRecipients());
    }
    
    // empty.toString()
    @Test
    public void testEmptyToString() {
        ListExpression empty = ListExpression.makeEmpty();
        assertEquals("", empty.toString());
    }
    
    // empty.hashCode()
    @Test
    public void testEmptyHashCode() {
        ListExpression empty = ListExpression.makeEmpty();
        assertEquals(ListExpression.makeEmpty().hashCode(), empty.hashCode());
    }
    
    // empty.equals()
    @Test
    public void testEmptyEquals() {
        ListExpression empty = ListExpression.makeEmpty();
        assertTrue(empty.equals(ListExpression.makeEmpty()));
        assertTrue(! empty.equals(ANGEEMAIL));
        assertTrue(! empty.equals(APARTMENT));
    }
    
    // email ////////////////////////////////////////////////////////////////////////////////////
    
    // email.getEmailRecipients()
    @Test
    public void testEmailDefine() {
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ListExpression.makeEmail(ANGE));
        ListExpression angEmail = ListExpression.makeEmail(ANGE.toUpperCase());
        assertEquals(definition, angEmail.getEmailRecipients());
    }
    
    // email.toString()
    @Test
    public void testEmailToString() {
        assertEquals(ANGE, ANGEEMAIL.toString());
    }
    
    // email.hashCode()
    @Test
    public void testEmailHashCode() {
        assertEquals(ListExpression.makeEmail(ANGE).hashCode(), ANGEEMAIL.hashCode());
    }
    
    // email.equals()
    @Test
    public void testEmailEquals() {
        assertTrue(ANGEEMAIL.equals(ListExpression.makeEmail(ANGE)));
        assertTrue(! ANGEEMAIL.equals(NANCEEMAIL));
        assertTrue(!ANGEEMAIL.equals(APARTMENT));
    }
    
    // Sequence ////////////////////////////////////////////////////////////////////////////////////
    
    // Sequence.getEmailRecipients()  with first, second = empty, empty
    @Test
    public void testSequenceEmptyEmpty() {
        ListExpression seq = ListExpression.makeSequence(ListExpression.makeEmpty(), ListExpression.makeEmpty());
        assertEquals(new HashSet<>(), seq.getEmailRecipients());
    }
    
    // Sequence.getEmailRecipients()  with first, second = empty, email
    @Test
    public void testSequenceEmptyEmail() {
        ListExpression seq = ListExpression.makeSequence(ListExpression.makeEmpty(), NANCEEMAIL);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(NANCEEMAIL);
        assertEquals(definition, seq.getEmailRecipients());
    }
    
    // Sequence.getEmailRecipients()  with first, second = empty, sequence
    @Test
    public void testSequenceEmptySequence() {
        ListExpression seq = ListExpression.makeSequence(ListExpression.makeEmpty(), SEQ);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(NANCEEMAIL);
        definition.add(ANGEEMAIL);
        assertEquals(definition, seq.getEmailRecipients());
    }
   
    // Sequence.getEmailRecipients()  with first, second = empty, union
    @Test
    public void testSequenceEmptyUnion() {
        ListExpression seq = ListExpression.makeSequence(ListExpression.makeEmpty(), APARTMENT);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        definition.add(NANCEEMAIL);
        assertEquals(definition, seq.getEmailRecipients());
    }
    
    // union.getEmailRecipients()  with first, second = empty, difference
    @Test
    public void testSequenceEmptyDifference() {
        ListExpression seq = ListExpression.makeSequence(ListExpression.makeEmpty(), DIFF);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(NANCEEMAIL);
        definition.add(ANGEEMAIL);
        definition.add(YDEMAIL);
        assertEquals(definition, seq.getEmailRecipients());
    }
    
    // Sequence.getEmailRecipients()  with first, second = empty, intersection
    @Test
    public void testSequenceEmptyIntersection() {
        ListExpression seq = ListExpression.makeSequence(ListExpression.makeEmpty(), INT);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        assertEquals(definition, seq.getEmailRecipients());
    }
    
    // Sequence.getEmailRecipients()  with first, second = empty, mailingList
    @Test
    public void testSequenceEmptyMailingList() {
        ListExpression seq = ListExpression.makeSequence(ListExpression.makeEmpty(), HOBBITS);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(FRODO);
        definition.add(BILBO);
        definition.add(SAM);
        definition.add(MERRY);
        definition.add(PIPPIN);
        assertEquals(definition, seq.getEmailRecipients());
    }
    
    // Sequence.getEmailRecipients()  with first, second = email, email
    @Test
    public void testSequenceEmailEmail() {
        ListExpression seq = ListExpression.makeSequence(NANCEEMAIL,ListExpression.makeEmail("YIDA-WANG@MIT.EDU"));
        Set<ListExpression> definition = new HashSet<>();
        definition.add(YDEMAIL);
        assertEquals(definition, seq.getEmailRecipients());
    }
    
    // Sequence.getEmailRecipients() with first, second = email, sequence
    @Test
    public void testSequenceEmailSequence() {
        ListExpression seq = ListExpression.makeSequence(ANGEEMAIL, SEQ);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(NANCEEMAIL);
        definition.add(ANGEEMAIL);
        assertEquals(definition, seq.getEmailRecipients());
    }
    
    // Sequence.getEmailRecipients()  with first, second = email, union
    @Test
    public void testSequenceEmailUnion() {
        ListExpression seq = ListExpression.makeSequence(APARTMENT, NANCEEMAIL);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(NANCEEMAIL);
        assertEquals(definition, seq.getEmailRecipients());
    }
    
    // Sequence.getEmailRecipients()  with first, second = email, difference
    @Test
    public void testSequenceEmailDifference() {
        ListExpression seq = ListExpression.makeSequence(DIFF_EMPTY, NANCEEMAIL);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(NANCEEMAIL);
        assertEquals(definition, seq.getEmailRecipients());
    }
    
    // Sequence.getEmailRecipients()  with first, second = email, intersection
    @Test
    public void testSequenceEmailIntersection() {
        ListExpression seq = ListExpression.makeSequence(ANGEEMAIL, INT);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        assertEquals(definition, seq.getEmailRecipients());
    }
    
    // Sequence.getEmailRecipients()  with first, second = email, mailingList
    @Test
    public void testSequenceEmailMailingList() {
        ListExpression seq = ListExpression.makeSequence(ANGEEMAIL, HOBBITS);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(FRODO);
        definition.add(BILBO);
        definition.add(SAM);
        definition.add(MERRY);
        definition.add(PIPPIN);
        assertEquals(definition, seq.getEmailRecipients());
    }
    
    // Sequence.getEmailRecipients()  with first, second = sequence, sequence
    @Test
    public void testSequenceSequenceSequence() {
        ListExpression seq = ListExpression.makeSequence(SEQ, SEQ);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        definition.add(NANCEEMAIL);
        assertEquals(definition, seq.getEmailRecipients());
    }
    
    // Sequence.getEmailRecipients()  with first, second = sequence, union
    @Test
    public void testSequenceSequenceUnion() {
        ListExpression seq = ListExpression.makeSequence(SEQ, FRIENDS);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        definition.add(NANCEEMAIL);
        definition.add(YDEMAIL);
        assertEquals(definition, seq.getEmailRecipients());
    }
    
    // Sequence.getEmailRecipients()  with first, second = sequence, intersection
    @Test
    public void testSequenceSequenceIntersection() {
        ListExpression seq = ListExpression.makeSequence(SEQ, INT);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        assertEquals(definition, seq.getEmailRecipients());
    }
    
    // Sequence.getEmailRecipients()  with first, second = sequence, difference
    @Test
    public void testSequenceSequenceDifference() {
        ListExpression seq = ListExpression.makeSequence(SEQ, DIFF_EMPTY);
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, seq.getEmailRecipients());
    }
    
    // Sequence.getEmailRecipients()  with first, second = sequence, mailingList
    @Test
    public void testSequenceSequenceMailingList() {
        ListExpression seq = ListExpression.makeSequence(SEQ, HOBBITS);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(FRODO);
        definition.add(BILBO);
        definition.add(SAM);
        definition.add(MERRY);
        definition.add(PIPPIN);
        assertEquals(definition, seq.getEmailRecipients());
    }
    
    // Sequence.getEmailRecipients()  with first, second = union, union
    @Test
    public void testSequenceUnionUnion() {
        ListExpression two = ListExpression.makeUnion(YDEMAIL, RANDO);
        ListExpression seq = ListExpression.makeSequence(APARTMENT, two);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(YDEMAIL);
        definition.add(RANDO);
        assertEquals(definition, seq.getEmailRecipients());
    }
   
    // Sequence.getEmailRecipients()  with first, second = union, difference
    @Test
    public void testSequenceUnionDifference() {
        ListExpression two = ListExpression.makeSequence(YDEMAIL, RANDO);
        ListExpression seq = ListExpression.makeSequence(DIFF, two);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(RANDO);
        assertEquals(definition, seq.getEmailRecipients());
    }
    
    // Sequence.getEmailRecipients()  with first, second = union, intersection
    @Test
    public void testSequenceUnionIntersection() {
        ListExpression two = ListExpression.makeSequence(YDEMAIL, RANDO);
        ListExpression seq = ListExpression.makeSequence(INT, two);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(RANDO);
        assertEquals(definition, seq.getEmailRecipients());
    }
    
    // Sequence.getEmailRecipients()  with first, second = union, mailingList
    @Test
    public void testSequenceUnionMailingList() {
        ListExpression seq = ListExpression.makeSequence(APARTMENT, HOBBITS);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(FRODO);
        definition.add(BILBO);
        definition.add(SAM);
        definition.add(MERRY);
        definition.add(PIPPIN);
        assertEquals(definition, seq.getEmailRecipients());
    }
    
    // Sequence.getEmailRecipients()  with first, second = difference, difference
    @Test
    public void testSequenceDifferenceDifference() {
        ListExpression seq = ListExpression.makeSequence(DIFF, DIFF_EMPTY);
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, seq.getEmailRecipients());
    }
   
    // Sequence.getEmailRecipients()  with first, second = difference, intersection
    @Test
    public void testSequenceDifferenceIntersection() {
        ListExpression seq = ListExpression.makeSequence(DIFF, INT);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        assertEquals(definition, seq.getEmailRecipients());
    }
    
    // Sequence.getEmailRecipients()  with first, second = difference, mailingList
    @Test
    public void testSequenceDifferenceMailingList() {
        ListExpression seq = ListExpression.makeSequence(DIFF, HOBBITS);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(FRODO);
        definition.add(BILBO);
        definition.add(SAM);
        definition.add(MERRY);
        definition.add(PIPPIN);
        assertEquals(definition, seq.getEmailRecipients());
    }
    
    // Sequence.getEmailRecipients()  with first, second = intersection, intersection
    @Test
    public void testSequenceIntersectionIntersection() {
        ListExpression seq = ListExpression.makeSequence(INT_EMPTY, INT);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        assertEquals(definition, seq.getEmailRecipients());
    }
    
    // Sequence.getEmailRecipients()  with first, second = intersection, mailingList
    @Test
    public void testSequenceIntersectionMailingList() {
        ListExpression seq = ListExpression.makeSequence(INT, HOBBITS);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(FRODO);
        definition.add(BILBO);
        definition.add(SAM);
        definition.add(MERRY);
        definition.add(PIPPIN);
        assertEquals(definition, seq.getEmailRecipients());
    }
    
    // Sequence.getEmailRecipients()  with first, second = mailingList, mailingList
    @Test
    public void testSequenceMailingListMailingList() {
        ListExpression seq = ListExpression.makeSequence(HOBBITS, ListExpression.makeMailingList("empty", ListExpression.makeEmpty()));
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, seq.getEmailRecipients());
    }
    
    // Sequence.toString() with first, second = empty, empty
    @Test
    public void testSequenceToStringEmpty() {
        ListExpression seq = ListExpression.makeSequence(ListExpression.makeEmpty(), ListExpression.makeEmpty());
        assertEquals("", seq.toString());
    }
    
    // Sequence.toString() with first, second = email, union
    @Test
    public void testSequenceToString() {
        ListExpression seq = ListExpression.makeSequence(FRIENDS, NANCEEMAIL);
        assertEquals(NANCE.toLowerCase(), seq.toString());
        assertEquals(ListExpression.makeSequence(FRIENDS, NANCEEMAIL).toString(), seq.toString());
    }
    
    // Sequence.toString() with first, second = diff, inter
    @Test
    public void testSequenceToStringDifferenceIntersection() {
        ListExpression seq = ListExpression.makeSequence(DIFF, INT_EMPTY);
        assertEquals("", seq.toString());
    }
    
    // Sequence.toString() with first, second = sequence, mailing list
    @Test
    public void testSequenceToStringSequenceMail() {
        ListExpression seq = ListExpression.makeSequence(SEQ, HOBBITS);
        assertEquals(ListExpression.makeSequence(SEQ, HOBBITS).toString(), seq.toString());
        assertTrue(!ListExpression.makeSequence(HOBBITS, SEQ).toString().equals(seq.toString()));
    }
    
    // Sequence.toString() with first, second = email, diff
    @Test
    public void testSequenceToStringEmailDifference() {
        ListExpression seq = ListExpression.makeSequence(ANGEEMAIL, DIFF);
        assertEquals(DIFF.toString(), seq.toString());
        assertTrue(!ListExpression.makeSequence(DIFF, ANGEEMAIL).toString().equals(seq.toString()));
    }
    
    // Sequence.toString() with first, second = inter, email
    @Test
    public void testSequenceToStringIntersectionEmail() {
        ListExpression seq = ListExpression.makeSequence(INT_EMPTY, RANDO);
        assertEquals(RANDO.toString(), seq.toString());
        assertTrue( ! ListExpression.makeSequence(RANDO, INT_EMPTY).toString().equals(seq.toString()));
    }
    
    // Sequence.hashCode() with first, second = empty, empty
    @Test
    public void testSequenceHashCodeEmpty() {
        ListExpression seq = ListExpression.makeSequence(ListExpression.makeEmpty(), ListExpression.makeEmpty());
        assertEquals(ListExpression.makeSequence(ListExpression.makeEmpty(), ListExpression.makeEmpty()).hashCode(), seq.hashCode());
    }
    
    // Sequence.hashCode() with first, second = union, email
    @Test
    public void testSequenceHashCodeUnionEmail() {
        ListExpression seq = ListExpression.makeSequence(FRIENDS, NANCEEMAIL);
        assertEquals(ListExpression.makeSequence(FRIENDS, NANCEEMAIL).hashCode(), seq.hashCode());
    }
    
    // Sequence.hashCode() with first, second = diff, seq
    @Test
    public void testSequenceHashCodeDifferenceSequence() {
        ListExpression seq = ListExpression.makeSequence(DIFF_EMPTY, SEQ);
        assertEquals(ListExpression.makeSequence(DIFF_EMPTY, SEQ).hashCode(), seq.hashCode());
    }
    
    // Sequence.hashCode() with first, second = mailinglist, empty
    @Test
    public void testSequenceHashCodeMailingEmpty() {
        ListExpression seq = ListExpression.makeSequence(HOBBITS, ListExpression.makeEmpty());
        assertEquals(ListExpression.makeSequence(ListExpression.makeEmpty(), HOBBITS).hashCode(), seq.hashCode());
    }
    
    // Sequence.hashCode() with first, second = int, union
    @Test
    public void testSequenceHashCodeIntersectionUnion() {
        ListExpression seq = ListExpression.makeSequence(INT, FRIENDS);
        assertEquals(ListExpression.makeSequence(FRIENDS, INT).hashCode(), seq.hashCode());
    }
    
    // Sequence.hashCode() with first, second = email, mailing list
    @Test
    public void testSequenceHashCodeEmailMailingList() {
        ListExpression seq = ListExpression.makeSequence(ANGEEMAIL, HOBBITS);
        assertEquals(ListExpression.makeSequence(HOBBITS, ANGEEMAIL).hashCode(), seq.hashCode());
    }
    
    // Sequence.equals() with first, second = empty, empty
    @Test
    public void testSequenceEqualsEmpty() {
        ListExpression empty = ListExpression.makeSequence(ListExpression.makeEmpty(), ListExpression.makeEmpty());
        assertTrue(empty.equals(ListExpression.makeSequence(ListExpression.makeEmpty(), ListExpression.makeEmpty())));
        assertTrue(! empty.equals(FRIENDS));
        assertTrue(! empty.equals(ANGE));
    }
    
    // Sequence.equals() with first, second = email, union
    @Test
    public void testSequenceEqualsEmailUnion() {
        ListExpression set = ListExpression.makeSequence(YDEMAIL, APARTMENT);
        ListExpression alsoSet = ListExpression.makeSequence(YDEMAIL, APARTMENT);
        assertTrue(set.equals(alsoSet));
        assertTrue(! set.equals(NANCEEMAIL));
        assertTrue(! set.equals(ListExpression.makeEmpty()));
    }
    
    // Sequence.equals() with first, second = mailing list, empty
    @Test
    public void testSequenceEqualsMailingListEmpty() {
        ListExpression set = ListExpression.makeSequence(HOBBITS, ListExpression.makeEmpty());
        ListExpression alsoSet = ListExpression.makeSequence(HOBBITS, ListExpression.makeEmpty());
        ListExpression flipped = ListExpression.makeSequence(ListExpression.makeEmpty(), HOBBITS);
        assertTrue(set.equals(alsoSet));
        assertTrue(! set.equals(flipped));
        assertTrue(! set.equals(ListExpression.makeEmpty()));
    }
    
    // Sequence.equals() with first, second = seq, diff
    @Test
    public void testSequenceEqualsSequenceDifference() {
        ListExpression set = ListExpression.makeSequence(SEQ, DIFF_EMPTY);
        ListExpression alsoSet = ListExpression.makeSequence(SEQ, DIFF_EMPTY);
        ListExpression flipped = ListExpression.makeSequence(DIFF_EMPTY, SEQ);
        assertTrue(set.equals(alsoSet));
        assertTrue(! set.equals(flipped));
        assertTrue(! set.equals(NANCEEMAIL));
        assertTrue(! set.equals(ListExpression.makeEmpty()));
    }
    
    // Sequence.equals() with first, second = email, intersection
    @Test
    public void testSequenceEqualsEmailIntersection() {
        ListExpression set = ListExpression.makeSequence(YDEMAIL, INT);
        ListExpression alsoSet = ListExpression.makeSequence(YDEMAIL, INT);
        ListExpression flipped = ListExpression.makeSequence(INT, YDEMAIL);
        assertTrue(set.equals(alsoSet));
        assertTrue(! set.equals(flipped));
        assertTrue(! set.equals(NANCEEMAIL));
        assertTrue(! set.equals(ListExpression.makeEmpty()));
    }
    
    // Sequence.equals() with first, second = difference, email
    @Test
    public void testSequenceEqualsDifferenceEmail() {
        ListExpression set = ListExpression.makeSequence(DIFF_EMPTY, NANCEEMAIL);
        ListExpression alsoSet = ListExpression.makeSequence(DIFF_EMPTY, NANCEEMAIL);
        ListExpression flipped = ListExpression.makeSequence(NANCEEMAIL,DIFF_EMPTY);
        assertTrue(set.equals(alsoSet));
        assertTrue(! set.equals(flipped));
        assertTrue(! set.equals(NANCEEMAIL));
        assertTrue(! set.equals(ListExpression.makeEmpty()));
    }
    
    // union ////////////////////////////////////////////////////////////////////////////////////
    
    // union.getEmailRecipients()  with first, second = empty, empty
    @Test
    public void testUnionEmptyEmpty() {
        ListExpression union = ListExpression.makeUnion(ListExpression.makeEmpty(), ListExpression.makeEmpty());
        assertEquals(new HashSet<>(), union.getEmailRecipients());
    }
    
    // union.getEmailRecipients()  with first, second = empty, email
    @Test
    public void testUnionEmptyEmail() {
        ListExpression union = ListExpression.makeUnion(ListExpression.makeEmpty(), NANCEEMAIL);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(NANCEEMAIL);
        assertEquals(definition, union.getEmailRecipients());
    }
    
    // union.getEmailRecipients()  with first, second = empty, sequence
    @Test
    public void testUnionEmptySequence() {
        ListExpression union = ListExpression.makeUnion(ListExpression.makeEmpty(), SEQ);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(NANCEEMAIL);
        definition.add(ANGEEMAIL);
        assertEquals(definition, union.getEmailRecipients());
    }
   
    
    // union.getEmailRecipients()  with first, second = empty, union
    @Test
    public void testUnionEmptyUnion() {
        ListExpression union = ListExpression.makeUnion(ListExpression.makeEmpty(), APARTMENT);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        definition.add(NANCEEMAIL);
        assertEquals(definition, union.getEmailRecipients());
    }
    
    // union.getEmailRecipients()  with first, second = empty, difference
    @Test
    public void testUnionEmptyDifference() {
        ListExpression union = ListExpression.makeUnion(ListExpression.makeEmpty(), DIFF);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(NANCEEMAIL);
        definition.add(ANGEEMAIL);
        definition.add(YDEMAIL);
        assertEquals(definition, union.getEmailRecipients());
    }
    
    // union.getEmailRecipients()  with first, second = empty, intersection
    @Test
    public void testUnionEmptyIntersection() {
        ListExpression union = ListExpression.makeUnion(ListExpression.makeEmpty(), INT);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        assertEquals(definition, union.getEmailRecipients());
    }
    
    // union.getEmailRecipients()  with first, second = empty, mailingList
    @Test
    public void testUnionEmptyMailingList() {
        ListExpression union = ListExpression.makeUnion(ListExpression.makeEmpty(), HOBBITS);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(FRODO);
        definition.add(BILBO);
        definition.add(SAM);
        definition.add(MERRY);
        definition.add(PIPPIN);
        assertEquals(definition, union.getEmailRecipients());
    }
    
    // union.getEmailRecipients()  with first, second = email, email
    @Test
    public void testUnionEmailEmail() {
        ListExpression union = ListExpression.makeUnion(NANCEEMAIL,ListExpression.makeEmail("YIDA-WANG@MIT.EDU"));
        Set<ListExpression> definition = new HashSet<>();
        definition.add(NANCEEMAIL);
        definition.add(YDEMAIL);
        assertEquals(definition, union.getEmailRecipients());
    }
    
    // union.getEmailRecipients() with first, second = email, sequence
    @Test
    public void testUnionEmailSequence() {
        ListExpression union = ListExpression.makeUnion(ANGEEMAIL, SEQ);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(NANCEEMAIL);
        definition.add(ANGEEMAIL);
        assertEquals(definition, union.getEmailRecipients());
    }
    
    // union.getEmailRecipients()  with first, second = email, union
    @Test
    public void testUnionEmailUnion() {
        ListExpression union = ListExpression.makeUnion(APARTMENT, NANCEEMAIL);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        definition.add(NANCEEMAIL);
        assertEquals(definition, union.getEmailRecipients());
    }
    
    // union.getEmailRecipients()  with first, second = email, difference
    @Test
    public void testUnionEmailDifference() {
        ListExpression union = ListExpression.makeUnion(DIFF_EMPTY, NANCEEMAIL);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(NANCEEMAIL);
        assertEquals(definition, union.getEmailRecipients());
    }
    
    // union.getEmailRecipients()  with first, second = email, intersection
    @Test
    public void testUnionEmailIntersection() {
        ListExpression union = ListExpression.makeUnion(ANGEEMAIL, INT);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        assertEquals(definition, union.getEmailRecipients());
    }
    
    // union.getEmailRecipients()  with first, second = email, mailingList
    @Test
    public void testUnionEmailMailingList() {
        ListExpression union = ListExpression.makeUnion(ANGEEMAIL, HOBBITS);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(FRODO);
        definition.add(BILBO);
        definition.add(SAM);
        definition.add(MERRY);
        definition.add(PIPPIN);
        definition.add(ANGEEMAIL);
        assertEquals(definition, union.getEmailRecipients());
    }
    
    // union.getEmailRecipients()  with first, second = sequence, sequence
    @Test
    public void testUnionSequenceSequence() {
        ListExpression union = ListExpression.makeUnion(SEQ, SEQ);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        definition.add(NANCEEMAIL);
        assertEquals(definition, union.getEmailRecipients());
    }
    
    // union.getEmailRecipients()  with first, second = sequence, union
    @Test
    public void testUnionSequenceUnion() {
        ListExpression union = ListExpression.makeUnion(SEQ, FRIENDS);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        definition.add(NANCEEMAIL);
        definition.add(YDEMAIL);
        assertEquals(definition, union.getEmailRecipients());
    }
    
    // union.getEmailRecipients()  with first, second = sequence, intersection
    @Test
    public void testUnionSequenceIntersection() {
        ListExpression union = ListExpression.makeUnion(SEQ, INT);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        definition.add(NANCEEMAIL);
        assertEquals(definition, union.getEmailRecipients());
    }
    
    // union.getEmailRecipients()  with first, second = sequence, difference
    @Test
    public void testUnionSequenceDifference() {
        ListExpression union = ListExpression.makeUnion(SEQ, DIFF_EMPTY);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        definition.add(NANCEEMAIL);
        assertEquals(definition, union.getEmailRecipients());
    }
    
    // union.getEmailRecipients()  with first, second = sequence, mailingList
    @Test
    public void testUnionSequenceMailingList() {
        ListExpression union = ListExpression.makeUnion(SEQ, HOBBITS);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        definition.add(NANCEEMAIL);
        definition.add(FRODO);
        definition.add(BILBO);
        definition.add(SAM);
        definition.add(MERRY);
        definition.add(PIPPIN);
        assertEquals(definition, union.getEmailRecipients());
    }
    
    // union.getEmailRecipients()  with first, second = union, union
    @Test
    public void testUnionUnionUnion() {
        ListExpression two = ListExpression.makeUnion(YDEMAIL, RANDO);
        ListExpression union = ListExpression.makeUnion(APARTMENT, two);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        definition.add(NANCEEMAIL);
        definition.add(YDEMAIL);
        definition.add(RANDO);
        assertEquals(definition, union.getEmailRecipients());
    }
   
    // union.getEmailRecipients()  with first, second = union, difference
    @Test
    public void testUnionUnionDifference() {
        ListExpression two = ListExpression.makeUnion(YDEMAIL, RANDO);
        ListExpression union = ListExpression.makeUnion(DIFF, two);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        definition.add(NANCEEMAIL);
        definition.add(YDEMAIL);
        definition.add(RANDO);
        assertEquals(definition, union.getEmailRecipients());
    }
    
    // union.getEmailRecipients()  with first, second = union, intersection
    @Test
    public void testUnionUnionIntersection() {
        ListExpression two = ListExpression.makeUnion(YDEMAIL, RANDO);
        ListExpression union = ListExpression.makeUnion(INT, two);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        definition.add(RANDO);
        definition.add(YDEMAIL);
        assertEquals(definition, union.getEmailRecipients());
    }
    
    // union.getEmailRecipients()  with first, second = union, mailingList
    @Test
    public void testUnionUnionMailingList() {
        ListExpression union = ListExpression.makeUnion(APARTMENT, HOBBITS);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        definition.add(NANCEEMAIL);
        definition.add(FRODO);
        definition.add(BILBO);
        definition.add(SAM);
        definition.add(MERRY);
        definition.add(PIPPIN);
        assertEquals(definition, union.getEmailRecipients());
    }
    
    // union.getEmailRecipients()  with first, second = difference, difference
    @Test
    public void testUnionDifferenceDifference() {
        ListExpression union = ListExpression.makeUnion(DIFF, DIFF_EMPTY);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        definition.add(NANCEEMAIL);
        definition.add(YDEMAIL);
        assertEquals(definition, union.getEmailRecipients());
    }
   
    
    // union.getEmailRecipients()  with first, second = difference, intersection
    @Test
    public void testUnionDifferenceIntersection() {
        ListExpression union = ListExpression.makeUnion(DIFF, INT);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        definition.add(NANCEEMAIL);
        definition.add(YDEMAIL);
        assertEquals(definition, union.getEmailRecipients());
    }
    
    // union.getEmailRecipients()  with first, second = difference, mailingList
    @Test
    public void testUnionDifferenceMailingList() {
        ListExpression union = ListExpression.makeUnion(DIFF_EMPTY, HOBBITS);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(FRODO);
        definition.add(BILBO);
        definition.add(SAM);
        definition.add(MERRY);
        definition.add(PIPPIN);
        assertEquals(definition, union.getEmailRecipients());
    }
    
    // union.getEmailRecipients()  with first, second = intersection, intersection
    @Test
    public void testUnionIntersectionIntersection() {
        ListExpression union = ListExpression.makeUnion(INT_EMPTY, INT);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        assertEquals(definition, union.getEmailRecipients());
    }
    
    // union.getEmailRecipients()  with first, second = intersection, mailingList
    @Test
    public void testUnionIntersectionMailingList() {
        ListExpression union = ListExpression.makeUnion(INT, HOBBITS);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        definition.add(FRODO);
        definition.add(BILBO);
        definition.add(SAM);
        definition.add(MERRY);
        definition.add(PIPPIN);
        assertEquals(definition, union.getEmailRecipients());
    }
    
    // union.getEmailRecipients()  with first, second = mailing, mailingList
    @Test
    public void testUnionMailingListMailingList() {
        ListExpression union = ListExpression.makeUnion(HOBBITS, HOBBITS);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(FRODO);
        definition.add(BILBO);
        definition.add(SAM);
        definition.add(MERRY);
        definition.add(PIPPIN);
        assertEquals(definition, union.getEmailRecipients());
    }
    
    // union.toString() with first, second = empty, empty
    @Test
    public void testUnionToStringEmpty() {
        ListExpression union = ListExpression.makeUnion(ListExpression.makeEmpty(), ListExpression.makeEmpty());
        assertEquals("", union.toString());
    }
    
    // union.toString() with first, second = email, union
    @Test
    public void testUnionToString() {
        ListExpression union = ListExpression.makeUnion(FRIENDS, NANCEEMAIL);
        assertEquals(FRIENDS.toString(), union.toString());
        assertEquals(ListExpression.makeUnion(FRIENDS, NANCEEMAIL).toString(), union.toString());
    }
    
    // union.toString() with first, second = diff, inter
    @Test
    public void testUnionToStringDifferenceIntersection() {
        ListExpression union = ListExpression.makeUnion(DIFF, INT_EMPTY);
        assertEquals(DIFF.toString(), union.toString());
        assertEquals(ListExpression.makeUnion(INT_EMPTY, DIFF).toString(), union.toString());
    }
    
    // union.toString() with first, second = sequence, mailing list
    @Test
    public void testUnionToStringSequenceMail() {
        ListExpression union = ListExpression.makeUnion(SEQ, HOBBITS);
        assertEquals(ListExpression.makeUnion(HOBBITS, APARTMENT).toString(), union.toString());
        assertEquals(ListExpression.makeUnion(HOBBITS, SEQ).toString(), union.toString());
    }
    
    // union.toString() with first, second = email, diff
    @Test
    public void testUnionToStringEmailDifference() {
        ListExpression union = ListExpression.makeUnion(ANGEEMAIL, DIFF);
        assertEquals(DIFF.toString(), union.toString());
        assertEquals(ListExpression.makeUnion(DIFF, ANGEEMAIL).toString(), union.toString());
    }
    
    // union.toString() with first, second = inter, email
    @Test
    public void testUnionToStringIntersectionEmail() {
        ListExpression union = ListExpression.makeUnion(INT_EMPTY, RANDO);
        assertEquals(RANDO.toString(), union.toString());
        assertEquals(ListExpression.makeUnion(RANDO, INT_EMPTY).toString(), union.toString());
    }
    
    // union.hashCode() with first, second = empty, empty
    @Test
    public void testUnionHashCodeEmpty() {
        ListExpression union = ListExpression.makeUnion(ListExpression.makeEmpty(), ListExpression.makeEmpty());
        assertEquals(ListExpression.makeUnion(ListExpression.makeEmpty(), ListExpression.makeEmpty()).hashCode(), union.hashCode());
    }
    
    // union.hashCode() with first, second = union, email
    @Test
    public void testUnionHashCodeUnionEmail() {
        ListExpression union = ListExpression.makeUnion(FRIENDS, NANCEEMAIL);
        assertEquals(ListExpression.makeUnion(FRIENDS, NANCEEMAIL).hashCode(), union.hashCode());
    }
    
    // union.hashCode() with first, second = diff, seq
    @Test
    public void testUnionHashCodeDifferenceSequence() {
        ListExpression union = ListExpression.makeUnion(DIFF_EMPTY, SEQ);
        assertEquals(ListExpression.makeUnion(DIFF_EMPTY, SEQ).hashCode(), union.hashCode());
    }
    
    // union.hashCode() with first, second = mailinglist, empty
    @Test
    public void testUnionHashCodeMailingEmpty() {
        ListExpression union = ListExpression.makeUnion(HOBBITS, ListExpression.makeEmpty());
        assertEquals(ListExpression.makeUnion(ListExpression.makeEmpty(), HOBBITS).hashCode(), union.hashCode());
    }
    
    // union.hashCode() with first, second = int, union
    @Test
    public void testUnionHashCodeIntersectionUnion() {
        ListExpression union = ListExpression.makeUnion(INT, FRIENDS);
        assertEquals(ListExpression.makeUnion(FRIENDS, INT).hashCode(), union.hashCode());
    }
    
    // union.hashCode() with first, second = email, mailing list
    @Test
    public void testUnionHashCodeEmailMailingList() {
        ListExpression union = ListExpression.makeUnion(ANGEEMAIL, HOBBITS);
        assertEquals(ListExpression.makeUnion(HOBBITS, ANGEEMAIL).hashCode(), union.hashCode());
    }
    
    // union.equals() with first, second = empty, empty
    @Test
    public void testUnionEqualsEmpty() {
        ListExpression empty = ListExpression.makeUnion(ListExpression.makeEmpty(), ListExpression.makeEmpty());
        assertTrue(empty.equals(ListExpression.makeUnion(ListExpression.makeEmpty(), ListExpression.makeEmpty())));
        assertTrue(! empty.equals(FRIENDS));
        assertTrue(! empty.equals(ANGE));
    }
    
    // union.equals() with first, second = email, union
    @Test
    public void testUnionEqualsEmailUnion() {
        ListExpression set = ListExpression.makeUnion(YDEMAIL, APARTMENT);
        assertTrue(set.equals(FRIENDS));
        assertTrue(! set.equals(NANCEEMAIL));
        assertTrue(! set.equals(ListExpression.makeEmpty()));
    }
    
    // union.equals() with first, second = mailing list, empty
    @Test
    public void testUnionEqualsMailingListEmpty() {
        ListExpression set = ListExpression.makeUnion(HOBBITS, ListExpression.makeEmpty());
        ListExpression alsoSet = ListExpression.makeUnion(HOBBITS, ListExpression.makeEmpty());
        ListExpression flipped = ListExpression.makeUnion(ListExpression.makeEmpty(), HOBBITS);
        assertTrue(set.equals(alsoSet));
        assertTrue(! set.equals(flipped));
        assertTrue(! set.equals(ListExpression.makeEmpty()));
    }
    
    // union.equals() with first, second = seq, diff
    @Test
    public void testUnionEqualsSequenceDifference() {
        ListExpression set = ListExpression.makeUnion(SEQ, DIFF_EMPTY);
        ListExpression alsoSet = ListExpression.makeUnion(SEQ, DIFF_EMPTY);
        ListExpression flipped = ListExpression.makeUnion(DIFF_EMPTY, SEQ);
        assertTrue(set.equals(alsoSet));
        assertTrue(! set.equals(flipped));
        assertTrue(! set.equals(NANCEEMAIL));
        assertTrue(! set.equals(ListExpression.makeEmpty()));
    }
    
    // union.equals() with first, second = email, intersection
    @Test
    public void testUnionEqualsEmailIntersection() {
        ListExpression set = ListExpression.makeUnion(YDEMAIL, INT);
        ListExpression alsoSet = ListExpression.makeUnion(YDEMAIL, INT);
        ListExpression flipped = ListExpression.makeUnion(INT, YDEMAIL);
        assertTrue(set.equals(alsoSet));
        assertTrue(! set.equals(flipped));
        assertTrue(! set.equals(NANCEEMAIL));
        assertTrue(! set.equals(ListExpression.makeEmpty()));
    }
    
    // union.equals() with first, second = difference, email
    @Test
    public void testUnionEqualsDifferenceEmail() {
        ListExpression set = ListExpression.makeUnion(DIFF_EMPTY, NANCEEMAIL);
        ListExpression alsoSet = ListExpression.makeUnion(DIFF_EMPTY, NANCEEMAIL);
        ListExpression flipped = ListExpression.makeUnion(NANCEEMAIL,DIFF_EMPTY);
        assertTrue(set.equals(alsoSet));
        assertTrue(! set.equals(flipped));
        assertTrue(! set.equals(NANCEEMAIL));
        assertTrue(! set.equals(ListExpression.makeEmpty()));
    }
    
    // intersection /////////////////////////////////////////////////////////////////////////////
    
    // intersection.getEmailRecipients()  with first, second = empty, empty
    @Test
    public void testIntersectionEmptyEmpty() {
        ListExpression inter = ListExpression.makeIntersection(ListExpression.makeEmpty(), ListExpression.makeEmpty());
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, inter.getEmailRecipients());
    }
    
    // intersection.getEmailRecipients()  with first, second = empty, email
    @Test
    public void testIntersectionEmptyEmail() {
        ListExpression inter = ListExpression.makeIntersection(ListExpression.makeEmpty(), ANGEEMAIL);
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, inter.getEmailRecipients());
    }
    
    // intersection.getEmailRecipients()  with first, second = empty, sequence
    @Test
    public void testIntersectionEmptySequence() {
        ListExpression inter = ListExpression.makeIntersection(ListExpression.makeEmpty(), SEQ);
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, inter.getEmailRecipients());
    }
    
    // intersection.getEmailRecipients()  with first, second = empty, union
    @Test
    public void testIntersectionEmptyUnion() {
        ListExpression inter = ListExpression.makeIntersection(ListExpression.makeEmpty(), FRIENDS);
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, inter.getEmailRecipients());
    }
    
    // intersection.getEmailRecipients()  with first, second = empty, difference
    @Test
    public void testIntersectionEmptyDifference() {
        ListExpression inter = ListExpression.makeIntersection(ListExpression.makeEmpty(), DIFF_EMPTY);
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, inter.getEmailRecipients());
    }
    
    // intersection.getEmailRecipients()  with first, second = empty, intersection
    @Test
    public void testIntersectionEmptyIntersection() {
        ListExpression inter = ListExpression.makeIntersection(ListExpression.makeEmpty(), INT);
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, inter.getEmailRecipients());
    }
    
    // intersection.getEmailRecipients()  with first, second = empty, mailingList
    @Test
    public void testIntersectionEmptyMailingList() {
        ListExpression inter = ListExpression.makeIntersection(ListExpression.makeEmpty(), HOBBITS);
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, inter.getEmailRecipients());
    }
    
    // intersection.getEmailRecipients()  with first, second = email, email
    @Test
    public void testIntersectionEmailEmail() {
        ListExpression inter = ListExpression.makeIntersection(NANCEEMAIL, NANCEEMAIL);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(NANCEEMAIL);
        assertEquals(definition, inter.getEmailRecipients());
    }
    
    // intersection.getEmailRecipients()  with first, second = email, sequence
    @Test
    public void testIntersectionEmailSequence() {
        ListExpression inter = ListExpression.makeIntersection(NANCEEMAIL, SEQ);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(NANCEEMAIL);
        assertEquals(definition, inter.getEmailRecipients());
    }
    
    // intersection.getEmailRecipients()  with first, second = email, union
    @Test
    public void testIntersectionEmailUnion() {
        ListExpression inter = ListExpression.makeIntersection(NANCEEMAIL, FRIENDS);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(NANCEEMAIL);
        assertEquals(definition, inter.getEmailRecipients());        
    }
    
    // intersection.getEmailRecipients()  with first, second = email, difference    
    @Test
    public void testIntersectionEmailDifference() {
        ListExpression inter = ListExpression.makeIntersection(NANCEEMAIL, DIFF);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(NANCEEMAIL);
        assertEquals(definition, inter.getEmailRecipients());        
    }
    
    // intersection.getEmailRecipients()  with first, second = email, intersection
    @Test
    public void testIntersectionEmailIntersection() {
        ListExpression inter = ListExpression.makeIntersection(NANCEEMAIL, INT);
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, inter.getEmailRecipients());        
    }
    
    // intersection.getEmailRecipients()  with first, second = email, mailingList
    @Test
    public void testIntersectionEmpailMailingList() {
        ListExpression inter = ListExpression.makeIntersection(ANGEEMAIL, HOBBITS);
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, inter.getEmailRecipients());
    }
    
    // intersection.getEmailRecipients()  with first, second = sequence, sequence
    @Test
    public void testIntersectionSequenceSequence() {
        ListExpression inter = ListExpression.makeIntersection(SEQ, SEQ);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(NANCEEMAIL);
        definition.add(ANGEEMAIL);
        assertEquals(definition, inter.getEmailRecipients());        
    }
    
    // intersection.getEmailRecipients()  with first, second = sequence, union
    @Test
    public void testIntersectionSequenceUnion() {
        ListExpression inter = ListExpression.makeIntersection(SEQ, FRIENDS);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(NANCEEMAIL);
        definition.add(ANGEEMAIL);
        assertEquals(definition, inter.getEmailRecipients());        
    }
    
    // intersection.getEmailRecipients()  with first, second = sequence, intersection
    @Test
    public void testIntersectionSequenceIntersection() {
        ListExpression inter = ListExpression.makeIntersection(SEQ, INT);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        assertEquals(definition, inter.getEmailRecipients());
    }
    
    // intersection.getEmailRecipients()  with first, second = sequence, difference
    @Test
    public void testIntersectionSequenceDifference() {
        ListExpression inter = ListExpression.makeIntersection(SEQ, DIFF);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(NANCEEMAIL);
        definition.add(ANGEEMAIL);
        assertEquals(definition, inter.getEmailRecipients());        
    }
    
    // intersection.getEmailRecipients()  with first, second = sequence, mailingList
    @Test
    public void testIntersectionSequenceMailingList() {
        ListExpression inter = ListExpression.makeIntersection(SEQ, HOBBITS);
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, inter.getEmailRecipients());
    }
    
    // intersection.getEmailRecipients()  with first, second = union, union
    @Test
    public void testIntersectionUnionUnion() {
        ListExpression inter = ListExpression.makeIntersection(FRIENDS, APARTMENT);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(NANCEEMAIL);
        definition.add(ANGEEMAIL);
        assertEquals(definition, inter.getEmailRecipients());        
    }
   
    // intersection.getEmailRecipients()  with first, second = union, difference

    @Test
    public void testIntersectionUnionDifference() {
        ListExpression inter = ListExpression.makeIntersection(APARTMENT, DIFF);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(NANCEEMAIL);
        definition.add(ANGEEMAIL);
        assertEquals(definition, inter.getEmailRecipients());        
    }
    
    // intersection.getEmailRecipients()  with first, second = union, intersection

    @Test
    public void testIntersectionUnionIntersection() {
        ListExpression inter = ListExpression.makeIntersection(FRIENDS, INT);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        assertEquals(definition, inter.getEmailRecipients());        
    }
    
    // intersection.getEmailRecipients()  with first, second = union, mailingList
    @Test
    public void testIntersectionUnionMailingList() {
        ListExpression inter = ListExpression.makeIntersection(COUSINS, HOBBITS);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(MERRY);
        definition.add(PIPPIN);
        assertEquals(definition, inter.getEmailRecipients());
    }
    
    // intersection.getEmailRecipients()  with first, second = difference, difference
    @Test
    public void testIntersectionDifferenceDifference() {
        ListExpression inter = ListExpression.makeIntersection(DIFF, DIFF_EMPTY);
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, inter.getEmailRecipients());
        
    }
    
    // intersection.getEmailRecipients()  with first, second = difference, intersection
    @Test
    public void testIntersectionDifferenceIntersection() {
        ListExpression inter = ListExpression.makeIntersection(DIFF, INT_EMPTY);
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, inter.getEmailRecipients());        
    }
    
    // intersection.getEmailRecipients()  with first, second = difference, mailingList
    @Test
    public void testIntersectionDifferenceMailingList() {
        ListExpression inter = ListExpression.makeIntersection(DIFF, HOBBITS);
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, inter.getEmailRecipients());
    }
    
    // intersection.getEmailRecipients()  with first, second = intersection, intersection
    @Test
    public void testIntersectionIntersectionIntersection() {
        ListExpression inter = ListExpression.makeIntersection(INT, INT);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        assertEquals(definition, inter.getEmailRecipients());        
    }
    
    // intersection.getEmailRecipients()  with first, second = intersection, mailingList
    @Test
    public void testIntersectionIntersectionMailingList() {
        ListExpression inter = ListExpression.makeIntersection(INT_EMPTY, HOBBITS);
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, inter.getEmailRecipients());
    }
    
    // intersection.getEmailRecipients()  with first, second = mailinglist, mailingList
    @Test
    public void testIntersectionMailingListMailingList() {
        ListExpression inter = ListExpression.makeIntersection(HOBBITS, HOBBITS);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(MERRY);
        definition.add(PIPPIN);
        definition.add(FRODO);
        definition.add(SAM);
        definition.add(BILBO);
        assertEquals(definition, inter.getEmailRecipients());
    }
    
    // Intersection.toString() with first, second = empty, empty
    @Test
    public void testIntersectionToStringEmpty() {
        ListExpression inter = ListExpression.makeIntersection(ListExpression.makeEmpty(), ListExpression.makeEmpty());
        assertEquals("", inter.toString());
    }
    
    // Intersection.toString() with first, second = email, union
    @Test
    public void testIntersectionToString() {
        ListExpression inter = ListExpression.makeIntersection(FRIENDS, NANCEEMAIL);
        assertEquals(NANCE.toLowerCase(), inter.toString());
        assertEquals(ListExpression.makeIntersection(FRIENDS, NANCEEMAIL).toString(), inter.toString());
    }
    
    // Intersection.toString() with first, second = diff, inter
    @Test
    public void testIntersectionToStringDifferenceIntersection() {
        ListExpression inter = ListExpression.makeIntersection(DIFF, INT_EMPTY);
        assertEquals("", inter.toString());
    }
    
    // Intersection.toString() with first, second = sequence, mailing list
    @Test
    public void testIntersectionToStringSequenceMail() {
        ListExpression inter = ListExpression.makeIntersection(SEQ, HOBBITS);
        assertEquals(ListExpression.makeIntersection(SEQ, HOBBITS).toString(), inter.toString());
        assertEquals(ListExpression.makeIntersection(HOBBITS, SEQ).toString(),inter.toString());
    }
    
    // Intersection.toString() with first, second = email, diff
    @Test
    public void testIntersectionToStringEmailDifference() {
        ListExpression inter = ListExpression.makeIntersection(ANGEEMAIL, DIFF);
        assertEquals(ANGEEMAIL.toString(), inter.toString());
        assertTrue(!ListExpression.makeUnion(DIFF, ANGEEMAIL).toString().equals(inter.toString()));
    }
    
    // Intersection.toString() with first, second = inter, email
    @Test
    public void testIntersectionToStringIntersectionEmail() {
        ListExpression inter = ListExpression.makeIntersection(INT_EMPTY, RANDO);
        assertEquals("", inter.toString());
        assertEquals(ListExpression.makeIntersection(RANDO, INT_EMPTY).toString(), inter.toString());
    }
    
    // Intersection.hashCode() with first, second = empty, empty
    @Test
    public void testIntersectionHashCodeEmpty() {
        ListExpression inter = ListExpression.makeIntersection(ListExpression.makeEmpty(), ListExpression.makeEmpty());
        assertEquals(ListExpression.makeUnion(ListExpression.makeEmpty(), ListExpression.makeEmpty()).hashCode(), inter.hashCode());
    }
    
    // Intersection.hashCode() with first, second = union, email
    @Test
    public void testIntersectionHashCodeUnionEmail() {
        ListExpression inter = ListExpression.makeIntersection(FRIENDS, NANCEEMAIL);
        assertEquals(ListExpression.makeUnion(FRIENDS, NANCEEMAIL).hashCode(), inter.hashCode());
    }
    
    // Intersection.hashCode() with first, second = diff, seq
    @Test
    public void testIntersectionHashCodeDifferenceSequence() {
        ListExpression inter = ListExpression.makeIntersection(DIFF_EMPTY, SEQ);
        assertEquals(ListExpression.makeUnion(DIFF_EMPTY, SEQ).hashCode(), inter.hashCode());
    }
    
    // Intersection.hashCode() with first, second = mailinglist, empty
    @Test
    public void testIntersectionHashCodeMailingEmpty() {
        ListExpression inter = ListExpression.makeIntersection(HOBBITS, ListExpression.makeEmpty());
        assertEquals(ListExpression.makeUnion(ListExpression.makeEmpty(), HOBBITS).hashCode(), inter.hashCode());
    }
    
    // Intersection.hashCode() with first, second = int, union
    @Test
    public void testIntersectionHashCodeIntersectionUnion() {
        ListExpression inter = ListExpression.makeIntersection(INT, FRIENDS);
        assertEquals(ListExpression.makeUnion(FRIENDS, INT).hashCode(), inter.hashCode());
    }
    
    // Intersection.hashCode() with first, second = email, mailing list
    @Test
    public void testIntersectionHashCodeEmailMailingList() {
        ListExpression inter = ListExpression.makeIntersection(ANGEEMAIL, HOBBITS);
        assertEquals(ListExpression.makeUnion(HOBBITS, ANGEEMAIL).hashCode(), inter.hashCode());
    }
    
    // Intersection.equals() with first, second = empty, empty
    @Test
    public void testIntersectionEqualsEmpty() {
        ListExpression empty = ListExpression.makeIntersection(ListExpression.makeEmpty(), ListExpression.makeEmpty());
        assertTrue(empty.equals(ListExpression.makeIntersection(ListExpression.makeEmpty(), ListExpression.makeEmpty())));
        assertTrue(! empty.equals(FRIENDS));
        assertTrue(! empty.equals(ANGE));
    }
    
    // Intersection.equals() with first, second = email, union
    @Test
    public void testIntersectionEqualsEmailUnion() {
        ListExpression set = ListExpression.makeIntersection(YDEMAIL, APARTMENT);
        ListExpression alsoSet = ListExpression.makeIntersection(YDEMAIL, APARTMENT);
        assertTrue(set.equals(alsoSet));
        assertTrue(! set.equals(NANCEEMAIL));
        assertTrue(! set.equals(ListExpression.makeEmpty()));
    }
    
    // Intersection.equals() with first, second = mailing list, empty
    @Test
    public void testIntersectionEqualsMailingListEmpty() {
        ListExpression set = ListExpression.makeIntersection(HOBBITS, ListExpression.makeEmpty());
        ListExpression alsoSet = ListExpression.makeIntersection(HOBBITS, ListExpression.makeEmpty());
        ListExpression flipped = ListExpression.makeIntersection(ListExpression.makeEmpty(), HOBBITS);
        assertTrue(set.equals(alsoSet));
        assertTrue(! set.equals(flipped));
        assertTrue(! set.equals(ListExpression.makeEmpty()));
    }
    
    // Intersection.equals() with first, second = seq, diff
    @Test
    public void testIntersectionEqualsSequenceDifference() {
        ListExpression set = ListExpression.makeIntersection(SEQ, DIFF_EMPTY);
        ListExpression alsoSet = ListExpression.makeIntersection(SEQ, DIFF_EMPTY);
        ListExpression flipped = ListExpression.makeIntersection(DIFF_EMPTY, SEQ);
        assertTrue(set.equals(alsoSet));
        assertTrue(! set.equals(flipped));
        assertTrue(! set.equals(NANCEEMAIL));
        assertTrue(! set.equals(ListExpression.makeEmpty()));
    }
    
    // Intersection.equals() with first, second = email, intersection
    @Test
    public void testIntersectionEqualsEmailIntersection() {
        ListExpression set = ListExpression.makeIntersection(YDEMAIL, INT);
        ListExpression alsoSet = ListExpression.makeIntersection(YDEMAIL, INT);
        ListExpression flipped = ListExpression.makeIntersection(INT, YDEMAIL);
        assertTrue(set.equals(alsoSet));
        assertTrue(! set.equals(flipped));
        assertTrue(! set.equals(NANCEEMAIL));
        assertTrue(! set.equals(ListExpression.makeEmpty()));
    }
    
    // Intersection.equals() with first, second = difference, email
    @Test
    public void testIntersectionEqualsDifferenceEmail() {
        ListExpression set = ListExpression.makeIntersection(DIFF_EMPTY, NANCEEMAIL);
        ListExpression alsoSet = ListExpression.makeIntersection(DIFF_EMPTY, NANCEEMAIL);
        ListExpression flipped = ListExpression.makeIntersection(NANCEEMAIL,DIFF_EMPTY);
        assertTrue(set.equals(alsoSet));
        assertTrue(! set.equals(flipped));
        assertTrue(! set.equals(NANCEEMAIL));
        assertTrue(! set.equals(ListExpression.makeEmpty()));
    }
    
    // difference ///////////////////////////////////////////////////////////////////////////////
    
    // difference.getEmailRecipients()  with first, second = empty, empty
    @Test
    public void testDifferenceEmptyEmpty() {
        ListExpression differ = ListExpression.makeDifference(ListExpression.makeEmpty(), ListExpression.makeEmpty());
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, differ.getEmailRecipients());        
    }
    
    // iDifference.getEmailRecipients()  with first, second = empty, email
    @Test
    public void testDifferenceEmptyEmail() {
        ListExpression differ = ListExpression.makeDifference(ANGEEMAIL, ListExpression.makeEmpty());
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        assertEquals(definition, differ.getEmailRecipients());          
    }
    
    // Difference.getEmailRecipients()  with first, second = empty, sequence
    @Test
    public void testDifferencenEmptySequence() {
        ListExpression differ = ListExpression.makeDifference(ListExpression.makeEmpty(), SEQ);
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, differ.getEmailRecipients());
    }
    
    // Differencen.getEmailRecipients()  with first, second = empty, union
    @Test
    public void testDifferenceEmptyUnion() {
        ListExpression differ = ListExpression.makeDifference(APARTMENT, ListExpression.makeEmpty());
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        definition.add(NANCEEMAIL);
        assertEquals(definition, differ.getEmailRecipients());        
    }
    
    // Difference.getEmailRecipients()  with first, second = empty, difference
    @Test
    public void testDifferenceEmptyDifference() {
        ListExpression differ = ListExpression.makeDifference(ListExpression.makeEmpty(), DIFF);
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, differ.getEmailRecipients());        
    }
    
    // Difference.getEmailRecipients()  with first, second = empty, intersection
    @Test
    public void testDifferenceEmptyIntersection() {
        ListExpression differ = ListExpression.makeDifference(INT, ListExpression.makeEmpty());
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        assertEquals(definition, differ.getEmailRecipients());        
    }
    
    // difference.getEmailRecipients()  with first, second = empty, mailingList
    @Test
    public void testDifferenceEmptyMailingList() {
        ListExpression differ = ListExpression.makeDifference(ListExpression.makeEmpty(), HOBBITS);
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, differ.getEmailRecipients());        
    }
    
    // Difference.getEmailRecipients()  with first, second = email, email
    @Test
    public void testDifferenceEmailEmail() {
        ListExpression differ = ListExpression.makeDifference(ANGEEMAIL, NANCEEMAIL);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        assertEquals(definition, differ.getEmailRecipients());        
    }
    
    // Difference.getEmailRecipients()  with first, second = email, sequence
    @Test
    public void testDifferenceEmailSequence() {
        ListExpression differ = ListExpression.makeDifference(ANGEEMAIL, SEQ);
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, differ.getEmailRecipients());        
    }
    
    // Difference.getEmailRecipients()  with first, second = email, union
    @Test
    public void testDifferenceEmailUnion() {
        ListExpression differ = ListExpression.makeDifference(ANGEEMAIL, FRIENDS);
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, differ.getEmailRecipients());        
    }
    
    // Difference.getEmailRecipients()  with first, second = email, difference    
    @Test
    public void testDifferenceEmailDifference() {
        ListExpression differ = ListExpression.makeDifference(NANCEEMAIL, DIFF);
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, differ.getEmailRecipients());        
    }
    
    // Difference.getEmailRecipients()  with first, second = email, intersection
    @Test
    public void testDifferenceEmailIntersection() {
        ListExpression differ = ListExpression.makeDifference(ANGEEMAIL, INT);
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, differ.getEmailRecipients());
    }
    
    // difference.getEmailRecipients()  with first, second = email, mailinglist
    @Test
    public void testDifferenceEmailMailingList() {
        ListExpression differ = ListExpression.makeDifference(HOBBITS, FRODO);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(SAM);
        definition.add(MERRY);
        definition.add(BILBO);
        definition.add(PIPPIN);
        assertEquals(definition, differ.getEmailRecipients());        
    }
    
    // Difference.getEmailRecipients()  with first, second = sequence, sequence
    @Test
    public void testDifferenceSequenceSequence() {
        ListExpression differ = ListExpression.makeDifference(SEQ, SEQ);
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, differ.getEmailRecipients());        
    }
    
    // Difference.getEmailRecipients()  with first, second = sequence, union
    @Test
    public void testDifferenceSequenceUnion() {
        ListExpression differ = ListExpression.makeDifference(SEQ, FRIENDS);
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, differ.getEmailRecipients());        
    }
    
    // Difference.getEmailRecipients()  with first, second = sequence, intersection
    @Test
    public void testDifferenceSequenceIntersection() {
        ListExpression differ = ListExpression.makeDifference(SEQ, INT_EMPTY);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        definition.add(NANCEEMAIL);
        assertEquals(definition, differ.getEmailRecipients());        
    }
    
    // Difference.getEmailRecipients()  with first, second = sequence, difference
    @Test
    public void testDifferenceSequenceDifference() {
        ListExpression differ = ListExpression.makeDifference(SEQ, DIFF);
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, differ.getEmailRecipients());          
    }
    
    // difference.getEmailRecipients()  with first, second = sequence, mailinglist
    @Test
    public void testDifferenceSequenceMailingList() {
        ListExpression differ = ListExpression.makeDifference(ListExpression.makeSequence(COUSINS, FRODO), HOBBITS);
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, differ.getEmailRecipients());        
    }
    
    // Difference.getEmailRecipients()  with first, second = union, union
    @Test
    public void testDifferenceUnionUnion() {
        ListExpression differ = ListExpression.makeDifference(APARTMENT, FRIENDS);
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, differ.getEmailRecipients());          
    }
   
    // Difference.getEmailRecipients()  with first, second = union, difference
    @Test
    public void testDifferenceUnionDifference() {
        ListExpression differ = ListExpression.makeDifference(FRIENDS, DIFF);
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, differ.getEmailRecipients());          
    }
    
    // Difference.getEmailRecipients()  with first, second = union, intersection
    @Test
    public void testDifferenceUnionIntersection() {
        ListExpression differ = ListExpression.makeDifference(APARTMENT, INT_EMPTY);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        definition.add(NANCEEMAIL);
        assertEquals(definition, differ.getEmailRecipients());          
    }
    
    // difference.getEmailRecipients()  with first, second = union, mailinglist
    @Test
    public void testDifferenceeUnionMailingList() {
        ListExpression differ = ListExpression.makeDifference(LOVE, HOBBITS);
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, differ.getEmailRecipients());        
    }
    
    // Difference.getEmailRecipients()  with first, second = difference, difference
    @Test
    public void testDifferenceDifferenceDifference() {
        ListExpression differ = ListExpression.makeDifference(DIFF, DIFF_EMPTY);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        definition.add(NANCEEMAIL);
        definition.add(YDEMAIL);
        assertEquals(definition, differ.getEmailRecipients());          
    }
    
    // Difference.getEmailRecipients()  with first, second = difference, intersection
    @Test
    public void testDifferenceDifferenceIntersection() {
        ListExpression differ = ListExpression.makeDifference(DIFF_EMPTY, INT_EMPTY);
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, differ.getEmailRecipients());          
    }
        
    // difference.getEmailRecipients()  with first, second = difference, mailinglist
    @Test
    public void testDifferenceDifferenceMailingList() {
        ListExpression differ = ListExpression.makeDifference(DIFF_EMPTY, HOBBITS);
        Set<ListExpression> definition = new HashSet<>();
        assertEquals(definition, differ.getEmailRecipients());        
    }
    
    // Difference.getEmailRecipients()  with first, second = intersection, intersection
    @Test
    public void testDifferenceIntersectionIntersection() {
        ListExpression differ = ListExpression.makeDifference(INT, INT_EMPTY);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        assertEquals(definition, differ.getEmailRecipients());          
    }
    
    // difference.getEmailRecipients()  with first, second = intersection, mailinglist
    @Test
    public void testDifferenceIntersectionMailingList() {
        ListExpression differ = ListExpression.makeDifference(INT, HOBBITS);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        assertEquals(definition, differ.getEmailRecipients());        
    }
    
    // difference.getEmailRecipients()  with first, second = mailinglist, mailinglist
    @Test
    public void testDifferenceMailingListMailingList() {
        ListExpression differ = ListExpression.makeDifference(HOBBITS, ListExpression.makeMailingList("empty", ListExpression.makeEmpty()));
        Set<ListExpression> definition = new HashSet<>();
        definition.add(FRODO);
        definition.add(BILBO);
        definition.add(SAM);
        definition.add(MERRY);
        definition.add(PIPPIN);
        assertEquals(definition, differ.getEmailRecipients());        
    }
    
    // Difference.toString() with first, second = empty, empty
    @Test
    public void testDifferenceToStringEmpty() {
        ListExpression diff = ListExpression.makeDifference(ListExpression.makeEmpty(), ListExpression.makeEmpty());
        assertEquals("", diff.toString());
    }
    
    // Difference.toString() with first, second = email, union
    @Test
    public void testDifferenceToString() {
        ListExpression diff = ListExpression.makeDifference(FRIENDS, NANCEEMAIL);
        assertEquals(ListExpression.makeUnion(ANGEEMAIL, YDEMAIL).toString(), diff.toString());
        assertEquals(ListExpression.makeDifference(FRIENDS, NANCEEMAIL).toString(), diff.toString());
    }
    
    // Difference.toString() with first, second = diff, inter
    @Test
    public void testDifferenceToStringDifferenceIntersection() {
        ListExpression diff = ListExpression.makeDifference(DIFF, INT_EMPTY);
        assertEquals(DIFF.toString(), diff.toString());
    }
    
    // Difference.toString() with first, second = sequence, mailing list
    @Test
    public void testDifferenceToStringSequenceMail() {
        ListExpression diff = ListExpression.makeDifference(SEQ, HOBBITS);
        assertEquals(ListExpression.makeDifference(SEQ, HOBBITS).toString(), diff.toString());
        assertTrue(!ListExpression.makeDifference(HOBBITS, SEQ).toString().equals(diff.toString()));
    }
    
    // Difference.toString() with first, second = email, diff
    @Test
    public void testDifferenceToStringEmailDifference() {
        ListExpression diff = ListExpression.makeDifference(ANGEEMAIL, DIFF);
        assertEquals("", diff.toString());
        assertTrue(!ListExpression.makeUnion(DIFF, ANGEEMAIL).toString().equals(diff.toString()));
    }
    
    // Difference.toString() with first, second = inter, email
    @Test
    public void testDifferenceToStringIntersectionEmail() {
        ListExpression diff = ListExpression.makeDifference(INT_EMPTY, RANDO);
        assertEquals("", diff.toString());
        assertEquals(ListExpression.makeDifference(INT_EMPTY, RANDO).toString(), diff.toString());
    }
    
    // Difference.hashCode() with first, second = empty, empty
    @Test
    public void testDifferenceHashCodeEmpty() {
        ListExpression diff = ListExpression.makeDifference(ListExpression.makeEmpty(), ListExpression.makeEmpty());
        assertEquals(ListExpression.makeUnion(ListExpression.makeEmpty(), ListExpression.makeEmpty()).hashCode(),diff.hashCode());
    }
    
    // Difference.hashCode() with first, second = union, email
    @Test
    public void testDifferenceHashCodeUnionEmail() {
        ListExpression diff = ListExpression.makeDifference(FRIENDS, NANCEEMAIL);
        assertEquals(ListExpression.makeUnion(FRIENDS, NANCEEMAIL).hashCode(), diff.hashCode());
    }
    
    // Difference.hashCode() with first, second = diff, seq
    @Test
    public void testDifferenceHashCodeDifferenceSequence() {
        ListExpression diff = ListExpression.makeDifference(DIFF_EMPTY, SEQ);
        assertEquals(ListExpression.makeUnion(DIFF_EMPTY, SEQ).hashCode(), diff.hashCode());
    }
    
    // Difference.hashCode() with first, second = mailinglist, empty
    @Test
    public void testDifferenceHashCodeMailingEmpty() {
        ListExpression diff = ListExpression.makeDifference(HOBBITS, ListExpression.makeEmpty());
        assertEquals(ListExpression.makeUnion(ListExpression.makeEmpty(), HOBBITS).hashCode(), diff.hashCode());
    }
    
    // Difference.hashCode() with first, second = int, union
    @Test
    public void testDifferenceHashCodeIntersectionUnion() {
        ListExpression diff = ListExpression.makeDifference(INT, FRIENDS);
        assertEquals(ListExpression.makeUnion(FRIENDS, INT).hashCode(), diff.hashCode());
    }
    
    // Difference.hashCode() with first, second = email, mailing list
    @Test
    public void testDifferenceHashCodeEmailMailingList() {
        ListExpression diff = ListExpression.makeDifference(ANGEEMAIL, HOBBITS);
        assertEquals(ListExpression.makeDifference(HOBBITS, ANGEEMAIL).hashCode(), diff.hashCode());
    }
    
    // Difference.equals() with first, second = empty, empty
    @Test
    public void testDifferenceEqualsEmpty() {
        ListExpression empty = ListExpression.makeDifference(ListExpression.makeEmpty(), ListExpression.makeEmpty());
        assertTrue(empty.equals(ListExpression.makeDifference(ListExpression.makeEmpty(), ListExpression.makeEmpty())));
        assertTrue(! empty.equals(FRIENDS));
        assertTrue(! empty.equals(ANGE));
    }
    
    // Difference.equals() with first, second = email, union
    @Test
    public void testDifferenceEqualsEmailUnion() {
        ListExpression set = ListExpression.makeDifference(YDEMAIL, APARTMENT);
        ListExpression alsoSet = ListExpression.makeDifference(YDEMAIL, APARTMENT);
        assertTrue(set.equals(alsoSet));
        assertTrue(! set.equals(NANCEEMAIL));
        assertTrue(! set.equals(ListExpression.makeEmpty()));
    }
    
    // Difference.equals() with first, second = mailing list, empty
    @Test
    public void testDifferenceEqualsMailingListEmpty() {
        ListExpression set = ListExpression.makeDifference(HOBBITS, ListExpression.makeEmpty());
        ListExpression alsoSet = ListExpression.makeDifference(HOBBITS, ListExpression.makeEmpty());
        ListExpression flipped = ListExpression.makeDifference(ListExpression.makeEmpty(), HOBBITS);
        assertTrue(set.equals(alsoSet));
        assertTrue(! set.equals(flipped));
        assertTrue(! set.equals(ListExpression.makeEmpty()));
    }
    
    // Difference.equals() with first, second = seq, diff
    @Test
    public void testDifferenceEqualsSequenceDifference() {
        ListExpression set = ListExpression.makeDifference(SEQ, DIFF_EMPTY);
        ListExpression alsoSet = ListExpression.makeDifference(SEQ, DIFF_EMPTY);
        ListExpression flipped = ListExpression.makeDifference(DIFF_EMPTY, SEQ);
        assertTrue(set.equals(alsoSet));
        assertTrue(! set.equals(flipped));
        assertTrue(! set.equals(NANCEEMAIL));
        assertTrue(! set.equals(ListExpression.makeEmpty()));
    }
    
    // Difference.equals() with first, second = email, intersection
    @Test
    public void testDifferenceEqualsEmailIntersection() {
        ListExpression set = ListExpression.makeDifference(YDEMAIL, INT);
        ListExpression alsoSet = ListExpression.makeDifference(YDEMAIL, INT);
        ListExpression flipped = ListExpression.makeDifference(INT, YDEMAIL);
        assertTrue(set.equals(alsoSet));
        assertTrue(! set.equals(flipped));
        assertTrue(! set.equals(NANCEEMAIL));
        assertTrue(! set.equals(ListExpression.makeEmpty()));
    }
    
    // Difference.equals() with first, second = difference, email
    @Test
    public void testDifferenceEqualsDifferenceEmail() {
        ListExpression set = ListExpression.makeDifference(DIFF_EMPTY, NANCEEMAIL);
        ListExpression alsoSet = ListExpression.makeDifference(DIFF_EMPTY, NANCEEMAIL);
        ListExpression flipped = ListExpression.makeDifference(NANCEEMAIL,DIFF_EMPTY);
        assertTrue(set.equals(alsoSet));
        assertTrue(! set.equals(flipped));
        assertTrue(! set.equals(NANCEEMAIL));
        assertTrue(! set.equals(ListExpression.makeEmpty()));
    }
    
    // Mailing List ////////////////////////////////////////////////////////////////////////////////////////////
    
    // mailinglist.getEmailRecipients()  with recipients = empty
    @Test
    public void testMailingListEmpty() {
        ListExpression mailingList = ListExpression.makeMailingList("empty", ListExpression.makeEmpty());
        assertEquals(new HashSet<>(), mailingList.getEmailRecipients());
    }
    
    // mailinglist.getEmailRecipients()  with recipients = email
    @Test
    public void testMailingListEmail() {
        ListExpression mailingList = ListExpression.makeMailingList("nance", NANCEEMAIL);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(NANCEEMAIL);
        assertEquals(definition, mailingList.getEmailRecipients());
    }
    
    // mailinglist.getEmailRecipients()  with recipients = sequence
    @Test
    public void testMailingListSequence() {
        ListExpression mailingList = ListExpression.makeMailingList("seq", SEQ);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(NANCEEMAIL);
        definition.add(ANGEEMAIL);
        assertEquals(definition, mailingList.getEmailRecipients());
    }
   
    // mailinglist.getEmailRecipients()  with recipients = union
    @Test
    public void testMailingListUnion() {
        ListExpression mailingList = ListExpression.makeMailingList("apartment", APARTMENT);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(NANCEEMAIL);
        definition.add(ANGEEMAIL);
        assertEquals(definition, mailingList.getEmailRecipients());
    }
    
    // mailinglist.getEmailRecipients()  with recipients = intersection
    @Test
    public void testMailingListIntersection() {
        ListExpression mailingList = ListExpression.makeMailingList("intersection", INT);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(ANGEEMAIL);
        assertEquals(definition, mailingList.getEmailRecipients());
    }
    
    // mailinglist.getEmailRecipients()  with recipients = difference
    @Test
    public void testMailingListDifference() {
        ListExpression mailingList = ListExpression.makeMailingList("diff", DIFF);
        Set<ListExpression> definition = new HashSet<>();
        definition.add(NANCEEMAIL);
        definition.add(ANGEEMAIL);
        definition.add(YDEMAIL);
        assertEquals(definition, mailingList.getEmailRecipients());
    }
    
    // mailinglist.toString() with recipients = empty
    @Test
    public void testMailingListToStringEmpty() {
        ListExpression mailingList = ListExpression.makeMailingList("empty", ListExpression.makeEmpty());
        assertEquals("", mailingList.toString());
    }
    
    // MailingList.toString() with recipients = email
    @Test
    public void testMailingListToStringEmail() {
        ListExpression mailingList = ListExpression.makeMailingList("nance", NANCEEMAIL);
        assertEquals(NANCEEMAIL.toString(), mailingList.toString());
        assertEquals(ListExpression.makeIntersection(FRIENDS, NANCEEMAIL).toString(), mailingList.toString());
    }
    
    // MailingList.toString() with recipients = seq
    @Test
    public void testMailingListToStringSequence() {
        ListExpression mailingList = ListExpression.makeMailingList("seq", SEQ);
        assertEquals(SEQ.toString(), mailingList.toString());
    }
    
    // MailingList.toString() with recipients = union
    @Test
    public void testMailingListToStringUnion() {
        ListExpression mailingList = ListExpression.makeMailingList("can", FRIENDS);
        assertEquals(FRIENDS.toString(), mailingList.toString());
        assertTrue(!ListExpression.makeDifference(HOBBITS, SEQ).toString().equals(mailingList.toString()));
    }
    
    // MailingList.toString() with recipients = diff
    @Test
    public void testMailingListToStringDifference() {
        ListExpression mailingList = ListExpression.makeMailingList("dif", DIFF);
        assertEquals(DIFF.toString(), mailingList.toString());
        assertEquals(ListExpression.makeMailingList("dif", DIFF).toString(),mailingList.toString());
    }
    
    // MailingList.toString() with recipients = inter
    @Test
    public void testMailingListToStringIntersection() {
        ListExpression mailingList = ListExpression.makeMailingList("inter", INT_EMPTY);
        assertEquals("", mailingList.toString());
        assertEquals(ListExpression.makeDifference(INT_EMPTY, RANDO).toString(), mailingList.toString());
    }
    
    // MailingList.toString() with recipients = mailinglists
    @Test
    public void testMailingListToStringMailingList() {
        ListExpression mailingList = ListExpression.makeMailingList("ihobbit", HOBBITS);
        assertEquals(HOBBITS.toString(), mailingList.toString());
        assertEquals(ListExpression.makeMailingList("ihobbit", HOBBITS).toString(), mailingList.toString());
    }
    
    // MailingList.hashCode() with recipients = empty
    @Test
    public void testMailingListHashCodeEmpty() {
        ListExpression mailingList = ListExpression.makeMailingList("empty", ListExpression.makeEmpty());
        assertEquals(ListExpression.makeUnion(ListExpression.makeEmpty(), ListExpression.makeEmpty()).hashCode(), mailingList.hashCode());
    }
    
    // MailingList.hashCode() with recipients = email
    @Test
    public void testMailingListHashCodeEmail() {
        ListExpression mailingList = ListExpression.makeMailingList("nance", NANCEEMAIL);
        assertEquals(NANCEEMAIL.hashCode(), mailingList.hashCode());
    }
    
    // MailingList.hashCode() with recipients = seq
    @Test
    public void testMailingListHashCodeDifferenceSequence() {
        ListExpression mailingList = ListExpression.makeMailingList("seq", SEQ);
        assertEquals(ListExpression.makeMailingList("seq", SEQ).hashCode(), mailingList.hashCode());
    }
    
    // MailingList.hashCode() with recipients = union
    @Test
    public void testDifferenceHashCodeUnion() {
        ListExpression mailingList = ListExpression.makeMailingList("union", APARTMENT);
        assertEquals(ListExpression.makeMailingList("union", APARTMENT).hashCode(), mailingList.hashCode());
    }
    
    // MailingList.hashCode() with recipients = diff
    @Test
    public void testMailingListHashCodeDifference() {
        ListExpression mailingList = ListExpression.makeMailingList("diff", DIFF);
        assertEquals(ListExpression.makeMailingList("diff", DIFF).hashCode(), mailingList.hashCode());
    }
    
    // MailingList.hashCode() with recipients = inter
    @Test
    public void testMailingListHashCodIntersection() {
        ListExpression mailingList = ListExpression.makeMailingList("int", INT);
        assertEquals(ListExpression.makeMailingList("int", INT).hashCode(), mailingList.hashCode());
    }
    
    // MailingList.hashCode() with recipients = mailinglist
    @Test
    public void testMailingListHashCodeMailingList() {
        ListExpression mailingList = ListExpression.makeMailingList("morehobbits", HOBBITS);
        assertEquals(ListExpression.makeMailingList("morehobbits", HOBBITS).hashCode(), mailingList.hashCode());
    }
    
    
    // MailingList.equals() with recipients = empty
    @Test
    public void testMailingListEqualsEmpty() {
        ListExpression empty = ListExpression.makeMailingList("empty", ListExpression.makeEmpty());
        assertTrue(empty.equals(ListExpression.makeMailingList("empty", ListExpression.makeEmpty())));
        assertTrue(! empty.equals(FRIENDS));
        assertTrue(! empty.equals(ANGE));
    }
    
    // MailingList.equals() with recipients = email
    @Test
    public void testMailingListEqualsEmail() {
        ListExpression set = ListExpression.makeMailingList("me", YDEMAIL);
        ListExpression alsoSet = ListExpression.makeMailingList("me", YDEMAIL);
        assertTrue(set.equals(alsoSet));
        assertTrue(! set.equals(NANCEEMAIL));
        assertTrue(! set.equals(ListExpression.makeEmpty()));
    }
    
    // MailingList.equals() with recipients = union
    @Test
    public void testMailingListEqualsUnion() {
        ListExpression set = ListExpression.makeMailingList("union", APARTMENT);
        ListExpression alsoSet = ListExpression.makeMailingList("union", APARTMENT);
        assertTrue(set.equals(alsoSet));
        assertTrue(! set.equals(ListExpression.makeEmpty()));
    }
    
    // MailingList.equals() with recipients = seq
    @Test
    public void testMailingListEqualsSequence() {
        ListExpression set = ListExpression.makeMailingList("seq", SEQ);
        ListExpression alsoSet = ListExpression.makeMailingList("seq", SEQ);
        assertTrue(set.equals(alsoSet));
        assertTrue(! set.equals(NANCEEMAIL));
        assertTrue(! set.equals(ListExpression.makeEmpty()));
    }
    
    // MailingList.equals() with recipients = intersection
    @Test
    public void testMailingListEqualsIntersection() {
        ListExpression set = ListExpression.makeMailingList("int", INT);
        ListExpression alsoSet = ListExpression.makeMailingList("int", INT);
        assertTrue(set.equals(alsoSet));
        assertTrue(! set.equals(NANCEEMAIL));
        assertTrue(! set.equals(ListExpression.makeEmpty()));
    }
    
    // MailingList.equals() with recipients = difference
    @Test
    public void testMailingListEqualsDifference() {
        ListExpression set = ListExpression.makeMailingList("empty", DIFF_EMPTY);
        ListExpression alsoSet = ListExpression.makeMailingList("empty", DIFF_EMPTY);
        assertTrue(set.equals(alsoSet));
        assertTrue(! set.equals(NANCEEMAIL));
        assertTrue(! set.equals(ListExpression.makeEmpty()));
    }
    
    // MailingList.equals() with recipients = mailinglist
    @Test
    public void testMailingListEqualsMailingList() {
        ListExpression set = ListExpression.makeMailingList("alsohobbits", HOBBITS);
        ListExpression alsoSet = ListExpression.makeMailingList("alsohobbits", HOBBITS);
        assertTrue(set.equals(alsoSet));
        assertTrue(! set.equals(NANCEEMAIL));
        assertTrue(! set.equals(ListExpression.makeEmpty()));
    }
    
    // DefinedMailingLists //////////////////////////////////////////////////////////////////////////
    
    // empty creator
    @Test
    public void testDefinedMailingListsEmptyCreator() {
        assertEquals(new HashMap<>(), new DefinedMailingLists().getMailingLists());
    }
    
    // not empty creator
    @Test
    public void testDefinedMailingListsCreator() {
        MAILING_LISTS.put("hobbits", HOBBITS);
        Map<String, ListExpression> test = new HashMap<>();
        test.put("hobbits", HOBBITS);
        assertEquals(test, new DefinedMailingLists(MAILING_LISTS).getMailingLists());
    }
    
    // getMailingLists: should return a mailing list
    @Test
    public void testDefinedMailingListsGetMailingLists() {
        MAILING_LISTS.put("hobbits", HOBBITS);
        MAILING_LISTS.put("empty", INT_EMPTY);
        MAILING_LISTS.put("rando", RANDO);
        Map<String, ListExpression> test = new HashMap<>();
        test.put("hobbits", HOBBITS);
        test.put("empty", INT_EMPTY);
        test.put("rando", RANDO);
        assertEquals(test, new DefinedMailingLists(MAILING_LISTS).getMailingLists());
    }
    
    // addMailingList: add mailinglists to this mailinglist
    @Test
    public void testDefinedMailingListsAddMailingLists() {
        MAILING_LISTS.put("hobbits", HOBBITS);
        DefinedMailingLists lists = new DefinedMailingLists(MAILING_LISTS);
        Map<String, ListExpression> test = new HashMap<>();
        test.put("hobbits", HOBBITS);
        lists.addMailingList("empty", INT_EMPTY);
        test.put("empty", INT_EMPTY);
        lists.addMailingList("rando", RANDO);
        test.put("rando", RANDO);
        lists.addMailingList("emp", ListExpression.makeEmpty());
        test.put("emp", ListExpression.makeEmpty());
        assertEquals(test, lists.getMailingLists());
    }
    
    
    
}