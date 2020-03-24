package com.wasteless.ui.search;

import android.graphics.ColorSpace;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Arrays;


public class SearchModel {

    private ArrayList<Number> dummyData;
    private ArrayList<String> filters = new ArrayList<String>(
            Arrays.asList("category",
                                     "date",
                                     "name",
                                     "tag")
    );

//    private String activeFilter = "name";

    public void generateDummyData() {
        for (int i = 0;  i < 20; i++ ) {
            dummyData.add(i);
        }
        System.out.println(dummyData);
        Log.d("dummyData", "" + dummyData);
    }

    private MutableLiveData<String> searchValue;
    private MutableLiveData<String> activeFilter;

    public ArrayList<Number> getDummyData() {
        generateDummyData();
        return this.dummyData;
    }

    public MutableLiveData<String> getActiveFilter() {
        return activeFilter;
    }

    public ArrayList<String> getFilters() {
        return filters;
    }

    public void setActiveFilter(MutableLiveData<String> filter) {
        this.activeFilter = filter;
    }

    public void setSearchValue(MutableLiveData<String> searchValue) {
        this.searchValue = searchValue;
    }

}
