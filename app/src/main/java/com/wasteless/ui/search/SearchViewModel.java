package com.wasteless.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<String> searchValue;
    private MutableLiveData<String> filter;

    private SearchModel searchModel = new SearchModel();



//    searchModel.dummyData.getDummyData();

//    public SearchViewModel() {
//        searchValue = new MutableLiveData<>();
//        searchValue.setValue("This is search fragment");
//    }


//    public LiveData<String> getText() {
//        return searchValue;
//    }
}