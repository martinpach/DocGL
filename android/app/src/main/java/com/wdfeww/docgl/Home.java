package com.wdfeww.docgl;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wdfeww.docgl.data.methods.FontManager;
import com.wdfeww.docgl.data.methods.NavigationMenu;
import com.wdfeww.docgl.data.model.Appointment;
import com.wdfeww.docgl.data.model.Patient;
import com.wdfeww.docgl.data.remote.Service;
import com.wdfeww.docgl.data.remote.ServiceGenerator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity {
    Toolbar toolbar;
    DrawerLayout drawer_layout;
    NavigationView nav_view;
    String token, medkit, calendar;
    LinearLayout main_layout;
    List<Appointment> appointments;
    Patient patient;
    Class className;
    FloatingActionButton fab;
    NavigationMenu navigationMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        main_layout = (LinearLayout) findViewById(R.id.main_layout);

        Bundle bundle = this.getIntent().getExtras();
        patient = bundle.getParcelable("patient");
        token = getIntent().getStringExtra("token");

        className = getClass();
        nav_view = (NavigationView) findViewById(R.id.nav_view);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);

        medkit = this.getResources().getString(R.string.fa_medkit);
        calendar = this.getResources().getString(R.string.fa_calendar);

        navigationMenu = new NavigationMenu(token, patient, this, toolbar, drawer_layout, nav_view, className);
        navigationMenu.initMenu();

        checkAppointments();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundColor(this.getResources().getColor(R.color.color2));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationMenu.redirect(NewAppointmentChooseDoctor.class);
            }
        });


    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    private void checkAppointments() {

        Service service = ServiceGenerator.createService(Service.class, token);
        Call<List<Appointment>> call = service.getPatientAppointments(patient.getId());
        call.enqueue(new Callback<List<Appointment>>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (response.isSuccessful()) {
                    appointments = response.body();
                    int count =0;
                    for(Appointment appointment:appointments){
                        if(!appointment.getCanceled()){
                            count++;
                        }
                    }
                    if (count < 1) {
                        createDefaultLayout();
                    } else {
                        try {
                            createAppointmentListLayout();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {
                createErrorLayout();
            }
        });
    }

    private void createErrorLayout() {
        main_layout.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 50, 0, 0);
        TextView tv = new TextView(this);
        tv.setText(this.getResources().getString(R.string.home_error_text));
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setTextAppearance(this, R.style.home_error_text);
        main_layout.addView(tv);
    }

    private void createDefaultLayout() {
        main_layout.removeAllViews();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 50, 0, 0);


        TextView tv1 = new TextView(this);
        tv1.setText(this.getResources().getString(R.string.home_txt1));
        tv1.setLayoutParams(params);
        tv1.setGravity(Gravity.CENTER);
        tv1.setTextAppearance(this, R.style.home_text);
        main_layout.addView(tv1);

        TextView tv2 = new TextView(this);
        tv2.setText(this.getResources().getString(R.string.home_txt2));
        tv2.setLayoutParams(params);
        tv2.setGravity(Gravity.CENTER);
        tv2.setTextAppearance(this, R.style.home_clickable_text);
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeAppointment();
            }
        });
        main_layout.addView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setText(this.getResources().getString(R.string.home_txt3));
        tv3.setLayoutParams(params);
        tv3.setGravity(Gravity.CENTER);
        tv3.setTextAppearance(this, R.style.home_clickable_text);
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile();
            }
        });
        main_layout.addView(tv3);

        TextView tv4 = new TextView(this);
        tv4.setText(this.getResources().getString(R.string.home_txt4));
        tv4.setLayoutParams(params);
        tv4.setGravity(Gravity.CENTER);
        tv4.setTextAppearance(this, R.style.home_clickable_text);
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSettings();
            }
        });
        main_layout.addView(tv4);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void createAppointmentListLayout() throws ParseException {
        main_layout.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(30, 50, 0, 0);

        TextView tv = new TextView(this);
        tv.setText(this.getResources().getString(R.string.home_txt5));
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.LEFT);
        tv.setTextAppearance(this, R.style.home_text);
        main_layout.addView(tv);

        params = new LinearLayout.LayoutParams(getWindowManager().getDefaultDisplay().getWidth() - 60, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = 40;
        LinearLayout.LayoutParams txt_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        txt_params.topMargin = 7;
        for (final Appointment appointment : appointments) {
            if (!appointment.getCanceled()){
            final LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(params);
            linearLayout.setBackground(this.getResources().getDrawable(R.drawable.background_appointment));
            linearLayout.setClickable(true);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setId(appointment.getId());
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDetailOfAppointment(appointment);
                }
            });
            main_layout.addView(linearLayout);


            SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a");

            TextView tv1 = new TextView(this);
            tv1.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));
            tv1.setTextSize(25.0f);
            tv1.setLayoutParams(txt_params);
            tv1.setText(calendar + " " + sdf1.format(new SimpleDateFormat("yyyy-MM-dd").parse(appointment.getDate())) + " " + sdf2.format(new SimpleDateFormat("hh:mm:ss").parse(appointment.getTime())));
            tv1.setGravity(Gravity.LEFT);
            linearLayout.addView(tv1);

            TextView tv2 = new TextView(this);
            tv2.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));
            tv2.setTextSize(25.0f);
            tv2.setLayoutParams(txt_params);
            tv2.setText(appointment.getPatientLastName()+ " "+ appointment.getPatientFirstName());
            tv2.setGravity(Gravity.LEFT);
            linearLayout.addView(tv2);

            TextView tv3 = new TextView(this);
            tv3.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));
            tv3.setTextSize(25.0f);
            tv3.setLayoutParams(txt_params);
            tv3.setText("@ " + appointment.getDoctor().getWorkplace() + ", " + appointment.getDoctor().getCity());
            tv3.setGravity(Gravity.LEFT);
            linearLayout.addView(tv3);

            TextView tv4 = new TextView(this);
            tv4.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));
            tv4.setTextSize(25.0f);
            tv4.setLayoutParams(txt_params);
            tv4.setText(medkit + " Doctor " + appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName());
            tv4.setGravity(Gravity.LEFT);
            linearLayout.addView(tv4);
            }}

    }

    public void makeAppointment() {
        navigationMenu.redirect(NewAppointmentChooseDoctor.class);
    }

    public void profile() {
        Intent intent = new Intent(getBaseContext(), Profile.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("patient", patient);
        intent.putExtras(bundle);
        intent.putExtra("token", token);
        startActivity(intent);
    }

    public void userSettings() {

    }

    private void showDetailOfAppointment(Appointment appointment){
        Intent intent = new Intent(getBaseContext(), DetailOfAppointment.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("appointment", appointment);
        bundle.putParcelable("patient", patient);
        intent.putExtras(bundle);
        intent.putExtra("token", token);
        startActivity(intent);
    }
}
