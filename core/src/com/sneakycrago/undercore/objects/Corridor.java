package com.sneakycrago.undercore.objects;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sneakycrago.undercore.utils.Score;

/**
 * Created by Sneaky Crago on 26.03.2017.
 */

public class Corridor {
    private final int TEXTURE_SIZE = 32;
    private final int HEIGHT = 24;
    private final int SPEED = -90; //-90
    private final int FREE_SPACE = TEXTURE_SIZE*3;
    private final int BLOCK_SIZE = TEXTURE_SIZE*19;

    private Vector2 posBlock;
    private Vector2 velocity;

    private Rectangle[] topLeftRects;
    private Rectangle[] topRightRects;
    private Rectangle[] bottomRets;

    private Rectangle endZone;
    public Corridor(float x) {
        posBlock = new Vector2(0,11);
        velocity = new Vector2();

        topLeftRects = new Rectangle[8];
        topRightRects = new Rectangle[8];
        bottomRets = new Rectangle[15];

        createRects(x);
    }
    public void update(float delta, float x){
        //movement
        velocity.add(0, 0);
        velocity.scl(delta);

        // x = SPEED * delta = движение по x
        posBlock.add(SPEED * delta, velocity.y);
        posBlock.add(SPEED * delta, velocity.y);

        velocity.scl(1 / delta);

        //update positions(for rectangles)
        moveRects(x);
        endZone.setX(posBlock.x + x + BLOCK_SIZE);

        checkScore();
    }
    //SCORE
    boolean halfScore = false;
    boolean secondHalfScore = false;
    public void checkScore() {
        if(bottomRets[7].getX() <= 96+16 && !halfScore) {
            halfScore = true;
            Score.addGameScore(3);
        }
        if(topRightRects[7].getX() <= 96+16 && !secondHalfScore) {
            secondHalfScore = true;
            Score.addGameScore(3);
        }
    }

    //RECTS
    //create rectangles
    public void createRects(float x){
        createTopLeftRects(x);
        createTopRightRects(x);
        createBottomRects(x);

        endZone = new Rectangle(x + BLOCK_SIZE, posBlock.y, 1, 288);

    }

    //move rects block
    public void moveRects(float x) {
        moveTopLeftRects(x);
        moveTopRightRects(x);
        moveBottomRects(x);
    }

    //move rect elements
    public void moveTopLeftRects(float x) {
        topLeftRects[0].setX(posBlock.x +x);
        for(int i = 1; i<topLeftRects.length; i++) {
            topLeftRects[i].setX(posBlock.x +x+ TEXTURE_SIZE*i);
        }
    }
    public void moveTopRightRects(float x) {
        topRightRects[0].setX(posBlock.x + x + 32*8+ 32*3 -3);
        for(int i =1; i<7; i++){
            topRightRects[i].setX(posBlock.x + x +TEXTURE_SIZE*(8+3) + TEXTURE_SIZE*i - 3);
        }
        topRightRects[7].setX(posBlock.x + x + BLOCK_SIZE-32 - 3);
    }
    public void moveBottomRects(float x) {
        bottomRets[0].setX(posBlock.x + x+ 32*2);

        for(int i = 1; i < 7; i++) {
            bottomRets[i].setX(posBlock.x + x+ 32*2 + TEXTURE_SIZE*i-3);
        }

        bottomRets[7].setX(posBlock.x +x+ 32*2 + TEXTURE_SIZE *7-3);

        for(int i = 8; i < bottomRets.length; i++) {
            bottomRets[i].setX(posBlock.x + x + 32*2 + TEXTURE_SIZE*i);
        }
    }

