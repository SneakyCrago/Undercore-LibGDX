package com.sneakycrago.undercore;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.sneakycrago.undercore.utils.AdsController;
import com.sneakycrago.undercore.utils.Currency;

public class AndroidLauncher extends AndroidApplication implements AdsController {
	private static final String TAG = "Admob";
	private static final String APP_ID = "ca-app-pub-3492165730340168~7340545933";
	protected AdView adView;
	protected InterstitialAd interstitialAd;
	protected RewardedVideoAd rewardedMoney;
	protected RewardedVideoAd rewardedReborn;

	private String id;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//MobileAds.initialize(this, APP_ID);
		interstitialAd = new InterstitialAd(this);
		interstitialAd.setAdUnitId("ca-app-pub-3492165730340168/5724211932");

		id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
		AdRequest.Builder builder = new AdRequest.Builder();
		AdRequest ad = builder.addTestDevice(id).build();
		interstitialAd.loadAd(ad);

		interstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				Log.i(TAG, "Ad Loaded...");
			}
			@Override
			public void onAdFailedToLoad(int errorCode) {
				// Code to be executed when an ad request fails.
				Log.i("Ads", "onAdFailedToLoad");
			}
			@Override
			public void onAdOpened() {
				Log.i("Ads", "onAdOpened");
				Application.showInterstitialAd = false;
			}
			@Override
			public void onAdClosed() {
				Log.i("Ads", "onAdClosed");
				AdRequest.Builder builder = new AdRequest.Builder();
				AdRequest ad = builder.addTestDevice(id).build();
				interstitialAd.loadAd(ad);
			}
		});

		rewardedMoney = MobileAds.getRewardedVideoAdInstance(this);
		rewardedMoney.setRewardedVideoAdListener(new RewardedVideoAdListener() {
			@Override
			public void onRewardedVideoAdLoaded() {

			}

			@Override
			public void onRewardedVideoAdOpened() {

			}

			@Override
			public void onRewardedVideoStarted() {

			}

			@Override
			public void onRewardedVideoAdClosed() {

			}

			@Override
			public void onRewarded(RewardItem rewardItem) {

			}

			@Override
			public void onRewardedVideoAdLeftApplication() {

			}

			@Override
			public void onRewardedVideoAdFailedToLoad(int i) {

			}
		});

		loadRewardedMoneyAd();

		rewardedReborn = MobileAds.getRewardedVideoAdInstance(this);
		rewardedReborn.setRewardedVideoAdListener(new RewardedVideoAdListener() {
			@Override
			public void onRewardedVideoAdLoaded() {

			}

			@Override
			public void onRewardedVideoAdOpened() {

			}

			@Override
			public void onRewardedVideoStarted() {

			}

			@Override
			public void onRewardedVideoAdClosed() {

			}

			@Override
			public void onRewarded(RewardItem rewardItem) {

			}

			@Override
			public void onRewardedVideoAdLeftApplication() {

			}

			@Override
			public void onRewardedVideoAdFailedToLoad(int i) {

			}
		});

		loadRewardedRebornAd();

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Application(this), config);
	}

	private void loadRewardedMoneyAd(){
		AdRequest.Builder builder = new AdRequest.Builder();
		AdRequest ad = builder.addTestDevice(id).build();
		rewardedMoney.loadAd("ca-app-pub-3492165730340168/8817279139", ad);
	}
	private void loadRewardedRebornAd(){
		AdRequest.Builder builder = new AdRequest.Builder();
		AdRequest ad = builder.addTestDevice(id).build();
		rewardedReborn.loadAd("ca-app-pub-3492165730340168/4247478732", ad);
	}

	@Override
	public void showInterstitialAd(final Runnable then) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (then != null) {
					interstitialAd.setAdListener(new AdListener() {
						@Override
						public void onAdClosed() {
							Gdx.app.postRunnable(then);
							//String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
							AdRequest.Builder builder = new AdRequest.Builder();
							AdRequest ad = builder.addTestDevice(id).build();
							interstitialAd.loadAd(ad);
						}
					});
				}
				interstitialAd.show();
			}
		});
	}

	@Override
	public void showRewardedVideo(final Runnable then) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (then != null) {
					rewardedMoney.setRewardedVideoAdListener(new RewardedVideoAdListener() {
						@Override
						public void onRewardedVideoAdLoaded() {
							Log.i(TAG, "RewardedVideo for money loaded");
						}

						@Override
						public void onRewardedVideoAdOpened() {

						}

						@Override
						public void onRewardedVideoStarted() {

						}

						@Override
						public void onRewardedVideoAdClosed() {
							Gdx.app.postRunnable(then);

							AdRequest.Builder builder = new AdRequest.Builder();
							AdRequest ad = builder.addTestDevice(id).build();
							rewardedMoney.loadAd("ca-app-pub-3492165730340168/8817279139", ad);
						}

						@Override
						public void onRewarded(RewardItem rewardItem) {
							//rewardItem.getAmount();
							Currency.currency += 10;
							Application.preferences.putInteger("currency", Currency.currency);
							Application.preferences.flush();
						}

						@Override
						public void onRewardedVideoAdLeftApplication() {

						}

						@Override
						public void onRewardedVideoAdFailedToLoad(int i) {

						}
					});
				}
				rewardedMoney.show();
			}
		});
	}


	@Override
	public void showRewardedReborn(final Runnable then) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				rewardedReborn.setRewardedVideoAdListener(new RewardedVideoAdListener() {
					@Override
					public void onRewardedVideoAdLoaded() {

					}

					@Override
					public void onRewardedVideoAdOpened() {

					}

					@Override
					public void onRewardedVideoStarted() {

					}

					@Override
					public void onRewardedVideoAdClosed() {
						Gdx.app.postRunnable(then);

						AdRequest.Builder builder = new AdRequest.Builder();
						AdRequest ad = builder.addTestDevice(id).build();
						rewardedReborn.loadAd("ca-app-pub-3492165730340168/4247478732", ad);
					}

					@Override
					public void onRewarded(RewardItem rewardItem) {
						Application.reborn = true;
					}

					@Override
					public void onRewardedVideoAdLeftApplication() {

					}

					@Override
					public void onRewardedVideoAdFailedToLoad(int i) {

					}
				});
				rewardedReborn.show();
			}
		});
	}

    @Override
    public void isRewardedRebornLoaded() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Application.loadedReborn = rewardedReborn.isLoaded();
			}
		});
    }

	@Override
	public void isRewardedMoneyLoaded() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Application.loadedMoney = rewardedMoney.isLoaded();
			}
		});
	}

}
