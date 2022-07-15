package com.mygdx.snake;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Square {
    int x;
    int y;
    Color color;

    public Square(int x, int y) {
        this(x, y, Color.WHITE);
    }
    public Square(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }
    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(color);
        shapeRenderer.rect(x, y, Game.SQUARE_SIZE, Game.SQUARE_SIZE); // it's a square
    }
}
