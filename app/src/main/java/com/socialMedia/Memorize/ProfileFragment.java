package com.socialMedia.Memorize;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
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
import java.util.Collections;
import java.util.function.Consumer;


public class ProfileFragment extends Fragment {

    private static FragmentActivity myContext;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    static RecyclerView recyclerView;
    static RecyclerView gridRecyclerView;
    static recycleAdapterPosts adapterPosts;
    static ArrayList<Post> posts;

    static DatabaseReference databasePost= FirebaseDatabase.getInstance().getReference("Posts");

    TextView userName,name,following,followers;
    ImageView profileImage;

    static   FirebaseUser currentUser;
    FirebaseAuth auth;
    static  DatabaseReference database;

   // private final AtomicBoolean running = new AtomicBoolean(false);

    static LinearLayoutCompat linearLayout;
    static SpinKitView progressbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        userName =view.findViewById(R.id.profile_userName);
        name=view.findViewById(R.id.profile_Name);
        following=view.findViewById(R.id.profile_following);
        followers=view.findViewById(R.id.profile_followers);
        profileImage=view.findViewById(R.id.profile_image);
        linearLayout=view.findViewById(R.id.fragmentProfile_layout);
        progressbar=view.findViewById(R.id.profile_progressbar);


        auth=FirebaseAuth.getInstance();
        currentUser= auth.getCurrentUser();
        database= FirebaseDatabase.getInstance().getReference("Users");

        recyclerView=view.findViewById(R.id.profile_recyclerView);
        gridRecyclerView=view.findViewById(R.id.profile_gridView);
        posts=new ArrayList<>();

            setInfoUser();
       // getPostsToArray();

