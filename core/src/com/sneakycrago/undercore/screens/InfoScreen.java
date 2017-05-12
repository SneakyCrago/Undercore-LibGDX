package com.sneakycrago.undercore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.sneakycrago.undercore.Application;

/**
 * Created by Sneaky Crago on 24.04.2017.
 */

public class InfoScreen implements Screen {
    Application game;

    public InfoScreen(Application game) {
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(18/255f,25/255f,26/255f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.font10.draw(game.batch,"THE BEST GAME EVER" , 512/2 - 120, 210);
        game.font10.draw(game.batch,"MADE BY GOOD GUYS, WHICH CARES" , 512/2 - 220, 210 -30);
        game.font10.draw(game.batch,"tap to return" , 512/2 - 80, 210-60);
        game.batch.end();

        if(Gdx.input.justTouched()){
            game.setScreen(game.mainMenuTest);
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
