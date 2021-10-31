package cache.managers;
import javafx.util.Pair;
import settings.CacheSettings;
import tuners.ISizeTuner;

import java.util.*;

public class LfuCache implements ICache{
    private final String name;
    private final int maxCacheSize;
    private final ISizeTuner sizeTuner;
    private final long slotLength;
    private int currentCacheSize;
    private Map<Long,Integer> blockIdToFrequency;
    private ArrayList<Long> cache;
    private int iterationCount=0;

    public LfuCache(CacheSettings settings){
        blockIdToFrequency = new HashMap<>();
        cache = new ArrayList<>();
        this.name = settings.name;
        this.maxCacheSize = settings.cacheSize;
       this.currentCacheSize = this.maxCacheSize;
        this.slotLength = settings.slotLength;
        this.sizeTuner = settings.sizeTuner;
    }
    @Override
    public void update(long candidateKey) {
        if(!isContained(candidateKey)){
            if(cache.size() < currentCacheSize){
                cache.add(candidateKey);
            }else{
                replaceBlockIfNeeded(candidateKey);
            }
        }
        sizeTuner.addKey(candidateKey);
        ++iterationCount;
        if(iterationCount == slotLength){
            iterationCount=0;
            currentCacheSize = sizeTuner.getEstimatedSize();
            adjustCacheSize();
        }
        blockIdToFrequency.put(candidateKey,blockIdToFrequency.getOrDefault(candidateKey,0) + 1);

    }

    private void replaceBlockIfNeeded(long candidateKey) {
        int candidateFreq = blockIdToFrequency.getOrDefault(candidateKey,0);
        Pair<Long,Integer> candidateToBeEvictedIdAndFreq = getCandidateToBeEvicted();
        if(candidateToBeEvictedIdAndFreq.getValue()>candidateFreq){
            return;
        }
        cache.remove(candidateToBeEvictedIdAndFreq.getKey());
        cache.add(candidateKey);
    }

    private Pair<Long, Integer> getCandidateToBeEvicted() {
        Long minKey = cache.stream().min(
                Comparator.comparingLong((Long k) -> blockIdToFrequency.getOrDefault(k, 0))
        ).get();
        return new Pair<>(minKey, blockIdToFrequency.getOrDefault(minKey, 0));
    }

    private void adjustCacheSize() {
        while (cache.size() > currentCacheSize){
            cache.remove(getCandidateToBeEvicted().getKey());
        }
    }

    @Override
    public boolean isContained(long key) {
        return cache.contains(key);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void finish() {
        sizeTuner.finish();
    }
}
