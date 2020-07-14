/* Copyright (c) 2007-2017 MIT 6.005/6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Multi-player Minesweeper server.
 * 
 * <p>PS4 instructions: you MUST NOT change the specs of main() or runGameServer(),
 *                      or the implementation of main().
 */
public class GameServer {

    /** Default server port. */
    private static final int DEFAULT_PORT = 4444;
    /** Default board size. */
    private static final int DEFAULT_SIZE = 12;

    /** Socket for receiving client connections. */
    private final ServerSocket serverSocket;

    /** Minesweeper game board. */
    private final GameBoard gameBoard; 
    
    /** The number of clients currently connected to the server. */
    private int numberOfClientsConnected;
    
    /** Lock used to guard GameServer **/
    private final Object lock = new Object();
    
    /*
     * Abstraction function:
     *      AF(serverSocket, port, gameBoard) = Multi-player Minesweeper server that listens
     *                                          on port port, handles client 
     *                                          connections using serverSocket, and allows
     *                                          clients to play on the Minesweeper game board
     *                                          as represented by gameBoard.
     *                                               
     * Rep invariant:
     *      - clients must be an nonnegative integer (clients >= 0)
     *      
     * Safety from rep exposure:
     *      - DEFAULT_PORT and DEFAULT_SIZE are private final primitive fields and thus, 
     *        their values cannot be reassigned or mutated (immutable) by clients.
     *      - serverSocket is a private final field and is never returned from any methods. 
     *      - gameBoard is a private final field, constructor uses 
     *        defensive copying of bombBoard to initialize gameBoard, and 
     *        references to gameBoard are never returned from any methods.
     *      - numberOfClientsConnected is a private field which is never returned from any methods.
     * 
     * Thread safety for instance of GameServer:
     *      - An instance of GameServer is not necessarily thread-safe;
     *        serve(), for example, calls has the reference to and uses serverSocket in 
     *        serverSocket.accept(), which is not necessarily thread-safe
     *        (as there is no documentation of thread safety in its specs:
     *        http://download.java.net/jdk7/archive/b123/docs/api/java/net/ServerSocket.html#accept())
     *        
     * Thread safety for system started by main():
     *      - gameBoard is a thread-safe ADT (as argued in GameBoard)
     *      - accesses and mutations of numberOfClientsConnected are guarded
     *        by lock, and thus are thread-safe.
     *      - threads are confined within serve() and thus, are not exposed and
     *        cannot be mutated or reassigned by clients. 
     *      - data used in handleConnection(Socket socket) and the helper functions called
     *        are local variables (ex: BufferedReader in, Printwriter out, etc.) 
     *        and thus are thread-safe through confinement. 
    */
    
    /**
     * Check that the rep invariant holds.
     */
    private void checkRep(){
        assert (numberOfClientsConnected >= 0);
    }
    
    /**
     * Make a new game server that listens for connections on the port specified,
     * and allows clients to connect to play Minesweeper on a 
     * game board with dimensions and bomb placements as specified by the parameters given.
     * @param port port number, requires 0 <= port <= 65535
     * @param sizeX the width of the new Minesweeper game board, must be
     * a positive integer value and equal to bombBoard[0].length
     * @param sizeY the height of the new Minesweeper game board, must be
     * a positive integer value and equal to bombBoard.length
     * @param bombBoard a sizeX-by-sizeY board of '0' and '1' only,
     * where '0' represents that the square contains no bomb, and 
     * '1' represents that the square contains a bomb. 
     * bombBoard is used to determine bomb locations for the game board created for the server.
     * @throws IOException if an error occurs opening the server socket
     */
    public GameServer(final int port, final int sizeX, final int sizeY, final char[][] bombBoard) throws IOException {
        serverSocket = new ServerSocket(port);
        char[][] bombBoardCopy = new char[sizeY][sizeX];
        for (int i = 0; i < sizeY; i++){
            for (int j = 0; j < sizeX; j++){
                bombBoardCopy[i][j] = bombBoard[i][j];
            }
        }
        gameBoard = new GameBoard(sizeX, sizeY, bombBoardCopy);
        numberOfClientsConnected = 0;
        checkRep();
    }
    
