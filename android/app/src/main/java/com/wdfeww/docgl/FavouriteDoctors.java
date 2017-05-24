package com.wdfeww.docgl;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wdfeww.docgl.data.methods.FontManager;
import com.wdfeww.docgl.data.methods.JsonReqestBody;
import com.wdfeww.docgl.data.methods.NavigationMenu;
import com.wdfeww.docgl.data.model.Appointment;
import com.wdfeww.docgl.data.model.Doctor;
import com.wdfeww.docgl.data.model.Patient;
import com.wdfeww.docgl.data.remote.Service;
import com.wdfeww.docgl.data.remote.ServiceGenerator;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wdfeww.docgl.R.id.errorMessage;

public class FavouriteDoctors extends AppCompatActivity {


    Toolbar toolbar;
    String token;
    Patient patient;
    Class className;
    NavigationMenu navigationMenu;
    DrawerLayout drawer_layout;
    NavigationView nav_view;
    LinearLayout main_layout, results;
    List<Doctor> doctors;
    TextView noResult;
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    LinearLayout.LayoutParams txt_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

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

        noResult = (TextView) findViewById(R.id.noResult);

        getFavouriteDoctors();
    }


    private void getFavouriteDoctors() {
        Service service = ServiceGenerator.createService(Service.class, token);
        Call<List<Doctor>> call = service.getFavouriteDoctors(patient.getId());
        call.enqueue(new Callback<List<Doctor>>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if (response.isSuccessful()) {
                    doctors = response.body();
                    if (doctors.size() > 0) {
                        noResult.setVisibility(View.GONE);
                        showResults();
                    } else {

                        noResult.setVisibility(View.VISIBLE);

                    }

                } else {

                    noResult.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Server not responding or no internet connection.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showResults() {
        params.setMargins(0,0,0,20);
        results.removeAllViews();
        for (final Doctor doctor : doctors) {
            final LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(params);
            linearLayout.setBackground(this.getResources().getDrawable(R.drawable.background_appointment));
            linearLayout.setClickable(true);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setId(doctor.getId());
            results.addView(linearLayout);


            TextView tv1 = new TextView(this);
            tv1.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));
            tv1.setTextSize(25.0f);
            tv1.setLayoutParams(txt_params);
            tv1.setText("Name: " + doctor.getFirstName() + " " + doctor.getLastName());
            tv1.setGravity(Gravity.LEFT);
            linearLayout.addView(tv1);

            TextView tv2 = new TextView(this);
            tv2.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));
            tv2.setTextSize(25.0f);
            tv2.setLayoutParams(txt_params);
            tv2.setText("Specialisation: " + doctor.getSpecialization());
            tv2.setGravity(Gravity.LEFT);
            linearLayout.addView(tv2);

            TextView tv3 = new TextView(this);
            tv3.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));
            tv3.setTextSize(25.0f);
            tv3.setLayoutParams(txt_params);
            tv3.setText("City: " + doctor.getCity());
            tv3.setGravity(Gravity.LEFT);
            linearLayout.addView(tv3);

            TextView tv4 = new TextView(this);
            tv4.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));
            tv4.setTextSize(25.0f);
            tv4.setLayoutParams(txt_params);
            tv4.setText("Workplace: " + doctor.getWorkplace());
            tv4.setGravity(Gravity.LEFT);
            linearLayout.addView(tv4);

            TextView tv5 = new TextView(this);
            tv5.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));
            tv5.setTextSize(25.0f);
            tv5.setLayoutParams(txt_params);
            tv5.setText(this.getResources().getString(R.string.fill_heart));
            tv5.setGravity(Gravity.CENTER);
            tv5.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                   removeFromFavourites(doctor);
                    return false;
                }
            });
            linearLayout.addView(tv5);
        }
    }

    private void removeFromFavourites(Doctor doctor){
        JSONObject json = JsonReqestBody.addDoctorToFavourite(doctor.getId());
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (json.toString()));
        Service service =
                ServiceGenerator.createService(Service.class, token);
        Call<ResponseBody> call = service.addDoctorToFavourite(patient.getId(), body);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Doctor was removed from your favourite list", Toast.LENGTH_LONG).show();
                    navigationMenu.redirect(FavouriteDoctors.class);
                }else {
                    Toast.makeText(getApplicationContext(), "Error: Doctor was not removed from your favourite list", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Server error!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
