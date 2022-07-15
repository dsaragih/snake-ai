package com.mygdx.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class Snake {
    private final ArrayList<Square> body = new ArrayList<>();
    Square head;
    private int dx, dy;

    public Snake(int x, int y) {
        // Initialize the head of the snake
        head = new Square(x, y, Color.GREEN);
        body.add(head);
        body.add(new Square(x - Game.SQUARE_SIZE, y, Color.GREEN));
        dx = 1; // moves to the right
    }
    public void update() {
        body.remove(body.size() - 1);
        body.add(new Square(head.x, head.y, Color.GREEN));
        dirCalc();
        head.x += dx * Game.SQUARE_SIZE;
        head.y += dy * Game.SQUARE_SIZE;
    }
    private void dirCalc() {
        if ((Gdx.input.isKeyPressed(Input.Keys.A) && dx != 1)) { dy = 0; dx = -1; }
        else if ((Gdx.input.isKeyPressed(Input.Keys.D) && dx != -1)) { dy = 0; dx = 1;}
        else if ((Gdx.input.isKeyPressed(Input.Keys.W)) && dy != -1) { dx = 0; dy = 1;}
        else if ((Gdx.input.isKeyPressed(Input.Keys.S)) && dy != 1) { dx = 0; dy = -1;}
    }
    public void draw(ShapeRenderer shapeRenderer) {
        for (Square square: body) {
            square.draw(shapeRenderer);
        }
    }

}
