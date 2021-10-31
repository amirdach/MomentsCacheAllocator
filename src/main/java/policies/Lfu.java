package policies;

import settings.CacheSettings;

public class Lfu extends BasePolicy {

    public Lfu(CacheSettings settings) {
        super(settings);
    }

    @Override
    public void update(long key) {

    }

}
