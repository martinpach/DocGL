package com.wdfeww.docgl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


public class Home extends AppCompatActivity {
    TextView responseText;
    String firstName,lastName,email,token;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        responseText = (TextView) findViewById(R.id.responseText);
        firstName = getIntent().getStringExtra("firstName");
        lastName = getIntent().getStringExtra("lastName");
        email = getIntent().getStringExtra("email");
        token = getIntent().getStringExtra("token");
        id = getIntent().getIntExtra("id",id);
        //responseText.setText("id: "+intent.getStringExtra("id")+" firstname: "+intent.getStringExtra("firstName")+ " lastname: "+intent.getStringExtra("lastName")+ " email: "+intent.getStringExtra("email")+
        //" token: "+intent.getStringExtra("token"));

      responseText.setText("logged as: " + firstName + " " + lastName + " " + email+ " "+ id + " "+ token);

    }
}
