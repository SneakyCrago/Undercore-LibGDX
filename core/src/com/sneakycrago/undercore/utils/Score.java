package com.sneakycrago.undercore.utils;

/**
 * Created by Sneaky Crago on 02.04.2017.
 */

public class Score {

    public static int gameScore;
    public static int bestScore;

    public Score() {
        gameScore = 0;
    }

    public static int getGameScore() {
        return gameScore;
    }

    public static void setGameScore(int gameScore) {
        Score.gameScore = gameScore;
    }

    public static void addGameScore(int increment) {
        gameScore += increment;
    }

    public static void makeBestScore(){
        if(gameScore < bestScore){
            bestScore = getBestScore();
        } else {
            bestScore = gameScore;
        }
    }

    public static int getBestScore() {
        return bestScore;
    }
}
