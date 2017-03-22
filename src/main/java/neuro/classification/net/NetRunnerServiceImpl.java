package neuro.classification.net;

import neuro.classification.domain.Observation;
import neuro.classification.service.ObservationService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class NetRunnerServiceImpl implements NetRunnerService {

    private final static double ACTIVATION_ALFA = 0.8d;
    private final static double TRAINING_ALFA = -0.1d;
    private final static double TRAINING_THRESHOLD = 0.2d;

    @Autowired
    private ObservationService observationService;

    @Autowired
    private CoefficientService coefficientService;

    @Override
    public void run() {

        getTrainedWeights();


    }


    private double calculateWeightDelta(final double in, final double weight, final int expectedAnswer, final double realAnswer, final double alfa) {

        final double y_in = in * weight;
        final double error = expectedAnswer - realAnswer;

        return alfa * error * y_in * (1 - y_in) * in;
    }

    private double calculateSigmoid(final double alfa, final double y_in) {
        return 1 / (1 + Math.pow(Math.E, -alfa * y_in));
    }

    private void getTrainedWeights() {

        final List<List<Double>> coefficientMatrix = coefficientService.getCoefficientInitialMatrix();
        final List<Map<Pair<Integer, Integer>, List<Observation>>> trainingDataWithAnswers = observationService.getTrainingDataWithAnswers();

        final boolean[] doTraining = {false};

        do {

            doTraining[0] = false;



            trainingDataWithAnswers.forEach(data -> {

                data.entrySet().forEach(dataEntry -> {

                    final Pair<Integer, Integer> answers = dataEntry.getKey();
                    final List<Observation> trainingData = dataEntry.getValue();

                    trainingData.forEach(trainingCoordinate -> {

                        final double x = trainingCoordinate.getX();
                        final double y = trainingCoordinate.getY();

                        final List<Double> latestWeights = coefficientMatrix.get(coefficientMatrix.size() - 1);

                        final double y_in_1 = x * latestWeights.get(0) + y * latestWeights.get(2);
                        final double y_in_2 = x * latestWeights.get(1) + y * latestWeights.get(3);

                        final double activation_1 = calculateSigmoid(ACTIVATION_ALFA, y_in_1);
                        final double activation_2 = calculateSigmoid(ACTIVATION_ALFA, y_in_2);

                        final Integer answer_1 = answers.getLeft();
                        final Integer answer_2 = answers.getRight();

                        if (Math.abs(Math.abs(answer_1) - Math.abs(activation_1)) > TRAINING_THRESHOLD || Math.abs(Math.abs(answer_2) - Math.abs(activation_2)) > TRAINING_THRESHOLD) {

                            doTraining[0] = true;

                            final double x_delta_0 = calculateWeightDelta(x, latestWeights.get(0), answer_1, y_in_1, TRAINING_ALFA);
                            final double y_delta_2 = calculateWeightDelta(y, latestWeights.get(2), answer_1, y_in_1, TRAINING_ALFA);

                            final double x_delta_1 = calculateWeightDelta(x, latestWeights.get(1), answer_2, y_in_2, TRAINING_ALFA);
                            final double y_delta_3 = calculateWeightDelta(y, latestWeights.get(3), answer_2, y_in_2, TRAINING_ALFA);

                            final List<Double> newWeights = new ArrayList<>();
                            newWeights.add(latestWeights.get(0) + x_delta_0);
                            newWeights.add(latestWeights.get(1) + x_delta_1);
                            newWeights.add(latestWeights.get(2) + y_delta_2);
                            newWeights.add(latestWeights.get(3) + y_delta_3);

                            coefficientMatrix.add(newWeights);
                        }

                        int stop = 0;
                    });
                });


            });

        } while (doTraining[0]);

    }


}

