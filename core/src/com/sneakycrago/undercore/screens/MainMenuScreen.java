package com.sneakycrago.undercore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sneakycrago.undercore.Application;
import com.sneakycrago.undercore.objects.Player;
import com.sneakycrago.undercore.utils.Currency;
import com.sneakycrago.undercore.utils.Globals;
import com.sneakycrago.undercore.utils.Score;

/**
 * Created by Sneaky Crago on 13.05.2017.
 */

public class MainMenuScreen implements Screen {
    Application game;

    private OrthographicCamera camera;
    private OrthographicCamera spriteCamera;
    private Stage[] buttonsStage;
    //private Stage stage;

    private Button[] play, gameSkin, plSkin, settings,plusButton, changeMode;
    private Button[] arrowLeft, arrowRight, records,achieve,saves;
    private TextButton[] enButton, ruButton;
    private Button[] priceButton;
    private Slider[] volumeSlider;

    //private TextureRegion arrowPressedTex,flipArrowPressedTex, flipArrowTex;
    private TextureRegion plusWhiteTex,recordsTex, achieveTex, savesTex ;

    private Sprite skull, currency, currencyPrice, lockPrice, skullUp, skullDown, plusSprite, volumeSprite;
    private Sprite[] skinPrew, skullInsane;

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
    private boolean normalMode =true, hardMode = false;


    Texture tex = new Texture(Gdx.files.internal("textures/bar.png"));

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
        skull.setScale(0.75f);
        //384 + 32,0, 96, 310 black right board
        skull.setPosition((384 + 32)*2  + (192 - skull.getWidth())/2, 620/2 - skull.getHeight()/2);

        skullInsane = new Sprite[3];
        for(int i=0; i< skullInsane.length; i++){
            skullInsane[i] = new Sprite(game.fullA2.findRegion("skull1.0"));
        }
        skullInsane[0].setScale(0.75f);
        skullInsane[1].setScale(0.5f);
        skullInsane[2].setScale(0.5f);
        skullInsane[0].setPosition((384 + 32)*2  + (192 - skull.getWidth())/2, 620/2 - skull.getHeight()/2);
        skullInsane[1].setPosition((384 + 32)*2  + (192 - skull.getWidth())/2, 620/2 + 50 );
        skullInsane[2].setPosition((384 + 32)*2  + (192 - skull.getWidth())/2, 620/2  -skull.getHeight()- 50);

        currency = new Sprite(game.currencyTexture);
        float scale = 0.14f;
        currency.setSize(199*scale, 300*scale);

        currencyPrice = new Sprite(game.currencyTexture);
        scale = 0.07f;
        currencyPrice.setSize(199*scale, 300*scale);

        skullUp = new Sprite(game.fullA2.findRegion("skull1.1"));
        skullUp.setScale(0.5f);
        skullUp.setPosition((384 + 32)*2  + (192 - skull.getWidth())/2, 620/2 + 50 );

        skullDown = new Sprite(game.fullA2.findRegion("skull1.1"));
        skullDown.setScale(0.5f);
        skullDown.setPosition((384 + 32)*2  + (192 - skull.getWidth())/2, 620/2  -skullDown.getHeight()- 50);
        //skullDown.setPosition(0,0);

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

        //test
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
        if(activeWorld || activePlayerSkin && normalMode){
            for(int i =0;i < arrowLeft.length; i++){
                arrowLeft[i].setVisible(true);
                arrowRight[i].setVisible(true);
            }
        } else if(activePlay || activeSettings && normalMode) {
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

        normalMode =true;
        hardMode = false;
    }

