package com.wdfeww.docgl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.wdfeww.docgl.data.methods.Checker;
import com.wdfeww.docgl.data.methods.JsonReqestBody;
import com.wdfeww.docgl.data.model.User;
import com.wdfeww.docgl.data.remote.Service;
import com.wdfeww.docgl.data.remote.ServiceGenerator;

import org.json.JSONObject;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppLogin extends AppCompatActivity {

    private Button login, sign_up;
    private EditText password, username;
    private TextView errorMessageUsername, errorMessagePassword, successMessage;
    private User user;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_login);
        prefs = this.getSharedPreferences("com.wdfeww.docgl", Context.MODE_PRIVATE);
        login = (Button) findViewById(R.id.btn_login);
        login.setEnabled(true);
        sign_up = (Button) findViewById(R.id.sign_up);
        password = (EditText) findViewById(R.id.edit_txt_password);
        username = (EditText) findViewById(R.id.edit_txt_username);
        errorMessageUsername = (TextView) findViewById(R.id.errorMessageUsername);
        errorMessagePassword = (TextView) findViewById(R.id.errorMessagePassword);
        successMessage = (TextView) findViewById(R.id.successMessage);

        login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Checker.isNameValid(username.getText().toString().trim())) {
                            errorMessageUsername.setText("");
                            if (Checker.isPasswordValid(password.getText().toString().trim())) {
                                errorMessagePassword.setText("");
                                login();
                            } else {
                                errorMessagePassword.setText("Invalid password.");
                            }
                        } else {
                            errorMessageUsername.setText("Please type your username.");
                        }

                    }
                }
        );

        sign_up.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), Register.class);
                        startActivity(intent);
                    }
                }
        );

    }


    private void login() {
        login.setEnabled(false);
        JSONObject json = JsonReqestBody.login(username.getText().toString().trim(), password.getText().toString().trim());
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (json.toString()));
        Service loginService =
                ServiceGenerator.createService(Service.class, "");
        Call<User> call = loginService.userLogin(body);

        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    login.setEnabled(false);
                    errorMessagePassword.setText("");
                    successMessage.setText("Login success!");
                    user = response.body();

                    SharedPreferences.Editor prefsEditor = prefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(user);
                    prefsEditor.putString("LoggedUser", json);
                    json = gson.toJson(user.getPatient());
                    prefsEditor.putString("LoggedPatient", json);
                    prefsEditor.commit();


                    updateFCMToken();
                    redirect();
                } else {
                    login.setEnabled(true);
                    errorMessagePassword.setText("Incorrect username or password!");
                    successMessage.setText("");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                login.setEnabled(true);
                errorMessagePassword.setText("Server not responding!");
            }
        });
    }

    private void redirect() {
        Intent intent = new Intent(getBaseContext(), Home.class);
        this.finish();
        startActivity(intent);
    }

    private void updateFCMToken(){
        JSONObject json = JsonReqestBody.updateFCMRegistrationToken(FirebaseInstanceId.getInstance().getToken());
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (json.toString()));
        Service loginService =
                ServiceGenerator.createService(Service.class, user.getToken());
        Call<ResponseBody> call = loginService.updatePatientsFCMRegistrationToken(user.getPatient().getId(), body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("DocGL", "Token: token was updated" );


                } else {
                    Log.d("DocGL", "Token: token was not updated" );

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("DocGL", "Token: Server error "+ t.getMessage() );

            }
        });
    }
}
