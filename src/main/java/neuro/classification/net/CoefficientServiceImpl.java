package neuro.classification.net;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CoefficientServiceImpl implements CoefficientService {

    @Override
    public List<List<Double>> getCoefficientInitialMatrix() {
        final List<List<Double>> weightCoefficients = new ArrayList<>();

        final List<Double> initialWeights = new ArrayList<>(Arrays.asList(0.5d, 0.5d, 0.5d, 0.5d, 1d, 1d));

        weightCoefficients.add(initialWeights);

        return weightCoefficients;
    }
}
