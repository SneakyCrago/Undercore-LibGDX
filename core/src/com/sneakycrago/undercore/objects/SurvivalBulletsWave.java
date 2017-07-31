package com.sneakycrago.undercore.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.sneakycrago.undercore.Application;

import java.util.Iterator;

/**
 * Created by Sneaky Crago on 22.07.2017.
 */

public class SurvivalBulletsWave {

    private Array<SurvivalBullets> bullets;

    private float time;
    Application game;

    public SurvivalBulletsWave(Application GAME){
        this.game = GAME;

        bullets = new Array<SurvivalBullets>();
        //spawnBullet();
    }
    public void draw(ShapeRenderer shapeRenderer){
        //bullets.add();
        for(SurvivalBullets survivalBullets: bullets) {
            survivalBullets.draw(shapeRenderer);
        }

    }
    public void update(float delta){
        time += Gdx.graphics.getDeltaTime();

        if(time >= 7) {
            spawnBullet();
            time = 0;
        }

        Iterator<SurvivalBullets> iter = bullets.iterator();
        while(iter.hasNext()){
            SurvivalBullets survivalBullets = iter.next();
            survivalBullets.update(delta);
            if(survivalBullets.delete()) iter.remove();
        }
    }

    private void spawnBullet(){
        SurvivalBullets survivalBullets = new SurvivalBullets(game);

        bullets.add(survivalBullets);
    }
}
