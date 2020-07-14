import java.util.*;

import org.junit.runner.notification.RunListener.ThreadSafe;

public class Racing {

    private static final int NUMBER_OF_THREADS = 2;
    private static final int NUMBER_OF_OPERATIONS = 5;
    
    public static void main(String[] args) {
        // TODO: choose one of these data structures
        //final Set<Integer> set = new HashSet<>();
        final List<Integer> list = new ArrayList<>();
        //final List<Integer> list = new LinkedList<>();
        //final Map<Integer,Integer> map = new HashMap<>();
        
        System.out.println(NUMBER_OF_THREADS
                            + " threads do "
                            + NUMBER_OF_OPERATIONS
                            + " mutations each...");
        
        Set<Thread> threads = new HashSet<>();
        for (int i = 0; i < NUMBER_OF_THREADS; ++i) {
            final int threadNumber = i; // only final local variables can be seen by inner classes
                                        // like the Runnable below
            Thread t = new Thread(new Runnable() {
                public void run() {
                    System.out.println("thread " + threadNumber + " is starting work");
                    
                    for (int j = 0; j < NUMBER_OF_OPERATIONS; ++j) {
                        int value = j*NUMBER_OF_THREADS + threadNumber;
                        //System.out.println(value);  // uncomment this to mask the race condition
                        
                        // TODO: put value in the data structure
                        list.add(value);
                    }
                    
                    System.out.println("thread " + threadNumber + " is done");
                    
                    // TODO: print some part of the final data structure (e.g. its size) 
                    //       to see if it's correct
                    System.out.println("size of list is:" + list.size());
                }
            });
            t.start(); // don't forget to start the thread!
            threads.add(t);      
        }
        for (Thread t: threads) {
            try {
            t.join();
            } catch (InterruptedException e){
                throw new AssertionError("should never get here");
            }
        }
       
    }
}
