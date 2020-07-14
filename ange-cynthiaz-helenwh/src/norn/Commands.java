package norn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import lib6005.parser.UnableToParseException;

/**
 * String-based console commands provided for the mailing list expression system.
 */

public class Commands{
    
    /*
     * Thread Safety Argument:
     *      - Commands is threadsafe by immutability:
     *          - The fields are final immutable String objects, and thus, cannot be 
     *            mutated by clients. Therefore, they are thread-safe via immutability.
     *          - load method takes in an UserInput instance, which is thread-safe,
     *            while save method takes in an immutable String object. Thus, 
     *            both static methods are thread-safe.
     */
    
    /** Messages **/
    public static final String ERROR_MESSAGE_LOAD_INVALID_EXPRESSION = "ERROR: File contains invalid list expression";
    public static final String ERROR_MESSAGE_LOAD_MAIL_LOOPS = "ERROR: File contains invalid mail loops";
    public static final String NEWLINE_ERROR_MESSAGE = "ERROR: Filename contains newlines.";
    public static final String READ_ERROR_MESSAGE = "ERROR: File cannot be opened for reading";
    public static final String WRITE_ERROR_MESSAGE = "ERROR: File cannot be opened for writing";
    public static final String SUCCESS_TEMPLATE_MESSAGE = "Successfully saved to ";

    /**
     * Saves all currently-defined named lists to a user-provided filename.
     * The contents of the file will be a single valid list expression – specifically, 
     * a sequence of list definitions.
     * @param fileName name of file, cannot be empty and cannot contain newline "\n"
     * @param definedMailingLists currently-defined named lists as a single valid list expression
     * @return "Successfully saved to fileName" if successfully saved definedMailingLists to fileName, 
     * or WRITE_ERROR_MESSAGE if filename cannot be opened for writing 
     * (e.g. because the name contains illegal characters or refers to a folder that doesn’t exist)
     * or NEWLINE_ERROR_MESSAGE if filename contains invalid newline characters
     */
    static String save(String fileName, String definedMailingLists) {
        if (fileName.contains("\n")){
            return NEWLINE_ERROR_MESSAGE;
        }
        try {
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");
            writer.print(definedMailingLists);                
            writer.close();
        } catch (IOException e) {
            return WRITE_ERROR_MESSAGE;
        } 
        return SUCCESS_TEMPLATE_MESSAGE + fileName;
    }
    
    /**
     * Loads all currently-defined named lists from a user-provided filename.
     * @param fileName name of file, cannot be empty and cannot contain newline "\n"
     * @param userInput the current input system which manages defined mailing lists to load to
     * @return currently-defined named lists as a single valid list expression,
     * or READ_ERROR_MESSAGE if filename cannot be opened 
     * (e.g. because the file doesn’t exist) 
     * or ERROR_MESSAGE_LOAD_INVALID_EXPRESSION if the file does not 
     * contain a valid list expression
     * or ERROR_MESSAGE_LOAD_MAIL_LOOPS if the file contains a mail loop
     * or NEWLINE_ERROR_MESSAGE if filename contains invalid newline characters
     */
    static String load(String fileName, UserInput userInput) {
        if (fileName.contains("\n")){
            return NEWLINE_ERROR_MESSAGE;
        }
        String output = "";
        File file = new File(fileName);
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);
            String contents;
            while ((contents = reader.readLine()) != null) {
                output += contents;
            }
            //clean up by closing streams
            reader.close();
            fileReader.close();
        }
        catch (IOException e){
            return READ_ERROR_MESSAGE;
        } 
        // check if list expression is parseable
        try {
            userInput.readInput(output);
        } catch (UnableToParseException e) {
            return ERROR_MESSAGE_LOAD_INVALID_EXPRESSION;
        } catch (AssertionError e) {
            return ERROR_MESSAGE_LOAD_MAIL_LOOPS;
        }
        return output;
    }

}