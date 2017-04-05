package com.sneakycrago.undercore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.TimeUtils;
import com.sneakycrago.undercore.Application;
import com.sneakycrago.undercore.utils.Score;

/**
 * Created by Sneaky Crago on 25.03.2017.
 */

public class GameOver implements Screen {
    Application game;

    private OrthographicCamera camera;

    private static Texture background;

    private float timer;
    private float time;

    public GameOver(Application game) {
        this.game = game;
        camera = game.camera;

        background = new Texture("textures/game_over_background.png");

        time = 0;
        timer = TimeUtils.nanoTime();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        time += (TimeUtils.nanoTime() - timer);
        timer = TimeUtils.nanoTime();

        Score.makeBestScore();

        game.batch.begin();
        game.batch.draw(background,0,0);
        game.font.draw(game.batch,"Best score: "+ Score.getBestScore(), 180, 195);
        game.font.draw(game.batch,"Your score: "+ Score.getGameScore(), 180, 160);
        game.batch.end();

        if((time / 1000000000) >= 2) {
            if (Gdx.input.justTouched() || Gdx.input.isKeyPressed(Input.Keys.Z)) {
                game.setScreen(new GameScreen(game));
                time = 0;
            }
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
}
