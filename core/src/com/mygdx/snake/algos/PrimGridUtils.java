package com.mygdx.snake.algos;

import com.mygdx.snake.Game;
import com.mygdx.snake.Point;
import com.mygdx.snake.Square;

import java.util.ArrayList;

public class PrimGridUtils {
    public ArrayList<ArrayList<Node>> primGrid = new ArrayList<>();
    private GridUtils grid = new GridUtils();

    public PrimGridUtils() {
        for (int i = Game.SQUARE_SIZE; i < Game.HEIGHT; i += 2 * Game.SQUARE_SIZE) {
            ArrayList<Node> tmp = new ArrayList<>();
            for (int j = Game.SQUARE_SIZE; j < Game.WIDTH; j += 2 * Game.SQUARE_SIZE) {
                tmp.add(new Node(j, i));
            }
            primGrid.add(tmp);
        }
    }
    public Node getCorrNode(Point p) {
        return getNode(new Node(p.x + Game.SQUARE_SIZE, p.y + Game.SQUARE_SIZE));
    }

    public ArrayList<Point> getNodeSquares(Point p) {
        ArrayList<Point> tmp = new ArrayList<>();
        tmp.add(p);
        Point t;
        if (checkInBounds(t = new Point(p.x - Game.SQUARE_SIZE, p.y))) tmp.add(grid.getPoint(t));
        if (checkInBounds(t = new Point(p.x, p.y - Game.SQUARE_SIZE))) tmp.add(grid.getPoint(t));
        if (checkInBounds(t = new Point(p.x - Game.SIZE, p.y - Game.SQUARE_SIZE))) tmp.add(grid.getPoint(t));

        return tmp;
    }
    private Node getNode(Node n) {
        int g_y = (int) ((n.y - Game.SQUARE_SIZE) / (2 * Game.SQUARE_SIZE));
        int g_x = (int) ((n.x - Game.SQUARE_SIZE) / (2 * Game.SQUARE_SIZE));
        return primGrid.get(g_y).get(g_x);
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
        if (p.x < 0 || p.x >= Game.WIDTH || p.y < 0 || p.y >= Game.HEIGHT) return false;
        return true;
    }

}
