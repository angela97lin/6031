/* Copyright (c) 2015-2017 MIT 6.005/6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package norn;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests for the static methods of Commands.
 */
public class CommandsTest { 
    
    // Testing strategy for Commands.java
    
    // Partitions for save(String fileName, String definedMailingLists) -> String result
    //      Invalid inputs:
    //          fileName refers to a folder that doesn't exist
    //          fileName contains newlines
    //      Valid inputs:
    //          fileName.length() = 1, > 1
    //          fileName exists, doesn't exist
    //          definedMailingLists is a sequence of 0, 1, > 1 list definitions

    // Partitions for load(String fileName) -> String result
    //      Invalid inputs:
    //          fileName refers to a file that doesn't exist
    //          file does not contain a valid list expression
    //      Valid inputs:
    //          fileName.length() = 1, > 1
    //          result is a sequence of 0, 1, > 1 list definitions
    //          file contains 0, 1, >1 lines
   
    //---------------------------------------------------------------------------------

    private static final String FILES_PATH = "test/";
    private static final String NONEXISTING_FILES_PATH = "test/fakeDirectory/";
    
    /**
     * Checks to see if a file has the expected contents
     * @param fileName name of file, file should not contain newlines
     * @param expected contents of file
     * @return true of expected contents equals the contents of file
     * @throws IOException 
     */
    private boolean matchFileContents(String fileName, String expected) throws IOException {
        File file = new File(fileName);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String output;
        try {
            output = bufferedReader.readLine();
            if (bufferedReader.readLine() != null) return false; // file shouldn't contain newlines
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            bufferedReader.close();
            fileReader.close();
        }
        if (output == null) output = "";
        return output.replaceAll("\\s", "").equals(expected.replaceAll("\\s", ""));
    }
    
    //---------------------------------------------------------------------------------
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    //---------------------------------------------------------------------------------

    // Tests for save() 

    // covers   invalid inputs: fileName refers to a folder that doesn't exist
    @Test
    public void testSaveInvalidFolder() throws IOException {
        final String fileName = NONEXISTING_FILES_PATH + "ohno.txt";
        final String result = Commands.save(fileName, "");
        assertEquals("expected error message", Commands.WRITE_ERROR_MESSAGE, result);
    }

    // covers   valid inputs: 
    //          fileName.length() = 1
    //          fileName doesn't exist
    //          result is a sequence of 0 list definitions
    @Test
    public void testSaveFileNameLengthOneEmptyString() throws IOException {
        final String fileName = FILES_PATH + "a";
        final String result = Commands.save(fileName, "");
        assertEquals("expected success message", "Successfully saved to "+fileName, result);
        assertTrue("expected contents of saved file", matchFileContents(fileName, ""));
    }
    
    // covers   valid inputs: 
    //          fileName.length() > 1
    //          fileName doesn't exist
    //          result is a sequence of 1 list definition
    @Test
    public void testSaveOneDefinition() throws IOException {
        final String fileName = FILES_PATH + "yas?yas!yas.";
        final String definedMailingLists = "6.031 = me@mit.edu, you@mit.edu";
        final String result = Commands.save(fileName, definedMailingLists);
        assertEquals("expected success message", "Successfully saved to "+fileName, result);
        assertTrue("expected contents of saved file", matchFileContents(fileName, definedMailingLists));
    }
    
    // covers   valid inputs: 
    //          fileName.length() > 1
    //          fileName exists
    //          result is a sequence of > 1 list definitions
    @Test
    public void testSaveExisting() throws IOException {
        final String fileName = FILES_PATH + "existing.txt";
        final String definedMailingLists = "6.036 = helen@mit.edu, cynthia@mit.edu;"
                + "6.031 = angela@mit.edu, helen@mit.edu, cynthia@mit.edu;"
                + "6.004 = angela@mit.edu";
        final String result = Commands.save(fileName, definedMailingLists);
        assertEquals("expected success message", "Successfully saved to "+fileName, result);
        assertTrue("expected contents of saved file", matchFileContents(fileName, definedMailingLists));
    }
    
