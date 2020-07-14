/* Copyright (c) 2015-2017 MIT 6.005/6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package norn;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
/**
 * Tests for inputs and commands to console, and args passed through command line.
 * The JUnit tests for inputOutput() in Main by using ByteArrayI/OStreams to mimic the console,
 * and an array of strings to mimic args.
 * Since main() acts as a tiny wrapper around inputOutput(), a few manual tests are included
 * as comments on the bottom (e.g. java -cp bin:lib/parserlib.jar norn.Main filename1 filename2 ...) 
 * A few manual tests are also included to test for consistency of updates 
 * between console and web server.
 */
public class MainTest { 
    
    // Testing strategy for main(String[] args)
    
    // Partitions for INPUTS TO COMMAND LINE (args): 
    //          Testing fileNames parameter in 
    //              inputOutput(InputStream inputStream, PrintStream out, 
    //                          UserInput userInput, String[] fileNames)
    //          (not as thorough because tested load() in CommandsTest)
    //      Invalid inputs:
    //          invalid fileName
    //          fileName does not exist
    //          fileName does not contain valid list expressions
    //      Valid inputs:
    //          fileNames.length = 0, 1, >1 given
    //          fileName.length() = 1, >1
    //          a file replaces definitions defined in other files

    // Partitions for INPUTS TO CONSOLE:
    //          Testing inputStream, out, userInput parameters in 
    //              inputOutput(InputStream inputStream, PrintStream out, 
    //                          UserInput userInput, String[] fileNames)
    //          (not as thorough because already tested readInput() in UserInputTest, 
    //           and save() and load() in CommandsTest)
    //      invalid list expressions (invalid chars, mail loops)
    //      basic list expressions (empty, email, union, intersect, difference, definition, sequences)
    //      save (0, 1, >1 list definitions, invalid filename)
    //      load (0, 1, >1 list definitions, replaces an already-existing definition,
    //            invalid list expression, invalid filename)
    //      save and load


    // -check that updates to the defined mailing lists are consistent between the console
    //     and the web server
    
    //---------------------------------------------------------------------------------
    
    // Helper functions
    
    /**
     * Asserts if the String output of an outputStream for each command contains
     * the correct set of recipients
     * @param expected holds a list of expected recipients (one for each command 
     * entered in the inputStream)
     * @param actual String output of the outputStream
     */
    private static void checkCorrectOutput(List<List<String>> expected, String actual) {
        String[] outputs = actual.split("> ");
        // start at index 1 because splitting on "> " always causes first item to be empty
        for (int i = 1; i < outputs.length; i++) {
            checkCorrectRecipients(expected.get(i-1), outputs[i]);
        }
        assertEquals(outputs.length-1, expected.size());
    }

    /**
     * Asserts that the output of a command contains the correct mailing addresses. 
     * @param recipients A list of the mailing addresses that should appear in actual. 
     * @param actual The output to test
     */
    private static void checkCorrectRecipients(List<String> recipients, String actual) {
        //check that every string in contains appears in actual
        for (String address : recipients) {
            assertTrue(actual.contains(address));
        }
        //assert that actual does not contain any extra addresses 
        assertTrue(actual.split(",").length == recipients.size());
    }
    
    private static final String FILES_PATH = "test/";
    private static final String[] EMPTY_ARGS = new String[0];
    
    //---------------------------------------------------------------------------------

    // Tests for INPUTS TO COMMAND LINE
    
