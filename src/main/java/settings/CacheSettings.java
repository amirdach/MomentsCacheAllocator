package settings;

import factories.ICounterFactory;
import policies.moments.ICounter;
import tuners.ISizeTuner;

public class CacheSettings {
    public long traceLength;
    public int cacheSize;
    public long elementMaxValue;
    public ICounterFactory counterFactory;
    public String name;
    public ISizeTuner sizeTuner;
    public long slotLength;
    public CacheSettings(long traceLength, int cacheSize, long elementMaxValue, ICounterFactory counterFactory,
                         String name,ISizeTuner sizeTuner, long slotLength){
        this.traceLength = traceLength;
        this.cacheSize = cacheSize;
        this.elementMaxValue = elementMaxValue;
        this.counterFactory = counterFactory;
        this.name = name;
        this.sizeTuner = sizeTuner;
        this.slotLength = slotLength;
    }
}
