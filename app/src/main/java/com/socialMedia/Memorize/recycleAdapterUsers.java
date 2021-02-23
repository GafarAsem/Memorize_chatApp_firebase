package com.socialMedia.Memorize;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class recycleAdapterUsers extends RecyclerView.Adapter<recycleAdapterUsers.MyViewHolder> {


    private ArrayList<String> User,Name,ImageUrl;
    private  onClickListner monClickListner;

    public recycleAdapterUsers(ArrayList<String> user, ArrayList<String> name, ArrayList<String> imageUrl, onClickListner monClickListner) {
        User = user;
        Name = name;
        ImageUrl = imageUrl;
        this.monClickListner=monClickListner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user,parent,false);

        return new MyViewHolder(itemView,monClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.user.setText(User.get(position));
        holder.name.setText(Name.get(position));
        Picasso.get().load(ImageUrl.get(position)).into(holder.profileImage);
    }

   

    @Override
    public int getItemCount() {
        return User.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView user,name;
        ImageView profileImage;
        onClickListner onClickListner;

        public MyViewHolder(@NonNull View itemView,onClickListner onClickListner) {
            super(itemView);

            user=itemView.findViewById(R.id.listuser_userName);
            name=itemView.findViewById(R.id.listuser_Name);
            profileImage=itemView.findViewById(R.id.listuser_imageView);
            this.onClickListner=onClickListner;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        this.onClickListner.onClickitem(getAdapterPosition());
        }
    }
    public interface  onClickListner{

        void onClickitem(int position);
    }
}
