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
        return new ScatterChart(xAxis, yAxis);
    }
}
