package com.example.g_store;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class activity_signup extends AppCompatActivity {
    private FirebaseDatabase FireDB;
    private DatabaseReference mirefe;
    private EditText et1,et2,et3,et4,et5, et6;
    private String txfirstname,txlastname,txemail,txpassword,txconfirmpassword;
    private Double txvirtualm;
    private String emailpattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        et1=findViewById(R.id.firstname);
        et2=findViewById(R.id.lastname);
        et3=findViewById(R.id.email);
        et4=findViewById(R.id.etpassword);
        et5=findViewById(R.id.confirm_password);
        progressDialog= new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }
    public void registrar(View v) {
        txfirstname=et1.getText().toString();
        txlastname=et2.getText().toString();
        txemail=et3.getText().toString().trim();
        txpassword=et4.getText().toString().trim();
        txconfirmpassword= et5.getText().toString().trim();
        txvirtualm = 1000000.0;
        if (validarcampos()){
            if (confirmarcontraseña()){
                mAuth.createUserWithEmailAndPassword(txemail, txpassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Map<String, Object> user = new HashMap<>();
                        user.put("FirstName", txfirstname);
                        user.put("LastName", txlastname);
                        user.put("Email", txemail);
                        user.put("Password", txpassword);
                        user.put("virtualM", txvirtualm);
                        db.collection("users").document(txemail).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(activity_signup.this, "Registro Exitoso !", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(activity_signup.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(activity_signup.this, "Error - " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity_signup.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }


            }
    }


    private boolean validarcampos(){
        boolean validar=true;
        if(et1.getText().toString().isEmpty()){
            validar= false;
            et1.setError(getString(R.string.campo_vacio));
        }else if(et2.getText().toString().isEmpty()){
            validar= false;
            et2.setError(getString(R.string.campo_vacio));
        }else if (et3.getText().toString().isEmpty()){
            if (txemail.matches(emailpattern)){
                validar= true;
            } else {
                et3.setError(getString(R.string.email_invalido));
                validar = false;
            }
            et3.setError(getString(R.string.campo_vacio));
        }else if (et4.getText().toString().isEmpty()){
            validar= false;
            et4.setError(getString(R.string.campo_vacio));
        }else if (et5.getText().toString().isEmpty()){
            validar= false;
            et5.setError(getString(R.string.campo_vacio));
        }
        return validar;
    }

    private boolean confirmarcontraseña(){
        boolean validar= true;
        if (txconfirmpassword.equals(txpassword)){
            Toast.makeText(this, getString(R.string.MSG_ESPERA),
                    Toast.LENGTH_SHORT).show();
        }else {
            et4.setError(getString(R.string.contraseña_no_coincide));
            et5.setError(getString(R.string.contraseña_no_coincide));
            validar= false;
        }
        return validar;
    }

}
