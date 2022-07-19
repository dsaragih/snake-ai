package com.mygdx.snake;

import com.badlogic.gdx.graphics.Color;
import java.util.List;

public class Food extends Square{

    public Food(List<Square> snake) {
        super(0, 0, Color.RED);
        renew(snake);
    }
    public void renew(List<Square> snake) {
        Point pos = findNewPos(snake);
        x = pos.x; y = pos.y;
    }
    public Point findNewPos(List<Square> snake) {
        int x_grid = Game.WIDTH / Game.SQUARE_SIZE;
        int y_grid = Game.HEIGHT / Game.SQUARE_SIZE;
        int x = ((int) (Math.random() * x_grid)) * Game.SQUARE_SIZE;
        int y = ((int) (Math.random() * y_grid)) * Game.SQUARE_SIZE;

        for (Square s : snake) {
            if (s.x == x && s.y == y) {
                return this.findNewPos(snake);
            }
        }
        return new Point(x, y);
        //return new Point(x, Game.HEIGHT - y);
    }

}
