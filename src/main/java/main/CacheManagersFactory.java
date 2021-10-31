package main;

import cache.managers.ICache;
import cache.managers.LfuCache;
import cache.managers.LruCache;
import factories.CounterFactory;
import factories.EstimatedCounterFactory;
import policies.CacheManager;
import settings.CacheSettings;
import tuners.F02SizeTuner;
import tuners.TunerSettings;
import tuners.moments.*;
import util.randomAdapters.Murmur3_32RandomAdapter;
import util.randomAdapters.NativeRandomAdapter;
import util.randomAdapters.Sha256RandomAdapter;
import util.randomAdapters.SipRandomAdapter;

import java.util.Arrays;
import java.util.List;

public class CacheManagersFactory {
    private final long traceLength;
    private final int cacheSize;
    private final long maxKey;
    private final int nonPhaseSlotLength;
    private final long phaseSlotLength;

    public CacheManagersFactory(long traceLength, int cacheSize,long maxKey,long phaseSlotLength){
        this.traceLength = traceLength;
        this.cacheSize = cacheSize;
        this.maxKey = maxKey;
        this.nonPhaseSlotLength = 500000;
        this.phaseSlotLength = phaseSlotLength;
    }

    //region LRU
    public CacheManager createLRUF0F2(){
        return createF0F2(CacheType.LRU,cacheSize);
    }
    public CacheManager createLRURegular(){
        return createCustomSizeRegular(CacheType.LRU,cacheSize);
    }
    public CacheManager createLRUCustomSizeRegular(int cacheSize){
        return createCustomSizeRegular(CacheType.LRU,cacheSize);
    }
    public CacheManager createLRUMorris(){
        return createMorris(CacheType.LRU,cacheSize);
    }
    //endregion

    //region LFU
    public CacheManager createLFURegular(){
        return createLFUCustomSizeRegular(cacheSize);
    }
    public CacheManager createLFUCustomSizeRegular(int cacheSize){
        return createCustomSizeRegular(CacheType.LFU,cacheSize);
    }
    public CacheManager createLFUF0F2(){
        return createF0F2(CacheType.LFU,cacheSize);
    }
    public CacheManager createLFUMorris(){
        return createMorris(CacheType.LFU,cacheSize);
    }
    //endregion

    private CacheManager createMorris(CacheType cacheType,int cacheSize){
        String name = getCacheName(cacheType)+"_Morris";
        CacheSettings morrisSettings = new CacheSettings(traceLength,cacheSize, maxKey,new EstimatedCounterFactory(),name
                ,new ConstantSizeTuner(new TunerSettings(cacheSize,1,nonPhaseSlotLength,null,null,name
        )),nonPhaseSlotLength);
        ICache cache = createCache(cacheType,morrisSettings);
        CacheManager morrisManager = new CacheManager(morrisSettings,cache);
        return morrisManager;
    }
    private CacheManager createF0F2(CacheType cacheType, int cacheSize){
        String name = getCacheName(cacheType) +"_F0F2";
        IF0 f0 = createAggregatedF0Policy(maxKey,traceLength);
        CacheSettings f02Settings = new CacheSettings(traceLength,cacheSize, maxKey,new CounterFactory(),name
                ,new F02SizeTuner(new TunerSettings(cacheSize,0.7,phaseSlotLength,f0, new F2(),name)),phaseSlotLength);
        ICache cache = createCache(cacheType,f02Settings);
        CacheManager f02Cache = new CacheManager(f02Settings,cache);
        return f02Cache;
    }
    private CacheManager createCustomSizeRegular(CacheType cacheType,int cacheSize){
        String name = "_Custom_Size_"+cacheSize;
        CacheSettings naiveSettings = new CacheSettings(traceLength,cacheSize, maxKey,new CounterFactory(),
                getCacheName(cacheType) + name
                ,new ConstantSizeTuner(new TunerSettings(cacheSize,1,nonPhaseSlotLength,null,null, name
        )),nonPhaseSlotLength);
        ICache cache = createCache(cacheType,naiveSettings);
        CacheManager naiveManager = new CacheManager(naiveSettings,cache);
        return naiveManager;
    }

    private String getCacheName(CacheType cacheType) {
        return cacheType==CacheType.LFU?"LFU":"LRU";
    }

    private ICache createCache(CacheType cacheType,CacheSettings settings){
        return cacheType==CacheType.LFU?new LfuCache(settings):new LruCache(settings);
    }
    private IF0 createAggregatedF0Policy(long maxValueOfKey, long traceLength) {
        int fmSize = (int)log2(Math.min(maxValueOfKey,traceLength)) + 5;
        List hashes = Arrays.asList(
                new Fm(fmSize, new NativeRandomAdapter()),
                //new Fm(fmSize, new MathRandomAdapter()),
                new Fm(fmSize,new SipRandomAdapter()),
                new Fm(fmSize,new Murmur3_32RandomAdapter()),
                new Fm(fmSize,new Sha256RandomAdapter()),
                new Fm(fmSize,new SipRandomAdapter())
                //new Fm(fmSize,new Md5RandomAdapter())
                //new Fm(fmSize,new StubRandomAdapter())
        );
        IF0 fm_aggregated = new FmAggregator(hashes);
        return fm_aggregated;
    }

    enum CacheType{LRU,LFU};
    private static int log2(long N)
    {
        int result = (int)(Math.log(N) / Math.log(2));
        return result;
    }
}
