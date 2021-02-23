package com.socialMedia.Memorize;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.function.Consumer;

public class  User {

    String Email,UserID,Name,PhotoID;

    ArrayList<String> Followers;
    ArrayList<String> Following;
    ArrayList<String> Liked;
    ArrayList<String> Saved;
    ArrayList<String> Posts;


    public boolean datachange=false;

    public boolean isDatachange() {
        return datachange;
    }

    public User(String email, String userID, String name, String photoID) {
        Email = email;
        UserID = userID;
        Name = name;
        PhotoID=photoID;
        Followers= new ArrayList<String>();
        Following= new ArrayList<String>();
        Liked= new ArrayList<String>();
        Saved= new ArrayList<String>();
        Posts= new ArrayList<String>();

    }

    public User(String email, DatabaseReference database) {
        Email = email;
        Name=null;
        Followers= new ArrayList<String>();
        Following= new ArrayList<String>();
        Liked= new ArrayList<String>();
        Saved= new ArrayList<String>();
        Posts= new ArrayList<String>();
        setValues(email,database);
    }

    public User(String userID, DatabaseReference database,Context context) {
        Email = null;
        Name=userID;
        Followers= new ArrayList<String>();
        Following= new ArrayList<String>();
        Liked= new ArrayList<String>();
        Saved= new ArrayList<String>();
        Posts= new ArrayList<String>();
        setValuesWithUser(userID,database);
    }

    private void setValuesWithUser(String userID,  DatabaseReference frb) {
        Query query = frb.orderByChild("userID").equalTo(userID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                        Email = dataSnapshot.child("email").getValue(String.class);
                        UserID = dataSnapshot.child("userID").getValue(String.class);
                        Name = dataSnapshot.child("name").getValue(String.class);

                        try{
                            dataSnapshot.child("posts").getChildren().forEach(new Consumer<DataSnapshot>() {
                                @Override
                                public void accept(DataSnapshot dataSnapshot) {
                                    Posts.add(dataSnapshot.getValue(String.class));
                                }
                            });
                        }catch (Exception e){
                            int i=0;
                        }
                        try{
                            dataSnapshot.child("saved").getChildren().forEach(new Consumer<DataSnapshot>() {
                                @Override
                                public void accept(DataSnapshot dataSnapshot) {
                                    Saved.add(dataSnapshot.getValue(String.class));
                                }
                            });
                        }catch (Exception e){
                            int i=0;
                        }
                        try{
                            dataSnapshot.child("liked").getChildren().forEach(new Consumer<DataSnapshot>() {
                                @Override
                                public void accept(DataSnapshot dataSnapshot) {
                                    Liked.add(dataSnapshot.getValue(String.class));
                                }
                            });
                        }catch (Exception e){
                            int i=0;
                        }
                        try{
                            dataSnapshot.child("following").getChildren().forEach(new Consumer<DataSnapshot>() {
                                @Override
                                public void accept(DataSnapshot dataSnapshot) {
                                    Following.add(dataSnapshot.getValue(String.class));
                                }
                            });

                        }catch (Exception e){
                            int i=0;
                        }
                        try{
                            dataSnapshot.child("followers").getChildren().forEach(new Consumer<DataSnapshot>() {
                                @Override
                                public void accept(DataSnapshot dataSnapshot) {
                                    Followers.add(dataSnapshot.getValue(String.class));
                                }
                            });
                            //Followers=dataSnapshot.child("Followers").getValue(ArrayList.class);
                        }catch (Exception e){
                            int i=0;
                        }
                        try{
                            PhotoID=dataSnapshot.child("photoID").getValue(String.class);
                        }catch (Exception e){
                            int i=0;
                        }

//                        Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();
                        datachange=true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void setValues(String email, DatabaseReference frb) {
        Query query = frb.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                        Email = dataSnapshot.child("email").getValue(String.class);
                        UserID = dataSnapshot.child("userID").getValue(String.class);
                        Name = dataSnapshot.child("name").getValue(String.class);

                        try{
                            Posts=dataSnapshot.child("Posts").getValue(ArrayList.class);
                        }catch (Exception e){
                            int i=0;
                        }
                        try{
                            Saved= dataSnapshot.child("Saved").getValue(ArrayList.class);
                        }catch (Exception e){
                            int i=0;
                        }
                        try{
                            Liked= dataSnapshot.child("Liked").getValue(ArrayList.class);
                        }catch (Exception e){
                            int i=0;
                        }
                        try{
                            dataSnapshot.child("following").getChildren().forEach(new Consumer<DataSnapshot>() {
                                @Override
                                public void accept(DataSnapshot dataSnapshot) {
                                    Following.add(dataSnapshot.getValue(String.class));
                                }
                            });

                        }catch (Exception e){
                            int i=0;
                        }
                        try{
                            dataSnapshot.child("followers").getChildren().forEach(new Consumer<DataSnapshot>() {
                                @Override
                                public void accept(DataSnapshot dataSnapshot) {
                                    Followers.add(dataSnapshot.getValue(String.class));
                                }
                            });
                            //Followers=dataSnapshot.child("Followers").getValue(ArrayList.class);
                        }catch (Exception e){
                            int i=0;
                        }
                        try{
                            PhotoID=dataSnapshot.child("photoID").getValue(String.class);
                        }catch (Exception e){
                            int i=0;
                        }

//                        Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();
                        datachange=true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setDatachange(Boolean datachange) {
        this.datachange = datachange;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhotoID() {
        return PhotoID;
    }

    public void setPhotoID(String photoID) {
        PhotoID = photoID;
    }

    public ArrayList<String> getFollowers() {
        return Followers;
    }

    public void setFollowers(ArrayList<String> followers) {
        Followers = followers;
    }

    public ArrayList<String> getFollowing() {
        return Following;
    }

    public void setFollowing(ArrayList<String> following) {
        Following = following;
    }

    public ArrayList<String> getLiked() {
        return Liked;
    }

    public void setLiked(ArrayList<String> liked) {
        Liked = liked;
    }

    public ArrayList<String> getSaved() {
        return Saved;
    }

    public void setSaved(ArrayList<String> saved) {
        Saved = saved;
    }

    public ArrayList<String> getPosts() {
        return Posts;
    }

    public void setPosts(ArrayList<String> posts) {
        Posts = posts;
    }
}
