package com.sneakycrago.undercore.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Sneaky Crago on 30.07.2017.
 */

public class ArrowEffect {

    Vector2 position;
    Vector2 velocity;
    float move = 0;
    float SPEED = 100;
    float startX, startY;

    float time;

    public ArrowEffect(Vector2 position,Vector2 velocity) {
        this.position = position;
        this.velocity = velocity;

        if(velocity.y == 0) {
            startX = position.x;
            startY = position.y;
        } else if(velocity.y <0) {
            startX = position.x; //4
            startY = position.y; //+16
        } else if(velocity.y >0) {
            startX = position.x; //4
            startY = position.y; //-16
        }
        startX = position.x+12;
        startY = position.y+16;
        lastY = position.y;

    }
    public void update(float delta){
        time += Gdx.graphics.getDeltaTime();

        move += delta*SPEED;

        lastX = startX-move;
    }

    private float lastY;
    private float lastX;
    public void draw(ShapeRenderer shapeRenderer){
        /*
        if(velocity.y ==0) {
            //shapeRenderer.circle(startX - move, startY + 16, 6);
            shapeRenderer.rect(startX - move, startY-6, 12,12);
        } else if(velocity.y >0) {
            shapeRenderer.circle(startX - move, startY, 6);
            //shapeRenderer.rect(startX - move, startY + 16 - move -6, 3,12);
        } else if(velocity.y <0) {
            shapeRenderer.circle(startX - move, startY , 6);
        } */
        /*
        if(velocity.y ==0) {
            shapeRenderer.rectLine(0,startY, startX, startY,12);
        } else{
            shapeRenderer.rectLine(0-move,startY, startX-move, startY,12);
        }

        if(velocity.y >0) {
            shapeRenderer.rectLine(lastX,lastY, startX, startY,12);
        }
        if(velocity.y <0){
            shapeRenderer.rectLine(lastX,lastY, startX, startY,12);
        }   */
        if(velocity.y == 0) {
            shapeRenderer.rectLine(startX-move, startY, startX+lenght-move, startY, 12);
        } else if(velocity.y >0){
            //shapeRenderer.circle(startX - move, startY, 6);
            shapeRenderer.rectLine(startX-move, startY-move, startX+lenght-move, startY+lenght-move, 12);
        } else if(velocity.y <0) {
            shapeRenderer.rectLine(startX-move, startY+lenght-move, startX+lenght-move, startY-move, 12);
        }
    }
    private float lenght = 4;

    public boolean delete(){
        if(time >= 2) {
            return true;
        } else
            return  false;
    }
}
