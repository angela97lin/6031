import java.io.*;
import java.util.Arrays;

/**
 * An example of composing streams. Also demonstrates byte array input/output streams.
 */
public class Streams {

    public static void main(String[] args) {

        // part 1: input streams
        System.out.println("-- input streams --");

        byte[] inputBytes = new byte[] {
                72, 101, 108, 108, 111, 44, 32, 119, 111, 114, 108, 100, 33, 10, 86, 101,
                110, 105, 44, 32, 118, 105, 100, 105, 44, 32, 118, 105, 99, 105, 10, 77,
                114, 46, 32, 87, 97, 116, 115, 111, 110, 32, 45, 45, 32, 99, 111, 109,
                101, 32, 104, 101, 114, 101, 32, 45, 45, 32, 73, 32, 119, 97, 110, 116,
                32, 116, 111, 32, 115, 101, 101, 32, 121, 111, 117, 46, 10 };

        // byte stream
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);

        // byte stream --> character stream
        InputStreamReader characterStream = new InputStreamReader(inputStream);

        // character stream --> buffered stream
        BufferedReader reader = new BufferedReader(characterStream);

        // read from the stream one line at a time, print each line
        try {
            for (String line; (line  = reader.readLine()) != null;){
                System.out.println(line);
            }
        }
        catch (IOException e){
            throw new AssertionError("error reading line");
        }

        // part 2: output streams
        System.out.println("\n-- output streams --");

        // byte stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // character stream --> byte stream
        OutputStreamWriter byteWriter = new OutputStreamWriter(outputStream);

        // convenient printer --> character stream
        PrintWriter writer = new PrintWriter(byteWriter, true); //true = autoflush

        // print each line to the stream, one line at a time
        for (String line : new String[] {
                "Hofstadter's Law:",
                "It always takes longer than you expect,",
        "even when you take into account Hofstadter's Law." }) {
            writer.println(line);
            //note: print does not autoflush
            //writer.flush();
        }

        // print output to console
        byte[] outputBytes = outputStream.toByteArray();
        System.out.println("output (" + outputBytes.length + " bytes) = " + Arrays.toString(outputBytes));
    }
}
