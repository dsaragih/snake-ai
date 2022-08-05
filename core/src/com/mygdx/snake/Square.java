package com.mygdx.snake;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Square extends Rectangle {
    Color color;

    public Square(float x, float y, Color color) {
        super(x, y, Game.SQUARE_SIZE - 1, Game.SQUARE_SIZE - 1);
        this.color = color;
    }
    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(color);
        shapeRenderer.rect(x, y, width, height); // it's a square
    }
}
