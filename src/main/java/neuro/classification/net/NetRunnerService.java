package neuro.classification.net;

import neuro.classification.domain.Result;

import java.util.List;

public interface NetRunnerService {

    Result getTrainingResults();

    int getDataTypeClassification(double x, double y, List<Double> trainedWeights);
}
