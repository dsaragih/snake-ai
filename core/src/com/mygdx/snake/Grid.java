package com.mygdx.snake;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class Grid {
    public ArrayList<ArrayList<Point>> Matrix = new ArrayList<>();
    private ArrayList<Square> body;

    public Grid(ArrayList<Square> body) {
        this.body = body;
        for (int i = 0; i < Game.HEIGHT; i += Game.SQUARE_SIZE) {
            ArrayList<Point> tmp = new ArrayList<>();
            for (int j = 0; j < Game.WIDTH; j += Game.SQUARE_SIZE) {
                tmp.add(new Point(j, i));
            }
            Matrix.add(tmp);
        }
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
        if (checkNeighbor(t = new Point(p.x - Game.SQUARE_SIZE, p.y))) tmp.add(Game.grid.getPoint(t));
        if (checkNeighbor(t = new Point(p.x + Game.SQUARE_SIZE, p.y))) tmp.add(Game.grid.getPoint(t));
        if (checkNeighbor(t = new Point(p.x, p.y - Game.SQUARE_SIZE))) tmp.add(Game.grid.getPoint(t));
        if (checkNeighbor(t = new Point(p.x, p.y + Game.SQUARE_SIZE))) tmp.add(Game.grid.getPoint(t));

        return tmp;
    }
    private boolean checkNeighbor(Point p) {
        if (p.x < 0 || p.x >= Game.WIDTH || p.y < 0 || p.y >= Game.HEIGHT) return false;
//        for (Square s : body) {
//            if (s.x == p.x && s.y == p.y) return false;
//        }
        return true;
    }
}
