package de.walhalla.app.fragments.news;

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
    private static final String TAG = "news.Fragment";

    @Override
    public void start () {

    }

    @Override
    public void createView (@NonNull @NotNull View view,
                            @NonNull @NotNull LayoutInflater inflater) {
        LinearLayout layout = view.findViewById(R.id.fragment_container);
        TextView title = new TextView(requireContext());
        title.setText(R.string.menu_messages);
        layout.addView(title);
    }

    @Override
    public void viewCreated () {

    }

    @Override
    public void toolbarContent () {
        toolbar.setTitle(R.string.menu_messages);
    }

    @Override
    public void stop () {

    }
}
