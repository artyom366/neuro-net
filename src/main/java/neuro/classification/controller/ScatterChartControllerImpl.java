package neuro.classification.controller;

import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import org.springframework.stereotype.Component;

@Component
public class ScatterChartControllerImpl implements ScatterChartController {

    @Override
    public ScatterChart<Number, Number> init() {
        final NumberAxis xAxis = new NumberAxis(0, 10, 1);
        final NumberAxis yAxis = new NumberAxis(0, 10, 1);

        final ScatterChart<Number, Number> chart = new ScatterChart(xAxis, yAxis);
        chart.setMinHeight(400f);
        chart.setPrefHeight(400f);
        chart.setLegendVisible(false);

        return chart;
    }
}
