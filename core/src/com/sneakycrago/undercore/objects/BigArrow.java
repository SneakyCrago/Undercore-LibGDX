package com.sneakycrago.undercore.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Random;

/**
 * Created by Sneaky Crago on 27.03.2017.
 */

public class BigArrow {

    //BIG ARROW LOGIC
    public final int INVISIBLE = 512+128*2;
    public  boolean playEffect = true, playEffect2 = true;
    public boolean secondStart = false;
    public Sprite arrow, arrow2;


    private int SPEED = 90 * 6; // 90*6 x6 now
    private int FREE_SPACE = 96;

    private Texture arrowTexture;

    private Vector2 posArrow, posArrow2;
    private Vector2 velocity, velocity2;

    private Rectangle line, line2;
    private Polygon arrowPolygon, arrowPolygon2;

    private Random random = new Random();
    private int randomHeight, randomHeight2;
    private int randomValue, randomValue2;

    // Effect variables
    private float alpha = 1;
    private float start =0.1f * alpha;
    private float alphaMax = 0;
    private float startMax =0.1f * alpha;

    private float alpha2 = 1;
    private float start2 =0.1f * alpha;
    private float alphaMax2 = 0;
    private float startMax2 =0.1f * alpha;

    private float timer = TimeUtils.nanoTime();
    private float time = 0;

    private float timer2 = TimeUtils.nanoTime();
    private float time2 = 0;

    public BigArrow() {
        posArrow = new Vector2(-256,11);
        velocity = new Vector2();

        posArrow2 = new Vector2(0,11);
        velocity2 = new Vector2();

        random();
        secondArrowRandom();

        arrowTexture = new Texture("textures/big_arrow.png");

        arrow = new Sprite(arrowTexture);
        arrow.setAlpha(0);
        arrow.setSize(102, 96 );
        arrow.setPosition(512 - 128, randomHeight);

        arrow2 = new Sprite(arrowTexture);
        arrow2.setAlpha(0);
        arrow2.setSize(102, 96);
        arrow2.setPosition(512 - 128, randomHeight2);

        line = new Rectangle();
        line.set(0,arrow.getY(),posArrow.x,96);

        line2 = new Rectangle();
        line2.set(0,arrow2.getY(),posArrow2.x,96);

        arrowPolygon = new Polygon(new float[] {
                0,0,
                102/2 +3,0,
                102, 96/2,
                102/2 + 3,96,
                0, 96,
                102/2 -2, 96/2
        });

        arrowPolygon2 = new Polygon(new float[] {
                0,0,
                102/2 +3,0,
                102, 96/2,
                102/2 + 3,96,
                0, 96,
                102/2 -2, 96/2
        });

        arrowPolygon.setPosition(arrow.getX(),arrow.getY());
        arrowPolygon2.setPosition(arrow.getX(),arrow.getY());
        //line.set(-512,arrow.getY(), 512, 96);
    }

    public void random() {
        //simple

        //randomHeight = (int) posArrow.y + FREE_SPACE;

        //hard

        randomValue = random.nextInt(3) + 1;
        if(randomValue == 1) {
            randomHeight = (int) posArrow.y;
        } else if(randomValue == 2) {
            randomHeight = (int) posArrow.y + FREE_SPACE;
        } else if (randomValue ==3) {
            randomHeight =(int) posArrow.y + FREE_SPACE*2 ;
        }
    }
    public void secondArrowRandom() {
        //simple
        /*
        randomValue2 = random.nextInt(2) + 1;
        if(randomValue2 == 1) {
            randomHeight2 = (int) posArrow2.y;
        } else if (randomValue2 ==2) {
            randomHeight2 =(int) posArrow2.y + FREE_SPACE*2 ;
        } */
        //hard
        //check 1 random
        if(randomValue == 1) {
            randomValue2 = random.nextInt(3) + 1;
            while(randomValue2 == 1) {
                randomValue2 = random.nextInt(3) + 1;
            }
        } else if(randomValue == 2){
            randomValue2 = random.nextInt(3) + 1;
            while(randomValue2 == 2) {
                randomValue2 = random.nextInt(3) + 1;
            }
        } else if (randomValue == 3) {
            randomValue2 = random.nextInt(2)+ 1;
        }

        if(randomValue2 == 1) {
            randomHeight2 = (int) posArrow2.y;
        } else if(randomValue2 == 2) {
            randomHeight2 = (int) posArrow2.y + FREE_SPACE;
        } else if(randomValue2 == 3) {
            randomHeight2 =(int) posArrow2.y + FREE_SPACE*2 ;
        }
        //create randomHeight2
    }

