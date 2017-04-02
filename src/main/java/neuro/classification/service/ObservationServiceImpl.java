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

//        final List<Observation> data1 = new LinkedList<>();
//        data1.add(new Observation(1d, 1d));
//        data1.add(new Observation(1d, 2d));
//        data1.add(new Observation(2d, 2d));
//        data1.add(new Observation(2d, 1d));
//        data1.add(new Observation(1.5d, 1.5d));
//
//        final List<Observation> data2 = new LinkedList<>();
//        data2.add(new Observation(1d, 7d));
//        data2.add(new Observation(1d, 8d));
//        data2.add(new Observation(2d, 8d));
//        data2.add(new Observation(2d, 7d));
//        data2.add(new Observation(1.5d, 7.5d));
//
//        final List<Observation> data3 = new LinkedList<>();
//        data3.add(new Observation(7d, 7d));
//        data3.add(new Observation(7d, 8d));
//        data3.add(new Observation(8d, 8d));
//        data3.add(new Observation(8d, 7d));
//        data3.add(new Observation(7.5d, 7.5d));
//
//        final List<Observation> data4 = new LinkedList<>();
//        data4.add(new Observation(7d, 1d));
//        data4.add(new Observation(7d, 2d));
//        data4.add(new Observation(8d, 2d));
//        data4.add(new Observation(8d, 1d));
//        data4.add(new Observation(7.5d, 1.5d));

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
        data1Answers.put(new ImmutablePair<>(0, 0), data1);

        final List<Observation> data2 = trainingData.get("Data 2");
        final Map<Pair<Integer, Integer>, List<Observation>> data2Answers = new HashMap<>();
        data2Answers.put(new ImmutablePair<>(1, 0), data2);

        final List<Observation> data3 = trainingData.get("Data 3");
        final Map<Pair<Integer, Integer>, List<Observation>> data3Answers = new HashMap<>();
        data3Answers.put(new ImmutablePair<>(1, 1), data3);

        final List<Observation> data4 = trainingData.get("Data 4");
        final Map<Pair<Integer, Integer>, List<Observation>> data4Answers = new HashMap<>();
        data4Answers.put(new ImmutablePair<>(0, 1), data4);

        final List<Map<Pair<Integer, Integer>, List<Observation>>> trainingDataWithAnswers = new ArrayList<>();
        trainingDataWithAnswers.add(data1Answers);
        trainingDataWithAnswers.add(data2Answers);
        trainingDataWithAnswers.add(data3Answers);
        trainingDataWithAnswers.add(data4Answers);

        return trainingDataWithAnswers;
    }

    @Override
    public List<Map<Pair<Integer, Integer>, List<Observation>>> getTestDataWithAnswers() {
        return getTrainingDataWithAnswers();

//        final Map<String, List<Observation>> testData = getTestData();
//
//        final List<Observation> data1 = testData.get("Data 1");
//        final Map<Pair<Integer, Integer>, List<Observation>> data1Answers = new HashMap<>();
//        data1Answers.put(new ImmutablePair<>(0, 0), data1);
//
//        final List<Observation> data2 = testData.get("Data 2");
//        final Map<Pair<Integer, Integer>, List<Observation>> data2Answers = new HashMap<>();
//        data2Answers.put(new ImmutablePair<>(1, 0), data2);
//
//        final List<Observation> data3 = testData.get("Data 3");
//        final Map<Pair<Integer, Integer>, List<Observation>> data3Answers = new HashMap<>();
//        data3Answers.put(new ImmutablePair<>(1, 1), data3);
//
//        final List<Observation> data4 = testData.get("Data 4");
//        final Map<Pair<Integer, Integer>, List<Observation>> data4Answers = new HashMap<>();
//        data4Answers.put(new ImmutablePair<>(0, 1), data4);
//
//        final List<Map<Pair<Integer, Integer>, List<Observation>>> testDataWithAnswers = new ArrayList<>();
//        testDataWithAnswers.add(data1Answers);
//        testDataWithAnswers.add(data2Answers);
//        testDataWithAnswers.add(data3Answers);
//        testDataWithAnswers.add(data4Answers);
//
//        return testDataWithAnswers;
    }

    private Map<String, List<Observation>> getTestData() {

        final Map<String, List<Observation>> data = new TreeMap<>();

        final List<Observation> data1 = new LinkedList<>();
        data1.add(new Observation(1.3d, 1.3d));
        data1.add(new Observation(1.3d, 2.3d));
        data1.add(new Observation(2.3d, 2.3d));
        data1.add(new Observation(2.3d, 1.3d));
        data1.add(new Observation(1.7d, 1.7d));

        final List<Observation> data2 = new LinkedList<>();
        data2.add(new Observation(1.3d, 7.3d));
        data2.add(new Observation(1.3d, 8.3d));
        data2.add(new Observation(2.3d, 8.3d));
        data2.add(new Observation(2.3d, 7.3d));
        data2.add(new Observation(1.7d, 7.7d));

        final List<Observation> data3 = new LinkedList<>();
        data3.add(new Observation(7.3d, 7.3d));
        data3.add(new Observation(7.3d, 8.3d));
        data3.add(new Observation(8.3d, 8.3d));
        data3.add(new Observation(8.3d, 7.3d));
        data3.add(new Observation(7.7d, 7.7d));

        final List<Observation> data4 = new LinkedList<>();
        data4.add(new Observation(7.3d, 1.3d));
        data4.add(new Observation(7.3d, 2.3d));
        data4.add(new Observation(8.3d, 2.3d));
        data4.add(new Observation(8.3d, 1.3d));
        data4.add(new Observation(7.7d, 1.7d));

        data.put("Data 1", data1);
        data.put("Data 2", data2);
        data.put("Data 3", data3);
        data.put("Data 4", data4);

        return data;
    }

}
