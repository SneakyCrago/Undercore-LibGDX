package com.sneakycrago.undercore.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import com.sneakycrago.undercore.Application;
import com.badlogic.gdx.math.Circle;
import com.sneakycrago.undercore.utils.Globals;

import java.util.Iterator;

/**
 * Created by Sneaky Crago on 25.03.2017.
 */

public class Player {
    private float GRAVITY = -15;  // -10; jump 250
    private final int BOT_SIDE = 11;
    private final int TOP_SIDE = 267; // 267 ???
    private final int TEXTURE_SIZE = 32;
    private final float lineY = 5;

    private Vector2 position;
    private Vector2 velocity;

    private float playerPosY;
    private float playerPosX;

    public boolean Jump = true;
    public boolean Line = false;
    public boolean Shield = false;

    public boolean alive = true;
    //public boolean dead = false;

    private Rectangle playerCubeRectangle;
    private com.badlogic.gdx.math.Circle playerCircle, circleArrowUp, circleArrowDown, circleArrowSmall;

    private Polygon playerPolygon;

    private float elapsedTime = 0f, currentFrame;
    private Sprite spriteAnim;
    private Texture texture, moonGrayTex;

    private TextureRegion moonTex, moonLineTex, dummyTex, dummyLineTex, arrowTex, arrowLineTex;
    private Sprite moon, dummy, arrow;

    private Application game;


    public Player(float x, float y, Application game) {
        this.game = game;

        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);


        playerCircle = new Circle(x+16,y+16,16);


        circleArrowUp = new Circle(x+9, y+32-11, 9);
        circleArrowDown  = new Circle(x+9, y+11, 9);
        circleArrowSmall = new Circle(x+16+12,y+16,3);

        playerCubeRectangle = new Rectangle(x, y, 32, 32);


        playerPolygon = new Polygon(new float[] {
                0,0,
                0,32,
                32,32,
                32,0});

        //texture = new Texture[5];
        if(game.normalMode) {
            initTextures();
        } else if(game.hardMode) {
            initHardTextures();
        }

        playerCubeRectangle.setPosition(position.x, position.y);
        playerPolygon.setPosition(position.x, position.y);

        degrees = 0;