    int langY = 40;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(18/255f,25/255f,26/255f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
        game.shapeRenderer.setColor(Color.BLACK);
        game.shapeRenderer.rect(384 + 32,0, 96, 310);

        //Menu
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

        if(activePlayerSkin && drawPlSkin && normalMode){
            switch (Application.gameSkin){
                case 0: game.shapeRenderer.setColor(Color.BLACK);
                    break;
                case 1:  game.shapeRenderer.setColor(Globals.Background1Color);
                    break;
                case 2:  game.shapeRenderer.setColor(Globals.Background2Color);
                    break;
                case 3:  game.shapeRenderer.setColor(Globals.Background3Color);
                    break;
                case 4:  game.shapeRenderer.setColor(Globals.Background4Color);
                    break;
            }
            game.shapeRenderer.circle(player.getPlayerRectangle().getX()+ 16, player.getPlayerRectangle().getY() + 16, 48, 64);
        }
        game.shapeRenderer.setColor(15/255f, 16/255f, 16/255f,1f);
        game.shapeRenderer.rect(moneyX/2 -2 +3,moneyY/2 +3,currency.getWidth()/2 + moneyWidth/2 + 7 + plusSprite.getWidth()/2 +4,16);

        if(activePlayerSkin && drawPlSkin && normalMode){
            player.drawPlayerCube(game.shapeRenderer);
        }

        game.shapeRenderer.end();
        game.batch.setProjectionMatrix(spriteCamera.combined);
        game.batch.begin();

        if(activeWorld || activePlay && normalMode){
            skinPrew[Application.gameSkin].draw(game.batch);
        }

        if(activePlayerSkin && drawPlSkin && normalMode){
            player.inMenuAnimation(game.batch, delta, game);
        }
        game.batch.end();

        if(activeWorld && normalMode) {
            if(Application.skinLocked[Application.gameSkin]) {
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


        buttonsStage[Application.gameSkin].act(delta);
        buttonsStage[Application.gameSkin].draw();

        game.batch.begin();
        if(normalMode) {
            skull.draw(game.batch);
            skullUp.draw(game.batch);
            skullDown.draw(game.batch);
        } else if(hardMode) {
            for (int i = 0; i < skullInsane.length; i++) {
                skullInsane[i].draw(game.batch);
            }
        }
        if(activeWorld || activePlay && normalMode) {
            glyphLayout.setText(game.menuScoreFont, "" + Score.getBestScore(), Color.WHITE, (squardWidth - line * 2) * 2, Align.center, true);
            game.menuScoreFont.draw(game.batch, glyphLayout, scoreX, scoreY -20);
        } else{
            for(int i=0; i < priceButton.length; i++) {
                priceButton[i].setVisible(false);
            }
        }
        if(activeWorld || activePlayerSkin && normalMode){
            for(int i =0;i < arrowLeft.length; i++){
                arrowLeft[i].setVisible(true);
                arrowRight[i].setVisible(true);
            }
        } else if(hardMode){
            for(int i =0;i < arrowLeft.length; i++){
                arrowLeft[i].setVisible(false);
                arrowRight[i].setVisible(false);
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
                if(normalMode) {
                    glyphLayout.setText(game.menuBig, "Normal", Color.WHITE, (384 + 32) * 2, Align.center, true);
                    game.menuBig.draw(game.batch, glyphLayout, 0, 310 * 2 - 20);
                }
                languageGlyphHeight = glyphLayout.height;
            } else if(game.ru && !activeSettings){
                if(normalMode){
                    glyphLayout.setText(game.menuBigRu, "Обычный", Color.WHITE, (384 + 32) * 2, Align.center, true);
                    game.menuBigRu.draw(game.batch, glyphLayout, 0, 310 * 2 - 20);
                }
                languageGlyphHeight = glyphLayout.height;
            }
        }

        if(hardMode && !activeSettings){
            if(game.en) {
                glyphLayout.setText(game.menuBig, "Hard", Color.WHITE, (384 + 32) * 2, Align.center, true);
                game.menuBig.draw(game.batch, glyphLayout, 0, 310 * 2 - 20);

                glyphLayout.setText(game.tutFont, "Winter is coming", Color.WHITE, squardWidth * 2, Align.center, true);
                game.tutFont.draw(game.batch, glyphLayout, (384 + 32) - squardWidth, 310 + up * 2 + glyphLayout.height / 2);
            }
            if(game.ru) {
                glyphLayout.setText(game.menuBigRu, "Сложный", Color.WHITE, (384 + 32) * 2, Align.center, true);
                game.menuBigRu.draw(game.batch, glyphLayout, 0, 310 * 2 - 20);

                glyphLayout.setText(game.tutFontRu, "Зима близко...", Color.WHITE, squardWidth * 2, Align.center, true);
                game.tutFontRu.draw(game.batch, glyphLayout, (384 + 32) - squardWidth, 310 + up * 2 + glyphLayout.height / 2);
            }
        }
        if(activePlayerSkin && normalMode){
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
        }

        glyphLayout.setText(game.menu0Font, ""+Currency.currency, Color.WHITE, (384 + 32)* 2, Align.center, true);
        moneyX = (384 + 32) -currency.getWidth() - glyphLayout.width/2 - 4 -3;
        moneyY = 310*2 - 20 -60-currency.getHeight()/2 - glyphLayout.height/2 - minusLesha*2;
        moneyWidth = glyphLayout.width;
        game.menu0Font.draw(game.batch, glyphLayout, 0, 310*2 - 20 -60- minusLesha*2);

        game.batch.end();

        // Spites
        game.batch.begin();
        currency.setPosition(moneyX, moneyY);
        currency.draw(game.batch);


        plusSprite.setPosition(moneyX+ moneyWidth + 45,moneyY +6 + 16 - plusSprite.getHeight()/2);
        plusSprite.draw(game.batch);

        if(activeWorld && normalMode) {
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
                if(Application.skinLocked[Application.gameSkin] && normalMode) {
                    currencyPrice.draw(game.batch);
                    lockPrice.draw(game.batch);
                }
            }
        }

        if(activeSettings){
            volumeSprite.setPosition((384 + 32) - squardWidth + 64,
                    310 + squardHeight/2 + up*2 - volumeSprite.getHeight()/2);
            volumeSprite.draw(game.batch);
        }
        game.batch.end();
        //test
        for(int i=0; i< plusButton.length; i++) {
            plusButton[i].setSize(currency.getWidth() + moneyWidth + 14 + plusSprite.getWidth() +8 , 32);
            plusButton[i].setPosition(moneyX +4,moneyY +6);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
            game.mainMenuScreen.show();
        }
    }
    private float priceX, priceY;


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
            plusButton[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {

                    Currency.currency += 100;

                }
            });

