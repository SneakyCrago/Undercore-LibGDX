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

    private  Application game;

    public WhiteSides(Application game){
        this.game = game;
    }

    // draw whiteSides at Screen
    public void drawWhiteSides(ShapeRenderer shapeRenderer) {

        if(game.normalMode) {
            switch (Application.gameSkin) {
                case 0:
                    shapeRenderer.setColor(Globals.SidesColor);
                    break;
                case 1:
                    shapeRenderer.setColor(Globals.Sides1Color);
                    break;
                case 2:
                    shapeRenderer.setColor(Globals.Sides2Color);
                    break;
                case 3:
                    shapeRenderer.setColor(Globals.Sides3Color);
                    break;
                case 4:
                    shapeRenderer.setColor(Globals.Sides4Color);
                    break;
            }
        } else if(game.hardMode) {
            shapeRenderer.setColor(Color.WHITE);
        }
            shapeRenderer.rect(0, 11 - lineY, 512, lineY);
            shapeRenderer.rect(0, 310 - 11, 512, lineY);

        if(game.normalMode) {
            switch (Application.gameSkin) {
                case 0:
                    shapeRenderer.setColor(Color.BLACK);
                    break;
                case 1:
                    shapeRenderer.setColor(Globals.Inner1Color);
                    break;
                case 2:
                    shapeRenderer.setColor(Globals.Inner2Color);
                    break;
                case 3:
                    shapeRenderer.setColor(Globals.Inner3Color);
                    break;
                case 4:
                    shapeRenderer.setColor(Globals.Inner4Color);
                    break;
            }
        } else if(game.hardMode) {
            shapeRenderer.setColor(Color.BLACK);
        }

        shapeRenderer.rect(0,0, 512, 11-lineY);
        shapeRenderer.rect(0, 310 - 11 + lineY, 512, 11-lineY);
    }

    public static int lineSurvival = 3;
    public void drawSurvival(ShapeRenderer shapeRenderer){
        shapeRenderer.setColor(Globals.SidesColor);

        shapeRenderer.rect(0,0,lineSurvival, 310);
        shapeRenderer.rect(0,0, 512, lineSurvival);
        shapeRenderer.rect(512-lineSurvival,0, lineSurvival, 310);
        shapeRenderer.rect(0,310-lineSurvival, 512, lineSurvival);
    }

}
