package com.teampenguin.apps.notenote.Application;

import android.app.Application;
import android.util.Log;

import com.teampenguin.apps.notenote.Utils.SharedPreferencesHelper;

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesHelper.init(this);
        Log.d(TAG, "onCreate: yeah");
    }
}
