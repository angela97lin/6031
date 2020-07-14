package norn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

/*
 * Test the WebServer class
 */
public class WebServerTest {
    private static final int SERVER_PORT = 5021;
    
    /*
     * Testing Strategy for the Web Server:
     * 
     * ports: 
     *  -test the given port (5021)
     *  
     * URLs:
     *  -test valid URLs (/eval/...)
     *  -test invalid URLs (different context, wrong port, etc.)
     *  -test multiple URLs evaluate correctly
     *  
     * Mailing Lists:
     *  -test that after server is started, mailing list definitions are updated for
     *      -same URL
     *      -multiple URLs
     *  -test other basic mailing list operations
     *      -union, intersection, difference, sequence
     *      -list reassignment
     * 
     * Error Messages:
     *  -test that correct error messages are returned for
     *      -unparsable inputs
     *      -mail loops
     *  -test correct ouputs after error messages are returned
     *  
     * Thread Safety:
     *      -test multiple threads
     *      -Outcomes are nondeterministic depending on thread order, so check that 
     *      -correct outcomes for the deterministic list expressions
     *      -all URLS are evaluated
     *      
     *      and manually check that other values produced are reasonable (agree with
     *          definedMailingList specs)
     */
    
    //TODO: whats a reasonable number of "threads" allowed to connect to the server?
    
    /*
     * Some manual tests:
     *  -check that updates to the defined mailing lists are consistent between the console
     *      and the web server
     */

    // Test connecting to the given port (5021)
    // Test invalid URL (wrong context)
    @Test
    public void testInvalidURL1() throws IOException {
        UserInput userInput = new UserInput();
        final WebServer server = new WebServer(SERVER_PORT, userInput);
        server.start();

        final String invalid = "http://localhost:" + server.port() + "/???/!!!";
        final URL url = new URL(invalid);
        
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        assertEquals("response code", 404, connection.getResponseCode());
        server.stop();
    }
    
    // Test invalid URL (wrong context)
    @Test
    public void testInvalidURL2() throws IOException {
        UserInput userInput = new UserInput();
        final WebServer server = new WebServer(SERVER_PORT, userInput);
        server.start();

        final String invalid = "http://localhost:" + server.port() + "/leva/a@aa!";
        final URL url = new URL(invalid);

        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        assertEquals("response code", 404, connection.getResponseCode());
        server.stop();
    }
    
    // Test invalid URL (wrong port)
    @Test
    public void testInvalidURL3() throws IOException {
        UserInput userInput = new UserInput();
        final WebServer server = new WebServer(SERVER_PORT, userInput);
        server.start();

        final String invalid = "http://localhost:" + SERVER_PORT + "/hello/eval/a@a";
        final URL url = new URL(invalid);

        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        assertEquals("response code", 404, connection.getResponseCode());
        server.stop();
    }
    
    // Test that mailing list stored (list assignment) for the one URL
    // Test valid URL, union, sequencing
    @Test
    public void testValidURL() throws IOException {
        UserInput userInput = new UserInput();
        WebServer server = new WebServer(SERVER_PORT, userInput);
        server.start();
        final String valid = "http://localhost:" + SERVER_PORT + "/eval/bagginses=bilbo@shire,frodo@shire;bagginses";
        final URL url = new URL(valid);

        final InputStream input = url.openStream();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String expected = "<a href=\"mailto:bilbo@shire, frodo@shire\">email these recipients</a><br>bilbo@shire, frodo@shire";
        String expected2 = "<a href=\"mailto:frodo@shire, bilbo@shire\">email these recipients</a><br>frodo@shire, bilbo@shire";
        String output = reader.readLine();
        assertTrue(expected.equals(output)|expected2.equals(output));
        assertEquals("end of stream", null, reader.readLine());
        server.stop();
    }
    
