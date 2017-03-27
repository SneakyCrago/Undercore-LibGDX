package com.sneakycrago.undercore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sneakycrago.undercore.Application;
import com.sneakycrago.undercore.objects.Corridor;
import com.sneakycrago.undercore.objects.Player;
import com.sneakycrago.undercore.objects.Wall;
import com.sneakycrago.undercore.objects.WhiteSides;

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

    private final int start_wall = 512;
    int start_corridor;

    public GameScreen(Application game) {
        this.game = game;
        this.camera = game.camera;
    }

    @Override
    public void show() {
        whiteSides = new WhiteSides();
        player = new Player(96, 139);

        wall = new Wall(start_wall);

        start_corridor = start_wall + wall.getBLOCK_SIZE() + 256;

        corridor = new Corridor(start_corridor);

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
        whiteSides.drawWhiteSides(game.shapeRenderer); //draw WhiteSides
        player.drawPlayerCube(game.shapeRenderer); //draw PlayerCube

        // DRAW ELEMENTS
        wall.drawWallBlock(game.shapeRenderer, start_wall); //draw walls
        corridor.drawCorridor(game.shapeRenderer,start_corridor);

        // DRAW LINE(Input)
        // onLine Player Position
        if(Gdx.input.isKeyPressed(Input.Keys.X)) {
            player.onLine();
            player.drawPlayerLine(game.shapeRenderer);
        } else {
            player.onRelease();
        }

        //COLLISION CHECK
        collisionCheck();

        // COLLISION DEBUG
        game.shapeRenderer.setColor(0f,0f,1f,1f);
        /*
        //endzone walls
        game.shapeRenderer.rect(wall.getZoneRect().getX(),wall.getZoneRect().getY(), wall.getZoneRect().getWidth(),
                wall.getZoneRect().getHeight());
        if(player.getPlayerRectangle().overlaps(wall.getZoneRect())) {
            System.out.println("EndZone");
        }
        */

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
        for(int i = 0; i < wall.getMassiveRect().length; i++) { //between walls
            if (player.getPlayerRectangle().overlaps(wall.getMassiveRect()[i]) ||
                    player.getPlayerRectangle().overlaps(wall.getMassiveRect2()[i])) {
                game.setScreen(game.gameOver);
                System.out.println("Collision: Walls");
            }
        }
        if(player.sidesCollision()) {
            game.setScreen(game.gameOver); //between sides
            System.out.println("Collision: Sides");
        }

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
    }
}
