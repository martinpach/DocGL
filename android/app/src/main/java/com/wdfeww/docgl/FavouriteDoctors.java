package com.wdfeww.docgl;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.wdfeww.docgl.data.methods.NavigationMenu;
import com.wdfeww.docgl.data.model.Patient;

public class FavouriteDoctors extends AppCompatActivity {


    Toolbar toolbar;
    String token;
    Patient patient;
    Class className;
    NavigationMenu navigationMenu;
    DrawerLayout drawer_layout;
    NavigationView nav_view;
    LinearLayout main_layout,results;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite_doctors);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = this.getIntent().getExtras();
        patient = bundle.getParcelable("patient");
        token = getIntent().getStringExtra("token");

        className = getClass();

        nav_view = (NavigationView) findViewById(R.id.nav_view);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);

        main_layout = (LinearLayout) findViewById(R.id.main_layout);
        results = (LinearLayout) findViewById(R.id.results);

        navigationMenu = new NavigationMenu(token, patient, this, toolbar, drawer_layout, nav_view, className);
        navigationMenu.initMenu();

        getFavouriteDoctors();
    }


    private void getFavouriteDoctors(){

    }

}
