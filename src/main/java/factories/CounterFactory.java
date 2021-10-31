package factories;

import policies.moments.Counter;
import policies.moments.ICounter;

public class CounterFactory implements ICounterFactory {

    @Override
    public ICounter generate() {
        return new Counter();
    }
}
