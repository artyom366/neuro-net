package neuro.classification.controller;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import org.springframework.stereotype.Service;

@Service
public class LineChartControllerImpl implements LineChartController {

    private final static float LAYOUT_X = 500f;
    private final static float LAYOUT_Y = 0f;

    private final static float WIDTH = 485f;
    private final static float HEIGHT = 422f;

    private final static String X_AXIS_LABEL = "Iterations";
    private final static String Y_AXIS_LABEL = "Error";

    @Override
    public LineChart<Number,Number> getXYChart() {

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel(X_AXIS_LABEL);
        yAxis.setLabel(Y_AXIS_LABEL);

        final LineChart<Number,Number> lineChart = new LineChart<>(xAxis,yAxis);
        lineChart.setLayoutX(LAYOUT_X);
        lineChart.setLayoutY(LAYOUT_Y);
        lineChart.setPrefHeight(HEIGHT);
        lineChart.setPrefWidth(WIDTH);
        lineChart.setLegendVisible(false);
        lineChart.setCreateSymbols(false);

        return lineChart;
    }
}
