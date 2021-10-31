package tests;

import traces.Synthetic;
import tuners.moments.F2;
import tuners.moments.IF2;

public class F2Tester {
    public void test(){
        long maxValueOfKey = 10000 ;
        long traceLength = 1000 * 1000 * 5;
        double traceSquaredLength = Math.pow(traceLength,2);
        double skewFactor = 2;
        int skewedHomogeneity = calcHomogeneity(maxValueOfKey, traceLength, skewFactor);
        skewFactor = 0.4;
        int uniformHomogeneity = calcHomogeneity(maxValueOfKey, traceLength, skewFactor);
        System.out.println("skewedHomogeneity = " + skewedHomogeneity + ", " + 1000 * (skewedHomogeneity/traceSquaredLength));
        System.out.println("uniformHomogeneity = " + uniformHomogeneity+ ", " + + 1000 * (uniformHomogeneity/traceSquaredLength));
        if( skewedHomogeneity > uniformHomogeneity){
            System.out.println("Test passed");
        }else{
            System.out.println("Test failed!!!!!!!!!!!!!!!!!!");
        }
    }

    private int calcHomogeneity(long maxValueOfKey, long traceLength, double skewFactor) {
        IF2 f2 = new F2();
        Synthetic.zipfian(maxValueOfKey, skewFactor, traceLength).forEach(key -> {
            f2.addKey(key);
        });
        return f2.getHomogeneityMeasurement();
    }
}
