package com.sneakycrago.undercore.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sneakycrago.undercore.screens.GameScreen;
import com.sneakycrago.undercore.utils.Score;

import java.awt.Desktop;
import java.util.Random;

/**
 * Created by Sneaky Crago on 25.03.2017.
 */

public class Wall {

    public int SPEED = -90; //-90

    private final int TEXTURE_SIZE = 32;
    private final int FREE_SPACE = 128;
    public final int BLOCK_SIZE = TEXTURE_SIZE*8 + FREE_SPACE *7;

    private int massive[];

    private Random random;

    private Vector2 posBlock;
    private Vector2 velocity;

    private Rectangle[] massiveRect;
    private Rectangle[] massiveRect2;

    private Rectangle endZone;

    public boolean[] isScored;

    public Wall(float START){

        isScored = new boolean[8];

        for(int i = 0; i < isScored.length; i++) {
            //isScored[i] = false;
        }

        random = new Random();
        rand();

        System.out.print("Wall: ");
        for(int i = 0; i < 7; i++) {
            System.out.print(massive[i] + " ");
        }
        System.out.println(massive[7]);

        posBlock = new Vector2(0, 11);
        velocity = new Vector2();

        massiveRect = new Rectangle[8];
        massiveRect2 = new Rectangle[8];

        createRects(START);
    }


    // movement
    public void update(float delta, float START) {
        //movement
        velocity.add(0, 0);
        velocity.scl(delta);

        // x = SPEED * delta = движение по x
        posBlock.add(SPEED * delta, velocity.y);
        posBlock.add(SPEED * delta, velocity.y);

        velocity.scl(1 / delta);

        //update positions
        for(int i = 0; i < massiveRect.length; i++) {
            massiveRect[i].setPosition(posBlock.x,posBlock.y);
        }

        moveRects(START);

        //SCORE
        checkScore();
    }

    //create massive for position
    private int[] rand() {
        massive = new int[8];
        massive[0] = random.nextInt(5) + 1; // (5)
        for (int i = 1; i < massive.length; i++) {
            massive[i] = random.nextInt(5) + 1;
            while (massive[i] == massive[i-1]) {
                massive[i] = random.nextInt(5) + 1;
            }
            //исключает ситуацию 15
            if(massive[i-1] == 1) {
                    massive[i] = random.nextInt(3) + 2;
                }
        }
        return massive;
    }
    //add SCORE
    public void checkScore() {
        for (int i = 0; i < massiveRect.length; i++){
            if(massiveRect[i].getX() <= 96+16 && !isScored[i]) {
                isScored[i] = true;
                Score.addGameScore(1);
            }
        }
    }

    //initialization
    public void createRects(float START) {
        int test = 0;
        endZone = new Rectangle();
        endZone.set(posBlock.x + START, posBlock.y, 2, 288);
        for (int i = 0; i < massiveRect.length; i++){
            if(massive[i] == 1) {
                massiveRect[i] = new Rectangle();
                massiveRect[i].set(posBlock.x +START + TEXTURE_SIZE * i + i * FREE_SPACE,posBlock.y + TEXTURE_SIZE*3,
                        TEXTURE_SIZE, TEXTURE_SIZE*6);
                massiveRect2[i] = new Rectangle();
            } else if(massive[i] == 2){
                massiveRect[i] = new Rectangle();
                massiveRect[i].set(posBlock.x +START + TEXTURE_SIZE * i + i * FREE_SPACE,posBlock.y,
                        TEXTURE_SIZE, TEXTURE_SIZE + 16);
                massiveRect2[i] = new Rectangle();
                massiveRect2[i].set(posBlock.x +START + TEXTURE_SIZE * i + i * FREE_SPACE,posBlock.y + 48 +TEXTURE_SIZE*3,
                        TEXTURE_SIZE, TEXTURE_SIZE * 4 + 16);
            }else if(massive[i] == 3){
                massiveRect[i] = new Rectangle();
                massiveRect[i].set(posBlock.x +START + TEXTURE_SIZE * i + i * FREE_SPACE,posBlock.y,
                        TEXTURE_SIZE, TEXTURE_SIZE *3);
                massiveRect2[i] = new Rectangle();
                massiveRect2[i].set(posBlock.x +START + TEXTURE_SIZE * i + i * FREE_SPACE, posBlock.y + TEXTURE_SIZE*6,
                        TEXTURE_SIZE, TEXTURE_SIZE * 3);
            }else if(massive[i] == 4){
                massiveRect[i] = new Rectangle();
                massiveRect[i].set(posBlock.x +START + TEXTURE_SIZE * i + i * FREE_SPACE, posBlock.y,
                        TEXTURE_SIZE, TEXTURE_SIZE*4 + 16);
                massiveRect2[i] = new Rectangle();
                massiveRect2[i].set(posBlock.x +START + TEXTURE_SIZE * i + i * FREE_SPACE,posBlock.y + TEXTURE_SIZE*7+16,
                        TEXTURE_SIZE, TEXTURE_SIZE + 16);
            }else if(massive[i] == 5){
                massiveRect[i] = new Rectangle();
                massiveRect[i].set(posBlock.x +START + TEXTURE_SIZE * i + i * FREE_SPACE,posBlock.y,
                        TEXTURE_SIZE, TEXTURE_SIZE*6);
                massiveRect2[i] = new Rectangle();
            }
        }
    }

