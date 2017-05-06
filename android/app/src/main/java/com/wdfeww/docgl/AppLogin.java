package com.wdfeww.docgl;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


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


import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AppLogin extends AppCompatActivity {

    private Button login, sign_up;
    private EditText password, username;
    private TextView errorMessageUsername, errorMessagePassword, successMessage;
    private APIService mAPIService;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_login);

        login = (Button) findViewById(R.id.btn_login);
        sign_up = (Button) findViewById(R.id.sign_up);
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
                        if (Checker.isNameValid(username.getText().toString().trim())) {
                            errorMessageUsername.setText("");
                            if (Checker.isPasswordValid(password.getText().toString().trim())) {
                                login();
                            } else {
                                errorMessageUsername.setText("Invalid password.");
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
        JSONObject json = JsonReqestBody.login(username.getText().toString().trim(), password.getText().toString().trim());
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (json.toString()));

        final Call<User> call = mAPIService.userLogin(body);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    errorMessagePassword.setText("");
                    successMessage.setText("Login success!");
                    user = response.body();
                    redirectToHome();
                } else {
                    errorMessagePassword.setText("Incorrect username or password!");
                    successMessage.setText("");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                errorMessagePassword.setText("Server not responding!");
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
