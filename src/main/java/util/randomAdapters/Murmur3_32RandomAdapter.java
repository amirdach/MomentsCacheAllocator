package util.randomAdapters;

import com.google.common.hash.Hashing;

public class Murmur3_32RandomAdapter implements IRandomAdapter{
    @Override
    public int nextInt(long seed, int bound) {
        return Hashing.murmur3_32().hashLong(seed).asInt() % bound;
    }
}
