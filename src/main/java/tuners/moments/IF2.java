package tuners.moments;

import java.util.ArrayList;
import java.util.List;

// The less uniform the distribution is the bigger the value should be
public interface IF2 {
    void addKey(long key);
    int getHomogeneityMeasurement();
    void reset();
    default List<Double> getValuesHistory(){return new ArrayList<>();}
}
