package com.sneakycrago.undercore;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sneakycrago.undercore.screens.GameOver;
import com.sneakycrago.undercore.screens.GameScreen;

public class Application extends Game {

	public static final String TITLE = "Undercore";
	public static final float VERSION = 0.1f;
	public static final int V_WIDTH = 512;
	public static final int V_HEIGHT = 310;

	public SpriteBatch batch;
	public OrthographicCamera camera;
	public ShapeRenderer shapeRenderer;

	public AssetManager assetManager;
	public BitmapFont font;

	public GameScreen gameScreen;
	public GameOver gameOver;

	public int score;

	//public Color gameOrange = new Color(255/255f,162/255f, 38/255f, 1f);
	//public Color gameBlue = new Color(22/255f,238/255f,247/255f,1f);

	@Override
	public void create () {

		camera = new OrthographicCamera();
		camera.setToOrtho(false, V_WIDTH, V_HEIGHT);
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(camera.combined);

		assetManager = new AssetManager();
		font = new BitmapFont();

		gameScreen = new GameScreen(this);
		gameOver = new GameOver(this);

		setScreen(gameScreen);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		shapeRenderer.dispose();

		assetManager.dispose();
		font.dispose();

		gameScreen.dispose();
		gameOver.dispose();
	}

	//узнать счет
	public int getScore() {
		return score;
	}
	// установить счет(для рестарта)
	public void setScore(int score) {
		this.score = score;
	}

	// прибавить к счету заданное число
	public void addScore(int increment) {
		score += increment;
	}
}
