/* Copyright (c) 2007-2017 MIT 6.005/6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import org.junit.Test;

/**
 * Tests for GameServer.
 * Note: adapted from https://github.com/6031-sp17/ps4
 */
public class GameServerTest {
    
    /*
     * Testing strategy for GameServerTest:
     * 
     * Creating GameServer:
     *      - not using parameters
     *      - using size parameters
     *      - using file parameters
     * 
     * LOOK
     *      - width, height:
     *          - width = 1, >1
     *          - height = 1, >1
     *          - width == height, width != height
     *      - board squares:
     *          - has bombs: 0, 1, >1 
     *          - has flags ("F"): 0, 1, >1
     *          - has spaces (for dug and 0 neighbors that have a bomb)
     *          - has integer COUNT in range [1-8] (for dug and COUNT neighbors that have a bomb)
     *          
     * DIG
     *      - x , y:
     *          - x = 0, 1, 1 < x < board[0].length, x > board[0].length
     *          - y = 0, 1, 1 < y < board.length, y > board.length
     *          - x == y, x != y
     *      - square (x,y):
     *          - is an untouched square 
     *              - contains a bomb
     *              - does not contain a bomb
     *                  - has 0 untouched neighbor squares with bombs
     *                  - has 1 untouched neighbor squares with bombs
     *                  - has > 1 untouched neighbor squares with bombs
     *          - is NOT an untouched square
     *              - is flagged 
     * FLAG
     *      - x , y:
     *          - x = 0, 1, 1 < x < board[0].length, x > board[0].length
     *          - y = 0, 1, 1 < y < board.length, y > board.length
     *          - x == y, x != y
     *      - square (x,y):
     *          - is an untouched square 
     *          - is NOT an untouched square (flagged, revealed integer, space)
     * DEFLAG
     *      - x , y:
     *          - x = 0, 1, 1 < x < board[0].length, x > board[0].length
     *          - y = 0, 1, 1 < y < board.length, y > board.length
     *          - x == y, x != y
     *      - square (x,y):
     *          - is a flagged square 
     *          - is NOT a flagged square (untouched, revealed integer, space)
     * 
     * HELP_REQ
     * 
     * BYE
     * 
     * invalid/does not match grammar
     */
    
    private static final String LOCALHOST = "127.0.0.1";
    private static final int MAX_CONNECTION_ATTEMPTS = 10;
    private static final String BOARDS_PKG = "minesweeper/boards/";

    /**
     * Start a GameServer with a board file from BOARDS_PKG.
     * 
     * @param boardFile board to load
     * @return thread running the server
     * @throws IOException if the board file cannot be found
     */
    private static Thread startGameServer(String boardFile, int portNumber) throws IOException {
        final URL boardURL = ClassLoader.getSystemClassLoader().getResource(BOARDS_PKG + boardFile);
        if (boardURL == null) {
            throw new IOException("Failed to locate resource " + boardFile);
        }
        final String boardPath;
        try {
            boardPath = new File(boardURL.toURI()).getAbsolutePath();
        } catch (URISyntaxException urise) {
            throw new IOException("Invalid URL " + boardURL, urise);
        }
        final String[] args = new String[] {
                "--port", Integer.toString(portNumber),
                "--file", boardPath
        };
        Thread serverThread = new Thread(() -> GameServer.main(args));
        serverThread.start();
        return serverThread;
    }
    
    /**
     * Start a GameServer with a board of sizeX by sizeY
     * @param sizeX the width of the board that will be used by the server
     * @param sizeY the height of the board that will be used by the server
     * @return thread running the server
     * @throws IOException if the board file cannot be found
     */
    private static Thread startGameServer(int sizeX, int sizeY, int portNumber) throws IOException {
        String sizeParameterString = sizeX + "," + sizeY;
        final String[] args = new String[] {
                "--port", Integer.toString(portNumber),
                "--size", sizeParameterString
        };
        Thread serverThread = new Thread(() -> GameServer.main(args));
        serverThread.start();
        return serverThread;
    }
    
    /**
     * Start a GameServer with a randomized 12 x 12 board (as specified in ps4 instructions)
     * @return thread running the server
     * @throws IOException if the board file cannot be found
     */
    private static Thread startGameServer(int portNumber) throws IOException {
        final String[] args = new String[] {
                "--port", Integer.toString(portNumber)
        };
        Thread serverThread = new Thread(() -> GameServer.main(args));
        serverThread.start();
        return serverThread;
    }

