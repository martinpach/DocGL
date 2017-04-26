package com.wdfeww.docgl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by wdfeww on 4/25/17.
 */

public class Home extends AppCompatActivity {
    TextView responseText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        String s = getIntent().getStringExtra("PATIENT_RESPONSE");
        responseText.setText(s);
    }
}
