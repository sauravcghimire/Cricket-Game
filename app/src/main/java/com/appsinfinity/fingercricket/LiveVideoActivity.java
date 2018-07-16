package com.appsinfinity.fingercricket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.appsinfinity.fingercricket.utils.Utils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

/**
 * Created by macbook on 11/8/16.
 */

public class LiveVideoActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener {

    String videoId;
    InterstitialAd interstitialAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.live_video);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                finish();
            }
        });

        requestNewInterstitial();

        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                finish();
            }
        }.start();

        videoId = getIntent().getStringExtra("videoId");
        videoId = videoId.substring(videoId.indexOf("=") + 1);
        if (videoId.contains("&")) {
            videoId = videoId.substring(0, videoId.indexOf("&"));
        }
        YouTubePlayerSupportFragment mYoutubePlayerFragment = new YouTubePlayerSupportFragment();
        mYoutubePlayerFragment.initialize("AIzaSyBpcDXChTlhX-QjOm5Dk1d-YDg1LtqSx4I", this);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.youtube_view, mYoutubePlayerFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.loadVideo(videoId);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }


    public static void startVideo(Context context,String videoId){
        Intent intent = new Intent(context,LiveVideoActivity.class);
        intent.putExtra("videoId",videoId);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(interstitialAd.isLoaded()){
            Utils.showInterstitialAd(interstitialAd);
        }else {
            super.onBackPressed();
        }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        interstitialAd.loadAd(adRequest);
    }
}
