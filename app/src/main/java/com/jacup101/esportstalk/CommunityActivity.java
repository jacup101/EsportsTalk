package com.jacup101.esportstalk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CommunityActivity extends AppCompatActivity {

    private String communityID;

    private ImageView coverPhoto;
    private ImageView logo;
    private FloatingActionButton addPostButton;
    private Button followButton;
    private ImageButton homeButton;
    private TextView nameText;

    private RecyclerView recyclerView;

    private DatabaseHelper databaseHelper;

    List<Post> posts;
    PostAdapter adapter;

    PostSharedViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        //can assume that a community has been passed in intent
        communityID = getIntent().getStringExtra("community");

        nameText = findViewById(R.id.textView_communityName);
        coverPhoto = findViewById(R.id.imageView_communityCover);
        logo = findViewById(R.id.imageView_communityLogo);

        addPostButton = findViewById(R.id.button_communityAddPost);
        followButton = findViewById(R.id.button_communityFollow);
        homeButton = findViewById(R.id.imageButton_communityHome);
        homeButton.setOnClickListener(v -> goHome());

        recyclerView = findViewById(R.id.recyclerView_community);

        viewModel = (PostSharedViewModel) new ViewModelProvider(this).get(PostSharedViewModel.class);


        posts = new ArrayList<Post>();
        viewModel.setPostList(posts);
        adapter = new PostAdapter(posts, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel.getPostList().observe(this, new Observer<List<Post>>() {

            @Override
            public void onChanged(List<Post> postList) {
                if(postList != null) {
                    posts = postList;
                    adapter.updateAdapter(posts);
                }
            }
        });


        databaseHelper = new DatabaseHelper(this);

        ArrayList<String > arr = new ArrayList<>();
        arr.add(communityID);
        databaseHelper.searchCommunities(arr,viewModel.getPostList());


        MutableLiveData<String[]> data = new MutableLiveData<String[]>();



        data.observe(this, new Observer<String[]>() {
            @Override
            public void onChanged(String[] strings) {
                if(strings != null) {
                    Picasso.get().load(strings[0]).into(logo);
                    Picasso.get().load(strings[1]).into(coverPhoto);

                    nameText.setText(strings[2]);
                    followButton.setText("Follow (" + strings[3] + ")");

                }
            }
        });
        databaseHelper.getCommunityProperties(data,communityID);
        addPostButton.setOnClickListener( v-> startNewPost());
    }



    private void startNewPost() {
        Intent intent = new Intent(this,NewPostActivity.class);
        intent.putExtra("community",communityID);
        startActivity(intent);
    }
    private void goHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
