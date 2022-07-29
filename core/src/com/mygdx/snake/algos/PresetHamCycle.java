package com.mygdx.snake.algos;

import com.mygdx.snake.Game;
import com.mygdx.snake.Point;

import java.util.ArrayList;

public class PresetHamCycle {
    /*
    We are assuming that a Hamiltonian cycle exists, and so
    either width / square_size or height / square_size is even.
     */
    ArrayList<Point> path;
    Point head;
    public PresetHamCycle(Point head) {
        path = new ArrayList<>();
        this.head = head;

    }
    private boolean isOnEvenRow() {
        return head.y / Game.SQUARE_SIZE % 2 == 0;
    }
    private boolean isOnLastCol() {
        return checkInBounds(new Point(head.x + Game.SQUARE_SIZE, head.y));
    }
    private boolean isOnSecondColInterior() {
        return head.x == Game.SQUARE_SIZE && head.y >= Game.SQUARE_SIZE && head.y < Game.HEIGHT - Game.SQUARE_SIZE;
    }
    private boolean isOnFirstCol() {
        return checkInBounds(new Point(head.x - Game.SQUARE_SIZE, head.y)) && head.y < Game.HEIGHT - Game.SQUARE_SIZE;
    }
    public ArrayList<Point> findHamCycle() {
        Point dir;
        while(path.size() < Game.SIZE) {
            if (isOnEvenRow()) dir = new Point(-Game.SQUARE_SIZE, 0);
            else dir = new Point(Game.SQUARE_SIZE, 0);

            if ((isOnLastCol() && !isOnEvenRow()) || (isOnSecondColInterior() && isOnEvenRow()))
                dir = new Point(0, -Game.SQUARE_SIZE);
            else if (isOnFirstCol()) dir = new Point(0, Game.SQUARE_SIZE);
            path.add(dir);
            head = head.add(dir.multiply(Game.SQUARE_SIZE));
        }
        return path;
    }

    private boolean checkInBounds (Point p) {
        return p.x < 0 || p.x >= Game.WIDTH || p.y < 0 || p.y >= Game.HEIGHT;
    }
}