    // move every frame
    public void moveRects(float START){
        endZone.setX(posBlock.x +START);
        for (int i = 0; i < massiveRect.length; i++){
            if(massive[i] == 1) {
                massiveRect[i].setPosition(posBlock.x +START + TEXTURE_SIZE * i + i * FREE_SPACE,posBlock.y + TEXTURE_SIZE*3);
                massiveRect2[i].setX(posBlock.x +START + TEXTURE_SIZE * i + i * FREE_SPACE);

            } else if(massive[i] == 2){
                massiveRect[i].setX(posBlock.x +START + TEXTURE_SIZE * i + i * FREE_SPACE);
                massiveRect2[i].setX(posBlock.x +START + TEXTURE_SIZE * i + i * FREE_SPACE);

            }else if(massive[i] == 3){
                massiveRect[i].setX(posBlock.x +START + TEXTURE_SIZE * i + i * FREE_SPACE);
                massiveRect2[i].setX(posBlock.x +START + TEXTURE_SIZE * i + i * FREE_SPACE);

            }else if(massive[i] == 4){
                massiveRect[i].setX(posBlock.x +START + TEXTURE_SIZE * i + i * FREE_SPACE);
                massiveRect2[i].setX(posBlock.x +START + TEXTURE_SIZE * i + i * FREE_SPACE);

            }else if(massive[i] == 5){
                massiveRect[i].setX(posBlock.x +START + TEXTURE_SIZE * i + i * FREE_SPACE);
                massiveRect2[i].setX(posBlock.x +START + TEXTURE_SIZE * i + i * FREE_SPACE);

            }
        }
    }

