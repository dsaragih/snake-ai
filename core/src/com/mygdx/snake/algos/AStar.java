package com.mygdx.snake.algos;

import java.util.*;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.snake.Game;

import javafx.util.Pair;

import com.mygdx.snake.Point;
import com.mygdx.snake.Snake;
import com.mygdx.snake.Square;

public class AStar {
    HashMap<Point, Double> gScore;
    HashMap<Point, Double> fScore;
    HashMap<Point, Point> cameFrom;
    PriorityQueue<Point> q;
    GridUtils grid;
    ArrayList<Square> body;
    Point start;
    private double INF = Double.POSITIVE_INFINITY;
    Point end;

    public AStar(Snake snake, float food_x, float food_y) {
        grid = new GridUtils(snake.body);
        this.body = snake.body;
        start = grid.getPoint(snake.head.x, snake.head.y);
        end = grid.getPoint(food_x, food_y);
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
    private int moveSnake(ArrayList<Point> seq, ArrayList<Square> body) {
        Square head = body.get(0);
        for (Point dir : seq) {
            head = body.get(0);
            body.remove(body.size() - 1);
            float new_x = head.x + dir.x * Game.SQUARE_SIZE;
            float new_y = head.y + dir.y * Game.SQUARE_SIZE;
            head = new Square(new_x, new_y, Color.GREEN);
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
    public ArrayList<Point> solve() {
        return short_solve();
    }

    private void isPathTrapped(Point curr, ArrayList<Point> visited, Stack<Point> stack) {
        visited.add(curr);
        stack.add(curr);
        int size = (Game.WIDTH * Game.HEIGHT) / (Game.SQUARE_SIZE * Game.SQUARE_SIZE);

        if (visited.size() > (size - grid.body.size()) / 2) return;
        for (Point neighbor : grid.getNotLostNeighbors(curr)) {
            if (visited.contains(neighbor)) continue;
            visited.add(neighbor);
            stack.add(neighbor);
            isPathTrapped(neighbor, visited, stack);
        }
        if (!stack.isEmpty()) stack.remove(curr);
    }

    private ArrayList<Point> short_solve() {
        Pair<ArrayList<Point>, Integer> pair = new Pair<>(new ArrayList<Point>(), 0);
        Point current;
        while (!q.isEmpty()) {
            current = q.poll();
            ArrayList<Point> seq = getMoveSequence(current);

            int score = moveSnake(seq, new ArrayList<>(body));
            int size = (Game.WIDTH * Game.HEIGHT) / (Game.SQUARE_SIZE * Game.SQUARE_SIZE);

            if (current.equals(end) && score > (size - grid.body.size()) / 2) {
                System.out.println("Score: " + score);
                return seq;
            } else if (current.equals(end)) {
                if (pair.getValue() < score) pair = new Pair<>(seq, score);
            }

            for (Point neighbor : grid.getNotLostNeighbors(current)) {
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
        }
        System.out.println("Stops??");
        return pair.getKey().size() == 0 ? new ArrayList<>(Arrays.asList(new Point(0, 0))) : pair.getKey();
    }
}
