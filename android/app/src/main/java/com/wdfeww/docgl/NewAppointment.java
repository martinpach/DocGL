package com.wdfeww.docgl;

import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wdfeww.docgl.data.methods.FontManager;
import com.wdfeww.docgl.data.methods.NavigationMenu;
import com.wdfeww.docgl.data.model.Doctor;
import com.wdfeww.docgl.data.model.Patient;
import com.wdfeww.docgl.data.remote.Service;
import com.wdfeww.docgl.data.remote.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewAppointment extends AppCompatActivity {
    Toolbar toolbar;
    LinearLayout main_layout, getDocLayout, results,chooseSpecLayout;
    DrawerLayout drawer_layout;
    NavigationView nav_view;
    Patient patient;
    String token, name, spec;
    Class className;
    NavigationMenu navigationMenu;
    RadioButton radioButton1, radioButton2;
    EditText search;
    TextView errorMessage, successMessage;
    List<Doctor> doctors;
    Button btn_search;
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    LinearLayout.LayoutParams txt_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_appointment_choose_doctor);

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
        final RadioGroup radioGroup = new RadioGroup(this);
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

        btn_search = new Button(this);
        btn_search.setText("search");
        btn_search.setLayoutParams(params);

        results = (LinearLayout) findViewById(R.id.results);

        main_layout = (LinearLayout) findViewById(R.id.main_layout);

        Bundle bundle = this.getIntent().getExtras();
        patient = bundle.getParcelable("patient");
        token = getIntent().getStringExtra("token");

        className = getClass();
        getDocLayout = (LinearLayout) findViewById(R.id.getDocLayout);
        nav_view = (NavigationView) findViewById(R.id.nav_view);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationMenu = new NavigationMenu(token, patient, this, toolbar, drawer_layout, nav_view, className);
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
                        switch (radioGroup.getCheckedRadioButtonId()){
                            case 1:
                                spec = "DENTIST";
                                break;
                            case 2:
                                spec = "CARDIOLOGIST";
                                break;
                            case 3:
                                spec = "ORTHOPEDIST";
                                break;}
                        searchDoctor();
                    }
                });

                search.setOnKeyListener(new View.OnKeyListener() {
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            name = search.getText().toString().trim();
                            switch (radioGroup.getCheckedRadioButtonId()){
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
                        showResults();
                    } else {
                        successMessage.setVisibility(View.GONE);
                        errorMessage.setVisibility(View.VISIBLE);
                        errorMessage.setText("your favourite list of doctors is empty");
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


    private void searchDoctor() {

            Service service = ServiceGenerator.createService(Service.class, token);
            Call<List<Doctor>> call = service.searchDoctor(name, spec);
            call.enqueue(new Callback<List<Doctor>>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                    if (response.isSuccessful()) {
                        doctors = response.body();
                        if (doctors.size() > 0) {
                            errorMessage.setVisibility(View.GONE);
                            successMessage.setVisibility(View.VISIBLE);
                            if(doctors.size()==1){
                                successMessage.setText("one doctor found");
                            }
                            else
                            successMessage.setText("found " + response.body().size() + " doctors");
                            showResults();
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
private void showResults() {
    results.removeAllViews();
    for(Doctor doctor: doctors){
        final LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(params);
        linearLayout.setBackground(this.getResources().getDrawable(R.drawable.background_appointment));
        linearLayout.setClickable(true);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setId(doctor.getId());
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        results.addView(linearLayout);


        TextView tv1 = new TextView(this);
        tv1.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));
        tv1.setTextSize(25.0f);
        tv1.setLayoutParams(txt_params);
        tv1.setText("Name: "+doctor.getFirstName()+" "+doctor.getLastName());
        tv1.setGravity(Gravity.LEFT);
        linearLayout.addView(tv1);

        TextView tv2 = new TextView(this);
        tv2.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));
        tv2.setTextSize(25.0f);
        tv2.setLayoutParams(txt_params);
        tv2.setText("Specialisation: "+doctor.getSpecialization());
        tv2.setGravity(Gravity.LEFT);
        linearLayout.addView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));
        tv3.setTextSize(25.0f);
        tv3.setLayoutParams(txt_params);
        tv3.setText("City: "+doctor.getCity());
        tv3.setGravity(Gravity.LEFT);
        linearLayout.addView(tv3);

        TextView tv4 = new TextView(this);
        tv4.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));
        tv4.setTextSize(25.0f);
        tv4.setLayoutParams(txt_params);
        tv4.setText("Workplace: "+doctor.getWorkplace());
        tv4.setGravity(Gravity.LEFT);
        linearLayout.addView(tv4);
    }


    }

}
