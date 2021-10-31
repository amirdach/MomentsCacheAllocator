package policies.moments;

public class Counter implements ICounter{
    private int counter = 0;
    public Counter(){
        increment();
    }
    @Override
    public void increment() {
        ++counter;
    }

    @Override
    public long get() {
        return counter;
    }
}
