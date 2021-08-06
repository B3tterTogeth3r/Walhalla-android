package de.walhalla.app.fragments.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import org.jetbrains.annotations.NotNull;

import de.walhalla.app.R;
import de.walhalla.app.abstraction.CustomFragment;

/**
 * Created by B3tterTogeth3r on 27.07.2021.
 */
public class Fragment extends CustomFragment implements View.OnClickListener {
    private static final String TAG = "home.Fragment";
    /** width of the display */
    private static int width;
    /** height of the display */
    private static int height;
    /** margin of the displayed box */
    private final int boxMargin = 50;
    private int boxWidth;
    private int boxHeight;
    private RelativeLayout program, greeting, chargen, news;

    @Override
    public void start () {

    }

    @Override
    public void onResume () {
        super.onResume();
    }

    @Override
    public void createView (@NonNull @NotNull View view,
                            @NonNull @NotNull LayoutInflater inflater) {
        LinearLayout layout = view.findViewById(R.id.fragment_container);
        layout.removeAllViewsInLayout();

        RelativeLayout frame = new RelativeLayout(requireContext());
        frame.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT));

        Display display =
                ((WindowManager) requireContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        boxWidth = (int) (width / 2.5);
        boxHeight = (height / 4);

        // region Add program
        RelativeLayout.LayoutParams programParams = boxParams();
        programParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        program = new RelativeLayout(requireContext());
        program.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.border_round));
        program.setLayoutParams(programParams);
        program.setId(R.id.program);

        // Add icon
        program.addView(image(AppCompatResources.getDrawable(requireContext(),
                R.drawable.ic_home)));
        // Add description
        program.addView(text(R.string.menu_program));
        frame.addView(program);
        // endregion

        // region Add greeting
        RelativeLayout.LayoutParams greetingParams = boxParams();
        greetingParams.addRule(RelativeLayout.END_OF, R.id.program);
        greeting = new RelativeLayout(requireContext());
        greeting.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.border_round));
        greeting.setLayoutParams(greetingParams);
        greeting.setId(R.id.greeting);

        // Add icon
        greeting.addView(image(AppCompatResources.getDrawable(requireContext(),
                R.drawable.ic_greeting)));
        // Add description
        greeting.addView(text(R.string.menu_greeting));
        frame.addView(greeting);
        // endregion

        // region Add chargen
        RelativeLayout.LayoutParams chargenParams = boxParams();
        chargenParams.addRule(RelativeLayout.BELOW, R.id.program);
        chargen = new RelativeLayout(requireContext());
        chargen.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.border_round));
        chargen.setLayoutParams(chargenParams);
        chargen.setId(R.id.chargen);

        // Add icon
        chargen.addView(image(AppCompatResources.getDrawable(requireContext(),
                R.drawable.ic_group_line)));
        // Add description
        chargen.addView(text(R.string.menu_chargen));
        frame.addView(chargen);
        // endregion

        // region Add news
        RelativeLayout.LayoutParams newsParams = boxParams();
        newsParams.addRule(RelativeLayout.END_OF, R.id.chargen);
        newsParams.addRule(RelativeLayout.BELOW, R.id.program);
        news = new RelativeLayout(requireContext());
        news.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.border_round));
        news.setLayoutParams(newsParams);
        news.setId(R.id.news);

        // Add icon
        news.addView(image(AppCompatResources.getDrawable(requireContext(),
                R.drawable.ic_message)));
        // Add description
        news.addView(text(R.string.menu_messages));
        frame.addView(news);
        // endregion

        layout.addView(frame);
    }

    // region params
    @NotNull
    private RelativeLayout.LayoutParams boxParams () {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(boxWidth, boxHeight);
        params.setMargins(boxMargin, boxMargin, boxMargin, boxMargin);
        return params;
    }

    @NotNull
    private ImageView image (Drawable drawable) {
        ImageView icon = new ImageView(requireContext());
        icon.setImageDrawable(drawable);
        icon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black),
                android.graphics.PorterDuff.Mode.MULTIPLY);
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        (int) (boxHeight / 1.5));
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.setMargins(25, 0, 25, 75);
        icon.setLayoutParams(params);
        icon.setId(R.id.icon);

        return icon;
    }

    @NotNull
    private TextView text (@StringRes int resid) {
        TextView text = new TextView(requireContext());
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(Gravity.CENTER_HORIZONTAL);
        params.setMargins(5, 0, 5, 75);
        text.setLayoutParams(params);
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        text.setTextAppearance(android.R.style.TextAppearance_Material_Subhead);

        text.setText(resid);
        return text;
    }
    // endregion

    @Override
    public void viewCreated () {
        try {
            program.setOnClickListener(this);
            greeting.setOnClickListener(this);
            chargen.setOnClickListener(this);
            news.setOnClickListener(this);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void toolbarContent () {
        toolbar.getMenu().clear();
        toolbar.setTitle(R.string.app_name);
    }

    @Override
    public void stop () {

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick (@NonNull View v) {
        switch (v.getId()) {
            case R.id.program:
                Log.d(TAG, "onClick: program");
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,
                                new de.walhalla.app.fragments.program.Fragment())
                        .commit();
                break;
            case R.id.greeting:
                Log.d(TAG, "onClick: greeting");
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,
                                new de.walhalla.app.fragments.greeting.Fragment())
                        .commit();
                break;
            case R.id.chargen:
                Log.d(TAG, "onClick: chargen");
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,
                                new de.walhalla.app.fragments.chargen.Fragment())
                        .commit();
                break;
            case R.id.news:
                Log.d(TAG, "onClick: news");
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,
                                new de.walhalla.app.fragments.news.Fragment())
                        .commit();
                break;
        }
    }
}
