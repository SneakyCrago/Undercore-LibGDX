package com.sneakycrago.undercore.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sneakycrago.undercore.Application;
import com.sneakycrago.undercore.utils.Globals;
import com.sneakycrago.undercore.utils.Score;

import java.util.Random;

/**
 * Created by Sneaky Crago on 03.04.2017.
 */

public class Circle {

    public int SPEED = -90;
    private int FREE_SPACE = 64;
    private int SPRITE_SIZE = 90;
    public int BLOCK_SIZE;

    private TextureAtlas circleAtlas;

    private Sprite circleTop[],circleBot[];
    private Sprite circleMiddle[];

    private Rectangle topRect[],botRect[];
    private Rectangle middleRect[];

    private Rectangle startZone;
    private Rectangle endZone;

    private Vector2 posCircle;
    private Vector2 velocity;

    private Random random;

    private int[] massive;
    private boolean isFive;

    private int botHeight = 11 +6;
    private int topHeight = 11 + Globals.FREE_SPACE *2;
    private int middleHeight = 11 + Globals.FREE_SPACE;

    private int firstX = 23;
    private int secondX = 15;
    private int px_size =8;

    private int x;

    public Circle(int start) {

        x = start;

        posCircle = new Vector2(0,11);
        velocity = new Vector2();

        circleAtlas = new TextureAtlas(Gdx.files.internal("textures/circle.atlas"), Gdx.files.internal("textures/"));

        random = new Random();

        randomizeBlock();
        createBlock();
        createRects();

        for(int i=0; i< isScoredTop.length; i++){
            isScoredTop[i]=false;
        }

        for(int i=0; i< isScoredMid.length; i++){
            isScoredMid[i]=false;
        }
    }

    public void update(float delta) {
        //movement
        velocity.add(0, 0);
        velocity.scl(delta);

        // x = SPEED * delta = движение по x
        posCircle.add(SPEED * delta, velocity.y);
        posCircle.add(SPEED * delta, velocity.y);

        velocity.scl(1 / delta);

        moveSprites();
        moveRects();

        if(Application.playerAlive) {
            checkScore();
        }
    }

    private boolean isScoredTop[];
    private boolean isScoredMid[];
    public void checkScore() {
        for(int i = 0; i < isScoredTop.length; i++) {
            if(circleTop[i].getX() +SPRITE_SIZE/2 <= 96 +16 && !isScoredTop[i]){
                isScoredTop[i] = true;
                Score.addGameScore(1);
            }
        }
        for(int i = 0; i < isScoredMid.length; i++) {
            if (circleMiddle[i].getX() +SPRITE_SIZE/2 <= 96+16 && !isScoredMid[i]) {
                isScoredMid[i] = true;
                Score.addGameScore(1);
            }
        }
    }

    private void randomizeBlock(){
        isFive = random.nextBoolean();

        if(isFive) {
            massive = new int[5];

            circleBot = new Sprite[3];
            circleTop = new Sprite[3];
            isScoredTop = new boolean[3];

            topRect = new Rectangle[3*4];
            botRect = new Rectangle[3*4];

            circleMiddle = new Sprite[2];
            isScoredMid = new boolean[2];

            middleRect = new Rectangle[2*4];

            BLOCK_SIZE = SPRITE_SIZE*5 + FREE_SPACE*4;

        } else {
            massive = new int[7];

            circleBot = new Sprite[4];
            circleTop = new Sprite[4];
            isScoredTop = new boolean[4];

            topRect = new Rectangle[4*4];
            botRect = new Rectangle[4*4];

            circleMiddle = new Sprite[3];
            isScoredMid = new boolean[3];

            middleRect = new Rectangle[3*4];

            BLOCK_SIZE = SPRITE_SIZE*7 + FREE_SPACE*6;
        }
    }

