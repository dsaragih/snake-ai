package com.mygdx.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class Snake {
    public final ArrayList<Square> body = new ArrayList<>();
    private Square head;
    private int dx, dy;

    public Snake(int x, int y) {
        // Initialize the head of the snake
        head = new Square(x, y, Color.GREEN);
        body.add(head);
        body.add(new Square(x - Game.SQUARE_SIZE, y, Color.GREEN));
        dx = 1; // moves to the right
    }
    public void update(Food food, boolean PlayerControl) {
        if (!checkCollideWithFood(food)) {
            body.remove(body.size() - 1);
        }
        if (PlayerControl) dirCalc();
        float new_x = head.x + dx * Game.SQUARE_SIZE;
        float new_y = head.y + dy * Game.SQUARE_SIZE;
        head = new Square(new_x, new_y, Color.GREEN);
        body.add(0, head);
    }
    private boolean checkCollideWithFood(Food food) {
        if (head.overlaps(food)) {
            food.renew(body);
            return true;
        }
        return false;
    }
    public boolean checkGameEnd() {
        if (head.x == Game.WIDTH || head.x < 0 || head.y == Game.HEIGHT || head.y < 0) return true;
        for (Square s : body.subList(1, body.size())) {
            if (head.overlaps(s)) return true;
        }
        return false;
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
