package com.sneakycrago.undercore.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.sneakycrago.undercore.Application;

import java.util.Random;

/**
 * Created by Sneaky Crago on 29.04.2017.
 */

public class SniperZone {

    private Sniper sniper[], sniper2[];

    public int BLOCK_SIZE;

    private Random random;

    private int waveChance, amountOfWave, snipersCount, randomHeight, randomHeight2;

    private int secondAmount;

    private int SPACE =384;

    private Rectangle startZone, endZone;

    private Vector2 posBlock;

    private int x;

    //private Texture texture;

    private float SPEED;


    public SniperZone(float SPEED) {
        this.SPEED = SPEED;

        random = new Random();

        posBlock = new Vector2(0,11);


        startZone = new Rectangle();
        endZone = new Rectangle();

        //if(Application.gameSkin == 2) {
        //    texture = game.snipersSkin[1];
        //} else {
        //    texture =  game.snipersSkin[0];
        //}
        deathSound = false;
    }

    public void init(int START, Application game){
        x = START;

        posBlock.set(x,11);
        amountOfWave = 0;
        snipersCount =0;
        randomHeight = 0;
        randomHeight2 = 0;
        secondAmount =0;
        waveChance = random.nextInt(101);
        waveAmountCreate();

        sniper = new Sniper[amountOfWave];
        for (int i = 0; i < amountOfWave; i++) {
            snipersCount = random.nextInt(2) + 1;

            randomHeight = random.nextInt(7);
            if (snipersCount == 2) {
                randomHeight2 = random.nextInt(7);
                while (randomHeight == randomHeight2) {
                        randomHeight2 = random.nextInt(7);
                }
                secondAmount += 1;
            }
            if(game.normalMode) {
                if (Application.gameSkin == 2) {
                    sniper[i] = new Sniper(game,game.snipersSkin[1], SPEED);
                } else {
                    sniper[i] = new Sniper(game,game.snipersSkin[0], SPEED);
                }
            } else if(game.hardMode) {
                sniper[i] = new Sniper(game,game.sniperHard, SPEED);
            }
            sniper[i].init(x + SPACE * i, 8 + randomHeight * 32 + randomHeight * 8);

            BLOCK_SIZE = amountOfWave * 32 + amountOfWave * SPACE;
        }
        sniper2 = new Sniper[secondAmount];
        if(secondAmount != 0) {
            for (int i = 0; i < secondAmount; i++) {
                if(game.normalMode) {
                    if (Application.gameSkin == 2) {
                        sniper2[i] = new Sniper(game,game.snipersSkin[1], SPEED);
                    } else {
                        sniper2[i] = new Sniper(game,game.snipersSkin[0], SPEED);
                    }
                } else if(game.hardMode){
                    sniper2[i] = new Sniper(game,game.sniperHard, SPEED);
                }
                sniper2[i].init(x + SPACE * i, 8 + randomHeight2 * 32 + randomHeight2 * 8);
            }
        }

        deathSound = false;

        startZone.set(posBlock.x, posBlock.y, 1,288);
        endZone.set(posBlock.x + BLOCK_SIZE, posBlock.y,1,288);
    }

    public void secondChance(int START, Application game){
        x = START;

        posBlock.set(x,11);

        sniper = new Sniper[amountOfWave];
        for (int i = 0; i < amountOfWave; i++) {
            snipersCount = random.nextInt(2) + 1;

            randomHeight = random.nextInt(7);
            if (snipersCount == 2) {
                randomHeight2 = random.nextInt(7);
                while (randomHeight == randomHeight2) {
                    randomHeight2 = random.nextInt(7);
                }
                secondAmount += 1;
            }
            if(game.normalMode) {
                if (Application.gameSkin == 2) {
                    sniper[i] = new Sniper(game,game.snipersSkin[1], SPEED);
                } else {
                    sniper[i] = new Sniper(game,game.snipersSkin[0], SPEED);
                }
            } else if(game.hardMode) {
                sniper[i] = new Sniper(game,game.sniperHard, SPEED);
            }
            sniper[i].init(x + SPACE * i, 8 + randomHeight * 32 + randomHeight * 8);

            BLOCK_SIZE = amountOfWave * 32 + amountOfWave * SPACE;
        }
        sniper2 = new Sniper[secondAmount];
        if(secondAmount != 0) {
            for (int i = 0; i < secondAmount; i++) {
                if(game.normalMode) {
                    if (Application.gameSkin == 2) {
                        sniper2[i] = new Sniper(game,game.snipersSkin[1], SPEED);
                    } else {
                        sniper2[i] = new Sniper(game,game.snipersSkin[0], SPEED);
                    }
                } else if(game.hardMode) {
                    sniper2[i] = new Sniper(game,game.sniperHard, SPEED);
                }
                sniper2[i].init(x + SPACE * i, 8 + randomHeight2 * 32 + randomHeight2 * 8);
            }
        }

        deathSound = false;

        startZone.set(posBlock.x, posBlock.y, 1,288);
        endZone.set(posBlock.x + BLOCK_SIZE, posBlock.y,1,288);

    }

