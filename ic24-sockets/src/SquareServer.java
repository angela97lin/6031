import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * SquareServer is a server that squares integers passed to it.
 * It accepts requests of the form:
 *      Request ::= Number "\n"
 *      Number ::= [0-9]+
 * and for each request, returns a reply of the form:
 *      Reply ::= (Number | "err") "\n"
 * where Number is the square of the request number, or
 * "err" indicates a malformed request.
 * 
 * TODO FIXME SquareServer can handle only one client at a time.
 */
public class SquareServer {
    
    /** Default port number where the server listens for connections. */
    public static final int SQUARE_PORT = 4949;
    
    private ServerSocket serverSocket;
    // Rep invariant: serverSocket != null
    
    /**
     * Make a SquareServer that listens for connections on port.
     * @param port port number, requires 0 <= port <= 65535
     * @throws IOException if there is an error listening on port
     */
    public SquareServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }
    
    /**
     * Run the server, listening for connections and handling them.
     * @throws IOException if the main server socket is broken
     */
    public void serve() throws IOException {
        while (true) {
            // block until a client connects
            Socket clientSocket = serverSocket.accept();
            try {
                handle(clientSocket);
            } catch (IOException ioe) {
                ioe.printStackTrace(); // but don't terminate serve()
            } finally {
                serverSocket.close();
                clientSocket.close();
            }
            
        }

    }
    
    /**
     * Handle one client connection. Returns when client disconnects.
     * @param socket socket where client is connected
     * @throws IOException if connection encounters an error
     */
    private void handle(Socket socket) throws IOException {
        System.err.println("client connected");
        
        // get the socket's input stream, and wrap converters around it
        // that convert it from a byte stream to a character stream,
        // and that buffer it so that we can read a line at a time
        InputStreamReader inputStream = new InputStreamReader(socket.getInputStream());
        BufferedReader in = new BufferedReader(inputStream);
        
        // similarly, wrap character=>bytestream converter around the
        // socket output stream, and wrap a PrintWriter around that so
        // that we have more convenient ways to write Java primitive
        // types to it.
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        try {
            // each request is a single line containing a number
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                System.err.println("request: " + line);
                try {
                    int x = Integer.valueOf(line);
                    // compute answer and send back to client
                    int y = x * x;
                    System.err.println("response:" + y);
                    out.println(y);
                } catch (NumberFormatException nfe) {
                    // complain about ill-formatted request
                    System.err.println("error");
                    out.println("error");
                }
                // important! flush our buffer so the reply is sent
                out.flush();
            }
        } finally {
            out.close();
            in.close();
        }
    }
    
    /**
     * Start a SquareServer running on the default port.
     * @param args unused
     */
    public static void main(String[] args) {
        try {
            SquareServer server = new SquareServer(SQUARE_PORT);
            server.serve();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
