package com.appsinfinity.fingercricket.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by macbook on 11/25/16.
 */

public class GamePreference {
    private static final String IS_INSTRUCTION_SEEN = "isInstructionSeen";
    private static final String IS_INTL_TEAM_SELECTED = "isIntlTeamSelected";
    private static final String IS_IPL_TEAM_SELECTED = "isIplTeamSelected";
    private static final String IS_USER_SAVED = "isUserSaved";
    private static final String SELECTED_INTL_TEAM = "selectedIntlTeam";
    private static final String SELECTED_IPL_TEAM = "selectedIplTeam";
    private static final String GAME_COUNT = "game_count";

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public GamePreference(Context context) {
        this.context = context;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.editor = sharedPreferences.edit();
    }

    public void setIsInstructionSeen(boolean isInstructionSeen) {
        editor.putBoolean(IS_INSTRUCTION_SEEN, isInstructionSeen);
        editor.apply();
    }

    public boolean isInstructionSeen() {
        return sharedPreferences.getBoolean(IS_INSTRUCTION_SEEN, false);
    }

    public void setIsUserSaved(boolean isUserSaved) {
        editor.putBoolean(IS_USER_SAVED, isUserSaved);
        editor.apply();
    }

    public boolean isUserSaved() {
        return sharedPreferences.getBoolean(IS_USER_SAVED, false);
    }

    public void setIsIntlTeamSelected(boolean isIntlTeamSelected) {
        editor.putBoolean(IS_INTL_TEAM_SELECTED, isIntlTeamSelected);
        editor.apply();
    }

    public boolean isIntlTeamSelected() {
        return sharedPreferences.getBoolean(IS_INTL_TEAM_SELECTED, false);
    }

    public void setIsIplTeamSelected(boolean isIplTeamSelected) {
        editor.putBoolean(IS_IPL_TEAM_SELECTED, isIplTeamSelected);
        editor.apply();
    }

    public boolean isIplTeamSelected() {
        return sharedPreferences.getBoolean(IS_IPL_TEAM_SELECTED, false);
    }

    public void setSelectedIplTeam(int selectedIplTeam){
        editor.putInt(SELECTED_IPL_TEAM,selectedIplTeam);
        editor.apply();
    }

    public int getSelectedIplTeam(){
        return sharedPreferences.getInt(SELECTED_IPL_TEAM,0);
    }

    public void setGameCount(int gameCount) {
        editor.putInt(GAME_COUNT, gameCount);
        editor.apply();
    }

    public int getGameCount() {
        return sharedPreferences.getInt(GAME_COUNT, 0);
    }

    public void setSelectedIntllTeam(int selectedIntlTeam) {
        editor.putInt(SELECTED_INTL_TEAM, selectedIntlTeam);
        editor.apply();
    }

    public int getSelectedIntlTeam() {
        return sharedPreferences.getInt(SELECTED_INTL_TEAM, 0);
    }
}
