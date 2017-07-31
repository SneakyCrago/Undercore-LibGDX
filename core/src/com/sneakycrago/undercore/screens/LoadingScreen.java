package com.sneakycrago.undercore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.sneakycrago.undercore.Application;

/**
 * Created by Sneaky Crago on 13.05.2017.
 */

public class LoadingScreen implements Screen, InputProcessor {
    Application game;

    private OrthographicCamera camera;

    private Texture pawTexture;
    private Sprite paw;

    private float progress;

    private float timer = TimeUtils.nanoTime();
    private float time = 0;

    private boolean fontsLoaded = false, screensCreated = false, texturesLoaded= false, assetsLoaded = false;

    public LoadingScreen(Application game){
        //System.out.println("GameScreen");
        this.game = game;
        this.camera = game.camera;

        pawTexture = new Texture(Gdx.files.internal("textures/logo_big.png"), true);
        pawTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Nearest);
        pawTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        pawTexture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        paw = new Sprite(pawTexture);
        paw.setSize(512,310);
        paw.setPosition(512/2 - paw.getWidth()/2, 310/2 - paw.getHeight()/2);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void show() {

        this.progress = 0;
    }

    @Override
    public void render(float delta) {
        game.adTimer();
        Gdx.gl.glClearColor(0f,0f,0f,0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        paw.draw(game.batch);
        game.batch.end();
        if(!assetsLoaded) {
            game.assetManager.update();
            game.loadTextures();
            game.loadSounds();
            game.assetManager.finishLoading();
        }
        if(!texturesLoaded){
            texturesLoaded = true;
            //Animation
            game.playerSkin0 = game.assetManager.get("textures/animation/playerAnim.png");
            game.playerSkin1 = game.assetManager.get("textures/animation/playerAnim1.png");
            game.playerSkin2 = game.assetManager.get("textures/animation/playerAnim2.png");
            game.playerSkin3 = game.assetManager.get("textures/animation/playerAnim3.png");
            game.playerSkin4 = game.assetManager.get("textures/animation/playerAnim4.png");

            game.snipersSkin = new Texture[2];
            game.snipersSkin[0] = game.assetManager.get("textures/animation/sniper.png");
            game.snipersSkin[1] = game.assetManager.get("textures/animation/sniper2.png");

            //Atlas
            game.fullA1 = game.assetManager.get("textures/FullA1.atlas"); //buttons
            game.fullA2 = game.assetManager.get("textures/FullA2.atlas"); //objects
            game.fullA3 = game.assetManager.get("textures/FullA3.atlas");
            // Player Skins
            game.moonAtlas = game.assetManager.get("textures/player/moon.atlas");
            game.dummyAtlas = game.assetManager.get("textures/player/dummy.atlas");
            game.arrowAltas = game.assetManager.get("textures/player/arrow.atlas");

            game.circlesSkin = new TextureRegion[game.skinsAmount];

            game.circlesSkin[0] = new TextureRegion(game.fullA2.findRegion("circle"));
            game.circlesSkin[1] = new TextureRegion(game.fullA2.findRegion("circle1"));
            game.circlesSkin[2] = new TextureRegion(game.fullA2.findRegion("circle2"));
            game.circlesSkin[3] = new TextureRegion(game.fullA2.findRegion("circle3"));
            game.circlesSkin[4] = new TextureRegion(game.fullA2.findRegion("circle4"));

            game.bigArrowSkin = new TextureRegion[game.skinsAmount];
            game.bigArrowSkin[0] = new TextureRegion(game.fullA2.findRegion("big_arrow"));
            game.bigArrowSkin[1] = new TextureRegion(game.fullA2.findRegion("big_arrow2"));
            game.bigArrowSkin[2] = new TextureRegion(game.fullA2.findRegion("big_arrow3"));
            game.bigArrowSkin[3] = new TextureRegion(game.fullA2.findRegion("big_arrow4"));
            game.bigArrowSkin[4] = new TextureRegion(game.fullA2.findRegion("big_arrow5"));

            game.laserSkin = new TextureRegion[game.skinsAmount];
            game.laserSkin[0] = new TextureRegion(game.fullA2.findRegion("laser"));
            game.laserSkin[1] = new TextureRegion(game.fullA2.findRegion("laser1"));
            game.laserSkin[2] = new TextureRegion(game.fullA2.findRegion("laser2"));
            game.laserSkin[3] = new TextureRegion(game.fullA2.findRegion("laser3"));
            game.laserSkin[4] = new TextureRegion(game.fullA2.findRegion("laser4"));

            game.flipLaserSkin = new TextureRegion[game.skinsAmount];
            game.flipLaserSkin[0] = new TextureRegion(game.fullA2.findRegion("laser"));
            game.flipLaserSkin[1] = new TextureRegion(game.fullA2.findRegion("laser1"));
            game.flipLaserSkin[2] = new TextureRegion(game.fullA2.findRegion("laser2"));
            game.flipLaserSkin[3] = new TextureRegion(game.fullA2.findRegion("laser3"));

            game.flipLaserSkin[4] = new TextureRegion(game.fullA2.findRegion("laser4"));
            game.flipLaserSkin[0].flip(false, true);
            game.flipLaserSkin[1].flip(false, true);
            game.flipLaserSkin[2].flip(false, true);
            game.flipLaserSkin[3].flip(false, true);
            game.flipLaserSkin[4].flip(false, true);

            game.currencyTexture = game.fullA2.findRegion("Currency");

            game.moonAnimTex = new Texture[Application.skinsAmount];
            game.moonAnimTex[0] = new Texture(Gdx.files.internal("textures/player/moonAnim0.png"));
            game.moonAnimTex[1] = new Texture(Gdx.files.internal("textures/player/moonAnim1.png"));
            game.moonAnimTex[2] = new Texture(Gdx.files.internal("textures/player/moonAnim2.png"));
            game.moonAnimTex[3] = new Texture(Gdx.files.internal("textures/player/moonAnim3.png"));
            game.moonAnimTex[4] = new Texture(Gdx.files.internal("textures/player/moonAnim4.png"));

            game.skinPrewTex = new TextureRegion[game.skinsAmount];

            for(int i=0; i < game.skinPrewTex.length; i++){
                game.skinPrewTex[i] = new TextureRegion(game.fullA2.findRegion("Skinprew" +i));
            }
            game.skinPrewRandomTex = game.assetManager.get("textures/skinPrewRandom.png");
            createMainMenuTexures();

            // HardMode
            game.hardModeBackground = game.assetManager.get("textures/hardMode/backgroundHard.png");
            game.playerHardAnim = game.assetManager.get("textures/hardMode/playerAnimHard.png");
            game.moonHardAnim = game.assetManager.get("textures/hardMode/moonAnimHard.png");
            game.bigArrowHard = game.assetManager.get("textures/hardMode/big_arrowHard.png");
            game.circleHard = game.assetManager.get("textures/hardMode/circleHard.png");
            game.sniperHard = game.assetManager.get("textures/hardMode/sniperHard.png");

            game.laserHard1 = game.assetManager.get("textures/hardMode/laserHard.png");
            game.laserHard2 = game.assetManager.get("textures/hardMode/laserHard.png");

            game.laserHard = new TextureRegion(game.laserHard1);
            game.flipLaserHard = new TextureRegion(game.laserHard2);
            game.flipLaserHard.flip(false, true);

            game.jumpSound = game.assetManager.get("sounds/jump.mp3"); // new
            game.lineSound = game.assetManager.get("sounds/line.mp3");

            game.deathAllSound = game.assetManager.get("sounds/death_all.wav"); // old
            game.deathAllSound = game.assetManager.get("sounds/death_2.wav"); // new

            game.ambientSound = game.assetManager.get("sounds/background.ogg");

            game.ambientSound.setLooping(true);

            //survival
            //game.bulletTV = new Texture("textures/survival/TV_bullet1.png");
            //game.survivalExplosion = new Texture("textures/survival/explosion.png");
            //game.survivalBook = new Texture[3];

            //game.survivalBook[0] = new Texture("textures/survival/book1.png");
            //game.survivalBook[1] = new Texture("textures/survival/book2.png");
            //game.survivalBook[2] = new Texture("textures/survival/book3.png");


        }

        if(!screensCreated && texturesLoaded && progress >= game.assetManager.getProgress() - 0.001f) {
            game.mainMenuScreen = new MainMenuScreen(game);
            game.tutorialScreen = new TutorialScreen(game);
            game.gameScreen = new GameScreen(game);
            game.gameOver = new GameOver(game);
            game.x2GameModeScreen = new X2GameMode(game);
            game.survivalModeScreen = new SurvivalMode(game);
            screensCreated = true;
            //System.out.println("Screens created");
        }

        if(!fontsLoaded && texturesLoaded&& progress >= game.assetManager.getProgress() - 0.001f) {
            game.initFonts();
            game.initDeathFonts();
            game.initMenuFonts();
            game.initRuFonts();

            fontsLoaded = true;
            //System.out.println("Fonts created");
        }

        progress = MathUtils.lerp(progress, game.assetManager.getProgress(), .1f);
        if(game.assetManager.update() && screensCreated && fontsLoaded&& progress >= game.assetManager.getProgress() - 0.001f) {
            //if(game.android) {
            //    game.gpgsController.signIn();
            //}
            if (!game.gpgsController.isSignedIn()) {
                game.gpgsController.connect();
            }
            game.assetManager.finishLoading();
            game.setScreen(game.mainMenuScreen);
        }

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(62/255f,181/255f,241/255f,1f);
        game.shapeRenderer.rect(0,0 , progress * 512+1, 8);

        game.shapeRenderer.end();

        time += (TimeUtils.nanoTime() - timer);
        timer = TimeUtils.nanoTime();

    }
    private void createMainMenuTexures(){
        game.mplayTex = new TextureRegion[game.skinsAmount];
        game.mplayPressedTex = new TextureRegion[game.skinsAmount];

        game.mworldTex = new TextureRegion[game.skinsAmount];
        game.mworldPressedTex = new TextureRegion[game.skinsAmount];

        game.mplayerTex = new TextureRegion[game.skinsAmount];
        game.mplayerPressedTex = new TextureRegion[game.skinsAmount];

        game.msettingsTex = new TextureRegion[game.skinsAmount];
        game.msettingsPressedTex = new TextureRegion[game.skinsAmount];

        for(int i=0; i < game.skinsAmount; i++) {
            game.mplayTex[i] = new TextureRegion(game.fullA1.findRegion("mplay"+i));
            game.mplayPressedTex[i] = new TextureRegion(game.fullA1.findRegion("mplay"+i+"pressed"));

            game.mworldTex[i] = new TextureRegion(game.fullA1.findRegion("mworld"+i));
            game.mworldPressedTex[i] = new TextureRegion(game.fullA1.findRegion("mworld"+i+"pressed"));

            game.mplayerTex[i] = new TextureRegion(game.fullA1.findRegion("mplayer"+i));
            game.mplayerPressedTex[i] = new TextureRegion(game.fullA1.findRegion("mplayer"+i+"pressed"));

            game.msettingsTex[i] = new TextureRegion(game.fullA1.findRegion("msettings"+i));
            game.msettingsPressedTex[i] = new TextureRegion(game.fullA1.findRegion("msettings"+i+"pressed"));
        }
        game.arrowRightTex = new TextureRegion[game.skinsAmount];

        game.arrowRightTex[0] = new TextureRegion(game.fullA2.findRegion("Arrow R OI 1"));
        game.arrowRightTex[1] = new TextureRegion(game.fullA2.findRegion("Arrow R OI 2"));
        game.arrowRightTex[2] = new TextureRegion(game.fullA2.findRegion("Arrow R OI 3"));
        game.arrowRightTex[3] = new TextureRegion(game.fullA2.findRegion("Arrow R OI 4"));
        game.arrowRightTex[4] = new TextureRegion(game.fullA2.findRegion("Arrow R OI 5"));

        game.arrowRightPressedTex = new TextureRegion[game.skinsAmount];
        game.arrowRightPressedTex[0] = new TextureRegion(game.fullA2.findRegion("Arrow R OI 1pressed"));
        game.arrowRightPressedTex[1] = new TextureRegion(game.fullA2.findRegion("Arrow R OI 2pressed"));
        game.arrowRightPressedTex[2] = new TextureRegion(game.fullA2.findRegion("Arrow R OI 3pressed"));
        game.arrowRightPressedTex[3] = new TextureRegion(game.fullA2.findRegion("Arrow R OI 4pressed"));
        game.arrowRightPressedTex[4] = new TextureRegion(game.fullA2.findRegion("Arrow R OI 5pressed"));


        game.arrowLeftTex = new TextureRegion[game.skinsAmount];
        game.arrowLeftTex[0] = new TextureRegion(game.fullA2.findRegion("Arrow R OI 1"));
        game.arrowLeftTex[1] = new TextureRegion(game.fullA2.findRegion("Arrow R OI 2"));
        game.arrowLeftTex[2] = new TextureRegion(game.fullA2.findRegion("Arrow R OI 3"));
        game.arrowLeftTex[3] = new TextureRegion(game.fullA2.findRegion("Arrow R OI 4"));
        game.arrowLeftTex[4] = new TextureRegion(game.fullA2.findRegion("Arrow R OI 5"));

        game.arrowLeftPressedTex = new TextureRegion[game.skinsAmount];
        game.arrowLeftPressedTex[0] = new TextureRegion(game.fullA2.findRegion("Arrow R OI 1pressed"));
        game.arrowLeftPressedTex[1] = new TextureRegion(game.fullA2.findRegion("Arrow R OI 2pressed"));
        game.arrowLeftPressedTex[2] = new TextureRegion(game.fullA2.findRegion("Arrow R OI 3pressed"));
        game.arrowLeftPressedTex[3] = new TextureRegion(game.fullA2.findRegion("Arrow R OI 4pressed"));
        game.arrowLeftPressedTex[4] = new TextureRegion(game.fullA2.findRegion("Arrow R OI 5pressed"));
        for(int i=0; i< game.skinsAmount; i++) {
            game.arrowLeftTex[i].flip(true,false);
            game.arrowLeftPressedTex[i].flip(true,false);
        }
        game.playRoundTex = new TextureRegion[game.skinsAmount];
        game.playRoundTex[0] = new TextureRegion(game.fullA1.findRegion("play0"));
        game.playRoundTex[1] = new TextureRegion(game.fullA1.findRegion("play1"));
        game.playRoundTex[2] = new TextureRegion(game.fullA1.findRegion("play2"));
        game.playRoundTex[3] = new TextureRegion(game.fullA1.findRegion("play3"));
        game.playRoundTex[4] = new TextureRegion(game.fullA1.findRegion("play4"));

        game.playRoundPressedTex = new TextureRegion[game.skinsAmount];
        game.playRoundPressedTex[0] = new TextureRegion(game.fullA1.findRegion("play0pressed"));
        game.playRoundPressedTex[1] = new TextureRegion(game.fullA1.findRegion("play1pressed"));
        game.playRoundPressedTex[2] = new TextureRegion(game.fullA1.findRegion("play2pressed"));
        game.playRoundPressedTex[3] = new TextureRegion(game.fullA1.findRegion("play3pressed"));
        game.playRoundPressedTex[4] = new TextureRegion(game.fullA1.findRegion("play4pressed"));

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
