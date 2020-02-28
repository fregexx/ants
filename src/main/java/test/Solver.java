package test;

import java.util.ArrayList;
import java.util.List;

public class Solver {

    public static final int[][] array = new int[][]{
            {0, 20, 35, 42},
            {20, 0, 34, 30},
            {35, 34, 0, 12},
            {42, 30, 12, 0}
    };

    public static final int INITIAL_INDEX = 0;

    public static List<Integer> path = new ArrayList<>();

    public Solver() {
        path.add(INITIAL_INDEX);
    }

    public List<Integer> solve() {
        int sourceIndex = INITIAL_INDEX;
        for (int i = 0; i < array.length-1; i++) {
            int targetIndex = getShortestPathIndexFrom(sourceIndex);
            path.add(targetIndex);
            sourceIndex = targetIndex;
        }
        System.out.println(path);
        return path;
    }

    private int getShortestPathIndexFrom(int sourceIndex) {
        int[] targets = array[sourceIndex];
        int minIndex = INITIAL_INDEX;
        int minPath = Integer.MAX_VALUE;
        for (int i = 0; i < targets.length; i++) {
            if (i == sourceIndex || path.contains(i)) {
                continue;
            }
            if (targets[i] < minPath) {
                minIndex = i;
                minPath = targets[i];
            }
        }
        return minIndex;
    }
}
