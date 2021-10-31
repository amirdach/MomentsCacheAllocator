package policies;

import cache.managers.ICache;
import cache.managers.LruCache;
import reports.PolicyStats;
import settings.CacheSettings;

public class CacheManager{
    private PolicyStats stats;
    private ICache cache;
    public CacheManager(CacheSettings settings, ICache cache) {
        //super(settings);
        this.cache = cache;
        stats = new PolicyStats(getName());
    }

    public void update(long key) {
        if(cache.isContained(key)){
            stats.recordHit();
        }else{
            stats.recordMiss();
        }
        cache.update(key);

    }
    public double getHitRatio(){
        return stats.hitRate();
    }
    public String getName(){
        return cache.getName();
    }
    public void finish(){
        cache.finish();
    }
}
