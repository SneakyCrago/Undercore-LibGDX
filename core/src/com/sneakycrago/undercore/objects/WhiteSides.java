package com.sneakycrago.undercore.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by Sneaky Crago on 25.03.2017.
 */

public class WhiteSides {
    private float lineY = 3;

    // draw whiteSides at Screen
    public void drawWhiteSides(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(22/255f,238/255f,247/255f,1f);
        shapeRenderer.rect(0, 11 - lineY,512, lineY);
        shapeRenderer.rect(0, 310 - 11, 512, lineY);
    }
}
