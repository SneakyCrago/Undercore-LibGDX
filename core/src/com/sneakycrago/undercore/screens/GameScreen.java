package com.sneakycrago.undercore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.sneakycrago.undercore.Application;
import com.sneakycrago.undercore.objects.BigArrow;
import com.sneakycrago.undercore.objects.Corridor;
import com.sneakycrago.undercore.objects.Laser;
import com.sneakycrago.undercore.objects.Player;
import com.sneakycrago.undercore.objects.SmallArrow;
import com.sneakycrago.undercore.objects.Wall;
import com.sneakycrago.undercore.objects.WhiteSides;
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

    private final int start_wall = 512;
    private int start_laser = 512;
    int start_corridor;

    private Random random;

    //BIG ARROW LOGIC
    private int amountOfBigArrows;
    private int bigArrowsCount = 1;
    private boolean bigArrowBlockStart = false;
    private boolean laserZoneStart = false;

    int nextZoneRandom;

    // for platform switch
    private boolean android = false;
    private boolean desktop = true;

    public GameScreen(Application game) {
        System.out.println("START GAME");
        this.game = game;
        this.camera = game.camera;
    }


    @Override
    public void show() {

        random = new Random();

        whiteSides = new WhiteSides();
        player = new Player(96, 139);

        //create objects
        //START BLOCK
        wall = new Wall(start_wall);

        start_corridor = start_wall + wall.getBLOCK_SIZE() + 256;
        corridor = new Corridor(start_corridor);

        laser = new Laser(start_laser);
        //Arrows
        //BIG ARROW LOGIC
        bigArrow = new BigArrow();

        amountOfBigArrows = random.nextInt(3) + 3;

        System.out.println("amount of big arrows: " + amountOfBigArrows);

        /////////////////
        nextZoneRandom = random.nextInt(2)+1;
        if(nextZoneRandom ==1) {
            System.out.println("NEXT ZONE: BIG ARROWS");
        } else {
            System.out.println("NEXT ZONE: LASERS");
        }
        Score.setGameScore(0);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        // SPRITES
        game.batch.begin();
        if(laserZoneStart) {
         laser.drawLaserGun(game.batch);
        }
        game.font.draw(game.batch, ""+ Score.getGameScore(),0 , 288);
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
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //WHITE SIDES & PLAYER
        whiteSides.drawWhiteSides(game.shapeRenderer); //draw WhiteSides
        player.drawPlayerCube(game.shapeRenderer); //draw PlayerCube

        // DRAW ELEMENTS
        // StartBlock
        wall.drawWallBlock(game.shapeRenderer, start_wall); //draw walls
        corridor.drawCorridor(game.shapeRenderer,start_corridor);


        //LaserBlock
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
        game.shapeRenderer.end();

        zoneCreator();

        //COLLISION CHECK
        //collisionCheck();

        //collisionDebug();
        /*
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(0f,0f,1f,1f);
        game.shapeRenderer.rect(corridor.getEndZone().getX(), corridor.getEndZone().getY(),
                corridor.getEndZone().getWidth(), corridor.getEndZone().getHeight());

        game.shapeRenderer.rect(laser.getEndZone().getX(), laser.getEndZone().getY(),
                laser.getEndZone().getWidth(), laser.getEndZone().getHeight());
        game.shapeRenderer.end();
        */
    }

    public void update(float delta) {
        player.update(delta);
        wall.update(delta, start_wall);
        corridor.update(delta, start_corridor);
        if(laserZoneStart) {
            laser.update(delta);
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
    boolean overlaped = false;
    public void zoneCreator(){
        if(nextZoneRandom == 1) {
            if (player.getPlayerRectangle().overlaps(corridor.getEndZone())) {
                bigArrowBlockStart = true;
                System.out.println("Corridor finished");
            }
        } else if (nextZoneRandom == 2) {
            if (player.getPlayerRectangle().overlaps(corridor.getEndZone())) {
                laserZoneStart = true;
                System.out.println("Corridor finished");
            }
        }
        if(bigArrowEnd) {
            laser = new Laser(512+128);
            laserZoneStart = true;
            bigArrowBlockStart = false;
            bigArrowEnd = false;

            overlaped = false;
        }
        if(player.getPlayerRectangle().overlaps(laser.getEndZone()) && !overlaped) {
            overlaped = true;
            bigArrowBlockStart = true;
            bigArrow = new BigArrow();
            amountOfBigArrows = random.nextInt(3) + 3;
            bigArrowsCount = 0;
            //laserZoneStart = false;
            System.out.println("bigArrowStart" + bigArrowBlockStart);
            System.out.println("bigArrowEnd" + bigArrowEnd);
        }
    }
    // COLLISION DEBUG
    public void collisionDebug(){
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        game.shapeRenderer.setColor(0f,0f,1f,1f);

        //endzone walls
        game.shapeRenderer.rect(wall.getZoneRect().getX(),wall.getZoneRect().getY(), wall.getZoneRect().getWidth(),
                wall.getZoneRect().getHeight());
        if(player.getPlayerRectangle().overlaps(wall.getZoneRect())) {
            System.out.println("EndZone");
        }

        //polygons
        game.shapeRenderer.polygon(player.getPlayerPolygon().getTransformedVertices());
        game.shapeRenderer.polygon(bigArrow.getArrowPolygon().getTransformedVertices());
        game.shapeRenderer.polygon(bigArrow.getArrowPolygon2().getTransformedVertices());


        for(int i=0; i < laser.getDownWall().length; i++) {
            game.shapeRenderer.setColor(0f,0f,1f,1f);
            game.shapeRenderer.rect(laser.getTopWall()[i].getX(),laser.getTopWall()[i].getY(),
                    laser.getTopWall()[i].getWidth(),laser.getTopWall()[i].getHeight());
            game.shapeRenderer.rect(laser.getDownWall()[i].getX(),laser.getDownWall()[i].getY(),
                    laser.getDownWall()[i].getWidth(),laser.getDownWall()[i].getHeight());

            game.shapeRenderer.rect(laser.getOrangeLaserRect()[i].getX(), laser.getOrangeLaserRect()[i].getY(),
                    laser.getOrangeLaserRect()[i].getWidth(),laser.getOrangeLaserRect()[i].getHeight());

            game.shapeRenderer.setColor(1f,0f,0f,1f);
            game.shapeRenderer.rect(laser.getBlueLaserRect()[i].getX(), laser.getBlueLaserRect()[i].getY(),
                    laser.getBlueLaserRect()[i].getWidth(),laser.getBlueLaserRect()[i].getHeight());
        }
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

            //BIG ARROW check collision
            if(player.getPlayerRectangle().overlaps(bigArrow.getLineRectangle())) { // between line
                game.setScreen(game.gameOver);
                System.out.println("Collision: BIG ARROW Line");
            }
            if(player.getPlayerRectangle().overlaps(bigArrow.getLine2Rectangle())) {
                game.setScreen(game.gameOver);
                System.out.println("Collision: BIG ARROW Line2");
            }
            if(Intersector.overlapConvexPolygons(player.getPlayerPolygon(), bigArrow.getArrowPolygon())) {
                game.setScreen(game.gameOver);
                System.out.println("Collision: BIG ARROW Polygon");
            }
            if(Intersector.overlapConvexPolygons(player.getPlayerPolygon(), bigArrow.getArrowPolygon2())){
                game.setScreen(game.gameOver);
                System.out.println("Collision: BIG ARROW Polygon2");
            }
        }
    }
}
