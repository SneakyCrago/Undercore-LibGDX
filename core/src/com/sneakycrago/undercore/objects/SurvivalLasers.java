package com.sneakycrago.undercore.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sneakycrago.undercore.utils.Globals;

import java.util.Random;


/**
 * Created by Sneaky Crago on 20.07.2017.
 */

public class SurvivalLasers {

    private int line =20; // толщина линии
    private int question = 10; // ширина знака

    private float time; // время
    private float gapTime = 3; // время промежутка
    private float span = 0.5f; //промежуток мерцания

    private int laserSize; // отвечает за увеличение размера лазера
    private int laserSpeed = 90*12; // скорость движения линии

    private int waveCount;

    private boolean drawDangerousZone = false; // рисовать зону опасности
    private boolean startWave; //начать движение лазера

    private Random random;

    public SurvivalLasers(){
        random = new Random();

        randomizeWave();
    }

    public void draw(ShapeRenderer shapeRenderer){
        if(drawDangerousZone) {
            drawDangerousZone(shapeRenderer);
        }
        if(startWave) {
            drawlaser(shapeRenderer);
        }
    }


    // слева - 170 по центру -172 справа - 170
    private void drawDangerousZone(ShapeRenderer shapeRenderer){
            // 1 - снизу 2 -сверху 3 - лево, 4- центр, 5- справа
            switch (wavePosition) {
                case 1:
                    shapeRenderer.setColor(Color.RED);
                    shapeRenderer.rect(0, 0, 512, 155);

                    shapeRenderer.setColor(Color.BLACK);
                    shapeRenderer.rect(line, line, 512-line*2, 155-line*2);
                    //Question
                    shapeRenderer.setColor(Color.RED);
                    shapeRenderer.rect(256 -question/2, 155/2 - question*5/2, question*1.3f,question);
                    shapeRenderer.rect(256 -question/2, 155/2 - question*5/2 + question*2, question*1.3f,question*3);
                    break;
                case 2:
                    //Line
                    shapeRenderer.setColor(Color.RED);
                    shapeRenderer.rect(0, 155, 512, 155);

                    shapeRenderer.setColor(Color.BLACK);
                    shapeRenderer.rect(line, line+ 155, 512-line*2, 155-line*2);
                    //Question
                    shapeRenderer.setColor(Color.RED);
                    shapeRenderer.rect(256 -question/2,155+ 155/2 - question*5/2, question*1.3f,question);
                    shapeRenderer.rect(256 -question/2,155+ 155/2 - question*5/2 + question*2, question*1.3f,question*3);
                    break;
                case 3:
                    //Line
                    shapeRenderer.setColor(Color.RED);
                    shapeRenderer.rect(0, 0, 170, 310);

                    shapeRenderer.setColor(Color.BLACK);
                    shapeRenderer.rect(line, line, 170-line*2, 310-line*2);
                    //Question
                    shapeRenderer.setColor(Color.RED);
                    shapeRenderer.rect(170/2 -question/2,310/2 - question*5/2, question*1.3f,question);
                    shapeRenderer.rect(170/2 -question/2,310/2 - question*5/2 + question*2, question*1.3f,question*3);
                    break;
                case 4:
                    //Line
                    shapeRenderer.setColor(Color.RED);
                    shapeRenderer.rect(170, 0, 172, 310);

                    shapeRenderer.setColor(Color.BLACK);
                    shapeRenderer.rect(170+line, line, 172-line*2, 310-line*2);
                    //Question
                    shapeRenderer.setColor(Color.RED);
                    shapeRenderer.rect(170+170/2 -question/2,310/2 - question*5/2, question*1.3f,question);
                    shapeRenderer.rect(170+170/2 -question/2,310/2 - question*5/2 + question*2, question*1.3f,question*3);
                    break;
                case 5:
                    //Line
                    shapeRenderer.setColor(Color.RED);
                    shapeRenderer.rect(170+172, 0, 170, 310);

                    shapeRenderer.setColor(Color.BLACK);
                    shapeRenderer.rect(170+172+line, line, 170-line*2, 310-line*2);
                    //Question
                    shapeRenderer.setColor(Color.RED);
                    shapeRenderer.rect(170+172+170/2 -question/2,310/2 - question*5/2, question*1.3f,question);
                    shapeRenderer.rect(170+172+170/2 -question/2,310/2 - question*5/2 + question*2, question*1.3f,question*3);
                    break;
            }

    }
    private void drawlaser(ShapeRenderer shapeRenderer){
        switch (wavePosition) {
            case 1:
                shapeRenderer.setColor(Globals.SidesColor);
                shapeRenderer.rect(0, 0, laserSize, 155);

                shapeRenderer.setColor(Globals.RedLineColor);
                shapeRenderer.rect(laserSize - 64 - line, line, 64, 155 - line * 2);
                break;
            case 2:
                shapeRenderer.setColor(Globals.SidesColor);
                shapeRenderer.rect(0, 155, laserSize, 155);

                shapeRenderer.setColor(Globals.RedLineColor);
                shapeRenderer.rect(laserSize - 64 - line,155+ line, 64, 155 - line * 2);
                break;
            case 3:
                shapeRenderer.setColor(Globals.SidesColor);
                shapeRenderer.rect(0, 0, 170, laserSize);

                shapeRenderer.setColor(Globals.RedLineColor);
                shapeRenderer.rect(line,laserSize - 64 - line, 170 - line * 2, 64);
                break;
            case 4:
                shapeRenderer.setColor(Globals.SidesColor);
                shapeRenderer.rect(170, 0, 172, laserSize);

                shapeRenderer.setColor(Globals.RedLineColor);
                shapeRenderer.rect(170+line,laserSize - 64 - line, 172 - line * 2, 64);
                break;
            case 5:
                shapeRenderer.setColor(Globals.SidesColor);
                shapeRenderer.rect(170+172, 0, 170, laserSize);

                shapeRenderer.setColor(Globals.RedLineColor);
                shapeRenderer.rect(170+172+line,laserSize - 64 - line, 170 - line * 2, 64);
                break;
        }
    }

    // gapTime - промежуток паузы
    private boolean randomHelper; // единичная проверка

    public void update(float delta){
        time += Gdx.graphics.getDeltaTime();

        if(time > gapTime && time <gapTime + span*2) {
            drawDangerousZone = true;
        } else {
            drawDangerousZone = false;
        }

        if(time > gapTime + span*2 + 0.1f) { // + 1.5
            startWave = true;
            randomHelper = true;
        }
        if(startWave) {
            laserSize += laserSpeed * delta;
            // окончание волны
            if (laserSize >= 1024) { //endWave
                laserSize = 0;
                startWave = false;

                randomizeWave();
            }
        }
        if(randomHelper) {
            time = 0;

            waveCount += 1;
            if (waveCount % 3 == 0 && gapTime > 0.8) {
                gapTime -= 0.1f;
            }
            randomHelper = false;
        }

        //System.out.println(time);
    }

    public boolean startBullets(){
        if(gapTime <= 1.85)
            return true;
        else
            return false;
    }

    private int wavePosition; // 1 - снизу 2 -сверху 3 - лево, 4- центр, 5- справа

    private void randomizeWave(){
        wavePosition = random.nextInt(5) +1;
    }

}
