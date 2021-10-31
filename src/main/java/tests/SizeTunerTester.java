package tests;

import reports.GraphLogger;
import traces.Synthetic;
import tuners.F02SizeTuner;
import tuners.ISizeTuner;
import tuners.TunerSettings;
import tuners.moments.*;
import util.randomAdapters.Murmur3_32RandomAdapter;
import util.randomAdapters.NativeRandomAdapter;
import util.randomAdapters.Sha256RandomAdapter;
import util.randomAdapters.SipRandomAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class SizeTunerTester {
    // Test the correlation between the distribution to the suggested size.
    private static long maxValueOfKey = 1000 * 1000;
    private static long traceLength = 500 * 1000;
    private static int cacheSize = 1000;
    private static int phaseSlotLength =10 * 1000;
    public static void test(Stream<Double> skews, ISizeTuner sizeTuner){
        GraphLogger logger = new GraphLogger();

        skews.forEach(skew->{
            double size = runTrace(skew,sizeTuner);
            sizeTuner.reset();
            logger.addPoint(skew,size);
            System.out.println("(" + skew +"," + size+")");
            System.out.println("------------------------------------------");
        });
        logger.buildGraph("Estimated size","Skew factor","Size");
    }

    public static void testGraduallySkews(){
        ISizeTuner sizeTuner = createSizeTuner();
        DoubleStream.Builder builder = DoubleStream.builder();
        double skew = 0.0, delta = 0.01;
        for (int i = 0; i < 100; i++) {
            builder.add(skew);
            skew += delta;
        }
        List<Double> skews = builder.build().boxed().collect(toList());
         test(skews.stream(),sizeTuner);
         sizeTuner.reportGraph();
        reportGraphs(sizeTuner.getF0(),sizeTuner.getF2(),skews);
    }

    private static double runTrace(double skewFactor,ISizeTuner sizeTuner){
        Synthetic.zipfian(maxValueOfKey,skewFactor,traceLength).forEach(key -> {
            sizeTuner.addKey(key);
        });
        return sizeTuner.getEstimatedSize();
    }

    private static ISizeTuner createSizeTuner(){
        IF0 f0= createAggregatedF0Policy(maxValueOfKey,traceLength);
        return new F02SizeTuner(new TunerSettings(cacheSize,0.7,phaseSlotLength,f0, new F2(),"Tester"));
    }

    private static IF0 createAggregatedF0Policy(long maxValueOfKey, long traceLength) {
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
    private static int log2(long N) {
        int result = (int)(Math.log(N) / Math.log(2));
        return result;
    }

    public static void reportGraphs(IF0 f0, IF2 f2,List<Double> xValues){
        //log.buildGraph("Estimated size","Skew factor","Size");
        reportMomentGraph(f0.getClass().getName(),xValues,f0.getValuesHistory());
        reportMomentGraph(f2.getClass().getName(), xValues,f2.getValuesHistory());
    }
    private static void reportMomentGraph(String name, List<Double> xValues,List<Double> yValues){
        GraphLogger graph = new GraphLogger();
        graph.addXValues(xValues);
        graph.addYValues(yValues);
        graph.buildGraph(name,"Moment value","size");
    }
}
