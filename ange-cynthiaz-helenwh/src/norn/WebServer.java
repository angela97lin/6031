package norn;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import lib6005.parser.UnableToParseException;

/**
 * A thread-safe web server that handles list expressions.
 */

public class WebServer {
    
    // AF(server, server_port) = an HTTPServer on server_port that evaluates 
    //                           mailing list expressions on the handle /eval/
    
    // Rep Invariant: 
    //    1 <= server_port <= 65535

    // Safety from rep exposure:
    //      - all fields are private and final
    //      - server_port is an int type, which is immutable
    //      - server is created in the constructor, and never returned
    //        server can only be accessed by instance methods (i.e. start(), stop())  
    
    // Thread Safety Argument:
    //      UserInput is being mutated in evaluate(), but it is threadsafe
    //      WebServer is thread-safe via the monitor pattern.
    //      server_port is thread-safe via immutability, and accesses to server,
    //      are only done in this class and thread-safe via the monitor pattern.
    
    private final HttpServer server;
    private final int server_port;
    
    /**
     * Creates a new web server to handle requests.
     * @param port the port to create the server on
     * @param userInput the environment of user-defined mailing lists
     * @throws IOException when there is an error creating the server
     */
    public WebServer(int port, UserInput userInput) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 5);
        server_port = port;
        server.createContext("/eval/", new HttpHandler() {
            public void handle(HttpExchange exchange) throws IOException {
                evaluate(exchange, userInput);
            }
        });
    }
    
    /**
     * Starts running the server.
     */
    synchronized void start() {
        System.err.println("Server is listening on http://localhost:" + server_port);
        server.start();
    }
    
    /**
     * Retrieves the port of our server.
     * @return the port of the server
     */
    synchronized int port() {
        return server.getAddress().getPort();
    }
    
    /**
     * Stops running a server.
     */
    synchronized void stop() {
        server.stop(0);
    }
    
    /**
     * Evaluate a /eval/<list-expression> request.
     * 
     * If <list-expression> is a valid list expression, then returns an 
     * HTML response to the client that, when viewed in a web browser, 
     * provides two user interface features:
     * 
     * displays the set of recipients represented by list-expression, so that 
     * the user can copy and paste them
     * presents a mailto: hyperlink containing those recipients, so that the 
     * user can click on it and start an email message if their web browser 
     * is configured to handle mailto: links
     * 
     * If <list-expression> is not a valid list expression, then returns a 
     * 404 error response to the HTTP client.  
     * 
     * @param exchange HTTP request/response. Modified by this method to send
     * @param userInput the input system shared between the console user and all
     * port 5021 web users
     * @return a response to the client and close the exchange.
     */
    private synchronized static void evaluate(final HttpExchange exchange, UserInput userInput) throws IOException {
        // if you want to know the requested path:
        final String path = exchange.getRequestURI().toString();
        // it will always start with the base path from server.createContext():
        final String base = exchange.getHttpContext().getPath();
        assert path.startsWith(base);
        
        final String toParse = path.substring(base.length());
        try { // listExpression is valid
            final String emailRecipientsString = userInput.readInput(toParse);
            
            String htmlOutput = "<a href=\"mailto:" 
                    + emailRecipientsString 
                    + "\">email these recipients</a><br>" 
                    + emailRecipientsString;
            
            // respond with HTTP code 200 to indicate success
            // response length 0 indicates a response will be written
            // you must call this method before calling getResponseBody()
            exchange.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");
            exchange.sendResponseHeaders(200, 0);
            
            // write the response to the output stream
            PrintWriter out = new PrintWriter(exchange.getResponseBody(), true);
            out.println(htmlOutput);
            
        } catch (UnableToParseException e) { // listExpression is invalid
            // respond with HTTP code 404 to indicate an error
            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
            exchange.sendResponseHeaders(200, 0);
            PrintWriter err = new PrintWriter(exchange.getResponseBody(), true);
            err.println("ERROR: Invalid list expression");            
        } catch (AssertionError e) { // listExpression contains invalid mail loops
            // respond with HTTP code 404 to indicate an error
            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
            exchange.sendResponseHeaders(200, 0);
            PrintWriter err = new PrintWriter(exchange.getResponseBody(), true);
            err.println("ERROR: Mail loops detected");            
        } 
        // if you do not close the exchange, the response will not be sent!
        exchange.close();
    }
    
} 
