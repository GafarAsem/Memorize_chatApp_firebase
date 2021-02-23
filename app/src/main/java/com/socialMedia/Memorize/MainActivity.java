package com.socialMedia.Memorize;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    NavController navController;
    FloatingActionButton fab;
    FirebaseAuth mAuth;
    static  String ThisUserID;
    DatabaseReference databasePost= FirebaseDatabase.getInstance().getReference("Posts");
    DatabaseReference databaseUser= FirebaseDatabase.getInstance().getReference("Users");;
    FirebaseUser currentUser;
    String email=" ";
    String displayName=" ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ربط المتحكم بعارض الصفحات
        bottomNavigationView=findViewById(R.id.main_bottomNavigation);
        navController= Navigation.findNavController(this,R.id.main_navController);
        fab=findViewById(R.id.main_fab);
        mAuth=FirebaseAuth.getInstance();

        AppBarConfiguration appBarConfiguration=new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(bottomNavigationView,navController);
        //جعل الخلفية شفافة
        bottomNavigationView.setBackground(null);
    }
    public void onClickFab(View view){

        Intent i=new Intent(this,AddPostActivity.class);
        i.putExtra("email",email);
        i.putExtra("name",displayName);
        startActivity(i);
    }

    public void onClickProfile(MenuItem item) {
        startActivity(new Intent(this,MethodLogActivity.class));

    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();

        if(currentUser!=null) {
            Query queryUser = databaseUser.orderByChild("email").equalTo(currentUser.getEmail());

            queryUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            ThisUserID = dataSnapshot.child("userID").getValue(String.class);

                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        }
        else {
            startActivity(new Intent(this,MethodLogActivity.class));

        }
    }

    public void onClickEditInfo(MenuItem item) {

        Intent i=new Intent(this,Edit_infoActivity.class);
        i.putExtra("new",false);
        startActivity(i);

    }

    public void onClickFollowers(View view) {
        Intent i=new Intent(this,ListUserActivity.class);
        i.putExtra("type","followers");
        TextView userText=findViewById(R.id.profile_userName);
        String userID=userText.getText().toString();
        i.putExtra("userID",userText.getText().toString());
        startActivity(i);
    }
    public void onClickFollowing(View view) {
        Intent i=new Intent(this,ListUserActivity.class);
        i.putExtra("type","following");
        TextView userText=findViewById(R.id.profile_userName);

        i.putExtra("userID",userText.getText().toString());
        startActivity(i);
    }

    public void onClickHeartPost(View view) {

        ImageButton heartButton;
        if(view.getId()==R.id.bigPost_heart){
            heartButton=view.findViewById(R.id.bigPost_heart);}
        else  heartButton=view.findViewById(R.id.miniPost_heart);

        int drawable;

        try {
             drawable = (Integer) heartButton.getTag(R.id.like);
        }catch (Exception e){
            drawable=R.drawable.heart;
        }
        String tag= (String)heartButton.getTag(R.id.postID);
        if(drawable==R.drawable.heart) {
            heartButton.setImageResource(R.drawable.heart_white);
            heartButton.setTag(R.id.like,R.drawable.heart_white);
            final String[] userID = new String[1];


            Query queryPost=databasePost.orderByChild("postID").equalTo(tag);
            Query queryUser=databaseUser.orderByChild("email").equalTo(currentUser.getEmail());
            queryUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<String> liked = new ArrayList<String>();
                    if (snapshot.exists()){
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            userID[0] =dataSnapshot.child("userID").getValue(String.class);
                            for (DataSnapshot likes : dataSnapshot.child("liked").getChildren()) {
                                liked.add(likes.getValue(String.class));
                            }
                            liked.add(tag);
                            dataSnapshot.child("liked").getRef().setValue(liked);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            queryPost.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<String> liked = new ArrayList<String>();
                    if (snapshot.exists()){
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                            for (DataSnapshot likes : dataSnapshot.child("liked").getChildren()) {
                                liked.add(likes.getValue(String.class));
                            }
                            liked.add(userID[0]);
                            dataSnapshot.child("liked").getRef().setValue(liked);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        else {
            heartButton.setTag(R.id.like,R.drawable.heart);
            heartButton.setImageResource(R.drawable.heart);
            final String[] userID = new String[1];
            Query queryPost=databasePost.orderByChild("postID").equalTo(tag);
            Query queryUser=databaseUser.orderByChild("email").equalTo(currentUser.getEmail());
            queryUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<String> liked = new ArrayList<String>();
                    if (snapshot.exists()){
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            userID[0] =dataSnapshot.child("userID").getValue(String.class);
                            for (DataSnapshot likes : dataSnapshot.child("liked").getChildren()) {
                                liked.add(likes.getValue(String.class));
                            }
                            liked.remove(tag);
                            dataSnapshot.child("liked").getRef().setValue(liked);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            queryPost.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<String> liked = new ArrayList<String>();
                    if (snapshot.exists()){
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                            for (DataSnapshot likes : dataSnapshot.child("liked").getChildren()) {
                                liked.add(likes.getValue(String.class));
                            }
                            liked.remove(userID[0]);
                            dataSnapshot.child("liked").getRef().setValue(liked);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    public void onClickStarPost(View view) {
        ImageButton starButton;
        if(view.getId()==R.id.bigPost_star){
         starButton=view.findViewById(R.id.bigPost_star);}
        else  starButton=view.findViewById(R.id.miniPost_star);

        int drawable;

        try {
            drawable = (Integer) starButton.getTag(R.id.save);
        }catch (Exception e){
            drawable=R.drawable.star;
        }
        String tag= (String)starButton.getTag(R.id.postID);

        if(drawable==R.drawable.star) {
            starButton.setImageResource(R.drawable.star_white);
            starButton.setTag(R.id.save,R.drawable.star_white);

            final String[] userID = new String[1];
            Query queryPost=databasePost.orderByChild("postID").equalTo(tag);
            Query queryUser=databaseUser.orderByChild("email").equalTo(currentUser.getEmail());

            queryUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<String> saved = new ArrayList<String>();
                    if (snapshot.exists()){
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            userID[0] =dataSnapshot.child("userID").getValue(String.class);
                            for (DataSnapshot likes : dataSnapshot.child("saved").getChildren()) {
                                saved.add(likes.getValue(String.class));
                            }
                            saved.add(tag);
                            dataSnapshot.child("saved").getRef().setValue(saved);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            queryPost.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<String> saved = new ArrayList<String>();
                    if (snapshot.exists()){
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                            for (DataSnapshot likes : dataSnapshot.child("saved").getChildren()) {
                                saved.add(likes.getValue(String.class));
                            }
                            saved.add(userID[0]);
                            dataSnapshot.child("saved").getRef().setValue(saved);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        else
        {
            starButton.setTag(R.id.save,R.drawable.star);
            starButton.setImageResource(R.drawable.star);

            final String[] userID = new String[1];
            Query queryPost=databasePost.orderByChild("postID").equalTo(tag);
            Query queryUser=databaseUser.orderByChild("email").equalTo(currentUser.getEmail());

            queryUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<String> saved = new ArrayList<String>();
                    if (snapshot.exists()){
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            userID[0] =dataSnapshot.child("userID").getValue(String.class);
                            for (DataSnapshot likes : dataSnapshot.child("saved").getChildren()) {
                                saved.add(likes.getValue(String.class));
                            }
                            saved.remove(tag);
                            dataSnapshot.child("saved").getRef().setValue(saved);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            queryPost.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<String> saved = new ArrayList<String>();
                    if (snapshot.exists()){
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                            for (DataSnapshot likes : dataSnapshot.child("saved").getChildren()) {
                                saved.add(likes.getValue(String.class));
                            }
                            saved.remove(userID[0]);
                            dataSnapshot.child("saved").getRef().setValue(saved);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    public void onClickListType(MenuItem item) {
        ProfileFragment.getPostsToArray();
    }

    public void onClickGridType(MenuItem item) {
        ProfileFragment.getMyPostsGrid();
    }

    public void onClickSearchUser(MenuItem item) {

        EditText edSearch=findViewById(R.id.Home_searchText);
        Intent i=new Intent(this,ListUserActivity.class);
        i.putExtra("type","search");
        i.putExtra("userSearch",edSearch.getText().toString());
        startActivity(i);

    }
}