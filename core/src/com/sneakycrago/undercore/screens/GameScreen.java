package com.sneakycrago.undercore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.sneakycrago.undercore.Application;
import com.sneakycrago.undercore.objects.BigArrow;
import com.sneakycrago.undercore.objects.Corridor;
import com.sneakycrago.undercore.objects.Player;
import com.sneakycrago.undercore.objects.SmallArrow;
import com.sneakycrago.undercore.objects.Wall;
import com.sneakycrago.undercore.objects.WhiteSides;

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

    private final int start_wall = 512;
    int start_corridor;

    private Random random;

    private int amountOfBigArrows;
    private int bigArrowsCount = 1;

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

        //Arrows
        bigArrow = new BigArrow();

        amountOfBigArrows = random.nextInt(3) + 3;
        System.out.println("amount of big arrows: " + amountOfBigArrows);
        /////////////////
        game.setScore(0);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        game.batch.begin();
        game.font.draw(game.batch, ""+ game.getScore(), 0, 288);
        game.batch.end();

        // BIG ARROW behind(backwards) from player
        bigArrowLogic(delta);

        // INPUT
        //restart screen
        if(Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
            game.setScreen(new GameScreen(game));
        }

        //jump
        if(Gdx.input.justTouched()) {
            player.onClick();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            player.onClick();
        }

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //WHITE SIDES & PLAYER
        whiteSides.drawWhiteSides(game.shapeRenderer); //draw WhiteSides
        player.drawPlayerCube(game.shapeRenderer); //draw PlayerCube

        // DRAW ELEMENTS
        wall.drawWallBlock(game.shapeRenderer, start_wall); //draw walls
        corridor.drawCorridor(game.shapeRenderer,start_corridor);

        // DRAW LINE(Input.DoubleTap)
        // onLine Player Position
        if(Gdx.input.isKeyPressed(Input.Keys.X)) {
            player.onLine();
            player.drawPlayerLine(game.shapeRenderer);
        } else {
            player.onRelease();
        }

        //COLLISION CHECK
        collisionCheck();
        game.shapeRenderer.end();

        // COLLISION DEBUG
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        game.shapeRenderer.setColor(0f,0f,1f,1f);
        /*
        //endzone walls
        game.shapeRenderer.rect(wall.getZoneRect().getX(),wall.getZoneRect().getY(), wall.getZoneRect().getWidth(),
                wall.getZoneRect().getHeight());
        if(player.getPlayerRectangle().overlaps(wall.getZoneRect())) {
            System.out.println("EndZone");
        }
        */

        //polygons
        //game.shapeRenderer.polygon(player.getPlayerPolygon().getTransformedVertices());
        //game.shapeRenderer.polygon(bigArrow.getArrowPolygon().getTransformedVertices());
        //game.shapeRenderer.polygon(bigArrow.getArrowPolygon2().getTransformedVertices());

        game.shapeRenderer.end();
    }

    public void update(float delta) {
        player.update(delta);
        wall.update(delta, start_wall);
        corridor.update(delta, start_corridor);
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
            //game.setScreen(game.gameOver); //between sides
            //System.out.println("Collision: Sides");
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
        //BIG ARROW
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

    public void bigArrowLogic(float delta) {
        //DRAW BIG ARROW LINE
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if(!bigArrow.playEffect) {
            bigArrow.drawArrowLine(game.shapeRenderer);
        }
        if(!bigArrow.playEffect2) {
            bigArrow.drawArrow2Line(game.shapeRenderer);
        }

        game.shapeRenderer.end();

        //DRAW BIG ARROW
        game.batch.begin();
        bigArrow.drawArrows(game.batch);
        bigArrow.arrowEffect(delta);
        if(!bigArrow.playEffect) {
            bigArrow.update(delta);
        }

        if(bigArrow.secondStart) {
            bigArrow.arrowEffect2(delta);
        }
        if(!bigArrow.playEffect2) {
            bigArrow.update2(delta);
        }
        game.batch.end();

        /*for(int i = 0; i < amountOfBigArrows; i++) {
            if(bigArrow.arrow2.getX() >= bigArrow.INVISIBLE) {
                bigArrow = new BigArrow();
                //i++;
            System.out.println("number of arrow" + i);
            }
        }*/
        if(bigArrow.arrow2.getX() >= bigArrow.INVISIBLE && bigArrowsCount < amountOfBigArrows) {
            bigArrow = new BigArrow();
            bigArrowsCount++;
            System.out.println("number of arrow" + bigArrowsCount);
        }
    }
}
