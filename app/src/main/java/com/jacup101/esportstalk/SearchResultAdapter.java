package com.jacup101.esportstalk;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder>{

    private List<SearchResult> results;
    private Context context;

    public SearchResultAdapter(List<SearchResult> results, Context context) {
        this.results = results;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.item_searchresult,parent,false);
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchResult result = results.get(position);

        holder.text.setText(result.getText());



    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void updateAdapter(List<SearchResult> results) {
        this.results = results;
        notifyDataSetChanged();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView text;


        public ViewHolder(View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.textView_searchItem);
            text.setOnClickListener(v -> clicked(v));

        }
        private void clicked(View v) {
            SearchResult result = results.get(getAdapterPosition());
            if(result.getType().equals("post")) {
                Post post = result.getPost();
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
            if(result.getType().equals("community")) {
                Intent intent = new Intent(context,CommunityActivity.class);
                intent.putExtra("community",result.getStrID());
                context.startActivity(intent);
            }
        }

    }
}
