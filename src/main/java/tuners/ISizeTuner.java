package tuners;

import tuners.moments.IF0;
import tuners.moments.IF2;

public interface ISizeTuner {
    int getEstimatedSize();
    default void addKey(long key){}
    default void reset(){}
    default void reportGraph(){}
    default IF0 getF0(){return null;}
    default IF2 getF2(){return null;}
    default void finish(){}
}