    public void update(float delta) {
        //movement
        velocity.add(0, 0);
        velocity.scl(delta);

        // x = SPEED * delta = движение по x
        posArrow.add(SPEED * delta, velocity.y);
        posArrow.add(SPEED * delta, velocity.y);

        velocity.scl(1 / delta);


        moveArrow();

        line.set(0,arrow.getY(),arrow.getX() + 48,96);
        if (arrow.getX() > INVISIBLE) {
            line.set(512,0,0,0);
        }
        //line.setX(arrow.getX() + 48-512);
        arrowPolygon.setPosition(arrow.getX(),arrow.getY());
    }
    public void update2(float delta) {
        //movement
        velocity2.add(0, 0);
        velocity2.scl(delta);

        // x = SPEED * delta = движение по x
        posArrow2.add(SPEED * delta, velocity2.y);
        posArrow2.add(SPEED * delta, velocity2.y);

        velocity2.scl(1 / delta);

        moveArrow2();

        line2.set(0,arrow2.getY(),arrow2.getX() + 48,96);
        if (arrow2.getX() > INVISIBLE) {
            line2.set(512,0,0,0);
        }

        arrowPolygon2.setPosition(arrow2.getX(),arrow2.getY());
        //line.setX(arrow.getX() + 48-512);
    }

    private void moveArrow() {
        arrow.setAlpha(1);
        arrow.setPosition(posArrow.x -128*2, randomHeight);
    }
    private void moveArrow2() {
        arrow2.setAlpha(1);
        arrow2.setPosition(posArrow2.x - 128*2, randomHeight2);
    }

    public void drawArrows(SpriteBatch batch) {
        arrow.draw(batch);
        arrow2.draw(batch);
    }
    public void drawArrowLine(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(163 / 255f, 248 / 255f, 251 / 255f, 1f);
        if(arrow.getX() <= INVISIBLE) {
            //shapeRenderer.rect(0, arrow.getY(), arrow.getX() + 48, 96);
            shapeRenderer.rect(line.x,line.y,line.width,line.height);
        }
        if (arrow.getX() > INVISIBLE) {
            line.set(512,0,0,0);
        }

    }
    public void drawArrow2Line(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(163 / 255f, 248 / 255f, 251 / 255f, 1f);
        if(arrow2.getX() <= INVISIBLE) {
            //shapeRenderer.rect(0, arrow.getY(), arrow.getX() + 48, 96);
            shapeRenderer.rect(line2.x,line2.y,line2.width,line2.height);
        }
        if (arrow2.getX() > INVISIBLE) {
            line2.set(512,0,0,0);
        }
    }

    public void arrowEffect(float delta) {
        if(playEffect) {
            if ((alphaMax + startMax) < 1) {
                time += (TimeUtils.nanoTime() - timer);
                timer = TimeUtils.nanoTime();

                alphaMax += startMax;
                startMax = delta / 0.55f;
                arrow.setAlpha(alphaMax);
                //System.out.println(time / 1000000000);
                //System.out.println(alpha);
            } else if ((alpha - start) > 0) {
                secondStart = true;
                time += (TimeUtils.nanoTime() - timer);
                timer = TimeUtils.nanoTime();

                alpha -= start;
                start = delta / 0.55f;
                arrow.setAlpha(alpha);
                //System.out.println(time / 1000000000);
                //System.out.println(alpha);
            } else if((time / 1000000000) >= 0.995) {
                playEffect = false;
                //System.out.println(playEffect);
            }
        }

    }
    public void arrowEffect2(float delta) {
        if(playEffect2) {
            if ((alphaMax2 + startMax2) < 1) {
                time2 += (TimeUtils.nanoTime() - timer2);
                timer2 = TimeUtils.nanoTime();

                alphaMax2 += startMax2;
                startMax2 = delta / 0.55f;
                arrow2.setAlpha(alphaMax2);
                //System.out.println(time2 / 1000000000);
                //System.out.println(alpha2);
            } else if ((alpha2 - start2) > 0) {
                time2 += (TimeUtils.nanoTime() - timer2);
                timer2 = TimeUtils.nanoTime();

                alpha2 -= start2;
                start2 = delta / 0.55f;
                arrow2.setAlpha(alpha2);
                //System.out.println(time2 / 1000000000);
                //System.out.println(alpha2);
            } else if((time2 / 1000000000) >= 0.995) {
                playEffect2 = false;
            }
        }

    }

    public Rectangle getLineRectangle() {
        return line;
    }
    public Rectangle getLine2Rectangle() {
        return line2;
    }

    public Polygon getArrowPolygon() {
        return arrowPolygon;
    }
    public Polygon getArrowPolygon2() {
        return arrowPolygon2;
    }
}

