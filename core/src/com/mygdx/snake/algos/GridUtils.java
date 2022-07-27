package com.mygdx.snake.algos;

import com.mygdx.snake.Game;
import com.mygdx.snake.Point;
import com.mygdx.snake.Square;

import java.util.ArrayList;

public class GridUtils {
    public ArrayList<ArrayList<Point>> Matrix = new ArrayList<>();
    public ArrayList<Square> body;

    public GridUtils(ArrayList<Square> body) {
        this.body = body;
        for (int i = 0; i < Game.HEIGHT; i += Game.SQUARE_SIZE) {
            ArrayList<Point> tmp = new ArrayList<>();
            for (int j = 0; j < Game.WIDTH; j += Game.SQUARE_SIZE) {
                tmp.add(new Point(j, i));
            }
            Matrix.add(tmp);
        }
    }
    public GridUtils() {
        this(new ArrayList<Square>());
    }
    public Point getPoint(float x, float y) {
        int g_y = (int) (y / Game.SQUARE_SIZE);
        int g_x = (int) (x / Game.SQUARE_SIZE);
        return Matrix.get(g_y).get(g_x);
    }
    public Point getPoint(Point p) {
        return getPoint(p.x, p.y);
    }

    public void update(ArrayList<Square> body) {
        this.body = body;
    }
    public ArrayList<Point> getNeighbors(Point p) {
        ArrayList<Point> tmp = new ArrayList<>();
        Point t;
        if (checkInBounds(t = new Point(p.x - Game.SQUARE_SIZE, p.y))) tmp.add(getPoint(t));
        if (checkInBounds(t = new Point(p.x + Game.SQUARE_SIZE, p.y))) tmp.add(getPoint(t));
        if (checkInBounds(t = new Point(p.x, p.y - Game.SQUARE_SIZE))) tmp.add(getPoint(t));
        if (checkInBounds(t = new Point(p.x, p.y + Game.SQUARE_SIZE))) tmp.add(getPoint(t));

        return tmp;
    }
    private boolean checkInBounds(Point p) {
        if (p.x < 0 || p.x >= Game.WIDTH || p.y < 0 || p.y >= Game.HEIGHT) return false;
        return true;
    }
    public ArrayList<Point> getNotLostNeighbors(Point p) {
        ArrayList<Point> tmp = new ArrayList<>();
        Point t;
        if (checkDoesntLose(t = new Point(p.x - Game.SQUARE_SIZE, p.y))) tmp.add(getPoint(t));
        if (checkDoesntLose(t = new Point(p.x + Game.SQUARE_SIZE, p.y))) tmp.add(getPoint(t));
        if (checkDoesntLose(t = new Point(p.x, p.y - Game.SQUARE_SIZE))) tmp.add(getPoint(t));
        if (checkDoesntLose(t = new Point(p.x, p.y + Game.SQUARE_SIZE))) tmp.add(getPoint(t));

        return tmp;
    }
    private boolean checkDoesntLose(Point p) {
        for (Square s : body) {
            if (s.x == p.x && s.y == p.y) return false;
        }
        return checkInBounds(p);
    }
}
