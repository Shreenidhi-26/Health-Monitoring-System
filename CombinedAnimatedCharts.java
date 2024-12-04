import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CombinedAnimatedCharts extends JPanel implements ActionListener {
    private Timer timer;
    private DefaultCategoryDataset barDataset;
    private XYSeriesCollection scatterDataset;
    private int currentAge = 0;

    public CombinedAnimatedCharts() {
        super(new GridLayout(2, 1)); // Two rows, one for each chart

        // Create bar chart dataset
        barDataset = new DefaultCategoryDataset();

        // Create scatter plot dataset
        scatterDataset = new XYSeriesCollection();
        XYSeries scatterSeries = new XYSeries("Scatter Data");
        scatterDataset.addSeries(scatterSeries);

        // Create the bar chart
        JFreeChart barChart = ChartFactory.createBarChart(
                "Animated Bar Chart", // Chart title
                "Person ID",          // X-axis label
                "Sleep Duration",     // Y-axis label
                barDataset,           // Dataset
                PlotOrientation.VERTICAL,
                true,                 // Include legend
                true,                 // Include tooltips
                false                 // Include URLs
        );

        // Customize bar chart color
        BarRenderer barRenderer = (BarRenderer) barChart.getCategoryPlot().getRenderer();
        barRenderer.setSeriesPaint(0, Color.BLUE); // Change bar color to blue

        // Create the scatter plot
        JFreeChart scatterChart = ChartFactory.createScatterPlot(
                "Animated Scatter Plot", // Chart title
                "Physical Activity Level", // X-axis label
                "Quality of Sleep",       // Y-axis label
                scatterDataset,           // Dataset
                PlotOrientation.VERTICAL,
                true,                     // Include legend
                true,                     // Include tooltips
                false                     // Include URLs
        );

        // Customize scatter plot color
        XYLineAndShapeRenderer scatterRenderer = new XYLineAndShapeRenderer();
        scatterRenderer.setSeriesPaint(0, Color.BLUE); // Change point color to blue
        scatterChart.getXYPlot().setRenderer(scatterRenderer);

        // Create chart panels
        ChartPanel barChartPanel = new ChartPanel(barChart);
        ChartPanel scatterChartPanel = new ChartPanel(scatterChart);

        // Add chart panels to the panel
        add(barChartPanel);
        add(scatterChartPanel);

        // Set up the timer
        timer = new Timer(1000, this); // Update every 1000 milliseconds (1 second)
        timer.start();
    }

    public void actionPerformed(ActionEvent e) {
        // Update bar chart dataset with new data
        int sleepDuration = generateRandomSleepDuration();
        barDataset.addValue(sleepDuration, "Sleep Duration", String.valueOf(currentAge));
        currentAge++;

        // Remove old data if necessary (optional)
        if (barDataset.getColumnCount() > 10) {
            barDataset.removeColumn(0);
        }

        // Update scatter plot dataset with new data
        double x = generateRandomValue();
        double y = generateRandomValue();
        scatterDataset.getSeries(0).add(x, y);
    }

    // Generate random sleep duration data (for demonstration)
    private int generateRandomSleepDuration() {
        return (int) (Math.random() * 8) + 1; // Random value between 1 and 8 hours
    }

    // Generate random value (for demonstration)
    private double generateRandomValue() {
        return Math.random() * 10; // Random value between 0 and 10
    }

    private static void createAndShowGUI() {
        // Create and set up the window
        JFrame frame = new JFrame("Combined Animated Charts");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create and set up the content pane
        JComponent newContentPane = new CombinedAnimatedCharts();
        newContentPane.setOpaque(true); // Content panes must be opaque
        frame.setContentPane(newContentPane);

        // Display the window
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Schedule a job for the event dispatch thread:
        // creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
