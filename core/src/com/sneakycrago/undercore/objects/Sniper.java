package com.sneakycrago.undercore.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.Circle;
import com.sneakycrago.undercore.Application;
import com.sneakycrago.undercore.utils.Globals;
import com.sneakycrago.undercore.utils.Score;

/**
 * Created by Sneaky Crago on 27.04.2017.
 */

public class Sniper {

    private final int SPEED = -90; // -90

    private int x;
    private int y;

    private int line = 3;

    private Vector2 posBlock;
    private Vector2 velocity;

    private Vector2 bulletPos;

    private Sprite bullet;
    private Texture texture;


    //trajectory variables
    private int playerLastPos;
    private float startX = -32, startY, endX = 96, endY;
    private float distance, directionX, directionY, X, Y, grad, currentFrame;
    private boolean playerChecked = false;
    private boolean isRotated1 = false;

    private Rectangle stationRect;

    private Circle circle;

    public Sniper(int START, int Y){
        x = START;
        y = Y;
        startY = Y;

        texture = new Texture(Gdx.files.internal("textures/animation/sniper.png"));

        posBlock = new Vector2(x,11);
        velocity = new Vector2();

        bullet = new Sprite(texture);
        bullet.setSize(32,32);
        bullet.setOrigin(16,16);
        bullet.setPosition(startX, startY);

        bulletPos = new Vector2(startX,y);

        stationRect = new Rectangle(posBlock.x,posBlock.y + y, 32, 32);

        circle = new Circle(bullet.getX()+ 16, bullet.getY() + 16, 16);
    }
    public void update(float delta){
        //movement
        velocity.add(0, 0);
        velocity.scl(delta);

        // x = SPEED * delta = движение по x
        posBlock.add(SPEED * delta, velocity.y);
        posBlock.add(SPEED * delta, velocity.y);

        velocity.scl(1 / delta);


        endY =playerLastPos - 16;
        currentFrame += 12 * delta;

        moveBullet(delta);
        moveBulletSpite();

        moveCollision();

    }

    private boolean isScored = false;

    public void checkScore() {
        if(stationRect.getX() <= 96+16 && !isScored) {
                isScored = true;
                Score.addGameScore(1);
        }
    }


    //BulletDrawingLogic
    public void drawBullet(SpriteBatch spriteBatch){
        if(currentFrame >4){
           currentFrame = 0;
        }

        bullet.setRegion(128 * (int)currentFrame,0, 128,128);

        bullet.draw(spriteBatch);

    }

    //SniperBlockDrawing
    public void drawSniperBlock(ShapeRenderer shapeRenderer, int playerY){
        drawSniperStation(shapeRenderer);

        getPlayerPos(playerY);

    }
    public void drawSniperLine(ShapeRenderer shapeRenderer, int playerY) {
        drawLine(shapeRenderer, playerY);
    }

    private void switchInner(ShapeRenderer shapeRenderer){
        switch(Application.gameSkin) {
            case 0: shapeRenderer.setColor(Color.BLACK);
                break;
            case 1: shapeRenderer.setColor(Globals.Inner1Color);
                break;
            case 2: shapeRenderer.setColor(Globals.Inner2Color);
                break;
            case 3: shapeRenderer.setColor(Globals.Inner3Color);
                break;
            case 4: shapeRenderer.setColor(Globals.Inner4Color);
                break;
        }
    }

    private void switchSides(ShapeRenderer shapeRenderer){
        switch(Application.gameSkin) {
            case 0: shapeRenderer.setColor(Globals.SidesColor);
                break;
            case 1: shapeRenderer.setColor(Globals.Sides1Color);
                break;
            case 2: shapeRenderer.setColor(Globals.Sides2Color);
                break;
            case 3: shapeRenderer.setColor(Globals.Sides3Color);
                break;
            case 4: shapeRenderer.setColor(Globals.Sides4Color);
                break;
        }
    }

    private void drawSniperStation(ShapeRenderer shapeRenderer) {

        switchInner(shapeRenderer);

        shapeRenderer.rect(posBlock.x + 3, posBlock.y + y + 3, 32-6, 32-6);

        switchSides(shapeRenderer);

        shapeRenderer.rect(posBlock.x, posBlock.y + y, line, 32);
        shapeRenderer.rect(posBlock.x +line, posBlock.y + y, 32-line, line);
        shapeRenderer.rect(posBlock.x + 32-line, posBlock.y + y + line, line, 32-line);
        shapeRenderer.rect(posBlock.x + line, posBlock.y +y + 32-line, 32-line, line);

    }
    private void drawLine(ShapeRenderer shapeRenderer,int playerY){
        if(posBlock.x < 256+ 128 && posBlock.x > -96) {
            shapeRenderer.setColor(Globals.RedLineColor);
            shapeRenderer.circle(posBlock.x + 16, posBlock.y + y + 16, 1);
            shapeRenderer.rectLine(posBlock.x + 16, posBlock.y + y + 16, 96 + 16, playerY, 2);
        }
        if(posBlock.x < -96 && posBlock.x > -192) {
            shapeRenderer.setColor(1f,0f,0f,1f);
            shapeRenderer.rectLine(posBlock.x + 16, posBlock.y + y + 16, 96 + 16, playerY, 4);
        }
    }

    private void getPlayerPos(int playerY){
        if(posBlock.x < -192 && !playerChecked) {
            playerLastPos = playerY;
            playerChecked = true;
        }
    }

    private void moveBulletSpite(){
        bullet.setPosition(bulletPos.x, bulletPos.y);
    }
    private void moveBullet(float delta){
        distance = (float) Math.sqrt(Math.pow(endX-startX,2)+Math.pow(endY-startY,2));
        directionX = (endX-startX) / distance;
        directionY = (endY-startY) / distance;
        if(playerLastPos != 0) {
            bulletPos.x += directionX * SPEED*(-2) *delta;
            bulletPos.y += directionY * SPEED*(-2)* delta;
        }
        if(playerLastPos != 0 && !isRotated1) {
            X = (96 - 16 - bullet.getX());
            Y = (playerLastPos - bullet.getY());
            grad = (float) Math.atan2(Y, X) * (180 / (float) Math.PI);
            bullet.setRotation(grad);
            isRotated1 = true;
        }
    }

    public void dispose(){
        texture.dispose();
    }

    private void moveCollision() {
        stationRect.setPosition(posBlock.x, posBlock.y + y);

        circle.set(bulletPos.x, bulletPos.y, 16);
    }

    public Rectangle getStationRect() {
        return stationRect;
    }

    public Circle getCircle() {
        return circle;
    }

    public Sprite getBullet() {
        return bullet;
    }
}
