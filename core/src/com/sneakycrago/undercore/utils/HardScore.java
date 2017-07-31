package com.sneakycrago.undercore.utils;

/**
 * Created by Sneaky Crago on 30.07.2017.
 */

public class HardScore {
    public static int hardScore;
    public static int bestHardScore;

    public HardScore() {
        hardScore = 0;
    }

    public static int getGameScore() {
        return hardScore;
    }

    public static void setGameScore(int gameScore) {
        hardScore = gameScore;
    }

    public static void addGameScore(int increment) {
        hardScore += increment;
    }

    public static void makeBestScore(){
        if(hardScore < bestHardScore){
            bestHardScore = getBestScore();
        } else {
            bestHardScore = hardScore;
        }
    }

    public static int getBestScore() {
        return bestHardScore;
    }
}
