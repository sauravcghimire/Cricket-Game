package com.appsinfinity.fingercricket;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.appsinfinity.fingercricket.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by eeposit on 12/1/16.
 */

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().hasExtra("adLink")) {
            Intent intent = new Intent(this, AdsActivity.class);
            intent.putExtra("adLink", getIntent().getStringExtra("adLink"));
            intent.putExtra("adImage", getIntent().getStringExtra("adImage"));
            intent.putExtra("adMessage", getIntent().getStringExtra("adMessage"));
            startActivity(intent);
            finish();
            return;
        }
        if (getIntent().hasExtra("news")) {
            NewsActivity.statNewsActivity(this);
            finish();
            return;
        }
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Home.startHome(SplashActivity.this);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
