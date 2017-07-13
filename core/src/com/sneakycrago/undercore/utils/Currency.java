package com.sneakycrago.undercore.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.sneakycrago.undercore.Application;

import java.util.Random;

/**
 * Created by Sneaky Crago on 25.04.2017.
 */

public class Currency {

    public static int Money = 0; // permanent
    public static int currency = 0; // all game
    public static int maxMoney = 0;

    private static Random random = new Random();
    private static float  X = 18f; //old - 12f
    private static double chance;
    private static double fraction; //остаток от деления
    private static int randomNumber = 0;

    private int scrooge = 9999; // 10000-1


    public void countCurency(int numberOfBlocks, Application game){
        randomNumber = random.nextInt(101);
        chance = X * numberOfBlocks;
        Money += Math.floor(chance/100);
        maxMoney += Math.floor(chance/100);
        fraction = chance - Math.floor(chance/100)*100;

        if(randomNumber <= fraction){
            Money++;

            if(maxMoney == 0 && game.android) {
                game.unlockFirstCoin();
            }
            if(maxMoney == scrooge && game.android) {
                game.unlockScroogeAchievment();
            }
            if(maxMoney == 100-1) {
                game.unlockTreasureAchievment();
            }
            if(maxMoney == 200-1) {
                game.unlockNoblemanAchievment();
            }
            if(maxMoney == 500-1) {
                game.unlockFirstCapitalAchievment();
            }
            if(maxMoney == 1337-1) {
                game.unlock1337Achievment();
            }

            maxMoney++;
        }
    }

    public static void resetMoney(){
        Money = 0;
    }

    public static void addMoneyToCurrency(){
        currency += Money;
    }
}
