package com.socialMedia.Memorize;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    RecyclerView recyclerView;
    recycleAdapterPosts adapterPosts;
    ArrayList<Post> posts;

    DatabaseReference database= FirebaseDatabase.getInstance().getReference("Posts");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_home, container, false);

        AppBarLayout appBarLayout=view.findViewById(R.id.Home_appBarLayout);
        recyclerView=view.findViewById(R.id.profile_recyclerView);
//        refreshLayout=view.findViewById(R.id.Home_swipRefresh);
//
//        refreshLayout.setOnRefreshListener(this);
//        refreshLayout.setRefreshing(true);


        appBarLayout.setBackground(null);



        posts=new ArrayList<>();
        FirebaseApp.initializeApp(getContext());
        getPostsToArray();


         return view;
    }

    void getPostsToArray(){

        posts.clear();

        Thread thread =new Thread(){

            @Override
            public void run() {


                database.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){

                            final int[] j = {0};
                            final int[] finalJ = {j[0]};
                            for (DataSnapshot snapshot1:snapshot.getChildren()){

                                try {

                                    Query query =database.orderByChild("postID").equalTo(snapshot1.child("postID").getValue(String.class));
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
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            adapterPosts = new recycleAdapterPosts(posts);

                                                            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

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
                                }catch (Exception e){
                                    int i=0;
                                }

                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        int i=0;
                    }
                });
            }
        };
        thread.start();

    }


    @Override
    public void onRefresh() {
        //refreshLayout.setRefreshing(true);
        getPostsToArray();
    }
}