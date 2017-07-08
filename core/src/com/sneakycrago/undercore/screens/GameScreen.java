package com.sneakycrago.undercore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.sneakycrago.undercore.Application;
import com.sneakycrago.undercore.objects.BigArrow;
import com.sneakycrago.undercore.objects.Circle;
import com.sneakycrago.undercore.objects.Corridor;
import com.sneakycrago.undercore.objects.Laser;
import com.sneakycrago.undercore.objects.Player;
import com.sneakycrago.undercore.objects.SmallArrowZone;
import com.sneakycrago.undercore.objects.SniperZone;
import com.sneakycrago.undercore.objects.Wall;
import com.sneakycrago.undercore.objects.WhiteSides;
import com.sneakycrago.undercore.utils.Currency;
import com.sneakycrago.undercore.utils.Globals;
import com.sneakycrago.undercore.utils.Score;

import java.util.Random;



/**
 * Created by Sneaky Crago on 25.03.2017.
 */

public class GameScreen implements Screen, InputProcessor {
    Application game;

    private OrthographicCamera camera;
    private OrthographicCamera spriteCamera;

    private Random random;

    //objects
    private WhiteSides whiteSides;
    private Player player;
    private Wall wall;
    private Corridor corridor;
    private BigArrow bigArrow;
    private Laser laser;
    private Circle circle;
    private SniperZone sniperZone;
    private SmallArrowZone smallArrowZone;

    private final int SPAWN = 512;
    int start_corridor;

    //BIG ARROW LOGIC
    private int amountOfBigArrows;
    private int bigArrowsCount = 0;
    private int blocksNumber, blockCounter = 7;

    private boolean startZoneStart = true;
    private boolean bigArrowBlockStart = false;
    private boolean laserZoneStart = false;
    private boolean circlesStart = false;
    private boolean snipersStart = false;
    private boolean smallArrowStart = false;

    int nextZoneRandom;
    // for platform switch

    private boolean circleCreated = false, laserCreated = false, sniperCreated = false, smallArrowCreated = false,
    bigArrowCreated = false;

    private GlyphLayout glyphLayout;

    private Stage stage;

    private Button watchButton, nopeButton;

    private Viewport viewport;
    private Viewport spriteViewport;

    //private MyInputProcessor inputProcessor;
    private int counter;

    private boolean secondChance;
    private boolean secondDeath;

    private boolean deathWall = false, deathBigArrow = false, deathCircle  = false,
            deathLaser = false;
    private int sidesDeath; // 1 - START 2 - LASER 3 - BIG_ARROW 4 - CIRCLE 5 - SMALL_ARROW 6 - SNIPERS

    private boolean startAfterDeath;

    private boolean startPlay;

    private Currency currency;

    public GameScreen(Application game) {
        this.game = game;
        this.camera = game.camera;

        viewport = new StretchViewport(Application.V_WIDTH*2,Application.V_HEIGHT*2,camera);
        viewport.apply();

        stage = new Stage(viewport, game.batch);

        spriteCamera = new OrthographicCamera();
        spriteCamera.setToOrtho(false, Application.V_WIDTH, Application.V_HEIGHT);

        random = new Random();

        glyphLayout = new GlyphLayout();


        spriteViewport = new StretchViewport(Application.V_WIDTH, Application.V_HEIGHT, spriteCamera);
        spriteViewport.apply();

        spriteCamera.position.set(camera.viewportWidth/2 - 256,camera.viewportHeight /2 - 155,0);

        currency = new Currency();
    }

    //Laser laser;
    @Override
    public void show() {
        //Gdx.input.setInputProcessor(inputProcessor);
        Gdx.input.setInputProcessor(this);
        createButtons();
        resetZoneCreator(); // Reset boolean values

        game.mainMenuScreen.pause();
        game.mainMenuScreen.dispose();
        game.gameOver.dispose();

        //Application.gameSkin = random.nextInt(5);

        //create objects
        whiteSides = new WhiteSides();
        player = new Player(96, 139, game);

        wall = new Wall(); //START BLOCK
        start_corridor = SPAWN + wall.getBLOCK_SIZE() + 256;
        corridor = new Corridor();

        Application.playerAlive = true;

        startZoneStart = true;
        wall.init(SPAWN);
        corridor.init(start_corridor);

        laser = new Laser(game); //Lasers
        bigArrow = new BigArrow(game);
        circle = new Circle(game);
        smallArrowZone = new SmallArrowZone();
        sniperZone = new SniperZone();

        amountOfBigArrows = random.nextInt(3) + 3;

        //System.out.println("Big Arrows: " + amountOfBigArrows);

        //Обнуляем счет
        Score.setGameScore(0);
        blocksNumber = 0;
        Currency.resetMoney();

        deathSound = false;


        //makeSmallArrow(SPAWN);
        //startZoneStart = false;

        game.ambientSound.setVolume(Application.volume * 0.1f);
        game.ambientSound.play();

        time = 2.99f;
        counter =3;

        secondChance = false;
        secondDeath = false;

        deathWall = false;
        deathBigArrow = false;
        deathCircle  = false;
        deathLaser = false;
        game.deathSmallArow = false;
        game.deathSnipers = false;

        blockCounter = 7;
        counterCheked = false;
        sidesDeath = 1; // Считает смерть от белых сторон, цифра - зона в которой умер игрок

        startAfterDeath = false;

        startPlay = true;

        calculatedDeath = false;

        if(!game.android) {
            Application.loadedReborn = true;
        }

    }


    private float time = 3;

   int buttonX, buttonY;


