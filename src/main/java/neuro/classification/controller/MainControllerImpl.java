package neuro.classification.controller;

import javafx.scene.Scene;
import javafx.scene.chart.Axis;
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

import java.util.List;
import java.util.Map;

@Component
public class MainControllerImpl implements MainController {

    private final static float WIDTH = 510f;
    private final static float HEIGHT = 800f;

    private final static String TRAIN_BUTTON_CAPTION = "Train";

    @Autowired
    private ScatterChartController scatterChartController;

    @Autowired
    private ObservationService observationService;

    @Autowired
    private NetRunnerService netRunnerService;

    @Override
    public Scene buildMainScene() {

        final ScatterChart<Number, Number> scatterChart = scatterChartController.init();
        scatterChart.setOnMouseClicked(event -> handleMouseClickEvent(event, scatterChart));

        final Button trainButton = ButtonFactory.getButton(25, 500, 30, 50, TRAIN_BUTTON_CAPTION);
        trainButton.setOnAction(event -> {
            addTrainingDataToScatterPlot(scatterChart, observationService.getTrainingData());
            netRunnerService.run();
        });

        final Pane root = new Pane();
        root.getChildren().addAll(scatterChart, trainButton);

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

    private void clearScatterChart(final ScatterChart<Number, Number> scatterChart) {
        scatterChart.getData().clear();
    }

    private void handleMouseClickEvent(final MouseEvent event, final ScatterChart<Number, Number> scatterChart) {
        final Axis<Number> xAxis = scatterChart.getXAxis();
        final Axis<Number> yAxis = scatterChart.getYAxis();

        final Number xValue = xAxis.getValueForDisplay(event.getX());
        final Number yValue = yAxis.getValueForDisplay(event.getY());

        //todo classificate new data
    }
}
