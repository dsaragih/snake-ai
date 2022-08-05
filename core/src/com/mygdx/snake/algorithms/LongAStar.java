package com.mygdx.snake.algorithms;

import java.util.*;

import com.mygdx.snake.*;
import com.mygdx.snake.algorithms.utilities.AStarUtils;


public class LongAStar {
    HashMap<Point, Double> gScore;
    HashMap<Point, Double> fScore;
    HashMap<Point, Point> cameFrom;
    PriorityQueue<Point> q;
    AStarUtils util;
    List<Point> body;
    Point start;
    private final double INF = Double.POSITIVE_INFINITY;
    Point end;

    public LongAStar(List<Point> body, Food food) {
        util = new AStarUtils(body);
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
        return -Math.sqrt(Math.pow(p.x - end.x, 2) + Math.pow(p.y - end.y, 2));
    }

    private void calculate(Point current, Point neighbor) {
        double tmpGScore = gScore.getOrDefault(current, INF) - 1;

        if (!gScore.containsKey(neighbor)) {
            cameFrom.put(neighbor, current);
            gScore.put(neighbor, tmpGScore);
            fScore.put(neighbor, tmpGScore + h(neighbor));
            if (!q.contains(neighbor)) {
                q.add(neighbor);

            }
        }
    }

    public ArrayList<Point> solve() {
        Point current;
        while (!q.isEmpty()) {
            current = q.poll();
            ArrayList<Point> seq = util.getMoveSequence(current, cameFrom);

            int score = util.moveSnake(seq, new ArrayList<>(body));

            if (current.equals(end) && score > (Game.SIZE - util.body.size()) / 2) {
                return seq;
            } else if (current.equals(end)) return seq;
            for (Point neighbor : util.getNotLostNeighbors(current)) {
                calculate(current, neighbor);
            }
        }
        return new ArrayList<>(Arrays.asList(new Point(0, 0)));
    }
}
