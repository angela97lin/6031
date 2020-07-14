package hogwarts;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import hogwarts.Castle.House;
import hogwarts.Castle.SameNameException;

public class WizardProcs2 {
    
    // Implement using map/filter/reduce
    
    public static void main(String[] args) throws SameNameException {
        Castle castle = Castle.makeHogwarts();
        List<Wizard> wizards = castle.wizards();
        
        System.out.println("Wizards:     " + wizards);
        
        // we need a Stream<Wizard> to use map/filter/reduce
        // to obtain a Stream from a List, use stream()
        // to obtain a List from a Stream, use collect(Collectors.toList())
        //   collect is just reduce optimized for mutable containers
        System.out.println("Wizards:     " 
            + wizards.stream()
                     .collect(toList()));
        
        System.out.println("Names:       " 
            + wizards.stream()
                     .map(w -> w.getName())
                     .collect(toList()));
        
        System.out.println("Houses:      "
            + wizards.stream()
                     .map(Wizard::getHouse) 
                     .collect(toList()));
        
        System.out.println("Friendly:    " 
            + wizards.stream()
                     .filter(p -> p.getFriends().size() > 2)
                     .collect(toList()));
        
        System.out.println("Hufflepuffs: " 
            + wizards.stream()
                     // TODO: count the wizards in house Hufflepuff
                     .filter(p -> p.getHouse() == (House.Hufflepuff))
                     //filter(inHouse(House.Hufflepuff))
                     .count()
                     );
        
        System.out.println("Last by name:    " 
            + wizards.parallelStream()
                    .peek(w -> System.out.println(Thread.currentThread())) //must have thread safety/no reliance
                     // TODO: find the wizard whose name is last alphabetically
                     // hint: use reduce
                    .reduce((w,v) -> w.getName().compareTo(v.getName()) > 0 ? w : v)
                    //.min((a, b) -> b.getName().compareTo(a.getName()))
                     );

        System.out.println("Friendships:    " 
            + wizards.stream()
                     // TODO: create concatenation of "x likes y\n"
                     // hint: use flatMap and reduce
                    .flatMap(w -> w.getFriends().stream().map(y -> w.getName() + " likes " + y.getName() + "\n"))
                    .reduce("", String::concat)
                     );
    }
    
    private static Predicate<Wizard> inHouse(Castle.House house){
        return w -> w.getHouse() == house;
    }
}
