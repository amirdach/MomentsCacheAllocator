package cache.managers;

import factories.ICounterFactory;
import policies.moments.ICounter;
import settings.CacheSettings;
import tuners.ISizeTuner;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class LruCache implements ICache{
    private Map<Long,ICounter> counters;
    private Queue<Long> cache;
    private final long maxCacheSize;
    private final ICounterFactory counterFactory;
    private final String name;
    private final ISizeTuner sizeTuner;
    private final long slotLength;
    private int iterationCount=0;
    private long currentCacheSize;
    public LruCache(CacheSettings settings) {
        counters = new HashMap<>();
        maxCacheSize = settings.cacheSize;
        currentCacheSize = maxCacheSize;
        cache = new LinkedList<>();
        counterFactory = settings.counterFactory;
        name = settings.name;
        slotLength = settings.slotLength;
        sizeTuner = settings.sizeTuner;
    }

    @Override
    public void update(long candidateKey) {
        if (!isContained(candidateKey)) {
            if(cache.size() < currentCacheSize){
                admit(candidateKey);
                counters.put(candidateKey,counterFactory.generate());
                return;
            }
            long lruKey = cache.peek();
            long lruFrequency = counters.containsKey(lruKey)?counters.get(lruKey).get():0;
            long candidateFrequency = counters.containsKey(candidateKey)?counters.get(candidateKey).get():0;
            if(lruFrequency < candidateFrequency){
                evict(lruKey);
                admit(candidateKey);
            }
        }else{
            cache.remove(candidateKey);
            cache.offer(candidateKey);
        }
        sizeTuner.addKey(candidateKey);
        if(counters.containsKey(candidateKey)){
            counters.get(candidateKey).increment();
        }else{
            counters.put(candidateKey,counterFactory.generate());
        }
        ++iterationCount;
        if(iterationCount == slotLength){
            iterationCount=0;
            currentCacheSize = sizeTuner.getEstimatedSize();
            adjustCacheSize();
        }
    }

    @Override
    public boolean isContained(long key) {
        boolean isBlockExist = cache.contains(key);
        return isBlockExist;
    }

    @Override
    public String getName() {
        //return getClass().getName() + "_" + counters.getClass().getName();
        return name;
    }

    @Override
    public void finish() {
        sizeTuner.finish();
    }

    private void adjustCacheSize() {
        while (cache.size() > currentCacheSize){
            cache.poll();
        }
    }
    private void admit(long key) {
        cache.add(key);
    }
    private void evict(long key) {
        cache.remove(key);
    }

}
