package com.mygdx.snake.algos;

import com.mygdx.snake.Food;
import com.mygdx.snake.Game;
import com.mygdx.snake.Point;

import java.util.*;

public class CycleShortcut {
    HashMap<Point, Double> gScore;
    HashMap<Point, Double> fScore;
    HashMap<Point, Point> cameFrom;
    PriorityQueue<Point> q;
    GridUtils grid;
    List<Point> body;
    List<Point> cycle;
    Point start;
    private double INF = Double.POSITIVE_INFINITY;
    Point end;

    public CycleShortcut(List<Point> body, Food food, List<Point> cycle) {
        grid = new GridUtils(body);
        this.cycle = cycle;
        this.body = body;
        start = grid.getPoint(body.get(0));
        end = grid.getPoint(food.x, food.y);
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
    private void moveSnake(List<Point> seq, List<Point> body) {
        Point head;
        for (Point dir : seq) {
            head = body.get(0);
            body.remove(body.size() - 1);
            float new_x = head.x + dir.x;
            float new_y = head.y + dir.y;
            head = new Point(new_x, new_y);
            body.add(0, head);
            grid.update(body);
        }
    }
    public ArrayList<Point> hamSolve() {
        /*
        Given the position of the food, find shortcuts through the Hamiltonian cycle to
        reach the food as efficiently as possible.
         */
        while (!q.isEmpty()) {
            Point current = q.poll();
            ArrayList<Point> seq = getMoveSequence(current);
            moveSnake(seq, new ArrayList<>(body));

            if (current.equals(end)) {
                return seq;
            }
            for (Point neighbor : grid.getNotLostNeighbors(current)) {
                if (!respectsOrder(current, neighbor)) continue;
                calculate(current, neighbor);
            }
        }
        System.out.println("End of hamilton");
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
        Point tail = grid.body.get(grid.body.size() - 1);
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

    private ArrayList<Point> getMoveSequence(Point curr) {
        // This is a bit of an abuse of language, but Point here is essentially a vector
        ArrayList<Point> res = new ArrayList<>();
        while(cameFrom.containsKey(curr)) {
            Point prev = cameFrom.get(curr);
            res.add(0, new Point(curr.x - prev.x, curr.y - prev.y));
            curr = prev;
        }
        return res;
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