    // Test multiple valid URLs
    // Test basic operations (union, intersection)
    // Test list reassignment
    @Test
    public void testManyURLOperations() throws IOException {
        UserInput userInput = new UserInput();
        final WebServer server = new WebServer(SERVER_PORT, userInput);
        server.start();

        final String valid = "http://localhost:" + server.port() + "/eval/bagginses=bilbo@shire,frodo@shire;bagginses";
        final URL url = new URL(valid);
        
        final String valid2 = "http://localhost:" + server.port() + "/eval/bagginses=bagginses*bilbo@shire";
        final URL url2 = new URL(valid2);
        // in this test, we will just assert correctness of the server's output
        final InputStream input = url.openStream();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        
        final InputStream input2 = url2.openStream();
        final BufferedReader reader2 = new BufferedReader(new InputStreamReader(input2));
        
        
        String expected = "<a href=\"mailto:bilbo@shire, frodo@shire\">email these recipients</a><br>bilbo@shire, frodo@shire";
        String expected2 = "<a href=\"mailto:frodo@shire, bilbo@shire\">email these recipients</a><br>frodo@shire, bilbo@shire";
        
        String output = reader.readLine();
        assertTrue(expected.equals(output)|expected2.equals(output));
        assertEquals("end of stream", null, reader.readLine());
        
        String expected3 = "<a href=\"mailto:bilbo@shire\">email these recipients</a><br>bilbo@shire";
        String output2 = reader2.readLine();
        assertEquals(expected3, output2);
        assertEquals("end of stream", null, reader2.readLine());
        
        server.stop();
    }
    
    // Test multiple valid URLs
    // Some other basic operations (union, difference, sequencing)
    @Test
    public void testManyURLOperations2() throws IOException {
        UserInput userInput = new UserInput();
        final WebServer server = new WebServer(SERVER_PORT, userInput);
        server.start();

        final String valid = "http://localhost:" + server.port() + "/eval/bagginses=bilbo@shire,frodo@shire;bagginses";
        final URL url = new URL(valid);
        
        final String valid2 = "http://localhost:" + server.port() + "/eval/bagginses=bagginses!frodo@shire";
        final URL url2 = new URL(valid2);
        
        final String valid3 = "http://localhost:" + server.port() + "/eval/bagginses;";
        final URL url3 = new URL(valid3);
        
        final InputStream input = url.openStream();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        
        final InputStream input2 = url2.openStream();
        final BufferedReader reader2 = new BufferedReader(new InputStreamReader(input2));
        
        final InputStream input3 = url3.openStream();
        final BufferedReader reader3 = new BufferedReader(new InputStreamReader(input3));
        
        String expected = "<a href=\"mailto:bilbo@shire, frodo@shire\">email these recipients</a><br>bilbo@shire, frodo@shire";
        String expected2 = "<a href=\"mailto:frodo@shire, bilbo@shire\">email these recipients</a><br>frodo@shire, bilbo@shire";
        
        String output = reader.readLine();
        assertTrue(expected.equals(output)|expected2.equals(output));
        assertEquals("end of stream", null, reader.readLine());
        
        String expected3 = "<a href=\"mailto:bilbo@shire\">email these recipients</a><br>bilbo@shire";
        String output2 = reader2.readLine();
        assertEquals(expected3, output2);
        assertEquals("end of stream", null, reader2.readLine());
        
        String expectedEmpty = "<a href=\"mailto:\">email these recipients</a><br>";
        String output3 = reader3.readLine();
        assertEquals(expectedEmpty, output3);
        assertEquals("end of stream", null, reader3.readLine());
        
        server.stop();
    }
    
    // Test connecting to the given port
    // Test that correct error returned for mailing loop
    @Test
    public void testMailingLoopBasic() throws IOException {
        UserInput userInput = new UserInput();
        final WebServer server = new WebServer(SERVER_PORT, userInput);
        server.start();

        final String invalid = "http://localhost:" + server.port() + "/eval/bagginses=a;a=bagginses,c";
        final URL url = new URL(invalid);

        final InputStream input = url.openStream();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        
        String expectedInvalid = "ERROR: Mail loops detected";

        String output = reader.readLine();
        assertEquals(expectedInvalid, output);
        assertEquals("end of stream", null, reader.readLine());
        
        server.stop();
    }

