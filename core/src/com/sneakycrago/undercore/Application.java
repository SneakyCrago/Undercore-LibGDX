package com.sneakycrago.undercore;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.IntArray;
import com.sneakycrago.undercore.screens.GameOver;
import com.sneakycrago.undercore.screens.GameScreen;
import com.sneakycrago.undercore.screens.LoadingScreen;
import com.sneakycrago.undercore.screens.MainMenuScreen;
import com.sneakycrago.undercore.screens.TutorialScreen;
import com.sneakycrago.undercore.utils.AdsController;
import com.sneakycrago.undercore.utils.Currency;
import com.sneakycrago.undercore.utils.Globals;
import com.sneakycrago.undercore.utils.GpgsController;
import com.sneakycrago.undercore.utils.Score;

import java.util.Random;

public class Application extends Game {

	public static final String TITLE = "Undercore";
	public static final float VERSION = 0.70f;
	public static final int V_WIDTH = 512;
	public static final int V_HEIGHT = 310;

	public SpriteBatch batch;
	public OrthographicCamera camera;
	public ShapeRenderer shapeRenderer;

	public BitmapFont font, font30,font30white, font10, borderFont, smallWhiteFont, font40white,
	death0, death1,death2,death3,death4, menu0Font,tutFont, menu1Font,menu2Font,menu3Font,menu4Font, menuBig,
			menuScoreFont, bigNumbers, fontNope, startEN;

	public BitmapFont menu0FontRu, menuBigRu, tutFontRu, tutFont2RU, settingsFontRU,
			death0RU,death1RU,death2RU,death3RU,death4RU, fontRU, smallWhiteFontRU, borderFontRU, fontNopeRu,
    startRU, start40RU;

	public Texture playerSkin0,playerSkin1,playerSkin2,playerSkin3,playerSkin4, skinPrewRandomTex;
	public TextureRegion currencyTexture;

    public TextureAtlas fullA1, fullA2, fullA3;

    public TextureRegion[] circlesSkin, bigArrowSkin, laserSkin, flipLaserSkin, skinPrewTex;
	public TextureRegion[] mplayTex,mplayPressedTex, mworldTex,mworldPressedTex,
			mplayerTex,mplayerPressedTex, msettingsTex, msettingsPressedTex,
			arrowLeftTex, arrowRightTex, arrowLeftPressedTex, arrowRightPressedTex,
			playRoundTex, playRoundPressedTex;
    public Texture[] snipersSkin;

	public GameScreen gameScreen;
	public GameOver gameOver;
	public LoadingScreen loadingScreen;
	public MainMenuScreen mainMenuScreen;
	public TutorialScreen tutorialScreen;

	public Sound jumpSound, deathAllSound;
	public Music ambientSound;

	public AssetManager assetManager= new AssetManager();

	public boolean activePlay = true;

	public static Preferences preferences;
	boolean loadPrefs = true; // загружать ли сохранения
	public static int gameSkin = 0; // 0 - standard
	public static boolean playerAlive;

	public static int skinsAmount = 5;

	public static int price = 200;
	public static int startPrice = 200;
	public static int skinsBought = 0;
	public static boolean[] skinLocked = new boolean[skinsAmount];
	public static boolean goTutorial = true;
	public static boolean ru = false, en = true;
	public static float volume = 1f;

	public String FONT_CHARS = "";

	public AdsController adsController;
    public GpgsController gpgsController;

	public IntArray openedSkins;

	public float time = 0;
	public static boolean showInterstitialAd = false;

	public static boolean android = false;

	public static boolean reborn = false; // Awarded Boolean

    public String leaderboard_Highscore = "CgkI28yY58YGEAIQAA";

	public String achievement_novice_runner = "CgkI28yY58YGEAIQAQ"; // Score - 14
	public String achievement_solid_runner ="CgkI28yY58YGEAIQCA"; // Score - 50
	public String achievement_like_a_forest ="CgkI28yY58YGEAIQBw"; // Score - 88
	public String achievement_dead_again ="CgkI28yY58YGEAIQAw"; // Die - 5
	public String achievement_scrooge_mcduck ="CgkI28yY58YGEAIQBA"; // Collect - 10000
	public String achievement_big_brooother ="CgkI28yY58YGEAIQBQ"; // Open allSkins
	public String achievement_hell_1 = "CgkI28yY58YGEAIQBg"; // Unlock new Challenge
	public String achievement_what_is_that_shiny_thing = "CgkI28yY58YGEAIQDA";

	public Application(AdsController adsController, GpgsController gpgsController){
		this.adsController = adsController;
        this.gpgsController = gpgsController;
		android = true;
	}


