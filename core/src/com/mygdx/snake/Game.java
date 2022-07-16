package com.mygdx.snake;

import com.badlogic.gdx.ApplicationAdapter;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;

import static java.lang.Thread.sleep;

public class Game extends ApplicationAdapter {
	ShapeRenderer shapeRenderer;
	OrthographicCamera camera;
	final static int WIDTH = 800;
	final static int HEIGHT = 600;
	final static int SQUARE_SIZE = 20;
	Snake snake;
	Food food;
	boolean gameEnd = false;
	
	@Override
	public void create () {
		shapeRenderer = new ShapeRenderer();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
		snake = new Snake(10 * SQUARE_SIZE, 8 * SQUARE_SIZE);
		food = new Food(snake.body);
	}

	public void update() {
		snake.update(food);
		gameEnd = snake.checkGameEnd();
		stagger();
	}
	private void stagger() {
		try {
			sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		camera.update();
		if (!gameEnd) {
			update();
		}
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Filled);
		snake.draw(shapeRenderer);
		food.draw(shapeRenderer);
		shapeRenderer.end();

	}
	
	@Override
	public void dispose () {
		shapeRenderer.dispose();
	}
}
