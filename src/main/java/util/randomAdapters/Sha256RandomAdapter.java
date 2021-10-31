package util.randomAdapters;

import com.google.common.hash.Hashing;

public class Sha256RandomAdapter implements IRandomAdapter{
    @Override
    public int nextInt(long seed, int bound) {
        return Hashing.sha256().hashLong(seed).asInt() % bound;
    }
}
