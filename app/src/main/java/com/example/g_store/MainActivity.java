package com.example.g_store;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private EditText et1, et2;
    private String txemail, txpassword;
    private String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et1 = findViewById(R.id.etEmail);
        et2 = findViewById(R.id.etPassword);

        mAuth = FirebaseAuth.getInstance();
    }

    public void ver(View v) {
        Intent i = new Intent(this, Gamesedit.class);
        startActivity(i);
    }

    public void iniciarsesion(View v) {
        txemail = et1.getText().toString().trim();
        txpassword = et2.getText().toString().trim();

        if (!TextUtils.isEmpty(txemail)) {
            if (txemail.matches(emailpattern)) {
                if (!TextUtils.isEmpty(txpassword)) {
                    mAuth.signInWithEmailAndPassword(txemail, txpassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(MainActivity.this, "Inicio de Sesion Exitoso !", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, MenuInicio.class);
                            intent.putExtra("Email", txemail);
                            startActivity(intent);
                            finish();
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Error - " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    et1.setError("el campo esta vacio");
                }
            } else {
                et2.setError("el campo no cumple los requisitos de un correo");
            }
        } else {
            et1.setError("El campo esta vacio");
        }

    }

    private void guardarEstadobutton() {
        SharedPreferences preferences = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        boolean estado=true;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("estado_usuario", estado);
        editor.commit();
    }
}