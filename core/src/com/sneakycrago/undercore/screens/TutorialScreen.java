package com.sneakycrago.undercore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sneakycrago.undercore.Application;

/**
 * Created by Sneaky Crago on 19.05.2017.
 */

public class TutorialScreen implements Screen {
    Application game;
    OrthographicCamera camera;

    private Stage stage;
    private Viewport viewport;

    private Texture tutorial0, tutorial1;

    private ImageButton gotIt;

    private TextureRegion gotItTexture;

    private boolean drawSecond = false;

    private GlyphLayout glyphLayout;

    public TutorialScreen(Application game){
        this.game = game;
        this.camera = game.camera;


        viewport =new StretchViewport(Application.V_WIDTH*2,Application.V_HEIGHT*2);

        stage = new Stage(viewport, game.batch);

        viewport.apply();
        camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);

        tutorial0 = game.assetManager.get("textures/tutorial0.png", Texture.class);
        tutorial1 = game.assetManager.get("textures/tutorial1.png", Texture.class);

        //gotItTexture = new TextureRegion(game.assetManager.get("textures/buttons/GotItButton.png", Texture.class));
        gotItTexture = new TextureRegion(game.assetManager.get("textures/GotItButton_big.png", Texture.class));
        glyphLayout = new GlyphLayout();


    }

    @Override
    public void show() {
        //camera.setToOrtho(false,512, 310);
        //game.batch.setProjectionMatrix(camera.combined);

        Gdx.input.setInputProcessor(stage);
        gotIt = new ImageButton(new TextureRegionDrawable(gotItTexture));
        gotIt.setPosition(1024/2-gotIt.getWidth()/2, 10);

        gotIt.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //game.setScreen(game.gameScreen);
                if(!drawSecond) {
                    drawSecond = true;
                } else if(drawSecond){
                    drawSecond = false;
                    game.goTutorial = false;
                    game.preferences.putBoolean("goTutorial", game.goTutorial);
                    game.preferences.flush();
                    //System.out.println(game.goTutorial + " prefs: " + game.preferences.getBoolean("goTutorial"));
                    game.setScreen(game.gameScreen);
                }
            }
        });

        stage.addActor(gotIt);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(18/255f,25/255f,26/255f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        if(!drawSecond) {
            game.batch.draw(tutorial0, 0, 0);
        }
        if(drawSecond){
            game.batch.draw(tutorial1, 0,0);
        }
        game.batch.end();
        stage.draw();

        game.batch.begin();
        //Button
        if(game.en) {
            glyphLayout.setText(game.tutFont, "Got it!", Color.WHITE, gotIt.getWidth(), Align.center, true);
            game.tutFont.draw(game.batch, glyphLayout, gotIt.getX(), gotIt.getY() + gotIt.getHeight() / 2 + glyphLayout.height / 2);
        } else if(game.ru){
            glyphLayout.setText(game.tutFontRu, "Понял!", Color.WHITE, gotIt.getWidth(), Align.center, true);
            game.tutFontRu.draw(game.batch, glyphLayout, gotIt.getX(), gotIt.getY() + gotIt.getHeight() / 2 + glyphLayout.height / 2);
        }
        if(!drawSecond) {
            if(game.en) {
                glyphLayout.setText(game.tutFont, "Tap to jump", Color.WHITE, 512 * 2, Align.center, true);
                game.tutFont.draw(game.batch, glyphLayout, 0, 310 * 2 - 120);
            } else if(game.ru){
                glyphLayout.setText(game.tutFontRu, "Нажимай что бы прыгать", Color.WHITE, 512 * 2, Align.center, true);
                game.tutFontRu.draw(game.batch, glyphLayout, 0, 310 * 2 - 120);
            }
        } else {
            if(game.en) {
                glyphLayout.setText(game.tutFont, "Double-tap to keep the same height", Color.WHITE, 512 * 2, Align.center, true);
                game.tutFont.draw(game.batch, glyphLayout, 0, 310 * 2 - 120);
            }else if(game.ru){
                glyphLayout.setText(game.tutFont2RU, "Для поддержания высоты используйте два пальца", Color.WHITE, 512 * 2, Align.center, true);
                game.tutFont2RU.draw(game.batch, glyphLayout, 0, 310 * 2 - 120);
            }
        }
        game.batch.end();


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
