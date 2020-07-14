package music;

/**
 * Represents a function to transpose music upward or downward in pitch.
 */
public class TransposeVisitor implements Music.Visitor<Music> {
    
    private final int shift;
    public TransposeVisitor(int semitonesUp) {
        this.shift = semitonesUp;
    }

    @Override
    public Music on(Concat concat) {
        return new Concat(concat.first().accept(this), concat.second().accept(this));
    }

    @Override
    public Music on(Note note) {
        return new Note(note.duration(), note.pitch().transpose(this.shift), note.instrument());
    }

    @Override
    public Music on(Rest rest) {
        return rest;
    }

}