    // Test connecting to the given port
    // Test that correct error returned for mailing loop
    // Test that legal operations still allowed after error
    @Test
    public void testMailingLoopError() throws IOException {
        UserInput userInput = new UserInput();
        final WebServer server = new WebServer(SERVER_PORT, userInput);
        server.start();

        final String valid = "http://localhost:" + server.port() + "/eval/bagginses=a";
        final URL url = new URL(valid);
        
        final String invalid2 = "http://localhost:" + server.port() + "/eval/a=bagginses,c";
        final URL url2 = new URL(invalid2);
        
        final String valid3 = "http://localhost:" + server.port() + "/eval/a=a@a,c";
        final URL url3 = new URL(valid3);

        final InputStream input = url.openStream();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        final InputStream input2 = url2.openStream();
        final BufferedReader reader2 = new BufferedReader(new InputStreamReader(input2));

        final InputStream input3 = url3.openStream();
        final BufferedReader reader3 = new BufferedReader(new InputStreamReader(input3));

        
        String expectedinValid = "ERROR: Mail loops detected";
        String expectedValid1 = "<a href=\"mailto:\">email these recipients</a><br>";
        String expectedValid2 = "<a href=\"mailto:a@a\">email these recipients</a><br>a@a";
        
        String output = reader.readLine();
        assertEquals(expectedValid1, output);
        assertEquals("end of stream", null, reader.readLine());
        String output2 = reader2.readLine();
        assertEquals(expectedinValid,output2);
        assertEquals("end of stream", null, reader2.readLine());
        String output3 = reader3.readLine();
        assertEquals(expectedValid2,output3);
        assertEquals("end of stream", null, reader3.readLine());
        
        server.stop();
    }
    
    // Test connecting to the given port 
    // Test that correct error returned for unparsable expressions
    // Test that operation still evaluates correctly after errors
    @Test
    public void testUnableToParseErrorBasic() throws IOException, URISyntaxException {
        UserInput userInput = new UserInput();
        final WebServer server = new WebServer(SERVER_PORT, userInput);
        server.start();
        
        final String string = "http://localhost:" + server.port() + "/eval/bagginses=a@a;bagginses=a?!;bagginses";
        final URL url = new URL(string);
        
        final InputStream input = url.openStream();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        
        String expectedInvalid = "ERROR: Invalid list expression";
        
        String outputValid = reader.readLine();
        assertEquals(expectedInvalid, outputValid);
        assertEquals("end of stream", null, reader.readLine());
        
        server.stop();
    }
    
    // Test connecting to the given port 
    // Test that correct error returned for unparsable expressions
    // Test that operation still evaluates correctly after errors
    @Test
    public void testUnableToParseError() throws IOException, URISyntaxException {
        UserInput userInput = new UserInput();
        final WebServer server = new WebServer(SERVER_PORT, userInput);
        server.start();
        
        final String valid = "http://localhost:" + server.port() + "/eval/bagginses=a@a";
        final URL urlValid = new URL(valid);

        final String invalid = "http://localhost:" + server.port() + "/eval/bagginses=a?!";
        final URL url = new URL(invalid);
        
        final String valid2 = "http://localhost:" + server.port() + "/eval/bagginses";
        final URL urlValid2 = new URL(valid2);
        
        final InputStream inputValid = urlValid.openStream();
        final BufferedReader readerValid = new BufferedReader(new InputStreamReader(inputValid));
        
        final InputStream input = url.openStream();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        
        final InputStream inputValid2 = urlValid2.openStream();
        final BufferedReader readerValid2 = new BufferedReader(new InputStreamReader(inputValid2));
        
        
        String expectedValid = "<a href=\"mailto:a@a\">email these recipients</a><br>a@a";
        String expectedInvalid = "ERROR: Invalid list expression";
        
        String outputValid = readerValid.readLine();
        assertEquals(expectedValid, outputValid);
        assertEquals("end of stream", null, readerValid.readLine());
        
        String output = reader.readLine();
        assertEquals(expectedInvalid, output);
        assertEquals("end of stream", null, reader.readLine());
        
        String outputValid2 = readerValid2.readLine();
        assertEquals(expectedValid, outputValid2);
        assertEquals("end of stream", null, readerValid2.readLine());
        
        server.stop();
    }
    
