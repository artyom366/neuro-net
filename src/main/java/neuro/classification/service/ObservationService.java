package neuro.classification.service;

import neuro.classification.domain.Observation;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

public interface ObservationService {

    Map<String, List<Observation>> getTrainingData();

    List<Map<Pair<Integer, Integer>, List<Observation>>> getTrainingDataWithAnswers();
}
