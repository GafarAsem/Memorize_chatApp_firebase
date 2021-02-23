package com.socialMedia.Memorize;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassActivity extends AppCompatActivity {

    EditText emailText;
    Button btnSend;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);

        emailText = findViewById(R.id.ETmailForget);
        btnSend = findViewById(R.id.sendPassBtn);
        auth = FirebaseAuth.getInstance();
    }
    public void setBtnSend(View view){
        String Emailuser=emailText.getText().toString();

        if(Emailuser.matches("")){
            emailText.setError("Enter your email");
        }
        else{
            btnSend.setEnabled(false);
            btnSend.setText("waite ...");
            Task<Void> task = auth.sendPasswordResetEmail(Emailuser);
            task.addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    btnSend.setEnabled(true);
                    btnSend.setText("Send again");
                    Toast.makeText(ForgetPassActivity.this, "Reset password sent", Toast.LENGTH_SHORT).show();
                }
            });
            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ForgetPassActivity.this, "email is invalid", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}