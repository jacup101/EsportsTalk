package com.jacup101.esportstalk;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {

    private View view;

    List<Post> posts;
    PostAdapter adapter;
    RecyclerView recyclerView;

    PostSharedViewModel viewModel;

    DatabaseHelper database;
    TabLayout tabLayout;
    ImageButton searchButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_posts, container, false);

        database = new DatabaseHelper(getContext());

        viewModel = (PostSharedViewModel) new ViewModelProvider(requireActivity()).get(PostSharedViewModel.class);
        tabLayout = view.findViewById(R.id.tabLayout_postSelect);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // called when tab selected
                String tabSelect = tab.getText().toString();
                parseClick(tabSelect);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // called when tab unselected
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                String tabSelect = tab.getText().toString();
                parseClick(tabSelect);
                // called when a tab is reselected
            }
        });

        recyclerView = view.findViewById(R.id.recyclerView_postFragment);

        searchButton = view.findViewById(R.id.imageButton_searchButton);
        searchButton.setOnClickListener(v -> startSearch());

        //Load in posts / viewmodel
        posts = new ArrayList<Post>();
        if(viewModel.getPostList() == null) viewModel.setPostList(posts);
        adapter = new PostAdapter(posts, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        viewModel.getPostList().observe(getViewLifecycleOwner(), new Observer<List<Post>>() {

            @Override
            public void onChanged(List<Post> postList) {
                if(postList != null) {
                    posts = postList;
                    adapter.updateAdapter(posts);
                }
            }
        });
        //Load posts back
        database.loadAllToView(viewModel.getPostList(),adapter);
        return view;

    }

    private void parseClick(String select) {
        if(select.equals("All")) {
            database.loadAllToView(viewModel.getPostList(),adapter);
        }
        if(select.equals("Followed")) {
            //TODO: Load followed posts instead of all posts here
            database.loadAllToView(viewModel.getPostList(),adapter);
        }
    }

    private void startSearch() {
        Intent intent = new Intent(getContext(),SearchActivity.class);
        startActivity(intent);
    }

}
