package com.sneakycrago.undercore.objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;


/**
 * Created by Sneaky Crago on 27.03.2017.
 */

public class SmallArrow {

    private final int SPEED = -90*2;

    private Vector2 posBlock;
    private Vector2 velocity;

    int x;

    private Random random;

    private int wave;

    private int[] massiveWave1;

    private int[] wavePos;

    private int[] massiveWave2;

    private int massiveHelper;

    public SmallArrow(float START) {
        x =(int) START;
        posBlock = new Vector2(0,11);
        velocity = new Vector2();

        random = new Random();

        wave = random.nextInt(2) + 1;
        System.out.println(wave);

        if (wave == 1){
            massiveWave1 = new int[random.nextInt(2)+3];

            wavePos = new int[random.nextInt(16) +1];

        } else {
            massiveHelper = random.nextInt(2) + 1;
            if(massiveHelper == 1) {
                massiveWave2 = new int[3];

                massiveWave2[0] = 1;
                massiveWave2[0] = 2;
                massiveWave2[0] = 1;
            } else{
                massiveWave2 = new int[3];

                massiveWave2[0] = 2;
                massiveWave2[0] = 1;
                massiveWave2[0] = 2;
            }
        }
    }

    // movement
    public void update(float delta) {
        //movement
        velocity.add(0, 0);
        velocity.scl(delta);

        // x = SPEED * delta = движение по x
        posBlock.add(SPEED * delta, velocity.y);
        posBlock.add(SPEED * delta, velocity.y);

        velocity.scl(1 / delta);
    }

    public void drawArrow(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(22/255f,238/255f,247/255f,1f);
        for(int i = 0;i < 16; i++)
        shapeRenderer.rect(x, 11+ 6 + 4* i+14*i,48, 4);

        if(wave == 1){
            for(int i = 0; i < massiveWave1.length; i++){

            }
        } else{

        }
    }
}
