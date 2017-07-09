package com.sneakycrago.undercore.utils;

/**
 * Created by Sneaky Crago on 07.07.2017.
 */

public interface GpgsController {
    public void connect();
    public void disconnect();
    /** Вход */
    public void signIn();
    /** Выход */
    /**
     * Разблокировать достижение
     *
     * @param achievementId
     *            ID достижения. Берется из файла games-ids.xml
     */
    public void unlockAchievement(String achievementId);
    public void submitScore(int highScore);

    public void submitMoney(int maxMoney);
    /** Показать Activity с достижениями */
    public void showAchievements();
    /** Показать Activity с таблицей рекордов */
    public void showScores();
    /** Узнать статус входа пользователя */
    public boolean isSignedIn();
}
