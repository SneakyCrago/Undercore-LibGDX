package com.sneakycrago.undercore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sneakycrago.undercore.Application;
import com.sneakycrago.undercore.utils.Currency;
import com.sneakycrago.undercore.utils.Score;
import java.util.Random;

/**
 * Created by Sneaky Crago on 25.03.2017.
 */

public class GameOver implements Screen {
    Application game;

    private final static int btnRadius = 32;
    private final static int btnRadiusPlay = 42;

    private final static int exitX = 512/2 + 32 + 8 +8, menuX = 512/2 - 96 - 8-8, bntY =20;

    private OrthographicCamera camera;
    private OrthographicCamera spriteCamera;

    private Random random;

    private int frase;
    private String death;

    private Sprite currency;

    private boolean newFrase = true;

    private int best;

    private GlyphLayout glyphLayout;

    private BitmapFont deathFont, deathFontRU;

    private Viewport viewport;
    private Viewport spriteViewport;

    private Stage stage;
    private TextureRegion playTex,playPressedTex, menuTex, menuPressedTex, exitTex, exitPressedTex;

    private Button playButton, menuButton, exitButton;

    private float moneyX;

    public GameOver(Application game) {

        random = new Random();

        this.game = game;
        this.camera = game.camera;

        spriteCamera = new OrthographicCamera();
        spriteCamera.setToOrtho(false, Application.V_WIDTH, Application.V_HEIGHT);

        viewport = new StretchViewport(Application.V_WIDTH*2,Application.V_HEIGHT*2,camera);
        viewport.apply();

        spriteViewport = new StretchViewport(Application.V_WIDTH, Application.V_HEIGHT, spriteCamera);
        spriteViewport.apply();

        camera.position.set(camera.viewportWidth/2 - 256,camera.viewportHeight /2,0);
        spriteCamera.position.set(camera.viewportWidth/2 - 256,camera.viewportHeight /2 - 155,0);


        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        switch (Application.gameSkin){
            case 0:
                playTex = new TextureRegion(game.fullA1.findRegion("play0"));
                playPressedTex = new TextureRegion(game.fullA1.findRegion("play0pressed"));
                menuTex = new TextureRegion(game.fullA1.findRegion("menu0"));
                menuPressedTex = new TextureRegion(game.fullA1.findRegion("menu0pressed"));
                exitTex = new TextureRegion(game.fullA1.findRegion("exit0"));
                exitPressedTex = new TextureRegion(game.fullA1.findRegion("exit0pressed"));
                break;
            case 1:
                playTex = new TextureRegion(game.fullA1.findRegion("play1"));
                playPressedTex = new TextureRegion(game.fullA1.findRegion("play1pressed"));
                menuTex = new TextureRegion(game.fullA1.findRegion("menu1"));
                menuPressedTex = new TextureRegion(game.fullA1.findRegion("menu1pressed"));
                exitTex = new TextureRegion(game.fullA1.findRegion("exit1"));
                exitPressedTex = new TextureRegion(game.fullA1.findRegion("exit1pressed"));
                break;
            case 2: playTex = new TextureRegion(game.fullA1.findRegion("play2"));
                playPressedTex = new TextureRegion(game.fullA1.findRegion("play2pressed"));
                menuTex = new TextureRegion(game.fullA1.findRegion("menu2"));
                menuPressedTex = new TextureRegion(game.fullA1.findRegion("menu2pressed"));
                exitTex = new TextureRegion(game.fullA1.findRegion("exit2"));
                exitPressedTex = new TextureRegion(game.fullA1.findRegion("exit2pressed"));
                break;
            case 3:
                playTex = new TextureRegion(game.fullA1.findRegion("play3"));
                playPressedTex = new TextureRegion(game.fullA1.findRegion("play3pressed"));
                menuTex = new TextureRegion(game.fullA1.findRegion("menu3"));
                menuPressedTex = new TextureRegion(game.fullA1.findRegion("menu3pressed"));
                exitTex = new TextureRegion(game.fullA1.findRegion("exit3"));
                exitPressedTex = new TextureRegion(game.fullA1.findRegion("exit3pressed"));
                break;
            case 4:
                playTex = new TextureRegion(game.fullA1.findRegion("play4"));
                playPressedTex = new TextureRegion(game.fullA1.findRegion("play4pressed"));
                menuTex = new TextureRegion(game.fullA1.findRegion("menu4"));
                menuPressedTex = new TextureRegion(game.fullA1.findRegion("menu4pressed"));
                exitTex = new TextureRegion(game.fullA1.findRegion("exit4"));
                exitPressedTex = new TextureRegion(game.fullA1.findRegion("exit4pressed"));
        }

        playButton = new Button(new TextureRegionDrawable(playTex),
                new TextureRegionDrawable(playPressedTex),
                new TextureRegionDrawable(playTex));

        menuButton = new Button(new TextureRegionDrawable(menuTex),
                new TextureRegionDrawable(menuPressedTex),
                new TextureRegionDrawable(menuTex));

        exitButton = new Button(new TextureRegionDrawable(exitTex),
                new TextureRegionDrawable(exitPressedTex),
                new TextureRegionDrawable(exitTex));

        playButton.setPosition((512/2 -btnRadiusPlay )*2, 50 *2);
        playButton.setSize(168,168);
        menuButton.setPosition(menuX *2, bntY *2);
        menuButton.setSize(128,128);
        exitButton.setPosition(exitX*2, bntY*2);
        exitButton.setSize(128,128);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                newFrase = true;
                best = Score.getBestScore();
                //game.setScreen(new GameScreen(game));
                game.setScreen(game.gameScreen);
                game.gameOver.pause();

                game.preferences.putInteger("gameSkin", Application.gameSkin);
                game.preferences.flush();
            }
        });

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                newFrase = true;
                best = Score.getBestScore();
                game.activePlay = true;
                game.setScreen(game.mainMenuScreen);
                game.mainMenuScreen.show();
                game.gameOver.pause();
                game.preferences.putInteger("gameSkin", Application.gameSkin);
                game.preferences.flush();
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                newFrase = true;
                best = Score.getBestScore();
                Gdx.app.exit();
                game.preferences.putInteger("gameSkin", Application.gameSkin);
                game.preferences.flush();
            }
        });

        stage.addActor(playButton);
        stage.addActor(menuButton);
        stage.addActor(exitButton);


        currency = new Sprite(game.currencyTexture);
        float scale = 0.12f;
        currency.setSize(199*scale, 300*scale);
        glyphLayout = new GlyphLayout();

        createNewFrase();

        deathFont = new BitmapFont();
        deathFontRU = new BitmapFont();
        if(game.en) {
            switch (Application.gameSkin) {
                case 0:
                    deathFont = game.death0;
                    break;
                case 1:
                    deathFont = game.death1;
                    break;
                case 2:
                    deathFont = game.death2;
                    break;
                case 3:
                    deathFont = game.death3;
                    break;
                case 4:
                    deathFont = game.death4;
                    break;
            }
        }else if(game.ru){
            switch (Application.gameSkin) {
                case 0:
                    deathFontRU = game.death0RU;
                    break;
                case 1:
                    deathFontRU = game.death1RU;
                    break;
                case 2:
                    deathFontRU = game.death2RU;
                    break;
                case 3:
                    deathFontRU = game.death3RU;
                    break;
                case 4:
                    deathFontRU = game.death4RU;
                    break;
            }
        }

        game.gameScreen.dispose();

        //Money & Score
        Currency.addMoneyToCurrency();

        best = Score.getBestScore();
        Score.makeBestScore();
        //Save
        game.preferences.putInteger("bestScore", Score.bestScore);
        game.preferences.flush();
        game.preferences.putInteger("currency", Currency.currency);
        game.preferences.flush();

        Application.reborn = false;
    }

    @Override
    public void render(float delta) {
        game.adTimer();
        Gdx.gl.glClearColor(18/255f,25/255f,26/255f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        stage.act(delta);
        stage.draw();

        game.batch.begin();

        game.font.draw(game.batch, ""+game.time, 0, 40);
        game.font.draw(game.batch, ""+game.showInterstitialAd, 0, 80);

        game.font10.draw(game.batch, "fps:"+Gdx.graphics.getFramesPerSecond(), 80*2, 15*2 );
        if(game.en) {
            glyphLayout.setText(deathFont, death, Color.WHITE, 512 * 2, Align.center, true);
            deathFont.draw(game.batch, glyphLayout, 0, (310 - 10) * 2);
        } else if(game.ru){
            glyphLayout.setText(deathFontRU, death, Color.WHITE, 512 * 2, Align.center, true);
            deathFontRU.draw(game.batch, glyphLayout, 0, (310 - 10) * 2);
        }
        glyphLayout.setText(game.font30white,"" +Currency.Money,Color.WHITE,512*2, Align.center, true);

        game.font30white.draw(game.batch,glyphLayout , 0+10, currency.getY()*2 + glyphLayout.height + 10);
        moneyX = 256- currency.getWidth()-glyphLayout.width/4;

        currency.setPosition(moneyX, 210);

        if(Score.getGameScore() > best){
            Score.makeBestScore();
            if(game.en) {
                glyphLayout.setText(game.font, "New record!", Color.WHITE, 512 * 2, Align.center, true);
                game.font.draw(game.batch, glyphLayout, 0, (205 - 10) * 2);
                glyphLayout.setText(game.smallWhiteFont, "Score: " + Score.getGameScore(), Color.WHITE, 512 * 2, Align.center, true);
                game.smallWhiteFont.draw(game.batch, glyphLayout, 0, (205 - 24 - 15) * 2);
            } else if(game.ru){
                glyphLayout.setText(game.fontRU, "Новый рекорд!", Color.WHITE, 512 * 2, Align.center, true);
                game.fontRU.draw(game.batch, glyphLayout, 0, (205 - 10) * 2);
                glyphLayout.setText(game.smallWhiteFontRU, "Счет: " + Score.getGameScore(), Color.WHITE, 512 * 2, Align.center, true);
                game.smallWhiteFontRU.draw(game.batch, glyphLayout, 0, (205 - 24 - 15) * 2);
            }
        } else {
            if(game.en) {
                glyphLayout.setText(game.font40white, "Score: " + Score.getGameScore(), Color.WHITE, 512 * 2, Align.center, true);
                game.font40white.draw(game.batch, glyphLayout, 0, (205 - ((24 + 15) / 2) - 6) * 2);
            } else if(game.ru){
                glyphLayout.setText(game.tutFontRu, "Счет: " + Score.getGameScore(), Color.WHITE, 512 * 2, Align.center, true);
                game.tutFontRu.draw(game.batch, glyphLayout, 0, (205 - ((24 + 15) / 2) - 6) * 2);
            }
        }

        spriteCamera.update();
        game.batch.setProjectionMatrix(spriteCamera.combined);

        currency.draw(game.batch);

        game.batch.end();



    }

    private void createNewFrase(){
        if(newFrase) {
            frase = random.nextInt(11) + 1;
            if (game.ru) {
                switch (frase) {
                    case 1:
                        death = "Конец игры";
                        break;
                    case 2:
                        death = "Ты пытался";
                        break;
                    case 3:
                        death = "Тупик";
                        break;
                    case 4:
                        death = "ХА-ХА";
                        break;
                    case 5:
                        death = "Мертв";
                        break;
                    case 6:
                        death = "Сломан";
                        break;
                    case 7:
                        death = "В следующий раз";
                        break;
                    case 8:
                        death = "Что? Уже?!";
                        break;
                    case 9:
                        death = "Удачи";
                        break;
                    case 10:
                        death = "Привет!";
                        break;
                    case 11:
                        death = "Слезы не помогут";
                        break;
                    //case 13:death = "Son, I am disappointed";
                    //    break;
                }
            } else if (game.en) {
                switch (frase) {
                    case 1:
                        death = "Game Over";
                        break;
                    case 2:
                        death = "Failed...";
                        break;
                    case 3:
                        death = "Dead End";
                        break;
                    case 4:
                        death = "HA-HA";
                        break;
                    case 5:
                        death = "R.I.P.";
                        break;
                    case 6:
                        death = "REKT";
                        break;
                    case 7:
                        death = "You got so far";
                        break;
                    case 8:
                        death = "Next time";
                        break;
                    case 9:
                        death = "What? Already?!";
                        break;
                    case 10:
                        death = "Good Luck";
                        break;
                    case 11:
                        death = "Bye.";
                        break;
                    case 12:
                        death = "Tears won't help";
                        break;
                }
                newFrase = false;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
        camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);

        spriteViewport.update(width,height);
        spriteCamera.position.set(camera.viewportWidth/2 - 256,camera.viewportHeight /2 - 155,0);
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
        stage.dispose();
    }
}
