package de.walhalla.app.fragments.home;

import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import de.walhalla.app.R;
import de.walhalla.app.abstraction.CustomFragment;

/**
 * Created by B3tterTogeth3r on 27.07.2021.
 */
public class Fragment extends CustomFragment {
    private static final String TAG = "home.Fragment";
    /** width of the display */
    private static int width;
    /** height of the display */
    private static int height;
    /** default linear layout */
    private LinearLayout layout;

    @Override
    public void start() {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void createView(@NonNull @NotNull View view, @NonNull @NotNull LayoutInflater inflater) {
        layout = view.findViewById(R.id.fragment_container);

        Display display = ((WindowManager) requireContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();

        RelativeLayout program = new RelativeLayout(requireContext());
    }

    @Override
    public void viewCreated() {
        Log.d(TAG, "viewCreated: width: "+ width + ", height: " + height);
    }

    @Override
    public void toolbarContent() {

    }

    @Override
    public void stop() {

    }
}