    /**
     * Make a new game server that listens for connections on port specified,
     * and allows clients to connect to play Minesweeper on a 
     * game board with dimensions as specified.
     * @param port port number, requires 0 <= port <= 65535
     * @param sizeX the width of the new Minesweeper game board, must be
     * a positive integer value and equal to bombBoard[0].length
     * @param sizeY the height of the new Minesweeper game board, must be
     * a positive integer value and equal to bombBoard.length
     * @throws IOException if an error occurs opening the server socket
     */
    public GameServer(final int port, final int sizeX, final int sizeY) throws IOException {
        serverSocket = new ServerSocket(port);
        gameBoard = new GameBoard(sizeX, sizeY); 
        numberOfClientsConnected = 0;
        checkRep();
    }

    /**
     * Run the server, listening for and handling client connections.
     * Never returns, unless an exception is thrown.
     * 
     * @throws IOException if an error occurs waiting for a connection
     *                     (IOExceptions from individual clients do *not* terminate serve())
     */
    public void serve() throws IOException {
        while (true) {
            // block until a client connects
            Socket socket = serverSocket.accept();

            //create a new thread to handle client
            Thread handler = new Thread(new Runnable(){
                public void run(){
                    try {
                        synchronized (lock){
                            numberOfClientsConnected += 1;
                        }
                        try {
                            handleConnection(socket);
                        } finally {
                            socket.close();
                            synchronized (lock){
                                numberOfClientsConnected -= 1;
                            }
                        }
                    } catch (IOException ioe) {
                        ioe.printStackTrace(); // but do not stop serving
                    }
                }
            });
            
            //start thread to handle client
            handler.start();
            checkRep();
        }
    }
    
    /**
     * Handle a single client connection. Returns when client disconnects.
     * @param socket socket where the client is connected
     * @throws IOException if the connection encounters an error or terminates unexpectedly
     */
    private void handleConnection(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        try {
            final int boardWidth = gameBoard.getWidth();
            final int boardHeight = gameBoard.getHeight();
            String welcomeMessage = welcomeMessage(boardWidth, boardHeight);
            out.println(welcomeMessage);
            for (String line = in.readLine(); line != null; line = in.readLine()) {                
                String output = handleRequest(line);
                if (output != "bye") {
                    out.print(output);
                    out.flush();
                } else {
                    //terminate connection with current client
                    break;
                }
            }
        } finally {
            out.close();
            in.close();
        }
    }

    /**
     * Handler for client input, performing requested operations and returning an output message.
     * @param input message from client
     * @return message that is sent to client, or returns "bye" if client 
     * sent a BYE message, to signal termination of connection with client, as handled in handleConnection(Socket socket)
     */
    private String handleRequest(String input) {
        String regex = "(look)|(help)|(bye)|"
                + "(dig -?\\d+ -?\\d+)|(flag -?\\d+ -?\\d+)|(deflag -?\\d+ -?\\d+)";
        if (!input.matches(regex)) {
            // invalid input
            //send help message, discard incoming message
            String helpMessageString = helpMessageRequest();
            return helpMessageString;
        } 
        String[] tokens = input.split(" ");
        if (tokens[0].equals("look")) { //'look' request
            String lookString = gameBoard.look();
            return lookString;
        } else if (tokens[0].equals("help")) { //'help' request
            String helpMessageString = helpMessageRequest();
            return helpMessageString;
        } else if (tokens[0].equals("bye")) { //'bye' request
            return "bye";
        } else {
            int x = Integer.parseInt(tokens[1]);
            int y = Integer.parseInt(tokens[2]);
            if (tokens[0].equals("dig")) { // 'dig x y' request
                String digMessage = digMessageRequest(x, y);
                return digMessage;
            } else if (tokens[0].equals("flag")) { // 'flag x y' request
                String flagMessage = flagMessageRequest(x, y);
                return flagMessage;
            } else if (tokens[0].equals("deflag")) { // 'deflag x y' request
                String deFlagMessage = deFlagMessageRequest(x, y);
                return deFlagMessage;
            }
        }
        //Should never reach here; returns in every case above.
        throw new UnsupportedOperationException();
    }

