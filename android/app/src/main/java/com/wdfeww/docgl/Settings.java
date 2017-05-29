package com.wdfeww.docgl;

import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.wdfeww.docgl.data.SQLiteDatabase.MyDBHandler;
import com.wdfeww.docgl.data.methods.NavigationMenu;
import com.wdfeww.docgl.data.model.Patient;


public class Settings extends AppCompatActivity {
    Toolbar toolbar;
    DrawerLayout drawer_layout;
    NavigationView nav_view;
    String token;
    Class className;
    Patient patient;
    NavigationMenu navigationMenu;
    LinearLayout main_layout;
    MyDBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = this.getIntent().getExtras();
        patient = bundle.getParcelable("patient");
        token = getIntent().getStringExtra("token");

        className = getClass();
        main_layout = (LinearLayout) findViewById(R.id.main_layout);
        nav_view = (NavigationView) findViewById(R.id.nav_view);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationMenu = new NavigationMenu(token, patient, this, toolbar, drawer_layout, nav_view, className);
        navigationMenu.initMenu();

        dbHandler = new MyDBHandler(getApplicationContext());

        toggleButtonNotifications();
    }

    private void toggleButtonNotifications() {
        ToggleButton toggle = (ToggleButton) findViewById(R.id.notifications);
        toggle.setChecked(dbHandler.isNotificationsEnabled(patient.getId()));
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dbHandler.setNotifications(true, patient.getId());
                } else {
                    dbHandler.setNotifications(false, patient.getId());
                }
            }
        });
    }

}
