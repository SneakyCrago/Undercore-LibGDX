package com.sneakycrago.undercore.objects;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sneakycrago.undercore.Application;
import com.sneakycrago.undercore.utils.Globals;
import com.sneakycrago.undercore.utils.Score;

/**
 * Created by Sneaky Crago on 26.03.2017.
 */

public class Corridor {
    public int SPEED = -90; //-90
    private final int TEXTURE_SIZE = 32;
    private final int HEIGHT = 24;
    private final int FREE_SPACE = TEXTURE_SIZE*3;
    public final int BLOCK_SIZE = TEXTURE_SIZE*19;

    private Vector2 posBlock;
    private Vector2 velocity;

    private Rectangle[] topLeftRects;
    private Rectangle[] topRightRects;
    private Rectangle[] bottomRets;

    private Rectangle endZone;

    private int x;

    public Corridor(float START) {

        x =(int) START;

        posBlock = new Vector2(0,11);
        velocity = new Vector2();

        topLeftRects = new Rectangle[7];
        topRightRects = new Rectangle[7];
        bottomRets = new Rectangle[15];

        createRects(x);
    }
    public void update(float delta){
        if(Application.playerAlive) {
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
    }
    //SCORE
    boolean halfScore = false;
    boolean secondHalfScore = false;
    public void checkScore() {
        if(bottomRets[7].getX() <= 96+16 && !halfScore) {
            halfScore = true;
            Score.addGameScore(3);
        }
        if(topRightRects[6].getX() <= 96+16 && !secondHalfScore) {
            secondHalfScore = true;
            Score.addGameScore(3);
        }
    }

    //RECTS
    //create rectangles
    private void createRects(float x){
        createTopLeftRects(x);
        createTopRightRects(x);
        createBottomRects(x);

        endZone = new Rectangle(x + BLOCK_SIZE, posBlock.y, 1, 288);

    }

    //move rects block
    private void moveRects(float x) {
        moveTopLeftRects(x);
        moveTopRightRects(x);
        moveBottomRects(x);
    }

    //move rect elements
    private void moveTopLeftRects(float x) {
        topLeftRects[0].setX(posBlock.x +x);
        for(int i = 1; i<topLeftRects.length; i++) {
            topLeftRects[i].setX(posBlock.x +x+ TEXTURE_SIZE*i);
        }
    }
    private void moveTopRightRects(float x) {
        topRightRects[0].setX(posBlock.x +32+ x + 32*8+ 32*3 -3);
        for(int i =1; i<6; i++){
            topRightRects[i].setX(posBlock.x +32 + x +TEXTURE_SIZE*(8+3) + TEXTURE_SIZE*i - 3);
        }
        topRightRects[6].setX(posBlock.x + x + BLOCK_SIZE-32 - 3);
    }
    private void moveBottomRects(float x) {
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
    private void createTopLeftRects(float x) {
        topLeftRects[0] = new Rectangle();
        topLeftRects[0].set(posBlock.x + x, posBlock.y +24+ FREE_SPACE, 32, TEXTURE_SIZE*6 - 24);
        for(int i = 1; i < topLeftRects.length; i++) {
            topLeftRects[i] = new Rectangle();
            topLeftRects[i].set(posBlock.x + x+ TEXTURE_SIZE*2, posBlock.y+ 24+FREE_SPACE + HEIGHT*i,
                    TEXTURE_SIZE, TEXTURE_SIZE*6 -HEIGHT*i - 24);
        }
    }
    private void createTopRightRects(float x) {
        topRightRects[0] = new Rectangle();
        topRightRects[0].set(posBlock.x + x + TEXTURE_SIZE*(8+3) -3 + 32, posBlock.y+ FREE_SPACE + HEIGHT*7, TEXTURE_SIZE, HEIGHT);
        for(int i = 1; i < 6; i++) {
            topRightRects[i] = new Rectangle();
            topRightRects[i].set(posBlock.x +32 + x +TEXTURE_SIZE*(8+3) + TEXTURE_SIZE*i - 3,
                    posBlock.y + FREE_SPACE + + HEIGHT*7-HEIGHT*i, TEXTURE_SIZE, HEIGHT +HEIGHT*i);
        }
        topRightRects[6] = new Rectangle();
        topRightRects[6].set(posBlock.x + x + BLOCK_SIZE-TEXTURE_SIZE - 3,posBlock.y+ FREE_SPACE + 24,
                TEXTURE_SIZE + 3, TEXTURE_SIZE*6 -24);
    }
    private void createBottomRects(float x) {
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
    public void drawCorridor(ShapeRenderer shapeRenderer) {
        drawDown(shapeRenderer, x);
        drawTopLeft(shapeRenderer, x);
        drawTopRight(shapeRenderer, x);
    }

    //colors switch
    private void switchInner(ShapeRenderer shapeRenderer){
        switch (Application.gameSkin){
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
        switch(Application.gameSkin){
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

    //draw block elements
    private void drawDown(ShapeRenderer shapeRenderer, float x) {
        //down corridor
        switchInner(shapeRenderer);
        shapeRenderer.rect(posBlock.x + x + TEXTURE_SIZE* 2 + 3, 11-3, (TEXTURE_SIZE * 15)-6, 3); // bot

        for(int i =0; i < bottomRets.length; i++){
            shapeRenderer.rect(bottomRets[i].getX(), bottomRets[i].getY(), bottomRets[i].getWidth(), bottomRets[i].getHeight() -3);
        }

        switchSides(shapeRenderer);
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
    private void drawTopLeft(ShapeRenderer shapeRenderer, float x) {
        switchInner(shapeRenderer);
        shapeRenderer.rect(posBlock.x + x+ 3, 310 -11, (TEXTURE_SIZE * 7)-6, 3);

        for(int i =0; i < topLeftRects.length; i++){
            shapeRenderer.rect(topLeftRects[i].getX(), topLeftRects[i].getY(), topLeftRects[i].getWidth(), topLeftRects[i].getHeight());
        }

        switchSides(shapeRenderer);

        shapeRenderer.rect(posBlock.x + x,posBlock.y +FREE_SPACE + 24,3, TEXTURE_SIZE*5 + 8);

        shapeRenderer.rect(posBlock.x + x,posBlock.y +FREE_SPACE +24,TEXTURE_SIZE, 3);
        for(int i = 1; i <8;i++) {
            shapeRenderer.rect(posBlock.x + x +(TEXTURE_SIZE* i) -3, posBlock.y - 24+ FREE_SPACE + 24+ HEIGHT * i,
                    3, 24);
            shapeRenderer.rect(posBlock.x + x +((TEXTURE_SIZE) * i) -3, posBlock.y- 24 + FREE_SPACE + 24+ 24+ HEIGHT * i,
                    TEXTURE_SIZE, 3);
        }
    }
    private void drawTopRight(ShapeRenderer shapeRenderer, float x){
        switchInner(shapeRenderer);
        shapeRenderer.rect(posBlock.x + x+ TEXTURE_SIZE * (8+3) + 32, 310-11, TEXTURE_SIZE*7 - 3,3);

        for(int i =0; i < topRightRects.length; i++){
            shapeRenderer.rect(topRightRects[i].getX(), topRightRects[i].getY(), topRightRects[i].getWidth(), topRightRects[i].getHeight());
        }

        switchSides(shapeRenderer);

        shapeRenderer.rect(posBlock.x + x + TEXTURE_SIZE *19 -3,posBlock.y + FREE_SPACE + 24,3, TEXTURE_SIZE*5 + 8);

        shapeRenderer.rect(posBlock.x + x + TEXTURE_SIZE* 18 -3, posBlock.y+ FREE_SPACE +24,3,24);
        shapeRenderer.rect(posBlock.x + x+ TEXTURE_SIZE*18 -3, posBlock.y +FREE_SPACE + 24, TEXTURE_SIZE + 3,3);
        //down
        for(int i=7; i > 1;i--){
            shapeRenderer.rect(posBlock.x + x + TEXTURE_SIZE* (7+3) +(TEXTURE_SIZE* i) -3,
                    posBlock.y+ FREE_SPACE + 24 +HEIGHT*8 - HEIGHT * i, 3, 24);
            shapeRenderer.rect(posBlock.x + x +TEXTURE_SIZE* (7+3)+((TEXTURE_SIZE) * i) -3,
                    posBlock.y+ FREE_SPACE + 24  + HEIGHT*8 - HEIGHT * i, TEXTURE_SIZE + 3, 3);
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

