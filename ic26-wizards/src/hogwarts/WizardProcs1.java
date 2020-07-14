package hogwarts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

import hogwarts.Castle.House;
import hogwarts.Castle.SameNameException;

public class WizardProcs1 {

    // Implement the wizard processing functions using familiar Java loops

    /**  @return names of wizards in input */
    static List<String> names(List<Wizard> wizards) {
        List<String> names = new ArrayList<>();
        for (Wizard w : wizards){
            String n =  w.getName();
            names.add(n);
        }
        return names;
    }

    /** @return houses of wizards in input */
    static List<House> houses(List<Wizard> wizards) {
        List<House> houses = new ArrayList<>();
        for (Wizard w : wizards){
            House h =  w.getHouse();
            houses.add(h);
        }
        return houses;    
    }

    /** @return only the wizards in input with more than 2 friends */
    static List<Wizard> friendly(List<Wizard> wizards) {
        List<Wizard> friendly = new ArrayList<>();
        for (Wizard w : wizards){
            List<Wizard> f =  w.getFriends();
            if (f.size() > 2){
                friendly.add(w);
            }
        }
        return friendly;
    }

    /** @return number of wizards in input who are in house Hufflepuff */
    static int countHufflepuffs(List<Wizard> wizards) {
        int numberOfHufflepuff = 0;
        for (Wizard w : wizards){
            House h =  w.getHouse();
            if (h.name().equals(House.Hufflepuff)){
                numberOfHufflepuff += 1;
            }            
        }
        return numberOfHufflepuff;
    }

    /** @param list of wizards, must be nonempty
     * @return wizard in input whose name is last alphabetically */
    static Wizard lastByName(List<Wizard> wizards) {
        Wizard last = wizards.get(0);//rip if no wizards to begin with
        for (Wizard w : wizards){
            if (w.getName().compareTo(last.getName()) > 0 ){
                last = w;
            }
        }        
        return last;
    }

    /** @return concatenation of "x likes y\n" 
        for all (x,y) such that x is in wizards and y is in x.getFriends() */
    static String friendships(List<Wizard> wizards) {
        return null; // TODO
    }

    public static void main(String[] args) throws SameNameException {
        Castle castle = Castle.makeHogwarts();
        List<Wizard> wizards = castle.wizards();

        System.out.println("Wizards:      " + wizards);
        System.out.println("Names:        " + names(wizards));
        System.out.println("Houses:       " + houses(wizards));
        System.out.println("Friendly:     " + friendly(wizards));
        System.out.println("Hufflepuffs:  " + countHufflepuffs(wizards));
        System.out.println("Last by name: " + lastByName(wizards));
        System.out.println("Friendships:  " + friendships(wizards));
    }
}
