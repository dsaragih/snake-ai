package com.mygdx.snake;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class Grid {
    public ArrayList<Square> Matrix = new ArrayList<>();
    ShapeRenderer shapeRenderer;

    public Grid(ShapeRenderer shapeRenderer) {
        this.shapeRenderer = shapeRenderer;
        for (int i = 0; i <= (Game.HEIGHT - Game.SQUARE_SIZE); i += Game.SQUARE_SIZE) {
            for (int j = 0; j < Game.WIDTH - Game.SQUARE_SIZE; j += Game.SQUARE_SIZE) {

                Matrix.add(new Square(j, i));
            }
        }
    }
    public void draw() {
        for (Square sq : Matrix) {
            (new Square(0, 100)).draw(shapeRenderer);
            sq.draw(shapeRenderer);
        }
    }
    public void update(Snake snake) {

    }
}
