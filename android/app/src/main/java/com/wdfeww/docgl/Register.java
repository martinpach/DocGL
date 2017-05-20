package com.wdfeww.docgl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wdfeww.docgl.data.methods.Checker;
import com.wdfeww.docgl.data.methods.JsonReqestBody;
import com.wdfeww.docgl.data.model.User;
import com.wdfeww.docgl.data.remote.Service;
import com.wdfeww.docgl.data.remote.ServiceGenerator;

import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {
    private Button sign_up;
    private EditText edit_txt_firstname, edit_txt_lastname, edit_txt_email, edit_txt_username, edit_txt_password;
    private TextView errorMessage, successMessage;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
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
                    redirectToHome();
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
        Bundle bundle = new Bundle();
        bundle.putParcelable("patient", user.getPatient());
        intent.putExtra("token", user.getToken());
        intent.putExtras(bundle);
        this.finish();
        startActivity(intent);
    }

}
