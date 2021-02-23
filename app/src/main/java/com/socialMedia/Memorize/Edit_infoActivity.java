package com.socialMedia.Memorize;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class Edit_infoActivity extends AppCompatActivity {



    EditText user,name;
    ImageView profileImage;
    Bitmap bitmapImage;

    FirebaseAuth auth;
    DatabaseReference database;
    StorageReference storageRef;
    Uri PathImage=Uri.EMPTY;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        user=findViewById(R.id.Edit_userNametext);
        name=findViewById(R.id.Edit_Nametext);
        profileImage=findViewById(R.id.Edit_imageView);

        auth=FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance().getReference("Users");
        storageRef= FirebaseStorage.getInstance().getReference("/User Profiles");

    }
    public void onClickSendEdit(MenuItem item) {

        FirebaseUser currentUser = auth.getCurrentUser();

        if (getIntent().getExtras().getBoolean("new"))
        {ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("loading");
            pd.setCancelable(false);
            pd.show();
            final String[] imageName = {UUID.randomUUID().toString() + "jbg"};
            storageRef.child(imageName[0]).putFile(PathImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageRef.child(imageName[0]).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageName[0] =String.valueOf(uri);
                            User User = new User(
                                    currentUser.getEmail(),
                                    user.getText().toString(),
                                    name.getText().toString(),
                                    imageName[0]
                            );

                            database.push().setValue(User);
                            pd.cancel();
                            Fragment frg = null;
                            frg = getSupportFragmentManager().findFragmentById(R.id.profileFragment);
                            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.detach(frg);
                            ft.attach(frg);
                            ft.commit();
                            finish();
                        }
                    });
                }
            });


        }
        else{
            try {


                if(PathImage!=Uri.EMPTY){
                    ProgressDialog pd = new ProgressDialog(this);
                    pd.setMessage("loading");
                    pd.setCancelable(false);
                    pd.show();
                    final String[] imageName = {UUID.randomUUID().toString() + "jbg"};
                    storageRef.child(imageName[0]).putFile(PathImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageRef.child(imageName[0]).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageName[0] =String.valueOf(uri);
                                    User User=new User(currentUser.getEmail(),database);

                                    Thread thread = new Thread() {
                                        @Override
                                        public void run() {
                                            try {

                                                while (!User.isDatachange()){
                                                    int i=0;
                                                }

                                                User.setPhotoID(imageName[0]);
                                                User.setUserID(user.getText().toString());
                                                User.setName(name.getText().toString());
                                                Query query = database.orderByChild("email").equalTo(currentUser.getEmail());
                                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (snapshot.exists()) {
                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                                            {
                                                                dataSnapshot.getRef().setValue(User);

                                                            }
                                                            pd.cancel();
                                                            Fragment frg = null;
                                                            frg = getSupportFragmentManager().findFragmentById(R.id.profileFragment);
                                                            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                                            ft.detach(frg);
                                                            ft.attach(frg);
                                                            ft.commit();
                                                            finish();

                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });

                                            } catch (Exception e) {
                                                int i=0;
                                            }
                                        }
                                    };
                                    thread.start();

                                }
                            });
                        }
                    });

                }
                else {
                    ProgressDialog pd = new ProgressDialog(this);
                    pd.setMessage("loading");
                    pd.setCancelable(false);
                    pd.show();
                    User User=new User(currentUser.getEmail(),database);

                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {

                                while (!User.isDatachange()){
                                    int i=0;
                                }
                                User.setUserID(user.getText().toString());
                                User.setName(name.getText().toString());
                                Query query = database.orderByChild("email").equalTo(currentUser.getEmail());
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                            {
                                                dataSnapshot.getRef().setValue(User);

                                            }
                                            pd.cancel();
                                            Fragment frg = null;
                                            frg = getSupportFragmentManager().findFragmentById(R.id.profileFragment);
                                            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                            ft.detach(frg);
                                            ft.attach(frg);
                                            ft.commit();
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            } catch (Exception e) {
                                int i=0;
                            }
                        }
                    };
                    thread.start();

                }


            }
            catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }



    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClickEditImage(View view) {
        if(Getpermission()){
            Intent i = new Intent(
                    Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_FIRST_USER);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean Getpermission() {
        try{
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
                return false;

            }
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
                return false;
            }
            else{
                return true;
            }
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_FIRST_USER && resultCode == RESULT_OK && null != data) {
             PathImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(PathImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            bitmapImage =BitmapFactory.decodeFile(picturePath);
            profileImage.setImageBitmap(bitmapImage);
        }
    }





}