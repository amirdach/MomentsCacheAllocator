package policies.moments;

import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.Random;

public class CounterEstimator implements ICounter{
    private BitSet counter;
    private final int bitsLength = 5;
    public CounterEstimator(){
        counter = new BitSet(bitsLength);
        for (int i = 0; i < bitsLength; i++) {
            counter.set(i,false);
        }
        increment();
    }
    // Ignoring carry for now
//    public void increment(){
//        if(isMaximized()){
//            return;
//        }
//        for (int i=0;i<bitsLength;++i){
//            if(counter.get(i)==false){
//                counter.set(i,true);
//                break;
//            }else{
//                counter.set(i,false);
//            }
//        }
//    }
    public void increment(){
        if(!shouldIncrement()){
            return;
        }
        int nextClearBitIndex = counter.nextClearBit(0);
        if(nextClearBitIndex >= bitsLength){
            return;
        }
        if(nextClearBitIndex>0){
            counter.set(0,nextClearBitIndex,false);
        }
        counter.flip(nextClearBitIndex);
    }

    private boolean isMaximized() {
        BitSet t = new BitSet(1);
        t.set(0,true);
        int x = t.nextClearBit(0);
        return true;
    }

    public long get(){
//        int x = ByteBuffer.wrap().getInt();
//        ByteBuffer wrapped = ByteBuffer.wrap(counter.toByteArray()); // big-endian by default
       try{
           long rawValue = getInternalValue();
            return (long) (Math.pow(2,rawValue) - 1);
       }catch (Exception ex){
           return 0;
       }
    }

    private boolean shouldIncrement() {
        Random rnd = new Random();
        double chance = 1 / Math.pow(2,getInternalValue());
        return rnd.nextDouble() < chance; // TODO - change this comparison to diff check
    }

    private long getInternalValue() {
        if(counter.isEmpty())return 0;
        return counter.toLongArray()[0];
    }
}
