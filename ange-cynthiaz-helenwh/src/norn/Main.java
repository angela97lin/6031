package norn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lib6005.parser.UnableToParseException;

/**
 * Console interface to the list expression system.
 */
public class Main {

    /*
     * Thread safety for system started by main():
     *      - all fields are private constants, and are thus safe via immutability
     *      - web server and console only share a UserInput object,
     *        which is thread-safe.
     *      - inputOutput is only called from the main thread, and thus
     *        is safe via confinement (only accessed by one thread)
     *      - WebServer, used in main(), is a thread-safe object.
     *      - handleSave() and handleLoad() only interact with UserInput and Strings, which
     *        are thread-safe objects.
     */

    /*
     * Port where web server is listening, as specified by 6.031 specs.
     */
    private static final int SERVER_PORT = 5021;
    
    private static final String LOAD_PREFIX = "!load ";
    private static final String FILENAME = "([^\\s]+)";
    private static final String LOAD_COMMAND = LOAD_PREFIX + FILENAME;
    private static final String SAVE_PREFIX = "!save ";
    private static final String SAVE_COMMAND = SAVE_PREFIX + FILENAME;    

    private static final String SAVE_SYNTAX_ERROR_MESSAGE = "usage: !save must be followed by a valid file name";
    private static final String LOAD_SYNTAX_ERROR_MESSAGE = "usage: !load must be followed by a valid file name";

    /**
     * Main method.
     * Starts the web server on port 5021.
     * Accepts any number of filenames as command-line arguments, and then
     * loads them in the order given before displaying its first prompt.
     * Reads expression and command inputs from the console and outputs results.
     * !save --fileName-- and !load --fileName-- commands save and load 
     * all currently-defined named lists to the user-provided filename.
     * Prints a success message or an error message upon loading or saving,
     * as specified in Commands.load() and Commands.save()
     * Prints an error message if mail loops are encountered.
     * Prints an error message if the input entered cannot be parsed.
     * An empty input terminates the program.
     * @param args an array representing the files to load 
     * @throws IOException if there is an error reading the input
     */
    public static void main(String[] args) throws IOException {
        String[] fileNames = Arrays.copyOf(args, args.length);
        // web server users and console user share same UserInput instance
        final UserInput userInput = new UserInput(); 
        //create new web server
        final WebServer server = new WebServer(SERVER_PORT, userInput);
        server.start();
        inputOutput(System.in, System.out, userInput, fileNames);
        //end web server after exiting program via empty input
        server.stop();
    }

    /**
     * Handles user inputs to the console interface, as specified in Main.main()
     * @param inputStream the stream to read user inputs from
     * @param out the stream to print responses to 
     * @param userInput the current input system which manages currently defined mailing lists
     * @param fileNames an array of files to load before displaying the first prompt
     */
    static void inputOutput(InputStream inputStream, PrintStream out, 
                                   UserInput userInput, String[] fileNames) {
        final BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        
        //load all command-line argument filenames into environment
        for (final String fileName : fileNames){
            final String loadOutput = Commands.load(fileName, userInput); 
            System.out.println(loadOutput);
        }
        while (true) {
            out.print("> ");
            String input;
            try {
                input = in.readLine();
                if (input == null) return; // reached end of stream
            } catch (IOException e) {
                System.out.println(e.getClass().getName() + ": " + e.getMessage());
                return;
            } if (input.isEmpty()) {
                return; // exits the program on empty input
            }
            try {
                if (input.startsWith(LOAD_PREFIX)) {
                    final String loadOutput = handleLoad(input, userInput);
                    out.println(loadOutput);
                } else if (input.startsWith(SAVE_PREFIX)) {
                    final String saveOutput = handleSave(input, userInput);
                    out.println(saveOutput);                
                } else {
                        final String output = userInput.readInput(input);
                        out.println(output);
                }
            } catch (UnableToParseException pe) {
                out.println(pe.getClass().getName() + ": " + pe.getMessage());
            } catch (AssertionError ae) {
                out.println(ae.getClass().getName() + ": " + ae.getMessage());
            } catch (RuntimeException re) {
                out.println(re.getClass().getName() + ": " + re.getMessage());
            }
        }
    }

    /**
     * Handles !load commands.
     * @param input the user input to the console
     * @param userInput the current input system which manages defined mailing lists
     * @return a human-readable message informing the user of the currently-defined named lists 
     * loaded from their load command, as a single valid list expression, 
     * or a failure message as specified in Commands.load()
     */
    private static String handleLoad(final String input, UserInput userInput) {
        final Matcher commandMatcher = Pattern.compile(LOAD_COMMAND).matcher(input);
        if (!commandMatcher.matches()) {
            return LOAD_SYNTAX_ERROR_MESSAGE;
        }
        final String fileName = commandMatcher.group(1);
        final String output = Commands.load(fileName, userInput);
        return output;

    }

    /**
     * Handles !save commands.
     * @param input the user input to the console
     * @param userInput the current input system which manages currently defined mailing lists
     * @return a human-readable message informing the user of the success or failure of
     * their save command, as specified in Commands.save()
     */
    private static String handleSave(final String input, UserInput userInput) {
        final Matcher commandMatcher = Pattern.compile(SAVE_COMMAND).matcher(input);
        if (!commandMatcher.matches()) {
            return SAVE_SYNTAX_ERROR_MESSAGE;
        } 
        final String fileName = commandMatcher.group(1);
        final String definedMailingLists = userInput.getParsableDefinedMailingLists();
        final String output = Commands.save(fileName, definedMailingLists);
        return output;

    }


}
