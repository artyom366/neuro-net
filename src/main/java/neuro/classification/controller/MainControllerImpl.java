package neuro.classification.controller;

import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import neuro.classification.component.ButtonFactory;
import neuro.classification.domain.Observation;
import neuro.classification.net.NetRunnerService;
import neuro.classification.service.ObservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class MainControllerImpl implements MainController {

    private final static float WIDTH = 1000f;
    private final static float HEIGHT = 800f;

    private final static String TRAIN_BUTTON_CAPTION = "Train Net";
    private final static String TOGGLE_LINES_BUTTON_CAPTION = "Toggle Class";
    private final static String CLEAR_BUTTON_CAPTION = "Clear";

    private final List<Double> weights = new ArrayList<>();

    @Autowired
    private ScatterChartController scatterChartController;

    @Autowired
    private ChartController chartController;

    @Autowired
    private ObservationService observationService;

    @Autowired
    private NetRunnerService netRunnerService;

    @Override
    public Scene buildMainScene() {

        final ScatterChart<Number, Number> scatterChart = scatterChartController.init();
        scatterChart.setOnMouseClicked(event -> handleMouseClickOnScatterChartEvent(event, scatterChart));

        final Button trainButton = ButtonFactory.getButton(25f, 450f, 30f, 100f, TRAIN_BUTTON_CAPTION);
        trainButton.setOnAction(event -> {
            addTrainingDataToScatterPlot(scatterChart, observationService.getTrainingData());

            final List<List<Double>> trainingResults = netRunnerService.getTrainingResults();
            final List<Double> latestResults = trainingResults.get(trainingResults.size() - 1);

            weights.add(latestResults.get(0));
            weights.add(latestResults.get(1));
            weights.add(latestResults.get(2));
            weights.add(latestResults.get(3));
            weights.add(latestResults.get(4));
            weights.add(latestResults.get(5));
        });

        final Button toggleLinesButton = ButtonFactory.getButton(140f, 450f, 30f, 100f, TOGGLE_LINES_BUTTON_CAPTION);
        toggleLinesButton.setOnAction(event -> handleClickClearButtonEvent(scatterChart));

        final Button clearButton = ButtonFactory.getButton(255f, 450f, 30f, 50f, CLEAR_BUTTON_CAPTION);
        clearButton.setOnAction(event -> clearScatterChart(scatterChart));

        final LineChart<Number, Number> errorChart = chartController.getXYChart();

        final Pane root = new Pane();
        root.getChildren().addAll(scatterChart, trainButton, toggleLinesButton, clearButton, errorChart);

        return new Scene(root, WIDTH, HEIGHT);
    }

    private void addTrainingDataToScatterPlot(final ScatterChart<Number, Number> scatterChart, final Map<String, List<Observation>> trainingData) {

        clearScatterChart(scatterChart);

        trainingData.entrySet().forEach(entry -> {
            final XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(entry.getKey());
            entry.getValue().forEach(data -> series.getData().add(new XYChart.Data<>(data.getX(), data.getY())));
            scatterChart.getData().add(series);
        });
    }

    private void handleMouseClickOnScatterChartEvent(final MouseEvent event, final ScatterChart<Number, Number> scatterChart) {
        final Axis<Number> xAxis = scatterChart.getXAxis();
        final Axis<Number> yAxis = scatterChart.getYAxis();

        final Number xValue = xAxis.getValueForDisplay(event.getX());
        final Number yValue = yAxis.getValueForDisplay(event.getY());

        final double correctedX = xValue.doubleValue() - 0.85d;
        final double correctedY = yValue.doubleValue() + 0.5d;

        final int type = netRunnerService.getDataTypeClassification(correctedX, correctedY, weights);
        scatterChart.getData().get(type).getData().add(new XYChart.Data<>(correctedX, correctedY));
    }

    private void handleClickClearButtonEvent(final ScatterChart<Number, Number> scatterChart) {
        clearScatterChart(scatterChart);
    }

    private void clearScatterChart(final ScatterChart<Number, Number> scatterChart) {
        scatterChart.getData().clear();
    }
}