    @Override
    public void render(float delta) {
        game.adTimer();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteCamera.update();
        game.batch.setProjectionMatrix(spriteCamera.combined);

        if(Application.playerAlive){
            nopeButton.setVisible(false);
            watchButton.setVisible(false);
        }
        if(game.test && Gdx.input.isKeyPressed(Input.Keys.Q)) {
                collisionCheck(delta);
        }
        if(Application.gameSkin == 0) {
            Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        } else if(Application.gameSkin == 1) {
            Gdx.gl.glClearColor(175/255f,209/255f,234/255f, 1f);
        }
        switch (Application.gameSkin){
            case 0: Gdx.gl.glClearColor(0f, 0f, 0f, 1);
                break;
            case 1: Gdx.gl.glClearColor(175/255f,209/255f,234/255f, 1f);
                break;
            case 2: Gdx.gl.glClearColor(26/255f, 50/255f, 15/255f, 1f);
                break;
            case 3: Gdx.gl.glClearColor(2255/255f, 255/255f, 255/255f, 1f);
                break;
            case 4: Gdx.gl.glClearColor(252/255f, 214/255f, 225/255f, 1f);
                break;
        }

        update(delta);


        // SPRITES
        game.batch.begin();

        if(circlesStart) {
            circle.drawCircles(game.batch);
        }
        if(laserZoneStart) {
         laser.drawLaserGun(game.batch);
        }

        if(startAfterDeath || startPlay){
            player.inMenuAnimation(game.batch,delta,game);
        }

        game.batch.end();

        // BIG ARROW behind(backwards) from player
        bigArrowLogic(delta);
        // Shape objects
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        whiteSides.drawWhiteSides(game.shapeRenderer); //draw WhiteSides

        // DRAW ELEMENTS
        if(startZoneStart) {
            wall.drawWallBlock(game.shapeRenderer); //draw walls
            corridor.drawCorridor(game.shapeRenderer);
        }
        if(smallArrowStart) {
            smallArrowZone.drawArrowZone(game.shapeRenderer);
        }
        if(laserZoneStart) {
            laser.drawLaserBlock(game.shapeRenderer, delta);
        }

        if(snipersStart) {
            sniperZone.drawSniperBlock(game.shapeRenderer, (int) player.getPlayerRectangle().getY() + 16);
        }

        game.shapeRenderer.end();


        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        //game info
        if(game.test) {
            game.font10.draw(game.batch, "fps:" + Gdx.graphics.getFramesPerSecond(), 0, (288 - 24) * 2);
            game.font10.draw(game.batch, "blocks:" + blocksNumber, 0, (288 - 24 - 12) * 2);
            game.font10.draw(game.batch, "money:" + Currency.Money, 0, (288 - 24 - 12 * 2) * 2);
            game.font10.draw(game.batch, "counter:" + blockCounter, 0, (288 - 24 - 12 * 3) * 2);
            game.font10.draw(game.batch, "loadedReborn:" + Application.loadedReborn, 0, (288 - 24 - 12 * 4) * 2);
            game.font10.draw(game.batch, "Award(reborn):" + Application.reborn, 0, (288 - 24 - 12 * 5) * 2);
            game.font10.draw(game.batch, "secondChance:" + secondChance, 0, (288 - 24 - 12 * 6) * 2);
            game.font10.draw(game.batch, "afterDeath:" + startAfterDeath, 0, (288 - 24 - 12 * 7) * 2);
        }
        glyphLayout.setText(game.borderFont, ""+ Score.getGameScore() , Color.WHITE,512*2, Align.center, true);
        game.borderFont.draw(game.batch, glyphLayout, 0, (11 + 288 - 4)*2);

        if(startAfterDeath) {
            if(game.en) {
                glyphLayout.setText(game.startEN, "TAP TO CONTINUE", Color.WHITE, 512 * 2, Align.center, true);
                game.startEN.draw(game.batch, glyphLayout, 0, 310 + glyphLayout.height / 2);
            } else if(game.ru) {
                glyphLayout.setText(game.startRU, "НАЖМИТЕ ЧТО БЫ ПРОДОЛЖИТЬ", Color.WHITE, 512 * 2, Align.center, true);
                game.startRU.draw(game.batch, glyphLayout, 0 + 40, 310 + glyphLayout.height / 2);
            }
        }

        if(startPlay) {
            if(game.en) {
                glyphLayout.setText(game.startEN, "TAP TO START", Color.WHITE, 512 * 2, Align.center, true);
                game.startEN.draw(game.batch, glyphLayout, 0, 310 + glyphLayout.height / 2);
            } else if(game.ru) {
                glyphLayout.setText(game.start40RU, "НАЖМИТЕ ЧТО БЫ НАЧАТЬ", Color.WHITE, 512 * 2, Align.center, true);
                game.start40RU.draw(game.batch, glyphLayout, 0+ 40, 310 + glyphLayout.height / 2);
            }
        }

        spriteCamera.update();
        game.batch.setProjectionMatrix(spriteCamera.combined);

        if(snipersStart) {
            sniperZone.drawBullet(game.batch);
        }
        player.drawPlayerAnimation(game.batch);
        game.batch.end();

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        player.drawPlayerCube(game.shapeRenderer); //draw PlayerCube
        if(onLine) {
            player.drawPlayerLine(game.shapeRenderer);
            player.Jump = false;
        }

        game.shapeRenderer.end();


        zoneCreator();

        if(!Application.playerAlive){
            game.ambientSound.stop();
        }

        //COLLISION
        collisionCheck(delta);
        //collisionDebug();

        //SECOND CHANCE
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        stage.act(delta);
        stage.draw();
        watchButton.setPosition(buttonX*2,buttonY*2);
        nopeButton.setPosition(buttonX*2,buttonY*2 - buttonHeight*2 - 8*2);

        if(secondChance){
           Gdx.input.setInputProcessor(this);
        }
        if(blockCounter >8){
            blockCounter = 8;
        }
        rebornHelper();
        makeReborn();
        if(game.android) {
            game.adsController.isRewardedRebornLoaded();
        }

        checkScoreAchievements();
    }
    private int counterHelper;
    private boolean counterCheked =  false;
    private void counterCheck() {
        if(blockCounter != 0 && !counterCheked) {
            counterHelper = blockCounter - 1;
            blockCounter = counterHelper;
            counterCheked = true;
        }
    }
    private void rebornHelper(){
        if(deathWall) {
            snipersStart = false;
            laserZoneStart = false;
            circlesStart = false;
            smallArrowStart = false;
            bigArrowBlockStart = false;
        }

        if(deathLaser) {
            circlesStart = false;
            snipersStart = false;
            smallArrowStart = false;
            startZoneStart = false;

            counterCheck();

            laserZoneOverlaped = false;
        }

        if(deathBigArrow) {
            laserZoneStart = false;
            circlesStart = false;
            snipersStart = false;
            smallArrowStart = false;
            startZoneStart = false;

        }

        if(deathCircle) {
            laserZoneStart = false;
            snipersStart = false;
            smallArrowStart = false;
            startZoneStart = false;

            counterCheck();

            circleZoneOverlaped = false;
        }
        if(game.deathSmallArow) {
            laserZoneStart = false;
            circlesStart = false;
            snipersStart = false;
            startZoneStart = false;

            counterCheck();

            smallArrowZoneOverlaped = false;
        }
        if(game.deathSnipers){
            laserZoneStart = false;
            circlesStart = false;
            smallArrowStart = false;
            startZoneStart = false;

            counterCheck();

            snipersZoneOverlaped = false;
        }
        if(secondChance) {
            deathWall = false;
            deathLaser = false;
            deathBigArrow = false;
            deathCircle = false;
            game.deathSmallArow = false;
            game.deathSnipers = false;

        }
    }
    private int squardWidth = 160+16 +8;
    private int squardHeight = 256-32;
    private int line = 3;
    private int btnLine = 2;

