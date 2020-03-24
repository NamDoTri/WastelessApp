package com.wasteless.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SearchViewModel extends ViewModel {

//    private MutableLiveData<String> searchValue;
//    private MutableLiveData<String> filter;

    SearchModel searchModel = new SearchModel();
    public String[] getFiltersFromModel() {
        String[] filters = searchModel.getFilters();
        return filters;
    };

//    searchModel.setActiveFilter("date");


    //    searchModel.dummyData.getDummyData();

//    public SearchViewModel() {
//        searchValue = new MutableLiveData<>();
//        searchValue.setValue("This is search fragment");
//    }


//    public LiveData<String> getText() {
//        return searchValue;
//    }
}