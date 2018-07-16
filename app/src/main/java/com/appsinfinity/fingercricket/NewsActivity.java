package com.appsinfinity.fingercricket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.appsinfinity.fingercricket.adapter.NewsAdapter;
import com.appsinfinity.fingercricket.controller.RssFeedTaskController;
import com.appsinfinity.fingercricket.interfaces.ControllerInterface;
import com.appsinfinity.fingercricket.interfaces.NewsSelection;
import com.appsinfinity.fingercricket.models.NewsDto;
import com.appsinfinity.fingercricket.utils.Utils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by macbook on 2/11/17.
 */

public class NewsActivity extends AppCompatActivity implements ControllerInterface<List<NewsDto>>, NewsSelection {

    @BindView(R.id.rv_news)
    RecyclerView list;
    @BindView(R.id.progressBar)
    public ProgressBar progressBar;

    LinearLayoutManager linearLayoutManager;
    InterstitialAd interstitialAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                startActivity(new Intent(NewsActivity.this, Home.class));
                finish();
            }
        });
        requestNewInterstitial();
        linearLayoutManager = new LinearLayoutManager(this);
        populateNews();
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    private void populateNews() {
        new RssFeedTaskController(this).execute();
    }

    @Override
    public void onTaskStarted() {

    }

    @Override
    public void onTaskFinished(List<NewsDto> newsDtos) {
        progressBar.setVisibility(View.GONE);
        NewsAdapter newsAdapter = new NewsAdapter(this, newsDtos);
        list.setLayoutManager(linearLayoutManager);
        list.setAdapter(newsAdapter);
    }

    public static void statNewsActivity(Context context) {
        context.startActivity(new Intent(context, NewsActivity.class));
        ((AppCompatActivity) context).finish();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        interstitialAd.loadAd(adRequest);
    }

    @Override
    public void onNewsSelected(NewsDto newsDto) {
        if (interstitialAd.isLoaded()) {
            Utils.showInterstitialAd(interstitialAd);
        }
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", newsDto.getLink());
        startActivity(intent);
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
}
