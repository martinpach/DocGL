package com.wdfeww.docgl;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wdfeww.docgl.data.methods.NavigationMenu;
import com.wdfeww.docgl.data.model.Appointment;
import com.wdfeww.docgl.data.model.Patient;
import com.wdfeww.docgl.data.model.User;
import com.wdfeww.docgl.data.remote.Service;
import com.wdfeww.docgl.data.remote.ServiceGenerator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailOfAppointment extends AppCompatActivity {
    Appointment appointment;
    Toolbar toolbar;
    DrawerLayout drawer_layout;
    NavigationView nav_view;
    String token;
    Button btn2, btn1, btn3;
    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10, tv11, tv12, tv13, errorMessage, successMessage;
    LinearLayout.LayoutParams params, btn_params, text_params, sub_text_params,btn_confirmation_params;
    LinearLayout main_layout, linearLayout;
    TextView logged_user;
    Class className;
    Patient patient;
    NavigationMenu navigationMenu;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_of_appointment);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = this.getIntent().getExtras();
        appointment = bundle.getParcelable("appointment");
        patient = bundle.getParcelable("patient");
        token = getIntent().getStringExtra("token");


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

        main_layout = (LinearLayout) findViewById(R.id.main_layout);

        className = getClass();
        nav_view = (NavigationView) findViewById(R.id.nav_view);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        logged_user = new TextView(this);
        logged_user.setTextAppearance(this, R.style.profile_text);
        navigationMenu = new NavigationMenu(token, patient, this, toolbar, drawer_layout, nav_view, className);
        navigationMenu.initMenu();


        try {
            showDetail();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showDetail() throws ParseException {
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a");
        main_layout.removeAllViews();
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 50, 0, 0);
        btn_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        btn_params.setMargins(10, 65, 10, 0);
        btn_confirmation_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        btn_confirmation_params.setMargins(10, 35, 10,10);
        text_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        text_params.setMargins(20, 30, 0, 0);
        sub_text_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        sub_text_params.setMargins(30, 15, 30, 0);


        tv1 = new TextView(this);
        tv1.setText(this.getResources().getString(R.string.appointmentDetail));
        tv1.setLayoutParams(params);
        tv1.setGravity(Gravity.LEFT);
        tv1.setTextAppearance(this, R.style.home_text);
        main_layout.addView(tv1);

        tv6 = new TextView(this);
        tv6.setText(this.getResources().getString(R.string.appointmentPatient));
        tv6.setLayoutParams(text_params);
        tv6.setGravity(Gravity.LEFT);
        tv6.setTextAppearance(this, R.style.profile_text);
        main_layout.addView(tv6);

        tv7 = new TextView(this);
        tv7.setText(appointment.getPatientFirstName() + " " + appointment.getPatientLastName());
        tv7.setLayoutParams(sub_text_params);
        tv7.setGravity(Gravity.LEFT);
        tv7.setTextAppearance(this, R.style.profile_sub_text);
        main_layout.addView(tv7);

        tv8 = new TextView(this);
        tv8.setText(this.getResources().getString(R.string.appointmentNote));
        tv8.setLayoutParams(text_params);
        tv8.setGravity(Gravity.LEFT);
        tv8.setTextAppearance(this, R.style.profile_text);
        main_layout.addView(tv8);

        tv9 = new TextView(this);
        tv9.setText(appointment.getNote());
        tv9.setLayoutParams(sub_text_params);
        tv9.setGravity(Gravity.LEFT);
        tv9.setTextAppearance(this, R.style.profile_sub_text);
        main_layout.addView(tv9);

        tv2 = new TextView(this);
        tv2.setText(this.getResources().getString(R.string.appointmentPlace));
        tv2.setLayoutParams(text_params);
        tv2.setGravity(Gravity.LEFT);
        tv2.setTextAppearance(this, R.style.profile_text);
        main_layout.addView(tv2);

        tv3 = new TextView(this);
        tv3.setText(appointment.getDoctor().getWorkplace() + ", " + appointment.getDoctor().getCity());
        tv3.setLayoutParams(sub_text_params);
        tv3.setGravity(Gravity.LEFT);
        tv3.setTextAppearance(this, R.style.profile_sub_text);
        main_layout.addView(tv3);

        tv4 = new TextView(this);
        tv4.setText(this.getResources().getString(R.string.appointmentDateAndTime));
        tv4.setLayoutParams(text_params);
        tv4.setGravity(Gravity.LEFT);
        tv4.setTextAppearance(this, R.style.profile_text);
        main_layout.addView(tv4);

        tv5 = new TextView(this);
        tv5.setText(sdf1.format(new SimpleDateFormat("yyyy-MM-dd").parse(appointment.getDate())) + " " + sdf2.format(new SimpleDateFormat("hh:mm:ss").parse(appointment.getTime())));
        tv5.setLayoutParams(sub_text_params);
        tv5.setGravity(Gravity.LEFT);
        tv5.setTextAppearance(this, R.style.profile_sub_text);
        main_layout.addView(tv5);

        tv10 = new TextView(this);
        tv10.setText(this.getResources().getString(R.string.appointmentDoctorName));
        tv10.setLayoutParams(text_params);
        tv10.setGravity(Gravity.LEFT);
        tv10.setTextAppearance(this, R.style.profile_text);
        main_layout.addView(tv10);

        tv11 = new TextView(this);
        tv11.setText(appointment.getDoctor().getLastName() + " " + appointment.getDoctor().getFirstName());
        tv11.setLayoutParams(sub_text_params);
        tv11.setGravity(Gravity.LEFT);
        tv11.setTextAppearance(this, R.style.profile_sub_text);
        main_layout.addView(tv11);


        tv12 = new TextView(this);
        tv12.setText(this.getResources().getString(R.string.appointmentDoctorInfo));
        tv12.setLayoutParams(text_params);
        tv12.setGravity(Gravity.LEFT);
        tv12.setTextAppearance(this, R.style.profile_text);
        main_layout.addView(tv12);

        tv13 = new TextView(this);
        tv13.setText("Specialization: " + appointment.getDoctor().getSpecialization() + "\n"
                + "Phone: " + appointment.getDoctor().getPhone() + "\n"
                + "Email: " + appointment.getDoctor().getEmail());
        tv13.setLayoutParams(sub_text_params);
        tv13.setGravity(Gravity.LEFT);
        tv13.setTextAppearance(this, R.style.profile_sub_text);
        main_layout.addView(tv13);

        main_layout.addView(errorMessage);
        main_layout.addView(successMessage);

        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setLayoutParams(params);
        main_layout.addView(linearLayout);

        btn1 = new Button(this);
        btn1.setText(this.getResources().getString(R.string.appointmentCancel));
        btn1.setGravity(Gravity.CENTER);
        btn1.setLayoutParams(btn_params);
        btn1.setBackground(this.getResources().getDrawable(R.drawable.btn_transparent_background));
        btn1.setTextColor(this.getResources().getColor(R.color.color5));
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn2.setVisibility(View.VISIBLE);
                btn3.setVisibility(View.VISIBLE);
                btn1.setVisibility(View.GONE);
            }
        });
        main_layout.addView(btn1);

        btn2 = new Button(this);
        btn2.setVisibility(View.GONE);
        btn2.setText(this.getResources().getString(R.string.ok));
        btn2.setGravity(Gravity.CENTER);
        btn2.setLayoutParams(btn_confirmation_params);
        btn2.setBackground(this.getResources().getDrawable(R.drawable.btn_transparent_background));
        btn2.setTextColor(this.getResources().getColor(R.color.color1));
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAppointment();
            }
        });
        linearLayout.addView(btn2);

        btn3 = new Button(this);
        btn3.setVisibility(View.GONE);
        btn3.setText(this.getResources().getString(R.string.cancel));
        btn3.setGravity(Gravity.CENTER);
        btn3.setLayoutParams(btn_confirmation_params);
        btn3.setBackground(this.getResources().getDrawable(R.drawable.btn_transparent_background));
        btn3.setTextColor(this.getResources().getColor(R.color.color2));
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn2.setVisibility(View.GONE);
                btn3.setVisibility(View.GONE);
                btn1.setVisibility(View.VISIBLE);
            }
        });
        linearLayout.addView(btn3);
        LinearLayout linearLayoutBottom = new LinearLayout(this);
        linearLayoutBottom.setLayoutParams(params);
        main_layout.addView(linearLayoutBottom);
    }
    private void cancelAppointment(){
        Service loginService =
                ServiceGenerator.createService(Service.class, token);
        Call<ResponseBody> call = loginService.cancelAppointment(appointment.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    errorMessage.setVisibility(View.GONE);
                    successMessage.setText("Appointment was cancelled!");
                    successMessage.setVisibility(View.VISIBLE);
                    redirect();
                }
                else{
                    successMessage.setVisibility(View.GONE);
                    errorMessage.setText("Appointment is already cancelled!");
                    errorMessage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                successMessage.setVisibility(View.GONE);
                errorMessage.setText("Server not responding!");
                errorMessage.setVisibility(View.VISIBLE);
            }
        });
    }
    private void redirect(){

        navigationMenu.redirect(Home.class);
    }

}
