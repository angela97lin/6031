package music;

import static music.Pitch.OCTAVE;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MusicLanguage defines static methods for constructing and manipulating Music expressions.
 */
public class MusicLanguage {

    // Prevent instantiation
    protected MusicLanguage() {}

    ////////////////////////////////////////////////////
    // Factory methods
    ////////////////////////////////////////////////////

    /**
     * Make Music from a string using a variant of abc notation
     *    (see http://www.walshaw.plus.com/abc/examples/).
     * 
     * <p> The notation consists of whitespace-delimited symbols representing
     * either notes or rests. The vertical bar | may be used as a delimiter
     * for measures; notes() treats it as a space.
     * Grammar:
     * <pre>
     *     notes ::= symbol*
     *     symbol ::= . duration          // for a rest
     *              | pitch duration      // for a note
     *     pitch ::= accidental letter octave*
     *     accidental ::= empty string    // for natural,
     *                  | _               // for flat,
     *                  | ^               // for sharp
     *     letter ::= [A-G]
     *     octave ::= '                   // to raise one octave
     *              | ,                   // to lower one octave
     *     duration ::= empty string      // for 1-beat duration
     *                | /n                // for 1/n-beat duration
     *                | n                 // for n-beat duration
     *                | n/m               // for n/m-beat duration
     * </pre>
     * <p> Examples (assuming 4/4 common time, i.e. 4 beats per measure):
     *     C     quarter note, middle C
     *     A'2   half note, high A
     *     _D/2  eighth note, middle D flat
     * 
     * @param notes string of notes and rests in simplified abc notation given above
     * @param instr instrument to play the notes with
     * @return the music in notes played by instr
     */
    public static Music notes(String notes, Instrument instr) {
        Music music = rest(0);
        for (String sym : notes.split("[\\s|]+")) {
            if (!sym.isEmpty()) {
                music = concat(music, parseSymbol(sym, instr));
            }
        }
        return music;
    }

    /* Parse a symbol into a Note or a Rest. */
    private static Music parseSymbol(String symbol, Instrument instr) {
        Matcher m = Pattern.compile("([^/0-9]*)([0-9]+)?(/[0-9]+)?").matcher(symbol);
        if (!m.matches()) throw new IllegalArgumentException("couldn't understand " + symbol);

        String pitchSymbol = m.group(1);

        double duration = 1.0;
        if (m.group(2) != null) duration *= Integer.valueOf(m.group(2));
        if (m.group(3) != null) duration /= Integer.valueOf(m.group(3).substring(1));

        if (pitchSymbol.equals(".")) return rest(duration);
        else return note(duration, parsePitch(pitchSymbol), instr);
    }

    /* Parse a symbol into a Pitch. */
    private static Pitch parsePitch(String symbol) {
        if (symbol.endsWith("'"))
            return parsePitch(symbol.substring(0, symbol.length()-1)).transpose(OCTAVE);
        else if (symbol.endsWith(","))
            return parsePitch(symbol.substring(0, symbol.length()-1)).transpose(-OCTAVE);
        else if (symbol.startsWith("^"))
            return parsePitch(symbol.substring(1)).transpose(1);
        else if (symbol.startsWith("_"))
            return parsePitch(symbol.substring(1)).transpose(-1);
        else if (symbol.length() != 1)
            throw new IllegalArgumentException("can't understand " + symbol);
        else
            return new Pitch(symbol.charAt(0));
    }

    /**
     * @param duration duration in beats, must be >= 0
     * @param pitch pitch to play
     * @param instrument instrument to use
     * @return pitch played by instrument for duration beats
     */
    public static Music note(double duration, Pitch pitch, Instrument instrument) {
        return new Note(duration, pitch, instrument);
    }

    /**
     * @param duration duration in beats, must be >= 0
     * @return rest that lasts for duration beats
     */
    public static Music rest(double duration) {
        return new Rest(duration);
    }

    ////////////////////////////////////////////////////
    // Functional objects
    ////////////////////////////////////////////////////

    // TODO

    ////////////////////////////////////////////////////
    // Generic functions
    ////////////////////////////////////////////////////

    // TODO compose : (T->U) x (U->V) -> (T->V)
    public static <T,U,V> Function<T,V> compose(Function<T,U> f, Function<U,V> g) {
        return (t) -> g.apply(f.apply(t));
    }
    
    //Series: Function<Music, Music> transform x int repeats x Music m x Function<two Musics, Music> -> Music
    
    ////////////////////////////////////////////////////
    // Producers
    ////////////////////////////////////////////////////

    /**
     * @param m1 first piece of music
     * @param m2 second piece of music
     * @return m1 followed by m2
     */
    public static Music concat(Music m1, Music m2) {
        return new Concat(m1, m2);
    }

    /**
     * Transpose all notes upward or downward in pitch.
     * @param m music
     * @param semitonesUp semitones by which to transpose
     * @return m' such that for all notes n in m, the corresponding note n' in m'
     *         has n'.pitch() == n.pitch().transpose(semitonesUp), and m' is
     *         otherwise identical to m
     */
    public static Music transpose(Music m, int semitonesUp) {
        return m.accept(new TransposeVisitor(semitonesUp)); 
    }

    public static Music together(Music m1, Music m2) {
        return null; // TODO how could we implement this?
    }

    public static Music delay(Music m, double delay) {
        return concat(rest(delay), m);
    }

    //
    // TODO round : Music x ??? -> Music
    //
    public static Music round(Music m, double delay, int numRounds) {
        if (numRounds == 1){
            return m;
        }
        // else recursive case
        // return ???
        //return together(m, round(delay(m, delay), delay, numRounds-1));
        //return round(together(m, delay(m, delay*numRounds)), delay, numRounds - 1);
        //return canon(m, delay, Function.identity(music), numRounds);
        return canon(m, delay, music -> music, numRounds);
        
    }

    // TODO repeat : Music x ??? -> Music
    public static Music repeat(Music m, Function<Music, Music> f, int numRounds) {
        return canon(m, m.duration(), f, numRounds);
        // if base case
        return m;
        // else recursive case
        // return ???
    }
    
    
}
