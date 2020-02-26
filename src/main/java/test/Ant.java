package test;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Data
public class Ant {
    private int currentIndex;
    private List<Integer> visited = new ArrayList<>();
    private List<Path> visitedPathSegments = new ArrayList<>();

    public Ant(int currentIndex) {
        this.currentIndex = currentIndex;
        visited.add(currentIndex);
    }

    public void goTo(int targetIndex) {
        visited.add(targetIndex);
        visitedPathSegments.add(Graph.getPath(currentIndex, targetIndex));
        currentIndex = targetIndex;
    }

    public boolean isPathVisited(Path path) {
        return visited.containsAll(Arrays.asList(path.getA(), path.getB()));
    }
}
