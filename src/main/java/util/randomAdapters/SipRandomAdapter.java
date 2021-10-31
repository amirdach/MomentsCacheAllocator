package util.randomAdapters;

import com.google.common.hash.Hashing;

public class SipRandomAdapter implements IRandomAdapter{
    @Override
    public int nextInt(long seed, int bound) {
        return Hashing.sipHash24().hashLong(seed).asInt() % bound;
    }
}
