import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for the Tournament data type.
 */
public class TournamentTest {
    
    // MISSING testing strategy
    
    @Test public void testWinnerSinglePlayer() {
        Player player = new Player("Alice", 98);
        Tournament singlePlayer = Tournament.single(player);
        Player winner = null; // TODO
        assertEquals(player, winner);
    }
    
    @Test public void testWinnerOneMatch() {
        Player player1 = new Player("Alice", 98);
        Player player2 = new Player("Bob", 76);
        Tournament oneMatch = Tournament.match(/* TODO */);
        Player winner = null; // TODO
        assertEquals(player1, winner);
    }
    
    // MISSING test additional partitions
    
}
