package de.walhalla.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.appcompat.widget.Toolbar;

/**
 * Created by B3tterTogeth3r on 27.07.2021.
 */
@SuppressLint("StaticFieldLeak")
public class App extends Application {
    private static final String TAG = "App";
    private static Context ctx;
    private static Toolbar toolbar;
    private static boolean internetConnection = false;

    public App() {
        try {
            SplashActivity.newDone.appDone();
        } catch (Exception ignored) {
        }
    }

    public static Context getContext() {
        return ctx;
    }

    public static Toolbar getToolbar() {
        return toolbar;
    }

    public static void setToolbar(Toolbar toolbar) {
        App.toolbar = toolbar;
    }

    /**
     * @return true, if the app has an active internet connection
     */
    public static boolean isInternetConnection() {
        return internetConnection;
    }

    /**
     * @param internetConnection
     *         true, if the app has an active internet connection
     */
    public static void setInternetConnection(boolean internetConnection) {
        App.internetConnection = internetConnection;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        App.ctx = this;
    }
}
