package tuners.moments;

import com.typesafe.config.Config;
import settings.CacheSettings;
import tuners.ISizeTuner;
import tuners.TunerSettings;

public class ConstantSizeTuner implements ISizeTuner {
    private int maximumCacheSize;
    public ConstantSizeTuner(TunerSettings settings){
        maximumCacheSize = settings.cacheSize;
    }
    @Override
    public int getEstimatedSize() {
        return maximumCacheSize;
    }

}
