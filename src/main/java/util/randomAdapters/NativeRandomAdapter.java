package util.randomAdapters;

import java.util.Random;

public class NativeRandomAdapter implements IRandomAdapter{
    public NativeRandomAdapter(){
        rnd = new Random();
    }
    @Override
    public int nextInt(long seed,int bound) {
        rnd.setSeed(seed);
        return rnd.nextInt(bound);
    }

    private Random rnd;
}
