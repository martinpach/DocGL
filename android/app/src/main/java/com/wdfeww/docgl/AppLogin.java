package com.wdfeww.docgl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.wdfeww.docgl.data.remote.APIService;
import com.wdfeww.docgl.data.remote.ApiUtils;

import org.json.JSONException;
import org.json.JSONObject;


import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


public class AppLogin extends AppCompatActivity {

    Button login;
    EditText password, username;
    TextView errorMessageUsername, errorMessagePassword, successMessage, output;
    private APIService mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_login);

        login = (Button) findViewById(R.id.btn_login);
        password = (EditText) findViewById(R.id.edit_txt_password);
        username = (EditText) findViewById(R.id.edit_txt_username);
        errorMessageUsername = (TextView) findViewById(R.id.errorMessageUsername);
        errorMessagePassword = (TextView) findViewById(R.id.errorMessagePassword);
        successMessage = (TextView) findViewById(R.id.successMessage);


        mAPIService = ApiUtils.getAPIService();

        login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkUsername() && checkUPassword()) {
                            String jsonParams = "{\"userName\":\"" + username.getText().toString().trim() + "\",\"password\":\"" + password.getText().toString().trim() + "\"}";
                            RequestBody body = null;
                            try {

                                body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Call<ResponseBody> response = mAPIService.userLogin(body);
                            response.enqueue(new Callback<ResponseBody>() {


                                @Override
                                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> rawResponse) {
                                    try {
                                        if (rawResponse.isSuccessful()) {
                                            errorMessagePassword.setText("");
                                            successMessage.setText("Login success!");
                                            //Intent intent = new Intent(AppLogin.this, Home.class);
                                            //intent.putExtra("PATIENT_RESPONSE", rawResponse.body().string());
                                            //startActivity(intent);
                                        } else {
                                            errorMessagePassword.setText("Incorrect username or password!");
                                            successMessage.setText("");
                                        }

                                    } catch (Exception e) {
                                        errorMessagePassword.setText("Server not responding!");
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                                    errorMessagePassword.setText("Server not responding!");
                                }


                            });


                        }

                    }
                }
        );
    }


    private boolean checkUsername() {
        if (username.getText().length() == 0) {
            errorMessageUsername.setText("Please type your username.");
            return false;
        } else {
            errorMessageUsername.setText("");
            return true;
        }

    }

    private boolean checkUPassword() {
        if (password.getText().length() == 0) {
            errorMessagePassword.setText("Please type your password.");
            return false;
        } else if (password.getText().length() < 6) {
            errorMessagePassword.setText("Invalid password.");
            return false;
        } else {
            errorMessagePassword.setText("");
            return true;
        }

    }
}