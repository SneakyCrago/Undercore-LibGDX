package com.sneakycrago.undercore.utils;

/**
 * Created by Sneaky Crago on 07.07.2017.
 */

public interface GpgsController {
    void connect();
     void disconnect();
    /** Вход */
    void signIn();
    /** Выход */
    /**
     * Разблокировать достижение
     *
     * @param achievementId
     *            ID достижения. Берется из файла games-ids.xml
     */
    void unlockAchievement(String achievementId);
    void submitScore(int highScore);
    void submitHardScore(int highScore);

    void submitMoney(int maxMoney);
    /** Показать Activity с достижениями */
    void showAchievements();
    /** Показать Activity с таблицей рекордов */
     void showScores();
    /** Узнать статус входа пользователя */
     boolean isSignedIn();
}