    //create elements
    public void createTopLeftRects(float x) {
        topLeftRects[0] = new Rectangle();
        topLeftRects[0].set(posBlock.x + x, posBlock.y + FREE_SPACE, 32, TEXTURE_SIZE*6);
        for(int i = 1; i < topLeftRects.length; i++) {
            topLeftRects[i] = new Rectangle();
            topLeftRects[i].set(posBlock.x + x+ TEXTURE_SIZE*2, posBlock.y+ FREE_SPACE + HEIGHT*i,
                    TEXTURE_SIZE, TEXTURE_SIZE*6 -HEIGHT*i);
        }
    }
    public void createTopRightRects(float x) {
        topRightRects[0] = new Rectangle();
        topRightRects[0].set(posBlock.x + x + TEXTURE_SIZE*(8+3) -3, posBlock.y+ FREE_SPACE + HEIGHT*7, TEXTURE_SIZE, HEIGHT);
        for(int i = 1; i < 7; i++) {
            topRightRects[i] = new Rectangle();
            topRightRects[i].set(posBlock.x + x +TEXTURE_SIZE*(8+3) + TEXTURE_SIZE*i - 3, posBlock.y + FREE_SPACE + + HEIGHT*7-HEIGHT*i,
                    TEXTURE_SIZE, HEIGHT +HEIGHT*i);
        }
        topRightRects[7] = new Rectangle();
        topRightRects[7].set(posBlock.x + x + BLOCK_SIZE-TEXTURE_SIZE - 3,posBlock.y+ FREE_SPACE,
                TEXTURE_SIZE + 3, TEXTURE_SIZE*6);
    }
    public void createBottomRects(float x) {
        bottomRets[0] = new Rectangle();
        bottomRets[0].set(posBlock.x + x+ 32*2, posBlock.y, TEXTURE_SIZE,HEIGHT+ 3);
        for(int i = 1; i < 7; i++) {
            bottomRets[i] = new Rectangle();
            bottomRets[i].set(posBlock.x + x+ 32*2 + TEXTURE_SIZE*i -3, posBlock.y, TEXTURE_SIZE,HEIGHT+ HEIGHT*i + 3);
        }
            bottomRets[7] = new Rectangle();
            bottomRets[7].set(posBlock.x +x+ 32*2 + TEXTURE_SIZE *7 -3, posBlock.y, TEXTURE_SIZE + 3, TEXTURE_SIZE*6 + 3);
        for(int i = 8; i < bottomRets.length; i++) {
            bottomRets[i] = new Rectangle();
            bottomRets[i].set(posBlock.x + x + 32*2 + TEXTURE_SIZE*i, posBlock.y, TEXTURE_SIZE,3 + HEIGHT*7 - HEIGHT*(i-8));
        }
    }

    //DRAW
    //draw block
    public void drawCorridor(ShapeRenderer shapeRenderer, float x) {
        drawDown(shapeRenderer, x);
        drawTopLeft(shapeRenderer, x);
        drawTopRight(shapeRenderer, x);
    }

