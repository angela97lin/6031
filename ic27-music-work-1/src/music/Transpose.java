package music;

public class Transpose implements Music {

    private final Music music;
    int transposeBy;
    
    public Transpose(Music music, int transposeBy){
        this.music = music;
        this.transposeBy = transposeBy;
    }
    
    @Override
    public double duration() {
        return music.duration();
    }

    @Override
    public void play(SequencePlayer player, double atBeat) {
        music.play(new TransposePlay(player, transposeBy), atBeat);
        /*
        music.play(new SequencePlayer(){

            @Override
            public void addNote(Instrument instr, Pitch pitch, double startBeat, double numBeats) {
                player.addNote(instr, pitch.transpose(transposeBy), startBeat, numBeats);
                
            }

            @Override
            public void play() {
                player.play();
            }
            
        }, atBeat);
         */
    }

}
