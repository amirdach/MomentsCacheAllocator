package tuners.moments;

import java.util.ArrayList;
import java.util.List;

public class F2 implements IF2{
    public void addKey(long key){
        counter += hash(key);
    }
    public int getHomogeneityMeasurement(){
        int result = (int)Math.pow(counter,2);
        //System.out.println("F2: " + counter);
        addValue(result);
        return result;
    }

    @Override
    public void reset() {
        counter = 0;
    }

    private int hash(long key){
        long reminder = key % 2;
        return reminder == 1? 1:-1;
    }

    private void addValue(double val){
        valuesHistory.add(val);
    }
    public List<Double> getValuesHistory(){
        return valuesHistory;
    }
    private List<Double> valuesHistory = new ArrayList<>();
    private int counter = 0;
}
