package com.sneakycrago.undercore;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sneakycrago.undercore.screens.GameOver;
import com.sneakycrago.undercore.screens.GameScreen;
import com.sneakycrago.undercore.screens.InfoScreen;
import com.sneakycrago.undercore.screens.MainMenu;
import com.sneakycrago.undercore.utils.Currency;
import com.sneakycrago.undercore.utils.Score;

public class Application extends Game {

	public static final String TITLE = "Undercore";
	public static final float VERSION = 0.55f;
	public static final int V_WIDTH = 512;
	public static final int V_HEIGHT = 310;

	public SpriteBatch batch;
	public OrthographicCamera camera;
	public ShapeRenderer shapeRenderer;

	public AssetManager assetManager;
	public BitmapFont font, font30,font30white, font10;

	public GameScreen gameScreen;
	public GameOver gameOver;
	public MainMenu mainMenu;
	public InfoScreen infoScreen;

	public Preferences preferences;
	boolean loadPrefs = true; // загружать ли ресурсы

	public static int gameSkin = 1; // 0 - standard
	public static int playerSkin = 0;

	@Override
	public void create () {

		camera = new OrthographicCamera();
		camera.setToOrtho(false, V_WIDTH, V_HEIGHT);
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(camera.combined);

		assetManager = new AssetManager();

		initFonts();

		mainMenu = new MainMenu(this);
		gameScreen = new GameScreen(this);
		gameOver = new GameOver(this);
		infoScreen = new InfoScreen(this);

		getPrefs();

		if(loadPrefs){
			if(preferences.contains("bestScore")){
				System.out.println("checked preferences");
				Score.bestScore = preferences.getInteger("bestScore");
				Currency.currency = preferences.getInteger("currency");
			}
		}
		//setScreen(gameScreen);
		setScreen(mainMenu);
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
	private void initFonts(){
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/font.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

		params.size = 24;
		params.color = Color.WHITE;
		params.minFilter = Texture.TextureFilter.Linear;
		params.magFilter = Texture.TextureFilter.Linear;
		font = generator.generateFont(params);

		FreeTypeFontGenerator.FreeTypeFontParameter params30 = new FreeTypeFontGenerator.FreeTypeFontParameter();
		params30.size = 30;
		params30.color = Color.GRAY;
		params30.minFilter = Texture.TextureFilter.Linear;
		params30.magFilter = Texture.TextureFilter.Linear;
		font30 = generator.generateFont(params30);

		FreeTypeFontGenerator.FreeTypeFontParameter params30white = new FreeTypeFontGenerator.FreeTypeFontParameter();
		params30white.size = 30;
		params30white.color = Color.WHITE;
		params30white.minFilter = Texture.TextureFilter.Linear;
		params30white.magFilter = Texture.TextureFilter.Linear;
		font30white = generator.generateFont(params30white);

		FreeTypeFontGenerator.FreeTypeFontParameter params10 = new FreeTypeFontGenerator.FreeTypeFontParameter();
		params10.size = 10;
		params10.color = Color.GRAY;
		params10.minFilter = Texture.TextureFilter.Linear;
		params10.magFilter = Texture.TextureFilter.Linear;
		font10 = generator.generateFont(params10);
	}
	protected Preferences getPrefs() {
		if(preferences==null){
			preferences = Gdx.app.getPreferences("saves");
		}
		return preferences;
	}
}
