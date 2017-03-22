package neuro.classification.service;

import neuro.classification.domain.Observation;
import neuro.classification.generator.RandomGenerator;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ObservationServiceImpl implements ObservationService {

    @Override
    public Map<String, List<Observation>> getTrainingData() {

        final Map<String, List<Observation>> data = new TreeMap<>();

        final List<Observation> data1 = new LinkedList<>();
        data1.add(new Observation(RandomGenerator.generateUniformDouble(0d, 3.1d), RandomGenerator.generateUniformDouble(0d, 3.1d)));
        data1.add(new Observation(RandomGenerator.generateUniformDouble(0d, 3.1d), RandomGenerator.generateUniformDouble(0d, 3.1d)));
        data1.add(new Observation(RandomGenerator.generateUniformDouble(0d, 3.1d), RandomGenerator.generateUniformDouble(0d, 3.1d)));
        data1.add(new Observation(RandomGenerator.generateUniformDouble(0d, 3.1d), RandomGenerator.generateUniformDouble(0d, 3.1d)));
        data1.add(new Observation(RandomGenerator.generateUniformDouble(0d, 3.1d), RandomGenerator.generateUniformDouble(0d, 3.1d)));

        final List<Observation> data2 = new LinkedList<>();
        data2.add(new Observation(RandomGenerator.generateUniformDouble(0d, 3.1d), RandomGenerator.generateUniformDouble(7d, 10.1d)));
        data2.add(new Observation(RandomGenerator.generateUniformDouble(0d, 3.1d), RandomGenerator.generateUniformDouble(7d, 10.1d)));
        data2.add(new Observation(RandomGenerator.generateUniformDouble(0d, 3.1d), RandomGenerator.generateUniformDouble(7d, 10.1d)));
        data2.add(new Observation(RandomGenerator.generateUniformDouble(0d, 3.1d), RandomGenerator.generateUniformDouble(7d, 10.1d)));
        data2.add(new Observation(RandomGenerator.generateUniformDouble(0d, 3.1d), RandomGenerator.generateUniformDouble(7d, 10.1d)));

        final List<Observation> data3 = new LinkedList<>();
        data3.add(new Observation(RandomGenerator.generateUniformDouble(7d, 10.1d), RandomGenerator.generateUniformDouble(7d, 10.1d)));
        data3.add(new Observation(RandomGenerator.generateUniformDouble(7d, 10.1d), RandomGenerator.generateUniformDouble(7d, 10.1d)));
        data3.add(new Observation(RandomGenerator.generateUniformDouble(7d, 10.1d), RandomGenerator.generateUniformDouble(7d, 10.1d)));
        data3.add(new Observation(RandomGenerator.generateUniformDouble(7d, 10.1d), RandomGenerator.generateUniformDouble(7d, 10.1d)));
        data3.add(new Observation(RandomGenerator.generateUniformDouble(7d, 10.1d), RandomGenerator.generateUniformDouble(7d, 10.1d)));

        final List<Observation> data4 = new LinkedList<>();
        data4.add(new Observation(RandomGenerator.generateUniformDouble(7d, 10.1d), RandomGenerator.generateUniformDouble(0d, 3.1d)));
        data4.add(new Observation(RandomGenerator.generateUniformDouble(7d, 10.1d), RandomGenerator.generateUniformDouble(0d, 3.1d)));
        data4.add(new Observation(RandomGenerator.generateUniformDouble(7d, 10.1d), RandomGenerator.generateUniformDouble(0d, 3.1d)));
        data4.add(new Observation(RandomGenerator.generateUniformDouble(7d, 10.1d), RandomGenerator.generateUniformDouble(0d, 3.1d)));
        data4.add(new Observation(RandomGenerator.generateUniformDouble(7d, 10.1d), RandomGenerator.generateUniformDouble(0d, 3.1d)));

        data.put("Data 1", data1);
        data.put("Data 2", data2);
        data.put("Data 3", data3);
        data.put("Data 4", data4);

        return data;
    }

    @Override
    public List<Map<Pair<Integer, Integer>, List<Observation>>> getTrainingDataWithAnswers() {
        final Map<String, List<Observation>> trainingData = getTrainingData();

        final List<Observation> data1 = trainingData.get("Data 1");
        final Map<Pair<Integer, Integer>, List<Observation>> data1Answers = new HashMap<>();
        data1Answers.put(new ImmutablePair<>(-1, -1), data1);

        final List<Observation> data2 = trainingData.get("Data 2");
        final Map<Pair<Integer, Integer>, List<Observation>> data2Answers = new HashMap<>();
        data2Answers.put(new ImmutablePair<>(1, -1), data2);

        final List<Observation> data3 = trainingData.get("Data 3");
        final Map<Pair<Integer, Integer>, List<Observation>> data3Answers = new HashMap<>();
        data3Answers.put(new ImmutablePair<>(1, 1), data3);

        final List<Observation> data4 = trainingData.get("Data 4");
        final Map<Pair<Integer, Integer>, List<Observation>> data4Answers = new HashMap<>();
        data4Answers.put(new ImmutablePair<>(-1, 1), data4);

        final List<Map<Pair<Integer, Integer>, List<Observation>>> trainingDataWithAnswers = new ArrayList<>();
        trainingDataWithAnswers.add(data1Answers);
        trainingDataWithAnswers.add(data2Answers);
        trainingDataWithAnswers.add(data3Answers);
        trainingDataWithAnswers.add(data4Answers);

        return trainingDataWithAnswers;
    }
}