    public void update(float delta){
        for(int i =0;i < amountOfWave; i++) {
            sniper[i].update(delta);
            if(Application.playerAlive) {
                sniper[i].checkScore();
            }
        }
        if(secondAmount != 0) {
            for (int i = 0; i < secondAmount; i++) {
                sniper2[i].update(delta);
            }
        }
        posBlock.add(-90 * delta, 0);
        posBlock.add(-90 * delta, 0);

        startZone.setX(posBlock.x);
        endZone.setX(posBlock.x + BLOCK_SIZE);
    }
    public void drawSniperBlock(ShapeRenderer shapeRenderer, int playerY){
        for(int i =0;i < amountOfWave; i++) {
            sniper[i].drawSniperBlock(shapeRenderer, playerY);
            if(Application.playerAlive) {
                sniper[i].drawSniperLine(shapeRenderer, playerY);
            }
        }
        if(secondAmount != 0) {
            for (int i = 0; i < secondAmount; i++) {
                sniper2[i].drawSniperBlock(shapeRenderer, playerY);
            }
            if(Application.playerAlive) {
                for (int i = 0; i < secondAmount; i++) {
                    sniper2[i].drawSniperLine(shapeRenderer, playerY);
                }
            }
        }
    }
    public void drawBullet(SpriteBatch sb){
        for(int i =0;i < amountOfWave; i++) {
            sniper[i].drawBullet(sb);
        }
        if(secondAmount != 0) {
            for (int i = 0; i < secondAmount; i++) {
                sniper2[i].drawBullet(sb);
            }
        }
    }

    private void waveAmountCreate(){
        if(waveChance <= 3) {
            amountOfWave = 5;
        } else if(waveChance > 3 && waveChance <=10){
            amountOfWave = 4;
        } else if(waveChance > 10 && waveChance <= 50){
            amountOfWave = 3;
        } else if(waveChance > 50 && waveChance <= 80){
            amountOfWave = 2;
        } else {
            amountOfWave = 1;
        }
    }
    private boolean deathSound = false;

