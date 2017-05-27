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
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
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

    //private Stage stage;
    private Viewport viewport;
    private Viewport spriteViewport;

    //private MyInputProcessor inputProcessor;


    public GameScreen(Application game) {
        //System.out.println();
        //System.out.println("GameScreen");
        this.game = game;
        this.camera = game.camera;

        spriteCamera = new OrthographicCamera();
        spriteCamera.setToOrtho(false, Application.V_WIDTH, Application.V_HEIGHT);

        random = new Random();

        glyphLayout = new GlyphLayout();

        //inputProcessor = new MyInputProcessor();

        viewport = new StretchViewport(Application.V_WIDTH*2,Application.V_HEIGHT*2,camera);
        viewport.apply();

        spriteViewport = new StretchViewport(Application.V_WIDTH, Application.V_HEIGHT, spriteCamera);
        spriteViewport.apply();

        spriteCamera.position.set(camera.viewportWidth/2 - 256,camera.viewportHeight /2 - 155,0);
    }

    //Laser laser;
    @Override
    public void show() {
        //Gdx.input.setInputProcessor(inputProcessor);
        Gdx.input.setInputProcessor(this);

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

        //test
        test = true;
    }

    private boolean test = false;

    private float timer = TimeUtils.nanoTime();
    private float time = 0;

    int calls;

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteCamera.update();
        game.batch.setProjectionMatrix(spriteCamera.combined);

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
        game.font10.draw(game.batch, "fps:" + Gdx.graphics.getFramesPerSecond(), 0, (288 - 24)*2);
        game.font10.draw(game.batch, "blocks:" + blocksNumber, 0, (288 - 24 - 12)*2);
        game.font10.draw(game.batch, "money:" + Currency.Money, 0, (288 - 24 - 12 * 2)*2);
        game.font10.draw(game.batch, "counter:" + blockCounter, 0, (288 - 24 - 12 * 3)*2);

        glyphLayout.setText(game.borderFont, ""+ Score.getGameScore() , Color.WHITE,512*2, Align.center, true);
        game.borderFont.draw(game.batch, glyphLayout, 0, (11 + 288 - 4)*2);

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

        //COLLISION
        //collisionCheck();
        //collisionDebug();
        game.ambientSound.setVolume(Application.volume);
        game.ambientSound.play();
        if(!Application.playerAlive){
            game.ambientSound.stop();
        }
    }

    public void update(float delta) {
        player.update(delta);
        if(startZoneStart) {
            wall.update(delta);
            corridor.update(delta);
        }
        if(laserZoneStart) {
            laser.update(delta);
        }
        if(circlesStart) {
            circle.update(delta);
        }
        if(snipersStart) {
            sniperZone.update(delta);
        }
        if(smallArrowStart) {
            smallArrowZone.update(delta);
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
                if (nextZoneRandom == 1) {
                    makeBigArrow();
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
                    makeLaser(start_corridor + corridor.BLOCK_SIZE - 96);
                } else if (nextZoneRandom == 3) {
                    //System.out.println("NEXT ZONE: CIRCLES");
                    makeCircle(start_corridor + corridor.BLOCK_SIZE - 96);
                } else if (nextZoneRandom == 4) {
                    //System.out.println("NEXT ZONE: SNIPERS");
                    makeSniper(start_corridor + corridor.BLOCK_SIZE - 96);
                } else if (nextZoneRandom == 5) {
                    //System.out.println("NEXT ZONE: SMALL ARROWS");
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
                } else if (blockCounter == 8) {
                    makeStart(laser.BLOCK_ZONE + 128 + 256);
                }
            }

            if (player.getPlayerRectangle().overlaps(laser.getEndZone()) && !laserEndCheck && laserZoneStart) {
                if (zoneCreate == 1 && blockCounter != 8) {
                    //System.out.println("BIG ARROW START");
                    makeBigArrow();
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
                }
            }

            if (player.getPlayerRectangle().overlaps(circle.getEndZone()) && !circleEndCheck) {
                if (zoneCreate == 1 && blockCounter != 8) {
                    makeBigArrow();
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
            }
        }

        if(player.getPlayerRectangle().overlaps(sniperZone.getEndZone()) &&!snipersEndCheck){
            if (zoneCreate == 1 && blockCounter != 8) {
               makeBigArrow();
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
            }
        }
        if(player.getPlayerRectangle().overlaps(smallArrowZone.getEndZone()) &&!smallArrowEndCheck){
            if (zoneCreate == 1  && blockCounter != 8) {
                makeBigArrow();
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
    private void collisionCheck(){
        //WHITE SIDES
        if(player.sidesCollision()) {
            playerWallDeath();
        }
        if(startZoneStart) {
            //WALL
            for (int i = 0; i < wall.getMassiveRect().length; i++) {
                if (player.getPlayerRectangle().overlaps(wall.getMassiveRect()[i]) ||
                        player.getPlayerRectangle().overlaps(wall.getMassiveRect2()[i]) && player.alive) {
                    playerWallDeath();
                }
            }
            //CORRIDOR
            for(int i=0; i< corridor.getTopLeftRects().length; i++) { //between corridor top
                if (player.getPlayerRectangle().overlaps(corridor.getTopLeftRects()[i]) ||
                        player.getPlayerRectangle().overlaps(corridor.getTopRightRects()[i]) && player.alive){
                    playerWallDeath();
                }
            }
            for(int i=0; i < corridor.getBottomRets().length;i++) {
                if(player.getPlayerRectangle().overlaps(corridor.getBottomRets()[i]) && player.alive) { // between corridor bottom
                    playerWallDeath();
                }
            }
        }

        //BIG ARROW check collision
        if(bigArrowBlockStart) {
            if (player.getPlayerRectangle().overlaps(bigArrow.getLineRectangle()) && player.alive) { // between line
                playerDeath();
                //System.out.println("Collision: BIG ARROW Line");
            }
            if (player.getPlayerRectangle().overlaps(bigArrow.getLine2Rectangle())&& player.alive) {
                playerDeath();
                //System.out.println("Collision: BIG ARROW Line2");
            }
            if (Intersector.overlapConvexPolygons(player.getPlayerPolygon(), bigArrow.getArrowPolygon())&& player.alive) {
                playerDeath();
                //System.out.println("Collision: BIG ARROW Polygon");
            }
            if (Intersector.overlapConvexPolygons(player.getPlayerPolygon(), bigArrow.getArrowPolygon2())&& player.alive) {
                playerDeath();
                //System.out.println("Collision: BIG ARROW Polygon2");
            }
        }
        //LaserCollision
        if(laserZoneStart) {
            for (int i = 0; i < laser.getTopWall().length; i++) {
                if (player.getPlayerRectangle().overlaps(laser.getTopWall()[i]) ||
                        player.getPlayerRectangle().overlaps(laser.getDownWall()[i]) && player.alive) {
                    playerDeath();
                    //System.out.println("Collision: LASER WALL");
                }
                // проверяем оранжевый лазер, если на линии -> смерть
                if (player.getPlayerRectangle().overlaps(laser.getOrangeLaserRect()[i]) && player.Line && player.alive) {
                    playerDeath();
                    //System.out.println("Collision: Orange LASER");
                }
                // проверяем голубой лазер, если состояние в прыжке -> смерть
                if (player.getPlayerRectangle().overlaps(laser.getBlueLaserRect()[i]) && player.Jump && player.alive) {
                    playerDeath();
                    //System.out.println("Collision: Blue LASER");
                }
            }
        }
        //Circles
        if(circlesStart) {
            for (int i = 0; i < circle.getMiddleRect().length; i++) {
                if (player.getPlayerRectangle().overlaps(circle.getMiddleRect()[i]) && player.alive) {
                    playerDeath();
                    //System.out.println("Collision: Middle CIRCLE");
                }
            }
            for (int i = 0; i < circle.getTopRect().length; i++) {
                if (player.getPlayerRectangle().overlaps(circle.getTopRect()[i]) ||
                        player.getPlayerRectangle().overlaps(circle.getBotRect()[i]) && player.alive) {
                    playerDeath();
                    //System.out.println("Collision: Botz or Top CIRCLE");
                }
            }
        }
        //Sniper
        if(snipersStart) {
            sniperZone.checkCollision(player.getPlayerRectangle(), game, player);
        }
        if(smallArrowStart) {
            smallArrowZone.checkCollision(player.getPlayerRectangle(), game, player);
        }
        if(player.getPosition().y <= -64*2){
            game.setScreen(game.gameOver);
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
    private void playerWallDeath(){
        player.alive = false;
        Application.playerAlive = false;
        player.deathAnimation();
        if(!deathSound) {
            game.deathWallSound.play(Application.volume);
            deathSound = true;
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
        Currency.countCurency(blocksNumber);

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
                player.onClick();
                break;
            case Input.Keys.Q:
                collisionCheck();
                break;
            case Input.Keys.X:
                player.onLine();
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
            player.onClick();
            game.jumpSound.play(Application.volume);
        } else if(pointer == 1){
            player.onLine();
            onLine = true;
        } else{
            onLine = false;
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