    /**
     * Connect to a GameServer and return the connected socket.
     * 
     * @param server abort connection attempts if the server thread dies
     * @return socket connected to the server
     * @throws IOException if the connection fails
     */
    private static Socket connectToGameServer(Thread server, int portNumber) throws IOException {
        int attempts = 0;
        while (true) {
            try {
                Socket socket = new Socket(LOCALHOST, portNumber);
                socket.setSoTimeout(3000);
                return socket;
            } catch (ConnectException ce) {
                if ( ! server.isAlive()) {
                    throw new IOException("Server thread not running");
                }
                if (++attempts > MAX_CONNECTION_ATTEMPTS) {
                    throw new IOException("Exceeded max connection attempts", ce);
                }
                try { Thread.sleep(attempts * 10); } 
                catch (InterruptedException ie) { 
                    throw new IOException("Interrupted!", ie);
                }
            }
        }
    }
    
    @Test(timeout = 10000)
    public void testGameServerNoParameters() throws IOException {
        int portNumber = 4006;
        Thread thread = startGameServer(portNumber);

        Socket socket = connectToGameServer(thread, portNumber);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        assertTrue("expected HELLO message", in.readLine().startsWith("Welcome"));

        out.println("look");
        assertEquals("- - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - -", in.readLine());
        
        out.println("bye");
        socket.close();
    }
    
    @Test(timeout = 10000)
    public void testGameServerSizeParameters() throws IOException {
        int portNumber = 4007;
        Thread thread = startGameServer(5, 4, portNumber);

        Socket socket = connectToGameServer(thread, portNumber);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        assertTrue("expected HELLO message", in.readLine().startsWith("Welcome"));

        out.println("look");
        assertEquals("- - - - -", in.readLine());
        assertEquals("- - - - -", in.readLine());
        assertEquals("- - - - -", in.readLine());
        assertEquals("- - - - -", in.readLine());

        out.println("bye");
        socket.close();
    }
    
    //uses test1
    //covers case where LOOK: has 1, > 1 flags
    //                  FLAG: x = 0, 1
    //                        y = 0, 1
    //                        x == y
    //                        is an untouched square
    //                  DEFLAG: x = 0
    //                          y = 0
    //                          x == y
    //                          is a flagged square
    //                  
    @Test(timeout = 10000)
    public void testGame1() throws IOException {
        int portNumber = 4008;
        Thread thread = startGameServer("test1", portNumber);

        Socket socket = connectToGameServer(thread, portNumber);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        assertTrue("expected HELLO message", in.readLine().startsWith("Welcome"));

        out.println("help");
        assertTrue("expected HELP message", in.readLine().startsWith("Hello lost Minesweeper player!"));

        out.println("look");
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());

