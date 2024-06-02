package com.ntu.moulsocial;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Firebase persistence
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        // Initialize Firebase Authentication
        FirebaseApp.initializeApp(this);
    }
}

