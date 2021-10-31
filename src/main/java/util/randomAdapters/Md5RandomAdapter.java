package util.randomAdapters;

import com.google.common.hash.Hashing;

public class Md5RandomAdapter implements IRandomAdapter{
    @Override
    public int nextInt(long seed, int bound) {
        return Hashing.md5().hashLong(seed).asInt() % bound;
    }
}
