package com.mygdx.snake.algos;

import java.lang.reflect.Array;
import java.util.*;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.snake.*;

import javafx.util.Pair;

public class AStar {
    HashMap<Point, Double> gScore;
    HashMap<Point, Double> fScore;
    HashMap<Point, Point> cameFrom;
    PriorityQueue<Point> q;
    GridUtils grid;
    List<Point> body;
    Point start;
    private double INF = Double.POSITIVE_INFINITY;
    Point end;

    public AStar(List<Point> body, Food food) {
        grid = new GridUtils(body);
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
        return Math.sqrt(Math.pow(p.x - end.x, 2) + Math.pow(p.y - end.y, 2));
    }
    private int moveSnake(List<Point> seq, List<Point> body) {
        Point head = body.get(0);
        for (Point dir : seq) {
            head = body.get(0);
            body.remove(body.size() - 1);
            float new_x = head.x + dir.x;
            float new_y = head.y + dir.y;
            head = new Point(new_x, new_y);
            body.add(0, head);
            grid.update(body);
        }
        Stack<Point> stack = new Stack<>();
        ArrayList<Point> visited = new ArrayList<>();
        isPathTrapped(new Point(head.x, head.y), visited, stack);

        return visited.size();
    }
    public ArrayList<Point> hamSolve(List<Point> cycle) {
        /*
        Given the position of the food, find shortcuts through the Hamiltonian cycle to
        reach the food as efficiently as possible.
         */
        while (!q.isEmpty()) {
            Point current = q.poll();
            ArrayList<Point> seq = getMoveSequence(current);
            int score = moveSnake(seq, new ArrayList<>(body));

            if (current.equals(end)) {
                return seq;
            }
            for (Point neighbor : grid.getNeighbors(current)) {
                if (!respectsOrder(current, neighbor, cycle)) continue;
                calculate(current, neighbor);
            }
        }
        return new ArrayList<>(Arrays.asList(new Point(0, 0)));
    }
    private int whereInCycle(Point p, List<Point> cycle) {
        int idx = 0;
        for (int i = 0; i < cycle.size(); i++) {
            if (cycle.get(i).equals(p)) idx = i;
        }
        return idx;
    }

    private boolean respectsOrder(Point curr, Point next, List<Point> cycle) {
        /*
        Order: tail - head - tail
        Precondition: Order respected
         */
        Point tail = grid.body.get(grid.body.size() - 1);
        int tailIdx = whereInCycle(tail, cycle);
        int headIdx = whereInCycle(curr, cycle);
        int nextIdx = whereInCycle(next, cycle);

        if (headIdx >= tailIdx) {
            return nextIdx > headIdx || nextIdx < tailIdx - 1;
        } else {
            return nextIdx > headIdx && nextIdx < tailIdx - 1;
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
        //System.out.println("Neighs: " + neighbor.x + " " + neighbor.y );
        if (tmpGScore < gScore.getOrDefault(neighbor, INF)) {
            cameFrom.put(neighbor, current);
            gScore.put(neighbor, tmpGScore);
            //System.out.println(tmpGScore + h(neighbor));
            fScore.put(neighbor, tmpGScore + h(neighbor));
            if (!q.contains(neighbor)) {
                q.add(neighbor);

            }
        }

    }

    private void isPathTrapped(Point curr, ArrayList<Point> visited, Stack<Point> stack) {
        visited.add(curr);
        stack.add(curr);

        if (visited.size() > (Game.SIZE - grid.body.size()) / 2) return;
        for (Point neighbor : grid.getNotLostNeighbors(curr)) {
            if (visited.contains(neighbor)) continue;
            visited.add(neighbor);
            stack.add(neighbor);
            isPathTrapped(neighbor, visited, stack);
        }
        if (!stack.isEmpty()) stack.remove(curr);
    }

    public ArrayList<Point> solve() {
        Point current;
        while (!q.isEmpty()) {
            current = q.poll();
            ArrayList<Point> seq = getMoveSequence(current);

            int score = moveSnake(seq, new ArrayList<>(body));

            if (current.equals(end) && score > (Game.SIZE - grid.body.size() / 2)) {
                return seq;
            }
            for (Point neighbor : grid.getNotLostNeighbors(current)) {
                calculate(current, neighbor);
            }

        }
        return new ArrayList<>(Arrays.asList(new Point(0, 0)));
    }
}
