package com.wdfeww.docgl;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wdfeww.docgl.data.methods.Checker;

public class Profile extends AppCompatActivity {
    String firstName, lastName, email, token, username;
    Button btn2, btn1;
    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, errorMessage, successMessage;
    EditText et1, et2, et3, et4, et5, et6, et7;
    int id;
    LinearLayout.LayoutParams params, btn_params, text_params, sub_text_params;
    LinearLayout main_layout;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
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
        firstName = getIntent().getStringExtra("firstName");
        lastName = getIntent().getStringExtra("lastName");
        email = getIntent().getStringExtra("email");
        token = getIntent().getStringExtra("token");
        id = getIntent().getIntExtra("id", id);
        username = getIntent().getStringExtra("username");
        showProfile();
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
        tv3.setText(firstName);
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
        tv5.setText(lastName);
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
        tv7.setText(email);
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
        tv9.setText(username);
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
        et1.setText(firstName);
        et1.setLayoutParams(sub_text_params);
        et1.setInputType(InputType.TYPE_CLASS_TEXT);
        main_layout.addView(et1);

        main_layout.addView(tv4);
        et2 = new EditText(this);
        et2.setText(lastName);
        et2.setLayoutParams(sub_text_params);
        et2.setInputType(InputType.TYPE_CLASS_TEXT);
        main_layout.addView(et2);

        main_layout.addView(tv6);
        et3 = new EditText(this);
        et3.setText(email);
        et3.setLayoutParams(sub_text_params);
        et3.setInputType(InputType.TYPE_CLASS_TEXT);
        main_layout.addView(et3);

        main_layout.addView(tv8);
        et4 = new EditText(this);
        et4.setText(username);
        et4.setLayoutParams(sub_text_params);
        et4.setInputType(InputType.TYPE_CLASS_TEXT);
        main_layout.addView(et4);

        btn1.setText(this.getResources().getString(R.string.save));
        btn1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                firstName = et1.getText().toString().trim();
                lastName = et2.getText().toString().trim();
                email = et3.getText().toString().trim();
                username = et4.getText().toString().trim();

                showProfile();
            }
        });
        main_layout.addView(btn1);
    }

    private void changePassword() {
        main_layout.removeAllViews();

        tv1.setText(this.getResources().getString(R.string.changePassword));
        main_layout.addView(tv1);
        tv2.setText(this.getResources().getString(R.string.oldPassword));
        main_layout.addView(tv2);

        et5 = new EditText(this);
        et5.setHint(this.getResources().getString(R.string.type_old_password));
        et5.setHintTextColor(this.getResources().getColor(R.color.color6));
        et5.setLayoutParams(sub_text_params);
        et5.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);
        et5.setTransformationMethod(PasswordTransformationMethod.getInstance());
        main_layout.addView(et5);

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
                if(Checker.isPasswordValid(et6.getText().toString().trim())){
                    errorMessage.setVisibility(View.GONE);
                    if(et6.getText().toString().trim().equals(et7.getText().toString().trim())){
                        errorMessage.setVisibility(View.GONE);
                        successMessage.setVisibility(View.VISIBLE);
                        successMessage.setText("Password was changed!");
                    }else{
                        errorMessage.setVisibility(View.VISIBLE);
                        errorMessage.setText("Passwords are not same!");
                    }
                }else{
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText("New password must contain of minimum: one lower case, one upper case character, one number, one special character and minimum length of password is six characters.");
                }
            }
        });
        main_layout.addView(btn1);
    }
}