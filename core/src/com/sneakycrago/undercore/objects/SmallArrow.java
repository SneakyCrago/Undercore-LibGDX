package com.sneakycrago.undercore.objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.sneakycrago.undercore.Application;
import com.sneakycrago.undercore.utils.Globals;
import com.sneakycrago.undercore.utils.Score;

import java.util.Random;


/**
 * Created by Sneaky Crago on 27.03.2017.
 */

public class SmallArrow {

    private final int SPEED = -90*2; //-90*2;

    private Vector2 posBlock;
    private Vector2 velocity;

    int x;

    private Random random;

    private int randomArrowsAmount, randPos[], max =15 , amountOfArrows = 12;

    private int wave;

    private Rectangle upWave[], downWave[], randomWave[];

    public SmallArrow(float START, int WAVE) {
        x =(int) START;
        wave = WAVE;
        posBlock = new Vector2(0,11);
        velocity = new Vector2();

        random = new Random();

        // max  186

       if(wave == 1) {
            upWave = new Rectangle[amountOfArrows];
            for (int i = 0; i < amountOfArrows; i++) {
                upWave[i] = new Rectangle(posBlock.x + x + 24 * i, posBlock.y + 6 + 4 * i + 14 * i, 48, 4);
            }
       } else if(wave == 2){
           downWave = new Rectangle[amountOfArrows];
           for(int i=0; i< amountOfArrows; i++) {
                downWave[i] = new Rectangle(posBlock.x + x + 24*i, posBlock.y + 288 -6 - 4 * i - 14 * i, 48,4);
           }
       } else if(wave ==3) {
           randomArrowsAmount = random.nextInt(5) + 1;
           randPos = new int[randomArrowsAmount];
           randPos[0] = random.nextInt(max) + 1;
           for(int i=1; i < randPos.length; i++) {
               randPos[i] = random.nextInt(max) + 1;
               while (randPos[i] == randPos[i-1]) {
                   randPos[i] = random.nextInt(max) + 1;
               //System.out.println("Positions: " + randPos[i]);
               }
           }
           randomWave = new Rectangle[randomArrowsAmount];
            for(int i =0; i < randomWave.length; i++){
                randomWave[i] = new Rectangle(posBlock.x + x, posBlock.y +6 + randPos[i] *4+ randPos[i]*14,48,4);
            }
       }

    }

    // movement
    public void update(float delta) {
        if(Application.playerAlive) {
            velocity.add(0, 0);
            velocity.scl(delta);

            // x = SPEED * delta = движение по x
            posBlock.add(SPEED * delta, velocity.y);
            posBlock.add(SPEED * delta, velocity.y);

            velocity.scl(1 / delta);

            //move rects
            if (wave == 1) {
                for (int i = 0; i < amountOfArrows; i++) {
                    upWave[i].setPosition(posBlock.x + x + 24 * i, posBlock.y + 6 + 4 * i + 14 * i);
                }
            } else if (wave == 2) {
                for (int i = 0; i < amountOfArrows; i++) {
                    downWave[i].setPosition(posBlock.x + x + 24 * i, posBlock.y + 288 - 10 - 4 * i - 14 * i);
                }
            } else if (wave == 3) {
                for (int i = 0; i < randomWave.length; i++) {
                    randomWave[i].setX(posBlock.x + x);
                }
            }
        }
    }
    private boolean isScored = false;

    public void checkScore() {
        if(wave == 1) {
            if (upWave[amountOfArrows -1].getX() <= 96 + 16 && !isScored) {
                isScored = true;
                Score.addGameScore(1);
            }
        } else if(wave == 2){
            if (downWave[amountOfArrows -1].getX() <= 96 + 16 && !isScored) {
                isScored = true;
                Score.addGameScore(1);
            }
        } else if(wave == 3){
            if (randomWave[0].getX() <= 96 + 16 && !isScored) {
                isScored = true;
                Score.addGameScore(1);
            }
        }
    }

    public void drawArrow(ShapeRenderer shapeRenderer) {
        switch (Application.gameSkin) {
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
        if(wave ==1) {
            for (int i = 0; i < amountOfArrows; i++) {
                shapeRenderer.rect(upWave[i].getX(), upWave[i].getY(), upWave[i].getWidth(), upWave[i].getHeight());
            }
        } else if(wave ==2){
            for (int i = 0; i < amountOfArrows; i++) {
                shapeRenderer.rect(downWave[i].getX(), downWave[i].getY(), downWave[i].getWidth(), downWave[i].getHeight());
            }
        } else if(wave == 3){
            for (int i=0; i < randomWave.length; i++){
                shapeRenderer.rect(randomWave[i].getX(), randomWave[i].getY(), randomWave[i].getWidth(), randomWave[i].getHeight());
            }
        }
    }

    public Rectangle[] getDownWave() {
        return downWave;
    }

    public Rectangle[] getUpWave() {
        return upWave;
    }

    public Rectangle[] getRandomWave() {
        return randomWave;
    }
}
