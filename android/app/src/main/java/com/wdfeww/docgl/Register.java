package com.wdfeww.docgl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class Register extends AppCompatActivity {
    private Button sign_up;
    private EditText edit_txt_firstname, edit_txt_lastname, edit_txt_email, edit_txt_username, edit_txt_password;
    private TextView errorMessage, successMessage;
    private User user;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        prefs = this.getSharedPreferences("com.wdfeww.docgl", Context.MODE_PRIVATE);
        sign_up = (Button) findViewById(R.id.sign_up);
        sign_up.setEnabled(true);
        edit_txt_firstname = (EditText) findViewById(R.id.edit_txt_firstname);
        edit_txt_lastname = (EditText) findViewById(R.id.edit_txt_lastname);
        edit_txt_email = (EditText) findViewById(R.id.edit_txt_email);
        edit_txt_username = (EditText) findViewById(R.id.edit_txt_username);
        edit_txt_password = (EditText) findViewById(R.id.edit_txt_password);
        errorMessage = (TextView) findViewById(R.id.errorMessage);
        successMessage = (TextView) findViewById(R.id.successMessage);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Checker.isNameValid(edit_txt_firstname.getText().toString().trim())) {
                    errorMessage.setText("");
                    if (Checker.isNameValid(edit_txt_lastname.getText().toString().trim())) {
                        errorMessage.setText("");
                        if (Checker.isEmailValid(edit_txt_email.getText().toString().trim())) {
                            errorMessage.setText("");
                            if (Checker.isNameValid(edit_txt_username.getText().toString().trim())) {
                                errorMessage.setText("");
                                if (Checker.isPasswordValid(edit_txt_password.getText().toString().trim())) {
                                    errorMessage.setText("");
                                    register();
                                } else {
                                    errorMessage.setText("Password must contain of minimum: one lower case, one upper case character, one number, one special character and minimum length of password is six characters.");
                                }
                            } else {
                                errorMessage.setText("Please type your username.");
                            }
                        } else {
                            errorMessage.setText("Please type valid email.");
                        }
                    } else {
                        errorMessage.setText("Please type your last name.");
                    }
                } else {
                    errorMessage.setText("Please type your first name.");
                }
            }
        });
    }

    private void register() {
        sign_up.setEnabled(false);
        JSONObject json = JsonReqestBody.register(edit_txt_username.getText().toString().trim(), edit_txt_password.getText().toString().trim(),
                edit_txt_firstname.getText().toString().trim(), edit_txt_lastname.getText().toString().trim(),
                edit_txt_email.getText().toString().trim());
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (json.toString()));
        Service loginService =
                ServiceGenerator.createService(Service.class, "");
        Call<User> call = loginService.userRegister(body);
        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    errorMessage.setText("");
                    successMessage.setText("Registration success!");
                    user = response.body();
                    SharedPreferences.Editor prefsEditor = prefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(user);
                    prefsEditor.putString("LoggedUser", json);
                    json = gson.toJson(user.getPatient());
                    prefsEditor.putString("LoggedPatient", json);
                    prefsEditor.commit();
                    redirectToHome();
                    updateFCMToken();
                    sign_up.setEnabled(false);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                sign_up.setEnabled(true);
                errorMessage.setText("Server not responding!");
            }
        });
    }

    private void redirectToHome() {
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
