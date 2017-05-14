package com.sneakycrago.undercore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.sneakycrago.undercore.Application;
import com.sneakycrago.undercore.utils.Currency;
import com.sneakycrago.undercore.utils.Score;

import java.awt.Shape;


/**
 * Created by Sneaky Crago on 22.04.2017.
 */

public class MainMenuTest implements Screen {
    Application game;

    private OrthographicCamera camera;
    private static Texture background;


    private BitmapFont font, fontWhite;

    private Vector3 touch;

    private Rectangle btnPlay, btnPlayerSkin, btnMapSkin, btnInfo;
    private boolean playReleased,playerSkinReleased, mapSkinReleased, infoReleased;
    private boolean drawInProgress;

    Texture texture;
    BitmapFont fontHiero;

    public MainMenuTest(Application game) {
        System.out.println();
        System.out.println("MainMenuTest");
        this.game = game;
        this.camera = game.camera;
        this.font = game.font30;
        this.fontWhite = game.font30white;

    }

    @Override
    public void show() {

        touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);

        btnPlay = new Rectangle(48, 280 + 4, 188-48*2, -30 -4*2);
        btnPlayerSkin = new Rectangle(5, 280 + 4 - btnFree, 180, -30 -4*2);
        btnMapSkin = new Rectangle(5 +10,  280 + 4 - btnFree * 2, 150, -30 -4*2);
        btnInfo = new Rectangle(48, 280 + 4 - btnFree * 3, 188-48*2, -30 -4*2);

        playReleased = false;
        playerSkinReleased = false;
        mapSkinReleased = false;
        infoReleased = false;
        drawInProgress = false;

        //Money
        Currency.resetMoney();
    }

    int btnFree = 50;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touch);

        if(Gdx.input.justTouched()) {
            System.out.println(touch);

        }

        // 188 black width
        //94 black region middle

        game.batch.begin();

        //PLAY BUTTON
        if(touch.x > btnPlay.getX() && touch.x < btnPlay.getX() +btnPlay.getWidth() &&
                touch.y < btnPlay.getY() && touch.y > btnPlay.getY() + btnPlay.getHeight() &&
                Gdx.input.isTouched()){
            playReleased = true;
            font.draw(game.batch, "Play", 94 - 34, 280);
        } else if(touch.x > btnPlay.getX() && touch.x < btnPlay.getX() +btnPlay.getWidth() &&
                touch.y < btnPlay.getY() && touch.y > btnPlay.getY() + btnPlay.getHeight() && playReleased) {

            game.setScreen(new GameScreen(game));
            playReleased = false;
        } else {
            fontWhite.draw(game.batch, "Play", 94 - 34, 280);
        }

        //PLAYER SKIN
        if(touch.x > btnPlayerSkin.getX() && touch.x < btnPlayerSkin.getX() +btnPlayerSkin.getWidth() &&
                touch.y < btnPlayerSkin.getY() && touch.y > btnPlayerSkin.getY() + btnPlayerSkin.getHeight() &&
                Gdx.input.isTouched()) {

            playerSkinReleased = true;
            font.draw(game.batch, "Player skin", 5, 280 - btnFree);
        } else if(touch.x > btnPlayerSkin.getX() && touch.x < btnPlayerSkin.getX() +btnPlayerSkin.getWidth() &&
                touch.y < btnPlayerSkin.getY() && touch.y > btnPlayerSkin.getY() + btnPlayerSkin.getHeight() && playerSkinReleased){

            drawInProgress = true;
            playerSkinReleased = false;
        } else {
            fontWhite.draw(game.batch, "Player skin", 5, 280 - btnFree);
        }

        //MAP SKIN
        if(touch.x > btnMapSkin.getX() && touch.x < btnMapSkin.getX() +btnMapSkin.getWidth() &&
                touch.y < btnMapSkin.getY() && touch.y > btnMapSkin.getY() + btnMapSkin.getHeight() &&
                Gdx.input.isTouched()) {
            mapSkinReleased = true;
            font.draw(game.batch, "Map skin", 5 +15,  280 - btnFree * 2);
        } else if (touch.x > btnMapSkin.getX() && touch.x < btnMapSkin.getX() +btnMapSkin.getWidth() &&
                touch.y < btnMapSkin.getY() && touch.y > btnMapSkin.getY() + btnMapSkin.getHeight() && mapSkinReleased){
            drawInProgress = true;
            mapSkinReleased = false;
        } else {
            fontWhite.draw(game.batch, "Map skin", 5 +15,  280 - btnFree * 2);
        }

        // INFO
        if(touch.x > btnInfo.getX() && touch.x < btnInfo.getX() +btnInfo.getWidth() &&
                touch.y < btnInfo.getY() && touch.y > btnInfo.getY() + btnInfo.getHeight() &&
                Gdx.input.isTouched()) {
            infoReleased = true;
            font.draw(game.batch, "Info", 94 - 34, 280 - btnFree * 3);
        } else if(touch.x > btnInfo.getX() && touch.x < btnInfo.getX() +btnInfo.getWidth() &&
                touch.y < btnInfo.getY() && touch.y > btnInfo.getY() + btnInfo.getHeight() && infoReleased){

            game.setScreen(game.infoScreen);
            infoReleased = false;
        } else {
            fontWhite.draw(game.batch, "Info", 94 - 34, 280 - btnFree * 3);
        }

        if(drawInProgress) {
            font.draw(game.batch, "In progress", 512 / 2, 310/2 + 12);
        }

        // Money and Score
        game.font30.draw(game.batch, "Money: " + Currency.currency, 210, 310-5-30);
        game.font30.draw(game.batch, "Score: " + Score.bestScore, 210, 310-35*2);

        // SMALL INFO PANEL
        game.font10.draw(game.batch, "ver. " + Application.VERSION, 10, 15);
        game.font10.draw(game.batch, "fps:"+Gdx.graphics.getFramesPerSecond(), 80, 15);

        game.batch.end();

        //renderDebug();
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
        font.dispose();
        fontWhite.dispose();
        fontHiero.dispose();
    }

    public void renderDebug(){
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        game.shapeRenderer.setColor(Color.WHITE);
        game.shapeRenderer.rect(btnPlay.getX(),btnPlay.getY(),btnPlay.getWidth(), btnPlay.getHeight());
        game.shapeRenderer.rect(btnPlayerSkin.getX(),btnPlayerSkin.getY(),btnPlayerSkin.getWidth(), btnPlayerSkin.getHeight());
        game.shapeRenderer.rect(btnMapSkin.getX(),btnMapSkin.getY(),btnMapSkin.getWidth(), btnMapSkin.getHeight());
        game.shapeRenderer.rect(btnInfo.getX(),btnInfo.getY(),btnInfo.getWidth(), btnInfo.getHeight());
        game.shapeRenderer.end();
    }

}
