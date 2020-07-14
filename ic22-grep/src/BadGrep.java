import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BadGrep {
    //"right answer" = 1000
    private static final int NUMBER_OF_THREADS = 2;

    private static int numMatches = 0; // danger!

    public static void main(String[] args) throws InterruptedException {

        // substring to search for
        final String substring = "use";

        // build a list of lines to search
        List<String> inputLines = Collections.synchronizedList(new ArrayList<>()); // danger!
        for (int ii = 0; ii < 1000; ++ii) {
            inputLines.add("don't use a list for this");
            inputLines.add("it's not threadsafe");
        }

        // list for accumulating matching lines
        List<String> outputMatches = Collections.synchronizedList(new ArrayList<>());

        Thread[] searchers = new Thread[NUMBER_OF_THREADS]; // use multiple consumers

        for (int ii = 0; ii < searchers.length; ii++) { // create consumers
            searchers[ii] = new Thread(new Runnable() {
                public void run() {
                    synchronized(outputMatches){
                        while (inputLines.size() > 0) {
                            int i = (int) (Math.random() * inputLines.size());
                            String line = inputLines.get(i);
                            inputLines.remove(i);
                            if (line.contains(substring)) {
                                outputMatches.add(line);
                                ++numMatches;
                            }
                        }
                    }
                }
            });
        }

        for (Thread consumer : searchers) { // start all the threads
            consumer.start();
        }

        for (Thread consumer : searchers) { // wait for them all to stop
            consumer.join();
        }

        System.out.println(numMatches + " lines matched");
    }
}
