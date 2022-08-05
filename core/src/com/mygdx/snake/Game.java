package com.mygdx.snake;

import com.badlogic.gdx.ApplicationAdapter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import static java.lang.Thread.sleep;

public class Game extends ApplicationAdapter {
	ShapeRenderer shapeRenderer;
	OrthographicCamera camera;
	Stage stage;
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
		stage = new Stage(new ScreenViewport());
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
		snake = new Snake(SQUARE_SIZE,  SQUARE_SIZE);
		food = new Food(snake.body);
	}

	public void update() {
		int delay;
		if (Gdx.input.isKeyJustPressed(Input.Keys.M)) PlayerControl = !PlayerControl;

		if (PlayerControl) {
			snake.updatePlayer(food);
			delay = 100;
		}
		else {
			snake.update(food);
			delay = 8;
		}

		gameEnd = snake.checkGameEnd();
		stagger(delay);
	}

	private void drawStart(Label.LabelStyle style) {
		Label label = new Label("Press M to control snake", style);
		label.setPosition(0, Gdx.graphics.getHeight() / 2f);
		label.setSize(Gdx.graphics.getWidth(), 20);
		label.setAlignment(Align.center);
		label.setFontScale(1.5f);
		stage.addActor(label);
	}
	private void drawEnd(Label.LabelStyle style) {
		Label label = new Label("Press SPACE to restart", style);
		label.setPosition(0, Gdx.graphics.getHeight() / 2f);
		label.setSize(Gdx.graphics.getWidth(), 20);
		label.setAlignment(Align.center);
		label.setFontScale(1.5f);
		stage.addActor(label);
	}
	private void drawWin(Label.LabelStyle style) {
		Label label = new Label("You've WON! Press SPACE to restart", style);
		label.setPosition(0, Gdx.graphics.getHeight() / 2f);
		label.setSize(Gdx.graphics.getWidth(), 20);
		label.setAlignment(Align.center);
		label.setFontScale(1.5f);
		stage.addActor(label);
	}
	private void stagger(int delay) {
		try {
			sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		camera.update();

		Label.LabelStyle labelStyle = new Label.LabelStyle();
		labelStyle.font = new BitmapFont();
		labelStyle.fontColor = Color.BLUE;

		if (gameEnd == 0) {
			update();
			drawStart(labelStyle);
		} else if (gameEnd == 1) {
			drawEnd(labelStyle);
			if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
				gameEnd = 0;
				PlayerControl = false;
				create();
			}
		} else {
			drawWin(labelStyle);
			food.updateWin();
			if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
				gameEnd = 0;
				PlayerControl = false;
				create();
			}
		}
		stage.act();
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Filled);
		snake.draw(shapeRenderer);
		food.draw(shapeRenderer);
		shapeRenderer.end();
		stage.draw();
		stage.clear();
	}
	
	@Override
	public void dispose () {
		shapeRenderer.dispose();
		stage.dispose();
	}
}
