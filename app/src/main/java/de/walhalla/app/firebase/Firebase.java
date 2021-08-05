package de.walhalla.app.firebase;

import static com.google.firebase.analytics.FirebaseAnalytics.UserProperty.ALLOW_AD_PERSONALIZATION_SIGNALS;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

import de.walhalla.app.App;
import de.walhalla.app.MainActivity;
import de.walhalla.app.SplashActivity;
import de.walhalla.app.utils.CacheData;

/**
 * A collection of all the uses Firebase services and the functions used in this android app.
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @see <a href="https://firebase.google.com/docs/crashlytics">Crashlytics</a>
 * @see <a href="https://firebase.google.com/docs/auth/android/password-auth">Authentication</a>
 * @see <a href="https://firebase.google.com/docs/analytics">Analytics</a>
 * @see <a href="https://firebase.google.com/docs/database/android/start">Realtime Database</a>
 * @see <a href="https://firebase.google.com/docs/firestore/quickstart">Cloud Firestore</a>
 * @see <a href="https://firebase.google.com/docs/remote-config/get-started?platform=android">Remote
 * Config</a>
 * @see <a href="https://firebase.google.com/docs/storage">Storage</a>
 * @since 1.0
 */
@SuppressLint("StaticFieldLeak")
public class Firebase {
    private static final String NOT_SIGNED_IN = "not signed in";
    private static FirebaseCrashlytics CRASHLYTICS;
    private static FirebaseAuth AUTH;
    private static FirebaseAnalytics ANALYTICS;
    private static FirebaseDatabase REALTIME_DB;
    private static FirebaseFirestore FIRESTORE;
    private static FirebaseStorage STORAGE;
    private static FirebaseRemoteConfig REMOTE_CONFIG;

    public Firebase() {
        ANALYTICS = FirebaseAnalytics.getInstance(App.getContext());
        AUTH = FirebaseAuth.getInstance();
        CRASHLYTICS = FirebaseCrashlytics.getInstance();
        REALTIME_DB = FirebaseDatabase.getInstance("https://walhalla-adfc5-default-rtdb.europe-west1.firebasedatabase.app/");
        REALTIME_DB.setPersistenceEnabled(true);
        FIRESTORE = FirebaseFirestore.getInstance();
        REMOTE_CONFIG = FirebaseRemoteConfig.getInstance();
        REMOTE_CONFIG.fetchAndActivate();
        STORAGE = FirebaseStorage.getInstance("gs://walhalla-adfc5.appspot.com");

        if (AUTH.getCurrentUser() == null) {
            // if no user is signed in, write "not signed in"
            CRASHLYTICS.setUserId(NOT_SIGNED_IN);
            ANALYTICS.setUserId(NOT_SIGNED_IN);
        } else {
            // Sets the user id to the crash values.
            String uid = AUTH.getCurrentUser().getUid();
            CRASHLYTICS.setUserId(uid);
            ANALYTICS.setUserId(uid);
            REALTIME_DB.getReference("/online_users/" + uid).setValue(true);
            REALTIME_DB.getReference("/online_users/" + uid).onDisconnect().setValue(false);
            Realtime.internet(uid);
        }
        try {
            SplashActivity.newDone.firebaseDone();
        } catch (Exception ignored) {
        }
    }

    /**
     * Sets value of Person/{userID}/online
     *
     * @param uid
     *         {userID}
     * @param status
     *         true or false
     */
    private static void onlineStatus(String uid, boolean status) {
        FIRESTORE.collection("Person").document(uid).update("online", status);
    }

    // region Firebase Remote Config

    /**
     * @see <a href="https://firebase.google.com/docs/remote-config/get-started?platform=android">Remote
     * Config</a>
     */
    public static class RemoteConfig {
        public static void apply() {
            REMOTE_CONFIG.fetchAndActivate();
        }

        public static void update() {
            REMOTE_CONFIG.fetchAndActivate();
        }
    }
    // endregion

    // region Firebase Cloud Storage

    /**
     * local functions for the apps Firebase storage buckets.
     *
     * @see <a href="https://firebase.google.com/docs/storage">Storage</a>
     */
    public static class Storage {
        private static final String TAG = "Storage";
        /** Reference to the root path */
        private static final StorageReference REFERENCE = STORAGE.getReference();
        private static final long ONE_MEGABYTE = 1024 * 1024;

        // region private functions
        @NonNull
        private static byte[] compressImage(@NonNull Bitmap original) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            original.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            return baos.toByteArray();
        }

