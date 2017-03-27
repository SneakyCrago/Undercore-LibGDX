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
import com.sneakycrago.undercore.Application;

/**
 * Created by Sneaky Crago on 25.03.2017.
 */

public class GameOver implements Screen {
    Application game;

    private OrthographicCamera camera;

    private static Texture background;

    public GameOver(Application game) {
        this.game = game;
        camera = game.camera;

        background = new Texture("test/game_over.png");

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(background,0,0);
        game.font.draw(game.batch,"Your score: "+ game.getScore(), 205, 165);
        game.batch.end();

        if(Gdx.input.justTouched() || Gdx.input.isKeyPressed(Input.Keys.Z)) {
            game.setScreen(new GameScreen(game));
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
