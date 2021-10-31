package util.randomAdapters;

public class StubRandomAdapter implements IRandomAdapter{
    @Override
    public int nextInt(long seed, int bound) {
        return 30;
    }
}