    //draw block elements
    public void drawDown(ShapeRenderer shapeRenderer, float x) {
        //down corridor
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(posBlock.x + x + TEXTURE_SIZE* 2 + 3, 11-3, (TEXTURE_SIZE * 15)-6, 3); // bot

        shapeRenderer.setColor(22/255f,238/255f,247/255f,1f);
        //1
        shapeRenderer.rect(posBlock.x + x + TEXTURE_SIZE* 2,posBlock.y,3,HEIGHT);
        shapeRenderer.rect(posBlock.x+ x + TEXTURE_SIZE*2,posBlock.y + HEIGHT, TEXTURE_SIZE,3);
        //
        shapeRenderer.rect(posBlock.x + x + (TEXTURE_SIZE* 4)- 3,posBlock.y +HEIGHT * 2,3,HEIGHT);
        //up
        for(int i = 1; i <8;i++) {
            shapeRenderer.rect(posBlock.x + x + TEXTURE_SIZE* 2 +(TEXTURE_SIZE* i) -3, posBlock.y + HEIGHT * i,
                    3, 24);
            shapeRenderer.rect(posBlock.x + x +TEXTURE_SIZE* 2+((TEXTURE_SIZE) * i) -3, posBlock.y + 24+ HEIGHT * i,
                    TEXTURE_SIZE, 3);
        }
        shapeRenderer.rect(posBlock.x + x +TEXTURE_SIZE* 2+((TEXTURE_SIZE) * 7) -3, posBlock.y + 24+ HEIGHT * 7,
                TEXTURE_SIZE + 3, 3);
        //down
        for(int i=7; i > 0;i--){
            shapeRenderer.rect(posBlock.x + x + TEXTURE_SIZE* 9 +(TEXTURE_SIZE* i) -3, posBlock.y +HEIGHT*8 - HEIGHT * i,
                    3, 24);
            shapeRenderer.rect(posBlock.x + x +TEXTURE_SIZE* 9+((TEXTURE_SIZE) * i) -3, posBlock.y + HEIGHT*8 - HEIGHT * i,
                    TEXTURE_SIZE + 3, 3);
        }
        shapeRenderer.rect(posBlock.x + x + TEXTURE_SIZE* (17) -3, posBlock.y,
                3, 24);
    }
    public void drawTopLeft(ShapeRenderer shapeRenderer, float x) {
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(posBlock.x + x+ 3, 310 -11, (TEXTURE_SIZE * 9)-6, 3);

        shapeRenderer.setColor(22/255f,238/255f,247/255f,1f);

        shapeRenderer.rect(posBlock.x + x,posBlock.y +FREE_SPACE,3, TEXTURE_SIZE*6);

        shapeRenderer.rect(posBlock.x + x,posBlock.y +FREE_SPACE,TEXTURE_SIZE, 3);
        for(int i = 1; i <9;i++) {
            shapeRenderer.rect(posBlock.x + x +(TEXTURE_SIZE* i) -3, posBlock.y - 24+ FREE_SPACE+ HEIGHT * i,
                    3, 24);
            shapeRenderer.rect(posBlock.x + x +((TEXTURE_SIZE) * i) -3, posBlock.y- 24 + FREE_SPACE+ 24+ HEIGHT * i,
                    TEXTURE_SIZE, 3);
        }
    }
    public void drawTopRight(ShapeRenderer shapeRenderer, float x){
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(posBlock.x + x+ TEXTURE_SIZE * (8+3), 310-11, TEXTURE_SIZE*8 - 3,3);

        shapeRenderer.setColor(22/255f,238/255f,247/255f,1f);
        shapeRenderer.rect(posBlock.x + x + TEXTURE_SIZE *19 -3,posBlock.y + FREE_SPACE,3, TEXTURE_SIZE*6);

        shapeRenderer.rect(posBlock.x + x + TEXTURE_SIZE* 18 -3, posBlock.y+ FREE_SPACE ,3,24);
        shapeRenderer.rect(posBlock.x + x+ TEXTURE_SIZE*18 -3, posBlock.y +FREE_SPACE, TEXTURE_SIZE + 3,3);
        //down
        for(int i=7; i > 0;i--){
            shapeRenderer.rect(posBlock.x + x + TEXTURE_SIZE* (7+3) +(TEXTURE_SIZE* i) -3, posBlock.y+ FREE_SPACE +HEIGHT*8 - HEIGHT * i,
                    3, 24);
            shapeRenderer.rect(posBlock.x + x +TEXTURE_SIZE* (7+3)+((TEXTURE_SIZE) * i) -3, posBlock.y+ FREE_SPACE + HEIGHT*8 - HEIGHT * i,
                    TEXTURE_SIZE + 3, 3);
        }
    }

    public Rectangle[] getTopLeftRects() {
        return topLeftRects;
    }
    public Rectangle[] getTopRightRects() {
        return topRightRects;
    }
    public Rectangle[] getBottomRets() {
        return bottomRets;
    }

    public Rectangle getEndZone() {
        return endZone;
    }
}

