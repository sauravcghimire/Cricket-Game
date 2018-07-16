package com.appsinfinity.fingercricket;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by macbook on 2/12/17.
 */

public class FCMTOkenService extends FirebaseInstanceIdService {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("sasa", "Refreshed token: " + refreshedToken);
    }
}