    /**
     * Start a game server using the given arguments.
     * 
     * <br> Usage:
     * <pre>
     *      minesweeper.GameServer [--port PORT] [--size SIZE_X,SIZE_Y | --file FILE]
     * </pre>
     * 
     * <p>  PORT is an optional integer in the range 0 to 65535 inclusive, specifying the port the server
     *      should be listening on for incoming connections.
     * <br> E.g. "--port 1234" starts the server listening on port 1234.
     * 
     * <p>  SIZE_X and SIZE_Y are optional positive integer arguments, specifying that a random board of size
     *      SIZE_X*SIZE_Y should be generated.
     * <br> E.g. "--size 42,58" starts the server initialized with a random board of size 42 x 58.
     * 
     * <p>  FILE is an optional argument specifying a file pathname where a board has been stored. If this
     *      argument is given, the stored board should be loaded as the starting board.
     * <br> E.g. "--file boardfile.txt" starts the server initialized with the board stored in
     *      boardfile.txt.
     * 
     * <p>  The board file format, for use with the "--file" option, is specified by the following grammar:
     * <pre>
     *      FILE ::= BOARD LINE+
     *      BOARD ::= X SPACE Y NEWLINE
     *      LINE ::= (VALUE SPACE)* VALUE NEWLINE
     *      VALUE ::= "0" | "1"
     *      X ::= INT
     *      Y ::= INT
     *      SPACE ::= " "
     *      NEWLINE ::= "\n" | "\r" "\n"?
     *      INT ::= [0-9]+
     * </pre>
     *      The file must contain Y LINEs where each LINE contains X VALUEs.
     *      1 indicates a bomb, 0 indicates no bomb.
     * 
     * <p>  If neither --file nor --size is given, generate a random board of size 12 x 12.
     * 
     * <p>  Note that --file and --size may not be specified simultaneously.
     * 
     * @param args arguments as described
     */
    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        int sizeX = DEFAULT_SIZE;
        int sizeY = DEFAULT_SIZE;
        Optional<File> file = Optional.empty();

        Queue<String> arguments = new LinkedList<>(Arrays.asList(args));
        try {
            while ( ! arguments.isEmpty()) {
                String flag = arguments.remove();
                try {
                    if (flag.equals("--port")) {
                        port = Integer.parseInt(arguments.remove());
                    } else if (flag.equals("--size")) {
                        String[] sizes = arguments.remove().split(",");
                        sizeX = Integer.parseInt(sizes[0]);
                        sizeY = Integer.parseInt(sizes[1]);
                        file = Optional.empty();
                    } else if (flag.equals("--file")) {
                        sizeX = -1;
                        sizeY = -1;
                        file = Optional.of(new File(arguments.remove()));
                        if ( ! file.get().isFile()) {
                            throw new IllegalArgumentException("file not found: \"" + file.get() + "\"");
                        }
                    } else {
                        throw new IllegalArgumentException("unknown option: \"" + flag + "\"");
                    }
                } catch (NoSuchElementException nsee) {
                    throw new IllegalArgumentException("missing argument for " + flag);
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("unable to parse number for " + flag);
                }
            }
        } catch (IllegalArgumentException iae) {
            System.err.println(iae.getMessage());
            System.err.println("usage: GameServer [--port PORT] [--size SIZE_X,SIZE_Y | --file FILE]");
            return;
        }

