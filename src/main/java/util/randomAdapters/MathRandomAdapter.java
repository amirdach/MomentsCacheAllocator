package util.randomAdapters;

import org.apache.commons.math3.random.RandomDataGenerator;

public class MathRandomAdapter implements IRandomAdapter{
    public MathRandomAdapter(){
        rnd = new RandomDataGenerator();
    }
    @Override
    public int nextInt(long seed, int bound) {
        rnd.reSeed(seed);
        return rnd.nextInt(0,bound);
    }
    private RandomDataGenerator rnd;
}
