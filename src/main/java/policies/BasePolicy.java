package policies;

import settings.CacheSettings;

public abstract class BasePolicy implements IPolicy{
    protected int size;
    private CacheSettings settings;

    public BasePolicy(CacheSettings settings){
        this.settings = settings;
    }

    @Override
    public abstract void update(long key);
}