    private int buttonWidth = squardWidth - 64-16;
    private int buttonHeight = 32;

    private void makeReborn(){
        if (Application.reborn) {
            secondChance = true;

            game.ambientSound.setVolume(Application.volume * 0.1f);
            game.ambientSound.play();

            nopeButton.setVisible(false);
            watchButton.setVisible(false);
            if(startAfterDeath) {
                player.alive = true;
                Application.playerAlive = true;

                deathSound = false;
                player.deathPlayed = false;
            }
                if (deathWall) {
                    wall.secondChance(SPAWN);
                    corridor.init(start_corridor);
                }
                if (deathBigArrow) {
                    makeBigArrow();
                }
                if (deathCircle) {
                    makeCircle(SPAWN);
                }
                if (deathLaser) {
                    //makeLaser(SPAWN);
                    laser.secondChange(SPAWN);
                    //deathLaser = false;
                }
                if (game.deathSmallArow) {
                    //makeSmallArrow(SPAWN);
                    smallArrowZone.secondChance(SPAWN + 64);
                }
                if (game.deathSnipers) {
                    //makeSniper(SPAWN);
                    sniperZone.secondChance(SPAWN, game);
                }

                //Gdx.input.setInputProcessor(this);

                //player = new Player(96,139,game);
                player.secondChance(96, 139);
                Application.reborn = false;

        }
    }
    private void showAd(){
        if(Application.loadedReborn) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            game.shapeRenderer.setColor(19 / 255f, 22 / 255f, 22 / 255f, 0.6f);
            game.shapeRenderer.rect(0, 0, Application.V_WIDTH, Application.V_HEIGHT);
            game.shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);

            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            switchButtonColor();
            game.shapeRenderer.rect(512 / 2 - squardWidth / 2, 310 / 2 - squardHeight / 2, squardWidth, squardHeight);
            game.shapeRenderer.setColor(Globals.GreyBackgroundColor);
            game.shapeRenderer.rect(512 / 2 - squardWidth / 2 + line, 310 / 2 - squardHeight / 2 + line,
                    squardWidth - line * 2, squardHeight - line * 2);
            //Buttons
            switchButtonColor();
            game.shapeRenderer.rect(512 / 2 - squardWidth / 2 + (squardWidth - buttonWidth) / 2, 310 / 2 - squardHeight / 2 + 24,
                    buttonWidth, buttonHeight);

            game.shapeRenderer.rect(512 / 2 - squardWidth / 2 + (squardWidth - buttonWidth) / 2, 310 / 2 - squardHeight / 2 + 24 + buttonHeight + 8,
                    buttonWidth, buttonHeight);

            game.shapeRenderer.setColor(Color.BLACK);
            game.shapeRenderer.rect(512 / 2 - squardWidth / 2 + (squardWidth - buttonWidth) / 2 + btnLine, 310 / 2 - squardHeight / 2 + 24 + btnLine,
                    buttonWidth - btnLine * 2, buttonHeight - btnLine * 2);

            game.shapeRenderer.rect(512 / 2 - squardWidth / 2 + (squardWidth - buttonWidth) / 2 + btnLine, 310 / 2 - squardHeight / 2
                            + buttonHeight + 8 + 24 + btnLine,
                    buttonWidth - btnLine * 2, buttonHeight - btnLine * 2);

            buttonX = 512 / 2 - squardWidth / 2 + (squardWidth - buttonWidth) / 2;
            buttonY = 310 / 2 - squardHeight / 2 + buttonHeight + 8 + 24;
            game.shapeRenderer.end();

            camera.update();
            game.batch.setProjectionMatrix(camera.combined);

