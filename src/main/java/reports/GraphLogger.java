package reports;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.style.Styler;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GraphLogger {
    public GraphLogger(){
        xData = new ArrayList<>();
        yData = new ArrayList<>();
    }
    public void addPoint(double x, double y){
        xData.add(x);
        yData.add(y);
    }
    public  void addXValues(List<Double> x){
        xData = x;
    }
    public void addYValues(List<Double> y){
        yData = y;
    }
    public void buildGraph(String title,String xAxis,String yAxis){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)(screenSize.getWidth() * 0.75);
        int height = (int)(screenSize.getHeight() * 0.75);
        XYChart chart = new XYChartBuilder().width(width).height(height)
                .title(title)
                .xAxisTitle(xAxis)
                .yAxisTitle(yAxis)
                .build();
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.addSeries(title, xData, yData);
        try {
            BitmapEncoder.saveBitmap(chart, "./"+title, BitmapEncoder.BitmapFormat.JPG);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new SwingWrapper<>(chart).displayChart();
    }
    public void buildGraphWithMultipleSeries(String title,String xAxis,String yAxis,String title2, List<Double> xData2,List<Double> yData2){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)(screenSize.getWidth() * 0.75);
        int height = (int)(screenSize.getHeight() * 0.75);
        XYChart chart = new XYChartBuilder().width(width).height(height)
                .title(title)
                .xAxisTitle(xAxis)
                .yAxisTitle(yAxis)
                .build();
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.addSeries(title, xData, yData);
        chart.addSeries(title2,xData2,yData2);
        try {
            BitmapEncoder.saveBitmap(chart, "./"+title, BitmapEncoder.BitmapFormat.JPG);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new SwingWrapper<>(chart).displayChart();
    }
    private List<Double> xData;
    private List<Double> yData;
}
