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
	public final static int WIDTH = 400;
	public final static int HEIGHT = 400;
	public final static int SQUARE_SIZE = 20;
	public final static int SIZE = (WIDTH * HEIGHT) / (SQUARE_SIZE * SQUARE_SIZE);
	Snake snake;
	Food food;
	int gameEnd = 0;
	boolean PlayerControl = false;
	
	@Override
	public void create () {
		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
		snake = new Snake(SQUARE_SIZE,  SQUARE_SIZE);
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
		font.draw(batch, "Press M to auto-play", WIDTH/2f, HEIGHT/2f, 0, WIDTH, false);
		batch.end();
	}
	private void drawEnd() {
		batch.begin();
		BitmapFont font = new BitmapFont();
		font.draw(batch, "Press SPACE to restart", WIDTH/2f, HEIGHT/2f, 0, WIDTH, false);
		batch.end();
	}
	private void drawWin() {
		batch.begin();
		BitmapFont font = new BitmapFont();
		font.draw(batch, "Winner! Press SPACE to restart!!", WIDTH/2f, HEIGHT/2f, 0, WIDTH, false);
		batch.end();
	}
	private void stagger() {
		try {
			sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		camera.update();
		if (gameEnd == 0) {
			update();
			drawStart();
		} else if (gameEnd == 1) {
			drawEnd();
			if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
				gameEnd = 0;
				create();
			}
		} else {
			drawWin();
			food.updateWin();
			if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
				gameEnd = 0;
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
