package com.wdfeww.docgl.data.methods;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.wdfeww.docgl.Home;
import com.wdfeww.docgl.Profile;
import com.wdfeww.docgl.R;

/**
 * Created by wdfeww on 5/8/17.
 */

public class NavigationMenu extends Activity {

    String firstName, lastName, email, token, username;
    int id;
    TextView logged_user;
    private final Context context;
    Toolbar toolbar;
    final DrawerLayout drawer_layout;
    NavigationView nav_view;
    Class className;
    public NavigationMenu(int id, String firstName, String lastName, String email, String token,
                          String username, Context context, Toolbar toolbar, DrawerLayout drawer_layout,
                          NavigationView nav_view,TextView logged_user, Class className) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.token = token;
        this.username = username;
        this.context = context;
        this.toolbar = toolbar;
        this.drawer_layout = drawer_layout;
        this.nav_view = nav_view;
        this.logged_user=logged_user;
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

        logged_user.setText(username);

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
                        break;

                }
                return false;
            }
        });
    }
    public void redirect(Class nameOfClass) {
        Intent intent = new Intent(context, nameOfClass);
        intent.putExtra("firstName", firstName);
        intent.putExtra("lastName", lastName);
        intent.putExtra("email", email);
        intent.putExtra("id", id);
        intent.putExtra("token", token);
        intent.putExtra("username", username);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(int id) {
        this.id = id;
    }
}