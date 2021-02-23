package com.socialMedia.Memorize;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Consumer;

public class Post {


     String postID,title,note,publishID,date,photoID;
     ArrayList<String> liked,saved;
    boolean dataChange=false;

    public boolean isDataChange() {
        return dataChange;
    }

    public void setDataChange(boolean dataChange) {
        this.dataChange = dataChange;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Post(String title, String note, String publishID,String photoID) {

        this.postID= UUID.randomUUID().toString();
        this.title = title;
        this.note = note;
        this.publishID = publishID;
        this.photoID=photoID;

        this.date= LocalDate.now().toString();

        liked=new ArrayList<>();
        saved=new ArrayList<>();

    }

    public Post(){

    }

    public void setValues(String type, String valueType, DatabaseReference database) {

//
                Query query =database.orderByChild("postID").equalTo(postID);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot:snapshot.getChildren())
                            {

                                setPostID(dataSnapshot.child("postID").getValue(String.class));
                                setTitle(dataSnapshot.child("title").getValue(String.class));
                                setNote(dataSnapshot.child("note").getValue(String.class));
                                setPublishID(dataSnapshot.child("publish").getValue(String.class));
                                setDate(dataSnapshot.child("date").getValue(String.class));
                                setPhotoID(dataSnapshot.child("photoID").getValue(String.class));

                                try {
                                    dataSnapshot.child("liked").getChildren().forEach(new Consumer<DataSnapshot>() {
                                        @Override
                                        public void accept(DataSnapshot dataSnapshot) {
                                            liked.add(dataSnapshot.getValue(String.class));
                                        }
                                    });
                                }catch (Exception e){
                                    int i=0;
                                }
                                try {
                                    dataSnapshot.child("saved").getChildren().forEach(new Consumer<DataSnapshot>() {
                                        @Override
                                        public void accept(DataSnapshot dataSnapshot) {
                                            saved.add(dataSnapshot.getValue(String.class));
                                        }
                                    });
                                }catch (Exception e){
                                    int i=0;
                                }


                            }
                            setDataChange(true);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public void setPublishID(String publishID) {
        this.publishID = publishID;
    }

    public void setPhotoID(String photoID) {
        this.photoID = photoID;
    }

    public void setLiked(ArrayList<String> liked) {
        this.liked = liked;
    }

    public void setSaved(ArrayList<String> saved) {
        this.saved = saved;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setPublish(String publish) {
        this.publishID = publish;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPublishID() {
        return publishID;
    }

    public String getPostID() {
        return postID;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    public String getPublish() {
        return publishID;
    }

    public String getDate() {
        return date;
    }

    public String getPhotoID() {
        return photoID;
    }

    public ArrayList<String> getLiked() {
        return liked;
    }

    public ArrayList<String> getSaved() {
        return saved;
    }
}