    public void collisionDebug(ShapeRenderer shapeRenderer){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLUE);
        for(int i=0; i < amountOfWave; i++) {
            shapeRenderer.rect(sniper[i].getStationRect().getX(),sniper[i].getStationRect().getY(),
                    sniper[i].getStationRect().getWidth(), sniper[i].getStationRect().getHeight());
            shapeRenderer.circle(sniper[i].getBullet().getX() + 16,sniper[i].getBullet().getY() + 16, 16);
        }
        if(secondAmount != 0){
            for(int i=0; i < secondAmount; i++) {
                shapeRenderer.rect(sniper2[i].getStationRect().getX(),sniper2[i].getStationRect().getY(),
                        sniper2[i].getStationRect().getWidth(), sniper2[i].getStationRect().getHeight());

            shapeRenderer.circle(sniper2[i].getBullet().getX() + 16,sniper2[i].getBullet().getY() + 16, 16);
            }
        }
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(startZone.getX(),startZone.getY(),startZone.getWidth(),startZone.getHeight());
        shapeRenderer.rect(endZone.getX(),endZone.getY(),endZone.getWidth(),endZone.getHeight());
        shapeRenderer.end();
    }
    public void checkCollision( Application game, Player pl) {

        for (int i = 0; i < amountOfWave; i++) {
            if (pl.getPlayerRectangle().overlaps(sniper[i].getStationRect()) && pl.alive) {
                pl.alive = false;
                Application.playerAlive = false;
                pl.deathAnimation();
                game.deathSnipers = true;
                if(!deathSound) {
                    game.deathAllSound.play(Application.volume);
                    deathSound = true;
                }
                //System.out.println("Collision: SNIPERS");
            }
            if(Intersector.overlaps(sniper[i].getCircle(), pl.getPlayerRectangle()) && pl.alive){
                pl.alive = false;
                Application.playerAlive = false;
                pl.deathAnimation();
                game.deathSnipers = true;
                //System.out.println("Collision: SNIPERS");
                if(!deathSound) {
                    game.deathAllSound.play(Application.volume);
                    deathSound = true;
                }
            }
        }

        if (secondAmount != 0) {
            for (int i = 0; i < secondAmount; i++) {
                if (pl.getPlayerRectangle().overlaps(sniper2[i].getStationRect()) && pl.alive) {
                    pl.alive = false;
                    Application.playerAlive = false;
                    pl.deathAnimation();
                    game.deathSnipers = true;
                    //System.out.println("Collision: SNIPERS");
                    if(!deathSound) {
                        game.deathAllSound.play(Application.volume);
                        deathSound = true;
                    }
                }
                if(Intersector.overlaps(sniper2[i].getCircle(), pl.getPlayerRectangle()) && pl.alive){
                    pl.alive = false;
                    Application.playerAlive = false;
                    pl.deathAnimation();
                    game.deathSnipers = true;
                    //System.out.println("Collision: SNIPERS");
                    if(!deathSound) {
                        game.deathAllSound.play(Application.volume);
                        deathSound = true;
                    }
                }
            }
        }
    }

    public void checkCollisionCircle(Application game, Player pl) {

        for (int i = 0; i < amountOfWave; i++) {
            //if (player.overlaps(sniper[i].getStationRect()) && pl.alive) {
            if(Intersector.overlaps(pl.getPlayerCircle(), sniper[i].getStationRect()) && pl.alive) {
                pl.alive = false;
                Application.playerAlive = false;
                pl.deathAnimation();
                game.deathSnipers = true;
                if(!deathSound) {
                    game.deathAllSound.play(Application.volume);
                    deathSound = true;
                }
                //System.out.println("Collision: SNIPERS");
            }
            if(Intersector.overlaps(sniper[i].getCircle(), pl.getPlayerCircle()) && pl.alive){
                pl.alive = false;
                Application.playerAlive = false;
                pl.deathAnimation();
                game.deathSnipers = true;
                //System.out.println("Collision: SNIPERS");
                if(!deathSound) {
                    game.deathAllSound.play(Application.volume);
                    deathSound = true;
                }
            }
        }

        if (secondAmount != 0) {
            for (int i = 0; i < secondAmount; i++) {
                //if (player.overlaps(sniper2[i].getStationRect()) && pl.alive) {
                if(Intersector.overlaps(pl.getPlayerCircle(),sniper2[i].getStationRect()) && pl.alive) {
                    pl.alive = false;
                    Application.playerAlive = false;
                    pl.deathAnimation();
                    game.deathSnipers = true;
                    //System.out.println("Collision: SNIPERS");
                    if(!deathSound) {
                        game.deathAllSound.play(Application.volume);
                        deathSound = true;
                    }
                }
                if(Intersector.overlaps(sniper2[i].getCircle(), pl.getPlayerCircle()) && pl.alive){
                    pl.alive = false;
                    Application.playerAlive = false;
                    pl.deathAnimation();
                    game.deathSnipers = true;
                    //System.out.println("Collision: SNIPERS");
                    if(!deathSound) {
                        game.deathAllSound.play(Application.volume);
                        deathSound = true;
                    }
                }
            }
        }
    }

    public void checkCollisonArrow(Application game, Player pl){
        for (int i = 0; i < amountOfWave; i++) {
            //if (player.overlaps(sniper[i].getStationRect()) && pl.alive) {
            if(Intersector.overlaps(pl.getCircleArrowDown(), sniper[i].getStationRect()) ||
                    Intersector.overlaps(pl.getCircleArrowUp(), sniper[i].getStationRect()) ||
                    Intersector.overlaps(pl.getCircleArrowSmall(), sniper[i].getStationRect())&& pl.alive) {
                pl.alive = false;
                Application.playerAlive = false;
                pl.deathAnimation();
                game.deathSnipers = true;
                if(!deathSound) {
                    game.deathAllSound.play(Application.volume);
                    deathSound = true;
                }
                //System.out.println("Collision: SNIPERS");
            }
            if(Intersector.overlaps(pl.getCircleArrowDown(),sniper[i].getCircle()) ||
                    Intersector.overlaps(pl.getCircleArrowUp(),sniper[i].getCircle()) ||
                    Intersector.overlaps(pl.getCircleArrowSmall(),sniper[i].getCircle()) && pl.alive){
                pl.alive = false;
                Application.playerAlive = false;
                pl.deathAnimation();
                game.deathSnipers = true;
                //System.out.println("Collision: SNIPERS");
                if(!deathSound) {
                    game.deathAllSound.play(Application.volume);
                    deathSound = true;
                }
            }
        }

        if (secondAmount != 0) {
            for (int i = 0; i < secondAmount; i++) {
                //if (player.overlaps(sniper2[i].getStationRect()) && pl.alive) {
                if(Intersector.overlaps(pl.getCircleArrowDown(),sniper2[i].getStationRect()) ||
                        Intersector.overlaps(pl.getCircleArrowSmall(),sniper2[i].getStationRect()) ||
                        Intersector.overlaps(pl.getCircleArrowUp(),sniper2[i].getStationRect())&& pl.alive) {
                    pl.alive = false;
                    Application.playerAlive = false;
                    pl.deathAnimation();
                    game.deathSnipers = true;
                    //System.out.println("Collision: SNIPERS");
                    if(!deathSound) {
                        game.deathAllSound.play(Application.volume);
                        deathSound = true;
                    }
                }
                if(Intersector.overlaps(pl.getCircleArrowDown(),sniper2[i].getCircle()) ||
                        Intersector.overlaps(pl.getCircleArrowUp(),sniper2[i].getCircle()) ||
                        Intersector.overlaps(pl.getCircleArrowSmall(),sniper2[i].getCircle())&& pl.alive){
                    pl.alive = false;
                    Application.playerAlive = false;
                    pl.deathAnimation();
                    game.deathSnipers = true;
                    //System.out.println("Collision: SNIPERS");
                    if(!deathSound) {
                        game.deathAllSound.play(Application.volume);
                        deathSound = true;
                    }
                }
            }
        }
    }


    public Rectangle getStartZone() {
        return startZone;
    }

    public Rectangle getEndZone() {
        return endZone;
    }
}