        @NonNull
        private static String formatName(@NonNull String name) {
            return name;
        }
        // endregion

        /**
         * Upload an image to the {@link Storage Firebase Storage Bucket} of the app. After that
         * update the files metadata to contain the name and uid of the uploading user and the
         * semester it was uploaded in (just the ID is enough). All of that, including the metadata
         * has to be saved also in {@link Firestore} in the image container.
         *
         * @param imageBitmap
         *         Bitmap to upload
         * @param name
         *         name of the file to upload
         */
        public static void uploadImage(Bitmap imageBitmap, String name) {
            String imageName = formatName(name);
            REFERENCE.child("image/" + imageName).putBytes(compressImage(imageBitmap))
                    .addOnSuccessListener(taskSnapshot -> Crashlytics.log(TAG + ":onSuccess: upload of image " + imageName + "complete."))
                    .addOnFailureListener(e ->
                            Crashlytics.log(
                                    TAG + ":onFailure: upload of image " + imageName + " failed.",
                                    e));
        }

        /**
         * Download an image from the {@link Storage Image Storage Bukket}
         *
         * @param image_name
         *         the name of the image to download
         * @return Bitmap of the image
         */
        public static Bitmap downloadImage(String image_name) {
            final Bitmap[] result = {null};
            REFERENCE.child("images" + image_name)
                    .getBytes(ONE_MEGABYTE)
                    .addOnSuccessListener(bytes -> {
                        // Data for "images/island.jpg" is returns, use this as needed
                        result[0] = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    })
                    .addOnFailureListener(exception -> {
                        // Handle any errors
                        Crashlytics.log(TAG + ":downloadImage:onFailure: download failed", exception);
                    });

            while (result[0] == null) {
                // wait
            }
            return result[0];
        }

        /**
         * Upload an image into the recipe bucket of the firebase storage.
         *
         * @param recipeBitmap
         *         Bitmap of the file
         * @param name
         *         name of the file to upload
         */
        public static void uploadRecipe(Bitmap recipeBitmap, String name) {
            String imageName = formatName(name);
            REFERENCE.child("recipe/" + imageName).putBytes(compressImage(recipeBitmap))
                    .addOnSuccessListener(taskSnapshot -> Crashlytics.log(TAG + ":onSuccess: upload of image " + imageName + "complete."))
                    .addOnFailureListener(e ->
                            Crashlytics.log(
                                    TAG + ":onFailure: upload of image " + imageName + " failed.",
                                    e));
        }

        /**
         * Download the requested recipe
         *
         * @param recipe_name
         *         the name of the recipe to download
         * @return bitmap value of the recipe
         */
        public static Bitmap downloadRecipe(String recipe_name) {
            final Bitmap[] result = {null};
            REFERENCE.child("images" + recipe_name)
                    .getBytes(ONE_MEGABYTE)
                    .addOnSuccessListener(bytes -> {
                        // Data for "images/island.jpg" is returns, use this as needed
                        result[0] = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    })
                    .addOnFailureListener(exception -> {
                        // Handle any errors
                        Crashlytics.log(TAG + ":downloadImage:onFailure: download failed", exception);
                    });

            while (result[0] == null) {
                // wait
            }
            return result[0];
        }

        /**
         * Upload the protocol file of a specific meeting
         */
        public static void uploadProtocol() {
            // TODO: upload protocol of a meeting only as a pdf file
        }

