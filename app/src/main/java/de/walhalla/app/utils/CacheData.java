package de.walhalla.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import de.walhalla.app.App;
import de.walhalla.app.firebase.Firebase;

/**
 * @author B3tterTogeth3r
 * @since 1.0
 * @version 1.0
 * @see android.content.SharedPreferences
 */
public class CacheData {
    private static final String TAG = "CacheData";
    private static SharedPreferences SP;

    public CacheData() {
        SP = App.getContext().getSharedPreferences(TAG, Context.MODE_PRIVATE);
    }

    /**
     * Change the value of the analytics data collection
     */
    public static void ChangeAnalyticsCollection(boolean value) {
        if(value)
            SP.edit().putBoolean(Firebase.Analytics.TAG, true).apply();
        else
            SP.edit().putBoolean(Firebase.Analytics.TAG, false).apply();
    }

    public static boolean getAnalyticsCollection(){
        return SP.getBoolean(Firebase.Analytics.TAG, true);
    }
}
