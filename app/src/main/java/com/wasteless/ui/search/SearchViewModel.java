package com.wasteless.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<String> searchValue;
    private MutableLiveData<String> filter;

//    public SearchViewModel() {
//        searchValue = new MutableLiveData<>();
//        searchValue.setValue("This is search fragment");
//    }

    public void setFilter(MutableLiveData<String> filter) {
        this.filter = filter;
    }

    public void setSearchValue(MutableLiveData<String> searchValue) {
        this.searchValue = searchValue;
    }

//    public LiveData<String> getText() {
//        return searchValue;
//    }
}