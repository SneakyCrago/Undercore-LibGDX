package com.sneakycrago.undercore.utils;

import com.sneakycrago.undercore.Application;

/**
 * Created by Sneaky Crago on 01.07.2017.
 */

public interface AdsController {
    public void showInterstitialAd (Runnable then);

    public void showRewardedVideo (Runnable then);

    public void showRewardedReborn (Runnable then);

    public void isRewardedRebornLoaded();

    public void isRewardedMoneyLoaded();
}
