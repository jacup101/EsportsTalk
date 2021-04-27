package com.jacup101.esportstalk;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class SearchSharedViewModel extends ViewModel {
    //PostSharedViewModel provides a unified location to store the posts that are to be displayed on screen
    private MutableLiveData<List<SearchResult>> shareList = new MutableLiveData<>();

    public void setShareList(List<SearchResult> searchResults) {
        this.shareList.setValue(searchResults);
    }
    public MutableLiveData<List<SearchResult>> getShareList() {
        return shareList;
    }


}
