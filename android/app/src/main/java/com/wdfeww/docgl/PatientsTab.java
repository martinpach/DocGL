package com.wdfeww.docgl;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wdfeww.docgl.data.SQLiteDatabase.MyDBHandler;
import com.wdfeww.docgl.data.SQLiteDatabase.Patients;
import com.wdfeww.docgl.data.SQLiteDatabase.DBOutput;
import com.wdfeww.docgl.data.methods.Checker;
import com.wdfeww.docgl.data.methods.NavigationMenu;
import com.wdfeww.docgl.data.model.Patient;
import com.wdfeww.docgl.data.model.User;

import java.util.List;

public class PatientsTab extends AppCompatActivity {

    Toolbar toolbar;
    String token;
    Patient patient;
    Class className;
    NavigationMenu navigationMenu;
    DrawerLayout drawer_layout;
    NavigationView nav_view;
    LinearLayout main_layout, results;
    EditText et1, et2;
    Button btn1;
    TextView errorMessage, successMessage;

    Runnable mRunnable;
    Handler mHandler = new Handler();

    MyDBHandler dbHandler;

    Patients newPatient;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patients_tab);

        SharedPreferences prefs = this.getSharedPreferences("com.wdfeww.docgl", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("LoggedUser", "");
        user = gson.fromJson(json, User.class);
        json = prefs.getString("LoggedPatient", "");
        patient = gson.fromJson(json, Patient.class);
        token = user.getToken();

        newPatient = new Patients();

        errorMessage = (TextView) findViewById(R.id.errorMessage);
        errorMessage.setVisibility(View.GONE);
        successMessage = (TextView) findViewById(R.id.successMessage);
        successMessage.setVisibility(View.GONE);

        btn1 = (Button) findViewById(R.id.btn1);
        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        className = getClass();

        nav_view = (NavigationView) findViewById(R.id.nav_view);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);

        main_layout = (LinearLayout) findViewById(R.id.main_layout);
        results = (LinearLayout) findViewById(R.id.results);

        navigationMenu = new NavigationMenu(this, toolbar, drawer_layout, nav_view, className);
        navigationMenu.initMenu();

        dbHandler = new MyDBHandler(getApplicationContext());

        mRunnable = new Runnable() {

            @Override
            public void run() {

                successMessage.setVisibility(View.INVISIBLE);
                successMessage.setVisibility(View.GONE);
            }
        };

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Checker.isNameValid(et1.getText().toString().trim())) {
                    if (Checker.isNameValid(et2.getText().toString().trim())) {

                        newPatient.set_patientFirstName(et1.getText().toString().trim());
                        newPatient.set_patientLastName(et2.getText().toString().trim());
                        newPatient.set_userId(patient.getId());
                        dbHandler.addPatient(newPatient);
                        et1.setText("");
                        et2.setText("");
                        errorMessage.setVisibility(View.GONE);
                        successMessage.setText("Patient added.");
                        successMessage.setVisibility(View.VISIBLE);
                        mHandler.removeCallbacks(mRunnable);
                        mHandler.postDelayed(mRunnable, 2000);
                        initPatients();

                    } else {

                        errorMessage.setVisibility(View.VISIBLE);
                        errorMessage.setText("Please type patient last name.");
                        successMessage.setVisibility(View.GONE);

                    }
                } else {

                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText("Please type patient first name.");
                    successMessage.setVisibility(View.GONE);

                }
            }
        });

        initPatients();

    }

    @Override
    public void onBackPressed() {
        navigationMenu.redirect();
    }

    public void initPatients() {
        List<DBOutput> dbOutputs = dbHandler.databaseToString(patient.getId());
        results.removeAllViews();
        for (final DBOutput dbOutput : dbOutputs) {

            LinearLayout.LayoutParams text_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            text_params.setMargins(30, 30, 30, 0);
            TextView tv = new TextView(this);
            tv.setText(dbOutput.getFirstname() + " " + dbOutput.getLastname());
            tv.setLayoutParams(text_params);
            tv.setGravity(Gravity.LEFT);
            tv.setTextAppearance(this, R.style.profile_text);
            tv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    dbHandler.deletePatient(dbOutput.getPatientID());
                    initPatients();
                    return true;
                }
            });
            results.addView(tv);
        }
    }
}
