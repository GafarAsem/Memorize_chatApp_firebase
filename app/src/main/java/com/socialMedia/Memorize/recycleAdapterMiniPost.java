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

public class recycleAdapterMiniPost extends RecyclerView.Adapter<recycleAdapterMiniPost.MyViewHolder> {


    private ArrayList<Post> posts;
//    private  onClickListner monClickListner;

    public recycleAdapterMiniPost(ArrayList<Post> posts) {
        this.posts = posts;

    }

    @NonNull
    @Override
    public recycleAdapterMiniPost.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.mini_post_layout,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull recycleAdapterMiniPost.MyViewHolder holder, int position) {

        Picasso.get().load(posts.get(position).getPhotoID()).fit().centerCrop().into(holder.imageView);
        holder.title.setText(posts.get(position).getTitle());

        holder.heart.setTag(R.id.postID,posts.get(position).getPostID());
        holder.star.setTag(R.id.postID,posts.get(position).getPostID());

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

        TextView title;
        ImageView imageView;
        ImageButton heart ,star;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.miniPost_image);

            title=itemView.findViewById(R.id.miniPost_Title);

            heart=itemView.findViewById(R.id.miniPost_heart);
            star=itemView.findViewById(R.id.miniPost_star);


        }


    }
    public interface  onClickListner{

        void onClickitem(int position);
    }
}