    private void createBlock(){

        for(int i=0; i < circleTop.length; i++) {
            switch (Application.gameSkin){
                case 0: circleTop[i] = new Sprite(circleAtlas.findRegion("circle"));
                    circleBot[i] = new Sprite(circleAtlas.findRegion("circle"));
                    break;
                case 1: circleTop[i] = new Sprite(circleAtlas.findRegion("circle1"));
                    circleBot[i] = new Sprite(circleAtlas.findRegion("circle1"));
                    break;
                case 2: circleTop[i] = new Sprite(circleAtlas.findRegion("circle2"));
                    circleBot[i] = new Sprite(circleAtlas.findRegion("circle2"));
                    break;
                case 3: circleTop[i] = new Sprite(circleAtlas.findRegion("circle3"));
                    circleBot[i] = new Sprite(circleAtlas.findRegion("circle3"));
                    break;
                case 4: circleTop[i] = new Sprite(circleAtlas.findRegion("circle4"));
                    circleBot[i] = new Sprite(circleAtlas.findRegion("circle4"));
                    break;
            }
            circleTop[i].setSize(SPRITE_SIZE,SPRITE_SIZE);
            circleBot[i].setSize(SPRITE_SIZE,SPRITE_SIZE);

            circleTop[i].setPosition(posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), topHeight);
            circleBot[i].setPosition(posCircle.x + x + i*(SPRITE_SIZE *2)+ i*(FREE_SPACE*2), botHeight);
        }
        for(int i=0; i < circleMiddle.length; i++) {

            switch (Application.gameSkin){
                case 0: circleMiddle[i] = new Sprite(circleAtlas.findRegion("circle"));
                    break;
                case 1: circleMiddle[i] = new Sprite(circleAtlas.findRegion("circle1"));
                    break;
                case 2: circleMiddle[i] = new Sprite(circleAtlas.findRegion("circle2"));
                    break;
                case 3: circleMiddle[i] = new Sprite(circleAtlas.findRegion("circle3"));
                    break;
                case 4: circleMiddle[i] = new Sprite(circleAtlas.findRegion("circle4"));
                    break;
            }
            circleMiddle[i].setSize(SPRITE_SIZE,SPRITE_SIZE);

            circleMiddle[i].setPosition(posCircle.x + x + i*(SPRITE_SIZE*2)+ 64 + SPRITE_SIZE + i*(FREE_SPACE*2), middleHeight);
        }
    }
    private void moveSprites(){
        for(int i=0; i < circleTop.length; i++) {
            circleTop[i].setX(posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));
            circleBot[i].setX(posCircle.x + x + i*(SPRITE_SIZE *2)+ i*(FREE_SPACE*2));
        }
        for(int i=0; i < circleMiddle.length; i++) {
            circleMiddle[i].setX(posCircle.x + x + i*(SPRITE_SIZE*2)+ 64 + SPRITE_SIZE + i*(FREE_SPACE*2));
        }
    }

    //RECTS each 4 rects = 1 Circle
    private void createRects(){
        for(int i=0; i< topRect.length; i++) {
            topRect[i] = new Rectangle();
            botRect[i] = new Rectangle();
        }

        for (int i =0; i < middleRect.length; i++) {
            middleRect[i] = new Rectangle();
        }

        createMiddle();
        createTopBot();

        startZone = new Rectangle(posCircle.x + x, posCircle.y, 1,288);
        endZone = new Rectangle();
        endZone.set(posCircle.x+ x + BLOCK_SIZE,posCircle.y, 1, 288);
    }
    private void createMiddle() {
        int i =0;

        create1(middleRect[0], posCircle.x + x + i*(SPRITE_SIZE*2)+ 64 + SPRITE_SIZE + i*(FREE_SPACE*2), middleHeight);
        create2(middleRect[1], posCircle.x + x + i*(SPRITE_SIZE*2)+ 64 + SPRITE_SIZE + i*(FREE_SPACE*2), middleHeight);
        create3(middleRect[2], posCircle.x + x + i*(SPRITE_SIZE*2)+ 64 + SPRITE_SIZE + i*(FREE_SPACE*2), middleHeight);
        create4(middleRect[3], posCircle.x + x + i*(SPRITE_SIZE*2)+ 64 + SPRITE_SIZE + i*(FREE_SPACE*2), middleHeight);

        i=1;

        create1(middleRect[4], posCircle.x + x + i*(SPRITE_SIZE*2)+ 64 + SPRITE_SIZE + i*(FREE_SPACE*2), middleHeight);
        create2(middleRect[5], posCircle.x + x + i*(SPRITE_SIZE*2)+ 64 + SPRITE_SIZE + i*(FREE_SPACE*2), middleHeight);
        create3(middleRect[6], posCircle.x + x + i*(SPRITE_SIZE*2)+ 64 + SPRITE_SIZE + i*(FREE_SPACE*2), middleHeight);
        create4(middleRect[7], posCircle.x + x + i*(SPRITE_SIZE*2)+ 64 + SPRITE_SIZE + i*(FREE_SPACE*2), middleHeight);

        if(!isFive) {
            i=2;
            create1(middleRect[8], posCircle.x + x + i *(SPRITE_SIZE*2)+64+SPRITE_SIZE+i*(FREE_SPACE * 2), middleHeight);
            create2(middleRect[9], posCircle.x + x + i *(SPRITE_SIZE*2)+64+SPRITE_SIZE+i*(FREE_SPACE * 2), middleHeight);
            create3(middleRect[10], posCircle.x + x + i *(SPRITE_SIZE*2)+64+SPRITE_SIZE+i*(FREE_SPACE * 2), middleHeight);
            create4(middleRect[11], posCircle.x + x + i *(SPRITE_SIZE*2)+64+SPRITE_SIZE+i*(FREE_SPACE * 2), middleHeight);
        }
    }
    private void createTopBot(){
        int i =0;

        create1(topRect[0], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), topHeight);
        create2(topRect[1], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), topHeight);
        create3(topRect[2], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), topHeight);
        create4(topRect[3], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), topHeight);

        create1(botRect[0], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), botHeight);
        create2(botRect[1], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), botHeight);
        create3(botRect[2], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), botHeight);
        create4(botRect[3], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), botHeight);

        i =1;
        create1(topRect[4], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), topHeight);
        create2(topRect[5], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), topHeight);
        create3(topRect[6], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), topHeight);
        create4(topRect[7], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), topHeight);

        create1(botRect[4], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), botHeight);
        create2(botRect[5], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), botHeight);
        create3(botRect[6], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), botHeight);
        create4(botRect[7], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), botHeight);

        i=2;
        create1(topRect[8], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), topHeight);
        create2(topRect[9], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), topHeight);
        create3(topRect[10], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), topHeight);
        create4(topRect[11], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), topHeight);

        create1(botRect[8], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), botHeight);
        create2(botRect[9], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), botHeight);
        create3(botRect[10], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), botHeight);
        create4(botRect[11], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), botHeight);

        if(!isFive){
            i =3;
            create1(topRect[12], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), topHeight);
            create2(topRect[13], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), topHeight);
            create3(topRect[14], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), topHeight);
            create4(topRect[15], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), topHeight);

            create1(botRect[12], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), botHeight);
            create2(botRect[13], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), botHeight);
            create3(botRect[14], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), botHeight);
            create4(botRect[15], posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2), botHeight);
        }
    }

    private void moveRects(){
        moveMiddle();
        moveTopBot();

        startZone.setX(posCircle.x +x);
        endZone.setX(posCircle.x +x+ BLOCK_SIZE);
    }
    private void moveMiddle() {
        int i=0;
        middleRect[0].setX(posCircle.x+firstX + x + i * (SPRITE_SIZE * 2) + 64 + SPRITE_SIZE + i * (FREE_SPACE * 2));
        middleRect[1].setX(posCircle.x+ x + i * (SPRITE_SIZE * 2) + 64 + SPRITE_SIZE + i * (FREE_SPACE * 2));
        middleRect[2].setX(posCircle.x + secondX + x + i * (SPRITE_SIZE * 2) + 64 + SPRITE_SIZE + i * (FREE_SPACE * 2));
        middleRect[3].setX(posCircle.x +secondX+1 -8+ x + i * (SPRITE_SIZE * 2) + 64 + SPRITE_SIZE + i * (FREE_SPACE * 2));

        i =1;
        middleRect[4].setX(posCircle.x+firstX + x + i * (SPRITE_SIZE * 2) + 64 + SPRITE_SIZE + i * (FREE_SPACE * 2));
        middleRect[5].setX(posCircle.x+ x + i * (SPRITE_SIZE * 2) + 64 + SPRITE_SIZE + i * (FREE_SPACE * 2));
        middleRect[6].setX(posCircle.x  + secondX+ x + i * (SPRITE_SIZE * 2) + 64 + SPRITE_SIZE + i * (FREE_SPACE * 2));
        middleRect[7].setX(posCircle.x+secondX+1 -8 + x + i * (SPRITE_SIZE * 2) + 64 + SPRITE_SIZE + i * (FREE_SPACE * 2));

        if(!isFive) {
            i =2;
            middleRect[8].setX(posCircle.x+firstX + x + i * (SPRITE_SIZE * 2) + 64 + SPRITE_SIZE + i * (FREE_SPACE * 2));
            middleRect[9].setX(posCircle.x+ x + i * (SPRITE_SIZE * 2) + 64 + SPRITE_SIZE + i * (FREE_SPACE * 2));
            middleRect[10].setX(posCircle.x + secondX + x + i * (SPRITE_SIZE * 2) + 64 + SPRITE_SIZE + i * (FREE_SPACE * 2));
            middleRect[11].setX(posCircle.x+secondX+1 -8 + x + i * (SPRITE_SIZE * 2) + 64 + SPRITE_SIZE + i * (FREE_SPACE * 2));
        }
    }
    private void moveTopBot(){
        int i=0;
        topRect[0].setX(posCircle.x+firstX  + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));
        topRect[1].setX(posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));
        topRect[2].setX(posCircle.x + secondX  + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));
        topRect[3].setX(posCircle.x +secondX+1 -8 + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));

        botRect[0].setX(posCircle.x+firstX  + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));
        botRect[1].setX(posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));
        botRect[2].setX(posCircle.x + secondX  + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));
        botRect[3].setX(posCircle.x +secondX+1 -8 + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));
        i=1;

        topRect[4].setX(posCircle.x+firstX  + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));
        topRect[5].setX(posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));
        topRect[6].setX(posCircle.x + secondX  + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));
        topRect[7].setX(posCircle.x +secondX+1 -8 + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));

        botRect[4].setX(posCircle.x+firstX  + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));
        botRect[5].setX(posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));
        botRect[6].setX(posCircle.x + secondX  + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));
        botRect[7].setX(posCircle.x +secondX+1 -8 + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));

        i=2;

        topRect[8].setX(posCircle.x+firstX  + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));
        topRect[9].setX(posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));
        topRect[10].setX(posCircle.x + secondX  + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));
        topRect[11].setX(posCircle.x +secondX+1 -8 + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));

        botRect[8].setX(posCircle.x+firstX  + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));
        botRect[9].setX(posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));
        botRect[10].setX(posCircle.x + secondX  + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));
        botRect[11].setX(posCircle.x +secondX+1 -8 + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));

        if(!isFive) {
            i=3;
            topRect[12].setX(posCircle.x+firstX  + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));
            topRect[13].setX(posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));
            topRect[14].setX(posCircle.x + secondX  + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));
            topRect[15].setX(posCircle.x +secondX+1 -8 + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));

            botRect[12].setX(posCircle.x+firstX  + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));
            botRect[13].setX(posCircle.x + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));
            botRect[14].setX(posCircle.x + secondX  + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));
            botRect[15].setX(posCircle.x +secondX+1 -8 + x + i*(SPRITE_SIZE*2) + i*(FREE_SPACE *2));
        }

    }

    private void create1(Rectangle rect, float x, int y){
            rect.set(posCircle.x + x +firstX,y, 90 - firstX*2 + 1,90);
    }
    private void create2(Rectangle rect, float x, int y){
        rect.set(posCircle.x + x ,y + firstX-1, 90,90 - firstX*2+1);
    }
    private void create3(Rectangle rect, float x, int y){
        rect.set(posCircle.x + x + secondX,y+secondX -px_size, 60,75);
    }
    private void create4(Rectangle rect, float x, int y){
        rect.set(posCircle.x + x +secondX+1 -8,y + secondX, 75,60);
    }

    public void drawCircles(SpriteBatch batch) {
        for(int i =0; i< circleTop.length; i++) {
            circleTop[i].draw(batch);
            circleBot[i].draw(batch);
        }
        for(int i = 0; i< circleMiddle.length; i++){
            circleMiddle[i].draw(batch);
        }
    }

    public void dispose() {
        circleAtlas.dispose();
        System.out.println("Circle dispose");
    }

    public Rectangle[] getTopRect() {
        return topRect;
    }
    public Rectangle[] getBotRect() {
        return botRect;
    }
    public Rectangle[] getMiddleRect() {
        return middleRect;
    }

    public Rectangle getStartZone() {
        return startZone;
    }
    public Rectangle getEndZone() {
        return endZone;
    }
}
