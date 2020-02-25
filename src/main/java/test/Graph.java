package test;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Graph {

    public static final int size = 4;
    private static List<Path> graph = new ArrayList<>();
    private static List<Integer> visited = new ArrayList<>();

    static {
        graph.add(new Path(0, 1, 20));
        graph.add(new Path(0, 2, 35));
        graph.add(new Path(0, 3, 42));
        graph.add(new Path(1, 2, 34));
        graph.add(new Path(1, 3, 30));
        graph.add(new Path(2, 3, 12));
        visited.add(0);
    }

    public Path getPath(int i, int j) {
        return graph.stream()
                .filter(path -> path.equals(i, j))
                .findAny()
                .orElse(null);
    }

    public List<Path> getPossiblePaths(int i) {
        return graph.stream()
                .filter(path -> isPathConnected(i, path))
                .filter(path -> !isPathVisited(path))
                .collect(Collectors.toList());
    }

    public List<Integer> getPossiblePathsIndexes(int i) {
        return getPossiblePaths(i).stream()
                .map(path -> path.getA() == i ? path.getB() : path.getA())
                .collect(Collectors.toList());
    }

    private boolean isPathConnected(int i, Path path){
        return path.getA() == i || path.getB() == i;
    }

    private boolean isPathVisited(Path path) {
        return visited.containsAll(Arrays.asList(path.getA(), path.getB()));
    }

    public void setVisited(int i){
        visited.add(i);
    }
}
