package com.sneakycrago.undercore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.TimeUtils;
import com.sneakycrago.undercore.Application;
import com.sneakycrago.undercore.utils.Currency;
import com.sneakycrago.undercore.utils.Globals;
import com.sneakycrago.undercore.utils.Score;
import com.sun.org.apache.xalan.internal.xsltc.dom.DocumentCache;
import com.sun.org.apache.xalan.internal.xsltc.runtime.Parameter;

/**
 * Created by Sneaky Crago on 25.03.2017.
 */

public class GameOver implements Screen {
    Application game;

    private final static int btnRadius = 32;
    private final static int btnRadiusPlay = 42;

    private final static int exitX = 512/2 + 32 + 8 +8, menuX = 512/2 - 96 - 8-8, bntY =20;

    private OrthographicCamera camera;
    private TextureAtlas buttons;
    private Sprite start, menu, exit, startPressed, menuPressed, exitPressed;
    private Circle startBtn, menuBtn, exitBtn;
    private Vector3 touch;

    private boolean playReleased = false;
    private boolean menuReleased = false;
    private boolean exitReleased = false;

    public GameOver(Application game) {
        this.game = game;
        this.camera = game.camera;

        buttons = new TextureAtlas(Gdx.files.internal("textures/buttons/buttons.atlas"),
                Gdx.files.internal("textures/buttons"));
    }

    @Override
    public void show() {
        start = new Sprite(buttons.findRegion("start"));
        start.setSize(btnRadiusPlay*2,btnRadiusPlay*2);
        start.setPosition(512/2 -btnRadiusPlay, 50);

        menu = new Sprite(buttons.findRegion("menu"));
        menu.setSize(btnRadius*2,btnRadius*2);
        menu.setPosition(menuX, bntY);

        exit = new Sprite(buttons.findRegion("exit"));
        exit.setSize(btnRadius*2,btnRadius*2);
        exit.setPosition(exitX, bntY);

        startBtn = new Circle(512/2 -32 + btnRadius, 50 + btnRadius, btnRadius);
        menuBtn = new Circle(512/2 - 96 - 8 + btnRadius, 20 + btnRadius, btnRadius);
        exitBtn = new Circle(512/2 + 32 + 8 + btnRadius, 20 + btnRadius, btnRadius);

        startPressed = new Sprite(buttons.findRegion("start_pressed"));
        startPressed.setSize(btnRadiusPlay*2,btnRadiusPlay*2);
        startPressed.setPosition(512/2 -btnRadiusPlay, 50);

        menuPressed = new Sprite(buttons.findRegion("menu_pressed"));
        menuPressed.setSize(btnRadius*2,btnRadius*2);
        menuPressed.setPosition(menuX, bntY);

        exitPressed = new Sprite(buttons.findRegion("exit_pressed"));
        exitPressed.setSize(btnRadius*2,btnRadius*2);
        exitPressed.setPosition(exitX, bntY);

        touch = new Vector3();

        //Money
        Currency.addMoneyToCurrency();
        Score.makeBestScore();
        game.preferences.putInteger("bestScore", Score.bestScore);
        game.preferences.flush();
        game.preferences.putInteger("currency", Currency.currency);
        game.preferences.flush();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(18/255f,25/255f,26/255f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touch);

        game.batch.begin();
        start.draw(game.batch);
        menu.draw(game.batch);
        exit.draw(game.batch);
        game.font10.draw(game.batch, "fps:"+Gdx.graphics.getFramesPerSecond(), 80, 15);

        game.font30.draw(game.batch,"Game over", 512/2 - 24*3, 270+15);
        game.font.draw(game.batch,"Best score: "+ Score.getBestScore(), 512/2 - 24*3, 195 + 45);
        game.font.draw(game.batch,"Your score: "+ Score.getGameScore(), 512/2 - 24*3, 160 + 45);
        game.font.draw(game.batch,"Money: "+ Currency.Money, 512/2 - 24*3, 160-35 + 45);

        if(checkPlayButton() && Gdx.input.isTouched()){
            startPressed.draw(game.batch);
            playReleased = true;
        } else if(checkPlayButton() && playReleased){
            game.setScreen(new GameScreen(game));
            playReleased = false;
        }

        if(checkMenuButton() && Gdx.input.isTouched()){
            menuPressed.draw(game.batch);
            menuReleased = true;
        } else if(checkMenuButton() && menuReleased) {
            game.setScreen(game.mainMenu);
            menuReleased = false;
        }


        if(checkExitButton() && Gdx.input.isTouched()) {
            exitPressed.draw(game.batch);
            exitReleased = true;
        } else if(checkExitButton() && exitReleased){
            Gdx.app.exit();
            exitReleased = false;
            }


        game.batch.end();
    }

    public boolean checkPlayButton(){
        double circleX = 512/2 ;
        double circleY = 50 + btnRadiusPlay;
        double a = 0, b = 0;

        if(touch.x < circleX) {
            a = circleX - touch.x;
        } else if(touch.x > circleX) {
            a = touch.x - circleX;
        }
        if(touch.y < circleY) {
            b = circleY -touch.y;
        } else if(touch.y > circleY){
            b = touch.y - circleY;
        }
        double c2 = a*a + b*b;
        double c = Math.sqrt(c2);

        if(c > btnRadiusPlay) {
            return false;
        } else if(c <= btnRadiusPlay) {
            return true;
        }
        return false;
    }
    public boolean checkMenuButton(){
        double circleX = menuX + btnRadius;
        double circleY = 20 + btnRadius;
        double a = 0, b = 0;


        if(touch.x < circleX) {
            a = circleX - touch.x;
        } else if(touch.x > circleX) {
            a = touch.x - circleX;
        }
        if(touch.y < circleY) {
            b = circleY -touch.y;
        } else if(touch.y > circleY){
            b = touch.y - circleY;
        }
        double c2 = a*a + b*b;
        double c = Math.sqrt(c2);

        if(c > btnRadius) {
            return false;
        } else if(c <= btnRadius) {
            return true;
        }
        return false;
    }
    public boolean checkExitButton(){
        double circleX = exitX + btnRadius;
        double circleY = bntY + btnRadius;
        double a = 0, b = 0;

        if(touch.x < circleX) {
            a = circleX - touch.x;
        } else if(touch.x > circleX) {
            a = touch.x - circleX;
        }
        if(touch.y < circleY) {
            b = circleY -touch.y;
        } else if(touch.y > circleY){
            b = touch.y - circleY;
        }
        double c2 = a*a + b*b;
        double c = Math.sqrt(c2);

        if(c > btnRadius) {
            return false;
        } else if(c <= btnRadius) {
            return true;
        }
        return false;
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
        buttons.dispose();
    }
}
