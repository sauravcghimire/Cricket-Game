package com.appsinfinity.fingercricket.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by macbook on 11/25/16.
 */

public class Tournament implements Parcelable {
    int id,type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.type);
    }

    public Tournament() {
    }

    protected Tournament(Parcel in) {
        this.id = in.readInt();
        this.type = in.readInt();
    }

    public static final Parcelable.Creator<Tournament> CREATOR = new Parcelable.Creator<Tournament>() {
        @Override
        public Tournament createFromParcel(Parcel source) {
            return new Tournament(source);
        }

        @Override
        public Tournament[] newArray(int size) {
            return new Tournament[size];
        }
    };
}
