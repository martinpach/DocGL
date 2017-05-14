package com.wdfeww.docgl;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wdfeww.docgl.data.methods.Checker;
import com.wdfeww.docgl.data.methods.JsonReqestBody;
import com.wdfeww.docgl.data.methods.NavigationMenu;
import com.wdfeww.docgl.data.model.Patient;
import com.wdfeww.docgl.data.remote.Service;
import com.wdfeww.docgl.data.remote.ServiceGenerator;

import org.json.JSONObject;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity {
    Toolbar toolbar;
    DrawerLayout drawer_layout;
    NavigationView nav_view;
    String token;
    Button btn2, btn1;
    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, errorMessage, successMessage;
    EditText et1, et2, et3, et4, et6, et7;
    LinearLayout.LayoutParams params, btn_params, text_params, sub_text_params;
    LinearLayout main_layout;
    TextView logged_user;
    Class className;
    Patient patient;
    NavigationMenu navigationMenu;
    int i = 0;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        Bundle bundle = this.getIntent().getExtras();
            patient = bundle.getParcelable("patient");
            token = getIntent().getStringExtra("token");

        className = getClass();
        nav_view = (NavigationView) findViewById(R.id.nav_view);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        logged_user = new TextView(this);
        logged_user.setTextAppearance(this, R.style.profile_text);
        navigationMenu = new NavigationMenu(token, patient, this, toolbar, drawer_layout, nav_view, className);
        navigationMenu.initMenu();

        showProfile();

    }

    @Override
    public void onBackPressed() {

        if(i==1)
            navigationMenu.redirect(Profile.class);
        else
            navigationMenu.redirect();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showProfile() {

        main_layout.removeAllViews();
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 50, 0, 0);
        btn_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        btn_params.setMargins(10, 65, 10, 0);
        text_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        text_params.setMargins(20, 30, 0, 0);
        sub_text_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        sub_text_params.setMargins(30, 15, 30, 0);

        tv1 = new TextView(this);
        tv1.setText(this.getResources().getString(R.string.myProfile));
        tv1.setLayoutParams(params);
        tv1.setGravity(Gravity.LEFT);
        tv1.setTextAppearance(this, R.style.home_text);
        main_layout.addView(tv1);

        tv2 = new TextView(this);
        tv2.setText(this.getResources().getString(R.string.firstname));
        tv2.setLayoutParams(text_params);
        tv2.setGravity(Gravity.LEFT);
        tv2.setTextAppearance(this, R.style.profile_text);
        main_layout.addView(tv2);

        tv3 = new TextView(this);
        tv3.setText(patient.getFirstName());
        tv3.setLayoutParams(sub_text_params);
        tv3.setGravity(Gravity.LEFT);
        tv3.setTextAppearance(this, R.style.profile_sub_text);
        main_layout.addView(tv3);

        tv4 = new TextView(this);
        tv4.setText(this.getResources().getString(R.string.lastName));
        tv4.setLayoutParams(text_params);
        tv4.setGravity(Gravity.LEFT);
        tv4.setTextAppearance(this, R.style.profile_text);
        main_layout.addView(tv4);

        tv5 = new TextView(this);
        tv5.setText(patient.getLastName());
        tv5.setLayoutParams(sub_text_params);
        tv5.setGravity(Gravity.LEFT);
        tv5.setTextAppearance(this, R.style.profile_sub_text);
        main_layout.addView(tv5);

        tv6 = new TextView(this);
        tv6.setText(this.getResources().getString(R.string.email));
        tv6.setLayoutParams(text_params);
        tv6.setGravity(Gravity.LEFT);
        tv6.setTextAppearance(this, R.style.profile_text);
        main_layout.addView(tv6);

        tv7 = new TextView(this);
        tv7.setText(patient.getEmail());
        tv7.setLayoutParams(sub_text_params);
        tv7.setGravity(Gravity.LEFT);
        tv7.setTextAppearance(this, R.style.profile_sub_text);
        main_layout.addView(tv7);

        tv8 = new TextView(this);
        tv8.setText(this.getResources().getString(R.string.username));
        tv8.setLayoutParams(text_params);
        tv8.setGravity(Gravity.LEFT);
        tv8.setTextAppearance(this, R.style.profile_text);
        main_layout.addView(tv8);

        tv9 = new TextView(this);
        tv9.setText(patient.getUserName());
        tv9.setLayoutParams(sub_text_params);
        tv9.setGravity(Gravity.LEFT);
        tv9.setTextAppearance(this, R.style.profile_sub_text);
        main_layout.addView(tv9);

        btn1 = new Button(this);
        btn1.setText(this.getResources().getString(R.string.editProfile));
        btn1.setGravity(Gravity.CENTER);
        btn1.setLayoutParams(btn_params);
        btn1.setBackground(this.getResources().getDrawable(R.drawable.btn_transparent_background));
        btn1.setTextAppearance(this, R.style.btn_transparent);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });
        main_layout.addView(btn1);


        btn2 = new Button(this);
        btn2.setText(this.getResources().getString(R.string.changePassword));
        btn2.setGravity(Gravity.CENTER);
        btn2.setLayoutParams(btn_params);
        btn2.setBackground(this.getResources().getDrawable(R.drawable.btn_transparent_background));
        btn2.setTextAppearance(this, R.style.btn_transparent);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
        main_layout.addView(btn2);
    }

    private void editProfile() {
        main_layout.removeAllViews();

        tv1.setText(this.getResources().getString(R.string.editProfile));
        main_layout.addView(tv1);
        main_layout.addView(tv2);
        et1 = new EditText(this);
        et1.setText(patient.getFirstName());
        et1.setLayoutParams(sub_text_params);
        et1.setInputType(InputType.TYPE_CLASS_TEXT);
        main_layout.addView(et1);

        main_layout.addView(tv4);
        et2 = new EditText(this);
        et2.setText(patient.getLastName());
        et2.setLayoutParams(sub_text_params);
        et2.setInputType(InputType.TYPE_CLASS_TEXT);
        main_layout.addView(et2);

        main_layout.addView(tv6);

        et3 = new EditText(this);
        et3.setText(patient.getEmail());
        et3.setLayoutParams(sub_text_params);
        et3.setInputType(InputType.TYPE_CLASS_TEXT);
        main_layout.addView(et3);

        main_layout.addView(tv8);

        main_layout.addView(tv9);
        errorMessage.setLayoutParams(sub_text_params);
        main_layout.addView(errorMessage);

        successMessage.setLayoutParams(sub_text_params);
        main_layout.addView(successMessage);

        btn1.setText(this.getResources().getString(R.string.save));
        btn1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

              patient.setFirstName(et1.getText().toString().trim());
              patient.setLastName(et2.getText().toString().trim());
              patient.setEmail(et3.getText().toString().trim());
                updateProfile();


            }


        });
        main_layout.addView(btn1);
    }
    private void updateProfile() {
        JSONObject json = JsonReqestBody.updateProfile(patient.getFirstName(), patient.getLastName(), patient.getEmail());
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (json.toString()));
        Service loginService =
                ServiceGenerator.createService(Service.class, token);
        Call<ResponseBody> call = loginService.updateProfile(patient.getId(), body);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    successMessage.setText("Profile was changed.");
                    successMessage.setVisibility(View.VISIBLE);
                    errorMessage.setVisibility(View.GONE);
                    showProfile();
                }else {
                    errorMessage.setText("Profile was not changed.");
                    successMessage.setVisibility(View.GONE);
                    errorMessage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                errorMessage.setText("Server not working or no internet connection.");
                successMessage.setVisibility(View.GONE);
                errorMessage.setVisibility(View.VISIBLE);
            }
        });

    }
    private void changePassword() {
        main_layout.removeAllViews();

        tv1.setText(this.getResources().getString(R.string.changePassword));
        main_layout.addView(tv1);

        tv4.setText(this.getResources().getString(R.string.newPassword));
        main_layout.addView(tv4);

        et6 = new EditText(this);
        et6.setHint(this.getResources().getString(R.string.type_new_password));
        et6.setHintTextColor(this.getResources().getColor(R.color.color6));
        et6.setLayoutParams(sub_text_params);
        et6.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);
        et6.setTransformationMethod(PasswordTransformationMethod.getInstance());
        main_layout.addView(et6);

        et7 = new EditText(this);
        et7.setHint(this.getResources().getString(R.string.confirm_new_password));
        et7.setHintTextColor(this.getResources().getColor(R.color.color6));
        et7.setLayoutParams(sub_text_params);
        et7.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);
        et7.setTransformationMethod(PasswordTransformationMethod.getInstance());
        main_layout.addView(et7);

        errorMessage.setLayoutParams(sub_text_params);
        main_layout.addView(errorMessage);

        successMessage.setLayoutParams(sub_text_params);
        main_layout.addView(successMessage);

        btn1.setText(this.getResources().getString(R.string.changePassword));
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Checker.isPasswordValid(et6.getText().toString().trim())) {
                    errorMessage.setVisibility(View.GONE);
                    if (et6.getText().toString().trim().equals(et7.getText().toString().trim())) {
                        errorMessage.setVisibility(View.GONE);
                        changePasswordCall(et7.getText().toString().trim());

                    } else {
                        errorMessage.setVisibility(View.VISIBLE);
                        errorMessage.setText("Passwords are not same!");
                    }
                } else {
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText("New password must contain of minimum: one lower case, one upper case character, one number, one special character and minimum length of password is six characters.");
                }
            }
        });
        main_layout.addView(btn1);
    }

    private void changePasswordCall(String newPassword) {

        JSONObject json = JsonReqestBody.changePass(newPassword);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (json.toString()));
        Service loginService =
                ServiceGenerator.createService(Service.class, token);
        Call<ResponseBody> call = loginService.changePassword(patient.getId(), body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    errorMessage.setVisibility(View.GONE);
                    successMessage.setText("Password was changed!");
                    successMessage.setVisibility(View.VISIBLE);
                    navigationMenu.redirect(AppLogin.class);

                } else {
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText("Password can not be same as old one!");
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
