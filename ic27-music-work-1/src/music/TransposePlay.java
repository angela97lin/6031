package music;

public class TransposePlay implements SequencePlayer {

    private final int transposeBy;
    
    private SequencePlayer player;
    
    public TransposePlay(SequencePlayer player, int transposeBy){
        this.transposeBy = transposeBy;
        this.player = player;
        
    }
    @Override
    public void addNote(Instrument instr, Pitch pitch, double startBeat, double numBeats) {
        player.addNote(instr, pitch.transpose(transposeBy), startBeat, numBeats);
    }

    @Override
    public void play() {
        player.play();
    }

}
