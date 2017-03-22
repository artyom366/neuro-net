package neuro.classification.net;

import neuro.classification.generator.RandomGenerator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CoefficientServiceImpl implements CoefficientService {

    @Override
    public List<List<Double>> getCoefficientInitialMatrix() {
        final List<List<Double>> weightCoefficients = new ArrayList<>();

        final List<Double> initialWeights = new ArrayList<>(Arrays.asList(
                RandomGenerator.generateUniformDouble(),
                RandomGenerator.generateUniformDouble(),
                RandomGenerator.generateUniformDouble(),
                RandomGenerator.generateUniformDouble()));

        weightCoefficients.add(initialWeights);

        return weightCoefficients;
    }
}
