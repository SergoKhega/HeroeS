package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.List;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {

    private static final int GRID_WIDTH = 27;
    private static final int GRID_HEIGHT = 21;
    private static final int[][] MOVEMENT = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    @Override
    public List<Edge> getTargetPath(Unit startUnit, Unit endUnit, List<Unit> obstacles) {
        int startX = startUnit.getxCoordinate();
        int startY = startUnit.getyCoordinate();
        int endX = endUnit.getxCoordinate();
        int endY = endUnit.getyCoordinate();

        int[][] map = new int[GRID_HEIGHT][GRID_WIDTH];

        for (Unit unit : obstacles) {
            if (unit.isAlive() && !unit.equals(startUnit) && !unit.equals(endUnit)) {
                map[unit.getyCoordinate()][unit.getxCoordinate()] = -1;
            }
        }

        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{startX, startY});
        map[startY][startX] = 1;

        boolean destinationFound = false;
        while (!queue.isEmpty() && !destinationFound) {
            int[] currentPosition = queue.poll();
            int x = currentPosition[0];
            int y = currentPosition[1];

            for (int i = 0; i < MOVEMENT.length; i++) {
                int[] direction = MOVEMENT[i];
                int newX = x + direction[0];
                int newY = y + direction[1];

                if (isValidMove(newX, newY) && map[newY][newX] == 0) {
                    map[newY][newX] = map[y][x] + 1;
                    queue.offer(new int[]{newX, newY});
                    if (newX == endX && newY == endY) {
                        destinationFound = true;
                        break;
                    }
                }
            }
        }

        if (map[endY][endX] == 0) {
            return new ArrayList<>();
        }

        return rebuildPath(map, startX, startY, endX, endY);
    }

    private boolean isValidMove(int x, int y) {
        return x >= 0 && x < GRID_WIDTH && y >= 0 && y < GRID_HEIGHT;
    }

    private List<Edge> rebuildPath(int[][] map, int startX, int startY, int endX, int endY) {
        List<Edge> path = new LinkedList<>();
        int x = endX;
        int y = endY;

        while (!(x == startX && y == startY)) {
            path.add(new Edge(x, y));
            int currentStep = map[y][x];

            for (int[] direction : MOVEMENT) {
                int newX = x + direction[0];
                int newY = y + direction[1];

                if (isValidMove(newX, newY) && map[newY][newX] > 0 && map[newY][newX] < currentStep) {
                    x = newX;
                    y = newY;
                    break;
                }
            }
        }
        path.add(new Edge(startX, startY));
        Collections.reverse(path);
        return path;
    }
}

