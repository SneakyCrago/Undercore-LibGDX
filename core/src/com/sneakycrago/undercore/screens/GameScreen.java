package com.sneakycrago.undercore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.sneakycrago.undercore.Application;
import com.sneakycrago.undercore.objects.BigArrow;
import com.sneakycrago.undercore.objects.Circle;
import com.sneakycrago.undercore.objects.Corridor;
import com.sneakycrago.undercore.objects.Laser;
import com.sneakycrago.undercore.objects.Player;
import com.sneakycrago.undercore.objects.SmallArrow;
import com.sneakycrago.undercore.objects.SmallArrowZone;
import com.sneakycrago.undercore.objects.Sniper;
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
    private int blocksNumber;

    private boolean startZoneStart = true;
    private boolean bigArrowBlockStart = false;
    private boolean laserZoneStart = false;
    private boolean circlesStart = false;
    private boolean snipersStart = false;
    private boolean smallArrowStart = false;

    int nextZoneRandom;

    // for platform switch
    private boolean android = false;
    private boolean desktop = true;

    private boolean circleCreated = false, laserCreated = false, sniperCreated = false, smallArrowCreated = false;

    public GameScreen(Application game) {
        System.out.println();
        System.out.println("GameScreen");
        this.game = game;
        this.camera = game.camera;
    }


    @Override
    public void show() {
        random = new Random();

        /////////////////////// FIX IT ///////////////////
        //create objects
        whiteSides = new WhiteSides();
        player = new Player(96, 139);
        wall = new Wall(start_wall); //START BLOCK
        start_corridor = start_wall + wall.getBLOCK_SIZE() + 256;
        corridor = new Corridor(start_corridor);
        laser = new Laser(start_laser); //Lasers
        circle = new Circle(SPAWN); // Circles //SPAWN
        bigArrow = new BigArrow();
        sniperZone = new SniperZone(SPAWN);
        smallArrowZone = new SmallArrowZone(SPAWN);

        amountOfBigArrows = random.nextInt(3) + 3;
        System.out.println("Big Arrows: " + amountOfBigArrows);
        // зона после стартового блока
        //nextZoneRandom = random.nextInt(5)+1; // создаем зону после стартового блока
        nextZoneRandom = 5;
        if(nextZoneRandom ==1) {
            System.out.println("NEXT ZONE: BIG ARROWS");
        } else if(nextZoneRandom ==2) {
            System.out.println("NEXT ZONE: LASERS");
            laser = new Laser(start_corridor + corridor.BLOCK_SIZE + 256);
            laserZoneStart = true;

            laserCreated = true;
        } else if(nextZoneRandom ==3){
            System.out.println("NEXT ZONE: CIRCLES");
            circle = new Circle(start_corridor + corridor.BLOCK_SIZE + 256);
            circlesStart = true;
            circleCreated = true;
        } else if(nextZoneRandom ==4){
            System.out.println("NEXT ZONE: SNIPERS");
            sniperZone = new SniperZone(start_corridor + corridor.BLOCK_SIZE + 256);
            snipersStart = true;

            sniperCreated = true;
        } else if(nextZoneRandom ==5){
            System.out.println("NEXT ZONE: SMALL ARROWS");
            smallArrowZone = new SmallArrowZone((start_corridor+ corridor.BLOCK_SIZE + 256) *2);
            //smallArrowZone = new SmallArrowZone(SPAWN);
            smallArrowStart = true;

            smallArrowCreated = true;
        }

        //Обнуляем счет
        Score.setGameScore(0);
        blocksNumber = 0;
        Currency.resetMoney();

        //test
        //wall.SPEED = 0;
        //corridor.SPEED = 0;
        //laser.SPEED = 0;
        //circle.SPEED = 0;
        //bigArrow.SPEED = 0;


    }

    @Override
    public void render(float delta) {
        if(Application.gameSkin == 0) {
            Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        } else if(Application.gameSkin == 1) {
            Gdx.gl.glClearColor(175/255f,209/255f,234/255f, 1f);
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        // SPRITES and Text
        game.batch.begin();
        game.font.draw(game.batch, ""+ Score.getGameScore(),0 +2, 11 + 288 - 4);

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
        player.drawPlayerCube(game.shapeRenderer); //draw PlayerCube
        // DRAW ELEMENTS
        wall.drawWallBlock(game.shapeRenderer, start_wall); //draw walls
        corridor.drawCorridor(game.shapeRenderer, start_corridor);
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

        //game info
        game.batch.begin();
        game.font10.draw(game.batch, "fps:"+Gdx.graphics.getFramesPerSecond(), 0, 288-24);
        game.font10.draw(game.batch, "blocks:" + blocksNumber, 0, 288-24-12);
        game.font10.draw(game.batch, "money:" + Currency.Money, 0, 288-24-12*2);

        if(snipersStart) {
            sniperZone.drawBullet(game.batch);
        }
        player.drawPlayerAnimation(game.batch);

        game.batch.end();

        zoneCreator();

        //COLLISION
        //collisionCheck();
        //collisionDebug();

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        game.shapeRenderer.setColor(Color.BLUE);
        //game.shapeRenderer.rect(sniper.getStationRect().getX(), sniper.getStationRect().getY(),
        //        sniper.getStationRect().getWidth(),sniper.getStationRect().getHeight());
        game.shapeRenderer.end();
    }

    public void update(float delta) {
        player.update(delta);
        wall.update(delta, start_wall);
        corridor.update(delta, start_corridor);

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
    smallArrowZoneOverlaped = false;

    // for MONEY
    private boolean startEndCheck = false, laserEndCheck = false, circleEndCheck = false, snipersEndCheck = false,
    smallArrowEndCheck = false;

    public void zoneCreator(){
        if (player.getPlayerRectangle().overlaps(corridor.getEndZone()) && !startEndCheck) {
            countMoney();
            startEndCheck = true;
        }

        // START BLOCK
        if (nextZoneRandom == 1 &&player.getPlayerRectangle().overlaps(corridor.getEndZone())) {
            bigArrowBlockStart = true;
        }

        createBigArrowZone(); // BIG ARROW
        createLaserZone(); // LASER
        createCircleZone(); //CIRCLE
        createSniperZone(); //SNIPER
        createSmallArrowZone(); //Small Arrow
    }
    public void createBigArrowZone(){
        if(bigArrowEnd) {
            bigArrowBlockStart = false;
            bigArrowEnd = false;


            laserEndCheck = false;
            circleEndCheck= false;
            startEndCheck = false;
            snipersEndCheck = false;
            smallArrowEndCheck = false;

            countMoney();

            //zoneCreate = random.nextInt(4) + 1;
            zoneCreate = 4;
            System.out.println("Zone: " + zoneCreate);
            if (zoneCreate == 1) {
                laser = new Laser(512 + 64);
                laserZoneStart = true;

                laserCreated = true;

                laserZoneOverlaped = false;
            } else if (zoneCreate == 2) {
                circle = new Circle(512 + 64);
                circlesStart = true;
                circleCreated = true;

                circleZoneOverlaped = false;
            } else if(zoneCreate == 3) {
                sniperZone = new SniperZone(512 + 64);
                snipersStart = true;
                sniperCreated = true;

                snipersZoneOverlaped = false;
            } else if(zoneCreate == 4){
                smallArrowZone = new SmallArrowZone(512 + 64);
                smallArrowStart = true;
                smallArrowCreated = true;

                smallArrowZoneOverlaped = false;
            }
        }
    }
    public void createLaserZone(){
        if(player.getPlayerRectangle().overlaps(laser.getStartZone())&& !laserZoneOverlaped){
            circleEndCheck = false;
            startEndCheck = false;
            snipersEndCheck = false;
            smallArrowEndCheck = false;

            circleZoneOverlaped = false;
            snipersZoneOverlaped = false;
            smallArrowZoneOverlaped = false;
            laserZoneOverlaped = true;


            //zoneCreate = random.nextInt(4) + 1;
            zoneCreate = 4;

            if(zoneCreate == 2) {
                circle = new Circle(laser.BLOCK_ZONE +128 + 256);
                circlesStart = true;
                circleCreated = true;
            }
            if(zoneCreate == 3) {
                sniperZone = new SniperZone(laser.BLOCK_ZONE + 128+256);
                snipersStart = true;
                sniperCreated = true;

                snipersZoneOverlaped = false;
            }
            if(zoneCreate == 4) {
                smallArrowZone = new SmallArrowZone((laser.BLOCK_ZONE + 128+256) *2);
                smallArrowStart = true;
                smallArrowCreated = true;

                smallArrowZoneOverlaped = false;
            }
        }
        if(player.getPlayerRectangle().overlaps(laser.getEndZone())) {
            if (zoneCreate == 1) {
                bigArrowBlockStart = true;

                bigArrow = new BigArrow();
                amountOfBigArrows = random.nextInt(3) + 3;
                bigArrowsCount = 0;

            }
        }
        // Money
        if(player.getPlayerRectangle().overlaps(laser.getEndZone()) && !laserEndCheck){
            countMoney();
            laserEndCheck = true;
        }
    }
    public void createCircleZone(){
        if(player.getPlayerRectangle().overlaps(circle.getStartZone()) && !circleZoneOverlaped) {
            laserEndCheck = false;
            startEndCheck = false;
            snipersEndCheck = false;
            smallArrowEndCheck = false;

            laserZoneOverlaped = false;
            snipersZoneOverlaped = false;
            smallArrowZoneOverlaped = false;
            circleZoneOverlaped = true;

            //zoneCreate = random.nextInt(4) + 1;
            zoneCreate = 4;

            if (zoneCreate == 2) {
                laser = new Laser(circle.BLOCK_SIZE +128 + 256);
                laserZoneStart = true;
                laserCreated = true;

                bigArrowBlockStart = false;
                bigArrowEnd = false;
                laserZoneOverlaped = false;
            }
            if(zoneCreate == 3) {
                sniperZone = new SniperZone(circle.BLOCK_SIZE + 128+256);
                snipersStart = true;
                sniperCreated = true;

                bigArrowBlockStart = false;
                bigArrowEnd = false;
                snipersZoneOverlaped = false;
            }
            if(zoneCreate == 4) {
                smallArrowZone = new SmallArrowZone((circle.BLOCK_SIZE + 128+256) *2);
                smallArrowStart = true;
                smallArrowCreated = true;

                bigArrowBlockStart = false;
                bigArrowEnd = false;
                smallArrowZoneOverlaped = false;
            }

        }

        if(player.getPlayerRectangle().overlaps(circle.getEndZone())){
            if (zoneCreate == 1) {
                bigArrowBlockStart = true;
                bigArrow = new BigArrow();
                amountOfBigArrows = random.nextInt(3) + 3;
                bigArrowsCount = 0;
            }
        }
        if(player.getPlayerRectangle().overlaps(circle.getEndZone()) &&!circleEndCheck){
            countMoney();
            circleEndCheck = true;
        }
    }
    public void createSniperZone(){
        if(player.getPlayerRectangle().overlaps(sniperZone.getStartZone()) && !snipersZoneOverlaped){
            laserEndCheck = false;
            startEndCheck = false;
            circleEndCheck = false;
            smallArrowEndCheck = false;

            snipersZoneOverlaped = true;

            //zoneCreate = random.nextInt(4) + 1;
            zoneCreate = 4;

            if (zoneCreate == 2) {
                laser = new Laser(sniperZone.BLOCK_SIZE + 128 + 256);
                laserZoneStart = true;
                laserCreated = true;

                bigArrowBlockStart = false;
                bigArrowEnd = false;
                laserZoneOverlaped = false;
            }
            if(zoneCreate == 3) {
                circle = new Circle(sniperZone.BLOCK_SIZE + 128 + 256);
                circlesStart = true;
                circleCreated = true;

                bigArrowBlockStart = false;
                bigArrowEnd = false;
                circleZoneOverlaped = false;
            }
            if(zoneCreate == 4){
                smallArrowZone = new SmallArrowZone((sniperZone.BLOCK_SIZE + 128 + 256) *2);
                smallArrowStart = true;
                smallArrowCreated = true;

                bigArrowBlockStart = false;
                bigArrowEnd = false;
                smallArrowZoneOverlaped = false;
            }
        }

        if(player.getPlayerRectangle().overlaps(sniperZone.getEndZone())){
            if (zoneCreate == 1) {
                bigArrowBlockStart = true;
                bigArrow = new BigArrow();
                amountOfBigArrows = random.nextInt(3) + 3;
                bigArrowsCount = 0;
            }
        }

        if(player.getPlayerRectangle().overlaps(sniperZone.getEndZone()) &&!snipersEndCheck){
            countMoney();
            snipersEndCheck = true;
        }
    }
    public void createSmallArrowZone(){
        if(player.getPlayerRectangle().overlaps(smallArrowZone.getStartZone()) && !smallArrowZoneOverlaped) {
            laserEndCheck = false;
            startEndCheck = false;
            circleEndCheck = false;
            snipersEndCheck = false;

            smallArrowZoneOverlaped = true;

            zoneCreate = random.nextInt(4) + 1;
            if(zoneCreate == 2){
                laser = new Laser(smallArrowZone.BLOCK_SIZE/2  + 512);
                laserZoneStart = true;
                laserCreated = true;

                bigArrowBlockStart = false;
                bigArrowEnd = false;
                laserZoneOverlaped = false;
            }
            if(zoneCreate == 3) {
                circle = new Circle(smallArrowZone.BLOCK_SIZE/2  + 512);
                circlesStart = true;
                circleCreated = true;

                bigArrowBlockStart = false;
                bigArrowEnd = false;
                circleZoneOverlaped = false;
            }
            if(zoneCreate ==4){
                sniperZone = new SniperZone(smallArrowZone.BLOCK_SIZE/2 + 512);
                snipersStart = true;
                sniperCreated = true;

                bigArrowBlockStart = false;
                bigArrowEnd = false;
                snipersZoneOverlaped = false;
            }
        }
        if(player.getPlayerRectangle().overlaps(smallArrowZone.getEndZone())) {
            if (zoneCreate == 1) {
                bigArrowBlockStart = true;
                bigArrow = new BigArrow();
                amountOfBigArrows = random.nextInt(3) + 3;
                bigArrowsCount = 0;
            }
        }
        // Money
        if(player.getPlayerRectangle().overlaps(smallArrowZone.getEndZone()) &&!smallArrowEndCheck){
            countMoney();
            smallArrowEndCheck = true;
        }
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
    public void collisionCheck(){
        //WALL
        for(int i = 0; i < wall.getMassiveRect().length; i++) {
            if (player.getPlayerRectangle().overlaps(wall.getMassiveRect()[i]) ||
                    player.getPlayerRectangle().overlaps(wall.getMassiveRect2()[i])) {
                game.setScreen(game.gameOver);
                System.out.println("Collision: Walls");
            }
        }
        //WHITE SIDES
        if(player.sidesCollision()) {
            game.setScreen(game.gameOver); //between sides
            System.out.println("Collision: Sides");
        }
        //CORRIDOR
        for(int i=0; i< corridor.getTopLeftRects().length; i++) { //between corridor top
            if (player.getPlayerRectangle().overlaps(corridor.getTopLeftRects()[i]) ||
                    player.getPlayerRectangle().overlaps(corridor.getTopRightRects()[i])){
                game.setScreen(game.gameOver);
                System.out.println("Collision: Corridor Left");
            }
        }
        for(int i=0; i < corridor.getBottomRets().length;i++) {
            if(player.getPlayerRectangle().overlaps(corridor.getBottomRets()[i])) { // between corridor bottom
                game.setScreen(game.gameOver);
                System.out.println("Collision: Corridor Bottom");
            }
        }
        //BIG ARROW check collision
        if(bigArrowBlockStart) {
            if (player.getPlayerRectangle().overlaps(bigArrow.getLineRectangle())) { // between line
                game.setScreen(game.gameOver);
                System.out.println("Collision: BIG ARROW Line");
            }
            if (player.getPlayerRectangle().overlaps(bigArrow.getLine2Rectangle())) {
                game.setScreen(game.gameOver);
                System.out.println("Collision: BIG ARROW Line2");
            }
            if (Intersector.overlapConvexPolygons(player.getPlayerPolygon(), bigArrow.getArrowPolygon())) {
                game.setScreen(game.gameOver);
                System.out.println("Collision: BIG ARROW Polygon");
            }
            if (Intersector.overlapConvexPolygons(player.getPlayerPolygon(), bigArrow.getArrowPolygon2())) {
                game.setScreen(game.gameOver);
                System.out.println("Collision: BIG ARROW Polygon2");
            }
        }
        //LaserCollision
        for(int i =0; i < laser.getTopWall().length; i++) {
            if (player.getPlayerRectangle().overlaps(laser.getTopWall()[i]) ||
                    player.getPlayerRectangle().overlaps(laser.getDownWall()[i])) {
                game.setScreen(game.gameOver);
                System.out.println("Collision: LASER WALL");
            }
            // проверяем оранжевый лазер, если на линии -> смерть
            if(player.getPlayerRectangle().overlaps(laser.getOrangeLaserRect()[i]) && player.Line){
                game.setScreen(game.gameOver);
                System.out.println("Collision: Orange LASER");
            }
            // проверяем голубой лазер, если состояние в прыжке -> смерть
            if(player.getPlayerRectangle().overlaps(laser.getBlueLaserRect()[i]) && player.Jump) {
                game.setScreen(game.gameOver);
                System.out.println("Collision: Blue LASER");
            }
        }

        //Circles
        for(int i=0; i < circle.getMiddleRect().length; i++) {
            if(player.getPlayerRectangle().overlaps(circle.getMiddleRect()[i])) {
                game.setScreen(game.gameOver);
                System.out.println("Collision: Middle CIRCLE");
            }
        }
        for(int i =0; i< circle.getTopRect().length;i++){
            if(player.getPlayerRectangle().overlaps(circle.getTopRect()[i]) ||
                    player.getPlayerRectangle().overlaps(circle.getBotRect()[i])){
                game.setScreen(game.gameOver);
                System.out.println("Collision: Bot or Top CIRCLE");
            }
        }

        //Sniper
        if(snipersStart) {
            sniperZone.checkCollision(player.getPlayerRectangle(), game);
        }

        smallArrowZone.checkCollision(player.getPlayerRectangle(), game);
    }

    boolean bigArrowEnd = false;
    public void bigArrowLogic(float delta) {
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

    public void countMoney(){
        blocksNumber +=1;
        Currency.countCurency(blocksNumber);
        //Currency.addMoneyToCurrency();
        System.out.println("Money: " + Currency.Money);
        System.out.println("Currency: " + Currency.currency);
    }
}
