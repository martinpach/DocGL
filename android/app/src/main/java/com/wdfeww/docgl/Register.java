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


public class Register extends AppCompatActivity {
    private Button sign_up;
    private EditText edit_txt_firstname, edit_txt_lastname, edit_txt_email, edit_txt_username, edit_txt_password;
    private TextView errorMessage, successMessage;
    private APIService mAPIService;
    String res;

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
                if (checkFirstname() && checkLastname() && checkEmail() && checkUsername() && checkUPassword()) {
                    String jsonParams = "{\"userName\":\"" + edit_txt_username.getText().toString().trim() + "\",\"password\":\"" + edit_txt_password.getText().toString().trim() + "\",\"firstName\":\""
                            + edit_txt_lastname.getText().toString().trim() + "\",\"lastName\":\"" + edit_txt_lastname.getText().toString().trim() + "\",\"email\":\"" + edit_txt_email.getText().toString().trim()
                            + "\",\"userType\":\"PATIENT\"}";
                    RequestBody body = null;
                    try {

                        body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final Call<ResponseBody> response = mAPIService.userRegister(body);
                    response.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> rawResponse) {
                            try {
                                if (rawResponse.isSuccessful()) {
                                    errorMessage.setText("");
                                    successMessage.setText("Registration success!");
                                    res = rawResponse.body().string();
                                    login();
                                } else {
                                    errorMessage.setText("Username or email is already used!");
                                    successMessage.setText("");
                                }

                            } catch (Exception e) {
                                errorMessage.setText("Server not responding!");
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                            errorMessage.setText("Server not responding!");
                        }


                    });
                }

            }


        });
    }

    private void login() {
        Intent intent = new Intent(getApplicationContext(), Home.class);
        intent.putExtra("JSON_RESPONSE", res);
        startActivity(intent);
    }

    private boolean checkFirstname() {
        if (edit_txt_firstname.getText().length() == 0) {
            errorMessage.setText("Please type your firstname.");
            return false;
        } else {
            errorMessage.setText("");
            return true;
        }
    }

    private boolean checkLastname() {
        if (edit_txt_lastname.getText().length() == 0) {
            errorMessage.setText("Please type your lastname.");
            return false;
        } else {
            errorMessage.setText("");
            return true;
        }
    }

    private boolean checkUsername() {
        if (edit_txt_username.getText().length() == 0) {
            errorMessage.setText("Please type your username.");
            return false;
        } else {
            errorMessage.setText("");
            return true;
        }
    }

    private boolean checkUPassword() {
        String string_pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}";
        Pattern pattern = Pattern.compile(string_pattern);
        Matcher matcher = pattern.matcher(edit_txt_password.getText());
        Log.i("DEBUG", "" + matcher.matches());

        if (edit_txt_password.getText().length() == 0) {
            errorMessage.setText("Please type your password.");
            return false;
        } else if (!matcher.matches()) {
            errorMessage.setText("Password must contain of minimum: one lower case, one upper case character, one number, one special character and minimum length of password is six characters.");
            return false;
        } else {
            errorMessage.setText("");
            return true;
        }
    }

    private boolean checkEmail() {
        String string_pattern = "(^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$)";
        Pattern pattern = Pattern.compile(string_pattern);
        Matcher matcher = pattern.matcher(edit_txt_email.getText());
        Log.i("DEBUG", "" + matcher.matches());

        if (edit_txt_email.getText().length() == 0) {
            errorMessage.setText("Please type your email.");
            return false;
        } else if (!matcher.matches()) {
            errorMessage.setText("Wrong email.");
            return false;
        } else {
            errorMessage.setText("");
            return true;
        }
    }
}
