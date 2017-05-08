package com.sneakycrago.undercore.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sneakycrago.undercore.Application;
import com.sneakycrago.undercore.utils.Globals;

/**
 * Created by Sneaky Crago on 25.03.2017.
 */

public class WhiteSides {
    private float lineY = 3;

    // draw whiteSides at Screen
    public void drawWhiteSides(ShapeRenderer shapeRenderer) {
        if(Application.gameSkin ==0) {
            shapeRenderer.setColor(Globals.SidesColor);
        } else if(Application.gameSkin == 1) {
            shapeRenderer.setColor(Globals.Sides1Color);
        }
            shapeRenderer.rect(0, 11 - lineY, 512, lineY);
            shapeRenderer.rect(0, 310 - 11, 512, lineY);


        if(Application.gameSkin == 1){
            shapeRenderer.setColor(Globals.Inner1Color);
            shapeRenderer.rect(0,0, 512, 11-lineY);
            shapeRenderer.rect(0, 310 - 11 + lineY, 512, 11-lineY);
        }
    }
}
