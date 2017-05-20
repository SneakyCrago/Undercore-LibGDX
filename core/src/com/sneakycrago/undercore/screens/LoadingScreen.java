package com.sneakycrago.undercore.screens;

import com.badlogic.gdx.Gdx;
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

public class LoadingScreen implements Screen {
    Application game;

    private OrthographicCamera camera;

    private Texture pawTexture;
    private Sprite paw;

    private float progress;

    private float timer = TimeUtils.nanoTime();
    private float time = 0;

    private boolean fontsLoaded = false, screensCreated = false, texturesLoaded= false;

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

    }

    @Override
    public void show() {


        this.progress = 0;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f,0f,0f,0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        paw.draw(game.batch);
        game.batch.end();

        if(!texturesLoaded){
            game.assetManager.update();
            game.loadTextures();
            game.assetManager.finishLoading();
            texturesLoaded = true;

            game.playerSkin0 = game.assetManager.get("textures/animation/playerAnim.png");
            game.playerSkin1 = game.assetManager.get("textures/animation/playerAnim1.png");
            game.playerSkin2 = game.assetManager.get("textures/animation/playerAnim2.png");
            game.playerSkin3 = game.assetManager.get("textures/animation/playerAnim3.png");
            game.playerSkin4 = game.assetManager.get("textures/animation/playerAnim4.png");

            game.circleAtlas = game.assetManager.get("textures/circle.atlas");

            game.circlesSkin = new TextureRegion[5];
            game.circlesSkin[0] = new TextureRegion(game.circleAtlas.findRegion("circle"));
            game.circlesSkin[1] = new TextureRegion(game.circleAtlas.findRegion("circle1"));
            game.circlesSkin[2] = new TextureRegion(game.circleAtlas.findRegion("circle2"));
            game.circlesSkin[3] = new TextureRegion(game.circleAtlas.findRegion("circle3"));
            game.circlesSkin[4] = new TextureRegion(game.circleAtlas.findRegion("circle4"));

            game.snipersSkin = new Texture[2];
            game.snipersSkin[0] = game.assetManager.get("textures/animation/sniper.png");
            game.snipersSkin[1] = game.assetManager.get("textures/animation/sniper2.png");

            game.bigArrowAtlas = game.assetManager.get("textures/big_arrow.atlas");

            game.bigArrowSkin = new TextureRegion[5];
            game.bigArrowSkin[0] = new TextureRegion(game.bigArrowAtlas.findRegion("big_arrow"));
            game.bigArrowSkin[1] = new TextureRegion(game.bigArrowAtlas.findRegion("big_arrow1"));
            game.bigArrowSkin[2] = new TextureRegion(game.bigArrowAtlas.findRegion("big_arrow2"));
            game.bigArrowSkin[3] = new TextureRegion(game.bigArrowAtlas.findRegion("big_arrow3"));
            game.bigArrowSkin[4] = new TextureRegion(game.bigArrowAtlas.findRegion("big_arrow4"));
            //m.out.println("Textures Loaded");

            game.laserAtlas = game.assetManager.get("textures/laser.atlas");

            game.laserSkin = new TextureRegion[5];
            game.laserSkin[0] = new TextureRegion(game.laserAtlas.findRegion("laser"));
            game.laserSkin[1] = new TextureRegion(game.laserAtlas.findRegion("laser1"));
            game.laserSkin[2] = new TextureRegion(game.laserAtlas.findRegion("laser2"));
            game.laserSkin[3] = new TextureRegion(game.laserAtlas.findRegion("laser3"));
            game.laserSkin[4] = new TextureRegion(game.laserAtlas.findRegion("laser4"));

            game.flipLaserSkin = new TextureRegion[5];
            game.flipLaserSkin[0] = new TextureRegion(game.laserAtlas.findRegion("laser"));
            game.flipLaserSkin[0].flip(false, true);
            game.flipLaserSkin[1] = new TextureRegion(game.laserAtlas.findRegion("laser1"));
            game.flipLaserSkin[1].flip(false, true);
            game.flipLaserSkin[2] = new TextureRegion(game.laserAtlas.findRegion("laser2"));
            game.flipLaserSkin[2].flip(false, true);
            game.flipLaserSkin[3] = new TextureRegion(game.laserAtlas.findRegion("laser3"));
            game.flipLaserSkin[3].flip(false, true);
            game.flipLaserSkin[4] = new TextureRegion(game.laserAtlas.findRegion("laser4"));
            game.flipLaserSkin[4].flip(false, true);

            game.currencyTexture= new Texture(Gdx.files.internal("textures/currency.png"), true);
            game.currencyTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Nearest);



        }

        if(!screensCreated && texturesLoaded && progress >= game.assetManager.getProgress() - 0.001f) {
            game.gameScreen = new GameScreen(game);
            game.gameOver = new GameOver(game);
            game.mainMenuScreen = new MainMenuScreen(game);
            game.tutorialScreen = new TutorialScreen(game);
            screensCreated = true;
            //System.out.println("Screens created");
        }

        if(!fontsLoaded && texturesLoaded&& progress >= game.assetManager.getProgress() - 0.001f) {
            game.initFonts();
            game.initDeathFonts();
            game.initMenuFonts();

            fontsLoaded = true;
            //System.out.println("Fonts created");
        }

        progress = MathUtils.lerp(progress, game.assetManager.getProgress(), .1f);
        if(game.assetManager.update() && screensCreated && fontsLoaded&& progress >= game.assetManager.getProgress() - 0.001f) {
            game.assetManager.finishLoading();
            game.setScreen(game.mainMenuScreen);
        }

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(62/255f,181/255f,241/255f,1f);
        game.shapeRenderer.rect(0,0 , progress * 512, 8);
        game.shapeRenderer.end();

        time += (TimeUtils.nanoTime() - timer);
        timer = TimeUtils.nanoTime();

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
}
