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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
    /*
    private TextureAtlas buttons;
    private Sprite start, menu, exit, startPressed, menuPressed, exitPressed;
    private Circle startBtn, menuBtn, exitBtn;
    private Vector3 touch;
    */
    private OrthographicCamera spriteCamera;

    private Random random;

    private int frase;
    private String death;

    private boolean playReleased = false;
    private boolean menuReleased = false;
    private boolean exitReleased = false;

    private Texture currencyTexture;
    private Sprite currencySprite;

    private boolean newFrase = true;

    private int best;

    private GlyphLayout glyphLayout;

    private BitmapFont deathFont;

    private Viewport viewport;
    private Viewport spriteViewport;

    private Stage stage;

    private TextureAtlas buttonsAtlas;

    private TextureRegion playTex,playPressedTex, menuTex, menuPressedTex, exitTex, exitPressedTex;

    private ImageButton play, menu, exit;

    public GameOver(Application game) {

        random = new Random();

        this.game = game;
        this.camera = game.camera;

        spriteCamera = new OrthographicCamera();
        spriteCamera.setToOrtho(false, Application.V_WIDTH, Application.V_HEIGHT);
    }

    @Override
    public void show() {
        viewport = new StretchViewport(Application.V_WIDTH*2,Application.V_HEIGHT*2,camera);
        viewport.apply();

        spriteViewport = new StretchViewport(Application.V_WIDTH, Application.V_HEIGHT, spriteCamera);
        spriteViewport.apply();

        camera.position.set(camera.viewportWidth/2 - 256,camera.viewportHeight /2,0);
        spriteCamera.position.set(camera.viewportWidth/2 - 256,camera.viewportHeight /2 - 155,0);


        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        buttonsAtlas = game.assetManager.get("textures/buttons/gameOver.atlas");

        switch (Application.gameSkin){
            case 0:
                playTex = new TextureRegion(buttonsAtlas.findRegion("play0"));
                playPressedTex = new TextureRegion(buttonsAtlas.findRegion("play0Pressed"));

                menuTex = new TextureRegion(buttonsAtlas.findRegion("menu0"));
                menuPressedTex = new TextureRegion(buttonsAtlas.findRegion("menu0Pressed"));

                exitTex = new TextureRegion(buttonsAtlas.findRegion("exit0"));
                exitPressedTex = new TextureRegion(buttonsAtlas.findRegion("exit0Pressed"));
                break;
            case 1:
                playTex = new TextureRegion(buttonsAtlas.findRegion("play1"));
                playPressedTex = new TextureRegion(buttonsAtlas.findRegion("play1Pressed"));

                menuTex = new TextureRegion(buttonsAtlas.findRegion("menu1"));
                menuPressedTex = new TextureRegion(buttonsAtlas.findRegion("menu1Pressed"));

                exitTex = new TextureRegion(buttonsAtlas.findRegion("exit1"));
                exitPressedTex = new TextureRegion(buttonsAtlas.findRegion("exit1Pressed"));
                break;
            case 2: playTex = new TextureRegion(buttonsAtlas.findRegion("play2"));
                playPressedTex = new TextureRegion(buttonsAtlas.findRegion("play2Pressed"));

                menuTex = new TextureRegion(buttonsAtlas.findRegion("menu2"));
                menuPressedTex = new TextureRegion(buttonsAtlas.findRegion("menu2Pressed"));

                exitTex = new TextureRegion(buttonsAtlas.findRegion("exit2"));
                exitPressedTex = new TextureRegion(buttonsAtlas.findRegion("exit2Pressed"));
                break;
            case 3:
                playTex = new TextureRegion(buttonsAtlas.findRegion("play3"));
                playPressedTex = new TextureRegion(buttonsAtlas.findRegion("play3Pressed"));

                menuTex = new TextureRegion(buttonsAtlas.findRegion("menu3"));
                menuPressedTex = new TextureRegion(buttonsAtlas.findRegion("menu3Pressed"));

                exitTex = new TextureRegion(buttonsAtlas.findRegion("exit3"));
                exitPressedTex = new TextureRegion(buttonsAtlas.findRegion("exit3Pressed"));
                break;
            case 4:
                playTex = new TextureRegion(buttonsAtlas.findRegion("play4"));
                playPressedTex = new TextureRegion(buttonsAtlas.findRegion("play4Pressed"));

                menuTex = new TextureRegion(buttonsAtlas.findRegion("menu4"));
                menuPressedTex = new TextureRegion(buttonsAtlas.findRegion("menu4Pressed"));

                exitTex = new TextureRegion(buttonsAtlas.findRegion("exit4"));
                exitPressedTex = new TextureRegion(buttonsAtlas.findRegion("exit4Pressed"));
        }


        play = new ImageButton(new TextureRegionDrawable(playTex),
                new TextureRegionDrawable(playPressedTex),
                new TextureRegionDrawable(playTex));

        menu = new ImageButton(new TextureRegionDrawable(menuTex),
                new TextureRegionDrawable(menuPressedTex),
                new TextureRegionDrawable(menuTex));

        exit = new ImageButton(new TextureRegionDrawable(exitTex),
                new TextureRegionDrawable(exitPressedTex),
                new TextureRegionDrawable(exitTex));


        play.setPosition((512/2 -btnRadiusPlay )*2, 50 *2);
        menu.setPosition(menuX *2, bntY *2);
        exit.setPosition(exitX*2, bntY*2);

        play.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                newFrase = true;
                best = Score.getBestScore();
                game.setScreen(new GameScreen(game));
            }
        });

        menu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                newFrase = true;
                best = Score.getBestScore();
                game.setScreen(game.mainMenuScreen);
            }
        });

        exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                newFrase = true;
                best = Score.getBestScore();
                Gdx.app.exit();
            }
        });

        stage.addActor(play);
        stage.addActor(menu);
        stage.addActor(exit);
        Gdx.input.setInputProcessor(stage);

        currencyTexture = new Texture(Gdx.files.internal("textures/currency.png"), true);
        currencyTexture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Nearest);
        currencySprite = new Sprite(currencyTexture);
        currencySprite.setScale(0.125f);
        currencySprite.setPosition(256 - currencySprite.getWidth()/2 - 20, 0 + 80 - currencySprite.getY()/4);//205 - 24 -15 + 15

        glyphLayout = new GlyphLayout();

        createNewFrase();

        deathFont = new BitmapFont();
        switch (Application.gameSkin){
            case 0: deathFont = game.death0;
                break;
            case 1: deathFont = game.death1;
                break;
            case 2: deathFont = game.death2;
                break;
            case 3: deathFont = game.death3;
                break;
            case 4: deathFont = game.death4;
                break;
        }

        //Money & Score
        Currency.addMoneyToCurrency();

        best = Score.getBestScore();
        Score.makeBestScore();
        //Save
        game.preferences.putInteger("bestScore", Score.bestScore);
        game.preferences.flush();
        game.preferences.putInteger("currency", Currency.currency);
        game.preferences.flush();
    }

    @Override
    public void render(float delta) {
        spriteCamera.update();
        game.batch.setProjectionMatrix(spriteCamera.combined);

        Gdx.gl.glClearColor(18/255f,25/255f,26/255f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        currencySprite.draw(game.batch);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);


        game.font10.draw(game.batch, "fps:"+Gdx.graphics.getFramesPerSecond(), 80*2, 15*2 );
        glyphLayout.setText(deathFont, death , Color.WHITE,512*2, Align.center, true);
        deathFont.draw(game.batch, glyphLayout, 0, (310-10)*2);

        glyphLayout.setText(game.font30white,"" +Currency.Money,Color.WHITE,512*2, Align.center, true);
        game.font30white.draw(game.batch,glyphLayout , 0, (310/2 + currencySprite.getY() + 8)*2);

        currencySprite.setX(256 - currencySprite.getWidth()/2 -glyphLayout.width/2 - 5);

        if(Score.getGameScore() > best){
            Score.makeBestScore();
            glyphLayout.setText(game.font,"New record!", Color.WHITE,512*2, Align.center, true);
            game.font.draw(game.batch,glyphLayout, 0, (205 -10) *2);
            glyphLayout.setText(game.smallWhiteFont, "Score: "+ Score.getGameScore() , Color.WHITE,512*2, Align.center, true);
            game.smallWhiteFont.draw(game.batch,glyphLayout, 0,(205 - 24 -15)*2);
        } else {
            glyphLayout.setText(game.smallWhiteFont, "Score: "+ Score.getGameScore() , Color.WHITE,512*2, Align.center, true);
            game.smallWhiteFont.draw(game.batch,glyphLayout, 0,(205 -((24+15)/2) -6) *2);
        }

        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    private void createNewFrase(){
        if(newFrase) {
            frase = random.nextInt(12) + 1;
            switch (frase) {
                case 1:death = "Game Over";
                    break;
                case 2:death = "Failed...";
                    break;
                case 3:death = "Dead End";
                    break;
                case 4:death = "HA-HA";
                    break;
                case 5:death = "R.I.P.";
                    break;
                case 6:death = "REKT";
                    break;
                case 7:death = "You got so far";
                    break;
                case 8:death = "Next time";
                    break;
                case 9:death = "What? Already?!";
                    break;
                case 10:death = "Good Luck";
                    break;
                case 11:death = "Bye.";
                    break;
                case 12:death = "Tears won't help";
                    break;
                //case 13:death = "Son, I am disappointed";
                //    break;
            }
            newFrase = false;
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
    }
}
