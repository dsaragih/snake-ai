package com.mygdx.snake.algorithms.utilities;

import com.mygdx.snake.Game;
import com.mygdx.snake.Point;

import java.util.ArrayList;

public class PrimGridUtils {
    public ArrayList<ArrayList<Node>> Matrix = new ArrayList<>();
    private final GridUtils grid;

    public PrimGridUtils(GridUtils grid) {
        for (int i = Game.SQUARE_SIZE; i < Game.HEIGHT; i += 2 * Game.SQUARE_SIZE) {
            ArrayList<Node> tmp = new ArrayList<>();
            for (int j = Game.SQUARE_SIZE; j < Game.WIDTH; j += 2 * Game.SQUARE_SIZE) {
                tmp.add(new Node(j, i));
            }
            Matrix.add(tmp);
        }
        this.grid = grid;
    }
    public int getSize() {
        return Matrix.size() * Matrix.get(0).size();
    }
    public Node getCorrNode(Point p) {
        return getNode(new Node(p.x + Game.SQUARE_SIZE, p.y + Game.SQUARE_SIZE));
    }

    public Point getPoint(Point p) { return grid.getPoint(p);}

    public ArrayList<Point> getNodeSquares(Point p) {
        ArrayList<Point> tmp = new ArrayList<>();
        tmp.add(getPoint(p));
        Point t;
        if (checkInBounds(t = new Point(p.x - Game.SQUARE_SIZE, p.y))) tmp.add(getPoint(t));
        if (checkInBounds(t = new Point(p.x, p.y - Game.SQUARE_SIZE))) tmp.add(getPoint(t));
        if (checkInBounds(t = new Point(p.x - Game.SQUARE_SIZE, p.y - Game.SQUARE_SIZE))) tmp.add(getPoint(t));

        return tmp;
    }
    private Node getNode(Node n) {
        int g_y = (int) ((n.y - Game.SQUARE_SIZE) / (2 * Game.SQUARE_SIZE));
        int g_x = (int) ((n.x - Game.SQUARE_SIZE) / (2 * Game.SQUARE_SIZE));
        return Matrix.get(g_y).get(g_x);
    }
    public ArrayList<Node> getNeighbors(Point p) {
        ArrayList<Node> tmp = new ArrayList<>();
        Node t;
        if (checkInBounds(t = new Node(p.x - 2 * Game.SQUARE_SIZE, p.y))) tmp.add(getNode(t));
        if (checkInBounds(t = new Node(p.x + 2 * Game.SQUARE_SIZE, p.y))) tmp.add(getNode(t));
        if (checkInBounds(t = new Node(p.x, p.y - 2 * Game.SQUARE_SIZE))) tmp.add(getNode(t));
        if (checkInBounds(t = new Node(p.x, p.y + 2 * Game.SQUARE_SIZE))) tmp.add(getNode(t));

        return tmp;
    }
    private boolean checkInBounds(Point p) {
        return !(p.x < 0) && !(p.x >= Game.WIDTH) && !(p.y < 0) && !(p.y >= Game.HEIGHT);
    }

}
