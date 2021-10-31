package tuners;

import tuners.moments.IF0;
import tuners.moments.IF2;

public class TunerSettings {
    public int cacheSize;
    public double currentIterationWeight;
    public long slotLength;
    public IF0 f0;
    public IF2 f2;
    public String name;
    public TunerSettings(int cacheSize, double currentIterationWeight, long slotLength, IF0 f0,IF2 f2, String name) {
        this.cacheSize = cacheSize;
        this.currentIterationWeight = currentIterationWeight;
        this.slotLength = slotLength;
        this.f0 = f0;
        this.f2 = f2;
        this.name = name;
    }
}
