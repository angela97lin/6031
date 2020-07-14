import java.io.*;
import java.net.*;

public class WebStreams {
    
    public static void main(String[] args) {
        
        String ps0 = "http://web.mit.edu/6.031/www/sp17/psets/ps/";
        
        try {
            // create a URL object from ps0
            URL url = new URL(ps0);
            
            // open an input stream from the URL
            InputStream input = url.openStream();
            
            // wrap the input stream with a BufferedReader
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            
            // read from the reader one line at a time, printing each line
            String line = "";
            while ((line = reader.readLine()) != null){
                System.out.println(line);
            }
            reader.close();
            
            
        } catch (IOException ioe) {
            System.err.println("I/O error: " + ioe);
        }
    }
}
