package com.mygdx.snake.algos;
import com.mygdx.snake.Game;
import com.mygdx.snake.Point;

import java.util.ArrayList;

public class RecursiveHamCycle {
    int size;
    Point head;
    ArrayList<Point> path;
    public RecursiveHamCycle(Point head) {
        path = new ArrayList<>();
        this.head = head;
        this.size = (Game.WIDTH * Game.HEIGHT) / (Game.SQUARE_SIZE * Game.SQUARE_SIZE);

    }
    private boolean isSafe(ArrayList<Point> path, Point p) {
        return !path.contains(p);
    }
    private boolean areAdjacent(Point p, Point q) {
        return (Math.abs(p.x - q.x) + Math.abs(p.y - q.y)) == Game.SQUARE_SIZE;
    }
    private boolean solve() {
        Point current = path.get(path.size() - 1);
        if (path.size() == this.size) {
            return areAdjacent(current, path.get(0));
        }
        for (Point p : Game.grid.getNeighbors(current)) {
            if (isSafe(path, p)) {
                path.add(p);
                if (solve()) {
                    return true;
                }
                path.remove(p);
            }
        }
        return false;
    }
    public ArrayList<Point> getMoveSeq() {
        // where the snake starts off
        path.add(Game.grid.getPoint(head));
        if (!solve()) return null;
        path.add(path.get(0));

        ArrayList<Point> res = new ArrayList<>();
        for (int i = 0; i < path.size() - 1; ++i) {
            Point curr = path.get(i);
            Point next = path.get(i + 1);
            res.add(new Point((next.x - curr.x) / Game.SQUARE_SIZE, (next.y - curr.y) / Game.SQUARE_SIZE));
        }
        return res;
    }

}
