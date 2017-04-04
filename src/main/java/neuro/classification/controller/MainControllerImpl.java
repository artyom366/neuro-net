package neuro.classification.controller;

import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import neuro.classification.component.ButtonFactory;
import neuro.classification.domain.Observation;
import neuro.classification.domain.Result;
import neuro.classification.net.NetRunnerService;
import neuro.classification.service.ObservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Component
public class MainControllerImpl implements MainController {

    private final static float WIDTH = 1000f;
    private final static float HEIGHT = 600f;

    private final static String TRAIN_BUTTON_CAPTION = "Train Net";
    private final static String TOGGLE_LINES_BUTTON_CAPTION = "Toggle Class";
    private final static String CLEAR_BUTTON_CAPTION = "Clear";

    private final List<Double> weights = new ArrayList<>();

    @Autowired
    private ScatterChartController scatterChartController;

    @Autowired
    private LineChartController lineChartController;

    @Autowired
    private ObservationService observationService;

    @Autowired
    private NetRunnerService netRunnerService;

    @Override
    public Scene buildMainScene() {

        final Label weightsLabel = new Label("Weights:");
        final Label learningAlfaLabel = new Label("L_Alfa:");
        final Label trainingAlfaLabel = new Label("T_Alfa:");
        final Label classificationThreshold = new Label("Errror:");


        final ScatterChart<Number, Number> observationChart = scatterChartController.init();
        observationChart.setOnMouseClicked(event -> handleMouseClickOnScatterChartEvent(event, observationChart));

        final LineChart<Number, Number> errorChart = lineChartController.getXYChart();

        final Button trainButton = ButtonFactory.getButton(25f, 450f, 30f, 100f, TRAIN_BUTTON_CAPTION);
        trainButton.setOnAction(event -> {
            addTrainingDataToScatterPlot(observationChart, observationService.getTrainingData());

            final Result result = netRunnerService.getTrainingResults();
            final List<Double> latestResults = result.getLatestTrainingWeights();

            weights.add(latestResults.get(0));
            weights.add(latestResults.get(1));
            weights.add(latestResults.get(2));
            weights.add(latestResults.get(3));
            weights.add(latestResults.get(4));
            weights.add(latestResults.get(5));

            addErrorDataToLineChart(errorChart, result);
        });

        final Button toggleLinesButton = ButtonFactory.getButton(25F, 500f, 30f, 100f, TOGGLE_LINES_BUTTON_CAPTION);
        toggleLinesButton.setOnAction(event -> handleClickToggleLienButtonEvent(observationChart, weights));

        final Button clearButton = ButtonFactory.getButton(25F, 550f, 30f, 100f, CLEAR_BUTTON_CAPTION);
        clearButton.setOnAction(event -> handleClickClearButtonEvent(observationChart, errorChart));



        final Pane root = new Pane();
        root.getChildren().addAll(observationChart, trainButton, toggleLinesButton, clearButton, errorChart);

        return new Scene(root, WIDTH, HEIGHT);
    }

    private void addTrainingDataToScatterPlot(final ScatterChart<Number, Number> observationChart, final Map<String, List<Observation>> trainingData) {

        clearScatterChart(observationChart);

        trainingData.entrySet().forEach(entry -> {
            final XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(entry.getKey());
            entry.getValue().forEach(data -> series.getData().add(new XYChart.Data<>(data.getX(), data.getY())));
            observationChart.getData().add(series);
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

    private void addErrorDataToLineChart(final LineChart<Number, Number> lineChart, final Result result) {
        clearLineChart(lineChart);

        final XYChart.Series<Number, Number> error = new XYChart.Series<>();
        IntStream.range(0, result.getErrors().size())
                .forEach(index -> error.getData().add(new XYChart.Data<>(index, result.getErrors().get(index))));


        lineChart.getData().add(error);
    }


    private void handleClickToggleLienButtonEvent(final ScatterChart<Number, Number> observationChart, final List<Double> weights) {

        if (observationChart.getData().size() == 6) {
            observationChart.getData().get(4).getData().clear();
            observationChart.getData().get(5).getData().clear();
            observationChart.getData().remove(4, 6);
            return;
        }

        final Circle dot = new Circle();
        dot.setRadius(2d);
        dot.setFill(Color.LIGHTGRAY);

        final XYChart.Series<Number, Number> line_1 = new XYChart.Series<>();
        line_1.setName("line 1");
        line_1.setNode(dot);

        final XYChart.Series<Number, Number> line_2 = new XYChart.Series<>();
        line_2.setName("line 2");
        line_2.setNode(dot);

        for (int i = 0; i <= 10; i++) {
            double y = (double) i * weights.get(0)/weights.get(2) - weights.get(4)/weights.get(2);
            line_1.getData().add(new XYChart.Data<>(i, y));
        }

        observationChart.getData().add(line_1);

        for (int i = 0; i <= 10; i++) {
            double x = (double) i * weights.get(3)/weights.get(1) - weights.get(5)/weights.get(1);
            line_1.getData().add(new XYChart.Data<>(x, i));
        }

        observationChart.getData().add(line_2);
    }

    private void handleClickClearButtonEvent(final ScatterChart<Number, Number> observationChart, final LineChart<Number, Number> errorChart) {
        clearScatterChart(observationChart);
        clearLineChart(errorChart);
    }

    private void clearScatterChart(final ScatterChart<Number, Number> scatterChart) {
        scatterChart.getData().clear();
    }

    private void clearLineChart(final LineChart<Number, Number> lineChart) {
        lineChart.getData().clear();
    }
}
