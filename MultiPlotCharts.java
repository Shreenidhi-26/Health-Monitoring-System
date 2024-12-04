import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

public class MultiPlotCharts {
    public static void main(String[] args) {
        // Create datasets from CSV
        DefaultCategoryDataset barDataset = createBarDatasetFromCSV("health.csv", 50);
        DefaultXYDataset scatterDataset = createScatterDatasetFromCSV("health.csv", 100);
        DefaultCategoryDataset lineDataset = createLineDatasetFromCSV("health.csv", 50);
        DefaultCategoryDataset areaDataset = createAreaDatasetFromCSV("health.csv", 30);
        XYSeriesCollection heatMapDataset = createHeatMapDatasetFromCSV("health.csv");

        // Create the bar chart
        JFreeChart barChart = ChartFactory.createBarChart(
                "Bar Chart Example",    // Chart title
                "Person ID",            // X-axis label
                "Sleep Duration",       // Y-axis label
                barDataset);            // Dataset

        // Customize color
        CategoryPlot barPlot = (CategoryPlot) barChart.getPlot();
        BarRenderer barRenderer = (BarRenderer) barPlot.getRenderer();
        barRenderer.setSeriesPaint(0, Color.BLUE); // Change bar color to blue

        // Create the scatter plot
        JFreeChart scatterChart = ChartFactory.createScatterPlot(
                "Scatter Plot Example", // Chart title
                "Physical Activity Level",  // X-axis label
                "Quality of Sleep",         // Y-axis label
                scatterDataset);            // Dataset

        // Customize color
        XYPlot scatterPlot = (XYPlot) scatterChart.getPlot();
        scatterPlot.setBackgroundPaint(Color.LIGHT_GRAY); // Change background color to light gray

        // Create the line plot
        JFreeChart lineChart = ChartFactory.createLineChart(
                "Line Chart Example",   // Chart title
                "Age",                  // X-axis label
                "Heart Rate",           // Y-axis label
                lineDataset);           // Dataset

        // Customize color
        CategoryPlot linePlot = (CategoryPlot) lineChart.getPlot();
        LineAndShapeRenderer lineRenderer = (LineAndShapeRenderer) linePlot.getRenderer();
        lineRenderer.setSeriesPaint(0, Color.RED); // Change line color to red

        // Create the area chart
        JFreeChart areaChart = ChartFactory.createAreaChart(
                "Area Chart Example",   // Chart title
                "Occupation",           // X-axis label
                "Daily Steps",          // Y-axis label
                areaDataset);           // Dataset

        // Customize color
        CategoryPlot areaPlot = (CategoryPlot) areaChart.getPlot();
        areaPlot.setForegroundAlpha(0.7f); // Adjust transparency
        areaPlot.setBackgroundPaint(Color.GREEN); // Change area color to green

        // Create the heat map chart
        JFreeChart heatMapChart = ChartFactory.createScatterPlot(
                "Heat Map Example",     // Chart title
                "Physical Activity Level", // X-axis label
                "Quality of Sleep",        // Y-axis label
                heatMapDataset);            // Dataset

        // Create panels to display each chart
        ChartPanel barChartPanel = new ChartPanel(barChart);
        ChartPanel scatterChartPanel = new ChartPanel(scatterChart);
        ChartPanel lineChartPanel = new ChartPanel(lineChart);
        ChartPanel areaChartPanel = new ChartPanel(areaChart);
        ChartPanel heatMapChartPanel = new ChartPanel(heatMapChart);

        // Create a new frame to hold the chart panels
        JFrame frame = new JFrame("Multi-Plot Charts");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 2)); // 3x2 grid layout

        // Add chart panels to the frame
        frame.add(barChartPanel);
        frame.add(scatterChartPanel);
        frame.add(lineChartPanel);
        frame.add(areaChartPanel);
        frame.add(heatMapChartPanel);

        // Adjust frame size and make it visible
        frame.pack();
        frame.setVisible(true);
    }

    private static DefaultCategoryDataset createBarDatasetFromCSV(String filename, int rowsToRead) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Deque<String> lastRows = readLastRowsFromCSV(filename, rowsToRead);

        // Parse and add the last rows to the dataset
        for (String row : lastRows) {
            String[] parts = row.split(",");
            String personID = parts[0];
            double sleepDuration = Double.parseDouble(parts[4]); // Sleep Duration
            dataset.addValue(sleepDuration, "Sleep Duration", personID);
        }

        return dataset;
    }

    private static DefaultXYDataset createScatterDatasetFromCSV(String filename, int rowsToRead) {
        DefaultXYDataset dataset = new DefaultXYDataset();
        Deque<String> lastRows = readLastRowsFromCSV(filename, rowsToRead);

        double[][] data = new double[2][lastRows.size()];
        int index = 0;
        for (String row : lastRows) {
            String[] parts = row.split(",");
            double physicalActivityLevel = Double.parseDouble(parts[6]); // Physical Activity Level
            double qualityOfSleep = Double.parseDouble(parts[5]); // Quality of Sleep
            data[0][index] = physicalActivityLevel;
            data[1][index] = qualityOfSleep;
            index++;
        }

        dataset.addSeries("Quality of Sleep vs. Physical Activity Level", data);

        return dataset;
    }

    private static DefaultCategoryDataset createLineDatasetFromCSV(String filename, int rowsToRead) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Deque<String> lastRows = readLastRowsFromCSV(filename, rowsToRead);

        // Parse and add the last rows to the dataset
        int index = 0;
        for (String row : lastRows) {
            String[] parts = row.split(",");
            int age = Integer.parseInt(parts[2]); // Age
            int heartRate = Integer.parseInt(parts[10]); // Heart Rate
            dataset.addValue(heartRate, "Heart Rate", String.valueOf(age));
            index++;
        }

        return dataset;
    }

    private static DefaultCategoryDataset createAreaDatasetFromCSV(String filename, int rowsToRead) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Deque<String> lastRows = readLastRowsFromCSV(filename, rowsToRead);

        // Parse and add the last rows to the dataset
        for (String row : lastRows) {
            String[] parts = row.split(",");
            String occupation = parts[3]; // Occupation
            int dailySteps = Integer.parseInt(parts[11]); // Daily Steps
            dataset.addValue(dailySteps, "Daily Steps", occupation);
        }

        return dataset;
    }

    private static XYSeriesCollection createHeatMapDatasetFromCSV(String filename) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("Heat Map Data");

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                double physicalActivityLevel = Double.parseDouble(parts[6]); // Physical Activity Level
                double qualityOfSleep = Double.parseDouble(parts[5]); // Quality of Sleep
                series.add(physicalActivityLevel, qualityOfSleep);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        dataset.addSeries(series);

        return dataset;
    }

    private static Deque<String> readLastRowsFromCSV(String filename, int rowsToRead) {
        Deque<String> lastRows = new ArrayDeque<>(rowsToRead);

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                if (lastRows.size() >= rowsToRead) {
                    lastRows.pollFirst(); // Remove the oldest row if the deque is full
                }
                lastRows.offerLast(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lastRows;
    }
}
