/* Copyright (c) 2015-2017 MIT 6.005/6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import java.io.File;
import java.io.IOException;

/**
 * Example program using GraphPoet.
 * 
 * <p>PS2 instructions: you are free to change this example class.
 */
public class Main {
    
    /**
     * Generate example poetry.
     * 
     * @param args unused
     * @throws IOException if a poet corpus file cannot be found or read
     */
    public static void main(String[] args) throws IOException {
        final GraphPoet nimoy = new GraphPoet(new File("src/poet/letitsnow"));
        final String input = "There's a patch of old snow in a corner That I should have guessed "
                + "Was a blow-away paper the rain Had brought to rest. It is speckled with grime as if "
                + "Small print overspread it, The news of a day I've forgotten If I ever read it. ";
        System.out.println(input + "\n>>>\n" + nimoy.poem(input));
    }
    
}
