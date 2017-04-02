package neuro.classification.net;

import neuro.classification.domain.Observation;
import neuro.classification.service.ObservationService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class NetRunnerServiceImpl implements NetRunnerService {

    private final static double ACTIVATION_ALFA = -1d;
    private final static double TRAINING_ALFA = 1d;
    private final static double TRAINING_THRESHOLD = 0.01d;
    private final static double CLASSIFICATE_THRESHOLD = 0.1d;
    private final static int TYPE_1 = 1;
    private final static int TYPE_0 = 0;
    private final static int X0 = 1;

    @Autowired
    private ObservationService observationService;

    @Autowired
    private CoefficientService coefficientService;

    @Override
    public List<List<Double>> getTrainingResults() {

        final List<List<Double>> coefficientMatrix = coefficientService.getCoefficientInitialMatrix();

        do {

            final List<Map<Pair<Integer, Integer>, List<Observation>>> trainingDataWithAnswers = observationService.getTrainingDataWithAnswers();
            trainNetwork(coefficientMatrix, trainingDataWithAnswers);

            final List<Map<Pair<Integer, Integer>, List<Observation>>> testDataWithAnswers = observationService.getTestDataWithAnswers();
            final double totalError = runTestData(coefficientMatrix, testDataWithAnswers);

            final int testDataCount = getTestDataSize(testDataWithAnswers);

            final double normalizedError = normalizeError(totalError, testDataCount);

            if (isErrorIsAcceptable(normalizedError)) {
                return coefficientMatrix;
            }

        } while (true);
    }

    private int getTestDataSize(final List<Map<Pair<Integer, Integer>, List<Observation>>> testDataWithAnswers) {

        int count = 0;
        for (final Map<Pair<Integer, Integer>, List<Observation>> map : testDataWithAnswers) {
            for (final Map.Entry<Pair<Integer, Integer>, List<Observation>> entries : map.entrySet()) {
                count += entries.getValue().size();
            }
        }

        return count;
    }

    private double calculateWeightDelta(final double alfa, final int expectedAnswer, final double realAnswer, final double in) {

        final double error = expectedAnswer - realAnswer;
        final double sigmoidDifferential = 0.5d * (1 + realAnswer) * (1 - realAnswer);
        final double result = alfa * error * realAnswer * sigmoidDifferential * in;

        return result;
    }

    private double calculateSigmoid(final double alfa, final double in) {

        final double power = alfa * in;
        final double divider = 1 + Math.pow(Math.E, power);
        final double result = 1 / divider;

        return result;
    }

    private void trainNetwork(final List<List<Double>> coefficientMatrix, final List<Map<Pair<Integer, Integer>, List<Observation>>> trainingDataWithAnswers) {

        trainingDataWithAnswers.forEach(data -> {
            data.entrySet().forEach(dataEntry -> {

                final Pair<Integer, Integer> answers = dataEntry.getKey();
                final List<Observation> trainingData = dataEntry.getValue();

                trainingData.forEach(trainingCoordinate -> {

                    final double x = trainingCoordinate.getX();
                    final double y = trainingCoordinate.getY();

                    final List<Double> latestWeights = coefficientMatrix.get(coefficientMatrix.size() - 1);

                    final double in_1 = x * latestWeights.get(0) + y * latestWeights.get(2) + X0 * latestWeights.get(4);
                    final double in_2 = x * latestWeights.get(1) + y * latestWeights.get(3) + X0 * latestWeights.get(5);

                    final double activation_1 = calculateSigmoid(ACTIVATION_ALFA, in_1);
                    final double activation_2 = calculateSigmoid(ACTIVATION_ALFA, in_2);

                    final Integer answer_1 = answers.getLeft();
                    final Integer answer_2 = answers.getRight();

                    final double x_delta_0 = calculateWeightDelta(TRAINING_ALFA, answer_1, activation_1, x);
                    final double y_delta_2 = calculateWeightDelta(TRAINING_ALFA, answer_1, activation_1, y);
                    final double x0_delta_4 = calculateWeightDelta(TRAINING_ALFA, answer_1, activation_1, X0);

                    final double x_delta_1 = calculateWeightDelta(TRAINING_ALFA, answer_2, activation_2, x);
                    final double y_delta_3 = calculateWeightDelta(TRAINING_ALFA, answer_2, activation_2, y);
                    final double x0_delta_5 = calculateWeightDelta(TRAINING_ALFA, answer_2, activation_2, X0);

                    final List<Double> newWeights = new ArrayList<>();
                    newWeights.add(latestWeights.get(0) + x_delta_0);
                    newWeights.add(latestWeights.get(1) + x_delta_1);
                    newWeights.add(latestWeights.get(2) + y_delta_2);
                    newWeights.add(latestWeights.get(3) + y_delta_3);
                    newWeights.add(latestWeights.get(4) + x0_delta_4);
                    newWeights.add(latestWeights.get(5) + x0_delta_5);

                    coefficientMatrix.add(newWeights);
                });
            });
        });
    }
    
    private double runTestData(final List<List<Double>> coefficientMatrix, final List<Map<Pair<Integer, Integer>, List<Observation>>> testDataWithAnswers) {

        final List<Double> trainedWeights = coefficientMatrix.get(coefficientMatrix.size() - 1);
        final List<Double> totalError = new ArrayList<>(Collections.singletonList(0d));

        testDataWithAnswers.forEach(data -> {
            data.entrySet().forEach(dataEntry -> {

                final Pair<Integer, Integer> answers = dataEntry.getKey();
                final List<Observation> testData = dataEntry.getValue();

                testData.forEach(trainingCoordinate -> {

                    final double x = trainingCoordinate.getX();
                    final double y = trainingCoordinate.getY();

                    final double in_1 = x * trainedWeights.get(0) + y * trainedWeights.get(2) + X0 * trainedWeights.get(4);
                    final double in_2 = x * trainedWeights.get(1) + y * trainedWeights.get(3) + X0 * trainedWeights.get(5);

                    final double activation_1 = calculateSigmoid(ACTIVATION_ALFA, in_1);
                    final double activation_2 = calculateSigmoid(ACTIVATION_ALFA, in_2);

                    final Integer answer_1 = answers.getLeft();
                    final Integer answer_2 = answers.getRight();

                    estimateTotalError(answer_1, answer_2, activation_1, activation_2, totalError);
                });
            });
        });

        return totalError.get(totalError.size() - 1);
    }

    private void estimateTotalError(final Integer answer_1, final Integer answer_2, final double activation_1, final double activation_2, final List<Double> totalError) {
        totalError.add(totalError.get(totalError.size() - 1) + (Math.pow(answer_1 - activation_1, 2) + (Math.pow(answer_2 - activation_2, 2))));
    }

    private double normalizeError(final double totalError, final int observationCount) {
        return Math.sqrt(totalError / observationCount);
    }

    private boolean isErrorIsAcceptable(final double normalizedError) {
        return normalizedError < TRAINING_THRESHOLD;
    }

    @Override
    public Pair<Integer, Integer> classificateData(final double x, final double y, final List<Double> trainedWeights) {

        final double in_1 = x * trainedWeights.get(0) + y * trainedWeights.get(2) + X0 * trainedWeights.get(4);
        final double in_2 = x * trainedWeights.get(1) + y * trainedWeights.get(3) + X0 * trainedWeights.get(5);

        final double activation_1 = calculateSigmoid(ACTIVATION_ALFA, in_1);
        final double activation_2 = calculateSigmoid(ACTIVATION_ALFA, in_2);

        return getClassification(activation_1, activation_2);
    }

    private Pair<Integer, Integer> getClassification(final double activation_1, final double activation_2) {
        Integer answer_1 = 0;
        Integer answer_2 = 0;

        if (isType1(activation_1)) {
            answer_1 = 1;
        }

        if (isType1(activation_2)) {
            answer_2 = 1;
        }

        return new ImmutablePair<>(answer_1, answer_2);
    }

    private boolean isType1(final double activation) {
        return Math.abs(TYPE_1 - activation) < CLASSIFICATE_THRESHOLD;
    }
}

