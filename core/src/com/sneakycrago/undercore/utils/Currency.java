package com.sneakycrago.undercore.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Random;

/**
 * Created by Sneaky Crago on 25.04.2017.
 */

public class Currency {

    public static int Money = 0;
    public static int currency = 0;

    private static Random random = new Random();
    private static float  X = 12f;
    private static double chance;
    private static double fraction; //остаток от деления
    private static int randomNumber = 0;

    public static void countCurency(int numberOfBlocks){

        randomNumber = random.nextInt(101);
        chance = X * numberOfBlocks;
        Money += Math.floor(chance/100);
        fraction = chance - Math.floor(chance/100)*100;

        if(randomNumber <= fraction){
            Money++;
        }
    }


    public static void resetMoney(){
        Money = 0;
    }

    public static void addMoneyToCurrency(){
        currency += Money;
    }
}
