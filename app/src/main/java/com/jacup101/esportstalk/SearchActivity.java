package com.jacup101.esportstalk;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    EditText searchBar;
    Button buttonSearch;

    TabLayout tabLayoutSearch;
    RecyclerView recyclerView;

    String tabSelect = "Posts";

    DatabaseHelper databaseHelper;


    SearchSharedViewModel viewModel;
    List<SearchResult> results;
    SearchResultAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchBar = findViewById(R.id.editText_searchBar);
        buttonSearch = findViewById(R.id.button_searchGo);

        viewModel = (SearchSharedViewModel) new ViewModelProvider(this).get(SearchSharedViewModel.class);

        tabLayoutSearch = findViewById(R.id.tabLayout_search);
        tabLayoutSearch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // called when tab selected
                tabSelect = tab.getText().toString();
                parseClick(tabSelect);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // called when tab unselected
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                tabSelect = tab.getText().toString();
                parseClick(tabSelect);
                // called when a tab is reselected
            }
        });

        buttonSearch.setOnClickListener(v -> parseClick(tabSelect));

        databaseHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerView_search);
        results = new ArrayList<SearchResult>();
        viewModel.setShareList(results);
        adapter = new SearchResultAdapter(results, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel.getShareList().observe(this, new Observer<List<SearchResult>>() {

            @Override
            public void onChanged(List<SearchResult> resultList) {
                if(resultList != null) {
                    results = resultList;
                    adapter.updateAdapter(results);
                }
            }
        });

    }

    private void parseClick(String str) {
        if(str.equals("Posts")) {
            databaseHelper.searchPostsList(viewModel.getShareList(),searchBar.getText().toString());
        }
        if(str.equals("Communities")) {
            databaseHelper.searchCommunityList(viewModel.getShareList(),searchBar.getText().toString());
        }
        if(str.equals("Users")) {
            databaseHelper.searchUsersList(viewModel.getShareList(),searchBar.getText().toString());
        }

    }


}
