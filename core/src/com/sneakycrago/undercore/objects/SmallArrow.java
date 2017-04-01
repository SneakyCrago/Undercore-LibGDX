package com.sneakycrago.undercore.objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;


/**
 * Created by Sneaky Crago on 27.03.2017.
 */

public class SmallArrow {

    private final int SPEED = -90*2;

    private Vector2 posBlock;
    private Vector2 velocity;

    public SmallArrow() {

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
    }

    public void drawArrow(ShapeRenderer shapeRenderer, int x) {
        shapeRenderer.setColor(22/255f,238/255f,247/255f,1f);
        for(int i = 0;i < 16; i++)
        shapeRenderer.rect(x, 11+ 4 + 4* i+8*i,48, 4 );
    }
}
