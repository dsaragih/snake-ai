package com.mygdx.snake.algos;
import com.mygdx.snake.Game;
import com.mygdx.snake.Point;

import java.util.ArrayList;

public class HamiltonianCycle {
    int size;
    Point head;
    public HamiltonianCycle(Point head) {
        this.head = head;
        // Try a backtracking algorithm
        this.size = (Game.WIDTH * Game.HEIGHT) / (Game.SQUARE_SIZE * Game.SQUARE_SIZE);

    }
    private boolean isSafe(ArrayList<Point> path, Point p) {
        return !path.contains(p);
    }
    private boolean areAdjacent(Point p, Point q) {
        return (Math.abs(p.x - q.x) + Math.abs(p.y - q.y)) == Game.SQUARE_SIZE;
    }
    private boolean solve(ArrayList<Point> path) {
        Point current = path.get(path.size() - 1);
        if (path.size() == this.size) {
            //for (Point p : path) System.out.println("x: " + p.x + " y: " + p.y);
            return areAdjacent(current, path.get(0));
        }
        for (Point p : Game.grid.getNeighbors(current)) {
            if (isSafe(path, p)) {
                path.add(p);
                if (solve(path)) {
                    return true;
                }
                path.remove(p);
            }
        }
        return false;
    }
    public ArrayList<Point> getMoveSeq() {
        ArrayList<Point> path = new ArrayList<>();
        // where the snake starts off
        path.add(Game.grid.getPoint(head));
        if (!solve(path)) return null;
        path.add(path.get(0));
        System.out.println(path.size());

        ArrayList<Point> res = new ArrayList<>();
        for (int i = 0; i < path.size() - 1; ++i) {
            Point curr = path.get(i);
            Point next = path.get(i + 1);
            res.add(new Point((next.x - curr.x) / Game.SQUARE_SIZE, (next.y - curr.y) / Game.SQUARE_SIZE));
        }
        for (Point p : res) {
            System.out.println("dx: " + p.x + "dy: " + p.y);
        }
        return res;
    }

//    public Point solve(float dx, float dy) {
//        if (inInterior(dx, dy)) {
//            return new Point(dx, dy);
//        }
//        else {
//            if (dx != 0) {
//                int y_sq = (int) (head.y / Game.SQUARE_SIZE);
//                if (y_sq % 2 == 0) return new Point(0, 1);
//                else return new Point(0, -1);
//            }
//            else if (dy != 0) {
//                if (checkNotOnBounds(new Point(head.x + Game.SQUARE_SIZE, head.y))) return new Point(1, 0);
//                else return new Point(-1, 0);
//            }
//        }
//        return new Point(0, 1);
//    }
//    private boolean inInterior (float dx, float dy) {
//        return checkNotOnBounds(new Point(head.x + dx * Game.SQUARE_SIZE, head.y + dy * Game.SQUARE_SIZE));
//    }
//    private boolean checkNotOnBounds (Point p) {
//        return !(p.x < 0) && !(p.x >= Game.WIDTH) && !(p.y < 0) && !(p.y >= Game.HEIGHT);
//    }
}
