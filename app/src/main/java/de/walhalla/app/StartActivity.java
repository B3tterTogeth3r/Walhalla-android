package de.walhalla.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This Activity is the loading screen of the app. Every needed data is being loaded and the user is
 * displayed a progressbar. If an error occurred, the user gets an alert dialog with that error.
 * Also most variables, that could change over time are created here.
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @see AppCompatActivity
 * @since 1.0
 */
public class StartActivity extends AppCompatActivity {
    private static final String TAG = "StartActivity";

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateProgressbar();
    }

    private void updateProgressbar(){

        /* Go to MainActivity */
        Intent mainIntent = new Intent(StartActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