    // block drawing random generator
    public void drawWallBlock(ShapeRenderer shapeRenderer, float START) {
        for(int init = 0; init < massive.length; init++) {
            if (massive[init] == 1) {
                drawWallPos1(shapeRenderer,START + TEXTURE_SIZE * init + init * FREE_SPACE);
            } else if (massive[init] == 2) {
                drawWallPos2(shapeRenderer,START + TEXTURE_SIZE * init + init * FREE_SPACE);
            } else if (massive[init] == 3) {
                drawWallPos3(shapeRenderer,START + TEXTURE_SIZE * init + init * FREE_SPACE);
            } else if (massive[init] == 4) {
                drawWallPos4(shapeRenderer,START + TEXTURE_SIZE * init + init * FREE_SPACE);
            } else if (massive[init] == 5) {
                drawWallPos5(shapeRenderer,START + TEXTURE_SIZE * init + init * FREE_SPACE);

            }

        }

    }
    // positions drawing constructor
    public void drawWallPos1(ShapeRenderer shapeRenderer, float x) {
        //space
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(posBlock.x + x + 3, 310 -11, TEXTURE_SIZE-6, 3);
        //block
        shapeRenderer.setColor(22/255f,238/255f,247/255f,1f);
        shapeRenderer.rect(posBlock.x + x, posBlock.y + TEXTURE_SIZE *3, 3, TEXTURE_SIZE * 6);  //left
        shapeRenderer.rect(posBlock.x + x + TEXTURE_SIZE - 3, posBlock.y + TEXTURE_SIZE *3, 3, TEXTURE_SIZE * 6); // right

        shapeRenderer.rect(posBlock.x + x + 3, posBlock.y + TEXTURE_SIZE *3, TEXTURE_SIZE-6, 3);
    }
    public void drawWallPos2(ShapeRenderer shapeRenderer, float x) {
        //space
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(posBlock.x + x + 3, 310 -11, TEXTURE_SIZE-6, 3); //top
        shapeRenderer.rect(posBlock.x + x + 3, 11-3, TEXTURE_SIZE-6, 3); // bot

        shapeRenderer.setColor(22/255f,238/255f,247/255f,1f);
        //bot block
        shapeRenderer.rect(posBlock.x + x, posBlock.y, 3, 48);  //left
        shapeRenderer.rect(posBlock.x + x + TEXTURE_SIZE - 3, posBlock.y, 3, 48); // right

        shapeRenderer.rect(posBlock.x + x + 3, posBlock.y + 48 - 3, TEXTURE_SIZE-6, 3);

        //top block
        shapeRenderer.rect(posBlock.x + x, posBlock.y + TEXTURE_SIZE *4.5f, 3, TEXTURE_SIZE * 4.5f);  //left
        shapeRenderer.rect(posBlock.x + x + TEXTURE_SIZE - 3, posBlock.y + TEXTURE_SIZE *4.5f, 3, TEXTURE_SIZE * 4.5f); // right

        shapeRenderer.rect(posBlock.x + x + 3, posBlock.y + TEXTURE_SIZE *4.5f, TEXTURE_SIZE-6, 3);
    }
    public void drawWallPos3(ShapeRenderer shapeRenderer, float x) {
        //space
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(posBlock.x + x + 3, 310 -11, TEXTURE_SIZE-6, 3); //top
        shapeRenderer.rect(posBlock.x + x + 3, 11-3, TEXTURE_SIZE-6, 3); // bot

        shapeRenderer.setColor(22/255f,238/255f,247/255f,1f);
        //bot block
        shapeRenderer.rect(posBlock.x + x, posBlock.y, 3, TEXTURE_SIZE * 3);  //left
        shapeRenderer.rect(posBlock.x + x + TEXTURE_SIZE - 3, posBlock.y, 3, TEXTURE_SIZE * 3); // right

        shapeRenderer.rect(posBlock.x + x + 3, posBlock.y + TEXTURE_SIZE * 3 -3, TEXTURE_SIZE-6, 3);

        //top
        shapeRenderer.rect(posBlock.x + x, posBlock.y + TEXTURE_SIZE * 6, 3, TEXTURE_SIZE * 3);  //left
        shapeRenderer.rect(posBlock.x + x + TEXTURE_SIZE - 3, posBlock.y + TEXTURE_SIZE * 6, 3, TEXTURE_SIZE * 3); // right

        shapeRenderer.rect(posBlock.x + x + 3, posBlock.y + TEXTURE_SIZE * 6, TEXTURE_SIZE-6, 3);
    }
    public void drawWallPos4(ShapeRenderer shapeRenderer, float x) {
        //space
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(posBlock.x + x + 3, 310 -11, TEXTURE_SIZE-6, 3); //top
        shapeRenderer.rect(posBlock.x + x + 3, 11-3, TEXTURE_SIZE-6, 3); // bot

        shapeRenderer.setColor(22/255f,238/255f,247/255f,1f);
        //bot block
        shapeRenderer.rect(posBlock.x + x, posBlock.y, 3, TEXTURE_SIZE * 3 + 48);  //left
        shapeRenderer.rect(posBlock.x + x + TEXTURE_SIZE - 3, posBlock.y, 3, TEXTURE_SIZE * 3 + 48); // right

        shapeRenderer.rect(posBlock.x + x + 3, posBlock.y + TEXTURE_SIZE * 3 -3 + 48, TEXTURE_SIZE-6, 3);

        //top
        shapeRenderer.rect(posBlock.x + x, posBlock.y + TEXTURE_SIZE * 7 + 16, 3, 48);  //left
        shapeRenderer.rect(posBlock.x + x + TEXTURE_SIZE - 3, posBlock.y + TEXTURE_SIZE * 7 + 16, 3, 48); // right

        shapeRenderer.rect(posBlock.x + x + 3, posBlock.y + TEXTURE_SIZE * 7 + 16, TEXTURE_SIZE-6, 3);
    }
    public void drawWallPos5(ShapeRenderer shapeRenderer, float x) {
        //space
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(posBlock.x + x + 3, 11-3, TEXTURE_SIZE-6, 3); // bot

        shapeRenderer.setColor(22/255f,238/255f,247/255f,1f);

        shapeRenderer.rect(posBlock.x + x, posBlock.y, 3, TEXTURE_SIZE * 6);  //left
        shapeRenderer.rect(posBlock.x + x + TEXTURE_SIZE - 3, posBlock.y, 3, TEXTURE_SIZE * 6); // right

        shapeRenderer.rect(posBlock.x + x + 3, posBlock.y + TEXTURE_SIZE * 6 -3, TEXTURE_SIZE-6, 3);
    }

    public Rectangle[] getMassiveRect() {
        return massiveRect;
    }
    public Rectangle[] getMassiveRect2() {
        return massiveRect2;
    }

    public Rectangle getZoneRect() {
        return endZone;
    }

    public int getBLOCK_SIZE() {
        return BLOCK_SIZE;
    }
}
