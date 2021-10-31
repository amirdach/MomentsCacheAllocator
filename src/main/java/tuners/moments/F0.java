package tuners.moments;

import java.util.Random;
// Check this Algorithm for F0, might be interesting to implement:
// source : https://people.eecs.berkeley.edu/~satishr/cs270/sp11/rough-notes/Streaming.pdf
public class F0 implements IF0{
    public void addKey(long key){
        double hashedKey = hash(key);
        if(Double.compare(hashedKey,X) < 0){
            X = hashedKey;
            //System.out.println("X : " + X);
        }
    }
    public double getDistinctItemsCount(){
        double result =  ((double)1/X);
        //System.out.println("F0: " + X);
        return result;
    }

    @Override
    public void reset() {
        X = 1.0;
    }

    // Try implementing according to:
    // https://courses.engr.illinois.edu/cs598csc/fa2014/Lectures/lecture_2.pdf
    private double hash(long key){
        //double val = (double)1 / (Hashing.consistentHash(key,150000) + 1);
        long temp = key;//(Hasher.consistentHash(key,10000) + 1);
        //System.out.println("temp = " + temp);
        //double val = (double)1 / (temp % 1000000);
//        double val = (Hasher.consistentHash(key,1000) + 1) ;
//        return val/ (double)1000;
        Random rnd = new Random(key);
        double res= rnd.nextDouble();
        //System.out.println("result =" + res);
        return res;
    }
    private double X=1.0;


}
