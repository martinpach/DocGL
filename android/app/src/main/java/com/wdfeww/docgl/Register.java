package com.wdfeww.docgl;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wdfeww.docgl.data.methods.Checker;
import com.wdfeww.docgl.data.methods.JsonReqestBody;
import com.wdfeww.docgl.data.model.User;
import com.wdfeww.docgl.data.remote.APIService;
import com.wdfeww.docgl.data.remote.ApiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Register extends AppCompatActivity {
    private Button sign_up;
    private EditText edit_txt_firstname, edit_txt_lastname, edit_txt_email, edit_txt_username, edit_txt_password;
    private TextView errorMessage, successMessage;
    private APIService mAPIService;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        sign_up = (Button) findViewById(R.id.sign_up);
        edit_txt_firstname = (EditText) findViewById(R.id.edit_txt_firstname);
        edit_txt_lastname = (EditText) findViewById(R.id.edit_txt_lastname);
        edit_txt_email = (EditText) findViewById(R.id.edit_txt_email);
        edit_txt_username = (EditText) findViewById(R.id.edit_txt_username);
        edit_txt_password = (EditText) findViewById(R.id.edit_txt_password);
        errorMessage = (TextView) findViewById(R.id.errorMessage);
        successMessage = (TextView) findViewById(R.id.successMessage);
        mAPIService = ApiUtils.getAPIService();

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Checker.isNameValid(edit_txt_firstname.getText().toString().trim())) {
                    if (Checker.isNameValid(edit_txt_lastname.getText().toString().trim())) {
                        if (Checker.isEmailValid(edit_txt_email.getText().toString().trim())) {
                            if (Checker.isNameValid(edit_txt_username.getText().toString().trim())) {
                                if (Checker.isPasswordValid(edit_txt_password.getText().toString().trim())) {
                                    register();
                                } else {
                                    errorMessage.setText("Password must contain of minimum: one lower case, one upper case character, one number, one special character and minimum length of password is six characters");
                                }
                            } else {
                                errorMessage.setText("Please type your username name.");
                            }
                        } else {
                            errorMessage.setText("Please type valid email.");
                        }
                    } else {
                        errorMessage.setText("Please type your lastname name.");
                    }
                } else {
                    errorMessage.setText("Please type your first name.");
                }

            }
        });
    }

    private void register() {
        JSONObject json = JsonReqestBody.register(edit_txt_username.getText().toString().trim(), edit_txt_password.getText().toString().trim(),
                edit_txt_firstname.getText().toString().trim(), edit_txt_lastname.getText().toString().trim(),
                edit_txt_email.getText().toString().trim());
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (json.toString()));
        final Call<User> call = mAPIService.userRegister(body);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    errorMessage.setText("");
                    successMessage.setText("Register success!");
                    user = response.body();
                    redirectToHome();
                } else {
                    errorMessage.setText("Username or email is already used!");
                    successMessage.setText("");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                errorMessage.setText("Server not responding!");
            }
        });
    }

    private void redirectToHome() {
        Intent intent = new Intent(getBaseContext(), Home.class);
        intent.putExtra("firstName", user.getPatient().getFirstName());
        intent.putExtra("lastName", user.getPatient().getLastName());
        intent.putExtra("email", user.getPatient().getEmail());
        intent.putExtra("id", user.getPatient().getId());
        intent.putExtra("token", user.getToken());
        startActivity(intent);
    }

}
