package com.sneakycrago.undercore.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sneakycrago.undercore.Application;
import com.sneakycrago.undercore.utils.Globals;

/**
 * Created by Sneaky Crago on 25.03.2017.
 */

public class Player {
    private  int GRAVITY = -15;  // -10; jump 250
    private final int BOT_SIDE = 11;
    private final int TOP_SIDE = 267; // 267 ???
    private final int TEXTURE_SIZE = 32;
    private final float lineY = 5;

    private Vector2 position;
    private Vector2 velocity;

    private int playerPos;

    public boolean Jump = true;
    public boolean Line = false;
    public boolean Shield = false;

    public boolean alive = true;
    //public boolean dead = false;

    private Rectangle playerCubeRectangle;

    private Polygon playerPolygon;

    private float elapsedTime = 0f, currentFrame;
    private Sprite spriteAnim;
    private Texture texture;
    //private Texture[] texture;

    public Player(float x, float y, Application game) {
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);


        playerCubeRectangle = new Rectangle(x, y, 32, 32);

        playerPolygon = new Polygon(new float[] {
                0,0,
                0,32,
                32,32,
                32,0});

        //texture = new Texture[5];

        switch (Application.gameSkin){
            case 0: //texture[0] = game.playerSkin0;
                texture = game.playerSkin0;
                spriteAnim = new Sprite(texture);
                break;
            case 1: texture = game.playerSkin1;
                spriteAnim = new Sprite(texture);
                break;
            case 2: texture = game.playerSkin2;
                spriteAnim = new Sprite(texture);
                break;
            case 3: texture = game.playerSkin3;
                spriteAnim = new Sprite(texture);
                break;
            case 4: texture = game.playerSkin4;
                spriteAnim = new Sprite(texture);
                break;
        }