            game.batch.begin();
            glyphLayout.setText(game.bigNumbers, "" + counter, Color.WHITE, squardWidth * 2, Align.center, true);
            game.bigNumbers.draw(game.batch, glyphLayout, 512 - squardWidth, 310 + glyphLayout.height);
            if (game.en) {
                glyphLayout.setText(game.borderFont, "WATCH AD", Color.WHITE, squardWidth * 2, Align.center, true); // Посмотри рекламу
                game.borderFont.draw(game.batch, glyphLayout, 512 - squardWidth, 310 + squardHeight - glyphLayout.height);

                glyphLayout.setText(game.menu0Font, "FOR SECOND CHANCE", Color.WHITE, squardWidth * 2, Align.center, true); //b
                game.menu0Font.draw(game.batch, glyphLayout, 512 - squardWidth, 310 + squardHeight / 2 + glyphLayout.height);


                glyphLayout.setText(game.tutFont, "WATCH", Color.WHITE, buttonWidth * 2, Align.center, true);
                game.tutFont.draw(game.batch, glyphLayout, buttonX * 2, buttonY * 2 + buttonHeight * 2 - line * 2 - glyphLayout.height / 2);

                glyphLayout.setText(game.fontNope, "Nope.", Color.WHITE, buttonWidth * 2, Align.center, true);
                game.fontNope.draw(game.batch, glyphLayout, buttonX * 2, buttonY * 2 - glyphLayout.height - line - glyphLayout.height);
            } else if (game.ru) {
                glyphLayout.setText(game.borderFontRU, "Смотри рекламу", Color.WHITE, squardWidth * 2, Align.center, true); // Посмотри рекламу
                game.borderFontRU.draw(game.batch, glyphLayout, 512 - squardWidth, 310 + squardHeight - glyphLayout.height);

                glyphLayout.setText(game.menu0FontRu, "И ПОЛУЧИ ВТОРОЙ ШАНС", Color.WHITE, squardWidth * 2, Align.center, true); //b
                game.menu0FontRu.draw(game.batch, glyphLayout, 512 - squardWidth, 310 + squardHeight / 2 + glyphLayout.height);
                glyphLayout.setText(game.tutFont2RU, "Смотреть", Color.WHITE, buttonWidth * 2, Align.center, true);
                game.tutFont2RU.draw(game.batch, glyphLayout, buttonX * 2, buttonY * 2 + buttonHeight * 2 - line * 2 - glyphLayout.height / 2);

                glyphLayout.setText(game.fontNopeRu, "Неа.", Color.WHITE, buttonWidth * 2, Align.center, true);
                game.fontNopeRu.draw(game.batch, glyphLayout, buttonX * 2, buttonY * 2 - glyphLayout.height - line - glyphLayout.height);
            }
            game.batch.end();

        }
    }
    private void showRewardedRebornAd(){
        if (game.android) {
            game.adsController.showRewardedReborn(new Runnable() {
                @Override
                public void run() {
                    //System.out.println("RewardedVideo for reborn app closed");
                }
            });
        }
    }

    public void checkScoreAchievements(){
        if(game.android) {
            if (Score.gameScore == 14 && Score.bestScore < 14)
                game.gpgsController.unlockAchievement(game.achievement_novice_runner);

            if (Score.gameScore == 50 && Score.bestScore < 50)
                game.gpgsController.unlockAchievement(game.achievement_solid_runner);

            if (Score.gameScore == 88 && Score.bestScore < 88)
                game.gpgsController.unlockAchievement(game.achievement_like_a_forest);
        }
    }
    public void submitScore(){
        game.gpgsController.submitScore(Score.gameScore);
    }

    private void createButtons(){
        watchButton = new Button(new BaseDrawable());

        watchButton.setPosition(buttonX,buttonY);
        //watchButton.setPosition(buttonX*2, buttonY*2);
        watchButton.setSize(buttonWidth*2, buttonHeight*2);


        watchButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showRewardedRebornAd();
                if(!game.android){
                    Application.reborn = true;
                }
                startAfterDeath = true;
            }
        });

        nopeButton = new Button(new BaseDrawable());
        nopeButton.setSize(buttonWidth*2, buttonHeight*2);
        nopeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.gameOver);
                submitScore();
                //deathScreen();

                showInterstitialAd();
            }
        });
        stage.addActor(watchButton);
        stage.addActor(nopeButton);
    }
    private void switchButtonColor(){
        switch (Application.gameSkin) {
            case 0:game.shapeRenderer.setColor(Globals.SidesColor);
                break;
            case 1:game.shapeRenderer.setColor(Globals.Background1Color);
                break;
            case 2:game.shapeRenderer.setColor(Globals.Inner2Color);
                break;
            case 3:game.shapeRenderer.setColor(Globals.Background3Color);
                break;
            case 4:game.shapeRenderer.setColor(Globals.Background4Color);
                break;
        }
    }


    public void update(float delta) {
        if(!startAfterDeath && !startPlay) {
            player.update(delta);
            if (startZoneStart) {
                wall.update(delta);
                corridor.update(delta);
            }
            if (laserZoneStart) {
                laser.update(delta);
            }
            if (circlesStart) {
                circle.update(delta);
            }
            if (snipersStart) {
                sniperZone.update(delta);
            }
            if (smallArrowStart) {
                smallArrowZone.update(delta);
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
        //game.setScreen(game.mainMenuScreen);
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }


    private int zoneCreate;

    private boolean laserZoneOverlaped = false, circleZoneOverlaped = false, snipersZoneOverlaped = false,
    smallArrowZoneOverlaped = false, startOverlaped = false;

    // for MONEY and endZoneCheck
    private boolean startEndCheck = false, laserEndCheck = false, circleEndCheck = false, snipersEndCheck = false,
    smallArrowEndCheck = false;


    private void zoneCreator(){
        if(player.alive) {
            if (player.getPlayerRectangle().overlaps(corridor.getEndZone()) && !startEndCheck) {
                countMoney();
                // 1 - START 2 - LASER 3 - BIG_ARROW 4 - CIRCLE 5 - SMALL_ARROW 6 - SNIPERS
                if (nextZoneRandom == 1) {
                    makeBigArrow();
                }
                switch (nextZoneRandom) {
                    case 1: sidesDeath = 3;
                        break;
                    case 2: sidesDeath = 2;
                        break;
                    case 3: sidesDeath = 4;
                        break;
                    case 4: sidesDeath = 6;
                        break;
                    case 5: sidesDeath = 5;
                        break;
                }
                startEndCheck = true;
                startOverlaped = false;
            }
            if (player.getPlayerRectangle().overlaps(wall.getZoneRect()) && !startOverlaped) {

                blockCounter = 0;

                bigArrowBlockStart = false;
                bigArrowEnd = false;

                laserEndCheck = false;
                circleEndCheck= false;
                startEndCheck = false;
                snipersEndCheck = false;
                smallArrowEndCheck = false;

                circleZoneOverlaped = false;
                snipersZoneOverlaped = false;
                smallArrowZoneOverlaped = false;
                laserZoneOverlaped = false;

                startOverlaped = true;

                nextZoneRandom = random.nextInt(5) + 1; // создаем зону после стартового блока
                if (nextZoneRandom == 1) {
                    //System.out.println("NEXT ZONE: BIG ARROWS");
                } else if (nextZoneRandom == 2) {
                    //System.out.println("NEXT ZONE: LASERS");
                    //if(secondChance) makeLaser(start_corridor + corridor.BLOCK_SIZE + 512);
                    //else
                    makeLaser(start_corridor + corridor.BLOCK_SIZE - 96);
                } else if (nextZoneRandom == 3) {
                    //System.out.println("NEXT ZONE: CIRCLES");
                    //if(secondChance) makeCircle(start_corridor + corridor.BLOCK_SIZE +512);
                    //else
                    makeCircle(start_corridor + corridor.BLOCK_SIZE - 96);
                } else if (nextZoneRandom == 4) {
                    //System.out.println("NEXT ZONE: SNIPERS");
                    //if(secondChance) makeSniper(start_corridor + corridor.BLOCK_SIZE + 512);
                    //else
                    makeSniper(start_corridor + corridor.BLOCK_SIZE - 96);
                } else if (nextZoneRandom == 5) {
                    //System.out.println("NEXT ZONE: SMALL ARROWS");
                    //if(secondChance) makeSmallArrow((start_corridor + corridor.BLOCK_SIZE +512) * 2);
                    //else
                    makeSmallArrow((start_corridor + corridor.BLOCK_SIZE - 96) * 2);
                }

            }
            createBigArrowZone(); // BIG ARROW
            createLaserZone(); // LASER
            createCircleZone(); //CIRCLE
            createSniperZone(); //SNIPER
            createSmallArrowZone(); //Small Arrow
        }
    }
    private void createBigArrowZone(){
        if(bigArrowEnd) {
            bigArrowBlockStart = false;
            bigArrowEnd = false;

            laserEndCheck = false;
            circleEndCheck= false;
            startEndCheck = false;
            snipersEndCheck = false;
            smallArrowEndCheck = false;

            circleZoneOverlaped = false;
            snipersZoneOverlaped = false;
            smallArrowZoneOverlaped = false;
            laserZoneOverlaped = false;
            startOverlaped = false;


            countMoney();

            blockCounter +=1;
            if(blockCounter != 8) {
                zoneCreate = random.nextInt(4) + 1;
                if (zoneCreate == 1) {
                    makeLaser(512 + 64);
                } else if (zoneCreate == 2) {
                    makeCircle(512+64);
                } else if (zoneCreate == 3) {
                    makeSniper(512 + 64);
                } else if (zoneCreate == 4) {
                    makeSmallArrow(512 + 64);
                }
            } else if(blockCounter == 8) {
                makeStart(SPAWN + 64);
                sidesDeath = 1;
            }
            switch (zoneCreate) {
                // 1 - START 2 - LASER 3 - BIG_ARROW 4 - CIRCLE 5 - SMALL_ARROW 6 - SNIPERS
                case 1: sidesDeath = 2;
                    break;
                case 2: sidesDeath = 4;
                    break;
                case 3: sidesDeath = 6;
                    break;
                case 4: sidesDeath = 5;
                    break;
            }
        }
    }
    private void createLaserZone(){
        if(laserZoneStart) {
            if (player.getPlayerRectangle().overlaps(laser.getStartZone()) && !laserZoneOverlaped) {
                laserZoneOverlaped = true;

                bigArrowBlockStart = false;
                bigArrowEnd = false;

                circleEndCheck = false;
                startEndCheck = false;
                snipersEndCheck = false;
                smallArrowEndCheck = false;

                snipersZoneOverlaped = false;
                smallArrowZoneOverlaped = false;
                circleZoneOverlaped = false;
                startOverlaped = false;


                blockCounter += 1;
                if (blockCounter != 8) {
                    zoneCreate = random.nextInt(4) + 1;
                    if (zoneCreate == 2) {
                        makeCircle(laser.BLOCK_ZONE + 128 + 256);
                    }
                    if (zoneCreate == 3) {
                        makeSniper(laser.BLOCK_ZONE + 128 + 256);
                    }
                    if (zoneCreate == 4) {
                        makeSmallArrow((laser.BLOCK_ZONE + 128 + 256) * 2);
                    }
                }
                if (blockCounter == 8) {
                    makeStart(laser.BLOCK_ZONE + 128 + 256);
                    sidesDeath = 1;
                }
            }

            if (player.getPlayerRectangle().overlaps(laser.getEndZone()) && !laserEndCheck && laserZoneStart) {
                if (zoneCreate == 1 && blockCounter != 8) {
                    //System.out.println("BIG ARROW START");
                    makeBigArrow();
                    switch (zoneCreate) {
                        // 1 - START 2 - LASER 3 - BIG_ARROW 4 - CIRCLE 5 - SMALL_ARROW 6 - SNIPERS
                        case 1: sidesDeath = 3;
                            break;
                        case 2: sidesDeath = 4;
                            break;
                        case 3: sidesDeath = 6;
                            break;
                        case 4: sidesDeath = 5;
                            break;
                    }
                }
                countMoney();
                laserEndCheck = true;
            }
        }
    }
    private void createCircleZone(){
        if(circlesStart) {
            if (player.getPlayerRectangle().overlaps(circle.getStartZone()) && !circleZoneOverlaped) {
                circleZoneOverlaped = true;

                bigArrowBlockStart = false;
                bigArrowEnd = false;

                laserEndCheck = false;
                startEndCheck = false;
                snipersEndCheck = false;
                smallArrowEndCheck = false;

                snipersZoneOverlaped = false;
                smallArrowZoneOverlaped = false;
                laserZoneOverlaped = false;
                startOverlaped = false;

                blockCounter += 1;

                if (blockCounter != 8) {
                    zoneCreate = random.nextInt(4) + 1;
                    if (zoneCreate == 2) {
                        makeLaser(circle.BLOCK_SIZE + 128 + 256);
                    }
                    if (zoneCreate == 3) {
                        makeSniper(circle.BLOCK_SIZE + 128 + 256);
                    }
                    if (zoneCreate == 4) {
                        makeSmallArrow((circle.BLOCK_SIZE + 128 + 256) * 2);
                    }
                } else if (blockCounter == 8) {
                    makeStart(circle.BLOCK_SIZE + 128 + 256);
                    sidesDeath = 1;
                }
            }

            if (player.getPlayerRectangle().overlaps(circle.getEndZone()) && !circleEndCheck) {
                if (zoneCreate == 1 && blockCounter != 8) {
                    makeBigArrow();
                }
                switch (zoneCreate) {
                    // 1 - START 2 - LASER 3 - BIG_ARROW 4 - CIRCLE 5 - SMALL_ARROW 6 - SNIPERS
                    case 1: sidesDeath = 3;
                        break;
                    case 2: sidesDeath = 2;
                        break;
                    case 3: sidesDeath = 6;
                        break;
                    case 4: sidesDeath = 5;
                        break;
                }
                countMoney();
                circleEndCheck = true;
            }
        }
    }
    private void createSniperZone(){
        if(player.getPlayerRectangle().overlaps(sniperZone.getStartZone()) && !snipersZoneOverlaped){
            snipersZoneOverlaped = true;

            bigArrowBlockStart = false;
            bigArrowEnd = false;

            laserEndCheck = false;
            circleEndCheck= false;
            startEndCheck = false;
            smallArrowEndCheck = false;

            circleZoneOverlaped = false;
            smallArrowZoneOverlaped = false;
            laserZoneOverlaped = false;
            startOverlaped = false;

            blockCounter += 1;


            if(blockCounter != 8) {
                zoneCreate = random.nextInt(4) + 1;
                if (zoneCreate == 2) {
                    makeLaser(sniperZone.BLOCK_SIZE + 128 + 256);
                }
                if (zoneCreate == 3) {
                    makeCircle(sniperZone.BLOCK_SIZE + 128 + 256);
                }
                if (zoneCreate == 4) {
                    makeSmallArrow((sniperZone.BLOCK_SIZE + 128 + 256) * 2);
                }
            } else if(blockCounter == 8) {
                makeStart(sniperZone.BLOCK_SIZE + 128 + 256);
                sidesDeath = 1;
            }
        }

        if(player.getPlayerRectangle().overlaps(sniperZone.getEndZone()) &&!snipersEndCheck){
            if (zoneCreate == 1 && blockCounter != 8) {
               makeBigArrow();
            }
            switch (zoneCreate) {
                // 1 - START 2 - LASER 3 - BIG_ARROW 4 - CIRCLE 5 - SMALL_ARROW 6 - SNIPERS
                case 1: sidesDeath = 3;
                    break;
                case 2: sidesDeath = 2;
                    break;
                case 3: sidesDeath = 4;
                    break;
                case 4: sidesDeath = 5;
                    break;
            }
            countMoney();
            snipersEndCheck = true;
        }
    }
    private void createSmallArrowZone(){
        if(player.getPlayerRectangle().overlaps(smallArrowZone.getStartZone()) && !smallArrowZoneOverlaped) {
            smallArrowZoneOverlaped = true;

            bigArrowBlockStart = false;
            bigArrowEnd = false;

            laserEndCheck = false;
            circleEndCheck= false;
            startEndCheck = false;
            snipersEndCheck = false;

            circleZoneOverlaped = false;
            snipersZoneOverlaped = false;
            laserZoneOverlaped = false;
            startOverlaped = false;

            blockCounter += 1;

            if(blockCounter != 8) {
                zoneCreate = random.nextInt(4) + 1;
                if (zoneCreate == 2) {
                    makeLaser(smallArrowZone.BLOCK_SIZE / 2 + 512);
                }
                if (zoneCreate == 3) {
                    makeCircle(smallArrowZone.BLOCK_SIZE / 2 + 512);
                }
                if (zoneCreate == 4) {
                    makeSniper(smallArrowZone.BLOCK_SIZE / 2 + 512);
                }
            } else if(blockCounter == 8){
                makeStart(smallArrowZone.BLOCK_SIZE / 2 + 512);
                sidesDeath = 1;
            }
        }
        if(player.getPlayerRectangle().overlaps(smallArrowZone.getEndZone()) &&!smallArrowEndCheck){
            if (zoneCreate == 1  && blockCounter != 8) {
                makeBigArrow();
            }
            switch (zoneCreate) {
                // 1 - START 2 - LASER 3 - BIG_ARROW 4 - CIRCLE 5 - SMALL_ARROW 6 - SNIPERS
                case 1: sidesDeath = 3;
                    break;
                case 2: sidesDeath = 2;
                    break;
                case 3: sidesDeath = 4;
                    break;
                case 4: sidesDeath = 6;
                    break;
            }
            // Money
            countMoney();
            smallArrowEndCheck = true;
        }
    }

    private void resetZoneCreator(){
        bigArrowBlockStart = false;
        bigArrowEnd = false;

        laserEndCheck = false;
        circleEndCheck= false;
        startEndCheck = false;
        snipersEndCheck = false;
        smallArrowEndCheck = false;

        circleZoneOverlaped = false;
        snipersZoneOverlaped = false;
        smallArrowZoneOverlaped = false;
        laserZoneOverlaped = false;
        startOverlaped = false;

        circlesStart = false;
        snipersStart = false;
        smallArrowStart = false;
        laserZoneStart = false;
        startZoneStart = false;
    }

    private void makeStart(int lastZone){
        wall.init(lastZone);
        corridor.init(lastZone +start_corridor - 512);

        startZoneStart = true;
        startOverlaped = false;
    }
    private void makeBigArrow(){
        bigArrowBlockStart = true;
        bigArrow.init();
        amountOfBigArrows = random.nextInt(3) + 3;
        bigArrowsCount = 0;
    }
    private void makeLaser(int lastZone){
        laser.init(lastZone, game);

        laserZoneStart = true;
        laserCreated = true;

        laserZoneOverlaped = false;
    }
    private void makeCircle(int lastZone){
        circle.init(lastZone);
        circlesStart = true;
        circleCreated = true;

        circleZoneOverlaped = false;
    }
    private void makeSniper(int lastZone){
        sniperZone.init(lastZone, game);
        snipersStart = true;
        sniperCreated = true;

        snipersZoneOverlaped = false;

    }
    private void makeSmallArrow(int lastZone){
        smallArrowZone.init(lastZone);
        smallArrowStart = true;
        smallArrowCreated = true;

        smallArrowZoneOverlaped = false;
    }

    private boolean calculatedDeath = false;
    // COLLISION
    public void collisionDebug(){
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        game.shapeRenderer.setColor(0f,0f,1f,1f);
        //WALLS endZone
        game.shapeRenderer.rect(wall.getZoneRect().getX(),wall.getZoneRect().getY(), wall.getZoneRect().getWidth(),
                wall.getZoneRect().getHeight());
        //CORRIDOR
        for(int i=0; i< corridor.getBottomRets().length; i++) {
            game.shapeRenderer.rect(corridor.getBottomRets()[i].getX(),corridor.getBottomRets()[i].getY(),
                    corridor.getBottomRets()[i].getWidth(), corridor.getBottomRets()[i].getHeight());
        }
        for(int i =0; i < corridor.getTopLeftRects().length; i++){
            game.shapeRenderer.rect(corridor.getTopRightRects()[i].getX(),corridor.getTopRightRects()[i].getY(),
                    corridor.getTopRightRects()[i].getWidth(),corridor.getTopRightRects()[i].getHeight());
            game.shapeRenderer.rect(corridor.getTopLeftRects()[i].getX(),corridor.getTopLeftRects()[i].getY(),
                    corridor.getTopLeftRects()[i].getWidth(),corridor.getTopLeftRects()[i].getHeight());
        }

        //CORRIDOR endZone
        game.shapeRenderer.rect(corridor.getEndZone().getX(), corridor.getEndZone().getY(),
               corridor.getEndZone().getWidth(), corridor.getEndZone().getHeight());

        //POLYGONS
        game.shapeRenderer.polygon(player.getPlayerPolygon().getTransformedVertices());
        game.shapeRenderer.polygon(bigArrow.getArrowPolygon().getTransformedVertices());
        game.shapeRenderer.polygon(bigArrow.getArrowPolygon2().getTransformedVertices());

        // LASER
        if(laserZoneStart) {
            for (int i = 0; i < laser.getDownWall().length; i++) {
                game.shapeRenderer.setColor(0f, 0f, 1f, 1f);
                game.shapeRenderer.rect(laser.getTopWall()[i].getX(), laser.getTopWall()[i].getY(),
                        laser.getTopWall()[i].getWidth(), laser.getTopWall()[i].getHeight());
                game.shapeRenderer.rect(laser.getDownWall()[i].getX(), laser.getDownWall()[i].getY(),
                        laser.getDownWall()[i].getWidth(), laser.getDownWall()[i].getHeight());

                game.shapeRenderer.rect(laser.getOrangeLaserRect()[i].getX(), laser.getOrangeLaserRect()[i].getY(),
                        laser.getOrangeLaserRect()[i].getWidth(), laser.getOrangeLaserRect()[i].getHeight());

                game.shapeRenderer.setColor(1f, 0f, 0f, 1f);

                game.shapeRenderer.rect(laser.getStartZone().getX(), laser.getEndZone().getY(),
                        laser.getStartZone().getWidth(), laser.getStartZone().getHeight());

                game.shapeRenderer.rect(laser.getBlueLaserRect()[i].getX(), laser.getBlueLaserRect()[i].getY(),
                        laser.getBlueLaserRect()[i].getWidth(), laser.getBlueLaserRect()[i].getHeight());

            }
        }

        //CIRCLES
        if(circlesStart) {
            game.shapeRenderer.setColor(0f, 0f, 1f, 1f);
            for (int i = 0; i < circle.getMiddleRect().length; i++) {
                game.shapeRenderer.rect(circle.getMiddleRect()[i].getX(), circle.getMiddleRect()[i].getY(),
                        circle.getMiddleRect()[i].getWidth(), circle.getMiddleRect()[i].getHeight());
            }
            for (int i = 0; i < circle.getTopRect().length; i++) {
                game.shapeRenderer.rect(circle.getTopRect()[i].getX(), circle.getTopRect()[i].getY(),
                        circle.getTopRect()[i].getWidth(), circle.getTopRect()[i].getHeight());
                game.shapeRenderer.rect(circle.getBotRect()[i].getX(), circle.getBotRect()[i].getY(),
                        circle.getBotRect()[i].getWidth(), circle.getBotRect()[i].getHeight());
            }
        game.shapeRenderer.setColor(1f,0f,0f,1f);

        game.shapeRenderer.rect(circle.getStartZone().getX(), circle.getStartZone().getY(),
                    circle.getStartZone().getWidth(),circle.getStartZone().getHeight());

        game.shapeRenderer.rect(circle.getEndZone().getX(),circle.getEndZone().getY(),
                circle.getEndZone().getWidth(),circle.getEndZone().getHeight());
        }
        game.shapeRenderer.end();

        // LASER
        if(laserZoneStart) {
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            game.shapeRenderer.rect(laser.getEndZone().getX(), laser.getEndZone().getY(),
                    laser.getEndZone().getWidth(), laser.getEndZone().getHeight());

            game.shapeRenderer.end();
        }
        // SNIPER
        if(snipersStart) {
            sniperZone.collisionDebug(game.shapeRenderer);
        }
        if(smallArrowStart) {
            smallArrowZone.collisionDebug(game.shapeRenderer);
        }
    }
    private void collisionCheck(float delta){
        //WHITE SIDES
        if(player.sidesCollision()) {
            playerDeath();
            // 1 - START 2 - LASER 3 - BIG_ARROW 4 - CIRCLE 5 - SMALL_ARROW 6 - SNIPERS
            switch (sidesDeath) {
                case 1: deathWall = true;
                    break;
                case 2: deathLaser = true;
                    break;
                case 3: deathBigArrow = true;
                    break;
                case 4: deathCircle = true;
                    break;
                case 5: game.deathSmallArow = true;
                    break;
                case 6: game.deathSnipers = true;
                    break;
            }
        }
        if(startZoneStart) {
            //WALL
            for (int i = 0; i < wall.getMassiveRect().length; i++) {
                if (player.getPlayerRectangle().overlaps(wall.getMassiveRect()[i]) ||
                        player.getPlayerRectangle().overlaps(wall.getMassiveRect2()[i]) && player.alive) {
                    playerDeath();
                    if(!secondChance) {
                        deathWall = true;
                    }
                }
            }
            //CORRIDOR
            for(int i=0; i< corridor.getTopLeftRects().length; i++) { //between corridor top
                if (player.getPlayerRectangle().overlaps(corridor.getTopLeftRects()[i]) ||
                        player.getPlayerRectangle().overlaps(corridor.getTopRightRects()[i]) && player.alive){
                    playerDeath();
                    if(!secondChance) {
                        deathWall = true;
                    }
                }
            }
            for(int i=0; i < corridor.getBottomRets().length;i++) {
                if(player.getPlayerRectangle().overlaps(corridor.getBottomRets()[i]) && player.alive) { // between corridor bottom
                    playerDeath();
                    if(!secondChance) {
                        deathWall = true;
                    }
                }
            }
        }

        //BIG ARROW check collision
        if(bigArrowBlockStart) {
            if (player.getPlayerRectangle().overlaps(bigArrow.getLineRectangle()) && player.alive) { // between line
                playerDeath();
                if(!secondChance) {
                    deathBigArrow = true;
                }
                //System.out.println("Collision: BIG ARROW Line");
            }
            if (player.getPlayerRectangle().overlaps(bigArrow.getLine2Rectangle())&& player.alive) {
                playerDeath();
                if(!secondChance) {
                    deathBigArrow = true;
                }
                //System.out.println("Collision: BIG ARROW Line2");
            }
            if (Intersector.overlapConvexPolygons(player.getPlayerPolygon(), bigArrow.getArrowPolygon())&& player.alive) {
                playerDeath();
                if(!secondChance) {
                    deathBigArrow = true;
                }
                //System.out.println("Collision: BIG ARROW Polygon");
            }
            if (Intersector.overlapConvexPolygons(player.getPlayerPolygon(), bigArrow.getArrowPolygon2())&& player.alive) {
                playerDeath();
                if(!secondChance) {
                    deathBigArrow = true;
                }
                //System.out.println("Collision: BIG ARROW Polygon2");
            }
        }
        //LaserCollision
        if(laserZoneStart) {
            for (int i = 0; i < laser.getTopWall().length; i++) {
                if (player.getPlayerRectangle().overlaps(laser.getTopWall()[i]) ||
                        player.getPlayerRectangle().overlaps(laser.getDownWall()[i]) && player.alive) {
                    playerDeath();
                    if(!secondChance) {
                        deathLaser = true;
                    }
                    //System.out.println("Collision: LASER WALL");
                }
                // проверяем оранжевый лазер, если на линии -> смерть
                if (player.getPlayerRectangle().overlaps(laser.getOrangeLaserRect()[i]) && player.Line && player.alive) {
                    playerDeath();
                    if(!secondChance) {
                        deathLaser = true;
                    }
                    //System.out.println("Collision: Orange LASER");
                }
                // проверяем голубой лазер, если состояние в прыжке -> смерть
                if (player.getPlayerRectangle().overlaps(laser.getBlueLaserRect()[i]) && player.Jump && player.alive) {
                    playerDeath();
                    if(!secondChance) {
                        deathLaser = true;
                    }
                    //System.out.println("Collision: Blue LASER");
                }
            }
        }
        //Circles
        if(circlesStart) {
            for (int i = 0; i < circle.getMiddleRect().length; i++) {
                if (player.getPlayerRectangle().overlaps(circle.getMiddleRect()[i]) && player.alive) {
                    playerDeath();
                    if(!secondChance) {
                        deathCircle = true;
                    }
                    //System.out.println("Collision: Middle CIRCLE");
                }
            }
            for (int i = 0; i < circle.getTopRect().length; i++) {
                if (player.getPlayerRectangle().overlaps(circle.getTopRect()[i]) ||
                        player.getPlayerRectangle().overlaps(circle.getBotRect()[i]) && player.alive) {
                    playerDeath();
                    if(!secondChance) {
                        deathCircle = true;
                    }
                    //System.out.println("Collision: Botz or Top CIRCLE");
                }
            }
        }
        //Sniper
        if(snipersStart) {
            sniperZone.checkCollision(player.getPlayerRectangle(), game, player);
            //if(!secondChance) {
            //    deathSnipers = true;
            //}
        }
        //SmallArrow
        if(smallArrowStart) {
            smallArrowZone.checkCollision(player.getPlayerRectangle(), game, player);
            //if(!secondChance) {
            //   deathSmallArow = true;
            //}
        }

        if(player.getPosition().y <= -64*2){
            // android //TODO: uncomment when finish ad logic
                if(!secondChance && Application.loadedReborn  && Score.gameScore >= 14 ) {
                //if(!secondChance) { // desktop
                    nopeButton.setVisible(true);
                    watchButton.setVisible(true);
                    Gdx.input.setInputProcessor(stage);

                    showAd();
                    if(!calculatedDeath) {
                        game.countDeathAmount();
                        calculatedDeath = true;
                    }
                    if(Application.loadedReborn) {
                        time -= delta;
                        if (time >= 2 && time <= 3) {
                            counter = 3;
                        } else if (time >= 1 && time <= 2) {
                            counter = 2;
                        } else if (time >= 0 && time <= 1) {
                            counter = 1;
                        } else {
                            counter = 0;
                            game.setScreen(game.gameOver);
                            submitScore();
                            showInterstitialAd();
                        }
                    } else {
                        game.setScreen(game.gameOver);
                        submitScore();
                    }
                } else if(secondChance) {
                    game.gameScreen.pause();
                    game.setScreen(game.gameOver);
                    submitScore();
                    game.countDeathAmount();
                    showInterstitialAd();
                    secondChance = false;
                } else if(!Application.reborn) {
                    game.setScreen(game.gameOver);
                    submitScore();
                }
        }
    }


    private boolean deathSound = false;
    private void playerDeath(){
        player.alive = false;
        Application.playerAlive = false;
        player.deathAnimation();
        if(!deathSound) {
            game.deathAllSound.play(Application.volume);
            deathSound = true;
        }
    }

    private void showInterstitialAd(){
        if(game.showInterstitialAd && game.android) {
            game.adsController.showInterstitialAd(new Runnable() {
                @Override
                public void run() {
                    //System.out.println("Interstitial app closed");
                }
            });
            game.showInterstitialAd = false;
        }
    }

    boolean bigArrowEnd = false;
    private void bigArrowLogic(float delta) {
        if (bigArrowBlockStart) {
            //DRAW BIG ARROW LINE
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            if (!bigArrow.playEffect) {
                bigArrow.drawArrowLine(game.shapeRenderer);
            }
            if (!bigArrow.playEffect2) {
                bigArrow.drawArrow2Line(game.shapeRenderer);
            }
            game.shapeRenderer.end();

            //DRAW BIG ARROW
            game.batch.begin();
            bigArrow.drawArrows(game.batch);
            bigArrow.arrowEffect(delta);
            if (bigArrow.secondStart) {
                bigArrow.arrowEffect2(delta);
            }
            if (!bigArrow.playEffect) {
                bigArrow.update(delta);
            }
            if (!bigArrow.playEffect2) {
                bigArrow.update2(delta);
            }
            game.batch.end();

            // random amount 3-5
            if (bigArrow.arrow2.getX() >= bigArrow.INVISIBLE && bigArrowsCount < amountOfBigArrows) {
                bigArrow.init();
                bigArrowsCount++;
                //System.out.println("number of arrow" + bigArrowsCount);
            } else if(bigArrowsCount == amountOfBigArrows){
                bigArrowEnd = true;
            }
        }
    }

    private void countMoney(){
        blocksNumber +=1;

        currency.countCurency(blocksNumber, game);

        //Currency.addMoneyToCurrency();
        //System.out.println("Money: " + Currency.Money);
        //System.out.println("Currency: " + Currency.currency);
    }

    private boolean onLine = false;

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode){
            case Input.Keys.SHIFT_LEFT:
                game.setScreen(new GameScreen(game));
                break;
            case Input.Keys.Z:
                player.onClick(game);
                break;
            case Input.Keys.Q:
                //collisionCheck(delta);
                break;
            case Input.Keys.X:
                player.onLine(game);
                onLine = true;
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode){
            case Input.Keys.X:
                player.onRelease();
                onLine = false;
                break;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(pointer == 0) {
            player.onClick(game);
            //if(Application.playerAlive) {
            //    game.jumpSound.play(Application.volume);
            //}
        } else if(pointer == 1){
            player.onLine(game);
            onLine = true;
        } else{
            onLine = false;
        }
        if(startAfterDeath) {
            startAfterDeath = false;
        }
        if(startPlay) {
            startPlay = false;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(pointer == 1){
            player.onRelease();
            onLine = false;
        } else if(pointer == 0){
            player.onRelease();
            onLine = false;
        }
        return true;
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
