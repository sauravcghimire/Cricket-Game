package com.appsinfinity.fingercricket;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.appsinfinity.fingercricket.models.Tournament;
import com.appsinfinity.fingercricket.utils.AppConstants;
import com.appsinfinity.fingercricket.utils.FirebaseUtil;
import com.appsinfinity.fingercricket.utils.GamePreference;
import com.appsinfinity.fingercricket.utils.Utils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by macbook on 4/21/15.
 */
public class Home extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.highlights)
    TextView highlights;
    @BindView(R.id.news)
    TextView news;
    TextView inter, ipl, exit;
    GamePreference gamePreference;
    InterstitialAd interstitialAd,interstitialAd2;
    String value = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.first_home);
        ButterKnife.bind(this);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        interstitialAd = new InterstitialAd(this);
        interstitialAd2 = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));
        interstitialAd2.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
        interstitialAd2.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                startVideo(value);
            }
        });
        requestNewInterstitial();

        gamePreference = new GamePreference(this);
        inter = (TextView) findViewById(R.id.international);
        ipl = (TextView) findViewById(R.id.ipl);
        exit = (TextView) findViewById(R.id.exit);
        inter.setOnClickListener(this);
        ipl.setOnClickListener(this);
        highlights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gamePreference.getGameCount() < 3) {
                    showNotEnoughGameDialog(3);
                } else {
                    final ProgressDialog pd = new ProgressDialog(Home.this);
                    pd.setMessage("Please wait ...");
                    pd.setTitle("Loading Video");
                    pd.show();
                    FirebaseUtil.getDatabaseRefWithName("link").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            pd.dismiss();
                            value = (String)dataSnapshot.getValue();
                            if(interstitialAd2.isLoaded()){
                               Utils.showInterstitialAd(interstitialAd2);
                            }else{
                                startVideo(value);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            if(interstitialAd.isLoaded()){
                                Utils.showInterstitialAd(interstitialAd);
                            }
                            Toast.makeText(Home.this,"No videos at this time",Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    });
                }
            }
        });
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gamePreference.getGameCount() < 2) {
                    showNotEnoughGameDialog(2);
                } else {
                    if (interstitialAd.isLoaded()) {
                        Utils.showInterstitialAd(interstitialAd);
                    }

                    NewsActivity.statNewsActivity(Home.this);
                }
            }
        });
        exit.setOnClickListener(this);
    }

    private void startVideo(String value) {
        LiveVideoActivity.startVideo(Home.this, value);
    }

    private void showNotEnoughGameDialog(int i) {
        new AlertDialogWrapper.Builder(this)
                .setTitle("Can not Open")
                .setMessage("You have to play " + (i - gamePreference.getGameCount()) + " more games to enjoy this feature.")
                .setPositiveButton("I understand.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onClick(View v) {
        Tournament tournament = new Tournament();
        switch (v.getId()) {
            case R.id.international:
                tournament.setId(0);
                tournament.setType(AppConstants.INTL);
                GameActivity.start(this, tournament);

                break;
            case R.id.ipl:
                tournament.setId(0);
                tournament.setType(AppConstants.IPL);
                GameActivity.start(this, tournament);
                break;
            case R.id.exit:
                new AlertDialogWrapper.Builder(this)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure to exit?")
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                finish();

                            }
                        }).show();
                break;

        }
    }


    @Override
    public void onBackPressed() {
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        interstitialAd.loadAd(adRequest);
        interstitialAd2.loadAd(adRequest);
    }

    public static void startHome(Context context) {
        Intent intent = new Intent(context, Home.class);
        context.startActivity(intent);
    }


}
