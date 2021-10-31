package factories;

import policies.moments.Counter;
import policies.moments.CounterEstimator;
import policies.moments.ICounter;

public interface ICounterFactory {
    ICounter generate();
}

