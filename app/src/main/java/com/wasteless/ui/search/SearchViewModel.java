package com.wasteless.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SearchViewModel extends ViewModel {

//    private MutableLiveData<String> searchValue;
//    private MutableLiveData<String> filter;

    private MutableLiveData<String> searchValue;
    private MutableLiveData<String> activeFilter;

    SearchModel searchModel = new SearchModel();
    public String[] getFiltersFromModel() {
        String[] filters = searchModel.getFilters();
        return filters;
    };

    public void setActiveFilter(MutableLiveData<String> filter) {
        this.activeFilter = filter;
    }

    public void setSearchValue(MutableLiveData<String> searchValue) {
        this.searchValue = searchValue;
    }

    public MutableLiveData<String> getActiveFilter() {
        return activeFilter;
    }

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