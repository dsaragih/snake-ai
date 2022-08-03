package com.mygdx.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.snake.algos.*;

import java.util.ArrayList;
import java.util.List;

public class Snake {
    public final ArrayList<Square> body = new ArrayList<>();
    public Square head;
    private float dx, dy;
    private List<Point> moveSeq;
    private int curr = 0;
    private PrimHamCycle primHamCycle;

    public Snake(int x, int y) {
        // Initialize the head of the snake
        head = new Square(x, y, Color.GREEN);
        body.add(head);
        dx = Game.SQUARE_SIZE; // moves to the right
    }
    public List<Point> getBodyPoint() {
        List<Point> res = new ArrayList<Point>() {
        };
        for (Square s : body) {
            res.add(new Point(s.x, s.y));
        }
        return res;
    }
    public void update(Food food) {
        if (primHamCycle == null) {
            primHamCycle = new PrimHamCycle(new Point(head.x, head.y));
            callHamCycle(food);
        }

        if (!checkCollideWithFood(food)) {
            body.remove(body.size() - 1);
        }

        if (curr >= moveSeq.size()) curr = 0;
        Point p = moveSeq.get(curr);
        dx = p.x;
        dy = p.y;
        curr++;

        float new_x = head.x + dx;
        float new_y = head.y + dy;
        head = new Square(new_x, new_y, Color.GREEN);
        body.add(0, head);
    }
    public void updatePlayer(Food food) {
        dirCalc();
        if (!checkCollideWithFood(food)) {
            body.remove(body.size() - 1);
        }

        float new_x = head.x + dx;
        float new_y = head.y + dy;
        head = new Square(new_x, new_y, Color.GREEN);

        body.add(0, head);
    }
    private void callHamCycle(Food food) {
        moveSeq = primHamCycle.solve(getBodyPoint(), food);
        curr = 0;
    }

    private void callAStar(Food food) {
        AStar algo = new AStar(getBodyPoint(), food);
        moveSeq = algo.solve();
        curr = 0;
    }
    private boolean checkCollideWithFood(Food food) {
        if (head.overlaps(food)) {
            if (body.size() < (Game.WIDTH * Game.HEIGHT) / (Game.SQUARE_SIZE * Game.SQUARE_SIZE)) food.renew(body);
            callHamCycle(food);
            return true;
        }
        return false;
    }
    public int checkGameEnd() {

        if (head.x == Game.WIDTH || head.x < 0 || head.y == Game.HEIGHT || head.y < 0) {
            return 1;
        }
        for (Square s : body.subList(1, body.size())) {
            if (head.overlaps(s)) {
//                System.out.println("Headx: " + head.x + " Head y: " + head.y);
//                System.out.println("Tail x: " + body.get(body.size() - 1).x + " Tail y: " + body.get(body.size() - 1).y);
//                System.out.println("EAT " + s.x + " " + s.y);
                return 1;
            }
        }
        if (body.size() == (Game.WIDTH * Game.HEIGHT) / (Game.SQUARE_SIZE * Game.SQUARE_SIZE)) {
            return 2;
        }
        return 0;
    }

    private void dirCalc() {
        if ((Gdx.input.isKeyPressed(Input.Keys.A) && dx < 1)) { dy = 0; dx = -Game.SQUARE_SIZE; }
        else if ((Gdx.input.isKeyPressed(Input.Keys.D) && dx > -1)) { dy = 0; dx = Game.SQUARE_SIZE;}
        else if ((Gdx.input.isKeyPressed(Input.Keys.W)) && dy > -1) { dx = 0; dy = Game.SQUARE_SIZE;}
        else if ((Gdx.input.isKeyPressed(Input.Keys.S)) && dy < 1) { dx = 0; dy = -Game.SQUARE_SIZE;}
    }
    public void draw(ShapeRenderer shapeRenderer) {
        body.get(0).draw(shapeRenderer);
        for (Square square: body.subList(1, body.size())) {
            square.draw(shapeRenderer);
        }
    }

}
