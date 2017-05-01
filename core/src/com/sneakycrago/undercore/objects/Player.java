package com.sneakycrago.undercore.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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

    public boolean Alive = true;
    public boolean Death = false;

    private Rectangle playerCubeRectangle;

    private Polygon playerPolygon;

    private TextureAtlas animationAtlas;
    private Animation animation;
    private float elapsedTime = 0f;


    public Player(float x, float y) {
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);

        playerCubeRectangle = new Rectangle(x, y, 32, 32);

        playerPolygon = new Polygon(new float[] {
                0,0,
                0,32,
                32,32,
                32,0});

        animationAtlas = new TextureAtlas(Gdx.files.internal("textures/animation/playerAnim.atlas"),
                Gdx.files.internal("textures/animation"));

        animation = new Animation(1f/18f, animationAtlas.getRegions()); // 1f/18f
    }

    public void update(float delta) {
        velocity.add(0, GRAVITY);
        velocity.scl(delta);

        // x = SPEED * delta = движение по x
        position.add(0, velocity.y);
        // не заходит за белые границы
        sidesGhost(true);

        velocity.scl(1 / delta);

        playerCubeRectangle.setPosition(position.x, position.y);

        playerPolygon.setPosition(position.x, position.y);

    }

    // Прыжок
    public void onClick() {
        velocity.y = 300;
        GRAVITY = -15;

        Jump = true;   //1
        Line = false;  //0
        Shield = false;//0
    }
    //Состояние: Линия
    public void onLine() {

        velocity.y = playerPos;
        position.add(0, playerPos);
        GRAVITY = 0;

        Jump = false;  //0
        Line = true;   //1
        Shield = false;//0
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
        shapeRenderer.setColor(Globals.OrangeColor);
        shapeRenderer.rect(position.x, position.y, TEXTURE_SIZE,TEXTURE_SIZE);

        if(Line) {
            shapeRenderer.setColor(Globals.LightBlueColor);
            shapeRenderer.rect(position.x, position.y, TEXTURE_SIZE,TEXTURE_SIZE);
        }
    }
    public void drawPlayerLine(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Globals.LightBlueColor);
        shapeRenderer.rect(0, playerCubeRectangle.getY() + 16 - lineY /2, 96 - 3, lineY);
        shapeRenderer.rect(96 + 32  + 3, playerCubeRectangle.getY() + 16 - lineY /2, 512, lineY);
    }

    public void drawPlayerAnimation(SpriteBatch sb){
        elapsedTime += Gdx.graphics.getDeltaTime();
        if(Jump) {
            sb.draw(animation.getKeyFrame(elapsedTime, true), playerCubeRectangle.getX() - 32-2, playerCubeRectangle.getY());
        }
    }

    public boolean sidesCollision() {
        if(position.y <= 11 || position.y >= 267) {
            return true;
        } else {
            return false;
        }
    }
    // может ли зайти за белые границы
    public void sidesGhost(boolean set) {
        if(set) {
            if (position.y < BOT_SIDE) {
                position.y = BOT_SIDE;
            }
            if (position.y > TOP_SIDE) {
                position.y = TOP_SIDE;
            }
        } else if(!set) {
            if (position.y < -32) {
                position.y = -32;
            }
            if (position.y > 310+32) {
                position.y = 310+32;
            }
        }
    }

    public Rectangle getPlayerRectangle() {
        return playerCubeRectangle;
    }

    public Polygon getPlayerPolygon() {
        return playerPolygon;
    }
}

