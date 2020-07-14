import java.math.BigInteger;
import java.util.*;

/**
 * Compute primes in a single thread.
 */
public class SequentialPrimeSearch {
    
    public static void main(final String[] args) {
        final int numberOfDigits = 6;
        final BigInteger low  = new BigInteger("1000000000000000000000000".substring(0, numberOfDigits));
        final BigInteger high = new BigInteger("9999999999999999999999999".substring(0, numberOfDigits));
        System.out.println("searching all " + numberOfDigits + "-digit numbers...");
        final SortedSet<BigInteger> primes = primesBetween(low, high);
        System.out.println("found " + primes.size() + " primes");
        System.out.println(primes.first().toString() + " " + primes.last().toString());
    }
    
    /**
     * @return all prime numbers p such that low <= p <= high.
     * (all primes are found with very high probability but not certainty).
     */
    public static SortedSet<BigInteger> primesBetween(final BigInteger low, final BigInteger high) {
        final SortedSet<BigInteger> primes = new TreeSet<>();
        addPrimesBetween(low, high, primes);
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
}
