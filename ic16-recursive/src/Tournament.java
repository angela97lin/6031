/**
 * Represents an immutable single-elimination tournament.
 */
public interface Tournament {
    
    /*
     * Datatype Definition
     * 
     * Tournament = SingleTournament + match(Tournament,Tournament)
     * 
     */
    /**
     * @param player the only player in the tournament
     * @return tournament with only the given player
     */
    public static Tournament single(Player player) {
        return new SingleTournament(player);
    }
    
    /**
     * Creates a tournament match given two tournaments
     * @param firstTournament
     * @param secondTournament
     * @return
     */
    public static Tournament match(Tournament firstTournament, Tournament secondTournament) {
        return new SingleMatch(firstTournament, secondTournament);
    }
    
    /**
     * For a tournament, returns the player with the higher skill level
     * @return
     */
    public Player winner();
        
}

// In general, define every class in its own .java file.
// But for this exercise, write your concrete variants below,
//   and don't use the "public" modifier on the classes.

class SingleTournament implements Tournament {
    
    // Abstraction function:
    //   AF() = represents a single elimination tournament with
    // Representation invariant:
    //   player is immutable
    // Safety from rep exposure:
    //   

    private final Player player;
    
    public SingleTournament(Player player){
        this.player = player;
    }
    
    @Override
    public Player winner(){
        return player;
    }
}

class SingleMatch implements Tournament {
    // Abstraction function:
    //   AF() = represents a single elimination tournament with 
    // Representation invariant:
    //   Tournaments are immutable, 
    // Safety from rep exposure:
    //   tournament and players are immutable
    private final Tournament first;
    private final Tournament second;
    
    public SingleMatch(Tournament firstTournament, Tournament secondTournament){
        first = firstTournament;
        second = secondTournament;
    }
    
    @Override
    public Player winner(){
        Player firstWinner = first.winner();
        Player secondWinner = second.winner();
        if (firstWinner.getSkill() > secondWinner.getSkill()){
            return firstWinner;
        } else {
            return secondWinner;
        }
    }
}
