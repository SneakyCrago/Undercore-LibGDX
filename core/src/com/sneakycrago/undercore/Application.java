package com.sneakycrago.undercore;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sneakycrago.undercore.screens.GameOver;
import com.sneakycrago.undercore.screens.GameScreen;
import com.sneakycrago.undercore.screens.LoadingScreen;
import com.sneakycrago.undercore.screens.MainMenuScreen;
import com.sneakycrago.undercore.screens.TutorialScreen;
import com.sneakycrago.undercore.utils.Currency;
import com.sneakycrago.undercore.utils.Globals;
import com.sneakycrago.undercore.utils.Score;

public class Application extends Game {

	public static final String TITLE = "Undercore";
	public static final float VERSION = 0.70f;
	public static final int V_WIDTH = 512;
	public static final int V_HEIGHT = 310;

	public SpriteBatch batch;
	public OrthographicCamera camera;
	public ShapeRenderer shapeRenderer;

	public BitmapFont font, font30,font30white, font10, borderFont, smallWhiteFont, font40white,
	death0, death1,death2,death3,death4, menu0Font,tutFont, menu1Font,menu2Font,menu3Font,menu4Font, menuBig;

	public Texture playerSkin0,playerSkin1,playerSkin2,playerSkin3,playerSkin4, currencyTexture;

    public TextureAtlas circleAtlas,bigArrowAtlas, laserAtlas;

    public TextureRegion[] circlesSkin, bigArrowSkin, laserSkin, flipLaserSkin;
    public Texture[] snipersSkin;

	public GameScreen gameScreen;
	public GameOver gameOver;
	public LoadingScreen loadingScreen;
	public MainMenuScreen mainMenuScreen;
	public TutorialScreen tutorialScreen;

	public AssetManager assetManager= new AssetManager();

	public Preferences preferences;
	boolean loadPrefs = true; // загружать ли сохранения

	public static int gameSkin = 4; // 0 - standard

	public static boolean playerAlive;

	public boolean goTutorial;

	@Override
	public void create () {

		camera = new OrthographicCamera();
		camera.setToOrtho(false, V_WIDTH, V_HEIGHT);
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(camera.combined);

		assetManager = new AssetManager();

		loadingScreen = new LoadingScreen(this);

		getPrefs();

		if(loadPrefs){
			if(preferences.contains("bestScore")){
				Score.bestScore = preferences.getInteger("bestScore");
				Currency.currency = preferences.getInteger("currency");
				goTutorial = preferences.getBoolean("goTutorial");
			}
		} else{
			goTutorial = true;
		}
		setScreen(loadingScreen);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		shapeRenderer.dispose();

		font.dispose();
		font30.dispose();
		font30white.dispose();
		font10.dispose();
		borderFont.dispose();
		smallWhiteFont.dispose();
		font40white.dispose();
		death0.dispose();
		death1.dispose();
		death2.dispose();
		death3.dispose();
		death4.dispose();
		menu0Font.dispose();

		assetManager.dispose();

		gameScreen.dispose();
		gameOver.dispose();

        playerSkin0.dispose();
        playerSkin1.dispose();
        playerSkin2.dispose();
        playerSkin3.dispose();
        playerSkin4.dispose();

        circleAtlas.dispose();

	}

