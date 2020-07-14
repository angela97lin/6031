import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Trap {
    
    public static void main(String[] args) throws IOException {
        // Use external grep program to search for a string of digits 123
        Process process = Runtime.getRuntime().exec("grep 123");
        PrintWriter grepInput = new PrintWriter(new OutputStreamWriter(process.getOutputStream()));
        BufferedReader grepOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        
        for (int i = 0; i < 1_000; ++i) {
            grepInput.println(Math.random());
        }
        grepInput.flush(); // ha! we avoided THAT trap
        grepInput.close();

        int countMatches = 0;
        while (grepOutput.readLine() != null) {
            ++countMatches;
        }
        System.out.println(countMatches);
    }
}
