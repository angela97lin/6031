import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/** Search web pages for lines containing a string. */
public class Grep {
    public static void main(String[] args) throws Exception {

        // substring to search for
        String substring = "specification";

        // URLs to search
        String[] urls = new String[] {
                "http://web.mit.edu/6.031/www/sp17/psets/ps0/",
                "http://web.mit.edu/6.031/www/sp17/psets/ps1/",
                "http://web.mit.edu/6.031/www/sp17/psets/ps2/",
                "http://web.mit.edu/6.031/www/sp17/psets/ps3/",
        };

        // list for accumulating matching lines --> threadsafe datatypes
        List<Item> matches = Collections.synchronizedList(new ArrayList<>());

        // queue for sending lines from producers to consumers
        BlockingQueue<Item> queue = new LinkedBlockingQueue<>();

        Thread[] downloaders = new Thread[urls.length]; // one downloader per URL
        Thread[] searchers = new Thread[1]; // TODO use multiple searchers

        for (int ii = 0; ii < searchers.length; ii++) { // create & start searching threads
            Thread searcher = searchers[ii] = new Thread(new Searcher(substring, queue, matches));
            searcher.start();
        }

        for (int ii = 0; ii < urls.length; ii++) { // create & start downloading threads
            Thread downloader = downloaders[ii] = new Thread(new Downloader(urls[ii], queue));
            downloader.start();
        }

        for (Thread downloader : downloaders) { // wait for downloaders to stop
            downloader.join();
        }

        // stop searching threads somehow
        // ...
        // ...

        for (Thread searcher : searchers) { // wait for searchers to stop
            searcher.join();
        }

        for (Item match : matches) {
            System.out.println(match);
        }
        System.out.println(matches.size() + " lines matched");
    }
}

class Downloader implements Runnable {

    private final URL url;
    private final String urlString;
    private final BlockingQueue<Item> queue;

    Downloader(String url, BlockingQueue<Item> q) throws MalformedURLException {
        urlString = url;
        this.url = new URL(url);
        queue = q;
    }

    public void run() {
        // TODO read lines and push them onto the queue for consumers
        try {

            // open an input stream from the URL
            InputStream input = url.openStream();

            // wrap the input stream with a BufferedReader
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            // read from the reader one line at a time, printing each line
            String line = "";
            int lineNumber = 0;
            while ((line = reader.readLine()) != null){

                queue.put(new Text(urlString, lineNumber, line));
                lineNumber++;

            }
            reader.close();


        } catch (IOException ioe) {
            System.err.println("I/O error: " + ioe);
        } catch (InterruptedException e) {
            System.err.println("ewwwww! : " + e);
    }
}
}

class Searcher implements Runnable {

    private final String keyword;
    private final BlockingQueue<Item> queue;
    private final List<Item> matches;
    
    Searcher(String keyword, BlockingQueue<Item> q, List<Item> m) {
        this.keyword = keyword;
        this.queue = q;
        this.matches = m;
    }

    public void run() {
        // TODO take downloaded lines off the queue and add matches to the list
        try {
            while(true){
                Item item = queue.take();
                if (item.GTFO()){
                    return;
                }
                if (item.text().contains(keyword)){
                    matches.add(item);
                }
            }
        } catch (InterruptedException e) {
            System.out.println("nancy pls :(");
        }
    }
}


/** Represents single item of work. */
interface Item {
    /** @return the filename */
    public String filename();
    /** @return the line number */
    public int lineNumber();
    /** @return the text */
    public String text();
    public boolean GTFO();
}

class GTFO implements Item {

    @Override
    public String filename() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int lineNumber() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String text() {
        // TODO Auto-generated method stub
        return null;
    }



    @Override
    public boolean GTFO() {
        return true;
    }
    
}
class Text implements Item {
    private final String filename;
    private final int lineNumber;
    private final String text;

    public Text(String filename, int lineNumber, String text) {
        this.filename = filename;
        this.lineNumber = lineNumber;
        this.text = text;
    }

    @Override public String filename() {
        return filename;
    }

    @Override public int lineNumber() {
        return lineNumber;
    }

    @Override public String text() {
        return text;
    }

    @Override public String toString() {
        return filename + ":" + lineNumber + " " + text;
    }

    @Override
    public boolean GTFO() {
        return false;
    }
}
