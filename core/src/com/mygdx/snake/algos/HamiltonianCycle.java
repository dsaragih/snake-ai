package com.mygdx.snake.algos;
import com.mygdx.snake.Game;
import com.mygdx.snake.Point;

public class HamiltonianCycle {
    Point head;
    public HamiltonianCycle(Point head) {
        // Try a backtracking algorithm

        this.head = head;
    }
    public Point solve(float dx, float dy) {
        if (inInterior(dx, dy)) {
            return new Point(dx, dy);
        }
        else {
            if (dx != 0) {
                int y_sq = (int) (head.y / Game.SQUARE_SIZE);
                if (y_sq % 2 == 0) return new Point(0, 1);
                else return new Point(0, -1);
            }
            else if (dy != 0) {
                if (checkNotOnBounds(new Point(head.x + Game.SQUARE_SIZE, head.y))) return new Point(1, 0);
                else return new Point(-1, 0);
            }
        }
        return new Point(0, 1);
    }
    private boolean inInterior (float dx, float dy) {
        return checkNotOnBounds(new Point(head.x + dx * Game.SQUARE_SIZE, head.y + dy * Game.SQUARE_SIZE));
    }
    private boolean checkNotOnBounds (Point p) {
        return !(p.x < 0) && !(p.x >= Game.WIDTH) && !(p.y < 0) && !(p.y >= Game.HEIGHT);
    }
}
