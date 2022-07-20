package com.mygdx.snake.algos;

import java.util.ArrayList;
import com.mygdx.snake.Game;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

import com.mygdx.snake.Point;
import com.mygdx.snake.Snake;
import com.mygdx.snake.Square;

public class AStar {
    HashMap<Point, Double> gScore;
    HashMap<Point, Double> fScore;
    HashMap<Point, Point> cameFrom;
    PriorityQueue<Point> q;
    ArrayList<Square> body;
    Point start;
    private final double INF = Double.POSITIVE_INFINITY;
    Point end;

    public AStar(Snake snake, float food_x, float food_y) {
        start = Game.grid.getPoint(snake.head.x, snake.head.y);
        body = snake.body;
        end = Game.grid.getPoint(food_x, food_y);
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
        //return Math.round(Math.abs(p.x - end.x) + Math.abs(p.y - end.y));
        return Math.sqrt(Math.pow(p.x - end.x, 2) + Math.pow(p.y - end.y, 2));
    }

    private ArrayList<Point> getNeighbors(Point p) {
        ArrayList<Point> tmp = new ArrayList<>();
        Point t;
        if (checkNeighbor(t = new Point(p.x - Game.SQUARE_SIZE, p.y))) tmp.add(Game.grid.getPoint(t));
        if (checkNeighbor(t = new Point(p.x + Game.SQUARE_SIZE, p.y))) tmp.add(Game.grid.getPoint(t));
        if (checkNeighbor(t = new Point(p.x, p.y - Game.SQUARE_SIZE))) tmp.add(Game.grid.getPoint(t));
        if (checkNeighbor(t = new Point(p.x, p.y + Game.SQUARE_SIZE))) tmp.add(Game.grid.getPoint(t));

        return tmp;
    }
    private boolean checkNeighbor(Point p) {
        if (p.x < 0 || p.x >= Game.WIDTH || p.y < 0 || p.y >= Game.HEIGHT) return false;
        for (Square s : body) {
            if (s.x == p.x && s.y == p.y) return false;
        }
        return true;
    }
    private Point getMoveSequence(Point curr) {
        // This is a bit of an abuse of language, but Point here is essentially a vector
        ArrayList<Point> res = new ArrayList<>();
        while(cameFrom.containsKey(curr)) {
            Point prev = cameFrom.get(curr);
            res.add(new Point((curr.x - prev.x) / Game.SQUARE_SIZE, (curr.y - prev.y) / Game.SQUARE_SIZE));
            curr = prev;
        }
        return res.get(res.size() - 1);
    }
    public Point solve() {
        while (!q.isEmpty()) {
            Point current = q.peek();
            //System.out.println("Current: " + current.x + " " + current.y + " " + end.x + " " + end.y);
            if (current.equals(end)) {
                return getMoveSequence(current);
            }
            q.remove();
            //if (!q.isEmpty()) System.out.println("After current: " + q.peek().x + " " + q.peek().y);
            for (Point neighbor : getNeighbors(current)) {
                double tmpGScore = gScore.getOrDefault(current, INF) + 1;
                //System.out.println(neighbor.x + " " + neighbor.y + "-" + tmpGScore + " - " + gScore.getOrDefault(neighbor, 900.0));
                if (tmpGScore < gScore.getOrDefault(neighbor, INF)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tmpGScore);
                    fScore.put(neighbor, tmpGScore + h(neighbor));
                    if (!q.contains(neighbor)) q.add(neighbor);
                }
            }
        }
        return new Point(-1, 0);
    }

}
