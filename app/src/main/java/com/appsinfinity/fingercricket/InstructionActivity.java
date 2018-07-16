package com.appsinfinity.fingercricket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.appsinfinity.fingercricket.models.Tournament;
import com.appsinfinity.fingercricket.utils.GamePreference;
import com.appsinfinity.fingercricket.utils.Utils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by Saurav on 4/1/2015.
 */
public class InstructionActivity extends AppCompatActivity implements View.OnClickListener {


    GamePreference gamePreference;
    Tournament tournament;
    Button btnDismiss;
    InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Makes Activity Full Screen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.instruction);
        btnDismiss = (Button) findViewById(R.id.button_dismiss);

        /*Initialize Ad*/
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial2();
            }
        });


        gamePreference = new GamePreference(this);
        tournament = getIntent().getParcelableExtra(GameActivity.KEY_TOURNAMENT);

        btnDismiss.setOnClickListener(this);

    }

    private void requestNewInterstitial2() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        interstitialAd.loadAd(adRequest);
    }

    @Override
    public void onBackPressed() {
    }

    public static void start(Context context, Tournament tournament){
        Intent intent = new Intent(context,InstructionActivity.class);
        intent.putExtra(GameActivity.KEY_TOURNAMENT,tournament);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        gamePreference.setIsInstructionSeen(true);
        if (interstitialAd.isLoaded()) {
            Utils.showInterstitialAd(interstitialAd);
        }
        GameActivity.start(InstructionActivity.this,tournament);
        finish();
    }
}
