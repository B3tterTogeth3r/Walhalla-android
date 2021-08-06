package de.walhalla.app.fragments.program;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import de.walhalla.app.R;
import de.walhalla.app.abstraction.CustomFragment;

/**
 * In this fragment the program of the selected semester. By default the current semester gets
 * displayed. The user can change the selected semester by tapping on the top toolbar.
 *
 * @author B3tterToegth3r
 */
public class Fragment extends CustomFragment {
    private static final String TAG = "program.Fragment";

    @Override
    public void start () {

    }

    @Override
    public void createView (@NonNull @NotNull View view,
                            @NonNull @NotNull LayoutInflater inflater) {
        LinearLayout layout = view.findViewById(R.id.fragment_container);
        TextView title = new TextView(requireContext());
        title.setText(R.string.menu_program);
        layout.addView(title);
    }

    @Override
    public void viewCreated () {

    }

    @Override
    public void toolbarContent () {
        toolbar.setTitle(R.string.menu_program);
    }

    @Override
    public void stop () {

    }
}
