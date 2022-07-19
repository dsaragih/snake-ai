package com.mygdx.snake;

import com.badlogic.gdx.ApplicationAdapter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;

import static java.lang.Thread.sleep;

public class Game extends ApplicationAdapter {
	ShapeRenderer shapeRenderer;
	OrthographicCamera camera;
	SpriteBatch batch;
	public final static int WIDTH = 800;
	public final static int HEIGHT = 600;
	public final static int SQUARE_SIZE = 20;
	Snake snake;
	Food food;
	boolean gameEnd = false;
	boolean PlayerControl = false;
	
	@Override
	public void create () {
		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
		snake = new Snake(10 * SQUARE_SIZE, 8 * SQUARE_SIZE);
		food = new Food(snake.body);
	}

	public void update() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.M)) PlayerControl = !PlayerControl;
		snake.update(food, PlayerControl);
		gameEnd = snake.checkGameEnd();
		stagger();
	}
	private void drawStart() {
		batch.begin();
		BitmapFont font = new BitmapFont();
		font.draw(batch, "Press M to auto-play", WIDTH/2, HEIGHT/2, 0, WIDTH, false);
		batch.end();
	}
	private void drawEnd() {
		batch.begin();
		BitmapFont font = new BitmapFont();
		font.draw(batch, "Press SPACE to restart", WIDTH/2, HEIGHT/2, 0, WIDTH, false);
		batch.end();
	}
	private void stagger() {
		try {
			sleep(1);
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
			drawStart();
		} else {
			drawEnd();
			if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
				gameEnd = false;
				create();
			}
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
		batch.dispose();
	}
}
