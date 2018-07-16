package com.appsinfinity.fingercricket.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Button;

import com.appsinfinity.fingercricket.R;
import com.appsinfinity.fingercricket.models.Player;
import com.appsinfinity.fingercricket.models.Team;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Saurav on 3/29/2015.
 */
public class Utils {

    public static final int INTERNET_TYPE_ALL = 0;
    public static final int INTERNET_TYPE_WIFI = 1;
    public static final int INTERNET_TYPE_MOBILE = 2;
    public static final String TEXT_ALERT_INTERNET_CONNECTION_UNAVAILABLE = "Internet not available";

    public static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    public static boolean isOperationSuccess(String jsonResult) {

        boolean isSuccess = false;
        JSONObject json;

        try {
            json = new JSONObject(jsonResult.trim());
            if (json.has("status")) {
                String value = json.getString("status");

                if (value.equalsIgnoreCase("success")) {
                    isSuccess = true;
                }
            }

        } catch (JSONException e) {
            isSuccess = false;
        } catch (NullPointerException exception) {
            isSuccess = false;
        }
        return isSuccess;
    }

    public static boolean isInternetConnectionAvailable(Context context, int internetType) {
        boolean isAvailable = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            switch (internetType) {
                case 0:
                    if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI || networkInfo
                            .getType() == ConnectivityManager.TYPE_MOBILE) {
                        isAvailable = true;
                    }
                    break;
                case 1:
                    if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        isAvailable = true;
                    }
                case 2:
                    if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        isAvailable = true;
                    }
                    break;
                default:
            }
        }
        return isAvailable;
    }

    public static void showAlert(String noticeText, Context context) {
        AlertDialog.Builder noticeAlertBuilder = new AlertDialog.Builder(context);
        noticeAlertBuilder.setMessage(noticeText);
        noticeAlertBuilder.setCancelable(false);
        noticeAlertBuilder.setNegativeButton("OK",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                    }
                }
        );

        AlertDialog dialog = noticeAlertBuilder.create();
        dialog.show();

        setDialogSelector(dialog);
    }

    public static void setDialogSelector(AlertDialog dialog) {
        Button negButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        if (negButton != null) {
        }
        Button posButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if (posButton != null) {
        }
    }

    public static Team getTeam(Context context, int tournamentType, int position) {
        Team team = new Team();
        String teamName;
        if (tournamentType == AppConstants.INTL)
            teamName = context.getResources().getStringArray(R.array.teams)[position];
        else
            teamName = context.getResources().getStringArray(R.array.teams_ipl)[position].replace(" ", "");
        int playerArrayId = context.getResources().getIdentifier(teamName, "array", context.getPackageName());
        team.setName(teamName);
        team.setPlayers(getPlayers(context, playerArrayId));
        return team;
    }

    private static ArrayList<Player> getPlayers(Context context, int playerArrayId) {
        ArrayList<Player> players = new ArrayList<>();
        String[] playersNameArray = context.getResources().getStringArray(playerArrayId);
        for (int i = 0; i < playersNameArray.length; i++) {
            Player player = new Player();
            player.setName(playersNameArray[i]);
            player.setId(i);
            players.add(player);
        }
        return players;
    }

    public static void showInterstitialAd(InterstitialAd interstitialAd) {
        Answers.getInstance().logCustom(new CustomEvent("Interstitial Ad Shown"));
        interstitialAd.show();
    }
}
