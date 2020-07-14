import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

// Original painter
// Don't change me, change the versions below
class Painter {
    
    private Set<Brush> brushes;
    private Palette palette;
    
    public Painter(Set<Brush> brushes) {
        this.brushes = brushes;
        this.palette = new Palette();
    }
    public void mix() {
        // mixing
    }
    
    public void paint() {
        // painting
    }
}

class Brush {
    // ...
}

class Palette {    
    // ...
}

///////////////////////////////////////////////////////

// ImmutablePainter is immutable
class ImmutablePainter {
    // TODO: change some of the code in this class

    private final Set<Brush> brushes;
    private final Palette palette;
    
    // Thread safety argument:
    //    this.brushes is an unmodifiable, synchronized set which we have made 
    //    through defensive copying, so it is safe from mutation and observer
    //    methods are threadsafe
    //    brushes and palette are immutable and private final

    public ImmutablePainter(Set<Brush> brushes) {
        //defensive copy of brushes 
        Set<Brush> brushesCopy = new HashSet<Brush>();
        for (Brush brush : brushes) {
            brushesCopy.add(brush);
        }

        this.brushes = Collections.synchronizedSet(Collections.unmodifiableSet(brushesCopy));
        this.palette = new Palette();
    }
    public void mix() {
        // mixing
    }
    public void paint() {
        // painting
    }
}

// MonitorPainter implements the monitor pattern
class MonitorPainter {
    // TODO: change some of the code in this class
    
    private final Set<Brush> brushes;
    private final Palette palette;
    
    // Thread safety argument:
    //    rep is private final
    //    We have used the monitor pattern to protect all accesses to the rep
    //    by lock on this; all methods are synchronized
    //    Brush objects are threadsafe.
    
    
    public MonitorPainter(Set<Brush> brushes) {
        Set<Brush> brushesCopy = new HashSet<Brush>();
        for (Brush brush : brushes) {
            brushesCopy.add(brush);
        }
        //synchronizedSet technically not necessary??
        this.brushes = Collections.synchronizedSet(brushesCopy);        
        this.palette = new Palette();
    }
    public synchronized void mix() {
        // mixing
    }
    public synchronized void paint() {
        // painting
    }
}

// LockPainter protects all access to its rep using the lock on brushes
class LockPainter {
    // TODO: change some of the code in this class
    
    private final Set<Brush> brushes;
    private final Palette palette;
    
    // Thread safety argument:
    //   rep is private final 
    //   all accesses to brushes and palette happen within LockPainter methods,
    //   which are all guarded by LockPainter's lock

    
    public LockPainter(Set<Brush> brushes) {
        Set<Brush> brushesCopy = new HashSet<Brush>();
        for (Brush brush : brushes) {
            brushesCopy.add(brush);
        }

        this.brushes = Collections.synchronizedSet(Collections.unmodifiableSet(brushesCopy));        
        this.palette = new Palette();
    }
    public void mix() {
        // mixing
    }
    public void paint() {
        // painting
    }
}

// NoLocksPainter knows that Brush and Palette are threadsafe, so doesn't use locks
class NoLocksPainter {
    // TODO: change some of the code in this class
    
    private final Set<Brush> brushes;
    private final Palette palette;
    
    // Note: any relationship between brushes and palette can break rep invariant/make not threadsafe
    // even though Brush and Palette are threadsafe
    //thus, must state no relationship
    // Thread safety argument:
    //    all parts of rep are threadsafe
    
    public NoLocksPainter(Set<Brush> brushes) {
        this.brushes = Collections.synchronizedSet(new HashSet<>(brushes));//threadsafe defensive copying
        this.palette = new Palette();
    }
    public void mix() {
        // mixing
    }
    public void paint() {
        // painting
    }
}
