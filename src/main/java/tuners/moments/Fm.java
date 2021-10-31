package tuners.moments;

import util.BitsVector;
import util.randomAdapters.IRandomAdapter;

public class Fm implements IF0{
    private final IRandomAdapter randomAdapter;
    private BitsVector bits;
    private int size;
    private final double divisionFactor = 0.77351;

    public Fm(int size, IRandomAdapter randomAdapter){
        bits = new BitsVector(size);
        this.size = size;
        this.randomAdapter = randomAdapter;
    }
    @Override
    public void addKey(long key) {
        int rndOutput = randomAdapter.nextInt(key,(int)Math.pow(2,size));
        int index = -1;
        if(rndOutput==0) {
            index = size-1 ;
        }else{
            index = getFirstSetBitPos(rndOutput);
        }
        bits.set(index);
    }

    private int findSetMsb(int n) {
        return (int)(Math.log(n) / Math.log(2));
    }
    private int getFirstSetBitPos(int n)
    {
        return (int)((Math.log10(n & -n)) / Math.log10(2));
    }
    @Override
    public double getDistinctItemsCount() {
        double temp = Math.pow(2,bits.getLsbZeroBitIndex()) / divisionFactor;
        return Math.floor(temp);
    }

    @Override
    public void reset() {
        bits.clear();
    }

    public int getLsbZeroBitIndex(){
        return bits.getLsbZeroBitIndex();
    }

    public String getName() {
        return "FM_"+randomAdapter.getClass().getName();
    }
}