    // Test connecting to the given port 
    // Test that correct error returned for unparsable expressions
    // Test that operation still evaluates correctly after errors
    @Test
    public void testUnableToParseError2() throws IOException, URISyntaxException {
        UserInput userInput = new UserInput();
        final WebServer server = new WebServer(SERVER_PORT, userInput);
        server.start();
        
        final String valid = "http://localhost:" + server.port() + "/eval/bagginses=a@a";
        final URL urlValid = new URL(valid);
        
        final String invalid2 = "http://localhost:" + server.port() + "/eval/a=bagginses,-symbolz\'";
        final URL url2 = new URL(new URI(invalid2).toASCIIString());
        
        final String valid2 = "http://localhost:" + server.port() + "/eval/bagginses";
        final URL urlValid2 = new URL(valid2);
        
        final InputStream inputValid = urlValid.openStream();
        final BufferedReader readerValid = new BufferedReader(new InputStreamReader(inputValid));

        final InputStream input2 = url2.openStream();
        final BufferedReader reader2 = new BufferedReader(new InputStreamReader(input2));
        
        final InputStream inputValid2 = urlValid2.openStream();
        final BufferedReader readerValid2 = new BufferedReader(new InputStreamReader(inputValid2));
        
        String expectedValid = "<a href=\"mailto:a@a\">email these recipients</a><br>a@a";
        String expectedInvalid = "ERROR: Invalid list expression";
        
        String outputValid = readerValid.readLine();
        assertEquals(expectedValid, outputValid);
        assertEquals("end of stream", null, readerValid.readLine());
        
        String output2 = reader2.readLine();
        assertEquals(expectedInvalid, output2);
        assertEquals("end of stream", null, reader2.readLine());
        
        String outputValid2 = readerValid2.readLine();
        assertEquals(expectedValid, outputValid2);
        assertEquals("end of stream", null, readerValid2.readLine());
        
        server.stop();
    }

    
    // Test connecting to the given port 
    // Test that correct error returned for unparsable expressions
    // Test that operation still evaluates correctly after errors
    @Test
    public void testUnableToParseError3() throws IOException, URISyntaxException {
        UserInput userInput = new UserInput();
        final WebServer server = new WebServer(SERVER_PORT, userInput);
        server.start();
        
        final String valid = "http://localhost:" + server.port() + "/eval/bagginses=a@a";
        final URL urlValid = new URL(valid);

        final String invalid3 = "http://localhost:" + server.port() + "/eval/a=(bagginses, symbolz";
        final URL url3 = new URL(invalid3);
        
        final String valid2 = "http://localhost:" + server.port() + "/eval/bagginses";
        final URL urlValid2 = new URL(valid2);
        
        final InputStream inputValid = urlValid.openStream();
        final BufferedReader readerValid = new BufferedReader(new InputStreamReader(inputValid));
        
        final InputStream input3 = url3.openStream();
        final BufferedReader reader3 = new BufferedReader(new InputStreamReader(input3));
        
        final InputStream inputValid2 = urlValid2.openStream();
        final BufferedReader readerValid2 = new BufferedReader(new InputStreamReader(inputValid2));
        
        
        String expectedValid = "<a href=\"mailto:a@a\">email these recipients</a><br>a@a";
        String expectedInvalid = "ERROR: Invalid list expression";
        
        String outputValid = readerValid.readLine();
        assertEquals(expectedValid, outputValid);
        assertEquals("end of stream", null, readerValid.readLine());
        
        String output3 = reader3.readLine();
        assertEquals(expectedInvalid, output3);
        assertEquals("end of stream", null, reader3.readLine());
        
        String outputValid2 = readerValid2.readLine();
        assertEquals(expectedValid, outputValid2);
        assertEquals("end of stream", null, readerValid2.readLine());
        
        server.stop();
    }

    
    // Test that multiple threads can connect to the server
    // Outcomes are nondeterministic depending on thread order, so check that 
    //      -correct outcomes for the deterministic list expressions
    //      -all URLS are evaluated
    @Test
    public void testMultipleThreads() throws IOException, URISyntaxException, InterruptedException {
        UserInput userInput = new UserInput();
        final WebServer server = new WebServer(SERVER_PORT, userInput);
        server.start();
        
        final String one = "http://localhost:" + server.port() + "/eval/bagginses=a@a";
        final URL url1 = new URL(one);

        final String two = "http://localhost:" + server.port() + "/eval/a=(bagginses,symbolz)";
        final URL url2 = new URL(two);
        
        final String three = "http://localhost:" + server.port() + "/eval/symbolz=(blash@mE,HELPME@ME)!blasH@me";
        final URL url3 = new URL(three);
        
        final String four = "http://localhost:" + server.port() + "/eval/;;baggin(ses";
        final URL url4 = new URL(four);
        
        List<URL> urls = new ArrayList<>();
        urls.add(url1);
        urls.add(url2);
        urls.add(url3);
        urls.add(url4);
        
        List<String> outputs = Collections.synchronizedList(new ArrayList<String>());
        Set<Thread> threads = Collections.synchronizedSet(new HashSet<>());
        for (URL url:urls) {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    InputStream input;
                    BufferedReader reader;
                    try {
                        input = url.openStream();
                        reader = new BufferedReader(new InputStreamReader(input));
                        String output = reader.readLine();
                        outputs.add(url+" produces output:"+output);
                        assertEquals("end of stream", null, reader.readLine());
                    } catch (IOException e) {
                        System.out.println("here");
                        e.printStackTrace();
                    }
                }
            });
            threads.add(t);
            t.start();
        }
       
        waitForAllThreadsToFinish(threads);
        
        //Some outputs (url3) are nondeterministic, so print the outputs to verify correctness
        //System.out.println(outputs);
        
        String expectedOutput1 = "<a href=\"mailto:helpme@me\">email these recipients</a><br>helpme@me";
        String expectedURL1 = "http://localhost:5021/eval/symbolz=(blash@mE,HELPME@ME)";
        
        String expectedInvalidOutput = "ERROR: Invalid list expression";
        String expectedURLInvalid = "http://localhost:5021/eval/;;baggin(ses";
        
        String expectedOutput2 = "<a href=\"mailto:a@a\">email these recipients</a><br>a@a";
        String expectedURL2 = "http://localhost:5021/eval/bagginses=a@a";
        
        String expectedURL3 = "http://localhost:5021/eval/a=(bagginses,symbolz)";
        
        for (String output : outputs) {
            if (output.contains(expectedURL1)) {
                assertTrue(output.contains(expectedOutput1));
            } else if (output.contains(expectedURL2)) {
                assertTrue(output.contains(expectedOutput2));
            } else if (output.contains(expectedURLInvalid)) {
                assertTrue(output.contains(expectedInvalidOutput));
            } else {
                assertTrue(output.contains(expectedURL3));
            }
        }
        
        server.stop();
        
    }
    
    /*
     * Returns after all threads in the collection have exited.
     */
    private static void waitForAllThreadsToFinish(Collection<Thread> threads) {
        // wait for all the threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException ie) {
                throw new AssertionError("should never happen", ie);
            }
        }
    }

}

