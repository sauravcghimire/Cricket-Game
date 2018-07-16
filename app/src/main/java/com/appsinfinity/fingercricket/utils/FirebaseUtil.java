package com.appsinfinity.fingercricket.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by sushantdevkota on 11/22/16.
 */

public class FirebaseUtil {
    public static String USER = "user";

    public static DatabaseReference getDatabaseRefWithName(String name) {
        return FirebaseDatabase.getInstance().getReference().child(name);
    }

    public static DatabaseReference getUserNodeWithId(String fbId) {
        return getDatabaseRefWithName(USER).child(fbId);
    }

}
