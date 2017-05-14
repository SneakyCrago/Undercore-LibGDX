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
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
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


    private int space = 32;
    private int buttonHeight = 32;

    private GlyphLayout glyphLayout;

    private Viewport viewport;

    public MainMenuScreen(Application game) {
        System.out.println();
        System.out.println("MainMenuScreen");
        this.game = game;
        this.camera = game.camera;

        glyphLayout = new GlyphLayout();

        viewport =new StretchViewport(Application.V_WIDTH*2,Application.V_HEIGHT*2);

        stage = new Stage(viewport, game.batch);

        viewport.apply();
        camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);
        Gdx.input.setInputProcessor(stage);


    }

    @Override
    public void show() {


        camera.setToOrtho(false,512, 310);
        game.batch.setProjectionMatrix(camera.combined);

        mainMenuAtlas = game.assetManager.get("textures/buttons/mainMenu.atlas");

        skull = new Sprite(mainMenuAtlas.findRegion("skull"));
        skull.setScale(0.75f);
        //384 + 32,0, 96, 310 black right board
        skull.setPosition((384 + 32)*2  + (192 - skull.getWidth())/2, 620/2 - skull.getHeight()/2);

        createButtons();

        Gdx.input.setInputProcessor(stage);

    }

    private int squardWidth = 256, squardHeight = 256-96, line = 2,
    scoreX =((384 + 32)/2 - squardWidth/2 + line)*2, scoreY = (310/2 + squardHeight - squardHeight/2 + 20 - line)*2 ;


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(18/255f,25/255f,26/255f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(Color.BLACK);
        game.shapeRenderer.rect(384 + 32,0, 96, 310 );
        game.shapeRenderer.rect((384 + 32)/2 - squardWidth/2, 310/2 - squardHeight/2 + 20, squardWidth, squardHeight);
        game.shapeRenderer.setColor(15/255f, 16/255f, 16/255f,1f);
        game.shapeRenderer.rect((384 + 32)/2 - squardWidth/2 + line, 310/2 - squardHeight/2 + 20 + line,
                squardWidth -line*2, squardHeight- line*2);
        game.shapeRenderer.end();

        stage.act(delta);
        stage.draw();

        game.batch.begin();

        skull.draw(game.batch);

        glyphLayout.setText(game.smallWhiteFont, "Score: "+ Score.getBestScore(), Color.WHITE ,(squardWidth - line*2)*2, Align.center, true);
        game.smallWhiteFont.draw(game.batch, glyphLayout, scoreX, scoreY - 5);


        //glyphLayout.setText(game.menu0Font, "Score: "+ Score.getBestScore(), Color.WHITE, 400, Align.bottomLeft, true);
        //game.menu0Font.draw(game.batch, glyphLayout, 0, 600);
        glyphLayout.setText(game.menu0Font, "Money: "+ Currency.currency, Color.WHITE, 400, Align.bottomLeft, true);
        game.menu0Font.draw(game.batch, glyphLayout, 0, 600-20);

        glyphLayout.setText(game.menuBig, "Normal", Color.WHITE, (384 + 32)* 2,Align.center, true);
        game.menuBig.draw(game.batch, glyphLayout, 0, 310*2 - 40);

        //Buttons Text
        glyphLayout.setText(game.menu0Font, "Play" , Color.WHITE, 85, Align.bottomLeft, true);
        game.menu0Font.draw(game.batch, glyphLayout, play.getX() + 75, play.getY() + play.getHeight()/2 + glyphLayout.height/2);
        glyphLayout.setText(game.menu0Font, "World" , Color.WHITE, 85, Align.bottomLeft, true);
        game.menu0Font.draw(game.batch, glyphLayout, gameSkin.getX() + 75, gameSkin.getY() + gameSkin.getHeight()/2+ glyphLayout.height/2);
        glyphLayout.setText(game.menu0Font, "Player" , Color.WHITE, 85, Align.bottomLeft, true);
        game.menu0Font.draw(game.batch, glyphLayout, plSkin.getX() + 75, plSkin.getY() + plSkin.getHeight()/2 + glyphLayout.height/2);
        glyphLayout.setText(game.menu0Font, "Settings" , Color.WHITE, 100, Align.bottomLeft, true);
        game.menu0Font.draw(game.batch, glyphLayout, settings.getX() + 60, settings.getY() + settings.getHeight()/2 + glyphLayout.height/2);

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
                game.setScreen(new GameScreen(game));
                game.mainMenuScreen.hide();
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
