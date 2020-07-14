/**
 * Represents an immutable single-elimination tournament.
 */
public interface Tournament {    
    // Datatype definition:
    //    Tournament = Bye(p:Player) + Match(t1:Tournament, t2:Tournament)
    
    /**
     * @return the winner of the tournament
     */
    public Player winner();
    // winner() is defined as:
    //    winner(Bye(p)) = p
    //    winner(Match(t1, t2)) = 
    //         winner(t1) if skill(winner(t1)) > skill(winner(t2))
    //         winner(t2) otherwise

    /**
     * @return true iff the tournament is perfectly balanced 
     *                 (every player is scheduled for the same number of games)
     */
    public boolean balanced();
    // balanced() is defined as:
    //   balanced(Bye(p)) = true
    //   balanced(Match(t1, t2)) = balanced(t1) && balanced(t2) && size(t1)==size(t2)
    //            
    // 
    
    /**
     * 
     * @return returns the size ("depth") of a tournament
     */
    public int size();
    // size() is defined as:
    //   size(Bye(p)) = 1
    //   size(Match(t1, t2)) = max(size(t1) + size(t2)) +1
    
    // TODO: another operation that you'll need in order to implement balanced().
    // Mimic winner() and balanced() above:
    // - Javadoc comment with spec
    // - Java method signature
    // - mathematical definition in a non-Javadoc comment
}

// In general, define every class or interface in its own .java file.
// But to keep this exercise compact, we're defining a placeholder Player type below,
// without the "public" modifier.

/**
 * Represents an immutable named player with skill level in [0,100].
 */
interface Player {
    public String name();
    public int skill();
}