    // covers invalid input, fileName has newlines
    @Test
    public void testSaveFileNameWithNewLine() throws IOException {
        final String fileName = FILES_PATH + "\n";
        final String definedMailingLists = "6.031 = me@mit.edu, you@mit.edu";
        final String result = Commands.save(fileName, definedMailingLists);
        assertEquals("expected error message", Commands.NEWLINE_ERROR_MESSAGE, result);
    }
    
    
    //---------------------------------------------------------------------------------

    // Tests for load() 
    
    // covers   invalid inputs: fileName refers to a file that doesn't exist
    @Test
    public void testLoadNonExistingFile() throws IOException {
        final String fileName = FILES_PATH + "doesNotExist.txt";
        final String result = Commands.load(fileName, new UserInput());
        assertEquals("expected error message", Commands.READ_ERROR_MESSAGE, result);
    }

    // covers   invalid inputs: file does not contain a valid list expression
    @Test
    public void testLoadInvalidListExpressionMailLoops() throws IOException {
        final String fileName = FILES_PATH + "invalidListExpressionMailLoops.txt";
        final String result = Commands.load(fileName, new UserInput());
        assertEquals("expected error message", Commands.ERROR_MESSAGE_LOAD_MAIL_LOOPS, result);
    }
    
    // covers invalid input : filename has new line character
    @Test
    public void testLoadInvalidFileNameWithNewLines() throws IOException {
        final String fileName = FILES_PATH + "\n.txt";
        final String result = Commands.load(fileName, new UserInput());
        assertEquals("expected error message", Commands.NEWLINE_ERROR_MESSAGE, result);

    }
    
    // covers   valid inputs:
    //          fileName.length() > 1
    //          result is a sequence of 0 list definitions
    //          file contains 0 lines
    @Test
    public void testLoadEmptyFile() throws IOException {
        final String fileName = FILES_PATH + "empty.txt";
        final String result = Commands.load(fileName, new UserInput());
        assertEquals("expected empty", "", result);
    }
    
    // covers   valid inputs:
    //          fileName.length() > 1
    //          result is a sequence of 0 list definitions
    //          file contains 1 line
    @Test
    public void testLoadFileWithOneSpace() throws IOException {
        final String fileName = FILES_PATH + "space.txt";
        final String result = Commands.load(fileName, new UserInput());
        assertEquals("expected result", "", result.replaceAll("\\s+", ""));
    }
    
    // covers   valid inputs:
    //          fileName.length() = 1
    //          result is a sequence of 1 list definitions
    //          file contains 1 line
    @Test
    public void testLoadOneDefinition() throws IOException {
        final String fileName = FILES_PATH + "1.txt";
        final String result = Commands.load(fileName, new UserInput());
        final String expected = "food = apple@fruits, spam@musubi, xiaolong@bao";
        assertEquals("expected result", expected.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
    }
    
    // covers   valid inputs:
    //          fileName.length() > 1
    //          result is a sequence of 2 list definitions
    //          file contains >1 lines
    @Test
    public void testLoadListExpressionNewLines() throws IOException {
        final String fileName = FILES_PATH + "listExpressionNewLines.txt";
        // file contents:
        // a = apple@banana;
        // b = a, cherry@dragonfruit
        final String result = Commands.load(fileName, new UserInput());
        final String expected = "a = apple@banana; b = a, cherry@dragonfruit";
        assertEquals("expected result", expected.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
    }
    
    // covers   valid inputs:
    //          fileName.length() > 1
    //          result is a sequence of > 1 list definitions
    @Test
    public void testLoadManyDefinitions() throws IOException {
        final String fileName = FILES_PATH + "fruitsvegetablesdessert";
        final String result = Commands.load(fileName, new UserInput());
        final String expected = "fruits = water@melon, straw@berry, blue@berry;"
                + "vegetables = sweet@potato, spring@onion;"
                + "dessert = cheese@cake, ice@cream";
        assertEquals("expected result", expected.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
    }
    
}