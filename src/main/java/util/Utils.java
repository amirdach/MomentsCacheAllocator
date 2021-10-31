package util;

public final class Utils {
    public static double mapTo01Range(double x){
        return 1/(1 + Math.pow(Math.E,-1*x));
    }
}