    // covers invalid inputs: invalid fileName (newlines)
    @Test
    public void testArgsInvalidFileName() {
        String input = "";
        byte[] inputBytes = input.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);
        UserInput userInput = new UserInput();
        String[] invalid = {"woohoo\nwoohoo"};
        Main.inputOutput(inputStream, out, userInput, invalid);
        assertEquals("no definitions loaded", "", userInput.getParsableDefinedMailingLists());
    }    
    
    // covers invalid inputs: fileName does not exist
    @Test
    public void testArgsFolderDoesNotExist() {
        String input = "";
        byte[] inputBytes = input.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);
        UserInput userInput = new UserInput();
        String[] invalid = {FILES_PATH + "fakeDirectory/hi.txt"};
        Main.inputOutput(inputStream, out, userInput, invalid);
        assertEquals("no definitions loaded", "", userInput.getParsableDefinedMailingLists());
    }
    
    // covers invalid inputs: fileName does not exist
    @Test
    public void testArgsFileNameDoesNotExist() {
        String input = "";
        byte[] inputBytes = input.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);
        UserInput userInput = new UserInput();
        String[] invalid = {FILES_PATH + "hi.txt"};
        Main.inputOutput(inputStream, out, userInput, invalid);
        assertEquals("no definitions loaded", "", userInput.getParsableDefinedMailingLists());
    }
    
    // covers invalid inputs: fileName does not contain valid list expressions
    @Test
    public void testArgsInvalidListExpressions() {
        String input = "";
        byte[] inputBytes = input.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);
        UserInput userInput = new UserInput();
        String[] invalid = {FILES_PATH + "loadInvalidListExpression.txt"};
        Main.inputOutput(inputStream, out, userInput, invalid);
        assertEquals("no definitions loaded", "", userInput.getParsableDefinedMailingLists());
    }
    
    // covers valid inputs: fileNames.length = 0
    @Test
    public void testArgsNone() {
        String input = "";
        byte[] inputBytes = input.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);
        UserInput userInput = new UserInput();
        Main.inputOutput(inputStream, out, userInput, EMPTY_ARGS);
        assertEquals("no definitions loaded", "", userInput.getParsableDefinedMailingLists());
    }

    // covers valid inputs: fileNames.length = 1
    //                      fileName.length > 1
    @Test
    public void testArgsOne() {
        String input = "6.031";
        byte[] inputBytes = input.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);
        UserInput userInput = new UserInput();
        String[] fileNames = {FILES_PATH + "loadOneDefinition.txt"};
        // file contents:
        // 6.031 = ange@mit.edu, helenwh@mit.edu, cynthiaz@mit.edu
        Main.inputOutput(inputStream, out, userInput, fileNames);
        List<String> expected = Arrays.asList("ange@mit.edu", "helenwh@mit.edu", "cynthiaz@mit.edu");
        String[] outputs = outputStream.toString().split("> ");
        checkCorrectRecipients(expected, outputs[outputs.length-1]); // check for last output
    }

    // covers valid inputs: fileNames.length > 1
    //                      fileName.length() = 1, >1
    @Test
    public void testArgsMoreThanOne() {
        String input1 = "6.031";
        String input2 = "one";
        String input3 = "two";
        String input = input1 + "\n" + input2 + "\n" + input3;
        byte[] inputBytes = input.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);
        UserInput userInput = new UserInput();
        String[] fileNames = {FILES_PATH + "1", FILES_PATH + "loadOneDefinition.txt"};
        // file contents in 1:
        // one = 1@1; two = 2@2
        // file contents in loadOneDefinition.txt:
        // 6.031 = ange@mit.edu, helenwh@mit.edu, cynthiaz@mit.edu
        Main.inputOutput(inputStream, out, userInput, fileNames);
        List<String> expected1 = Arrays.asList("ange@mit.edu", "helenwh@mit.edu", "cynthiaz@mit.edu");
        List<String> expected2 = Arrays.asList("1@1");
        List<String> expected3 = Arrays.asList("2@2");
        checkCorrectOutput(Arrays.asList(expected1, expected2, expected3), outputStream.toString());
    }

    // covers valid inputs: a file replaces definitions defined in other files
    @Test
    public void testArgsReplace() {
        String input = "daysUntilSummerBreak";
        byte[] inputBytes = input.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);
        UserInput userInput = new UserInput();
        String[] fileNames = {FILES_PATH + "loadToBeReplaced.txt", FILES_PATH + "loadToReplace.txt"};
        // file contents in loadToBeReplaced.txt:
        // daysUntilSummerBreak = 0@days, 0@hours, 0@minutes
        // file contents in loadToReplace.txt:
        // daysUntilSummerBreak = 12@days, 6@hours, 40@minutes
        Main.inputOutput(inputStream, out, userInput, fileNames);
        List<String> expected = Arrays.asList("12@days", "6@hours", "40@minutes");
        checkCorrectOutput(Arrays.asList(expected), outputStream.toString());
    }
    
    //---------------------------------------------------------------------------------

    // Tests for INPUTS TO CONSOLE

    // covers invalid list expressions: invalid chars
    @Test
    public void testInvalidChar() {
        String input = "invalid?";
        byte[] inputBytes = input.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);
        UserInput userInput = new UserInput();
        Main.inputOutput(inputStream, out, userInput, EMPTY_ARGS);
        assertTrue("expected unable to parse error message", outputStream.toString().toLowerCase()
                .contains("parse"));
    }

    // covers invalid list expressions: mail loops
    @Test
    public void testMailLoops() {
        String input = "hi = hello \n hello = hola@hola, hi";
        byte[] inputBytes = input.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);
        UserInput userInput = new UserInput();
        Main.inputOutput(inputStream, out, userInput, EMPTY_ARGS);
        assertTrue("expected mail loop error message", outputStream.toString().toLowerCase()
                .contains("mail loop"));
    }

    // covers basic list expressions: empty
    @Test
    public void testEmpty() {
        String input = "";
        byte[] inputBytes = input.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);
        UserInput userInput = new UserInput();
        Main.inputOutput(inputStream, out, userInput, EMPTY_ARGS);
        assertEquals("expected no output", "> ", outputStream.toString());
    }
    
    // covers basic list expressions: email
    @Test
    public void testEmail() {
        String input1 = "student@mit.edu";
        String input2 = "student+2@mit.edu";
        String input = input1 + "\n" + input2;
        byte[] inputBytes = input.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);
        UserInput userInput = new UserInput();
        Main.inputOutput(inputStream, out, userInput, EMPTY_ARGS);
        List<String> expected1 = Arrays.asList(input1);
        List<String> expected2 = Arrays.asList(input2);
        checkCorrectOutput(Arrays.asList(expected1, expected2), outputStream.toString());
    }
    
    // covers basic list expressions: union
    @Test
    public void testUnion() {
        String email1 = "student@mit.edu";
        String email2 = "student+2@mit.edu";
        String input = email1 + "," + email2;
        byte[] inputBytes = input.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);
        UserInput userInput = new UserInput();
        Main.inputOutput(inputStream, out, userInput, EMPTY_ARGS);
        List<String> expected1 = Arrays.asList(email1, email2);
        checkCorrectOutput(Arrays.asList(expected1), outputStream.toString());
    }

    // covers basic list expressions: intersect, definition
    @Test
    public void testIntersectDefinition() {
        String input1 = "6.031 = ange@mit.edu, helenwh@mit.edu, cynthiaz@mit.edu";
        String input2 = "6.036 = helenwh@mit.edu, cynthiaz@mit.edu";
        String input3 = "6.031*6.036";
        String input = input1 + "\n" + input2 + "\n" + input3;
        byte[] inputBytes = input.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);
        UserInput userInput = new UserInput();
        Main.inputOutput(inputStream, out, userInput, EMPTY_ARGS);
        List<String> expected1 = Arrays.asList("ange@mit.edu", "helenwh@mit.edu", "cynthiaz@mit.edu");
        List<String> expected2 = Arrays.asList("helenwh@mit.edu", "cynthiaz@mit.edu");
        List<String> expected3 = Arrays.asList("helenwh@mit.edu", "cynthiaz@mit.edu");
        checkCorrectOutput(Arrays.asList(expected1, expected2, expected3), outputStream.toString());
    }
    
    // covers basic list expressions: difference, definition, sequences
    @Test
    public void testDifferenceDefinitionSequences() {
        String input1 = "6.031 = ange@mit.edu, helenwh@mit.edu, cynthiaz@mit.edu; 6.036 = helenwh@mit.edu, cynthiaz@mit.edu";
        String input2 = "6.031!6.036";
        String input3 = "6.031, jellee@mit.edu";
        String input = input1 + "\n" + input2 + "\n" + input3;
        byte[] inputBytes = input.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);
        UserInput userInput = new UserInput();
        Main.inputOutput(inputStream, out, userInput, EMPTY_ARGS);
        List<String> expected1 = Arrays.asList("helenwh@mit.edu", "cynthiaz@mit.edu");
        List<String> expected2 = Arrays.asList("ange@mit.edu");
        List<String> expected3 = Arrays.asList("ange@mit.edu", "helenwh@mit.edu", "cynthiaz@mit.edu", "jellee@mit.edu");
        checkCorrectOutput(Arrays.asList(expected1, expected2, expected3), outputStream.toString());
    }

    // covers save (0 list definitions)
    @Test
    public void testSaveEmpty() throws IOException {
        String fileName = FILES_PATH + "saveEmpty.txt";
        String input = "!save " + fileName;
        byte[] inputBytes = input.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);
        UserInput userInput = new UserInput();
        Main.inputOutput(inputStream, out, userInput, EMPTY_ARGS);
        assertTrue("new file saved", new File(fileName).exists());
    }
    
    // covers save (1 list definition)
    @Test
    public void testSaveOneDefinition() throws IOException {
        String fileName = FILES_PATH + "saveOneDefinition.txt";
        String input1 = "6.031 = ange@mit.edu, helenwh@mit.edu, cynthiaz@mit.edu";
        String input2 = "!save " + fileName;
        String input = input1 + "\n" + input2;
        byte[] inputBytes = input.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);
        UserInput userInput = new UserInput();
        Main.inputOutput(inputStream, out, userInput, EMPTY_ARGS);
        assertTrue("new file saved", new File(fileName).exists());
    }

    // covers save (>1 list definitions)
    @Test
    public void testSaveMultipleDefinitions() throws IOException {
        String fileName = FILES_PATH + "saveMultipleDefinitions.txt";
        String input1 = "fruits = banana@yellow, strawberry@red";
        String input2 = "vegetables = sweet@potato, kale@superfood";
        String input3 = "korean = bibim@bap, kim@bap";
        String input4 = "food = fruits, vegetables, korean";
        String input5 = "!save " + fileName;
        String input = input1 + "\n" + input2 + "\n" + input3 + "\n" + input4 + "\n" + input5;
        byte[] inputBytes = input.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);
        UserInput userInput = new UserInput();
        Main.inputOutput(inputStream, out, userInput, EMPTY_ARGS);
        assertTrue("new file saved", new File(fileName).exists());
    }

    // covers save (invalid filename)
    @Test
    public void testSaveInvalidFilename() throws IOException {
        String fileName = FILES_PATH + "nonExistingDirectory/shouldNotBeSaved.txt";
        String input1 = "fruits = banana@yellow, strawberry@red";
        String input2 = "!save " + fileName;
        String input = input1 + "\n" + input2;
        byte[] inputBytes = input.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);
        UserInput userInput = new UserInput();
        Main.inputOutput(inputStream, out, userInput, EMPTY_ARGS);
        assertTrue("new file not saved", !(new File(fileName).exists()));
    }
    
    // Tests for load

    // covers load (0 list definitions)
    @Test
    public void testLoadEmpty() throws IOException {
        String fileName = FILES_PATH + "loadEmpty.txt";
        // empty file
        String input = "!load " + fileName;
        byte[] inputBytes = input.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);
        UserInput userInput = new UserInput();
        Main.inputOutput(inputStream, out, userInput, EMPTY_ARGS);
        assertEquals("no definitions loaded", "", userInput.getParsableDefinedMailingLists());
    }

    // covers load (0 list definitions)
    @Test
    public void testLoadUnion() throws IOException {
        String fileName = FILES_PATH + "loadUnion.txt";
        //ange@mit.edu, helenwh@mit.edu, cynthiaz@mit.edu
        String input = "!load " + fileName;
        byte[] inputBytes = input.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);
        UserInput userInput = new UserInput();
        Main.inputOutput(inputStream, out, userInput, EMPTY_ARGS);
        assertEquals("no definitions loaded", "", userInput.getParsableDefinedMailingLists());
    }    
    
    // covers load (1 list definition)
    @Test
    public void testLoadOneDefinition() throws IOException {
        String fileName = FILES_PATH + "loadOneDefinition.txt";
        // file contents:
        // 6.031 = ange@mit.edu, helenwh@mit.edu, cynthiaz@mit.edu
        String input1 = "!load " + fileName;
        String input2 = "6.031";
        String input = input1 + "\n" + input2;
        byte[] inputBytes = input.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);
        UserInput userInput = new UserInput();
        Main.inputOutput(inputStream, out, userInput, EMPTY_ARGS);
        List<String> expected = Arrays.asList("ange@mit.edu", "helenwh@mit.edu", "cynthiaz@mit.edu");
        String[] outputs = outputStream.toString().split("> ");
        checkCorrectRecipients(expected, outputs[outputs.length-1]); // check for last output
    }

    // covers load (>1 list definitions)
    @Test
    public void testLoadMultipleDefinitions() throws IOException {
        String fileName = FILES_PATH + "loadMultipleDefinitions.txt";
//        file contents:
//        fruits = banana@yellow, strawberry@red; 
//        vegetables = sweet@potato, kale@superfood; 
//        korean = bibim@bap, kim@bap; 
//        food = fruits, vegetables, korean
        String input1 = "!load " + fileName;
        String input2 = "fruits";
        String input3 = "vegetables";
        String input4 = "korean";
        String input5 = "food";
        String input = input1 + "\n" + input2 + "\n" + input3 + "\n" + input4 + "\n" + input5;
        byte[] inputBytes = input.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);
        UserInput userInput = new UserInput();
        Main.inputOutput(inputStream, out, userInput, EMPTY_ARGS);
        List<String> expected1 = Arrays.asList("banana@yellow", "strawberry@red");
        List<String> expected2 = Arrays.asList("sweet@potato", "kale@superfood");
        List<String> expected3 = Arrays.asList("bibim@bap", "kim@bap");
        List<String> expected4 = Arrays.asList("banana@yellow", "strawberry@red", 
                "sweet@potato", "kale@superfood", "bibim@bap", "kim@bap");
        String[] outputs = outputStream.toString().split("> ");
        List<List<String>> expectedOutputs = Arrays.asList(expected1, expected2, expected3, expected4);
        // starts from 2nd output because 1st output was empty and 2nd output is !saved output
        for (int i = 2; i < outputs.length; i++) {
            checkCorrectRecipients(expectedOutputs.get(i-2), outputs[i]);
        }
        assertEquals(outputs.length-2, expectedOutputs.size());
    }

    // covers load (replaces an already-existing definition)
    @Test
    public void testLoadReplaceDefinition() throws IOException {
        String fileName = FILES_PATH + "loadToReplace.txt";
//        file contents:
//        daysUntilSummerBreak = 12@days, 6@hours, 40@minutes
        String input1 = "daysUntilSummerBreak = 13@days, 6@hours, 29@minutes";
        String input2 = "!load " + fileName;
        String input3 = "daysUntilSummerBreak";
        String input = input1 + "\n" + input2 + "\n" + input3;
        byte[] inputBytes = input.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);
        UserInput userInput = new UserInput();
        Main.inputOutput(inputStream, out, userInput, EMPTY_ARGS);
        List<String> expected = Arrays.asList("12@days", "6@hours", "40@minutes");
        String[] outputs = outputStream.toString().split("> ");
        checkCorrectRecipients(expected, outputs[outputs.length-1]);
    }

    // covers load (invalid list expression: invalid char)
    @Test
    public void testLoadInvalidChar() throws IOException {
        String fileName = FILES_PATH + "loadInvalidListExpression.txt";
        // file contents:
        // I am invalid :(
        String input = "!load " + fileName;
        byte[] inputBytes = input.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);
        UserInput userInput = new UserInput();
        Main.inputOutput(inputStream, out, userInput, EMPTY_ARGS);
        assertEquals("no definitions loaded", "", userInput.getParsableDefinedMailingLists());
    }

    // covers load (invalid list expression: mail loops)
    @Test
    public void testLoadMailLoops() throws IOException {
        String fileName = FILES_PATH + "loadMailLoops.txt";
        // file contents:
        // a = b; b = a
        String input = "!load " + fileName;
        byte[] inputBytes = input.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);
        UserInput userInput = new UserInput();
        Main.inputOutput(inputStream, out, userInput, EMPTY_ARGS);
        assertEquals("no definitions loaded", "", userInput.getParsableDefinedMailingLists());
    }
    
    // covers load (invalid filename)
    @Test
    public void testLoadInvalidFilename() throws IOException {
        String fileName = FILES_PATH + "doesNotExist.txt";
        String input = "!load " + fileName;
        byte[] inputBytes = input.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);
        UserInput userInput = new UserInput();
        Main.inputOutput(inputStream, out, userInput, EMPTY_ARGS);
        assertEquals("no definitions loaded", "", userInput.getParsableDefinedMailingLists());
    }

    // covers save and load
    @Test
    public void testSaveLoad() throws IOException {
        String fileName = FILES_PATH + "saveThenLoad.txt";
        String input1 = "fruits = banana@yellow, strawberry@red";
        String input2 = "vegetables = sweet@potato, kale@superfood";
        String input3 = "!save " + fileName;
        String input4 = "fruits = none@none";
        String input5 = "!load " + fileName;
        String input6 = "fruits";
        String input = input1 + "\n" + input2 + "\n" + input3 + "\n" + input4 + "\n" + input5 + "\n" + input6;
        byte[] inputBytes = input.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);
        UserInput userInput = new UserInput();
        Main.inputOutput(inputStream, out, userInput, EMPTY_ARGS);
        assertTrue("new file saved", new File(fileName).exists());
        String[] outputs = outputStream.toString().split("> ");
        checkCorrectRecipients(Arrays.asList("banana@yellow", "strawberry@red"), outputs[outputs.length-1]);
    }

    //---------------------------------------------------------------------------------  
    
    // Manual tests for INPUTS TO COMMAND LINE (args)
    //      (Light testing since main() only acts as a tiny wrapper around inputOutput(), 
    //      which has been tested above)
    //      (STATUS denotes whether the test has passed or failed)

    // covers   invalid inputs: fileNames does not exist
    // INPUTS (to command line):
    //      java -cp bin:lib/parserlib.jar norn.Main test/doesNotExist
    // EXPECTED OUTPUT: 
    //      --error message--
    // STATUS: passed

    // covers   valid inputs: fileNames.length = 1
    // INPUTS (to command line):
    //      java -cp bin:lib/parserlib.jar norn.Main test/1
    //      > one, two
    // EXPECTED OUTPUT: 
    //      1@1, 2@2
    // STATUS: passed

    // covers   valid inputs: fileNames.length > 1
    // INPUTS (to command line):
    //      java -cp bin:lib/parserlib.jar norn.Main test/loadOneDefinition.txt 
    //                                               test/loadMultipleDefinitions.txt
    //      > 6.031 ! cynthiaz@mit.edu
    //      > food * fruits
    // EXPECTED OUTPUT: 
    //      ange@mit.edu, helenwh@mit.edu
    //      banana@yellow, strawberry@red
    // STATUS: passed

    
    // Manual tests for INPUTS TO CONSOLE
    //      (Light testing since main() only acts as a tiny wrapper around inputOutput(), 
    //      which has been tested above)
    //      (STATUS denotes whether the test has passed or failed)

    // covers   invalid list expressions (invalid chars, mail loops)
    // INPUTS (to console):
    //      > %
    //      > a=b;b=a@a
    //      > b=a;
    // EXPECTED OUTPUT: 
    //      --unable to parse error message--
    //      a@a
    //      --mail loop error message--
    // STATUS: passed
    
    // covers   basic list expressions (email, definition, difference, sequences)
    //          save (>1 list definitions)
    //          save (invalid filename)
    // INPUTS (to console):
    //      > hola@hola
    //      > hi = hi@hi
    //      > hello = hi, hello@hello
    //      > hello!hi
    //      > hello; bye@bye
    //      > !save test/greetings
    //      > !save test/fakeFolder/greetings
    // EXPECTED OUTPUT:
    //      hola@hola
    //      hi@hi
    //      hi@hi, hello@hello
    //      hello@hello
    //      bye@bye
    //      --saved success message, and expected new "test/greetings" file--
    //      --file writing error message--
    // STATUS: passed
    
    // covers   load (1 list definition, replaces an already-existing definition,
    //                invalid list expression, invalid filename)
    // INPUTS (to console):
    //      > daysUntilSummerBreak = infinity@time
    //      > !load test/loadToReplace.txt
    //      > daysUntilSummerBreak
    //      > !load test/invalidListExpressionMailLoops.txt
    //      > !load test/doesNotExist
    // EXPECTED OUTPUT:
    //      infinity@time
    //      --load success message--
    //      12@days, 6@hours, 40@minutes
    //      --file containing mail loops error message--
    //      --file reading error message--
    // STATUS: passed

    //---------------------------------------------------------------------------------  
    
    // Manual tests for consistency between console and web server

    // covers   basic list expressions in console and webserver (union, difference)
    // INPUTS (to console):
    //      greetings = hi@english, nihao@chinese, hola@spanish, ciao@italian
    //      notEuropeanGreetings = greetings!(hola@spanish, ciao@italian)
    // INPUTS (to browser):
    //      localhost:5021/eval/notEuropeanGreetings,ohio@japanese
    //      localhost:5021/eval/greetings
    // EXPECTED OUTPUT (from console):
    //      hi@english, nihao@chinese, hola@spanish, ciao@italian
    //      hi@english, nihao@chinese
    // EXPECTED OUTPUT (from browser):
    //      hi@english, nihao@chinese,ohio@japanese
    //      hi@english, nihao@chinese, hola@spanish, ciao@italian
    // STATUS: passed
    
    // covers   load (>1 list definitions)
    //          basic list expressions in browser (union)
    // INPUTS (to console):
    //      > !load test/loadMultipleDefinitions.txt
    // INPUTS (to browser):
    //      localhost:5021/eval/fruits,vegetables
    // EXPECTED OUTPUT (from console):
    //      --load success message--
    // EXPECTED OUTPUT (from browser):
    //      banana@yellow, strawberry@red, sweet@potato, kale@superfood
    // STATUS: passed
    
    // covers   save (>1 list definitions)
    //          basic list expressions in browser (definitions, union, intersection, sequences)
    // INPUTS (to browser):
    //      localhost:5021/eval/fruits=kiwi@green,grape@purple;cranberries@red
    //      localhost:5021/eval/purple=fruits*grape@purple
    // INPUTS (to console):
    //      > !save test/purpleFruits
    // EXPECTED OUTPUT (from browser):
    //      cranberries@red
    //      grape@purple
    // EXPECTED OUTPUT (from console):
    //      --save success message, new test/purpleFruits file created--
    // STATUS: passed
    
}