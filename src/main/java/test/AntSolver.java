package test;

import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AntSolver {

    private static double beta = 1;
    private static double alpha = 1;
    private int currentIndex = 0;

    private Graph graph = new Graph();

    public void solve() {
        List<Integer> possiblePathsIndexes = graph.getPossiblePathsIndexes(currentIndex);
        Map<Double, Integer> probabilities = possiblePathsIndexes.stream()
                .collect(Collectors.toMap(this::getP, Function.identity()));

        Integer targetIndex = getTargetIndex(probabilities);
        goTo(targetIndex);
        System.out.println();
    }

    private void goTo(int targetIndex) {
        currentIndex = targetIndex;
        graph.setVisited(targetIndex);
    }

    public double getP(int targetIndex) {
        Path path = graph.getPath(currentIndex, targetIndex);
        double sum = getPossiblePathsValuesSum(currentIndex);

        return 100 * path.getValue(beta, alpha) / sum;
    }

    private Integer getTargetIndex(Map<Double, Integer> probabilities) {
        Random random = new Random();
        double rndProb = random.nextDouble() * 100;

        SortedMap<Double, Integer> sortedMap = new TreeMap<>(probabilities);

        Map<Range<Double>, Integer> map = new HashMap<>();

        Iterator<Double> iterator = sortedMap.keySet().iterator();

        double start = 0;
        while (iterator.hasNext()) {
            double next = iterator.next();
            double end = start + next;
            map.put(Range.between(start, end), sortedMap.get(next));
            start = end;
        }

        return map.entrySet().stream()
                .filter(entry -> entry.getKey().contains(rndProb))
                .map(Map.Entry::getValue)
                .findAny().orElse(null);
    }

    private double getPossiblePathsValuesSum(int currentIndex) {
        List<Path> paths = graph.getPossiblePaths(currentIndex);
        return paths.stream()
                .map(path -> path.getValue(beta, alpha))
                .mapToDouble(Double::valueOf)
                .sum();
    }
}
