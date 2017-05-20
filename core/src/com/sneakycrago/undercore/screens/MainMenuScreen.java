package com.sneakycrago.undercore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

/**
 * Created by Sneaky Crago on 13.05.2017.
 */

public class MainMenuScreen implements Screen {
    Application game;

    private OrthographicCamera camera;
    private Stage stage;

    private TextureAtlas mainMenuAtlas;
    private ImageButton play , gameSkin, plSkin, settings;
    private TextureRegion playTex, playerTex, worldTex, settingsTex, playPressedTex,
            playerPressedTex, worldPressedTex, settingsPressedTex;
    private Sprite skull;

    private Sprite currency, plusWhite;

    private int space = 32;
    private int buttonHeight = 32;

    private GlyphLayout glyphLayout;

    private Viewport viewport;


    public MainMenuScreen(Application game) {
        this.game = game;
        this.camera = game.camera;

        glyphLayout = new GlyphLayout();

        viewport =new StretchViewport(Application.V_WIDTH*2,Application.V_HEIGHT*2);

        stage = new Stage(viewport, game.batch);

        viewport.apply();
        camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);

        mainMenuAtlas = game.assetManager.get("textures/buttons/mainMenu.atlas");

        skull = new Sprite(mainMenuAtlas.findRegion("skull"));
        skull.setScale(0.75f);
        //384 + 32,0, 96, 310 black right board
        skull.setPosition((384 + 32)*2  + (192 - skull.getWidth())/2, 620/2 - skull.getHeight()/2);
        createButtons();

        currency = new Sprite(game.currencyTexture);
        float scale = 0.14f;
        currency.setSize(199*scale, 300*scale);

