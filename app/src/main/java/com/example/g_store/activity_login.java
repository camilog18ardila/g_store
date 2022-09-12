package com.example.g_store;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class activity_login extends AppCompatActivity {
    private EditText et1,et2,et3,et4,et5;
    private String txfirstname,txlastname,txemail,txpassword,txconfirmpassword;
    private String emailpattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        et1=findViewById(R.id.firstname);
        et2=findViewById(R.id.lastname);
        et3=findViewById(R.id.email);
        et4=findViewById(R.id.etpassword);
        et5=findViewById(R.id.confirm_password);

        mAuth = FirebaseAuth.getInstance();
    }

    public void ver (View v) {
        Intent i=new Intent(this,activity_signup.class);
        startActivity(i);
    }
}