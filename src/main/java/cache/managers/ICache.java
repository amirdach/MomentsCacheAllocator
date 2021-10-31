package cache.managers;

public interface ICache {
   void update(long key);
    boolean isContained(long key);
    String getName();
    default void finish(){}
}
