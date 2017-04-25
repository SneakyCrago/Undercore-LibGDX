package com.sneakycrago.undercore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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
    private SmallArrow smallArrow;
    private BigArrow bigArrow;
    private Laser laser;
    private Circle circle;

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

    int nextZoneRandom;

    // for platform switch
    private boolean android = false;
    private boolean desktop = true;

    public GameScreen(Application game) {
        System.out.println();
        System.out.println("GameScreen");
        this.game = game;
        this.camera = game.camera;
    }


    @Override
    public void show() {
        random = new Random();

        //create objects
        whiteSides = new WhiteSides();
        player = new Player(96, 139);
        wall = new Wall(start_wall); //START BLOCK
        start_corridor = start_wall + wall.getBLOCK_SIZE() + 256;
        corridor = new Corridor(start_corridor);
        laser = new Laser(start_laser); //Lasers
        circle = new Circle(SPAWN); // Circles //SPAWN
        smallArrow = new SmallArrow(256); //Arrows
        bigArrow = new BigArrow();

        amountOfBigArrows = random.nextInt(3) + 3;
        System.out.println("Big Arrows: " + amountOfBigArrows);
        // зона после стартового блока
        nextZoneRandom = random.nextInt(3)+1; // создаем зону после стартового блока
        if(nextZoneRandom ==1) {
            System.out.println("NEXT ZONE: BIG ARROWS");
        } else if(nextZoneRandom ==2) {
            System.out.println("NEXT ZONE: LASERS");
            laser = new Laser(start_corridor + corridor.BLOCK_SIZE + 256);
            laserZoneStart = true;
        } else if(nextZoneRandom ==3){
            System.out.println("NEXT ZONE: CIRCLES");
            circle = new Circle(start_corridor + corridor.BLOCK_SIZE + 256);
            circlesStart = true;
        }
        //Обнуляем счет
        Score.setGameScore(0);
        blocksNumber = 10000;
        Currency.resetMoney();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        // SPRITES and Text
        game.batch.begin();
        game.font.draw(game.batch, ""+ Score.getGameScore(),0 +2, 11 + 288 - 4);
        game.font10.draw(game.batch, "fps:"+Gdx.graphics.getFramesPerSecond(), 0, 288-24);
        game.font10.draw(game.batch, "blocks:" + blocksNumber, 0, 288-24-12);
        game.font10.draw(game.batch, "money:" + Currency.Money, 0, 288-24-12*2);

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
        //smallArrow.drawArrow(game.shapeRenderer);
        if(laserZoneStart) {
            laser.drawLaserBlock(game.shapeRenderer, delta);
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


        //game.shapeRenderer.line(0,310/2,player.getPlayerRectangle().x +16, player.getPlayerRectangle().y +16);


        game.shapeRenderer.end();

        zoneCreator();

        //COLLISION
        //collisionCheck();
        //collisionDebug();

        if(Gdx.input.isKeyPressed(Input.Keys.Q)){
            collisionCheck();
        }
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

    }

    private int zoneCreate;
    private boolean laserZoneOverlaped = false;
    private boolean circleZoneOverlaped = false;

    private boolean startEndCheck = false, laserEndCheck = false, circleEndCheck = false;

    public void zoneCreator(){
        if (player.getPlayerRectangle().overlaps(corridor.getEndZone()) && !startEndCheck) {
            countMoney();
            startEndCheck = true;
        }
        // START BLOCK
        if(nextZoneRandom == 1) {
            if (player.getPlayerRectangle().overlaps(corridor.getEndZone())) {
                bigArrowBlockStart = true;
            }
        } else if (nextZoneRandom == 2) {
            if (player.getPlayerRectangle().overlaps(corridor.getEndZone())) {
                laserZoneStart = true;
            }
        } else if(nextZoneRandom == 3) {
            if (player.getPlayerRectangle().overlaps(corridor.getEndZone())) {
                circlesStart = true;
            }
        }

        createBigArrowZone(); // BIG ARROW
        createLaserZone(); // LASER
        createCircleZone(); //CIRCLE
    }
    public void createBigArrowZone(){
        if(bigArrowEnd) {
            bigArrowBlockStart = false;
            bigArrowEnd = false;

            circleZoneOverlaped = false;

            laserEndCheck = false;
            circleEndCheck= false;
            startEndCheck = false;

            countMoney();

            zoneCreate = random.nextInt(2) + 1;
            System.out.println("Zone: " + zoneCreate);
            if (zoneCreate == 1) {
                laser = new Laser(512 + 64);
                laserZoneStart = true;

                laserZoneOverlaped = false;
            } else if (zoneCreate == 2) {
                circle = new Circle(512 + 64);
                circlesStart = true;
            }
        }
    }
    public void createLaserZone(){
        if(player.getPlayerRectangle().overlaps(laser.getStartZone())&& !laserZoneOverlaped){
            circleEndCheck = false;
            startEndCheck = false;

            circleZoneOverlaped = false;
            laserZoneOverlaped = true;
            zoneCreate = random.nextInt(2) + 1;
            System.out.println("Zone: " + zoneCreate);

            if(zoneCreate == 2) {
                circle = new Circle(laser.BLOCK_ZONE +128 + 256);
                circlesStart = true;
            }
        }
        if(player.getPlayerRectangle().overlaps(laser.getEndZone())) {
            if (zoneCreate == 1) {
                bigArrowBlockStart = true;

                bigArrow = new BigArrow();
                amountOfBigArrows = random.nextInt(3) + 3;
                bigArrowsCount = 0;
                //laserZoneStart = false;

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

            circleZoneOverlaped = true;
            laserZoneOverlaped = false;
            zoneCreate = random.nextInt(2) + 1;
            System.out.println("Zone: " + zoneCreate);
            if (zoneCreate == 2) {
                laser = new Laser(circle.BLOCK_SIZE +128 + 256);
                laserZoneStart = true;

                bigArrowBlockStart = false;
                bigArrowEnd = false;
                laserZoneOverlaped = false;
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

    public void countMoney(){
        blocksNumber +=1;
        Currency.countCurency(blocksNumber);
        //Currency.addMoneyToCurrency();
        System.out.println("Money: " + Currency.Money);
        System.out.println("Currency: " + Currency.currency);
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
}
