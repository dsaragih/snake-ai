package com.mygdx.snake.algos;

import java.util.*;

import com.mygdx.snake.*;


public class AStar {
    HashMap<Point, Double> gScore;
    HashMap<Point, Double> fScore;
    HashMap<Point, Point> cameFrom;
    PriorityQueue<Point> q;
    GridUtils grid;
    List<Point> body;
    Point start;
    private final double INF = Double.POSITIVE_INFINITY;
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
