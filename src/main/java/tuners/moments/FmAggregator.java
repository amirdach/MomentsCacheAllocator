package tuners.moments;

import java.util.ArrayList;
import java.util.List;

public class FmAggregator implements IF0{
    public FmAggregator(List<Fm> moments){
        this.moments = moments;
    }
    @Override
    public void addKey(long key) {
        for (Fm moment:moments) {
            moment.addKey(key);
        }
    }

    @Override
    public double getDistinctItemsCount() {
        double lsbZeroSum = 0.0;
        for (Fm moment:moments) {
            lsbZeroSum += moment.getLsbZeroBitIndex();
            //System.out.println(moment.getName() + ": " + moment.getLsbZeroBitIndex());
        }
        double lsbZeroAvg = lsbZeroSum / moments.size();
        //System.out.println("Final power: " + lsbZeroAvg);
        double temp = Math.pow(2,lsbZeroAvg) / divisionFactor;
        double result = Math.floor(temp);
        addValue(result);
        return result;
    }

    @Override
    public void reset() {
        for (Fm f0:moments) {
            f0.reset();
        }
    }
    private void addValue(double val){
        valuesHistory.add(val);
    }
    public List<Double> getValuesHistory(){
        return valuesHistory;
    }
    private List<Double> valuesHistory = new ArrayList<>();
    private List<Fm> moments;
    private final double divisionFactor = 0.77351;
}
