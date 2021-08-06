package de.walhalla.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import de.walhalla.app.firebase.Firebase;
import de.walhalla.app.interfaces.SplashInterface;


/**
 * This activity is just to remove the white loading screen and wait until all the necessary values
 * and methods have been loaded before moving on to {@link StartActivity}.
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @see AppCompatActivity
 * @since 1.0
 */
public class SplashActivity extends AppCompatActivity implements SplashInterface {
    private static final String TAG = "SplashActivity";
    public static SplashInterface newDone;

    @Override
    protected void onCreate (@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newDone = this;
        new App();
    }

    /**
     * @see <a href="https://firebase.google.com/docs/dynamic-links/android/receive">Firebase
     * Dynamic Links</a>
     */
    private void dynamicLink () {
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).addOnSuccessListener(pendingDynamicLinkData -> {
            try {
                Uri deepLink = pendingDynamicLinkData.getLink();
                if (deepLink != null) {
                    // TODO Manage dynamic link
                    Firebase.Crashlytics.log("Deep link found. Value is: " + deepLink);
                }
            } catch (Exception ignored) {
            }
        }).addOnFailureListener(this, e -> Firebase.Crashlytics.log(TAG + ":getDynamicLink" +
                ":onFailure", e));
    }

    void goOn () {
        // Go to start Activity after fetching dynamic links and intents from push messages
        Intent startIntent = new Intent(this, MainActivity.class);
        startActivity(startIntent);
        finish();
    }

    @Override
    public void appDone () {
        new Firebase(getApplicationContext());
    }

    @Override
    public void firebaseDone () {
        // fetch dynamic links and push message intents
        dynamicLink();
        goOn();
    }
}
