package traces;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * The key and metadata for accessing a cache.
 *
 * @author ben.manes@gmail.com (Ben Manes)
 */
public class AccessEvent  implements Serializable {
    private final Long key;

    public AccessEvent(long key) {
        this.key = key;
    }

    /** Returns the key. */
    public Long key() {
        return key;
    }

    /** Returns the weight of the entry. */
    public int weight() {
        return 1;
    }

    /** Returns the hit penalty of the entry */
    public double hitPenalty() {
        return 0;
    }

    /** Returns the miss penalty of the entry */
    public double missPenalty() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof AccessEvent)) {
            return false;
        }
        AccessEvent event = (AccessEvent) o;
        return Objects.equals(key(), event.key())
                && Objects.equals(weight(), event.weight())
                && Objects.equals(hitPenalty(), event.hitPenalty())
                && Objects.equals(missPenalty(), event.missPenalty());
    }

    @Override
    public int hashCode() {
        return Objects.hash(key(), weight(), missPenalty(), hitPenalty());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("key", key())
                .add("weight", weight())
                .add("hit penalty", hitPenalty())
                .add("miss penalty", missPenalty())
                .toString();
    }

    /** Returns an event for the given key. */
    public static AccessEvent forKey(long key) {
        return new AccessEvent(key);
    }

    /** Returns an event for the given key and weight.
     * @return*/
    public static WeightedAccessEvent forKeyAndWeight(long key, int weight) {
        return new WeightedAccessEvent(key, weight);
    }

    /** Returns an event for the given key and penalties.
     * @return*/
    public static PenaltiesAccessEvent forKeyAndPenalties(long key, double hitPenalty, double missPenalty) {
        return new PenaltiesAccessEvent(key, hitPenalty, missPenalty);
    }

    private static final class WeightedAccessEvent extends AccessEvent {
        private final int weight;

        WeightedAccessEvent(long key, int weight) {
            super(cantorHashCode(key, weight));
            this.weight = weight;
            checkArgument(weight >= 0);
        }

        @Override
        public int weight() {
            return weight;
        }
    }

    /** Cantor pairing function. */
    private static long cantorHashCode(long key, int weight) {
        return (key + weight) * (key + weight + 1) / 2 + weight;
    }

    private static final class PenaltiesAccessEvent extends AccessEvent {
        private final double missPenalty;
        private final double hitPenalty;

        PenaltiesAccessEvent(long key, double hitPenalty, double missPenalty) {
            super(key);
            this.hitPenalty = hitPenalty;
            this.missPenalty = missPenalty;
            checkArgument(hitPenalty >= 0);
            checkArgument(missPenalty >= hitPenalty);
        }

        @Override
        public double missPenalty() {
            return missPenalty;
        }

        @Override
        public double hitPenalty() {
            return hitPenalty;
        }
    }
}