        out.println("flag 0 0");
        assertEquals("F - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());

        out.println("look");
        assertEquals("F - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        
        out.println("flag 1 0");
        assertEquals("F F - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());

        out.println("look");
        assertEquals("F F - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        
        out.println("deflag 0 0");
        assertEquals("- F - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());
        assertEquals("- - - - - - - - - - - - - - - -", in.readLine());

        out.println("bye");
        socket.close();
    }
    
    
    //uses test2
    //covers case where LOOK: width = 1, height = 1
    //                        has 1 bomb
    //                        has 0 flags
    //                  DIG: x = 0, y = 0
    //                       x == y
    //                       contains a bomb
    //                  FLAG: is NOT an untouched square (space)
    //                  invalid / does not match grammar
    @Test(timeout = 10000)
    public void testGame2() throws IOException {
        int portNumber = 6031;
        Thread thread = startGameServer("test2", portNumber);

        Socket socket = connectToGameServer(thread, portNumber);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        assertTrue("expected HELLO message", in.readLine().startsWith("Welcome"));

        out.println("look");
        assertEquals("-", in.readLine());

        out.println("dig 0 0");
        assertEquals("BOOM!", in.readLine());

        out.println("flag 0 0");
        assertEquals(" ", in.readLine());

        out.println("WHAT IS GOING ON?????????????????");
        assertTrue("expected HELP message", in.readLine().startsWith("Hello lost Minesweeper player!"));

        out.println("bye");
        socket.close();
    }
    
    
    //uses test3
    //covers case where LOOK: width != height
    //                  DIG: x = 1, y = 1
    //                       x == y, x != y
    //                       1 < x < board[0].length
    //                       1 < y < board.length
    //                       x > board[0].length
    //                       y > board.length
    //                       has 0 untouched neighbor squares with bombs
    //                       is flagged
    //                       has 1 untouched neighbor squares with bombs
    //                  FLAG: x != y
    //                        1 < x < board[0].length
    //                        1 < y < board.length
    //                        x > board[0].length
    //                        y > board.length
    //                        is NOT an untouched square (flagged, revealed integer, space)
    //                      
    @Test(timeout = 10000)
    public void testGame3() throws IOException {
        int portNumber = 6036;
        Thread thread = startGameServer("test3", portNumber);

        Socket socket = connectToGameServer(thread, portNumber);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        assertTrue("expected HELLO message", in.readLine().startsWith("Welcome"));

        out.println("look");
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());

        out.println("dig 1 1");
        assertEquals("      1 - - -", in.readLine());
        assertEquals("      1 - 2 1", in.readLine());
        assertEquals("      1 1 1  ", in.readLine());
        assertEquals("             ", in.readLine());

        out.println("dig 10 10");
        assertEquals("      1 - - -", in.readLine());
        assertEquals("      1 - 2 1", in.readLine());
        assertEquals("      1 1 1  ", in.readLine());
        assertEquals("             ", in.readLine());
        
        out.println("flag 6 0");
        assertEquals("      1 - - F", in.readLine());
        assertEquals("      1 - 2 1", in.readLine());
        assertEquals("      1 1 1  ", in.readLine());
        assertEquals("             ", in.readLine());
        
        out.println("flag 3 0");
        assertEquals("      1 - - F", in.readLine());
        assertEquals("      1 - 2 1", in.readLine());
        assertEquals("      1 1 1  ", in.readLine());
        assertEquals("             ", in.readLine());
        
        out.println("flag 1 0");
        assertEquals("      1 - - F", in.readLine());
        assertEquals("      1 - 2 1", in.readLine());
        assertEquals("      1 1 1  ", in.readLine());
        assertEquals("             ", in.readLine());
        
        out.println("flag 6 0");
        assertEquals("      1 - - F", in.readLine());
        assertEquals("      1 - 2 1", in.readLine());
        assertEquals("      1 1 1  ", in.readLine());
        assertEquals("             ", in.readLine());
        
        out.println("dig 6 0");
        assertEquals("      1 - - F", in.readLine());
        assertEquals("      1 - 2 1", in.readLine());
        assertEquals("      1 1 1  ", in.readLine());
        assertEquals("             ", in.readLine());
        
        out.println("flag 100 100");
        assertEquals("      1 - - F", in.readLine());
        assertEquals("      1 - 2 1", in.readLine());
        assertEquals("      1 1 1  ", in.readLine());
        assertEquals("             ", in.readLine());
        
        out.println("dig 4 1");
        assertEquals("BOOM!", in.readLine());

        out.println("look");
        assertEquals("          1 F", in.readLine());
        assertEquals("          1 1", in.readLine());
        assertEquals("             ", in.readLine());
        assertEquals("             ", in.readLine());

        out.println("bye");
        socket.close();
    }
    
    //uses test4
    //covers case where DEFLAG: x > board[0].length
    //                          y > board.length
    //                          1 < x < board[0].length
    //                          1 < y < board.length
    @Test(timeout = 10000)
    public void testGame4() throws IOException {
        int portNumber = 8217;
        Thread thread = startGameServer("test4", portNumber);

        Socket socket = connectToGameServer(thread,portNumber);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        assertTrue("expected HELLO message", in.readLine().startsWith("Welcome"));

        out.println("look");
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());

        out.println("deflag 0 0");
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());

        out.println("flag 1 1");
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- F - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());

        out.println("deflag 1 1");
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        
        out.println("deflag 100 100");
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        
        out.println("flag 4 4");
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - F - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());

        out.println("deflag 4 4");
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        
        out.println("dig 4 1");

        out.println("look");
        assertEquals("             ", in.readLine());
        assertEquals("             ", in.readLine());
        assertEquals("             ", in.readLine());
        assertEquals("             ", in.readLine());
        assertEquals("             ", in.readLine());
        assertEquals("             ", in.readLine());
        assertEquals("             ", in.readLine());
        assertEquals("             ", in.readLine());
        assertEquals("             ", in.readLine());

