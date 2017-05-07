package com.wdfeww.docgl;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wdfeww.docgl.data.methods.JsonReqestBody;
import com.wdfeww.docgl.data.model.Apointment;
import com.wdfeww.docgl.data.model.User;
import com.wdfeww.docgl.data.remote.APIService;
import com.wdfeww.docgl.data.remote.ApiUtils;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Home extends AppCompatActivity {

    String firstName, lastName, email, token;
    private APIService mAPIService;
    int id;
    LinearLayout main_layout;
    List<Apointment> apointments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        main_layout = (LinearLayout) findViewById(R.id.main_layout);
        firstName = getIntent().getStringExtra("firstName");
        lastName = getIntent().getStringExtra("lastName");
        email = getIntent().getStringExtra("email");
        token = getIntent().getStringExtra("token");
        id = getIntent().getIntExtra("id", id);
        mAPIService = ApiUtils.getAPIService();
        checkApointments();

    }

    private void checkApointments() {
        final Call<List<Apointment>> call = mAPIService.getPatientAppointments(id);

        call.enqueue(new Callback<List<Apointment>>() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(Call<List<Apointment>> call, Response<List<Apointment>> response) {
                if (response.isSuccessful()) {
                    apointments = response.body();
                    System.out.println(apointments.size());
                    if (apointments.size() < 1) {
                        createDefaultLayout();
                    } else {
                        try {
                            createApointmentListLayout();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Apointment>> call, Throwable t) {
                createErrorLayout();
            }
        });
    }

    private void createErrorLayout() {
        main_layout.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 50, 0, 0);
        TextView tv = new TextView(this);
        tv.setText(this.getResources().getString(R.string.home_error_text));
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setTextAppearance(this, R.style.home_error_text);
        main_layout.addView(tv);
    }

    private void createDefaultLayout() {
        main_layout.removeAllViews();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 50, 0, 0);


        TextView tv1 = new TextView(this);
        tv1.setText(this.getResources().getString(R.string.home_txt1));
        tv1.setLayoutParams(params);
        tv1.setGravity(Gravity.CENTER);
        tv1.setTextAppearance(this, R.style.home_text);
        main_layout.addView(tv1);

        TextView tv2 = new TextView(this);
        tv2.setText(this.getResources().getString(R.string.home_txt2));
        tv2.setLayoutParams(params);
        tv2.setGravity(Gravity.CENTER);
        tv2.setTextAppearance(this, R.style.home_clickable_text);
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeAppointment();
            }
        });
        main_layout.addView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setText(this.getResources().getString(R.string.home_txt3));
        tv3.setLayoutParams(params);
        tv3.setGravity(Gravity.CENTER);
        tv3.setTextAppearance(this, R.style.home_clickable_text);
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
        main_layout.addView(tv3);

        TextView tv4 = new TextView(this);
        tv4.setText(this.getResources().getString(R.string.home_txt4));
        tv4.setLayoutParams(params);
        tv4.setGravity(Gravity.CENTER);
        tv4.setTextAppearance(this, R.style.home_clickable_text);
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSettings();
            }
        });
        main_layout.addView(tv4);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void createApointmentListLayout() throws ParseException {
        main_layout.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 50, 0, 0);

        TextView tv = new TextView(this);
        tv.setText(this.getResources().getString(R.string.home_txt5));
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.LEFT);
        tv.setTextAppearance(this, R.style.home_text);
        main_layout.addView(tv);

        params = new LinearLayout.LayoutParams(getWindowManager().getDefaultDisplay().getWidth() - 40, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 30, 0, 0);

        for (Apointment apointment : apointments) {

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(params);
            linearLayout.setBackground(this.getResources().getDrawable(R.drawable.background_apointment));
            linearLayout.setClickable(true);
            main_layout.addView(linearLayout);

            linearLayout.setOrientation(LinearLayout.VERTICAL);

            SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a");

            TextView tv1 = new TextView(this);
            tv1.setText(sdf1.format(new SimpleDateFormat("yyyy-MM-dd").parse(apointment.getDate())) + " " + sdf2.format(new SimpleDateFormat("hh:mm:ss").parse(apointment.getTime())));
            tv1.setGravity(Gravity.LEFT);
            linearLayout.addView(tv1);

            TextView tv2 = new TextView(this);
            tv2.setText(apointment.getNote());
            tv2.setGravity(Gravity.LEFT);
            linearLayout.addView(tv2);

            TextView tv3 = new TextView(this);
            tv3.setText("Doctor "+apointment.getDoctor().getFirstName()+ " " +apointment.getDoctor().getLastName());
            tv3.setGravity(Gravity.LEFT);
            linearLayout.addView(tv3);
        }

    }

    public void makeAppointment() {

    }

    public void updateProfile() {

    }

    public void userSettings() {

    }
}
