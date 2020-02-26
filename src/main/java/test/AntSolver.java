package test;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Range;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Data
public class AntSolver {

    private static double beta = 1;
    private static double alpha = 1;
    private static int START_INDEX = 0;
    private int ANTS_COUNT = 40;
    private double q = 20;    // коэфициент оставляемого феромона
    private double p = 0.2;    // коэфициент испарения феромона < 1

    private Graph graph = new Graph();

    public void solve() {
        for (int i = 0; i < ANTS_COUNT; i++) {
            Ant ant = new Ant(START_INDEX);

            findPath(ant);
            double totalPath = getTotalPathLength(ant);
            double deltaTau = getDeltaTau(totalPath);
            Graph.updateTau(ant.getVisitedPathSegments(), deltaTau, p);
            logResults(i, ant, totalPath);
        }

    }

    private static void logResults(int i, Ant ant, double totalPath) {
        List<Integer> path = ant.getVisited();
        StringJoiner stringJoiner = new StringJoiner(" -> ");
        path.forEach(ind -> stringJoiner.add(String.valueOf(ind)));

        log.info("Ant {}, Path: [ {} ], Total length: {}   ###   Tau values: | {} |", String.format("%3s", i), stringJoiner.toString(), String.format("%5s", totalPath), Graph.log());
    }

    private void findPath(Ant ant) {
        for (int i = 0; i < Graph.size - 1; i++) {
            List<Integer> possibleTargets = Graph.getPossibleTargets(ant);
            Map<Double, Integer> probabilities = getProbabilities(possibleTargets, ant);    // probability -> index

            Integer targetIndex = getTargetIndex(probabilities);
            ant.goTo(targetIndex);
        }
//        ant.goTo(START_INDEX);
    }

    // вероятности перехода в возможные вершины
    private Map<Double, Integer> getProbabilities(List<Integer> possibleTargets, Ant ant) {
        return possibleTargets.stream()
                .collect(Collectors.toMap(i -> getProbability(i, ant), Function.identity()));
    }

    // вероятность перехода в вершину графа
    private double getProbability(int targetIndex, Ant ant) {
        int currentIndex = ant.getCurrentIndex();
        Path path = Graph.getPath(currentIndex, targetIndex);
        double sum = getPossiblePathsValuesSum(ant);

        return 100 * path.getValue(beta, alpha) / sum;
    }

    private double getPossiblePathsValuesSum(Ant ant) {
        List<Path> paths = Graph.getPossiblePaths(ant);
        return paths.stream()
                .map(path -> path.getValue(beta, alpha))
                .mapToDouble(Double::valueOf)
                .sum();
    }

    // выбор вершины в которую идём с учетом вероятностей
    private Integer getTargetIndex(Map<Double, Integer> probabilities) {
        Random random = new Random();
        double rndProb = random.nextDouble() * 100;

        SortedMap<Double, Integer> sortedMap = new TreeMap<>(probabilities);
        Iterator<Double> iterator = sortedMap.keySet().iterator();

        Map<Range<Double>, Integer> rangeToIndex = new HashMap<>();

        double start = 0;
        while (iterator.hasNext()) {
            double next = iterator.next();
            double end = start + next;
            rangeToIndex.put(Range.between(start, end), sortedMap.get(next));
            start = end;
        }

        return rangeToIndex.entrySet().stream()
                .filter(entry -> entry.getKey().contains(rndProb))
                .map(Map.Entry::getValue)
                .findAny().orElseThrow(() -> new RuntimeException("Error calculating target index"));
    }

    private double getTotalPathLength(Ant ant) {
        return ant.getVisitedPathSegments().stream()
                .mapToDouble(Path::getDistance)
                .sum();
    }

    private double getDeltaTau(double totalPath) {
        return q / totalPath;
    }
}