        effect = new Array<ArrowEffect>();
    }

    public void initTextures(){
        switch (Application.playerSkin) {
            case 0:
                if (game.normalMode || game.hardMode) {
                    switch (Application.gameSkin) {
                        case 0: //texture[0] = game.playerSkin0;
                            texture = game.playerSkin0;
                            spriteAnim = new Sprite(texture);
                            break;
                        case 1:
                            texture = game.playerSkin1;
                            spriteAnim = new Sprite(texture);
                            break;
                        case 2:
                            texture = game.playerSkin2;
                            spriteAnim = new Sprite(texture);
                            break;
                        case 3:
                            texture = game.playerSkin3;
                            spriteAnim = new Sprite(texture);
                            break;
                        case 4:
                            texture = game.playerSkin4;
                            spriteAnim = new Sprite(texture);
                            break;
                    }

                } else if (game.survivalMode) {
                    texture = game.playerSkin0;
                    spriteAnim = new Sprite(texture);
                }
                texture = game.playerSkin0;
                spriteAnim = new Sprite(texture);

                spriteAnim.setSize(32,32);
                spriteAnim.setPosition(position.x - 32, position.y);
                break;
            case 1:
                switch (Application.gameSkin) {
                    case 0:
                        texture = game.moonAnimTex[0];
                        spriteAnim = new Sprite(texture);

                        moonTex = new TextureRegion(game.moonAtlas.findRegion("moon0"));
                        moonLineTex = new TextureRegion(game.moonAtlas.findRegion("moon0line"));
                        break;
                    case 1:
                        texture = game.moonAnimTex[1];
                        spriteAnim = new Sprite(texture);

                        moonTex = new TextureRegion(game.moonAtlas.findRegion("moon1"));
                        moonLineTex = new TextureRegion(game.moonAtlas.findRegion("moon1line"));
                        break;
                    case 2:
                        texture = game.moonAnimTex[2];
                        spriteAnim = new Sprite(texture);

                        moonTex = new TextureRegion(game.moonAtlas.findRegion("moon2"));
                        moonLineTex = new TextureRegion(game.moonAtlas.findRegion("moon2line"));
                        break;
                    case 3:
                        texture = game.moonAnimTex[3];
                        spriteAnim = new Sprite(texture);

                        moonTex = new TextureRegion(game.moonAtlas.findRegion("moon3"));
                        moonLineTex = new TextureRegion(game.moonAtlas.findRegion("moon3line"));
                        break;
                    case 4:
                        texture = game.moonAnimTex[4];
                        spriteAnim = new Sprite(texture);

                        moonTex = new TextureRegion(game.moonAtlas.findRegion("moon4"));
                        moonLineTex = new TextureRegion(game.moonAtlas.findRegion("moon4line"));
                        break;
                }
                spriteAnim.setSize(32,32);
                spriteAnim.setOrigin(48,16);
                spriteAnim.setPosition(position.x - 32, position.y);

                moon = new Sprite(moonTex);

                moon.setSize(32, 32);
                moon.setOrigin(16,16);
                moon.setPosition(position.x, position.y);

                break;
            case 2:
                switch (Application.gameSkin) {
                    case 0:
                        texture = game.playerSkin4;
                        spriteAnim = new Sprite(texture);

                        dummyTex = new TextureRegion(game.dummyAtlas.findRegion("dummy0"));
                        dummyLineTex = new TextureRegion(game.dummyAtlas.findRegion("dummy0line"));
                        break;
                    case 1:
                        dummyTex = new TextureRegion(game.dummyAtlas.findRegion("dummy1"));
                        dummyLineTex = new TextureRegion(game.dummyAtlas.findRegion("dummy1line"));
                        break;
                    case 2:
                        dummyTex = new TextureRegion(game.dummyAtlas.findRegion("dummy2"));
                        dummyLineTex = new TextureRegion(game.dummyAtlas.findRegion("dummy2line"));
                        break;
                    case 3:
                        dummyTex = new TextureRegion(game.dummyAtlas.findRegion("dummy3"));
                        dummyLineTex = new TextureRegion(game.dummyAtlas.findRegion("dummy3line"));
                        break;
                    case 4:
                        dummyTex = new TextureRegion(game.dummyAtlas.findRegion("dummy4"));
                        dummyLineTex = new TextureRegion(game.dummyAtlas.findRegion("dummy4line"));
                        break;
                }

                dummy = new Sprite(dummyTex);

                dummy.setSize(32, 32);
                dummy.setPosition(position.x, position.y);

                break;
            case 3:
                switch (Application.gameSkin) {
                    case 0:
                        texture = game.playerSkin4;
                        spriteAnim = new Sprite(texture);

                        arrowTex = new TextureRegion(game.arrowAltas.findRegion("arrow0"));
                        arrowLineTex = new TextureRegion(game.arrowAltas.findRegion("arrow0line"));
                        break;
                    case 1:
                        arrowTex = new TextureRegion(game.arrowAltas.findRegion("arrow1"));
                        arrowLineTex = new TextureRegion(game.arrowAltas.findRegion("arrow1line"));
                        break;
                    case 2:
                        arrowTex = new TextureRegion(game.arrowAltas.findRegion("arrow2"));
                        arrowLineTex = new TextureRegion(game.arrowAltas.findRegion("arrow2line"));
                        break;
                    case 3:
                        arrowTex = new TextureRegion(game.arrowAltas.findRegion("arrow3"));
                        arrowLineTex = new TextureRegion(game.arrowAltas.findRegion("arrow3line"));
                        break;
                    case 4:
                        arrowTex = new TextureRegion(game.arrowAltas.findRegion("arrow4"));
                        arrowLineTex = new TextureRegion(game.arrowAltas.findRegion("arrow4line"));
                        break;
                }

                arrow = new Sprite(arrowTex);

                arrow.setSize(32, 32);
                arrow.setOrigin(16,16);
                arrow.setPosition(position.x, position.y);

                break;
        }
    }

    public void initHardTextures(){
        switch (Application.playerSkinHard) {
            case 0:
                texture = game.playerHardAnim;
                break;
            case 1:
                texture = game.moonHardAnim;
                break;
        }

        moonTex = new TextureRegion(game.moonAtlas.findRegion("moon_hard"));
        moonGrayTex = new Texture(Gdx.files.internal("textures/player/moonGray.png"));
        moonLineTex = new TextureRegion(moonGrayTex);

        moon = new Sprite(moonTex);

        moon.setSize(32, 32);
        moon.setOrigin(16,16);
        moon.setPosition(position.x, position.y);


        spriteAnim = new Sprite(texture);

        spriteAnim.setSize(32,32);
        spriteAnim.setPosition(position.x - 32, position.y);
    }

    public void secondChance(int x, int y){
        position.set(x,y);
        velocity.set(0, 0);
        playerCubeRectangle.setPosition(position.x, position.y);
        playerPolygon.setPosition(position.x, position.y);
        if(game.normalMode) {
            if(Application.playerSkin == 1) {
                moon.setPosition(position.x, position.y);
                moon.setRotation(0);
            }
            if(Application.playerSkin == 2)
            dummy.setPosition(position.x, position.y);
            if(Application.playerSkin == 3) {
                arrow.setPosition(position.x, position.y);
                arrow.setRotation(0);
            }
        } else if(game.hardMode) {
            if(Application.playerSkinHard == 1){
                moon.setPosition(position.x,position.y);
                moon.setRotation(0);
            }
        }
        alive = true;
    }

    float degrees;
    float maxDegrees = 30;
    float maxDegreesArrow = 45;
    public void update(float delta, float x) {
        velocity.add(0, GRAVITY*x);
        velocity.scl(delta);

        // x = SPEED * delta = движение по x
        position.add(0, velocity.y);
        // не заходит за белые границы

        velocity.scl(1 / delta);

        playerCubeRectangle.setPosition(position.x, position.y);

        playerCircle.setPosition(position.x+16, position.y+16);

        circleArrowUp.setPosition(position.x+9, position.y+32-11);
        circleArrowDown.setPosition(position.x+9, position.y+11);
        circleArrowSmall.setPosition(position.x+16+12,position.y+16);

        playerPolygon.setPosition(position.x, position.y);

        spriteAnim.setPosition(position.x - 32, position.y);

        currentFrame += 22*delta;

        if(game.normalMode) {
            if (Application.playerSkin == 2) {
                dummy.setPosition(position.x, position.y);
            }
            if (Application.playerSkin == 3) {
                arrow.setPosition(position.x, position.y);
                if (alive) {
                    if (velocity.y < 0) {
                        degrees = -maxDegreesArrow;

                        circleArrowUp.setPosition(position.x + 9 + 6, position.y + 32 - 11);
                        circleArrowDown.setPosition(position.x + 9, position.y + 11 + 6);
                        circleArrowSmall.setPosition(position.x + 16 + 8, position.y + 8);

                        //spawnBullet();
                    }
                    if (velocity.y > 0) {
                        degrees = maxDegreesArrow;

                        circleArrowUp.setPosition(position.x + 9, position.y + 32 - 11 - 6);
                        circleArrowDown.setPosition(position.x + 9 + 6, position.y + 11);
                        circleArrowSmall.setPosition(position.x + 16 + 8, position.y + 24);

                        //spawnBullet();
                    }
                    if (velocity.y == 0) {
                        degrees = 0;
                        circleArrowUp.setPosition(position.x + 9, position.y + 32 - 11);
                        circleArrowDown.setPosition(position.x + 9, position.y + 11);
                        circleArrowSmall.setPosition(position.x + 16 + 12, position.y + 16);

                        //spawnBullet();
                    }
                } else if (!alive) {
                    degrees = -90;
                }

                arrow.setRotation(degrees);

            }
            if (Application.playerSkin == 1) {
                //degrees += 4.5f; // 6
                if (alive) {
                    if (velocity.y < 0) {
                        degrees -= 3f;

                        if (degrees < -maxDegrees) {
                            degrees = -maxDegrees;
                        }
                    }
                    if (velocity.y > 0) {
                        degrees += 3f;

                        if (degrees > maxDegrees) {
                            degrees = maxDegrees;
                        }
                    }
                } else if (!alive) {
                    degrees = -90;
                }

                moon.setRotation(degrees);

                spriteAnim.setRotation(degrees);

                moon.setPosition(position.x, position.y);
            }
        } else if(game.hardMode) {
            if(Application.playerSkinHard == 1) {
                //degrees += 4.5f; // 6
                if (alive) {
                    if (velocity.y < 0) {
                        degrees -= 3f;

                        if (degrees < -maxDegrees) {
                            degrees = -maxDegrees;
                        }
                    }
                    if (velocity.y > 0) {
                        degrees += 3f;

                        if (degrees > maxDegrees) {
                            degrees = maxDegrees;
                        }
                    }
                } else if (!alive) {
                    degrees = -90;
                }

                moon.setRotation(degrees);

                spriteAnim.setRotation(degrees);

                moon.setPosition(position.x, position.y);
            }
        }
    }
    public void updateSurvival(float delta, float x) {
        velocity.add(0, GRAVITY*x);
        velocity.scl(delta);

        // x = SPEED * delta = движение по x
        position.add(velocity.x, velocity.y);
        // не заходит за белые границы

        velocity.scl(1 / delta);

        playerCubeRectangle.setPosition(position.x, position.y);

        playerPolygon.setPosition(position.x, position.y);

        /*if(onRight) {
            spriteAnim.setPosition(position.x - 32, position.y);
        } else if (onLeft) {
            spriteAnim.setPosition(position.x + 32, position.y);
        } */

        //currentFrame += 22*delta;
        if(Jump)
        currentFrame += 6*delta;
    }

    public void checkWhiteSides(){
        if(alive) {
            if(game.normalMode) {
                if (position.y < BOT_SIDE) {
                    position.y = BOT_SIDE;
                    playerCubeRectangle.setY(BOT_SIDE);
                    spriteAnim.setY(BOT_SIDE);
                    if (Application.playerSkin == 3 && game.normalMode) {
                        arrow.setY(BOT_SIDE - 5);
                        position.y = BOT_SIDE - 5;
                    }
                }
                if (position.y > TOP_SIDE) {
                    position.y = TOP_SIDE;
                    playerCubeRectangle.setY(TOP_SIDE);
                    spriteAnim.setY(TOP_SIDE);
                    if (Application.playerSkin == 3 && game.normalMode) {
                        arrow.setY(TOP_SIDE + 5);
                        position.y = TOP_SIDE + 5;
                    }
                }
            } else if(game.hardMode) {
                if (position.y < BOT_SIDE) {
                    position.y = BOT_SIDE;
                    playerCubeRectangle.setY(BOT_SIDE);
                    spriteAnim.setY(BOT_SIDE);

                }
                if (position.y > TOP_SIDE) {
                    position.y = TOP_SIDE;
                    playerCubeRectangle.setY(TOP_SIDE);
                    spriteAnim.setY(TOP_SIDE);
                }
            }
        }
    }
    public void checkSurvivalSides(){
        if(alive) {
            //Y AXIS
            if (position.y < WhiteSides.lineSurvival) {
                position.y = WhiteSides.lineSurvival;
                playerCubeRectangle.setY(WhiteSides.lineSurvival);
            }
            if (position.y > 278- WhiteSides.lineSurvival) { //310 -32=278
                position.y = 278- WhiteSides.lineSurvival;
                playerCubeRectangle.setY(278- WhiteSides.lineSurvival);
            }
            //X AXIS
            if(position.x > 480- WhiteSides.lineSurvival) { //512-32 = 480
                position.x = 480- WhiteSides.lineSurvival;
                playerCubeRectangle.setX(480- WhiteSides.lineSurvival);
            }
            if(position.x < WhiteSides.lineSurvival) {
                position.x =WhiteSides.lineSurvival;
                playerCubeRectangle.setX(WhiteSides.lineSurvival);
            }
        }
    }

    // Прыжок
    public void onClick(Application game, float x) {
        if(alive && Jump) {
            velocity.y = 300*x;
            GRAVITY = -15*x;

            game.jumpSound.play(Application.volume);

            Jump = true;   //1
            Line = false;  //0
            Shield = false;//0


        }
    }

    //SurvivalMode
    private float X_FORCE = 150;

    // for animation
    private boolean onLeft = false;
    private boolean onRight = true;
    // jump right
    public void jumpRight(Application game){
        if(alive && Jump) {
            velocity.y = 300;
            velocity.x = X_FORCE;
            GRAVITY = -15;

            game.jumpSound.play(Application.volume);

            Jump = true;   //1
            Line = false;  //0
            Shield = false;//0

            onLeft = false;
            onRight = true;
        }
    }

    // jump left
    public void jumpLeft(Application game){
        if(alive && Jump) {
            velocity.y = 300;
            velocity.x = -X_FORCE;
            GRAVITY = -15;

            game.jumpSound.play(Application.volume);

            Jump = true;   //1
            Line = false;  //0
            Shield = false;//0

            onLeft = true;
            onRight = false;
        }
    }

    //Состояние: Линия
    public void onLine(Application game) {
        if(alive) {
            velocity.y = playerPosY;
            velocity.x = playerPosX;
            position.add(playerPosX, playerPosY);
            GRAVITY = 0;

            game.lineSound.play(Application.volume);

            Jump = false;  //0
            Line = true;   //1
            Shield = false;//0

            //if(Application.playerSkin == 3){
            //    spawnBullet();
            //}
        }
    }

    // Возвращает гравитацию, когда отпускаем кнопку(кнопки на сенсоре), с линией или щитом
    public void onRelease(float x) {
        GRAVITY = -15*x;

        Jump = true;   //1
        Line = false;  //0
        Shield = false;//0
    }

    //DRAW
    public void drawPlayerCube(ShapeRenderer shapeRenderer){
        if(game.normalMode) {
            switchCubeColor(shapeRenderer);
            //shapeRenderer.rect(position.x, position.y, TEXTURE_SIZE,TEXTURE_SIZE);
            shapeRenderer.rect(playerCubeRectangle.getX(), playerCubeRectangle.getY(), 32, 32);

            if (Line && alive) {
                switchLineCubeColor(shapeRenderer);
                shapeRenderer.rect(playerCubeRectangle.getX(), playerCubeRectangle.getY(), 32, 32);
            }
        } else if(game.hardMode && Application.playerSkinHard ==0) {
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rect(playerCubeRectangle.getX(), playerCubeRectangle.getY(), 32, 32);

            if (Line && alive) {
                shapeRenderer.setColor(Globals.Gray);
                shapeRenderer.rect(playerCubeRectangle.getX(), playerCubeRectangle.getY(), 32, 32);
            }
        }
    }

    public void drawPlayerSprite(SpriteBatch batch){
        if(game.normalMode) {
            if (Application.playerSkin == 1) {
                moon.setRegion(moonTex);
                moon.draw(batch);
            } else if (Application.playerSkin == 2) {
                dummy.setRegion(dummyTex);
                dummy.draw(batch);
            } else if (Application.playerSkin == 3) {
                arrow.setRegion(arrowTex);
                arrow.draw(batch);
            }
        } else if(game.hardMode) {
            if(Application.playerSkinHard == 1) {

                moon.setRegion(moonTex);
                moon.draw(batch);
            }
        }
    }

    public void drawPlayerSpriteLine(SpriteBatch batch) {
        if(game.normalMode) {
                if (Application.playerSkin == 1) {
                    moon.setRegion(moonLineTex);
                    moon.draw(batch);
                }
                if (Application.playerSkin == 2) {
                    dummy.setRegion(dummyLineTex);
                    dummy.draw(batch);
                }
                if (Application.playerSkin == 3) {
                    arrow.setRegion(arrowLineTex);
                    arrow.draw(batch);
                }
        }else if(game.hardMode){
            if(Application.playerSkinHard == 1) {
                moon.setRegion(moonLineTex);
                moon.draw(batch);
            }
        }
    }

    public void drawPlayerLine(ShapeRenderer shapeRenderer) {
        if(alive) {
            switchLineCubeColor(shapeRenderer);
            //shapeRenderer.rect(0, playerCubeRectangle.getY() + 16 - lineY / 2, 96 - 3, lineY);
            //shapeRenderer.rect(96 + 32 + 3, playerCubeRectangle.getY() + 16 - lineY / 2, 512, lineY);

            shapeRenderer.rect(0,playerCubeRectangle.getY() + 16 - lineY / 2, playerCubeRectangle.getX()-3, lineY);
            shapeRenderer.rect(playerCubeRectangle.getX() + 32 + 3,  playerCubeRectangle.getY() + 16 - lineY / 2,
                    512 -playerCubeRectangle.getX() + 32 + 3, lineY);
        }
    }
    public void drawPlayerAnimation(SpriteBatch sb){
        if(alive) {
           animationLogic(sb);
        } else if(Application.playerSkin == 1 ) {
            animationLogic(sb);
        }
    }
    private void animationLogic(SpriteBatch sb){
        elapsedTime += Gdx.graphics.getDeltaTime();
        if (currentFrame > 8) {
            currentFrame = 0;
        }

        spriteAnim.setRegion(32 * (int) currentFrame, 0, 32, 32);
        if (Jump || Application.playerSkin == 2 ) {
            if(onRight) {
                spriteAnim.draw(sb);
            }
        }
    }
    public void inMenuAnimation(SpriteBatch sb,float delta, Application game){
        if(game.normalMode) {
            if (Application.playerSkin != 1 && Application.playerSkin != 3) {
                switch (Application.gameSkin) {
                    case 0: //texture[0] = game.playerSkin0;
                        texture = game.playerSkin0;
                        spriteAnim = new Sprite(texture);
                        break;
                    case 1:
                        texture = game.playerSkin1;
                        spriteAnim = new Sprite(texture);
                        break;
                    case 2:
                        texture = game.playerSkin2;
                        spriteAnim = new Sprite(texture);
                        break;
                    case 3:
                        texture = game.playerSkin3;
                        spriteAnim = new Sprite(texture);
                        break;
                    case 4:
                        texture = game.playerSkin4;
                        spriteAnim = new Sprite(texture);
                        break;
                }
            } else if (Application.playerSkin == 1) {
                switch (Application.gameSkin) {
                    case 0: //texture[0] = game.playerSkin0;
                        texture = game.moonAnimTex[0];

                        break;
                    case 1: //texture[0] = game.playerSkin0;
                        texture = game.moonAnimTex[1];
                        break;
                    case 2: //texture[0] = game.playerSkin0;
                        texture = game.moonAnimTex[2];
                        break;
                    case 3: //texture[0] = game.playerSkin0;
                        texture = game.moonAnimTex[3];
                        break;
                    case 4: //texture[0] = game.playerSkin0;
                        texture = game.moonAnimTex[4];
                        break;
                }
            }
        } else if(game.hardMode) {
            if(Application.playerSkinHard ==0) {
                texture = game.playerHardAnim;
            } else if(Application.playerSkinHard ==1) {
                texture = game.moonHardAnim;
            }
        }
        spriteAnim = new Sprite(texture);
        spriteAnim.setSize(32,32);
        spriteAnim.setOrigin(48,16);
        spriteAnim.setPosition(position.x - 32, position.y);
            elapsedTime += Gdx.graphics.getDeltaTime();
            if (currentFrame > 8) {
                currentFrame = 0;
            }
            spriteAnim.setPosition(position.x - 32, position.y);
            currentFrame += 22 * delta;

            spriteAnim.setRegion(32 * (int) currentFrame, 0, 32, 32);
            spriteAnim.draw(sb);
    }

    private float SPEED = 45;
    private float move,moveX, moveY;
    private float animTime;
    public void arrowAnimation(ShapeRenderer shapeRenderer,float delta){

        animTime += Gdx.graphics.getDeltaTime();

        spawnBullet();
        if(animTime >= 0.5f) {
            animTime =0;
        }

        Iterator<ArrowEffect> iter = effect.iterator();
        while(iter.hasNext()){
            ArrowEffect arrowEffects = iter.next();
            arrowEffects.update(delta);
            if(arrowEffects.delete()) iter.remove();
        }

        for(ArrowEffect arrowEffects: effect) {
            if(Jump) {
                switchCubeColor(shapeRenderer);
            } else if(Line) {
                switchLineCubeColor(shapeRenderer);
            }
            arrowEffects.draw(shapeRenderer);
        }
    }

    private Array<ArrowEffect> effect;
    private void spawnBullet(){
        ArrowEffect arrowEffect = new ArrowEffect(position, velocity);

        effect.add(arrowEffect);
    }

    private void switchCubeColor(ShapeRenderer shapeRenderer){

        switch(Application.gameSkin) {
            case 0:
                shapeRenderer.setColor(Globals.OrangeColor);
                break;
            case 1:
                shapeRenderer.setColor(Globals.Sides1Color);
                break;
            case 2:
                shapeRenderer.setColor(Globals.Player2Color);
                break;
            case 3: shapeRenderer.setColor(Globals.Player3Color);
                break;
            case 4: shapeRenderer.setColor(Globals.Player4Color);
                break;
        }
    }
    private void switchLineCubeColor(ShapeRenderer shapeRenderer){
        if(game.normalMode) {
            if (Application.playerSkin != 2) {
                switch (Application.gameSkin) {
                    case 0:
                        shapeRenderer.setColor(Globals.LightBlueColor);
                        break;
                    case 1:
                        shapeRenderer.setColor(Globals.Line1Color);
                        break;
                    case 2:
                        shapeRenderer.setColor(Globals.Line2Color);
                        break;
                    case 3:
                        shapeRenderer.setColor(Globals.Line3Color);
                        break;
                    case 4:
                        shapeRenderer.setColor(Globals.Line4Color);
                }
            } else if (Application.playerSkin == 2) {
                switch (Application.gameSkin) {
                    case 0:
                        shapeRenderer.setColor(Globals.LightBlueColor);
                        break;
                    case 1:
                        shapeRenderer.setColor(213 / 255f, 227 / 255f, 232 / 255f, 1f);
                        break;
                    case 2:
                        shapeRenderer.setColor(Globals.Line2Color);
                        break;
                    case 3:
                        shapeRenderer.setColor(Globals.Line3Color);
                        break;
                    case 4:
                        shapeRenderer.setColor(Globals.Line4Color);
                }
            }
        } else if(game.hardMode) {
            shapeRenderer.setColor(Globals.Gray);
        }
    }


    public boolean sidesCollision() {
        if(alive) {
            if(game.normalMode) {
                if (Application.playerSkin != 3) {
                    if (position.y <= 11 || position.y >= 267) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    if (position.y <= 11 - 4 || position.y >= 267 + 4) {
                        return true;
                    } else {
                        return false;
                    }
                }
            } else if(game.hardMode) {
                if (position.y <= 11 || position.y >= 267) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }
    public boolean survivalSidesCollision(){
        if(alive){
            if(position.y <= WhiteSides.lineSurvival || position.y >= 278 - lineY
                    || position.x <= WhiteSides.lineSurvival || position.x >= 480 -WhiteSides.lineSurvival){
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private boolean isJumped = false;
    public boolean deathPlayed = false;

    public void deathAnimation(){
        if(!deathPlayed) {
            velocity.y = 300;
            GRAVITY = -30; //15
            isJumped = true;
            deathPlayed = true;
            Line = false;
        }
    }

    public Rectangle getPlayerRectangle() {
        return playerCubeRectangle;
    }
    public Polygon getPlayerPolygon() {
        return playerPolygon;
    }
    public Vector2 getPosition() {
        return position;
    }
    public Circle getPlayerCircle() {
        return playerCircle;
    }
    public Circle getCircleArrowUp() {
        return circleArrowUp;
    }
    public Circle getCircleArrowDown() {
        return circleArrowDown;
    }
    public Circle getCircleArrowSmall() {
        return circleArrowSmall;
    }
}