    public int deathAmount;

	private Random random = new Random();

	public boolean deathSmallArow = false, deathSnipers = false;

	public static boolean loadedMoney, loadedReborn;

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

		skinLocked[0] = false;
		for(int i=1; i < skinLocked.length; i++){
			skinLocked[i] = true;
		}

		for(int i = 32; i < 127; i++) FONT_CHARS += (char)i;
		for(int i = 1024; i < 1104; i++) FONT_CHARS += (char)i;

		if(loadPrefs){
			if(preferences.contains("bestScore")) Score.bestScore = preferences.getInteger("bestScore");
			if(preferences.contains("currency")) Currency.currency = preferences.getInteger("currency");

			if(preferences.contains("goTutorial"))goTutorial = preferences.getBoolean("goTutorial");
			if(preferences.contains("ru")) ru = preferences.getBoolean("ru");
			if(preferences.contains("en")) en = preferences.getBoolean("en");

			// volume, price, skinLocked
			if(preferences.contains("volume")) volume = preferences.getFloat("volume");

			if(preferences.contains("price")) price = preferences.getInteger("price", price);

			if(preferences.contains("skinLocked1")) skinLocked[1] = preferences.getBoolean("skinLocked1");
			if(preferences.contains("skinLocked2")) skinLocked[2] = preferences.getBoolean("skinLocked2");
			if(preferences.contains("skinLocked3")) skinLocked[3] = preferences.getBoolean("skinLocked3");
			if(preferences.contains("skinLocked4")) skinLocked[4] = preferences.getBoolean("skinLocked4");

			if(preferences.contains("skinsBought")) skinsBought = preferences.getInteger("skinsBought");

			if(preferences.contains("gameSkin")) gameSkin = preferences.getInteger("gameSkin");

            if(preferences.contains("deathAmount")) deathAmount = preferences.getInteger("deathAmount");

			if(preferences.contains("maxMoney")) Currency.maxMoney = preferences.getInteger("maxMoney");
		} else{ //RESET SAVES TO ZERO
			Score.bestScore = 0;
			Currency.currency = 0;
			goTutorial = true;
			en = true;
			ru = false;

			preferences.putInteger("bestScore",Score.bestScore);
			preferences.flush();
			preferences.putInteger("currency", Currency.currency);
			preferences.flush();
			preferences.putBoolean("goTutorial", goTutorial);
			preferences.flush();
			preferences.putBoolean("en",en);
			preferences.flush();
			preferences.putBoolean("ru", ru);
			preferences.flush();

            deathAmount = 0;

            preferences.putInteger("deathAmount", deathAmount);
            preferences.flush();

			Currency.maxMoney = 0;

			preferences.putInteger("maxMoney", Currency.maxMoney);
			preferences.flush();
		}
		adTimer();

		openedSkins = new IntArray();

		for(int i=0; i < skinLocked.length; i++) {
			if(!skinLocked[i]) {
				openedSkins.add(i);
			}
		}


