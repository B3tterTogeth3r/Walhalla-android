package de.walhalla.app.abstraction;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.ListenerRegistration;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.walhalla.app.R;
import de.walhalla.app.firebase.Firebase;

/**
 * This class is so that every fragment inside the app looks the same, has similar runtime and a
 * similar looking basic code for better understanding. Because every {@code fragment} inside the
 * app needs the same start method, the same variable for the {@link #toolbar}, should register all
 * its {@link #registration realtimelistener} in the same list, so they can be all stopped at {@link
 * #onStop()} this super class was created.
 *
 * @author B3tterTogeth3r
 * @version 1.0
 * @since 1.0
 */
public abstract class CustomFragment extends Fragment {
    private final String TAG = "CustomFragment";
    /**
     * A list to collect all realtime listeners into the firestore database.
     *
     * @see ListenerRegistration
     * @see Firebase.Firestore
     * @since 1.0
     */
    public ArrayList<ListenerRegistration> registration;
    /**
     * The top Toolbar of the whole application
     *
     * @see Toolbar
     */
    public Toolbar toolbar;

    /**
     * fixme describe comment better
     * <p>
     * called after new {@link #registration} got reset and the site can start
     */
    public abstract void start ();

    /**
     * @implNote Don't call in the extending classes of this {@link CustomFragment}
     * @deprecated use abstract subclass
     */
    @Override
    public void onAttach (@NonNull Context context) {
        super.onAttach(context);
    }

    /**
     * @param inflater
     *         The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container
     *         If non-null, this is the parent view that the fragment's UI should be attached to.
     *         The fragment should not add the view itself, but this can be used to generate the
     *         LayoutParams of the view.
     * @param savedInstanceState
     *         If non-null, this fragment is being re-constructed from a previous saved state as
     *         given here.
     * @return Return the View for the fragment's UI, or null.
     * @deprecated use abstract {@link #createView(View, LayoutInflater)} instead.
     */
    @Nullable
    @Override
    public View onCreateView (@NonNull @NotNull LayoutInflater inflater,
                              @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                              @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);
        createView(view, inflater);
        return view;
    }

    /**
     * @param view
     *         The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState
     *         If non-null, this fragment is being re-constructed from a previous saved state as
     *         given here.
     * @deprecated use abstract {@link #viewCreated()} instead
     */
    @Override
    public void onViewCreated (@NonNull @NotNull View view,
                               @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: running");
        try {
            toolbar = requireActivity().findViewById(R.id.toolbar);
            toolbar.findViewById(R.id.custom_title).setVisibility(View.GONE);
            toolbar.setTitle("Walhalla");
            super.onViewCreated(view, savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            viewCreated();
            toolbarContent();
        }
    }

    /**
     * @deprecated use abstract {@link #start()} instead
     */
    @Override
    public void onStart () {
        try {
            super.onStart();
            registration = new ArrayList<>();
        } finally {
            start();
        }
    }

    @Override
    public void onResume () {
        super.onResume();
        try {
            toolbarContent();
        } catch (Exception e) {
            Firebase.Crashlytics.log("Refilling toolbar at onResume did not work", e);
        }
    }

    /**
     * @deprecated use abstract {@link #stop()} instead
     */
    @Override
    public void onStop () {
        super.onStop();
        try {
            for (ListenerRegistration reg : registration) {
                reg.remove();
            }
            registration.clear();
        } catch (Exception e) {
            Firebase.Crashlytics.log("Something went wrong while removing the snapshot listener",
                    e);
        } finally {
            toolbar.getMenu().clear();
            toolbar.setTitle(R.string.app_name);
            toolbar.setSubtitle("");
            stop();
        }
    }

    /**
     * Create the view and initialize the necessary variables for the site.
     *
     * @param view
     *         inflated View created in {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * @param inflater
     *         LayoutInflater for inflating new Layouts into the view
     * @implNote <b>DON'T CALL FUNCTIONS THAT WORK WITH DATA OF {@link #start()} IN HERE</b>
     * @see #onViewCreated(View, Bundle)
     */
    public abstract void createView (@NonNull @NotNull View view,
                                     @NonNull @NotNull LayoutInflater inflater);

    /**
     * @see #onViewCreated(View, Bundle)
     */
    public abstract void viewCreated ();

    /**
     * Called before {@link #viewCreated() viewCreated} returns a result. This is to format the
     * toolbar in every Subclass the same way.
     *
     * @see #toolbar
     */
    public abstract void toolbarContent ();

    /**
     * Called when the Fragment is no longer started.  This is generally tied to {@link #onStop()
     * onStop} of the containing Activity's lifecycle.
     *
     * @implNote Called after every entry in {@link #registration} got stopped and the list
     * cleared.
     * @see #onStop()
     */
    public abstract void stop ();


}
