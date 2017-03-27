package com.sneakycrago.undercore.objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sneakycrago.undercore.Application;

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

    private boolean Jump = true;
    private boolean Line = false;
    private boolean Shield = false;

    private Rectangle playerCubeRectangle;

    public Player(float x, float y) {
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);

        playerCubeRectangle = new Rectangle(x, y, 32, 32);

    }

    public void update(float delta) {
        velocity.add(0, GRAVITY);
        velocity.scl(delta);

        // x = SPEED * delta = движение по x
        position.add(0, velocity.y);
        // не заходит за белые границы
        if (position.y < BOT_SIDE) {
            position.y = BOT_SIDE;
        }
        if (position.y > TOP_SIDE) {
            position.y = TOP_SIDE;
        }
        velocity.scl(1 / delta);

        playerCubeRectangle.setPosition(position.x, position.y);
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

    public void drawPlayerCube(ShapeRenderer shapeRenderer){
        shapeRenderer.setColor(255/255f,162/255f, 38/255f, 1f);
        shapeRenderer.rect(position.x, position.y, TEXTURE_SIZE,TEXTURE_SIZE);
    }

    public void drawPlayerLine(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(255/255f,162/255f, 38/255f, 1f);
        shapeRenderer.rect(0, playerCubeRectangle.getY() + 16 - lineY /2, 96 - 3, lineY);
        shapeRenderer.rect(96 + 32  + 3, playerCubeRectangle.getY() + 16 - lineY /2, 512, lineY);
    }

    public boolean sidesCollision() {
        if(position.y <= 11 || position.y >= 267) {
            return true;
        } else {
            return false;
        }
    }

    public Rectangle getPlayerRectangle() {
        return playerCubeRectangle;
    }
}

