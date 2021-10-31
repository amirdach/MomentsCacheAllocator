package main;

import policies.CacheManager;
import reports.distributions.DistributionLogger;
import traces.Synthetic;

import java.util.Arrays;
import java.util.List;

public final class Executer {
//https://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.435.150&rep=rep1&type=pdf
    // Cliff hanger
    // TODO - refactor and extract a module for recording the distribution, then use it in Caffeine
    public static void main(String[] argv){
//        CounterEstimator ce = new CounterEstimator();
//        for (int i = 0; i < 50; i++) {
//            ce.increment();
//            System.out.println(ce.get());
//        }
// ----------------- Map to [0,1] range tester-----------------------------
//        for (int i = 0; i < 10; i++) {
//            Random rnd = new Random();
//            double x = rnd.nextDouble() * 3;
//            double y = Utils.mapTo01Range(x);
//            System.out.println("("+x+"," +y+")");
//        }
// ----------------- F0 tester-----------------------------

//        for (int i = 0; i < 100; i++) {
//            F0Tester tester = new F0Tester();
//            tester.test();
//        }
//        F0Tester.printMinMax();
//        F0Tester f0Tester= new F0Tester();
//        f0Tester.test();
// ----------------- F2 tester-----------------------------

//        for (int i = 0; i < 100; i++) {
//            F2Tester f2Tester = new F2Tester();
//            f2Tester.test();
//        }
//

// ----------------- Fm tester-----------------------------
        //SizeTunerTester.testGraduallySkews();

//        boolean flag = true;
//        while (flag);
        // TODO - Add regular manager with half cache size. +
        // TODO - Show the correlation between the trace distribution and the result of F0 and F2.
        // TODO - Run multiple traces to check the results.
        // TODO - Search and add more Moments-based managers.
        // TODO - Start writing the summary and do a brief review on researches of the field.
        // TODO - Do some refactoring and clean the code.
        long maxValueOfKey = 1000 * 1000 ;
        long traceLength = 1000 * 1000 * 10 ;
        double skewFactor = 0.8;
        int cacheSize = 4096;
        long phaseSlotLength = 10000;
        DistributionLogger distLog = new DistributionLogger();
        List<CacheManager> managers = buildPolicies(cacheSize,traceLength, maxValueOfKey,phaseSlotLength);
        distLog.init(maxValueOfKey);
        Synthetic.zipfian(maxValueOfKey,skewFactor,traceLength).forEach(key -> {
            distLog.addKey(key);
            for (CacheManager manager: managers) {
                manager.update(key);
            }
        });

        for (CacheManager manager: managers) {
            System.out.println(manager.getName() + ": " + manager.getHitRatio());
            manager.finish();
        }

        distLog.buildHistogram();
    }
    private static List<CacheManager> buildPolicies(int cacheSize,long traceLength,long maxKey,long phaseSlotLength) {

        CacheManagersFactory factory = new CacheManagersFactory(traceLength,cacheSize,maxKey,phaseSlotLength);
        //return Arrays.asList(morrisManager,naiveManager,f02Cache);
        return Arrays.asList(
                //factory.createLRUMorris(),

                factory.createLRURegular(),
                factory.createLRUF0F2(),
                factory.createLRUCustomSizeRegular(cacheSize/2),

                factory.createLFURegular(),
                factory.createLFUF0F2(),
                factory.createLFUCustomSizeRegular(cacheSize/2)
        );
    }




}
