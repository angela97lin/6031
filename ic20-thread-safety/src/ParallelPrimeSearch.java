import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Compute primes in multiple threads.
 */
public class ParallelPrimeSearch {
    
    //
    // Thread safety argument:
    //     TODO
    //
    
    public static void main(final String[] args) {
        final int numberOfDigits = 6;
        final BigInteger low  = new BigInteger("1000000000000000000000000".substring(0, numberOfDigits));
        final BigInteger high = new BigInteger("9999999999999999999999999".substring(0, numberOfDigits));
        System.out.println("searching all " + numberOfDigits + "-digit numbers...");
        final SortedSet<BigInteger> primes = primesBetween(low, high);
        System.out.println("found " + primes.size() + " primes");
        System.out.println("from " + primes.first() + " to " + primes.last());
    }
    
    private static int NUMBER_OF_THREADS = 4;
    
    /**
     * @return all prime numbers p such that low <= p <= high.
     * (all primes are found with very high probability but not certainty).
     */
    public static SortedSet<BigInteger> primesBetween(final BigInteger low, final BigInteger high) {
        final SortedSet<BigInteger> primes = Collections.synchronizedSortedSet(new TreeSet<>());
        
        // partition the [low, high] range into one part for each thread
        final BigInteger[] divisorAndRemainder = high.subtract(low).divideAndRemainder(BigInteger.valueOf(NUMBER_OF_THREADS));
        final BigInteger interval = divisorAndRemainder[0];
        final BigInteger remainder = divisorAndRemainder[1];
        
        Set<Thread> threads = Collections.synchronizedSet(new HashSet<>());
        for (int i = 0; i < NUMBER_OF_THREADS; ++i) {
            final BigInteger lowForThisThread = low.add(interval.multiply(BigInteger.valueOf(i)));
            final BigInteger highForThisThread = lowForThisThread.add(interval);
            
            // TODO: make a thread to find the primes in [lowForThisThread, highForThisThread]
            Thread t = new Thread(new Runnable() {
                public void run() {
                    addPrimesBetween(lowForThisThread, highForThisThread,primes);
//                   System.out.println("found " + primes.size() + " primes");
//                    System.out.println("from " + primes.first() + " to " + primes.last());
                }
            });
            t.start();
            threads.add(t);
        }
        
        // handle the remainder in the main thread
        final BigInteger lowLeftover = high.subtract(remainder);
        final BigInteger highLeftover = high;
        addPrimesBetween(lowLeftover, highLeftover, primes);
        
        waitForAllThreadsToFinish(threads);
        
        return primes;
    }
    
    /*
     * Modifies primes set by adding all prime numbers p such that low <= p <= high.
     * (all primes are found with very high probability but not certainty).
     */
    private static void addPrimesBetween(final BigInteger low, final BigInteger high, final Set<BigInteger> primes) {
        BigInteger prime;
        
        // find the first prime >= low
        if (low.compareTo(BigInteger.ONE) <= 0) {
            prime = BigInteger.valueOf(2);
        } else {
            prime = low.subtract(BigInteger.ONE).nextProbablePrime();
        }
        
        while (prime.compareTo(high) <= 0) {
            primes.add(prime);
            prime = prime.nextProbablePrime();
        }
    }
    
    /*
     * Returns after all threads in the collection have exited.
     */
    private static void waitForAllThreadsToFinish(Collection<Thread> threads) {
        // wait for all the threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException ie) {
                throw new AssertionError("should never happen", ie);
            }
        }
    }
}
