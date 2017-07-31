package com.sneakycrago.undercore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sneakycrago.undercore.Application;
import com.sneakycrago.undercore.objects.Player;
import com.sneakycrago.undercore.utils.Currency;
import com.sneakycrago.undercore.utils.Globals;
import com.sneakycrago.undercore.utils.HardScore;
import com.sneakycrago.undercore.utils.Score;

import java.util.Random;

/**
 * Created by Sneaky Crago on 13.05.2017.
 */

public class MainMenuScreen implements Screen, InputProcessor {
    Application game;

    private OrthographicCamera camera;
    private OrthographicCamera spriteCamera;
    private Stage[] buttonsStage;
    //private Stage stage;

    private Button[] play, gameSkin, plSkin, settings,plusButton;
    private Button[] arrowLeft, arrowRight, records,achieve,saves;
    private TextButton[] enButton, ruButton;
    private Button[] priceButton, pricePlayer;
    private Slider[] volumeSlider;
    private Button[] normalMode, speedMode, survivalMode;

    //private TextureRegion arrowPressedTex,flipArrowPressedTex, flipArrowTex;
    private TextureRegion plusWhiteTex,recordsTex, achieveTex, savesTex ;

    private Sprite skull,skullShaded, currency, currencyPrice, lockPrice, skullUp, skullDown, plusSprite, volumeSprite,
                    skinPrewRandom, skinPrewHard;
    private Sprite[] skinPrew;

    private GlyphLayout glyphLayout;

    private Viewport viewport;
    private Viewport spriteViewport;

    private Player player;

    private int space = 32;
    private int buttonHeight = 32;
    private int squardWidth = 256, squardHeight = 256-96, line = 2,
            scoreX =((384 + 32)/2 - squardWidth/2 + line)*2, scoreY = (310/2 + squardHeight - squardHeight/2  - line)*2;
    private float moneyX, moneyY, moneyWidth;
    private int up = 5;
    private int minusLesha = 6;
    private float languageGlyphHeight;

    private boolean activePlay, activeWorld, activePlayerSkin, activeSettings;
    private boolean drawPlSkin, drawPlNew;
    //private boolean normalMode =true, hardMode = false;

    //private boolean showRandom = false;
    private Random random = new Random();

    public MainMenuScreen(Application game) {
        this.game = game;
        this.camera = game.camera;

        glyphLayout = new GlyphLayout();

        viewport =new StretchViewport(Application.V_WIDTH*2,Application.V_HEIGHT*2);

        buttonsStage = new Stage[Application.skinsAmount];
        //stage = new Stage(viewport, game.batch);
        for(int i=0; i<buttonsStage.length; i++){
            buttonsStage[i] = new Stage(viewport, game.batch);
        }
        player = new Player((384 + 32)/2 -16, 310/2 -16,game);

        viewport.apply();
        camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);

        spriteCamera = new OrthographicCamera();
        spriteCamera.setToOrtho(false, Application.V_WIDTH, Application.V_HEIGHT);
        spriteViewport = new StretchViewport(Application.V_WIDTH, Application.V_HEIGHT, spriteCamera);
        spriteViewport.apply();

        skull = new Sprite(game.fullA2.findRegion("skull1.0"));
        skull.setScale(0.4f);
        //384 + 32,0, 96, 310 black right board
        //skull.setPosition((384 + 32)*2  + (192 - skull.getWidth())/2, 620/2 - skull.getHeight()/2);
        skull.setPosition(1024- widthMods - modsBackRad- skull.getWidth()/2,
                modsStart*2+heightMods*4+spaceMods*4 +spaceMods+5+ heightMods-skull.getHeight()/2);

        skullShaded = new Sprite(game.fullA2.findRegion("skull1.1"));
        skullShaded.setScale(0.4f);
        skullShaded.setPosition(1024- widthMods - modsBackRad- skull.getWidth()/2,
                  modsStart*2+heightMods*4+spaceMods*4 +spaceMods+5+ heightMods-skull.getHeight()/2);
        currency = new Sprite(game.currencyTexture);
        float scale = 0.14f;
        currency.setSize(199*scale, 300*scale);

        currencyPrice = new Sprite(game.currencyTexture);
        scale = 0.07f;
        currencyPrice.setSize(199*scale, 300*scale);

        lockPrice = new Sprite(game.fullA2.findRegion("LockWhite"));
        lockPrice.setSize(20,20);

        plusWhiteTex = game.fullA2.findRegion("PlusWhite");

        plusSprite = new Sprite(game.fullA2.findRegion("PlusWhite"));
        plusSprite.setSize(16, 16);

        volumeSprite = new Sprite(game.fullA2.findRegion("Volume"));
        volumeSprite.setSize(96,96);


        skinPrew = new Sprite[5];
        for(int i=0; i < skinPrew.length; i++){
            skinPrew[i] = new Sprite(game.skinPrewTex[i]);
            skinPrew[i].setPosition((384 + 32) / 2 - squardWidth / 2 + line, 310 / 2 - squardHeight / 2 + up + line);
        }
        skinPrewRandom = new Sprite(game.skinPrewRandomTex);
        skinPrewRandom.setPosition((384 + 32) / 2 - squardWidth / 2 + line, 310 / 2 - squardHeight / 2 + up + line);
        skinPrewRandom.setSize(254,158);

        skinPrewHard = new Sprite(game.hardModeBackground);
        skinPrewHard.setPosition((384 + 32) / 2 - squardWidth / 2 + line, 310 / 2 - squardHeight / 2 + up + line);
        skinPrewHard.setSize(254,158);

        skinsSwitch = Application.gameSkin;

