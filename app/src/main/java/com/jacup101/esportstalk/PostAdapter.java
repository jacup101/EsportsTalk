package com.jacup101.esportstalk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    private List<Post> posts;
    private Context context;

    public PostAdapter(List<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.item_post,parent,false);
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.title.setText(post.getTitle());
        holder.content.setText(post.getContent());
        holder.user.setText(post.getUser());
        holder.date.setText(post.getDate());
        holder.community.setText(post.getCommunity());

        if(post.getType().equals("image") && post.getImageUri() != null) {
            //Toast.makeText(context,"Something was found", Toast.LENGTH_SHORT).show();
            Log.d("loading image", "type" + post.getType() + "title" + post.getTitle());
            holder.imageView.setAdjustViewBounds(true);
            Picasso.get().load(post.getImageUri()).into(holder.imageView);
            Log.d("image_view","" + holder.imageView.getHeight());
        } else {
;
            holder.imageView.setImageBitmap(null);
            holder.imageView.setAdjustViewBounds(false);
        }
        if(post.getType().equals("video") && post.getVideoID() != null) {
            holder.youtubeView.setVisibility(View.VISIBLE);
            holder.youtubeView.getYouTubePlayerWhenReady(youTubePlayer -> {
                youTubePlayer.cueVideo(post.getVideoID(),0);
                // do stuff with it
            });
        } else {
            holder.youtubeView.setVisibility(View.GONE);
            holder.youtubeView.getYouTubePlayerWhenReady(youTubePlayer -> {
                youTubePlayer.pause();
                // do stuff with it
            });
        }

        //TODO: ADD IMAGES/VIDEOS BASED ON TYPE
        //TODO: ADD COMMUNITY
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
    public void resetData() {
        posts.removeAll(posts);
    }
    public void updateAdapter(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView user;
        private TextView content;
        private ImageView imageView;
        private TextView date;
        private TextView community;
        private YouTubePlayerView youtubeView;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textView_postTitle);

            title.setOnClickListener(v -> goToPost(v));



            user = itemView.findViewById(R.id.textView_postUser);
            content = itemView.findViewById(R.id.textView_postText);
            community = itemView.findViewById(R.id.textView_postCommunity);
            date = itemView.findViewById(R.id.textView_postDate);

            community.setOnClickListener(v -> goToCommunity(v));

            imageView = itemView.findViewById(R.id.imageView_postImage);
            youtubeView = itemView.findViewById(R.id.youtubePlayerView_postVideo);
        }

        public void goToPost(View v) {
            int pos = getAdapterPosition();
            Post post = posts.get(pos);

            Intent intent = new Intent(context,PostActivity.class);
            intent.putExtra("title",post.getTitle());
            intent.putExtra("user",post.getUser());
            intent.putExtra("content",post.getContent());
            intent.putExtra("type",post.getType());
            intent.putExtra("id",post.getId());
            intent.putExtra("date",post.getDate());
            intent.putExtra("community",post.getCommunity());
            intent.putExtra("commentString",post.getCommentString());
            if(post.getImageUri()!=null) intent.putExtra("imgurl",post.getImageUri().toString());
            if(post.getVideoID()!=null) intent.putExtra("vidid",post.getVideoID());
            context.startActivity(intent);
        }
        public void goToCommunity(View v) {
            int pos = getAdapterPosition();
            Post post = posts.get(pos);

            Intent intent = new Intent(context,CommunityActivity.class);
            intent.putExtra("community",post.getCommunity());
            context.startActivity(intent);

        }
    }
}
