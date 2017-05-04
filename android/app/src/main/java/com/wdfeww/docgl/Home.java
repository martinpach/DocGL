package com.wdfeww.docgl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


public class Home extends AppCompatActivity {
    TextView responseText, settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        responseText = (TextView) findViewById(R.id.responseText);

        //Intent intent = getIntent();
        //responseText.setText("id: "+intent.getStringExtra("id")+" firstname: "+intent.getStringExtra("firstName")+ " lastname: "+intent.getStringExtra("lastName")+ " email: "+intent.getStringExtra("email")+
        //" token: "+intent.getStringExtra("token"));

        //responseText.setText(intent.getStringExtra("JSON_RESPONSE"));

    }
}