        /**
         * download the protocol file of a specific meeting.
         *
         * @param protocol_name
         *         name of the file
         */
        public static void downloadProtocol(String protocol_name) {
            // TODO: download protocol of a meeting, if it has a pdf file.
        }
    }
    // endregion

    // region Firebase Cloud Firestore

    /**
     * @see <a href="https://firebase.google.com/docs/firestore/quickstart">Cloud Firestore</a>
     */
    public static class Firestore {

    }
    // endregion

    //region Firebase Realtime Database

    /**
     * @see <a href="https://firebase.google.com/docs/database/android/start">Realtime Database</a>
     */
    public static class Realtime {
        private static final String TAG = "Realtime";

        /**
         * Sets a {@link App#isInternetConnection() boolean} for the internet connection of the
         * app.
         *
         * @see <a href="https://firebase.google.com/docs/database/android/offline-capabilities#section-connection-state">Connection
         * State</a>
         * @since 1.0
         */
        public static void internet(String uid) {
            REALTIME_DB.getReference("/checker/internet").onDisconnect().setValue("true", (error, ref) -> {
                // set value of Person/{userID}/online to false
                Firebase.onlineStatus(uid, false);
            });

            DatabaseReference connectedRef = REALTIME_DB.getReference(".info/connected");
            connectedRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean connected = false;
                    try {
                        //noinspection ConstantConditions
                        connected = snapshot.getValue(Boolean.class);
                    } catch (Exception e) {
                        Log.d(TAG, "not connected");
                        App.setInternetConnection(false);
                    }
                    if (connected) {
                        Log.d(TAG, "connected");
                        App.setInternetConnection(true);
                        // set value of Person/{userID}/online to true
                        Firebase.onlineStatus(uid, true);
                    } else {
                        Log.d(TAG, "not connected");
                        App.setInternetConnection(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w(TAG, "Listener was cancelled");
                }
            });
        }
    }
    // endregion

    // region Google Analytics via Firebase

    /**
     * @see <a href="https://firebase.google.com/docs/analytics">Analytics</a>
     */
    public static class Analytics {
        public static final String TAG = "Analytics";

        /**
         * Log the used sites of the user to see which sites are used more often and which are not
         * to enable better usage of ads on the pages.
         *
         * @param menu_id
         *         id of the clicked menu item
         * @param fragment_name
         *         name of the fragment the user opened
         */
        public static void screenChange(int menu_id, String fragment_name) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, String.valueOf(menu_id));
            bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "fragment." + fragment_name);
            ANALYTICS.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
        }

        /**
         * @see <a href="https://firebase.google.com/docs/analytics/configure-data-collection?platform=android">Data
         * Collection</a>
         */
        public static void ChangeDataCollection() {
            boolean value = CacheData.getAnalyticsCollection();
            CacheData.ChangeAnalyticsCollection(value);
            ANALYTICS.setUserProperty(ALLOW_AD_PERSONALIZATION_SIGNALS, "" + value);
            ANALYTICS.setAnalyticsCollectionEnabled(value);
        }
    }
    // endregion

    // region Google Crashlytics via Firebase

    /**
     * @see <a href="https://firebase.google.com/docs/crashlytics">Firebase Crashlytics</a>
     */
    public static class Crashlytics {
        private static final String TAG = "Firebase.Crashlytics";

        public static void log(String message) {
            Log.e(TAG, "recordException: " + message);
            CRASHLYTICS.log(message);
        }

        public static void log(String message, Throwable e) {
            Log.e(TAG, "recordException: " + message, e);
            CRASHLYTICS.log(message);
            CRASHLYTICS.recordException(e);
        }
    }
    // endregion

    // region Google Authentication via Firebase (Email-Password)

    /**
     * @see <a href="">Firebase Authentication</a>
     */
    public static class Auth {
        private static final String TAG = "Auth";
        // TODO Add Firebase Email-Link auth for android and web apps

        /**
         * @param email
         *         email of the user
         * @param password
         *         password of the user
         * @see <a href="https://firebase.google.com/docs/auth/android/password-auth">Authentication
         * for Android</a>
         */
        public static void createUser(String email, String password) {
            AUTH.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUser: onComplete: success");
                            // TODO Fire custom listener and set value of Person/{userID}/online to true
                        } else {
                            Log.d(TAG, "createUser: onComplete: failure", task.getException());
                            Crashlytics.log(TAG + "createUser: onComplete: failure", task.getException());
                            Snackbar.make(MainActivity.parentLayout, "Authentication failed", Snackbar.LENGTH_SHORT).show();
                            // TODO Fire custom listener with "null" value
                        }
                    });
        }

        /**
         * @param email
         *         email of the user
         * @param password
         *         password of the user
         * @see <a href="https://firebase.google.com/docs/auth/android/password-auth">Authentication
         * for Android</a>
         */
        public static void signIn(String email, String password) {
            AUTH.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                            task -> {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "createUser: onComplete: success");
                                    // TODO Fire custom listener and set value of Person/{userID}/online to true
                                } else {
                                    Log.d(TAG, "createUser: onComplete: failure", task.getException());
                                    Crashlytics.log(TAG + "createUser: onComplete: failure", task.getException());
                                    Snackbar.make(MainActivity.parentLayout, "Authentication failed", Snackbar.LENGTH_SHORT).show();
                                    // TODO Fire custom listener with "null" value
                                }
                            });
        }

        /**
         * @see <a href="https://firebase.google.com/docs/auth/android/password-auth">Authentication
         * for Android</a>
         */
        public static void signOut() {
            Firebase.onlineStatus(AUTH.getUid(), false);
            AUTH.signOut();
        }
    }
    // endregion
}
