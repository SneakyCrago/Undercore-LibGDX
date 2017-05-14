package com.sneakycrago.undercore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
        System.out.println();
        System.out.println("GameScreen");
        this.game = game;
        this.camera = game.camera;


    }

    @Override
    public void show() {
        pawTexture = new Texture(Gdx.files.internal("textures/logo_big.png"), true);
        pawTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Nearest);
        paw = new Sprite(pawTexture);
        paw.setSize(512,310);
        paw.setPosition(512/2 - paw.getWidth()/2, 310/2 - paw.getHeight()/2);

        this.progress = 0;




        //pawTexture = new Texture(Gdx.files.internal("textures/logo.png"), true);

    }

    @Override
    public void render(float delta) {

        //Gdx.gl.glClearColor(18/255f,25/255f,26/255f,1f);
        Gdx.gl.glClearColor(0f,0f,0f,0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        paw.draw(game.batch);
        game.batch.end();



        if(!texturesLoaded){
            game.loadTextures();
            texturesLoaded = true;
            System.out.println("TexturesLoaded");
        }

        if(!screensCreated) {
            game.mainMenuTest = new MainMenuTest(game);
            game.gameScreen = new GameScreen(game);
            game.gameOver = new GameOver(game);
            game.infoScreen = new InfoScreen(game);
            game.mainMenuScreen = new MainMenuScreen(game);
            screensCreated = true;
            System.out.println("Screens created");
        }

        if(!fontsLoaded) {
            game.initFonts();
            game.initDeathFonts();
            game.initMenuFonts();

            fontsLoaded = true;
            System.out.println("Fonts loaded");
        }

        progress = MathUtils.lerp(progress, game.assetManager.getProgress(), .1f);
        if(game.assetManager.update() && screensCreated && fontsLoaded && progress >= game.assetManager.getProgress() - 0.001f) {
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
