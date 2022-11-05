package com.example.g_store;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {
    private EditText editTextEmailResetPassword;
    private TextView textViewMsg;
    private Button buttonResetPassword;
    private String email;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        mAuth= FirebaseAuth.getInstance();
        progressDialog= new ProgressDialog(this);

        editTextEmailResetPassword=(EditText) findViewById(R.id.editTextResetPassword);
        buttonResetPassword= (Button) findViewById(R.id.buttonResetPassword);
        textViewMsg=(TextView) findViewById(R.id.textViewMsg);
        textViewMsg.setVisibility(View.INVISIBLE);

        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email= editTextEmailResetPassword.getText().toString();
                if (!email.isEmpty()){
                    progressDialog.setMessage(getString(R.string.progress_mensaje_reset));
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    resetPassword();
                }else{
                    editTextEmailResetPassword.setError(getString(R.string.campo_vacio));
                }
            }
        });


    }
    public void resetPassword(){
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    textViewMsg.setVisibility(View.VISIBLE);
                    textViewMsg.setText(getString(R.string.msg_reset_isSuccessful));
                    editTextEmailResetPassword.setVisibility(View.INVISIBLE);
                    buttonResetPassword.setVisibility(View.INVISIBLE);
                }else{
                    textViewMsg.setText(getString(R.string.msg_rest_setError));
                    textViewMsg.setVisibility(View.VISIBLE);
                }
                progressDialog.dismiss();
            }
        });
    }
}