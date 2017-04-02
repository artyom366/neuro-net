package neuro.classification.net;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface NetRunnerService {

    List<List<Double>> getTrainingResults();

    int getDataTypeClassification(double x, double y, List<Double> trainedWeights);
}
