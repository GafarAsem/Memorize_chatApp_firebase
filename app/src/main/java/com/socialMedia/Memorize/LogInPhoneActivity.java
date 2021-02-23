package com.socialMedia.Memorize;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class LogInPhoneActivity extends AppCompatActivity {

    CountryCodePicker codeCountry;
    EditText phoneText;
    LinearLayout linearLayout;
    PinView pincode;
    final String[] verificationId = new String[1];
    Button okbtn;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_phone);

        codeCountry=findViewById(R.id.codePicker);
        phoneText=findViewById(R.id.ED_phone);
        linearLayout=findViewById(R.id.linearLayout);
        pincode=findViewById(R.id.pinCode);
        okbtn=findViewById(R.id.okbtn);

        mAuth=FirebaseAuth.getInstance();



    }

    public void onClickNext(View view) {
        if(!phoneText.getText().toString().matches("")&&okbtn.getText().toString().equals("Send Code")){
           String userPhone=codeCountry.getSelectedCountryCodeWithPlus()+""+phoneText.getText().toString();
            sendCode(userPhone);
            phoneText.setEnabled(false);
            codeCountry.setEnabled(false);
            okbtn.setEnabled(false);
            okbtn.setText("Waite...");
        }
        else if(okbtn.getText().toString().equals("Verification")&&!pincode.getText().toString().matches("")){
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId[0], pincode.getText().toString());
            signInWithPhoneAuthCredential(credential);
        }
        else{
            phoneText.setError("Enter Your Number");
        }
    }

    private void sendCode(String userPhone) {

        String code = null;

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                okbtn.setEnabled(true); //pincode.setVisibility(View.VISIBLE);
                okbtn.setText("Send Code");
                FirebaseCrashlytics.getInstance().recordException(e);
                Toast.makeText(LogInPhoneActivity.this, "Code not sent...", Toast.LENGTH_LONG).show();
                phoneText.setEnabled(true);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                okbtn.setEnabled(true); pincode.setVisibility(View.VISIBLE);
                okbtn.setText("Verification");
                Toast.makeText(LogInPhoneActivity.this, "Code sent ...", Toast.LENGTH_LONG).show();
                verificationId[0] = s;
            }


        }
        ;
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(userPhone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);



    }

     void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Toast.makeText(LogInPhoneActivity.this, "Log in user", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LogInPhoneActivity.this,MainActivity.class));
                           LogInPhoneActivity.this.finish();



                        } else {
                            // Sign in failed, display a message and update the UI

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(LogInPhoneActivity.this, "The code is wrong", Toast.LENGTH_SHORT).show();
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

}