package com.socialMedia.Memorize;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ListUserActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, recycleAdapterUsers.onClickListner {


    TextView listTitle;
    RecyclerView recyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    recycleAdapterUsers RV;

    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser currentUser=auth.getCurrentUser();
    DatabaseReference database= FirebaseDatabase.getInstance().getReference("Users");

    ArrayList<String> users,names,urlImages;

    SpinKitView progressbar;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);

        listTitle=findViewById(R.id.listActivity_Title);

        progressbar=findViewById(R.id.listActivity_progressbar);
        linearLayout=findViewById(R.id.ListUserActivity_layout);
        mSwipeRefreshLayout=findViewById(R.id.listActivity_swipeRefresh);
        recyclerView=findViewById(R.id.listAcivity_recycleView);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setRefreshing(true);
        listTitle.setText(getIntent().getExtras().getString("type"));

        users=new ArrayList<>();
        names=new ArrayList<>();
        urlImages=new ArrayList<>();
//////////////////////////////////////////////////////////////////////////////////////////////////
        FirebaseApp.initializeApp(this);
        setListView();
    }

    public void setListView(){

        if(!getIntent().getExtras().getString("type").matches("search")) {
            try {
                users.clear();
                names.clear();
                urlImages.clear();
                Query query = database.orderByChild("userID").equalTo(getIntent().getExtras().getString("userID"));
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean exixt = snapshot.exists();
                        if (exixt) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                if (dataSnapshot.child(getIntent().getExtras().getString("type")).exists())
                                    dataSnapshot.child(getIntent().getExtras().getString("type")).getChildren().forEach(new Consumer<DataSnapshot>() {
                                        @Override
                                        public void accept(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                String USER = dataSnapshot.getValue(String.class);
                                                users.add(USER);
                                                Query Query = database.orderByChild("userID").equalTo(USER);
                                                Query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (snapshot.exists()) {
                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                                                                names.add(dataSnapshot.child("name").getValue(String.class));
                                                                urlImages.add(dataSnapshot.child("photoID").getValue(String.class));

                                                                setAdapter();
                                                            }
                                                        } else {
                                                            recyclerView.setAdapter(null);
                                                            mSwipeRefreshLayout.setRefreshing(false);
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                        recyclerView.setAdapter(null);
                                                        mSwipeRefreshLayout.setRefreshing(false);
                                                    }
                                                });
                                            } else {
                                                recyclerView.setAdapter(null);
                                                mSwipeRefreshLayout.setRefreshing(false);
                                            }
                                        }
                                    });
                                else {
                                    recyclerView.setAdapter(null);
                                    ;
                                    mSwipeRefreshLayout.setRefreshing(false);
                                }

                            }
                        } else {
                            recyclerView.setAdapter(null);
                            ;
                            mSwipeRefreshLayout.setRefreshing(false);
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        recyclerView.setAdapter(null);
                        ;
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            users.clear();
            names.clear();
            urlImages.clear();

            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean exixt = snapshot.exists();
                    if (exixt) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            String USER = dataSnapshot.child("userID").getValue(String.class);
                            if(USER.contains(getIntent().getExtras().getString("userSearch"))) {
                                users.add(USER);
                                Query Query = database.orderByChild("userID").equalTo(USER);
                                Query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                                                names.add(dataSnapshot.child("name").getValue(String.class));
                                                urlImages.add(dataSnapshot.child("photoID").getValue(String.class));

                                                setAdapter();
                                            }
                                        } else {
                                            recyclerView.setAdapter(null);
                                            mSwipeRefreshLayout.setRefreshing(false);
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        recyclerView.setAdapter(null);
                                        mSwipeRefreshLayout.setRefreshing(false);
                                    }
                                });
                            }

                        }
                    } else {
                        recyclerView.setAdapter(null);
                        ;
                        mSwipeRefreshLayout.setRefreshing(false);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    recyclerView.setAdapter(null);
                    ;
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });

        }

    }



    private void setAdapter() {

        mSwipeRefreshLayout.setRefreshing(false);
       RV=new recycleAdapterUsers(users,names,urlImages,this);
       RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
       recyclerView.setLayoutManager(layoutManager);
       recyclerView.setItemAnimator(new DefaultItemAnimator());

        try{
            recyclerView.setAdapter(RV);
        }catch (Exception e){
           int i=0;
        }



    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setListView();


            }
        }, 1000);
    }

    @Override
    public void onClickitem(int position) {
        Intent i = new Intent(ListUserActivity.this, UserActivity.class);
        i.putExtra("userID",users.get(position));
        startActivity(i);
    }
}