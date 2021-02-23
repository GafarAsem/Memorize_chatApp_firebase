package com.socialMedia.Memorize;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class recycleAdapterPosts extends RecyclerView.Adapter<recycleAdapterPosts.MyViewHolder> {


    private ArrayList<Post> posts;
//    private  onClickListner monClickListner;

    public recycleAdapterPosts(ArrayList<Post> posts) {

        this.posts = posts;
    }

    @NonNull
    @Override
    public recycleAdapterPosts.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.max_post_layout,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull recycleAdapterPosts.MyViewHolder holder, int position) {

        Picasso.get().load(posts.get(position).getPhotoID()).fit().centerCrop().into(holder.imageView);
        holder.title.setText(posts.get(position).getTitle());
        try {
            holder.heart.setTag(R.id.postID, posts.get(position).getPostID());
            holder.star.setTag(R.id.postID, posts.get(position).getPostID());
        }catch (Exception e){
            int i=0;
        }

        holder.note.setText(posts.get(position).getNote());
        holder.date.setText(posts.get(position).getDate());

        if(posts.get(position).getLiked().contains(MainActivity.ThisUserID)){
            holder.heart.setImageResource(R.drawable.heart_white);
            holder.heart.setTag(R.id.like,R.drawable.heart_white);
        }
        if(posts.get(position).getSaved().contains(MainActivity.ThisUserID)){
            holder.star.setImageResource(R.drawable.star_white);
            holder.star.setTag(R.id.save,R.drawable.star_white);
        }
    }



    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {

        TextView title,note,date;
        ImageView imageView;
        ImageButton heart ,star;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

                    imageView=itemView.findViewById(R.id.bigPost_image);

                    title=itemView.findViewById(R.id.bigPost_Title);
                    note=itemView.findViewById(R.id.bigPost_note);
                    date=itemView.findViewById(R.id.bigPost_date);

                    heart=itemView.findViewById(R.id.bigPost_heart);
                    star=itemView.findViewById(R.id.bigPost_star);


        }


    }
    public interface  onClickListner{

        void onClickitem(int position);
    }
}
