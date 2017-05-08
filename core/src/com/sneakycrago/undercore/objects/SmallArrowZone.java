package com.sneakycrago.undercore.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sneakycrago.undercore.Application;

import java.util.Random;

/**
 * Created by Sneaky Crago on 04.05.2017.
 */

public class SmallArrowZone {

    private int x;

    private SmallArrow smallArrow[];

    private int massive[],length[], randomHelper;
    private int upHelper[], downHelper[], randHelper[];
    public int BLOCK_SIZE;

    private int lenghtBig = 312, randomLenght = 48;
    private final int SPACE = 256;

    private Random random;

    private Rectangle startZone, endZone;

    private Vector2 posBlock;

    private final int SPEED = -90*2;

    public SmallArrowZone(int start) {
        x = start;

        posBlock = new Vector2(x, 11);

        random = new Random();

        createMassive();

        smallArrow = new SmallArrow[massive.length];

        if(randomHelper == 1) {
            if(massive[2] == 3) {
                BLOCK_SIZE = length[2]  + 48; //length[2] - SPACE *2 + 48
            } else {
                BLOCK_SIZE = length[2]  + lenghtBig; //length[2]- SPACE *2 + lenghtBig
            }
        } else {
            if(massive[4] == 3) {
                BLOCK_SIZE = length[4]  + 48; //length[4] - SPACE *2 + 48
            } else {
                BLOCK_SIZE = length[4]  + lenghtBig; //length[4] - SPACE *2 + lenghtBig
            }
        }

        for(int i =0; i < smallArrow.length; i++){
            if(massive[i] == 1){
                smallArrow[i] = new SmallArrow(x + length[i],1);  // up Wave
            } else if(massive[i] == 2){
                smallArrow[i] = new SmallArrow(x + length[i],2);  // down Wave
            } else if(massive[i] == 3){
                smallArrow[i] = new SmallArrow(x + length[i],3);  // random Wave
            }
        }


        startZone = new Rectangle(posBlock.x, posBlock.y, 1, 288);

        endZone = new Rectangle(posBlock.x + BLOCK_SIZE, posBlock.y, 1, 288);
    }

    private void createMassive(){
        randomHelper = random.nextInt(2) + 1;
        if(randomHelper == 1) {
            massive = new int[3];
        } else {
            massive = new int[5];
        }

        System.out.print("SMALL ARROW: ");
        massive[0] = random.nextInt(3) +1; // (3)+1
        System.out.print(massive[0] + " ");
        for(int i =1; i< massive.length; i++){
            massive[i] = random.nextInt(3) + 1;

            if(massive[i] == 1 || massive[i] == 2) {
                while (massive[i] == massive[i - 1]) {
                    massive[i] = random.nextInt(3) + 1;
                }
            }
            System.out.print(massive[i] + " ");
        }
        System.out.println();

        length = new int[massive.length];

        length[0] = 0;
        for(int i =1;  i < length.length; i++) {
            if(massive[i-1] == 1 || massive[i-1] == 2){
                length[i] = length[i-1] + lenghtBig + SPACE;
            } else if(massive[i-1] == 3) {
                length[i] = length[i-1] + randomLenght + SPACE;
            }
        }

        //helpers
        upHelper = new int[massive.length];
        downHelper = new int[massive.length];
        randHelper = new int[massive.length];
        for(int i = 0; i < massive.length; i++){
            if(massive[i] == 1){
                upHelper[i] =  1;
                downHelper[i] = 0;
                randHelper[i] = 0;
            } else if(massive[i] == 2) {
                upHelper[i] =  0;
                downHelper[i] = 1;
                randHelper[i] = 0;
            } else if(massive[i] == 3) {
                upHelper[i] =  0;
                downHelper[i] = 0;
                randHelper[i] = 1;
            }

        }
    }


    public void drawArrowZone(ShapeRenderer shapeRenderer){
        for(int i=0; i < smallArrow.length; i++) {
            smallArrow[i].drawArrow(shapeRenderer);
        }
    }
    public void update(float delta){
        for(int i=0; i < smallArrow.length; i++) {
            smallArrow[i].update(delta);
            smallArrow[i].checkScore();
        }

        posBlock.add(SPEED * delta, 0);
        posBlock.add(SPEED * delta, 0);

        startZone.setX(posBlock.x);
        endZone.setX(posBlock.x + BLOCK_SIZE);
    }

    public void collisionDebug(ShapeRenderer shapeRenderer){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(startZone.getX(),startZone.getY(), startZone.getWidth(), startZone.getHeight());
        shapeRenderer.rect(endZone.getX(),endZone.getY(), endZone.getWidth(), endZone.getHeight());
        shapeRenderer.end();
    }
    public void checkCollision(Rectangle player, Application game){

        for(int i =0; i < massive.length; i++) {
            if(downHelper[i] == 1) {
                for (int k = 0; k < smallArrow[i].getDownWave().length; k++) {
                    if (player.overlaps(smallArrow[i].getDownWave()[k])) {
                        game.setScreen(game.gameOver);
                        System.out.println("Collision: SMALL ARROW");
                    }
                }
            }
            if(upHelper[i] == 1) {
                for (int k = 0; k < smallArrow[i].getUpWave().length; k++) {
                    if (player.overlaps(smallArrow[i].getUpWave()[k])) {
                        game.setScreen(game.gameOver);
                        System.out.println("Collision: SMALL ARROW");
                    }
                }
            }
            if(randHelper[i] == 1) {
                for (int k = 0; k < smallArrow[i].getRandomWave().length; k++) {
                    if (player.overlaps(smallArrow[i].getRandomWave()[k])) {
                        game.setScreen(game.gameOver);
                        System.out.println("Collision: SMALL ARROW");
                    }
                }
            }
        }
    }

    public Rectangle getStartZone() {
        return startZone;
    }
    public Rectangle getEndZone() {
        return endZone;
    }
}


