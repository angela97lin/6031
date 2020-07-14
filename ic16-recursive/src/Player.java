/**
 * Represents a immutable player.
 */
public class Player {
    
    // Abstraction function:
    //    AF(name,skill) = represents a player named name with skill level skill.
    // Representation invariant:
    //    name must not be empty
    // Safety from rep exposure: 
    //    All fields are immutable and private.

    private final String name;
    private final int skill;
    
    /**
     * Constructs a new Player with name and skill.
     */
    public Player(String name, int skill) {
        this.name = name;
        this.skill = skill;
    }
 
    /**
     * Returns the name of a player
     * @return String name, the name of the player
     */
    public String getName(){
        return name;
    }
    
    /**
     * Returns the skill level of a player
     * @return skill of player
     */
    public int getSkill(){
        return skill;
    }
    
    @Override 
    public String toString(){
        return name;
    }
    
    @Override 
    public boolean equals(Object obj){
        if (!(obj instanceof Player)) return false;
        Player thatPlayer = (Player) obj;
        return (thatPlayer.getName().equals(this.getName()) &&
                thatPlayer.getSkill() == this.getSkill());
    }
    
    @Override 
    public int hashCode(){
        return skill; 
    }
}
