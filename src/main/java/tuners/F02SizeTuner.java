package tuners;

import reports.GraphLogger;
import reports.IndexBasedGraphLogger;
import tuners.moments.IF0;
import tuners.moments.IF2;
import util.Utils;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class F02SizeTuner implements ISizeTuner{
    // Maybe HillClimbing?
    // Exponential back-off with previous weights?
    // Predicting the size of the work set
    // ---------- F2 ---------------------
    // The bigger F2 value is the narrower the normal distribution of the trace.
    // Uniform distribution -> Maximum cache size is needed
    // Skewer normal distribution -> Less cache size is needed

    // ---------- F0 ---------------------
    // The more distinct elements the less cache size is needed and vice versa


    // Idea 1: Define the function using the edge case, when should I assign the maximum size? when the minimum?
    //         Maybe output [0,1] and multiply it with max_cache_size.
    // Idea 2: Create a function from (F0,F2) -> [0,1]. split the output interval into buckets.
    //        In each bucket save the best (cache_size, hit_ratio) and use it as a cache with p probability
    //        and with 1-p explore other cache_size.
    private IF0 f0;
    private double f0Weight = 0.5;
    private double f2Weight = 0.5;
    private IF2 f2;
    private double currentWeight = 0.7;
    private int maximumCacheSize;
    private long slotLength;
    private String name;
    private int pastAccumulatedSize = 0;
    private IndexBasedGraphLogger log = new IndexBasedGraphLogger();
    public F02SizeTuner(TunerSettings settings){
        maximumCacheSize = settings.cacheSize;
        currentWeight = settings.currentIterationWeight;
        slotLength = settings.slotLength;
        f0 = settings.f0;
        f2 = settings.f2;
        this.name = settings.name;
    }
    public void setLastHitRatio(double hitRatio){

    }
    private void addToGraph(int y){
        log.addValue(y);
    }

    @Override
    public int getEstimatedSize() {
        // TODO - remove these 2 lines
        double distinctItemsCount = f0.getDistinctItemsCount();
        double distinctItemFrac = Utils.mapTo01Range(distinctItemsCount / (double)slotLength);
        int homogeneityMeasurement = f2.getHomogeneityMeasurement();
        // result is less than a zero! left calculation is bigger than 1!
        double homogeneityMeasurementFrac = 1 - normalizeValue(homogeneityMeasurement / Math.pow(slotLength,2));
        //double homogeneityMeasurementFrac = 1 - Utils.mapTo01Range(homogeneityMeasurement / (double)slotLength);
//        System.out.println("F0 = " + distinctItemFrac);
//        System.out.println("F2 = " + homogeneityMeasurementFrac);
        double grade = f0Weight * distinctItemFrac + f2Weight * homogeneityMeasurementFrac;
        int newCacheSize = (int)(maximumCacheSize * grade);
        //System.out.println("grade = " +grade +", newCacheSize = " + newCacheSize);
        f0.reset();
        f2.reset();
        // Exponential backoff
        if(pastAccumulatedSize==0){
            pastAccumulatedSize = newCacheSize;
        }else{
            newCacheSize = (int)Math.ceil(currentWeight * newCacheSize + (1-currentWeight) * pastAccumulatedSize);
        }
        addToGraph(newCacheSize);
        return newCacheSize;
    }

    private double normalizeValue(double val) {
//        while(Double.compare(val,0.1)<0){
//            val *= 10;
//        }
//        return val;
        return val * 1000 > 1? 1: val *1000;
    }

    @Override
    public void addKey(long key) {
        f0.addKey(key);
        f2.addKey(key);
    }

    @Override
    public void reset() {
        f0.reset();
        f2.reset();
    }

    @Override
    public IF2 getF2() {
        return f2;
    }

    @Override
    public IF0 getF0() {
        return f0;
    }
    @Override
    public void finish(){
        log.buildGraph(name,"index","Suggested cache size");
    }
}