        //spriteAnim = new Sprite(texture);
        spriteAnim.setSize(32,32);
        spriteAnim.setPosition(position.x - 32, position.y);
    }
    public void secondChance(int x, int y){
        position.set(x,y);
        velocity.set(0, 0);
        playerCubeRectangle.setPosition(position.x, position.y);
        playerPolygon.setPosition(position.x, position.y);
        alive = true;
    }

    public void update(float delta) {
        velocity.add(0, GRAVITY);
        velocity.scl(delta);

        // x = SPEED * delta = движение по x
        position.add(0, velocity.y);
        // не заходит за белые границы

        velocity.scl(1 / delta);

        playerCubeRectangle.setPosition(position.x, position.y);

        playerPolygon.setPosition(position.x, position.y);

        spriteAnim.setPosition(position.x - 32, position.y);

        currentFrame += 22*delta;

        if(alive) {
            if (position.y < BOT_SIDE) {
                position.y = BOT_SIDE;
                playerCubeRectangle.setY(BOT_SIDE);
                spriteAnim.setY(BOT_SIDE);
            }
            if (position.y > TOP_SIDE) {
                position.y = TOP_SIDE;
                playerCubeRectangle.setY(TOP_SIDE);
                spriteAnim.setY(TOP_SIDE);
            }
        }
    }

    // Прыжок
    public void onClick() {
        if(alive && Jump) {
            velocity.y = 300;
            GRAVITY = -15;

            Jump = true;   //1
            Line = false;  //0
            Shield = false;//0
        }
    }
    //Состояние: Линия
    public void onLine() {
        if(alive) {
            velocity.y = playerPos;
            position.add(0, playerPos);
            GRAVITY = 0;

            Jump = false;  //0
            Line = true;   //1
            Shield = false;//0
        }
    }

    // Возвращает гравитацию, когда отпускаем кнопку(кнопки на сенсоре), с линией или щитом
    public void onRelease() {
        GRAVITY = -15;

        Jump = true;   //1
        Line = false;  //0
        Shield = false;//0
    }

    //DRAW
    public void drawPlayerCube(ShapeRenderer shapeRenderer){
        switchCubeColor(shapeRenderer);
        shapeRenderer.rect(position.x, position.y, TEXTURE_SIZE,TEXTURE_SIZE);

        if(Line && alive) {
            switchLineCubeColor(shapeRenderer);
            shapeRenderer.rect(position.x, position.y, TEXTURE_SIZE,TEXTURE_SIZE);
        }
    }
    public void drawPlayerLine(ShapeRenderer shapeRenderer) {
        if(alive) {
            switchLineCubeColor(shapeRenderer);
            shapeRenderer.rect(0, playerCubeRectangle.getY() + 16 - lineY / 2, 96 - 3, lineY);
            shapeRenderer.rect(96 + 32 + 3, playerCubeRectangle.getY() + 16 - lineY / 2, 512, lineY);
        }
    }

    public void drawPlayerAnimation(SpriteBatch sb){
        if(alive) {
            elapsedTime += Gdx.graphics.getDeltaTime();
            if (currentFrame > 8) {
                currentFrame = 0;
            }
                spriteAnim.setRegion(32 * (int) currentFrame, 0, 32, 32);
                if (Jump) {
                    spriteAnim.draw(sb);
                }
        }
    }
    public void inMenuAnimation(SpriteBatch sb,float delta, Application game){
            switch (Application.gameSkin){
                case 0: //texture[0] = game.playerSkin0;
                    texture = game.playerSkin0;
                    spriteAnim = new Sprite(texture);
                    break;
                case 1: texture = game.playerSkin1;
                    spriteAnim = new Sprite(texture);
                    break;
                case 2: texture = game.playerSkin2;
                    spriteAnim = new Sprite(texture);
                    break;
                case 3: texture = game.playerSkin3;
                    spriteAnim = new Sprite(texture);
                    break;
                case 4: texture = game.playerSkin4;
                    spriteAnim = new Sprite(texture);
                    break;
            }
            spriteAnim.setSize(32,32);
            spriteAnim.setPosition(position.x - 32, position.y);
            elapsedTime += Gdx.graphics.getDeltaTime();
            if (currentFrame > 8) {
                currentFrame = 0;
            }
            spriteAnim.setPosition(position.x - 32, position.y);
            currentFrame += 22*delta;

            spriteAnim.setRegion(32 * (int) currentFrame, 0, 32, 32);
            spriteAnim.draw(sb);

    }

    private void switchCubeColor(ShapeRenderer shapeRenderer){

        switch(Application.gameSkin) {
            case 0:
                shapeRenderer.setColor(Globals.OrangeColor);
                break;
            case 1:
                shapeRenderer.setColor(Globals.Sides1Color);
                break;
            case 2:
                shapeRenderer.setColor(Globals.Player2Color);
                break;
            case 3: shapeRenderer.setColor(Globals.Player3Color);
                break;
            case 4: shapeRenderer.setColor(Globals.Player4Color);
                break;
        }
    }
    private void switchLineCubeColor(ShapeRenderer shapeRenderer){

        switch (Application.gameSkin) {
            case 0: shapeRenderer.setColor(Globals.LightBlueColor);
                break;
            case 1: shapeRenderer.setColor(Globals.Line1Color);
                break;
            case 2: shapeRenderer.setColor(Globals.Line2Color);
                break;
            case 3: shapeRenderer.setColor(Globals.Line3Color);
                break;
            case 4: shapeRenderer.setColor(Globals.Line4Color);
        }
    }


    public boolean sidesCollision() {
        if(alive) {
            if (position.y <= 11 || position.y >= 267) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private boolean isJumped = false;
    public boolean deathPlayed = false;

    public void deathAnimation(){
        if(!deathPlayed) {
            velocity.y = 300;
            GRAVITY = -30; //15
            isJumped = true;
            deathPlayed = true;
        }
    }

    public Rectangle getPlayerRectangle() {
        return playerCubeRectangle;
    }

    public Polygon getPlayerPolygon() {
        return playerPolygon;
    }

    public Vector2 getPosition() {
        return position;
    }
}

