package reports.distributions;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;

import java.awt.*;
import java.io.IOException;
import java.util.*;

public class DistributionLogger {
    public void init(double maxValueOfKey){
        distributionMap = new TreeMap<>();
        delta = (long)((double) maxValueOfKey / (double)numberOfBins);
        long minKey = 0, maxKey = delta;
        while(maxKey< maxValueOfKey){
            distributionMap.put(minKey,Long.valueOf(0));
            minKey = maxKey;
            maxKey += delta;
        }
    }
    public void addKey(Long key){
        long minKey = key - key % delta;
        //String keyRange = generateKey(minKey,minKey+delta);
        distributionMap.put(minKey,distributionMap.getOrDefault(minKey,Long.valueOf(0))+1);
    }
    public void buildHistogram(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = 1024;//(int)screenSize.getWidth();
        int height =768;// (int)screenSize.getHeight();
        CategoryChart chart = new CategoryChartBuilder().width(width).height(height)
                .title("Requests histogram")
                .xAxisTitle("Keys bins")
                .yAxisTitle("Frequency")
                .build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setAvailableSpaceFill(0.99);
        chart.getStyler().setOverlapped(true);

        java.util.List yData = new ArrayList();
        yData.addAll(distributionMap.values());
        java.util.List xData = Arrays.asList(distributionMap.keySet().stream().map(k->generateKey(k)).toArray());
        chart.addSeries("Freqs", xData, yData);
        chart.getStyler().setLegendVisible(false);

        try {
            BitmapEncoder.saveBitmap(chart, "./Distribution_Chart", BitmapEncoder.BitmapFormat.JPG);
//            BitmapEncoder.saveJPGWithQuality(chart, "./Sample_Chart_With_Quality.jpg", 0.95f);
//            BitmapEncoder.saveBitmapWithDPI(chart, "./Sample_Chart_300_DPI", BitmapEncoder.BitmapFormat.PNG, 300);

            //VectorGraphicsEncoder.saveVectorGraphic(chart, "./Sample_Chart", VectorGraphicsEncoder.VectorGraphicsFormat.PDF);

        } catch (IOException e) {
            e.printStackTrace();
        }
        new SwingWrapper<>(chart).displayChart();
    }

    private String generateKey(long minKey) {
        long maxKey = minKey + delta;
        return minKey + "-" + maxKey;
    }

    private Map<Long,Long> distributionMap;
    private int numberOfBins = 10;
    private  long delta;

}
