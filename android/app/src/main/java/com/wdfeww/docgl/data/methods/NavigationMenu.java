package com.wdfeww.docgl.data.methods;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wdfeww.docgl.AppLogin;
import com.wdfeww.docgl.FavouriteDoctors;
import com.wdfeww.docgl.Home;
import com.wdfeww.docgl.PatientsTab;
import com.wdfeww.docgl.Profile;
import com.wdfeww.docgl.R;
import com.wdfeww.docgl.Settings;
import com.wdfeww.docgl.data.model.Patient;
import com.wdfeww.docgl.data.model.User;

/**
 * Created by wdfeww on 5/8/17.
 */

public class NavigationMenu extends Activity {
    String token;
    TextView logged_user;
    private final Context context;
    Toolbar toolbar;
    final DrawerLayout drawer_layout;
    NavigationView nav_view;
    Class className;
    private Patient patient;
    User user;

    public NavigationMenu(Context context, Toolbar toolbar, DrawerLayout drawer_layout,
                          NavigationView nav_view, Class className) {
        this.context = context;
        this.toolbar = toolbar;
        this.drawer_layout = drawer_layout;
        this.nav_view = nav_view;
        this.className = className;

    }

    public void initMenu() {

        SharedPreferences prefs = context.getSharedPreferences("com.wdfeww.docgl", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("LoggedUser", "");
        user = gson.fromJson(json, User.class);
        json = prefs.getString("LoggedPatient", "");
        patient = gson.fromJson(json, Patient.class);
        token = user.getToken();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout.setDrawerListener(toggle);
        toggle.syncState();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (drawer_layout.isDrawerOpen(Gravity.LEFT)) {
                    drawer_layout.closeDrawer(Gravity.LEFT);
                } else {
                    drawer_layout.openDrawer(Gravity.LEFT);
                }
            }
        });

        logged_user = new TextView(context);
        logged_user.setTextAppearance(context, R.style.navigationMenu_username);
        logged_user.setText(patient.getUserName());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(30, 50, 0, 0);
        logged_user.setLayoutParams(params);
        logged_user.setBackgroundColor(context.getResources().getColor(R.color.color6));
        nav_view.addHeaderView(logged_user);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        drawer_layout.closeDrawer(Gravity.LEFT);
                        if (!(className.equals(Home.class)))
                            redirect(Home.class);
                        break;
                    case R.id.nav_profile:
                        drawer_layout.closeDrawer(Gravity.LEFT);
                        redirect(Profile.class);
                        break;
                    case R.id.nav_favorites:
                        drawer_layout.closeDrawer(Gravity.LEFT);
                        if (!(className.equals(FavouriteDoctors.class)))
                            redirect(FavouriteDoctors.class);
                        break;
                    case R.id.nav_patients:
                        drawer_layout.closeDrawer(Gravity.LEFT);
                        if (!(className.equals(PatientsTab.class)))
                            redirect(PatientsTab.class);
                        break;
                    case R.id.nav_settings:
                        drawer_layout.closeDrawer(Gravity.LEFT);
                        if (!(className.equals(Settings.class)))
                            redirect(Settings.class);
                        break;
                    case R.id.nav_logout:
                        drawer_layout.closeDrawer(Gravity.LEFT);
                        logout();
                        break;

                }
                return false;
            }
        });
    }

    public void redirect(Class nameOfClass) {

        Intent intent = new Intent(context, nameOfClass);
        if (nameOfClass == Home.class)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        else
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    public void redirect() {
        Intent intent = new Intent(context, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);

    }

    private void logout() {
        Intent intent = new Intent(context, AppLogin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ((Activity) context).finish();
        context.startActivity(intent);

    }


    public void setFirstName(String firstName) {
        this.patient.setFirstName(firstName);
    }

    public void setLastName(String lastName) {
        this.patient.setLastName(lastName);
    }

    public void setEmail(String email) {
        this.patient.setEmail(email);
    }

    public void setUsername(String username) {
        this.patient.setUserName(username);
    }

}
