package com.socialMedia.Memorize;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {


    TextView userTextView,name,followers,following;
    ImageView profileImage;
    Button btnFollow;

    DatabaseReference database;
    FirebaseUser currentUser;
    LinearLayoutCompat linearLayout;

    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        userTextView =findViewById(R.id.user_userName);
        name=findViewById(R.id.user_Name);
        followers=findViewById(R.id.user_followers);
        following=findViewById(R.id.user_following);
        profileImage=findViewById(R.id.user_image);
        btnFollow=findViewById(R.id.user_btnFollow);
        linearLayout=findViewById(R.id.activityUser_layout);


        database= FirebaseDatabase.getInstance().getReference("Users");
        currentUser= FirebaseAuth.getInstance().getCurrentUser();

        setValues();
    }

    private void setValues() {
        userid=getIntent().getExtras().getString("userID");
        final User[] user = new User[2];

        Thread thread =new Thread(){

            @Override
            public void run() {
                user[0] = new User(userid, database,getApplicationContext());
                user[1]=new User(currentUser.getEmail(), database);
                while (!user[0].isDatachange()||!user[1].isDatachange()) {
                    int i = 0;
                }
                runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void run() {

                        try {
                            String currentEmail=currentUser.getEmail();
                            if(currentEmail.matches(user[0].getEmail())){
                                btnFollow.setVisibility(View.GONE);
                            }

                            String userID=user[1].getUserID();
                            boolean t=user[0].getFollowers().contains(userID);
                            if (t) {

                                btnFollow.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.blueGray));
                                btnFollow.setText("Following");


                            }
                            linearLayout.setVisibility(View.VISIBLE);
                            userTextView.setText(user[0].getUserID());
                            name.setText(user[0].getName());
                            String imageurl=user[0].getPhotoID();
                            Picasso.get().load(imageurl).into(profileImage);
                            int fr, fg;
                            // Toast.makeText(UserActivity.this, "sssssss", Toast.LENGTH_SHORT).show();

                            try {
                                fr = user[0].getFollowers().size();
                            } catch (Exception e) {
                                //Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                fr = 0;
                            }

                            try {
                                fg = user[0].getFollowing().size();
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                fg = 0;
                            }


                            followers.setText("Followers " + fr);
                            following.setText("Following  " + fg);


                        }
                        catch (Exception e) {
                            String i = e.getMessage();
                            //  Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        };
        thread.start();



    }

    public void onClickFollowersUser(View view) {
        Intent i=new Intent(this,ListUserActivity.class);
        i.putExtra("type","followers");
        i.putExtra("userID",userTextView.getText().toString());
        startActivity(i);
    }
    public void onClickFollowingUser(View view) {
        Intent i=new Intent(this,ListUserActivity.class);
        i.putExtra("type","following");
        i.putExtra("userID",userTextView.getText().toString());
        startActivity(i);
    }



    void follow(boolean follow,String fromEmailID,String toUserID){



        User fromEmail=new User(fromEmailID,database);

        User toUser=new User(toUserID,database,getApplicationContext());

        Thread thread=new Thread(){

            @Override
            public void run() {

                while (!fromEmail.isDatachange()||!toUser.isDatachange())
                {

                    int i=0;

                }




                            ArrayList<String> following;
                            ArrayList<String> followers;

                            following = fromEmail.getFollowing();
                            followers = toUser.getFollowers();
                        if(follow) {
                            following.add(toUserID);

                            followers.add(fromEmail.getUserID());
                        }
                        else {
                            following.remove(toUserID);
                            followers.remove(fromEmail.getUserID());
                        }
                            fromEmail.setFollowing(following);
                            toUser.setFollowers(followers);

                        Query query = database.orderByChild("email").equalTo(currentUser.getEmail());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    dataSnapshot.getRef().setValue(fromEmail);

                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        Query Query = database.orderByChild("userID").equalTo(toUserID);
                        Query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    dataSnapshot.getRef().setValue(toUser);

                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });





            }
        };
        thread.start();







    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    public void onClickFollow(View view) {

        if(btnFollow.getText().toString().matches("Following")) {
            btnFollow.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.blue));
            btnFollow.setText("Follow");
            follow(false,currentUser.getEmail(),userid);
        }
        else {
            btnFollow.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.blueGray));
            btnFollow.setText("Following");
            follow(true,currentUser.getEmail(),userid);
        }

    }
}