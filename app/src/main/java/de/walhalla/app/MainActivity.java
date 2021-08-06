package de.walhalla.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import de.walhalla.app.firebase.Firebase;
import de.walhalla.app.utils.Variables;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    @SuppressLint("StaticFieldLeak")
    public static View parentLayout;
    private DrawerLayout drawerlayout;
    private NavigationView navigationView;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // [START Set the default ui]
        // Set content
        setContentView(R.layout.activity_main);

        //For easier access to this view for Toast and SnackBar messages
        parentLayout = findViewById(android.R.id.content);

        //Initialize Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        App.setToolbar(toolbar);

        drawerlayout = findViewById(R.id.drawer_layout);

        //The left site navigation controller
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        fillSideNav();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        // [END Set the default ui]

        // [START Open start fragment]
        if (savedInstanceState == null) {
            // [DEFAULT]
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new de.walhalla.app.fragments.home.Fragment()).commit();
        }
        // [START Open start fragment]
    }

    @Override
    public boolean onNavigationItemSelected (@NonNull @NotNull MenuItem item) {
        Firebase.Analytics.screenChange(item.getItemId(), "home");
        return false;
    }


    /**
     * Filling the left side nav with data
     */
    private void fillSideNav () {
        View view = navigationView.getHeaderView(0);
        ImageView image = view.findViewById(R.id.nav_headder);
        TextView title = view.findViewById(R.id.nav_title);
        TextView street = view.findViewById(R.id.nav_street);
        TextView city = view.findViewById(R.id.nav_city);
        image.setImageResource(R.drawable.wappen_2017);
        title.setText(Variables.Walhalla.NAME);
        street.setText(Variables.Walhalla.ADH_ADDRESS);
        city.setVisibility(View.GONE);

        //Navigation Body
        navigationView.getMenu().clear();
        Menu menu = navigationView.getMenu();
        //Public area
        menu.add(0, R.string.menu_home, 0, R.string.menu_home)
                //.setChecked(true)
                .setIcon(R.drawable.ic_home);
        menu.add(0, R.string.menu_about_us, 0, R.string.menu_about_us).setIcon(R.drawable.ic_info);
        menu.add(0, R.string.menu_rooms, 0, R.string.menu_rooms).setIcon(R.drawable.ic_rooms);
        menu.add(0, R.string.menu_program, 0, R.string.menu_program).setIcon(R.drawable.ic_calendar);
        menu.add(0, R.string.menu_messages, 0, R.string.menu_messages).setIcon(R.drawable.ic_message);
        menu.add(0, R.string.menu_chargen, 0, R.string.menu_chargen).setIcon(R.drawable.ic_group);
        menu.add(0, R.string.menu_chargen_phil, 0, R.string.menu_chargen_phil).setIcon(R.drawable.ic_group_line);

        /* Login/Sign up, Logout */
        Menu loginMenu = menu.addSubMenu(R.string.menu_user_editing);
        if (false) {
            loginMenu.add(1, R.string.menu_logout, 0, R.string.menu_logout).setIcon(R.drawable.ic_exit).setCheckable(false);
            loginMenu.add(0, R.string.menu_profile, 0, R.string.menu_profile).setIcon(R.drawable.ic_person);

            loginMenu.add(0, R.string.menu_drinks, 0, R.string.menu_drinks) //Change appearance
                    // depending on who is logged in
                    .setIcon(R.drawable.ic_beer);
            loginMenu.add(0, R.string.menu_balance, 0, R.string.menu_balance);

            //Only visible to members of the fraternity
            Menu menuLogin = menu.addSubMenu(R.string.menu_intern);
            menuLogin.add(0, R.string.menu_transcript, 0, R.string.menu_transcript).setIcon(R.drawable.ic_scriptor);
            menuLogin.add(0, R.string.menu_kartei, 0, R.string.menu_kartei).setIcon(R.drawable.ic_contacts);

            //Only visible to a active board member of the current semester
            //if (User.hasCharge()) {
            Menu menuCharge = menu.addSubMenu(R.string.menu_board_only);
            menuCharge.add(0, R.string.menu_new_person, 0, R.string.menu_new_person).setIcon(R.drawable.ic_person_add);
                /*menuCharge.add(0, R.string.menu_user, 0, R.string.menu_user)
                        .setIcon(R.drawable.ic_user_add);
                /*menuCharge.add(0, R.string.menu_account, 0, R.string.menu_account)
                        .setIcon(R.drawable.ic_account);*/
            menuCharge.add(0, R.string.menu_new_semester, 0, R.string.menu_new_semester);
            //}
        } else {
            loginMenu.add(1, R.string.menu_login, 0, R.string.menu_login)
                    ///.setCheckable(false)
                    .setIcon(R.drawable.ic_exit);
        }

        loginMenu.setGroupCheckable(1, false, true);

        Menu moreMenu = menu.addSubMenu(R.string.menu_more);
        moreMenu.add(1, R.string.menu_more_history, 1, R.string.menu_more_history);
        moreMenu.add(1, R.string.menu_more_frat_wue, 1, R.string.menu_more_frat_wue);
        moreMenu.add(1, R.string.menu_more_frat_organisation, 1,
                R.string.menu_more_frat_organisation);

        Menu menuEnd = menu.addSubMenu(R.string.menu_other);
        menuEnd.add(0, R.string.menu_settings, 0, R.string.menu_settings).setIcon(R.drawable.ic_settings).setCheckable(false);
        menuEnd.add(0, R.string.menu_donate, 0, R.string.menu_donate).setCheckable(false).setIcon(R.drawable.ic_donate);

        navigationView.invalidate();
    }

    /**
     * If the user presses the back button, the side menu is going to be opened. If the menu is
     * already open, the user can press the button again to close the app.
     */
    @Override
    public void onBackPressed () {
        //If drawer is open, show possibility to close the app via the back-button.
        if (drawerlayout.isDrawerOpen(GravityCompat.START)) {
            if (doubleBackToExitPressedOnce) {
                //Button pressed a second time within half a second.
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.exit_app_via_back, Toast.LENGTH_LONG).show();

            // TODO change this function to a non depicted one
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 500);
        } else { //Otherwise open the left menu
            drawerlayout.openDrawer(GravityCompat.START);
        }
    }
}