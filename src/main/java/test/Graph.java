package test;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Data
public class Graph {

    public static final int size = 4;
    public static List<Path> graph = new ArrayList<>();

    static {
        graph.add(new Path(0, 1, 20));
        graph.add(new Path(0, 2, 35));
        graph.add(new Path(0, 3, 42));
        graph.add(new Path(1, 2, 34));
        graph.add(new Path(1, 3, 30));
        graph.add(new Path(2, 3, 12));
    }

    public static Path getPath(int i, int j) {
        return graph.stream()
                .filter(path -> path.equals(i, j))
                .findAny()
                .orElse(null);
    }

    public static List<Path> getPossiblePaths(Ant ant) {
        int sourceIndex = ant.getCurrentIndex();
        return graph.stream()
                .filter(path -> isPathConnected(sourceIndex, path))
                .filter(path -> !ant.isPathVisited(path))
                .collect(Collectors.toList());
    }

    public static List<Integer> getPossibleTargets(Ant ant) {
        int sourceIndex = ant.getCurrentIndex();
        return getPossiblePaths(ant).stream()
                .map(path -> path.getA() == sourceIndex ? path.getB() : path.getA())
                .collect(Collectors.toList());
    }

    public static void updateTau(List<Path> route, double deltaTau, double p) {
        graph.stream()
                .peek(path -> path.setTau((1 - p) * path.getTau()))
                .filter(route::contains)
                .forEach(path -> path.setTau(path.getTau() + deltaTau));
    }

    private static boolean isPathConnected(int i, Path path) {
        return path.getA() == i || path.getB() == i;
    }

    public static String log() {
        List<Double> taus = graph.stream().map(Path::getTau).collect(Collectors.toList());

        StringJoiner stringJoiner = new StringJoiner(" | ");

        graph.forEach(path -> {
            stringJoiner.add(String.format("%-3.2f", path.getTau()));

        });
        return stringJoiner.toString();

//        StringBuilder str = new StringBuilder();
//        for (Double tau : taus) {
//            str.append("%-2.2f - ");
//        }
//        return String.format(str.toString(), taus.toArray());
    }
}
