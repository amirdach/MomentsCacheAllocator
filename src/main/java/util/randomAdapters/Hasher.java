package util.randomAdapters;

public class Hasher {
    // Code downloaded from Guava source code and adapted to work with long instead on an int
    // Source : https://github.com/google/guava/blob/920416b3ed43bc360dd446ea58158048c0b5d80e/android/guava/src/com/google/common/hash/Hashing.java#L536
    public static long consistentHash(long input, long buckets) {
        LinearCongruentialGenerator generator = new LinearCongruentialGenerator(input);
        long candidate = 0;
        long next;

        // Jump from bucket to bucket until we go out of range
        while (true) {
            next = (long) ((candidate + 1) / generator.nextDouble());
            if (next >= 0 && next < buckets) {
                candidate = next;
            } else {
                return candidate;
            }
        }
    }
    private static final class LinearCongruentialGenerator {
        private long state;

        public LinearCongruentialGenerator(long seed) {
            this.state = seed;
        }

        public double nextDouble() {
            state = 2862933555777941757L * state + 1;
            return ((double) ((long) (state >>> 33) + 1)) / 0x1.0p31;
        }
    }
}
