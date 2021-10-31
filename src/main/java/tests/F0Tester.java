package tests;

import javafx.util.Pair;
import reports.GraphLogger;
import traces.Synthetic;
import tuners.moments.*;
import util.Utils;
import util.randomAdapters.*;

import java.util.*;

public class F0Tester {
    public void test(){
        //F0 f0 = new F0();
//        AMS ams = new AMS();
        HashSet<Long> realDistinct = new HashSet<>();

//        IntStream.range(0,7500).forEach(x-> {
//            f0.addKey(x);
//        });
//        IntStream.range(0,7500).forEach(x-> f0.addKey(x));
//        Random rnd = new Random();
//        for (int i = 0; i < 10000000; i++) {
//            long val = rnd.nextInt(100000);
//            System.out.println(val);
//            realDistinct.add(val);
//           // f0.addKey(val);
//            ams.offer(val);
//        }
//        //long distinctItems = f0.getDistinctItemsCount();
//        //System.out.println("Estimated: " + distinctItems);
//        System.out.println("Estimated2: " + ams.cardinality());
//        System.out.println("Real: " + realDistinct.size());
//        System.out.println("");

        double skewedSkewFactor = 0.7, uniformSkewFactor = 0.3;
        if(skewToMin==null){
            initMinMax(skewedSkewFactor, uniformSkewFactor);
        }
        long maxValueOfKey = 1000 * 1000 * 10 ;
        long traceLength =  100 * 1000 * 5;
        // NOTE: When using max instead of a min we get about ~8% failed tests but the normalized values are not in [0,1]
        // and often exceeds 1.
        // When using min we get about ~25% of failed tests but all the normalized values are in [0,1]
        int fmSize = (int)log2(Math.max(maxValueOfKey,traceLength)) + 5; // dangerous parsing!!

//        System.out.println("--------Regular implementation testing--------");
//        testAlgo(f0,maxValueOfKey,traceLength, skewedSkewFactor, uniformSkewFactor);

        // --- Fm native random algorithm test
//        System.out.println("--------Fm_nativeRandom implementation testing--------");
//        IF0 fm_nativeRandom = new Fm(fmSize, new NativeRandomAdapter());
//        testAlgo(fm_nativeRandom,maxValueOfKey,traceLength, skewedSkewFactor, uniformSkewFactor);

        // --- Fm math random algorithm test
//        System.out.println("--------Fm_mathRandom implementation testing--------");
//        IF0 fm_mathRandom = new Fm(fmSize, new MathRandomAdapter());
//        testAlgo(fm_mathRandom,maxValueOfKey,traceLength, skewedSkewFactor, uniformSkewFactor);

        // --- Fm aggregated algorithm test
        System.out.println("--------Fm_aggregated implementation testing--------");
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
        //testAlgo(fm_aggregated,maxValueOfKey,traceLength, skewedSkewFactor, uniformSkewFactor);
        testMultiple(fm_aggregated,maxValueOfKey,traceLength);
        // --- Fm custom algorithm test
//        System.out.println("--------Fm_custom implementation testing--------");
//        IF0 fm_custom = new Fm(fmSize, new CustomRandomAdapter());
//        testAlgo(fm_custom,maxValueOfKey,traceLength, skewedSkewFactor, uniformSkewFactor);
    }
    private List<Double> realDistinctHistory = new ArrayList<>();
    private void testMultiple(IF0 f0, long maxValueOfKey, long traceLength){
        double skew = 0.0, delta = 0.1;
        List<Double> skews = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            testWithSkew(f0,maxValueOfKey,traceLength,skew);
            skews.add(skew);
            skew += delta;
        }
        List<Double> yValues = f0.getValuesHistory();
        GraphLogger graph = new GraphLogger();
        graph.addXValues(skews);
        graph.addYValues(yValues);
        graph.buildGraphWithMultipleSeries("F0","Skews","f0 values","real disticnt",skews,realDistinctHistory);
    }
    private void testWithSkew(IF0 f0, long maxValueOfKey, long traceLength,double skew){
        Synthetic.zipfian(maxValueOfKey, skew, traceLength).forEach(key -> {
            f0.addKey(key);
            realDistincts.add(key);
        });
        realDistinctHistory.add((double)realDistincts.size());
        realDistincts.clear();
        double estimatedDistinct = f0.getDistinctItemsCount();
        System.out.println("(" + skew + "," + estimatedDistinct +")");
        f0.reset();
    }
    private void testAlgo(IF0 f0, long maxValueOfKey, long traceLength, double skewedSkewFactor, double uniformSkewFactor){
        double traceSquaredLength = Math.pow(traceLength,2);
        Pair<Double, Integer> skewedDistinct = calcDistinct(maxValueOfKey, traceLength, skewedSkewFactor,f0);
        Pair<Double, Integer> uniformDistinct = calcDistinct(maxValueOfKey, traceLength, uniformSkewFactor,f0);
        System.out.println("[Real:"+skewedDistinct.getValue() +"] "+"skewedDistinct = " + skewedDistinct.getKey() + ", "
                + Utils.mapTo01Range(skewedDistinct.getKey() / traceLength));
        System.out.println("[Real:"+uniformDistinct.getValue() +"] "+"uniformDistinct = " + uniformDistinct.getKey()+ ", "
                + Utils.mapTo01Range(uniformDistinct.getKey()/traceLength));
        if( Double.compare(skewedDistinct.getKey(),uniformDistinct.getKey())<0){
            System.out.println("Test passed");
        }else{
            System.out.println("Test failed!!!!!!!!!!!!!!!!!!");
        }
    }
    private Pair<Double, Integer> calcDistinct(long maxValueOfKey, long traceLength, double skewFactor, IF0 f0) {
        realDistincts.clear();
        Synthetic.zipfian(maxValueOfKey, skewFactor, traceLength).forEach(key -> {
            f0.addKey(key);
            realDistincts.add(key);
        });
        double result = f0.getDistinctItemsCount();
        updateMinMax(skewFactor,result);
        return new Pair<Double,Integer>(result,realDistincts.size());
    }

    private void updateMinMax(double skewFactor, double result) {
        double currentMin = skewToMin.get(skewFactor);
        double newMin = Double.compare(currentMin,result)<0?currentMin:result;
        skewToMin.replace(skewFactor,newMin);

        double currentMax = skewToMax.get(skewFactor);
        double newMax = Double.compare(currentMax,result)>0?currentMax:result;
        skewToMax.replace(skewFactor,newMax);
    }

    private static void initMinMax(double skew1,double skew2){
        skewToMin = new HashMap<Double,Double>(){
            {
                put(skew1,Double.MAX_VALUE);
                put(skew2,Double.MAX_VALUE);
            }
        };
        skewToMax = new HashMap<Double,Double>(){
            {
                put(skew1,Double.MIN_VALUE);
                put(skew2,Double.MIN_VALUE);
            }
        };
    }
    public static void printMinMax(){
        for (Map.Entry<Double, Double> entry: skewToMin.entrySet()){
            System.out.println("Skew " + entry.getKey() +" min: " + entry.getValue());
        }
        for (Map.Entry<Double, Double> entry: skewToMax.entrySet()){
            System.out.println("Skew " + entry.getKey() +" max: " + entry.getValue());
        }
    }
    private int log2(long N)
    {

        // calculate log2 N indirectly
        // using log() method
        int result = (int)(Math.log(N) / Math.log(2));

        return result;
    }
    private static Map<Double,Double> skewToMin;
    private static Map<Double,Double> skewToMax;
    private final HashSet<Long> realDistincts = new HashSet<>();

}
