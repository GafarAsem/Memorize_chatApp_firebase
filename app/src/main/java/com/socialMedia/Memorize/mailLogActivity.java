package com.socialMedia.Memorize;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class mailLogActivity extends AppCompatActivity {

    FirebaseAuth auth ;
    EditText ETmail,ETpassword;

    Button btnsignIn,btnSignup;
   // @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_log);

        auth=FirebaseAuth.getInstance();
        ETmail=findViewById(R.id.ETmail);
        ETpassword=findViewById(R.id.ETpassword);

        btnsignIn=findViewById(R.id.signinbtn);
        btnSignup=findViewById(R.id.signupbtn);

    }

    public void signIn(View view){

        String Emailuser=ETmail.getText().toString();
        String passuser=ETpassword.getText().toString();
        if(Emailuser.matches("")){
            ETmail.setError("Enter your email");
        }
        if(passuser.length()<6){
            ETpassword.setError("Enter your password");
        }

        if(!Emailuser.matches("")&& !(passuser.length()<6)){
            btnsignIn.setText("Waite...");
            btnsignIn.setEnabled(false);
            btnSignup.setEnabled(false);
            Task<AuthResult> signin = auth.signInWithEmailAndPassword(Emailuser, passuser);
            signin.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(mailLogActivity.this, "Sign in done", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(mailLogActivity.this,MainActivity.class));
                    mailLogActivity.this.finish();
                }
            });
            signin.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    btnsignIn.setText("sign in");
                    btnsignIn.setEnabled(true);
                    btnSignup.setEnabled(true);
                    Toast.makeText(mailLogActivity.this, "email or password is invalid", Toast.LENGTH_LONG).show();
                }
            });

        }

    }
    public void signUp(View v){
        String Emailuser=ETmail.getText().toString();
        String passuser=ETpassword.getText().toString();
        if(Emailuser.matches("")){
            ETmail.setError("Enter your email");
        }
        if(passuser.length()<6){
            ETpassword.setError("Enter your password");
        }

        if(!Emailuser.matches("")&& !(passuser.length()<6)){
            btnSignup.setText("Waite...");
            btnSignup.setEnabled(false);
            btnsignIn.setEnabled(false);
            Task<AuthResult> signin = auth.createUserWithEmailAndPassword(Emailuser, passuser);
            signin.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(mailLogActivity.this, "Sign up done", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(mailLogActivity.this,MainActivity.class));
                    Intent i=new Intent(mailLogActivity.this,Edit_infoActivity.class);
                    i.putExtra("new",true);
                    startActivity(i);
                    mailLogActivity.this.finish();
                }
            });
            signin.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    btnSignup.setText("sign up");
                    btnSignup.setEnabled(true);
                    btnsignIn.setEnabled(true);
                    Toast.makeText(mailLogActivity.this, "Email is already register", Toast.LENGTH_LONG).show();
                }
            });

        }

    }

    public void OnclickForgerpass(View view) {
        startActivity(new Intent(this, ForgetPassActivity.class));
        mailLogActivity.this.finish();
    }
}