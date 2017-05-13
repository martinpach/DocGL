package com.wdfeww.docgl;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wdfeww.docgl.data.methods.DateDialog;
import com.wdfeww.docgl.data.methods.ExpandableListAdapter;
import com.wdfeww.docgl.data.methods.JsonReqestBody;
import com.wdfeww.docgl.data.model.Doctor;
import com.wdfeww.docgl.data.model.FreeDateToAppoitnment;
import com.wdfeww.docgl.data.model.Patient;
import com.wdfeww.docgl.data.remote.Service;
import com.wdfeww.docgl.data.remote.ServiceGenerator;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewAppointmentChooseDatetime extends AppCompatActivity {
    Toolbar toolbar;
    Patient patient;
    String token,dateFrom, dateTo;
    Doctor doctor;
    TextView errorMessage;
    LinearLayout results;
    EditText txtdateFrom, txtdateTo;
    List<FreeDateToAppoitnment> appointments;
    LinearLayout.LayoutParams params;
    Button btn_chceckAppointments;
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHashMap;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_appointment_choose_datetime);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        errorMessage = (TextView) findViewById(R.id.errorMessage);
        errorMessage.setTextAppearance(this, R.style.ErrorMessage);
        errorMessage.setBackground(this.getResources().getDrawable(R.drawable.error_message_background));
        errorMessage.setGravity(Gravity.CENTER);
        errorMessage.setVisibility(View.GONE);

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,0,30);
        txtdateFrom = (EditText) findViewById(R.id.txtdateFrom);
        txtdateTo = (EditText) findViewById(R.id.txtdateTo);
        Bundle bundle = this.getIntent().getExtras();
        patient = bundle.getParcelable("patient");
        doctor = bundle.getParcelable("doctor");
        token = getIntent().getStringExtra("token");
        results = (LinearLayout) findViewById(R.id.results);

        listView = new ExpandableListView(this);
        listView.setLayoutParams(params);
        listDataHeader = new ArrayList<>();
        listHashMap = new HashMap<>();

        txtdateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog dialog = new DateDialog(v);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft, "DatePicker");
            }
        });

        txtdateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog dialog = new DateDialog(v);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft, "DatePicker");
            }
        });

        btn_chceckAppointments = (Button) findViewById(R.id.btn_chceckAppointments);
        btn_chceckAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAppointments();
            }
        });
    }

    public void checkAppointments() {
        dateFrom = txtdateFrom.getText().toString().trim();
        dateTo = txtdateTo.getText().toString().trim();
        if (!(dateFrom.isEmpty())) {
            errorMessage.setVisibility(View.GONE);
            if (dateTo.isEmpty()) {
                dateTo=dateFrom;
                showAppointments();
            } else {
                showAppointments();
            }
        } else {
            errorMessage.setVisibility(View.VISIBLE);
            errorMessage.setText("Please choose date");
        }
    }

    private void showAppointments() {
        JSONObject json = JsonReqestBody.FreeDateToAppoitnment(doctor.getId(), dateFrom, dateTo);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (json.toString()));
        Service loginService =
                ServiceGenerator.createService(Service.class, "");
        Call<List<FreeDateToAppoitnment>> call = loginService.getFreeDateToAppoitnment(body);

        call.enqueue(new Callback<List<FreeDateToAppoitnment>>() {

            @Override
            public void onResponse(Call<List<FreeDateToAppoitnment>> call, Response<List<FreeDateToAppoitnment>> response) {
                if (response.isSuccessful()) {
                    appointments = response.body();
                    errorMessage.setVisibility(View.GONE);
                    if(appointments.isEmpty()){
                        errorMessage.setVisibility(View.VISIBLE);
                        errorMessage.setText("Doctor have not able to make appointment with you in chosen date.");
                    }else{
                        errorMessage.setVisibility(View.GONE);
                       fillListOfAppointmets();

                    }
                } else {
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText("Incorrect date chosen!");

                }
            }

            @Override
            public void onFailure(Call<List<FreeDateToAppoitnment>> call, Throwable t) {
                errorMessage.setVisibility(View.VISIBLE);
                errorMessage.setText("Server not responding!");
            }
        });
    }


    private void fillListOfAppointmets() {
        int count = 0;
        for(FreeDateToAppoitnment appointment: appointments){
            listDataHeader.add(appointment.getDate());
            listHashMap.put(listDataHeader.get(count),appointment.getTimes());
            count++;
        }
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listHashMap);
        listView.setAdapter(listAdapter);
        results.addView(listView);
    }

}