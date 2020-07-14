package hogwarts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import hogwarts.Castle.House;

public class Wizard {
    
    private final Castle castle;
    private final Castle.House house;
    private final String name;
    private final Set<Wizard> friends;
    
    // Rep invariant:
    //    all fields != null
    //    friend links are bidirectional: for all f in friends, f.friends contains this
    
    // Concurrency argument:
    //    all Wizard objects in the same castle are guarded by the castle's lock,
    //    may not touch the rep of any Wizard without synchronizing on the castle
    
    // check the rep invariant
    private void checkRep() {
        synchronized (castle) {
            assert house != null;
            assert name != null;
            assert friends != null;
            assert bidirectionalFriendships();
        }
    }
    
    // return true iff all the people this wizard thinks are friends, agree
    private boolean bidirectionalFriendships() {
        synchronized (castle) {
            for (Wizard p : friends) {
                assert p.isFriendsWith(this);
            }
            return true;
        }
    }
    
    /**
     * Make a Wizard.  Caller is responsible for adding the wizard to the
     * castle using castle.add().
     * @param castle castle where the wizard will live, must be != null
     * @param house wizard's house, must be != null
     * @param name wizard's name, must be != null
     */
    public Wizard(Castle castle, House house, String name) {
        this.castle = castle;
        this.house = house;
        this.name = name;
        this.friends = new HashSet<>();
        checkRep();
    }
    
    /**
     * @return wizard's name
     */
    public String getName() {
        synchronized (castle) {
            return name;
        }
    }
    
    /**
     * @return castle where this wizard lives
     */
    public Castle getCastle() {
        synchronized (castle) {
            return castle;
        }
    }
    
    /**
     * @return house affiliation of this wizard
     */
    public House getHouse() {
        synchronized (castle) {
            return house;
        }
    }
    
    /**
     * @return snapshot of this wizard's friends
     */
    public List<Wizard> getFriends() {
        synchronized (castle) {
            return new ArrayList<>(friends);
        }
    }
    
    /**
     * @return true if and only if this wizard is friends with that wizard
     */
    public boolean isFriendsWith(Wizard that) {
        synchronized (castle) {
            return this.friends.contains(that);
        }
    }
    
    /**
     * Make this wizard friends with that wizard.
     * (Has no effect if they're already friends.)
     * Modifies both this and that.
     */
    public void friend(Wizard that) {
        synchronized (castle) {
            if (this.friends.add(that)) {
                that.friend(this);
            }
            checkRep();
        }
    }
    
    /**
     * Remove the friendship between this wizard and that wizard.
     * (Has no effect if they're not currently friends.)
     * Modifies both this and that.
     */
    public void defriend(Wizard that) {
        synchronized (castle) {
            if (this.friends.remove(that)) {
                that.defriend(this);
            }
            checkRep();
        }
    }
    
    /*
     * @see java.lang.Object#toString()
     */
    public String toString() {
        synchronized (castle) {
            return "<" + name.replace(" ", "_") + ":" + house + ">";
        }
    }
}