            plusButton[i].setBounds(moneyX +4,moneyY +6, currency.getWidth() + moneyWidth + 14 + plusSprite.getWidth() +8 , 32);
            plusButton[i].setSize(currency.getWidth() + moneyWidth + 14 + plusSprite.getWidth() +8 , 32);
            plusButton[i].setPosition(moneyX +4,moneyY +6);

            arrowLeft[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {

                    if(activeWorld && normalMode) {
                        Application.gameSkin -= 1;
                        if (Application.gameSkin <= -1) {
                            Application.gameSkin = 4;
                        }

                    }
                    if(activePlayerSkin && drawPlSkin && normalMode){
                        drawPlSkin = true;
                        drawPlNew = false;
                    }
                    if(activePlayerSkin && drawPlNew && normalMode){
                        drawPlSkin = true;
                        drawPlNew = false;
                    }

                    volumeSlider[0].setValue(Application.volume);
                    volumeSlider[1].setValue(Application.volume);
                    volumeSlider[2].setValue(Application.volume);
                    volumeSlider[3].setValue(Application.volume);
                    volumeSlider[4].setValue(Application.volume);

                }
            });
            arrowRight[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(activeWorld && normalMode) {
                        Application.gameSkin += 1;
                        if (Application.gameSkin >= 5) {
                            Application.gameSkin = 0;
                        }
                    }
                    if(activePlayerSkin  && drawPlSkin && normalMode){
                        drawPlSkin = false;
                        drawPlNew = true;
                    }
                    if(activePlayerSkin && drawPlNew && normalMode){
                        drawPlSkin = false;
                        drawPlNew = true;
                    }
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
                    if(normalMode) {
                        if (!Application.skinLocked[Application.gameSkin] && !activeSettings) {
                            if (game.goTutorial) {
                                game.setScreen(game.tutorialScreen);
                            } else {
                                game.setScreen(game.gameScreen);
                            }
                            game.mainMenuScreen.pause();
                        }
                    }
                    activeSettings = false;
                    activeWorld = false;
                    activePlayerSkin = false;
                }
            });

            gameSkin[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                        activeWorld = true;

                        activePlay = false;
                        activePlayerSkin = false;
                        activeSettings = false;
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


                }
            });
            settings[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    activeSettings = true;

                    activePlay = false;
                    activeWorld = false;
                    activePlayerSkin = false;

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

        changeMode = new Button[Application.skinsAmount];

        priceButton = new Button[Application.skinsAmount-1];

        //TextButton.TextButtonStyle = new textButtonStyle;
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = game.menu0Font;
        //textButtonStyle.up = new BaseDrawable();
        //textButtonStyle.down = new BaseDrawable();
        //textButtonStyle.checked = new BaseDrawable();
        for(int i =0; i < enButton.length; i++) {
            enButton[i] = new TextButton("English", textButtonStyle);
        }

        for(int i=0; i < priceButton.length; i++) {
            priceButton[i] = new Button(new BaseDrawable());
            priceButton[i].setPosition((384 + 32) - squardWidth + line * 2, 310 - squardHeight + up * 2 + line * 2 );
            priceButton[i].setSize(squardWidth * 2 - line * 4, (squardHeight * 2 - line * 4)/10);
            priceButton[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (Currency.currency >= Application.price && Application.skinLocked[Application.gameSkin]) {
                        Currency.currency -= Application.price;
                        Application.skinLocked[Application.gameSkin] = false;
                        priceClicked();
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
        for(int i=0; i< changeMode.length; i++) {

            changeMode[i] = new Button(new BaseDrawable());

            changeMode[i].setSize(96 * 2, 310 * 2);
            changeMode[i].setPosition((384 + 32) * 2, 0);

            changeMode[i].addListener(new ActorGestureListener() {
                public void fling(InputEvent event, float velocityX, float velocityY, int button) {
                    if(normalMode) {
                        if (velocityX > velocityY) {
                            normalMode = false;
                            hardMode = true;
                        }
                    }
                    if(hardMode) {
                        if (velocityX < velocityY) {
                            normalMode = true;
                            hardMode = false;
                        }
                    }
                }
            });

            buttonsStage[i].addActor(changeMode[i]);
        }
    }
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
                }
            });
            buttonsStage[i].addActor(volumeSlider[i]);
        }
    }

    private void priceClicked(){
        Application.skinsBought +=1;
        Application.price = 500+ Application.skinsBought *100;
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
}
