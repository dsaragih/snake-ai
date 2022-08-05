package com.mygdx.snake.algorithms;

import com.mygdx.snake.Food;
import com.mygdx.snake.Game;
import com.mygdx.snake.Point;
import com.mygdx.snake.algorithms.utilities.AStarUtils;

import java.util.*;

public class CycleShortcut {
    HashMap<Point, Double> gScore;
    HashMap<Point, Double> fScore;
    HashMap<Point, Point> cameFrom;
    PriorityQueue<Point> q;
    AStarUtils util;
    List<Point> body;
    List<Point> cycle;
    Point start;
    private final double INF = Double.POSITIVE_INFINITY;
    Point end;

    public CycleShortcut(List<Point> body, Food food, List<Point> cycle) {
        this.util = new AStarUtils(body);
        this.cycle = cycle;
        this.body = body;
        start = util.getPoint(body.get(0));
        end = util.getPoint(food.x, food.y);
        gScore = new HashMap<>();
        fScore = new HashMap<>();
        cameFrom = new HashMap<>();
        gScore.put(start, 0.0);
        fScore.put(start, h(start));
        q = new PriorityQueue<>(new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                double c = fScore.get(o1) - fScore.get(o2);
                if (c < 0) return -1;
                else if (c > 0) return 1;
                else return 0;
            }
        });
        q.add(start);
    }
    private double h(Point p) {
        int endIdx = whereInCycle(end);
        int currIdx = whereInCycle(p);

        if (endIdx >= currIdx) return endIdx - currIdx;
        else {
            return endIdx + Game.SIZE - currIdx;
        }
    }
    public ArrayList<Point> hamSolve() {
        /*
        Given the position of the food, find shortcuts through the Hamiltonian cycle to
        reach the food as efficiently as possible.
         */
        while (!q.isEmpty()) {
            Point current = q.poll();
            ArrayList<Point> seq = util.getMoveSequence(current, cameFrom);
            util.moveSnake(seq, new ArrayList<>(body));

            if (current.equals(end)) {
                return seq;
            }
            for (Point neighbor : util.getNotLostNeighbors(current)) {
                if (!respectsOrder(current, neighbor)) continue;
                calculate(current, neighbor);
            }
        }
        return new ArrayList<>(Arrays.asList(new Point(0, 0)));
    }

    public int whereInCycle(Point p) {
        int idx = 0;
        for (int i = 0; i < cycle.size(); i++) {
            if (cycle.get(i).equals(p)) idx = i;
        }
        return idx;
    }

    private boolean respectsOrder(Point curr, Point next) {
        /*
        Order: tail - head - tail
        Precondition: Order respected
         */
        Point tail = util.body.get(util.body.size() - 1);
        int tailIdx = whereInCycle(tail);
        int headIdx = whereInCycle(curr);
        int nextIdx = whereInCycle(next);
        int nextIsFood = 1; //next.equals(end) ? 1 : 0;

        if (headIdx >= tailIdx) {
            return (nextIdx > headIdx && nextIdx < Game.SIZE + tailIdx - nextIsFood) || nextIdx < tailIdx - nextIsFood;
        } else {
            return nextIdx > headIdx && nextIdx < tailIdx - nextIsFood;
        }
    }


    private void calculate(Point current, Point neighbor) {
        double tmpGScore = gScore.getOrDefault(current, INF) + 1;
        if (tmpGScore < gScore.getOrDefault(neighbor, INF)) {
            cameFrom.put(neighbor, current);
            gScore.put(neighbor, tmpGScore);
            fScore.put(neighbor, tmpGScore + h(neighbor));
            if (!q.contains(neighbor)) {
                q.add(neighbor);

            }
        }

    }

}
