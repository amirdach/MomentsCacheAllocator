package tuners.moments;

import util.randomAdapters.MurmurHash;

public class AMS {
    //the maximum rho or the number of leading 0s in the bitmap
    private int R;


    public AMS() {
        R = 0;
    }

    /**
     * @param
     * key a new element from the dataset
     */
    public boolean offer(Object key) {
        boolean affected = false;
        if (key != null) {
            // non-negative hash values
            long v = MurmurHash.hash64(key);
            // calculating the position of the most significant bit that set to 1 in the final bitmap but
            // while we do not save bitmap so we save the max trailing zero number over all the traffic
            int r = rho(v);
            if (R < r) {
                R = r;
                affected = true;
            }
        }

        return affected;
    }


    /**
     * @return the cardinality estimation.
     **/
    public long cardinality() {
        return (long) (Math.pow(2, R));
    }

    /**
     * @return the position of the least significant 1-bit in the binary representation of y
     *         rho(O)=0
     */
    private int rho(long y) {
        return Long.numberOfTrailingZeros(y);
    }

}