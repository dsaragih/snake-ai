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

        if (tmpGScore < gScore.getOrDefault(neighbor, INF)) {
            cameFrom.put(neighbor, current);
            gScore.put(neighbor, tmpGScore);
            fScore.put(neighbor, tmpGScore + h(neighbor));
            if (!q.contains(neighbor)) {
                q.add(neighbor);

            }
        }
    }

    private List<Point> stayAlive(Point head) {
        Point bestPoint = head;
        int bestScore = 0;
        util.update(body);

        for (Point neighbor : util.getNotLostNeighbors(head)) {
            int score = util.getVisited(neighbor).size();

            if (score >= bestScore) {
                bestPoint = neighbor;
                bestScore = score;
            }
        }
        ArrayList<Point> res = new ArrayList<>();
        res.add(new Point(bestPoint.x - start.x, bestPoint.y - start.y));
        return res;
    }

    private boolean inOthersMoveSeq(Point current, Point neighbor) {
        Point iter1 = neighbor;
        while(cameFrom.containsKey(iter1)) {
            iter1 = cameFrom.get(iter1);
            if (iter1.equals(current)) return true;
        }
        Point iter2 = current;
        while(cameFrom.containsKey(iter2)) {
            iter2 = cameFrom.get(iter2);
            if (iter2.equals(neighbor)) return true;
        }
        return false;
    }

    public List<Point> solve() {

        Point current;
        int threshold = (Game.SIZE - util.body.size()) / 2;
        while (!q.isEmpty()) {
            current = q.poll();
            ArrayList<Point> seq = util.getMoveSequence(current, cameFrom);

            int score = util.moveSnake(seq, new ArrayList<>(body));

            if (current.equals(end) && score > threshold) {
                return seq;
            }
            if (score > threshold) {
                for (Point neighbor : util.getNotLostNeighbors(current)) {
                    if (inOthersMoveSeq(current, neighbor)) continue;
                    calculate(current, neighbor);
                }
            }
        }
        return stayAlive(start);
    }
}
