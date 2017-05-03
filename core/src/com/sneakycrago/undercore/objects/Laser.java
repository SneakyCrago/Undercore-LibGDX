package com.sneakycrago.undercore.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sneakycrago.undercore.utils.Globals;
import com.sneakycrago.undercore.utils.Score;

import java.util.Random;

/**
 * Created by Sneaky Crago on 01.04.2017.
 */

public class Laser {

    public final int SPEED = -90; // -90

    private final int FREE_SPACE = 64;

    private Vector2 velocity;
    private Vector2 posBlock;

    private Texture laserTexture;
    private TextureRegion flipLaserTexture;
    private Sprite[] laser, flipLaser;

    private int[] massive;

    private Random random;

    private int[] size;

    private Rectangle[] TopWall;
    private Rectangle[] DownWall;
    private Rectangle[] OrangeLaserRect;
    private Rectangle[] BlueLaserRect;

    private Rectangle endZone;
    private Rectangle startZone;

    public int BLOCK_ZONE;

    int x; // test Start
    public Laser(float START) {

        x =(int) START;

        posBlock = new Vector2(0, 11); // 0,11
        velocity = new Vector2();

        random = new Random();
        massive = new int[random.nextInt(3)+3];
        random();
        BLOCK_ZONE = (massive.length-1)*FREE_SPACE + massive.length * 32;
        size = new int[massive.length];
        for(int i = 0; i< massive.length; i++) {
            size[i] = 0;
        }
        TopWall = new Rectangle[massive.length];
        DownWall = new Rectangle[massive.length];
        OrangeLaserRect = new Rectangle[massive.length];
        BlueLaserRect = new Rectangle[massive.length];

        System.out.print("Lasers: ");
        for(int i =0; i < massive.length; i++) {
            System.out.print(+massive[i] + " ");
        }
        System.out.println();
        laserTexture = new Texture("textures/laser.png");
        flipLaserTexture = new TextureRegion(laserTexture,0,0,32,32);
        flipLaserTexture.flip(false, true);

        laser = new Sprite[massive.length];
        flipLaser = new Sprite[massive.length];

        for(int i = 0; i < massive.length; i++) {
            laser[i] = new Sprite(laserTexture);
            laser[i].setPosition(posBlock.x + x + i*FREE_SPACE +i*Globals.TEXTURE_SIZE, posBlock.y + 32);

            flipLaser[i] = new Sprite(flipLaserTexture);
            flipLaser[i].setPosition(posBlock.x + x + i*FREE_SPACE +i*Globals.TEXTURE_SIZE,
                    posBlock.y + Globals.FREE_SPACE * 2 + 32);
        }
        createRects();

        isScored = new boolean[massive.length];

        for(int i = 0; i < isScored.length; i++) {
            isScored[i] = false;
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

        for(int i = 0; i < massive.length; i++) {
            laser[i].setX(posBlock.x + x + i*FREE_SPACE +i*Globals.TEXTURE_SIZE);
            flipLaser[i].setX(posBlock.x + x + i*FREE_SPACE +i*Globals.TEXTURE_SIZE);
        }

        moveRects();

        checkScore();
    }

    public void random() {
        for(int i = 0; i < massive.length; i++){
            massive[i] = random.nextInt(2) + 1;
        }
    }

    //SCORE
    private boolean isScored[];
    public void checkScore() {
        for(int i = 0; i < TopWall.length; i++) {
            if(TopWall[i].getX() +16 <= 96+16 && !isScored[i]){
                isScored[i] = true;
                Score.addGameScore(1);
            }
        }
    }

    public void createRects() {
        startZone = new Rectangle(posBlock.x +x, posBlock.y, 1,288);
        endZone = new Rectangle(posBlock.x + x + BLOCK_ZONE,posBlock.y, 1, 288);
        for(int init = 0; init < massive.length; init++) {
            TopWall[init] = new Rectangle();
            TopWall[init].set(posBlock.x + x + init*FREE_SPACE +init*Globals.TEXTURE_SIZE,
                    posBlock.y + Globals.FREE_SPACE*2 + Globals.TEXTURE_SIZE, 32, 64);
            DownWall[init] = new Rectangle();
            DownWall[init].set(posBlock.x + x + init*FREE_SPACE +init*Globals.TEXTURE_SIZE,
                    posBlock.y, 32, 64);
            //Orange
            if(massive[init] == 1) {
                OrangeLaserRect[init] = new Rectangle();
                OrangeLaserRect[init].set(posBlock.x + x + init*FREE_SPACE +init*Globals.TEXTURE_SIZE +2,
                        posBlock.y + 64, 32 -4 , 160);

                BlueLaserRect[init] = new Rectangle();
            }
            //Blue
            if(massive[init] == 2) {
                OrangeLaserRect[init] = new Rectangle();

                BlueLaserRect[init] = new Rectangle();
                BlueLaserRect[init].set(posBlock.x + x + init*FREE_SPACE +init*Globals.TEXTURE_SIZE + 2,
                        posBlock.y + 64, 32 -4, 160);
            }
        }
    }
    public void moveRects(){
        endZone.setX(posBlock.x + x + BLOCK_ZONE);
        startZone.setX(posBlock.x + x);
        for(int init = 0; init < massive.length; init++) {
            TopWall[init].setX(posBlock.x + x + init*FREE_SPACE +init*Globals.TEXTURE_SIZE);
            DownWall[init].setX(posBlock.x + x + init*FREE_SPACE +init*Globals.TEXTURE_SIZE);
            if(massive[init] == 1) {
                OrangeLaserRect[init].setX(posBlock.x + x + init*FREE_SPACE +init*Globals.TEXTURE_SIZE +2);
            }
            if (massive[init] == 2) {
                BlueLaserRect[init].setX(posBlock.x + x + init*FREE_SPACE +init*Globals.TEXTURE_SIZE + 2);
            }
        }
    }

    public void drawLaserBlock(ShapeRenderer shapeRenderer, float delta){
        for(int init = 0; init < massive.length; init++){
            if(massive[init] == 1){
                drawLaserShell(shapeRenderer, x + init*FREE_SPACE +init*Globals.TEXTURE_SIZE);
            } else if(massive[init] ==2) {
                drawLaserShell(shapeRenderer,  x + init*FREE_SPACE +init*Globals.TEXTURE_SIZE);
            }
        }
        drawLaserLine(shapeRenderer, delta);
    }
    public void drawLaserGun(SpriteBatch batch){
        for(int init = 0; init < massive.length; init++){
            if(massive[init] == 1){
                laser[init].draw(batch);
                flipLaser[init].draw(batch);
            } else if(massive[init] == 2){
                laser[init].draw(batch);
                flipLaser[init].draw(batch);
            }
        }
    }

    // отрисовывает стену с пушкой
    public void drawLaserShell(ShapeRenderer shapeRenderer, int x) {
        //space
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(posBlock.x + x + 3, 310 -11, Globals.TEXTURE_SIZE-6, 3); //top
        shapeRenderer.rect(posBlock.x + x + 3, 11-3, Globals.TEXTURE_SIZE-6, 3); // bot

        shapeRenderer.setColor(Globals.SidesColor);
        //bot block
        shapeRenderer.rect(posBlock.x + x, posBlock.y, 3, Globals.TEXTURE_SIZE);  //left
        shapeRenderer.rect(posBlock.x + x + Globals.TEXTURE_SIZE - 3, posBlock.y, 3, Globals.TEXTURE_SIZE); // right
        //top
        shapeRenderer.rect(posBlock.x + x, posBlock.y + Globals.TEXTURE_SIZE * 8, 3, Globals.TEXTURE_SIZE );  //left
        shapeRenderer.rect(posBlock.x + x + Globals.TEXTURE_SIZE - 3, posBlock.y + Globals.TEXTURE_SIZE * 8,
                3, Globals.TEXTURE_SIZE ); // right

    }
    private int laserHeight = 160;
    private int laserSpeed = 540 + 540; //90*6=540
    private int laserFieldOfView = 128 + 32 * 5; // 128 + 32 * 4
    // DRAW LASER LINE отрисовывает сам лазер
    public void drawLaserLine(ShapeRenderer shapeRenderer, float delta) {
        for(int i = 0; i < massive.length; i++){
            if(massive[i] == 1 && size[i] <= laserHeight
                    && posBlock.x + x + i*FREE_SPACE +i*Globals.TEXTURE_SIZE <= laserFieldOfView) {
                shapeRenderer.setColor(Globals.OrangeColor);
                shapeRenderer.rect(2 + posBlock.x + x + i * FREE_SPACE + i * Globals.TEXTURE_SIZE,
                        posBlock.y + Globals.TEXTURE_SIZE * 2, Globals.TEXTURE_SIZE - 4, size[i]);
                size[i] += laserSpeed * delta;
                if(size[i] >= laserHeight) {
                    size[i] = laserHeight;
                }
            }
            if(massive[i] == 2 && size[i] >= -laserHeight
                    && posBlock.x + x + i*FREE_SPACE +i*Globals.TEXTURE_SIZE <= laserFieldOfView) {
                shapeRenderer.setColor(Globals.LightBlueColor);
                shapeRenderer.rect(2 + posBlock.x + x + i * FREE_SPACE + i * Globals.TEXTURE_SIZE,
                        posBlock.y + Globals.TEXTURE_SIZE * 4 + Globals.FREE_SPACE, Globals.TEXTURE_SIZE - 4, size[i]);
                size[i] -= laserSpeed * delta;
                if(size[i] <= -laserHeight) {
                    size[i] = -laserHeight;
                }
            }
        }
    }

    public void dispose() {
        laserTexture.dispose();
        System.out.println("Laser dispose");
    }

    public Rectangle[] getTopWall() {
        return TopWall;
    }
    public Rectangle[] getDownWall() {
        return DownWall;
    }
    public Rectangle[] getOrangeLaserRect() {
        return OrangeLaserRect;
    }
    public Rectangle[] getBlueLaserRect() {
        return BlueLaserRect;
    }

    public Rectangle getStartZone() {
        return startZone;
    }
    public Rectangle getEndZone() {
        return endZone;
    }
}
