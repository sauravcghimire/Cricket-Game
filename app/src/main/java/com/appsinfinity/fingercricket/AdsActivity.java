package com.appsinfinity.fingercricket;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsinfinity.fingercricket.utils.Utils;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by macbook on 2/11/17.
 */
public class AdsActivity extends AppCompatActivity {
    @BindView(R.id.iv_banner)
    ImageView ivBanner;
    @BindView(R.id.tv_ad_message)
    TextView tvAdMessage;
    InterstitialAd interstitialAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);
        ButterKnife.bind(this);
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                startActivity(new Intent(AdsActivity.this, Home.class));
                finish();
            }
        });
        requestNewInterstitial();
        Glide.with(this).load(getIntent().getStringArrayExtra("adImage")).into(ivBanner);
        tvAdMessage.setText(getIntent().getStringExtra("adMessage"));
        findViewById(R.id.v_ad_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getIntent().getStringExtra("adLink")));
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (interstitialAd.isLoaded()) {
            Utils.showInterstitialAd(interstitialAd);
        } else {
            startActivity(new Intent(this, Home.class));
            finish();
        }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        interstitialAd.loadAd(adRequest);
    }

}
