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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Align;
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

public class GameScreen implements Screen {
    Application game;

    private OrthographicCamera camera;

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


    private final int start_wall = 512;
    private int start_laser = 512; //512
    private final int SPAWN = 512;
    int start_corridor;

    private Random random;

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
    private boolean android = true;
    private boolean desktop = false;

    private boolean circleCreated = false, laserCreated = false, sniperCreated = false, smallArrowCreated = false,
    bigArrowCreated = false;

    private GlyphLayout glyphLayout;


    public GameScreen(Application game) {
        System.out.println();
        System.out.println("GameScreen");
        this.game = game;
        this.camera = game.camera;
    }


    @Override
    public void show() {
        random = new Random();

        Application.gameSkin = random.nextInt(5);

        //create objects
        whiteSides = new WhiteSides();
        player = new Player(96, 139);
        wall = new Wall(start_wall); //START BLOCK
        start_corridor = start_wall + wall.getBLOCK_SIZE() + 256;
        corridor = new Corridor(start_corridor);

        Application.playerAlive = true;

        startZoneStart = true;

        /////////////////////// FIX IT ///////////////////
        laser = new Laser(start_laser); //Lasers
        circle = new Circle(SPAWN); // Circles //SPAWN
        bigArrow = new BigArrow();
        sniperZone = new SniperZone(SPAWN);
        smallArrowZone = new SmallArrowZone(SPAWN);

        amountOfBigArrows = random.nextInt(3) + 3;
        System.out.println("Big Arrows: " + amountOfBigArrows);

        //Обнуляем счет
        Score.setGameScore(0);
        blocksNumber = 0;
        Currency.resetMoney();

        glyphLayout = new GlyphLayout();

        //test

    }

    @Override
    public void render(float delta) {
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
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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

        // INPUT
        if(desktop) {
            //restart screen
            if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
                game.setScreen(new GameScreen(game));
            }
            //jump
            if (Gdx.input.justTouched()) {
                player.onClick();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
                player.onClick();
            }
            if(Gdx.input.isKeyPressed(Input.Keys.Q)){
                collisionCheck();
            }
        }
        if(android) {
            if(Gdx.input.justTouched()) {
               player.onClick();
            }
        }

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
        // DRAW LINE(Input.DoubleTap)
        if(desktop) {
            // onLine Player Position
            if (Gdx.input.isKeyPressed(Input.Keys.X)) {
                player.onLine();
                player.drawPlayerLine(game.shapeRenderer);
            } else {
                player.onRelease();
            }
        }
        if(android) {
            if(Gdx.input.isTouched(1)){
                player.onLine();
                player.drawPlayerLine(game.shapeRenderer);
            } else {
                player.onRelease();
            }
        }

        game.shapeRenderer.end();

        if(desktop) {
            //game info
            game.batch.begin();
            game.font10.draw(game.batch, "fps:" + Gdx.graphics.getFramesPerSecond(), 0, 288 - 24);
            game.font10.draw(game.batch, "blocks:" + blocksNumber, 0, 288 - 24 - 12);
            game.font10.draw(game.batch, "money:" + Currency.Money, 0, 288 - 24 - 12 * 2);
            game.font10.draw(game.batch, "counter:" + blockCounter, 0, 288 - 24 - 12 * 3);
        }
        if(snipersStart) {
            sniperZone.drawBullet(game.batch);
        }
        player.drawPlayerAnimation(game.batch);

        //game.borderFont.draw(game.batch, ""+ Score.getGameScore(),0 +2, 11 + 288 - 4);
        glyphLayout.setText(game.borderFont, ""+ Score.getGameScore() , Color.WHITE,512, Align.center, true);
        game.borderFont.draw(game.batch, glyphLayout, 0, 11 + 288 - 4);

        game.batch.end();

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        player.drawPlayerCube(game.shapeRenderer); //draw PlayerCube
        game.shapeRenderer.end();

        zoneCreator();

