package com.mygdx.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.snake.algos.AStar;
import com.mygdx.snake.algos.PresetHamCycle;
import com.mygdx.snake.algos.PrimHamCycle;
import com.mygdx.snake.algos.RecursiveHamCycle;

import java.util.ArrayList;
import java.util.List;

public class Snake {
    public final ArrayList<Square> body = new ArrayList<>();
    public Square head;
    private float dx, dy;
    private List<Point> moveSeq;
    private int curr = 0;

    public Snake(int x, int y) {
        // Initialize the head of the snake
        head = new Square(x, y, Color.GREEN);
        body.add(head);
        dx = 1; // moves to the right
    }
    public void update(Food food, boolean PlayerControl) {

        if (PlayerControl) dirCalc();
        else {
            if (moveSeq == null) {
                //PresetHamCycle algo = new PresetHamCycle(new Point(head.x, head.y));
                callHamCycle();
            }
        }
        if (!checkCollideWithFood(food)) {
            body.remove(body.size() - 1);
        }
        if (curr >= moveSeq.size()) callHamCycle();
        Point p = moveSeq.get(curr);
        dx = p.x;
        dy = p.y;
        curr++;
        float new_x = head.x + dx;
        float new_y = head.y + dy;
        head = new Square(new_x, new_y, Color.GREEN);
        body.add(0, head);
    }
    private void callHamCycle() {
        PrimHamCycle algo = new PrimHamCycle(new Point(head.x, head.y));
        moveSeq = algo.solve();
        curr = 0;
    }

    private void callAStar(Food food) {
        AStar algo = new AStar(this, food.x, food.y);
        moveSeq = algo.solve();
        curr = 0;
    }
    private boolean checkCollideWithFood(Food food) {
        if (head.overlaps(food)) {
            if (body.size() < (Game.WIDTH * Game.HEIGHT) / (Game.SQUARE_SIZE * Game.SQUARE_SIZE)) food.renew(body);
            callAStar(food);
            return true;
        }
        return false;
    }
    public int checkGameEnd() {
        if (head.x == Game.WIDTH || head.x < 0 || head.y == Game.HEIGHT || head.y < 0) return 1;
        //System.out.println("Body size: " + body.size());
        for (Square s : body.subList(1, body.size())) {
            if (head.overlaps(s)) return 1;
        }
        if (body.size() == (Game.WIDTH * Game.HEIGHT) / (Game.SQUARE_SIZE * Game.SQUARE_SIZE)) {
            return 2;
        }
        return 0;
    }

    private void dirCalc() {
        if ((Gdx.input.isKeyPressed(Input.Keys.A) && dx != 1)) { dy = 0; dx = -1; }
        else if ((Gdx.input.isKeyPressed(Input.Keys.D) && dx != -1)) { dy = 0; dx = 1;}
        else if ((Gdx.input.isKeyPressed(Input.Keys.W)) && dy != -1) { dx = 0; dy = 1;}
        else if ((Gdx.input.isKeyPressed(Input.Keys.S)) && dy != 1) { dx = 0; dy = -1;}
        dx *= Game.SQUARE_SIZE;
        dy *= Game.SQUARE_SIZE;
    }
    public void draw(ShapeRenderer shapeRenderer) {
        for (Square square: body) {
            square.draw(shapeRenderer);
        }
    }

}
