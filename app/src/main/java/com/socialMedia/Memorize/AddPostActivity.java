package com.socialMedia.Memorize;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

public class AddPostActivity extends AppCompatActivity {


    FirebaseAuth auth;
    DatabaseReference databasePost;
    DatabaseReference databaseUser;
    StorageReference storageRef;
    FirebaseUser currentUser;

    Uri PathImage=Uri.EMPTY;
    Bitmap photo;

    ImageView posterImage;
    EditText titleEdit, noteEdit, dateEdit;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        posterImage =findViewById(R.id.addPost_imageView);
        titleEdit =findViewById(R.id.addPost_title);
        noteEdit =findViewById(R.id.addPost_note);
        dateEdit =findViewById(R.id.addPost_date);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateEdit.setText(LocalDate.now().toString());
        }
        databasePost = FirebaseDatabase.getInstance().getReference("Posts");
        databaseUser = FirebaseDatabase.getInstance().getReference("Users");
        storageRef= FirebaseStorage.getInstance().getReference("/Post Images");
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        getImage();

    }

    public void onClickAdd(MenuItem view){
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("loading");
        pd.setCancelable(false);
        pd.show();
        final User[] user = new User[2];


        final String[] imageName = {UUID.randomUUID().toString() + "jbg"};
        Thread thread =new Thread(){



            @Override
            public void run() {
                user[1]=new User(currentUser.getEmail(), FirebaseDatabase.getInstance().getReference("Users"));
                while (!user[1].isDatachange()) {
                    int i = 0;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        storageRef.child(imageName[0]).putFile(PathImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                storageRef.child(imageName[0]).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String userID=user[1].getUserID();

                                        imageName[0] =String.valueOf(uri);
                                        Post post = new Post(
                                                titleEdit.getText().toString(),
                                                noteEdit.getText().toString(),
                                                userID,
                                                imageName[0]
                                        );
                                        Query query=databaseUser.orderByChild("userID").equalTo(userID);
                                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                               Thread thread1=new Thread(){

                                                   @Override
                                                   public void run() {
                                                       User user1=new User(userID,databaseUser,getApplicationContext());
                                                       while (!user1.isDatachange()){
                                                           int i=0;}
                                                       ArrayList<String> posts = user1.getPosts();
                                                       posts.add(post.getPostID());
                                                       user1.setPosts(posts);
                                                       for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                                       {
                                                           dataSnapshot.getRef().setValue(user1);

                                                       }

                                                       pd.cancel();
                                                       finish();
                                                   }
                                               };
                                               thread1.start();

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                        // User.setFollowing(new ArrayList<>(Arrays.asList("eee","eee")));
                                        databasePost.push().setValue(post);



                                    }
                                });
                            }
                        });
                    }
                });
            }
        };

        thread.start();
    }




    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getImage() {
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
            photo=BitmapFactory.decodeFile(picturePath);
            posterImage.setImageBitmap(photo);
        }
    }



}
