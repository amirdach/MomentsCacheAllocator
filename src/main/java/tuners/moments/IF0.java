package tuners.moments;

import java.util.ArrayList;
import java.util.List;
// Future work : instead of Estimating distinct numbers we can estimate distinct shards
// The more uniform the distribution is the smaller the value is
public interface IF0 {
    void addKey(long key);
    double getDistinctItemsCount();
    void reset();
    default List<Double> getValuesHistory(){ return new ArrayList<>();}
}
