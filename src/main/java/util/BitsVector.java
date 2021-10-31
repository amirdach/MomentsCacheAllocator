package util;

import java.util.BitSet;

public class BitsVector {
    private final int size;
    private BitSet bits;
    public BitsVector(int size){
        this.size = size;
        bits = new BitSet(size);
    }
    public void clear(){
        bits.clear();
    }
    public void set(int index){
        bits.set(index,true);
    }
    public int getLsbZeroBitIndex(){
        return bits.nextClearBit(0) ;
    }
}