	private int borderSize =2;
	private int gameBorderSize = 2;
	public void initFonts(){
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/font.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

		params.size = 24*2;
		params.color = Color.WHITE;
		params.minFilter = Texture.TextureFilter.Linear;
		params.magFilter = Texture.TextureFilter.Linear;
		//MipMapLinearNearest,Nearest
		//params.borderColor = Color.BLACK;
		//params.borderWidth = borderSize;
		font = generator.generateFont(params);

		FreeTypeFontGenerator.FreeTypeFontParameter params30 = new FreeTypeFontGenerator.FreeTypeFontParameter();
		params30.size = 30;
		params30.color = Color.GRAY;
		params30.minFilter = Texture.TextureFilter.Linear;
		params30.magFilter = Texture.TextureFilter.Linear;
		//params30.borderColor = Color.BLACK;
		//params30.borderWidth = borderSize;
		font30 = generator.generateFont(params30);

		FreeTypeFontGenerator.FreeTypeFontParameter params30white = new FreeTypeFontGenerator.FreeTypeFontParameter();
		params30white.size = 30*2;
		params30white.color = Color.WHITE;
		params30white.minFilter = Texture.TextureFilter.Linear;
		params30white.magFilter = Texture.TextureFilter.Linear;
		//params30white.borderColor = Color.BLACK;
		//params30white.borderWidth = borderSize;
		font30white = generator.generateFont(params30white);

		FreeTypeFontGenerator.FreeTypeFontParameter params34white = new FreeTypeFontGenerator.FreeTypeFontParameter();
		params34white.size = 40;
		params34white.color = Color.WHITE;
		params34white.minFilter = Texture.TextureFilter.Linear;
		params34white.magFilter = Texture.TextureFilter.Linear;
		//params34white.borderColor = Color.BLACK;
		//params34white.borderWidth = borderSize;
		font40white = generator.generateFont(params34white);

		FreeTypeFontGenerator.FreeTypeFontParameter params10 = new FreeTypeFontGenerator.FreeTypeFontParameter();
		params10.size = 10*2;
		params10.color = Color.GRAY;
		params10.minFilter = Texture.TextureFilter.Linear;
		params10.magFilter = Texture.TextureFilter.Linear;
		//params10.borderColor = Color.BLACK;
		//params10.borderWidth = borderSize;
		font10 = generator.generateFont(params10);

		FreeTypeFontGenerator.FreeTypeFontParameter smallParams = new FreeTypeFontGenerator.FreeTypeFontParameter();
		smallParams.size = 15*2;
		smallParams.color = Color.WHITE;
		smallParams.minFilter = Texture.TextureFilter.Linear;
		smallParams.magFilter = Texture.TextureFilter.Linear;
		//smallParams.borderColor = Color.BLACK;
		//smallParams.borderWidth = borderSize;
		smallWhiteFont = generator.generateFont(smallParams);

		FreeTypeFontGenerator.FreeTypeFontParameter paramsBorder = new FreeTypeFontGenerator.FreeTypeFontParameter();

		paramsBorder.size = 24*2;
		paramsBorder.color = Color.WHITE;
		paramsBorder.minFilter = Texture.TextureFilter.Linear;
		paramsBorder.magFilter = Texture.TextureFilter.Linear;

		paramsBorder.borderColor = Color.BLACK;
		paramsBorder.borderWidth = gameBorderSize;
		borderFont = generator.generateFont(paramsBorder);

	}
	public void initMenuFonts(){
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/font.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

		params.size = 20;
		params.color = Color.WHITE;
		params.minFilter = Texture.TextureFilter.Linear;
		params.magFilter = Texture.TextureFilter.Linear;
		//MipMapLinearNearest,Nearest
		//params.borderColor = Color.BLACK;
		//params.borderWidth = borderSize;
		menu0Font = generator.generateFont(params);

		params.size = 60;
		menuBig = generator.generateFont(params);

		params.size = 40;
		tutFont = generator.generateFont(params);
	}
	public void initDeathFonts(){
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/font.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter death0params = new FreeTypeFontGenerator.FreeTypeFontParameter();

		death0params.size = 40*2;
		death0params.color = Globals.Text0Color;
		death0params.minFilter = Texture.TextureFilter.Linear;
		death0params.magFilter = Texture.TextureFilter.Linear;
		death0params.borderColor = Color.BLACK;
		death0params.borderWidth = gameBorderSize;
		death0params.genMipMaps = true;
		death0 = generator.generateFont(death0params);

		death0params.color = Globals.Text1Color;
		death1 = generator.generateFont(death0params);


		death0params.color = Globals.Text2Color;
		death2 = generator.generateFont(death0params);

		death0params.color = Globals.Text3Color;
		death3 = generator.generateFont(death0params);

		death0params.color = Globals.Text4Color;
		death4 = generator.generateFont(death0params);

	}
	protected Preferences getPrefs() {
		if(preferences==null){
			preferences = Gdx.app.getPreferences("saves");
			preferences.putBoolean("goTutorial", true);
		}
		return preferences;
	}

	public void loadTextures(){
		playerSkin0.setAssetManager(assetManager);
		playerSkin1.setAssetManager(assetManager);
		playerSkin2.setAssetManager(assetManager);
		playerSkin3.setAssetManager(assetManager);
		playerSkin4.setAssetManager(assetManager);
		currencyTexture.setAssetManager(assetManager);

		TextureLoader.TextureParameter param = new TextureLoader.TextureParameter();
		param.genMipMaps = true; // enabling mipmaps
		param.minFilter = Texture.TextureFilter.MipMapLinearNearest;
		param.magFilter = Texture.TextureFilter.Nearest;
		//Images
		assetManager.load("textures/big_arrow.atlas", TextureAtlas.class);
		assetManager.load("textures/big_arrow.png", Texture.class, param);

		assetManager.load("textures/circle.atlas", TextureAtlas.class);
		assetManager.load("textures/circle.png", Texture.class, param);

		assetManager.load("textures/laser.atlas", TextureAtlas.class);
		assetManager.load("textures/laser.png", Texture.class, param);

		assetManager.load("textures/currency.png", Texture.class, param);
		//Animation
		assetManager.load("textures/animation/playerAnim.png", Texture.class);
		assetManager.load("textures/animation/playerAnim1.png", Texture.class);
		assetManager.load("textures/animation/playerAnim2.png", Texture.class);
		assetManager.load("textures/animation/playerAnim3.png", Texture.class);
		assetManager.load("textures/animation/playerAnim4.png", Texture.class);
		assetManager.load("textures/animation/sniper.png", Texture.class);
		assetManager.load("textures/animation/sniper2.png", Texture.class);
		//Menu Death Screen
		assetManager.load("textures/buttons/gameOver.atlas", TextureAtlas.class);
		// Main Menu
		assetManager.load("textures/buttons/mainMenu.atlas", TextureAtlas.class);

		//Tutorial
		assetManager.load("textures/buttons/GotItButton_big.png", Texture.class, param);
		assetManager.load("textures/buttons/tutorial0.png", Texture.class, param);
		assetManager.load("textures/buttons/tutorial1.png", Texture.class, param);
	}


}