        return view;
    }

    private void setInfoUser() {

        final User[] user = new User[1];
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        user[0] = new User(currentUser.getEmail(), database);

                        while (!user[0].isDatachange()) {
                            int i = 0;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    linearLayout.setVisibility(View.VISIBLE);
                                    progressbar.setVisibility(View.GONE);

                                    userName.setText(user[0].getUserID());
                                    name.setText(user[0].getName());
                                    String imageurl=user[0].getPhotoID();
                                    Picasso.get().load(imageurl).into(profileImage);
                                    int fr, fg;

                                    try {
                                        fr = user[0].getFollowers().size();
                                    } catch (Exception e) {
                                        //Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        fr = 0;
                                    }

                                    try {
                                        fg = user[0].getFollowing().size();
                                    } catch (Exception e) {
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        fg = 0;
                                    }


                                    followers.setText("Followers " + fr);
                                    following.setText("Following  " + fg);


                                } catch (Exception e) {
                                    int i = 0;
                                  //  Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

                    }
                };
        thread.start();

    }

    private void toast(Exception e) {
        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }
    static void getPostsToArray(){
        linearLayout.setVisibility(View.INVISIBLE);
        progressbar.setVisibility(View.VISIBLE);
        posts.clear();
        final int[] j = {0};
        final int[] finalJ = {j[0]};
        Thread thread =new Thread(){

            @Override
            public void run() {
                Query queryUserPosts=database.orderByChild("email").equalTo(currentUser.getEmail());
                queryUserPosts.addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists())
                            for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                dataSnapshot.child("posts").getChildren().forEach(new Consumer<DataSnapshot>() {
                                    @Override
                                    public void accept(DataSnapshot dataSnapshot) {
                                        Query query =databasePost.orderByChild("postID").equalTo(dataSnapshot.getValue(String.class));
                                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @RequiresApi(api = Build.VERSION_CODES.O)
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()){
                                                    for (DataSnapshot dataSnapshot:snapshot.getChildren()) {

                                                        posts.add(new Post(dataSnapshot.child("title").getValue(String.class),
                                                                dataSnapshot.child("note").getValue(String.class),
                                                                dataSnapshot.child("publish").getValue(String.class),
                                                                dataSnapshot.child("photoID").getValue(String.class)
                                                        ));
                                                        posts.get(finalJ[0]).setPostID(dataSnapshot.child("postID").getValue(String.class));
                                                        try {

                                                            dataSnapshot.child("liked").getChildren().forEach(new Consumer<DataSnapshot>() {
                                                                @Override
                                                                public void accept(DataSnapshot dataSnapshot) {
                                                                    posts.get(finalJ[0]).liked.add(dataSnapshot.getValue(String.class));
                                                                }
                                                            });
                                                        }catch (Exception e){
                                                            int i=0;
                                                        }
                                                        try {
                                                            dataSnapshot.child("saved").getChildren().forEach(new Consumer<DataSnapshot>() {
                                                                @Override
                                                                public void accept(DataSnapshot dataSnapshot) {
                                                                    posts.get(finalJ[0]).saved.add(dataSnapshot.getValue(String.class));
                                                                }
                                                            });
                                                        }catch (Exception e){
                                                            int i=0;
                                                        }
                                                        finalJ[0]++;
                                                        myContext.runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {

                                                                adapterPosts = new recycleAdapterPosts(posts);

                                                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(myContext, LinearLayoutManager.HORIZONTAL, false);

                                                                recyclerView.setLayoutManager(layoutManager);
                                                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                                                try {
                                                                    recyclerView.setNestedScrollingEnabled(false);
                                                                    recyclerView.setOnFlingListener(null);
                                                                    SnapHelper snapHelper = new PagerSnapHelper();
                                                                    snapHelper.attachToRecyclerView(recyclerView);
                                                                }catch (Exception e){
                                                                    int i=0;
                                                                }
                                                                progressbar.setVisibility(View.INVISIBLE);
                                                                recyclerView.setVisibility(View.VISIBLE);
                                                                recyclerView.setAdapter(adapterPosts);
//                                                            refreshLayout.setRefreshing(false);


                                                            }
                                                        });
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                });
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressbar.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                });
            }
        };
        thread.start();


    }

    public static void getMyPostsGrid()
    {
        final int[] j = {0};
        final int[] finalJ = {j[0]};
        Thread thread =new Thread() {

            @Override
            public void run() {
                posts.clear();
                Query queryMyPost = databasePost.orderByChild("publishID").equalTo(MainActivity.ThisUserID);
                queryMyPost.addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDataChange (@NonNull DataSnapshot snapshot){

                        if (snapshot.exists()) {

                            for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                                posts.add(new Post(datasnapshot.child("title").getValue(String.class),
                                        datasnapshot.child("note").getValue(String.class),
                                        datasnapshot.child("publish").getValue(String.class),
                                        datasnapshot.child("photoID").getValue(String.class)
                                ));
                                posts.get(finalJ[0]).setPostID(datasnapshot.child("postID").getValue(String.class));
                                try {

                                    datasnapshot.child("liked").getChildren().forEach(new Consumer<DataSnapshot>() {
                                        @Override
                                        public void accept(DataSnapshot dataSnapshot) {
                                            posts.get(finalJ[0]).liked.add(dataSnapshot.getValue(String.class));
                                        }
                                    });
                                } catch (Exception e) {
                                    int i = 0;
                                }
                                try {
                                    datasnapshot.child("saved").getChildren().forEach(new Consumer<DataSnapshot>() {
                                        @Override
                                        public void accept(DataSnapshot dataSnapshot) {
                                            posts.get(finalJ[0]).saved.add(dataSnapshot.getValue(String.class));
                                        }
                                    });
                                } catch (Exception e) {
                                    int i = 0;
                                }
                                finalJ[0]++;


                            }
                            myContext.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    recycleAdapterMiniPost adapterMiniPost = new recycleAdapterMiniPost(posts);

                                    RecyclerView.LayoutManager gridLayoutManger = new GridLayoutManager(myContext, 2);

                                    gridRecyclerView.setLayoutManager(gridLayoutManger);
                                    gridRecyclerView.setItemAnimator(new DefaultItemAnimator());
//                                    try {
////                                        gridRecyclerView.setNestedScrollingEnabled(false);
////                                        gridRecyclerView.setOnFlingListener(null);
////                                        SnapHelper snapHelper = new PagerSnapHelper();
////                                        snapHelper.attachToRecyclerView(gridRecyclerView);
//                                    } catch (Exception e) {
//                                        int i = 0;
//                                    }
                                    try {
                                        gridRecyclerView.setAdapter(adapterMiniPost);
                                    }catch (Exception e){

                                    }


                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled (@NonNull DatabaseError error){

                    }
                });
            }
        };
        thread.start();

    }

}