    package jukebox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFrame;
    
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Internet jukebox.  Runs an HTTP server on SERVER_PORT that receives requests for songs to play
 * on this machine, and displays a window allowing the local user to control playing.
 */
public class Main {
    //Yida's: 18.189.79.6
    //Ang: same but.133 not.6
    
    /*
     * Port where web server is listening.
     */
    public static final int SERVER_PORT = 8081;
    //concurrentlinkedqueue => blocking queue
    
    /**
     * Main method.  Starts the web server and the graphical user interface.
     * @param args command-line arguments, ignored.
     */
    private static final BlockingQueue<File> queue = new LinkedBlockingQueue<>();
    
    
    public static void main(String[] args) throws IOException {      
        Thread.dumpStack();
        queue.add(new File("cello.wav"));
        startGUI();
        startWebServer();
    }

    // Sound file that the jukebox plays
    private static File currentSoundFile = new File("doublebass.wav");
    
    /*
     * Play the sound file on this computer's speakers.
     */
    private static void play() {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(currentSoundFile));
            // TODO #3: add a listener that finds out when the sound stops playing
            //          and plays it again
            clip.addLineListener(new LineListener(){
                public void update(LineEvent le){
                    
                }
            });
            
            clip.start();
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }
    
    // Creates and shows the graphical user interface.
    private static void startGUI() {
        JFrame window = new JFrame("Jukebox");
        
        JButton playButton = new JButton("Play");
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {  //action perform to make play (can use lambda expression)
                play();
            } 
        });        
        window.getContentPane().add(playButton);
        window.pack();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);        
    }
    
    /*
     * Starts the web server.
     * @param port port number where web server will listen for HTTP requests
     * @throws IOException if server can't start up, e.g. if the port is already busy
     */
    private static void startWebServer() throws IOException {
        // make a web server
        //final HttpServer server = HttpServer.create(new InetSocketAddress(SERVER_PORT), 0);
        InetAddress addr = InetAddress.getByName("18.189.79.133");
 
        final HttpServer server = HttpServer.create(new InetSocketAddress(addr, SERVER_PORT), 0);
        
        // TODO #2: add a handler for handlePlay() below
        //   so that /play/<filename> changes the file being played
        //   e.g. http://localhost:8081/play/doublebass.wav
        
        server.createContext("/play/strings.wav", new HttpHandler() {  // not threadsafe? is touching currentSoundFile
            public void handle(HttpExchange exchange) throws IOException {
                //final String path = exchange.getRequestURI().getPath();
                //final String soundFilename = path.substring(exchange.getHttpContext().getPath());
                handlePlay(exchange);
            }
        });
        
        // start the server
        server.start();
        System.err.println("Server is listening on http://localhost:" + SERVER_PORT);
    }
    
    /*
     * Handle a /play/<filename> request.
     * 
     * If <filename> exists in the filesystem, then <filename> replaces the current sound file
     * in the jukebox, and returns a 200 success response to the client.
     * 
     * If request doesn't have that form or if <filename> doesn't exist, then returns a 
     * 404 error response to the HTTP client.  
     * 
     * @param exchange HTTP request/response. Modified by this method to send
     * a response to the client and close the exchange.
     */
    private static void handlePlay(final HttpExchange exchange) throws IOException {
        // if you want to know the requested path:
        final String path = exchange.getRequestURI().getPath();
        
        // it will always start with the base path from server.createContext():
        final String base = exchange.getHttpContext().getPath();
        assert path.startsWith(base);
        
        final String soundFilename = path.substring(base.length());
        final File newSoundFile = new File(soundFilename);
        if (newSoundFile.exists()) {
            // change the sound that's playing
            currentSoundFile = newSoundFile;
            
            // respond with HTTP code 200 to indicate success
            // response length 0 indicates a response will be written
            // you must call this method before calling getResponseBody()
            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
            exchange.sendResponseHeaders(200, 0);
            
            // write the response to the output stream
            PrintWriter out = new PrintWriter(exchange.getResponseBody(), true);
            out.println("playing " + soundFilename);
        } else {
            // respond with HTTP code 404 to indicate an error
            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
            exchange.sendResponseHeaders(404, 0);
            PrintWriter err = new PrintWriter(exchange.getResponseBody(), true);
            err.println("can't find sound file: " + newSoundFile);            
        }
    
        // if you do not close the exchange, the response will not be sent!
        exchange.close();
    }
    
}
