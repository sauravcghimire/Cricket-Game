package com.appsinfinity.fingercricket.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by macbook on 11/25/16.
 */

public class Team implements Parcelable {
    int id, run;
    String name,subString;
    ArrayList<Player> players;

    public String getSubString() {
        return subString;
    }

    public int getRun() {
        return run;
    }

    public void setRun(int run) {
        this.run = run;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.subString = name.substring(0,3);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public Team() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.run);
        dest.writeString(this.name);
        dest.writeString(this.subString);
        dest.writeList(this.players);
    }

    protected Team(Parcel in) {
        this.id = in.readInt();
        this.run = in.readInt();
        this.name = in.readString();
        this.subString = in.readString();
        this.players = new ArrayList<Player>();
        in.readList(this.players, Player.class.getClassLoader());
    }

    public static final Creator<Team> CREATOR = new Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel source) {
            return new Team(source);
        }

        @Override
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };
}