        //for(int i =0; i < priceButton.length; i++) {
        //    priceButton[i].setVisible(true);
        //}
    }

    @Override
    public void show() {
        camera.setToOrtho(false,512, 310);
        game.batch.setProjectionMatrix(camera.combined);

        this.activePlay = game.activePlay;
        activeWorld = false;
        activePlayerSkin = false;
        activeSettings = false;


        createButtons();
        createSkinButtons();
        createTextButtons();
        createSlider();

        //Gdx.input.setInputProcessor(buttonsStage[Application.gameSkin]);

        switch (Application.gameSkin){
            case 0: Gdx.input.setInputProcessor(buttonsStage[0]);
                break;
            case 1: Gdx.input.setInputProcessor(buttonsStage[1]);
                break;
            case 2: Gdx.input.setInputProcessor(buttonsStage[2]);
                break;
            case 3: Gdx.input.setInputProcessor(buttonsStage[3]);
                break;
            case 4: Gdx.input.setInputProcessor(buttonsStage[4]);
                break;
        }
        game.loadingScreen.dispose();


        if(activePlay){
            for(int i=0; i< enButton.length; i++){
                enButton[i].setVisible(false);
                ruButton[i].setVisible(false);
            }
        }
        if(activeWorld || activePlayerSkin ){
            for(int i =0;i < arrowLeft.length; i++){
                arrowLeft[i].setVisible(true);
                arrowRight[i].setVisible(true);
            }
        } else if(activePlay || activeSettings) {
            for (int i = 0; i < arrowLeft.length; i++) {
                arrowLeft[i].setVisible(false);
                arrowRight[i].setVisible(false);
            }
        }
        volumeSlider[0].setValue(Application.volume);
        volumeSlider[1].setValue(Application.volume);
        volumeSlider[2].setValue(Application.volume);
        volumeSlider[3].setValue(Application.volume);
        volumeSlider[4].setValue(Application.volume);

        //game.normalMode =true;
        //game.hardMode = false;
        //game.survivalMode = false;

        // Coins tip
        tipTimer=0;
        showCoinsCounter = 0;
        arrowsClicked = 0;

        player.initTextures();
        player.initHardTextures();
    }

    int langY = 40;



    @Override
    public void render(float delta) {
        game.adTimer();
        Gdx.gl.glClearColor(18/255f,25/255f,26/255f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(showNoAds) {
            Gdx.input.setInputProcessor(this);
        } else {
            switch (Application.gameSkin) {
                case 0:
                    Gdx.input.setInputProcessor(buttonsStage[0]);
                    break;
                case 1:
                    Gdx.input.setInputProcessor(buttonsStage[1]);
                    break;
                case 2:
                    Gdx.input.setInputProcessor(buttonsStage[2]);
                    break;
                case 3:
                    Gdx.input.setInputProcessor(buttonsStage[3]);
                    break;
                case 4:
                    Gdx.input.setInputProcessor(buttonsStage[4]);
                    break;
            }
        }
        Application.volume = volumeSlider[Application.gameSkin].getValue();
        for(int i=0; i < volumeSlider.length; i++) {
            volumeSlider[i].setPosition(volumeSprite.getX() + volumeSprite.getWidth(),
                    volumeSprite.getY() + volumeSprite.getHeight() / 2 - volumeSlider[i].getHeight() / 2);
        }
        spriteCamera.update();


        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        //Background
        //game.shapeRenderer.setColor(Color.BLACK);
        //game.shapeRenderer.rect(384 + 32,0, 96, 310);

        // Change Mod Buttons
        drawSurvivalMod(0);
        drawSpeedMod(heightMods+spaceMods);
        drawNormalMod(heightMods*2+spaceMods*2);

        game.shapeRenderer.end();

        // Speed Mod Lines
        if(!game.hardMode) {
            //Gdx.gl.glEnable(GL20.GL_BLEND);
            //Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            game.shapeRenderer.setColor(Color.DARK_GRAY);
            game.shapeRenderer.rect(512 - widthMods + 30 - 12, modsStart + heightMods + spaceMods + heightMods / 2 + 6 + 12, 30, lineMods * 2);
            game.shapeRenderer.rect(512 - widthMods + 30, modsStart + heightMods + spaceMods + heightMods / 2 + 6, 30, lineMods * 2);
            game.shapeRenderer.rect(512 - widthMods + 30 - 20, modsStart + heightMods + spaceMods + heightMods / 2 + 6 - 12, 30, lineMods * 2);
            game.shapeRenderer.end();
            //Gdx.gl.glDisable(GL20.GL_BLEND);
        } else{
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            game.shapeRenderer.setColor(Color.WHITE);
            game.shapeRenderer.rect(512 - widthMods + 30 - 12, modsStart + heightMods + spaceMods + heightMods / 2 + 6 + 12, 30, lineMods * 2);
            game.shapeRenderer.rect(512 - widthMods + 30, modsStart + heightMods + spaceMods + heightMods / 2 + 6, 30, lineMods * 2);
            game.shapeRenderer.rect(512 - widthMods + 30 - 20, modsStart + heightMods + spaceMods + heightMods / 2 + 6 - 12, 30, lineMods * 2);
            game.shapeRenderer.end();
        }


        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //Menu Center
        game.shapeRenderer.setColor(Color.BLACK);
        game.shapeRenderer.rect((384 + 32)/2 - squardWidth/2, 310/2 - squardHeight/2 + up , squardWidth, squardHeight);
        game.shapeRenderer.setColor(15/255f, 16/255f, 16/255f,1f);
        game.shapeRenderer.rect((384 + 32)/2 - squardWidth/2 + line, 310/2 - squardHeight/2  + up + line,
                squardWidth -line*2, squardHeight- line*2);

        if(activeSettings){
            //Slider
            game.shapeRenderer.setColor(Color.BLACK);
            game.shapeRenderer.rect(volumeSlider[Application.gameSkin].getX() / 2 - 10, volumeSlider[Application.gameSkin].getY() / 2,
                    volumeSlider[Application.gameSkin].getWidth() / 2 + 10, volumeSlider[Application.gameSkin].getHeight() / 2);
            game.shapeRenderer.circle(volumeSlider[Application.gameSkin].getX() / 2 + volumeSlider[Application.gameSkin].getWidth() / 2 + 10 - volumeSlider[Application.gameSkin].getHeight() / 4,
                    volumeSlider[Application.gameSkin].getY() / 2 + volumeSlider[Application.gameSkin].getHeight() / 4,
                    volumeSlider[Application.gameSkin].getHeight() / 4, 64);
            switch (Application.gameSkin) {
                case 0:
                    game.shapeRenderer.setColor(Globals.SidesColor);
                    break;
                case 1:
                    game.shapeRenderer.setColor(Globals.Background1Color);
                    break;
                case 2:
                    game.shapeRenderer.setColor(Globals.Inner2Color);
                    break;
                case 3:
                    game.shapeRenderer.setColor(Globals.Background3Color);
                    break;
                case 4:
                    game.shapeRenderer.setColor(Globals.Background4Color);
                    break;
            }
            game.shapeRenderer.rect(volumeSlider[Application.gameSkin].getX() / 2 - 10, volumeSlider[Application.gameSkin].getY() / 2 + 3,
                    10, volumeSlider[Application.gameSkin].getHeight() / 2 - 6);
            game.shapeRenderer.rect(volumeSlider[Application.gameSkin].getX() / 2, volumeSlider[Application.gameSkin].getY() / 2 + 3,
                    Application.volume * (volumeSlider[Application.gameSkin].getWidth() / 2), volumeSlider[Application.gameSkin].getHeight() / 2 - 6);

            game.shapeRenderer.circle(volumeSlider[Application.gameSkin].getX() / 2 + Application.volume * (volumeSlider[Application.gameSkin].getWidth() / 2),
                    volumeSlider[Application.gameSkin].getY() / 2 + volumeSlider[Application.gameSkin].getHeight() / 4,
                    volumeSlider[Application.gameSkin].getHeight() / 4 - 3, 64);

        }

        if(activePlay){
            for(int i=0; i< enButton.length; i++){
                enButton[i].setVisible(false);
                ruButton[i].setVisible(false);
            }
        }

        if(activePlayerSkin && drawPlSkin ){
            if(game.normalMode) {
                switch (Application.gameSkin) {
                    case 0:
                        game.shapeRenderer.setColor(Color.BLACK);
                        break;
                    case 1:
                        game.shapeRenderer.setColor(Globals.Background1Color);
                        break;
                    case 2:
                        game.shapeRenderer.setColor(Globals.Background2Color);
                        break;
                    case 3:
                        game.shapeRenderer.setColor(Globals.Background3Color);
                        break;
                    case 4:
                        game.shapeRenderer.setColor(Globals.Background4Color);
                        break;
                }
            } else if(game.hardMode) {
                game.shapeRenderer.setColor(Color.BLACK);
            }
            //game.shapeRenderer.circle(player.getPlayerRectangle().getX() + 16, player.getPlayerRectangle().getY() + 16, 48, 64);
            game.shapeRenderer.rect((384 + 32)/2 - squardWidth/2 + line, 310/2 - squardHeight/2  + up + line,
                    squardWidth -line*2, squardHeight- line*2);
        }
        game.shapeRenderer.setColor(15/255f, 16/255f, 16/255f,1f);
        game.shapeRenderer.rect(moneyX/2 -2 +3,moneyY/2 +3,currency.getWidth()/2 + moneyWidth/2 + 7 + plusSprite.getWidth()/2 +4,16);

        if(activePlayerSkin){
            if(Application.playerSkin == 0 && game.normalMode) {
            player.drawPlayerCube(game.shapeRenderer);
            } else if(Application.playerSkinHard == 0 && game.hardMode) {
                player.drawPlayerCube(game.shapeRenderer);
            }
        }
        game.shapeRenderer.end();


        game.batch.setProjectionMatrix(spriteCamera.combined);
        game.batch.begin();

        if(activePlay || activeWorld){
            if(game.normalMode) {
                if (!game.showRandom) {
                    skinPrew[Application.gameSkin].draw(game.batch);
                } else if (game.showRandom) {
                    skinPrewRandom.draw(game.batch);
                }
            } else if(game.hardMode) {
                skinPrewHard.draw(game.batch);
            }
        }
        /*
        if(activeWorld){
            if(!game.showRandom) {
                skinPrew[Application.gameSkin].draw(game.batch);
            } else if (game.showRandom) {
                skinPrewRandom.draw(game.batch);
            }
        } */
        //draw Player Skins
        if(activePlayerSkin ){
        if(game.normalMode) {
            if (Application.playerSkin != 0 )
                player.drawPlayerSprite(game.batch);
            if (Application.playerSkin != 3 )
                player.inMenuAnimation(game.batch, delta, game);
        } else if(game.hardMode) {
            if (Application.playerSkinHard == 1)
                player.drawPlayerSprite(game.batch);
            if ( Application.playerSkinHard == 1 || Application.playerSkinHard == 0)
                player.inMenuAnimation(game.batch, delta, game);
        }
        }

        game.batch.end();



        if(activeWorld && game.normalMode ) {
            if(Application.skinLocked[Application.gameSkin] && !game.showRandom) {
                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                // Alpha
                game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                game.shapeRenderer.setColor(19 / 255f, 22 / 255f, 22 / 255f, 0.6f);
                game.shapeRenderer.rect((384 + 32) / 2 - squardWidth / 2 + line, 310 / 2 - squardHeight / 2 + up + line,
                        squardWidth - line * 2, 16);
                game.shapeRenderer.end();
                Gdx.gl.glDisable(GL20.GL_BLEND);
            }
        } else if (activeSettings || activePlayerSkin){

        }

        //System.out.println(Application.openMoon);
        //System.out.println(Application.playerSkinHard);
        // Player Skin shaded Rect
        if(activePlayerSkin && game.normalMode &&
                ((Application.playerSkin == 2 && !Application.openDummy) || (Application.playerSkin == 3 && !Application.openArrow))) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            // Alpha
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            game.shapeRenderer.setColor(19 / 255f, 22 / 255f, 22 / 255f, 0.6f);
            game.shapeRenderer.rect((384 + 32) / 2 - squardWidth / 2 + line, 310 / 2 - squardHeight / 2 + up + line,
                    squardWidth - line * 2, 16);
            game.shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }


        buttonsStage[Application.gameSkin].act(delta);
        buttonsStage[Application.gameSkin].draw();

        game.batch.begin();
        if(game.normalMode) {
            skull.draw(game.batch);
        } else {
            skullShaded.draw(game.batch);
        }
        /*if(game.normalMode) {
            skull.draw(game.batch);
            skullUp.draw(game.batch);
            skullDown.draw(game.batch);
        } else if(game.hardMode || game.survivalMode) {
            for (int i = 0; i < skullInsane.length; i++) {
                skullInsane[i].draw(game.batch);
            }
        } */
        if(activePlay) {
            if(game.normalMode) {
                glyphLayout.setText(game.menuScoreFont, "" + Score.getBestScore(), Color.WHITE, (squardWidth - line * 2) * 2, Align.center, true);
                game.menuScoreFont.draw(game.batch, glyphLayout, scoreX, scoreY - 20);
            } else if(game.hardMode) {
                glyphLayout.setText(game.menuScoreFont, "" + HardScore.getBestScore(), Color.WHITE, (squardWidth - line * 2) * 2, Align.center, true);
                game.menuScoreFont.draw(game.batch, glyphLayout, scoreX, scoreY - 20);
            }
        } /*else{
            for(int i=0; i < priceButton.length; i++) {
                priceButton[i].setVisible(false);
            }
        } */
        if(activeWorld) {
            if(game.normalMode) {
                glyphLayout.setText(game.menuScoreFont, "" + Score.getBestScore(), Color.WHITE, (squardWidth - line * 2) * 2, Align.center, true);
                game.menuScoreFont.draw(game.batch, glyphLayout, scoreX, scoreY - 20);
            } else if(game.hardMode) {
                glyphLayout.setText(game.menuScoreFont, "" + HardScore.getBestScore(), Color.WHITE, (squardWidth - line * 2) * 2, Align.center, true);
                game.menuScoreFont.draw(game.batch, glyphLayout, scoreX, scoreY - 20);
            }
        }
        if(activeWorld) {
            for (int i = 0; i < arrowLeft.length; i++) {
                arrowLeft[i].setVisible(true);
                arrowRight[i].setVisible(true);
            }
        }

        if(activeWorld) {
            if(game.normalMode) {
                if (Application.skinLocked[Application.gameSkin])
                    for (int i = 0; i < priceButton.length; i++) {
                        priceButton[i].setVisible(true);
                    }
            }
        } else if(game.hardMode || (activeWorld && game.hardMode)){
            for(int i=0; i < priceButton.length; i++) {
                priceButton[i].setVisible(false);
            }
        }
        if(activePlayerSkin && game.normalMode) {
            if(Application.playerSkin ==2 && !Application.openDummy) {
                for (int i = 0; i < pricePlayer.length; i++) {
                    pricePlayer[i].setVisible(true);
                }
            }
            if(Application.playerSkin ==3 && !Application.openArrow) {
                for (int i = 0; i < pricePlayer.length; i++) {
                    pricePlayer[i].setVisible(true);
                }
            }
        } else {
            for (int i = 0; i < pricePlayer.length; i++) {
                pricePlayer[i].setVisible(false);
            }
        }
        /*
        } else if(activeWorld&& hardMode) {
            for(int i =0;i < arrowLeft.length; i++){
                arrowLeft[i].setVisible(false);
                arrowRight[i].setVisible(false);
            }
        } */
        if(activePlayerSkin ){
            for(int i =0;i < arrowLeft.length; i++){
                arrowLeft[i].setVisible(true);
                arrowRight[i].setVisible(true);
            }
        }
        if(activePlay || activeSettings){
            for(int i =0;i < arrowLeft.length; i++){
                arrowLeft[i].setVisible(false);
                arrowRight[i].setVisible(false);
            }
        }

        if(activeSettings) {
                enButton[Application.gameSkin].setVisible(true);
                ruButton[Application.gameSkin].setVisible(true);

            volumeSlider[Application.gameSkin].setVisible(true);
            if(game.en ) {
                glyphLayout.setText(game.menuBig, "Settings", Color.WHITE, (384 + 32) * 2, Align.center, true);
                game.menuBig.draw(game.batch, glyphLayout, 0, 310 * 2 - 20);
                glyphLayout.setText(game.tutFont, "Language", Color.WHITE, squardWidth * 2, Align.center, true);
                game.tutFont.draw(game.batch, glyphLayout, (384 + 32) - squardWidth,
                        310 + squardHeight / 2 + up * 2 - glyphLayout.height / 2 -langY);
            } else if(game.ru && activeSettings){
                glyphLayout.setText(game.menuBigRu, "Настройки", Color.WHITE, (384 + 32) * 2, Align.center, true);
                game.menuBigRu.draw(game.batch, glyphLayout, 0, 310 * 2 - 20);

                glyphLayout.setText(game.tutFontRu, "Язык", Color.WHITE, squardWidth * 2, Align.center, true);
                game.tutFontRu.draw(game.batch, glyphLayout, (384 + 32) - squardWidth,
                        310 + squardHeight / 2 + up * 2 - glyphLayout.height / 2 -langY);
            }



        } else {
            enButton[Application.gameSkin].setVisible(false);
            ruButton[Application.gameSkin].setVisible(false);

            volumeSlider[Application.gameSkin].setVisible(false);
            if(game.en) {
                if(game.normalMode) {
                    glyphLayout.setText(game.menuBig, "Normal", Color.WHITE, (384 + 32) * 2, Align.center, true);
                    game.menuBig.draw(game.batch, glyphLayout, 0, 310 * 2 - 20);
                }
                languageGlyphHeight = glyphLayout.height;
            } else if(game.ru && !activeSettings){
                if(game.normalMode){
                    glyphLayout.setText(game.menuBigRu, "Обычный", Color.WHITE, (384 + 32) * 2, Align.center, true);
                    game.menuBigRu.draw(game.batch, glyphLayout, 0, 310 * 2 - 20);
                }
                languageGlyphHeight = glyphLayout.height;
            }
        }

        if(game.hardMode && !activeSettings){
            if(game.en) {
                glyphLayout.setText(game.menuBig, "Fast", Color.WHITE, (384 + 32) * 2, Align.center, true);
                game.menuBig.draw(game.batch, glyphLayout, 0, 310 * 2 - 20);

            }
            if(game.ru) {
                glyphLayout.setText(game.menuBigRu, "Быстрый", Color.WHITE, (384 + 32) * 2, Align.center, true);
                game.menuBigRu.draw(game.batch, glyphLayout, 0, 310 * 2 - 20);

            }
        }
        if(game.survivalMode && !activeSettings){
            if(game.en) {
                glyphLayout.setText(game.menuBig, "Survival", Color.WHITE, (384 + 32) * 2, Align.center, true);
                game.menuBig.draw(game.batch, glyphLayout, 0, 310 * 2 - 20);

            }
            if(game.ru) {
                glyphLayout.setText(game.menuBigRu, "Выживание", Color.WHITE, (384 + 32) * 2, Align.center, true);
                game.menuBigRu.draw(game.batch, glyphLayout, 0, 310 * 2 - 20);

            }
        }

        if(activePlayerSkin ){
            if(drawPlNew) {
                if(game.en) {
                    glyphLayout.setText(game.tutFont, "New skins will be available soon", Color.WHITE, squardWidth * 2, Align.center, true);
                    game.tutFont.draw(game.batch, glyphLayout, (384 + 32) - squardWidth, 310 + up * 2 + glyphLayout.height / 2);
                } else if(game.ru){
                    glyphLayout.setText(game.tutFontRu, "Новые скины будут скоро доступны", Color.WHITE, squardWidth * 2, Align.center, true);
                    game.tutFontRu.draw(game.batch, glyphLayout, (384 + 32) - squardWidth, 310 + up * 2 + glyphLayout.height / 2);
                }
            }
        }
        //Buttons Text
        if(game.en) {
            glyphLayout.setText(game.menu0Font, "Play", Color.WHITE, 85, Align.bottomLeft, true);
            game.menu0Font.draw(game.batch, glyphLayout, play[0].getX() + 75,
                    play[0].getY() + play[0].getHeight() / 2 + glyphLayout.height / 2);
            glyphLayout.setText(game.menu0Font, "World", Color.WHITE, 85, Align.bottomLeft, true);
            game.menu0Font.draw(game.batch, glyphLayout, gameSkin[0].getX() + 75,
                    gameSkin[0].getY() + gameSkin[0].getHeight() / 2 + glyphLayout.height / 2);
            glyphLayout.setText(game.menu0Font, "Player", Color.WHITE, 85, Align.bottomLeft, true);
            game.menu0Font.draw(game.batch, glyphLayout, plSkin[0].getX() + 75,
                    plSkin[0].getY() + plSkin[0].getHeight() / 2 + glyphLayout.height / 2);
            glyphLayout.setText(game.menu0Font, "Settings", Color.WHITE, 100, Align.bottomLeft, true);
            game.menu0Font.draw(game.batch, glyphLayout, settings[0].getX() + 60,
                    settings[0].getY() + settings[0].getHeight() / 2 + glyphLayout.height / 2);

            // Mods
            glyphLayout.setText(game.smallWhiteGray, "Coming soon", Color.WHITE, widthMods*2+ modsBackRad*2, Align.center, true);
            game.smallWhiteGray.draw(game.batch, glyphLayout, 1024- widthMods*2 - modsBackRad*2,
                    modsStart*2 +lineMods*2 +modsStart+ glyphLayout.height);

            if(game.hardMode) {
                glyphLayout.setText(game.smallWhiteFont, "Fast", Color.WHITE, widthMods*2 + modsBackRad*2, Align.center, true);
                game.smallWhiteFont.draw(game.batch, glyphLayout, 1024- widthMods*2 - modsBackRad*2,
                    modsStart*2 +lineMods*2+ heightMods*2+spaceMods*2+spaceMods+ glyphLayout.height);
            } else {
                glyphLayout.setText(game.smallWhiteGray, "Fast", Color.WHITE, widthMods*2 + modsBackRad*2, Align.center, true);
                game.smallWhiteGray.draw(game.batch, glyphLayout, 1024- widthMods*2 - modsBackRad*2,
                        modsStart*2 +lineMods*2+ heightMods*2+spaceMods*2+spaceMods+ glyphLayout.height);
            }
            if(game.normalMode) {
                glyphLayout.setText(game.smallWhiteFont, "Normal", Color.WHITE, widthMods * 2 + modsBackRad * 2, Align.center, true);
                game.smallWhiteFont.draw(game.batch, glyphLayout, 1024 - widthMods * 2 - modsBackRad * 2,
                        modsStart * 2 + lineMods * 2 + heightMods * 4 + spaceMods * 4 + spaceMods + glyphLayout.height);
            } else {
                glyphLayout.setText(game.smallWhiteGray, "Normal", Color.WHITE, widthMods * 2 + modsBackRad * 2, Align.center, true);
                game.smallWhiteGray.draw(game.batch, glyphLayout, 1024 - widthMods * 2 - modsBackRad * 2,
                        modsStart * 2 + lineMods * 2 + heightMods * 4 + spaceMods * 4 + spaceMods + glyphLayout.height);
            }

        } else if(game.ru){
            glyphLayout.setText(game.menu0FontRu, "Играть", Color.WHITE, 85, Align.bottomLeft, true);
            game.menu0FontRu.draw(game.batch, glyphLayout, play[0].getX() + 75 -4,
                    play[0].getY() + play[0].getHeight() / 2 + glyphLayout.height / 2);
            glyphLayout.setText(game.menu0FontRu, "Мир", Color.WHITE, 85, Align.bottomLeft, true);
            game.menu0FontRu.draw(game.batch, glyphLayout, gameSkin[0].getX() + 75 + 10,
                    gameSkin[0].getY() + gameSkin[0].getHeight() / 2 + glyphLayout.height / 2);
            glyphLayout.setText(game.menu0FontRu, "Игрок", Color.WHITE, 85, Align.bottomLeft, true);
            game.menu0FontRu.draw(game.batch, glyphLayout, plSkin[0].getX() + 75,
                    plSkin[0].getY() + plSkin[0].getHeight() / 2 + glyphLayout.height / 2);
            glyphLayout.setText(game.settingsFontRU, "Настройки", Color.WHITE, 100, Align.bottomLeft, true);
            game.settingsFontRU.draw(game.batch, glyphLayout, settings[0].getX() + 60 -5,
                    settings[0].getY() + settings[0].getHeight() / 2 + glyphLayout.height / 2);

            // Mods Buttons
            // Скоро в игре
            glyphLayout.setText(game.soonGrayFontRU, " Скоро \nв игре", Color.WHITE, widthMods*2+ modsBackRad*2, Align.center, true);
            game.soonGrayFontRU.draw(game.batch, glyphLayout, 1024- widthMods*2 - modsBackRad*2,
                    modsStart*2 +lineMods*2 +modsStart+ glyphLayout.height);

            if(game.hardMode) {
                glyphLayout.setText(game.menu0FontRu, "Быстрый", Color.WHITE, widthMods * 2 + modsBackRad * 2, Align.center, true);
                game.menu0FontRu.draw(game.batch, glyphLayout, 1024 - widthMods * 2 - modsBackRad * 2,
                        modsStart * 2 + lineMods * 2 + heightMods * 2 + spaceMods * 2 + spaceMods + glyphLayout.height);
            } else {
                glyphLayout.setText(game.menu0FontGrayRu, "Быстрый", Color.WHITE, widthMods * 2 + modsBackRad * 2, Align.center, true);
                game.menu0FontGrayRu.draw(game.batch, glyphLayout, 1024 - widthMods * 2 - modsBackRad * 2,
                        modsStart * 2 + lineMods * 2 + heightMods * 2 + spaceMods * 2 + spaceMods + glyphLayout.height);
            }

            if(game.normalMode) {
                glyphLayout.setText(game.menu0FontRu, "Обычный", Color.WHITE, widthMods * 2 + modsBackRad * 2, Align.center, true);
                game.menu0FontRu.draw(game.batch, glyphLayout, 1024 - widthMods * 2 - modsBackRad * 2,
                        modsStart * 2 + lineMods * 2 + heightMods * 4 + spaceMods * 4 + spaceMods + glyphLayout.height);
            } else {
                glyphLayout.setText(game.menu0FontGrayRu, "Обычный", Color.WHITE, widthMods * 2 + modsBackRad * 2, Align.center, true);
                game.menu0FontGrayRu.draw(game.batch, glyphLayout, 1024 - widthMods * 2 - modsBackRad * 2,
                        modsStart * 2 + lineMods * 2 + heightMods * 4 + spaceMods * 4 + spaceMods + glyphLayout.height);
            }
        }

        // Money
        glyphLayout.setText(game.menu0Font, ""+Currency.currency, Color.WHITE, (384 + 32)* 2, Align.center, true);
        moneyX = (384 + 32) -currency.getWidth() - glyphLayout.width/2 - 4 -3;
        moneyY = 310*2 - 20 -60-currency.getHeight()/2 - glyphLayout.height/2 - minusLesha*2;
        moneyWidth = glyphLayout.width;
        game.menu0Font.draw(game.batch, glyphLayout, 0, 310*2 - 20 -60- minusLesha*2);

        // Spites
        currency.setPosition(moneyX, moneyY);
        currency.draw(game.batch);


        plusSprite.setPosition(moneyX+ moneyWidth + 45,moneyY +6 + 16 - plusSprite.getHeight()/2);
        plusSprite.draw(game.batch);

        //Price, Lock and Currency for Shop
        if(activeWorld && !game.showRandom && game.normalMode) {
            glyphLayout.setText(game.menu0Font, "" + Application.price, Color.WHITE, (384 + 32) * 2, Align.center, true);
            if(Application.gameSkin != 0) {
                if(Application.skinLocked[Application.gameSkin]) {
                    game.menu0Font.draw(game.batch, glyphLayout, 0,
                            310 - squardHeight + up * 2 + line * 2 + glyphLayout.height + 8);//+ up+ line*2 + 8
                }
            }
            priceX= (384 + 32);
            priceY = 310 - squardHeight + up * 2 + glyphLayout.height + line * 2 + 16 - glyphLayout.height / 2;

            currencyPrice.setPosition((384 + 32) - glyphLayout.width / 2 - currencyPrice.getWidth() - 4,
                    310 - squardHeight + up * 2 + line * 2 + 16 - glyphLayout.height / 2 - 2);

            lockPrice.setPosition((384 + 32) + glyphLayout.width / 2 + 4,
                    310 - squardHeight + up * 2 + line * 2 + 6);
            if(Application.gameSkin == 1 || Application.gameSkin == 2 || Application.gameSkin ==3|| Application.gameSkin ==4 ) {
                if(Application.skinLocked[Application.gameSkin] && !game.showRandom) {
                    currencyPrice.draw(game.batch);
                    lockPrice.draw(game.batch);
                }
            }
        }
        //Price, Lock and Currency for Shop
        if(activePlayerSkin && game.normalMode && (Application.playerSkin ==2 || Application.playerSkin==3)) {
            if(Application.playerSkin ==2) {
                glyphLayout.setText(game.menu0Font, "" + Application.dummyPrice, Color.WHITE, (384 + 32) * 2, Align.center, true);
            }
            if(Application.playerSkin == 3){
                glyphLayout.setText(game.menu0Font, "" + Application.arrowPrice, Color.WHITE, (384 + 32) * 2, Align.center, true);

            }


            if((!Application.openDummy && Application.playerSkin == 2) || (!Application.openArrow && Application.playerSkin ==3)){
                game.menu0Font.draw(game.batch, glyphLayout, 0,
                            310 - squardHeight + up * 2 + line * 2 + glyphLayout.height + 8);//+ up+ line*2 + 8
            }
            if(Application.playerSkin == 1 && !Application.openMoon) {
                //if(game.en) {
                    game.menu0Font.draw(game.batch, glyphLayout, 0,
                            310 - squardHeight + up * 2 + line * 2 + glyphLayout.height + 8);//+ up+ line*2 + 8
                //} else if(game.ru) {
                //    game.menu0FontRu.draw(game.batch, glyphLayout, 0,
                //            310 - squardHeight + up * 2 + line * 2 + glyphLayout.height + 8);//+ up+ line*2 + 8
                //}
            }

            priceX= (384 + 32);
            priceY = 310 - squardHeight + up * 2 + glyphLayout.height + line * 2 + 16 - glyphLayout.height / 2;

            currencyPrice.setPosition((384 + 32) - glyphLayout.width / 2 - currencyPrice.getWidth() - 4,
                    310 - squardHeight + up * 2 + line * 2 + 16 - glyphLayout.height / 2 - 2);

            lockPrice.setPosition((384 + 32) + glyphLayout.width / 2 + 4,
                    310 - squardHeight + up * 2 + line * 2 + 6);

                if((!Application.openDummy && Application.playerSkin == 2) || (!Application.openArrow && Application.playerSkin ==3)) {
                    currencyPrice.draw(game.batch);
                    lockPrice.draw(game.batch);
                }
        }
        if(activePlayerSkin && ((game.normalMode && Application.playerSkin ==1) || (game.hardMode && Application.playerSkinHard == 1)) ) {
            if (!Application.openMoon) {
                if(game.en) {
                glyphLayout.setText(game.menu0border, "Can be unlocked at Fast mode" , Color.WHITE, (384 + 32) * 2, Align.center, true);
                } else if(game.ru) {
                    glyphLayout.setText(game.menu0borderRu, "Можно получить в Быстром режиме" , Color.WHITE, (384 + 32) * 2, Align.center, true);
                }
                if(game.en) {
                game.menu0border.draw(game.batch, glyphLayout, 0,
                        310 - squardHeight + up * 2 + line * 2 + glyphLayout.height + 8 + 10);//+ up+ line*2 + 8
                } else if(game.ru) {
                    game.menu0borderRu.draw(game.batch, glyphLayout, 0,
                            310 - squardHeight + up * 2 + line * 2 + glyphLayout.height + 8 + 10);//+ up+ line*2 + 8
                }
            }
        }

        if(activeSettings){
            volumeSprite.setPosition((384 + 32) - squardWidth + 64,
                    310 + squardHeight/2 + up*2 - volumeSprite.getHeight()/2);
            volumeSprite.draw(game.batch);
        }

        // test
        if(game.test) {
            glyphLayout.setText(game.smallWhiteFont, "" + Application.loadedMoney, Color.WHITE, 100, Align.bottomLeft, true);

            game.smallWhiteFont.draw(game.batch, glyphLayout, 0, 30);
            glyphLayout.setText(game.smallWhiteFont, "GPGS:" + game.gpgsController.isSignedIn(), Color.WHITE, 300, Align.bottomLeft, true);
            game.smallWhiteFont.draw(game.batch, glyphLayout, 0, 310 * 2);

            glyphLayout.setText(game.smallWhiteFont, "skins:" + game.skinsOpened, Color.WHITE, 300, Align.bottomLeft, true);
            game.smallWhiteFont.draw(game.batch, glyphLayout, 200, 310 * 2);
        }
       showAdTip();

        game.batch.end();


        for(int i=0; i< plusButton.length; i++) {
            plusButton[i].setSize(currency.getWidth() + moneyWidth + 14 + plusSprite.getWidth() +8 , 32);
            plusButton[i].setPosition(moneyX +4,moneyY +6);
        }
        if(showNoAds) {
            showNoAds();
        }
        if(game.android) {
            game.adsController.isRewardedMoneyLoaded();
        }

        game.checkSkinAchievments();
    }
    private float tipTimer;
    private float showTime = 0.8f;
    private float showCoinsCounter = 0;
    private int arrowsClicked = 0;
    // Watch Ad
    private void showAdTip(){
        if(game.test) {
            Application.loadedMoney = true;
        }
        if(Application.loadedMoney) {
            if(showCoinsCounter < 5 && arrowsClicked >= 2 && Score.bestScore >=2) {
                tipTimer += Gdx.graphics.getDeltaTime();
                if (tipTimer <= showTime) {
                    glyphLayout.setText(game.menu0FontGray, "< Get coins", Color.WHITE, 400, Align.bottomLeft, true);
                    game.menu0FontGray.draw(game.batch, glyphLayout, moneyX + 10 +
                            (currency.getWidth() / 2 + moneyWidth / 2 + 7 + plusSprite.getWidth() / 2 + 4) * 2, moneyY + 16 * 2);
                    glyphLayout.setText(game.menu0FontGray, "Get coins >", Color.WHITE, 400, Align.bottomLeft, true);
                    game.menu0FontGray.draw(game.batch, glyphLayout, moneyX - 10 - glyphLayout.width, moneyY + 16 * 2);
                } else if( tipTimer >= showTime+ showTime/1.5f){
                    tipTimer = 0;
                    showCoinsCounter++;
                }
            //game.menu0Font.draw(game.batch, glyphLayout, moneyX + 10 +
            //        (currency.getWidth()/2 + moneyWidth/2 + 7 + plusSprite.getWidth()/2 +4)*2,moneyY+16*2);
            }
        }
    }

    //Change Mod buttons
    private void changeColorActive(){
        switch (Application.gameSkin){
            case 0: game.shapeRenderer.setColor(0f, 224/255f, 227/255f, 1f); //0,224,227
                break;
            case 1:  game.shapeRenderer.setColor(160/255f,210/255f,236/255f, 1f); //160,210,236
                break;
            case 2:  game.shapeRenderer.setColor(77/255f,151/255f,55/255f, 1f); //77,151,55
                break;
            case 3:  game.shapeRenderer.setColor(Globals.Background3Color); //255,255,255 +
                break;
            case 4:  game.shapeRenderer.setColor(255/255f,167/255f,179/255f,1f); //255,167,179
                break;
        }
    }
    private void changeColorInactive(){
        switch (Application.gameSkin) {
            case 0: game.shapeRenderer.setColor(0f,105/255f,109/255f,1f); //0,105,109
                break;
            case 1: game.shapeRenderer.setColor(50/255f, 92/255f, 118/255f, 1f); //50,92,118
                break;
            case 2: game.shapeRenderer.setColor(10/255f,44/255f,5/255f, 1f); //10,44,5
                break;
            case 3: game.shapeRenderer.setColor(139/255f,139/255f,140/255f, 1f); //139,139,140
                break;
            case 4: game.shapeRenderer.setColor(139/255f,55/255f,65/255f,1f); //139,55,65
                break;
        }
    }

    private void drawBackgroundMod(){
        game.shapeRenderer.setColor(10/255f, 13/255f,14/255f, 1f);
    }

    private int lineMods = 2;
    private int spaceMods = 10;
    private int heightMods = 70;
    private int widthMods = 70;
    private int modsStart = 40; // modsStart(начало) + Y(на сколько поднимаем)
    private int modsBackRad = 8;

    private void drawSurvivalMod(int y){
        //ChangeMod Buttons
        //game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(Globals.MenuButtonsBackground);


        changeColorInactive();
        game.shapeRenderer.rect(512 -widthMods,y+modsStart , widthMods, lineMods);
        game.shapeRenderer.rect(512 -widthMods,y+modsStart + heightMods-lineMods, widthMods, lineMods);
        game.shapeRenderer.circle(512-widthMods,y+modsStart +modsBackRad, modsBackRad, 16);
        game.shapeRenderer.circle(512-widthMods,y+modsStart +heightMods-modsBackRad, modsBackRad, 16);

        //Draw Background
        drawBackgroundMod();
        game.shapeRenderer.circle(512-widthMods,y+modsStart +modsBackRad, modsBackRad-line, 16);
        game.shapeRenderer.rect(512 -widthMods - modsBackRad,y+ modsStart+modsBackRad, modsBackRad, heightMods- modsBackRad*2);
        game.shapeRenderer.circle(512-widthMods,y+modsStart +heightMods-modsBackRad, modsBackRad-line, 16);
        game.shapeRenderer.rect(512 -widthMods,y+ modsStart+lineMods, widthMods, heightMods-lineMods*2);

        // Draw LeftLine
        changeColorInactive();
        game.shapeRenderer.rect(512-widthMods- modsBackRad,y+modsStart +modsBackRad, lineMods, heightMods - modsBackRad*2);

    }
    private void drawSpeedMod(int y){
        //ChangeMod Buttons
        //game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(Globals.MenuButtonsBackground);

        // Draw Lines
        if(game.hardMode) {
            changeColorActive();
        } else {
            changeColorInactive();
        }
        game.shapeRenderer.rect(512 -widthMods,y+modsStart , widthMods, lineMods);
        game.shapeRenderer.rect(512 -widthMods,y+modsStart + heightMods-lineMods, widthMods, lineMods);
        game.shapeRenderer.circle(512-widthMods,y+modsStart +modsBackRad, modsBackRad, 16);
        game.shapeRenderer.circle(512-widthMods,y+modsStart +heightMods-modsBackRad, modsBackRad, 16);

        //Draw Background
        if(!game.hardMode)
            drawBackgroundMod();
        else
            game.shapeRenderer.setColor(Color.BLACK);
        game.shapeRenderer.circle(512-widthMods,y+modsStart +modsBackRad, modsBackRad-line, 16);
        game.shapeRenderer.rect(512 -widthMods - modsBackRad,y+ modsStart+modsBackRad, modsBackRad, heightMods- modsBackRad*2);
        game.shapeRenderer.circle(512-widthMods,y+modsStart +heightMods-modsBackRad, modsBackRad-line, 16);
        game.shapeRenderer.rect(512 -widthMods,y+ modsStart+lineMods, widthMods, heightMods-lineMods*2);

        // Draw LeftLine
        if(game.hardMode) {
            changeColorActive();
        } else {
            changeColorInactive();
        }
        game.shapeRenderer.rect(512-widthMods- modsBackRad,y+modsStart +modsBackRad, lineMods, heightMods - modsBackRad*2);

        //game.shapeRenderer.end();
    }
    private void drawNormalMod(int y){
        //ChangeMod Buttons
       // game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(Globals.MenuButtonsBackground);

        // Draw Lines
        if(game.normalMode) {
            changeColorActive();
        } else {
            changeColorInactive();
        }
        game.shapeRenderer.rect(512 -widthMods,y+modsStart , widthMods, lineMods);
        game.shapeRenderer.rect(512 -widthMods,y+modsStart + heightMods-lineMods, widthMods, lineMods);
        game.shapeRenderer.circle(512-widthMods,y+modsStart +modsBackRad, modsBackRad, 16);
        game.shapeRenderer.circle(512-widthMods,y+modsStart +heightMods-modsBackRad, modsBackRad, 16);

        //Draw Background
        if(!game.normalMode)
            drawBackgroundMod();
        else
            game.shapeRenderer.setColor(Color.BLACK);
        game.shapeRenderer.circle(512-widthMods,y+modsStart +modsBackRad, modsBackRad-line, 16);
        game.shapeRenderer.rect(512 -widthMods - modsBackRad,y+ modsStart+modsBackRad, modsBackRad, heightMods- modsBackRad*2);
        game.shapeRenderer.circle(512-widthMods,y+modsStart +heightMods-modsBackRad, modsBackRad-line, 16);
        game.shapeRenderer.rect(512 -widthMods,y+ modsStart+lineMods, widthMods, heightMods-lineMods*2);

        // Draw LeftLine
        if(game.normalMode) {
            changeColorActive();
        } else {
            changeColorInactive();
        }
        game.shapeRenderer.rect(512-widthMods- modsBackRad,y+modsStart +modsBackRad, lineMods, heightMods - modsBackRad*2);
        //game.shapeRenderer.end();
    }

    // Show no Ads available
    private void showNoAds(){
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(Color.BLACK);
        game.shapeRenderer.rect(0,0,512,310);
        game.shapeRenderer.end();

        game.batch.begin();

        if(game.en) {
            glyphLayout.setText(game.font40white, "No ads available at the moment", Color.WHITE, 0, Align.center, true);
            game.font40white.draw(game.batch, glyphLayout, 512, 310 + glyphLayout.height/2);
            glyphLayout.setText(game.smallWhiteFont, "TAP TO CONTINUE...", Color.WHITE, 0, Align.center, true);
            game.smallWhiteFont.draw(game.batch, glyphLayout, 512, 50+310/8-glyphLayout.height/2);
        } else if(game.ru) {
            glyphLayout.setText(game.tutFontRu, "Нет доступных объявлений", Color.WHITE, 0, Align.center, true);
            game.tutFontRu.draw(game.batch, glyphLayout, 512, 310 + glyphLayout.height/2);
            glyphLayout.setText(game.fontNopeRu, "НАЖМИТЕ ЧТО БЫ ПРОДОЛЖИТЬ...", Color.WHITE, 0, Align.center, true);
            game.fontNopeRu.draw(game.batch, glyphLayout, 512, 50+310/8-glyphLayout.height/2);
        }
        game.batch.end();
    }

    // ShowAds
    private void showRewardedMoneyAd(){
        if(game.android) {
            game.adsController.showRewardedVideo(new Runnable() {
                @Override
                public void run() {
                    System.out.println("RewardedVideo for money app closed");
                }
            });
        }
    }

    private float priceX, priceY;
    private boolean showNoAds;
    private int skinsSwitch;
    private int skinsSwitchHard;

    //Buttons
    private void createButtons(){
        plusButton = new Button[Application.skinsAmount];

        achieve = new Button[Application.skinsAmount];
        records = new Button[Application.skinsAmount];
        saves = new Button[Application.skinsAmount];
        arrowLeft = new Button[Application.skinsAmount];
        arrowRight = new Button[Application.skinsAmount];

        achieveTex = new TextureRegion(game.fullA3.findRegion("achieve"));
        recordsTex = new TextureRegion(game.fullA3.findRegion("records"));
        savesTex = new TextureRegion(game.fullA3.findRegion("saves"));
        for(int i=0; i<buttonsStage.length; i++){


            plusButton[i] = new Button();

            achieve[i] = new Button();
            records[i] = new Button();
            saves[i] = new Button();
            arrowLeft[i] = new Button();
            arrowRight[i] = new Button();

            plusButton[i] = new Button(new BaseDrawable());

            arrowLeft[i] = new Button(new TextureRegionDrawable(game.arrowLeftTex[i]),
                    new TextureRegionDrawable(game.arrowLeftPressedTex[i]),
                    new TextureRegionDrawable(game.arrowLeftTex[i]));
            arrowRight[i] = new Button(new TextureRegionDrawable(game.arrowRightTex[i]),
                    new TextureRegionDrawable(game.arrowRightPressedTex[i]),
                    new TextureRegionDrawable(game.arrowRightTex[i]));

            achieve[i] = new Button(new TextureRegionDrawable(achieveTex));
            records[i] = new Button(new TextureRegionDrawable(recordsTex));
            saves[i] = new Button(new TextureRegionDrawable(savesTex));

            records[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(game.android)
                    game.gpgsController.showScores();
                }
            });
            achieve[i].addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(game.android)
                    game.gpgsController.showAchievements();
                }
            });
            saves[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {

                }
            });

            plusButton[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(Application.loadedMoney) {
                        showRewardedMoneyAd();
                    } else {
                        showNoAds = true;
                    }
                    if(!game.android){
                     Currency.currency += 1000;
                        game.preferences.putInteger("currency", Currency.currency);
                        game.preferences.flush();
                    }
                }
            });

            plusButton[i].setBounds(moneyX +4,moneyY +6, currency.getWidth() + moneyWidth + 14 + plusSprite.getWidth() +8 , 32);
            plusButton[i].setSize(currency.getWidth() + moneyWidth + 14 + plusSprite.getWidth() +8 , 32);
            plusButton[i].setPosition(moneyX +4,moneyY +6);

            arrowLeft[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    arrowsClicked++;
                    // 0-0 1-1 2-2 3-3 4-4 5-showRandom
                    if(activeWorld && game.normalMode) {
                        skinsSwitch -= 1;

                        if(skinsSwitch == 0){
                            Application.gameSkin = 0;
                            player.initTextures();
                            game.showRandom = false;
                        }
                        if(skinsSwitch == 1){
                            Application.gameSkin = 1;
                            player.initTextures();
                            game.showRandom = false;
                        }
                        if(skinsSwitch == 2){
                            Application.gameSkin = 2;
                            player.initTextures();
                            game.showRandom = false;
                        }
                        if(skinsSwitch == 3){
                            Application.gameSkin = 3;
                            player.initTextures();
                            game.showRandom = false;
                        }
                        if(skinsSwitch == 4){
                            Application.gameSkin = 4;
                            player.initTextures();
                            game.showRandom = false;
                        }
                        if(skinsSwitch == 5){
                            Application.gameSkin = 0;
                            player.initTextures();
                            game.showRandom = true;
                        }

                        if(skinsSwitch == -1) {
                            skinsSwitch = 5;
                            Application.gameSkin = 0;
                            game.showRandom = true;
                        }
                    } else {
                        game.showRandom = false;
                    }

                    if(activePlayerSkin){
                        if(game.normalMode) {
                            Application.playerSkin -= 1;
                            player.initTextures();
                            if (Application.playerSkin == -1) {
                                Application.playerSkin = 3;
                                player.initTextures();
                            }
                            //drawPlSkin = true;
                            //drawPlNew = false;
                        }
                        if(game.hardMode && activePlayerSkin) {
                            skinsSwitchHard -=1;
                            player.initHardTextures();
                            if(skinsSwitchHard == 0) {
                                Application.playerSkinHard = 0;
                            }
                            if(skinsSwitchHard == 1) {
                                Application.playerSkinHard = 1;
                            }
                            //Application.playerSkin -= 1;
                            if (skinsSwitchHard == -1) {
                                skinsSwitchHard = 1;
                                Application.playerSkinHard = 1;
                            }
                        }
                    }
                    //if(activePlayerSkin && drawPlNew){
                    //    drawPlSkin = true;
                    //    drawPlNew = false;
                    //}

                    volumeSlider[0].setValue(Application.volume);
                    volumeSlider[1].setValue(Application.volume);
                    volumeSlider[2].setValue(Application.volume);
                    volumeSlider[3].setValue(Application.volume);
                    volumeSlider[4].setValue(Application.volume);
                    game.preferences.putInteger("gameSkin", Application.gameSkin);
                    game.preferences.flush();

                    game.preferences.putInteger("playerSkin", Application.playerSkin);
                    game.preferences.flush();
                    game.preferences.putInteger("playerSkinHard", Application.playerSkinHard);
                    game.preferences.flush();
                }
            });
            arrowRight[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    arrowsClicked++;
                    if(activeWorld && game.normalMode) {
                        skinsSwitch += 1;

                        if(skinsSwitch == 0){
                            Application.gameSkin = 0;
                            player.initTextures();
                            game.showRandom = false;
                        }
                        if(skinsSwitch == 1){
                            Application.gameSkin = 1;
                            player.initTextures();
                            game.showRandom = false;
                        }
                        if(skinsSwitch == 2){
                            Application.gameSkin = 2;
                            player.initTextures();
                            game.showRandom = false;
                        }
                        if(skinsSwitch == 3){
                            Application.gameSkin = 3;
                            player.initTextures();
                            game.showRandom = false;
                        }
                        if(skinsSwitch == 4){
                            Application.gameSkin = 4;
                            player.initTextures();
                            game.showRandom = false;
                        }
                        if(skinsSwitch == 5){
                            Application.gameSkin = 0;
                            player.initTextures();
                            game.showRandom = true;
                        }

                        if(skinsSwitch == 6) {
                            skinsSwitch = 0;
                            Application.gameSkin = 0;
                            game.showRandom = false;
                        }
                    } else {
                        game.showRandom = false;
                    }

                    if(activePlayerSkin  ){
                        if(game.normalMode) {
                            Application.playerSkin += 1;
                            player.initTextures();
                            if (Application.playerSkin == 4) {
                                Application.playerSkin = 0;
                                player.initTextures();
                            }
                        }
                        if(game.hardMode && activePlayerSkin) {
                            skinsSwitchHard +=1;
                            player.initHardTextures();
                            if(skinsSwitchHard == 0) {
                                Application.playerSkinHard = 0;
                            }
                            if(skinsSwitchHard == 1) {
                                Application.playerSkinHard = 1;
                            }
                            //Application.playerSkin -= 1;
                            if (skinsSwitchHard == 2) {
                                skinsSwitchHard = 0;
                                Application.playerSkinHard = 0;
                            }
                        }
                        //drawPlSkin = false;
                        //drawPlNew = true;
                    }
                    //if(activePlayerSkin && drawPlNew){
                    //    drawPlSkin = false;
                    //    drawPlNew = true;
                    //}
                    game.preferences.putInteger("gameSkin", Application.gameSkin);
                    game.preferences.flush();

                    game.preferences.putInteger("playerSkin", Application.playerSkin);
                    game.preferences.flush();
                    game.preferences.putInteger("playerSkinHard", Application.playerSkinHard);
                    game.preferences.flush();

                    volumeSlider[0].setValue(Application.volume);
                    volumeSlider[1].setValue(Application.volume);
                    volumeSlider[2].setValue(Application.volume);
                    volumeSlider[3].setValue(Application.volume);
                    volumeSlider[4].setValue(Application.volume);
                }
            });


            achieve[i].setSize(64, 64);
            records[i].setSize(64, 64);
            saves[i].setSize(64, 64);

            float scaleArrow = 0.6f;
            arrowLeft[i].setSize(111 * scaleArrow, 159 * scaleArrow);
            arrowRight[i].setSize(111 * scaleArrow, 159 * scaleArrow);

            arrowLeft[i].setPosition((384 + 32) - squardWidth - arrowLeft[i].getWidth() - 5, 310 - arrowLeft[i].getHeight() / 2);
            arrowRight[i].setPosition((384 + 32) + squardWidth + 5, 310 - arrowRight[i].getHeight() / 2);

            achieve[i].setPosition(arrowLeft[i].getX() / 2 - achieve[i].getWidth() / 2, 310 - achieve[i].getHeight() / 2);
            records[i].setPosition(achieve[i].getX(), achieve[i].getY() + achieve[i].getHeight() + 10);
            saves[i].setPosition(achieve[i].getX(), achieve[i].getY() - achieve[i].getHeight() - 10);

            buttonsStage[i].addActor(arrowLeft[i]);
            buttonsStage[i].addActor(arrowRight[i]);
            buttonsStage[i].addActor(plusButton[i]);
            buttonsStage[i].addActor(achieve[i]);
            buttonsStage[i].addActor(records[i]);
            buttonsStage[i].addActor(saves[i]);
        }
    }
    public void createSkinButtons(){
        play = new Button[Application.skinsAmount];
        gameSkin = new Button[Application.skinsAmount];
        plSkin = new Button[Application.skinsAmount];
        settings = new Button[Application.skinsAmount];

        for(int i=0; i<buttonsStage.length; i++) {
            play[i] = new Button(new TextureRegionDrawable(game.mplayTex[i]),
                    new TextureRegionDrawable(game.mplayPressedTex[i]),
                    new TextureRegionDrawable(game.mplayTex[i]));

            gameSkin[i] = new Button(new TextureRegionDrawable(game.mworldTex[i]),
                    new TextureRegionDrawable(game.mworldPressedTex[i]),
                    new TextureRegionDrawable(game.mworldTex[i]));

            plSkin[i] = new Button(new TextureRegionDrawable(game.mplayerTex[i]),
                    new TextureRegionDrawable(game.mplayerPressedTex[i]),
                    new TextureRegionDrawable(game.mplayerTex[i]));

            settings[i] = new Button(new TextureRegionDrawable(game.msettingsTex[i]),
                    new TextureRegionDrawable(game.msettingsPressedTex[i]),
                    new TextureRegionDrawable(game.msettingsTex[i]));

            play[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    activePlay = true;
                    for (int i = 0; i < arrowLeft.length; i++) {
                        arrowLeft[i].setVisible(false);
                        arrowRight[i].setVisible(false);
                    }
                    if(game.normalMode) {
                        if((Application.openDummy && Application.playerSkin ==2 )||
                                (Application.openArrow && Application.playerSkin ==3 ) ||
                                (Application.openMoon && Application.playerSkin==1)) {
                            if (game.goTutorial) {
                                game.setScreen(game.tutorialScreen);
                            } else {
                                game.setScreen(game.gameScreen);
                            }
                        } else{
                            Application.playerSkin =0;
                            if (game.goTutorial) {
                                game.setScreen(game.tutorialScreen);
                            } else {
                                game.setScreen(game.gameScreen);
                            }
                        }
                        if (!Application.skinLocked[Application.gameSkin] && !activeSettings && !game.showRandom) {
                            if (game.goTutorial) {
                                game.setScreen(game.tutorialScreen);
                            } else {
                                    game.setScreen(game.gameScreen);
                            }
                            game.normalMode = true;
                            game.hardMode = false;
                            game.survivalMode = false;
                            game.mainMenuScreen.pause();
                        } else{
                            Application.gameSkin =0;
                            Application.playerSkin = 0;
                            if (game.goTutorial) {
                                game.setScreen(game.tutorialScreen);
                            } else {
                                game.setScreen(game.gameScreen);
                            }
                        }
                    }

                    if(game.hardMode) {
                        if (!activeSettings && !game.showRandom) {
                            if(Application.playerSkinHard == 0 ||(Application.playerSkinHard == 1 && Application.openMoon)) {
                                if (game.goTutorial) {
                                    game.setScreen(game.tutorialScreen);
                                } else {
                                    game.setScreen(game.x2GameModeScreen);
                                    //game.setScreen(game.survivalModeScreen);
                                }
                            } else {
                                Application.playerSkinHard = 0;
                                if (game.goTutorial) {
                                    game.setScreen(game.tutorialScreen);
                                } else {
                                    game.setScreen(game.x2GameModeScreen);
                                    //game.setScreen(game.survivalModeScreen);
                                }
                            }

                            game.normalMode = false;
                            game.hardMode = true;
                            game.survivalMode = false;
                            game.mainMenuScreen.pause();
                        }
                    }
                    if(game.survivalMode) {
                        if (!Application.skinLocked[Application.gameSkin] && !activeSettings && !game.showRandom) {
                            if (game.goTutorial) {
                                game.setScreen(game.tutorialScreen);
                            } else {
                                //game.setScreen(game.x2GameModeScreen);
                                game.setScreen(game.survivalModeScreen);
                            }
                            game.normalMode = false;
                            game.hardMode = false;
                            game.survivalMode = true;
                            game.mainMenuScreen.pause();
                        }
                    }

                    if(game.showRandom) {
                        Application.gameSkin = game.openedSkins.get(game.randomizeSkins());
                        //System.out.println(game.openedSkins.get(game.randomizeSkins()));
                        if (!Application.skinLocked[Application.gameSkin]) {
                            game.setScreen(game.gameScreen);
                        }
                    }
                    /*
                    if(Application.skinLocked[Application.gameSkin] && !game.showRandom){
                        Application.gameSkin = 0;
                        if (game.goTutorial) {
                            game.setScreen(game.tutorialScreen);
                        } else {
                            game.setScreen(game.gameScreen);

                        }
                    } */

                    activeSettings = false;
                    activeWorld = false;
                    activePlayerSkin = false;

                    game.preferences.putInteger("gameSkin", Application.gameSkin);
                    game.preferences.flush();
                }
            });

            gameSkin[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                        activeWorld = true;

                        activePlay = false;
                        activePlayerSkin = false;
                        activeSettings = false;

                    game.preferences.putInteger("gameSkin", Application.gameSkin);
                    game.preferences.flush();
                }
            });
            plSkin[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    activePlayerSkin = true;

                    activePlay = false;
                    activeWorld = false;
                    activeSettings = false;


                    drawPlSkin= true;
                    drawPlNew = false;

                    game.preferences.putInteger("gameSkin", Application.gameSkin);
                    game.preferences.flush();

                }
            });
            settings[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    activeSettings = true;

                    activePlay = false;
                    activeWorld = false;
                    activePlayerSkin = false;

                    game.preferences.putInteger("gameSkin", Application.gameSkin);
                    game.preferences.flush();

                }
            });


            play[i].setSize(155, 80);
            gameSkin[i].setSize(155, 80);
            plSkin[i].setSize(155, 80);
            settings[i].setSize(155, 80);

            play[i].setPosition(64, buttonHeight);
            gameSkin[i].setPosition(play[i].getX() + play[i].getWidth() + space, buttonHeight);
            gameSkin[i].setPosition(64 + 155 + space, buttonHeight);
            plSkin[i].setPosition(gameSkin[i].getX() + gameSkin[i].getWidth() + space, buttonHeight);
            settings[i].setPosition(plSkin[i].getX() + plSkin[i].getWidth() + space, buttonHeight);

            buttonsStage[i].addActor(play[i]);
            buttonsStage[i].addActor(gameSkin[i]);
            buttonsStage[i].addActor(plSkin[i]);
            buttonsStage[i].addActor(settings[i]);

        }
    }
    private void createTextButtons(){
        enButton = new TextButton[Application.skinsAmount];
        ruButton = new TextButton[Application.skinsAmount];

        //changeMode = new Button[Application.skinsAmount];

        priceButton = new Button[Application.skinsAmount-1];
        pricePlayer = new Button[Application.skinsAmount];

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = game.menu0Font;
        for(int i =0; i < enButton.length; i++) {
            enButton[i] = new TextButton("English", textButtonStyle);
        }

        for(int i=0; i < pricePlayer.length; i++) {
            pricePlayer[i] = new Button(new BaseDrawable());
            pricePlayer[i].setPosition((384 + 32) - squardWidth + line * 2, 310 - squardHeight + up * 2 + line * 2 );
            pricePlayer[i].setSize(squardWidth * 2 - line * 4, (squardHeight * 4 - line * 4)/10);

            //pricePlayer[i].setDebug(true);

            pricePlayer[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(activePlayerSkin && game.normalMode){
                        if(Application.playerSkin == 2 && !Application.openDummy && Currency.currency >= Application.dummyPrice) {
                            Currency.currency -= Application.dummyPrice;
                            Application.openDummy = true;

                            game.preferences.putInteger("currency", Currency.currency);
                            game.preferences.flush();

                            game.preferences.putBoolean("openDummy", Application.openDummy);
                            game.preferences.flush();
                        }
                        if(Application.playerSkin == 3 && !Application.openArrow && Currency.currency >= Application.arrowPrice) {
                            Currency.currency -= Application.arrowPrice;
                            Application.openArrow = true;

                            game.preferences.putInteger("currency", Currency.currency);
                            game.preferences.flush();

                            game.preferences.putBoolean("openArrow", Application.openArrow);
                            game.preferences.flush();

                        }
                    }
                }
            });
        }

        for(int i=0; i < priceButton.length; i++) {
            priceButton[i] = new Button(new BaseDrawable());
            priceButton[i].setPosition((384 + 32) - squardWidth + line * 2, 310 - squardHeight + up * 2 + line * 2 );
            priceButton[i].setSize(squardWidth * 2 - line * 4, (squardHeight * 4 - line * 4)/10);

            priceButton[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(activeWorld && game.normalMode) {
                        if (Currency.currency >= Application.price && Application.skinLocked[Application.gameSkin]) {
                            Currency.currency -= Application.price;
                            Application.skinLocked[Application.gameSkin] = false;
                            game.openedSkins.add(Application.gameSkin);
                            priceClicked();

                            game.skinsOpened += 1;

                            game.preferences.putInteger("skinsOpened", game.skinsOpened);
                            game.preferences.flush();

                            game.preferences.putInteger("currency", Currency.currency);
                            game.preferences.flush();
                            game.preferences.putInteger("price", Application.price);
                            game.preferences.flush();
                            game.preferences.putBoolean("skinLocked1", Application.skinLocked[1]);
                            game.preferences.flush();
                            game.preferences.putBoolean("skinLocked2", Application.skinLocked[2]);
                            game.preferences.flush();
                            game.preferences.putBoolean("skinLocked3", Application.skinLocked[3]);
                            game.preferences.flush();
                            game.preferences.putBoolean("skinLocked4", Application.skinLocked[4]);
                            game.preferences.flush();
                        }
                    }

                }
            });
        }
        textButtonStyle.font = game.menu0FontRu;
        for(int i = 0; i < Application.skinsAmount; i++) {
            ruButton[i] = new TextButton("Русский", textButtonStyle);
            enButton[i].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.en = true;
                game.ru = false;

                game.preferences.putBoolean("en", game.en);
                game.preferences.flush();
                game.preferences.putBoolean("ru", game.ru);
                game.preferences.flush();
            }
        });

        ruButton[i].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.en = false;
                game.ru = true;

                game.preferences.putBoolean("en", game.en);
                game.preferences.flush();
                game.preferences.putBoolean("ru", game.ru);
                game.preferences.flush();
            }
        });
        enButton[i].setPosition((384 + 32) - squardWidth/2 -40+2, 310 -15- languageGlyphHeight/ 2 -langY);
        ruButton[i].setPosition((384 + 32) + squardWidth/2 -40-4, 310 -15- languageGlyphHeight/ 2 -langY);
        buttonsStage[i].addActor(enButton[i]);
        buttonsStage[i].addActor(ruButton[i]);
        enButton[i].setVisible(false);
        ruButton[i].setVisible(false);
        }
        for(int i=0; i< priceButton.length; i++){
            buttonsStage[i+1].addActor(priceButton[i]);
        }
        normalMode = new Button[Application.skinsAmount];
        speedMode = new Button[Application.skinsAmount];
        survivalMode = new Button[Application.skinsAmount];

        for(int i =0; i< Application.skinsAmount; i++) {
            normalMode[i] = new Button(new BaseDrawable());
            speedMode[i] = new Button(new BaseDrawable());
            survivalMode[i] = new Button(new BaseDrawable());

            normalMode[i].setSize(widthMods*2 + modsBackRad*2, heightMods*2);
            speedMode[i].setSize(widthMods*2+ modsBackRad*2, heightMods*2);
            survivalMode[i].setSize(widthMods*2+ modsBackRad*2, heightMods*2);


            survivalMode[i].setPosition(1024-widthMods*2-modsBackRad*2, modsStart*2);
            speedMode[i].setPosition(1024-widthMods*2-modsBackRad*2, modsStart*2+ heightMods*2+spaceMods*2);
            normalMode[i].setPosition(1024-widthMods*2-modsBackRad*2, modsStart*2+ heightMods*4+spaceMods*4);

            normalMode[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    player.initTextures();
                    Application.gameSkin = 0;
                    game.normalMode = true;
                    game.hardMode = false;
                    game.survivalMode = false;

                    game.preferences.putBoolean("normalMode", game.normalMode);
                    game.preferences.flush();
                    game.preferences.putBoolean("hardMode", game.hardMode);
                    game.preferences.flush();
                    game.preferences.putBoolean("survivalMode", game.survivalMode);
                    game.preferences.flush();
                }
            });
            speedMode[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    player.initHardTextures();
                    Application.gameSkin = 3;

                    game.normalMode = false;
                    game.hardMode = true;
                    game.survivalMode = false;

                    game.preferences.putBoolean("normalMode", game.normalMode);
                    game.preferences.flush();
                    game.preferences.putBoolean("hardMode", game.hardMode);
                    game.preferences.flush();
                    game.preferences.putBoolean("survivalMode", game.survivalMode);
                    game.preferences.flush();
                }
            });
            survivalMode[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // INACTIVE BUTTON NOW
                    //game.normalMode = false;
                    //game.hardMode = false;
                    //game.survivalMode = true;
                    //game.preferences.putBoolean("normalMode", game.normalMode);
                    //game.preferences.flush();
                    //game.preferences.putBoolean("hardMode", game.hardMode);
                    //game.preferences.flush();
                    //game.preferences.putBoolean("survivalMode", game.survivalMode);
                    //game.preferences.flush();
                }
            });

            buttonsStage[i].addActor(normalMode[i]);
            buttonsStage[i].addActor(speedMode[i]);
            buttonsStage[i].addActor(survivalMode[i]);
            buttonsStage[i].addActor(pricePlayer[i]);
        }

        /*
        for(int i=0; i< changeMode.length; i++) {

            changeMode[i] = new Button(new BaseDrawable());

            changeMode[i].setSize(96 * 2, 310 * 2);
            changeMode[i].setPosition((384 + 32) * 2, 0);

            changeMode[i].addListener(new ActorGestureListener() {
                public void fling(InputEvent event, float velocityX, float velocityY, int button) {
                    if(game.hardMode || game.survivalMode) {
                        if (velocityX > velocityY) {
                            game.normalMode = true;
                            game.hardMode = false;
                            game.survivalMode = false;
                        }
                    }

                    if(game.normalMode) {
                        if (velocityX < velocityY) {
                            game.normalMode = false;
                            game.hardMode = false;
                            game.survivalMode = true;
                        }

                    }
                }
            });

            buttonsStage[i].addActor(changeMode[i]);
        } */
    }
    // Volume Slider
    private void createSlider() {
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle(new BaseDrawable(), new BaseDrawable());

        volumeSlider = new Slider[Application.skinsAmount];
        for(int i =0; i < volumeSlider.length; i++) {
            volumeSlider[i] = new Slider(0, 1, 0.01f, false, sliderStyle);
            volumeSlider[i].setSize(594 / 2, 69 / 2);
            volumeSlider[i].setPosition(volumeSprite.getX() + volumeSprite.getWidth(),
                    volumeSprite.getY() + volumeSprite.getHeight() / 2 - volumeSlider[i].getHeight() / 2);
            //volumeSlider.setDebug(true);
            volumeSlider[i].addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    Application.volume = volumeSlider[Application.gameSkin].getValue();

                    game.preferences.putFloat("volume", Application.volume);
                    game.preferences.flush();
                }
            });
            buttonsStage[i].addActor(volumeSlider[i]);
        }
    }

    private void priceClicked(){
        Application.skinsBought +=1;
        game.preferences.putInteger("skinsBought", Application.skinsBought);
        game.preferences.flush();

        game.preferences.putInteger("gameSkin", Application.gameSkin);
        game.preferences.flush();
        Application.price = Application.startPrice+ Application.skinsBought *100;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

        spriteViewport.update(width,height);
        spriteCamera.position.set(camera.viewportWidth - 256,camera.viewportHeight - 155,0);
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

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        showNoAds = false;

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
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
