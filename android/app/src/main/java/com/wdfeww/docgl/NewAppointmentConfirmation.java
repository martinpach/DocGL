package com.wdfeww.docgl;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wdfeww.docgl.data.methods.JsonReqestBody;
import com.wdfeww.docgl.data.methods.NavigationMenu;
import com.wdfeww.docgl.data.model.Doctor;
import com.wdfeww.docgl.data.model.Patient;
import com.wdfeww.docgl.data.model.User;
import com.wdfeww.docgl.data.remote.Service;
import com.wdfeww.docgl.data.remote.ServiceGenerator;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewAppointmentConfirmation extends AppCompatActivity {
    Class className;
    NavigationView nav_view;
    DrawerLayout drawer_layout;
    NavigationMenu navigationMenu;
    String token, date, time, pFirstName, pLastName, note;
    Patient patient;
    Toolbar toolbar;
    Doctor doctor;
    LinearLayout results, main_layout;
    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, errorMessage, successMessage;
    EditText et1, et2, et3;
    LinearLayout.LayoutParams text_params, sub_text_params, btn_params;
    Button btn1;
    User user;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_appointment_confirmation);

        SharedPreferences prefs = this.getSharedPreferences("com.wdfeww.docgl", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("LoggedUser", "");
        user = gson.fromJson(json, User.class);
        json = prefs.getString("LoggedPatient", "");
        patient = gson.fromJson(json, Patient.class);
        token = user.getToken();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = this.getIntent().getExtras();
        doctor = bundle.getParcelable("doctor");
        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");

        results = (LinearLayout) findViewById(R.id.results);
        main_layout = (LinearLayout) findViewById(R.id.main_layout);

        className = getClass();
        nav_view = (NavigationView) findViewById(R.id.nav_view);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationMenu = new NavigationMenu(this, toolbar, drawer_layout, nav_view, className);
        navigationMenu.initMenu();

        errorMessage = new TextView(this);
        errorMessage.setTextAppearance(this, R.style.ErrorMessage);
        errorMessage.setBackground(this.getResources().getDrawable(R.drawable.error_message_background));
        errorMessage.setGravity(Gravity.CENTER);
        errorMessage.setVisibility(View.GONE);
        successMessage = new TextView(this);
        successMessage.setTextAppearance(this, R.style.SuccessMessage);
        successMessage.setBackground(this.getResources().getDrawable(R.drawable.success_message_background));
        successMessage.setGravity(Gravity.CENTER);
        successMessage.setVisibility(View.GONE);

        text_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        text_params.setMargins(20, 30, 0, 0);
        sub_text_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        sub_text_params.setMargins(30, 15, 30, 0);
        btn_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        btn_params.setMargins(10, 65, 10, 0);

        tv1 = new TextView(this);
        tv1.setText("Date and time:");
        tv1.setLayoutParams(text_params);
        tv1.setGravity(Gravity.LEFT);
        tv1.setTextAppearance(this, R.style.profile_text);
        main_layout.addView(tv1);

        tv2 = new TextView(this);
        tv2.setText(date + " " + time);
        tv2.setLayoutParams(sub_text_params);
        tv2.setGravity(Gravity.LEFT);
        tv2.setTextAppearance(this, R.style.profile_sub_text);
        main_layout.addView(tv2);

        tv3 = new TextView(this);
        tv3.setText("Informations about doctor:");
        tv3.setLayoutParams(text_params);
        tv3.setGravity(Gravity.LEFT);
        tv3.setTextAppearance(this, R.style.profile_text);
        main_layout.addView(tv3);

        tv4 = new TextView(this);
        tv4.setText("Doctor name: " + doctor.getFirstName() + " " + doctor.getLastName() + "\n" +
                "Specialisation: " + doctor.getSpecialization() + "\n" +
                "Workplace adress: " + doctor.getWorkplace() + " " + doctor.getCity());
        tv4.setLayoutParams(sub_text_params);
        tv4.setGravity(Gravity.LEFT);
        tv4.setTextAppearance(this, R.style.profile_sub_text);
        main_layout.addView(tv4);

        tv5 = new TextView(this);
        tv5.setText("Set patient name:");
        tv5.setLayoutParams(text_params);
        tv5.setGravity(Gravity.LEFT);
        tv5.setTextAppearance(this, R.style.profile_text);
        main_layout.addView(tv5);

        et1 = new EditText(this);
        et1.setHint("first name");
        et1.setLayoutParams(sub_text_params);
        et1.setInputType(InputType.TYPE_CLASS_TEXT);
        et1.setHintTextColor(this.getResources().getColor(R.color.color6));
        main_layout.addView(et1);

        et2 = new EditText(this);
        et2.setHint("last name");
        et2.setLayoutParams(sub_text_params);
        et2.setInputType(InputType.TYPE_CLASS_TEXT);
        et2.setHintTextColor(this.getResources().getColor(R.color.color6));
        main_layout.addView(et2);

        tv6 = new TextView(this);
        tv6.setText("Note:");
        tv6.setLayoutParams(text_params);
        tv6.setGravity(Gravity.LEFT);
        tv6.setTextAppearance(this, R.style.profile_text);
        main_layout.addView(tv6);

        et3 = new EditText(this);
        et3.setHint("note");
        et3.setLayoutParams(sub_text_params);
        et3.setInputType(InputType.TYPE_CLASS_TEXT);
        et3.setHintTextColor(this.getResources().getColor(R.color.color6));
        main_layout.addView(et3);

        errorMessage.setLayoutParams(sub_text_params);
        main_layout.addView(errorMessage);

        successMessage.setLayoutParams(sub_text_params);
        main_layout.addView(successMessage);

        btn1 = new Button(this);
        btn1.setGravity(Gravity.CENTER);
        btn1.setLayoutParams(btn_params);
        btn1.setBackground(this.getResources().getDrawable(R.drawable.btn_transparent_background));
        btn1.setTextAppearance(this, R.style.btn_transparent);
        btn1.setText("Create appointment");
        btn1.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                pFirstName = et1.getText().toString().trim();
                pLastName = et2.getText().toString().trim();
                note = et3.getText().toString().trim();

                createAppointment();

            }

        });
        main_layout.addView(btn1);

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
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void createAppointment() {
        if (pFirstName.isEmpty() && pLastName.isEmpty()) {
            errorMessage.setVisibility(View.VISIBLE);
            errorMessage.setText("Please fill first name and last name of patient.");

        } else {
            errorMessage.setVisibility(View.GONE);
            JSONObject json = JsonReqestBody.newAppointment(date, time, pFirstName, pLastName, doctor.getId(), note);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (json.toString()));
            Service loginService =
                    ServiceGenerator.createService(Service.class, token);
            Call<ResponseBody> call = loginService.createNewAppointment(patient.getId(), body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        errorMessage.setVisibility(View.GONE);
                        successMessage.setText("Appointment was created!");
                        successMessage.setVisibility(View.VISIBLE);
                        navigationMenu.redirect(Home.class);
                        try {
                            addAppoitnmenToCalendar();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    } else {
                        errorMessage.setVisibility(View.VISIBLE);
                        errorMessage.setText("This date and time is already taken!");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText("Server not responding!");
                    Log.d("Error", t.getMessage());
                }
            });
        }

    }

    private void addAppoitnmenToCalendar() throws ParseException {

        Calendar cal = Calendar.getInstance();
        String dateTime = date + " " + time;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        cal.setTime(simpleDateFormat.parse(dateTime));

        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", cal.getTimeInMillis());
        intent.putExtra("allDay", true);
        intent.putExtra(CalendarContract.Events.DESCRIPTION, pFirstName+" "+pLastName+" "+note);
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, doctor.getCity() + " " + doctor.getWorkplace());
        intent.putExtra("endTime", cal.getTimeInMillis() + 30 * 60 * 1000);
        intent.putExtra("title", doctor.getSpecialization());
        startActivity(intent);
    }
}
