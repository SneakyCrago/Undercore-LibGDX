package com.sneakycrago.undercore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.sneakycrago.undercore.Application;
import com.sneakycrago.undercore.objects.Player;
import com.sneakycrago.undercore.objects.SurvivalBulletsWave;
import com.sneakycrago.undercore.objects.SurvivalLasers;
import com.sneakycrago.undercore.objects.WhiteSides;

/**
 * Created by Sneaky Crago on 19.07.2017.
 */

public class SurvivalMode implements Screen, InputProcessor {

    Application game;

    private OrthographicCamera camera;
    private OrthographicCamera spriteCamera;

    //objects
    private Player player;
    private WhiteSides whiteSides;
    private SurvivalLasers survivalLasers;
    //private SurvivalBullets survivalBullets;
    private SurvivalBulletsWave survivalBulletsWave;


    public SurvivalMode(Application game) {
        this.game = game;
        this.camera = game.camera;

        spriteCamera = new OrthographicCamera();
        spriteCamera.setToOrtho(false, Application.V_WIDTH, Application.V_HEIGHT);
        spriteCamera.position.set(camera.viewportWidth -512,camera.viewportHeight -310,0);

    }

    @Override
    public void show() {

        Gdx.input.setInputProcessor(this);

        player = new Player(256-32, 139, game);

        whiteSides = new WhiteSides(game);
        survivalLasers = new SurvivalLasers();
        //survivalBullets = new SurvivalBullets();
        survivalBulletsWave = new SurvivalBulletsWave(game);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);

        update(delta);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();


        game.batch.end();

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        survivalLasers.draw(game.shapeRenderer);
        //survivalBullets.draw(game.shapeRenderer);

        whiteSides.drawSurvival(game.shapeRenderer);

        survivalBulletsWave.draw(game.shapeRenderer);

        player.drawPlayerCube(game.shapeRenderer); //draw PlayerCube
        if(onLine) {
            player.drawPlayerLine(game.shapeRenderer);
            player.Jump = false;
        }
        game.shapeRenderer.end();

        spriteCamera.update();
        camera.update();




        collisionCheck();
    }

    private void update(float delta){
        player.updateSurvival(delta, GAME_SPEED);
        player.checkSurvivalSides();

        survivalLasers.update(delta);

        if(survivalLasers.startBullets()) {
            survivalBulletsWave.update(delta);
        }

    }

    private void collisionCheck(){
        if(player.survivalSidesCollision()) {
            game.setScreen(game.gameOver);
           // System.out.println("Collision: White Sides");
        }
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
    private boolean onLine = false;

    private float GAME_SPEED = 1;
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode){
            case Input.Keys.SHIFT_LEFT:
                game.setScreen(new SurvivalMode(game));
                break;
            case Input.Keys.Z:
                //player.onClick(game,GAME_SPEED);
                player.jumpLeft(game);

                break;
            case Input.Keys.C:
                player.jumpRight(game);
                break;
            case Input.Keys.Q:
                //collisionCheck(delta);
                break;
            case Input.Keys.X:
                player.onLine(game);
                onLine = true;
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode){
            case Input.Keys.X:
                player.onRelease(GAME_SPEED);
                onLine = false;
                break;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(pointer == 0) {
            //player.onClick(game,GAME_SPEED);

            if(screenX <= Gdx.graphics.getWidth()/2) {
                player.jumpLeft(game);
            } else if(screenX > Gdx.graphics.getWidth()/2 && screenX <= Gdx.graphics.getWidth()) {
                player.jumpRight(game);
            }
            //if(Application.playerAlive) {
            //    game.jumpSound.play(Application.volume);
            //}
        } else if(pointer == 1){
            player.onLine(game);
            onLine = true;
        } else{
            onLine = false;
        }
        /*
        if(startAfterDeath) {
            startAfterDeath = false;
        }
        if(startPlay) {
            startPlay = false;
        }
        */
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(pointer == 1){
            player.onRelease(GAME_SPEED);
            onLine = false;
        } else if(pointer == 0){
            player.onRelease(GAME_SPEED);
            onLine = false;
        }
        return true;
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