        plusWhite = new Sprite(mainMenuAtlas.findRegion("PlusWhite"));
        plusWhite.setSize(16, 16);
    }

    @Override
    public void show() {
        camera.setToOrtho(false,512, 310);
        game.batch.setProjectionMatrix(camera.combined);

        Gdx.input.setInputProcessor(stage);

        game.loadingScreen.dispose();
    }

    private int squardWidth = 256, squardHeight = 256-96, line = 2,
    scoreX =((384 + 32)/2 - squardWidth/2 + line)*2, scoreY = (310/2 + squardHeight - squardHeight/2  - line)*2;

    private float moneyX, moneyY, moneyWidth;

    private int up = 5;

    private int minusLesha = 6;
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(18/255f,25/255f,26/255f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //Background
        game.shapeRenderer.setColor(Color.BLACK);
        game.shapeRenderer.rect(384 + 32,0, 96, 310);

        //Menu
        game.shapeRenderer.rect((384 + 32)/2 - squardWidth/2, 310/2 - squardHeight/2 + up , squardWidth, squardHeight);
        game.shapeRenderer.setColor(15/255f, 16/255f, 16/255f,1f);
        game.shapeRenderer.rect((384 + 32)/2 - squardWidth/2 + line, 310/2 - squardHeight/2  + up + line,
                squardWidth -line*2, squardHeight- line*2);

        // Alpha
        game.shapeRenderer.setColor(19/255f,22/255f,22/255f, 0.1f);
        game.shapeRenderer.rect((384 + 32)/2 - squardWidth/2 + line, 310/2 - squardHeight/2  + up + line, squardWidth -line*2, 16);
        // Currency BottomLeft Version
        //game.shapeRenderer.rect((384 + 32)/2 - 38, moneyY/2 + 1,76, 12);
        // Currency Center Version
        //game.shapeRenderer.rect(currency.getX() + currency.getWidth()/2,currency.getY(),
        //        currency.getWidth() + moneyWidth/2, 16);
        //game.shapeRenderer.rect(currency.getX() + currency.getWidth()/2, currency.getY(), 100, 16);
        game.shapeRenderer.rect(moneyX/2 -2 +3,moneyY/2 +3,currency.getWidth()/2 + moneyWidth/2 + 7 + plusWhite.getWidth()/2 +4,16);

        game.shapeRenderer.end();

        stage.act(delta);
        stage.draw();

        game.batch.begin();

        skull.draw(game.batch);

        glyphLayout.setText(game.smallWhiteFont, ""+ Score.getBestScore(), Color.WHITE ,(squardWidth - line*2)*2, Align.center, true);
        game.smallWhiteFont.draw(game.batch, glyphLayout, scoreX, scoreY - 5);


        //glyphLayout.setText(game.menu0Font, "Score: "+ Score.getBestScore(), Color.WHITE, 400, Align.bottomLeft, true);
        //game.menu0Font.draw(game.batch, glyphLayout, 0, 600);

        glyphLayout.setText(game.menuBig, "Normal", Color.WHITE, (384 + 32)* 2,Align.center, true);
        game.menuBig.draw(game.batch, glyphLayout, 0, 310*2 - 20);

        //Buttons Text
        glyphLayout.setText(game.menu0Font, "Play" , Color.WHITE, 85, Align.bottomLeft, true);
        game.menu0Font.draw(game.batch, glyphLayout, play.getX() + 75, play.getY() + play.getHeight()/2 + glyphLayout.height/2);
        glyphLayout.setText(game.menu0Font, "World" , Color.WHITE, 85, Align.bottomLeft, true);
        game.menu0Font.draw(game.batch, glyphLayout, gameSkin.getX() + 75, gameSkin.getY() + gameSkin.getHeight()/2+ glyphLayout.height/2);
        glyphLayout.setText(game.menu0Font, "Player" , Color.WHITE, 85, Align.bottomLeft, true);
        game.menu0Font.draw(game.batch, glyphLayout, plSkin.getX() + 75, plSkin.getY() + plSkin.getHeight()/2 + glyphLayout.height/2);
        glyphLayout.setText(game.menu0Font, "Settings" , Color.WHITE, 100, Align.bottomLeft, true);
        game.menu0Font.draw(game.batch, glyphLayout, settings.getX() + 60, settings.getY() + settings.getHeight()/2 + glyphLayout.height/2);

        //Currency.currency = 9999999;

        // Currency BottomLeft Version
        //glyphLayout.setText(game.menu0Font, ""+Currency.currency, Color.WHITE, (384 + 32)* 2, Align.bottomLeft, true);
        //moneyX = (384 + 32) - 40*2;
        //moneyY = 310*2 - 20 -60-currency.getHeight()/2 - glyphLayout.height/2 - minusLesha*2;
        //moneyWidth = glyphLayout.height;
        //game.menu0Font.draw(game.batch, glyphLayout, (384 + 32) - 38*2 + currency.getWidth(), 310*2 - 20 -60- minusLesha*2);

        // Currency Center Version
        glyphLayout.setText(game.menu0Font, ""+Currency.currency, Color.WHITE, (384 + 32)* 2, Align.center, true);
        moneyX = (384 + 32) -currency.getWidth() - glyphLayout.width/2 - 4 -3;
        moneyY = 310*2 - 20 -60-currency.getHeight()/2 - glyphLayout.height/2 - minusLesha*2;
        moneyWidth = glyphLayout.width;
        //game.menu0Font.draw(game.batch, glyphLayout, (384 + 32) - 38*2 + currency.getWidth(), 310*2 - 20 -60- minusLesha*2);
        game.menu0Font.draw(game.batch, glyphLayout, 0, 310*2 - 20 -60- minusLesha*2);

        //moneyX = (384 + 32)  -currency.getWidth() - glyphLayout.width/2 - 4 -3;
        //game.menu0Font.draw(game.batch, glyphLayout,-(384 + 32) + glyphLayout.width/2 + currency.getY()*2+ currency.getWidth()*2+ 10,310*2 - 20 -60 ); //- currency.getY()+ currency.getWidth() + 10
        //game.menu0Font.draw(game.batch, glyphLayout, 0,0);
        game.batch.end();


        // Spites
        game.batch.begin();
        currency.setPosition(moneyX, moneyY);
        currency.draw(game.batch);
        //(384 + 32)/2 - 38
        //plusWhite.setPosition((384 + 32) + glyphLayout.width/2 + 4 +3,
        //        310*2 - 20 -60 -glyphLayout.height/2 - plusWhite.getHeight()/2 - minusLesha*2);

        // Currency BottomLeft Version
        //plusWhite.setPosition(((384 + 32)/2 - 38 + 76)*2 - plusWhite.getWidth()-4,
        //                310*2 - 20 -60 -glyphLayout.height/2 - plusWhite.getHeight()/2 - minusLesha*2);

        //Currency Center Version
        plusWhite.setPosition((384 + 32) + glyphLayout.width/2 + 4 +3,
                        310*2 - 20 -60 -glyphLayout.height/2 - plusWhite.getHeight()/2 - minusLesha*2);
        plusWhite.draw(game.batch);
        game.batch.end();

    }

    private void createButtons(){
        playTex = new TextureRegion(mainMenuAtlas.findRegion("mplay0"));
        worldTex =new TextureRegion(mainMenuAtlas.findRegion("world0"));
        playerTex =new TextureRegion(mainMenuAtlas.findRegion("mplayer0"));
        settingsTex =new TextureRegion(mainMenuAtlas.findRegion("settings0"));

        playPressedTex = new TextureRegion(mainMenuAtlas.findRegion("mplay0pressed"));
        worldPressedTex = new TextureRegion(mainMenuAtlas.findRegion("world0pressed"));
        playerPressedTex = new TextureRegion(mainMenuAtlas.findRegion("mplayer0pressed"));
        settingsPressedTex = new TextureRegion(mainMenuAtlas.findRegion("settings0pressed"));

        play = new ImageButton(new TextureRegionDrawable(playTex),
                new TextureRegionDrawable(playPressedTex),
                new TextureRegionDrawable(playTex));

        gameSkin = new ImageButton(new TextureRegionDrawable(worldTex),
                new TextureRegionDrawable(worldPressedTex),
                new TextureRegionDrawable(worldTex));

        plSkin = new ImageButton(new TextureRegionDrawable(playerTex),
                new TextureRegionDrawable(playerPressedTex),
                new TextureRegionDrawable(playerTex));

        settings = new ImageButton(new TextureRegionDrawable(settingsTex),
                new TextureRegionDrawable(settingsPressedTex),
                new TextureRegionDrawable(settingsTex));

        play.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //game.setScreen(game.gameScreen);
                if(game.goTutorial) {
                    game.setScreen(game.tutorialScreen);
                } else {
                    game.setScreen(game.gameScreen);
                }
                game.mainMenuScreen.pause();
            }
        });

        play.setPosition(64, buttonHeight);
        gameSkin.setPosition(play.getX() + play.getWidth() + space, buttonHeight);
        gameSkin.setPosition(64 + 155 + space, buttonHeight);
        plSkin.setPosition(gameSkin.getX() + gameSkin.getWidth() + space, buttonHeight);
        settings.setPosition(plSkin.getX() + plSkin.getWidth() + space, buttonHeight);

        stage.addActor(play);
        stage.addActor(gameSkin);
        stage.addActor(plSkin);
        stage.addActor(settings);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
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
