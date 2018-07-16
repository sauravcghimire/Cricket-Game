package com.appsinfinity.fingercricket;

import android.content.Intent;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.MobileAds;

import io.fabric.sdk.android.Fabric;

/**
 * Created by macbook on 8/19/16.
 */
public class StickCricket extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-1760111108410027~1245209591");
        Fabric.with(this, new Crashlytics());
        startService(new Intent(this,FCMTOkenService.class));
    }
}
