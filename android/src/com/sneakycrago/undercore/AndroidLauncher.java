package com.sneakycrago.undercore;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.google.example.games.basegameutils.GameHelper;
import com.sneakycrago.undercore.utils.AdsController;
import com.sneakycrago.undercore.utils.Currency;
import com.sneakycrago.undercore.utils.GpgsController;

public class AndroidLauncher extends AndroidApplication implements AdsController,
		GameHelper.GameHelperListener, GpgsController, GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener {
	private static final String TAG = "Admob";
	private static final String APP_ID = "ca-app-pub-3492165730340168~7340545933";
	protected AdView adView;
	protected InterstitialAd interstitialAd;
	protected RewardedVideoAd rewardedMoney;
	protected RewardedVideoAd rewardedReborn;

	private String id;

	// помощник для работы с игровыми сервисами
	private GameHelper gameHelper;
	// класс нашей игры
	private Application game;

	private GoogleApiClient googleApiClient;

	private static final int RC_SIGN_IN           = 9001;
	private static final int REQUEST_ACHIEVEMENTS = 9002;
	private static final int REQUEST_LEADERBOARD  = 9003; // increment next constants

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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

		// Create the Google Api Client with access to the Play Games services
		googleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(Games.API).addScope(Games.SCOPE_GAMES)
				// add other APIs and scopes here as needed
				.build();

		// CLIENT_ALL указывет на использование API всех клиентов
		gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		// выключить автоматический вход при запуске игры
		gameHelper.setConnectOnStart(false);
		gameHelper.enableDebugLog(true);


		game = new Application(this, this);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Application(this, this), config);

		gameHelper.setup(this);

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

	// методы gameHelper’а: onStart(), onStop() вызываются для корректной работы
	// GPGS в жизненном цикле android-приложения
	@Override
	protected void onStart() {
		super.onStart();
		gameHelper.onStart(this);

		connect();
	}

	@Override
	protected void onStop() {
		super.onStop();
		gameHelper.onStop();

		disconnect();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// здесь gameHelper принимает решение о подключении, переподключении или
		// отключении от игровых сервисов, в зависимости от кода результата
		// Activity
		gameHelper.onActivityResult(requestCode, resultCode, data);

		if ( requestCode == RC_SIGN_IN ) {
			//Log.d( className, "RC_SIGN_IN, responseCode=" + response + ", intent=" + intent );
			if ( requestCode == RESULT_OK ) {
				googleApiClient.connect();
			}
		}
	}


	@Override
	public void connect() {
		if ( isSignedIn() ) { return; }

		googleApiClient.registerConnectionCallbacks( this );
		googleApiClient.registerConnectionFailedListener( this );
		googleApiClient.connect();

		Log.d( TAG, "Client: log in" );
	}

	@Override
	public void disconnect() {
		if ( !isSignedIn() ) { return; }
		googleApiClient.unregisterConnectionCallbacks( this );
		googleApiClient.unregisterConnectionFailedListener( this );
		googleApiClient.disconnect();

		Log.d( TAG, "Client: log out" );
	}

	@Override
	public void signIn() {
		try {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// инициировать вход пользователя. Может быть вызван диалог
					// входа. Выполняется в UI-потоке
					gameHelper.beginUserInitiatedSignIn();

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
		}

		googleApiClient.registerConnectionCallbacks( this );
		googleApiClient.registerConnectionFailedListener( this );
		googleApiClient.connect();
	}

	@Override
	public void signOut() {
		try {
			runOnUiThread(new Runnable() {
				@Override
				public void run()
				{
					gameHelper.signOut();
				}
			});
		}
		catch (Exception e) {
			Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage() + ".");
		}
	}

	@Override
	public void unlockAchievement(String achievementId) {
		// открыть достижение с ID achievementId
		if (isSignedIn()) {
			Games.Achievements.unlock(gameHelper.getApiClient(), achievementId);
		}
	}

	@Override
	public void submitScore(int highScore) {
		// отправить игровые очки в конкретную таблицу рекордов с ID
		if (isSignedIn()) {
			Games.Leaderboards.submitScore(gameHelper.getApiClient(), game.leaderboard_Highscore , highScore);
		} else {
			signIn();
		}
	}

	@Override
	public void showAchievements() {
		if (isSignedIn()) {
			// вызвать Activity с достижениями
			startActivityForResult(
					Games.Achievements.getAchievementsIntent(gameHelper
							.getApiClient()), 101);
		} else {
			signIn();
		}
	}

	@Override
	public void showScores() {
		if (isSignedIn()) {
			startActivityForResult(
					Games.Leaderboards.getAllLeaderboardsIntent(gameHelper
							.getApiClient()), 100);
		} else {
			signIn();
		}
	}

	@Override
	public boolean isSignedIn() {
		return gameHelper.isSignedIn();
	}

	@Override
	public void onSignInFailed() {

	}

	@Override
	public void onSignInSucceeded() {

	}

	@Override
	public void onConnected(@Nullable Bundle bundle) {

	}

	@Override
	public void onConnectionSuspended(int i) {
		googleApiClient.connect();
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

	}
}