		setScreen(loadingScreen);
	}
	public float AD_TIME = 300;
	public void adTimer(){
		time +=Gdx.graphics.getDeltaTime();
		if(time >= AD_TIME) {
			showInterstitialAd = true;
			time = 0;
			//System.out.println(showInterstitialAd);
		}
		//System.out.println(time);
	}

	public void countDeathAmount(){
        deathAmount +=1;

        preferences.putInteger("deathAmount", deathAmount);
        preferences.flush();
    }

	public int randomizeSkins(){
		return random.nextInt(openedSkins.size);
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

		fullA1.dispose();
		fullA2.dispose();
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

		params.size = 24;
		fontNope = generator.generateFont(params);

		params.size = 40;
		tutFont = generator.generateFont(params);

		params.size = 100;
		bigNumbers = generator.generateFont(params);

		params.size = 24*2;


		params.size = 15*2;
		params.borderColor = Color.BLACK;
		params.borderWidth = gameBorderSize;
		menuScoreFont = generator.generateFont(params);

        params.size =  40;
        params.borderColor = Color.BLACK;
        params.borderWidth = gameBorderSize;
        startEN = generator.generateFont(params);

	}
	public void initRuFonts(){
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/font.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter paramsRu = new FreeTypeFontGenerator.FreeTypeFontParameter();

		paramsRu.size = 20;
		paramsRu.color = Color.WHITE;
		paramsRu.minFilter = Texture.TextureFilter.Linear;
		paramsRu.magFilter = Texture.TextureFilter.Linear;
		paramsRu.characters = FONT_CHARS;

		menu0FontRu = generator.generateFont(paramsRu);

		paramsRu.size = 16;
		settingsFontRU = generator.generateFont(paramsRu);

		paramsRu.size = 60;
		menuBigRu = generator.generateFont(paramsRu);

		paramsRu.size = 40;
		tutFontRu = generator.generateFont(paramsRu);

		paramsRu.size = 34;
		tutFont2RU = generator.generateFont(paramsRu);

		paramsRu.size = 24*2;
		fontRU = generator.generateFont(paramsRu);

		paramsRu.size = 15*2;
		smallWhiteFontRU = generator.generateFont(paramsRu);

		paramsRu.size = 24;
		fontNopeRu = generator.generateFont(paramsRu);

		paramsRu.size = 40*2;
		paramsRu.color = Globals.Text0Color;
		paramsRu.borderColor = Color.BLACK;
		paramsRu.borderWidth = gameBorderSize;
		death0RU = generator.generateFont(paramsRu);


		paramsRu.color = Globals.Text1Color;
		death1RU = generator.generateFont(paramsRu);

		paramsRu.color = Globals.Text2Color;
		death2RU = generator.generateFont(paramsRu);

		paramsRu.color = Globals.Text3Color;
		death3RU = generator.generateFont(paramsRu);

		paramsRu.color = Globals.Text4Color;
		death4RU = generator.generateFont(paramsRu);

		paramsRu.size = 18*2;
		paramsRu.color = Color.WHITE;
		paramsRu.borderColor = Color.BLACK;
		paramsRu.borderWidth = gameBorderSize;
		borderFontRU = generator.generateFont(paramsRu);

        paramsRu.size =  30;
        paramsRu.borderColor = Color.BLACK;
        paramsRu.borderWidth = gameBorderSize;
        startRU = generator.generateFont(paramsRu);

		paramsRu.size =  40;
		paramsRu.borderColor = Color.BLACK;
		paramsRu.borderWidth = gameBorderSize;
		start40RU = generator.generateFont(paramsRu);


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
		}
		return preferences;
	}

	public void loadTextures(){
		playerSkin0.setAssetManager(assetManager);
		playerSkin1.setAssetManager(assetManager);
		playerSkin2.setAssetManager(assetManager);
		playerSkin3.setAssetManager(assetManager);
		playerSkin4.setAssetManager(assetManager);

		TextureLoader.TextureParameter param = new TextureLoader.TextureParameter();
		param.genMipMaps = true; // enabling mipmaps
		param.minFilter = Texture.TextureFilter.MipMapLinearNearest;
		param.magFilter = Texture.TextureFilter.Nearest;

		//Atlas
		assetManager.load("textures/FullA1.atlas", TextureAtlas.class);
		assetManager.load("textures/FullA2.atlas", TextureAtlas.class);
		assetManager.load("textures/FullA3.atlas", TextureAtlas.class);
		//Tutorial
		assetManager.load("textures/GotItButton_big.png", Texture.class);
		assetManager.load("textures/tutorial0.png", Texture.class);
		assetManager.load("textures/tutorial1.png", Texture.class);
		//Animation
		assetManager.load("textures/animation/playerAnim.png", Texture.class);
		assetManager.load("textures/animation/playerAnim1.png", Texture.class);
		assetManager.load("textures/animation/playerAnim2.png", Texture.class);
		assetManager.load("textures/animation/playerAnim3.png", Texture.class);
		assetManager.load("textures/animation/playerAnim4.png", Texture.class);
		assetManager.load("textures/animation/sniper.png", Texture.class);
		assetManager.load("textures/animation/sniper2.png", Texture.class);

		assetManager.load("textures/skinPrewRandom.png", Texture.class);

		//Test
	}

	public void loadSounds(){
		assetManager.load("sounds/jump.wav", Sound.class);
		//assetManager.load("sounds/ambient_game.mp3", Music.class);
		assetManager.load("sounds/background.mp3", Music.class);
		assetManager.load("sounds/death_all.wav", Sound.class);
	}

	public void checkScoreAchievements(){
		if(android) {
			if (Score.gameScore >= 14)
				gpgsController.unlockAchievement(achievement_novice_runner);

			if (Score.gameScore >= 50)
				gpgsController.unlockAchievement(achievement_solid_runner);

			if (Score.gameScore >= 88)
				gpgsController.unlockAchievement(achievement_like_a_forest);
		}
	}
	public void unlockFirstCoin(){
		if(android)
			gpgsController.unlockAchievement(achievement_what_is_that_shiny_thing);
	}
	public void unlockScroogeAchievment(){
		if(android)
			gpgsController.unlockAchievement(achievement_scrooge_mcduck);
	}
}
