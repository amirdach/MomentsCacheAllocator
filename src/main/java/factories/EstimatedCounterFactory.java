package factories;

import policies.moments.CounterEstimator;
import policies.moments.ICounter;

public class EstimatedCounterFactory implements ICounterFactory {

    @Override
    public ICounter generate() {
        return new CounterEstimator();
    }
}