        out.println("bye");
        socket.close();
    }
    
    //uses test6
    //covers case where DIG: has > 1 untouched neighbor squares with bombs
    //                  DEFLAG: x = 1, y = 1
    //                          x != y
    //                          is NOT a flagged square (revealed integer)
    @Test(timeout = 10000)
    public void testGame6() throws IOException {
        int portNumber = 1103;
        Thread thread = startGameServer("test6", portNumber);

        Socket socket = connectToGameServer(thread, portNumber);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        assertTrue("expected HELLO message", in.readLine().startsWith("Welcome"));

        out.println("look");
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());

        out.println("dig 3 4");
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - 8 - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());

        out.println("flag 1 1");
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- F - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - 8 - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        
        out.println("deflag 1 1");
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - 8 - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        
        out.println("flag 4 4");
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - 8 F - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        
        out.println("deflag 3 4");
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - 8 F - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        
        out.println("dig 2 4");
        assertEquals("BOOM!", in.readLine());

        out.println("bye");
        socket.close();
    }
 
    @Test(timeout = 10000)
    public void testGame7() throws IOException {
        int portNumber = 1703;
        Thread thread = startGameServer("test7", portNumber);

        Socket socket = connectToGameServer(thread, portNumber);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        assertTrue("expected HELLO message", in.readLine().startsWith("Welcome"));

        out.println("look");
        assertEquals("-", in.readLine());
        assertEquals("-", in.readLine());
        assertEquals("-", in.readLine());
        assertEquals("-", in.readLine());
        assertEquals("-", in.readLine());
        assertEquals("-", in.readLine());
        assertEquals("-", in.readLine());
        assertEquals("-", in.readLine());

        out.println("dig 3 1");
        assertEquals("-", in.readLine());
        assertEquals("-", in.readLine());
        assertEquals("-", in.readLine());
        assertEquals("-", in.readLine());
        assertEquals("-", in.readLine());
        assertEquals("-", in.readLine());
        assertEquals("-", in.readLine());
        assertEquals("-", in.readLine());
        
        out.println("dig 0 0");
        assertEquals("BOOM!", in.readLine());

        out.println("look");
        assertEquals(" ", in.readLine());
        assertEquals(" ", in.readLine());
        assertEquals(" ", in.readLine());
        assertEquals(" ", in.readLine());
        assertEquals(" ", in.readLine());
        assertEquals(" ", in.readLine());
        assertEquals("1", in.readLine());
        assertEquals("-", in.readLine());

        out.println("bye");
        socket.close();
    }
    
    @Test(timeout = 10000)
    public void testGame8() throws IOException {
        int portNumber = 2750;
        Thread thread = startGameServer("test8", portNumber);

        Socket socket = connectToGameServer(thread, portNumber);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        assertTrue("expected HELLO message", in.readLine().startsWith("Welcome"));

        out.println("look");
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());

        out.println("dig 4 1");
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - 2 - -", in.readLine());

        out.println("dig 3 1");
        assertEquals("BOOM!", in.readLine());

        out.println("look");
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - 1 1 - -", in.readLine());
        
        out.println("bye");
        socket.close();
    }
    
    @Test(timeout = 10000)
    public void testGame9() throws IOException {
        int portNumber = 2180;
        Thread thread = startGameServer("test9", portNumber);

        Socket socket = connectToGameServer(thread, portNumber);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        assertTrue("expected HELLO message", in.readLine().startsWith("Welcome"));

        out.println("look");
        assertEquals("- - - - - - -", in.readLine());

        out.println("dig 3 0");
        assertEquals("BOOM!", in.readLine());

        out.println("look");
        assertEquals("- 1       1 -", in.readLine());

        out.println("bye");
        socket.close();
    }
    
    //uses test5
    //covers case where LOOK: width > 1, height > 1
    //                        width == height
    //                        bombs > 1
    //                        has integer COUNT in range [1-8] (for dug and COUNT neighbors that have a bomb)
    //                        has spaces (for dug and 0 neighbors that have a bomb)
    //                  HELLO
    //                  BYE
    //                  DIG: x != y
    //                       contains a bomb 
    //                       does not contain a bomb
    //                       1 neighbor with bomb
    @Test(timeout = 10000)
    public void publishedTest() throws IOException {
        int portNumber = 4009;
        Thread thread = startGameServer("test5", portNumber);

        Socket socket = connectToGameServer(thread, portNumber);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        assertTrue("expected HELLO message", in.readLine().startsWith("Welcome"));

        out.println("look");
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());

        out.println("dig 3 1");
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - 1 - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());

        out.println("dig 4 1");
        assertEquals("BOOM!", in.readLine());

        out.println("look");
        assertEquals("             ", in.readLine());
        assertEquals("             ", in.readLine());
        assertEquals("             ", in.readLine());
        assertEquals("             ", in.readLine());
        assertEquals("             ", in.readLine());
        assertEquals("1 1          ", in.readLine());
        assertEquals("- 1          ", in.readLine());

        out.println("bye");
        socket.close();
    }
}
