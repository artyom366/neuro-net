package neuro.classification.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Result {

    private final List<Double> errors;
    private final List<List<Double>> weights;

    public Result() {
        this.errors = new ArrayList<>();
        this.weights = new ArrayList<>();
    }

    public List<Double> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public void addError(final double error) {
        this.errors.add(error);
    }

    public List<List<Double>> getWeights() {
        return Collections.unmodifiableList(weights);
    }

    public void addWeights(final List<Double> weights) {
        this.weights.add(Collections.unmodifiableList(weights));
    }

    public List<Double> getLatestTrainingWeights() {
        return this.weights.get(this.weights.size() - 1);
    }
}