        try {
            runGameServer(file, sizeX, sizeY, port);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    /**
     * Start a new GameServer running on the specified port, with either a random new board or a
     * board loaded from a file.
     * 
     * @param file if file.isPresent(), start with a board loaded from the specified file,
     *             according to the input file format defined in the documentation for main(..)
     * @param sizeX if (!file.isPresent()), start with a random board with width sizeX
     *              (and require sizeX > 0)
     * @param sizeY if (!file.isPresent()), start with a random board with height sizeY
     *              (and require sizeY > 0)
     * @param port the network port on which the server should listen, requires 0 <= port <= 65535
     * @throws IOException if a network error occurs
     */
    public static void runGameServer(Optional<File> file, int sizeX, int sizeY, int port) throws IOException {
        GameServer server;
        if (file.isPresent()){ //start with a board loaded from a specified file
            FileReader fileReader = new FileReader(file.get());
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            try {
                //first line should specify board size,
                //so read in first line, and parse to get sizeX and sizeY
                String line; 
                line = bufferedReader.readLine();
                String[] splitSizeParameters = line.split("\\s+");
                sizeX = Integer.parseInt(splitSizeParameters[0]);
                sizeY = Integer.parseInt(splitSizeParameters[1]);
                
                //parse the rest of the file to
                //get the values of our bombBoard,
                //which will be used to create our game board.
                char[][] bombBoard = new char[sizeY][sizeX];
               
                int row = 0;
                while ((line = bufferedReader.readLine()) != null){
                    String[] splitLine = line.split("\\s+");
                    for (int i = 0; i < splitLine.length; i++){
                        bombBoard[row][i] = splitLine[i].charAt(0);
                    }               
                    row += 1;
                }
                assert (row == sizeY);
                server = new GameServer(port, sizeX, sizeY, bombBoard);
            }
            catch (IOException e){
                throw new AssertionError("error reading line");
            } finally {
                //clean up by closing streams
                bufferedReader.close();
                fileReader.close();
            }
            
        } else { 
            //file parameter was not used/file did not exist; use size
            //parameters to create a randomized sizeX by sizeY board.
            server = new GameServer(port, sizeX, sizeY);
        }
        server.serve();
    }
    
    /**
     * Creates the appropriate welcome message that is displayed
     * to the client as soon as they establish a connection to the server,
     * as specified in ps4 instructions (http://web.mit.edu/6.031/www/sp17/psets/ps4/#protocol_and_specification)
     * @param boardWidth the width of the Minesweeper game board that can be
     * played with on this server.
     * @param boardHeight the height of the Minesweeper game board that can be
     * played with on this server.
     * @return a welcome message containing information about the game board
     * that can be played with on this server, and how many clients are
     * currently connected to the server.
     */
    private String welcomeMessage(final int boardWidth, final int boardHeight){
        synchronized (lock){ 
            String welcomeMessage = "Welcome to Minesweeper. Players: " + numberOfClientsConnected +
                    " including you. Board: " + boardWidth + " columns by " 
                    + boardHeight +" rows. Type 'help' for help.";
            return welcomeMessage;
        }
    }
    
    /**
     * Handles client request for help by printing out a message, 
     * which indicates all the commands the user can send to the server.
     * @return a helpful message indicating all commands the user can send to the
     * server.
     */
    private String helpMessageRequest(){
        String helpMessage ="Hello lost Minesweeper player! "
                + "Type 'look' to look at the current state of the game board, "
                + "'dig X Y' where X and Y are integers to dig at square with coordinates X,Y, "
                + "'flag X Y' where X and Y are integers to flag square with coordinates X,Y, "
                + "'deflag X Y' where X and Y are integers to dig at square with coordinates X,Y, "
                + "'help' to see this message, "
                + "and 'bye' to terminate connection to the Minesweeper server!\n";
        return helpMessage;
    }
    
    /**
     * Handles client request to dig at square with coordinates x,y by
     * performing dig operation at square and then returning an appropriate message.
     * @param x the x coordinate of the square the client wishes to dig at
     * @param y the y coordinate of the square the client wishes to dig at
     * @return a message, which is either the BOOM message if 
     * the client has dug up a bomb, or a normal BOARD message (as specified in ps4 instructions),
     * which is a string representation of the board’s state after digging.
     */
    private String digMessageRequest(final int x, final int y){
        boolean bombed = gameBoard.dig(x, y);
        if (bombed){
            return "BOOM!\n";
        } 
        //otherwise, just return normal BOARD message
        String look = gameBoard.look();
        return look;
    }
    
    /**
     * Handles client request to flag square with coordinates x,y by
     * flagging square and returning an appropriate message.
     * @param x the x coordinate of the square the client wishes to flag
     * @param y the y coordinate of the square the client wishes to flag
     * @return a BOARD message (as specified in ps4 instructions), 
     * which is a string representation of the board’s current state after flagging.
     */
    private String flagMessageRequest(final int x, final int y){
        gameBoard.flag(x, y);
        String look = gameBoard.look();
        return look;
    }
    
    /**
     * Handles client request to deflag square with coordinates x,y by
     * deflagging square and returning an appropriate message.
     * @param x the x coordinate of the square the client wishes to deflag
     * @param y the y coordinate of the square the client wishes to deflag
     * @return a BOARD message (as specified in ps4 instructions), 
     * which is a string representation of the board’s current state after deflagging.
     */
    private String deFlagMessageRequest(final int x, final int y){
        gameBoard.deflag(x, y);
        String look = gameBoard.look();
        return look;
    }
}
