package reports;

public class IndexBasedGraphLogger {
    private GraphLogger graphLogger;
    private int index = 1;
    public IndexBasedGraphLogger(){
        graphLogger = new GraphLogger();
    }
    public void addValue(int val){
        graphLogger.addPoint(index++,val);
    }
    public void buildGraph(String title,String xAxisTitle,String yAxisTitle){
        graphLogger.buildGraph(title,xAxisTitle,yAxisTitle);
    }
}
