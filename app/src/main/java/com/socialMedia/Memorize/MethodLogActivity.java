package com.socialMedia.Memorize;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MethodLogActivity extends AppCompatActivity {

    Button btnGoogle,btnEmail,btnPhone;
    FirebaseAuth auth;
    GoogleSignInOptions gso;
    GoogleSignInClient googleSignInClient;
    int RESULT_CODE_SINGIN=999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_method_log);
        
        btnGoogle=findViewById(R.id.btn_google);
        btnEmail=findViewById(R.id.btn_email);
        btnPhone=findViewById(R.id.btn_phone);
        auth=FirebaseAuth.getInstance();

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MethodLogActivity.this,mailLogActivity.class));
                finish();
            }
        });
        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MethodLogActivity.this,LogInPhoneActivity.class));
                finish();
            }
        });

        
    }

    public void onClickInsta(View view){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/photo.255/")));
    }
    public void onClickEmail(View view){
        Intent i=new Intent(Intent.ACTION_SEND);
        i.setData(Uri.parse("email"));//
        i.putExtra(Intent.EXTRA_EMAIL,new String[]{"gafarasem255@gmail.com"});
        i.setType("message/rfc822");
        startActivity(Intent.createChooser(i,"Launch Email"));

    }
    public void onLogGoogle(View view){
        // Configure Google Sign In
        gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

         googleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent singInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(singInIntent,RESULT_CODE_SINGIN);
    }


    //to sign in with gmail
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_CODE_SINGIN) {        //just to verify the code
            //create a Task object and use GoogleSignInAccount from Intent and write a separate method to handle singIn Result.

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if(task.isSuccessful()) {

                    Toast.makeText(MethodLogActivity.this, "Signed In successfully", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, MainActivity.class));
                finish();
            }
            else { Toast.makeText(this, "Signed In Failed", Toast.LENGTH_SHORT).show();}
        }
    }

    public void onClickLogout(View view) {

        auth.signOut();
        finish();
        Toast.makeText(this, "Sign out Done", Toast.LENGTH_SHORT).show();

    }

    ///////////////////////

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser==null){
            Button logout=findViewById(R.id.btn_LogOut);
            logout.setEnabled(false);
            logout.setVisibility(View.GONE);
        }
    }


}