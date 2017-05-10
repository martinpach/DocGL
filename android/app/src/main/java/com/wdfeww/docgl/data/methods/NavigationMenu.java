package com.wdfeww.docgl.data.methods;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.wdfeww.docgl.AppLogin;
import com.wdfeww.docgl.Home;
import com.wdfeww.docgl.Profile;
import com.wdfeww.docgl.R;
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
    Patient patient;

    public NavigationMenu(String token,Patient patient,
                          Context context, Toolbar toolbar, DrawerLayout drawer_layout,
                          NavigationView nav_view, Class className) {
        this.token = token;
        this.patient=patient;
        this.context = context;
        this.toolbar = toolbar;
        this.drawer_layout = drawer_layout;
        this.nav_view = nav_view;
        this.className=className;

    }

    public void initMenu(){

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout.setDrawerListener(toggle);
        toggle.syncState();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (drawer_layout.isDrawerOpen(Gravity.RIGHT)) {
                    drawer_layout.closeDrawer(Gravity.RIGHT);
                } else {
                    drawer_layout.openDrawer(Gravity.RIGHT);
                }
            }
        });

        logged_user = new TextView(context);
        logged_user.setTextAppearance(context, R.style.profile_text);
        logged_user.setText(patient.getUserName());

        nav_view.addHeaderView(logged_user);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        drawer_layout.closeDrawer(Gravity.RIGHT);
                        if(!(className.equals(Home.class)))
                            redirect(Home.class);
                        break;
                    case R.id.nav_profile:
                        drawer_layout.closeDrawer(Gravity.RIGHT);
                        if(!(className.equals(Profile.class)))
                        redirect(Profile.class);
                        break;
                    case R.id.nav_favorites:
                        drawer_layout.closeDrawer(Gravity.RIGHT);
                        break;
                    case R.id.nav_patients:
                        drawer_layout.closeDrawer(Gravity.RIGHT);
                        break;
                    case R.id.nav_settings:
                        drawer_layout.closeDrawer(Gravity.RIGHT);
                        break;
                    case R.id.nav_logout:
                        drawer_layout.closeDrawer(Gravity.RIGHT);
                        logout();
                        break;

                }
                return false;
            }
        });
    }
    public void redirect(Class nameOfClass) {
        Intent intent = new Intent(context, nameOfClass);
        Bundle bundle = new Bundle();
        bundle.putParcelable("patient", patient);
        intent.putExtras(bundle);
        intent.putExtra("token", token);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    private void logout(){
        Intent intent = new Intent(context, AppLogin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ((Activity)context).finish();
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
