package com.jacup101.esportstalk;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    private long id;

    private TextView title;
    private TextView user;
    private TextView content;
    private TextView community;
    private TextView date;

    private ImageView imageView;
    private RecyclerView recyclerView;

    private YouTubePlayerView youTubePlayerView;

    private EditText commentInput;
    private Button commentAdd;

    private DatabaseHelper databaseHelper;
    private List<Comment> comments;
    private CommentAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Intent intent = getIntent();

        id = intent.getLongExtra("id",1);

        title = findViewById(R.id.textView_postATitle);
        title.setText(intent.getStringExtra("title"));
        user = findViewById(R.id.textView_postAUser);
        user.setText(intent.getStringExtra("user"));

        content = findViewById(R.id.textView_postAContent);
        content.setText(intent.getStringExtra("content"));

        community = findViewById(R.id.textView_postACommunity);
        community.setText(intent.getStringExtra("community"));

        date = findViewById(R.id.textView_postADate);
        date.setText(intent.getStringExtra("date"));

        imageView = findViewById(R.id.imageView_postAImage);
        if(intent.getStringExtra("type").equals("image")) {
            Picasso.get().load(Uri.parse(intent.getStringExtra("imgurl"))).into(imageView);
        }
        youTubePlayerView = findViewById(R.id.youtubePlayer_postA);

        if(intent.getStringExtra("type").equals("video") && intent.getStringExtra("vidid") != null) {
            youTubePlayerView.setVisibility(View.VISIBLE);
            youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> {
                youTubePlayer.cueVideo(intent.getStringExtra("vidid"),0);
                // do stuff with it
            });
        } else {
            youTubePlayerView.setVisibility(View.GONE);
        }


        recyclerView = findViewById(R.id.recyclerView_postA);
        String commentString = intent.getStringExtra("commentString");
        parseCommentsFromString(commentString);
        adapter = new CommentAdapter(comments,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        commentInput = findViewById(R.id.editText_commentPostA);
        commentAdd = findViewById(R.id.button_postAComment);
        commentAdd.setOnClickListener(v->addComment(v));


        databaseHelper = new DatabaseHelper(this);

    }
    public void parseCommentsFromString(String commentString) {
        this.comments = new ArrayList<Comment>();
        while(commentString.indexOf("$E") != -1) {
            String sub = commentString.substring(commentString.indexOf("$CM"),commentString.indexOf("$E")+2);
            Log.d("substring_attempt", sub);
            String user = sub.substring(sub.indexOf("$U:") + 3,sub.indexOf("$P:"));
            String commentText = sub.substring(sub.indexOf("$P:") + 3,sub.indexOf("$E"));
            Comment comment = new Comment(user, commentText, this.id);
            comments.add(comment);
            commentString = commentString.substring(commentString.indexOf("$E") + 2);
        }


    }
    public void addComment(View v) {
        //TODO: Bar disallowed text, implement proper user functionality
        databaseHelper.addComment("" + id,commentInput.getText().toString(), "jacup101");
        comments.add(new Comment("Jacup101",commentInput.getText().toString(), id));
        adapter.notifyDataSetChanged();

    }
}
