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
import neuro.classification.component.LabelFactory;
import neuro.classification.domain.Observation;
import neuro.classification.domain.Result;
import neuro.classification.net.NetRunnerService;
import neuro.classification.net.NetRunnerServiceImpl;
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

        final Label w1Label = LabelFactory.getLabel(175f, 400f, 30f, 100f, "WX1:");
        final Label w2Label = LabelFactory.getLabel(175f, 430f, 30f, 100f, "WX2:");
        final Label w3Label = LabelFactory.getLabel(175f, 460f, 30f, 100f, "WY1:");
        final Label w4Label = LabelFactory.getLabel(175f, 490f, 30f, 100f, "WY2:");
        final Label w01Label = LabelFactory.getLabel(175f, 520f, 30f, 100f, "W01:");
        final Label w02Label = LabelFactory.getLabel(175f, 550f, 30f, 100f, "W02:");

        final Label w1Value = LabelFactory.getLabel(210f, 400f, 30f, 100f, "");
        final Label w2Value = LabelFactory.getLabel(210f, 430f, 30f, 100f, "");
        final Label w3Value = LabelFactory.getLabel(210f, 460f, 30f, 100f, "");
        final Label w4Value = LabelFactory.getLabel(210f, 490f, 30f, 100f, "");
        final Label w01Value = LabelFactory.getLabel(210f, 520f, 30f, 100f, "");
        final Label w02Value = LabelFactory.getLabel(210f, 550f, 30f, 100f, "");

        final Label activationAlfaLabel = LabelFactory.getLabel(360f, 400f, 30f, 100f, "A_Alfa:");
        final Label trainingAlfaLabel = LabelFactory.getLabel(360f, 430f, 30f, 100f, "T_Alfa:");
        final Label errorThresholdLabel = LabelFactory.getLabel(360f, 460f, 30f, 100f, "Error:");

        final Label activationAlfaValue = LabelFactory.getLabel(405f, 400f, 30f, 100f, "");
        final Label trainingAlfaValue = LabelFactory.getLabel(405f, 430f, 30f, 100f, "");
        final Label errorThresholdValue = LabelFactory.getLabel(405f, 460f, 30f, 100f, "");

        final ScatterChart<Number, Number> observationChart = scatterChartController.init();
        observationChart.setOnMouseClicked(event -> handleMouseClickOnScatterChartEvent(event, observationChart));

        final LineChart<Number, Number> errorChart = lineChartController.getXYChart();

        final Button trainButton = ButtonFactory.getButton(25f, 400f, 30f, 100f, TRAIN_BUTTON_CAPTION);
        trainButton.setOnAction(event -> {
            addTrainingDataToScatterPlot(observationChart, observationService.getTrainingData());

            final Result result = netRunnerService.getTrainingResults();
            final List<Double> latestResults = result.getLatestTrainingWeights();

            weights.clear();

            weights.add(latestResults.get(0));
            weights.add(latestResults.get(1));
            weights.add(latestResults.get(2));
            weights.add(latestResults.get(3));
            weights.add(latestResults.get(4));
            weights.add(latestResults.get(5));

            addErrorDataToLineChart(errorChart, result);
            addWeightsInfo(w1Value, w2Value, w3Value, w4Value, w01Value, w02Value);
            addGeneralInfo(activationAlfaValue, trainingAlfaValue, errorThresholdValue);
        });

        final Button toggleLinesButton = ButtonFactory.getButton(25F, 450f, 30f, 100f, TOGGLE_LINES_BUTTON_CAPTION);
        toggleLinesButton.setOnAction(event -> handleClickToggleLienButtonEvent(observationChart, weights));

        final Button clearButton = ButtonFactory.getButton(25F, 500f, 30f, 100f, CLEAR_BUTTON_CAPTION);
        clearButton.setOnAction(event -> handleClickClearButtonEvent(observationChart, errorChart, w1Value, w2Value, w3Value,
                w4Value, w01Value, w02Value, activationAlfaValue, trainingAlfaValue, errorThresholdValue));



        final Pane root = new Pane();
        root.getChildren().addAll(observationChart, trainButton, toggleLinesButton, clearButton, errorChart,
                w1Label, w2Label, w3Label, w4Label, w01Label, w02Label,
                w1Value, w2Value, w3Value, w4Value, w01Value, w02Value,
                activationAlfaLabel, trainingAlfaLabel, errorThresholdLabel,
                activationAlfaValue, trainingAlfaValue, errorThresholdValue);

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

    private void addWeightsInfo(final Label w1Value, final Label w2Value, final Label w3Value, final Label w4Value, final Label w01Value, final Label w02Value) {
        clearLabel(w1Value);
        clearLabel(w2Value);
        clearLabel(w3Value);
        clearLabel(w4Value);
        clearLabel(w01Value);
        clearLabel(w02Value);

        w1Value.setText(String.valueOf(weights.get(0)));
        w2Value.setText(String.valueOf(weights.get(1)));
        w3Value.setText(String.valueOf(weights.get(2)));
        w4Value.setText(String.valueOf(weights.get(3)));
        w01Value.setText(String.valueOf(weights.get(4)));
        w02Value.setText(String.valueOf(weights.get(5)));
    }

    private void addGeneralInfo(final Label activationAlfaValue, final Label trainingAlfaValue, final Label errorThresholdValue) {
        activationAlfaValue.setText(String.valueOf(NetRunnerServiceImpl.ACTIVATION_ALFA));
        trainingAlfaValue.setText(String.valueOf(NetRunnerServiceImpl.TRAINING_ALFA));
        errorThresholdValue.setText(String.valueOf(NetRunnerServiceImpl.TRAINING_THRESHOLD));
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

    private void handleClickClearButtonEvent(final ScatterChart<Number, Number> observationChart, final LineChart<Number, Number> errorChart,
                                             Label w1Value, Label w2Value, Label w3Value, Label w4Value, Label w01Value, Label w02Value,
                                             Label activationAlfaValue, Label trainingAlfaValue, Label errorValue) {
        clearScatterChart(observationChart);
        clearLineChart(errorChart);
        clearLabel(w1Value);
        clearLabel(w2Value);
        clearLabel(w3Value);
        clearLabel(w4Value);
        clearLabel(w01Value);
        clearLabel(w02Value);
        clearLabel(activationAlfaValue);
        clearLabel(trainingAlfaValue);
        clearLabel(errorValue);
    }

    private void clearScatterChart(final ScatterChart<Number, Number> scatterChart) {
        scatterChart.getData().clear();
    }

    private void clearLineChart(final LineChart<Number, Number> lineChart) {
        lineChart.getData().clear();
    }

    private void clearLabel(final Label label) {
        label.setText("");
    }
}
