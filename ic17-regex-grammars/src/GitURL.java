import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitURL {
    private final static String[] EXAMPLE_URLS = new String[] {
        "ssh://athena.dialup.mit.edu/mit/6.031/git/sp17/ps1/bitdiddle.git",
        "ssh://athena.dialup.mit.edu/mit/6.031/git/fa17/ps3/alyssa.git",
        "ssh://louis@athena.dialup.mit.edu/mit/6.031/git/sp18/ps1/louis.git",
    };
    
    public static void main(String[] args) {
        // Here's an unmaintainable regex
        final String originalRegex = 
                "ssh://(\\w+@)?athena.dialup.mit.edu/mit/6\\.031/git/(?<term>(fa|sp)\\d\\d)/(?<pset>ps\\d)/(?<student>\\w+)\\.git";        
        System.out.println("originalRegex=" + originalRegex);

        // Let's rewrite it as a regex written like a regular grammar with nonterminals.
        // Fill in each TODO with a regular expression, imitating a grammar rule.
        // Then assemble them into urlRegex at the end.
        final String hostname = "athena.dialup.mit.edu";
        final String optionalUsername = "(\\w+@)?";
        final String path = "/mit/6\\.031/git/"; 
        final String term = "(?<term>(fa|sp)\\d\\d)/";    // TODO: define term nonterminal 
        final String pset = "(?<pset>ps\\d)/";    // TODO: define pset nonterminal
        final String student = "(?<student>\\w+)\\"; // TODO: define username nonterminal
        final String betterRegex = "ssh://" 
                                 + optionalUsername + hostname
                                 + path + term + pset + student +".git"; 
        System.out.println("  betterRegex=" + betterRegex);
        System.out.println();

        Pattern pattern = Pattern.compile(betterRegex);
        for (String url : EXAMPLE_URLS) {
            System.out.println("url=" + url);
            Matcher matcher = pattern.matcher(url);
            if (matcher.matches()) {
                System.out.print("\t term=" + matcher.group("term"));
                System.out.print("\t pset=" + matcher.group("pset"));
                System.out.print("\t student=" + matcher.group("student"));
                System.out.print("\n\n");
            } else {
                System.out.println("\tbetterRegex doesn't match\n");
            }
        }
    }
}
