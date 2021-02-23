package com.socialMedia.Memorize;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class arrayAdapter extends ArrayAdapter {

   ArrayList<String> Users,Names,UrlImages;

    public arrayAdapter(@NonNull Context context,  ArrayList<String> users, ArrayList<String> names, ArrayList<String> urlImages) {
        super(context,R.layout.list_user,R.id.listuser_userName, users);
        Users = users;
        Names = names;
        UrlImages = urlImages;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater infalter=(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View List=infalter.inflate(R.layout.list_user,parent,false);


        TextView userText=List.findViewById(R.id.listuser_userName);
        userText.setText(Users.get(position));

        TextView nameText=List.findViewById(R.id.listuser_Name);
        nameText.setText(Names.get(position));

        ImageView imageView=List.findViewById(R.id.listuser_imageView);
        Picasso.get().load(UrlImages.get(position)).into(imageView);
        return List;
    }


}