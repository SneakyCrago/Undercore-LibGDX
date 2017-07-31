package com.sneakycrago.undercore.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sneakycrago.undercore.Application;

import java.util.Random;

/**
 * Created by Sneaky Crago on 20.07.2017.
 */

public class SurvivalBullets {

    //private float time;

    private Random random;

    private Vector2 position;
    private Rectangle bullet;

    private final int bulletSize = 32;
    private int SPEED = 45;
    private int randomYPos;
    private int sidePos; // 1 -left 2-right


    private float TVwidth = 203/4;
    private float TVheight =  132/4;
    private float degrees;

    public SurvivalBullets(Application game){
        random = new Random();

        position = new Vector2();

        randomizeBullet();


        bullet = new Rectangle();
        bullet.setSize(TVwidth,TVheight);

        if(sidePos == 1) {
            bullet.setPosition(-48, randomYPos);
            position.set(-48, randomYPos);
        } else if(sidePos == 2) {
            bullet.setPosition(512 +16, randomYPos);
            position.set(512 +16, randomYPos);
        }

    }


    public void draw(ShapeRenderer shapeRenderer){
            shapeRenderer.setColor(Color.FIREBRICK);
            shapeRenderer.rect(bullet.getX(),bullet.getY(), bullet.getWidth(), bullet.getHeight());
    }
    public void update(float delta){

        /* !!!old update!!!

        time += Gdx.graphics.getDeltaTime();

        if(time >= 7) {
        drawBullet = true;
        position.add(SPEED * delta, 0);
        bullet.setPosition(position.x, position.y);
        }
        if(bullet.getX() >= 512+16) {
            time = 0;
            drawBullet = false;


            randomizeBullet();

            position.set(-48, randomYPos);
            bullet.setPosition(-48, randomYPos);
        } */
        if(sidePos == 1) {
            position.add(SPEED * delta, 0);
        } else if(sidePos == 2){
            position.add(SPEED * delta * (-1), 0);
        }
        bullet.setPosition(position.x, position.y);

        degrees += 4.5f; // 6
        if(degrees >= 360){
            degrees = 0;
        }

    }

    public boolean delete(){
        if(bullet.getX() >= 512+16) {
            return true;
        } else
            return false;
    }

    public void randomizeBullet(){
        sidePos = random.nextInt(2) +1;

        degrees = random.nextInt(359)+1;

        randomYPos = random.nextInt(310- WhiteSides.lineSurvival*4 - bulletSize) + WhiteSides.lineSurvival*2;
        //System.out.println(randomYPos);
    }
}
