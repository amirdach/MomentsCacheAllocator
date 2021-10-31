package util.randomAdapters;

public class CustomRandomAdapter implements IRandomAdapter{

    @Override
    public int nextInt(long seed, int bound) {
        return (int)(a * seed + b) % bound;
    }
    private int a = 3;
    private int b = 5;

}