        //COLLISION
        collisionCheck();
        //collisionDebug();
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
        if(circleCreated) {
            circle.dispose();
        }
        if(laserCreated) {
            laser.dispose();
        }
        if(sniperCreated) {
            sniperZone.dispose();
        }
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
                    System.out.println("NEXT ZONE: BIG ARROWS");
                } else if (nextZoneRandom == 2) {
                    System.out.println("NEXT ZONE: LASERS");
                    makeLaser(start_corridor + corridor.BLOCK_SIZE - 96);
                } else if (nextZoneRandom == 3) {
                    System.out.println("NEXT ZONE: CIRCLES");
                    makeCircle(start_corridor + corridor.BLOCK_SIZE - 96);
                } else if (nextZoneRandom == 4) {
                    System.out.println("NEXT ZONE: SNIPERS");
                    sniperZone = new SniperZone(start_corridor + corridor.BLOCK_SIZE - 96);
                    makeSniper(start_corridor + corridor.BLOCK_SIZE - 96);
                } else if (nextZoneRandom == 5) {
                    System.out.println("NEXT ZONE: SMALL ARROWS");
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
                System.out.println("Zone: " + zoneCreate);
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
        if(player.getPlayerRectangle().overlaps(laser.getStartZone()) && !laserZoneOverlaped) {
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
            if(blockCounter != 8){
                zoneCreate = random.nextInt(4) + 1;
                if(zoneCreate ==2) {
                    makeCircle(laser.BLOCK_ZONE + 128+ 256);
                }
                if(zoneCreate ==3){
                    makeSniper(laser.BLOCK_ZONE + 128+256);
                }
                if(zoneCreate ==4) {
                    makeSmallArrow((laser.BLOCK_ZONE + 128+256) *2);
                }
            } else if(blockCounter ==8){
                makeStart(laser.BLOCK_ZONE + 128+256);
            }
        }

        if(player.getPlayerRectangle().overlaps(laser.getEndZone()) && !laserEndCheck){
            if(zoneCreate == 1 && blockCounter != 8){
                System.out.println("BIG ARROW START");
                makeBigArrow();
            }
            countMoney();
            laserEndCheck = true;
        }
    }
    private void createCircleZone(){
        if(player.getPlayerRectangle().overlaps(circle.getStartZone()) && !circleZoneOverlaped) {
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
            if(blockCounter != 8) {
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
            } else if(blockCounter == 8) {
                makeStart(circle.BLOCK_SIZE + 128 + 256);
            }
        }

        if(player.getPlayerRectangle().overlaps(circle.getEndZone()) &&!circleEndCheck){
            if (zoneCreate == 1 && blockCounter != 8) {
                makeBigArrow();
            }
            countMoney();
            circleEndCheck = true;
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

    private void makeStart(int lastZone){
        wall = new Wall(lastZone);
        corridor = new Corridor(lastZone +start_corridor - 512);

        startZoneStart = true;
        startOverlaped = false;
        /*
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
        startOverlaped = false; */
    }
    private void makeBigArrow(){
        bigArrowBlockStart = true;
        bigArrow = new BigArrow();
        amountOfBigArrows = random.nextInt(3) + 3;
        bigArrowsCount = 0;
        /*
        laserEndCheck = false;
        circleEndCheck= false;
        startEndCheck = false;
        snipersEndCheck = false;
        smallArrowEndCheck = false;

        circleZoneOverlaped = false;
        snipersZoneOverlaped = false;
        smallArrowZoneOverlaped = false;
        laserZoneOverlaped = false;
        startOverlaped = false; */
    }
    private void makeLaser(int lastZone){
        laser = new Laser(lastZone);
        laserZoneStart = true;
        laserCreated = true;

        laserZoneOverlaped = false;
        /*
        bigArrowBlockStart = false;
        bigArrowEnd = false;

        circleEndCheck = false;
        startEndCheck = false;
        snipersEndCheck = false;
        smallArrowEndCheck = false;

        circleZoneOverlaped = false;
        snipersZoneOverlaped = false;
        smallArrowZoneOverlaped = false;
        startOverlaped = false;
        */
    }
    private void makeCircle(int lastZone){
        circle = new Circle(lastZone);
        circlesStart = true;
        circleCreated = true;

        circleZoneOverlaped = false;
        /*
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
        */
    }
    private void makeSniper(int lastZone){
        sniperZone = new SniperZone(lastZone);
        snipersStart = true;
        sniperCreated = true;

        snipersZoneOverlaped = false;
        /*
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
        */
    }
    private void makeSmallArrow(int lastZone){
        smallArrowZone = new SmallArrowZone(lastZone);
        smallArrowStart = true;
        smallArrowCreated = true;

        smallArrowZoneOverlaped = false;
        /*
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
        */
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
        for(int i=0; i < laser.getDownWall().length; i++) {
            game.shapeRenderer.setColor(0f,0f,1f,1f);
            game.shapeRenderer.rect(laser.getTopWall()[i].getX(),laser.getTopWall()[i].getY(),
                    laser.getTopWall()[i].getWidth(),laser.getTopWall()[i].getHeight());
            game.shapeRenderer.rect(laser.getDownWall()[i].getX(),laser.getDownWall()[i].getY(),
                    laser.getDownWall()[i].getWidth(),laser.getDownWall()[i].getHeight());

            game.shapeRenderer.rect(laser.getOrangeLaserRect()[i].getX(), laser.getOrangeLaserRect()[i].getY(),
                    laser.getOrangeLaserRect()[i].getWidth(),laser.getOrangeLaserRect()[i].getHeight());

            game.shapeRenderer.setColor(1f,0f,0f,1f);

            game.shapeRenderer.rect(laser.getStartZone().getX(), laser.getEndZone().getY(),
                    laser.getStartZone().getWidth(),laser.getStartZone().getHeight());

            game.shapeRenderer.rect(laser.getBlueLaserRect()[i].getX(), laser.getBlueLaserRect()[i].getY(),
                    laser.getBlueLaserRect()[i].getWidth(),laser.getBlueLaserRect()[i].getHeight());

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
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.rect(laser.getEndZone().getX(), laser.getEndZone().getY(),
                laser.getEndZone().getWidth(), laser.getEndZone().getHeight());

        game.shapeRenderer.end();

        // SNIPER
        if(snipersStart) {
            sniperZone.collisionDebug(game.shapeRenderer);
        }

        smallArrowZone.collisionDebug(game.shapeRenderer);
    }
    private void collisionCheck(){
        //WHITE SIDES
        if(player.sidesCollision()) {
            playerDeath();
        }
        if(startZoneStart) {
            //WALL
            for (int i = 0; i < wall.getMassiveRect().length; i++) {
                if (player.getPlayerRectangle().overlaps(wall.getMassiveRect()[i]) ||
                        player.getPlayerRectangle().overlaps(wall.getMassiveRect2()[i]) && player.alive) {
                    playerDeath();
                }
            }
            //CORRIDOR
            for(int i=0; i< corridor.getTopLeftRects().length; i++) { //between corridor top
                if (player.getPlayerRectangle().overlaps(corridor.getTopLeftRects()[i]) ||
                        player.getPlayerRectangle().overlaps(corridor.getTopRightRects()[i]) && player.alive){
                    playerDeath();
                }
            }
            for(int i=0; i < corridor.getBottomRets().length;i++) {
                if(player.getPlayerRectangle().overlaps(corridor.getBottomRets()[i]) && player.alive) { // between corridor bottom
                    playerDeath();
                }
            }
        }

        //BIG ARROW check collision
        if(bigArrowBlockStart) {
            if (player.getPlayerRectangle().overlaps(bigArrow.getLineRectangle()) && player.alive) { // between line
                playerDeath();
                System.out.println("Collision: BIG ARROW Line");
            }
            if (player.getPlayerRectangle().overlaps(bigArrow.getLine2Rectangle())&& player.alive) {
                playerDeath();
                System.out.println("Collision: BIG ARROW Line2");
            }
            if (Intersector.overlapConvexPolygons(player.getPlayerPolygon(), bigArrow.getArrowPolygon())&& player.alive) {
                playerDeath();
                System.out.println("Collision: BIG ARROW Polygon");
            }
            if (Intersector.overlapConvexPolygons(player.getPlayerPolygon(), bigArrow.getArrowPolygon2())&& player.alive) {
                playerDeath();
                System.out.println("Collision: BIG ARROW Polygon2");
            }
        }
        //LaserCollision
        for(int i =0; i < laser.getTopWall().length; i++) {
            if (player.getPlayerRectangle().overlaps(laser.getTopWall()[i]) ||
                    player.getPlayerRectangle().overlaps(laser.getDownWall()[i]) && player.alive) {
                playerDeath();
                System.out.println("Collision: LASER WALL");
            }
            // проверяем оранжевый лазер, если на линии -> смерть
            if(player.getPlayerRectangle().overlaps(laser.getOrangeLaserRect()[i]) && player.Line && player.alive){
                playerDeath();
                System.out.println("Collision: Orange LASER");
            }
            // проверяем голубой лазер, если состояние в прыжке -> смерть
            if(player.getPlayerRectangle().overlaps(laser.getBlueLaserRect()[i]) && player.Jump && player.alive) {
                playerDeath();
                System.out.println("Collision: Blue LASER");
            }
        }

        //Circles
        for(int i=0; i < circle.getMiddleRect().length; i++) {
            if(player.getPlayerRectangle().overlaps(circle.getMiddleRect()[i]) && player.alive) {
                playerDeath();
                System.out.println("Collision: Middle CIRCLE");
            }
        }
        for(int i =0; i< circle.getTopRect().length;i++){
            if(player.getPlayerRectangle().overlaps(circle.getTopRect()[i]) ||
                    player.getPlayerRectangle().overlaps(circle.getBotRect()[i]) && player.alive){
                playerDeath();
                System.out.println("Collision: Botz or Top CIRCLE");
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

    private void playerDeath(){
        player.alive = false;
        Application.playerAlive = false;
        player.deathAnimation();
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
                bigArrow = new BigArrow();
                bigArrowsCount++;
                System.out.println("number of arrow" + bigArrowsCount);
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
}
