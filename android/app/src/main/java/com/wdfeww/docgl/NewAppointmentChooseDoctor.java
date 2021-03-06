package com.wdfeww.docgl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wdfeww.docgl.data.methods.FontManager;
import com.wdfeww.docgl.data.methods.JsonReqestBody;
import com.wdfeww.docgl.data.methods.NavigationMenu;
import com.wdfeww.docgl.data.model.Doctor;
import com.wdfeww.docgl.data.model.Patient;
import com.wdfeww.docgl.data.model.User;
import com.wdfeww.docgl.data.remote.Service;
import com.wdfeww.docgl.data.remote.ServiceGenerator;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewAppointmentChooseDoctor extends AppCompatActivity {
    Toolbar toolbar;
    LinearLayout main_layout, getDocLayout, results, chooseSpecLayout;
    DrawerLayout drawer_layout;
    NavigationView nav_view;
    Patient patient;
    String token, name, spec;
    Class className;
    NavigationMenu navigationMenu;
    RadioButton radioButton1, radioButton2;
    EditText search, searchCity;
    TextView errorMessage, successMessage;
    List<Doctor> doctors, s_doctors;
    Button btn_search;
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    LinearLayout.LayoutParams txt_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    User user;
    RadioGroup radioGroup;
    final int den = 1, cardio = 2, ortho = 3;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_appointment_choose_doctor);


        SharedPreferences prefs = this.getSharedPreferences("com.wdfeww.docgl", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("LoggedUser", "");
        user = gson.fromJson(json, User.class);
        json = prefs.getString("LoggedPatient", "");
        patient = gson.fromJson(json, Patient.class);
        token = user.getToken();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        errorMessage = new TextView(this);
        errorMessage.setTextAppearance(this, R.style.ErrorMessage);
        errorMessage.setBackground(this.getResources().getDrawable(R.drawable.error_message_background));
        errorMessage.setGravity(Gravity.CENTER);
        successMessage = new TextView(this);
        successMessage.setTextAppearance(this, R.style.SuccessMessage);
        successMessage.setBackground(this.getResources().getDrawable(R.drawable.success_message_background));
        successMessage.setGravity(Gravity.CENTER);

        chooseSpecLayout = new LinearLayout(this);
        chooseSpecLayout.setLayoutParams(params);
        chooseSpecLayout.setOrientation(LinearLayout.HORIZONTAL);

        radioGroup = new RadioGroup(this);
        radioGroup.setLayoutParams(txt_params);
        RadioButton rbD = new RadioButton(this);
        rbD.setText("Dentist");
        rbD.setLayoutParams(txt_params);
        RadioButton rbC = new RadioButton(this);
        rbC.setText("Cardiologist");
        rbC.setLayoutParams(txt_params);
        RadioButton rbO = new RadioButton(this);
        rbO.setText("Orthopedist");
        rbO.setLayoutParams(txt_params);
        radioGroup.addView(rbD);
        radioGroup.addView(rbC);
        radioGroup.addView(rbO);

        radioGroup.getChildAt(0).setId(den);
        radioGroup.getChildAt(1).setId(cardio);
        radioGroup.getChildAt(2).setId(ortho);
        radioGroup.check(radioGroup.getChildAt(0).getId());
        chooseSpecLayout.addView(radioGroup);

        params.setMargins(20, 30, 20, 0);
        txt_params.topMargin = 7;
        search = new EditText(this);
        search.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_black_24dp, 0, 0, 0);
        search.setTextColor(this.getResources().getColor(R.color.color5));
        search.setHintTextColor(this.getResources().getColor(R.color.color6));
        search.setHint("search");
        search.setLayoutParams(params);

        searchCity = new EditText(this);
        searchCity.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_black_24dp, 0, 0, 0);
        searchCity.setTextColor(this.getResources().getColor(R.color.color5));
        searchCity.setHintTextColor(this.getResources().getColor(R.color.color6));
        searchCity.setHint("city");
        searchCity.setLayoutParams(params);

        btn_search = new Button(this);
        btn_search.setText("search");
        btn_search.setLayoutParams(params);

        results = (LinearLayout) findViewById(R.id.results);

        main_layout = (LinearLayout) findViewById(R.id.main_layout);

        className = getClass();
        getDocLayout = (LinearLayout) findViewById(R.id.getDocLayout);
        nav_view = (NavigationView) findViewById(R.id.nav_view);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationMenu = new NavigationMenu(this, toolbar, drawer_layout, nav_view, className);
        navigationMenu.initMenu();

        radioButton1 = (RadioButton) findViewById(R.id.radioBtn1);
        radioButton2 = (RadioButton) findViewById(R.id.radioBtn2);

        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDocLayout.removeAllViews();
                results.removeAllViews();
                getDocLayout.addView(errorMessage);
                getDocLayout.addView(successMessage);
                errorMessage.setVisibility(View.GONE);
                successMessage.setVisibility(View.GONE);

                selectFromFavouritelist();
            }
        });
        radioButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                results.removeAllViews();
                getDocLayout.removeAllViews();
                getDocLayout.addView(search);
                getDocLayout.addView(searchCity);
                getDocLayout.addView(chooseSpecLayout);
                getDocLayout.addView(errorMessage);
                getDocLayout.addView(successMessage);
                errorMessage.setVisibility(View.GONE);
                successMessage.setVisibility(View.GONE);
                getDocLayout.addView(btn_search);

                btn_search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        name = search.getText().toString().trim();
                        switch (radioGroup.getCheckedRadioButtonId()) {
                            case 1:
                                spec = "DENTIST";
                                break;
                            case 2:
                                spec = "CARDIOLOGIST";
                                break;
                            case 3:
                                spec = "ORTHOPEDIST";
                                break;

                        }
                        searchDoctor();
                    }
                });

                search.setOnKeyListener(new View.OnKeyListener() {
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            name = search.getText().toString().trim();
                            switch (radioGroup.getCheckedRadioButtonId()) {
                                case 1:
                                    spec = "DENTIST";
                                    break;
                                case 2:
                                    spec = "CARDIOLOGIST";
                                    break;
                                case 3:
                                    spec = "ORTHOPEDIST";
                                    break;

                            }
                            searchDoctor();

                            return true;
                        }
                        return false;
                    }
                });

            }
        });

    }

    @Override
    public void onBackPressed() {
        navigationMenu.redirect();
    }


    private void selectFromFavouritelist() {
        Service service = ServiceGenerator.createService(Service.class, token);
        Call<List<Doctor>> call = service.getFavouriteDoctors(patient.getId());
        call.enqueue(new Callback<List<Doctor>>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if (response.isSuccessful()) {
                    doctors = response.body();
                    if (doctors.size() > 0) {
                        errorMessage.setVisibility(View.GONE);

                        for (Doctor doctor : doctors)
                            doctor.setFavourite(true);
                        if (radioButton1.isChecked())
                            showResults(doctors);
                    } else {
                        if (radioButton1.isChecked()) {
                            successMessage.setVisibility(View.GONE);
                            errorMessage.setVisibility(View.VISIBLE);
                            errorMessage.setText("your favourite list of doctors is empty");
                        }
                    }

                } else {
                    if (radioButton1.isChecked()) {
                        successMessage.setVisibility(View.GONE);
                        errorMessage.setVisibility(View.VISIBLE);
                        errorMessage.setText("Problem with search doctors.");
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {
                successMessage.setVisibility(View.GONE);
                errorMessage.setVisibility(View.VISIBLE);
                errorMessage.setText("Server not responding or no internet connection.");
            }
        });

    }


    private void searchDoctor() {
        selectFromFavouritelist();
        Service service = ServiceGenerator.createService(Service.class, token);
        Call<List<Doctor>> call = service.searchDoctor(name, spec);
        call.enqueue(new Callback<List<Doctor>>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if (response.isSuccessful()) {
                    s_doctors = response.body();
                    for (Doctor doctor : doctors) {
                        for (Doctor s_doctor : s_doctors) {
                            if (s_doctor.getId().intValue() == doctor.getId().intValue()) {
                                s_doctor.setFavourite(true);
                                System.out.println("setted true");
                            } else {
                                s_doctor.setFavourite(false);
                                System.out.println("setted false");
                            }

                        }
                    }
                    if (s_doctors.size() > 0) {

                        errorMessage.setVisibility(View.GONE);
                        successMessage.setVisibility(View.VISIBLE);
                        if (!(searchCity.getText().toString().trim().isEmpty())) {
                            for (Doctor doctor : s_doctors) {
                                if (!(doctor.getCity().toLowerCase().contains(searchCity.getText().toString().trim().toLowerCase()))) {
                                    s_doctors.remove(doctor);
                                }

                            }
                        }

                        if (s_doctors.size() == 1) {
                            errorMessage.setVisibility(View.GONE);
                            successMessage.setVisibility(View.VISIBLE);
                            successMessage.setText("one doctor found");
                        } else if (s_doctors.size() == 0) {
                            successMessage.setVisibility(View.GONE);
                            errorMessage.setVisibility(View.VISIBLE);
                            errorMessage.setText("no doctors found");
                        } else {
                            errorMessage.setVisibility(View.GONE);
                            successMessage.setVisibility(View.VISIBLE);
                            successMessage.setText("found " + response.body().size() + " doctors");
                        }


                        showResults(s_doctors);

                    } else {
                        successMessage.setVisibility(View.GONE);
                        errorMessage.setVisibility(View.VISIBLE);
                        errorMessage.setText("no doctors found");
                    }

                } else {
                    successMessage.setVisibility(View.GONE);
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText("Problem with search doctors.");
                }

            }

            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {
                successMessage.setVisibility(View.GONE);
                errorMessage.setVisibility(View.VISIBLE);
                errorMessage.setText("Server not responding or no internet connection.");
            }
        });


    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showResults(List<Doctor> docs) {
        results.removeAllViews();
        for (final Doctor doctor : docs) {
            final LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(params);
            linearLayout.setBackground(this.getResources().getDrawable(R.drawable.background_appointment));
            linearLayout.setClickable(true);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setId(doctor.getId());
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    redirect(doctor);
                }
            });
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

            System.out.println(doctor.isFavourite());
            if (doctor.isFavourite() == true) {
                final TextView tv5 = new TextView(this);
                tv5.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));
                tv5.setTextSize(25.0f);
                tv5.setLayoutParams(txt_params);
                tv5.setText(this.getResources().getString(R.string.fill_heart));
                tv5.setGravity(Gravity.LEFT);
                tv5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toggleFavourite(tv5, doctor);
                    }
                });
                linearLayout.addView(tv5);
            } else {
                final TextView tv5 = new TextView(this);
                tv5.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));
                tv5.setTextSize(25.0f);
                tv5.setLayoutParams(txt_params);
                tv5.setText(this.getResources().getString(R.string.notfill_heart));
                tv5.setGravity(Gravity.LEFT);
                tv5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toggleFavourite(tv5, doctor);
                    }
                });
                linearLayout.addView(tv5);
            }
        }


    }

    private void toggleFavourite(TextView tv, Doctor doctor) {
        if (doctor.isFavourite() == true) {
            Toast.makeText(getApplicationContext(), "doctor " + doctor.getFirstName() + " " + doctor.getLastName() + " was removed from favourite list", Toast.LENGTH_SHORT).show();
            doctor.setFavourite(false);
            tv.setText(this.getResources().getString(R.string.notfill_heart));
            addAndRemovefromfavouriteList(doctor);
        } else {
            Toast.makeText(getApplicationContext(), "doctor " + doctor.getFirstName() + " " + doctor.getLastName() + " was added to favourite list", Toast.LENGTH_SHORT).show();
            doctor.setFavourite(true);
            tv.setText(this.getResources().getString(R.string.fill_heart));
            addAndRemovefromfavouriteList(doctor);
        }
    }

    private void redirect(Doctor doctor) {
        Intent intent = new Intent(getBaseContext(), NewAppointmentChooseDatetime.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("doctor", doctor);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void addAndRemovefromfavouriteList(Doctor doc) {
        JSONObject json = JsonReqestBody.addDoctorToFavourite(doc.getId());
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (json.toString()));
        Service service =
                ServiceGenerator.createService(Service.class, token);
        Call<ResponseBody> call = service.addDoctorToFavourite(patient.getId(), body);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {


                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Server error!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
