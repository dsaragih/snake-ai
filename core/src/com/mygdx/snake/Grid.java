package com.mygdx.snake;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class Grid {
    public ArrayList<ArrayList<Point>> Matrix = new ArrayList<>();

    public Grid() {
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
}
